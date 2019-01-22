(ns oc.web.components.slack
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.components.ui.shared-misc :as shared-misc]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defcs slack < rum/static
                   rum/reactive
                   (drv/drv :auth-settings)
  [s]
  (let [auth-settings (drv/react s :auth-settings)]
    [:div
      [:div.slack-wrap
        {:id "wrap"}
        (site-header auth-settings)
        (site-mobile-menu)
        (login-overlays-handler)

        [:div.main.slack
          ; Hope page header
          [:section.carrot-plus-slack.group

            [:h1.slack-headline
              "Rise above the noise"]

            [:div.slack-subline
              (str
               "Leaders struggle to communicate effectively with fast-growing and "
               "remote teams. Carrot makes sure everyone hears you - even in noisy "
               "places like Slack and email. With Carrot, everyone stays on the same page.")]

            ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
            [:div.slack-button-container.group
              [:a.add-to-slack-button
                {:on-click #(do
                             (.preventDefault %)
                             (when-let [auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                                                   {:auth-source "slack"})]
                               (user-actions/login-with-slack auth-link)))}]]
            [:div.carrot-box-container.confirm-thanks.group
              {:style {:display "none"}}
              [:div.carrot-box-thanks
                [:div.thanks-headline "You are Confirmed!"]
                [:div.thanks-subheadline "Thank you for subscribing."]]]

            [:div.main-animation-container
              [:img.main-animation
                {:src (utils/cdn "/img/ML/slack_screenshot.png")
                 :srcSet (str (utils/cdn "/img/ML/slack_screenshot@2x.png") " 2x")}]]

            shared-misc/testimonials-logos-line]

          (shared-misc/keep-aligned-section true)

          shared-misc/testimonials-section

          shared-misc/keep-aligned-bottom]]

        (site-footer)]))