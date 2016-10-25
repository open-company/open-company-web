(ns open-company-web.components.growth.topic-growth
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.growth.growth-edit :refer (growth-edit)]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.caches :refer (company-cache)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.components.ui.popover :refer (add-popover-with-om-component add-popover hide-popover)]
            [open-company-web.components.growth.growth-sparklines :refer (growth-sparklines)]
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
                :z-index-offset 0
                :success-cb #(archive-metric-cb owner editing-cb metric-slug)}))

(defcomponent growth-popover [{:keys [initial-focus
                                      new-metric?
                                      growth-data
                                      growth-metrics
                                      curency
                                      hide-popover-cb
                                      growth-metric-slugs
                                      growth-editing-on-change
                                      growth-metadata-editing-on-change
                                      growth-data-editing-toggle
                                      growth-switch-focus
                                      growth-show-archive-confirm-popover] :as data} owner options]
  (render [_]
    (dom/div {:class "oc-popover-container-internal growth composed-section"
              :style {:width "100%" :height "100vh"}}
      (dom/button {:class "close-button"
                   :on-click #(hide-popover-cb)
                   :style {:top "50%"
                           :left "50%"
                           :margin-top "-225px"
                           :margin-left "195px"}}
        (dom/i {:class "fa fa-times"}))
      (dom/div {:class "oc-popover "
                :on-click (fn [e] (.stopPropagation e))
                :style {:width "390px"
                        :height "450px"
                        :margin-top "-225px"
                        :text-align "center"
                        :overflow-x "visible"
                        :overflow-y "scroll"}}
        (dom/h3 {} "Growth edit")

        (om/build growth-edit {:initial-focus initial-focus
                               :new-metric? new-metric?
                               :growth-data growth-data
                               :metrics growth-metrics
                               :currency curency
                               :metric-slugs growth-metric-slugs
                               :data-on-change-cb growth-editing-on-change
                               :metadata-on-change-cb growth-metadata-editing-on-change
                               :editing-cb growth-data-editing-toggle
                               :switch-focus-cb growth-switch-focus
                               :archive-metric-cb growth-show-archive-confirm-popover})))))

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
     }))

(defn- data-editing-on-change [owner new-data]
  (om/update-state! owner #(merge % {:growth-data new-data})))

(defn- metadata-editing-on-change [owner focus k v]
  (let [metrics (om/get-state owner :growth-metrics)
        metric (get metrics focus)
        new-metric (assoc metric k v)
        new-metrics (assoc metrics (if (= k :slug) v focus) new-metric)]
    (om/set-state! owner :growth-metrics new-metrics)))

(defcomponent topic-growth [{:keys [section section-data currency editable? foce-data-editing? editing-cb] :as data} owner options]

  (init-state [_]
    (get-state owner data true))

  (will-update [_ next-props _]
    ;; this means the section data has changed from the API or at a upper lever of this component
    (when-not (= next-props data)
      (om/set-state! owner (get-state owner next-props false))))

  (did-update [_ prev-props prev-state]
    (when (and (not (:foce-data-editing? prev-props))
               (:foce-data-editing? data))
      (add-popover-with-om-component growth-popover
        {:data (merge data {:initial-focus (om/get-state owner :focus)
                            :new-metric? (om/get-state owner :new-metric?)
                            :hide-popover-cb #(editing-cb false)
                            :growth-data (om/get-state owner :growth-data)
                            :growth-metrics (om/get-state owner :growth-metrics)
                            :metric-slugs (om/get-state owner :growth-metric-slugs)
                            :growth-data-on-change-cb (partial data-editing-on-change owner)
                            :growth-metadata-on-change-cb (partial metadata-editing-on-change owner (om/get-state owner :focus))
                            :growth-data-editing-toggle (partial data-editing-toggle owner editing-cb)
                            :growth-switch-focus-cb (partial switch-focus owner)
                            :growth-archive-metric-cb (partial show-archive-confirm-popover owner editing-cb (om/get-state owner :focus))})
         :width 390
         :height 450
         :container-id "growth-edit"}))
    (when (and (:foce-data-editing? prev-props)
               (not (:foce-data-editing? data)))
      (hide-popover nil "growth-edit")))

  (render-state [_ {:keys [focus growth-metrics growth-data growth-metric-slugs metric-slug new-metric?]}]

    (let [section-name (utils/camel-case-str (name section))
          no-data (utils/no-growth-data? growth-data)
          focus-metric-data (filter-growth-data focus growth-data)
          focus-metric-info (get growth-metrics focus)
          show-placeholder-chart? (and foce-data-editing?
                                       (< (count focus-metric-data) 2))
          subsection-data {:metric-data (if show-placeholder-chart?
                                          (growth-utils/fake-chart-placeholder-data focus-metric-info)
                                          focus-metric-data)
                           :metric-info focus-metric-info
                           :fake-chart show-placeholder-chart?
                           :editing foce-data-editing?
                           :focus focus
                           :currency currency
                           :read-only true
                           :total-metrics (count growth-metrics)}]
      (dom/div {:id "section-growth"
                :class (utils/class-set {:section-container true
                                         :editing foce-data-editing?})
                :key (name section)}

        ; Chart
        (when-not no-data
          (dom/div {:class "composed-section growth group"}
            ; growth data chart
            (dom/div {:class (utils/class-set {:composed-section-body true})}
              ;; growth metric sparklines
              (growth-sparklines {:growth-data growth-data
                                  :growth-metrics growth-metrics
                                  :growth-metric-slugs growth-metric-slugs
                                  :archive-cb (partial show-archive-confirm-popover owner editing-cb)}))))))))