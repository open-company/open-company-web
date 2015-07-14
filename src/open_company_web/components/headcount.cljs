(ns open-company-web.components.headcount
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [thousands-separator handle-change]]
              [open-company-web.components.report-line :refer [report-line report-editable-line]]
              [open-company-web.components.comment :refer [comment-component]]
              [open-company-web.components.pie-chart :refer [pie-chart]]))

(defn get-chart-data [data]
  { :columns [["string" "Job"] ["number" "Number"]]
    :values [["Founders" (:founders data)]
            ["Executives" (:executives data)]
            ["Full-time employees" (:ft-employees data)]
            ["Part-time employees" (:pt-employees data)]
            ["Full-time contractors" (:ft-contractors data)]
            ["Part-time contractors" (:pt-contractors data)]]})

(defcomponent headcount [data owner]
  (render [_]
    (let [founders (:founders data)
          executives (:executives data)
          ft-employees (:ft-employees data)
          ft-contractors (:ft-contractors data)
          pt-employees (:pt-employees data)
          pt-contractors (:pt-contractors data)
          comment (:comment data)
          total-headcount (+ founders executives ft-employees ft-contractors pt-employees pt-contractors)
          full-time-equivalent (+ founders executives ft-employees ft-contractors (/ pt-employees 2) (/ pt-contractors 2))]
      (dom/div {:class "report-list headcount clearfix"}
        (dom/div {:class "report-list-left"}
          (dom/h3 "Headcount:")
          (om/build report-editable-line {:cursor data :key :founders :label "founder"})
          (om/build report-editable-line {:cursor data :key :executives :label "executive"})
          (om/build report-editable-line {:cursor data :key :ft-employees :label "full-time employee"})
          (om/build report-editable-line {:cursor data :key :pt-employees :label "part-time employee"})
          (om/build report-editable-line {:cursor data :key :ft-contractors :label "full-time contractor"})
          (om/build report-editable-line {:cursor data :key :pt-contractors :label "part-time contractor"})
          (dom/div
            (om/build report-line {:number (thousands-separator total-headcount) :label "total headcount"})", "
            (om/build report-line {:number (thousands-separator full-time-equivalent) :label "full-time equivalent"}))
          (om/build comment-component {:value comment}))
        (om/build pie-chart (get-chart-data data))))))
