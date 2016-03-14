(ns open-company-web.components.growth.topic-growth
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.growth.growth-metric :refer (growth-metric)]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.caches :refer (company-cache)]
            [cuerdas.core :as s]))

(def focus-cache-key :last-selected-metric)

(defn switch-focus [owner focus]
  (utils/company-cache-key focus-cache-key focus)
  (om/set-state! owner :focus focus))

(defn metrics-map [metrics-coll]
  (apply merge (map #(hash-map (:slug %) %) (reverse metrics-coll))))

(defn metrics-order [metrics-coll]
  (map :slug metrics-coll))

(defn map-metric-data [metric-data]
  (apply merge (map #(hash-map (str (:period %) (:slug %)) %) metric-data)))

(defn get-state [owner data & [initial]]
  (let [section-data (:section-data data)
        all-metrics (:metrics section-data)
        metrics (metrics-map all-metrics)
        first-metric (:slug (first (:metrics section-data)))
        last-focus (utils/company-cache-key focus-cache-key)
        focus (if (and initial (:oc-editing data))
                growth-utils/new-metric-slug-placeholder
                (if initial
                  (or last-focus first-metric)
                  (om/get-state owner :focus)))
        growth-data (map-metric-data (:data section-data))
        metric-slugs (metrics-order all-metrics)]
    {:focus focus
     :growth-data growth-data
     :growth-metrics metrics
     :growth-metric-slugs metric-slugs}))

(defn subsection-click [e owner data]
  (.preventDefault e)
  (let [focus  (.. e -target -dataset -tab)
        section-data (:section-data data)
        metrics (metrics-map (:metrics section-data))]
    (switch-focus owner focus))
  (.stopPropagation e))

(defn filter-growth-data [focus growth-data]
  (vec (filter #(= (:slug %) focus) (vals growth-data))))

(defcomponent topic-growth [{:keys [section section-data currency] :as data} owner options]

  (init-state [_]
    (get-state owner data true))

  (will-receive-props [_ next-props]
    ; this means the section datas have changed from the API or at a upper lever of this component
    (when-not (= next-props (om/get-props owner))
      (om/set-state! owner (get-state owner next-props))))

  (render-state [_ {:keys [focus growth-metrics growth-data growth-metric-slugs]}]
    (let [section-name (utils/camel-case-str (name section))
          focus-metric-data (filter-growth-data focus growth-data)
          focus-metric-info (get growth-metrics focus)
          subsection-data {:metric-data focus-metric-data
                           :metric-info focus-metric-info
                           :read-only true
                           :total-metrics (count growth-metrics)}]
      (dom/div {:class "section-container" :id "section-growth" :key (name section)}
        (dom/div {:class "composed-section growth"}
          (dom/div {:class "link-bar"}
            (when focus
              (for [metric-slug growth-metric-slugs]
                (let [metric (get growth-metrics metric-slug)
                      mname (:name metric)
                      metric-classes (utils/class-set {:composed-section-link true
                                                       metric-slug true
                                                       :oc-header true
                                                       :active (= focus metric-slug)})]
                  (dom/a {:class metric-classes
                          :title (:description metric)
                          :data-tab metric-slug
                          :on-click #(subsection-click % owner data)} mname)))))
          ; growth data chart
          (dom/div {:class (utils/class-set {:composed-section-body true})}
            ;; growth metric currently shown
            (when (and focus (seq (:metric-data subsection-data)))
              (om/build growth-metric subsection-data {:opts options}))))))))