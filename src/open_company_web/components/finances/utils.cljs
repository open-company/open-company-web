(ns open-company-web.components.finances.utils
  (:require [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [goog.string :as gstring]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(def columns-num 6)

(defn chart-data-at-index [data keyw column-name prefix suffix idx]
  (let [data (to-array data)
        obj (get (vec (reverse data)) idx)
        has-value (and (contains? obj keyw) (not (nil? (keyw obj))))
        value (keyw obj)
        label (if has-value
                (if (and (= keyw :runway) (zero? value))
                  "Break-even"
                  (str (utils/get-period-string (:period obj)) " " column-name ": " prefix (utils/thousands-separator (keyw obj)) suffix))
                "N/A")
        period (utils/get-period-string (:period obj) "monthly" [:short])]
    [period
     value
     label]))

(defn- get-past-period [period diff]
  (let [period-date (utils/date-from-period period)
        past-date (t/minus period-date (t/months diff))]
    (utils/period-from-date past-date)))

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
  (when (seq initial-data)
    (let [first-period (:period (last initial-data))
          last-period (:period (first initial-data))
          months-diff (utils/periods-diff first-period last-period)]
      (vec
        (for [idx (range 0 (inc months-diff))]
          (let [prev-period (get-past-period last-period idx)
                period-exists (utils/period-exists prev-period initial-data)]
            (if period-exists
              (some #(when (= (:period %) prev-period) %) initial-data)
              (placeholder-data prev-period))))))))

(defn edit-placeholder-data [initial-data]
  (let [current-period (utils/current-period)
        last-period (if (last initial-data)
                      (:period (last initial-data))
                      (get-past-period current-period 12))
        diff (utils/periods-diff last-period current-period)
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
  (when period
    (let [period-date (utils/date-from-period period)
          month-name (utils/month-string (t/month period-date))]
      (str month-name ", " (t/year period-date)))))

(defn map-placeholder-data [data]
  (let [fixed-data (edit-placeholder-data data)]
    (apply merge (map #(hash-map (:period %) %) fixed-data))))

(defn fix-runway [runway]
  (if (neg? runway)
    (utils/abs runway)
    0))

(defn remove-trailing-zero [string]
  "Remove the last zero(s) in a numeric string only after the dot.
   Remote the dot too if it is the last char after removing the zeros"
  (cond

    (and (not= (.indexOf string ".") -1) (= (last string) "0"))
    (remove-trailing-zero (subs string 0 (dec (count string))))

    (= (last string) ".")
    (subs string 0 (dec (count string)))

    :else
    string))

(defn get-rounded-runway [runway-days & [flags]]
  (cond
    (< runway-days 90)
    (str runway-days " days")
    (< runway-days (* 30 24))
    (if (utils/in? flags :round)
      (if (utils/in? flags :remove-trailing-zero)
        (str (remove-trailing-zero (gstring/format "%.2f" (/ runway-days 30))) " months")
        (str (gstring/format "%.2f" (/ runway-days 30)) " months"))
      (str (quot runway-days 30) " months"))
    :else
    (if (utils/in? flags :round)
      (if (utils/in? flags :remove-trailing-zero)
        (str (remove-trailing-zero (gstring/format "%.2f" (/ runway-days (* 30 12)))) " years")
        (str (gstring/format "%.2f" (/ runway-days (* 30 12))) " years"))
      (str (quot runway-days (* 30 12)) " years"))))