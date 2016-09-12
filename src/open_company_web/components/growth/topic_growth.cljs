(ns open-company-web.components.growth.topic-growth
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.growth.growth-metric :refer (growth-metric)]
            [open-company-web.components.growth.growth-edit :refer (growth-edit)]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.caches :refer (company-cache)]
            [cuerdas.core :as s]))

(def focus-cache-key :last-selected-metric)

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
  (.preventDefault e)
  (let [data (om/get-props owner)
        focus  (.. e -target -dataset -tab)
        section-data (:section-data data)
        metrics (metrics-map (:metrics section-data))]
    (switch-focus owner focus options))
  (.stopPropagation e))

(defn- filter-growth-data [focus growth-data]
  (vec (filter #(= (:slug %) focus) (vals growth-data))))

(defn- data-editing-toggle [owner editing-cb editing]
  (om/set-state! owner :data-editing? editing)
  (editing-cb editing))

(defn- new-metric [owner editing-cb]
  (om/set-state! owner :new-metric? true)
  (data-editing-toggle owner editing-cb true))

(defn- render-pillboxes [owner editable? editing-cb options]

  (let [data (om/get-props owner)
        growth-metric-slugs (om/get-state owner :growth-metric-slugs)
        growth-metrics (om/get-state owner :growth-metrics)
        focus (om/get-state owner :focus)
        data-editing? (om/get-state owner :data-editing?)]

    (dom/div {:class "pillbox-container growth"}

      (when focus
        (for [metric-slug growth-metric-slugs]
          (let [metric (get growth-metrics metric-slug)
                mname (:name metric)
                metric-classes (utils/class-set {:pillbox true
                                                 metric-slug true
                                                 :active (= focus metric-slug)})]
            (dom/label {:class metric-classes
                        :title (:description metric)
                        :data-tab metric-slug
                        :on-click (partial pillbox-click owner options)} mname))))
      
      (when editable?
        ;; new metric
        (dom/label {:class (utils/class-set {:pillbox true
                                             :new true})
                    :title "Add a new metric"
                    :data-tab growth-utils/new-metric-slug-placeholder
                    :on-click (fn [e]
                                (.stopPropagation e)
                                (new-metric owner editing-cb))} "+ New")))))

(defn- get-state [owner data & [initial]]
  (let [section-data (:section-data data)
        all-metrics (:metrics section-data)
        metrics (metrics-map all-metrics)
        first-metric (:slug (first (:metrics section-data)))
        last-focus (utils/company-cache-key focus-cache-key)
        focus (if initial
                (or (:selected-metric data) last-focus first-metric)
                (om/get-state owner :focus))
        growth-data (growth-utils/growth-data-map (:data section-data))
        metric-slugs (metrics-order all-metrics)
        new-metric? (not focus)]
    {:growth-data growth-data
     :growth-metrics metrics
     :growth-metric-slugs metric-slugs
     :focus focus
     :new-metric? new-metric?
     :data-editing? (:initial-editing? data)}))

(defcomponent topic-growth [{:keys [section section-data currency editable? initial-editing? editing-cb] :as data} owner options]

  (init-state [_]
    (get-state owner data true))

  (will-update [_ next-props _]
    ;; this means the section data has changed from the API or at a upper lever of this component
    (when-not (= next-props data)
      (om/set-state! owner (get-state owner next-props true))))

  (render-state [_ {:keys [focus growth-metrics growth-data growth-metric-slugs metric-slug new-metric? data-editing?]}]
    (let [section-name (utils/camel-case-str (name section))
          no-data (utils/no-growth-data? growth-data)
          focus-metric-data (filter-growth-data focus growth-data)
          focus-metric-info (get growth-metrics focus)
          subsection-data {:metric-data focus-metric-data
                           :metric-info focus-metric-info
                           :currency currency
                           :read-only true
                           :total-metrics (count growth-metrics)}]

      (dom/div {:id "section-growth"
                :class (utils/class-set {:section-container true
                                         :editing data-editing?})
                :key (name section)}

        (if data-editing?

          (om/build growth-edit {
                         :initial-focus focus
                         :new-metric? new-metric? 
                         :growth-data growth-data
                         :metrics growth-metrics
                         :metric-slugs growth-metric-slugs
                         :editing-cb (partial data-editing-toggle owner editing-cb)
                         :show-first-edit-tip false ;show-first-edit-tip
                         ;:first-edit-tip-cb #(focus-headline owner)
                        }
                        {:opts {:currency currency} :key growth-data})

          (when-not no-data
            (dom/div {:class "composed-section growth group"}
              ; growth data chart
              (dom/div {:class (utils/class-set {:composed-section-body true})}
                ;; growth metric currently shown
                (when (and focus (seq (:metric-data subsection-data)))
                  (om/build growth-metric subsection-data {:opts options}))
                (when (> (count growth-metric-slugs) 1)
                  (render-pillboxes owner editable? editing-cb options))
                (when editable?
                  (dom/button {:class "btn-reset chart-pencil-button"
                               :title "Edit chart data"
                               :type "button"
                               :data-toggle "tooltip"
                               :data-placement "left"
                               :on-click #(do (om/set-state! owner :data-editing? true)
                                              (editing-cb true))}
                    (dom/i {:class "fa fa-pencil editable-pen"})))))))))))