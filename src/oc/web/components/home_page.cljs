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
          [:h1.headline "Grow Together"]
          [:div.subheadline#thank-you-top
            "Announcements, updates, ideas and stories that bring teams closer"]
          ; (when (and (not @(::confirm s))
          ;            (not @(::thanks-box-top s)))
          ;   (try-it-form "try-it-form-central" #(reset! (::thanks-box-top s) true)))
          (if (jwt/jwt)
            [:button.mlb-reset.get-started-centred-bt
              {:on-click #(router/nav! oc-urls/login)}
              "Your Boards"]
            [:button.mlb-reset.get-started-centred-bt
              {:on-click #(if (utils/in? (:route @router/path) "login")
                            (dis/dispatch! [:login-overlay-show :signup-with-slack])
                            (router/nav! oc-urls/sign-up-with-slack))}
            "Get started for free"])
          [:div.small-teams
            "Easy set-up • Free for small teams"]
          (when (and (not @(::confirm s))
                     @(::thanks-box-top s))
            (carrot-box-thanks))
          (when @(::confirm s)
            [:div.carrot-box-container.group
              [:div.carrot-box-thanks
                [:div.thanks-headline "You are Confirmed!"]
                [:div.thanks-subheadline "Thank you for subscribing."]]])

          ;; FIXME: Remove the carrot screenshot for the initial onboarding period
          (comment
            [:img.homepage-screenshot
              {:src (utils/cdn "/img/ML/home_page_screenshot.png")
               :width 756
               :height 511}])]


        [:div.illustrations.group

          [:div.illustration.illustration-1.group
            [:img {:src (utils/cdn "/img/ML/home_page_il_1_412_385.svg")}]
            [:div.description.group
              [:div.title
                "Get your team aligned"]
              [:div.subtitle
                "Whether your team is local or distributed, keep everyone focused on what’s most important."]]]

          [:div.illustration.illustration-2.group
            [:img {:src (utils/cdn "/img/ML/home_page_il_2_444_414.svg")}]
            [:div.description.group
              [:div.title
                "Simplify stakeholder updates"]
              [:div.subtitle
                "Create beautiful updates for your company, "
                "investors, and advisors in less time. It’s never been "
                "easier to keep all stakeholders in the loop."]]]

          [:div.illustration.illustration-3.group
            [:img {:src (utils/cdn "/img/ML/home_page_il_3_355_350.svg")}]
            [:div.description.group
              [:div.title
                "Expand your network"]
              [:div.subtitle
                "Share news with recruits, potential investors and "
                "customers to keep them engaged and supportive. "
                "It’s an easy way to build trust and grow your "
                "business."]]]]

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
