(ns open-company-web.components.finances.runway
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.iso4217 :refer [iso4217]]
            [open-company-web.components.charts :refer [column-chart]]
            [open-company-web.components.finances.utils :refer [get-chart-data]]
            [open-company-web.components.utility-components :refer [editable-pen]]))

(defcomponent runway [data owner]
  (render [_]
    (let [finances-data (:data (:finances (:company-data data)))
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-finances (sort #(sort-pred %1 %2) finances-data)
          value-set (first sorted-finances)
          runway-value (:runway value-set)
          runway-val (if (pos? runway-value) (str (utils/format-value runway-value) " days") "Profitable")
          period (utils/period-string (:period value-set))
          cur-symbol (utils/get-symbol-for-currency-code (:currency data))]
      (dom/div {:class (str "section runway" (when (:read-only data) " read-only"))}
        (dom/h3 {}
                runway-val
                (om/build editable-pen {:click-callback (:editable-click-callback data)}))
        (dom/p {} period)
        (om/build column-chart (get-chart-data sorted-finances
                                               ""
                                               :runway
                                               "runway"
                                               #js {"type" "string" "role" "style"}
                                               "fill-color: #0266C8"
                                               "###,### days"))))))