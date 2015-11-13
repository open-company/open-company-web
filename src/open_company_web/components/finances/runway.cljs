(ns open-company-web.components.finances.runway
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.iso4217 :refer [iso4217]]
            [open-company-web.components.charts :refer [column-chart]]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.utility-components :refer [editable-pen]]
            [goog.string :as gstring]))

(defn get-rounded-runway [runway-days & [flags]]
  (cond
    (< runway-days 90)
    (str runway-days " days")
    (< runway-days (* 30 24))
    (if (utils/in? flags :round)
      (str (gstring/format "%.2f" (/ runway-days 30)) "months")
      (str (quot runway-days 30) " months"))
    :else
    (if (utils/in? flags :round)
      (str (gstring/format "%.2f" (/ runway-days (* 30 12))) " years")
      (str (quot runway-days (* 30 12)) " years"))))

(defn get-runway-subtitle [cash avg-burn-rate runway-days cur-symbol]
  (str cur-symbol (.toLocaleString (or cash 0)) " ÷ " cur-symbol (.toLocaleString (or avg-burn-rate 0)) " 3-months avg. burn: " (get-rounded-runway runway-days [:round])))

(defcomponent runway [data owner]
  (render [_]
    (let [finances-data (:data (:section-data data))
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-finances (sort #(sort-pred %1 %2) finances-data)
          value-set (first sorted-finances)
          runway-value (:runway value-set)
          is-profitable (or (nil? runway-value) (neg? runway-value))
          runway (if is-profitable
                    "Profitable"
                    (str (utils/format-value runway-value) " days"))
          period (utils/period-string (:period value-set))
          currency (finances-utils/get-currency-for-current-company)
          cur-symbol (utils/get-symbol-for-currency-code (:currency data))
          runway-string (if is-profitable
                          runway
                          (get-rounded-runway runway-value))]
      (dom/div {:class (str "section runway" (when (:read-only data) " read-only"))}
        (dom/div {:class "chart-header-container"}
          (dom/div {:class "target-actual-container"}
            (dom/div {:class "actual-container"}
              (dom/h3 {:class "actual green"} runway-string)
              (dom/h3 {:class "actual-label gray"} (get-runway-subtitle (:cash value-set) (:avg-burn-rate value-set) runway-value cur-symbol)))))
        (om/build column-chart (finances-utils/get-chart-data sorted-finances
                                                              ""
                                                              :runway
                                                              "runway"
                                                              #js {"type" "string" "role" "style"}
                                                              "fill-color: #26C485"
                                                              "###,### days"
                                                              " days"))))))