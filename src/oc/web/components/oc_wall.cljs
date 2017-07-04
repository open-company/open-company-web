(ns oc.web.components.oc-wall
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.cookies :as cook]
            [oc.web.urls :as oc-urls]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defc oc-wall < rum/reactive
  [message button-type]
  [:div.oc-wall
    (back-to-dashboard-btn)
    (login-overlays-handler)
    [:div.oc-wall-message.center.mx-auto.max-width-3
      [:div
        [:div.oc-wall-cta
          message]
        [:button.btn-reset.btn-solid.mt2
          {:on-click #(case button-type
                        :logout
                        (router/redirect! oc-urls/logout)
                        :signup
                        (dis/dispatch! [:login-overlay-show :signup-with-email])
                        ;; else
                        (dis/dispatch! [:login-overlay-show :login-with-email]))}
          (case button-type
            :logout
            "LOG OUT"
            :signup
            "SIGN UP"
            ;; else
            "SIGN IN")]]
      [:div.logo-container
        [:img.logo-gold {:src (utils/cdn "/img/oc-logo-gold.svg")}]
        [:div.logo-cta "Build team alignment together."]]]
    (footer)])