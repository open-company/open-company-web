(ns open-company-web.components.ui.link
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.router :as router]))

(defcomponent link [data owner]
  (render [_]
    (dom/a #js {
      :href (:href data)
      :className (:class data)
      :onClick #(do
                (-> % .preventDefault)
                (router/nav! (:href data)))
      } (:name data))))