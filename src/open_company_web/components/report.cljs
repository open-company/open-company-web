(ns open-company-web.components.report
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [handle-change]]
              [open-company-web.components.headcount :refer [headcount readonly-headcount]]
              [open-company-web.components.finances :refer [finances readonly-finances]]
              [open-company-web.components.compensation :refer [compensation readonly-compensation]]
              [open-company-web.components.currency-picker :refer [currency-picker]]
              [open-company-web.components.link :refer [link]]
              [om-bootstrap.nav :as n]
              [open-company-web.api :refer [save-or-create-report]]))

(defcomponent report [data owner]
  (will-mount [_]
    (om/set-state! owner :selected-tab 1))
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
            (dom/button {
              :on-click #(save-or-create-report ticker year period report-data)
              } "Save!")
            (n/nav {
              :class "tab-navigation"
              :bs-style "tabs"
              :active-key (om/get-state owner :selected-tab)
              :on-select #(om/set-state! owner :selected-tab %) }
              (n/nav-item {:key 1 :href ""} "Headcount")
              (n/nav-item {:key 2 :href ""} "Finances")
              (n/nav-item {:key 3 :href ""} "Compensation"))
            (case (om/get-state owner :selected-tab)
              1 (om/build headcount (:headcount report-data))
              2 (om/build finances {
                  :finances (:finances report-data)
                  :currency (:currency report-data)})
              3 (om/build compensation {
                  :compensation (:compensation report-data)
                  :headcount (:headcount report-data)
                  :currency (:currency report-data)})))

          :else
          (dom/div nil "Report not found"))))))

(defcomponent readonly-report [data owner]
  (will-mount [_]
    (om/set-state! owner :selected-tab 1))
  (render [_]
    (let [ticker (:ticker data)
          year (:year data)
          period (:period data)
          company-data ((keyword ticker) data)
          report-key (keyword (str "report-" ticker "-" year "-" period))
          report-data (report-key company-data)
          headcount (:headcount report-data)]
      (dom/div
        (dom/h2 (:name company-data) " Report for " year " " period)
        (cond
          (:loading data)
          (dom/div nil "Loading")

          (and (contains? data (keyword ticker)) (contains? company-data report-key))
          (dom/div nil
            (n/nav {
              :class "tab-navigation"
              :bs-style "tabs"
              :active-key (om/get-state owner :selected-tab)
              :on-select #(om/set-state! owner :selected-tab %) }
              (n/nav-item {:key 1 :href ""} "Headcount")
              (n/nav-item {:key 2 :href ""} "Finances")
              (n/nav-item {:key 3 :href ""} "Compensation"))
            (case (om/get-state owner :selected-tab)
              1 (om/build readonly-headcount (:headcount report-data))
              2 (om/build readonly-finances {
                  :finances (:finances report-data)
                  :currency (:currency report-data)})
              3 (om/build readonly-compensation {
                  :compensation (:compensation report-data)
                  :headcount (:headcount report-data)
                  :currency (:currency report-data)})))

          :else
          (dom/div nil "Report not found"))))))
