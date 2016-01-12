(ns open-company-web.components.growth.growth
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.ui.rich-editor :refer (rich-editor)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.revisions-navigator :refer (revisions-navigator)]
            [open-company-web.api :as api]
            [open-company-web.components.ui.editable-title :refer (editable-title)]
            [open-company-web.components.growth.growth-metric :refer (growth-metric)]
            [open-company-web.components.ui.utility-components :refer (add-metric)]
            [open-company-web.components.section-footer :refer (section-footer)]
            [open-company-web.lib.section-utils :as section-utils]
            [open-company-web.components.growth.growth-edit :refer (growth-edit)]
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
  (map #(:slug %) metrics-coll))

(defn map-metric-data [metric-data]
  (apply merge (map #(hash-map (str (:period %) (:slug %)) %) metric-data)))

(defn get-state [owner data & [initial]]
  (let [section-data (:section-data data)
        notes-data (:notes section-data)
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
     :metadata-editing (if initial
                         (not (not (:oc-editing section-data)))
                         (om/get-state owner :metadata-editing))
     :title-editing (if initial
                      (not (not (:oc-editing section-data)))
                      (om/get-state owner :title-editing))
     :notes-editing (if initial
                      (not (not (:oc-editing section-data)))
                      (om/get-state owner :notes-editing))
     :data-editing (if initial
                     (not (not (:oc-editing section-data)))
                     (om/get-state owner :data-editing))
     :new-metric (if initial
                   (not (not (:oc-editing section-data)))
                   (om/get-state owner :new-metric))
     :growth-data growth-data
     :growth-metrics metrics
     :growth-metric-slugs metric-slugs
     :oc-editing (:oc-editing section-data)
     :title (:title section-data)
     :notes-body (:body notes-data)
     :as-of (:updated-at section-data)}))

(defn subsection-click [e owner data]
  (.preventDefault e)
  (let [focus  (.. e -target -dataset -tab)
        section-data (:section-data data)
        metrics (metrics-map (:metrics section-data))]
    (switch-focus owner focus)))

(defn start-title-editing-cb [owner data]
  (om/set-state! owner :title-editing true))

(defn start-notes-editing-cb [owner data]
  (om/set-state! owner :notes-editing true))

(defn start-data-editing-cb [owner data]
  (om/set-state! owner :data-editing true))

(defn cancel-cb [owner data]
  (if (om/get-state owner :oc-editing)
    ; remove an unsaved section
    (section-utils/remove-section (:section data))
    ; revert the edited data to the initial values
    (let [state (get-state owner data)]
      ; reset the finances fields to the initial values
      (om/set-state! owner :title (:title state))
      (om/set-state! owner :growth-data (:growth-data state))
      (om/set-state! owner :growth-metrics (:growth-metrics state))
      (om/set-state! owner :growth-metric-slugs (:growth-metric-slugs state))
      (om/set-state! owner :notes-body (:notes-body state))
      (when (om/get-state owner :new-metric)
        (let [section-data (:section-data data)
              first-metric (:slug (first (:metrics section-data)))
              focus (or (utils/company-cache-key focus-cache-key) first-metric)]
          (switch-focus owner focus)))
      ; and the editing state flags
      (om/set-state! owner :new-metric false)
      (om/set-state! owner :title-editing false)
      (om/set-state! owner :notes-editing false)
      (om/set-state! owner :data-editing false))))

(defn has-data-changes [owner data]
  (let [section-data (:section-data data)]
    (or (not= (:data section-data) (om/get-state owner :section-data))
        (not= (metrics-map (:metrics section-data)) (om/get-state owner :growth-metrics)))))

(defn has-changes [owner data]
  (let [section-data (:section-data data)
        notes-data (:notes-data section-data)]
    (or (not= (:title section-data) (om/get-state owner :title))
        (not= (:body notes-data) (om/get-state owner :notes-body)))))

(defn cancel-if-needed-cb [owner data]
  (when (and (not (has-changes owner data))
             (not (has-data-changes owner data)))
    (cancel-cb owner data)))

(defn change-cb [owner k v & [c]]
  (when c
    ; the body-counter is needed to avoid that a fast editing
    ; could replace a new updated html in the contenteditable field
    ; it is only an incremental prop that discard old html when it comes in
    (om/set-state! owner :body-counter c))
  (om/set-state! owner k v))

(defn get-growth-value [v]
  (if (s/blank? v)
    ""
    (if (js/isNaN v)
      0
      v)))

(defn fix-row [row]
  (let [fixed-value (get-growth-value (:value row))
        with-fixed-value (if (s/blank? fixed-value)
                           (dissoc row :value)
                           (assoc row :value fixed-value))
        fixed-target (get-growth-value (:target with-fixed-value))
        with-fixed-target (if (s/blank? fixed-target)
                           (dissoc with-fixed-value :target)
                           (assoc with-fixed-value :target fixed-target))]
    with-fixed-target))

(defn change-growth-cb [owner row]
  (let [fixed-row (fix-row row)
        period (:period fixed-row)
        slug (:slug fixed-row)
        growth-data (om/get-state owner :growth-data)
        fixed-data (if (and (not (:target fixed-row))
                            (not (:value fixed-row)))
                     (dissoc growth-data (str period slug))
                     (assoc growth-data (str period slug) fixed-row))]
    (om/set-state! owner :growth-data fixed-data)))

(defn change-growth-metric-cb [owner data slug properties-map]
  (let [change-slug (and (contains? properties-map :slug)
                         (not= (:slug properties-map) slug))
        metrics (or (om/get-state owner :growth-metrics) {})
        metric (or (get metrics slug) {})
        new-metric (merge metric properties-map)
        ; the slug has changed, change the key of the map too
        new-metrics (if change-slug
                      (-> metrics
                          (dissoc slug)
                          (assoc (:slug properties-map) new-metric))
                      (assoc metrics slug new-metric))
        focus (om/get-state owner :focus)]
    (when change-slug
      (let [slugs (om/get-state owner :growth-metric-slugs)
            remove-slug (vec (remove #(= % slug) slugs))
            add-slug (conj remove-slug (:slug properties-map))]
        ; switch the focus to the new metric-slug
        (om/set-state! owner :focus (:slug properties-map))
        ; save the new metrics list
        (om/set-state! owner :growth-metric-slugs add-slug)))
    (om/set-state! owner :growth-metrics new-metrics)))

(defn clean-data [data]
  ; a data entry is good if we have the period and one other value: cash, costs or revenue
  (if (and (not (nil? (:period data)))
           (not (nil? (:slug data)))
           (or (not (nil? (:target data)))
               (not (nil? (:value data)))))
    (dissoc data :new)
    nil))

(defn clean-growth-data [growth-data]
  (filter #(not (nil? %))
          (vec (map (fn [[_ v]] (clean-data v)) growth-data))))

(defn save-cb [owner data]
  (when (or (has-changes owner data) ; when the section already exists
            (has-data-changes owner data)
            (om/get-state owner :oc-editing)) ; when the section is new
    ; save focus
    (utils/company-cache-key focus-cache-key (om/get-state owner :focus))
    (let [title (om/get-state owner :title)
          notes-body (om/get-state owner :notes-body)
          growth-data (om/get-state owner :growth-data)
          fixed-growth-data (clean-growth-data growth-data)
          growth-metrics (om/get-state owner :growth-metrics)
          growth-metric-slugs (om/get-state owner :growth-metric-slugs)
          metrics-vec (vec (map #(get growth-metrics %) growth-metric-slugs))
          section-data {:title title
                        :data fixed-growth-data
                        :metrics metrics-vec
                        :notes {:body notes-body}}]
      (if (om/get-state owner :oc-editing)
        ; save a new section
        (let [slug (keyword (:slug @router/path))
              company-data (slug @dispatcher/app-state)]
          (api/patch-sections (:sections company-data) section-data (:section data)))
        ; save an existing section
        (api/save-or-create-section (merge section-data {:links (:links (:section-data data))
                                                         :section (:section data)}))))
    (om/set-state! owner :title-editing false)
    (om/set-state! owner :notes-editing false)
    (om/set-state! owner :data-editing false)
    (om/set-state! owner :oc-editing false)))

(defn new-metric [owner]
  (om/set-state! owner :new-metric true)
  (om/set-state! owner :focus growth-utils/new-metric-slug-placeholder)
  (om/set-state! owner :data-editing true))

(defn filter-growth-data [focus growth-data]
  (vec (filter #(= (:slug %) focus) (vals growth-data))))

(defn reset-metrics-cb [owner data]
  (let [state (get-state owner data)]
    (om/set-state! owner :growth-metrics (:growth-metrics state))
    (om/set-state! owner :growth-metric-slugs (:growth-metric-slugs state))))

(defn delete-metric-cb [owner data metric-slug]
  (let [all-metrics (vals (om/get-state owner :growth-metrics))
        new-metrics (vec (filter #(not= (:slug %) metric-slug) all-metrics))
        new-metrics-map (map-metric-data new-metrics)
        all-data (vals (om/get-state owner :growth-data))
        filtered-data (vec (filter #(not= (:slug %) metric-slug) all-data))
        new-data (metrics-map filtered-data)
        metrics-order (metrics-order new-metrics)]
    (om/set-state! owner :growth-metrics new-metrics-map)
    (om/set-state! owner :growth-data new-data)
    (om/set-state! owner :growth-metric-slugs metrics-order)
    (save-cb owner data)))

(defn metadata-edit-cb [owner editing]
  (om/set-state! owner :metadata-editing editing))

(defcomponent growth [data owner]

  (init-state [_]
    (get-state owner data true))

  (will-receive-props [_ next-props]
    ; this means the section datas have changed from the API or at a upper lever of this component
    (when-not (= next-props (om/get-props owner))
      (om/set-state! owner (get-state owner next-props))))

  (render [_]
    (let [focus (om/get-state owner :focus)
          section-data (:section-data data)
          section (:section data)
          section-name (utils/camel-case-str (name section))
          growth-metrics (om/get-state owner :growth-metrics)
          growth-data (om/get-state owner :growth-data)
          notes-data (:notes section-data)
          read-only-section (:read-only section-data)
          read-only (or (:read-only data) read-only-section)
          focus-metric-data (filter-growth-data focus growth-data)
          focus-metric-info (get growth-metrics focus)
          title-editing (om/get-state owner :title-editing)
          notes-editing (om/get-state owner :notes-editing)
          data-editing (om/get-state owner :data-editing)
          metadata-editing (om/get-state owner :metadata-editing)
          cancel-fn #(cancel-cb owner data)
          save-fn #(save-cb owner data)
          notes-body-change-fn (partial change-cb owner :notes-body)
          title-change-fn (partial change-cb owner :title)
          cancel-if-needed-fn #(cancel-if-needed-cb owner data)
          start-title-editing-fn #(when-not read-only
                                    (start-title-editing-cb owner data))
          start-notes-editing-fn #(when-not read-only
                                    (start-notes-editing-cb owner data))
          start-data-editing-fn #(when-not read-only
                                    (start-data-editing-cb owner data))
          slugs (om/get-state owner :growth-metric-slugs)
          subsection-data {:metric-data focus-metric-data
                           :metric-info focus-metric-info
                           :read-only read-only
                           :start-editing-cb start-data-editing-fn
                           :total-metrics (count growth-metrics)}]
      (dom/div {:class "section-container" :id "section-growth" :key (name section)}
        (dom/div {:class "composed-section growth"}
          (om/build editable-title {:read-only read-only
                                    :editing title-editing
                                    :title (om/get-state owner :title)
                                    :placeholder (or (:title-placeholder section-data)
                                                     section-name)
                                    :section (:section data)
                                    :start-editing-cb start-title-editing-fn
                                    :change-cb title-change-fn
                                    :cancel-cb cancel-fn
                                    :cancel-if-needed-cb cancel-if-needed-fn
                                    :save-cb save-fn})
          (when (not data-editing)
            (dom/div {:class "link-bar"}
              (when focus
                (for [metric-slug slugs]
                  (let [metric (get growth-metrics metric-slug)
                        mname (:name metric)
                        metric-classes (utils/class-set {:composed-section-link true
                                                         metric-slug true
                                                         :active (= focus metric-slug)})]
                    (dom/a {:class metric-classes
                            :title (:description metric)
                            :data-tab metric-slug
                            :on-click #(subsection-click % owner data)
                            } mname))))
              (when-not read-only
                (om/build add-metric {:click-callback #(new-metric owner)
                                      :metrics-count (count growth-metrics)}))))
          (if data-editing
            ; editing growth metric's data and metric's metadata
            (om/build growth-edit {:growth-data focus-metric-data
                                   :metric-slug focus
                                   :metadata-edit-cb (partial metadata-edit-cb owner)
                                   :new-metric (om/get-state owner :new-metric)
                                   :metrics growth-metrics
                                   :metric-count (count focus-metric-data)
                                   :change-growth-cb (partial change-growth-cb owner)
                                   :cancel-cb cancel-fn
                                   :delete-metric-cb (partial delete-metric-cb owner data)
                                   :reset-metrics-cb #(reset-metrics-cb owner data)
                                   :change-growth-metric-cb (partial change-growth-metric-cb owner data)
                                   :new-growth-section (om/get-state owner :oc-editing)})
            ; growth data chart
            (dom/div {:class (utils/class-set {:composed-section-body true
                                               :editable (not read-only)})}
              ;; growth metric currently shown
              (when (and focus (not (empty? (:metric-data subsection-data))))
                (om/build growth-metric subsection-data))))
          (om/build update-footer {:updated-at (:updated-at section-data)
                                   :author (:author section-data)
                                   :section :growth
                                   :editing (or title-editing notes-editing data-editing)
                                   :notes false})
          (when-not (or (and (empty? (:body notes-data))  ; there aren't notes
                             read-only)                   ; and it's read only
                        metadata-editing)                 ; or is editing the metadata
            (om/build rich-editor {:editing notes-editing
                                   :section :growth
                                   :body-counter (om/get-state owner :body-counter)
                                   :read-only read-only
                                   :body (om/get-state owner :notes-body)
                                   :placeholder (or (:body-placeholder section-data)
                                                    (str section-name " notes here..."))
                                   :start-editing-cb start-notes-editing-fn
                                   :change-cb notes-body-change-fn
                                   :cancel-cb cancel-fn
                                   :cancel-if-needed-cb cancel-if-needed-fn
                                   :save-cb save-fn}))
          (when (not (empty? (:author notes-data)))
            (om/build update-footer {:author (:author notes-data)
                                     :updated-at (:updated-at notes-data)
                                     :section :growth
                                     :editing (or title-editing notes-editing data-editing)
                                     :notes true}))
          (if (and (or title-editing notes-editing data-editing)
                   (not metadata-editing))
            (om/build section-footer {:edting (or title-editing notes-editing data-editing)
                                      :cancel-cb cancel-fn
                                      :is-new-section (om/get-state owner :oc-editing)
                                      :save-cb save-fn})
            (om/build revisions-navigator data)))))))