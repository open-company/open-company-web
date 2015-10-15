(ns open-company-web.components.finances.cash-flow
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.charts :refer [column-chart]]
            [open-company-web.components.utility-components :refer [editable-pen]]))

(def columns 4)

(defn chart-data-at-index [data idx prefix suffix]
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

(defn- get-chart-data [data prefix keyw column-name]
  "Vector of max *columns elements of [:Label value]"
  (let [chart-data (partial chart-data-at-index data)
        placeholder-vect (subvec [0 1 2 3 4 5 6] 0 (min (count data) columns))]
    { :prefix prefix
      ; :columns [["string" column-name] ["number" "Costs"] ["number" "Revenue"] ["number" "Cash flow"] {:role "style"}]
      :columns [["string" column-name]
                ["number" "Costs"]
                #js {"type" "string" "role" "style"}
                ["number" "Revenue"]
                #js {"type" "string" "role" "style"}
                ["number" "Cash flow"]
                #js {"type" "string" "role" "style"}]
      :values (into [] (map chart-data placeholder-vect))}))

(defn cash-flow-add-rows [columns data currency-symbol]
  (let [data-table (js/google.visualization.DataTable.)
        formatter (js/google.visualization.NumberFormat. #js {
                    "negativeColor" "red",
                    "negativeParens" true
                    "pattern" "###,###.##"
                    "prefix" (if (= currency-symbol "%") "" currency-symbol)
                    "suffix" (if-not (= currency-symbol "%") "" "%")})]
    (doseq [x columns]
      (if (vector? x)
        (.addColumn data-table (first x) (second x))
        (.addColumn data-table x)))
    (.addRows data-table (clj->js data))
    (.format formatter data-table 1)
    (.format formatter data-table 3)
    (.format formatter data-table 5)
    data-table))

(defn cash-flow-draw-chart [currency-symbol columns data dom-node]
  (when (.-google js/window)
    (let [data-table (cash-flow-add-rows columns data currency-symbol)
          options (clj->js {
                    :title  ""
                    :width 600
                    :height 250
                    ; :colors [ "red" "green" "green"]
                    :legend #js {"position" "none"}
                    :vAxis #js {"minValue" 0}})]
      (when dom-node (.draw (js/google.visualization.ColumnChart. dom-node) data-table options)))))

(defcomponent cash-flow-chart [chart-data owner]
  (did-mount [_]
    (cash-flow-draw-chart (:prefix chart-data)
                       (:columns chart-data)
                       (:values chart-data)
                       (.getDOMNode (om/get-ref owner "column-chart"))))
  (did-update [_ _ _]
    (cash-flow-draw-chart (:prefix chart-data)
                       (:columns chart-data)
                       (:values chart-data)
                       (.getDOMNode (om/get-ref owner "column-chart"))))
  (render [_]
    (dom/div #js {:className "chart-container column-chart" :ref "column-chart" })))

(defcomponent cash-flow [data owner]
  (render [_]
    (let [finances-data (:data (:finances (:company-data data)))
          value-set (first finances-data)
          period (utils/period-string (:period value-set))
          cur-symbol (utils/get-symbol-for-currency-code (:currency (:company-data data)))
          cash-val (str cur-symbol (utils/format-value (:cash value-set)))]
      (dom/div {:class (utils/class-set {:section true
                                         :cash true
                                         :read-only (:read-only data)})}
        (dom/h3 {}
                cash-val
                (om/build editable-pen {:click-callback (:editable-click-callback data)}))
        (dom/p {} period)
        (om/build cash-flow-chart (get-chart-data finances-data cur-symbol :cash "Cash"))))))