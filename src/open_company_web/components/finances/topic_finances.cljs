(ns open-company-web.components.finances.topic-finances
  (:require [clojure.string :as s]
            [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.ui.d3-dot-chart :refer (d3-dot-chart)]
            [open-company-web.components.ui.d3-column-chart :refer (d3-column-chart)]
            [open-company-web.lib.utils :as utils]))

(defn get-state [owner data & [initial]]
  (let [section-data (:section-data data)]
    {:finances-data (finances-utils/map-placeholder-data (:data section-data))}))

(defn has-revenues-or-costs [finances-data]
  (some #(or (not (zero? (:revenue %))) (not (zero? (:costs %)))) finances-data))


(defn chart-data-at-index [data prefix idx]
  (let [data (to-array data)
        obj (get (vec (reverse data)) idx)
        cash-flow (:burn-rate obj)
        cash-flow-pos? (pos? cash-flow)
        abs-cash-flow (utils/abs cash-flow)]
    [(utils/get-period-string (:period obj) "monthly" [:short])
     (:revenue obj)
     (occ/fill-color :oc-chart-green)
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
     (occ/fill-color (if cash-flow-pos? :oc-chart-green :red))
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
        negative-cash-flow (neg? cash-flow-val)
        abs-cash-flow (utils/abs cash-flow-val)
        cash-flow (when abs-cash-flow (str (when negative-cash-flow "-") prefix (utils/with-metric-prefix abs-cash-flow)))]
   [{:label (when revenue (str "Revenue " revenue))
     :sub-label (str "CASH FLOW - " (utils/get-month (:period data-set)) " " (utils/get-year (:period data-set)))
     :color (occ/get-color-by-kw :oc-gray-5)}
    {:label (when costs (str " Costs " costs))
     :color (occ/get-color-by-kw :oc-gray-5)}
    {:label (when cash-flow (str " Cash flow " cash-flow))
     :color (occ/get-color-by-kw :oc-gray-5)}]))

(defn get-currency-label [cur-symbol value]
  (let [abs-value (utils/abs (or value 0))
        short-value (utils/with-metric-prefix abs-value)]
    (str cur-symbol short-value)))

(defn get-runway-label [value]
  (finances-utils/get-rounded-runway value [:round :short]))
  ;(.log js/console value)
  ;(case
    ;(or (s/blank? value) (= value 0)) "-"
    ;(neg? value) (finances-utils/get-rounded-runway value [:round])
    ;:else "Pofitable"))

(defcomponent topic-finances [{:keys [section section-data currency] :as data} owner options]

  (init-state [_]
    (get-state owner data true))

  (will-update [_ next-props _]
    ; this means the section datas have changed from the API or at a upper lever of this component
    (when-not (= next-props data)
      (om/set-state! owner (get-state owner next-props))))

  (render-state [_ state]
    (let [finances-row-data (:data section-data)
          no-data (or (empty? finances-row-data) (utils/no-finances-data? finances-row-data))]

      (when-not no-data
        (let [fixed-finances-data (finances-utils/fill-gap-months finances-row-data)
              sort-pred (utils/sort-by-key-pred :period)
              sorted-finances (sort sort-pred (vals fixed-finances-data))          
              sum-revenues (apply + (map utils/abs (map :revenue finances-row-data)))
              subsection-data {:section-data section-data
                               :read-only true
                               :currency currency}
              subsection-options {:opts options}
              cur-symbol (utils/get-symbol-for-currency-code currency)
              fixed-sorted-costs (vec (map #(merge % {:label (get-currency-label cur-symbol (:costs %))
                                                      :sub-label (str "BURN - "
                                                                      (utils/get-month (:period %)) " " 
                                                                      (utils/get-year (:period %)))})
                                        sorted-finances))
              chart-opts {:opts {:chart-height 100
                                 :chart-width (:width (:chart-size options))
                                 :chart-keys [:costs]
                                 :interval "monthly"
                                 :label-color (occ/get-color-by-kw :oc-chart-red)
                                 :sub-label-color (occ/get-color-by-kw :oc-gray-5)
                                 :label-key :label
                                 :sub-label-key :sub-label
                                 :svg-click #(when (:topic-click options) ((:topic-click options) nil))
                                 :chart-colors {:costs (occ/get-color-by-kw :oc-chart-red)}
                                 :chart-selected-colors {:costs (occ/get-color-by-kw :oc-chart-red)}
                                 :chart-fill-polygons false
                                 :extra-info-keys [:cash :runway]
                                 :extra-info-labels {:cash "CASH" :runway "RUNWAY"}
                                 :extra-info-colors {:cash (occ/get-color-by-kw :oc-gray-5-3-quarter)
                                                     :runway (occ/get-color-by-kw :oc-gray-5-3-quarter)}
                                 :extra-info-presenters {:cash (partial get-currency-label cur-symbol)
                                                         :runway (partial get-runway-label)}
                                 :hide-nav (:hide-nav options)}}]

          (dom/div {:class "section-container" :id "section-finances"}          

            (dom/div {:class "composed-section finances group"}

              ;; has the company ever had revenue
              (if (pos? sum-revenues) 

                ;; post-revenue gets a bar chart
                (om/build d3-dot-chart {:chart-data fixed-sorted-costs} chart-opts)

                ;; pre-revenue gets a line chart
                (om/build d3-dot-chart {:chart-data fixed-sorted-costs} chart-opts)))))))))