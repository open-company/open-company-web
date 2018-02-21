(ns oc.web.components.home-page
  (:require-macros [dommy.core :refer (sel1)])
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.shared-misc :as shared-misc]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]
            [oc.web.components.ui.try-it-form :refer (try-it-form)]
            [oc.web.components.ui.site-mobile-menu :refer (site-mobile-menu)]
            [oc.web.components.ui.carrot-box-thanks :refer (carrot-box-thanks)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

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
        {:class (when (jwt/jwt) "no-get-started-button")}
        ; Hope page header
        [:section.cta.group
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
            "Carrot, your team digest."]
          [:div.subheadline
            "Keep everyone aligned around what matters most."]
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

          shared-misc/video

          shared-misc/horizontal-carousell

          [:div.stay-aligned-container
            [:div.stay-aligned-icon]
            [:div.stay-aligned-message
              "Stay aligned around the topics that matter."]]

          shared-misc/carrot-cards]

        shared-misc/carrot-testimonials

        (when-not (jwt/jwt)
          [:section.third-section
            [:div.third-section-title
              "Keep everyone aligned around what matters most."]
            [:button.mlb-reset.get-started-button
              "Get started for free"]])
      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])
