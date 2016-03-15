(ns open-company-web.components.growth.growth-metric
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.charts :refer (column-chart)]
            [open-company-web.components.ui.utility-components :refer (editable-pen)]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.router :as router]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(defcomponent growth-metric [data owner options]

  (render [_]
    (let [slug (keyword (:slug @router/path))
          company-data (slug @dispatcher/app-state)
          metric-info (:metric-info data)
          metric-data (:metric-data data)
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-metric (vec (sort sort-pred metric-data))
          actual-idx (growth-utils/get-actual sorted-metric)
          actual-set (sorted-metric actual-idx)
          actual (utils/thousands-separator (or (:value actual-set) 0))
          interval (:interval metric-info)
          period (utils/get-period-string (:period actual-set) interval)
          metric-unit (:unit metric-info)
          fixed-cur-unit (when (= metric-unit "currency")
                           (utils/get-symbol-for-currency-code (:currency company-data)))
          unit (when (= metric-unit "%") "%")
          actual-with-label (str fixed-cur-unit actual unit)
          chart-opts (when (contains? options :chart-size) {:opts {:chart-height (:height (:chart-size options))
                                                                   :chart-width (:width (:chart-size options))
                                                                   :chart-color (occ/get-color-by-kw :oc-blue-regular)}})]
      (dom/div {:class (utils/class-set {:section true
                                         (:slug metric-info) true
                                         :read-only (:read-only data)})
                :key (:slug metric-info)
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
            (om/build column-chart (growth-utils/get-chart-data sorted-metric
                                                                fixed-cur-unit
                                                                (:slug metric-info)
                                                                (:name metric-info)
                                                                unit
                                                                interval) chart-opts)))))))