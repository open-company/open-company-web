(ns oc.web.components.all-activity
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.logging :as logging]
            [oc.web.components.entry-card :refer (entry-card)]
            [oc.web.components.ui.loading :refer (rloading)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

;; 800px from the end of the current rendered results as point to add more entries in the batch
(def scroll-card-threshold 3)
(def card-avg-height 600)

(defn dbg [& args]
  (apply (partial logging/dbg "all-activity") args))

(def last-scroll (atom 0))

(defn- check-entry
  "Given an entry and a date string in the form YYYY-MM-DD
   check if the entry was created on the date or before."
  [entry date-str]
  (let [js-date (utils/js-date date-str)
        entry-date (utils/js-date (:created-at entry))]
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
  "Given a list of entries and a year and a month get the first available entry from the first of that month."
  [entries year month]
  (let [date-str (str year "-" (utils/add-zero month) "-" (days-for-month year month) "T23:59:59.999Z")]
    (loop [ens (vec (rest entries))
           en (first entries)]
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
  [entries]
  (when (pos? (count entries))
    (loop [ens entries
           en (first entries)]
      (let [el (sel1 [(str "div.entry-card-" (:uuid en))])]
        ;; Do not loop if there are no more entries or if found the first visible entry
        (if (or (zero? (count ens))
                (and el
                     (element-is-visible el)))
          en
          (recur (vec (rest ens))
                 (first (vec (rest ens)))))))))

(defn highlight-calendar
  "Get the first visible entry and highlight the corresponding year and month in the calendar."
  [s]
  ;; When we are not retrieving calendar and not waiting to scroll to an entry
  (when (and (not @(::retrieving-calendar s))
             (not @(::scroll-to-entry s)))
    (let [entries-batch (:entries (first (:rum/args s)))
          first-visible-entry (get-first-visible-entry entries-batch)
          js-date (utils/js-date (:created-at first-visible-entry))]
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
      (dbg "   :up" @(::has-next s))
      ;; Show a spinner at the top
      (reset! (::top-loading s) true)
      ;; if the user is close to the top margin, load more results if there is a link
      (dis/dispatch! [:all-activity-more @(::has-next s) :up])
      (reset! (::has-next s) false))
    ;; scrolling down
    (when (and @(::has-prev s)
               (= direction :down)
               (>= scroll-top (- max-scroll (* scroll-card-threshold card-avg-height))))
      (dbg "   :down" @(::has-prev s))
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
                          {:will-mount (fn [s]
                                        (let [all-activity-data (first (:rum/args s))
                                              year (:year all-activity-data)
                                              month (:month all-activity-data)
                                              direction (:direction all-activity-data)
                                              next-link (utils/link-for (:links all-activity-data) "previous")
                                              prev-link (utils/link-for (:links all-activity-data) "next")
                                              first-entry-date (utils/js-date (:created-at (first (:entries all-activity-data))))
                                              first-available-entry (when (and year month) (get-first-available-entry (:entries all-activity-data) year month))]
                                          (dbg "will-mount   year" year "month" month "first-entry-date" first-entry-date "first-available-entry" first-available-entry)
                                          (if (and year month)
                                            ;; Loading from calendar since we have year and month from the click action
                                            (do
                                              (reset! (::selected-year s) year)
                                              (reset! (::selected-month s) month)
                                              (reset! (::scroll-to-entry s) first-available-entry))
                                            ;; First load or subsequent load more with different set of entries
                                            (if (= direction :up)
                                              ;; did scrolled up, we need to scroll to the first of the old entries to not lose the previous position
                                              (let [saved-entries (:saved-entries all-activity-data)
                                                    last-new-entry-idx (dec (- (count (:entries all-activity-data)) saved-entries))
                                                    scroll-to-entry (get (:entries all-activity-data) last-new-entry-idx)
                                                    created-date (utils/js-date (:created-at scroll-to-entry))
                                                    to-year (.getFullYear created-date)
                                                    to-month (inc (int (.getMonth created-date)))]
                                                (reset! (::selected-year s) to-year)
                                                (reset! (::selected-month s) to-month)
                                                (reset! (::scroll-to-entry s) scroll-to-entry))
                                              ;; did scrolled down, results where simply concatenated, just need to update the calendar highlighting
                                              (do
                                                (reset! (::selected-year s) (.getFullYear first-entry-date))
                                                (reset! (::selected-month s) (inc (int (.getMonth first-entry-date)))))))
                                          (reset! (::retrieving-calendar s) nil)
                                          (reset! (::top-loading s) false)
                                          (reset! (::bottom-loading s) false)
                                          (when next-link
                                            (reset! (::has-next s) next-link))
                                          (when prev-link
                                            (reset! (::has-prev s) prev-link)))
                                        s)
                           :did-mount (fn [s]
                                        (dbg "did-mount")
                                        (reset! last-scroll (.-scrollTop (.-body js/document)))
                                        (reset! (::scroll-listener s)
                                         (events/listen js/window EventType/SCROLL #(did-scroll s %)))
                                        s)
                           :after-render (fn [s]
                                           (when-not @(::first-render-done s)
                                              (reset! (::first-render-done s) true))
                                           (when-let [scroll-to @(::scroll-to-entry s)]
                                             (when-let [entry-el (sel1 [(str "div.entry-card-" (:uuid scroll-to))])]
                                               (dbg "scrolling to:" entry-el)
                                               (utils/scroll-to-element entry-el 100 0))
                                             (utils/after 100 #(reset! (::scroll-to-entry s) nil)))
                                           s)
                           :did-remount (fn [_ s]
                                          (dbg "did-remount")
                                          (let [all-activity-data (first (:rum/args s))]
                                            (when-not (:loading-more all-activity-data)
                                              (when @(::top-loading s)
                                                (reset! (::top-loading s) false)
                                                (reset! (::has-next s) nil))
                                              (when @(::bottom-loading s)
                                                (reset! (::bottom-loading s) false)
                                                (reset! (::has-prev s) nil)))
                                            (when @(::retrieving-calendar s)
                                              (reset! (::retrieving-calendar s) false)
                                              ;; Scroll to the first entry of the selected month if any
                                              (let [calendar-data @(drv/get-ref s :calendar)
                                                    year @(::selected-year s)
                                                    month (or @(::selected-month s) (:month (first (filter #(= (:year %) year) calendar-data))))
                                                    first-available-entry (get-first-available-entry (:entries all-activity-data) @(::selected-year s) month)
                                                    next-link (utils/link-for (:links all-activity-data) "previous")
                                                    prev-link (utils/link-for (:links all-activity-data) "next")]
                                                (reset! (::has-next s) next-link)
                                                (reset! (::has-prev s) prev-link)
                                                (when first-available-entry
                                                  (reset! (::scroll-to-entry s) first-available-entry)))))
                                          s)
                           :will-unmount (fn [s]
                                          (when @(::scroll-listener s)
                                            (events/unlistenByKey @(::scroll-listener s)))
                                           s)}
  [s all-activity-data]
  (let [calendar-data (drv/react s :calendar)
        entries (:entries all-activity-data)]
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
                    @(::scroll-to-entry s))
            [:div.entries-overlay
              (rloading {:loading true})])
          (for [e entries]
            (rum/with-key (entry-card e (not (empty? (:headline e))) (not (empty? (:body e))) true) (str "all-activity-entry-" (:uuid e))))]
        (when @(::bottom-loading s)
          [:div.loading-updates.bottom-loading
            "Retrieving activity..."])]
      [:div.all-activity-nav
        [:div.all-activity-nav-inner
          (for [year calendar-data
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
                (for [month (:months year)]
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