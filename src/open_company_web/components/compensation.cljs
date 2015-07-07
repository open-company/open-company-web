(ns open-company-web.components.compensation
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.report-line :refer [report-line report-editable-line]]
              [open-company-web.utils :refer [thousands-separator handle-change get-symbols-for-currency-code]]
              [open-company-web.components.comment :refer [comment-component]]
              [goog.string :as gstring]))

(defn switch-values
  "Update all the values in the cursor with the values passed"
  [cursor last-values]
  (handle-change cursor (:founders last-values) :founders)
  (handle-change cursor (:executives last-values) :executives)
  (handle-change cursor (:employees last-values) :employees)
  (handle-change cursor (:contractors last-values) :contractors))

(defn calc-percentage
  [dollar total]
  (let [perc (gstring/format "%.2f" (* (/ dollar total) 100))]
    (js/parseFloat perc)))

(defn dollars->percentage
  "Calculate the percentage given the compensation component cursor"
  [cursor]
  (let [total (+ (:founders cursor) (:executives cursor) (:employees cursor) (:contractors cursor))]
    (handle-change cursor (calc-percentage (:founders cursor) total) :founders)
    (handle-change cursor (calc-percentage (:executives cursor) total) :executives)
    (handle-change cursor (calc-percentage (:employees cursor) total) :employees)
    (handle-change cursor (calc-percentage (:contractors cursor) total) :contractors)))

(defn copy-compensation-state
  "Copy the compensation state into the component state"
  [owner cursor]
  (when (not (:percentage cursor))
    (om/set-state! owner :initial-values cursor)))

(defcomponent compensation [data owner]
  (will-mount [_]
    (copy-compensation-state owner (:compensation data)))
  (render [_]
    (let [comp-data (:compensation data)
          percentage (:percentage comp-data)
          founders (:founders comp-data)
          executives (:executives comp-data)
          employees (:employees comp-data)
          contractors (:contractors comp-data)
          total-compensation (gstring/format "%.2f" (+ founders executives employees contractors))
          currency (first (:currency data))
          currency-symbol (get-symbols-for-currency-code currency)
          prefix (if percentage "%" currency-symbol)
          comment (:comment comp-data)]
      (dom/div {:class "report-list compensation"}
        (dom/h3 "Compensation: ")
        (dom/div
          (dom/span {:class "label"} "Report in: "
            (dom/input {
              :type "radio"
              :name "report-type"
              :value currency
              :id "report-type-$"
              :checked (not percentage)
              :on-click (fn[e] (switch-values comp-data (:initial-values (om/get-state owner)))
                          (handle-change comp-data false :percentage))})
            (dom/label {:class "switch-vis" :for "report-type-$"} (str " " currency-symbol "  "))
            (dom/input {
              :type "radio"
              :name "report-type"
              :value "%"
              :id "report-type-%"
              :checked percentage
              :on-click (fn[e] (handle-change comp-data true :percentage)
                               (copy-compensation-state owner comp-data)
                               (dollars->percentage comp-data))})
            (dom/label {:class "switch-vis" :for "report-type-%"} "  Percent ")))
        (om/build report-editable-line {
          :cursor comp-data
          :key :founders
          :prefix prefix
          :label "founders compensation this month"
          :pluralize false
          :on-change #(when (not percentage) (om/set-state! owner :initial-values comp-data))})
        (om/build report-editable-line {
          :cursor comp-data
          :key :executives
          :prefix prefix
          :label "executives compensation this month"
          :pluralize false
          :on-change #(when (not percentage) (om/set-state! owner :initial-values comp-data))})
        (om/build report-editable-line {
          :cursor comp-data
          :key :employees
          :prefix prefix
          :label "employees compensation this month"
          :pluralize false
          :on-change #(when (not percentage) (om/set-state! owner :initial-values comp-data))})
        (om/build report-editable-line {
          :cursor comp-data
          :key :contractors
          :prefix prefix
          :label "contractors compensation this month"
          :pluralize false
          :on-change #(when (not percentage) (om/set-state! owner :initial-values comp-data))})
        (dom/div
          (om/build report-line {
            :prefix prefix
            :number (thousands-separator total-compensation)
            :label "total compensation this month"}))
        (om/build comment-component {:value comment})))))
