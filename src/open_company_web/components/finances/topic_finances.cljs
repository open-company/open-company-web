(ns open-company-web.components.finances.topic-finances
  (:require [clojure.string :as s]
            [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.finance-utils :as finance-utils]
            [open-company-web.components.finances.finances-edit :refer (finances-edit)]
            [open-company-web.components.finances.finances-sparklines :refer (finances-sparklines)]
            [open-company-web.components.ui.popover :as popover :refer (add-popover-with-om-component add-popover hide-popover)]))

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
  (om/set-state! owner :finances-raw-data (vals fixed-data)))

;; archive stuff

(defn- show-archive-confirm-popover [owner data]
  (add-popover {:container-id "archive-metric-confirm"
                :message "This chart will be removed but will still appear in prior updates. Are you sure you want to remove it?"
                :cancel-title "KEEP"
                :cancel-cb #(hide-popover nil "archive-metric-confirm")
                :success-title "REMOVE"
                :z-index-offset 1
                :success-cb (fn []
                              (hide-popover nil "archive-metric-confirm")
                              (dispatcher/dispatch! [:foce-input {:data []}])
                              (om/update-state! owner #(merge % {:finances-raw-data {}
                                                                 :table-key (str (rand 4))
                                                                 :has-changes? true})))}))

(defcomponent finances-popover [{:keys [currency finances-raw-data section-data hide-popover-cb table-key data-section-on-change width height] :as data} owner options]
  (render [_]
    (dom/div {:class "oc-popover-container-internal finances composed-section"
              :style {:width "100%" :height "100vh"}}
      (dom/button {:class "close-button"
                   :on-click #(hide-popover-cb)
                   :style {:top "50%"
                           :left "50%"
                           :margin-top (str "-" (/ height 2) "px")
                           :margin-left (str (/ width 2) "px")}}
        (i/icon :simple-remove {:class "inline mr1" :stroke "4" :color "white" :accent-color "white"}))
      (dom/div {:class "oc-popover"
                :on-click (fn [e] (.stopPropagation e))
                :style {:width (str width "px")
                        :height (str height "px")
                        :margin-top (str "-" (/ height 2) "px")
                        :margin-left (str "-" (/ width 2) "px")
                        :text-align "center"
                        :overflow-x "visible"
                        :z-index (+ popover/default-z-index 1)
                        :overflow-y "scroll"}}

        (om/build finances-edit {:finances-data (finance-utils/finances-data-map finances-raw-data)
                                 :currency currency
                                 :table-key table-key
                                 :data-on-change-cb (:finances-data-on-change data)
                                 :editing-cb (:editing-cb data)
                                 :data-section-on-change data-section-on-change
                                 :main-height height
                                 :main-width width}
                                {:key (:updated-at section-data)})))))

(defcomponent topic-finances [{:keys [section section-data currency editable? foce-data-editing? editing-cb table-key data-section-on-change card-width columns-num] :as data} owner options]

  (init-state [_]
    {:finances-raw-data (:data section-data)
     :table-key (str (rand 4))})

  (will-receive-props [_ next-props]
    (when-not (= next-props data)
      (om/set-state! owner {:finances-raw-data (-> next-props :section-data :data)})))

  (did-update [_ prev-props prev-state]
    (when (and (not (:foce-data-editing? prev-props))
               (:foce-data-editing? data))
      (add-popover-with-om-component finances-popover (merge data {:finances-raw-data (om/get-state owner :finances-raw-data)
                                                                   :finances-data-on-change (partial finances-data-on-change owner)
                                                                   :table-key table-key
                                                                   :archive-data-cb #(show-archive-confirm-popover owner data)
                                                                   :hide-popover-cb #(editing-cb false)
                                                                   :editing-cb (partial data-editing-toggle owner editing-cb)
                                                                   :data-section-on-change data-section-on-change
                                                                   :width 400
                                                                   :height (min 380 (.-clientHeight (.-body js/document)))
                                                                   :z-index-popover 0
                                                                   :container-id "finances-edit"})))
    (when (and (:foce-data-editing? prev-props)
               (not (:foce-data-editing? data)))
      (hide-popover nil "finances-edit")))

  (render-state [_ {:keys [finances-raw-data]}]
    (let [no-data (or (empty? finances-raw-data) (utils/no-finances-data? finances-raw-data))]

      (when (not no-data)
        (dom/div {:id "section-finances" :class (utils/class-set {:section-container true
                                                                  :editing foce-data-editing?})}

          (dom/div {:class "composed-section finances group"}
            (let [sort-pred (utils/sort-by-key-pred :period)
                  sorted-finances (sort sort-pred finances-raw-data)
                  sum-revenues (apply + (map utils/abs (map :revenue finances-raw-data)))
                  cur-symbol (utils/get-symbol-for-currency-code currency)
                  chart-opts {:chart-type "bordered-chart"
                              :chart-height 112
                              :chart-width (:width (:chart-size options))
                              :chart-keys [:costs]
                              :interval "monthly"
                              :x-axis-labels true
                              :chart-colors {:costs (occ/get-color-by-kw :oc-red-dark)
                                             :revenue (occ/get-color-by-kw :oc-green-dark)}
                              :chart-selected-colors {:costs (occ/get-color-by-kw :oc-red-dark)
                                                      :revenue (occ/get-color-by-kw :oc-green-dark)}
                              :chart-fill-polygons false
                              :hide-nav (:hide-nav options)}
                  labels {:costs {:value-presenter (partial get-currency-label cur-symbol)
                                  :value-color (occ/get-color-by-kw :oc-red-dark)
                                  :label-presenter (if (pos? sum-revenues) #(str "Expenses") #(str "Burn"))
                                  :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)}
                          :cash {:value-presenter (partial get-currency-label cur-symbol)
                                 :value-color (occ/get-color-by-kw :oc-gray-5-3-quarter)
                                 :label-presenter #(str "Cash")
                                 :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)}
                          :revenue {:value-presenter (partial get-runway-label)
                                    :value-color (occ/get-color-by-kw :oc-gray-5-3-quarter)
                                    :label-presenter #(str "Revenue")
                                    :label-color (occ/get-color-by-kw :oc-gray-5-3-quarter)}}]

              (om/build finances-sparklines {:finances-data sorted-finances
                                             :archive-cb #(show-archive-confirm-popover owner data)
                                             :card-width card-width
                                             :columns-num columns-num
                                             :editing? editable?
                                             :currency currency}
                                            {:opts (merge chart-opts {:labels labels})}))))))))