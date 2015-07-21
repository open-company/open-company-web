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
          company-data (get data (keyword ticker))
          report-key (str "report-" ticker "-" year "-" period)
          report-data (get company-data report-key)]
      (dom/div
        (dom/h2 (get company-data "name") " Dashboard")
        (cond
          (:loading data)
          (dom/div nil "Loading")

          (not (:loading data))
          (dom/div nil
            (om/build currency-picker report-data)
            (om/build headcount (get report-data "headcount"))
            (om/build finances {
              "finances" (get report-data "finances")
              "currency" (get report-data "currency")})
            (om/build compensation {
              "compensation" (get report-data "compensation")
              "headcount" (get report-data "headcount")
              "currency" (get report-data "currency")}))

          :else
          (dom/div nil "Report not found"))))))
