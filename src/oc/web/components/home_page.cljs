(ns oc.web.components.home-page
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.try-it-form :refer (try-it-form)]
            [oc.web.components.ui.carrot-box-thanks :refer (carrot-box-thanks)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(defn retina-src [url]
  {:src (utils/cdn (str url ".png"))
   :src-set (str (utils/cdn (str url "@2x.png")) " 2x")})

(rum/defcs home-page < rum/static
                       (rum/local false ::thanks-box-top)
                       (rum/local false ::thanks-box-bottom)
                       (rum/local false ::confirm)
                       {:did-mount (fn [s]
                                    (when (:tif (:query-params @router/path))
                                      (utils/after 1500 #(.focus (sel1 [:input.try-it-form-central-input]))))
                                    s)
                       :will-mount (fn [s]
                                     (when (:confirm (:query-params @router/path))
                                       (reset! (::confirm s) true))
                                     s)}
  [s]
  [:div
    [:div {:id "wrap"} ; <!-- used to push footer to the bottom -->
      (site-header)
      ;; preload slack button as hidden
      [:img.hidden {:src "https://api.slack.com/img/sign_in_with_slack.png"}]
      (login-overlays-handler)

      [:div.main.home-page
        ; Hope page header
        [:div.cta
          [:div.balloon.big-yellow]
          [:div.balloon.big-red]
          [:div.balloon.big-purple]
          [:div.balloon.big-green]
          [:div.balloon.small-green]
          [:div.balloon.small-purple-face]
          [:div.balloon.small-red]
          [:div.balloon.small-yellow-face]
          [:div.balloon.small-red-face]
          [:div.balloon.small-green-face]
          [:div.balloon.small-blue-face]
          [:div.balloon.small-yellow-1]
          [:div.balloon.small-yellow-2]
          [:h1.headline
            "Get above the noise"]
          [:div.subheadline
            "Give your team a clear view of what’s most important."]
          ; (when (and (not @(::confirm s))
          ;            (not @(::thanks-box-top s)))
          ;   (try-it-form "try-it-form-central" #(reset! (::thanks-box-top s) true)))
          [:button.mlb-reset.get-started-button
            {:on-click #(if (utils/in? (:route @router/path) "login")
                          (dis/dispatch! [:login-overlay-show :signup-with-slack])
                          (router/nav! oc-urls/sign-up-with-slack))}
            "Get started for free"]
          (when (and (not @(::confirm s))
                     @(::thanks-box-top s))
            (carrot-box-thanks))
          (when @(::confirm s)
            [:div.carrot-box-container.group
              [:div.carrot-box-thanks
                [:div.thanks-headline "You are Confirmed!"]
                [:div.thanks-subheadline "Thank you for subscribing."]]])

          [:div.homepage-main-screenshot]
          [:div.homepage-screenshot-bubble
            (str
             "Chat apps simplify real-time work, but constant chatter makes it easy to miss key information. "
             "Carrot provides a high-level narrative that keeps everyone on the same page.")]]

        [:div.home-section.second-section
          [:div.why-balloon.big-red]
          [:div.why-balloon.big-purple]
          [:div.why-balloon.big-yellow]
          [:div.why-balloon.big-green]
          [:div.why-balloon.small-purple]
          [:div.why-balloon.small-yellow]
          [:div.illustrations-title
            [:div.why-carrot
              "Why Carrot?"]
            [:div.why-carrot-description
              (str
               "Growing teams need a place to rise above the noise of real-time conversations to see "
               "what’s really happening across the company.")]]

          [:div.illustrations.group
            [:div.illustration
              [:div.description
                [:div.title
                  "Transparency"]
                [:div.subtitle
                  (str
                   "A bird’s-eye view of important "
                   "information that’s easy to find for "
                   "current and new employees.")]]]
            [:div.illustration.right
              [:div.description
                [:div.title
                  "Simplicity"]
                [:div.subtitle
                  (str
                   "If you’re adding a quick team update, or "
                   "writing an overview for the next all-"
                   "hands, Carrot is simple and fast.")]]]
            [:div.illustration
              [:div.description
                [:div.title
                  "Feedback & engagement"]
                [:div.subtitle
                  (str
                   "Capture team sentiment and reactions "
                   "to key communications. It’s fun and "
                   "great for distributed teams too!")]]]
            [:div.illustration.right
              [:div.description
                [:div.title
                  "Privacy"]
                [:div.subtitle
                  (str
                   "Privacy settings mean sensitive "
                   "information can be invite-only, and "
                   "public information can be fully open.")]]]]
          [:div.slack-section
            [:div.slack-logo]
            [:div.slack-title
              "Did we mention our Slack integration?"]
            [:div.slack-description
              (str
               "Posts can be automatically shared to the right channels. Discussions about posts happen "
               "in Slack and Carrot - everything is kept in sync.")]
            [:button.mlb-reset.slack-btn
              "Learn More"]]]

        [:div.home-section.third-section.group
          [:div.illustrations-title
            [:div.why-carrot
              "Going beyond the team"]
            [:div.why-carrot-description
              "Create and organize stakeholder updates in less time"]]
          [:div.third-section-footer.group
            [:div.copy
              [:div.title
                "Simplify investor updates"]
              [:div.description
                (str
                 "Create beautiful updates in a snap, and keep them "
                 "organized in one place. Also ideal for keeping friends "
                 "and family in the loop.")]]
            [:div.copy-separator]
            [:div.copy
              [:div.title
                "Expand your network"]
              [:div.description
                (str
                 "Share news with recruits, potential investors and "
                 "customers to keep them engaged and supportive. ")]
              [:div.description
                (str
                 "It’s an easy way to build trust and grow your business.")]]]]

        [:div.home-section.fourth-section
          [:div.above-noise-container
            [:div.above-noise-title
              "Get above the noise"]
            [:div.above-noise-description
              "Give your team a clear view of what’s most important."]
            [:button.mlb-reset.get-started-button
              "Get started for free"]]]

        (comment
          (when-not (jwt/jwt)
            [:div.try-it
              {:id "mc_embed_signup"}
              [:div.try-it-title
                {:id "thank-you-bottom"}
                "Request early access"]
              [:button.get-started-button
                "Get Started"]]))

      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])
