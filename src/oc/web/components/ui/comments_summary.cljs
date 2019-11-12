(ns oc.web.components.ui.comments-summary
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn get-author-name [author]
  (if (= (:user-id author) (jwt/user-id))
    "you"
    (if (seq (:first-name author))
      (:first-name author)
      (first (string/split (:name author) " ")))))

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
           show-new-tag?
           hide-label?]}]
  (let [entry-comments (get comments-data (:uuid entry-data))
        sorted-comments (:sorted-comments entry-comments)
        comments-link (utils/link-for (:links entry-data) "comments")
        has-comments-data (and (sequential? sorted-comments) (pos? (count sorted-comments)))
        comments-authors (if has-comments-data
                           (vec
                            (map
                             first
                             (vals
                              (group-by :avatar-url (map :author (sort-by :created-at sorted-comments))))))
                           (reverse (:authors comments-link)))
        comments-count (if sorted-comments
                         (count sorted-comments)
                         (:count comments-link))
        face-pile-count (min max-face-pile (count comments-authors))
        is-mobile? (responsive/is-mobile-size?)
        faces-to-render (take max-face-pile comments-authors)
        face-pile-width (if (pos? face-pile-count)
                          (if is-mobile?
                            (+ 8 (* 12 face-pile-count))
                            (+ 10 (* 12 face-pile-count)))
                            0)]
    (when comments-count
      [:div.is-comments
        {:on-click (fn [e]
                     (nav-actions/open-post-modal entry-data true)
                     (comment-actions/add-comment-focus (:uuid entry-data)))}
        ; Comments authors heads
        (when-not (and hide-label?
                       (zero? comments-count))
          [:div.is-comments-authors.group
            {:style {:width (str face-pile-width "px")}
             :class (when (> (count faces-to-render) 1) "show-border")}
            (for [user-data faces-to-render]
              [:div.is-comments-author
                {:key (str "entry-comment-author-" (:uuid entry-data) "-" (:user-id user-data))}
                (user-avatar-image user-data (not (responsive/is-tablet-or-mobile?)))])])
        (when-not (and hide-label?
                       (zero? comments-count))
          ; Comments count
          [:div.is-comments-summary
            {:class (utils/class-set {(str "comments-count-" (:uuid entry-data)) true
                                      :add-a-comment (not (pos? comments-count))})}
            (if (pos? comments-count)
              [:div.group
                (str comments-count
                 (when-not hide-label?
                  (str " comment" (when (not= comments-count 1) "s"))))
                (when show-new-tag?
                  [:div.new-comments-tag
                    "(NEW)"])]
              (when-not hide-label?
                [:span.add-a-comment
                  "Add a comment"]))])])))
