(ns oc.web.components.stream-view-item
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.comments :refer (comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.interactions-summary :refer (reactions-summary)]))

(rum/defcs stream-view-item < rum/reactive
  [s activity-data]
  (let [is-mobile? (responsive/is-tablet-or-mobile?)]
    [:div.stream-view-item
      [:div.stream-view-item-header
        [:div.stream-header-head-author
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
                (utils/time-since t)])]]]
      [:div.stream-view-item-body
        [:div.stream-body-left
          [:span.posted-in
            {:dangerouslySetInnerHTML (utils/emojify (str "Posted in " (:board-name activity-data)))}]
          [:div.stream-item-headline
            {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
          [:div.stream-item-body
            {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))}]
          [:div.stream-item-reactions
            (reactions-summary activity-data)]]
        [:div.stream-body-right
          (comments activity-data)]]]))