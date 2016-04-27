(ns open-company-web.components.user-profile
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.router :as router]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.cookies :as cook]))

(defcomponent user-profile [data owner]
  (render [_]
    (dom/div {:class "user-profile row"}
      (om/build navbar {})
      (dom/div {:class "navbar-offset container-fluid"}
        (dom/div {:class "col-md-12 main"}
          (dom/div {:class "panel panel-default"}
            (dom/div {:class "panel-heading"}
              (dom/h4 {} "User profile")
              "Your user profile comes from Slack, you can update your profile in Slack.")
            (dom/div {:class "panel-body"}
              (dom/span {:class "user-profile-label"} "Name:")
              (dom/span {:class "user-profile-value"} (jwt/get-key :real-name))
              (dom/br)(dom/br)
              (dom/span {:class "user-profile-label"} "Organization:")
              (dom/span {:class "user-profile-value"} (jwt/get-key :org-name))
              (dom/br)(dom/br)
              (dom/span {:class "user-profile-label"} "Email:")
              (dom/span {:class "user-profile-value"} (jwt/get-key :email))
              (dom/br)(dom/br)
              (dom/span {:class "user-profile-label"} "Avatar:")
              (when (jwt/get-key :avatar)
                (dom/img {:class "user-avatar" :src (jwt/get-key :avatar)}))
              (dom/span {:class "user-profile-value"} (jwt/get-key :avatar))
              (dom/br)(dom/br)
              (dom/button {:class "btn btn-danger"
                           :on-click (fn [e]
                                       (router/history-back!))}
                          "Done"))))))))