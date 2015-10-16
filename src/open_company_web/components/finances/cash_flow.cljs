(ns open-company-web.components.finances.cash-flow
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.charts :refer [column-chart]]
            [open-company-web.components.utility-components :refer [editable-pen]]))

(def columns 5)

(defn chart-data-at-index [data idx]
  (let [rev-idx (- (dec (min (count data) columns)) idx)
        obj (get data rev-idx)
        cash-flow (- (:revenue obj) (:costs obj))
        cash-flow-pos? (pos? cash-flow)
        cash-flow (utils/abs (if (js/isNaN cash-flow) 0 cash-flow))]
    [(utils/period-string (:period obj))
     (:costs obj)
     "fill-color: red"
     (:revenue obj)
     "fill-color: green"
     cash-flow
     (str "fill-color: " (if cash-flow-pos? "green" "red"))]))

(defn- get-chart-data [data prefix]
  "Vector of max *columns elements of [:Label value]"
  (let [chart-data (partial chart-data-at-index data)
        placeholder-vect (range (min (count data) columns))]
    { :prefix prefix
      ; :columns [["string" column-name] ["number" "Costs"] ["number" "Revenue"] ["number" "Cash flow"] {:role "style"}]
      :columns [["string" "Period"]
                ["number" "Costs"]
                #js {"type" "string" "role" "style"}
                ["number" "Revenue"]
                #js {"type" "string" "role" "style"}
                ["number" "Cash flow"]
                #js {"type" "string" "role" "style"}]
      :values (into [] (map chart-data placeholder-vect))
      :pattern "###,###.##"}))

(defcomponent cash-flow [data owner]
  (render [_]
    (let [finances-data (:data (:finances (:company-data data)))
          value-set (first finances-data)
          period (utils/period-string (:period value-set))
          cur-symbol (utils/get-symbol-for-currency-code (:currency (:company-data data)))
          cash-val (str cur-symbol (utils/format-value (:cash value-set)))]
      (dom/div {:class (utils/class-set {:section true
                                         :cash-flow true
                                         :read-only (:read-only data)})}
        (dom/h3 {}
                cash-val
                (om/build editable-pen {:click-callback (:editable-click-callback data)}))
        (dom/p {} period)
        (om/build column-chart (get-chart-data finances-data cur-symbol))))))