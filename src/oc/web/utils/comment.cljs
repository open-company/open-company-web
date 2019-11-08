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

(defun sort-comments
  ([comments :guard nil?]
   [])
  ([comments :guard map?]
   (sort-comments (vals comments)))
  ([comments :guard sequential?]
   (let [root-comments (filterv (comp empty? :parent-uuid) comments)
         sorted-roots (sort-comments root-comments nil)
         all-comments-seqs (mapv #(vec (concat [%] (sort-comments comments (:uuid %)))) sorted-roots)]
     (vec (apply concat all-comments-seqs))))
  ([comments :guard sequential? parent-uuid]
   (let [check-fn (if parent-uuid #(-> % :parent-uuid (= parent-uuid)) (comp empty? :parent-uuid))
         filtered-comments (filterv check-fn comments)
         sorted-comments (sort-by :created-at filtered-comments)]
     (if (nil? parent-uuid)
      (vec (reverse sorted-comments))
      (vec sorted-comments)))))
