(ns oc.web.components.ui.comments-summary
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.face-pile :refer (face-pile)]))

(defn get-author-name [author]
  (if (:author? author)
    "you"
    (:short-name author)))

(defn comment-summary-string [authors]
  (case (count authors)
    0 ""
    1 (str (string/capital (get-author-name (first authors))) " commented")
    2 (str (string/capital (get-author-name (first authors))) " and " (get-author-name (second authors)) " commented")
    3 (str (string/capital (get-author-name (first authors))) ", "
           (get-author-name (second authors)) " and "
           (get-author-name (nth authors 2)) " commented")
    (str (string/capital (get-author-name (first authors))) ", "
         (get-author-name (second authors)) " and "
         (- (count authors) 2) " others commented")))

(rum/defc comments-summary < rum/static
  [{:keys [entry-data
           comments-data
           hide-label?
           hide-face-pile?
           new-comments-count
           publisher?
           add-comment-focus-prefix
           show-bubble-icon?
           current-activity-id]}]
  (let [entry-comments (get comments-data (:uuid entry-data))
        sorted-comments (:sorted-comments entry-comments)
        comments-link (utils/link-for (:links entry-data) "comments")
        comments-loaded? (seq sorted-comments)
        unwrapped-comments (vec (mapcat #(concat [%] (:thread-children %)) sorted-comments))
        show-new-tag? (pos? new-comments-count)
        comments (if show-new-tag?
                   (filter :unseen unwrapped-comments)
                   unwrapped-comments)
        comments-authors (if comments-loaded?
                           (->> comments
                                (sort-by :created-at)
                                (map :author)
                                (group-by :user-id)
                                vals
                                (map first)
                                vec)
                           (reverse (:authors comments-link)))
        comments-count (if comments-loaded?
                         (count unwrapped-comments)
                         (:count comments-link))
        is-mobile? (responsive/is-mobile-size?)
        show-new-tag? (pos? new-comments-count)]
    (when comments-count
      [:div.is-comments
        {:class (when show-new-tag? "has-new-comments")
         :on-click (fn [e]
                     ;; To avoid navigating to the post again and lose the coming from data
                     ;; nav only when not in the expanded post
                     (if (seq current-activity-id)
                       (when-let [add-comment-div (.querySelector js/document "div.add-comment")]
                         (.scrollIntoView add-comment-div #js {:behavior "smooth" :block "center"}))
                       (nav-actions/open-post-modal entry-data true))
                     (comment-actions/add-comment-focus (cu/add-comment-focus-value add-comment-focus-prefix (:uuid entry-data))))
         :data-placement "top"
         :data-toggle (when-not is-mobile? "tooltip")
         :data-container "body"
         :title (cond
                 (zero? comments-count)
                 "Add a comment"
                 (= new-comments-count comments-count)
                 (str new-comments-count " new comment" (when (not= new-comments-count 1) "s"))
                 :else
                 (str comments-count " comment" (when (not= comments-count 1) "s")
                  (when (pos? new-comments-count)
                    (str ", " new-comments-count " new"))))}
        ; Comments authors heads
        (when show-bubble-icon?
          [:div.is-comments-bubble])
        (when (and (not hide-face-pile?)
                  (or (not hide-label?)
                      (not (zero? comments-count))))
          [:div.is-comments-authors
           (face-pile {:width 16 :faces comments-authors})])
        (when (or show-bubble-icon?
                  (and (not hide-label?)
                       (pos? comments-count)))
          ; Comments count
          [:div.is-comments-summary
            {:class (utils/class-set {(str "comments-count-" (:uuid entry-data)) true
                                      :add-a-comment (not (pos? comments-count))})}
            [:div.is-comments-summary-inner
              (str
               (if show-new-tag?
                 (when-not hide-label?
                   (str new-comments-count " new comment" (when (not= new-comments-count 1) "s")))
                 (when-not hide-label?
                   (str comments-count " comment" (when (not= comments-count 1) "s")))))]])])))



(rum/defc foc-comments-summary < rum/static
  [{:keys [entry-data
           add-comment-focus-prefix
           current-activity-id
           new-comments-count]}]
  (let [sorted-comments (dis/activity-sorted-comments-data (:uuid entry-data))
        comments-link (utils/link-for (:links entry-data) "comments")
        comments-loaded? (seq sorted-comments)
        comments-count (max 0 (if sorted-comments
                                (count sorted-comments)
                                (:count comments-link)))
        is-mobile? (responsive/is-mobile-size?)]
    (when comments-count
      [:div.is-comments
        {:on-click (fn [e]
                     ;; To avoid navigating to the post again and lose the coming from data
                     ;; nav only when not in the expanded post
                     (if (seq current-activity-id)
                       (when-let [add-comment-div (.querySelector js/document "div.add-comment")]
                         (.scrollIntoView add-comment-div #js {:behavior "smooth" :block "center"}))
                       (nav-actions/open-post-modal entry-data true))
                     (comment-actions/add-comment-focus (cu/add-comment-focus-value add-comment-focus-prefix (:uuid entry-data))))
         :data-placement "top"
         :data-toggle (when-not is-mobile? "tooltip")
         :data-container "body"
         :title (str comments-count " comment" (when (not= comments-count 1) "s"))
         :class (when (pos? new-comments-count) "foc-new-comments")}
        ; Comments authors heads
        [:div.is-comments-bubble]
        ; Comments count
        [:div.is-comments-summary
          {:class (utils/class-set {(str "comments-count-" (:uuid entry-data)) true
                                    :add-a-comment (zero? comments-count)})}
          [:div.is-comments-summary-inner
            comments-count
            [:span.new-comments-count
              "(" new-comments-count " NEW)"]]]])))