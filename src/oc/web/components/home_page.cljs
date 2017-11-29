(ns oc.web.components.home-page
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.try-it-form :refer (try-it-form)]
            [oc.web.components.ui.carrot-box-thanks :refer (carrot-box-thanks)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(defn retina-src [url]
  {:src (utils/cdn (str url ".png"))
   :src-set (str (utils/cdn (str url "@2x.png")) " 2x")})

(rum/defcs home-page < (rum/local false ::thanks-box-top)
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
      (site-mobile-menu)
      ;; preload slack button as hidden
      [:img.hidden {:src "https://api.slack.com/img/sign_in_with_slack.png"}]
      (login-overlays-handler)

      [:div.main.home-page
        ; Hope page header
        [:section.cta.group
          [:div.balloon.big-yellow]
          [:div.balloon.big-red]
          [:div.balloon.big-purple]
          [:div.balloon.big-green]
          [:div.balloon.small-purple-face]
          [:div.balloon.small-red]
          [:div.balloon.small-yellow-face]
          [:div.balloon.small-yellow]
          [:div.balloon.small-red-face
            {:class (when (jwt/jwt) "no-get-started-button")}]
          [:div.balloon.small-purple]
          [:div.balloon.small-blue-face]
          [:div.balloon.small-red-1]
          [:div.balloon.small-yellow-1]
          [:div.balloon.small-green-face]

          [:h1.headline
            "Rise above the noise"]
          [:div.subheadline
            "Give your team a clear view of what’s most important."]
          ; (when (and (not @(::confirm s))
          ;            (not @(::thanks-box-top s)))
          ;   (try-it-form "try-it-form-central" #(reset! (::thanks-box-top s) true)))
          [:div.get-started-button-container
            (when-not (jwt/jwt)
              [:button.mlb-reset.get-started-button
                {:on-click #(if (utils/in? (:route @router/path) "login")
                              (dis/dispatch! [:login-overlay-show :signup-with-slack])
                              (router/nav! oc-urls/sign-up))}
                "Get started for free"])]
          (when (and (not @(::confirm s))
                     @(::thanks-box-top s))
            (carrot-box-thanks))
          (when @(::confirm s)
            [:div.carrot-box-container.group
              [:div.carrot-box-thanks
                [:div.thanks-headline "You are Confirmed!"]
                [:div.thanks-subheadline "Thank you for subscribing."]]])

          [:img.homepage-main-screenshot
            {:src (utils/cdn "/img/ML/new_homepage_screenshot.png")
             :srcSet (str (utils/cdn "/img/ML/new_homepage_screenshot@2x.png") " 2x")}]
          [:div.homepage-screenshot-bubble
            "Carrot provides the big picture that keeps everyone on the same page."]]

        [:section.second-section.group
          [:div.why-balloon.big-red]
          ; [:div.why-balloon.big-blue]
          [:div.why-balloon.small-yellow]
          [:div.why-balloon.big-purple]
          [:div.why-balloon.small-purple]
          [:div.why-balloon.big-yellow]
          [:div.why-balloon.small-yellow]
          [:div.why-balloon.big-green]
          [:div.why-balloon.small-red]
          [:div.why-balloon.small-purple-face]

          [:div.illustrations-title
            [:div.why-carrot
              "Why Carrot?"]
            [:div.why-carrot-description
              (str
               "Growing teams need a place to rise above the noise of real-time conversations to see "
               "what’s really happening across the company.")]]

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
                  "All together"]
                [:div.subtitle
                  (str
                   "Daily or weekly digest reinforces "
                   "big picture content so everyone has "
                   "the same information.")]]]]

          [:div.slack-section
            [:div.slack-logo]
            [:div.slack-title
              "Did we mention our Slack integration?"]
            [:div.slack-description
              (str
               "Posts are automatically shared to the right channels. Discussions about posts happen "
               "in Slack and Carrot - everything is kept in sync.")]
            [:button.mlb-reset.slack-btn
              "Learn More"]]]

        [:section.third-section.group
          [:div.illustrations-title
            [:div.why-carrot
              "Share your big picture with stakeholders"]
            [:div.why-carrot-description
              "Create and organize stakeholder updates in less time"]]
          [:div.third-section-footer.group
            [:div.copy
              [:div.copy-icon.copy-simplify]
              [:div.title
                "Simplify investor updates"]
              [:div.description
                (str
                 "Create beautiful updates in a snap, and keep them "
                 "organized in one place. Also ideal for keeping friends "
                 "and family in the loop.")]]
            [:div.copy-separator]
            [:div.copy
              [:div.copy-icon.copy-expand]
              [:div.title
                "Expand your network"]
              [:div.description
                (str
                 "Share news with recruits, potential investors and customers to keep them "
                 "engaged and supportive. Build trust and grow your business.")]]]]

        [:section.fourth-section.group
          [:div.above-noise-container
            [:div.above-noise-title
              "Rise above the noise"]
            [:div.above-noise-description
              "Give your team a clear view of what’s most important."]
            (when-not (jwt/jwt)
              [:button.mlb-reset.get-started-button
                {:on-click #(if (utils/in? (:route @router/path) "login")
                              (dis/dispatch! [:login-overlay-show :signup-with-slack])
                              (router/nav! oc-urls/sign-up))}
                "Get started for free"])]]
      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])
