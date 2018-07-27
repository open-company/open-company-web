(ns oc.web.components.entries-layout
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.section :as section-mixins]
            [oc.web.actions.section :as section-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.activity-card :refer (activity-card)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]))

(defn load-more-items-next-fn [s scroll]
  (when (compare-and-set! (::loading-more s) false true)
    (cond
      ;; A;; Posts
      (= (router/current-board-slug) "all-posts")
      (activity-actions/all-posts-more @(::next-link s) :up)
      ;; Must See
      (= (router/current-board-slug) "must-see")
      (activity-actions/must-see-more @(::next-link s) :up))))

(defn load-more-items-prev-fn [s scroll]
  (when (compare-and-set! (::loading-more s) false true)
    (cond
      ;; All Posts
      (= (router/current-board-slug) "all-posts")
      (activity-actions/all-posts-more @(::prev-link s) :down)
      ;; Must see
      (= (router/current-board-slug) "must-see")
      (activity-actions/must-see-more @(::prev-link s) :down))))

(def tiles-per-row 3)

(defn- item-scrolled-into-view-cb [_ item-uuid]
  ;; only in case of AP
  (activity-actions/ap-seen-events-gate item-uuid))

(rum/defcs entries-layout < rum/reactive
                          (drv/drv :filtered-posts)
                          (drv/drv :container-data)
                          (drv/drv :activities-read)
                          (rum/local false ::loading-more)
                          (rum/local nil ::prev-link)
                          (rum/local nil ::next-link)
                          ;; Mixins
                          (mixins/load-more-items 400)
                          (mixins/ap-seen-mixin "div.ap-seen-item-headline" item-scrolled-into-view-cb)
                          section-mixins/container-nav-in

                          {:init (fn [s]
                            (-> s
                             (assoc :load-more-items-next-fn (atom nil))
                             (assoc :load-more-items-prev-fn (atom nil))))
                           :before-render (fn [s]
                            (when (or (= (router/current-board-slug) "all-posts")
                                      (= (router/current-board-slug) "must-see"))
                              (let [container-data @(drv/get-ref s :container-data)
                                    next-link (utils/link-for (:links container-data) "previous")
                                    prev-link (utils/link-for (:links container-data) "next")]
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
                            s)}
  [s]
  [:div.entries-layout
    (let [posts-data (drv/react s :filtered-posts)
          is-mobile? (responsive/is-mobile-size?)
          entries (vals posts-data)
          sorted-entries (vec (reverse (sort-by :published-at entries)))
          activities-read (drv/react s :activities-read)]
      [:div.entry-cards-container.group
        ; Get the max number of pairs
        (let [top-index (js/Math.ceil (/ (count sorted-entries) tiles-per-row))]
          ; For each pair
          (for [idx (range top-index)
                ; calc the entries that needs to render in this row
                :let [start (* idx tiles-per-row)
                      end (min (+ start tiles-per-row) (count sorted-entries))
                      entries (subvec sorted-entries start end)
                      has-headline (some #(seq (:headline %)) entries)
                      has-body (some #(seq (:body %)) entries)
                      has-attachments (some #(pos? (count (:attachments %))) entries)]]
            ; Renteder the entries in thisnrow
            [:div.entries-cards-container-row.group
              {:key (str "entries-row-" idx)}
              (for [entry entries
                    :let [reads-data (get activities-read (:uuid entry))]]
                (rum/with-key (activity-card entry reads-data has-headline has-body (:new entry) has-attachments)
                  (str "entry-latest-" (:uuid entry) "-" (:updated-at entry))))
              ; If the row contains less than 2, add a placeholder

              ; div to avoid having the first cover the full width
              (when (< (count entries) 2)
                [:div.entry-card.entry-card-placeholder])
              (when (< (count entries) 3)
                [:div.entry-card.entry-card-placeholder])]))
        (when (and (pos? (count entries))
                   is-mobile?)
          (all-caught-up))])])