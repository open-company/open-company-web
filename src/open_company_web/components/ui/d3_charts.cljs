(ns open-company-web.components.ui.d3-charts
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]))

(def bar-width 15)

(def show-column 6)

(defn get-formatted-data [value prefix]
  (str prefix (.toLocaleString (js/parseFloat (str value)))))

(defn scale [data options]
  (let [chart-key (first (:chart-keys options))
        values (map chart-key (:chart-data data))
        data-max (.max (.-d3 js/window) (clj->js values))
        linear-fn (.. (.-d3 js/window) -scale linear)
        domain-fn (.domain linear-fn #js [0 data-max])
        range-fn (.range linear-fn #js [0 (- (:chart-height options) 100)])]
    range-fn))

(defn bar-position [chart-width i data-count]
  (let [bar-spacer (/ chart-width (inc (min show-column data-count)))]
    (- (* (inc i) bar-spacer) (/ bar-width 2))))

(defn bar-click [owner options scale-fn value idx]
  (.stopPropagation (.-event (.-d3 js/window)))
  (let [fill-color (:chart-color options)
        fill-selected-color (:chart-selected-color options)
        d3 (.-d3 js/window)
        selected (om/get-state owner :selected)
        cur-g (.select d3 (str "#chart-g-" selected))
        next-g (.select d3 (str "#chart-g-" idx))
        cur-x-label (.select d3 (str "#chart-x-label-" selected))
        next-x-label (.select d3 (str "#chart-x-label-" idx))
        chart-label (.select d3 (str "#chart-label"))]
    (.attr cur-g "fill" fill-color)
    (.attr next-g "fill" fill-selected-color)
    (.attr cur-x-label "fill" fill-color)
    (.attr next-x-label "fill" fill-selected-color)
    (-> chart-label
      (.text (get-formatted-data value (:prefix options)))
      (.attr "x" (bar-position (:chart-width options) idx (count (om/get-props owner :chart-data)))))

    (om/set-state! owner :selected idx)))

(defn d3-calc [owner data options]
  ; render the chart
  (let [selected (om/get-state owner :selected)
        chart-data (:chart-data data)
        fill-color (:chart-color options)
        fill-selected-color (:chart-selected-color options)
        chart-width (:chart-width options)
        scale-fn (scale data options)
        chart-key (first (:chart-keys options))
        data-vec (clj->js (map chart-key (:chart-data data)))
        chart-node (-> (.-d3 js/window)
                       (.select (om/get-ref owner "d3-column"))
                       (.attr "width" (:chart-width options))
                       (.attr "height" (:chart-height options))
                       (.on "click" #(.stopPropagation (.-event (.-d3 js/window)))))
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
          (.attr "id" (str "chart-x-label-" idx))
          (.attr "x" (- (bar-position chart-width idx (count chart-data)) 10))
          (.attr "y" (- (:chart-height options) 8))
          (.attr "fill" (if (= idx selected)
                          fill-selected-color
                          fill-color))
          (.text (utils/get-period-string (:period (get chart-data idx)) nil [:short])))))

    (-> chart-node
        (.append "text")
        (.attr "class" "chart-label")
        (.attr "id" "chart-label")
        (.attr "x" (bar-position chart-width selected (count chart-data)))
        (.attr "y" 40)
        (.attr "dx" "-24px")
        (.attr "fill" fill-selected-color)
        (.text (get-formatted-data (get data-vec selected) (:prefix options))))))

(defcomponent d3-column-chart [data owner options]

  (init-state [_]
    {:selected (dec (count (:chart-data data)))})

  (did-mount [_]
    (d3-calc owner data options))

  (did-update [_ old-props _]
    (when-not (= old-props data)
      (d3-calc owner data options)))

  (render [_]
    (dom/div {:class "d3-column-container"}
      (dom/svg #js {:className "d3-column-chart" :ref "d3-column"}))))