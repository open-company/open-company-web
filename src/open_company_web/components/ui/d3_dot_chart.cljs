(ns open-company-web.components.ui.d3-dot-chart
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.finances.utils :as finances-utils]
            [cljsjs.d3]))

(def dot-radius 5)

(def show-dots 6)

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

(defn dot-position [chart-width i data-count dots-num]
  (let [dot-spacer (/ chart-width (inc (min show-dots data-count)))]
    (- (* (inc i) dot-spacer) (/ (* (* dot-radius 2) dots-num) 2))))

(defn dot-click [owner options idx & [is-hover]]
  (.stopPropagation (.-event js/d3))
  (let [selected (om/get-state owner :selected)
        chart-label (.select js/d3 (str "#chart-label"))
        chart-width (:chart-width options)
        data (om/get-state owner :current-data)
        next-set (get data idx)
        label-key (:label-key options)
        data-count (count data)
        chart-keys-count (count (:chart-keys options))
        label-x-pos (dot-position chart-width idx data-count chart-keys-count)
        next-circle (.select js/d3 (str "circle#chart-dot-" idx))
        all-circles (.selectAll js/d3 "circle")]
    (.each all-circles (fn [d i]
                        (this-as circle-node
                          (let [circle (.select js/d3 circle-node)
                                color (.attr circle "data-fill")]
                            (-> circle
                                (.attr "fill" color)
                                (.attr "r" dot-radius))))))
    (let [selected-color (.attr next-circle "data-selectedFill")]
      (-> next-circle
          (.attr "fill" selected-color)
          (.attr "r" (* dot-radius 1.5))))
    (-> chart-label
      (.text (label-key next-set)))
    (let [chart-label-width (width chart-label)
          new-x-pos (+ label-x-pos (/ (- (* (* dot-radius 2) chart-keys-count) chart-label-width) 2))]
      (.attr chart-label "x" (min (max 0 new-x-pos) (- (:chart-width options) chart-label-width))))
    (when-not is-hover
      (om/set-state! owner :selected idx))))

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
                       (.select (om/get-ref owner "d3-dot"))
                       (.attr "width" (:chart-width options))
                       (.attr "height" (:chart-height options))
                       (.on "click" #(.stopPropagation (.-event js/d3))))
        scale-fn (scale owner options)
        label-key (:label-key options)]
    ; for each set of data
    (doseq [i (range (count chart-data))]
      (let [data-set (get chart-data i)
            max-val (apply max (vals (select-keys data-set chart-keys)))
            scaled-max-val (scale-fn max-val)]
        ; for each key in the set
        (doseq [j (range (count chart-keys))]
          (let [chart-key (get chart-keys j)
                cx (dot-position chart-width i (count chart-data) (count chart-keys))
                cy (scale-fn (chart-key data-set))]
            ; add a rect to represent the data
            (-> chart-node
                (.append "circle")
                (.attr "class" "chart-dot")
                (.attr "r" (if (= i selected)
                              (* dot-radius 1.5)
                              dot-radius))
                (.attr "fill" (if (= i selected)
                                (chart-key fill-selected-colors)
                                (chart-key fill-colors)))
                (.on "click" #(dot-click owner options i))
                (.on "mouseover" #(dot-click owner options i true))
                (.on "mouseout" #(dot-click owner options (om/get-state owner :selected)))
                (.attr "data-fill" (chart-key fill-colors))
                (.attr "data-selectedFill" (chart-key fill-selected-colors))
                (.attr "id" (str "chart-dot-" i))
                (.attr "cx" cx)
                (.attr "cy" cy))
            (when (< i (dec (count chart-data)))
              (let [next-data-set (get chart-data (inc i))
                    next-scaled-val (scale-fn (chart-key next-data-set))
                    next-cx (dot-position chart-width (inc i) (count chart-data) (count chart-keys))
                    next-cy (scale-fn (chart-key next-data-set))]
                (-> chart-node
                    (.append "line")
                    (.attr "class" "chart-line")
                    (.style "stroke" (chart-key fill-colors))
                    (.style "stroke-width" 2)
                    (.attr "x1" cx)
                    (.attr "y1" cy)
                    (.attr "x2" next-cx)
                    (.attr "y2" next-cy))))))))
    ; add the month labels
    (doseq [v chart-data]
      (let [idx (.indexOf (to-array chart-data) v)
            text (utils/get-period-string (:period (get chart-data idx)) nil [:short])
            x-pos (dot-position chart-width idx (count chart-data) (count chart-keys))
            label (-> chart-node
                      (.append "text")
                      (.attr "class" "chart-x-label")
                      (.attr "x" x-pos)
                      (.attr "y" (:chart-height options))
                      (.attr "fill" (:h-axis-color options))
                      (.on "click" #(dot-click owner options idx))
                      (.on "mouseover" #(dot-click owner options idx true))
                      (.on "mouseout" #(dot-click owner options (om/get-state owner :selected)))
                      (.text text))
            label-width (width label)]
        (.attr label "x" (+ x-pos (/ (- (* (* dot-radius 2) (count chart-keys)) label-width) 2)))))
    ; add the selected value label
    (let [x-pos (dot-position chart-width selected (count chart-data) (count chart-keys))
          chart-label (-> chart-node
                          (.append "text")
                          (.attr "class" "chart-label")
                          (.attr "id" "chart-label")
                          (.attr "x" x-pos)
                          (.attr "y" 50)
                          (.attr "fill" (:label-color options))
                          (.text (label-key (get chart-data selected))))
          chart-label-width (width chart-label)
          chart-label-pos (+ x-pos (/ (- (* (* dot-radius 2) (count chart-keys)) chart-label-width) 2))]
      (when (> chart-label-width 150)
        (.attr chart-label "class" "chart-label small"))
      (let [new-chart-label-width (width chart-label)
            new-chart-label-pos (+ x-pos (/ (- (* (* dot-radius 2) (count chart-keys)) new-chart-label-width) 2))]
        (.attr chart-label "x" (min (max 0 new-chart-label-pos) (- chart-width new-chart-label-width)))))))

(defcomponent d3-dot-chart [data owner options]

  (init-state [_]
    (let [current-data (vec (take-last show-dots (:chart-data data)))]
      {:selected (dec (count current-data))
       :current-data current-data}))

  (did-mount [_]
    (d3-calc owner options))

  (did-update [_ old-props _]
    (when-not (= old-props data)
      (om/set-state! owner (take-last show-dots (:chart-data data)))
      (d3-calc owner options)))

  (render [_]
    (dom/div {:class "d3-dot-container"
              :style #js {:width (str (:chart-width options) "px")
                          :height (str (:chart-height options) "px")}}
      (dom/svg #js {:className "d3-dot-chart" :ref "d3-dot"}))))