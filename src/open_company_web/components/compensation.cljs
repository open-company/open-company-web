(ns open-company-web.components.compensation
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.report-line :refer [report-line report-editable-line]]
              [open-company-web.utils :refer [thousands-separator handle-change]]))

(defcomponent compensation [data owner]
  (render [_]
    (let [dollars (:dollars data)
          founders (:founders data)
          executives (:executives data)
          employee (:employee data)
          contractor (:contractor data)
          comment (:comment data)
          prefix (if dollars "$" "%")
          total-compensation (+ founders executives employee contractor)]
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
              :on-click #(handle-change data true :dollars)})
            (dom/label {:class "switch-vis" :for "report-type-$"} " Dollars  ")
            (dom/input {
              :type "radio"
              :name "report-type"
              :value "%"
              :id "report-type-%"
              :checked (not dollars)
              :on-click #(handle-change data false :dollars)})
            (dom/label {:class "switch-vis" :for "report-type-%"} "  Percent ")))
        (om/build report-editable-line {
          :cursor data
          :key :founders
          :prefix prefix
          :label "founders compensation this month"
          :pluralize false})
        (om/build report-editable-line {
          :cursor data
          :key :executives
          :prefix prefix
          :label "executives compensation this month"
          :pluralize false})
        (om/build report-editable-line {
          :cursor data
          :key :employee
          :prefix prefix
          :label "employees compensation this month"
          :pluralize false})
        (om/build report-editable-line {
          :cursor data
          :key :contractor
          :prefix prefix
          :label "contractors compensation this month"
          :pluralize false})
        (dom/div
          (om/build report-line {
            :prefix prefix
            :number (thousands-separator total-compensation)
            :label "total compensation this month"}))
        (dom/div {:class "comment"} (str "Comment: " comment))))))
