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
    (println "render: " data)
    (let [symbol (:ticker data)
          company-data ((keyword symbol) data)]
      (println "company render " company-data)
      (dom/div
        (dom/h2 (str (:name data) " Dashboard"))
        (cond
          (:loading company-data)
            (dom/div
              (dom/h4 "Loading data..."))
          (contains? "symbol" company-data)
            (dom/div
              (om/build currency-picker company-data)
              (om/build headcount (:headcount company-data))
              (om/build finances {:finances (:finances company-data) :currency (:currency company-data)})
              (om/build compensation {:compensation (:compensation company-data) :headcount (:headcount company-data) :currency (:currency company-data)}))
          :else
            (dom/div
              (dom/h2 (str (:symbol data) " not found"))
              (om/build link {:href "/" :name "Back home"})))))))

; (defcomponent company [data owner]
;   (render [_]
;     (dom/div
;       (dom/h2 (str (:name data) " Dashboard"))
;       (om/build currency-picker data)
;       (om/build headcount (:headcount data))
;       (om/build finances {:finances (:finances data) :currency (:currency data)})
;       (om/build compensation {:compensation (:compensation data) :headcount (:headcount data) :currency (:currency data)}))))
;
; (defcomponent company-not-found [data owner]
;   (render [_]
;     (dom/div
;       (dom/h2 (str (:symbol data) " not found"))
;       (om/build link {:href "/" :name "Back home"}))))
