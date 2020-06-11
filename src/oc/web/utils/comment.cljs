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

(defn threads-map [sorted-comments]
  (apply merge
   (map #(hash-map (:uuid %) %) sorted-comments)))


(defn unread?
  "If unread was already set let's reuse it."
  [last-read-at comment-data]
  (if (contains? comment-data :unread)
    (:unread comment-data)
    (and (not (:author? comment-data))
         (< (.getTime (utils/js-date last-read-at))
            (.getTime (utils/js-date (:created-at comment-data)))))))

(defn- enrich-comment [comment-data last-read-at last-comment? collapsed-map]
  (let [collapsed-comment-map (get collapsed-map (:uuid comment-data))
        unread-comment? (unread? last-read-at (merge comment-data collapsed-comment-map))]
    (merge comment-data
     {:unread unread-comment?
      :expanded (or (:unread collapsed-comment-map)
                    ;; Keep the comment expanded if it was already
                    (:expanded collapsed-comment-map)
                    ;; Do not collapse root comments
                    (not (seq (:parent-uuid comment-data)))
                    ;; User has not read the post yet
                    (not (seq last-read-at))
                    ;; Do not collapse unread comments
                    unread-comment?
                    ;; Do not collapse last comments
                    last-comment?)})))

(defn collapsed-comments
  "Add a collapsed flag to every comment that is a reply and is not unread. Also add unread? flag to every unread one.
   Add a count of the collapsed comments to each root comment."
  [last-read-at comments & [collapsed-map root-comment]]
  (mapv
   (fn [comment]
    (if (#{:thread :comment} (:resource-type comment))
      (let [enriched-children (collapsed-comments last-read-at (:thread-children comment) collapsed-map comment)
            last-comment? (and root-comment
                               (= (:uuid comment) (-> root-comment :thread-children last :uuid)))]
        (-> comment
         (enrich-comment last-read-at last-comment? collapsed-map)
         (assoc :thread-children enriched-children)
         (assoc :unread-count (count (filter :unread enriched-children)))
         (assoc :collapsed-count (count (filter (comp not :expanded) enriched-children)))))
      comment))
   comments))

(defun add-comment-focus-value
  ([prefix :guard string? comment-data :guard map?]
   (add-comment-focus-value prefix (:resource-uuid comment-data) (:parent-uuid comment-data) (:uuid comment-data)))

  ([prefix :guard string? entry-uuid :guard string?]
   (add-comment-focus-value prefix entry-uuid nil nil))

  ([prefix :guard string? entry-uuid parent-comment-uuid]
   (add-comment-focus-value prefix entry-uuid parent-comment-uuid nil))

  ([prefix :guard string? entry-uuid :guard string? parent-comment-uuid edit-comment-uuid]
   (str prefix "-" (dis/add-comment-string-key entry-uuid parent-comment-uuid edit-comment-uuid))))