(ns open-company-web.components.finances.finances-metric
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-lib :as oc-lib]
            [open-company-web.components.ui.d3-chart :refer (d3-chart)]
            [open-company-web.lib.finance-utils :as finance-utils]
            [open-company-web.router :as router]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.dispatcher :as dispatcher]
            [cuerdas.core :as s]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(defn- format-delta
  "Create a display fragment for a delta value."

  ([delta]
  (let [pos (when (pos? delta) "+")]
    (dom/span {:class "domine"}
      (dom/span {:class "open-sans"}
        pos
        (if (zero? delta) "no change" (str (oc-lib/with-size-label delta) "%"))))))

  ([currency delta]
    (dom/span {:class "domine"}
      (dom/span {:class "open-sans"}
        (if (zero? delta)
          "no change"
          (oc-lib/with-currency currency (oc-lib/with-size-label delta) true))))))

(defn- finance-metric-delta [periods data-key currency]
  (let [finance-metric (first periods)
        period (utils/date-from-period (:period finance-metric))
        date (when period (oc-lib/format-period "monthly" period))
        value (get finance-metric data-key)
        ;; Check for older periods contiguous to most recent
        contiguous-periods (when (seq periods) (oc-lib/contiguous (map :period periods) :monthly))
        prior-contiguous? (>= (count contiguous-periods) 2)
        ;; Info on prior period
        prior-metric (when prior-contiguous?
                        (first (filter #(= (:period %) (second contiguous-periods)) periods)))
        prior-value (when prior-metric (get prior-metric data-key))
        metric-delta (when (and value prior-value) (- value prior-value))
        metric-delta-percent (when metric-delta (* 100 (float (/ metric-delta prior-value))))
        formatted-metric-delta (when metric-delta-percent (format-delta metric-delta-percent))]
    ;; Format output
    (cond
      (= data-key :cash)
      (dom/div {:class "bold"} date)
      (not= data-key :cash)
      (dom/div {:class ""} formatted-metric-delta))))

(defn- label-from-set [data-set data-key currency-symbol]
  (let [actual-val (data-key data-set)
        metric-name (s/capital (s/human (if (= data-key :costs) :expenses data-key)))
        period (utils/get-period-string (:period data-set))
        fixed-cur-unit currency-symbol]
    (when actual-val
      (dom/span {:class (str "domine" (when-not (= data-key :cash) " bold"))}
        (dom/span {:class "open-sans"} fixed-cur-unit (oc-lib/with-size-label actual-val))
        " "
        metric-name
        (when (and (= data-key :cash)
                   (neg? (:runway data-set)))
          (str ", " (finance-utils/get-rounded-runway (:runway data-set)) " runway"))))))

(defn- sub-label [period metric-info]
  (let [mname (:name metric-info)
        interval (:interval metric-info)
        label (if-not (clojure.string/blank? mname) (str mname " - ") "")]
    (str label (utils/get-month period) " " (utils/get-year period))))

(defn get-fixed-sorted-metric [finances-data currency data-key]
  (let [currency-symbol (utils/get-symbol-for-currency-code currency)]
    (vec (map #(merge % {:label (label-from-set % data-key currency-symbol)
                         :sub-label (let [idx (.indexOf (vec (map :period (reverse finances-data))) (:period %))
                                          periods (subvec (vec (reverse finances-data)) idx)]
                                     (finance-metric-delta periods data-key currency-symbol))}) finances-data))))

(defcomponent finances-metric [{:keys [finances-data data-key currency charts-count chart-selected-idx chart-selected-cb] :as data} owner options]

  (init-state [_]
    {:selected-metric-idx 0
     :fixed-sorted-metric (get-fixed-sorted-metric finances-data currency data-key)})

  (will-receive-props [_ next-props]
    (when (not= (:finances-data next-props) finances-data)
      (om/set-state! owner :fixed-sorted-metric (get-fixed-sorted-metric (:finances-data next-props) currency data-key))))

  (render-state [_ {:keys [selected-metric-idx fixed-sorted-metric]}]
    (let [filled-metric-data (finance-utils/fill-gap-months finances-data)
          actual-set (first finances-data)
          period (utils/get-period-string (:period actual-set))
          currency-symbol (utils/get-symbol-for-currency-code (:currency data))
          actual-with-label (label-from-set actual-set data-key currency-symbol)
          cash? (= data-key :cash)
          chart-opts {:opts {:chart-type "unbordered-chart"
                             :chart-keys [data-key]
                             :interval "monthly"
                             :x-axis-labels false
                             :chart-colors (finance-utils/finances-key-colors)
                             :chart-selected-colors (finance-utils/finances-key-colors)
                             :chart-fill-polygons (or (:chart-fill-polygons options) false)
                             :label-color (occ/get-color-by-kw :oc-gray-5)
                             :sub-label-color (occ/get-color-by-kw :oc-gray-5)
                             :sparklines-class "chart-sparklines"
                             :show-chart (not= data-key :cash)
                             :labels {:value {:position :bottom
                                              :order 1
                                              :value-presenter (if cash? #(:sub-label %2) #(or (:label %2) "-"))
                                              :value (occ/get-color-by-kw :oc-blue-dark)
                                              :label-presenter (if cash? #(or (:label %2) "-") #(:sub-label %2))
                                              :label-color (occ/get-color-by-kw :oc-gray-5)}}
                             :hide-nav (:hide-nav options)}}]
      (dom/div {:class (utils/class-set {:section true
                                         data-key true
                                         :fake-chart (:fake-chart data)
                                         :read-only (:read-only data)})
                :key data-key
                :on-click (:start-editing-cb data)}

        (dom/div {}
          (om/build d3-chart {:chart-data fixed-sorted-metric
                              :circle-radius (:circle-radius data)
                              :circle-stroke (:circle-stroke data)
                              :circle-fill (:circle-fill data)
                              :line-stroke-width (:line-stroke-width data)
                              :selected chart-selected-idx
                              :selected-metric-cb chart-selected-cb
                              :circle-selected-stroke (:circle-selected-stroke data)
                              :chart-height (:height (:chart-size data))
                              :chart-width (:width (:chart-size data))
                              :card-width (:card-width data)} chart-opts))))))