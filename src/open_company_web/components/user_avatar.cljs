(ns open-company-web.components.user-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.router :as router]))

(defn get-px [n]
  (str n "px"))

(defcomponent user-avatar [data owner]
  (render [_]
    (let [width (if (:width data)
                  (:width data)
                  30)
          height (if (:height data)
                   (:height data)
                   30)
          bd-radius (int (/ width 2))]
      (dom/div {:class "user-avatar"}
        (dom/a {:href "/profile"
                :on-click (fn [e]
                            (.preventDefault e)
                            (router/nav! "/profile"))}
          (dom/img {:class "user-avatar-img"
                    :style {:width (get-px width)
                            :height (get-px height)
                            :border-radius (get-px bd-radius)}
                    :src (jwt/get-key :avatar)
                    :title (jwt/get-key :real-name)})
          (dom/span {:class "user-avatar-name"} (jwt/get-key :name)))))))