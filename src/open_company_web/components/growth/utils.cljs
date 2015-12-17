(ns open-company-web.components.growth.utils
  (:require [open-company-web.lib.utils :as utils]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(defn columns-num [interval]
  (case interval
    "quarterly"
    8
    "weekly"
    8
    ;else
    12))

(defn get-minus [diff interval]
  (case interval
    "quarterly" (t/months (* diff 4))
    "monthly" (t/months diff)
    "weekly" (t/weeks diff)))

(defn- get-past-period [period diff interval]
  (let [period-date (utils/date-from-period period interval)
        past-date (t/minus period-date (get-minus diff interval))
        formatter (utils/get-formatter interval)]
    (f/unparse formatter past-date)))

(def metric-placeholder
  {:slug nil
   :name nil
   :interval "monthly"
   :goal nil})

(defn placeholder-data [period slug]
  {:period period
   :slug slug
   :value nil
   :target nil
   :new true})

(defn chart-placeholder-data [initial-data slug interval]
  (let [first-period (:period (last initial-data))
        last-period (:period (first initial-data))
        period-diff (utils/periods-diff first-period last-period interval)]
    (vec
      (for [idx (range 0 (inc period-diff))]
        (let [prev-period (get-past-period last-period idx interval)
              period-exists (utils/period-exists prev-period initial-data)]
          (if period-exists
            (some #(when (= (:period %) prev-period) %) initial-data)
            (placeholder-data prev-period slug)))))))

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
  (if (and slug interval)
    (let [fixed-data (edit-placeholder-data data slug interval)]
      (apply merge (map #(hash-map (str (:period %) slug) %) fixed-data)))
    {}))