(ns open-company-web.components.ui.d3-chart
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]
            [cljsjs.d3]))

;; ===== Graph Layout =====

(def circle-radius 5)
(def circle-stroke 3)
(def circle-selected-stroke 6)

(def show-data-points 4)
(def chart-step show-data-points)

(defn- max-y [data chart-keys]
  (let [filtered-data (map #(select-keys % chart-keys) data)]
    (apply max (vec (flatten (map vals filtered-data))))))

(defn- get-y [y max-y]
  (+ 10 (- max-y y)))

(defn- scale [owner options]
  (let [all-data (om/get-props owner :chart-data)
        chart-keys (:chart-keys options)
        data-max (max-y all-data chart-keys)
        linear-fn (.. js/d3 -scale linear)
        domain-fn (.domain linear-fn #js [0 data-max])
        range-fn (.range linear-fn #js [0 (- (:chart-height options) 35)])]
    range-fn))

(defn- dot-position [chart-width i]
  (let [dot-spacer (/ (- chart-width 20) (dec show-data-points))]
    (+ (* i dot-spacer) 10)))

(defn- current-data 
  "Get the subset of the data that's currently being displayed on the chart."
  [owner]
  (let [start (om/get-state owner :start)
        all-data (om/get-props owner :chart-data)
        stop (min (count all-data) (+ start show-data-points))]
    (subvec all-data start stop)))

(declare data-select)
(defn- d3-calc [owner options]
  (when-let [d3-dots (om/get-ref owner "d3-dots")]
    ;; clean the chart area
    (.each (.selectAll (.select js/d3 d3-dots) "*")
           (fn [_ _]
             (this-as el
               (.remove (.select js/d3 el)))))
    ;; render the chart
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
                         (.attr "width" chart-width)
                         (.attr "height" chart-height)
                         (.on "click" (fn []
                                        (when (:svg-click options)
                                          ((:svg-click options) nil))
                                        (.stopPropagation (.-event js/d3)))))
          scale-fn (scale owner options)
          data-max (max-y (om/get-props owner :chart-data) chart-keys)
          max-y (scale-fn data-max)
          x-axis-labels? (:x-axis-labels options)]

      (when (> (count chart-data) 1) ; when there is any data to chart
        (doseq [i (range (count chart-data))] ; for each data point in the dataset
          (let [data-set (get chart-data i)]
            ;; for each key in the set
            (doseq [j (range (count chart-keys))]
              (let [chart-key (get chart-keys j)
                    cx (dot-position chart-width i)
                    cy (scale-fn (chart-key data-set))]
                
                ;; add the line to connect this to the next dot and a polygon below the lines
                (when (and (chart-key data-set)
                           (< i (dec (count chart-data))))
                  (let [next-data-set (get chart-data (inc i))
                        next-cx (dot-position chart-width (inc i))
                        next-cy (scale-fn (chart-key next-data-set))]
                    (when (chart-key next-data-set)
                      ;; polygon below line
                      (when (:chart-fill-polygons options)
                        (-> chart-node
                            (.append "polygon")
                            (.attr "class" "chart-polygon")
                            (.style "fill" (chart-key fill-colors))
                            (.style "opacity" "0.35")
                            (.attr "points"
                              (str (inc cx) "," (get-y cy max-y) " "
                                   (dec next-cx) "," (get-y next-cy max-y) " "
                                   (dec next-cx) "," chart-height " "
                                   (inc cx) "," chart-height " "))))
                      ;; connecting line
                      (-> chart-node
                          (.append "line")
                          (.attr "class" "chart-line")
                          (.style "stroke" (chart-key fill-colors))
                          (.style "stroke-width" circle-stroke)
                          (.attr "x1" (+ cx 2))
                          (.attr "y1" (get-y cy max-y))
                          (.attr "x2" (- next-cx 2))
                          (.attr "y2" (get-y next-cy max-y))))))
                ;; add a circle to represent the data
                (-> chart-node
                    (.append "circle")
                    (.attr "class" (str "chart-dot chart-dot-" i))
                    (.attr "r" (if (not (chart-key data-set))
                                0
                                circle-radius))
                    (.attr "stroke" (chart-key fill-colors))
                    (.attr "stroke-width" (if (= i selected) circle-selected-stroke circle-stroke))
                    (.attr "fill" "white")
                    (.attr "data-fill" (chart-key fill-colors))
                    (.attr "data-hasvalue" (chart-key data-set))
                    (.attr "data-selectedFill" (chart-key fill-selected-colors))
                    (.attr "id" (str "chart-dot-" chart-key "-" i))
                    (.attr "cx" cx)
                    (.attr "cy" (get-y cy max-y))))))))
      ;; add the hovering rects
      (when (> (count chart-data) 1)
        (doseq [i (range (count chart-data))]
          (-> chart-node
            (.append "rect")
            (.attr "class" "hover-rect")
            (.attr "width" (/ chart-width show-data-points))
            (.attr "height" chart-height)
            (.attr "x" (* i (/ chart-width show-data-points)))
            (.attr "y" 0)
            (.on "mouseover" #(data-select owner options i))
            (.on "mouseout" #(data-select owner options (om/get-state owner :selected)))
            (.attr "fill" "transparent"))))

      ;; add the x-axis labels
      (when x-axis-labels?
        (doseq [i (range (count chart-data))]
           (let [period (:period (get (vec chart-data) i))
                 interval (:interval options)
                 label (utils/get-period-string period interval [:short])]
              (-> chart-node
                (.append "text")
                (.attr "class" (str "x-axis-label" (if (= i selected) " selected")))
                (.attr "x" (- (dot-position chart-width i) 10))
                (.attr "y" (- chart-height 2))
                (.text label))))))))

;; ===== Graph Events =====

(defn- data-select 
  "
  Handle graph event that (may) select a new point on the graph.

  Redraw the SVG data point circles so selected and unselected circles 
  can be drawn differently, and update the `selected` component state.
  "
  [owner options idx]
  (.stopPropagation (.-event js/d3)) ; we got this!
  (let [svg-el (om/get-ref owner "d3-dots")
        d3-svg-el (.select js/d3 svg-el)
        chart-width (:chart-width options)
        selected-circles (.selectAll d3-svg-el (str "circle.chart-dot-" idx))
        all-circles (.selectAll d3-svg-el "circle")]
    
    ;; draw all the circles normally
    (.each all-circles (fn [d i]
      (this-as circle-node
        (let [circle (.select js/d3 circle-node)
              color (.attr circle "data-fill")
              hasvalue (.attr circle "data-hasvalue")]
          (-> circle
              (.attr "stroke" color)
              (.attr "stroke-width" circle-stroke)
              (.attr "fill" "white")
              (.attr "r" (if hasvalue circle-radius 0)))))))
    
    ;; draw the selected circles specially
    (.each selected-circles (fn [d i]
      (this-as circle-node
        (let [circle (.select js/d3 circle-node)
              color (.attr circle "data-fill")
              hasvalue (.attr circle "data-hasvalue")]
          (-> circle
              (.attr "stroke" color)
              (.attr "stroke-width" circle-selected-stroke)
              (.attr "fill" "white")
              (.attr "r" (if hasvalue circle-radius 0)))))))

    ;; let the rest of the component know a (potentially) new data point is selected
    (om/set-state! owner :selected idx)))

(defn- prev-data [owner e]
  (.stopPropagation e) ; we got this!
  (let [start (om/get-state owner :start)
        next-start (- start chart-step)
        fixed-next-start (max 0 next-start)]
    (om/set-state! owner :start fixed-next-start)))

(defn- next-data [owner e]
  (.stopPropagation e) ; we got this!
  (let [start (om/get-state owner :start)
        all-data (om/get-props owner :chart-data)
        next-start (+ start chart-step)
        fixed-next-start (min (- (count all-data) show-data-points) next-start)]
    (om/set-state! owner :start fixed-next-start)))

;; ===== Utility Functions =====

(defn- label-keys-for
  "Return the keys of the labels, in order, for the specified position."
  [labels position]
  (->> (keys labels)
    (filter #(= position (get-in labels [% :position])))
    (sort-by #(get-in labels [% :order]))))

(defn- labels-for
  "Create a container of chart labels for a specified data set."
  [class-name label-keys labels data]
  (let [center? (> (count label-keys) 1)
        multiple-rows? (> (count (keys labels)) 3)]
    (dom/div {:class (str class-name (if multiple-rows? " multiple-rows"))
              :style (if center? 
                        {:text-align :center :align-items :center :justify-content :center}
                        {:align-items :flex-start :justify-content :flex-start :padding-left "20px"})}
      (for [label-key label-keys]
        (dom/div {:class "chart-labels"}
          (dom/div {:class "chart-value"
                    :style {:color (get-in labels [label-key :value-color])}}
            ((get-in labels [label-key :value-presenter]) label-key data))
          (dom/div {:class "chart-label"
                    :style {:color (get-in labels [label-key :label-color])
                            :text-align (if center? "center" "left")}}
            ((get-in labels [label-key :label-presenter]) label-key data)))))))

;; ===== D3 Chart Component =====

(defcomponent d3-chart [{:keys [chart-data] :as data} owner {:keys [chart-width chart-height] :as options}]

  (init-state [_]
    (let [start (max 0 (- (count chart-data) show-data-points))
          current-data (vec (take-last show-data-points chart-data))]
      {:start start
       :selected (dec (count current-data))}))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (d3-calc owner options)))

  (did-update [_ old-props old-state]
    (when-not (utils/is-test-env?)
      (when (or (not= old-props data) (not= old-state (om/get-state owner)))
        (d3-calc owner options))))

  (render-state [_ {:keys [start selected]}]

    (let [fixed-chart-height (if (> (count chart-data) 1)
                              chart-height
                              90)
          hide-chart-nav (:hide-nav options)
          selected-data-set (get (current-data owner) selected)
          labels (:labels options)
          top-label-keys (label-keys-for labels :top)
          bottom-label-keys (label-keys-for labels :bottom)
          chart-type (:chart-type options)]

      (dom/div {:class chart-type}
        
        ;; Top row labels
        (when (not (empty? top-label-keys))
          (labels-for "chart-top-label-container" top-label-keys labels selected-data-set))

        ;; Bottom row labels
        (when (not (empty? bottom-label-keys))
          (labels-for "chart-bottom-label-container" bottom-label-keys labels selected-data-set))

        ;; D3 Chart w/ optional nav. buttons
        (when (> (count chart-data) 1)
          (dom/div {:class "chart-container"
                    :style #js {:width (str (+ chart-width 20) "px")
                                :height (str fixed-chart-height "px")}}
            ;; Previous button
            (dom/div {:class (str "chart-prev" (when hide-chart-nav " hidden"))
                      :style #js {:paddingTop (str (- fixed-chart-height 17) "px")
                                  :opacity (if (> start 0) 1 0)}
                      :on-click #(prev-data owner %)}
              (dom/i {:class "fa fa-caret-left"}))
            
            ;; Chart
            (dom/svg #js {:className "d3-chart"
                          :ref "d3-dots"
                          :style #js {:marginLeft (str (if hide-chart-nav 10 0) "px")}})
            
            ;; Next button
            (dom/div {:class (str "chart-next" (when hide-chart-nav " hidden"))
                      :style #js {:paddingTop (str (- fixed-chart-height 17) "px")
                                  :opacity (if (< start (- (count chart-data) show-data-points)) 1 0)}
                      :on-click #(next-data owner %)}
              (dom/i {:class "fa fa-caret-right"}))))))))