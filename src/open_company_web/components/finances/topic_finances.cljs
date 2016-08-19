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
    (finances-utils/get-rounded-runway value [:round :short])))
  ;(case
    ;(or (s/blank? value) (= value 0)) "-"
    ;(neg? value) (finances-utils/get-rounded-runway value [:round])
    ;:else "Pofitable"))

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
              chart-opts {:chart-height 100
                          :chart-width (:width (:chart-size options))
                          :chart-keys [:costs]
                          :interval "monthly"
                          :svg-click #(when (:topic-click options) ((:topic-click options) nil))
                          :chart-colors {:costs (occ/get-color-by-kw :oc-chart-red)}
                          :chart-selected-colors {:costs (occ/get-color-by-kw :oc-chart-red)}
                          :chart-fill-polygons false
                          :hide-nav (:hide-nav options)}
              labels {:costs {:position :top
                              :order 1
                              :value-presenter (partial get-currency-label cur-symbol)
                              :value-color (occ/get-color-by-kw :oc-red-regular)
                              :label-presenter #(str "BURN")
                              :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)} 
                      :cash {:position :bottom
                             :order 1
                             :value-presenter (partial get-currency-label cur-symbol)
                             :value-color (occ/get-color-by-kw :oc-gray-5-3-quarter)
                             :label-presenter #(str "CASH")
                             :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)} 
                      :runway {:position :bottom
                               :order 2
                               :value-presenter (partial get-runway-label)
                               :value-color (occ/get-color-by-kw :oc-gray-5-3-quarter)
                               :label-presenter #(str "RUNWAY")
                               :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)}}]

          (dom/div {:class "section-container" :id "section-finances"}          

            (dom/div {:class "composed-section finances group"}

              ;; has the company ever had revenue?
              (if (pos? sum-revenues) 

                ;; post-revenue gets a revenue label and a revenue plot
                (let [post-labels (merge labels {:revenue {:position :top
                                                           :order 2
                                                           :value-presenter (partial get-currency-label cur-symbol)
                                                           :value-color (occ/get-color-by-kw :oc-green-regular)
                                                           :label-presenter #(str "REVENUE")
                                                           :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)}})]
                  (om/build d3-chart {:chart-data sorted-finances} {:opts (merge chart-opts {:labels post-labels})}))

                ;; pre-revenue gets just a cost label and plot
                (om/build d3-chart {:chart-data sorted-finances} {:opts (merge chart-opts {:labels labels})})))))))))