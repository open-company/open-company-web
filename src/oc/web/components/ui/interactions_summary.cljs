(ns oc.web.components.ui.interactions-summary
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn get-max-count-reaction [reactions]
  (if-let [max-reaction (first (sort-by :count reactions))]
    (:reaction max-reaction)
    "👏"))

(def comments-authors-data [
  {:avatar-url "" :first-name "Stuart" :last-name "Levinson" :user-id "1234-1234-1234"}
  {:avatar-url "" :first-name "Sean" :last-name "Johnson" :user-id "4321-4321-4321"}
  {:avatar-url "" :first-name "Iacopo" :last-name "Carraro" :user-id (jwt/get-key :user-id)}])

(rum/defcs interactions-summary < rum/static
  [s entry-data]
  (let [comments-link (or (utils/link-for (:links entry-data) "comments") {:count (int (rand 10))})
        reactions (:reactions entry-data)
        reactions-count (if reactions (apply + (map :count reactions)) (int (rand 20)))
        comments-count (min (:count comments-link) 4)]
    [:div.interactions-summary
      ;; Reactions
      [:div.is-reactions.group
        [:div.is-reactions-reaction
          (get-max-count-reaction (:reactions entry-data))]
        ; Reactions count
        [:div.is-reactions-summary
          (str reactions-count)]]
      ; Comments
      [:div.is-comments
        ; Comments authors heads
        [:div.is-comments-authors.group
          {:style {:width (str (+ 9 (* 15 comments-count)) "px")}}
          (for [c (range comments-count)
                :let [user-data (get comments-authors-data (mod c 3))]]
            [:div.is-comments-author
              {:key (str "entry-comment-author-" (:uuid entry-data) "-" (rand 7))}
              (user-avatar-image user-data)])]
        ; Comments count
        [:div.is-comments-summary
          (str (:count comments-link) " comments")]]]))