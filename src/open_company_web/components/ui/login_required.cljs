(ns open-company-web.components.ui.login-required
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.components.ui.login-button :as login]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]))

(defcomponent login-required [{:keys [welcome] :as data} owner]
  (render [_]
    (dom/div {:class "login-required"}
      (when-not welcome
        (om/build back-to-dashboard-btn {:button-cta "OPENCOMPANY.COM"}))
      (dom/div {:class "center mx-auto max-width-3 welcome"}
        (dom/div
          (dom/p {:class "login-rquired-cta"}
            (cond
              (jwt/jwt)
                "Sorry, you don't have access to this company dashboard. Please sign in with a different Slack account."
              welcome
                "OpenCompany, See the Big Picture"
              (= (:access data) "denied")
                (dom/div {:class "login-error-message"}
                  "OpenCompany requires verification with your Slack team. Please allow access.")  
              (:access data)
                (dom/div {:class "login-error-message"}
                  "There is a temporary error validating with Slack. Please try again later.")
              :else
                "Please sign in to access this company dashboard."))
          (dom/a {:href "#"
                  :class "login-button"
                  :on-click #(login/login! (:extended-scopes-url (:auth-settings data)) %)}
            (dom/img {:src "https://api.slack.com/img/sign_in_with_slack.png"})))
        (dom/div {:class "logo-container"}
          (dom/img {:class "logo-gold" :src "/img/oc-logo-gold.svg"})
          (dom/div {:class "logo-cta"} "OpenCompany makes it easy to see the big picture. Companies are strongest when everyone knows what matters most."))))))