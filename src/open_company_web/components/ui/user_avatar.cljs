(ns open-company-web.components.ui.user-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.router :as router]
            [open-company-web.urls :as oc-urls]
            [om-bootstrap.button :as b]))

(def df-user-avatar-size 30)

(defcomponent user-avatar [data owner]
  (render [_]
    (let [slug (router/current-company-slug)
          av-size (or (:size data) df-user-avatar-size)
          px-size (utils/px av-size)
          bd-radius (utils/px (int (/ av-size 2)))]
      (dom/div {:class "user-avatar dropdown"}
        (dom/button {:class "dropdown-toggle"
                     :type "button"
                     :id "user-profile-dropdown"
                     :data-toggle "dropdown"
                     :aria-haspopup true}
          (dom/img {:class "user-avatar-img"
                    :style {:width px-size
                            :height px-size
                            :border-radius bd-radius}
                    :src (jwt/get-key :avatar)
                    :title (jwt/get-key :real-name)})
          (dom/span {:class "user-avatar-name"} (jwt/get-key :name)))
        (dom/ul {:class "dropdown-menu"
                 :aria-labelledby "user-profile-dropdown"}
          (dom/li {}
            (dom/a {:href oc-urls/user-profile
                    :on-click (fn [e]
                               (.preventDefault e)
                               (.stopPropagation e)
                               (router/nav! oc-urls/user-profile))} "User profile"))
          (when slug
            (dom/li {}
              (dom/a {:href (oc-urls/company-profile)
                      :on-click (fn [e]
                                 (.preventDefault e)
                                 (.stopPropagation e)
                                 (router/nav! (oc-urls/company-profile)))} "Company profile")))
          (dom/li {}
            (dom/a {:href oc-urls/home
                    :on-click (fn [e]
                                (.preventDefault e)
                                (.stopPropagation e)
                                (cook/remove-cookie! :jwt)
                                (router/redirect! oc-urls/home))} "Logout")))))))