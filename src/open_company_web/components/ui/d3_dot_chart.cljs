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

(defn max-y [data chart-keys]
  (let [filtered-data (map #(select-keys % chart-keys) data)]
    (apply max (vec (flatten (map vals filtered-data))))))

(defn scale [owner options]
  (let [all-data (om/get-props owner :chart-data)
        chart-keys (:chart-keys options)
        data-max (max-y all-data chart-keys)
        linear-fn (.. js/d3 -scale linear)
        domain-fn (.domain linear-fn #js [0 data-max])
        range-fn (.range linear-fn #js [0 (- (:chart-height options) 100)])]
    range-fn))

(defn dot-position [chart-width i data-count dots-num]
  (let [dot-spacer (/ (- chart-width 90) (dec show-dots))
        pos (- (* i dot-spacer)
               (/ (* (* dot-radius 2) dots-num) 2)
               -40)]
    pos))

(defn current-data [owner]
  (let [start (om/get-state owner :start)
        all-data (om/get-props owner :chart-data)
        stop (min (count all-data) (+ start show-dots))]
    (subvec all-data start stop)))

(defn dot-click [owner options idx & [is-hover]]
  (.stopPropagation (.-event js/d3))
  (let [selected (om/get-state owner :selected)
        chart-label (.select js/d3 (str "#chart-label"))
        chart-width (:chart-width options)
        data (current-data owner)
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
    (.text chart-label (label-key next-set))
    (let [chart-label-width (width chart-label)
          new-x-pos (- (/ chart-width 2) (/ chart-label-width 2))]
      (.attr chart-label "x" (max 0 new-x-pos)))
    (when-not is-hover
      (om/set-state! owner :selected idx))))

(defn get-y [y max-y]
  (+ 70 (- max-y y)))

(defn d3-calc [owner options]
  (when-let [d3-dots (om/get-ref owner "d3-dots")]
    ; clean the chart area
    (.each (.selectAll (.select js/d3 d3-dots) "*")
           (fn [_ _]
             (this-as el
               (.remove (.select js/d3 el)))))
    ; render the chart
    (let [selected (om/get-state owner :selected)
          chart-data (current-data owner)
          fill-colors (:chart-colors options)
          fill-selected-colors (:chart-selected-colors options)
          chart-width (:chart-width options)
          chart-keys (:chart-keys options)
          ; main chart node
          chart-node (-> js/d3
                         (.select d3-dots)
                         (.attr "width" (:chart-width options))
                         (.attr "height" (:chart-height options))
                         (.on "click" #(.stopPropagation (.-event js/d3))))
          scale-fn (scale owner options)
          data-max (max-y (om/get-props owner :chart-data) chart-keys)
          max-y (scale-fn data-max)
          label-key (:label-key options)]
      ; for each set of data
      (doseq [i (range (count chart-data))]
        (let [data-set (get chart-data i)
              max-val (apply max (vals (select-keys data-set chart-keys)))
              scaled-max-val (scale-fn max-val)
              force-year (or (zero? i) (= i (dec (count chart-data))))
              text (utils/get-period-string (:period data-set) nil [:short (when force-year :force-year)])
              x-pos (dot-position chart-width i (count chart-data) (count chart-keys))
              label (-> chart-node
                        (.append "text")
                        (.attr "class" "chart-x-label")
                        (.attr "x" x-pos)
                        (.attr "y" (:chart-height options))
                        (.attr "fill" (:h-axis-color options))
                        (.on "click" #(dot-click owner options i))
                        (.on "mouseover" #(dot-click owner options i true))
                        (.on "mouseout" #(dot-click owner options (om/get-state owner :selected)))
                        (.text text))
              label-width (width label)]
          (.attr label "x" (+ x-pos (/ (- (* (* dot-radius 2) (count chart-keys)) label-width) 2)))
          ; for each key in the set
          (doseq [j (range (count chart-keys))]
            (let [chart-key (get chart-keys j)
                  cx (dot-position chart-width i (count chart-data) (count chart-keys))
                  cy (scale-fn (chart-key data-set))]
              ; add the line to connect this to the next dot
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
                      (.attr "y1" (get-y cy max-y))
                      (.attr "x2" next-cx)
                      (.attr "y2" (get-y next-cy max-y)))))
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
                  (.attr "cy" (get-y cy max-y)))))))
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
            chart-label-pos (- (/ chart-width 2) (/ chart-label-width 2))]
        (when (> chart-label-width 150)
          (.attr chart-label "class" "chart-label small"))
        (let [new-chart-label-width (width chart-label)
              new-chart-label-pos (- (/ chart-width 2) (/ new-chart-label-width 2))]
          (.attr chart-label "x" (max 0 new-chart-label-pos)))))))

(def chart-step show-dots)

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
        fixed-next-start (min (- (count all-data) show-dots) next-start)]
    (om/set-state! owner :start fixed-next-start)))

(defcomponent d3-dot-chart [{:keys [chart-data] :as data} owner {:keys [chart-width chart-height] :as options}]

  (init-state [_]
    (let [start (max 0 (- (count chart-data) show-dots))
          current-data (vec (take-last show-dots chart-data))]
      {:start start
       :selected (dec (count current-data))}))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (d3-calc owner options)))

  (did-update [_ old-props old-state]
    (when-not (utils/is-test-env?)
      (when (or (not= old-props data) (not= old-state (om/get-state owner)))
        (d3-calc owner options))))

  (render-state [_ {:keys [start]}]
    (dom/div {:class "d3-dot-container"
              :style #js {:width (str (+ chart-width 20) "px")
                          :height (str chart-height "px")}}
      (dom/div {:class "chart-prev"
                :style #js {:paddingTop (str (- chart-height 17) "px")
                            :opacity (if (> start 0) 1 0)}
                :on-click #(prev-data owner %)}
        (dom/i {:class "fa fa-caret-left"}))
      (dom/svg #js {:className "d3-dot-chart" :ref "d3-dots"})
      (dom/div {:class "chart-next"
                :style #js {:paddingTop (str (- chart-height 17) "px")
                            :opacity (if (< start (- (count chart-data) show-dots)) 1 0)}
                :on-click #(next-data owner %)}
        (dom/i {:class "fa fa-caret-right"})))))