(ns open-company-web.components.finances.utils
  (:require [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]))

(def columns-num 12)

(defn chart-data-at-index [data keyw column-name prefix suffix idx]
  (let [data (to-array data)
        rev-idx (- (- (min (count data) columns-num) 1) idx)
        has-value (< idx (count data))
        obj (if has-value (get data rev-idx) 0)
        value (keyw obj)
        label (if value
                (str (utils/period-string (:period obj)) " " column-name ": " prefix (.toLocaleString (keyw obj)) suffix)
                "Profitable")
        period (if has-value
                 (utils/period-string (:period obj) :short-month)
                 )]
    [period
     value
     label]))

(defn- get-past-period [period diff]
  (let [[year month] (clojure.string/split period "-")
        int-year (int year)
        int-month (int month)
        diff-month (- int-month diff)
        change-year (<= diff-month 0)
        fix-month (if change-year (+ columns-num diff-month) diff-month)
        fix-year (if change-year (dec int-year) int-year)]
    (str fix-year "-" (utils/add-zero fix-month))))

(defn- get-previous-period [period]
  (let [[year month] (clojure.string/split period "-")
        int-year (int year)
        int-month (int month)
        dec-month (dec int-month)
        fix-year (if (zero? dec-month) (dec int-year) int-year)
        fix-month (if (zero? dec-month) 12 dec-month)]
    (str fix-year "-" (utils/add-zero fix-month))))

(defn placeholder-data [data]
  (if (>= (count data) columns-num)
    data
    (let [first-period (or (:period (last data)) (utils/current-period))
          rest-data (- columns-num (count data))
          diff (- columns-num (count data))
          plc-vec (vec (reverse (range rest-data)))
          vect (map (fn [n]
                      {:period (get-past-period first-period (- diff n))
                       :cash 0
                       :revenue 0
                       :costs 0
                       :runway 0
                       :burn-rate 0
                       :value 0})
                    plc-vec)]
      (concat data vect))))

(defn- get-chart-data [data prefix keyw column-name & [style fill-color pattern tooltip-suffix]]
  "Vector of max *columns-num elements of [:Label value]"
  (let [fixed-data (placeholder-data data)
        chart-data (partial chart-data-at-index fixed-data keyw column-name prefix tooltip-suffix)
        placeholder-vect (vec (range columns-num))
        columns [["string" column-name]
                 ["number" (utils/camel-case-str (name keyw))]
                 #js {"type" "string" "role" "tooltip"}]
        columns (if style (conj columns style) columns)
        values (vec (map chart-data placeholder-vect))
        values (if fill-color (map #(assoc % 3 fill-color) values) values)]
    { :prefix prefix
      :columns columns
      :values values
      :pattern (if pattern pattern "###,###.##")}))

(defn get-currency-for-current-company []
  (let [slug (:slug @router/path)
        company-data ((keyword slug) @dispatcher/app-state)]
    (:currency company-data)))