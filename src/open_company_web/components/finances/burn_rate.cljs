(ns open-company-web.components.finances.burn-rate
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.charts :refer (column-chart)]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.lib.oc-colors :as occ]))

(defcomponent burn-rate [data owner]
  
  (render [_]
    (let [finances-data (:data (:section-data data))
          value-set (first finances-data)
          currency (:currency data)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          burn-rate-val (str cur-symbol (utils/thousands-separator (:burn-rate value-set)))]
      (dom/div {:class (utils/class-set {:section true
                                         :burn-rate true
                                         :read-only (:read-only data)})
                :on-click (:start-editing-cb data)}
        (dom/div {:class "chart-header-container"}
          (dom/div {:class "target-actual-container"}
            (dom/div {:class "actual-container"}
              (dom/h3 {:class "actual green"} burn-rate-val)
              (dom/h3 {:class "actual-label gray"} (str "as of " (finances-utils/get-as-of-string (:period value-set)))))))
        (om/build column-chart (finances-utils/get-chart-data finances-data
                                                              cur-symbol
                                                              :burn-rate
                                                              "Burn Rate"
                                                              #js {"type" "string" "role" "style"}
                                                              (occ/fill-color :blue)))))))