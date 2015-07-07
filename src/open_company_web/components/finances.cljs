(ns open-company-web.components.finances
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [abs thousands-separator handle-change get-symbols-for-currency-code]]
              [open-company-web.components.report-line :refer [report-editable-line]]
              [open-company-web.components.comment :refer [comment-component]]))

(defcomponent finances [data owner]
  (render [_]
    (let [cash (:cash (:finances data))
          revenue (:revenue (:finances data))
          costs (:costs (:finances data))
          comment (:comment (:finances data))
          currency (first (:currency data))
          burn-rate (- revenue costs)
          burn-rate-label (if (> burn-rate 0) "Growth rate: " "Burn rate: ")
          burn-rate-classes (str "num " (if (> burn-rate 0) "green" "red"))
          profitable (if (> burn-rate 0) "Yes" "No")
          run-away (if (<= burn-rate 0) (quot cash burn-rate) "N/A")
          currency-symbol (get-symbols-for-currency-code currency)]
      (dom/div {:class "report-list finances"}
        (dom/h3 "Finances:")
        (om/build report-editable-line {:cursor (:finances data) :key :cash :prefix currency-symbol :label "cash on hand" :pluralize false})
        (om/build report-editable-line {:cursor (:finances data) :key :revenue :prefix currency-symbol :label "revenue this month" :pluralize false})
        (om/build report-editable-line {:cursor (:finances data) :key :costs :prefix currency-symbol :label "costs this month" :pluralize false})
        (dom/div
          (dom/span {:class "label"} (str "Profitable this month? " profitable)))
        (dom/div
          (dom/span {:class "label"} burn-rate-label)
          (dom/span {:class burn-rate-classes} (thousands-separator (abs burn-rate)))
          (dom/span {:class "label"} currency-symbol))
        (dom/div
          (dom/span {:class "label"} "Runaway: " (if (<= burn-rate 0) (str (abs run-away) " months") "N/A")))
        (om/build comment-component {:value comment})))))
