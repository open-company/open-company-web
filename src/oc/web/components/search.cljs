(ns oc.web.components.search
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [taoensso.timbre :as timbre]
            [org.martinklepsch.derivatives :as drv]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.activity-utils :as au]
            [oc.web.urls :as oc-urls]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.actions.search :as search]
            [oc.web.stores.search :as store]))

(rum/defcs entry-display < rum/static
  (rum/local nil ::activity-url)
  {:after-render (fn [s]
                   (let [body-el (rum/ref-node s "body")]
                     (au/truncate-body body-el)))
   :on-click #(search/result-clicked @(::activity-url %))}
  [s data]
  (let [result (:_source data)
        title (:headline result)
        author (first (:author-name result))]
    (reset! (::activity-url s)
            (oc-urls/entry (:board-slug result) (:uuid result)))
    [:div.search-result
     [:div.author
      (user-avatar-image {:user-id (first (:author-id result))
                          :name author
                          :avatar-url (first (:author-url result))} false)
      [:span author]]
     [:div.content
      [:div.title title]
      (let [body-without-preview (utils/body-without-preview (:body result))
            emojied-body (utils/emojify body-without-preview)]
        [:p {:ref "body"
             :dangerouslySetInnerHTML  emojied-body}])
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
                                (timbre/debug e)
                                (when (not
                                       (utils/event-inside? e
                                         (sel1 [:div.search-box])))
                                  (let [node (rum/ref-node s "results")]
                                    (.add (.-classList node) "inactive"))))))
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
      (for [sr search-results]
        (let [key (str "result-" (:uuid (:_source sr)))]
          (case (:type (:_source sr))
            "entry" (rum/with-key (entry-display sr) key)
            "board" (rum/with-key (board-display sr) key))))
      ]]))