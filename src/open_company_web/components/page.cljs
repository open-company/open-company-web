(ns ^:figwheel-always open-company-web.components.page
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.headcount :refer [headcount]]
              [open-company-web.components.finances :refer [finances]]
              [open-company-web.components.compensation :refer [compensation]]))

(println "HERE!")

(defcomponent page [data owner]
  (render [owner]
    (dom/div
      (dom/h2 "Dashboard")
      (om/build headcount (:headcount data))
      (om/build finances (:finances data))
      (om/build compensation (:compensation data)))))
