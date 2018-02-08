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
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(defn get-started-button [auth-settings & [show-disclaimer]]
  (when-not (jwt/jwt)
    [:div
      [:button.signin-with-slack.mlb-reset
        {:on-click #(do
                     (.preventDefault %)
                     (when-let [auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                                           {:auth-source "slack"})]
                       (user-actions/login-with-slack auth-link)))}
        "Sign up with"
        [:div.slack-white-icon]]
      (when show-disclaimer
        [:div.signin-with-slack-disclaimer
          ; [:div.signin-with-slack-description
          ;   "Securely sign in or sign up with your Slack account."]
          [:div.signin-with-slack-description ;.second-line
            "By signing in, you agree to the "
            [:a
              {:href oc-urls/terms}
              "Terms of Use"]
            " and "
            [:a
              {:href oc-urls/privacy}
              "Privacy Policy."]]])]))

(rum/defcs slack < rum/static
                   rum/reactive
                   (drv/drv :auth-settings)
  [s]
  (let [auth-settings (drv/react s :auth-settings)]
    [:div
      [:div.slack-wrap
        {:id "wrap"}

        (site-header auth-settings true)
        (site-mobile-menu)
        (login-overlays-handler)

        [:div.main.slack.group
          {:class (when (jwt/jwt) "no-get-started-button")}
          [:section.carrot-plus-slack.group
            ;; Top Left
            [:div.balloon.big-green]
            [:div.balloon.small-purple]
            [:div.balloon.small-yellow]
            ;; Top Right
            [:div.balloon.big-red]
            [:div.balloon.small-yellow-face]
            [:div.balloon.small-purple-1]
            ;; Center Left
            [:div.balloon.big-blue]
            [:div.balloon.small-purple-2]
            [:div.balloon.small-green]
            [:div.balloon.big-purple]
            [:div.balloon.small-red]


            [:div.carrot-plus-slack]

            [:h1.slack
              "Grow together"]

            [:div.slack-subline-container
              [:div.slack-subline
                (str
                  "Company updates and stories that keep teams "
                  "aligned around what matters most.")]]

            (get-started-button auth-settings true)

            shared-misc/video

            shared-misc/horizontal-carousell

            [:div.designed-for-container
              [:div.designed-for
                "Designed for Slack"]
              [:div.designed-for-content
                (str
                 "Slack is fun and awesome for real-time work, but gets noisy. "
                 "With Carrot, leaders rise above the noise "
                 "to keep everyone on the same page.")]]

            shared-misc/carrot-cards]

          shared-misc/carrot-testimonials

          (when-not (jwt/jwt)
            [:section.third-section
              [:div.third-section-title
                (str
                 "Slack keeps your team connected in the moment. "
                 "Carrot keeps it aligned over time.")]
              (get-started-button auth-settings)])

          ]]

        (site-footer)]))