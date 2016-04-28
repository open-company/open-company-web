(ns open-company-web.components.finances.cash
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.charts :refer (column-chart)]
            [open-company-web.components.ui.d3-column-chart :refer (d3-column-chart)]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.lib.oc-colors :as occ]))

(defcomponent cash [data owner options]

  (render [_]
    (let [finances-data (:data (:section-data data))
          filled-finances-data (finances-utils/fill-gap-months finances-data)
          sort-pred (utils/sort-by-key-pred :period)
          sorted-finances (sort sort-pred (vals filled-finances-data))
          value-set (last sorted-finances)
          currency (:currency data)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          cash-val (str cur-symbol (utils/thousands-separator (:cash value-set)))
          fixed-sorted-finances (mapv #(merge % {:label (when-let [c (:cash %)]
                                                          (utils/thousands-separator c))
                                                 :sub-label (str "CASH - " (utils/get-month (:period %)) " " (utils/get-year (:period %)))}) sorted-finances)
          chart-opts {:opts {:chart-width (:width (:chart-size options))
                             :chart-height (:height (:chart-size options))
                             :chart-keys [:cash]
                             :label-color (occ/get-color-by-kw :oc-gray-5)
                             :label-key :label
                             :sub-label-key :sub-label
                             :interval "monthly"
                             :h-axis-color (occ/get-color-by-kw :oc-green-light)
                             :h-axis-selected-color (occ/get-color-by-kw :oc-green-regular)
                             :chart-colors {:cash (occ/get-color-by-kw :oc-green-light)}
                             :chart-selected-colors {:cash (occ/get-color-by-kw :oc-green-regular)}}}]
      (dom/div {:class (utils/class-set {:section true
                                         :cash true
                                         :read-only (:read-only data)})
                :on-click (:start-editing-cb data)}
        (when (:show-label options)
          (dom/div {:class "chart-header-container"}
            (dom/div {:class "target-actual-container"}
              (dom/div {:class "actual-container"}
                (dom/h3 {:class "actual green"} cash-val)
                (dom/h3 {:class "actual-label gray"} (str "as of " (finances-utils/get-as-of-string (:period value-set))))))))
        (om/build d3-column-chart {:chart-data fixed-sorted-finances} chart-opts)))))