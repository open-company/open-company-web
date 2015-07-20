(ns ^:figwheel-always open-company-web.components.page
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [handle-change]]
              [open-company-web.components.headcount :refer [headcount]]
              [open-company-web.components.finances :refer [finances]]
              [open-company-web.components.compensation :refer [compensation]]
              [open-company-web.components.currency-picker :refer [currency-picker]]
              [open-company-web.components.link :refer [link]]))

(enable-console-print!)

(defcomponent company [data owner]
  (render [_]
    (let [symbol (:ticker data)
          company-data ((keyword symbol) data)]
      (dom/div
        (dom/h2 (str (get company-data "name") " - Dashboard"))
        (cond
          (:loading data)
            (dom/div
              (dom/h4 "Loading data..."))
          (contains? company-data "symbol")
            (dom/div
              (om/build currency-picker company-data)
              (om/build headcount (company-data "headcount"))
              (om/build finances {
                "finances" (company-data "finances")
                "currency" (company-data "currency")})
              (om/build compensation {
                "compensation" (company-data "compensation")
                "headcount" (company-data "headcount")
                "currency" (company-data "currency")}))
          :else
            (dom/div
              (dom/h2 (str (:ticker data) " not found"))
              (om/build link {:href "/" :name "Back home"})))))))
