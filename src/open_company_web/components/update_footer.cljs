(ns open-company-web.components.update-footer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defcomponent update-footer [data owner]
  (init-state [_]
    {:hover false})
  (did-mount [_]
    (when (.-$ js/window)
      (.hover (.$ js/window "div.update-footer div.timeago")
              #(om/update-state! owner :hover (fn [_]true))
              #(om/update-state! owner :hover (fn [_]false)))))
  (render [_]
    (dom/div {:class "update-footer"}
      (dom/a {:href (str "/companies/" (:slug @router/path) "/" (name (:section data)))}
        (dom/div {:class "timeago"} (utils/time-since (:updated-at data)))
        (dom/div {:class (utils/class-set {:update-footer-hover true
                                           :show (om/get-state owner :hover)})}
          (dom/img {:src (:image (:author data)) :title (:name (:author data)) :class "author-image"})
            (dom/div {:class "author"} (:name (:author data))))))))