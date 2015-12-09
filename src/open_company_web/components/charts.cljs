(ns open-company-web.components.charts
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defn- get-max [chart-data]
  (let [values (:values chart-data)
        max-val (apply max (map #(nth % 1) values))]
    max-val))

(defn- get-pagination [owner data]
  (let [max-show (om/get-state owner :max-show)
        data-count (count data)]
    (if (>= max-show data-count)
      #js {"min" 0
           "max" (min max-show data-count)}
      (let [page (om/get-state owner :page)
            to (max max-show (- data-count (* page max-show)))
            from (max 0 (- to max-show))]
        #js {"min" from
             "max" to}))))

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
    (.addRows data-table (clj->js data))
    (doseq [idx (range (count columns))]
      (let [column (columns idx)]
        (when (and (vector? column) (= (first column) "number"))
          (.format formatter data-table idx))))
    data-table))

(defn chart-options [owner data column-thickness max-value startup]
  (clj->js {:title  ""
            :width (min 600 (* 50 (count data)))
            :height 290
            :animation #js {"startup" startup
                            "duration" 400
                            "easing" "out"}
            :legend #js {"position" "none"}
            :vAxis #js {"minValue" 0
                        "maxValue" max-value
                        "gridlineColor" "transparent"
                        "baselineColor" "transparent"
                        "textPosition" "none"}
            :hAxis #js {"textStyle" #js {"fontSize" 9}
                        "viewWindow" (get-pagination owner data)}
            :chartArea #js {"left" 0 "top" 30 "width" "100%" "height" "80%"}
            :bar #js { "groupWidth" column-thickness}}))

(defn draw-chart [owner currency-symbol columns pattern data column-thickness dom-node]
  (when (.-google js/window)
    (let [data-table (add-columns-and-rows columns data currency-symbol pattern)
          options (chart-options owner data column-thickness (om/get-state owner :max-value) true)]
      (when dom-node
        (let [column-chart (js/google.visualization.ColumnChart. dom-node)]
          (.draw column-chart data-table options)
          (om/set-state! owner :chart column-chart)
          (om/set-state! owner :chart-computed-data data-table))))))

(defn get-column-thickness [chart-data]
  (if (contains? chart-data :column-thickness)
    (:column-thickness chart-data)
    "14"))

(defn check-scrolls-and-draw [owner chart-data initial-check]
  (when-not (om/get-state owner :animated)
    (when-let [chart-ref (om/get-ref owner "charts")]
      (let [chart-node (.getDOMNode chart-ref)
            $chart-node (.$ js/window chart-node)
            chart-offset-top (.-top (.offset $chart-node))
            $window (.$ js/window js/window)
            window-scroll-top (.scrollTop $window)
            window-outer-height (.innerHeight $window)
            win-scroll  (+ window-scroll-top window-outer-height)]
        (when (and (> chart-offset-top window-scroll-top)
                   (< (+ chart-offset-top 290) win-scroll))
          (draw-chart owner
                      (:prefix chart-data)
                      (:columns chart-data)
                      (:pattern chart-data)
                      (:values chart-data)
                      (get-column-thickness chart-data)
                      (.getDOMNode (om/get-ref owner "column-chart")))
          (om/set-state! owner :animated true))))))

(defn draw-chart-when-visible [owner chart-data]
  (check-scrolls-and-draw owner chart-data true)
  (when-not (om/get-state owner :animated)
    (.scroll (.$ js/window js/window)
               (fn [e]
                 (check-scrolls-and-draw owner chart-data false)))))

(defn redraw-chart [owner chart-data]
  (let [data-table (om/get-state owner :chart-computed-data)
        chart-options (chart-options owner
                                     (:values chart-data)
                                     (get-column-thickness chart-data)
                                     (om/get-state owner :max-value)
                                     false)]
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
     :chart-computed-data nil
     :max-show (:max-show chart-data)
     :max-value (get-max chart-data)})

  (did-mount [_]
    (.setTimeout js/window #(draw-chart-when-visible owner chart-data) 200))

  (will-receive-props [_ next-props]
    (om/set-state! owner :max-value (get-max next-props)))

  (render [_]
    (let [data-count (count (:values chart-data))
          page (om/get-state owner :page)
          max-show (:max-show chart-data)]
      (dom/div #js {:className "charts"
                    :ref "charts"}
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
