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
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(defn get-started-button [auth-settings]
  (when-not (jwt/jwt)
    [:div
      [:button.signin-with-slack.mlb-reset
        {:on-click #(do
                     (.preventDefault %)
                     (when-let [auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                                           {:auth-source "slack"})]
                       (user-actions/login-with-slack auth-link)))}
        "Sign in with"
        [:div.slack-white-icon]]
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
          "Privacy Policy."]]]))

(rum/defcs slack < rum/static
                   rum/reactive
                   (drv/drv :auth-settings)
  [s]
  (let [auth-settings (drv/react s :auth-settings)]
    [:div
      [:div.slack-wrap
        {:id "wrap"}

        (site-header)
        (site-mobile-menu)
        (login-overlays-handler)

        [:div.main.slack.group
          [:section.carrot-plus-slack.group
            {:class (when (jwt/jwt) "no-get-started-button")}

            [:div.carrot-plus-slack]

            [:h1.slack
              "Where leaders speak"]

            [:div.slack-subline-container
              [:div.slack-subline
                "Leadership updates that keep everyone aligned"]
              [:div.slack-subline
                 "around what matters most."]]

            (get-started-button auth-settings)

            [:video.slack-main-animation
              {:controls false
               :auto-play true
               :poster (utils/cdn "/img/ML/new_homepage_screenshot.png")
               :loop true}
              [:source
                {:src (utils/cdn "/img/ML/animation.webm")
                 :type "video/webm"}]
              [:source
                {:src (utils/cdn "/img/ML/animation.mp4")
                 :type "video/mp4"}]
              [:div.fallback
                "Your browser doesn’t support this video format."]]

            [:div.horizontal-carousell
              [:div.horizontal-carousell-inner
                [:img.horizontal-carousell-1
                  (utils/retina-src "/img/ML/homepage_mobile_screenshot_1")]
                [:img.horizontal-carousell-2
                  (utils/retina-src "/img/ML/homepage_mobile_screenshot_2")]
                [:img.horizontal-carousell-3
                  (utils/retina-src "/img/ML/homepage_mobile_screenshot_3")]]]

            [:div.designed-for-container
              [:div.designed-for
                "Designed for Slack"]
              [:div.designed-for-content
                (str
                 "Slack is fun and awesome for real-time work, but gets noisy. "
                 "With Carrot, the important stuff rises above the noise "
                 "keeping everyone on the same page.")]]

            [:div.cards-container
              [:div.cards-row.group
                [:div.card.card-1
                  [:div.card-icon]
                  [:div.card-title
                    (str
                     "Highlight what’s "
                     "important")]
                  [:div.card-content
                    (str
                      "Elevate key updates above "
                      "the noise so they won’t be "
                      "missed. It’s perfect for "
                      "distributed teams, too.")]]
                [:div.card.card-2
                  [:div.card-icon]
                  [:div.card-title
                    (str
                      "Cross-team "
                      "awareness")]
                  [:div.card-content
                    (str
                      "Keep teams in sync with "
                      "each other so you can see "
                      "what’s happening across "
                      "the company.")]]
                [:div.card.card-3
                  [:div.card-icon]
                  [:div.card-title
                    (str
                      "Focused, topic-based "
                      "conversations")]
                  [:div.card-content
                    (str
                      "Capture team reactions, "
                      "comments and questions "
                      "together in one place.")]]]
              [:div.cards-row.group
                [:div.card.card-4
                  [:div.card-icon]
                  [:div.card-title
                    (str
                      "The whole "
                      "story")]
                  [:div.card-content
                    (str
                      "New employees get up to "
                      "speed quickly with the full "
                      "picture in one place.")]]
                [:div.card.card-5
                  [:div.card-icon]
                  [:div.card-title
                    (str
                      "Visible "
                      "engagement")]
                  [:div.card-content
                    (str
                      "Carrot measures team "
                      "engagement so leaders "
                      "can actually see if their "
                      "teams are aligned or not.")]]
                [:div.card.card-6
                  [:div.card-icon]
                  [:div.card-title
                    (str
                      "In sync "
                      "with Slack")]
                  [:div.card-content
                    (str
                      "Communication is "
                      "automatically shared to the "
                      "right channel. ")
                    [:a
                      {:href oc-urls/slack
                       :on-click #(router/nav! oc-urls/slack)}
                      "Learn more"]]]]]

          ]]]

        (site-footer)]))