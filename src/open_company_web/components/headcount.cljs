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
    :values [["Founders" (get data "founders")]
            ["Executives" (get data "executives")]
            ["Full-time employees" (get data "ft-employees")]
            ["Part-time employees" (get data "pt-employees")]
            ["Full-time contractors" (get data "ft-contractors")]
            ["Part-time contractors" (get data "pt-contractors")]]})

(defcomponent headcount [data owner]
  (render [_]
    (let [founders (get data "founders")
          executives (get data "executives")
          ft-employees (get data "ft-employees")
          ft-contractors (get data "ft-contractors")
          pt-employees (get data "pt-employees")
          pt-contractors (get data "pt-contractors")
          comment (get data "comment")
          total-headcount (+ founders executives ft-employees ft-contractors pt-employees pt-contractors)
          full-time-equivalent (+ founders executives ft-employees ft-contractors (/ pt-employees 2) (/ pt-contractors 2))]
      (dom/div {:class "report-list headcount clearfix"}
        (dom/div {:class "report-list-left"}
          (dom/h3 "Headcount:")
          (om/build report-editable-line {:cursor data :key "founders" :label "founder"})
          (om/build report-editable-line {:cursor data :key "executives" :label "executive"})
          (om/build report-editable-line {:cursor data :key "ft-employees" :label "full-time employee"})
          (om/build report-editable-line {:cursor data :key "ft-contractors" :label "part-time employee"})
          (om/build report-editable-line {:cursor data :key "pt-employees" :label "full-time contractor"})
          (om/build report-editable-line {:cursor data :key "pt-contractors" :label "part-time contractor"})
          (dom/div
            (om/build report-line {:number total-headcount :label "total" :pluralize false})
            (dom/span ", ")
            (om/build report-line {:number full-time-equivalent :label "full-time equivalent"}))
          (om/build comment-component {:value comment}))
        (om/build pie-chart (get-chart-data data))))))
