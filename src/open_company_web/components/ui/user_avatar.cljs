(ns open-company-web.components.ui.user-avatar
  (:require [rum.core :as rum]
            [open-company-web.lib.jwt :as jwt]))

(rum/defc user-avatar < rum/static
  [on-click]
  [:div.user-avatar
   [:button.user-avatar-button
    {:type "button"
     :id "user-profile-dropdown"
     :on-click #(on-click %)}
    [:img.user-avatar-img
     {:src (jwt/get-key :avatar)
      :title (jwt/get-key :real-name)}]]])