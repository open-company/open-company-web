(ns oc.web.components.home-page
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user]
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
          {:class (when (jwt/jwt) "no-get-started-button")}
          [:div.balloon.big-yellow]
          [:div.balloon.big-red]
          [:div.balloon.big-purple]
          [:div.balloon.big-green]
          [:div.balloon.small-blue]
          [:div.balloon.small-yellow-face]
          [:div.balloon.small-purple]
          [:div.balloon.small-red]
          [:div.balloon.small-purple-2]
          [:div.balloon.big-green-2]
          [:div.balloon.small-yellow]

          [:h1.headline
            "Where leaders speak"]
          [:div.subheadline.big-web-only
            "Leadership updates that keep everyone aligned"]
          [:div.subheadline.second-line.big-web-only
            "around what matters most"]
          [:div.subheadline.mobile-only
            (str
             "Leadership updates that keep everyone aligned "
             "around what matters most")]
          ; (when (and (not @(::confirm s))
          ;            (not @(::thanks-box-top s)))
          ;   (try-it-form "try-it-form-central" #(reset! (::thanks-box-top s) true)))
          [:div.get-started-button-container
            (when-not (jwt/jwt)
              [:button.mlb-reset.get-started-button
                {:on-click #(if (utils/in? (:route @router/path) "login")
                              (user/show-login :signup-with-slack)
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

          [:video.homepage-main-animation
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
            [:div.left-gradient]
            [:div.right-gradient]
            [:div.horizontal-carousell-inner
              [:img.horizontal-carousell-1
                {:src (retina-src "/img/ML/homepage_mobile_screenshot_1")}]
              [:img.horizontal-carousell-2
                {:src (retina-src "/img/ML/homepage_mobile_screenshot_2")}]
              [:img.horizontal-carousell-3
                {:src (retina-src "/img/ML/homepage_mobile_screenshot_3")}]]]

          [:div.stay-aligned-container
            [:div.stay-aligned-icon]
            [:div.stay-aligned-message
              "Stay aligned around topics that matter."]]


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
                    "Learn more"]]]]]]

        [:section.second-section
          [:div.balloon.big-yellow-1]
          [:div.balloon.small-blue-1]
          [:div.second-section-title
            "Don’t take our word for it"]
          [:div.second-section-subtitle
            "Here’s how we’re helping teams like yours."]
          [:div.cards-container.group
            [:div.card
              [:div.card-content
                (str
                 "We love Slack for spontaneous "
                 "stuff, but Carrot for more "
                 "focused, topic-based "
                 "conversations.")]
              [:div.card-author
                [:img.card-avatar
                  {:src (utils/cdn "/img/ML/happy_face_blue.svg")}]
                [:div.author-name
                  "John Smith"]
                [:div.author-company
                  "Acme Co."]]]
            [:div.card
              [:div.card-content
                (str
                 "Crucial updates get drowned "
                 "out by \"taco "
                 "Tuesday?\" and silly memes. "
                 "Carrot makes sure the "
                 "important stuff is always there.")]
              [:div.card-author
                [:img.card-avatar
                  {:src (utils/cdn "/img/ML/happy_face_purple.svg")}]
                [:div.author-name
                  "John Smith"]
                [:div.author-company
                  "Acme Co."]]]
            [:div.card
              [:div.card-content
                (str
                 "RIP team email! 👻  With "
                 "Carrot, email is finally "
                 "obsolete for team updates "
                 "and the rest.")]
              [:div.card-author
                [:img.card-avatar
                  {:src (utils/cdn "/img/ML/happy_face_red.svg")}]
                [:div.author-name
                  "John Smith"]
                [:div.author-company
                  "Acme Co."]]]
            [:div.card
              [:div.card-content
                (str
                 "I never knew if anyone even "
                 "saw my updates! Carrot shows "
                 "me who's viewed what so I know "
                 "who’s engaged and aligned. 🙏")]
              [:div.card-author
                [:img.card-avatar
                  {:src (utils/cdn "/img/ML/happy_face_yellow.svg")}]
                [:div.author-name
                  "John Smith"]
                [:div.author-company
                  "Acme Co."]]]]]

        (when-not (jwt/jwt)
          [:section.third-section
            [:div.third-section-title
              "Keep everyone aligned around what matters most."]
            [:button.mlb-reset.get-started-button
              "Get started for free"]])
      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])
