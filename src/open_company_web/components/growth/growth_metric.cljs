(ns open-company-web.components.growth.growth-metric
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.charts :refer [column-chart]]
            [clojure.string :as clj-str]))

(def columns-num 7)

(defn chart-data-at-index [data column-name prefix suffix has-target idx]
  (let [data (to-array data)
        rev-idx (- (dec (min (count data) columns-num)) idx)
        obj (get data rev-idx)
        value (:value obj)
        target (or (:target obj) 0)
        label (str (utils/period-string (:period obj))
                   " "
                   column-name
                   ": "
                   (if prefix prefix "")
                   (if value (.toLocaleString value) "")
                   (if suffix (str " " suffix) ""))
        target-label (str (utils/period-string (:period obj))
                           " target: "
                           (if prefix prefix "")
                           (if value (.toLocaleString target) "")
                           (if suffix (str " " suffix) ""))
        values (if has-target
                 [(utils/period-string (:period obj))
                  target
                  "fill-color: #DDDDDD"
                  target-label
                  value
                  "fill-color: #109DB7"
                  label]
                 [(utils/period-string (:period obj))
                  value
                  "fill-color: #109DB7"
                  label])]
    values))

(defn- get-chart-data [data prefix column-name tooltip-suffix]
  "Vector of max *columns elements of [:Label value]"
  (let [has-target (some #(:target %) data)
        chart-data (partial chart-data-at-index data column-name prefix tooltip-suffix has-target)
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
        mapper (vec (range (min (count data) columns-num)))
        values (vec (map chart-data mapper))]
    { :prefix (if prefix prefix "")
      :columns columns
      :values values
      :pattern "###,###.##"
      :column-thickness (if has-target "30%" "10%")}))

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
          period (utils/period-string (:period actual-set))
          metric-unit (:unit metric-info)
          cur-unit (utils/get-symbol-for-currency-code metric-unit)
          fixed-cur-unit (if (= cur-unit metric-unit)
                     nil
                     cur-unit)
          unit (if fixed-cur-unit nil (utils/camel-case-str metric-unit))
          name-has-unit (.indexOf (clj-str/lower-case (str (:name metric-info))) (clj-str/lower-case metric-unit))
          label (if fixed-cur-unit
                  (str fixed-cur-unit actual)
                  (if name-has-unit
                    (str actual)
                    (str actual " " unit)))
          chart-data (get-chart-data sorted-metric
                                     fixed-cur-unit
                                     (:name metric-info)
                                     unit)]
      (dom/div {:class (utils/class-set {:section true
                                         (:slug metric-info) true
                                         :read-only (:read-only data)})}
        (when (> (count metric-data) 0)
          (dom/div {}
            (om/build column-chart chart-data)
            (dom/div {:class "chart-footer-container"}
              (dom/div {:class (utils/class-set {:target-actual-container true :double (:target actual-set)})}
                (when (:target actual-set)
                  (dom/div {:class "target-container"}
                    (dom/h3 {:class "target"} (.toLocaleString (:target actual-set)))
                    (dom/h3 {:class "target-label"} "TARGET")))
                (dom/div {:class "actual-container"}
                  (dom/h3 {:class "actual"} label)
                  (dom/h3 {:class "actual-label"} "ACTUAL"))))
            (dom/div {:class "chart-footer-container"}
              (dom/div {:class "period-container"}
                (dom/p {} period)))
            ))))))
