(ns open-company-web.components.link
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [secretary.core :as secretary]
              [open-company-web.router :as router]))

(defcomponent link [data owner]
  (render [_]
    (dom/a #js {
      :href (:href data)
      :onClick #(do
                (-> % .preventDefault)
                (router/nav! (:href data)))
      } (:name data))))
