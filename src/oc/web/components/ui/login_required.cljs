(ns oc.web.components.ui.login-required
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.cookies :as cook]
            [oc.web.components.ui.login-button :refer (login-button)]
            [oc.web.components.ui.back-to-dashboard-btn :as btd]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defc login-required < rum/reactive
  [{:keys [welcome] :as data}]
  [:div.login-required
    (when-not welcome
      (btd/back-to-dashboard-btn {:button-cta "CARROT.IO" :click-cb #(router/redirect! oc-urls/home)}))
    [:div.welcome.center.mx-auto.max-width-3
      [:div
        [:div.login-required-cta
        (cond (jwt/jwt)
          [:div
            "Sorry, you don't have access to this company dashboard."
            [:br]
            "Please sign in with a different account."]
          welcome
          "Carrot, See the Big Picture"
          :else
          "Please sign in to access this company dashboard.")]
        (if (jwt/jwt)
          [:button.btn-reset.logout.mt2
            {:on-click #(do
                          (cook/set-cookie! :login-redirect (oc-urls/board))
                          (cook/set-cookie! :show-login-overlay "login-with-slack")
                          (cook/remove-cookie! :jwt)
                          (router/redirect! oc-urls/home))}
            "Log Out"]
            (login-button))]
     [:div.logo-container
       [:img.logo-gold {:src (str ls/cdn-url "/img/oc-logo-gold.svg")}]
       [:div.logo-cta "Carrot makes it easy to see the big picture. Companies are strongest when everyone knows what matters most."]]]])