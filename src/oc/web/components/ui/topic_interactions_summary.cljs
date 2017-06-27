(ns oc.web.components.ui.topic-interactions-summary
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(def comments-authors-data [
  {:avatar-url "" :first-name "Stuart" :last-name "Levinson" :user-id "1234-1234-1234"}
  {:avatar-url "" :first-name "Sean" :last-name "Johnson" :user-id "4321-4321-4321"}
  {:avatar-url "" :first-name "Iacopo" :last-name "Carraro" :user-id (jwt/get-key :user-id)}])

(rum/defcs topic-interactions-summary < rum/static
  [s entry-data]
  (let [comments-link (or (utils/link-for (:links entry-data) "comments") {:count (int (rand 10))})
        reactions (:reactions entry-data)
        reactions-count (if reactions (apply + (map :count reactions)) (int (rand 20)))
        comments-count (min (:count comments-link) 4)]
    [:div.topic-interactions-summary
      ;; Reactions
      [:div.tis-reactions
        [:div.tis-reactions-reactions
          (for [r (:reactions entry-data)]
            [:div.tis-reactions-reaction
              {:key (str "entry-reaction-" (:uuid entry-data) "-" (:reaction r))}
              (:reaction r)])]
        ; Reactions count
        [:div.tis-reactions-summary
          (str reactions-count " reactions")]]
      ; Comments
      [:div.tis-comments
        ; Comments authors heads
        [:div.tis-comments-authors
          {:style {:width (str (+ 9 (* 15 comments-count)) "px")}}
          (for [c (range comments-count)
                :let [user-data (get comments-authors-data (mod c 3))]]
            [:div.tis-comments-author
              {:key (str "entry-comment-author-" (:uuid entry-data) "-" (rand 7))}
              (user-avatar-image user-data)])]
        ; Comments count
        [:div.tis-comments-summary
          (str (:count comments-link) " comments")]]]))