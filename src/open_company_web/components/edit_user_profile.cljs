(ns open-company-web.components.edit-user-profile
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [open-company-web.components.ui.user-avatar :refer (user-avatar-image)]))

(defcomponent edit-user-profile [data owner]
  (render [_]
    (dom/div {:class "edit-user-profile fullscreen-page"}
      (back-to-dashboard-btn {:title "User Profile"})
      (dom/div {:class "edit-user-profile-internal mx-auto my4"}
        (dom/div {:class "edit-user-profile-content group"}
          (dom/div {:class "left-column"}
            (dom/div {:class "edit-user-profile-title data-title"} "FIRST NAME")
            (dom/input {:class "edit-user-profile"
                        :value (or (get-in data [:jwt :first-name]) "")})
            (dom/div {:class "edit-user-profile-title data-title"} "LAST NAME")
            (dom/input {:class "edit-user-profile"
                        :value (or (get-in data [:jwt :last-name]) "")})
            (dom/div {:class "edit-user-profile-title data-title"} "EMAIL")
            (dom/input {:class "edit-user-profile"
                        :value (or (get-in data [:jwt :email]) "")}))
          (dom/div {:class "right-column"}
            (dom/div {:class "edit-user-profile-title data-title"} "AVATAR")
            (user-avatar-image))))
      (let [columns-num (responsive/columns-num)
            card-width (responsive/calc-card-width)]
        (om/build footer (assoc data :footer-width (responsive/total-layout-width-int card-width columns-num)))))))