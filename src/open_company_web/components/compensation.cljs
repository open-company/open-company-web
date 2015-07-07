(ns open-company-web.components.compensation
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.report-line :refer [report-line report-editable-line]]
              [open-company-web.utils :refer [thousands-separator handle-change]]
              [open-company-web.components.comment :refer [comment-component]]
              [goog.string :as gstring]))

(defn switch-values
  "Update all the values in the cursor with the values passed"
  [cursor last-values]
  (handle-change cursor (:founders last-values) :founders)
  (handle-change cursor (:executives last-values) :executives)
  (handle-change cursor (:employee last-values) :employee)
  (handle-change cursor (:contractor last-values) :contractor))

(defn calc-percentage
  [dollar total]
  (let [perc (gstring/format "%.2f" (* (/ dollar total) 100))]
    (js/parseFloat perc)))

(defn dollars->percentage
  "Calculate the percentage given the compensation component cursor"
  [cursor]
  (let [total (+ (:founders cursor) (:executives cursor) (:employee cursor) (:contractor cursor))]
    (handle-change cursor (calc-percentage (:founders cursor) total) :founders)
    (handle-change cursor (calc-percentage (:executives cursor) total) :executives)
    (handle-change cursor (calc-percentage (:employee cursor) total) :employee)
    (handle-change cursor (calc-percentage (:contractor cursor) total) :contractor)))

(defn copy-compensation-state
  "Copy the compensation state into the component state"
  [owner cursor]
  (when (:dollars cursor)
    (om/set-state! owner :initial-values cursor)))

(defcomponent compensation [data owner]
  (will-mount [_]
    (copy-compensation-state owner data))
  (render [_]
    (let [dollars (:dollars data)
          founders (:founders data)
          executives (:executives data)
          employee (:employee data)
          contractor (:contractor data)
          comment (:comment data)
          prefix (if dollars "$" "%")
          total-compensation (gstring/format "%.2f" (+ founders executives employee contractor))]
      (dom/div {:class "report-list compensation"}
        (dom/h3 "Compensation: ")
        (dom/div
          (dom/span {:class "label"} "Report in: "
            (dom/input {
              :type "radio"
              :name "report-type"
              :value "$"
              :id "report-type-$"
              :checked dollars
              :on-click (fn[e] (switch-values data (:initial-values (om/get-state owner)))
                          (handle-change data true :dollars))})
            (dom/label {:class "switch-vis" :for "report-type-$"} " Dollars  ")
            (dom/input {
              :type "radio"
              :name "report-type"
              :value "%"
              :id "report-type-%"
              :checked (not dollars)
              :on-click (fn[e] (handle-change data false :dollars)
                               (copy-compensation-state owner data)
                               (dollars->percentage data))})
            (dom/label {:class "switch-vis" :for "report-type-%"} "  Percent ")))
        (om/build report-editable-line {
          :cursor data
          :key :founders
          :prefix prefix
          :label "founders compensation this month"
          :pluralize false
          :on-change #(when dollars (om/set-state! owner :initial-values data))})
        (om/build report-editable-line {
          :cursor data
          :key :executives
          :prefix prefix
          :label "executives compensation this month"
          :pluralize false
          :on-change #(when dollars (om/set-state! owner :initial-values data))})
        (om/build report-editable-line {
          :cursor data
          :key :employee
          :prefix prefix
          :label "employees compensation this month"
          :pluralize false
          :on-change #(when dollars (om/set-state! owner :initial-values data))})
        (om/build report-editable-line {
          :cursor data
          :key :contractor
          :prefix prefix
          :label "contractors compensation this month"
          :pluralize false
          :on-change #(when dollars (om/set-state! owner :initial-values data))})
        (dom/div
          (om/build report-line {
            :prefix prefix
            :number (thousands-separator total-compensation)
            :label "total compensation this month"}))
        (om/build comment-component {:value comment})))))
