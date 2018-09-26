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
    (site-header)
    (site-mobile-menu)
    [:div.home-wrap
      {:id "wrap"}
      (login-overlays-handler)
      [:div.main.home-page
        shared-misc/animation-lightbox
        ; Hope page header
        [:section.cta.group

          [:a.carrot-logo]

          [:h1.headline
            "Communicate"
            [:br]
            "what matters."]
          [:div.subheadline
            "With Carrot, leaders rise above the noise"
            [:br.big-web-only]
            "to keep distributed teams focused and up to date."]
          ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
          [:div.get-started-button-container
            [:button.mlb-reset.get-started-button
              {:id "get-started-centred-bt"}
              "Get started for free"]]
          shared-misc/no-credit-card
          (carrot-box-thanks "carrot-box-thanks-top")
          [:div.carrot-box-container.confirm-thanks.group
            {:style {:display "none"}}
            [:div.carrot-box-thanks
              [:div.thanks-headline "You are Confirmed!"]
              [:div.thanks-subheadline "Thank you for subscribing."]]]

          [:div.main-animation-container
            [:img.main-animation
              {:src (utils/cdn "/img/ML/homepage_screenshot.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshot@2x.png") " 2x")}]]

          [:div.cta-lightbox-starter
            [:h2 "Carrot keeps leaders and teams aligned."]
            [:button.mlb-reset.watch-video-bt
              {:onClick "OCStaticShowAnimationLightbox();"}]]]

        shared-misc/keep-aligned-section

        shared-misc/testimonials-section

        shared-misc/keep-aligned-bottom]]

    (site-footer)])
