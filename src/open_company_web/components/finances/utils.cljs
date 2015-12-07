(ns open-company-web.components.finances.utils
  (:require [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(def columns-num 12)

(defn chart-data-at-index [data keyw column-name prefix suffix idx]
  (let [data (to-array data)
        obj (get (vec (reverse data)) idx)
        has-value (and (contains? obj keyw) (not (nil? (keyw obj))))
        value (keyw obj)
        label (if has-value
                (if (and (= keyw :runway) (zero? value))
                  "Profitable"
                  (str (utils/period-string (:period obj)) " " column-name ": " prefix (.toLocaleString (keyw obj)) suffix))
                "N/A")
        period (utils/period-string (:period obj) :short-month)]
    [period
     value
     label]))

(defn- get-past-period [period diff]
  (let [[year month] (clojure.string/split period "-")
        period-date (t/date-time (int year) (int month))
        past-date (t/minus period-date (t/months diff))
        formatter (utils/get-formatter "monthly")]
    (f/unparse formatter past-date)))

(defn placeholder-data [period]
  {:period period
   :cash nil
   :costs nil
   :revenue nil
   :burn-rate nil
   :runway nil
   :avg-burn-rate nil
   :new true})

(defn chart-placeholder-data [initial-data]
  (let [first-period (:period (last initial-data))
        last-period (:period (first initial-data))
        months-diff (utils/periods-diff-in-months first-period last-period)]
    (vec
      (for [idx (range 0 (inc months-diff))]
        (let [prev-period (get-past-period last-period idx)
              period-exists (utils/period-exists prev-period initial-data)]
          (if period-exists
            (some #(when (= (:period %) prev-period) %) initial-data)
            (placeholder-data prev-period)))))))

(defn edit-placeholder-data [initial-data]
  (let [current-period (utils/current-period)
        last-period (if (last initial-data)
                      (:period (last initial-data))
                      (get-past-period current-period 12))
        diff (utils/periods-diff-in-months last-period current-period)
        data-count (max 13 (inc diff))]
    (let [fixed-data (for [idx (range 1 data-count)]
                       (let [prev-period (get-past-period current-period idx)
                             period-exists (utils/period-exists prev-period initial-data)]
                         (if period-exists
                           (some #(when (= (:period %) prev-period) %) initial-data)
                           (placeholder-data prev-period))))]
      (vec fixed-data))))

(defn- get-chart-data [data prefix keyw column-name & [style fill-color pattern tooltip-suffix]]
  "Vector of max *(count fixed-data) elements of [:Label value]"
  (let [fixed-data (chart-placeholder-data data)
        chart-data (partial chart-data-at-index fixed-data keyw column-name prefix tooltip-suffix)
        placeholder-vect (vec (range (count fixed-data)))
        columns [["string" column-name]
                 ["number" (utils/camel-case-str (name keyw))]
                 #js {"type" "string" "role" "tooltip"}]
        columns (if style (conj columns style) columns)
        values (vec (map chart-data placeholder-vect))
        values-with-color (if fill-color
                            (map #(assoc % 3 fill-color) values)
                            values)]
    { :prefix prefix
      :columns columns
      :values values-with-color
      :max-show columns-num
      :pattern (if pattern pattern "###,###.##")}))

(defn get-as-of-string [period]
  (let [[year month] (clojure.string/split period "-")
        month-name (utils/month-string month)]
    (str month-name ", " year)))

(defn map-placeholder-data [data]
  (let [fixed-data (edit-placeholder-data data)]
    (apply merge (map #(hash-map (:period %) %) fixed-data))))