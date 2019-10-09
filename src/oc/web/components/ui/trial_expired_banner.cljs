(ns oc.web.components.ui.trial-expired-banner
  (:require [rum.core :as rum]
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