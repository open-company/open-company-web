(ns oc.web.components.search
  (:require [rum.core :as rum]
            [taoensso.timbre :as timbre]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.urls :as oc-urls]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.actions.search :as search]
            [oc.web.stores.search :as store]))


(rum/defcs search-box < (drv/drv store/search-key)
                        rum/reactive
                        rum/static

[s c]
(let [search-results (drv/react s store/search-key)]
  [:div.search-box
    [:div.search
      {:content-editable true
       :ref "search-input"
       :placeholder "Search..."
       :on-blur #(let [node (rum/ref-node s "results")]
                   (.add (.-classList node) "inactive"))
       :on-key-up #(search/query (.-innerHTML (rum/ref-node s "search-input")))
       }]
    [:div.search-results {:ref "results"
                          :class (when (empty? search-results) "inactive")}
     (for [sr search-results]
       (let [result (:_source sr)]
         [:div.search-result {:key (str "result-" (:uuid (:_source sr)))}
          [:div.author
           (user-avatar-image {:user-id (:author-id result)
                               :name (:author-name result)
                               :avatar-url (first(:author-url result))} false)
           [:span (:author-name result)]]
          [:div
           [:span (:headline result)]
           [:span (:name result)]
           (let [body-without-preview (utils/body-without-preview (:body result))
                 activity-url (oc-urls/entry (:board-slug result) (:uuid result))
                 emojied-body (utils/emojify body-without-preview)]
             [:div {:dangerouslySetInnerHTML  emojied-body}])
          ]]))
     ]]))