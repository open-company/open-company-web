(ns open-company-web.components.finances-component.runaway
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.iso4217 :refer [iso4217]]
            [open-company-web.components.charts :refer [column-chart]]
            [open-company-web.components.finances-component.utils :refer [get-chart-data]]))

(defcomponent runaway [data owner]
  (render [_]
    (let [finances-data (:data (:oc:finances data))
          value-set (first finances-data)
          runaway-val (utils/format-value (:runaway value-set))
          period (utils/period-string (:period value-set))
          cur-symbol (utils/get-symbol-for-currency-code (:currency data))]
      (dom/div {:class "section runaway"}
        (dom/h3 {} runaway-val)
        (dom/p {} period)
        (om/build column-chart (get-chart-data finances-data "" :runaway "Runaway"))))))