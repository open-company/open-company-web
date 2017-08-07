(ns oc.web.components.all-activity
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dis]
            [oc.web.components.entry-card :refer (entry-card)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(def scroll-threshold 250)

(def last-scroll (atom 0))

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
    (loop [ens (rest entries)
           en nil]
      (if (and (pos? (count ens))
               (check-entry (first ens) date-str))
        (recur (rest ens)
               (first ens))
        en))))

(defn did-scoll [s e]
  (when (or @(::has-next s)
            @(::has-prev s))
    (let [body (.-body js/document)
          scroll-top (.-scrollTop body)
          max-scroll (- (.-scrollHeight body) (.-innerHeight js/window))
          min-scroll 0
          direction (if (> @last-scroll scroll-top)
                      :up
                      (if (< @last-scroll scroll-top)
                        :down
                        :stale))]
      (when (and @(::has-prev s)
                 (= direction :up)
                 (< (- scroll-top min-scroll) scroll-threshold))
        (dis/dispatch! [:all-activity-more @(::has-prev s)])
        (reset! (::scroll-to-entry s) false)
        (reset! (::has-prev s) false))
      (when (and @(::has-next s)
                 (= direction :down)
                 (< (- max-scroll scroll-top) scroll-threshold))
        (dis/dispatch! [:all-activity-more @(::has-next s)])
        (reset! (::scroll-to-entry s) false)
        (reset! (::has-next s) false))))
  (reset! last-scroll (.-scrollTop (.-body js/document))))

(rum/defcs all-activity < rum/static
                          rum/reactive
                          (drv/drv :all-activity)
                          (drv/drv :calendar)
                          (rum/local nil ::scroll-listener)
                          (rum/local false ::has-next)
                          (rum/local false ::has-prev)
                          (rum/local nil ::selected-year)
                          (rum/local nil ::selected-month)
                          (rum/local nil ::scroll-to-entry)
                          {:will-mount (fn [s]
                                        (reset! (::scroll-listener s)
                                         (events/listen js/window EventType/SCROLL #(did-scoll s %)))
                                        (dis/dispatch! [:calendar-get])
                                        s)
                           :did-mount (fn [s]
                                        (reset! last-scroll (.-scrollTop (.-body js/document)))
                                        (let [all-activity-data (first (:rum/args s))
                                              next-link (utils/link-for (:links all-activity-data) "next")
                                              prev-link (utils/link-for (:links all-activity-data) "previous")
                                              first-entry-date (utils/js-date (:created-at (first (:entries all-activity-data))))]
                                          (reset! (::selected-year s) (.getFullYear first-entry-date))
                                          (reset! (::selected-month s) (inc (int (.getMonth first-entry-date))))
                                          (when next-link
                                            (reset! (::has-next s) next-link))
                                          (when prev-link
                                            (reset! (::has-prev s) prev-link)))
                                        s)
                           :did-remount (fn [o s]
                                          (let [all-activity-data (first (:rum/args s))
                                                next-link (utils/link-for (:links all-activity-data) "next")
                                                prev-link (utils/link-for (:links all-activity-data) "previous")]
                                            (when @(::scroll-to-entry s)
                                              (let [first-month-entry (get-first-available-entry (:entries all-activity-data) @(::selected-year s) (or @(::selected-month s) 1))]
                                                (reset! (::scroll-to-entry s) first-month-entry)))
                                            (when next-link
                                              (reset! (::has-next s) next-link))
                                            (when prev-link
                                              (reset! (::has-prev s) prev-link)))
                                          s)
                           :after-render (fn [s]
                                           (when @(::scroll-to-entry s)
                                             (when-let [entry-el (sel1 [(str "div.entry-card-" (:uuid @(::scroll-to-entry s)))])]
                                               (utils/scroll-to-element entry-el -100)
                                               (reset! (::scroll-to-entry s) nil)))
                                           s)
                           :will-unmount (fn [s]
                                          (when @(::scroll-listener s)
                                            (events/unlistenByKey @(::scroll-listener s)))
                                           s)}
  [s all-activity-data]
  (let [calendar-data (drv/react s :calendar)]
    [:div.all-activity.group
      [:div.all-activity-cards
        [:div.group
          (for [e (:entries all-activity-data)]
            (rum/with-key (entry-card e (not (empty? (:headline e))) (not (empty? (:body e))) true) (str "all-activity-entry-" (:uuid e))))]]
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
                              (dis/dispatch! [:all-activity-calendar (utils/link-for (:links year) "self")]))
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
                                (dis/dispatch! [:all-activity-calendar (utils/link-for (:links month) "self")]))}
                  (utils/full-month-string (:month month))])])]]]))