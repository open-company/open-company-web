(ns oc.web.components.ui.trial-expired-banner
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.nav-sidebar :as nav-actions]))

(rum/defc trial-expired-banner < rum/static
  []
  [:div.trial-expired-banner-container
    [:div.trial-expired-banner
      "📣 Your 14 day free trial has ended. Please "
      [:button.mlb-reset.open-billing-bt
        {:on-click #(nav-actions/show-org-settings :billing)}
        "select a plan"]
      " to continue using Carrot. Need more time? "
      [:a.chat-with-us
        {:class "intercom-chat-link"
         :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
        "Chat with us"]
      "."]])

(rum/defc trial-expired-alert < rum/static
  [{:keys [left top bottom right]}]
  [:div.trial-expired-alert-container
    {:style {:left left
             :top top
             :bottom bottom
             :right right}}
    [:div.trial-expired-alert
      [:div.trial-expired-alert-title
        "14 day free trial has ended"]
      [:div.trial-expired-alert-description
        (str
         "Hi, it looks like your free trial has ended. Pick a plan that "
         "works best for your team to continue adding new posts.")]
      [:button.mlb-reset.trial-expired-alert-bt
        {:on-click #(do
                      (utils/event-stop %)
                      (nav-actions/show-org-settings :billing))}
        "Select a plan"]]])