(ns open-company-web.components.finances.costs
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.charts :refer (column-chart)]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.lib.oc-colors :as occ]))

(defcomponent costs [data owner]
  
  (render [_]
    (let [finances-data (:data (:section-data data))
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-finances (sort #(sort-pred %1 %2) finances-data)
          value-set (first sorted-finances)
          currency (:currency data)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          costs-val (str cur-symbol (utils/format-value (:costs value-set)))]
      (dom/div {:class (utils/class-set {:section true
                                         :costs true
                                         :read-only (:read-only data)})
                :on-click (:start-editing-cb data)}
        (dom/div {:class "chart-header-container"}
          (dom/div {:class "target-actual-container"}
            (dom/div {:class "actual-container"}
              (dom/h3 {:class "actual gray"} costs-val)
              (dom/h3 {:class "actual-label gray"} (str "as of " (finances-utils/get-as-of-string (:period value-set)))))))
        (om/build column-chart (finances-utils/get-chart-data sorted-finances
                                                              cur-symbol
                                                              :costs
                                                              "Costs"
                                                              #js {"type" "string" "role" "style"}
                                                              (occ/fill-color :gray)))))))