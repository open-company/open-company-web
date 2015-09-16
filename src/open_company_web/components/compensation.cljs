(ns open-company-web.components.compensation
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.report-line :refer [report-line report-editable-line]]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.comment :refer [comment-component comment-readonly-component]]
            [open-company-web.components.charts :as charts]
            [open-company-web.components.report.compensation-section :refer [compensation-section]]
            [open-company-web.components.report.percentage-switch :refer [percentage-switch]]
            [goog.string :as gstring]
            [om-bootstrap.random :as r]
            [om-bootstrap.panel :as p]))

(defn get-chart-data [data ticker]
  (let [head-data (:headcount data)
        comp-data (:compensation data)
        show-founders (> (:founders head-data) 0)
        show-executives (> (:executives head-data) 0)
        show-employees (> (+ (:ft-employees head-data) (:pt-employees head-data)) 0)
        show-contractors (> (:contractors head-data) 0)
        values (vector)
        values (if show-founders
                 (conj values [:Founders (:founders comp-data)])
                 values)
        values (if show-executives
                 (conj values [:Executives (:executives comp-data)])
                 values)
        values (if show-employees
                 (conj values [:Employees (:employees comp-data)])
                 values)
        values (if show-contractors
                 (conj values [:Contractors (:contractors comp-data)])
                 values)]
    { :prefix ticker
      :columns [["string" "Compensation"] ["number" "Amount"]]
      :values values}))

(defn calc-percentage
  [dollar total]
  (let [perc (gstring/format "%.2f" (* (/ dollar total) 100))]
    (js/parseFloat perc)))

(def compensation-rows [
  {:key-name :founders :label "Founders" :description "Founder cash compensation this quarter"}
  {:key-name :executives :label "Executives" :description "Executives cash compensation this quarter"}
  {:key-name :employees :label "Employees" :description "Employees cash compensation this quarter"}
  {:key-name :contractors :label "Contractors" :description "Cost for contractors this quarter"}])

(defcomponent compensation [data owner]
  (render [_]
    (let [head-data (:headcount data)
          headdata-count (+ (:founders head-data)
                            (:executives head-data)
                            (:ft-employees head-data)
                            (:pt-employees head-data)
                            (:contractors head-data))
          comp-data (:compensation data)
          compensation-count (+ (:founders comp-data)
                                (:executives comp-data)
                                (:employees comp-data)
                                (:contractors comp-data))
          currency (:currency data)
          currency-symbol (utils/get-symbol-for-currency-code currency)
          prefix (str currency-symbol " ")]
      (p/panel {:header (dom/h3 "Compensation") :class "report-panel compensation clearfix"}
        (dom/div {:class "compensation row"}

          ;; Compensation sections
          (dom/form {:class "form-horizontal col-sm-6"}

            ;; Percentage
            (when (> headdata-count 0)
              (om/build percentage-switch data))

            (for [section compensation-rows]
              (om/build compensation-section (merge section {:cursor data}))))
          ;; Pie chart
          (when (> compensation-count 0)
            (dom/div {:class "col-sm-6"}
              (om/build charts/column-chart (get-chart-data data prefix)))))

        ;; Comment
        (om/build comment-component {
          :cursor comp-data
          :placeholder "Comments: explain any recent significant changes in compensation costs"})))))

(defcomponent readonly-compensation [data owner]
  (render [_]
    (let [head-data (:headcount data)
          show-founders (> (:founders head-data) 0)
          show-executives (> (:executives head-data) 0)
          show-employees (> (+ (:ft-employees head-data) (:pt-employees head-data)) 0)
          show-contractors (> (:contractors head-data) 0)
          comp-data (:compensation data)
          percentage (:percentage comp-data)
          founders (if show-founders (:founders comp-data) 0)
          executives (if show-executives (:executives comp-data) 0)
          employees (if show-employees (:employees comp-data) 0)
          contractors (if show-contractors (:contractors comp-data) 0)
          currency (:currency data)
          currency-symbol (utils/get-symbol-for-currency-code currency)
          prefix (str (if percentage "%" currency-symbol) " ")
          founders-label (str "founder" (if (= (:founders head-data) 1) "" "s") " compensation this month")
          executives-label (str "executive" (if (= (:executives head-data) 1) "" "s") " compensation this month")
          employees-label (str "employee" (if (= (:employees head-data) 1) "" "s") " compensation this month")
          contractors-label (str "contractor" (if (= (:contractors head-data) 1) "" "s") " compensation this month")
          total-compensation 0
          total-compensation (+ (if show-founders founders 0) total-compensation)
          total-compensation (+ (if show-executives executives 0) total-compensation)
          total-compensation (+ (if show-employees employees 0) total-compensation)
          total-compensation (+ (if show-contractors contractors 0) total-compensation)
          total-compensation (gstring/format "%.2f" total-compensation)]
      (r/well {:class "report-list compensation clearfix"}
        (dom/div {:class "report-list-left"}

          ;; Report lines
          (let [sections [{:show-section show-founders :number (if percentage (calc-percentage founders total-compensation) founders) :label founders-label}
                          {:show-section show-executives :number (if percentage (calc-percentage executives total-compensation) executives) :label executives-label}
                          {:show-section show-employees :number (if percentage (calc-percentage employees total-compensation) employees) :label employees-label}
                          {:show-section show-contractors :number (if percentage (calc-percentage contractors total-compensation) founders) :label contractors-label}]]
            (for [section sections]
              (when (:show-section section)
                (dom/div
                  (om/build report-line (merge section {:prefix prefix :pluralize false}))))))

          ;; Total compensation
          (dom/div
            (om/build report-line {
              :prefix prefix
              :number (utils/thousands-separator
                        (if percentage (calc-percentage total-compensation total-compensation) total-compensation))
              :label "total compensation this month"}))
          (om/build comment-readonly-component {:cursor comp-data :key :comment :disabled true}))
        (om/build charts/pie-chart (get-chart-data data prefix))))))
