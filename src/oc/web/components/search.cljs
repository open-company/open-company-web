(ns oc.web.components.search
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [taoensso.timbre :as timbre]
            [org.martinklepsch.derivatives :as drv]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.lib.utils :as utils]
            [oc.web.urls :as oc-urls]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.actions.search :as search]
            [oc.web.stores.search :as store]))

(rum/defcs entry-display < rum/static

  [s data]
  (let [result (:_source data)
        title (utils/emojify (:headline result))
        author (first (:author-name result))
        activity-url (oc-urls/entry (:board-slug result) (:uuid result))]
    [:div.search-result
     {:on-click (fn [s]
                  (search/result-clicked activity-url)
                  s)}
     [:div.author
      (user-avatar-image {:user-id (first (:author-id result))
                          :name author
                          :avatar-url (first (:author-url result))} false)
      [:div.title {:dangerouslySetInnerHTML title}]
      [:div.time-since
       (let [t (or (:published-at result) (:created-at result))]
         [:time
          {:date-time t
           :data-toggle "tooltip"
           :data-placement "top"
           :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
           :title (utils/activity-date-tooltip result)}
          (utils/time-since t)])]
      ]]))

(rum/defcs board-display < rum/static
  [s data]
  (let [board (:_source data)]
    [:div.search-result
     [:div.content
      [:span (:name board)]
      ]
     ]))

(defn search-inactive [s]
  (reset! (::search-clicked? s) false)
  (reset! (::page-size s) 5)
  (set! (.-value (rum/ref-node s "search-input")) "")
  (search/inactive))

(rum/defcs search-box < (drv/drv store/search-key)
                        (drv/drv store/search-active?)
                        rum/reactive
                        rum/static
                        (rum/local nil ::window-click)
                        (rum/local false ::search-clicked?)
                        (rum/local 5 ::page-size)
                        (rum/local 0 ::page-start)
                        {:will-mount (fn [s]
                          (search/inactive)
                          (reset! (::window-click s)
                            (events/listen
                             js/window
                             EventType/CLICK
                             (fn [e]
                               (when (not
                                      (utils/event-inside? e
                                        (sel1 [:div.search-box])))
                                 (do
                                   (.stopPropagation e)
                                   (search-inactive s)))
                               e)))
                          s)
                         :will-unmount (fn [s]
                           (when @(::window-click s)
                             (events/unlistenByKey @(::window-click s))
                             (reset! (::window-click s) nil))
                           s)}
  [s]
  (let [search-results (drv/react s store/search-key)
        search-active? (drv/react s store/search-active?)]
    [:div.search-box {:class (when @(::search-clicked? s) "active")}
      [:button.search-close {:class (when (not @(::search-clicked? s))
                                      "inactive")
                             :on-click #(search-inactive s)}]
      [:img.spyglass {:src (utils/cdn "/img/ML/spyglass.svg")
                      :on-click (fn [e]
                                  (reset! (::search-clicked? s) true)
                                  (.focus (rum/ref-node s "search-input")))}]
      [:input.search
        {:ref "search-input"
         :placeholder "Search"
         :on-click #(reset! (::search-clicked? s) true)
         :on-focus #(let [search-query (.-value (rum/ref-node s "search-input"))]
                      (search/query search-query))
         :on-key-down #(when (= "Enter" (.-key %)) (.preventDefault %))
         :on-change #(search/query
                      (.-value (rum/ref-node s "search-input")))
         }]
      [:div.triangle {:class (when (not search-active?) "inactive")}]
      [:div.search-results {:ref "results"
                            :class (when (not search-active?) "inactive")}
        [:div.header
          [:span "SEARCH RESULTS"]
          (when (pos? (:count search-results))
            [:span.count (str "(" (:count search-results) ")")])]
        [:div.search-results-container
          (if (pos? (:count search-results))
            (let [results (reverse (:results search-results))]
              (for [sr (take @(::page-size s) results)]
                (let [key (str "result-" (:uuid (:_source sr)))]
                  (case (:type (:_source sr))
                    "entry" (rum/with-key (entry-display sr) key)
                    "board" (rum/with-key (board-display sr) key)))))
            [:div.empty-result
              [:div.message "No matching results..."]])]
        (when (< @(::page-size s) (:count search-results))
          [:div.show-more
            {:on-click (fn [e] (reset! (::page-size s)
                                       (+ @(::page-size s) 15)))}
            [:button] "Show More"])
       ]]))