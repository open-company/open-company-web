(ns open-company-web.components.growth.topic-growth
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.growth.growth-metric :refer (growth-metric)]
            [open-company-web.components.growth.growth-edit :refer (growth-edit)]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.caches :refer (company-cache)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.components.ui.popover :refer (add-popover hide-popover)]
            [cuerdas.core :as s]))

(def focus-cache-key :last-selected-metric)

(def new-metric-preset
  {:name ""
   :description ""
   :slug growth-utils/new-metric-slug-placeholder
   :interval "monthly"
   :unit "number"})

(defn- switch-focus [owner focus options]
  (utils/company-cache-key focus-cache-key focus)
  (om/set-state! owner :focus focus)
  (when (fn? (:switch-metric-cb options))
    ((:switch-metric-cb options) focus)))

(defn- metrics-map [metrics-coll]
  (apply merge (map #(hash-map (:slug %) %) (reverse metrics-coll))))

(defn- metrics-order [metrics-coll]
  (map :slug metrics-coll))

(defn- pillbox-click [owner options e]
  (let [data (om/get-props owner)
        focus  (.. e -target -dataset -tab)
        section-data (:section-data data)
        metrics (metrics-map (:metrics section-data))]
    (switch-focus owner focus options)))

(defn- filter-growth-data [focus growth-data]
  (vec (filter #(= (:slug %) focus) (vals growth-data))))

(defn- data-editing-toggle [owner editing-cb editing? & [new-metric?]]
  (if editing?
    (when new-metric?
      ; if entering editing mode for a new metric
      ; set the focus to the new metric slug
      (om/set-state! owner :focus growth-utils/new-metric-slug-placeholder)
      (om/set-state! owner :new-metric? true)
      ; and add a placeholder metadata to the metrics map
      (let [growth-metrics (om/get-state owner :growth-metrics)
            new-metrics (assoc growth-metrics growth-utils/new-metric-slug-placeholder new-metric-preset)]
        (om/set-state! owner :growth-metrics new-metrics)))
    ; disable new-metric? if exiting the editing
    (om/set-state! owner :new-metric? false))
  ; set editing
  (om/set-state! owner :data-editing? editing?)
  (editing-cb editing?))

(defn- archive-metric-cb [owner editing-cb metric-slug]
  (let [metric-slugs (remove #{metric-slug} (om/get-state owner :growth-metric-slugs)) ; remove the slug
        focus (first metric-slugs)
        fewer-metrics (growth-utils/metrics-as-sequence (om/get-state owner :growth-metrics) metric-slugs)]
    (om/set-state! owner :focus focus) ; new focus on the first remaining metric
    (om/set-state! owner :growth-metric-slugs metric-slugs) ; update valid slugs state
    (om/set-state! owner :growth-metrics (metrics-map fewer-metrics))
    ;; if last focus is the removing metric, remove the last focus cache
    (when (= (utils/company-cache-key focus-cache-key) metric-slug)
      (utils/remove-company-cache-key focus-cache-key))
    (dis/dispatch! [:save-topic-data "growth" {:metrics fewer-metrics}])
    (data-editing-toggle owner editing-cb false))) ; no longer data editing

(defn- show-archive-confirm-popover [owner editing-cb metric-slug]
  (add-popover {:container-id "archive-metric-confirm"
                :message "Prior updates to this chart will only be available in topic history. Are you sure you want to archive?"
                :cancel-title "KEEP"
                :cancel-cb #(hide-popover nil "delete-metric-confirm")
                :success-title "ARCHIVE"
                :success-cb #(archive-metric-cb owner editing-cb metric-slug)}))

(defn- render-pillboxes [owner editable? editing-cb options]

  (let [data (om/get-props owner)
        growth-metric-slugs (om/get-state owner :growth-metric-slugs)
        growth-metrics (om/get-state owner :growth-metrics)
        focus (om/get-state owner :focus)
        data-editing? (om/get-state owner :data-editing?)]

    (dom/div {:class "pillbox-container growth"}

      (when (and focus (> (count growth-metric-slugs) 1))
        (for [metric-slug growth-metric-slugs]
          (let [metric (get growth-metrics metric-slug)
                mname (:name metric)
                metric-classes (utils/class-set {:pillbox true
                                                 metric-slug true
                                                 :active (= focus metric-slug)})]
            (dom/label {:class metric-classes
                        :title (:description metric)
                        :data-toggle "tooltip"
                        :data-container "body"
                        :data-placement "bottom"
                        :data-tab metric-slug
                        :on-click (partial pillbox-click owner options)} mname)))))))

(defn- get-state [owner data initial]
  (let [section-data (:section-data data)
        all-metrics (:metrics section-data)
        metrics (if initial (metrics-map all-metrics) (om/get-state owner :growth-metrics))
        first-metric (:slug (first (:metrics section-data)))
        last-focus (utils/company-cache-key focus-cache-key)
        focus (if initial
                (or (:selected-metric data) last-focus first-metric)
                (om/get-state owner :focus)) ; preserve focus if this is for will-update
        growth-data (growth-utils/growth-data-map (:data section-data))
        metric-slugs (metrics-order all-metrics)
        new-metric? (if initial (not focus) (om/get-state owner :new-metric?))]
    {:growth-data growth-data
     :growth-metrics metrics
     :growth-metric-slugs metric-slugs
     :focus focus
     :new-metric? (if initial new-metric? (om/get-state owner :new-metric?)) ; preserve new metric if this is for will-update
     :data-editing? (:initial-editing? data)}))

(defn- data-editing-on-change [owner new-data]
  (om/update-state! owner #(merge % {:growth-data new-data})))

(defn- metadata-editing-on-change [owner focus k v]
  (let [metrics (om/get-state owner :growth-metrics)
        metric (get metrics focus)
        new-metric (assoc metric k v)
        new-metrics (assoc metrics (if (= k :slug) v focus) new-metric)]
    (om/set-state! owner :growth-metrics new-metrics)))

(defcomponent topic-growth [{:keys [section section-data currency editable? initial-editing? editing-cb] :as data} owner options]

  (init-state [_]
    (get-state owner data true))

  (will-update [_ next-props _]
    ;; this means the section data has changed from the API or at a upper lever of this component
    (when-not (= next-props data)
      (om/set-state! owner (get-state owner next-props false))))

  (render-state [_ {:keys [focus growth-metrics growth-data growth-metric-slugs metric-slug data-editing? new-metric?]}]
    (let [section-name (utils/camel-case-str (name section))
          no-data (utils/no-growth-data? growth-data)
          focus-metric-data (filter-growth-data focus growth-data)
          focus-metric-info (get growth-metrics focus)
          show-placeholder-chart? (and data-editing?
                                       (< (count focus-metric-data) 2))
          subsection-data {:metric-data (if show-placeholder-chart?
                                          (growth-utils/fake-chart-placeholder-data focus-metric-info)
                                          focus-metric-data)
                           :metric-info focus-metric-info
                           :fake-chart show-placeholder-chart?
                           :editing data-editing?
                           :focus focus
                           :currency currency
                           :read-only true
                           :total-metrics (count growth-metrics)}]
      (dom/div {:id "section-growth"
                :class (utils/class-set {:section-container true
                                         :editing data-editing?})
                :key (name section)}

        ; Chart
        (when-not no-data
          (dom/div {:class "composed-section growth group"}
            ; growth data chart
            (dom/div {:class (utils/class-set {:composed-section-body true})}
              ;; growth metric currently shown
              (when (and focus (seq (:metric-data subsection-data)))
                (om/build growth-metric subsection-data {:opts options}))
              (when (or (> (count growth-metric-slugs) 1)
                        editable?
                        (not data-editing?))
                (render-pillboxes owner editable? editing-cb options))
              (when (and editable? (not data-editing?))
                (dom/button {:class "btn-reset chart-pencil-button"
                             :title "Edit chart data"
                             :type "button"
                             :data-toggle "tooltip"
                             :data-container "body"
                             :data-placement "right"
                             :on-click #(do (om/set-state! owner :data-editing? true)
                                            (editing-cb true))}
                  (dom/i {:class "fa fa-pencil editable-pen"})))
              (when (and editable? (not data-editing?))
                (dom/button {:class "btn-reset chart-plus-button"
                             :title "Add a chart"
                             :type "button"
                             :data-toggle "tooltip"
                             :data-container "body"
                             :data-placement "right"
                             :on-click (fn [e]
                                         (data-editing-toggle owner editing-cb true true))}
                  (dom/i {:class "fa fa-plus"})))
              (when (and editable?
                         (not data-editing?)
                         (not= focus growth-utils/new-metric-slug-placeholder))
                (dom/button {:class "btn-reset chart-archive-button"
                             :title "Archive chart"
                             :type "button"
                             :data-toggle "tooltip"
                             :data-container "body"
                             :data-placement "right"
                             :on-click #(show-archive-confirm-popover owner editing-cb focus)}
                  (dom/i {:class "fa fa-archive"}))))))

        ; Data/metadata edit
        (when data-editing?
          (om/build growth-edit {
             :initial-focus focus
             :new-metric? new-metric?
             :growth-data growth-data
             :metrics growth-metrics
             :metric-slugs growth-metric-slugs
             :data-on-change-cb (partial data-editing-on-change owner)
             :metadata-on-change-cb (partial metadata-editing-on-change owner focus)
             :editing-cb (partial data-editing-toggle owner editing-cb)
             :switch-focus-cb (partial switch-focus owner)
             :archive-metric-cb (partial show-archive-confirm-popover owner editing-cb focus)}
            {:opts {:currency currency} :key growth-data}))))))