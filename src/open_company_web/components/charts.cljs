(ns open-company-web.components.charts
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defn pie-add-rows [columns data currency-symbol]
  (let [data-table (js/google.visualization.DataTable.)
        formatter (js/google.visualization.NumberFormat. #js {
                    "negativeColor" "red",
                    "negativeParens" true
                    "pattern" "###,###.##"
                    "prefix" (if (= currency-symbol "%") "" currency-symbol)
                    "suffix" (if (not (= currency-symbol "%")) "" "%")})]
    (doseq [x columns]
      (.addColumn data-table (first x) (second x)))
    (.addRows data-table (clj->js data))
    (.format formatter data-table 1)
    data-table))

(defn pie-draw-chart [currency-symbol columns data dom-node]
  (when (.-google js/window)
    (let [data-table (pie-add-rows columns data currency-symbol)
          options (clj->js {
                    :title  ""
                    :width 600
                    :height 250})]
      (when dom-node (.draw (js/google.visualization.PieChart. dom-node) data-table options)))))

(defcomponent pie-chart [chart-data owner]
  (did-mount [_]
    (pie-draw-chart (:prefix chart-data)
                    (:columns chart-data)
                    (:values chart-data)
                    (.getDOMNode (om/get-ref owner "pie-chart"))))
  (did-update [_ _ _]
    (pie-draw-chart (:prefix chart-data)
                    (:columns chart-data)
                    (:values chart-data)
                    (.getDOMNode (om/get-ref owner "pie-chart"))))
  (render [_]
    (dom/div #js {:className "chart-container pie-chart" :ref "pie-chart" })))

(defn column-add-rows [columns data currency-symbol]
  (let [data-table (js/google.visualization.DataTable.)
        formatter (js/google.visualization.NumberFormat. #js {
                    "negativeColor" "red",
                    "negativeParens" true
                    "pattern" "###,###.##"
                    "prefix" (if (= currency-symbol "%") "" currency-symbol)
                    "suffix" (if-not (= currency-symbol "%") "" "%")})]
    (doseq [x columns]
      (.addColumn data-table (first x) (second x)))
    (.addRows data-table (clj->js data))
    (.format formatter data-table 1)
    data-table))

(defn column-draw-chart [currency-symbol columns data dom-node]
  (when (.-google js/window)
    (let [data-table (column-add-rows columns data currency-symbol)
          options (clj->js {
                    :title  ""
                    :width 600
                    :height 250
                    :legend #js {"position" "none"}
                    :vAxis #js {"minValue" 0
                                "gridlineColor" "transparent"
                                "baselineColor" "transparent"
                                "textPosition" "none"}
                    :bar #js { "groupWidth" "10%"}})]
      (when dom-node (.draw (js/google.visualization.ColumnChart. dom-node) data-table options)))))

(defcomponent column-chart [chart-data owner]
  (did-mount [_]
    (column-draw-chart (:prefix chart-data)
                       (:columns chart-data)
                       (:values chart-data)
                       (.getDOMNode (om/get-ref owner "column-chart"))))
  (did-update [_ _ _]
    (column-draw-chart (:prefix chart-data)
                       (:columns chart-data)
                       (:values chart-data)
                       (.getDOMNode (om/get-ref owner "column-chart"))))
  (render [_]
    (dom/div #js {:className "chart-container column-chart" :ref "column-chart" })))