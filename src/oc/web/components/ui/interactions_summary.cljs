(ns oc.web.components.ui.interactions-summary
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
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

(rum/defc comments-summary < rum/static
  [entry-data show-zero-comments?]
  (let [comments-link (utils/link-for (:links entry-data) "comments")
        comments-authors (vec (sort-by :created-at (:authors comments-link)))
        comments-count (or (:count comments-link) 0)]
    (when (or show-zero-comments?
              (not (zero? comments-count)))
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