(ns oc.web.components.slack
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(defn get-started-button []
  (when-not (jwt/jwt)
    [:div
      [:button.signin-with-slack.mlb-reset
        {:on-click #(do
                     (.preventDefault %)
                     (when (:auth-settings @dis/app-state)
                       (dis/dispatch! [:login-with-slack])))}
        "Sign in with"
        [:div.slack-white-icon]]
      [:div.signin-with-slack-description
        "Securely sign in or sign up with your Slack account."]
      [:div.signin-with-slack-description.second-line
        "By signing in, you agree to the "
        [:a
          {:href oc-urls/terms}
          "Terms of Use"]
        " and "
        [:a
          {:href oc-urls/privacy}
          "Privacy Policy."]]]))

(rum/defcs slack
  [s]
  [:div
    [:div.slack-wrap
      {:id "wrap"}

      (site-header)
      (site-mobile-menu)
      (login-overlays-handler)

      [:div.main.slack.group
        [:section.carrot-plus-slack.group
          {:class (when (jwt/jwt) "no-get-started-button")}
          [:div.balloon.big-yellow]
          [:div.balloon.big-red]
          [:div.balloon.big-purple]
          [:div.balloon.small-purple-face]
          [:div.balloon.small-red]
          [:div.balloon.small-yellow-face]
          [:div.balloon.small-yellow]
          [:div.balloon.big-blue]
          [:div.balloon.small-purple]
          [:div.balloon.small-green-face]
          [:div.balloon.big-purple-1]

          [:div.carrot-plus-slack]

          [:h1.slack
            "Introducing Carrot for Slack"]

          [:div.slack-subline
            (str
             "Slack keeps your team connected in the moment. "
             "Carrot keeps it aligned over time.")]

          (get-started-button)

          [:div.slack-box-illustration-container
            [:div.slack-box-illustration]
            [:div.slack-box-description
              (str
               "Slack is great for real-time work, but it’s easy to miss important stuff in the noise - "
               "like key announcements, updates and plans that keep teams on the same page. "
               "Carrot works with Slack to make sure that doesn’t happen.")]]

        [:section.why-carrot
          [:div.why-balloon.big-yellow]
          [:div.why-balloon.small-purple]
          [:div.why-balloon.big-green]
          [:div.why-balloon.small-yellow]

          [:h1.why-carrot-headline
            "Why Carrot?"]
          [:div.why-carrot-subheadline
            (str
              "Growing and distributed teams need a place to rise above the noise of real-time "
              "conversations to see what’s really happening across the company.")]

          [:div.illustrations.group
            [:div.illustration-container
              [:div.illustration.illustration-1]
              [:div.description
                [:div.title
                  "Visibility"]
                [:div.subtitle
                  (str
                   "A bird’s-eye view of essential "
                 "information that’s easy to read and "
                 "creates real transparency.")]]]
            [:div.illustration-container.right
              [:div.illustration.illustration-2]
              [:div.description
                [:div.title
                  "In context"]
                [:div.subtitle
                  (str
                   "Related information stays organized to "
                   "have the most impact. Great for current "
                   "and new employees.")]]]
            [:div.illustration-container
              [:div.illustration.illustration-3]
              [:div.description
                [:div.title
                  "Feedback & engagement"]
                [:div.subtitle
                  (str
                   "Capture team sentiment and reactions "
                   "to key communications. It’s fun and "
                   "great for distributed teams too!")]]]
            [:div.illustration-container.right
              [:div.illustration.illustration-4]
              [:div.description
                [:div.title
                  "The big picture"]
                [:div.subtitle
                  (str
                   "Daily or weekly digest for email "
                   "and Slack ensures everyone has the "
                   "same view of what’s important.")]]]]]

        [:section.designed-for
          [:h1.designed-for-headline
            "Designed for Slack teams"]
          [:div.designed-for-subheadline
            (str
             "Onboard your Slack team with single sign-on to make management and security simple. "
             "The Carrot Slack bot makes sharing with Slack teams seamless.")]

          (get-started-button)]]]]

      (site-footer)])