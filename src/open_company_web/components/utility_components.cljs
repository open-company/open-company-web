(ns open-company-web.components.utility-components
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]))

(defcomponent editable-pen [data owner]
  (render [_]
    (dom/i {:class "fa fa-pencil editable-pen"
            :on-click #(do
                        (-> % .preventDefault)
                        (router/nav! "/finances/edit"))})))