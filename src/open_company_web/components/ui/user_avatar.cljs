(ns open-company-web.components.ui.user-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(def df-user-avatar-size 30)

(defcomponent user-avatar [data owner]
  (render [_]
    (let [av-size (or (:size data) df-user-avatar-size)
          px-size (utils/px av-size)
          bd-radius (utils/px (int (/ av-size 2)))]
      (dom/div {:class "user-avatar"}
        (dom/a {:href "/profile"
                :on-click (fn [e]
                            (.preventDefault e)
                            (router/nav! "/profile"))}
          (dom/img {:class "user-avatar-img"
                    :style {:width px-size
                            :height px-size
                            :border-radius bd-radius}
                    :src (jwt/get-key :avatar)
                    :title (jwt/get-key :real-name)})
          (dom/span {:class "user-avatar-name"} (jwt/get-key :name)))))))