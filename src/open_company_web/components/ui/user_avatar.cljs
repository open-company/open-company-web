(ns open-company-web.components.ui.user-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [om-bootstrap.button :as b]))

(defcomponent user-avatar [data owner]
  (render [_]
    (dom/div {:class "user-avatar"}
      (dom/button {:class "user-avatar-button"
                   :type "button"
                   :id "user-profile-dropdown"
                   :on-click #((:menu-click data) %)}
        (dom/img {:class "user-avatar-img"
                  :src (jwt/get-key :avatar)
                  :title (jwt/get-key :real-name)})))))