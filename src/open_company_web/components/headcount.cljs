(ns open-company-web.components.headcount
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [thousands-separator handle-change]]
              [open-company-web.components.report-line :refer [report-line report-editable-line]]
              [open-company-web.components.comment :refer [comment-component]]
              [open-company-web.components.pie-chart :refer [pie-chart]]
              [om-bootstrap.random :as  r]))

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
          total-headcount (+ founders executives ft-employees ft-contractors pt-employees pt-contractors)
          full-time-equivalent (+ founders executives ft-employees ft-contractors (/ pt-employees 2) (/ pt-contractors 2))]
      (r/well {:class "report-list headcount clearfix"}
        (dom/div {:class "report-list-left"}
          (om/build report-editable-line {:cursor data :key :founders :label "founder"})
          (om/build report-editable-line {:cursor data :key :executives :label "executive"})
          (om/build report-editable-line {:cursor data :key :ft-employees :label "full-time employee"})
          (om/build report-editable-line {:cursor data :key :pt-employees :label "part-time employee"})
          (om/build report-editable-line {:cursor data :key :ft-contractors :label "full-time contractor"})
          (om/build report-editable-line {:cursor data :key :pt-contractors :label "part-time contractor"})
          (dom/div
            (om/build report-line {:number total-headcount :label "total" :pluralize false})
            (dom/span ", ")
            (om/build report-line {:number full-time-equivalent :label "full-time equivalent"}))
          (om/build comment-component {:cursor data :key :comment}))
        (om/build pie-chart (get-chart-data data))))))

(defcomponent readonly-headcount [data owner]
  (render [_]
    (let [founders (:founders data)
          executives (:executives data)
          ft-employees (:ft-employees data)
          ft-contractors (:ft-contractors data)
          pt-employees (:pt-employees data)
          pt-contractors (:pt-contractors data)
          total-headcount (+ founders executives ft-employees ft-contractors pt-employees pt-contractors)
          full-time-equivalent (+ founders executives ft-employees ft-contractors (/ pt-employees 2) (/ pt-contractors 2))]
      (r/well {:class "report-list headcount clearfix"}
        (dom/div {:class "report-list-left"}
          (when (not (= founders nil))
            (dom/div
              (om/build report-line {:number founders :label "founder"})))
          (when (not (= executives nil))
            (dom/div
              (om/build report-line {:number executives :label "executive"})))
          (when (not (= ft-employees nil))
            (dom/div
              (om/build report-line {:number ft-employees :label "full-time employee"})))
          (when (not (= pt-employees nil))
            (dom/div
              (om/build report-line {:number pt-employees :label "part-time employee"})))
          (when (not (= ft-contractors nil))
            (dom/div
              (om/build report-line {:number ft-contractors :label "full-time contractor"})))
          (when (not (= pt-contractors nil))
            (dom/div
              (om/build report-line {:number pt-contractors :label "part-time contractor"})))
          (dom/div
            (om/build report-line {:number total-headcount :label "total" :pluralize false})
            (dom/span ", ")
            (om/build report-line {:number full-time-equivalent :label "full-time equivalent"}))
          (om/build comment-component {:cursor data :key :comment :disabled true}))
        (om/build pie-chart (get-chart-data data))))))
