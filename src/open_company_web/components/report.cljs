(ns open-company-web.components.report
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [handle-change]]
              [open-company-web.components.headcount :refer [headcount]]
              [open-company-web.components.finances :refer [finances]]
              [open-company-web.components.compensation :refer [compensation]]
              [open-company-web.components.currency-picker :refer [currency-picker]]
              [open-company-web.components.link :refer [link]]))

(defcomponent report [data owner]
  (render [_]
    (let [ticker (:ticker data)
          year (:year data)
          period (:period data)
          company-data ((keyword ticker) data)
          report-key (keyword (str "report-" ticker "-" year "-" period))
          report-data (report-key company-data)]
      (dom/div
        (dom/h2 (:name company-data) " Report for " year " " period)
        (cond
          (:loading data)
          (dom/div nil "Loading")

          (and (contains? data (keyword ticker)) (contains? company-data report-key))
          (dom/div nil
            (om/build currency-picker report-data)
            (om/build headcount (:headcount report-data))
            (om/build finances {
              :finances (:finances report-data)
              :currency (:currency report-data)})
            (om/build compensation {
              :compensation (:compensation report-data)
              :headcount (:headcount report-data)
              :currency (:currency report-data)}))

          :else
          (dom/div nil "Report not found"))))))
