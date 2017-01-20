(ns open-company-web.components.edit-user-profile
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]))

(defcomponent edit-user-profile [data owner]
  (render [_]
    (dom/div {:class "edit-user-profile fullscreen-page"}
      (back-to-dashboard-btn {:title "User Profile"})
      (dom/div {:class "edit-user-profile-internal mx-auto my4"}
        (dom/div {:class "edit-user-profile-content group"}
          (dom/div {:class "left-column"}
            (dom/div {:class "edit-user-profile-name-title data-title"} "NAME")
            (let [real-name (get-in data [:jwt :real-name])
                  first-name (get-in data [:jwt :first-name])
                  last-name (get-in data [:jwt :last-name])]
              (dom/div {:class "edit-user-profile-name"} (or real-name (str first-name " " last-name))))
            (dom/div {:class "edit-user-profile-email-title data-title"} "EMAIL")
            (dom/div {:class "edit-user-profile-email"} (get-in data [:jwt :email])))
          (dom/div {:class "right-column"}
            (when (get-in data [:jwt :avatar])
              (dom/div {:class "edit-user-profile-avatar-title data-title"} "AVATAR"))
            (when (get-in data [:jwt :avatar])
              (dom/img {:class "edit-user-profile-avatar" :src (get-in data [:jwt :avatar])})))))
      (let [columns-num (responsive/columns-num)
            card-width (responsive/calc-card-width)]
        (om/build footer (assoc data :footer-width (responsive/total-layout-width-int card-width columns-num)))))))