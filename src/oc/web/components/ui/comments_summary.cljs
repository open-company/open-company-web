(ns oc.web.components.ui.comments-summary
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

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

(def max-face-pile 3)

(rum/defc comments-summary < rum/static
  [{:keys [entry-data
           comments-data
           hide-label?
           hide-face-pile?
           new-comments-count
           publisher?
           add-comment-focus-prefix
           show-bubble-icon?]}]
  (let [entry-comments (get comments-data (:uuid entry-data))
        sorted-comments (:sorted-comments entry-comments)
        comments-link (utils/link-for (:links entry-data) "comments")
        comments-loaded? (seq sorted-comments)
        unwrapped-comments (vec (mapcat #(concat [%] (:thread-children %)) sorted-comments))
        comments-authors (if comments-loaded?
                           (vec (map first (vals (group-by :user-id (map :author (sort-by :created-at unwrapped-comments))))))
                           (reverse (:authors comments-link)))
        comments-count (if comments-loaded?
                         (count unwrapped-comments)
                         (:count comments-link))
        face-pile-count (if hide-face-pile?
                          0
                          (min max-face-pile (count comments-authors)))
        is-mobile? (responsive/is-mobile-size?)
        faces-to-render (take max-face-pile comments-authors)
        face-pile-width (if (pos? face-pile-count)
                          (if is-mobile?
                            (+ 8 (* 12 face-pile-count))
                            (+ 10 (* 12 face-pile-count)))
                            0)
        show-new-tag? (pos? new-comments-count)]
    (when comments-count
      [:div.is-comments
        {:class (when show-new-tag? "has-new-comments")
         :on-click (fn [e]
                     ;; To avoid navigating to the post again and lose the coming from data
                     ;; nav only when not in the expanded post
                     (if (seq (router/current-activity-id))
                       (when-let [add-comment-div (.querySelector js/document "div.add-comment")]
                         (.scrollIntoView add-comment-div #js {:behavior "smooth" :block "center"}))
                       (nav-actions/open-post-modal entry-data true))
                     (comment-actions/add-comment-focus (cu/add-comment-focus-value add-comment-focus-prefix (:uuid entry-data))))
         :data-placement "top"
         :data-toggle (when-not is-mobile? "tooltip")
         :data-container "body"
         :title (if (and (pos? new-comments-count)
                         (= new-comments-count comments-count))
                 (str new-comments-count " new comment" (when (not= new-comments-count 1) "s"))
                 (str comments-count " comment" (when (not= comments-count 1) "s")
                  (when (pos? new-comments-count)
                    (str ", " new-comments-count " new"))))}
        ; Comments authors heads
        (when show-bubble-icon?
          [:div.is-comments-bubble])
        (when (and (not hide-face-pile?)
                  (or (not hide-label?)
                      (not (zero? comments-count))))
          [:div.is-comments-authors.group
            {:style {:width (str face-pile-width "px")}
             :class (when (> (count faces-to-render) 1) "show-border")}
            (for [user-data faces-to-render]
              [:div.is-comments-author
                {:key (str "entry-comment-author-" (:uuid entry-data) "-" (:user-id user-data))}
                (user-avatar-image user-data {:tooltip? (not (responsive/is-tablet-or-mobile?))})])])
        (when (or show-bubble-icon?
                  (and (not hide-label?)
                       (pos? comments-count)))
          ; Comments count
          [:div.is-comments-summary
            {:class (utils/class-set {(str "comments-count-" (:uuid entry-data)) true
                                      :add-a-comment (not (pos? comments-count))})
             :data-toggle (when-not is-mobile? "tooltip")}
            [:div.is-comments-summary-inner.group
              (str
               comments-count
               (if show-new-tag?
                 (when-not hide-label?
                   (str " new comment" (when (not= new-comments-count 1) "s")))
                 (when-not hide-label?
                   (str " comment" (when (not= comments-count 1) "s")))))]])])))