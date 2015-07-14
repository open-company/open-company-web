(ns open-company-web.components.list-companies
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.link :refer [link]]))

(defcomponent list-page-item [data owner]
  (render [_]
    (dom/li
      (om/build link {:href (str "/edit/" (:id data)) :name (:name data)}))))

(defcomponent list-companies [data owner]
  (render [_]
    (dom/div
      (dom/h1 "Companies:")
      (dom/ul
        (om/build-all list-page-item (vals data))))))
