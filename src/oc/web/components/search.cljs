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
      [:div.name author]
      [:div.time-since
       (let [t (or (:published-at result) (:created-at result))]
         [:time
          {:date-time t
           :data-toggle "tooltip"
           :data-placement "top"
           :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
           :title (utils/activity-date-tooltip result)}
          (utils/time-since t)])]
      [:div.title {:dangerouslySetInnerHTML title}]
      ]]))

(rum/defcs board-display < rum/static
  [s data]
  (let [board (:_source data)]
    [:div.search-result
     [:div.content
      [:span (:name board)]
      ]
     ]))

(rum/defcs search-box < (drv/drv store/search-key)
                         rum/reactive
                         rum/static
                         (rum/local nil ::window-click)
                         {:will-mount (fn [s]
                           (reset! (::window-click s)
                             (events/listen
                              js/window
                              EventType/CLICK
                              (fn [e]
                                (when (not
                                       (utils/event-inside? e
                                         (sel1 [:div.search-box])))
                                  (let [node (rum/ref-node s "results")]
                                    (.add (.-classList node) "inactive")))
                                e)))
                           s)
                          :will-unmount (fn [s]
                            (when @(::window-click s)
                              (events/unlistenByKey @(::window-click s))
                              (reset! (::window-click s) nil))
                            s)}
  [s]
  (let [search-results (drv/react s store/search-key)]
    [:div.search-box
     [:div.search
      {:content-editable true
       :ref "search-input"
       :placeholder "Search..."
       :on-key-up #(search/query (.-innerHTML (rum/ref-node s "search-input")))
       }]
     [:div.search-results {:ref "results"
                           :class (when (empty? search-results) "inactive")}
      [:div.header
       [:span "Posts"]
       [:span.count (str "(" (count search-results) ")")]]
      (for [sr search-results]
        (let [key (str "result-" (:uuid (:_source sr)))]
          (case (:type (:_source sr))
            "entry" (rum/with-key (entry-display sr) key)
            "board" (rum/with-key (board-display sr) key))))
      ]]))