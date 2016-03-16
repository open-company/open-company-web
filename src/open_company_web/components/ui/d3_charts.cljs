(ns open-company-web.components.ui.d3-charts
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.finances.utils :as finances-utils]
            [cljsjs.d3]))

(def bar-width 15)

(def show-column 6)

(defn fix-chart-label-position []
  (let [chart-label (.select js/d3 "#chart-label")
        label-width (.-width (.getBBox (get (get chart-label 0) 0)))]
    (println "dx" (/ label-width 2))
    (.attr chart-label "dx" (str "-" (- (/ label-width 2) 10) "px"))))

(defn get-formatted-data [chart-key value prefix]
  (if (= chart-key :runway)
    (finances-utils/get-rounded-runway value [:round])
    (str prefix (.toLocaleString (js/parseFloat (str value))))))

(defn scale [owner options]
  (let [chart-key (first (:chart-keys options))
        values (map chart-key (om/get-state owner :current-data))
        data-max (.max js/d3 (clj->js values))
        linear-fn (.. js/d3 -scale linear)
        domain-fn (.domain linear-fn #js [0 data-max])
        range-fn (.range linear-fn #js [0 (- (:chart-height options) 100)])]
    range-fn))

(defn bar-position [chart-width i data-count]
  (let [bar-spacer (/ chart-width (inc (min show-column data-count)))]
    (- (* (inc i) bar-spacer) (/ bar-width 2))))

(defn bar-click [owner options scale-fn value idx]
  (.stopPropagation (.-event js/d3))
  (let [fill-color (:chart-color options)
        fill-selected-color (:chart-selected-color options)
        selected (om/get-state owner :selected)
        cur-g (.select js/d3 (str "#chart-g-" selected))
        next-g (.select js/d3 (str "#chart-g-" idx))
        chart-label (.select js/d3 (str "#chart-label"))
        chart-width (:chart-width options)
        data-count (count (om/get-state owner :current-data))
        label-x-position (bar-position chart-width idx data-count)]
    (.attr cur-g "fill" fill-color)
    (.attr next-g "fill" fill-selected-color)
    (-> chart-label
      (.text (get-formatted-data (first (:chart-keys options)) value (:prefix options)))
      (.attr "x" label-x-position))
    (fix-chart-label-position)
    (om/set-state! owner :selected idx)))

(defn d3-calc [owner options]
  ; render the chart
  (let [selected (om/get-state owner :selected)
        chart-data (om/get-state owner :current-data)
        fill-color (:chart-color options)
        fill-selected-color (:chart-selected-color options)
        chart-width (:chart-width options)
        scale-fn (scale owner options)
        chart-key (first (:chart-keys options))
        data-vec (clj->js (map chart-key chart-data))
        chart-node (-> js/d3
                       (.select (om/get-ref owner "d3-column"))
                       (.attr "width" (:chart-width options))
                       (.attr "height" (:chart-height options))
                       (.on "click" #(.stopPropagation (.-event js/d3))))
        bar (.selectAll chart-node "g")
        bar-enter (-> (.data bar data-vec)
                      (.enter)
                      (.append "g")
                      (.attr "class" "chart-g")
                      (.attr "id" (fn [d i](str "chart-g-" i)))
                      (.on "click" (partial bar-click owner options scale-fn))
                      (.attr "fill" (fn [d idx] (if (= idx selected)
                                                  fill-selected-color
                                                  fill-color)))
                      (.attr "transform"
                             (fn [d i]
                                (str "translate("
                                        (bar-position chart-width i (count chart-data))
                                        ","
                                        (- (:chart-height options) (scale-fn d) 20) ")"))))]
    (-> bar-enter
        (.append "rect")
        (.attr "class" "chart-bar")
        (.attr "id" (fn [d idx](str "chart-bar-" idx)))
        (.attr "width" (dec bar-width))
        (.attr "height" scale-fn))

    (doseq [v chart-data]
      (let [idx (.indexOf (to-array chart-data) v)]
        (-> chart-node
          (.append "text")
          (.attr "class" "chart-x-label")
          (.attr "x" (- (bar-position chart-width idx (count chart-data)) 8))
          (.attr "y" (:chart-height options))
          (.attr "fill" fill-selected-color)
          (.text (utils/get-period-string (:period (get chart-data idx)) nil [:short])))))

    (-> chart-node
        (.append "text")
        (.attr "class" "chart-label")
        (.attr "id" "chart-label")
        (.attr "x" (bar-position chart-width selected (count chart-data)))
        (.attr "y" 50)
        (.attr "fill" fill-selected-color)
        (.text (get-formatted-data (first (:chart-keys options)) (get data-vec selected) (:prefix options))))
    (fix-chart-label-position)))

(defcomponent d3-column-chart [data owner options]

  (init-state [_]
    (let [current-data (vec (take-last show-column (:chart-data data)))]
      {:selected (dec (count current-data))
       :current-data current-data}))

  (did-mount [_]
    (d3-calc owner options))

  (did-update [_ old-props _]
    (when-not (= old-props data)
      (om/set-state! owner (take-last show-column (:chart-data data)))
      (d3-calc owner options)))

  (render [_]
    (dom/div {:class "d3-column-container"}
      (dom/svg #js {:className "d3-column-chart" :ref "d3-column"}))))