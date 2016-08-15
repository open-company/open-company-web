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
            [open-company-web.components.ui.popover :refer (add-popover hide-popover)]))

(defn show-archive-confirm-popover [owner data]
  (add-popover {:container-id "archive-metric-confirm"
                :title (str "Archive " (om/get-state owner :metric-name))
                :message "Archiving removes this metric, but you won’t lose prior updates if you add it again later. Are you sure you want to archive this metric?"
                :cancel-title "CANCEL"
                :cancel-cb #(hide-popover nil "delete-metric-confirm")
                :success-title "ARCHIVE"
                :success-cb #((:delete-metric-cb data) (om/get-state owner :metric-slug))}))

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
        name-value (.val (js/$ "input#mtr-name"))
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
      (let [name-input (js/$ "input#mtr-name")
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
      (doto (js/$ "select#mtr-unit")
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
      (doto (js/$ "select#mtr-interval")
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
        slug (keyword (router/current-company-slug))
        sections-map (:categories (slug @new-sections))
        all-sections (mapcat :sections sections-map)
        growth-defaults (first (filter #(= (:section-name %) "growth") all-sections))
        all-metrics (:metrics growth-defaults)
        available-metrics (vec (filter #(not (utils/in? slugs (:slug %))) all-metrics))]
    {:intervals (:intervals growth-defaults)
     :units (:units growth-defaults)
     :metrics available-metrics}))

(def metric-defaults {
  :unit "number"
  :interval "monthly"})

(defcomponent growth-metric-edit [data owner options]

  (init-state [_]
    (let [metric-info (:metric-info data)
          company-currency-code (:currency options)
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
          all-metrics (:metrics data)
          {:keys [metrics intervals prompt] :as presets} (om/get-state owner :presets)
          units (om/get-state owner :units)]

      (dom/div {:class "growth-metric-edit p3"}

        ;; name
        (dom/div {:class "small-caps bold mb1"} "Metric name")
        (dom/input {:class "npt col-5 p1 mb3"
          :type "text"
          :value (om/get-state owner :metric-name)
          :on-change (fn [e]
                       (om/set-state! owner :metric-name (.. e -target -value))
                       (change-name owner data))
          :id "mtr-name"
          :placeholder "DAU"})

        ;; interval
        (dom/div {:class "small-caps bold mb1"} "Interval")
        (dom/select {:class "npt col-3 p1 mb3"
                     :default-value (om/get-state owner :interval)
                     :id "mtr-interval"
                     ; if there are data the interval can't be changed
                     :disabled (and (pos? (:metric-count data))
                                    (not (:new-metric data)))}
          (for [interval intervals]
            (dom/option {:value interval} (utils/camel-case-str interval))))

        ;; unit
        (dom/div {:class "small-caps bold mb1"} "Measured As")
        (dom/select {:class "npt col-3 p1 mb3"
                     :default-value (or (om/get-state owner :unit) "A number")
                     :id "mtr-unit"}
          (for [unit units]
            (dom/option {:value (:unit unit)} (:name unit))))

        ;; description
        (dom/div {:class "small-caps bold mb1"} "Description for tooltip")
        (dom/input {:class "npt col-10 p1 mb3"
                    :type "text"
                    :value (om/get-state owner :description)
                    :on-change (fn [e]
                                  (let [value (.. e -target -value)
                                        slug (om/get-state owner :metric-slug)
                                        change-cb (:change-growth-metric-cb data)]
                                    (om/set-state! owner :description value)
                                    (when slug
                                      (change-cb slug {:description value}))))})

        (dom/div
          ;; add or save button
          (dom/button {:class "btn-reset btn-solid mr1 primary-button"
                       :disabled (or (s/blank? (om/get-state owner :metric-slug))
                                     (s/blank? (om/get-state owner :metric-name))
                                     (s/blank? (om/get-state owner :unit))
                                     (s/blank? (om/get-state owner :interval)))
                       :on-click #((:save-cb data))}
            "SAVE")
          ;; archive button
          (when-not (:new-metric data)
            (dom/button {:class "btn-reset btn-outline mr1 secondary-button"
                         :title "Archive this metric"
                         :on-click #(show-archive-confirm-popover owner data)} "ARCHIVE"))
          ;; cancel button
          (dom/button {:class "btn-reset btn-outline mr1 secondary-button"
                       :on-click (:cancel-cb data)} "CANCEL"))))))