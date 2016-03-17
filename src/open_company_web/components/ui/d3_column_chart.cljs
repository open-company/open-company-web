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
  (.-width (.getBBox (.node el))))

(defn fix-chart-label-position []
  (let [chart-label (.select js/d3 "#chart-label")
        label-width (width chart-label)]
    (.attr chart-label "dx" (str "-" (- (/ label-width 2) 10) "px"))))

(defn get-formatted-data [chart-key value prefix]
  (if (= chart-key :runway)
    (finances-utils/get-rounded-runway value [:round])
    (str prefix (.toLocaleString (js/parseFloat (str value))))))

(defn get-max [owner]
  (let [all-data (om/get-props owner :chart-data)
        flatten-data (flatten (map vals all-data))]))

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
  (let [bar-spacer (/ chart-width (inc (min show-columns data-count)))]
    (- (* (inc i) bar-spacer) (/ (* bar-width columns-num) 2))))

(defn bar-click [owner options idx]
  (.stopPropagation (.-event js/d3))
  (let [selected (om/get-state owner :selected)
        chart-label (.select js/d3 (str "#chart-label"))
        chart-width (:chart-width options)
        cur-g (.select js/d3 (str "#chart-g-" selected))
        next-g (.select js/d3 (str "#chart-g-" idx))
        data (om/get-state owner :current-data)
        next-set (get data idx)
        label-key (:label-key options)
        data-count (count data)
        chart-keys-count (count (:chart-keys options))
        label-x-pos (bar-position chart-width idx data-count chart-keys-count)
        next-g-rects (.selectAll next-g "rect")
        cur-g-rects (.selectAll cur-g "rect")]
    (.each cur-g-rects
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
    (-> chart-label
      (.text (label-key next-set)))
    (let [chart-label-width (width chart-label)
          new-x-pos (+ label-x-pos (/ (- (* bar-width chart-keys-count) chart-label-width) 2))]
      (.attr chart-label "x" (min (max 0 new-x-pos) (- (:chart-width options) chart-label-width))))
    (om/set-state! owner :selected idx)))

(defn d3-calc [owner options]
  ; render the chart
  (let [selected (om/get-state owner :selected)
        chart-data (om/get-state owner :current-data)
        fill-colors (:chart-colors options)
        fill-selected-colors (:chart-selected-colors options)
        chart-width (:chart-width options)
        chart-keys (:chart-keys options)
        ; main chart node
        chart-node (-> js/d3
                       (.select (om/get-ref owner "d3-column"))
                       (.attr "width" (:chart-width options))
                       (.attr "height" (:chart-height options))
                       (.on "click" #(.stopPropagation (.-event js/d3))))
        scale-fn (scale owner options)
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
                          (.on "click" #(bar-click owner options i))
                          (.attr "transform"
                                 (str "translate("
                                      (bar-position chart-width i (count chart-data) (count chart-keys))
                                      ","
                                      (- (:chart-height options) scaled-max-val 20) ")")))]
        ; for each key in the set
        (doseq [j (range (count chart-keys))]
          (let [chart-key (get chart-keys j)
                scaled-val (scale-fn (chart-key data-set))]
            ; add a rect to represent the data
            (-> bar-enter
                (.append "rect")
                (.attr "class" "chart-bar")
                (.attr "fill" (if (= i selected)
                                (chart-key fill-selected-colors)
                                (chart-key fill-colors)))
                (.attr "data-fill" (chart-key fill-colors))
                (.attr "data-selectedFill" (chart-key fill-selected-colors))
                (.attr "id" (str "chart-bar-" (name chart-key) "-" i))
                (.attr "x" (* j bar-width))
                (.attr "y" (- scaled-max-val scaled-val))
                (.attr "width" bar-width)
                (.attr "height" (max 0 (scale-fn (chart-key (get chart-data i))))))))))
    ; add the month labels
    (doseq [v chart-data]
      (let [idx (.indexOf (to-array chart-data) v)
            text (utils/get-period-string (:period (get chart-data idx)) nil [:short])
            x-pos (bar-position chart-width idx (count chart-data) (count chart-keys))
            label (-> chart-node
                      (.append "text")
                      (.attr "class" "chart-x-label")
                      (.attr "x" x-pos)
                      (.attr "y" (:chart-height options))
                      (.attr "fill" (:h-axis-color options))
                      (.text text))
            label-width (width label)]
        (.attr label "x" (+ x-pos (/ (- (* bar-width (count chart-keys)) label-width) 2)))))
    ; add the selected value label
    (let [x-pos (bar-position chart-width selected (count chart-data) (count chart-keys))
          chart-label (-> chart-node
                          (.append "text")
                          (.attr "class" "chart-label")
                          (.attr "id" "chart-label")
                          (.attr "x" x-pos)
                          (.attr "y" 50)
                          (.attr "fill" (:label-color options))
                          (.text (label-key (get chart-data selected))))
          chart-label-width (width chart-label)
          chart-label-pos (+ x-pos (/ (- (* bar-width (count chart-keys)) chart-label-width) 2))]
      (when (> chart-label-width 150)
        (.attr chart-label "class" "chart-label small"))
      (let [new-chart-label-width (width chart-label)
            new-chart-label-pos (+ x-pos (/ (- (* bar-width (count chart-keys)) new-chart-label-width) 2))]
        (.attr chart-label "x" (min (max 0 new-chart-label-pos) (- chart-width new-chart-label-width)))))))

(defcomponent d3-column-chart [data owner options]

  (init-state [_]
    (let [current-data (vec (take-last show-columns (:chart-data data)))]
      {:selected (dec (count current-data))
       :current-data current-data}))

  (did-mount [_]
    (d3-calc owner options))

  (did-update [_ old-props _]
    (when-not (= old-props data)
      (om/set-state! owner (take-last show-columns (:chart-data data)))
      (d3-calc owner options)))

  (render [_]
    (dom/div {:class "d3-column-container"
              :style #js {:width (str (:chart-width options) "px")
                          :height (str (:chart-height options) "px")}}
      (dom/svg #js {:className "d3-column-chart" :ref "d3-column"}))))