(ns oc.web.components.ui.interactions-summary
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn get-max-count-reaction [reactions]
  (let [max-reaction (first (vec (reverse (sort-by :count (reverse reactions)))))]
    max-reaction))

(rum/defc reactions-summary < rum/static
  [entry-data]
  (let [max-reaction (get-max-count-reaction (:reactions entry-data))]
    (when-not (zero? (:count max-reaction))
      [:div.is-reactions.group
        [:div.is-reactions-reaction
          (:reaction max-reaction)]
        ; Reactions count
        [:div.is-reactions-summary
          {:class (str "reaction-" (:uuid entry-data) "-" (:reaction max-reaction))}
          (:count max-reaction)]])))

(rum/defcs comments-summary < rum/static
                              rum/reactive
                              (drv/drv :comments-data)
  [s entry-data show-zero-comments?]
  (let [all-comments-data (drv/react s :comments-data)
        comments-data (get all-comments-data (:uuid entry-data))
        comments-link (utils/link-for (:links entry-data) "comments")
        has-comments-data (and (sequential? comments-data) (pos? (count comments-data)))
        comments-authors (if has-comments-data
                           (vec (map first (vals (group-by :user-id (map :author (sort-by :created-at comments-data))))))
                           (vec (sort-by :created-at (:authors comments-link))))
        comments-count (max (count comments-data) (:count comments-link))]
    (when (and comments-count
               (or show-zero-comments?
                   (not (zero? comments-count))))
      [:div.is-comments
        ; Comments authors heads
        [:div.is-comments-authors.group
          {:style {:width (str (if (pos? (count comments-authors)) (+ 9 (* 15 (count comments-authors))) 0) "px")}}
          (for [user-data (take 4 comments-authors)]
            [:div.is-comments-author
              {:key (str "entry-comment-author-" (:uuid entry-data) "-" (:created-at user-data))}
              (user-avatar-image user-data true)])]
        ; Comments count
        [:div.is-comments-summary
          {:class (str "comments-count-" (:uuid entry-data))}
          (str comments-count " comment" (when (or (zero? comments-count) (> comments-count 1)) "s"))]])))

(rum/defcs interactions-summary < rum/static
  [s entry-data show-zero-comments?]
  [:div.interactions-summary.group
    ;; Reactions
    (reactions-summary entry-data)
    ; Comments
    (comments-summary entry-data show-zero-comments?)])