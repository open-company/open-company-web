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
        [:div.balloon.big-blue]
        [:div.balloon.small-green]
        [:div.balloon.big-green]
        [:div.balloon.small-purple-face]

        [:h1.headline
          "Company digest for growing and distributed teams."]
        [:div.subheadline
          (str
            "Key updates and announcements get lost in fast-moving chat and stuffed inboxes. "
            "Carrot makes it simple to stay aligned around what matters most.")]
        ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
        [:div.get-started-button-container
          [:button.mlb-reset.get-started-button
            {:id "get-started-centred-bt"}
            "Get started for free"]]
        [:div.subheadline-2
          "No credit card required  •  Works with Slack"]
        (carrot-box-thanks "carrot-box-thanks-top")
        [:div.carrot-box-container.confirm-thanks.group
          {:style {:display "none"}}
          [:div.carrot-box-thanks
            [:div.thanks-headline "You are Confirmed!"]
            [:div.thanks-subheadline "Thank you for subscribing."]]]

        mobile-horizontal-carousell

        desktop-video

        [:div.core-values-list
          [:div.core-value.key-announcement
            "Key announcements"]
          [:div.core-value.company-updates
            "Company & team updates"]
          [:div.core-value.strategic-plans
            "Strategic plans"]
          [:div.core-value.ideas-discussions
            "Ideas & follow-on discussions"]]]

        shared-misc/carrot-testimonials

        (when-not (jwt/jwt)
          shared-misc/keep-aligned)
      ] ; <!-- .main -->
    ] ;  <!-- #wrap -->

    (site-footer)])
