(ns oc.web.components.ui.mobile-tabbar
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.notifications :as notif-actions]
            [oc.web.components.user-notifications :as user-notifications]
            [oc.web.actions.nav-sidebar :as nav-actions]))

(rum/defc mobile-tabbar
  [{:keys [can-compose? user-notifications-data active-tab]}]
  [:div.mobile-tabbar
    {:class (dom-utils/class-set {:can-compose can-compose?})}
    [:button.mlb-reset.tab-button.following-tab
      {:on-click #(do
                    (dom-utils/stop-propagation! %)
                    (nav-actions/nav-to-container! % "following" (oc-urls/following)))
      :class (when (= :following active-tab) "active")}
      [:span.tab-icon]
      [:span.tab-label "Home"]]
    [:button.mlb-reset.tab-button.topics-tab
      {:on-click #(do
                    (dom-utils/stop-propagation! %)
                    (nav-actions/nav-to-container! % "topics" (oc-urls/topics)))
      :class (when (= :topics active-tab) "active")}
      [:span.tab-icon]
      [:span.tab-label "Explore"]]
    [:button.mlb-reset.tab-button.replies-tab
      {:on-click #(do
                    (dom-utils/stop-propagation! %)
                    (nav-actions/nav-to-container! % "replies" (oc-urls/replies)))
      :class (when (= :replies active-tab) "active")}
      [:span.tab-icon]
      [:span.tab-label "Activity"]]
    [:button.mlb-reset.tab-button.notifications-tab
      {:on-click #(do
                    (dom-utils/stop-propagation! %)
                    (notif-actions/toggle-mobile-user-notifications))
      :class (when (= :user-notifications active-tab) "active")}
      [:span.tab-icon
      (when (user-notifications/has-new-content? user-notifications-data)
        [:span.unread-dot])]
      [:span.tab-label "Alerts"]]
    (when can-compose?
      [:button.mlb-reset.tab-button.new-post-tab
        {:on-click #(do
                      (dom-utils/stop-propagation! %)
                      (activity-actions/ui-compose))
        :class (when (= :new active-tab) "active")}
        [:span.tab-icon]
        [:span.tab-label "New"]])])