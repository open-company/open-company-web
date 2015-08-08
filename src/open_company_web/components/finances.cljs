(ns open-company-web.components.finances
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.report-line :refer [report-line]]
              [open-company-web.components.comment :refer [comment-component comment-readonly-component]]
              [open-company-web.components.report.finances-section :refer [finances-section]]
              [om-bootstrap.random :as  r]
              [om-bootstrap.panel :as p]
              [open-company-web.lib.utils :as utils]))

(def finances-rows [
  {:label "Cash on hand" :key-name :cash :help-block "Cash and cash equivalents"}
  {:label "Revenue" :key-name :revenue :help-block "All revenue this quarter"}
  {:label "Costs" :key-name :costs :help-block "All costs this quarter including salaries"}])

(defcomponent finances [data owner]
  (will-mount [_]
    (let [fin-data (:finances data)
          cash (:cash fin-data)
          revenue (:revenue fin-data)
          costs (:costs fin-data)]
      (om/set-state! owner :cash (utils/thousands-separator cash))
      (om/set-state! owner :revenue (utils/thousands-separator revenue))
      (om/set-state! owner :costs (utils/thousands-separator costs))))
  (render [_]
    (let [fin-data (:finances data)
          cash (:cash fin-data)
          revenue (:revenue fin-data)
          costs (:costs fin-data)
          currency (:currency data)
          burn-rate (- revenue costs)
          burn-rate (if (js/isNaN burn-rate) 0 burn-rate)
          positive-diff (>= burn-rate 0)
          burn-rate-label (if positive-diff "Growth rate" "Burn rate")
          burn-rate-classes (str "num " (if positive-diff "green" "red"))
          burn-rate-helper (if positive-diff "Cash earned this quarter" "Cash used this quarter")
          profitable (if positive-diff "Yes" "No")
          run-away (if (not positive-diff) (quot cash burn-rate) "N/A")
          currency-dict (utils/get-currency currency)
          currency-symbol (utils/get-symbol-for-currency-code currency)]
      (p/panel {:header (dom/h3 "Finances") :class "finances clearfix"}
        (dom/div {:class "finances row"}
          (dom/form {:class "form-horizontal"}

            (for [section finances-rows]
              (om/build finances-section (merge section {
                :cursor fin-data
                :prefix currency-symbol
                :placeholder (:text currency-dict)})))

            ;; Profitable
            (dom/div {:class "form-group"}
              (dom/label {:class "col-md-2 control-label"} "Profitable?")
              (dom/label {:class "col-md-2 control-label"} profitable)
              (dom/p {:class "help-block"} "Costs exceed revenue this quarter"))

            ;; Burn rate
            (dom/div {:class "form-group"}
              (dom/label {:class "col-md-2 control-label"} burn-rate-label)
              (dom/label {:class "col-md-2 control-label"}
                (dom/span {:class burn-rate-classes} (str (utils/thousands-separator burn-rate) " " currency-symbol)))
              (dom/p {:class "help-block"} burn-rate-helper))

            ;; Runaway
            (when (<= burn-rate 0)
              (dom/div {:class "form-group"}
                (dom/label {:class "col-md-2 control-label"} "Runway?")
                (dom/label {:class "col-md-2 control-label"} run-away)
                (dom/p {:class "help-block"} (str "Time until cash on hand is " currency-symbol "0"))))))

        ;; Comment textarea
        (om/build comment-component {
          :cursor fin-data
          :placeholder "Comments: explain any recent significant changes in costs or revenue, provide guidance on revenue and profitablity expectations"
          })))))

(defcomponent readonly-finances [data owner]
  (render [_]
    (let [fin-data (:finances data)
          cash (:cash fin-data)
          revenue (:revenue fin-data)
          costs (:costs fin-data)
          currency (:currency data)
          burn-rate (- revenue costs)
          burn-rate-label (if (> burn-rate 0) "Growth rate: " "Burn rate: ")
          burn-rate-classes (str "num " (if (> burn-rate 0) "green" "red"))
          profitable (if (> burn-rate 0) "Yes" "No")
          run-away (if (<= burn-rate 0) (quot cash burn-rate) "N/A")
          currency-symbol (utils/get-symbol-for-currency-code currency)]
      (r/well {:class "report-list finances clearfix"}
        (dom/div {:class "report-list-left"}

          (let [sections [{:number cash :label "cash on hand"}
                          {:number revenue :label "revenue this month"}
                          {:number costs :label "costs this month"}]]
            (for [section sections]
              (when (not (= (:number section) nil))
                (dom/div
                  (om/build report-line (merge section {:prefix currency-symbol :pluralize false}))))))

          (dom/div
            (dom/span {:class "label"} (str "Profitable this month? " profitable)))
          (dom/div
            (dom/span {:class "label"} burn-rate-label)
            (dom/span {:class "label"} currency-symbol)
            (dom/span {:class burn-rate-classes} (utils/thousands-separator (utils/abs burn-rate))))
          (dom/div
            (dom/span {:class "label"} "Runaway: " (if (<= burn-rate 0) (str (utils/abs run-away) " months") "N/A")))
          (om/build comment-readonly-component {:cursor fin-data :key :comment :disabled true}))))))
