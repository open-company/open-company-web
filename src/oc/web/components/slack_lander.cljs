(ns oc.web.components.slack-lander
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
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
            [:div.balloon.big-blue]
            [:div.balloon.small-green]
            [:div.balloon.big-green]
            [:div.balloon.small-purple-face]
            [:div.balloon.big-yellow]
            [:div.balloon.small-purple]

            [:h1.headline
              "Join your team on Carrot"]
            [:div.subheadline
              "Your team is using Carrot to keep everyone aligned around what matters most."]
            ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
            [:div.get-started-button-container
              [:button.mlb-reset.get-started-button
                {:on-click #(user-actions/login-with-slack slack-auth-link)}
                [:span.slack-white-icon]
                (if (responsive/is-tablet-or-mobile?)
                  "Join your team"
                  "Sign in with Slack and join your team")]]

            [:div.main-animation-container
              [:img.main-animation
                {:src (utils/cdn "/img/ML/slack_screenshot.png")
                 :src-set (str (utils/cdn "/img/ML/slack_screenshot@2x.png") " 2x")}]]

            [:div.core-values-list.group
              [:div.core-value.key-announcement
                "Key announcements"]
              [:div.core-value.company-updates
                "Company & team updates"]
              [:div.core-value.strategic-plans
                "Strategic plans"]
              [:div.core-value.ideas-discussions
                "Ideas & follow-on discussions"]]]

          [:section.keep-aligned
            [:div.keep-aligned-title
              "Sign in with Slack to to join your team on Carrot"]
            [:button.mlb-reset.get-started-button
              {:on-click #(user-actions/login-with-slack slack-auth-link)}
              [:span.slack-white-icon]
              (if (responsive/is-tablet-or-mobile?)
                "Join your team"
                "Sign in with Slack and join your team")]]]]

      (site-footer)]))