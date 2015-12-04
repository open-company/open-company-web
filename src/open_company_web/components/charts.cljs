(ns open-company-web.components.charts
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defn add-columns-and-rows [columns data currency-symbol pattern]
  (let [data-table (js/google.visualization.DataTable.)
        formatter (js/google.visualization.NumberFormat. #js {
                    "negativeColor" "red",
                    "negativeParens" true
                    "pattern" pattern ;"###,###.##"
                    "prefix" (if (= currency-symbol "%") "" currency-symbol)
                    "suffix" (if-not (= currency-symbol "%") "" "%")})]
    (doseq [x columns]
      (if (vector? x)
        (.addColumn data-table (first x) (second x))
        (.addColumn data-table x)))
    (.addRows data-table data)
    (doseq [idx (range (count columns))]
      (let [column (columns idx)]
        (when (and (vector? column) (= (first column) "number"))
          (.format formatter data-table idx))))
    data-table))

(defn get-column-chart-options [data column-thickness]
  (clj->js {:title  ""
            :width (min 600 (* 50 (count data)))
            :height 290
            :animation #js {"startup" true
                            "duration" 1000
                            "easing" "out"}
            :legend #js {"position" "none"}
            :vAxis #js {"minValue" 0
                        "gridlineColor" "transparent"
                        "baselineColor" "transparent"
                        "textPosition" "none"}
            :hAxis #js {"textStyle" #js {"fontSize" 9}}
            :chartArea #js {"left" 0 "top" 30 "width" "100%" "height" "80%"}
            :bar #js { "groupWidth" column-thickness}}))

(defn column-draw-chart [owner currency-symbol columns pattern data column-thickness dom-node]
  (when (.-google js/window)
    (let [data-table (add-columns-and-rows columns data currency-symbol pattern)
          options (get-column-chart-options data column-thickness)]
      (when dom-node
        (let [column-chart (js/google.visualization.ColumnChart. dom-node)]
          (.draw column-chart data-table options)
          (om/set-state! owner :chart column-chart)
          (om/set-state! owner :chart-computed-data data-table))))))

(defn get-paginated-values [owner chart-data]
  (let [max-show (:max-show chart-data)
        all-values (vec (js->clj (:values chart-data)))
        page (om/get-state owner :page)
        from (* page max-show)
        to (min (+ (* page max-show) max-show) (count all-values))
        reversed-values (vec (reverse all-values))]
    (clj->js
      (if (< (count all-values) max-show)
        all-values
        (vec (reverse (subvec reversed-values from to)))))))

(defn get-column-thickness [chart-data]
  (if (contains? chart-data :column-thickness)
    (:column-thickness chart-data)
    "14"))

(defn draw-chart [owner chart-data]
  (let [values (get-paginated-values owner chart-data)]
    (column-draw-chart owner
                       (:prefix chart-data)
                       (:columns chart-data)
                       (:pattern chart-data)
                       values
                       (get-column-thickness chart-data)
                       (.getDOMNode (om/get-ref owner "column-chart")))))

(defn check-scrolls-and-draw [owner chart-data initial-check]
  (when-not (om/get-state owner :animated)
    (when-let [chart-ref (om/get-ref owner "column-chart")]
      (let [chart-node (.getDOMNode chart-ref)
            $chart-node (.$ js/window chart-node)
            chart-offset-top (.-top (.offset $chart-node))
            $window (.$ js/window js/window)
            window-scroll-top (.scrollTop $window)
            window-outer-height (.innerHeight $window)
            win-scroll  (+ window-scroll-top window-outer-height)]
        (when (and (> chart-offset-top window-scroll-top)
                   (< (+ chart-offset-top 290) win-scroll))
          (draw-chart owner chart-data)
          (om/set-state! owner :animated true))))))

(defn draw-chart-when-visible [owner chart-data]
  (check-scrolls-and-draw owner chart-data true)
  (when-not (om/get-state owner :animated)
    (.scroll (.$ js/window js/window)
               (fn [e]
                 (check-scrolls-and-draw owner chart-data false)))))

(defn redraw-chart [owner chart-data]
  (let [new-values (get-paginated-values owner chart-data)
        data-table (om/get-state owner :chart-computed-data)
        column-thickness (get-column-thickness chart-data)
        chart-options (get-column-chart-options new-values column-thickness)]
    ;; remove all the present values
    (.removeRows data-table 0 (.getNumberOfRows data-table))
    ;; add all the new values
    (.insertRows data-table 0 new-values)
    (.draw (om/get-state owner :chart) data-table chart-options)))

(defn previous-values [owner chart-data]
  (om/set-state! owner :page (inc (om/get-state owner :page)))
  (redraw-chart owner chart-data))

(defn next-values [owner chart-data]
  (om/set-state! owner :page (dec (om/get-state owner :page)))
  (redraw-chart owner chart-data))

(defcomponent column-chart [chart-data owner]

  (init-state [_]
    {:animated false
     :page 0
     :chart nil
     :chart-computed-data nil})

  (did-mount [_]
    (.setTimeout js/window #(draw-chart-when-visible owner chart-data) 200))

  (render [_]
    (let [data-count (count (:values chart-data))
          page (om/get-state owner :page)
          max-show (:max-show chart-data)]
      (dom/div {:class "charts"}
        (dom/div {:class (utils/class-set {:prev-values true
                                           :values-navigator true
                                           :hidden (not (> data-count (+ (* page max-show) max-show)))})
                  :on-click #(previous-values owner chart-data)}
          (dom/i {:class "fa fa-caret-left"}))
        (dom/div #js {:className "chart-container column-chart" :ref "column-chart" })
        (dom/div {:class (utils/class-set {:next-values true
                                           :values-navigator true
                                           :hidden (zero? (om/get-state owner :page))})
                  :on-click #(next-values owner chart-data)}
          (dom/i {:class "fa fa-caret-right"}))))))
