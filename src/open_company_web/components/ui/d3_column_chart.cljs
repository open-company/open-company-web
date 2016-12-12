(ns open-company-web.components.ui.d3-column-chart
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]
            [cljsjs.d3]))

;; ===== Graph Layout =====

(def bar-width 12)

(def show-data-points 4)
(def chart-step show-data-points)

(defn- current-data
  "Get the subset of the data that's currently being displayed on the chart."
  [owner]
  (let [start (om/get-state owner :start)
        all-data (om/get-props owner :chart-data)
        stop (min (count all-data) (+ start show-data-points))]
    (subvec all-data start stop)))

(defn- max-y [owner data chart-keys]
  (let [filtered-data (map #(select-keys % chart-keys) (current-data owner))]
    (apply max (vec (flatten (map vals filtered-data))))))

(defn- min-y [owner data chart-keys]
  (let [filtered-data (map #(select-keys % chart-keys) (current-data owner))]
    (apply min (vec (flatten (map vals filtered-data))))))

(defn- get-y [y max-y]
  (+ 10 (- max-y y)))

(defn- scale [owner options]
  (let [all-data (current-data owner)
        chart-keys (:chart-keys options)
        data-min (min-y owner all-data chart-keys)
        data-max (max-y owner all-data chart-keys)
        linear-fn (.. js/d3 -scale linear)
        domain-fn (.domain linear-fn #js [0 data-max])
        range-fn (.range linear-fn #js [0 (- (om/get-props owner :chart-height) 10)])]
    range-fn))

(defn- data-x-position
  "Given the width of the chart and an index of a data point within the displayed data set
  return the horizontal (x-axis) position of the data point."
  [chart-width i num-of-keys]
    (* i (+ (* bar-width num-of-keys) (* (/ bar-width 2) num-of-keys))))

(declare data-select)
(defn- d3-render-chart
  "Render a chart in SVG using D3 for the provided data and start position in the data."
  [owner options]
  (when-let [d3-chart (om/get-ref owner "d3-chart")]
    ;; clean the chart area
    (.each (.selectAll (.select js/d3 d3-chart) "*")
           (fn [_ _]
             (this-as el
               (.remove (.select js/d3 el)))))
    ;; render the chart
    (let [selected (om/get-state owner :selected)
          chart-data (current-data owner)
          data-count (count chart-data)
          fill-colors (:chart-colors options)
          fill-selected-colors (:chart-selected-colors options)
          chart-width (+ (om/get-props owner :chart-width) (if (:hide-nav options) 30 10))
          chart-height (om/get-props owner :chart-height)
          chart-keys (:chart-keys options)
          ; main chart node
          chart-node (-> js/d3
                         (.select d3-chart)
                         ; Make SVG 10px wider to give 5px on each side for month label overrun
                         (.attr "width" chart-width)
                         (.attr "height" chart-height)
                         (.on "click" (fn [] (.stopPropagation (.-event js/d3)))))
          scale-fn (scale owner options)
          data-max (max-y owner (om/get-props owner :chart-data) chart-keys)
          max-y (scale-fn data-max)
          x-axis-labels? (:x-axis-labels options)]

      (when (> (count chart-data) 1) ; when there is any data to chart
        (doseq [i (range (count chart-data))] ; for each data point in the dataset
          (let [data-set (get chart-data i)]
            ;; for each key in the set
            (doseq [j (range (count chart-keys))]
              (let [chart-key (get chart-keys j)
                    cx (data-x-position chart-width i (count chart-keys))
                    cy (scale-fn (chart-key data-set))]

                (-> chart-node
                    (.append "rect")
                    (.attr "class" (str "chart-bar chart-bar-" i))
                    (.attr "fill" (if (= i selected) (chart-key fill-selected-colors) (chart-key fill-colors)))
                    (.attr "data-fill" (chart-key fill-colors))
                    (.attr "data-chartkey" (name chart-key))
                    (.attr "data-hasvalue" (chart-key data-set))
                    (.attr "data-selectedFill" (chart-key fill-selected-colors))
                    (.attr "id" (str "chart-bar-" chart-key "-" i))
                    (.attr "width" bar-width)
                    (.attr "height" cy)
                    (.attr "x" (+ cx (* bar-width j)))
                    (.attr "y" (get-y cy max-y))))))))

      ;; add the hovering rects
      (when (> (count chart-data) 1)
        (doseq [i (range (count chart-data))]
          (-> chart-node
            (.append "rect")
            (.attr "class" "hover-rect")
            (.attr "width" (+ (* bar-width (count chart-keys)) (/ (* bar-width (count chart-keys)) 2)))
            (.attr "height" chart-height)
            (.attr "x" (* i (+ (* bar-width (count chart-keys)) (/ (* bar-width (count chart-keys)) 2))))
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
                (.attr "x" (- (data-x-position chart-width i (count chart-keys)) 12))
                (.attr "y" (- chart-height 2))
                (.text label))))))))

;; ===== Graph Events =====

(defn- data-select 
  "
  Handle graph event that (may) select a new point on the graph.

  Redraw the SVG data point bars so selected and unselected bars 
  can be drawn differently, and update the `selected` component state.
  "
  [owner options idx]
  (.stopPropagation (.-event js/d3)) ; we got this!
  (let [svg-el (om/get-ref owner "d3-chart")
        d3-svg-el (.select js/d3 svg-el)
        chart-width (+ (om/get-props owner :chart-width) (if (:hide-nav options) 30 10))
        selected-bars (.selectAll d3-svg-el (str "rect.chart-bar-" idx))
        all-bars (.selectAll d3-svg-el "rect.chart-bar")
        fill-colors (:chart-colors options)
        fill-selected-colors (:chart-selected-colors options)]
    
    ;; draw all the bars normally
    (.each all-bars (fn [d i]
      (this-as bar-node
        (let [bar (.select js/d3 bar-node)
              color (.attr bar "data-fill")
              hasvalue (.attr bar "data-hasvalue")
              chart-key (keyword (.attr bar "data-chartkey"))]
          (-> bar
              (.attr "fill" (chart-key fill-colors)))))))
    
    ;; draw the selected bars specially
    (.each selected-bars (fn [d i]
      (this-as bar-node
        (let [bar (.select js/d3 bar-node)
              color (.attr bar "data-fill")
              hasvalue (.attr bar "data-hasvalue")
              chart-key (keyword (.attr bar "data-chartkey"))]
          (-> bar
              (.attr "fill" (chart-key fill-selected-colors)))))))

    ;; let the rest of the component know a (potentially) new data point is selected
    (om/set-state! owner :selected idx)))

(defn- prev-data
  "Handle click to select next data in the set. Update the start state."
  [owner e]
  (.stopPropagation e) ; we got this!
  (let [start (om/get-state owner :start)
        next-start (- start chart-step)
        fixed-next-start (max 0 next-start)]
    (om/set-state! owner :start fixed-next-start)))

(defn- next-data
  "Handle click to select prior data in the set. Update the start state."
  [owner e]
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
                        {:align-items :flex-start :justify-content :flex-start})}
      (for [label-key label-keys]
        (dom/div {:class "chart-labels"}
          (dom/div {:class "chart-value py1"
                    :style {:color (get-in labels [label-key :value-color])}}
            ((get-in labels [label-key :value-presenter]) label-key data))
          (dom/div {:class "chart-label"
                    :style {:color (get-in labels [label-key :label-color])
                            :text-align (if center? "center" "left")}}
            ((get-in labels [label-key :label-presenter]) label-key data)))))))

;; ===== D3 Chart Component =====

(defcomponent d3-column-chart [{:keys [chart-width chart-height chart-data selected-metric-cb] :as data} owner options]

  (init-state [_]
    (let [start (max 0 (- (count chart-data) show-data-points))
          current-data (vec (take-last show-data-points chart-data))]
      {:start start
       :selected (dec (count current-data))}))

  (did-mount [_]
    (when (and (not (utils/is-test-env?)) (:show-chart options))
      (d3-render-chart owner options)))

  (did-update [_ old-props old-state]
    (when (and (not (utils/is-test-env?)) (:show-chart options))
      (when (or (not= old-props data) (not= old-state (om/get-state owner)))
        (d3-render-chart owner options)))
    (when (and (fn? selected-metric-cb)
               (not= (om/get-state owner :selected) (:selected old-state)))
      (selected-metric-cb (om/get-state owner :selected))))

  (will-receive-props [_ next-props]
    (when (not= (:selected next-props) (om/get-state owner :selected))
      (let [next-state {:selected (or (min (:selected next-props) (dec show-data-points))
                                      (om/get-state owner :selected)
                                      (dec (min (count (:chart-data next-props)) show-data-points)))
                        :start (if (not= (count chart-data) (count (:chart-data next-props)))
                                 (max 0 (- (count (:chart-data next-props)) show-data-points))
                                 (om/get-state owner :start))}]
        (om/update-state! owner #(merge % next-state)))))

  (render-state [_ {:keys [start selected]}]
    (let [hide-chart-nav (:hide-nav options)
          selected-data-set (get (current-data owner) selected)
          labels (:labels options)
          top-label-keys (label-keys-for labels :top)
          bottom-label-keys (label-keys-for labels :bottom)
          chart-class (str (:chart-type options) " group" (when (:fake-chart options) " fake-chart"))
          chart-top-label-class (str "chart-top-label-container" (when (:sparklines-class options) (str " " (:sparklines-class options))))
          chart-bottom-label-class (str "chart-bottom-label-container" (when (:sparklines-class options) (str " " (:sparklines-class options))))]
      (dom/div {:class (str "bar-chart " chart-class)}

        ;; D3 Chart w/ optional nav. buttons
        (when (and (> (count chart-data) 1) (:show-chart options))
          (dom/div {:class (str "chart-container" (when (:sparklines-class options) (str " " (:sparklines-class options))))
                    :style {:width "100%"
                            :height (str chart-height "px")}}
            ;; Previous button
            (dom/div {:class (str "chart-prev" (when hide-chart-nav " hidden"))
                      :style {:paddingTop (str (- chart-height 17) "px")
                              :opacity (if (> start 0) 1 0)}
                      :on-click #(prev-data owner %)}
              (dom/i {:class "fa fa-caret-left"}))
            
            ;; Chart
            (dom/svg {:className "d3-chart"
                      :ref "d3-chart"
                      ;; either leave space for the nav (0 margin), or take up the space with 10 px of margin
                      :style {:margin-left (str (if hide-chart-nav 0 10) "px")
                              :margin-right (str (if hide-chart-nav 0 10) "px")}})
            
            ;; Next button
            (dom/div {:class (str "chart-next" (when hide-chart-nav " hidden"))
                      :style {:paddingTop (str (- chart-height 17) "px")
                              :opacity (if (< start (- (count chart-data) show-data-points)) 1 0)}
                      :on-click #(next-data owner %)}
              (dom/i {:class "fa fa-caret-right"}))))

        ;; Bottom row labels
        (when (not (nil? bottom-label-keys))
          (labels-for chart-bottom-label-class bottom-label-keys labels selected-data-set))))))