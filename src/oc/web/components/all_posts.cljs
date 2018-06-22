(ns oc.web.components.all-posts
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.stream-item :refer (stream-item)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

;; 800px from the end of the current rendered results as point to add more items in the batch
(def scroll-card-threshold 5)
(def card-avg-height 372)

(def last-scroll (atom 0))

(defn- check-entry
  "Given an entry and a date string in the form YYYY-MM-DD
   check if the entry was created on the date or before."
  [entry date-str]
  (let [js-date (utils/js-date date-str)
        entry-date (utils/js-date (activity-utils/get-activity-date entry))]
    (>= (.getTime entry-date) (.getTime js-date))))

(defn days-for-month [y m]
  (case m
    1 31
    2 (if (zero? (mod y 4)) 29 28)
    3 31
    4 30
    5 31
    6 30
    7 31
    8 31
    9 30
    10 31
    11 30
    12 31))

(defn get-first-available-entry
  "Given a list of items and a year and a month get the first
   available entry from the first of that month."
  [items year month]
  (let [date-str (str year "-" (utils/add-zero month) "-" (days-for-month year month) "T23:59:59.999Z")]
    (loop [ens (vec (rest items))
           en (first items)]
      (if (and (pos? (count ens))
               (check-entry en date-str))
        (recur (vec (rest ens))
               (first ens))
        en))))

(defn did-scroll
  "Scroll listener, load more activities when the scroll is close to a margin."
  [s e]
  (let [scroll-top (.-scrollTop (.-scrollingElement js/document))
        direction (if (> @last-scroll scroll-top)
                    :up
                    (if (< @last-scroll scroll-top)
                      :down
                      :stale))
        min-scroll 0
        max-scroll (- (.-scrollHeight (.-body js/document)) (.-innerHeight js/window))]
    ;; scrolling up
    (when (and @(::has-next s)
               (not @(::scroll-to-entry s))
               (= direction :up)
               (= scroll-top min-scroll))
      ;; Show a spinner at the top
      (reset! (::top-loading s) true)
      ;; if the user is close to the top margin, load more results if there is a link
      (activity-actions/all-posts-more @(::has-next s) :up)
      (reset! (::has-next s) false))
    ;; scrolling down
    (when (and @(::has-prev s)
               (= direction :down)
               (>= scroll-top (- max-scroll (* scroll-card-threshold card-avg-height))))
      ;; Show a spinner at the bottom
      (reset! (::bottom-loading s) true)
      ;; if the user is close to the bottom margin, load more results if there is a link
      (activity-actions/all-posts-more @(::has-prev s) :down)
      (reset! (::has-prev s) false)))
  ;; Save the last scrollTop value
  (reset! last-scroll (.-scrollTop (.-body js/document))))

(defn- item-scrolled-into-view-cb [_ item-uuid]
  (activity-actions/ap-seen-events-gate item-uuid))

(rum/defcs all-posts  < rum/reactive
                        ;; Derivatives
                        (drv/drv :all-posts)
                        (drv/drv :ap-initial-at)
                        ;; Locals
                        (rum/local nil ::scroll-listener)
                        (rum/local false ::has-next)
                        (rum/local false ::has-prev)
                        (rum/local nil ::selected-year)
                        (rum/local nil ::selected-month)
                        (rum/local nil ::scroll-to-entry)
                        (rum/local nil ::top-loading)
                        (rum/local nil ::bottom-loading)
                        (rum/local nil ::retrieving-calendar)
                        (rum/local false ::show-all-caught-up-message)
                        (rum/local nil ::last-direction)
                        ;; Mixins
                        mixins/first-render-mixin
                        (mixins/ap-seen-mixin "div.ap-seen-item-headline" item-scrolled-into-view-cb)
                        {:will-mount (fn [s]
                          (let [all-posts-data @(drv/get-ref s :all-posts)
                                sorted-items (activity-utils/get-sorted-activities all-posts-data)
                                year (:year all-posts-data)
                                month (:month all-posts-data)
                                direction (:direction all-posts-data)
                                next-link (utils/link-for (:links all-posts-data) "previous")
                                prev-link (utils/link-for (:links all-posts-data) "next")
                                first-entry-date (utils/js-date (activity-utils/get-activity-date (first sorted-items)))
                                ap-initial-at @(drv/get-ref s :ap-initial-at)
                                first-available-entry (when (and year month)
                                                        (get-first-available-entry
                                                         (activity-utils/get-sorted-activities all-posts-data)
                                                         year
                                                         month))]
                            (if (and year month)
                              ;; Loading from calendar since we have year and month from the click action
                              (do
                                (reset! (::selected-year s) year)
                                (reset! (::selected-month s) month)
                                (reset! (::scroll-to-entry s) first-available-entry))
                              ;; First load or subsequent load more with different set of items
                              (if (= direction :up)
                                ;; did scrolled up, we need to scroll to the first of the old items
                                ;; to not lose the previous position
                                (let [saved-items (:saved-items all-posts-data)
                                      last-new-entry-idx (dec (- (count sorted-items) saved-items))
                                      scroll-to-entry (get sorted-items last-new-entry-idx)
                                      created-date (utils/js-date (activity-utils/get-activity-date scroll-to-entry))
                                      to-year (.getFullYear created-date)
                                      to-month (inc (int (.getMonth created-date)))]
                                  (reset! (::selected-year s) to-year)
                                  (reset! (::selected-month s) to-month)
                                  (reset! (::last-direction s) :up)
                                  (reset! (::scroll-to-entry s) scroll-to-entry))
                                ;; did scrolled down, results where simply concatenated, just need
                                ;; to update the calendar highlighting
                                (if (= direction :down)
                                  ; Load more :down scroll, needs to set the calendar
                                  (let [last-old-entry-idx (dec (:saved-items all-posts-data))
                                        last-old-entry (get sorted-items last-old-entry-idx)
                                        created-date (utils/js-date (activity-utils/get-activity-date last-old-entry))
                                        to-year (.getFullYear created-date)
                                        to-month (inc (int (.getMonth created-date)))]
                                    (reset! (::selected-year s) to-year)
                                    (reset! (::selected-month s) to-month))
                                  ; First load or calendar get
                                  (do
                                    (reset! (::selected-year s) (.getFullYear first-entry-date))
                                    (reset! (::selected-month s) (inc (int (.getMonth first-entry-date))))
                                    (when ap-initial-at
                                      (reset!
                                       (::scroll-to-entry s)
                                       (first (filter #(= (:published-at %) ap-initial-at) sorted-items))))))))
                            (reset! (::retrieving-calendar s) nil)
                            (reset! (::top-loading s) false)
                            (reset! (::bottom-loading s) false)
                            (when next-link
                              (reset! (::has-next s) next-link))
                            (if prev-link
                              (do
                                (reset! (::has-prev s) prev-link)
                                (reset! (::show-all-caught-up-message s) false))
                              (reset! (::show-all-caught-up-message s) (> (count sorted-items) 10))))
                          s)
                         :did-mount (fn [s]
                          (reset! last-scroll (.-scrollTop (.-body js/document)))
                          (reset! (::scroll-listener s)
                           (events/listen js/window EventType/SCROLL #(did-scroll s %)))
                          s)
                         :after-render (fn [s]
                          (when-let [scroll-to @(::scroll-to-entry s)]
                            (when-let [entry-el (sel1 [(str "div.stream-item-" (:uuid scroll-to))])]
                              (utils/scroll-to-element entry-el 0 0))
                            (utils/after 100 #(do
                                               (reset! (::scroll-to-entry s) nil)
                                               (reset! (::last-direction s) nil))))
                          s)
                         :before-render (fn [s]
                          (let [all-posts-data @(drv/get-ref s :all-posts)
                                sorted-items (activity-utils/get-sorted-activities all-posts-data)]
                            (when-not (:loading-more all-posts-data)
                              (when @(::top-loading s)
                                (reset! (::top-loading s) false)
                                (reset! (::has-next s) nil))
                              (when @(::bottom-loading s)
                                (reset! (::bottom-loading s) false)
                                (reset! (::has-prev s) nil)
                                (reset! (::show-all-caught-up-message s) true)))
                            (when @(::retrieving-calendar s)
                              (reset! (::retrieving-calendar s) false)
                              ;; Scroll to the first entry of the selected month if any
                              (let [year @(::selected-year s)
                                    month @(::selected-month s)
                                    first-available-entry (get-first-available-entry
                                                           sorted-items
                                                           @(::selected-year s)
                                                           month)
                                    next-link (utils/link-for (:links all-posts-data) "previous")
                                    prev-link (utils/link-for (:links all-posts-data) "next")]
                                (reset! (::has-next s) next-link)
                                (if prev-link
                                  (do
                                    (reset! (::has-prev s) prev-link)
                                    (reset! (::show-all-caught-up-message s) false))
                                  (do
                                    (reset! (::has-prev s) nil)
                                    (reset! (::show-all-caught-up-message s) (> (count sorted-items) 10))))
                                (when first-available-entry
                                  (reset! (::scroll-to-entry s) first-available-entry)))))
                          s)
                         :will-unmount (fn [s]
                          (when @(::scroll-listener s)
                            (events/unlistenByKey @(::scroll-listener s)))
                          s)}
  [s]
  (let [all-posts-data (drv/react s :all-posts)
        items (activity-utils/get-sorted-activities all-posts-data)]
    [:div.all-posts.group
      [:div.all-posts-cards
        (when @(::top-loading s)
          [:div.loading-updates.top-loading
            "Retrieving activity..."])
        [:div.all-posts-cards-inner.group
          (when (or @(::top-loading s)
                    @(::retrieving-calendar s)
                    (and (:loading-more all-posts-data)
                         (not @(:first-render-done s)))
                    @(::scroll-to-entry s)
                    (= @(::last-direction s) :up))
            [:div.activities-overlay
              (loading {:loading true})
              (when (or @(::top-loading s)
                        (= @(::last-direction s) :up))
                [:div.top-loading-message "Retrieving earlier activity..."])])
          (for [e items]
            (rum/with-key
             (stream-item e)
             (str "all-posts-entry-" (:uuid e) "-" (:updated-at e))))]
        (when @(::bottom-loading s)
          [:div.loading-updates.bottom-loading
            "Retrieving activity..."])
        (when (and @(::show-all-caught-up-message s)
                   (responsive/is-mobile-size?))
          (all-caught-up))]]))