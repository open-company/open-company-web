(ns open-company-web.components.ui.login-required
  (:require [rum.core :as rum]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.components.ui.login-button :refer (login-button)]
            [open-company-web.components.ui.back-to-dashboard-btn :as btd]
            [open-company-web.components.ui.login-overlay :refer (login-overlays-handler)]))

(rum/defc login-required < rum/static
  [{:keys [welcome] :as data}]
  [:div.login-required
   (login-overlays-handler (rum/react dis/app-state))
   (when-not welcome
     (btd/back-to-dashboard-btn {:button-cta "OPENCOMPANY.COM" :click-cb #(router/redirect! oc-urls/home)}))
   [:div.welcome.center.mx-auto.max-width-3
    [:div
     [:div.login-required-cta
      (cond (jwt/jwt)
            [:div
              "Sorry, you don't have access to this company dashboard."
              [:br]
              "Please sign in with a different account."]
            welcome
            "OpenCompany, See the Big Picture"
            :else
            "Please sign in to access this company dashboard.")]
     (login-button)]
    [:div.logo-container
     [:img.logo-gold {:src "/img/oc-logo-gold.svg"}]
     [:div.logo-cta "OpenCompany makes it easy to see the big picture. Companies are strongest when everyone knows what matters most."]]]])