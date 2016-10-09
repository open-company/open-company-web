(ns open-company-web.components.ui.user-avatar
  (:require [rum.core :as rum]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.components.ui.icon :as i]))

(rum/defc user-avatar < rum/static
  [on-click]
  (let [has-avatar (not (clojure.string/blank? (jwt/get-key :avatar)))]
    [:div.user-avatar
      [:button
        {:type "button"
         :class (str "user-avatar-button" (when has-avatar " no-image"))
         :id "user-profile-dropdown"
         :on-click #(on-click %)}
        (if-not has-avatar
          [:div.user-avatar-name.left (or (jwt/get-key :first-name) (jwt/get-key :last-name))]
          [:img.user-avatar-img
            {:src (jwt/get-key :avatar)
             :title (jwt/get-key :real-name)}])
        [:i.fa.fa-ellipsis-v.right]]]))