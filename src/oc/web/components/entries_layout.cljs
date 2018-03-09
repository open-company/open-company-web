(ns oc.web.components.entries-layout
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [taoensso.timbre :as timbre]
            [cljs-time.core :as time]
            [cljs-time.format :as f]
            [cuerdas.core :as s]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.activity-card :refer (activity-card)]))

(defn new?
  "
  An entry is new if:
    user is part of the team (we don't track new for non-team members accessing public boards)
      -and-
    user is not the post's author
      -and-
    published-at is < 30 days
      -and-
    published-at of the entry is newer than seen at
      -or-
    no seen at
  "
  [entry changes]
  (let [published-at (:published-at entry)
        too-old (f/unparse (f/formatters :date-time) (-> 30 time/days time/ago))
        seen-at (:seen-at changes)
        user-id (jwt/get-key :user-id)
        author-id (-> entry :author first :user-id)
        in-team? (jwt/user-is-part-of-the-team (:team-id (dis/org-data)))
        new? (and in-team?
                  (not= author-id user-id)
                  (> published-at too-old)
                  (or (> published-at seen-at)
                      (nil? seen-at)))]
    new?))

(defn load-more-items-next-fn [s scroll]
  (when (compare-and-set! (::loading-more s) false true)
    (dis/dispatch! [:all-posts-more @(::next-link s) :up])))

(defn load-more-items-prev-fn [s scroll]
  (when (compare-and-set! (::loading-more s) false true)
    (dis/dispatch! [:all-posts-more @(::prev-link s) :down])))

(rum/defcs entries-layout < rum/reactive
                          (drv/drv :change-data)
                          (drv/drv :board-data)
                          (rum/local nil ::board-uuid)
                          (rum/local false ::loading-more)
                          (rum/local nil ::prev-link)
                          (rum/local nil ::next-link)
                          ;; Mixins
                          (mixins/load-more-items 400)

                          {:init (fn [s]
                            (if (= (router/current-board-slug) "all-posts")
                              (-> s
                                (assoc :load-more-items-next-fn (atom nil))
                                (assoc :load-more-items-prev-fn (atom nil)))
                              s))
                           :before-render (fn [s]
                            (when (= (router/current-board-slug) "all-posts")
                              (let [board-data @(drv/get-ref s :board-data)
                                    next-link (utils/link-for (:links board-data) "previous")
                                    prev-link (utils/link-for (:links board-data) "next")]
                                (when (not= (:href @(::next-link s)) (:href next-link))
                                  (if next-link
                                    (do
                                     (reset! (::loading-more s) false)
                                     (reset! (::next-link s) next-link)
                                     (reset! (:load-more-items-next-fn s) (partial load-more-items-next-fn s)))
                                    (do
                                     (reset! (::next-link s) nil)
                                     (reset! (:load-more-items-next-fn s) nil))))

                                (when (not= (:href @(::prev-link s)) (:href prev-link))
                                  (if prev-link
                                    (do
                                     (reset! (::loading-more s) false)
                                     (reset! (::prev-link s) prev-link)
                                     (reset! (:load-more-items-prev-fn s) (partial load-more-items-prev-fn s)))
                                    (do
                                     (reset! (::prev-link s) nil)
                                     (reset! (:load-more-items-prev-fn s) nil))))))
                            s)
                           :after-render (fn [s]
                            (when (and (not (= (router/current-board-slug) "all-posts"))
                                       (not @(::board-uuid s)))
                              (let [board-data @(drv/get-ref s :board-data)]
                                (reset! (::board-uuid s) (:uuid board-data))))
                            s)
                           :will-unmount (fn [s]
                            (dis/dispatch! [:board-nav-away {:board-uuid @(::board-uuid s)}])
                            s)}
  [s]
  [:div.entries-layout
    (let [board-data (drv/react s :board-data)
          change-data (drv/react s :change-data)
          board-uuid (:uuid board-data)
          changes (get change-data board-uuid)
          is-mobile? (responsive/is-mobile-size?)
          entries (vals (:fixed-items board-data))
          sorted-entries (vec (reverse (sort-by :published-at entries)))]
      [:div.entry-cards-container.group
        ; Get the max number of pairs
        (let [top-index (js/Math.ceil (/ (count sorted-entries) 2))]
          ; For each pair
          (for [idx (range top-index)
                ; calc the entries that needs to render in this row
                :let [start (* idx 2)
                      end (min (+ start 2) (count sorted-entries))
                      entries (subvec sorted-entries start end)
                      has-headline (some #(seq (:headline %)) entries)
                      has-body (some #(seq (:body %)) entries)
                      has-attachments (some #(pos? (count (:attachments %))) entries)]]
            ; Renteder the entries in thisnrow
            [:div.entries-cards-container-row.group
              {:key (str "entries-row-" idx)}
              (for [entry entries
                    :let [is-new (new? entry changes)]]
                (rum/with-key (activity-card entry has-headline has-body is-new has-attachments)
                  (str "entry-latest-" (:uuid entry))))
              ; If the row contains less than 2, add a placeholder

              ; div to avoid having the first cover the full width
              (when (= (count entries) 1)
                [:div.entry-card.entry-card-placeholder])]))
        (when (and (pos? (count entries))
                   is-mobile?)
          (all-caught-up))])])