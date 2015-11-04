(ns open-company-web.components.growth.growth-metric
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.charts :refer [column-chart]]
            [clojure.string :as clj-str]))

(def columns 7)

(defn chart-data-at-index [data column-name prefix suffix idx]
  (let [data (to-array data)
        rev-idx (- (dec (min (count data) columns)) idx)
        obj (get data rev-idx)
        value (:value obj)
        label (str (utils/period-string (:period obj))
                   " "
                   column-name
                   ": "
                   (if prefix prefix "")
                   (if value (.toLocaleString value) "")
                   (if suffix (str " " suffix) ""))]
    [(utils/period-string (:period obj))
     value
     label]))

(defn- get-chart-data [data prefix column-name style fill-color tooltip-suffix]
  "Vector of max *columns elements of [:Label value]"
  (let [chart-data (partial chart-data-at-index data column-name prefix tooltip-suffix)
        placeholder-vect (into [] (range (min (count data) columns)))
        columns [["string" column-name]
                 ["number" column-name]
                 #js {"type" "string" "role" "tooltip"}]
        columns (if style (conj columns style) columns)
        values (into [] (map chart-data placeholder-vect))
        values (if fill-color (map #(assoc % 3 fill-color) values) values)]
    { :prefix (if prefix prefix "")
      :columns columns
      :values values
      :pattern "###,###.##"}))

(defcomponent growth-metric [data owner]
  (render [_]
    (let [metric-info (:metric-info data)
          metric-data (:metric-data data)
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-metric (sort #(sort-pred %1 %2) metric-data)
          value-set (first sorted-metric)
          period (utils/period-string (:period value-set))
          metric-unit (:unit metric-info)
          cur-unit (utils/get-symbol-for-currency-code metric-unit)
          fixed-cur-unit (if (= cur-unit metric-unit)
                     nil
                     cur-unit)
          unit (if fixed-cur-unit nil (utils/camel-case-str metric-unit))
          value (if (:value value-set) (.toLocaleString (:value value-set)) "")
          name-has-unit (.indexOf (clj-str/lower-case (str (:name metric-info))) (clj-str/lower-case metric-unit))
          label (if fixed-cur-unit
                  (str fixed-cur-unit value)
                  (if name-has-unit
                    (str value)
                    (str value " " unit)))]
      (dom/div {:class (utils/class-set {:section true
                                         (:slug metric-info) true
                                         :read-only (:read-only data)})}
        (when (> (count metric-data) 0)
          (dom/div {}
            (om/build column-chart (get-chart-data sorted-metric
                                                   fixed-cur-unit
                                                   (:name metric-info)
                                                   #js {"type" "string" "role" "style"}
                                                   "fill-color: #ADADAD"
                                                   unit))
            (dom/div {:class "chart-footer-container"}
              (dom/div {:class (utils/class-set {:target-value-container true :double (:target value-set)})}
                (when (:target value-set)
                  (dom/div {:class "target-container"}
                    (dom/h3 {:class "target"} (.toLocaleString (:target value-set)))
                    (dom/h3 {:class "target-label"} "TARGET")))
                (dom/div {:class "value-container"}
                  (dom/h3 {:class "value"} label)
                  (dom/h3 {:class "value-label"} "VALUE"))))
            (dom/div {:class "chart-footer-container"}
              (dom/div {:class "period-container"}
                (dom/p {} period)))
            ))))))
