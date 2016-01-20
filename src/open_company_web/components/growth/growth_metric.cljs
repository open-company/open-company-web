(ns open-company-web.components.growth.growth-metric
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.charts :refer (column-chart)]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.ui.utility-components :refer (editable-pen)]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(defn get-graph-tooltip [label prefix value suffix]
  (str label
       ": "
       (or prefix "")
       (if value (.toLocaleString value) "")
       (if suffix (str " " suffix) "")))

(defn chart-data-at-index [data column-name prefix suffix has-target interval idx]
  (let [data (to-array data)
        obj (get (vec (reverse data)) idx)
        value (or (:value obj) 0)
        target (or (:target obj) 0)
        label (get-graph-tooltip column-name prefix value suffix)
        target-label (get-graph-tooltip (str column-name " target") prefix (.toLocaleString target) suffix)
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

(defn- get-chart-data
  "Vector of max *columns elements of [:Label value]"
  [data prefix slug column-name tooltip-suffix interval]
  (let [fixed-data (growth-utils/chart-placeholder-data data slug interval)
        has-target (some :target data)
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
      :max-show (growth-utils/columns-num interval)
      :values values
      :pattern "###,###.##"
      :column-thickness (if has-target "28" "14")}))

(defn get-actual [metrics]
  (some #(when (:value (metrics %)) %) (vec (range (count metrics)))))

(defcomponent growth-metric [data owner]

  (render [_]
    (let [slug (keyword (:slug @router/path))
          company-data (slug @dispatcher/app-state)
          metric-info (:metric-info data)
          metric-data (:metric-data data)
          sort-pred (utils/sort-by-key-pred :period true)
          sorted-metric (vec (sort sort-pred metric-data))
          actual-idx (get-actual sorted-metric)
          actual-set (sorted-metric actual-idx)
          actual (.toLocaleString (or (:value actual-set) 0))
          interval (:interval metric-info)
          period (utils/get-period-string (:period actual-set) interval)
          metric-unit (:unit metric-info)
          fixed-cur-unit (when (= metric-unit "currency")
                           (utils/get-symbol-for-currency-code (:currency company-data)))
          unit (when (= metric-unit "%") "%")
          actual-with-label (str fixed-cur-unit actual unit)]
      (dom/div {:class (utils/class-set {:section true
                                         (:slug metric-info) true
                                         :read-only (:read-only data)})
                :key (:slug metric-info)
                :on-click (:start-editing-cb data)}
        (when (pos? (count metric-data))
          (dom/div {}
            (dom/div {:class "chart-header-container"}
              (dom/div {:class "target-actual-container"}
                (dom/div {:class "actual-container"}
                  (dom/h3 {:class "actual blue"} actual-with-label
                    (om/build editable-pen {:click-callback (:start-data-editing-cb data)}))
                  (dom/h3 {:class "actual-label gray"} (str "as of " period)))))
            (om/build column-chart (get-chart-data sorted-metric
                                                   fixed-cur-unit
                                                   (:slug metric-info)
                                                   (:name metric-info)
                                                   unit
                                                   interval))))))))