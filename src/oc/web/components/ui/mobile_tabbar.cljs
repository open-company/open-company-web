(ns oc.web.components.ui.mobile-tabbar
  (:require [rum.core :as rum]
            [oc.shared.useragent :as ua]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.actions.notifications :as notif-actions]
            [oc.web.components.user-notifications :as user-notifications]
            [oc.web.actions.nav-sidebar :as nav-actions]))

(rum/defc mobile-tabbar
  [{:keys [can-compose? show-add-post-tooltip
           user-notifications-data active-tab]}]
  (let [no-phisical-home-button (js/isiPhoneWithoutPhysicalHomeBt)]
    [:div.mobile-tabbar
      {:class (utils/class-set {:can-compose can-compose?
                                :ios-app-tabbar (and ua/mobile-app?
                                                    no-phisical-home-button)
                                :ios-web-tabbar (and (not ua/mobile-app?)
                                                    no-phisical-home-button)})}
      [:button.mlb-reset.tab-button.following-tab
       {:on-click #(do
                     (.stopPropagation %)
                     (nav-actions/nav-to-url! % "following" (oc-urls/following)))
        :class (when (= :following active-tab) "active")}
       [:span.tab-icon]
       [:span.tab-label "Home"]]
      [:button.mlb-reset.tab-button.topics-tab
       {:on-click #(do
                     (.stopPropagation %)
                     (nav-actions/nav-to-url! % "topics" (oc-urls/topics)))
        :class (when (= :topics active-tab) "active")}
       [:span.tab-icon]
       [:span.tab-label "Explore"]]
      [:button.mlb-reset.tab-button.replies-tab
       {:on-click #(do
                     (.stopPropagation %)
                     (nav-actions/nav-to-url! % "replies" (oc-urls/replies)))
        :class (when (= :replies active-tab) "active")}
       [:span.tab-icon]
       [:span.tab-label "Activity"]]
      [:button.mlb-reset.tab-button.notifications-tab
       {:on-click #(do
                     (.stopPropagation %)
                     (notif-actions/toggle-mobile-user-notifications))
        :class (when (= :user-notifications active-tab) "active")}
       [:span.tab-icon
        (when (user-notifications/has-new-content? user-notifications-data)
          [:span.unread-dot])]
       [:span.tab-label "Alerts"]]
      (when can-compose?
        [:button.mlb-reset.tab-button.new-post-tab
         {:on-click #(do
                       (.stopPropagation %)
                       (ui-compose show-add-post-tooltip))
          :class (when (= :new active-tab) "active")}
         [:span.tab-icon]
         [:span.tab-label "New"]])]))