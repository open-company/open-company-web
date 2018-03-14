(ns oc.web.components.ui.interactions-summary
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn get-max-count-reaction [reactions]
  (let [max-reaction (first (vec (reverse (sort-by :count (reverse reactions)))))]
    max-reaction))

(rum/defc reactions-summary < rum/static
  [entry-data]
  (let [max-reaction (get-max-count-reaction (:reactions entry-data))]
    (when-not (zero? (:count max-reaction))
      [:div.is-reactions.group
        {:class (when (:reacted max-reaction) "reacted")}
        [:div.is-reactions-reaction
          (:reaction max-reaction)]
        ; Reactions count
        [:div.is-reactions-summary
          {:class (utils/class-set {(str "reaction-" (:uuid entry-data) "-" (:reaction max-reaction)) true})}
          (if (:reacted max-reaction)
            (if (> (:count max-reaction) 1)
              (str "You and +" (dec (:count max-reaction)))
              (str "You reacted" (when-not (responsive/is-mobile-size?) " to this")))
            (str "+" (:count max-reaction)))]])))

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

(rum/defcs comments-summary < rum/static
                              rum/reactive
                              (drv/drv :comments-data)
  [s entry-data show-zero-comments?]
  (let [all-comments-data (drv/react s :comments-data)
        _comments-data (get all-comments-data (:uuid entry-data))
        comments-data (:sorted-comments _comments-data)
        comments-link (utils/link-for (:links entry-data) "comments")
        has-comments-data (and (sequential? comments-data) (pos? (count comments-data)))
        comments-authors (if has-comments-data
                           (vec
                            (map
                             first
                             (vals
                              (group-by :user-id (map :author (sort-by :created-at comments-data))))))
                           (reverse (:authors comments-link)))
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
              {:key (str "entry-comment-author-" (:uuid entry-data) "-" (:user-id user-data))}
              (user-avatar-image user-data (not (responsive/is-tablet-or-mobile?)))])]
        ; Comments count
        [:div.is-comments-summary
          {:class (str "comments-count-" (:uuid entry-data))}
          (if (responsive/is-tablet-or-mobile?)
            (comment-summary-string comments-authors)
            (str comments-count " comment" (when (> comments-count 1) "s")))]])))

(rum/defcs interactions-summary < rum/static
  [s entry-data show-zero-comments?]
  [:div.interactions-summary.group
    ;; Reactions
    (when (seq (:reactions entry-data))
      (reactions-summary entry-data))
    ; Comments
    (when (utils/link-for (:links entry-data) "comments")
      (comments-summary entry-data show-zero-comments?))])