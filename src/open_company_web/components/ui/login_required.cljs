(ns open-company-web.components.ui.login-required
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.components.ui.login-button :as login]))

(defcomponent login-required [{:keys [welcome] :as data} owner]
  (render [_]
    (dom/div {:class "max-width-3 p4 mx-auto center mb4"}
      (if (jwt/jwt)
        (dom/p {:class "mb2"}
          "Sorry, you don't have access to this company's dashboard."
          (dom/br)
          (dom/br)
          (dom/small
              (dom/a {:href "#"
                      :on-click #(login/login! (:extended-scopes-url (:auth-settings data)) %)}
               "Sign in with a different Slack account ")
            "to access different dashboards."))
        (dom/div
          (dom/p {:class "mb2"}
            (if welcome
              "OpenCompany, See the big picture"
              "Please log in to view this dashboard."))
          (om/build login/login-button data))))))