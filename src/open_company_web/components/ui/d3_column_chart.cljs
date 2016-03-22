(ns open-company-web.components.ui.d3-column-chart
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.finances.utils :as finances-utils]
            [cljsjs.d3]))

(def bar-width 15)

(def show-columns 6)

(defn width [el]
  (if (and el (.node el))
    (.-width (.getBBox (.node el)))
    0))

(defn current-data [owner]
  (let [start (om/get-state owner :start)
        all-data (vec (om/get-props owner :chart-data))
        stop (min (count all-data) (+ start show-columns))]
    (subvec all-data start stop)))

(defn scale [owner options]
  (let [all-data (om/get-props owner :chart-data)
        chart-keys (:chart-keys options)
        filtered-data (map #(select-keys % chart-keys) all-data)
        data-max (apply max (vec (flatten (map vals filtered-data))))
        linear-fn (.. js/d3 -scale linear)
        domain-fn (.domain linear-fn #js [0 data-max])
        range-fn (.range linear-fn #js [0 (- (:chart-height options) 100)])]
    range-fn))

(defn bar-position [chart-width i data-count columns-num]
  (let [bar-spacer (/ (- chart-width 90) (dec show-columns))
        pos (- (* i bar-spacer)
               (/ (* (* bar-width 2) columns-num) 2)
               -60)]
    pos))

(defn build-selected-label [chart-label-g label-value label-color]
  (.each (.selectAll chart-label-g "text")
         (fn [_ _]
           (this-as el
             (.remove (.select js/d3 el)))))
  (if (string? label-value)
    (-> chart-label-g
        (.append "text")
        (.attr "fill" label-color)
        (.attr "class" "chart-label")
        (.attr "dx" 0)
        (.attr "dy" 0)
        (.text label-value))
    (loop [idx 0
           txt-left 0]
      (let [{:keys [label color]} (get label-value idx)
            txt (-> chart-label-g
                    (.append "text")
                    (.attr "fill" color)
                    (.attr "class" "chart-label small")
                    (.attr "dx" 0)
                    (.attr "dy" 0)
                    (.text label))
            txt-width (width txt)]
        (.attr txt "dx" txt-left)
        (when (< idx (dec (count label-value)))
          (recur (inc idx)
                 (+ txt-left txt-width 10)))))))

(defn bar-click [owner options idx is-hover]
  (.stopPropagation (.-event js/d3))
  (let [selected (om/get-state owner :selected)
        chart-label (.select js/d3 (str "#chart-label"))
        chart-width (:chart-width options)
        cur-g (.select js/d3 (str "#chart-g-" selected))
        next-g (.select js/d3 (str "#chart-g-" idx))
        data (current-data owner)
        next-set (get data idx)
        label-key (:label-key options)
        data-count (count data)
        chart-keys-count (count (:chart-keys options))
        next-g-rects (.selectAll next-g "rect")
        all-rects (.selectAll js/d3 "rect.chart-bar")
        next-month-text (.select js/d3 (str "text#chart-x-label-" idx))
        all-month-text (.selectAll js/d3 ".chart-x-label")]
    (.each all-rects
           (fn [d i]
              (this-as rect
                (let [d3-rect (.select js/d3 rect)
                      color (.attr d3-rect "data-fill")]
                  (.attr d3-rect "fill" color)))))
    (.each next-g-rects
           (fn [d i]
              (this-as rect
                (let [d3-rect (.select js/d3 rect)
                      selected-color (.attr d3-rect "data-selectedFill")]
                  (.attr d3-rect "fill" selected-color)))))
    (.each all-month-text
           (fn [d i]
              (this-as month-text
                (let [d3-month-text (.select js/d3 month-text)]
                  (.attr d3-month-text "fill" (:h-axis-color options))))))
    (-> next-month-text
        (.attr "fill" (:h-axis-selected-color options)))
    (build-selected-label chart-label (label-key next-set) (:label-color options))
    (let [chart-label-width (width chart-label)
          new-x-pos (- (/ chart-width 2) (/ chart-label-width 2))]
      (.attr chart-label "transform" (str "translate("
                                          (max 0 new-x-pos)
                                          ","
                                          50")")))
    (when-not is-hover
      (om/set-state! owner :selected idx))))

(defn get-color [color-key options chart-key value]
  (let [color (chart-key (color-key options))]
    (if (fn? color)
      (color value)
      color)))

(defn d3-calc [owner options]
  (when-let [d3-column (om/get-ref owner "d3-column")]
    ; clean the chart area
    (.each (.selectAll (.select js/d3 d3-column) "*")
           (fn [_ _]
             (this-as el
               (.remove (.select js/d3 el)))))
    ; render the chart
    (let [selected (om/get-state owner :selected)
          chart-data (current-data owner)
          fill-colors (:chart-colors options)
          fill-selected-colors (:chart-selected-colors options)
          chart-width (:chart-width options)
          chart-height (:chart-height options)
          chart-keys (:chart-keys options)
          keys-count (count chart-keys)
          ; main chart node
          chart-node (-> js/d3
                         (.select d3-column)
                         (.attr "width" (:chart-width options))
                         (.attr "height" (:chart-height options))
                         (.on "click" #(.stopPropagation (.-event js/d3))))
          scale-fn (scale owner options)
          h-axis-color (:h-axis-color options)
          h-axis-selected-color (:h-axis-selected-color options)
          label-key (:label-key options)]
      ; for each set of data
      (doseq [i (range (count chart-data))]
        (let [data-set (get chart-data i)
              max-val (apply max (vals (select-keys data-set chart-keys)))
              scaled-max-val (scale-fn max-val)
              ; add a g element
              bar-enter (-> chart-node
                            (.append "g")
                            (.attr "class" "chart-g")
                            (.attr "id" (str "chart-g-" i))
                            (.attr "transform"
                                   (str "translate("
                                        (bar-position chart-width i (count chart-data) keys-count)
                                        ","
                                        (- (:chart-height options) scaled-max-val 20) ")")))
              ; month label
              force-year (or (zero? i) (= i (dec (count chart-data))))
              text (utils/get-period-string (:period data-set) nil [:short (when force-year :force-year)])
              x-pos (bar-position chart-width i (count chart-data) keys-count)
              label (-> chart-node
                        (.append "text")
                        (.attr "class" "chart-x-label")
                        (.attr "id" (str "chart-x-label-" i))
                        (.attr "x" x-pos)
                        (.attr "y" (:chart-height options))
                        (.attr "fill" (if (= i selected) h-axis-selected-color h-axis-color))
                        (.text text))
              label-width (width label)]
          ; set month label x position depending on its width
          (.attr label "x" (+ x-pos (/ (- (* bar-width keys-count) label-width) 2)))
          ; for each key in the set
          (doseq [j (range (count chart-keys))]
            (let [chart-key (get chart-keys j)
                  value (chart-key data-set)
                  scaled-val (scale-fn (utils/abs value))
                  color (get-color :chart-colors options chart-key value)
                  selected-color (get-color :chart-selected-colors options chart-key value)]
              ; add a rect to represent the data
              (-> bar-enter
                  (.append "rect")
                  (.attr "class" "chart-bar")
                  (.attr "fill" (if (= i selected)
                                  selected-color
                                  color))
                  (.attr "data-fill" color)
                  (.attr "data-selectedFill" selected-color)
                  (.attr "id" (str "chart-bar-" (name chart-key) "-" i))
                  (.attr "x" (* j bar-width))
                  (.attr "y" (- scaled-max-val scaled-val))
                  (.attr "width" bar-width)
                  (.attr "height" (max 0 scaled-val)))))))
      ; add the hovering rects
      (doseq [i (range (count chart-data))]
        (-> chart-node
            (.append "rect")
            (.attr "class" "hover-rect")
            (.attr "width" (/ chart-width (count chart-data)))
            (.attr "height" (- chart-height 50))
            (.attr "x" (* i (/ chart-width (count chart-data))))
            (.attr "y" 50)
            (.on "click" #(bar-click owner options i false))
            (.on "mouseover" #(bar-click owner options i true))
            (.on "mouseout" #(bar-click owner options (om/get-state owner :selected) true))
            (.attr "fill" "transparent")))
      ; add the selected value label
      (let [x-pos (/ chart-width 2)
            label-value (label-key (get chart-data selected))
            label-color (:label-color options)
            chart-label-g (-> chart-node
                              (.append "g")
                              (.attr "class" "chart-label-container")
                              (.attr "id" "chart-label")
                              (.attr "transform" (str "translate(" x-pos "," 50")")))]
        (build-selected-label chart-label-g label-value label-color)
        (let [chart-label-width (width chart-label-g)
              chart-label-pos (- (/ chart-width 2) (/ chart-label-width 2))]
          (.attr chart-label-g "transform" (str "translate("
                                                (max 0 chart-label-pos)
                                                ","
                                                50
                                                ")")))))))

(def chart-step show-columns)

(defn prev-data [owner e]
  (.stopPropagation e)
  (let [start (om/get-state owner :start)
        next-start (- start chart-step)
        fixed-next-start (max 0 next-start)]
    (om/set-state! owner :start fixed-next-start)))

(defn next-data [owner e]
  (.stopPropagation e)
  (let [start (om/get-state owner :start)
        all-data (om/get-props owner :chart-data)
        next-start (+ start chart-step)
        fixed-next-start (min (- (count all-data) show-columns) next-start)]
    (om/set-state! owner :start fixed-next-start)))

(defcomponent d3-column-chart [{:keys [chart-data] :as data} owner {:keys [chart-width chart-height] :as options}]

  (init-state [_]
    (let [start (max 0 (- (count chart-data) show-columns))
          current-data (vec (take-last show-columns chart-data))]
      {:start start
       :selected (dec (count current-data))}))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (d3-calc owner options)))

  (did-update [_ old-props old-state]
    (when-not (utils/is-test-env?)
      (when (or (not= old-props data)
                (not= old-state (om/get-state owner)))
        (d3-calc owner options))))

  (render-state [_ {:keys [start]}]
    (dom/div {:class "d3-column-container"
              :style #js {:width (str (+ chart-width 20) "px")
                          :height (str chart-height "px")}}
      (dom/div {:class "chart-prev"
                :style #js {:paddingTop (str (- chart-height 17) "px")
                            :opacity (if (> start 0) 1 0)}
                :on-click #(prev-data owner %)}
        (dom/i {:class "fa fa-caret-left"}))
      (dom/svg #js {:className "d3-column-chart" :ref "d3-column"})
      (dom/div {:class "chart-next"
                :style #js {:paddingTop (str (- chart-height 17) "px")
                            :opacity (if (< start (- (count chart-data) show-columns)) 1 0)}
                :on-click #(next-data owner %)}
        (dom/i {:class "fa fa-caret-right"})))))