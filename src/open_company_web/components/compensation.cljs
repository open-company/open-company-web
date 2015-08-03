(ns open-company-web.components.compensation
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.report-line :refer [report-line report-editable-line]]
              [open-company-web.lib.utils :as utils]
              [open-company-web.components.comment :refer [comment-component]]
              [open-company-web.components.pie-chart :refer [pie-chart]]
              [goog.string :as gstring]
              [om-bootstrap.random :as r]
              [om-bootstrap.panel :as p]))

(defn get-chart-data [data head-data symbol]
  (let [show-founders (> (:founders head-data) 0)
        show-executives (> (:executives head-data) 0)
        show-employees (> (+ (:ft-employees head-data) (:pt-employees head-data)) 0)
        show-contractors (> (:contractors head-data) 0)]
    { :prefix symbol
      :columns [["string" "Compensation"] ["number" "Amount"]]
      :values [[:Founders (if show-founders (:founders data) 0)]
              [:Executives (if show-executives (:executives data) 0)]
              [:Employees (if show-employees (:employees data) 0)]
              [:Contractors (if show-contractors (:contractors data) 0)]]}))

(defn calc-percentage
  [dollar total]
  (let [perc (gstring/format "%.2f" (* (/ dollar total) 100))]
    (js/parseFloat perc)))

(defcomponent compensation [data owner]
  (will-mount [_]
    (let [comp-data (:compensation data)
          founders (:founders comp-data)
          executives (:executives comp-data)
          employees (:employees comp-data)
          contractors (:contractors comp-data)]
      (om/set-state! owner :founders (utils/thousands-separator founders))
      (om/set-state! owner :executives (utils/thousands-separator executives))
      (om/set-state! owner :employees (utils/thousands-separator employees))
      (om/set-state! owner :contractors (utils/thousands-separator contractors))))
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
          currency-dict (utils/get-currency currency)
          currency-symbol (utils/get-symbol-for-currency-code currency)
          prefix (str currency-symbol " ")]
      (p/panel {:header (dom/h3 "Compensation") :class "compensation clearfix"}
        (dom/div {:class "compensation row"}
          (dom/form {:class "form-horizontal col-sm-6"}

            ;; Percentage
            (dom/div {:class "form-group"}
              (dom/label {:for "show-as" :class "col-md-4 control-label"} "Show as")
              (dom/div {:class "btn-group btn-toggle col-md-3"}
                (dom/button {
                  :type "button"
                  :class (str "btn btn-success" (when (not percentage) " active"))
                  :on-click #(utils/handle-change comp-data false :percentage)}
                  currency-symbol)
                (dom/button {
                  :type "button"
                  :class (str "btn btn-default" (when percentage " active"))
                  :on-click #(utils/handle-change comp-data true :percentage)}
                  "%"))
              (dom/p {:class "help-block"} (str "Viewers will see as " (if percentage "%" currency-symbol))))

            ;; Founders
            (when show-founders
              (dom/div {:class "form-group"}
                (dom/label {:for "founder-compensation" :class "col-md-4 control-label"} "Founders")
                (dom/div {:class "input-group col-md-3"}
                  (dom/div {:class "input-group-addon"} currency-symbol)
                  (dom/input {
                    :type "text"
                    :class "form-control"
                    :value (om/get-state owner :founders)
                    :on-focus #(om/set-state! owner :founders founders)
                    :on-change #(om/set-state! owner :founders (.. % -target -value))
                    :on-blur (fn [e]
                                (utils/handle-change comp-data (utils/String->Number (.. e -target -value)) :founders)
                                (om/set-state! owner :founders (utils/thousands-separator (.. e -target -value)))
                                (utils/save-values "save-report")
                                (.stopPropagation e))
                    :placeholder (:text currency-dict)}))
                (dom/p {:class "help-block"} "Founder cash compensation this quarter")))

              ;; Executives
              (when show-executives
                (dom/div {:class "form-group"}
                  (dom/label {:for "executives-compensation" :class "col-md-4 control-label"} "Executives")
                  (dom/div {:class "input-group col-md-3"}
                    (dom/div {:class "input-group-addon"} currency-symbol)
                    (dom/input {
                      :type "text"
                      :class "form-control"
                      :value (om/get-state owner :executives)
                      :on-focus #(om/set-state! owner :executives executives)
                      :on-change #(om/set-state! owner :executives (.. % -target -value))
                      :on-blur (fn [e]
                                  (utils/handle-change comp-data (utils/String->Number (.. e -target -value)) :executives)
                                  (om/set-state! owner :executives (utils/thousands-separator (.. e -target -value)))
                                  (utils/save-values "save-report")
                                  (.stopPropagation e))
                      :placeholder (:text currency-dict)}))
                  (dom/p {:class "help-block"} "Executives cash compensation this quarter")))

              ;; Executives
              (when show-employees
                (dom/div {:class "form-group"}
                  (dom/label {:for "employees-compensation" :class "col-md-4 control-label"} "Employees")
                  (dom/div {:class "input-group col-md-3"}
                    (dom/div {:class "input-group-addon"} currency-symbol)
                    (dom/input {
                      :type "text"
                      :class "form-control"
                      :value (om/get-state owner :employees)
                      :on-focus #(om/set-state! owner :employees employees)
                      :on-change #(om/set-state! owner :employees (.. % -target -value))
                      :on-blur (fn [e]
                                  (utils/handle-change comp-data (utils/String->Number (.. e -target -value)) :employees)
                                  (om/set-state! owner :employees (utils/thousands-separator (.. e -target -value)))
                                  (utils/save-values "save-report")
                                  (.stopPropagation e))
                      :placeholder (:text currency-dict)}))
                  (dom/p {:class "help-block"} "Employees cash compensation this quarter")))

              ;; Contractors
              (when show-contractors
                (dom/div {:class "form-group"}
                  (dom/label {:for "contractors-compensation" :class "col-md-4 control-label"} "Contractors")
                  (dom/div {:class "input-group col-md-3"}
                    (dom/div {:class "input-group-addon"} currency-symbol)
                    (dom/input {
                      :type "text"
                      :class "form-control"
                      :value (om/get-state owner :contractors)
                      :on-focus #(om/set-state! owner :contractors contractors)
                      :on-change #(om/set-state! owner :contractors (.. % -target -value))
                      :on-blur (fn [e]
                                  (utils/handle-change comp-data (utils/String->Number (.. e -target -value)) :contractors)
                                  (om/set-state! owner :contractors (utils/thousands-separator (.. e -target -value)))
                                  (utils/save-values "save-report")
                                  (.stopPropagation e))
                      :placeholder (:text currency-dict)}))
                  (dom/p {:class "help-block"} "Cost for contractors this quarter"))))

          ;; Pie chart
          (dom/div {:class "col-sm-6"}
            (om/build pie-chart (get-chart-data comp-data head-data prefix))))

        ;; Comment
        (dom/div {:class "row"}
          (dom/div {:class "col-md-1"})
          (dom/textarea {
            :class "col-md-10"
            :rows "5"
            :value (:comment comp-data)
            :on-change #(utils/handle-change comp-data (.. % -target -value) :comment)
            :on-blur (fn [e]
                        (utils/save-values "save-report")
                        (.stopPropagation e))
            :placeholder "Comments: explain any recent significant changes in compensation costs"})
          (dom/div {:class "col-md-1"}))))))

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
          (when show-founders
            (dom/div
              (om/build report-line {
                :number (if percentage (calc-percentage founders total-compensation) founders)
                :prefix prefix
                :label founders-label
                :pluralize false})))
          (when show-executives
            (dom/div
              (om/build report-line {
                :number (if percentage (calc-percentage executives total-compensation) executives)
                :prefix prefix
                :label executives-label
                :pluralize false})))
          (when show-employees
            (dom/div
              (om/build report-editable-line {
                :number (if percentage (calc-percentage employees total-compensation) employees)
                :prefix prefix
                :label employees-label
                :pluralize false})))
          (when show-contractors
            (dom/div
              (om/build report-editable-line {
                :number (if percentage (calc-percentage contractors total-compensation) contractors)
                :prefix prefix
                :label contractors-label
                :pluralize false})))
          (dom/div
            (om/build report-line {
              :prefix prefix
              :number (utils/thousands-separator
                        (if percentage (calc-percentage total-compensation total-compensation) total-compensation))
              :label "total compensation this month"}))
          (om/build comment-component {:cursor comp-data :key :comment :disabled true}))
        (om/build pie-chart (get-chart-data comp-data head-data prefix))))))
