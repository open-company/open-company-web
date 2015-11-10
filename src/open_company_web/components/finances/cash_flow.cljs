(ns open-company-web.components.finances.cash-flow
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.charts :refer [column-chart]]
            [open-company-web.components.utility-components :refer [editable-pen]]
            [open-company-web.components.finances.utils :as finances-utils]))

(def green-color "#26C485")
(def red-color "#d72a46")

(defn chart-data-at-index [data prefix idx]
  (let [data (to-array data)
        rev-idx (- (dec (min (count data) finances-utils/columns-num)) idx)
        obj (get data rev-idx)
        cash-flow (- (:revenue obj) (:costs obj))
        cash-flow (if (js/isNaN cash-flow) 0 cash-flow)
        cash-flow-pos? (pos? cash-flow)
        abs-cash-flow (utils/abs cash-flow)]
    [(utils/period-string (:period obj) :short-month)
     (:revenue obj)
     (str "fill-color: " green-color)
     (str (utils/period-string (:period obj)) " Revenue: " prefix (.toLocaleString (:revenue obj)))
     (:costs obj)
     (str "fill-color: " red-color)
     (str (utils/period-string (:period obj)) " Costs: " prefix (.toLocaleString (:costs obj)))
     abs-cash-flow
     (str "fill-color: " (if cash-flow-pos? green-color red-color))
     (str (utils/period-string (:period obj)) " Cash flow: " (when (neg? cash-flow) "-") prefix (.toLocaleString abs-cash-flow))]))

(defn- get-chart-data [data prefix]
  "Vector of max *columns elements of [:Label value]"
  (let [fixed-data (finances-utils/placeholder-data data)
        chart-data (partial chart-data-at-index fixed-data prefix)
        placeholder-vect (range finances-utils/columns-num)]
    { :prefix prefix
      :columns [["string" "Period"]
                ["number" "Revenue"]
                #js {"type" "string" "role" "style"}
                #js {"type" "string" "role" "tooltip"}
                ["number" "Costs"]
                #js {"type" "string" "role" "style"}
                #js {"type" "string" "role" "tooltip"}
                ["number" "Cash flow"]
                #js {"type" "string" "role" "style"}
                #js {"type" "string" "role" "tooltip"}]
      :values (into [] (map chart-data placeholder-vect))
      :pattern "###,###.##"
      :column-thickness "42"}))

(defcomponent cash-flow [data owner]
  (render [_]
    (let [finances-data (:data (:section-data data))
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-finances (sort #(sort-pred %1 %2) finances-data)
          value-set (first sorted-finances)
          period (utils/period-string (:period value-set))
          currency (finances-utils/get-currency-for-current-company)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          cash-val (str cur-symbol (utils/format-value (:cash value-set)))]
      (dom/div {:class (utils/class-set {:section true
                                         :cash-flow true
                                         :read-only (:read-only data)})}
        (om/build column-chart (get-chart-data sorted-finances cur-symbol))
        (dom/div {:class "chart-footer-container"}
          (dom/div {:class "target-actual-container"}
            (dom/div {:class "actual-container"}
              (dom/h3 {:class "actual green"} cash-val)
              (dom/h3 {:class "actual-label"} "ACTUAL"))))
        (dom/div {:class "chart-footer-container"}
          (dom/div {:class "period-container"}
            (dom/p {} period)))))))