(ns open-company-web.components.growth.growth-metric
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.d3-chart :refer (d3-chart)]
            [open-company-web.components.ui.utility-components :refer (editable-pen)]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.router :as router]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(defn- label-from-set [data-set interval metric-unit currency-symbol]
  (let [actual-val (:value data-set)
        actual (when actual-val (utils/thousands-separator actual-val))
        period (utils/get-period-string (:period data-set) interval)
        fixed-cur-unit (when (= metric-unit "currency") currency-symbol)
        unit (when (= metric-unit "%") "%")]
    (when actual-val
      (str fixed-cur-unit actual unit))))

(defn- sub-label [period metric-info]
  (let [mname (:name metric-info)
        interval (:interval metric-info)
        label (str mname " - ")]
    (cond
      (= interval "weekly")
      (str label (utils/get-weekly-period-day period) " " (utils/get-month period "weekly") " " (utils/get-year period "weekly"))
      (= interval "quarterly")
      (str label (utils/get-quarter-from-period period [:short]) " " (utils/get-year period))
      :else
      (str label (utils/get-month period) " " (utils/get-year period)))))

(defcomponent growth-metric [data owner options]

  (render [_]
    (let [{:keys [slug interval] :as metric-info} (:metric-info data)
          metric-data (:metric-data data)
          filled-metric-data (growth-utils/fill-gap-months metric-data slug interval)
          sort-pred (utils/sort-by-key-pred :period)
          sorted-metric (vec (sort sort-pred (vals filled-metric-data)))
          actual-idx (growth-utils/get-actual sorted-metric)
          actual-set (sorted-metric actual-idx)
          metric-unit (:unit metric-info)
          period (utils/get-period-string (:period actual-set) interval)
          currency-symbol (utils/get-symbol-for-currency-code (:currency data))
          actual-with-label (label-from-set actual-set interval metric-unit currency-symbol)
          fixed-sorted-metric (vec (map #(merge % {:label (label-from-set % interval metric-unit currency-symbol)
                                                   :sub-label (sub-label (:period %) metric-info)}) sorted-metric))
          chart-opts {:opts {:chart-type "unbordered-chart"
                             :chart-height 100
                             :chart-width (:width (:chart-size options))
                             :chart-keys [:value]
                             :interval interval
                             :x-axis-labels false
                             :chart-colors {:value (occ/get-color-by-kw :oc-chart-blue)}
                             :chart-selected-colors {:value (occ/get-color-by-kw :oc-chart-blue)}
                             :chart-fill-polygons true
                             :label-color (occ/get-color-by-kw :oc-gray-5)
                             :sub-label-color (occ/get-color-by-kw :oc-gray-5)
                             :labels {:value {:position :bottom
                                              :order 1
                                              :value-presenter #(:label %2)
                                              :value (occ/get-color-by-kw :oc-gray-5) 
                                              :label-presenter #(:sub-label %2)
                                              :label-color (occ/get-color-by-kw :oc-gray-5)}}
                             :hide-nav (:hide-nav options)}}]

      (dom/div {:class (utils/class-set {:section true
                                         slug true
                                         :read-only (:read-only data)})
                :key slug
                :on-click (:start-editing-cb data)}
        (when (pos? (count metric-data))
          (dom/div {}
            (om/build d3-chart {:chart-data fixed-sorted-metric} chart-opts)))))))