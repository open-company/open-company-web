(ns open-company-web.components.growth.growth-metric-edit
  (:require [om.core :as om]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [cljs-dynamic-resources.core :as cdr]
            [open-company-web.caches :refer (new-sections)]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.iso4217 :refer (sorted-iso4217)]
            [open-company-web.components.growth.utils :as growth-utils]
            [cuerdas.core :as s]
            [open-company-web.components.popover :refer (add-popover hide-popover)]))

(defn show-delete-confirm-popover [owner data]
  (add-popover {:container-id "delete-metric-confirm"
                :title (str "Delete " (om/get-state owner :metric-name))
                :message "Are you sure you want to delete this metric and all the associated data?"
                :cancel-title "CANCEL"
                :cancel-cb #(hide-popover nil "delete-metric-confirm")
                :success-title "DELETE"
                :success-cb #((:delete-metric-cb data) (om/get-state owner :metric-slug))
                :success-color-class "red"}))

(defn option-template [state]
  (if-not (.-id state) (.-text state))
  (let [text (.-text state)]
    (js/jQuery (str "<span>" (.-text state) "</span>"))))

(defn unit-option-template [state]
  (if-not (.-id state) (.-text state))
  (let [text (.-text state)
        currency (utils/get-currency text)
        fixed-text (if currency
                     (utils/get-symbol-for-currency-code text)
                     (utils/camel-case-str text))]
    (js/jQuery (str "<span>" fixed-text "</span>"))))

(defn change-name [owner data]
  (let [change-cb (:change-growth-metric-cb data)
        name-value (.val (.$ js/window "input#mtr-name"))
        slug (om/get-state owner :metric-slug)]
    (om/set-state! owner :metric-name name-value)
    ; if it's a newly created metric
    (if (:new-metric data)
      ; change the slug and update all the other fields
      (let [presets (om/get-state owner :presets)
            metrics (:metrics data)
            slugs (vec (map :slug (vals metrics)))
            new-slug (growth-utils/get-slug slugs presets (s/trim name-value))]
        (om/set-state! owner :metric-slug new-slug)
        (change-cb slug {:slug new-slug
                         :description (om/get-state owner :description)
                         :unit (om/get-state owner :unit)
                         :interval (om/get-state owner :interval)
                         :name name-value}))
      ; change only the name
      (change-cb slug {:name name-value}))))

(defn contains-slug? [slug present-metrics]
  (filter (fn [[k _]] (= k slug)) present-metrics))

(defn filter-metrics [preset-metrics present-metrics]
  (vec (filter #(empty? (contains-slug? (:slug %) present-metrics)) preset-metrics)))

(defn init-select2 [owner data]
  ; get needed states
  (let [req-libs-loaded (om/get-state owner :req-libs-loaded)
        did-mount (om/get-state owner :did-mount)
        select2-initialized (om/get-state owner :select2-initialized)
        change-cb (:change-growth-metric-cb data)]
    ; check if we are ready to initialize the widget and if we haven't aready done that
    (when (and req-libs-loaded did-mount (not select2-initialized))
      ; init name
      (let [name-input (.$ js/window "input#mtr-name")
            present-metrics (:metrics data)
            preset-metrics (:metrics (om/get-state owner :presets))
            usable-metrics (filter-metrics preset-metrics present-metrics)
            metrics-list (vec (sort compare (map :name usable-metrics)))
            autocomplete (.autocomplete name-input (clj->js {"source" metrics-list
                                                             "minLength" 0
                                                             "change" #(change-name owner data)}))]
        (.focus name-input (fn [_]
                            (this-as this
                              (when (clojure.string/blank? (.-value this))
                                (.autocomplete name-input "search" (.val name-input))))))
        (.focus name-input)
        (.val name-input (.val name-input)))
      ; init unit
      (doto (.$ js/window "select#mtr-unit")
        (.select2 (clj->js {"placeholder" "Metric unit"
                            "minimumResultsForSearch" -1
                            "templateResult" unit-option-template
                            "templateSelection" unit-option-template}))
        (.on "change" (fn [e]
                        (let [unit-value (.. e -target -value)
                              slug (om/get-state owner :metric-slug)]
                          (om/set-state! owner :unit unit-value)
                          (when slug
                            (change-cb slug {:unit unit-value}))))))
      ; init interval
      (doto (.$ js/window "select.metric-data#mtr-interval")
        (.select2 (clj->js {"placeholder" "Metric interval"
                            "minimumResultsForSearch" -1
                            "templateResult" option-template
                            "templateSelection" option-template}))
        (.on "change" (fn [e]
                        (let [interval-value (.. e -target -value)
                              slug (om/get-state owner :metric-slug)]
                          (om/set-state! owner :interval interval-value)
                          (when slug
                            (change-cb slug {:interval interval-value}))))))
      ; save flag so we don't reinitialize the widget
      (om/update-state! owner :select2-initialized (fn [_]true)))))

(defn get-presets [data]
  (let [slugs (:slugs data)
        slug (keyword (:slug @router/path))
        sections-map (:categories (slug @new-sections))
        all-sections (mapcat :sections sections-map)
        growth-defaults (first (filter #(= (:section-name %) "growth") all-sections))
        all-metrics (:metrics growth-defaults)
        available-metrics (vec (filter #(not (utils/in? slugs (:slug %))) all-metrics))]
    {:intervals (:intervals growth-defaults)
     :units (:units growth-defaults)
     :prompt (:prompt growth-defaults)
     :metrics available-metrics}))

(def metric-defaults {
  :unit "number"
  :interval "monthly"})

(defcomponent growth-metric-edit [data owner]

  (init-state [_]
    (let [metric-info (:metric-info data)
          company-slug (keyword (:slug @router/path))
          company-data (company-slug @dispatcher/app-state)
          company-currency-code (:currency company-data)
          presets (get-presets data)
          units (:units presets)
          fixed-units (vec (map #(if (= (:unit %) "currency")
                                   {:unit "currency"
                                    :name (utils/get-symbol-for-currency-code company-currency-code)}
                                   %) units))
          new-metric (:new-metric data)]
      {:req-libs-loaded false
       :did-mount false
       :select2-initialized false
       :presets presets
       :metric-name (:name metric-info)
       :metric-slug (:slug metric-info)
       :description (:description metric-info)
       :unit (if new-metric (:unit metric-defaults) (:unit metric-info))
       :units fixed-units
       :interval (if new-metric (:interval metric-defaults) (:interval metric-info))
       :currency company-currency-code}))

  (will-mount [_]
    ; load needed resources
    (cdr/add-style! "/lib/select2/css/select2.css")
    (cdr/add-scripts! [{:src "/lib/select2/js/select2.js"}]
                      (fn []
                        (om/update-state! owner :req-libs-loaded (fn [] true))
                        (init-select2 owner data))))

  (did-mount [_]
    (om/set-state! owner :did-mount true)
    (init-select2 owner data))

  (render [_]
    (let [{:keys [new-growth-section metric-info]} data
          {:keys [metrics intervals prompt] :as presets} (om/get-state owner :presets)
          units (om/get-state owner :units)]
      (dom/div {:class "growth-metric-edit"}
        (when (:new-metric data)
          (dom/div {:class "growth-metric-edit-row group"}
            (dom/p {} prompt)))
        ; name and unit
        (dom/div {:class "growth-metric-edit-row group"}
          ; name
          (dom/div {:class "metric-data-container group"}
            (dom/input {:class "metric-data metric-name"
                        :type "text"
                        :value (om/get-state owner :metric-name)
                        :on-change (fn [e]
                                     (om/set-state! owner :metric-name (.. e -target -value))
                                     (change-name owner data))
                        :id "mtr-name"
                        :placeholder "Metric name"
                        :style {"width" "240px"}}))
          ;; delete button
          (when-not (:new-metric data)
            (dom/button {:class "oc-btn oc-cancel black"
                         :title "Delete this metric"
                         :on-click #(show-delete-confirm-popover owner data)} "DELETE")))
        (dom/div {:class "growth-metric-edit-row group"}
          ; unit
          (dom/div {:class "metric-data-container right group"}
            (dom/label {:for "mtr-unit"} "Measured as")
            (dom/select {:class "metric-data metric-unit"
                         :default-value (om/get-state owner :unit)
                         :id "mtr-unit"
                         :placeholder "Metric unit"
                         :style {"width" "145px"}}
              (for [unit units]
                (let [currency (om/get-state owner :currency)
                      unit-value (:unit unit)
                      unit-name (:name unit)]
                  (dom/option {:key unit-value
                               :value unit-value} unit-name)))))
          ; interval
          (dom/div {:class "metric-data-container group"}
            (dom/label {:for "mtr-interval"} "Interval:")
            (dom/select {:class "metric-data metric-interval"
                         :default-value (om/get-state owner :interval)
                         :id "mtr-interval"
                         ; if there are data the interval can't be changed
                         :disabled (and (pos? (:metric-count data))
                                        (not (:new-metric data)))
                         :placeholder "Metric interval"
                         :style {"width" "150px"}}
              (for [interval intervals]
                (dom/option {:value interval} (utils/camel-case-str interval))))))
        ; textarea
        (dom/div {:class "growth-metric-edit-row group"}
          (dom/textarea {:class "metric-data metric-description"
                         :on-change (fn [e]
                                      (let [value (.. e -target -value)
                                            slug (om/get-state owner :metric-slug)
                                            change-cb (:change-growth-metric-cb data)]
                                        (when slug
                                          (change-cb slug {:description value}))))
                         :placeholder "Metric description"} (om/get-state owner :description)))
        (dom/div {:class "growth-metric-edit-row group"}
          (dom/button {:class "oc-btn oc-success green"
                       :disabled (or (s/blank? (om/get-state owner :metric-slug))
                                     (s/blank? (om/get-state owner :metric-name))
                                     (s/blank? (om/get-state owner :unit))
                                     (s/blank? (om/get-state owner :interval)))
                       :on-click #((:next-cb data))} (if (:new-metric data) "NEXT" "SAVE"))
          ; cancel button
          (dom/button {:class "oc-btn oc-link blue"
                       :on-click (:cancel-cb data)}
            (if new-growth-section
              "DELETE"
              "CANCEL")))))))