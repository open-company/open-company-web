(ns open-company-web.components.finances.cash-flow
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.d3-column-chart :refer (d3-column-chart)]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.lib.oc-colors :as occ]))

(defn chart-data-at-index [data prefix idx]
  (let [data (to-array data)
        obj (get (vec (reverse data)) idx)
        cash-flow (:burn-rate obj)
        cash-flow-pos? (pos? cash-flow)
        abs-cash-flow (utils/abs cash-flow)]
    [(utils/get-period-string (:period obj) "monthly" [:short])
     (:revenue obj)
     (occ/fill-color :oc-green-light)
     (str (utils/get-period-string (:period obj))
          " Revenue: "
          prefix
          (utils/thousands-separator (or (:revenue obj) 0)))
     (:costs obj)
     (occ/fill-color :red)
     (str (utils/get-period-string (:period obj))
          " Costs: "
          prefix
          (utils/thousands-separator (or (:costs obj) 0)))
     abs-cash-flow
     (occ/fill-color (if cash-flow-pos? :oc-green-light :red))
     (str (utils/get-period-string (:period obj))
          " Cash flow: "
          (when (neg? cash-flow) "-")
          prefix
          (utils/thousands-separator abs-cash-flow))]))

(defn- get-chart-data [data prefix]
  "Vector of max *columns elements of [:Label value]"
  (let [fixed-data (finances-utils/chart-placeholder-data data)
        chart-data (partial chart-data-at-index fixed-data prefix)
        placeholder-vect (range (count fixed-data))]
    { :prefix prefix
      :columns [["string" "Period"]
                ["number" "Revenue"]
                #js {"type" "string" "role" "style"}
                #js {"type" "string" "role" "tooltip"}
                ["number" "Costs"]
                #js {"type" "string" "role" "style"}
                #js {"type" "string" "role" "tooltip"}
                ["number" "Cash flow"]
                #js {"type" "string" "role" "style"}
                #js {"type" "string" "role" "tooltip"}]
      :values (vec (map chart-data placeholder-vect))
      :pattern "######.##"
      :max-show finances-utils/columns-num
      :column-thickness "42"}))


(defn chart-label-fn [prefix data-set]
  ; example: Revenue $12.3K Costs $107K Cash flow -$94.7K
  (let [revenue (when (:revenue data-set) (str prefix (utils/with-metric-prefix (:revenue data-set))))
        costs (when (:costs data-set) (str prefix (utils/with-metric-prefix (:costs data-set))))
        cash-flow-val (when (and revenue costs) (- (:revenue data-set) (:costs data-set)))
        cash-flow (when cash-flow-val (str (when (neg? cash-flow-val) "-") prefix (utils/with-metric-prefix cash-flow-val)))]
   [{:label (when revenue (str "Revenue " revenue))
     :sub-label (str "CASH FLOW - " (utils/get-month (:period data-set)) " " (utils/get-year (:period data-set)))
     :color (occ/get-color-by-kw :oc-gray-5)}
    {:label (when costs (str " Costs " costs))
     :color (occ/get-color-by-kw :oc-gray-5)}
    {:label (when cash-flow (str " Cash flow " cash-flow))
     :color (occ/get-color-by-kw :oc-gray-5)}]))

(defcomponent cash-flow [data owner options]
  
  (render [_]
    (let [finances-data (:data (:section-data data))
          fixed-finances-data (finances-utils/fill-gap-months finances-data)
          sort-pred (utils/sort-by-key-pred :period)
          sorted-finances (sort sort-pred (vals fixed-finances-data))
          all-revenues (remove js/isNaN (remove nil? (map #(js/parseFloat (:revenue %)) (:data data))))
          has-revenues (pos? (apply + all-revenues))
          value-set (last sorted-finances)
          currency (:currency data)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          cash-val (str cur-symbol (utils/thousands-separator (:cash value-set)))
          cash-flow-val (or (:avg-burn-rate value-set) 0)
          [year month] (clojure.string/split (:period value-set) "-")
          int-month (int month)
          month-3 (- int-month 2)
          month-3-fixed (utils/add-zero (if (<= month-3 0) (- 12 month-3) month-3))
          fixed-sorted-finances (vec (map #(merge % {:label (chart-label-fn cur-symbol %)}) sorted-finances))
          chart-opts {:opts {:chart-height (:height (:chart-size options))
                             :chart-width (:width (:chart-size options))
                             :chart-keys (if has-revenues
                                           [:revenue :costs]
                                           [:costs])
                             :label-key :label
                             :sub-label-key :sub-label
                             :interval "monthly"
                             :h-axis-color (occ/get-color-by-kw :oc-green-light)
                             :h-axis-selected-color (occ/get-color-by-kw :oc-green-regular)
                             :chart-colors (if has-revenues
                                            {:revenue (occ/get-color-by-kw :oc-green-light)
                                             :costs (occ/get-color-by-kw :oc-red-light)}
                                            {:costs (occ/get-color-by-kw :oc-red-light)})
                             :chart-selected-colors (if has-revenues
                                                      {:revenue (occ/get-color-by-kw :oc-green-regular)
                                                       :costs (occ/get-color-by-kw :oc-red-regular)}
                                                      {:costs (occ/get-color-by-kw :oc-red-regular)})
                             :prefix (utils/get-symbol-for-currency-code currency)}}]
      (dom/div {:class (utils/class-set {:section true
                                         :cash-flow true
                                         :read-only (:read-only data)})
                :on-click (:start-editing-cb data)}
        (when (:show-label options)
          (dom/div {:class "chart-header-container"}
            (dom/div {:class "target-actual-container"}
              (dom/div {:class "actual-container"}
                (dom/h3 {:class (utils/class-set {:actual true
                                                  :green (pos? cash-flow-val)
                                                  :red (not (pos? cash-flow-val))})}
                                                 (str cur-symbol (utils/thousands-separator (int cash-flow-val))))
                (dom/h3 {:class "actual-label gray"}
                        (str "3 months avg. " (utils/month-string month-3-fixed) " to " (utils/month-string month)))))))
        (om/build d3-column-chart {:chart-data fixed-sorted-finances} chart-opts)))))