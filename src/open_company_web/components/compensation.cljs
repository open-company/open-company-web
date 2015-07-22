(ns open-company-web.components.compensation
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.report-line :refer [report-line report-editable-line]]
              [open-company-web.utils :refer [thousands-separator handle-change get-symbols-for-currency-code]]
              [open-company-web.components.comment :refer [comment-component]]
              [open-company-web.components.pie-chart :refer [pie-chart]]
              [goog.string :as gstring]
              [om-bootstrap.random :as r]))

(defn get-chart-data [data head-data symbol]
  (let [show-founders (> (:founders head-data) 0)
        show-executives (> (:executives head-data) 0)
        show-employees (> (+ (:ft-employees head-data) (:pt-employees head-data)) 0)
        show-contrators (> (+ (:ft-contractors head-data) (:pt-contractors head-data)) 0)]
    { :symbol symbol
      :columns [["string" "Compensation"] ["number" "Amount"]]
      :values [[:Founders (if show-founders (:founders data) 0)]
              [:Executives (if show-executives (:executives data) 0)]
              [:Employees (if show-employees (:employees data) 0)]
              [:Contractors (if show-contrators (:contractors data) 0)]]}))

(defn calc-percentage
  [dollar total]
  (let [perc (gstring/format "%.2f" (* (/ dollar total) 100))]
    (js/parseFloat perc)))

(defcomponent compensation [data owner]
  (render [_]
    (let [head-data (:headcount data)
          show-founders (> (:founders head-data) 0)
          show-executives (> (:executives head-data) 0)
          show-employees (> (+ (:ft-employees head-data) (:pt-employees head-data)) 0)
          show-contrators (> (+ (:ft-contractors head-data) (:pt-contractors head-data)) 0)
          comp-data (:compensation data)
          percentage (:percentage comp-data)
          founders (if show-founders (:founders comp-data) 0)
          executives (if show-executives (:executives comp-data) 0)
          employees (if show-employees (:employees comp-data) 0)
          contractors (if show-contrators (:contractors comp-data) 0)
          currency (:currency data)
          currency-symbol (get-symbols-for-currency-code currency)
          prefix (str currency-symbol " ")
          comment (:comment comp-data)
          founders-label (str "founder" (if (= (:founders head-data) 1) "" "s") " comp. this month")
          executives-label (str "executive" (if (= (:executives head-data) 1) "" "s") " comp. this month")
          employees-label (str "employee" (if (= (:employees head-data) 1) "" "s") " comp. this month")
          contractors-label (str "contractor" (if (= (:contractors head-data) 1) "" "s") " comp. this month")
          total-compensation 0
          total-compensation (+ (if show-founders founders 0) total-compensation)
          total-compensation (+ (if show-executives executives 0) total-compensation)
          total-compensation (+ (if show-employees employees 0) total-compensation)
          total-compensation (+ (if show-contrators contractors 0) total-compensation)
          total-compensation (gstring/format "%.2f" total-compensation)]
      (r/well {:class "report-list compensation clearfix"}
        (dom/div {:class "report-list-left"}
          (dom/div
            (dom/span {:class "label"} "Show as: "
              (dom/input {
                :type "radio"
                :name "report-type"
                :value currency
                :id "report-type-$"
                :checked (not percentage)
                :on-click #(handle-change comp-data false :percentage)})
              (dom/label {:class "switch-vis" :for "report-type-$"} (str " " currency-symbol "  "))
              (dom/input {
                :type "radio"
                :name "report-type"
                :value "%"
                :id "report-type-%"
                :checked percentage
                :on-click #(handle-change comp-data true :percentage)})
              (dom/label {:class "switch-vis" :for "report-type-%"} "  Percent ")))
          (when show-founders
            (om/build report-editable-line {
              :cursor comp-data
              :key :founders
              :prefix prefix
              :label (str founders-label " ("(calc-percentage (:founders comp-data) total-compensation) "%)")
              :pluralize false}))
          (when show-executives
            (om/build report-editable-line {
              :cursor comp-data
              :key :executives
              :prefix prefix
              :label (str executives-label " ("(calc-percentage (:executives comp-data) total-compensation) "%)")
              :pluralize false}))
          (when show-employees
            (om/build report-editable-line {
              :cursor comp-data
              :key :employees
              :prefix prefix
              :label (str employees-label " ("(calc-percentage (:employees comp-data) total-compensation) "%)")
              :pluralize false}))
          (when show-contrators
            (om/build report-editable-line {
              :cursor comp-data
              :key :contractors
              :prefix prefix
              :label (str contractors-label " ("(calc-percentage (:contractors comp-data) total-compensation) "%)")
              :pluralize false}))
          (dom/div
            (om/build report-line {
              :prefix prefix
              :number (thousands-separator total-compensation)
              :label "total compensation this month"}))
          (om/build comment-component {:value comment}))
        (om/build pie-chart (get-chart-data comp-data head-data prefix))))))
