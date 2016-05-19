(ns open-company-web.components.growth.growth-metric
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.d3-dot-chart :refer (d3-dot-chart)]
            [open-company-web.components.ui.utility-components :refer (editable-pen)]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.router :as router]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(defn label-from-set [data-set interval metric-unit currency-symbol]
  (let [actual-val (:value data-set)
        actual (when actual-val (utils/thousands-separator actual-val))
        period (utils/get-period-string (:period data-set) interval)
        fixed-cur-unit (when (= metric-unit "currency") currency-symbol)
        unit (when (= metric-unit "%") "%")]
    (when actual-val
      (str fixed-cur-unit actual unit))))

(defn sub-label [period metric-info]
  (let [mname (:name metric-info)
        interval (:interval metric-info)
        label (str mname " - ")]
    (cond
      (= interval "weekly")
      (str label (utils/get-weekly-period-day period) " " (utils/get-month period "weekly") " " (utils/get-year period "weekly"))
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
          chart-opts {:opts {:chart-height (:height (:chart-size options))
                             :chart-width (:width (:chart-size options))
                             :chart-keys [:value]
                             :interval interval
                             :label-color (occ/get-color-by-kw :oc-gray-5)
                             :label-key :label
                             :sub-label-key :sub-label
                             :svg-click #(when (:topic-click options) ((:topic-click options) nil))
                             :chart-colors {:value (occ/get-color-by-kw :oc-new-chart-blue)}
                             :chart-selected-colors {:value (occ/get-color-by-kw :oc-new-chart-blue)}
                             :hide-nav (:hide-nav options)}}]
      (dom/div {:class (utils/class-set {:section true
                                         slug true
                                         :read-only (:read-only data)})
                :key slug
                :on-click (:start-editing-cb data)}
        (when (pos? (count metric-data))
          (dom/div {}
            (when (:show-label options)
              (dom/div {:class "chart-header-container"}
                (dom/div {:class "target-actual-container"}
                  (dom/div {:class "actual-container"}
                    (dom/h3 {:class "actual blue"} actual-with-label
                      (om/build editable-pen {:click-callback (:start-data-editing-cb data)}))
                    (dom/h3 {:class "actual-label gray"} (str "as of " period))))))
            (om/build d3-dot-chart {:chart-data fixed-sorted-metric} chart-opts)))))))