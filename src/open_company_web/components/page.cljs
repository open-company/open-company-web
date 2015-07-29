(ns ^:figwheel-always open-company-web.components.page
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [handle-change]]
              [open-company-web.components.headcount :refer [headcount]]
              [open-company-web.components.finances :refer [finances]]
              [open-company-web.components.compensation :refer [compensation]]
              [open-company-web.components.currency-picker :refer [currency-picker]]
              [open-company-web.components.navbar :refer [navbar]]
              [open-company-web.components.link :refer [link]]
              [open-company-web.components.sidebar :refer [sidebar]]
              [open-company-web.components.profile :refer [profile]]
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
          reports (filterv #(= (:rel %) "report") (:links company-data))]
      (dom/div {:class "row"}
        (om/build navbar company-data)
        (dom/div {:class "container-fluid"}
          (om/build sidebar {})
          (dom/div {:class "col-md-11 col-md-offset-1 main"}
            (cond
              (:loading data)
              (dom/div
                (dom/h4 "Loading data..."))

              (contains? company-data :symbol)
              (om/build profile data)
              ; (dom/div
              ;   (for [report reports]
              ;     (om/build report-link {
              ;       :report (:href report)
              ;       :symbol symbol})))

              :else
              (dom/div
                (dom/h2 (str (:ticker data) " not found"))
                (om/build link {:href "/" :name "Back home"})))))))))
