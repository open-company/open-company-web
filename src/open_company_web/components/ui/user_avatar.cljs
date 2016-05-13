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

(defcomponent user-avatar [data owner]
  (render [_]
    (let [slug (router/current-company-slug)]
      (dom/div {:class "user-avatar"}
        (dom/button {:class "dropdown-toggle"
                     :type "button"
                     :id "user-profile-dropdown"
                     :data-toggle "dropdown"
                     :aria-haspopup true}
          (dom/img {:class "user-avatar-img"
                    :src (jwt/get-key :avatar)
                    :title (jwt/get-key :real-name)}))
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