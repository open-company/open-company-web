(ns ^:figwheel-always open-company-web.components.page
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [handle-change]]
              [open-company-web.components.headcount :refer [headcount]]
              [open-company-web.components.finances :refer [finances]]
              [open-company-web.components.compensation :refer [compensation]]
              [open-company-web.components.currency-picker :refer [currency-picker]]))

(enable-console-print!)

(defcomponent page [data owner]
  (render [_]
    (dom/div
      (dom/h2 "Dashboard")
      (om/build currency-picker data)
      (om/build headcount (:headcount data))
      (om/build finances {:finances (:finances data) :currency (:currency data)})
      (om/build compensation {:compensation (:compensation data) :headcount (:headcount data) :currency (:currency data)}))))
