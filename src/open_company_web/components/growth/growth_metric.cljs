(ns open-company-web.components.growth.growth-metric
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.charts :refer (column-chart)]
            [clojure.string :as clj-str]
            [open-company-web.lib.oc-colors :as occ]))

(defn get-columns-num [interval]
  (case interval
    "monthly" 12
    8))

(defn get-graph-tooltip [period label prefix value suffix]
  (str label
       ": "
       (or prefix "")
       (if value (.toLocaleString value) "")
       (if suffix (str " " suffix) "")))

(defn chart-data-at-index [data column-name columns-num prefix suffix has-target interval idx]
  (let [data (to-array data)
        rev-idx (- (dec columns-num) idx)
        obj (get data rev-idx)
        value (:value obj)
        target (or (:target obj) 0)
        label (get-graph-tooltip (:period obj) column-name prefix value suffix)
        target-label (get-graph-tooltip (:period obj) "target" prefix (.toLocaleString target) suffix)
        period (utils/get-period-string (:period obj) interval [:short])
        values (if has-target
                 [period
                  target
                  (occ/fill-color :gray)
                  target-label
                  value
                  (occ/fill-color :blue)
                  label]
                 [period
                  value
                  (occ/fill-color :blue)
                  label])]
    values))

(defn- get-past-period [period diff columns-num]
  (let [[year month] (clojure.string/split period "-")
        int-year (int year)
        int-month (int month)
        diff-month (- int-month diff)
        change-year (<= diff-month 0)
        fix-month (if change-year (+ columns-num diff-month) diff-month)
        fix-year (if change-year (dec int-year) int-year)]
    (str fix-year "-" (utils/add-zero fix-month))))

(defn placeholder-data [data columns-num]
  (if (>= (count data) columns-num)
    data
    (let [first-period (or (:period (last data)) (utils/current-period))
          rest-data (- columns-num (count data))
          diff (- columns-num (count data))
          plc-vec (vec (reverse (range rest-data)))
          vect (map (fn [n]
                      {:period (get-past-period first-period (- diff n) columns-num)
                       :slug (:slug first-period)
                       :value 0})
                    plc-vec)]
      (concat data vect))))

(defn- get-chart-data
  "Vector of max *columns elements of [:Label value]"
  [data prefix column-name tooltip-suffix columns-num interval]
  (let [fixed-data (placeholder-data data columns-num)
        has-target (some #(:target %) data)
        chart-data (partial chart-data-at-index fixed-data column-name columns-num prefix tooltip-suffix has-target interval)
        columns (if has-target
                  [["string" column-name]
                   ["number" "target"]
                   #js {"type" "string" "role" "style"}
                   #js {"type" "string" "role" "tooltip"}
                   ["number" column-name]
                   #js {"type" "string" "role" "style"}
                   #js {"type" "string" "role" "tooltip"}]
                  [["string" column-name]
                   ["number" column-name]
                   #js {"type" "string" "role" "style"}
                   #js {"type" "string" "role" "tooltip"}])
        mapper (vec (range columns-num))
        values (vec (map chart-data mapper))]
    { :prefix (if prefix prefix "")
      :columns columns
      :values values
      :pattern "###,###.##"
      :column-thickness (if has-target "28" "14")}))

(defn get-actual [metrics]
  (some #(when (:value (metrics %)) %) (vec (range (count metrics)))))

(defcomponent growth-metric [data owner]

  (render [_]
    (let [metric-info (:metric-info data)
          metric-data (:metric-data data)
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-metric (vec (sort #(sort-pred %1 %2) metric-data))
          actual-idx (get-actual sorted-metric)
          actual-set (sorted-metric actual-idx)
          actual (.toLocaleString (:value actual-set))
          interval (:interval metric-info)
          period (utils/get-period-string (:period actual-set) interval)
          metric-unit (:unit metric-info)
          cur-unit (utils/get-symbol-for-currency-code metric-unit)
          fixed-cur-unit (if (= cur-unit metric-unit)
                            nil
                            cur-unit)
          unit (if fixed-cur-unit nil (utils/camel-case-str metric-unit))
          actual-with-label (if fixed-cur-unit
                              (str fixed-cur-unit actual)
                              (str actual (if (= unit "%") "" " ") unit))]
      (dom/div {:class (utils/class-set {:section true
                                         (:slug metric-info) true
                                         :read-only (:read-only data)})
                :key (:slug metric-info)}
        (when (> (count metric-data) 0)
          (dom/div {}
            (dom/div {:class "chart-header-container"}
              (dom/div {:class "target-actual-container"}
                (dom/div {:class "actual-container"}
                  (dom/h3 {:class "actual blue"} actual-with-label)
                  (dom/h3 {:class "actual-label gray"} (str "as of " (utils/get-period-string (:period actual-set) interval))))))
            (om/build column-chart (get-chart-data sorted-metric
                                                   fixed-cur-unit
                                                   (:name metric-info)
                                                   unit
                                                   (get-columns-num interval)
                                                   interval))))))))