(ns open-company-web.components.ui.d3-column-chart
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.finances.utils :as finances-utils]
            [cljsjs.d3]))

; (def bar-width 15)

(def chart-step 5)

(def unselected-opacity "0.25")
(def selected-opacity "0.7")

(defn current-data [owner]
  (let [start (om/get-state owner :start)
        all-data (vec (om/get-props owner :chart-data))
        stop (min (count all-data) (+ start chart-step))]
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

(def bar-spacer 2)

(defn bar-width [chart-width columns-num]
  (/ (- chart-width (* bar-spacer chart-step))
       chart-step))

(defn bar-position [chart-width i data-count columns-num]
  (let [bar-wdt (bar-width chart-width columns-num)
        pos (+ (* bar-wdt i) (* bar-spacer i))]
    pos))

(def chart-label-height 20)

(defn build-selected-label [chart-label-g label-value sub-label-value label-color chart-width]
  (.each (.selectAll chart-label-g "text")
         (fn [_ _]
           (this-as el
             (.remove (.select js/d3 el)))))
  (if (string? label-value)
    ;; Show only one value
    (do
      (-> chart-label-g
          (.append "text")
          (.attr "fill" label-color)
          (.attr "class" "chart-label")
          (.attr "dx" 0)
          (.attr "dy" 0)
          (.text label-value))
      (-> chart-label-g
          (.append "text")
          (.attr "fill" label-color)
          (.attr "class" "sub-chart-label")
          (.attr "dx" 0)
          (.attr "dy" chart-label-height)
          (.text sub-label-value)))
    ;; Show multiple values
    (loop [idx 0
           txt-left 0
           txt-top 0]
      (let [{:keys [label color]} (get label-value idx)
            txt (-> chart-label-g
                    (.append "text")
                    (.attr "fill" color)
                    (.attr "class" "chart-label small")
                    (.attr "dx" txt-left)
                    (.attr "dy" txt-top)
                    (.text label))
            txt-width (js/SVGgetWidth txt)
            txt-height (* idx chart-label-height)]
        (when-not (responsive/is-mobile)
          (.attr txt "dx" txt-left))
        (when (= idx (dec (count label-value)))
          (-> chart-label-g
            (.append "text")
            (.attr "fill" color)
            (.attr "class" "sub-chart-label")
            (.attr "dx" 0)
            (.attr "dy" (+ txt-top chart-label-height))
            (.text (:sub-label (get label-value 0)))))
        (when (< idx (dec (count label-value)))
          (recur (inc idx)
                 0
                 (+ txt-top chart-label-height)))))))

(defn bar-click [owner options idx]
  (.stopPropagation (.-event js/d3))
  (let [svg-el (om/get-ref owner "d3-column")
        d3-svg-el (.select js/d3 svg-el)
        chart-label (.select d3-svg-el (str "#column-chart-label"))
        chart-width (:chart-width options)
        next-g (.select d3-svg-el (str "#chart-g-" idx))
        data (current-data owner)
        next-set (get data idx)
        label-key (:label-key options)
        sub-label-key (:sub-label-key options)
        next-g-rects (.selectAll next-g "rect")
        all-rects (.selectAll d3-svg-el "rect.chart-bar")]
    (.each all-rects
           (fn [d i]
              (this-as rect
                (let [d3-rect (.select js/d3 rect)
                      color (.attr d3-rect "data-fill")]
                  (.attr d3-rect "fill" color)
                  (.style d3-rect "opacity" unselected-opacity)))))
    (.each next-g-rects
           (fn [d i]
              (this-as rect
                (let [d3-rect (.select js/d3 rect)
                      selected-color (.attr d3-rect "data-selectedFill")]
                  (.attr d3-rect "fill" selected-color)
                  (.style d3-rect "opacity" selected-opacity)))))
    (build-selected-label chart-label (get next-set label-key) (get next-set sub-label-key) (:label-color options) chart-width)
    (om/set-state! owner :selected idx)))

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
                         (.on "click" (fn []
                                        (when (:svg-click options)
                                          ((:svg-click options) nil))
                                        (.stopPropagation (.-event js/d3)))))
          scale-fn (scale owner options)
          label-key (:label-key options)
          sub-label-key (:sub-label-key options)]
      ; for each set of data
      (when (> (count chart-data) 1)
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
                                          (- (:chart-height options) scaled-max-val) ")")))]
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
                    (.style "opacity" (if (= i selected)
                                        selected-opacity
                                        unselected-opacity))
                    (.attr "data-fill" color)
                    (.attr "data-selectedFill" selected-color)
                    (.attr "data-hasvalue" value)
                    (.attr "id" (str "chart-bar-" (name chart-key) "-" i))
                    (.attr "x" (* j (/ (bar-width chart-width keys-count) keys-count)))
                    (.attr "y" (- scaled-max-val scaled-val))
                    (.attr "width" (/ (bar-width chart-width keys-count) keys-count))
                    (.attr "height" (max 0 scaled-val))))))))
      ; add the hovering rects
      (when (> (count chart-data) 1)
        (doseq [i (range (count chart-data))]
          (-> chart-node
              (.append "rect")
              (.attr "class" "hover-rect")
              (.attr "width" (/ chart-width chart-step))
              (.attr "height" (- chart-height 50))
              (.attr "x" (* i (/ chart-width chart-step)))
              (.attr "y" 50)
              (.on "mouseover" #(bar-click owner options i))
              (.on "mouseout" #(bar-click owner options (om/get-state owner :selected)))
              (.attr "fill" "transparent"))))
      ; add the selected value label
      (let [x-pos (/ chart-width 2)
            selected-data-set (get chart-data selected)
            label-value (get selected-data-set label-key)
            sub-label-value (get selected-data-set sub-label-key)
            label-color (:label-color options)
            chart-label-g (-> chart-node
                              (.append "g")
                              (.attr "class" "chart-label-container")
                              (.attr "id" "column-chart-label")
                              (.attr "transform" (str "translate(" 0 "," 0 ")")))]
        (build-selected-label chart-label-g label-value sub-label-value label-color chart-width)
        (let [chart-label-width (js/SVGgetWidth chart-label-g)
              chart-label-pos (- (/ chart-width 2) (/ chart-label-width 2))]
          (.attr chart-label-g "transform" (str "translate("
                                                0
                                                ","
                                                20
                                                ")")))))))

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
        fixed-next-start (min (- (count all-data) chart-step) next-start)]
    (om/set-state! owner :start fixed-next-start)))

(defn get-state [chart-data]
  (let [start (max 0 (- (count chart-data) chart-step))
        current-data (vec (take-last chart-step chart-data))]
    {:start start
     :selected (dec (count current-data))}))

(defcomponent d3-column-chart [{:keys [chart-data] :as data} owner {:keys [chart-width chart-height svg-click] :as options}]

  (init-state [_]
    (get-state chart-data))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (d3-calc owner options)))

  (did-update [_ old-props old-state]
    (when-not (utils/is-test-env?)
      (when (or (not= old-props data)
                 (not= old-state (om/get-state owner)))
        (when-not (= old-props data)
          (let [new-state (get-state chart-data)]
            (om/set-state! owner :start (:start new-state))
            (om/set-state! owner :selected (:selected new-state))))
        (d3-calc owner options))))

  (render-state [_ {:keys [start]}]
    (let [fixed-chart-height (if (> (count chart-data) 1)
                              chart-height
                              90)]
      (dom/div {:class "d3-column-container"
                :style #js {:width (str (+ chart-width 20) "px")
                            :height (str fixed-chart-height "px")}}
        (dom/div {:class "chart-prev"
                  :style #js {:paddingTop (str (- fixed-chart-height 17) "px")
                              :opacity (if (> start 0) 1 0)}
                  :on-click #(prev-data owner %)}
          (dom/i {:class "fa fa-caret-left"}))
        (dom/svg #js {:className "d3-column-chart" :ref "d3-column"})
        (dom/div {:class "chart-next"
                  :style #js {:paddingTop (str (- fixed-chart-height 17) "px")
                              :opacity (if (< start (- (count chart-data) chart-step)) 1 0)}
                  :on-click #(next-data owner %)}
          (dom/i {:class "fa fa-caret-right"}))))))