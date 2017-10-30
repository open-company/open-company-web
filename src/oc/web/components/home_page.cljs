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
          [:h1.headline "When teams have more to say"]
          [:div.subheadline
            "Not everything fits in a chat message."]
          [:div.subheadline.second-line
            "Elevate your team mission, announcements and updates to create transparency and alignment."]
          ; (when (and (not @(::confirm s))
          ;            (not @(::thanks-box-top s)))
          ;   (try-it-form "try-it-form-central" #(reset! (::thanks-box-top s) true)))
          (when-not (jwt/jwt)
            [:button.mlb-reset.get-started-centred-bt
              {:on-click #(if (utils/in? (:route @router/path) "login")
                            (dis/dispatch! [:login-overlay-show :signup-with-slack])
                            (router/nav! oc-urls/sign-up-with-slack))}
            "Get started for free"])
          (when-not (jwt/jwt)
            [:div.small-teams
              {:id "easy-setup-label"}
              "Easy set-up • Free for small teams"])
          (when (and (not @(::confirm s))
                     @(::thanks-box-top s))
            (carrot-box-thanks))
          (when @(::confirm s)
            [:div.carrot-box-container.group
              [:div.carrot-box-thanks
                [:div.thanks-headline "You are Confirmed!"]
                [:div.thanks-subheadline "Thank you for subscribing."]]])

          [:div.homepage-screenshot]]


        [:div.illustrations.group

          [:div.illustration.illustration-1.group
            [:div.illustration-image]
            [:div.description.group
              [:h1.headline "When teams have more to say"]
            [:div.subheadline
              "Not everything fits in a chat message. Elevate your team mission, announcements and updates to create transparency and alignment."]]]

          [:div.illustration.illustration-2.group
            [:div.illustration-image]
            [:div.description.group
              [:div.title
                "Keep stakeholders in the loop, too"]
              [:div.subtitle
                "Share updates with your investors and advisors, or the latest news with your customers and partners. Carrot keeps it all organized in one place."]]]]

        [:div.home-section.second-section
          [:div.illustrations-title
            [:div.why-carrot
              "Why Carrot?"]
            [:div.why-carrot-description
              "Growing teams need a place to rise above the noise of real-time conversations to see what’s really happening across the company."]]

          [:div.illustrations.group
            [:div.illustration.illustration-3.group
              [:div.illustration-image]
              [:div.description.group
                [:div.title
                  "Visibility"]
                [:div.subtitle
                  "Unlike chat streams and wikis, Carrot creates a birds-eye view of the latest news that’s quick and easy to read. The big picture pulls everyone closer."]]]

            [:div.illustration.illustration-4.group
              [:div.illustration-image]
              [:div.description.group
                [:div.title
                  "Easy alignment"]
                [:div.subtitle
                  "Whether you’re adding a quick team update, or writing an overview that covers many topics, or adding a guide for new employees, getting started is simple and fast."]]]

            [:div.illustration.illustration-5.group
              [:div.illustration-image]
              [:div.description.group
                [:div.title
                  "Feedback loops"]
                [:div.subtitle
                  "Getting on the same page is easier when everyone can react and add comments - great for distributed teams. It’s more fun, too! 💥✌"]]]

            [:div.illustration.illustration-6.group
              [:div.illustration-image]
              [:div.description.group
                [:div.title
                  "Works with Slack"]
                [:div.subtitle
                  "With Slack single sign-on and our Slack bot, posts are automatically shared to the right channels. Discussions about posts can happen in Slack or Carrot - everything is kept in sync. " [:a {:href "/about"} "Learn More"]]]]

            [:div.illustration.illustration-7.group
              [:div.illustration-image]
              [:div.description.group
                [:div.title
                  "Stay private or go public"]
                [:div.subtitle
                  "Boards can be private and invite-only, or can be made public - ideal for crowdfunded ventures, social enterprises, and startups interested in full transparency."]]]]]

        [:div.home-section.third-section
          [:div.illustrations-title
            [:div.why-carrot
              "Don’t forget your extended team"]
            [:div.why-carrot-description
              "Investors, advisors and other stakeholders stay engaged when you keep them in the loop. With Carrot, it’s never been easier."]
            [:div.centred-screenshot]]
          [:div.third-section-footer.group
            [:div.left-copy
              [:div.title
                "Simplify investor updates"]
              [:div.description
                "Create a Carrot board specifically for investors and advisors in no time. All of your updates will stay organized in one place so it’s easy to know what you’ve sent them in the past. Also ideal for keeping friends and family in the loop."]]
            [:div.right-copy
              [:div.title
                "Build a bigger network"]
              [:div.description
                "Share news with recruits, potential investors and customers to keep them engaged and supportive. It’s an easy way to build trust and grow your business."]]]]

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

        ; [:div.try-it
        ;   {:id "mc_embed_signup"}
        ;   [:div.try-it-title
        ;     {:id "thank-you-bottom"}
        ;     "Request early access"]
        ;   [:div.try-it-subtitle
        ;     "Easy set-up • Free for small teams"]
        ;   (when-not @(::thanks-box-bottom s)
        ;     [:div
        ;       (try-it-form "try-it-form-bottom" #(reset! (::thanks-box-bottom s) true))])
        ;   (when @(::thanks-box-bottom s)
        ;     (carrot-box-thanks))]

      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])
