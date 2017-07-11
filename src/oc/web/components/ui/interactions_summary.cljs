(ns oc.web.components.ui.interactions-summary
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn get-max-count-reaction [reactions]
  (let [max-reaction (first (sort-by :count reactions))]
    max-reaction))

(def comments-authors-data [
  {:avatar-url "" :first-name "Stuart" :last-name "Levinson" :user-id "1234-1234-1234"}
  {:avatar-url "" :first-name "Sean" :last-name "Johnson" :user-id "4321-4321-4321"}
  {:avatar-url "" :first-name "Iacopo" :last-name "Carraro" :user-id (jwt/get-key :user-id)}])

(rum/defcs interactions-summary < rum/static
  [s entry-data]
  (let [comments-link (or (utils/link-for (:links entry-data) "comments") {:count (int (rand 10))})
        comments-count (min (:count comments-link) 4)
        max-reaction (get-max-count-reaction (:reactions entry-data))]
    (js/console.log "max-reaction" max-reaction)
    [:div.interactions-summary
      ;; Reactions
      (when-not (zero? (:count max-reaction))
        [:div.is-reactions.group
          [:div.is-reactions-reaction
            (:reaction max-reaction)]
          ; Reactions count
          [:div.is-reactions-summary
            (:count max-reaction)]])
      ; Comments
      (when-not (zero? (:count comments-link))
        [:div.is-comments
          ; Comments authors heads
          [:div.is-comments-authors.group
            {:style {:width (str (+ 9 (* 15 comments-count)) "px")}}
            (for [idx (range comments-count)
                  :let [c (get (:comments entry-data) idx)
                        user-data (:author c)]]
              [:div.is-comments-author
                {:key (str "entry-comment-author-" (:uuid entry-data) "-" (:created-at c))}
                (user-avatar-image user-data)])]
          ; Comments count
          [:div.is-comments-summary
            (str (:count comments-link) " comments")]])]))