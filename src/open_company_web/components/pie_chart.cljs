(ns ^:figwheel-always open-company-web.components.pie-chart
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defn add-rows [columns data]
  (let [data-table (js/google.visualization.DataTable.)]
    (doseq [x columns]
      (.addColumn data-table (first x) (second x)))
    (.addRows data-table (clj->js data))
    data-table))

(defn draw-chart [columns data dom-node]
  (let [data-table (add-rows columns data)
        options (clj->js {
                  :title  ""
                  :width 400
                  :height 150})]
    (when dom-node (.draw (js/google.visualization.PieChart. dom-node) data-table options))))

(defcomponent pie-chart [chart-data owner]
  (did-mount [_]
    (draw-chart (:columns chart-data) (:values chart-data) (.getDOMNode (om/get-ref owner "chart"))))
  (did-update [_ _ _]
    (draw-chart (:columns chart-data) (:values chart-data) (.getDOMNode (om/get-ref owner "chart"))))
  (render [_]
    (dom/div #js {:className "chart-container" :ref "chart" })))
