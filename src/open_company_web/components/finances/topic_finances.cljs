(ns open-company-web.components.finances.topic-finances
  (:require [om.core :as om :include-macros true]
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
    {:focus (if initial
              (or (:selected-metric data) "cash")
              (om/get-state owner :focus))
     :finances-data (finances-utils/map-placeholder-data (:data section-data))}))

; (defn pillbox-click [owner options e]
;   (.preventDefault e)
;   (let [tab  (.. e -target -dataset -tab)]
;     (when (fn? (:switch-metric-cb options))
;       ((:switch-metric-cb options) tab))
;     (om/update-state! owner :focus (fn [] tab)))
;   (.stopPropagation e))

(defn has-revenues-or-costs [finances-data]
  (some #(or (not (zero? (:revenue %))) (not (zero? (:costs %)))) finances-data))

; (defn render-pillboxes [owner options]
;   (let [data (om/get-props owner)
;         section-data (:section-data data)
;         focus (om/get-state owner :focus)
;         classes "pillbox"
;         cash-classes (str classes (when (= focus "cash") " active"))
;         cash-flow-classes (str classes (when (= focus "cash-flow") " active"))
;         runway-classes (str classes (when (= focus "runway") " active"))
;         finances-row-data (:data section-data)
;         sum-revenues (apply + (map :revenue finances-row-data))
;         needs-runway (some #(neg? (:runway %)) finances-row-data)
;         first-title (if (pos? sum-revenues) "Cash flow" "Burn rate")
;         needs-cash-flow (has-revenues-or-costs finances-row-data)]
;     (dom/div {:class "pillbox-container finances"}
;       (dom/label {:class cash-classes
;                   :data-tab "cash"
;                   :on-click (partial pillbox-click owner options)} "Cash")
;       (when needs-cash-flow
;         (dom/label {:class cash-flow-classes
;                     :data-tab "cash-flow"
;                     :on-click (partial pillbox-click owner options)} first-title))
;       (when needs-runway
;         (dom/label {:class runway-classes
;                     :data-tab "runway"
;                     :on-click (partial pillbox-click owner options)} "Runway")))))

(defn chart-data-at-index [data prefix idx]
  (let [data (to-array data)
        obj (get (vec (reverse data)) idx)
        cash-flow (:burn-rate obj)
        cash-flow-pos? (pos? cash-flow)
        abs-cash-flow (utils/abs cash-flow)]
    [(utils/get-period-string (:period obj) "monthly" [:short])
     (:revenue obj)
     (occ/fill-color :oc-new-chart-green)
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
     (occ/fill-color (if cash-flow-pos? :oc-new-chart-green :red))
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

(defn get-label [cur-symbol costs]
  (str cur-symbol (.toLocaleString (js/parseFloat (str (or costs "0"))))))

(defcomponent topic-finances [{:keys [section section-data currency] :as data} owner options]

  (init-state [_]
    (get-state owner data true))

  (will-update [_ next-props _]
    ; this means the section datas have changed from the API or at a upper lever of this component
    (when-not (= next-props data)
      (om/set-state! owner (get-state owner next-props))))

  (render-state [_ state]
    (let [finances-row-data (:data section-data)
          fixed-finances-data (finances-utils/fill-gap-months finances-row-data)
          sort-pred (utils/sort-by-key-pred :period)
          sorted-finances (sort sort-pred (vals fixed-finances-data))          
          no-data (or (empty? finances-row-data) (utils/no-finances-data? finances-row-data))
          sum-revenues (apply + (map utils/abs (map :revenue finances-row-data)))
          subsection-data {:section-data section-data
                           :read-only true
                           :currency currency}
          subsection-options {:opts options}
          currency (:currency data)
          cur-symbol (utils/get-symbol-for-currency-code currency)
          fixed-sorted-costs (vec (map #(merge % {:label (get-label cur-symbol (:costs %))
                                           :sub-label (str "MONTHLY BURN - " (utils/get-month (:period %)) " " (utils/get-year (:period %)))}) sorted-finances))
          chart-opts {:opts {:chart-height (:height (:chart-size options))
                             :chart-width (:width (:chart-size options))
                             :chart-keys [:value]
                             :interval "monthly"
                             :label-color (occ/get-color-by-kw :oc-gray-5)
                             :label-key :label
                             :sub-label-key :sub-label
                             :svg-click #(when (:topic-click options) ((:topic-click options) nil))
                             :chart-colors {:value (occ/get-color-by-kw :oc-new-chart-blue)}
                             :chart-selected-colors {:value (occ/get-color-by-kw :oc-new-chart-blue)}
                             :hide-nav (:hide-nav options)}}]

      (when-not no-data
        (dom/div {:class "section-container" :id "section-finances"}          

          (dom/div {:class "composed-section finances group"}

            ;; has the company ever had revenue
            (if (pos? sum-revenues) 

              (om/build d3-dot-chart {:chart-data fixed-sorted-costs} chart-opts)

              (om/build d3-dot-chart {:chart-data fixed-sorted-costs} chart-opts))
              

            
            ;; chart
            (dom/div {:class (utils/class-set {:composed-section-body true})})
            
              ; (case focus

              ;   "cash"
              ;   (om/build cash subsection-data subsection-options)

              ;   "cash-flow"
              ;   (if (pos? sum-revenues)
              ;     (om/build cash-flow subsection-data subsection-options)
              ;     (om/build costs subsection-data subsection-options))

              ;   "runway"
              ;   (om/build runway subsection-data subsection-options)))
              ;(render-pillboxes owner options)
              ))))))