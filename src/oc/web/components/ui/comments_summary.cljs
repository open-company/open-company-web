(ns oc.web.components.ui.comments-summary
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
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
                              (group-by :avatar-url (map :author (sort-by :created-at comments-data))))))
                           (reverse (:authors comments-link)))
        comments-count (max (count comments-data) (:count comments-link))
        face-pile-count (min max-face-pile (count comments-authors))]
    (when (and comments-count
               (or show-zero-comments?
                   (not (zero? comments-count))))
      [:div.is-comments
        ; Comments authors heads
        [:div.is-comments-authors.group
          {:style {:width (str (if (pos? face-pile-count) (+ 10 (* 18 face-pile-count)) 0) "px")}}
          (for [user-data (take max-face-pile comments-authors)]
            [:div.is-comments-author
              {:key (str "entry-comment-author-" (:uuid entry-data) "-" (:user-id user-data))}
              (user-avatar-image user-data (not (responsive/is-tablet-or-mobile?)))])]
        ; Comments count
        [:div.is-comments-summary
          {:class (utils/class-set {(str "comments-count-" (:uuid entry-data)) true
                                    :add-a-comment (not (pos? comments-count))
                                    utils/hide-class true})}
          (if (responsive/is-tablet-or-mobile?)
            (comment-summary-string comments-authors)
            (if (pos? comments-count)
              (str comments-count " comment" (when (not= comments-count 1) "s"))
              [:span.add-a-comment "Add a comment"]))]])))