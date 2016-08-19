(ns open-company-web.components.finances.topic-finances
  (:require [clojure.string :as s]
            [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.ui.d3-chart :refer (d3-chart)]
            [open-company-web.lib.utils :as utils]))

(defn- has-revenues-or-costs [finances-data]
  (some #(or (not (zero? (:revenue %))) (not (zero? (:costs %)))) finances-data))

(defn- get-currency-label [cur-symbol selected-key data]
  (let [value (get data selected-key)
        abs-value (utils/abs (or value 0))
        short-value (utils/with-metric-prefix abs-value)]
    (str cur-symbol short-value)))

(defn- get-runway-label [selected-key data]
  (let [value (get data selected-key)]
    (cond
      (or (s/blank? value) (= value 0)) "-"
      (neg? value) (finances-utils/get-rounded-runway value [:round :short])
      :else "Profitable")))

(defn- get-state [owner data & [initial]]
  (let [section-data (:section-data data)]
    {:finances-data (finances-utils/map-placeholder-data (:data section-data))}))

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
              cur-symbol (utils/get-symbol-for-currency-code currency)
              chart-opts {:chart-type "bordered-chart"
                          :chart-height 112
                          :chart-width (:width (:chart-size options))
                          :chart-keys [:costs]
                          :interval "monthly"
                          :x-axis-labels true
                          :svg-click #(when (:topic-click options) ((:topic-click options) nil))
                          :chart-colors {:costs (occ/get-color-by-kw :oc-red-regular)
                                         :revenue (occ/get-color-by-kw :oc-green-regular)}
                          :chart-selected-colors {:costs (occ/get-color-by-kw :oc-red-regular)
                                                  :revenue (occ/get-color-by-kw :oc-green-regular)}
                          :chart-fill-polygons false
                          :hide-nav (:hide-nav options)}
              labels {:costs {:value-presenter (partial get-currency-label cur-symbol)
                              :value-color (occ/get-color-by-kw :oc-red-regular)
                              :label-presenter (if (pos? sum-revenues) #(str "EXPENSES") #(str "BURN"))
                              :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)} 
                      :cash {:value-presenter (partial get-currency-label cur-symbol)
                             :value-color (occ/get-color-by-kw :oc-gray-5-3-quarter)
                             :label-presenter #(str "CASH")
                             :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)} 
                      :runway {:value-presenter (partial get-runway-label)
                               :value-color (occ/get-color-by-kw :oc-gray-5-3-quarter)
                               :label-presenter #(str "RUNWAY")
                               :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)}}]

          (dom/div {:class "section-container" :id "section-finances"}          

            (dom/div {:class "composed-section finances group"}

              ;; has the company ever had revenue?
              (if (pos? sum-revenues)
                ;; post-revenue gets an additional revenue label, 4 total labels in 2 rows
                (let [revenue-labels (merge labels {:revenue {:position :top
                                                              :order 1
                                                              :value-presenter (partial get-currency-label cur-symbol)
                                                              :value-color (occ/get-color-by-kw :oc-green-regular)
                                                              :label-presenter #(str "REVENUE")
                                                              :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)}})
                      ordered-labels (-> revenue-labels
                                      (assoc-in [:costs :position] :top)
                                      (assoc-in [:costs :order] 2)
                                      (assoc-in [:cash :position] :bottom)
                                      (assoc-in [:cash :order] 1)
                                      (assoc-in [:runway :position] :bottom)
                                      (assoc-in [:runway :order] 2))]
                  (om/build d3-chart {:chart-data sorted-finances}
                                     {:opts (merge chart-opts {:labels ordered-labels
                                                               :chart-keys [:costs :revenue]})}))

                ;; pre-revenue gets just 3 labels in 1 row
                (let [ordered-labels (-> labels
                                      (assoc-in [:cash :position] :top)
                                      (assoc-in [:cash :order] 1)
                                      (assoc-in [:costs :position] :top)
                                      (assoc-in [:costs :order] 2)
                                      (assoc-in [:runway :position] :top)
                                      (assoc-in [:runway :order] 3))]
                  (om/build d3-chart {:chart-data sorted-finances}
                                     {:opts (merge chart-opts {:labels ordered-labels})}))))))))))