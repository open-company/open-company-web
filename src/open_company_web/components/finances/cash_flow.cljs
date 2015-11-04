(ns open-company-web.components.finances.cash-flow
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.charts :refer [column-chart]]
            [open-company-web.components.utility-components :refer [editable-pen]]
            [open-company-web.components.finances.utils :as finances-utils]))

(def columns 5)

(def green-color "#26C485")
(def red-color "#d72a46")

(defn chart-data-at-index [data prefix idx]
  (let [data (to-array data)
        rev-idx (- (dec (min (count data) columns)) idx)
        obj (get data rev-idx)
        cash-flow (- (:revenue obj) (:costs obj))
        cash-flow (if (js/isNaN cash-flow) 0 cash-flow)
        cash-flow-pos? (pos? cash-flow)
        abs-cash-flow (utils/abs cash-flow)]
    [(utils/period-string (:period obj) :short-month)
     (:costs obj)
     (str "fill-color: " red-color)
     (str (utils/period-string (:period obj)) " Costs: " prefix (.toLocaleString (:costs obj)))
     (:revenue obj)
     (str "fill-color: " green-color)
     (str (utils/period-string (:period obj)) " Revenue: " prefix (.toLocaleString (:revenue obj)))
     abs-cash-flow
     (str "fill-color: " (if cash-flow-pos? green-color red-color))
     (str (utils/period-string (:period obj)) " Cash flow: " (when (neg? cash-flow) "-") prefix (.toLocaleString abs-cash-flow))]))

(defn- get-chart-data [data prefix]
  "Vector of max *columns elements of [:Label value]"
  (let [chart-data (partial chart-data-at-index data prefix)
        placeholder-vect (range (min (count data) columns))]
    { :prefix prefix
      ; :columns [["string" column-name] ["number" "Costs"] ["number" "Revenue"] ["number" "Cash flow"] {:role "style"}]
      :columns [["string" "Period"]
                ["number" "Costs"]
                #js {"type" "string" "role" "style"}
                #js {"type" "string" "role" "tooltip"}
                ["number" "Revenue"]
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
        (dom/h3 {}
                cash-val
                (om/build editable-pen {:click-callback (:editable-click-callback data)}))
        (dom/p {} period)))))