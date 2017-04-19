(ns oc.web.components.chart
  (:require [rum.core :as rum]
            [oc.web.lib.charts :as c]
            [oc.web.lib.utils :as utils]
            [cljs-hash.goog :as gh]))

(defn chart-uid [topic-data]
  (when (:chart-url topic-data)
    (str (:topic topic-data) "-" (:created-at topic-data) "-" (gh/hash :md5 (:chart-url topic-data)))))

(declare render-chart)

(defn check-chart
  "After the render-chart function check if the chart node is empty, if so that means something went wrong
   and we need to retry the mount. Increase the retry variable, retry at most 5 times with a growing delay."
  [s]
  (let [retry @(::retry s)
        topic-data (first (:rum/args s))
        chart-id (chart-uid topic-data)]
    (when-let [chart-node (.getElementById js/document chart-id)]
      (when (and (empty? (.trim (.-innerHTML chart-node)))
                 (< retry 5))
        (reset! (::retry s) (* retry 2))
        (render-chart s)))))

(defn render-chart [s]
  (let [topic-data (first (:rum/args s))
        chart-url (:chart-url topic-data)
        chart-id (chart-uid topic-data)]
    (when (and (not (empty? topic-data))
               (not (empty? chart-url))
               (not= @(::last-url s) chart-url))
      (c/load-chart-url chart-url chart-id (second (:rum/args s)))
      (utils/after (* @(::retry s) 1500) #(check-chart s)))))

(defn chart-did-mount [s]
  (render-chart s)
  s)

(defn chart-did-remount [o s]
  (render-chart s)
  s)

(rum/defcs chart < (rum/local "" ::last-url)
                   (rum/local 1 ::retry)
                   {:did-remoout chart-did-remount
                    :did-mount chart-did-mount
                    :did-update chart-did-mount}

  [s topic-data card-width]
  (when (not (empty? (:chart-url topic-data)))
    (let [chart-id (chart-uid topic-data)]
      [:div.chart
        {:key chart-id
         :data-chart-src (:chart-url topic-data)
         :id chart-id}])))