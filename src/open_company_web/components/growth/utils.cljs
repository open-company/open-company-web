(ns open-company-web.components.growth.utils
  (:require [open-company-web.lib.utils :as utils]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(defn columns-num [interval]
  (case interval
    "quarterly" 8
    "monthly" 12
    "weekly" 8))

(defn get-minus [diff interval]
  (case interval
    "quarterly" (t/months (* diff 3))
    "monthly" (t/months diff)
    "weekly" (t/weeks diff)))

(defn- get-past-period [period diff interval]
  (let [[year month] (clojure.string/split period "-")
        period-date (t/date-time (int year) (int month))
        past-date (t/minus period-date (get-minus diff interval))
        formatter (utils/get-formatter interval)]
    (f/unparse formatter past-date)))

(defn placeholder-data [period slug]
  {:period period
   :slug slug
   :value nil
   :target nil
   :new true})

(defn edit-placeholder-data [initial-data slug interval]
  (let [tot-num (columns-num interval)
        current-period (utils/current-growth-period interval)
        first-period (if (first initial-data)
                      (:period (first initial-data))
                      (get-past-period current-period 12 interval))
        diff (utils/periods-diff first-period current-period interval)
        data-count (max (dec tot-num) diff)]
    (let [fixed-data (for [idx (range 0 data-count)]
                       (let [prev-period (get-past-period current-period idx interval)
                             period-exists (utils/period-exists prev-period initial-data)]
                         (if period-exists
                           (some #(when (= (:period %) prev-period) %) initial-data)
                           (placeholder-data prev-period slug))))]
      (vec fixed-data))))

(defn map-placeholder-data [data slug interval]
  (let [fixed-data (edit-placeholder-data data slug interval)]
    (apply merge (map #(hash-map (str (:period %) slug) %) fixed-data))))