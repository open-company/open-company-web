(ns open-company-web.components.user-profile
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]))

(defcomponent user-profile [data owner]
  (render [_]
    (dom/div {:class "user-profile fullscreen-page"}
      (back-to-dashboard-btn {:title "User Profile"})
      (dom/div {:class "user-profile-internal mx-auto my4"}
        (dom/div {:class "user-profile-content group"}
          (dom/div {:class "left-column"}
            (dom/div {:class "user-profile-name-title data-title"} "FIRST NAME")
            (dom/div {:class "user-profile-name"} (get-in data [:jwt :first-name]))
            (dom/div {:class "user-profile-name-title data-title"} "LAST NAME")
            (dom/div {:class "user-profile-name"} (get-in data [:jwt :last-name]))
            (dom/div {:class "user-profile-org-title data-title"} "SLACK ORGANIZATION")
            (dom/div {:class "user-profile-org"} (get-in data [:jwt :org-name]))
            (dom/div {:class "user-profile-email-title data-title"} "EMAIL")
            (dom/div {:class "user-profile-email"} (get-in data [:jwt :email])))
          (dom/div {:class "right-column"}
            (when (get-in data [:jwt :avatar])
              (dom/div {:class "user-profile-avatar-title data-title"} "AVATAR"))
            (when (get-in data [:jwt :avatar])
              (dom/img {:class "user-profile-avatar" :src (get-in data [:jwt :avatar])}))))
        (dom/div {:class "user-profile-disclaimer"}
          "User information is from your Slack account."
          (dom/div {:class "right"}
            (dom/button {:class "btn-reset btn-link"
                         :on-click #(dis/dispatch! [:login-with-slack])} "Refresh"))))
      (let [columns-num (responsive/columns-num)
            card-width (responsive/calc-card-width)]
        (om/build footer (assoc data :footer-width (responsive/total-layout-width-int card-width columns-num)))))))