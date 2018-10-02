(ns oc.web.components.slack-lander
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.user :as user-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.shared-misc :as shared-misc]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]))

(rum/defcs slack-lander < rum/reactive
                          (drv/drv :auth-settings)
  [s]
  (let [auth-settings (drv/react s :auth-settings)
        slack-auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                         {:auth-source "slack"})]
    [:div
      (site-header auth-settings true)
      (site-mobile-menu)
      [:div.slack-lander-wrap
        {:id "wrap"}
        [:div.main.slack-lander
          ; Hope page header
          [:section.cta.group
            
            [:a.carrot-logo
              {:href oc-urls/home-no-redirect}]

            [:h1.headline
              "Join your team on Carrot"]

            [:div.subheadline
              "Carrot makes it simple for Slack teams to stay aligned around what matters most."]
            ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
            [:button.mlb-reset.continue-with-slack-bt
              {:on-click #(user-actions/login-with-slack slack-auth-link)}]

            [:div.main-animation-container
              [:img.main-animation
                {:src (utils/cdn "/img/ML/slack_screenshot.png")
                 :src-set (str (utils/cdn "/img/ML/slack_screenshot@2x.png") " 2x")}]]]]]

      (site-footer)]))