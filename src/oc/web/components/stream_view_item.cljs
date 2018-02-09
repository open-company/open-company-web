(ns oc.web.compoents.stream-view-item
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(rum/defcs stream-view-item < rum/reactive
  [s activity-data]
  [:div.stream-view-item
    [:div.stream-view-item-header
      (user-avatar-image (:publisher activity-data))
      [:div.name (:name (:publisher activity-data))]
      [:div.time-since
        (let [t (or (:published-at activity-data) (:created-at activity-data))]
          [:time
            {:date-time t
             :data-toggle (when-not is-mobile? "tooltip")
             :data-placement "top"
             :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
             :title (utils/activity-date-tooltip activity-data)}
            (utils/time-since t)])]]
    [:div.stram-view-item-body
      ]])