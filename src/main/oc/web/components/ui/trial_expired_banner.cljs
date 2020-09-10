(ns oc.web.components.ui.trial-expired-banner
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]))

(rum/defcs trial-expired-banner < rum/static
                                  rum/reactive
                                  (drv/drv :org-data)
  [s]
  [:div.trial-expired-banner-container
    (if (jwt/is-admin? (:team-id (drv/react s :org-data)))
      [:div
        [:div.trial-expired-banner.reduced-copy
          "📣 Your 14-day free trial has ended. Please "
          [:button.mlb-reset.open-payments-bt
            {:on-click #(nav-actions/show-org-settings :payments)}
            "select a plan"]
          " to continue using Wut."]
        [:div.trial-expired-banner
          "📣 Your 14-day free trial has ended. Please "
          [:button.mlb-reset.open-payments-bt
            {:on-click #(nav-actions/show-org-settings :payments)}
            "select a plan"]
          " to continue using Wut. Need more time? "
          [:a.chat-with-us
            {:class "intercom-chat-link"
             :href oc-urls/contact-mail-to}
            "Chat with us"]
          "."]]
      [:div
        [:div.trial-expired-banner.reduced-copy
          "📣 Your 14-day free trial has ended. Please ask your team admin to choose a new plan."]
        [:div.trial-expired-banner
          "📣 Your team's 14-day free trial has ended. Please ask your team admin "
          "to subscribe to Wut. Need more time? "
          [:a.chat-with-us
            {:class "intercom-chat-link"
             :href oc-urls/contact-mail-to}
            "Chat with us"]
          "."]])])

(rum/defcs trial-expired-alert < rum/static
                                 rum/reactive
                                 (drv/drv :org-data)
  [s {:keys [left top bottom right]}]
  (let [org-data (drv/react s :org-data)
        is-admin? (jwt/is-admin? (:team-id org-data))]
    [:div.trial-expired-alert-container
      {:style {:left left
               :top top
               :bottom bottom
               :right right}}
      [:div.trial-expired-alert
        [:div.trial-expired-alert-title
          "Your 14-day free trial has ended"]
        (if is-admin?
          [:div.trial-expired-alert-description
            (str
             "Hi, it looks like your free trial has ended. Pick a plan that "
             "works best for your team to continue adding new posts.")]
          [:div.trial-expired-alert-description
            (str
             "Hi, it looks like your free trial has ended. Please ask your "
             "team admin to subscribe to Wut to continue adding new posts.")])
        (when is-admin?
          [:button.mlb-reset.trial-expired-alert-bt
            {:on-click #(do
                          (utils/event-stop %)
                          (nav-actions/show-org-settings :payments))}
            "Select a plan"])]]))