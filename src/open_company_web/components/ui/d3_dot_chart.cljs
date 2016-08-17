(ns open-company-web.components.ui.d3-dot-chart
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.finances.utils :as finances-utils]
            [cljsjs.d3]))

(def dot-radius 5)
(def dot-stroke 3)
(def dot-selected-stroke 6)

(def show-dots 4)

(defn max-y [data chart-keys]
  (let [filtered-data (map #(select-keys % chart-keys) data)]
    (apply max (vec (flatten (map vals filtered-data))))))

(defn scale [owner options]
  (let [all-data (om/get-props owner :chart-data)
        chart-keys (:chart-keys options)
        data-max (max-y all-data chart-keys)
        linear-fn (.. js/d3 -scale linear)
        domain-fn (.domain linear-fn #js [0 data-max])
        range-fn (.range linear-fn #js [0 (- (:chart-height options) 30)])]
    range-fn))

(defn dot-position [chart-width i]
  (let [dot-spacer (/ (- chart-width 20) (dec show-dots))]
    (+ (* i dot-spacer) 10)))

(defn current-data [owner]
  (let [start (om/get-state owner :start)
        all-data (om/get-props owner :chart-data)
        stop (min (count all-data) (+ start show-dots))]
    (subvec all-data start stop)))

(defn dot-select [owner options idx]
  (.stopPropagation (.-event js/d3))
  (let [svg-el (om/get-ref owner "d3-dots")
        d3-svg-el (.select js/d3 svg-el)
        chart-width (:chart-width options)
        data (current-data owner)
        next-set (get data idx)
        next-circle (.select d3-svg-el (str "circle#chart-dot-" idx))
        all-circles (.selectAll d3-svg-el "circle")]
    (.each all-circles (fn [d i]
                        (this-as circle-node
                          (let [circle (.select js/d3 circle-node)
                                color (.attr circle "data-fill")
                                hasvalue (.attr circle "data-hasvalue")]
                            (-> circle
                                (.attr "stroke" color)
                                (.attr "stroke-width" dot-stroke)
                                (.attr "fill" "white")
                                (.attr "r" (if hasvalue dot-radius 0)))))))
    (let [color (.attr next-circle "data-fill")
          hasvalue (.attr next-circle "data-hasvalue")]
      (-> next-circle
          (.attr "stroke" color)
          (.attr "stroke-width" dot-selected-stroke)
          (.attr "fill" "white")
          (.attr "r" (if hasvalue dot-radius 0))))
    (om/set-state! owner :selected idx)))

(defn get-y [y max-y]
  (+ 23 (- max-y y)))

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
                         (.attr "width" chart-width)
                         (.attr "height" chart-height)
                         (.on "click" (fn []
                                        (when (:svg-click options)
                                          ((:svg-click options) nil))
                                        (.stopPropagation (.-event js/d3)))))
          scale-fn (scale owner options)
          data-max (max-y (om/get-props owner :chart-data) chart-keys)
          max-y (scale-fn data-max)]

      ; for each set of data
      (when (> (count chart-data) 1)
        (doseq [i (range (count chart-data))]
          (let [data-set (get chart-data i)]
            ; for each key in the set
            (doseq [j (range (count chart-keys))]
              (let [chart-key (get chart-keys j)
                    cx (dot-position chart-width i)
                    cy (scale-fn (chart-key data-set))]
                
                ; add the line to connect this to the next dot and a polygon below the lines
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
                          (.style "stroke-width" dot-stroke)
                          (.attr "x1" (+ cx 2))
                          (.attr "y1" (get-y cy max-y))
                          (.attr "x2" (- next-cx 2))
                          (.attr "y2" (get-y next-cy max-y))))))
                ; add a circle to represent the data
                (-> chart-node
                    (.append "circle")
                    (.attr "class" "chart-dot")
                    (.attr "r" (if (not (chart-key data-set))
                                0
                                dot-radius))
                    (.attr "stroke" (chart-key fill-colors))
                    (.attr "stroke-width" (if (= i selected) dot-selected-stroke dot-stroke))
                    (.attr "fill" "white")
                    (.attr "data-fill" (chart-key fill-colors))
                    (.attr "data-hasvalue" (chart-key data-set))
                    (.attr "data-selectedFill" (chart-key fill-selected-colors))
                    (.attr "id" (str "chart-dot-" i))
                    (.attr "cx" cx)
                    (.attr "cy" (get-y cy max-y))))))))
      ; add the hovering rects
      (when (> (count chart-data) 1)
        (doseq [i (range (count chart-data))]
          (-> chart-node
              (.append "rect")
              (.attr "class" "hover-rect")
              (.attr "width" (/ chart-width show-dots))
              (.attr "height" chart-height)
              (.attr "x" (* i (/ chart-width show-dots)))
              (.attr "y" 0)
              (.on "mouseover" #(dot-select owner options i))
              (.on "mouseout" #(dot-select owner options (om/get-state owner :selected)))
              (.attr "fill" "transparent")))))))

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

  (render-state [_ {:keys [start selected]}]

    (let [fixed-chart-height (if (> (count chart-data) 1)
                              chart-height
                              90)
          hide-chart-nav (:hide-nav options)
          selected-data-set (get chart-data selected)
          selected-label (get selected-data-set (:label-key options))
          selected-sub-label (get selected-data-set (:sub-label-key options))]
      
      (dom/div
        ;; Top label / sub-label

        (dom/div {:class "chart-label-container"}
          (dom/div {:class "dot-chart-label"
                    :style {:color (:label-color options)}}
            selected-label)
          (dom/div {:class "dot-chart-label-sub"
                    :style {:color (:sub-label-color options)}}
            selected-sub-label))

        ;; D3 Chart w/ nav. buttons
        (dom/div {:class "d3-dot-container"
                  :style #js {:width (str (+ chart-width 20) "px")
                              :height (str fixed-chart-height "px")}}
          ;; Previous button
          (dom/div {:class (str "chart-prev" (when hide-chart-nav " hidden"))
                    :style #js {:paddingTop (str (- fixed-chart-height 17) "px")
                                :opacity (if (> start 0) 1 0)}
                    :on-click #(prev-data owner %)}
            (dom/i {:class "fa fa-caret-left"}))
          ;; Chart
          (dom/svg #js {:className "d3-dot-chart"
                        :ref "d3-dots"
                        :style #js {:marginLeft (str (if hide-chart-nav 10 0) "px")}})
          ;; Next button
          (dom/div {:class (str "chart-next" (when hide-chart-nav " hidden"))
                    :style #js {:paddingTop (str (- fixed-chart-height 17) "px")
                                :opacity (if (< start (- (count chart-data) show-dots)) 1 0)}
                    :on-click #(next-data owner %)}
            (dom/i {:class "fa fa-caret-right"})))

      ;; Bottom extra-info
      (when (not (empty? (:extra-info-keys options)))

        (dom/div {:class "extra-info-container"}
          (dom/div {:class "extra-info"}
            (dom/div {:class "extra-info-value"
                      :style {:color (:cash (:extra-info-colors options))}}
              ((:cash (:extra-info-presenters options)) (get selected-data-set :cash)))
            (dom/div {:class "extra-info-label"
                      :style {:color (:cash (:extra-info-colors options))}}
              "CASH"))
          (dom/div {:class "extra-info"}
            (dom/div {:class "extra-info-value"
                      :style {:color (:runway (:extra-info-colors options))}}
              ((:runway (:extra-info-presenters options)) (get selected-data-set :runway)))
            (dom/div {:class "extra-info-label"
                      :style {:color (:cash (:extra-info-colors options))}}
              "RUNWAY"))))

      )

      )))