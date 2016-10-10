(ns open-company-web.components.ui.user-avatar
  (:require [rum.core :as rum]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.dispatcher :as dis]
            [open-company-web.components.ui.icon :as i]))

(rum/defc user-avatar < rum/static
                        rum/reactive
  [on-click]
  (let [has-avatar (not (clojure.string/blank? (get-in (rum/react dis/app-state) [:jwt :avatar])))]
    [:div.user-avatar
      [:button
        {:type "button"
         :class (str "user-avatar-button" (when has-avatar " no-image"))
         :id "user-profile-dropdown"
         :on-click #(on-click %)}
        (if-not has-avatar
          [:div.user-avatar-name.left (or (get-in (rum/react dis/app-state) [:jwt :first-name]) (get-in (rum/react dis/app-state) [:jwt :last-name]))]
          [:img.user-avatar-img
            {:src (jwt/get-key :avatar)
             :title (get-in (rum/react dis/app-state) [:jwt :real-name])}])
        [:i.fa.fa-ellipsis-v.right]]]))