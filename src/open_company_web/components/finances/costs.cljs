(ns open-company-web.components.finances.costs
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.iso4217 :refer [iso4217]]
            [open-company-web.components.charts :refer [column-chart]]
            [open-company-web.components.finances.utils :refer [get-chart-data]]
            [open-company-web.components.utility-components :refer [editable-pen]]))

(defcomponent costs [data owner]
  (render [_]
    (let [finances-data (:data (:finances (:company-data data)))
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-finances (sort #(sort-pred %1 %2) finances-data)
          value-set (first sorted-finances)
          period (utils/period-string (:period value-set))
          cur-symbol (utils/get-symbol-for-currency-code (:currency (:company-data data)))
          costs-val (str cur-symbol (utils/format-value (:costs value-set)))]
      (dom/div {:class (utils/class-set {:section true
                                         :costs true
                                         :read-only (:read-only data)})}
        (dom/h3 {}
                costs-val
                (om/build editable-pen {:click-callback (:editable-click-callback data)}))
        (dom/p {} period)
        (om/build column-chart (get-chart-data sorted-finances
                                               cur-symbol
                                               :costs
                                               "Costs"
                                               #js {"type" "string" "role" "style"}
                                               "fill-color: #ADADAD"))))))