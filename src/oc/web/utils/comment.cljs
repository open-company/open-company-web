(ns oc.web.utils.comment
  (:require [cljsjs.medium-editor]
            [defun.core :refer (defun)]
            [goog.object :as gobj]
            [cuerdas.core :as string]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.utils.mention :as mention-utils]))

(defn setup-medium-editor [comment-node users-list]
  (let [extentions (if (seq users-list)
                     #js {"mention" (mention-utils/mention-ext users-list)}
                     #js {})
        config {:toolbar false
                :anchorPreview false
                :imageDragging false
                :extensions extentions
                :autoLink true
                :anchor false
                :targetBlank true
                :paste #js {:forcePlainText false
                            :cleanPastedHTML true
                            :cleanAttrs #js ["style" "alt" "dir" "size" "face" "color" "itemprop" "name" "id"]
                            :cleanTags #js ["meta" "video" "audio" "img" "button" "svg" "canvas"
                                            "figure" "input" "textarea"]
                            :unwrapTags #js ["div" "label" "font" "h1" "h2" "h3" "h4" "h5" "div" "p" "ul" "ol" "li"
                                             "h6" "strong" "section" "time" "em" "main" "u" "form" "header" "footer"
                                             "details" "summary" "nav" "abbr" "a"]}
                :placeholder #js {:text "Add a reply…"
                                  :hideOnClick true}
               :keyboardCommands #js {:commands #js [
                                  #js {
                                    :command false
                                    :key "B"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }
                                  #js {
                                    :command false
                                    :key "I"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }
                                  #js {
                                    :command false
                                    :key "U"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }]}}]
    (new js/MediumEditor comment-node (clj->js config))))

(defn add-comment-content [comment-node & [print?]]
  (let [comment-html (.-innerHTML comment-node)
        $comment-node (.html (js/$ "<div/>") comment-html)
        _remove-mentions-popup (.remove $comment-node ".oc-mention-popup")]
    (.html $comment-node)))

(defn- is-own-comment?
  [comment-data]
  (= (jwt/user-id) (:user-id (:author comment-data))))

(defn get-comments-finished
  [comments-key activity-data {:keys [status success body]}]
  (when success
    (dis/dispatch! [:comments-get/finish {:success success
                                          :error (when-not success body)
                                          :comments-key comments-key
                                          :body (when (seq body) (json->cljs body))
                                          :activity-uuid (:uuid activity-data)
                                          :secure-activity-uuid (router/current-secure-activity-id)}])))

(defn get-comments [activity-data]
  (when activity-data
    (let [comments-key (dis/activity-comments-key (router/current-org-slug) (:uuid activity-data))
          comments-link (utils/link-for (:links activity-data) "comments")]
      (when comments-link
        (dis/dispatch! [:comments-get
                        comments-key
                        activity-data])
        (api/get-comments comments-link #(get-comments-finished comments-key activity-data %))))))

(defn get-comments-if-needed [activity-data all-comments-data]
  (let [comments-link (utils/link-for (:links activity-data) "comments")
        activity-uuid (:uuid activity-data)
        comments-data (get all-comments-data activity-uuid)
        should-load-comments? (and ;; there is a comments link
                                   (map? comments-link)
                                   ;; there are comments to load,
                                   (pos? (:count comments-link))
                                   ;; they are not already loading,
                                   (not (:loading comments-data))
                                   ;; and they are not loaded already
                                   (not (contains? comments-data :sorted-comments)))]
    ;; Load the whole list of comments if..
    (when should-load-comments?
      (get-comments activity-data))))

(defn ungroup-comments [comments]
  (vec (mapcat #(concat [(dissoc % :thread-children)] (:thread-children %)) comments)))

(defun sort-comments
  ([comments :guard nil?]
   [])
  ([comments :guard map?]
   (sort-comments (vals comments)))
  ;; If we have already grouped  the comments we need to ungroup them to recalculate the grouping
  ([comments :guard (fn [c] (and (sequential? c)
                                 (some #(contains? % :thread-children) c)))]
    (sort-comments (ungroup-comments comments)))
  ([comments :guard sequential?]
   (let [root-comments (filterv (comp empty? :parent-uuid) comments)
         grouped-comments (mapv #(let [sorted-children (sort-comments comments (:uuid %))]
                                  (merge % {:thread-children sorted-children
                                            :last-activity-at (if (seq sorted-children)
                                                                (-> sorted-children last :created-at)
                                                                (:created-at %))}))
                           root-comments)]
     (vec (reverse (sort-by :created-at grouped-comments)))))
  ([comments :guard sequential? parent-uuid]
   (let [check-fn (if parent-uuid #(-> % :parent-uuid (= parent-uuid)) (comp empty? :parent-uuid))
         filtered-comments (filterv check-fn comments)
         sorted-comments (sort-by :created-at filtered-comments)]
     (if (nil? parent-uuid)
      (vec (reverse sorted-comments))
      (vec sorted-comments)))))

(defn new? [user-id last-read-at comment-data]
  (if (contains? comment-data :new)
    (:new comment-data)
    (and (not= (-> comment-data :author :user-id) user-id)
         (< (.getTime (utils/js-date last-read-at))
            (.getTime (utils/js-date (:created-at comment-data)))))))

(defn- enrich-comment [user-id last-read-at comment-data last-comment? collapsed-map]
  (let [collapsed-comment-map (get collapsed-map (:uuid comment-data))
        new-comment? (new? user-id last-read-at (merge comment-data collapsed-comment-map))]
    {:new new-comment?
     :expanded (or (:new collapsed-comment-map)
                   ;; Keep the comment expanded if it was already
                   (:expanded collapsed-comment-map)
                   ;; Do not collapse root comments
                   (not (seq (:parent-uuid comment-data)))
                   ;; User has not read the post yet
                   (not (seq last-read-at))
                   ;; Do not collapse new comments
                   new-comment?
                   ;; Do not collapse last comments
                   last-comment?)}))

(defn collapsed-comments
  "Add a collapsed flag to every comment that is a reply and is not new. Also add new? flag to every new one.
   Add a count of the collapsed comments to each root comment."
  [user-id last-read-at comments & [collapsed-map root-comment]]
  (mapv
   (fn [comment]
    (let [enriched-children (collapsed-comments user-id last-read-at (:thread-children comment) collapsed-map comment)
          last-comment? (and root-comment
                             (= (:uuid comment) (-> root-comment :thread-children last :uuid)))]
      (-> comment
       (merge (enrich-comment user-id last-read-at comment last-comment? collapsed-map))
       (assoc :thread-children enriched-children)
       (assoc :new-count (count (filter :new enriched-children)))
       (assoc :collapsed-count (count (filter (comp not :expanded) enriched-children))))))
   comments))

(defn add-comment-focus-value
  ([prefix entry-uuid]
   (add-comment-focus-value prefix entry-uuid nil nil))

  ([prefix entry-uuid parent-comment-uuid]
   (add-comment-focus-value prefix entry-uuid parent-comment-uuid nil))

  ([prefix entry-uuid parent-comment-uuid edit-comment-uuid]
   (str prefix "-" (dis/add-comment-string-key entry-uuid parent-comment-uuid edit-comment-uuid))))