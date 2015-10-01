(ns open-company-web.components.update-footer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defcomponent update-footer [data owner]
  (render [_]
    (dom/div {:class "update-footer"}
      (dom/a {:href (str "/companies/" (:slug @router/path) "/" (name (:section data)))}
        (dom/p {:class "timeago"} (utils/time-since (:updated-at data)))
        (dom/img {:src (:image (:author data)) :title (:name (:author data)) :class "author-image"})))))