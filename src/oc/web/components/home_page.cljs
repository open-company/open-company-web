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
          [:h1.headline
            "Above the noise"]
          [:div.subheadline
            "Give your team a clear view of what’s most important"]
          ; (when (and (not @(::confirm s))
          ;            (not @(::thanks-box-top s)))
          ;   (try-it-form "try-it-form-central" #(reset! (::thanks-box-top s) true)))
          (when-not (jwt/jwt)
            [:button.mlb-reset.get-started-button
              {:on-click #(if (utils/in? (:route @router/path) "login")
                            (dis/dispatch! [:login-overlay-show :signup-with-slack])
                            (router/nav! oc-urls/sign-up-with-slack))}
            "Get started for free"])
          (when (and (not @(::confirm s))
                     @(::thanks-box-top s))
            (carrot-box-thanks))
          (when @(::confirm s)
            [:div.carrot-box-container.group
              [:div.carrot-box-thanks
                [:div.thanks-headline "You are Confirmed!"]
                [:div.thanks-subheadline "Thank you for subscribing."]]])

          [:div.homepage-main-screenshot]]


        [:div.illustrations.group

          [:div.illustration.illustration-1.group
            [:img.illustration-image
              (retina-src "/img/ML/home_page_il_1_572_438")]
            [:div.description.group
              [:div.subtitle
                (str
                 "Chat apps simplify real-time work, but constant "
                 "chatter makes it easy to miss key information.")]
              [:div.subtitle.second-line
                (str
                 "Carrot provides a lasting view of what’s important "
                 "to keep everyone on the same page.")]]]]

          ; [:div.illustration.illustration-2.group
          ;   [:img.illustration-image
          ;     (retina-src "/img/ML/home_page_il_2_521_385")]
          ;   [:div.description.group
          ;     [:div.title
          ;       "Keep stakeholders in the loop, too"]
          ;     [:div.subtitle
          ;       (str
          ;        "Share updates with your investors and advisors, or "
          ;        "the latest news with your customers and partners. "
          ;        "Carrot keeps it all organized in one place.")]]]]

        [:div.home-section.second-section
          [:div.illustrations-title
            [:div.why-carrot
              "Why Carrot?"]
            [:div.why-carrot-description
              (str
               "Growing teams need a place to rise above the noise of real-time conversations "
               "to see what’s really happening across the company.")]]

          [:div.illustrations.group
            [:div.illustration.illustration-3.group
              [:img.illustration-image
                (retina-src "/img/ML/home_page_il_3_450_349")]
              [:div.description.group
                [:div.title
                  "Visibility"]
                [:div.subtitle
                  (str
                   "A bird’s eye view of essential information is easy "
                   "to read and creates real transparency.")]]]

            [:div.illustration.illustration-4.group
              [:img.illustration-image
                (retina-src "/img/ML/home_page_il_4_521_283")]
              [:div.description.group
                [:div.title
                  "Simplicity"]
                [:div.subtitle
                  (str
                   "If you’re adding a quick team update, or writing "
                   "an overview for the next all-hands, getting started "
                   "is simple and fast.")]]]

            [:div.illustration.illustration-5.group
              [:img.illustration-image
                (retina-src "/img/ML/home_page_il_5_424_329")]
              [:div.description.group
                [:div.title
                  "Feedback loops"]
                [:div.subtitle
                  (str
                   "Getting on the same page is easier when everyone "
                   "can react and add comments - great for distributed "
                   "teams. It’s more fun, too! 💥✌")]]]

            [:div.illustration.illustration-6.group
              [:img.illustration-image
                (retina-src "/img/ML/home_page_il_6_346_321")]
              [:div.description.group
                [:div.title
                  "Works with Slack"]
                [:div.subtitle
                  (str
                   "Posts are automatically shared to the right "
                   "channels. Discussions about posts happen in Slack "
                   "and Carrot - everything is kept in sync. ")]
                [:div.subtitle
                  [:a
                    {:href "/about"}
                    "Learn More"]]]]

            [:div.illustration.illustration-7.group
              [:img.illustration-image
                (retina-src "/img/ML/home_page_il_7_333_274")]
              [:div.description.group
                [:div.title
                  "Private"]
                [:div.subtitle
                  "Sensitive information can be invite-only."]
                [:div.title.title-right
                  "... or Public"]
                [:div.subtitle.subtitle-right
                  "Ideal for crowdfunded ventures, social enterprises, "
                  "and startups interested in full transparency."]]]]]

        [:div.home-section.third-section
          [:div.illustrations-title
            [:div.why-carrot
              "Beyond the team"]
            [:div.why-carrot-description
              "Create awesome stakeholder updates in less time"]
            [:div.centred-screenshot]]
          [:div.third-section-footer.group
            [:div.left-copy
              [:div.title
                "Simplify investor updates"]
              [:div.description
                (str
                 "Create beautiful updates in a snap, and keep them "
                 "organized in one place. Also ideal for keeping friends "
                 "and family in the loop.")]]
            [:div.right-copy
              [:div.title
                "Expand your network"]
              [:div.description
                (str
                 "Share news with recruits, potential investors and "
                 "customers to keep them engaged and supportive. ")]
              [:div.description
                (str
                 "It’s an easy way to build trust and grow your business.")]]]]

        (comment
          [:div.customers
            [:div.customers-title
              [:img {:src (utils/cdn "/img/ML/happy_face_yellow.svg")}]
              "Our happy clients"]
            [:div.customers-cards.group
              [:div.left-arrow
                [:button.mlb-reset.left-arrow-bt
                  {:disabled true}]]
              [:div.customers-cards-scroll
                [:div.customers-card]
                [:div.customers-card]
                [:div.customers-card]]
              [:div.right-arrow
                [:button.mlb-reset.right-arrow-bt
                  {:disabled true}]]]])

        (when-not (jwt/jwt)
          [:div.try-it
            {:id "mc_embed_signup"}
            [:div.try-it-title
              {:id "thank-you-bottom"}
              "Request early access"]
            [:button.get-started-button
              "Get Started"]])

      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])
