(ns open-company-web.components.finances.topic-finances
  (:require [clojure.string :as s]
            [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.components.ui.popover :as popover :refer (add-popover-with-om-component hide-popover)]
            [open-company-web.components.finances.finances-edit :refer (finances-edit)]
            [open-company-web.components.ui.d3-chart :refer (d3-chart)]
            [open-company-web.lib.finance-utils :as finance-utils]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.lib.utils :as utils]))

(defn- has-revenues-or-costs [finances-data]
  (some #(or (not (zero? (:revenue %))) (not (zero? (:costs %)))) finances-data))

(defn- get-currency-label [cur-symbol selected-key data]
  (let [value (get data selected-key)
        abs-value (utils/abs (or value 0))
        short-value (utils/with-metric-prefix abs-value)]
    (if (s/blank? value)
      "-"
      (str cur-symbol short-value))))

(defn- get-runway-label [selected-key data]
  (let [value (get data selected-key)
        ten-years (* -1 10 365)]
    (cond
      (or (s/blank? value) (= value 0)) "-"
      (<= value ten-years) ">9yrs"
      (neg? value) (finance-utils/get-rounded-runway value [:round :short])
      :else "-")))

(defn- data-editing-toggle [owner editing-cb editing]
  (editing-cb editing))

(defn finances-data-on-change [owner fixed-data]
  (om/set-state! owner :finances-row-data (vals fixed-data)))

(defcomponent finances-popover [{:keys [currency finances-row-data section-data hide-popover-cb] :as data} owner options]
  (render [_]
    (dom/div {:class "oc-popover-container-internal finances composed-section"
              :style {:width "100%" :height "100vh"}}
      (dom/button {:class "close-button"
                   :on-click #(hide-popover-cb)
                   :style {:top "50%"
                           :left "50%"
                           :margin-top "-225px"
                           :z-index (+ popover/default-z-index 2)
                           :margin-left "195px"}}
        (dom/i {:class "fa fa-times"}))
      (dom/div {:class "oc-popover"
                :on-click (fn [e] (.stopPropagation e))
                :style {:width "390px"
                        :height "450px"
                        :margin-top "-225px"
                        :text-align "center"
                        :overflow-x "visible"
                        :z-index (+ popover/default-z-index 1)
                        :overflow-y "scroll"}}
        (dom/h3 {} "Finances edit")

        (om/build finances-edit {:finances-data (finance-utils/finances-data-map finances-row-data)
                                 :currency currency
                                 :data-on-change-cb (:finances-data-on-change data)
                                 :editing-cb (:editing-cb data)}
                                {:key (:updated-at section-data)})))))

(defcomponent topic-finances [{:keys [section section-data currency editable? initial-editing? editing-cb] :as data} owner options]

  (init-state [_]
    {:data-editing? initial-editing?
     :finances-row-data (:data section-data)})

  (will-receive-props [_ next-props]
    (when-not (= next-props data)
      (om/set-state! owner {:data-editing? (:foce-data-editing? next-props)
                            :finances-row-data (-> next-props :section-data :data)})))

  (did-update [_ _ prev-state]
    (when (and (not (:data-editing? prev-state))
               (om/get-state owner :data-editing?))
      (add-popover-with-om-component finances-popover {:data (merge data {:finances-row-data (om/get-state owner :finances-row-data)
                                                                          :finances-data-on-change (partial finances-data-on-change owner)
                                                                          :hide-popover-cb #(do
                                                                                              (editing-cb false))
                                                                          :editing-cb (partial data-editing-toggle owner editing-cb)})
                                                       :width 390
                                                       :height 450
                                                       :z-index-popover 0
                                                       :container-id "finances-edit"}))
    (when (and (:data-editing? prev-state)
               (not (om/get-state owner :data-editing?)))
      (hide-popover nil "finances-edit")))

  (render-state [_ {:keys [data-editing? finances-row-data]}]
    (let [no-data (or (empty? finances-row-data) (utils/no-finances-data? finances-row-data))
          data-editing? (or initial-editing? data-editing?)]

      (when (not no-data)
        (dom/div {:id "section-finances" :class (utils/class-set {:section-container true
                                                                  :editing data-editing?})}

          (dom/div {:class "composed-section finances group"}
            (let [show-placeholder-chart? (and data-editing?
                                               (< (count finances-row-data) 2))
                  fixed-finances-data (if show-placeholder-chart?
                                        (finance-utils/finances-data-map (:data (utils/fix-finances {:data (finance-utils/fake-chart-placeholder-data)})))
                                        (finance-utils/fill-gap-months finances-row-data))
                  sort-pred (utils/sort-by-key-pred :period)
                  sorted-finances (sort sort-pred (vals fixed-finances-data))
                  sum-revenues (apply + (map utils/abs (map :revenue finances-row-data)))
                  cur-symbol (utils/get-symbol-for-currency-code currency)
                  chart-opts {:chart-type (str "bordered-chart" (when show-placeholder-chart? " fake-chart"))
                              :chart-height 112
                              :chart-width (:width (:chart-size options))
                              :chart-keys [:costs]
                              :interval "monthly"
                              :x-axis-labels true
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
                                      (assoc-in [:cash :position] :bottom)
                                      (assoc-in [:cash :order] 1)
                                      (assoc-in [:costs :position] :bottom)
                                      (assoc-in [:costs :order] 2)
                                      (assoc-in [:runway :position] :bottom)
                                      (assoc-in [:runway :order] 3))]
                  (om/build d3-chart {:chart-data sorted-finances}
                                     {:opts (merge chart-opts {:labels ordered-labels})}))))

            (when editable?
              (dom/button {:class "btn-reset chart-pencil-button"
                           :title "Edit chart data"
                           :type "button"
                           :data-toggle "tooltip"
                           :data-container "body"
                           :data-placement "left"
                           :on-click #(editing-cb true)}
                (dom/i {:class "fa fa-pencil editable-pen"})))))))))