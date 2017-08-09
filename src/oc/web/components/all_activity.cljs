(ns oc.web.components.all-activity
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.logging :as logging]
            [oc.web.components.entry-card :refer (entry-card)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.object :as gobj]))

;; 800px from the end of the current rendered results as point to add more entries in the batch
(def scroll-threshold 800)

(def last-scroll (atom 0))

(defn dbg [& args]
  (when (= @logging/carrot-log-level :debug)
    (apply (partial js/console.log "all-activity") args)))

(defn- check-entry
  "Given an entry and a date string in the form YYYY-MM-DD
   check if the entry was created on the date or before."
  [entry date-str]
  (let [js-date (utils/js-date date-str)
        entry-date (utils/js-date (:created-at entry))]
    (>= (.getTime entry-date) (.getTime js-date))))

(defn get-first-available-entry
  "Given a list of entries and a year and a month get the first available entry from the first of that month."
  [entries year month]
  (let [date-str (str year "-" (utils/add-zero month) "-01T00:00:00.000Z")]
    (loop [ens (vec (rest entries))
           en nil]
      (if (and (pos? (count ens))
               (check-entry (first ens) date-str))
        (recur (vec (rest ens))
               (first ens))
        en))))

(defn element-is-visible
  "Check if the element is in the visible portion of the page, considered the page scroll."
  [el]
  (let [el-offset-top (.-offsetTop el)
        body-scroll (.-scrollTop (.-body js/document))
        diff (- el-offset-top body-scroll)]
    (and (> diff 0)
         (< diff (.-innerHeight js/window)))))

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

(defn switch-year-month
  "Get the first visible entry and highlight the corresponding year and month in the calendar."
  [s]
  (let [entries-batch (:entries (first (:rum/args s)))
        first-visible-entry (get-first-visible-entry entries-batch)
        js-date (utils/js-date (:created-at first-visible-entry))]
    (reset! (::selected-year s) (.getFullYear js-date))
    (reset! (::selected-month s) (inc (.getMonth js-date)))))

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
               (= direction :up)
               (<= scroll-top (+ min-scroll scroll-threshold)))
      (dbg "   :up" @(::has-next s))
      ;; Show a spinner at the top
      (reset! (::top-loading s) true)
      ;; if the user is close to the top margin, load more results if there is a link
      (dis/dispatch! [:all-activity-more @(::has-next s) :up])
      (reset! (::has-next s) false))
    ;; scrolling down
    (when (and @(::has-prev s)
               (= direction :down)
               (>= scroll-top (- max-scroll scroll-threshold)))
      (dbg "   :down" @(::has-prev s))
      ;; Show a spinner at the bottom
      (reset! (::bottom-loading s) true)
      ;; if the user is close to the bottom margin, load more results if there is a link
      (dis/dispatch! [:all-activity-more @(::has-prev s) :down])
      (reset! (::has-prev s) false)))
  ;; Highlight the right year/month
  (when @(::first-render-done s)
    (switch-year-month s))
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
                          {:will-mount (fn [s]
                                        (reset! (::scroll-listener s)
                                         (events/listen js/window EventType/SCROLL #(did-scroll s %)))
                                        s)
                           :did-mount (fn [s]
                                        (dbg  "all-activity/did-mount")
                                        (reset! last-scroll (.-scrollTop (.-body js/document)))
                                        (let [all-activity-data (first (:rum/args s))
                                              year (:year all-activity-data)
                                              month (:month all-activity-data)
                                              next-link (utils/link-for (:links all-activity-data) "previous")
                                              prev-link (utils/link-for (:links all-activity-data) "next")
                                              first-entry-date (utils/js-date (:created-at (first (:entries all-activity-data))))
                                              first-available-entry (get-first-available-entry (:entries all-activity-data) year month)]
                                          (if (and year month)
                                            (do
                                              (reset! (::selected-year s) year)
                                              (reset! (::selected-month s) month)
                                              (reset! (::scroll-to-entry s) first-available-entry))
                                            (do
                                              (reset! (::selected-year s) (.getFullYear first-entry-date))
                                              (reset! (::selected-month s) (inc (int (.getMonth first-entry-date))))))
                                          (when next-link
                                            (reset! (::has-next s) next-link))
                                          (when prev-link
                                            (reset! (::has-prev s) prev-link)))
                                        s)
                           :after-render (fn [s]
                                           (when-not @(::first-render-done s)
                                              (reset! (::first-render-done s) true))
                                           (when @(::scroll-to-entry s)
                                             (when-let [entry-el (sel1 [(str "div.entry-card-" (:uuid @(::scroll-to-entry s)))])]
                                               (utils/scroll-to-element entry-el -20)
                                               (reset! (::scroll-to-entry s) nil))
                                             (switch-year-month s))
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
            [:i.fa.fa-spinner.fa-spin.fa-2x.fa-fw]])
        [:div.group
          (for [e entries]
            (rum/with-key (entry-card e (not (empty? (:headline e))) (not (empty? (:body e))) true) (str "all-activity-entry-" (:uuid e))))]
        (when @(::bottom-loading s)
          [:div.loading-updates.bottom-loading
            [:i.fa.fa-spinner.fa-spin.fa-2x.fa-fw]])]
      [:div.all-activity-nav
        [:div.all-activity-nav-inner
          (for [year calendar-data]
            [:div.group
              {:key (str "calendar-" (:year year))}
              [:div.nav-year
                {:on-click #(do
                              (reset! (::selected-year s) (:year year))
                              (reset! (::selected-month s) nil)
                              (reset! (::scroll-to-entry s) true)
                              (dis/dispatch! [:all-activity-calendar {:link (utils/link-for (:links year) "self") :year (:year year) :month 1}]))
                 :class (when (= @(::selected-year s) (:year year)) "selected")}
                (:year year)]
              (for [month (:months year)]
                [:div.nav-month
                  {:key (str "year-" (:year month) "-month-" (:month month))
                   :class (when (and (= @(::selected-year s) (:year month))
                                     (= @(::selected-month s) (:month month))) "selected")
                   :on-click #(do
                                (reset! (::selected-year s) (:year month))
                                (reset! (::selected-month s) (:month month))
                                (reset! (::scroll-to-entry s) true)
                                (dis/dispatch! [:all-activity-calendar {:link (utils/link-for (:links month) "self") :year (:year month) :month (:month month)}]))}
                  (utils/full-month-string (:month month))])])]]]))