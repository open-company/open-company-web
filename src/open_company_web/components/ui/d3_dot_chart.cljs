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
  (let [dot-spacer (/ (- chart-width 20) data-count)]
    (+ (/ dot-spacer 2)
       (* i dot-spacer)
       10)))

(defn current-data [owner]
  (let [start (om/get-state owner :start)
        all-data (om/get-props owner :chart-data)
        stop (min (count all-data) (+ start show-dots))]
    (subvec all-data start stop)))

(defn dot-click [owner options idx & [is-hover]]
  (.stopPropagation (.-event js/d3))
  (let [svg-el (om/get-ref owner "d3-dots")
        d3-svg-el (.select js/d3 svg-el)
        selected (om/get-state owner :selected)
        chart-label (.select d3-svg-el (str "#dot-chart-label"))
        chart-width (:chart-width options)
        data (current-data owner)
        next-set (get data idx)
        label-key (:label-key options)
        data-count (count data)
        chart-keys-count (count (:chart-keys options))
        label-x-pos (dot-position chart-width idx data-count chart-keys-count)
        next-circle (.select d3-svg-el (str "circle#chart-dot-" idx))
        all-circles (.selectAll d3-svg-el "circle")
        next-month-text (.select d3-svg-el (str "text#chart-x-label-" idx))
        all-month-text (.selectAll d3-svg-el ".chart-x-label")]
    (.each all-circles (fn [d i]
                        (this-as circle-node
                          (let [circle (.select js/d3 circle-node)
                                color (.attr circle "data-fill")
                                hasvalue (.attr circle "data-hasvalue")]
                            (-> circle
                                (.attr "fill" color)
                                (.attr "r" (if hasvalue dot-radius 0)))))))
    (let [selected-color (.attr next-circle "data-selectedFill")
          hasvalue (.attr next-circle "data-hasvalue")]
      (-> next-circle
          (.attr "fill" selected-color)
          (.attr "r" (if hasvalue (* dot-radius 1.5) 0))))
    (.text chart-label (label-key next-set))
    (.each all-month-text
           (fn [d i]
              (this-as month-text
                (let [d3-month-text (.select js/d3 month-text)]
                  (.attr d3-month-text "fill" (:h-axis-color options))))))
    (.attr next-month-text "fill" (:h-axis-selected-color options))
    (let [chart-label-width (js/SVGgetWidth chart-label)
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
          chart-height (:chart-height options)
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
          h-axis-color (:h-axis-color options)
          h-axis-selected-color (:h-axis-selected-color options)
          label-key (:label-key options)]
      ; for each set of data
      (doseq [i (range (count chart-data))]
        (let [data-set (get chart-data i)
              max-val (apply max (vals (select-keys data-set chart-keys)))
              scaled-max-val (scale-fn max-val)
              force-year (or (zero? i) (= i (dec (count chart-data))))
              text (utils/get-period-string (:period data-set) (:interval options) [:short (when force-year :force-year) (when (utils/is-mobile) :short-year)])
              x-pos (dot-position chart-width i (count chart-data) (count chart-keys))
              label (-> chart-node
                        (.append "text")
                        (.attr "class" "chart-x-label")
                        (.attr "id" (str "chart-x-label-" i))
                        (.attr "x" x-pos)
                        (.attr "y" (- (:chart-height options) 5))
                        (.attr "fill" (if (= i selected) h-axis-selected-color h-axis-color))
                        (.text text))
              label-width (js/SVGgetWidth label)]
          (.attr label "x" (+ x-pos (/ (- (* (* dot-radius 2) (count chart-keys)) label-width) 2)))
          ; for each key in the set
          (doseq [j (range (count chart-keys))]
            (let [chart-key (get chart-keys j)
                  cx (dot-position chart-width i (count chart-data) (count chart-keys))
                  cy (scale-fn (chart-key data-set))]
              ; add the line to connect this to the next dot
              (when (and (chart-key data-set)
                         (< i (dec (count chart-data))))
                (let [next-data-set (get chart-data (inc i))
                      next-cx (dot-position chart-width (inc i) (count chart-data) (count chart-keys))
                      next-cy (scale-fn (chart-key next-data-set))]
                  (when (chart-key next-data-set)
                    (-> chart-node
                        (.append "line")
                        (.attr "class" "chart-line")
                        (.style "stroke" (chart-key fill-colors))
                        (.style "stroke-width" 2)
                        (.attr "x1" cx)
                        (.attr "y1" (get-y cy max-y))
                        (.attr "x2" next-cx)
                        (.attr "y2" (get-y next-cy max-y))))))
              ; add a rect to represent the data
              (-> chart-node
                  (.append "circle")
                  (.attr "class" "chart-dot")
                  (.attr "r" (if (not (chart-key data-set))
                              0
                              (if (= i selected)
                                (* dot-radius 1.5)
                                dot-radius)))
                  (.attr "fill" (if (= i selected)
                                  (chart-key fill-selected-colors)
                                  (chart-key fill-colors)))
                  (.attr "data-fill" (chart-key fill-colors))
                  (.attr "data-hasvalue" (chart-key data-set))
                  (.attr "data-selectedFill" (chart-key fill-selected-colors))
                  (.attr "id" (str "chart-dot-" i))
                  (.attr "cx" cx)
                  (.attr "cy" (get-y cy max-y)))))))
      ; add the hovering rects
      (doseq [i (range (count chart-data))]
        (-> chart-node
            (.append "rect")
            (.attr "class" "hover-rect")
            (.attr "width" (/ chart-width (count chart-data)))
            (.attr "height" (- chart-height 50))
            (.attr "x" (* i (/ chart-width (count chart-data))))
            (.attr "y" 50)
            (.on "click" #(dot-click owner options i false))
            (.on "mouseover" #(dot-click owner options i true))
            (.on "mouseout" #(dot-click owner options (om/get-state owner :selected) true))
            (.attr "fill" "transparent")))
      ; add the selected value label
      (let [x-pos (dot-position chart-width selected (count chart-data) (count chart-keys))
            chart-label (-> chart-node
                            (.append "text")
                            (.attr "class" "dot-chart-label")
                            (.attr "id" "dot-chart-label")
                            (.attr "x" x-pos)
                            (.attr "y" 50)
                            (.attr "fill" (:label-color options))
                            (.text (label-key (get chart-data selected))))
            chart-label-width (js/SVGgetWidth chart-label)
            chart-label-pos (- (/ chart-width 2) (/ chart-label-width 2))]
        (when (> chart-label-width 150)
          (.attr chart-label "class" "dot-chart-label small"))
        (let [new-chart-label-width (js/SVGgetWidth chart-label)
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