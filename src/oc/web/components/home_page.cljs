(ns oc.web.components.home-page
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.router :as router]
            [oc.web.local-settings :as ls]
            [oc.web.lib.prevent-route-dispatch :as prd]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.try-it-form :refer (try-it-form)]
            [oc.web.components.ui.carrot-box-thanks :refer (carrot-box-thanks)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defcs home-page < rum/static
                       (rum/local false ::thanks-box-top)
                       (rum/local false ::thanks-box-bottom)
                       {:did-mount (fn [s]
                                    (when (:tyt (:query-params @router/path))
                                      (utils/after 2500 #(do
                                                          (reset! prd/prevent-route-dispatch true)
                                                          (reset! (::thanks-box-top s) true)
                                                          (router/redirect! (str (.-location js/window) "#thank-you-top"))
                                                          (reset! prd/prevent-route-dispatch false))))
                                    (when (:tyb (:query-params @router/path))
                                      (utils/after 2500 #(do
                                                          (reset! prd/prevent-route-dispatch true)
                                                          (reset! (::thanks-box-bottom s) true)
                                                          (router/redirect! (str (.-location js/window) "#thank-you-bottom"))
                                                          (reset! prd/prevent-route-dispatch false))))
                                    (when (:tif (:query-params @router/path))
                                      (utils/after 1500 #(.focus (sel1 [:input.try-it-input-central]))))
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
          [:h1.headline "Company updates that build transparency and alignment"]
          [:div.subheadline#thank-you-top "It's never been easier to get everyone aligned - inside and outside the company."]
          (try-it-form "try-it-input-central")
          [:div.small-teams
            "Easy set-up • Free for small teams"]
          (when @(::thanks-box-top s)
            (carrot-box-thanks))

          ;; FIXME: Remove the carrot screenshot for the initial onboarding period
          (comment
            [:img.homepage-screenshot
              {:src "/img/ML/home_page_screenshot.png"
               :width 756
               :height 511}])]


        [:div.illustrations.group

          [:div.illustration.illustration-1.group
            [:img {:src "/img/ML/home_page_il_1_412_385.svg"}]
            [:div.description.group
              [:div.title
                "Get aligned fast"]
              [:div.subtitle
                "Check out what’s new this week, or get new employees up to speed in a flash. Updates are in one place and easy to find."]]]

          [:div.illustration.illustration-2.group
            [:img {:src "/img/ML/home_page_il_2_444_414.svg"}]
            [:div.description.group
              [:div.title
                "Keep investors up to date"]
              [:div.subtitle
                "Investors and advisors are happier - and more helpful - when they’re in the loop!"]]]

          [:div.illustration.illustration-3.group
            [:img {:src "/img/ML/home_page_il_3_355_350.svg"}]
            [:div.description.group
              [:div.title
                "Grow your business"]
              [:div.subtitle
                "Share the latest news with recruits, potential investors, and customers. Build trust with a bigger audience and they’ll reward you for it."]]]]

        (comment
          [:div.customers
            [:div.customers-title
              [:img {:src "/img/ML/user_avatar_yellow.svg"}]
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

        [:div.try-it
          {:id "mc_embed_signup"}
          [:div.try-it-title
            {:id "thank-you-bottom"}
            "Request early access"]
          [:div.try-it-subtitle
            "Easy set-up • Free for small teams"]
          [:div
            (try-it-form "try-it-input-bottom" nil)]
          (when @(::thanks-box-bottom s)
            (carrot-box-thanks))]

      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])
