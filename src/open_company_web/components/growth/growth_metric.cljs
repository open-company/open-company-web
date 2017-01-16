(ns open-company-web.components.growth.growth-metric
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-lib :as oc-lib]
            [open-company-web.components.ui.d3-chart :refer (d3-chart)]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.router :as router]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(defn- format-delta
  "Create a display fragment for a delta value."

  ([delta]
  (let [pos (when (pos? delta) "+")]
    (dom/span {:class "open-sans"}
      "("
      (dom/span
        pos
        (if (zero? delta) "no change" (str (oc-lib/with-size-label delta) "%")))
      ")")))

  ([currency delta]
    (dom/span {:class "open-sans"}
      "("
      (dom/span
        (if (zero? delta) "no change" (oc-lib/with-currency currency (oc-lib/with-size-label delta) true)))
      ")")))

(defn- growth-metric-delta [periods {metric-name :name unit :unit interval :interval :as metadatum} currency]
  (let [growth-metric (first periods)
        slug (:slug growth-metric)
        period (when interval (utils/date-from-period (:period growth-metric) interval))
        date (when (and interval period) (oc-lib/format-period interval period))
        value (:value growth-metric)
        ;; Check for older periods contiguous to most recent
        contiguous-periods (when (seq periods) (oc-lib/contiguous (map :period periods) (keyword interval)))
        prior-contiguous? (>= (count contiguous-periods) 2)
        ;; Info on prior period
        prior-metric (when prior-contiguous?
                        (first (filter #(= (:period %) (second contiguous-periods)) periods)))
        prior-value (when prior-metric (:value prior-metric))
        metric-delta (when (and value prior-value (not= prior-value 0)) (- value prior-value))
        metric-delta-percent (when metric-delta (* 100 (float (/ metric-delta prior-value))))
        formatted-metric-delta (when metric-delta-percent (format-delta metric-delta-percent))]
    ;; Format output
    (dom/div {} date " " formatted-metric-delta)))

(defn- label-from-set [data-set metric-name description interval metric-unit currency-symbol]
  (let [actual-val (:value data-set)
        period (utils/get-period-string (:period data-set) interval)
        fixed-cur-unit (when (= metric-unit "currency") currency-symbol)
        unit (when (= metric-unit "%") "%")]
    (when actual-val
      (dom/span {:class "bold open-sans"
                 :data-toggle "tooltip"
                 :data-container "body"
                 :data-placement "top"
                 :title description}
        (dom/span {:class "open-sans"}
          fixed-cur-unit (oc-lib/with-size-label actual-val) unit)
        " " metric-name))))

(defn- sub-label [period metric-info]
  (let [mname (:name metric-info)
        interval (:interval metric-info)
        label (if-not (clojure.string/blank? mname) (str mname " - ") "")]
    (cond
      (= interval "weekly")
      (str label (utils/get-weekly-period-day period) " " (utils/get-month period "weekly") " " (utils/get-year period "weekly"))
      (= interval "quarterly")
      (str label (utils/get-quarter-from-period period [:short]) " " (utils/get-year period))
      :else
      (str label (utils/get-month period) " " (utils/get-year period)))))

(defcomponent growth-metric [data owner options]

  (did-update [_ _ _]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))

  (render [_]
    (let [{:keys [slug interval] :as metric-info} (:metric-info data)
          metric-data (:metric-data data)
          filled-metric-data (growth-utils/fill-gap-months metric-data slug interval)
          sort-pred (utils/sort-by-key-pred :period false)
          sorted-metric (vec (sort sort-pred (vals filled-metric-data)))
          actual-idx (growth-utils/get-actual sorted-metric)
          actual-set (sorted-metric actual-idx)
          metric-unit (:unit metric-info)
          period (utils/get-period-string (:period actual-set) interval)
          currency-symbol (utils/get-symbol-for-currency-code (:currency data))
          actual-with-label (label-from-set actual-set (:name metric-info) (:description metric-info) interval metric-unit currency-symbol)
          fixed-sorted-metric (vec (map #(merge % {:label (label-from-set % (:name metric-info) (:description metric-info) interval metric-unit currency-symbol)
                                                   :sub-label (let [idx (.indexOf (vec (map :period (reverse sorted-metric))) (:period %))
                                                                    periods (subvec (vec (reverse sorted-metric)) idx)]
                                                                (growth-metric-delta periods metric-info currency-symbol))}) sorted-metric))
          chart-opts {:opts {:chart-type "unbordered-chart"
                             :chart-keys [:value]
                             :interval interval
                             :x-axis-labels false
                             :chart-colors {:value (occ/get-color-by-kw :oc-blue-dark)}
                             :chart-selected-colors {:value (occ/get-color-by-kw :oc-blue-dark)}
                             :chart-fill-polygons (or (:chart-fill-polygons options) false)
                             :label-color (occ/get-color-by-kw :oc-gray-5)
                             :sub-label-color (occ/get-color-by-kw :oc-gray-5)
                             :sparklines-class "chart-sparklines growth-sparklines"
                             :show-chart true
                             :labels {:value {:position :bottom
                                              :order 1
                                              :value-presenter #(or (:label %2) (dom/span {:class "bold"} (:name metric-info)))
                                              :value (occ/get-color-by-kw :oc-blue-dark) 
                                              :label-presenter #(:sub-label %2)
                                              :label-color (occ/get-color-by-kw :oc-gray-5)}}
                             :hide-nav (:hide-nav options)}}]
      (dom/div {:class (utils/class-set {:section true
                                         slug true
                                         :fake-chart (:fake-chart data)
                                         :read-only (:read-only data)})
                :key slug
                :on-click (:start-editing-cb data)}
        (when (or (pos? (count metric-data)) (:name metric-info))
          (dom/div {}
            (om/build d3-chart {:chart-data fixed-sorted-metric
                                :circle-radius (:circle-radius data)
                                :circle-stroke (:circle-stroke data)
                                :circle-fill (:circle-fill data)
                                :line-stroke-width (:line-stroke-width data)
                                :circle-selected-stroke (:circle-selected-stroke data)
                                :chart-height (:height (:chart-size data))
                                :chart-width (:width (:chart-size data))
                                :card-width (:card-width data)} chart-opts)))))))