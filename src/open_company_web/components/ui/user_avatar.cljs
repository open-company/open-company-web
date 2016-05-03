(ns open-company-web.components.ui.user-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defcomponent user-avatar [data owner]
  (render [_]
    (dom/div {:class "user-avatar"}
      (dom/a {:href "/profile"
              :on-click (fn [e]
                          (.preventDefault e)
                          (router/nav! "/profile"))}
        (dom/img {:class "user-avatar-img"
                  :src (jwt/get-key :avatar)
                  :title (jwt/get-key :real-name)})))))