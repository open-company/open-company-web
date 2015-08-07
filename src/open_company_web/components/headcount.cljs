(ns open-company-web.components.headcount
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.lib.utils :as utils]
              [open-company-web.components.report-line :refer [report-line]]
              [open-company-web.components.comment :refer [comment-component comment-readonly-component]]
              [open-company-web.components.pie-chart :refer [pie-chart]]
              [open-company-web.components.report.headcount-section :refer [headcount-section]]
              [om-bootstrap.random :as r]
              [om-bootstrap.panel :as p]))

(defn get-chart-data [data]
  { :columns [["string" "Job"] ["number" "Number"]]
    :values [["Founders" (:founders data)]
            ["Executives" (:executives data)]
            ["Full-time employees" (:ft-employees data)]
            ["Part-time employees" (:pt-employees data)]
            ["Contractors" (:contractors data)]]})

(def headcount-rows [
  {:key-name :founders :label "Founders" :help-block "Currently employed founders"}
  {:key-name :executives :label "Executives" :help-block ""}
  {:key-name :ft-employees :label "Full-time" :help-block ""}
  {:key-name :pt-employees :label "Part-time" :help-block ""}
  {:key-name :contractors :label "Contractors" :help-block "People classified as contractors"}])

(defcomponent headcount [data owner]
  (render [_]
    (let [founders (if (contains? data :founders) (:founders data) 0)
          executives (if (contains? data :executives) (:executives data) 0)
          ft-employees (if (contains? data :ft-employees) (:ft-employees data) 0)
          pt-employees (if (contains? data :pt-employees) (:pt-employees data) 0)
          contractors (if (contains? data :contractors) (:contractors data) 0)
          total (+ founders executives ft-employees pt-employees contractors)]
      (p/panel {:header (dom/h3 "Headcount") :class "headcount clearfix"}
        (dom/div {:class "headcount row"}

          ;; Form
          (dom/form {:class "form-horizontal col-md-6"}
            ;; Build all rows
            (for [section headcount-rows]
              (om/build headcount-section (merge section {:cursor data})))

            (dom/div {:class "form-group"}
              (dom/label {:class "col-md-4 control-label"} "Total")
              (dom/label {:class "col-md-1 control-label"} (utils/thousands-separator total))))

          (dom/div {:class "col-sm-5"}
            (om/build pie-chart (get-chart-data data))))

        (om/build comment-component {
          :cursor data
          :placeholder "Comments: explain any recent additions or deductions in headcount, expected short-term hiring plans, important skills gaps and recruiting efforts"
          })))))

(defcomponent readonly-headcount [data owner]
  (render [_]
    (let [founders (:founders data)
          executives (:executives data)
          ft-employees (:ft-employees data)
          pt-employees (:pt-employees data)
          contractors (:contractors data)
          total-headcount (+ founders executives ft-employees pt-employees contractors)]
      (r/well {:class "report-list headcount clearfix"}
        (dom/div {:class "report-list-left"}
          (let [sections [{:number founders :label "founder"}
                          {:number executives :label "executive"}
                          {:number ft-employees :label "full-time employee"}
                          {:number pt-employees :label "part-time employee"}
                          {:number contractors :label "contractor"}]]
            (for [section sections]
              (when (:value section)
                (dom/div
                  (om/build report-line section)))))
          (dom/div
            (om/build report-line {:number total-headcount :label "total" :pluralize false}))
          (om/build comment-readonly-component {:cursor data :key :comment :disabled true}))
        (om/build pie-chart (get-chart-data data))))))
