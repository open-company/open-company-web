(ns open-company-web.components.headcount
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.lib.utils :as utils]
              [open-company-web.components.report-line :refer [report-line]]
              [open-company-web.components.comment :refer [comment-component]]
              [open-company-web.components.pie-chart :refer [pie-chart]]
              [om-bootstrap.random :as r]
              [om-bootstrap.panel :as p]))

(defn get-chart-data [data]
  { :columns [["string" "Job"] ["number" "Number"]]
    :values [["Founders" (:founders data)]
            ["Executives" (:executives data)]
            ["Full-time employees" (:ft-employees data)]
            ["Part-time employees" (:pt-employees data)]
            ["Contractors" (:contractors data)]]})

(defcomponent headcount [data owner]
  (will-mount [_]
    (let [founders (if (contains? data :founders) (:founders data) 0)
          executives (if (contains? data :executives) (:executives data) 0)
          ft-employees (if (contains? data :ft-employees) (:ft-employees data) 0)
          pt-employees (if (contains? data :pt-employees) (:pt-employees data) 0)
          contractors (if (contains? data :contractors) (:contractors data) 0)]
      (om/set-state! owner :founders (utils/thousands-separator founders))
      (om/set-state! owner :executives (utils/thousands-separator executives))
      (om/set-state! owner :ft-employees (utils/thousands-separator ft-employees))
      (om/set-state! owner :pt-employees (utils/thousands-separator pt-employees))
      (om/set-state! owner :contractors (utils/thousands-separator contractors))))
  (render [_]
    (let [founders (if (contains? data :founders) (:founders data) 0)
          executives (if (contains? data :executives) (:executives data) 0)
          ft-employees (if (contains? data :ft-employees) (:ft-employees data) 0)
          pt-employees (if (contains? data :pt-employees) (:pt-employees data) 0)
          contractors (if (contains? data :contractors) (:contractors data) 0)
          total (+ founders executives ft-employees pt-employees contractors)]
      (p/panel {:header (dom/h3 "Headcount") :class "headcount clearfix"}
        (dom/div {:class "headcount row"}
          (dom/form {:class "form-horizontal col-md-6"}
            (dom/div {:class "form-group"}
              (dom/label {:for "founders" :class "col-md-4 control-label"} "Founders")
              (dom/div {:class "input-group col-md-2"}
                (dom/input {
                  :type "text"
                  :class "form-control"
                  :value (om/get-state owner :founders)
                  :on-change #(om/set-state! owner :founders (.. % -target -value))
                  :on-focus #(om/set-state! owner :founders founders)
                  :on-blur (fn [e]
                              (utils/handle-change data (utils/String->Number (.. e -target -value)) :founders)
                              (om/set-state! owner :founders (utils/thousands-separator (.. e -target -value)))
                              (utils/save-values "save-report")
                              (.stopPropagation e))}))
              (dom/p {:class "help-block"} "Currently employed founders"))
            (dom/div {:class "form-group"}
              (dom/label {:for "executives" :class "col-md-4 control-label"} "Executives")
              (dom/div {:class "input-group col-md-2"}
                (dom/input {
                  :type "text"
                  :class "form-control"
                  :value (om/get-state owner :executives)
                  :on-change #(om/set-state! owner :executives (.. % -target -value))
                  :on-focus #(om/set-state! owner :executives executives)
                  :on-blur (fn [e]
                              (utils/handle-change data (utils/String->Number (.. e -target -value)) :executives)
                              (om/set-state! owner :executives (utils/thousands-separator (.. e -target -value)))
                              (utils/save-values "save-report")
                              (.stopPropagation e))})))
            (dom/div {:class "form-group"}
              (dom/label {:for "ft-employees" :class "col-md-4 control-label"} "Full-time")
              (dom/div {:class "input-group col-md-2"}
                (dom/input {
                  :type "text"
                  :class "form-control"
                  :value (om/get-state owner :ft-employees)
                  :on-change #(om/set-state! owner :ft-employees (.. % -target -value))
                  :on-focus #(om/set-state! owner :ft-employees ft-employees)
                  :on-blur (fn [e]
                              (utils/handle-change data (utils/String->Number (.. e -target -value)) :ft-employees)
                              (om/set-state! owner :ft-employees (utils/thousands-separator (.. e -target -value)))
                              (utils/save-values "save-report")
                              (.stopPropagation e))})))
            (dom/div {:class "form-group"}
              (dom/label {:for "pt-employees" :class "col-md-4 control-label"} "Part-time")
              (dom/div {:class "input-group col-md-2"}
                (dom/input {
                  :type "text"
                  :class "form-control"
                  :value (om/get-state owner :pt-employees)
                  :on-change #(om/set-state! owner :pt-employees (.. % -target -value))
                  :on-focus #(om/set-state! owner :pt-employees pt-employees)
                  :on-blur (fn [e]
                              (utils/handle-change data (utils/String->Number (.. e -target -value)) :pt-employees)
                              (om/set-state! owner :pt-employees (utils/thousands-separator (.. e -target -value)))
                              (utils/save-values "save-report")
                              (.stopPropagation e))})))
            (dom/div {:class "form-group"}
              (dom/label {:for "contractors" :class "col-md-4 control-label"} "Contractors")
              (dom/div {:class "input-group col-md-2"}
                (dom/input {
                  :type "text"
                  :class "form-control"
                  :value (om/get-state owner :contractors)
                  :on-change #(om/set-state! owner :contractors (.. % -target -value))
                  :on-focus #(om/set-state! owner :contractors contractors)
                  :on-blur (fn [e]
                              (utils/handle-change data (utils/String->Number (.. e -target -value)) :contractors)
                              (om/set-state! owner :contractors (utils/thousands-separator (.. e -target -value)))
                              (utils/save-values "save-report")
                              (.stopPropagation e))}))
              (dom/p {:class "help-block"} "People classified as contractors"))
            (dom/div {:class "form-group"}
              (dom/label {:class "col-md-4 control-label"} "Total")
              (dom/label {:class "col-md-1 control-label"} (utils/thousands-separator total))))
          (dom/div {:class "col-sm-5"}
            (om/build pie-chart (get-chart-data data))))
        (dom/div {:class "row"}
          (dom/div {:class "col-md-1"})
          (dom/textarea {
            :class "col-md-10"
            :rows "5"
            :value (:comment data)
            :on-change #(utils/handle-change data (.. % -target -value) :comment)
            :on-blur (fn [e]
                      (utils/save-values "save-report")
                      (.stopPropagation e))
            :placeholder "Comments: explain any recent additions or deductions in headcount, expected short-term hiring plans, important skills gaps and recruiting efforts"})
          (dom/div {:class "col-md-1"}))))))

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
          (when (not (nil? contractors))
            (dom/div
              (om/build report-line {:number contractors :label "contractor"})))
          (dom/div
            (om/build report-line {:number total-headcount :label "total" :pluralize false}))
          (om/build comment-component {:cursor data :key :comment :disabled true}))
        (om/build pie-chart (get-chart-data data))))))