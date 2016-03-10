(ns open-company-web.components.finances.cash
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.charts :refer (column-chart)]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.lib.oc-colors :as occ]))

(defcomponent cash [data owner]
  
  (render [_]
    (let [finances-data (:data (:section-data data))
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-finances (sort sort-pred finances-data)
          value-set (first sorted-finances)
          currency (:currency data)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          cash-val (str cur-symbol (utils/thousands-separator (:cash value-set)))]
      (dom/div {:class (utils/class-set {:section true
                                         :cash true
                                         :read-only (:read-only data)})
                :on-click (:start-editing-cb data)}
        (dom/div {:class "chart-header-container"}
          (dom/div {:class "target-actual-container"}
            (dom/div {:class "actual-container"}
              (dom/h3 {:class "actual green"} cash-val)
              (dom/h3 {:class "actual-label gray"} (str "as of " (finances-utils/get-as-of-string (:period value-set)))))))
        (om/build column-chart (finances-utils/get-chart-data sorted-finances
                                                              cur-symbol
                                                              :cash
                                                              "Cash"
                                                              #js {"type" "string" "role" "style"}
                                                              (occ/fill-color :oc-green-light)))))))