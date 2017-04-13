(ns oc.web.components.chart
  (:require [rum.core :as rum]
            [oc.web.lib.charts :as c]
            [oc.web.lib.utils :as utils]))

(defn render-chart [s]
  (let [chart-url (first (:rum/args s))]
    (when (and (not (empty? chart-url))
               (not= @(::last-url s) chart-url))
      (c/load-chart-url chart-url (second (:rum/args s))))))

(defn chart-did-mount [s]
  (render-chart s)
  s)

(defn chart-did-remount [o s]
  (render-chart s)
  s)

(rum/defcs chart < (rum/local "" ::last-url)
                   {:did-remoout chart-did-remount
                    :did-mount chart-did-mount
                    :did-update chart-did-mount}

  [s gchart-link card-width]
  [:div.chart
    {:key gchart-link
     :id (str "chart-" gchart-link)}])