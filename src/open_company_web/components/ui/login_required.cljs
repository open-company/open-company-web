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
      (dom/div {:class "center mx-auto max-width-3"}
        (if (jwt/jwt)
          (dom/div {}
            (dom/p {:class "login-rquired-cta"}
              "Sorry, you don't have access to this company's dashboard."
              (dom/br)
              (dom/br)
              (dom/small
                  (dom/a {:href "#"
                          :on-click #(login/login! (:extended-scopes-url (:auth-settings data)) %)}
                   "Sign in with a different Slack account ")
                "to access different dashboards.")))
          (dom/div
            (dom/p {:class "login-rquired-cta"}
              (if welcome
                "OpenCompany, See the Big Picture"
                "Please Sign In to Access This Screen"))
            (dom/a {:href "#"
                    :class "login-button"
                    :on-click #(login/login! (:extended-scopes-url (:auth-settings data)) %)}
              (dom/img {:src "https://api.slack.com/img/sign_in_with_slack.png"}))))
        (dom/div {:class "logo-container"}
          (dom/img {:class "logo-gold" :src "/img/oc-logo-gold.svg"})
          (dom/div {:class "logo-cta"} "OpenCompany makes it easy to see the big picture. Companies are stronger when everyone knows what matters most."))))))