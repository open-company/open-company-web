(ns open-company-web.components.pie-chart
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defn add-rows [columns data currency-symbol]
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

(defn draw-chart [currency-symbol columns data dom-node]
  (when (.-google js/window)
    (let [data-table (add-rows columns data currency-symbol)
          options (clj->js {
                    :title  ""
                    :width 600
                    :height 250})]
      (when dom-node (.draw (js/google.visualization.PieChart. dom-node) data-table options)))))

(defcomponent pie-chart [chart-data owner]
  (did-mount [_]
    (draw-chart (:prefix chart-data) (:columns chart-data) (:values chart-data) (.getDOMNode (om/get-ref owner "chart"))))
  (did-update [_ _ _]
    (draw-chart (:prefix chart-data) (:columns chart-data) (:values chart-data) (.getDOMNode (om/get-ref owner "chart"))))
  (render [_]
    (dom/div #js {:className "chart-container" :ref "chart" })))
