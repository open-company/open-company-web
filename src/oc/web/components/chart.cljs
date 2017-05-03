(ns oc.web.components.chart
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [cljs-hash.goog :as gh]))

(defn chart-uid [topic-data]
  (when (:chart-url topic-data)
    (str (:topic topic-data) "-" (gh/hash :md5 (:chart-url topic-data)))))

(rum/defcs chart
  [s topic-data card-width]
  (when (not (empty? (:chart-url topic-data)))
    (let [chart-id (chart-uid topic-data)
          url-fragment (last (clojure.string/split (:chart-url topic-data) #"/spreadsheets/d/"))
          chart-proxy-url (str "/_/sheets-proxy/spreadsheets/d/" url-fragment)]
      [:div.chart
        {:key chart-id
         :data-chart-src (:chart-url topic-data)}
        [:iframe
          {:style {:border "none" :margin "0" :padding "0" :width (str card-width "px") :height (str card-width "px")}
           :src chart-proxy-url}]])))