(ns open-company-web.components.finances.cash
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.charts :refer (column-chart)]
            [open-company-web.components.ui.d3-column-chart :refer (d3-column-chart)]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.lib.oc-colors :as occ]))

(defn- get-d3-chart-data [sorted-data]
  {:chart-data (filter #(not (nil? (:cash %))) sorted-data)})

(defcomponent cash [data owner options]
  
  (render [_]
    (let [finances-data (:data (:section-data data))
          sort-pred (utils/sort-by-key-pred :period)
          sorted-finances (sort sort-pred finances-data)
          value-set (last sorted-finances)
          currency (:currency data)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          cash-val (str cur-symbol (utils/thousands-separator (:cash value-set)))
          fixed-sorted-finances (vec (map #(merge % {:label (str cur-symbol (.toLocaleString (js/parseFloat (str (:cash %)))))}) sorted-finances))
          chart-opts {:opts {:chart-height (:height (:chart-size options))
                             :chart-width (:width (:chart-size options))
                             :chart-keys [:cash]
                             :label-color (occ/get-color-by-kw :oc-green-regular)
                             :label-key :label
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
        (om/build d3-column-chart (get-d3-chart-data fixed-sorted-finances) chart-opts)))))