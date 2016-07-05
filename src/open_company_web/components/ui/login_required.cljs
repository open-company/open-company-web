(ns open-company-web.components.ui.login-required
  (:require [rum.core :as rum]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.components.ui.login-button :as login]
            [open-company-web.components.ui.back-to-dashboard-btn :as btd]))

(rum/defc login-required [{:keys [welcome] :as data}]
  [:div.login-required
   (when-not welcome
     (btd/back-to-dashboard-btn {:button-cta "OPENCOMPANY.COM" :click-cb #(router/redirect! oc-urls/home)}))
   [:div.welcome.center.mx-auto.max-width-3
    [:div
     [:p.login-required-cta
      (cond (jwt/jwt)
            "Sorry, you don't have access to this company dashboard. Please sign in with a different Slack account."
            welcome
            "OpenCompany, See the Big Picture"
            (= (:access data) "denied")
            [:div.login-error-message
             "OpenCompany requires verification with your Slack team. Please allow access."]
            (:access data)
            [:div.login-error-message
             "There is a temporary error validating with Slack. Please try again later."]
            :else
            "Please sign in to access this company dashboard.")]
     [:a {:href "#"
          :class "login-button"
          :on-click #(login/login! (:extended-scopes-url (:auth-settings data)) %)}
      [:img {:src "https://api.slack.com/img/sign_in_with_slack.png"}]]]
    [:div.logo-container
     [:img.logo-gold {:src "/img/oc-logo-gold.svg"}]
     [:div.logo-cta "OpenCompany makes it easy to see the big picture. Companies are strongest when everyone knows what matters most."]]]])