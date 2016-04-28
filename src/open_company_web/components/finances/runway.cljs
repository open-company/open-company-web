(ns open-company-web.components.finances.runway
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.d3-column-chart :refer (d3-column-chart)]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.lib.oc-colors :as occ]))

(defn get-runway-subtitle [cash avg-burn-rate runway-days cur-symbol]
  (str cur-symbol (utils/thousands-separator (or cash 0))
       " ÷ a 3-month avg. burn of "
       cur-symbol (utils/thousands-separator (utils/abs (or (int avg-burn-rate) 0)))
       " ≅ "
       (str (finances-utils/get-rounded-runway runway-days [:round :remove-trailing-zero]))))

(defn runway-data-set [data-set]
  (let [runway (:runway data-set)
        fixed-runway (if (neg? runway) (utils/abs runway) 0)
        label (when runway (if (neg? runway) (finances-utils/get-rounded-runway runway [:round]) "Pofitable"))]
    (merge data-set {:runway fixed-runway
                     :label label})))

(defn runway-data [sorted-data]
  (vec (map runway-data-set sorted-data)))

(defcomponent runway [data owner options]
  
  (render [_]
    (let [finances-data (:data (:section-data data))
          fixed-finances-data (finances-utils/fill-gap-months finances-data)
          sort-pred (utils/sort-by-key-pred :period)
          sorted-finances (sort sort-pred (vals fixed-finances-data))
          fixed-runway-finances (map #(update-in % [:runway] finances-utils/fix-runway) sorted-finances)
          value-set (last fixed-runway-finances)
          runway-value (:runway value-set)
          is-profitable (pos? (:runway (last sorted-finances)))
          runway (if is-profitable
                    "Profitable"
                    (str (utils/thousands-separator (utils/abs runway-value)) " days"))
          currency (:currency data)
          cur-symbol (utils/get-symbol-for-currency-code (:currency data))
          runway-string (if is-profitable
                          runway
                          (finances-utils/get-rounded-runway runway-value))
          fixed-sorted-finances (runway-data sorted-finances)
          chart-opts {:opts {:chart-height (when (contains? options :chart-size) (:height (:chart-size options)))
                             :chart-width (when (contains? options :chart-size)(:width (:chart-size options)))
                             :chart-keys [:runway]
                             :label-color (occ/get-color-by-kw :oc-green-regular)
                             :label-key :label
                             :interval "monthly"
                             :h-axis-color (occ/get-color-by-kw :oc-green-light)
                             :h-axis-selected-color (occ/get-color-by-kw :oc-green-regular)
                             :chart-colors {:runway (occ/get-color-by-kw :oc-green-light)}
                             :chart-selected-colors {:runway (occ/get-color-by-kw :oc-green-regular)}
                             :prefix (utils/get-symbol-for-currency-code currency)}}]
      (dom/div {:class (str "section runway" (when (:read-only data) " read-only"))
                :on-click (:start-editing-cb data)}
        (when (:show-label options)
          (dom/div {:class "chart-header-container"}
            (dom/div {:class "target-actual-container"}
              (dom/div {:class "actual-container"}
                (dom/h3 {:class "actual green"} runway-string)
                (dom/h3 {:class "actual-label gray"} (get-runway-subtitle (:cash value-set) (:avg-burn-rate value-set) runway-value cur-symbol))))))
        (om/build d3-column-chart {:chart-data fixed-sorted-finances} chart-opts)))))