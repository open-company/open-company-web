(ns open-company-web.components.report
    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.lib.utils :refer [handle-change add-channel get-channel]]
              [open-company-web.components.headcount :refer [headcount readonly-headcount]]
              [open-company-web.components.finances :refer [finances readonly-finances]]
              [open-company-web.components.compensation :refer [compensation readonly-compensation]]
              [open-company-web.components.currency-picker :refer [currency-picker]]
              [open-company-web.components.link :refer [link]]
              [open-company-web.dispatcher :refer [app-state]]
              [om-bootstrap.nav :as n]
              [open-company-web.api :refer [save-or-create-report]]
              [cljs.core.async :refer [put! chan <!]]))

(defcomponent report [data owner]
  (init-state [_]
    (let [chan (chan)]
      (add-channel "save-report" chan)))
  (will-mount [_]
    (om/set-state! owner :selected-tab 1)
    (let [save-change (get-channel "save-report")]
        (go (loop []
          (let [change (<! save-change)
                symbol (:symbol data)
                year (:year data)
                period (:period data)
                company-data ((keyword symbol) @app-state)
                report-key (keyword (str "report-" symbol "-" year "-" period))
                report-data (report-key company-data)]
            (save-or-create-report symbol year period report-data)
            (recur))))))
  (render [_]
    (let [symbol (:symbol data)
          year (:year data)
          period (:period data)
          company-data ((keyword symbol) data)
          report-key (keyword (str "report-" symbol "-" year "-" period))
          report-data (report-key company-data)]
      (dom/div {:class "report-container"}
        (dom/h2 (:name company-data) " Report for " year " " period)
        (cond
          (:loading data)
          (dom/div nil "Loading")

          (and (contains? data (keyword symbol)) (contains? company-data report-key))
          (dom/div nil
            (om/build currency-picker report-data)
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
    (let [symbol (:symbol data)
          year (:year data)
          period (:period data)
          company-data ((keyword symbol) data)
          report-key (keyword (str "report-" symbol "-" year "-" period))
          report-data (report-key company-data)
          headcount (:headcount report-data)]
      (dom/div
        (dom/h2 (:name company-data) " Report for " year " " period)
        (cond
          (:loading data)
          (dom/div nil "Loading")

          (and (contains? data (keyword symbol)) (contains? company-data report-key))
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
