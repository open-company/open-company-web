(ns oc.web.components.all-activity
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.logging :as logging]
            [oc.web.components.activity-card :refer (activity-card)]
            [oc.web.components.ui.loading :refer (rloading)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

(defn get-activity-date [activity]
  (if (= (:type activity) "entry")
    (:created-at activity)
    (:published-at activity)))

;; 800px from the end of the current rendered results as point to add more items in the batch
(def scroll-card-threshold 5)
(def card-avg-height 600)

(def last-scroll (atom 0))

(defn- check-entry
  "Given an entry and a date string in the form YYYY-MM-DD
   check if the entry was created on the date or before."
  [entry date-str]
  (let [js-date (utils/js-date date-str)
        entry-date (utils/js-date (get-activity-date entry))]
    (>= (.getTime entry-date) (.getTime js-date))))

(defn days-for-month [y m]
  (case m
    1 31
    2 (if (= (mod y 4) 0) 29 28)
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
  "Given a list of items and a year and a month get the first available entry from the first of that month."
  [items year month]
  (let [date-str (str year "-" (utils/add-zero month) "-" (days-for-month year month) "T23:59:59.999Z")]
    (loop [ens (vec (rest items))
           en (first items)]
      (if (and (pos? (count ens))
               (check-entry en date-str))
        (recur (vec (rest ens))
               (first ens))
        en))))

(defn element-is-visible
  "Check if the element is in the visible portion of the page, considered the page scroll."
  [el]
  (let [el-offset-top (.-top (.offset (js/$ el))) ; plus parent top offset
        body-scroll (.-scrollTop (.-body js/document))]
    (>= (- el-offset-top body-scroll) 0)))

(defn get-first-visible-entry
  "Given the list of items rendered, get the first visible entry counting the page scroll."
  [items]
  (when (pos? (count items))
    (loop [ens items
           en (first items)]
      (let [el (sel1 [(str "div.activity-card-" (:uuid en))])]
        ;; Do not loop if there are no more items or if found the first visible entry
        (if (or (zero? (count ens))
                (and el
                     (element-is-visible el)))
          en
          (recur (vec (rest ens))
                 (first (vec (rest ens)))))))))

(defn compare-activities [act-1 act-2]
  (let [time-1 (get-activity-date act-1)
        time-2 (get-activity-date act-2)]
    (compare time-2 time-1)))

(defn get-sorted-items [all-activity-data]
  (vec (sort compare-activities (vals (:fixed-items all-activity-data)))))

(defn highlight-calendar
  "Highlight the current visible entry year and month in the calendar."
  [s]
  ;; When we are not retrieving calendar and not waiting to scroll to an entry
  (when (and (not @(::retrieving-calendar s))
             (not @(::scroll-to-entry s)))
    (let [items-batch (get-sorted-items (first (:rum/args s)))
          first-visible-entry (get-first-visible-entry items-batch)
          js-date (utils/js-date (get-activity-date first-visible-entry))]
      (reset! (::selected-year s) (.getFullYear js-date))
      (reset! (::selected-month s) (inc (.getMonth js-date))))))

(defn did-scroll
  "Scroll listener, load more activities when the scroll is close to a margin."
  [s e]
  (let [scroll-top (.-scrollTop (.-body js/document))
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
      (dis/dispatch! [:all-activity-more @(::has-next s) :up])
      (reset! (::has-next s) false))
    ;; scrolling down
    (when (and @(::has-prev s)
               (= direction :down)
               (>= scroll-top (- max-scroll (* scroll-card-threshold card-avg-height))))
      ;; Show a spinner at the bottom
      (reset! (::bottom-loading s) true)
      ;; if the user is close to the bottom margin, load more results if there is a link
      (dis/dispatch! [:all-activity-more @(::has-prev s) :down])
      (reset! (::has-prev s) false)))
  ;; Highlight the right year/month
  (when @(::first-render-done s)
    (highlight-calendar s))
  ;; Save the last scrollTop value
  (reset! last-scroll (.-scrollTop (.-body js/document))))

(rum/defcs all-activity < rum/static
                          rum/reactive
                          (drv/drv :all-activity)
                          (drv/drv :calendar)
                          (rum/local false ::first-render-done)
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
                          {:will-mount (fn [s]
                                        (let [all-activity-data (first (:rum/args s))
                                              sorted-items (get-sorted-items all-activity-data)
                                              year (:year all-activity-data)
                                              month (:month all-activity-data)
                                              direction (:direction all-activity-data)
                                              next-link (utils/link-for (:links all-activity-data) "previous")
                                              prev-link (utils/link-for (:links all-activity-data) "next")
                                              first-entry-date (utils/js-date (get-activity-date (first sorted-items)))
                                              first-available-entry (when (and year month) (get-first-available-entry (get-sorted-items all-activity-data) year month))]
                                          (if (and year month)
                                            ;; Loading from calendar since we have year and month from the click action
                                            (do
                                              (reset! (::selected-year s) year)
                                              (reset! (::selected-month s) month)
                                              (reset! (::scroll-to-entry s) first-available-entry))
                                            ;; First load or subsequent load more with different set of items
                                            (if (= direction :up)
                                              ;; did scrolled up, we need to scroll to the first of the old items to not lose the previous position
                                              (let [saved-items (:saved-items all-activity-data)
                                                    last-new-entry-idx (dec (- (count sorted-items) saved-items))
                                                    scroll-to-entry (get sorted-items last-new-entry-idx)
                                                    created-date (utils/js-date (get-activity-date scroll-to-entry))
                                                    to-year (.getFullYear created-date)
                                                    to-month (inc (int (.getMonth created-date)))]
                                                (reset! (::selected-year s) to-year)
                                                (reset! (::selected-month s) to-month)
                                                (reset! (::last-direction s) :up)
                                                (reset! (::scroll-to-entry s) scroll-to-entry))
                                              ;; did scrolled down, results where simply concatenated, just need to update the calendar highlighting
                                              (if (= direction :down)
                                                ; Load more :down scroll, needs to set the calendar
                                                (let [last-old-entry-idx (dec (:saved-items all-activity-data))
                                                      last-old-entry (get sorted-items last-old-entry-idx)
                                                      created-date (utils/js-date (get-activity-date last-old-entry))
                                                      to-year (.getFullYear created-date)
                                                      to-month (inc (int (.getMonth created-date)))]
                                                  (reset! (::selected-year s) to-year)
                                                  (reset! (::selected-month s) to-month))
                                                ; First load or calendar get
                                                (do
                                                  (reset! (::selected-year s) (.getFullYear first-entry-date))
                                                  (reset! (::selected-month s) (inc (int (.getMonth first-entry-date))))))))
                                          (reset! (::retrieving-calendar s) nil)
                                          (reset! (::top-loading s) false)
                                          (reset! (::bottom-loading s) false)
                                          (when next-link
                                            (reset! (::has-next s) next-link))
                                          (if prev-link
                                            (do
                                              (reset! (::has-prev s) prev-link)
                                              (reset! (::show-all-caught-up-message s) false))
                                            (reset! (::show-all-caught-up-message s) true)))
                                        s)
                           :did-mount (fn [s]
                                        (reset! last-scroll (.-scrollTop (.-body js/document)))
                                        (reset! (::scroll-listener s)
                                         (events/listen js/window EventType/SCROLL #(did-scroll s %)))
                                        s)
                           :after-render (fn [s]
                                           (when-not @(::first-render-done s)
                                              (reset! (::first-render-done s) true))
                                           (when-let [scroll-to @(::scroll-to-entry s)]
                                             (when-let [entry-el (sel1 [(str "div.activity-card-" (:uuid scroll-to))])]
                                               (utils/scroll-to-element entry-el 100 0))
                                             (utils/after 100 #(do (reset! (::scroll-to-entry s) nil)
                                                                   (reset! (::last-direction s) nil))))
                                           s)
                           :did-remount (fn [_ s]
                                          (let [all-activity-data (first (:rum/args s))
                                                sorted-items (get-sorted-items all-activity-data)]
                                            (when-not (:loading-more all-activity-data)
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
                                              (let [calendar-data @(drv/get-ref s :calendar)
                                                    year @(::selected-year s)
                                                    month (or @(::selected-month s) (:month (first (filter #(= (:year %) year) calendar-data))))
                                                    first-available-entry (get-first-available-entry sorted-items @(::selected-year s) month)
                                                    next-link (utils/link-for (:links all-activity-data) "previous")
                                                    prev-link (utils/link-for (:links all-activity-data) "next")]
                                                (reset! (::has-next s) next-link)
                                                (if prev-link
                                                  (do
                                                    (reset! (::has-prev s) prev-link)
                                                    (reset! (::show-all-caught-up-message s) false))
                                                  (do
                                                    (reset! (::has-prev s) nil)
                                                    (reset! (::show-all-caught-up-message s) true)))
                                                (when first-available-entry
                                                  (reset! (::scroll-to-entry s) first-available-entry)))))
                                          s)
                           :will-unmount (fn [s]
                                          (when @(::scroll-listener s)
                                            (events/unlistenByKey @(::scroll-listener s)))
                                           s)}
  [s all-activity-data]
  (let [calendar-data (drv/react s :calendar)
        items (get-sorted-items all-activity-data)]
    [:div.all-activity.group
      [:div.all-activity-cards
        (when @(::top-loading s)
          [:div.loading-updates.top-loading
            "Retrieving activity..."])
        [:div.all-activity-cards-inner.group
          (when (or @(::top-loading s)
                    @(::retrieving-calendar s)
                    (and (:loading-more all-activity-data)
                         (not @(::first-render-done s)))
                    @(::scroll-to-entry s)
                    (= @(::last-direction s) :up))
            [:div.activities-overlay
              (rloading {:loading true})
              (when (or @(::top-loading s)
                        (= @(::last-direction s) :up))
                [:div.top-loading-message "Retrieving earlier activity..."])])
          (for [e items]
            (rum/with-key (activity-card e (not (empty? (:headline e))) (not (empty? (:body e))) true) (str "all-activity-entry-" (:uuid e))))]
        (when @(::bottom-loading s)
          [:div.loading-updates.bottom-loading
            "Retrieving activity..."])
        (when @(::show-all-caught-up-message s)
          (all-caught-up))]
      [:div.all-activity-nav
        [:div.all-activity-nav-inner
          (for [year (vec (reverse (sort-by :year calendar-data)))
                :let [selected (= @(::selected-year s) (:year year))]]
            [:div.group
              {:key (str "calendar-" (:year year))}
              [:div.nav-year
                {:on-click #(let [link (utils/link-for (:links year) "self")
                                  month (:month (first (:months year)))]
                              (reset! (::selected-year s) (:year year))
                              (reset! (::selected-month s) month)
                              (reset! (::scroll-to-entry s) false)
                              (reset! (::retrieving-calendar s) (str (:year year)))
                              (dis/dispatch! [:all-activity-calendar {:link link :year (:year year) :month month}]))
                 :class (when selected "selected")}
                [:span.calendar-label (:year year)]
                (when (= @(::retrieving-calendar s) (str (:year year)))
                  [:span.retrieving "Retrieving..."])]
              (when selected
                (for [month (vec (reverse (sort-by :month (:months year))))]
                  [:div.nav-month
                    {:key (str "year-" (:year month) "-month-" (:month month))
                     :class (utils/class-set {:selected (and (= @(::selected-year s) (:year month))
                                                             (= @(::selected-month s) (:month month)))})
                     :on-click #(let [link (utils/link-for (:links month) "self")]
                                  (reset! (::selected-year s) (:year month))
                                  (reset! (::selected-month s) (:month month))
                                  (reset! (::scroll-to-entry s) false)
                                  (reset! (::retrieving-calendar s) (str (:year month) (:month month)))
                                  (dis/dispatch! [:all-activity-calendar {:link link :year (:year month) :month (:month month)}]))}
                    [:span.calendar-label (utils/full-month-string (:month month))]
                    (when (= @(::retrieving-calendar s) (str (:year month) (:month month)))
                      [:span.retrieving "Retrieving..."])]))])]]]))