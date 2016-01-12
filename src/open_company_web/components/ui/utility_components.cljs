(ns open-company-web.components.ui.utility-components
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defcomponent editable-pen [data owner]
  (render [_]
    (let [slug (:slug @router/path)]
      (dom/i {:class "fa fa-pencil editable-pen"
              :on-click (:click-callback data)}))))

(defcomponent add-metric [data owner]
  (render [_]
    (let [slug (:slug @router/path)]
      (dom/i {:class (utils/class-set {:fa true
                                       :fa-plus-circle true
                                       :add-metric true
                                       :no-links (< (:count-metrics data) 2)})
              :on-click (:click-callback data)}))))