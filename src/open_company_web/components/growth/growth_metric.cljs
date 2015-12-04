(ns open-company-web.components.growth.growth-metric
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.charts :refer (column-chart)]
            [clojure.string :as clj-str]
            [open-company-web.lib.oc-colors :as occ]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

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

(defn chart-data-at-index [data column-name prefix suffix has-target interval idx]
  (let [data (to-array data)
        obj (get (vec (reverse data)) idx)
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

(defn- monthly-past-period [period diff]
  (let [[year month] (clojure.string/split period "-")
          period-date (t/date-time (int year) (int month))
          past-date (t/minus period-date (t/months diff))
          formatter (utils/get-formatter "monthly")]
      (f/unparse formatter past-date)))

(defn- get-past-period [period diff interval]
  (cond
    ;; TODO: add get-past-period for weekly and quarterly
    (= interval "monthly")
    (monthly-past-period period diff)))

(defn chart-placeholder-data [initial-data interval]
  (let [first-period (:period (last initial-data))
        last-period (:period (first initial-data))
        months-diff (utils/periods-diff-in-months first-period last-period)]
    (vec
      (for [idx (range 0 (inc months-diff))]
        (let [prev-period (get-past-period last-period idx interval)
              period-exists (utils/period-exists prev-period initial-data)]
          (if period-exists
            (some #(when (= (:period %) prev-period) %) initial-data)
            {:period prev-period
             :slug (:slug first-period)
             :value nil}))))))

(defn- get-chart-data
  "Vector of max *columns elements of [:Label value]"
  [data prefix column-name tooltip-suffix interval]
  (let [fixed-data (chart-placeholder-data data interval)
        has-target (some #(:target %) data)
        chart-data (partial chart-data-at-index fixed-data column-name prefix tooltip-suffix has-target interval)
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
        mapper (vec (range (count fixed-data)))
        values (vec (map chart-data mapper))]
    { :prefix (if prefix prefix "")
      :columns columns
      :max-show (get-columns-num interval)
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
                                                   interval))))))))