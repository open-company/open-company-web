(ns ^:figwheel-always open-company-web.components.page
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [handle-change]]
              [open-company-web.components.headcount :refer [headcount]]
              [open-company-web.components.finances :refer [finances]]
              [open-company-web.components.compensation :refer [compensation]]
              [open-company-web.components.currency-picker :refer [currency-picker]]
              [open-company-web.components.link :refer [link]]
              [clojure.string :as str]))

(enable-console-print!)

(defcomponent report-link [data owner]
  (render [_]
    (let [symbol (:symbol data)
          link-parts (str/split (:report data) "/")
          year (nth link-parts 4)
          period (nth link-parts 5)]
      (dom/div
        (om/build link {
          :href (str "/companies/" symbol "/" year "/" period)
          :name (str year " - " period)})))))

(defcomponent company [data owner]
  (render [_]
    (let [symbol (:ticker data)
          company-data ((keyword symbol) data)
          reports (filterv #(= (% "rel") "report") (get company-data "links"))]
      (dom/div
        (dom/h2 (str (get company-data "name") " - Dashboard"))
        (cond
          (:loading data)
          (dom/div
            (dom/h4 "Loading data..."))

          (contains? company-data "symbol")
          (dom/div
            (for [report reports]
              (om/build report-link {
                :report (report "href")
                :symbol symbol})))

          :else
          (dom/div
            (dom/h2 (str (:ticker data) " not found"))
            (om/build link {:href "/" :name "Back home"})))))))
