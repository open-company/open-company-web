(ns open-company-web.components.growth.growth
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.revisions-navigator :refer (revisions-navigator)]
            [open-company-web.api :as api]
            [open-company-web.components.editable-title :refer (editable-title)]
            [open-company-web.components.growth.growth-metric :refer (growth-metric)]
            [open-company-web.components.utility-components :refer (add-metric)]
            [open-company-web.components.section-footer :refer (section-footer)]
            [open-company-web.lib.section-utils :as section-utils]
            [open-company-web.components.growth.growth-edit :refer (growth-edit)]
            [open-company-web.components.growth.utils :as growth-utils]))

(defn subsection-click [e owner]
  (.preventDefault e)
  (let [tab  (.. e -target -dataset -tab)]
    (om/set-state! owner :focus tab)))

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
    (let [section-data (:section-data data)
          notes-data (:notes section-data)]
      ; reset the finances fields to the initial values
      (om/set-state! owner :title (:title section-data))
      (om/set-state! owner :growth-data (:data section-data))
      (om/set-state! owner :growth-metrics (:metrics section-data))
      (om/set-state! owner :notes-body (:body notes-data))
      ; and the editing state flags
      (om/set-state! owner :title-editing false)
      (om/set-state! owner :notes-editing false)
      (om/set-state! owner :data-editing false))))

(defn has-data-changes [owner data]
  (let [section-data (:section-data data)]
    (or (not= (:data section-data) (om/get-state owner :section-data))
        (not= (:metrics section-data) (om/get-state owner :growth-metrics)))))

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
  (if (js/isNaN v)
    0
    v))

(defn fix-row [row]
  (let [fixed-value (assoc row :value (get-growth-value (:value row)))
        fixed-target (assoc row :target (get-growth-value (:target row)))
        fixed-row (if (or (not (empty? (:value row)))
                          (not (empty? (:target row))))
                    (dissoc fixed-target :new)
                    (assoc fixed-target :new true))]
    fixed-target))

(defn change-growth-cb [owner row]
  (let [fixed-row (fix-row row)
        period (:period fixed-row)
        slug (:slug fixed-row)
        growth-data (om/get-state owner :growth-data)
        fixed-data (assoc growth-data (str period slug) fixed-row)]
    (om/set-state! owner :growth-data fixed-data)))

(defn save-cb [owner data]
  (when (or (has-changes owner data) ; when the section already exists
            (has-data-changes owner data)
            (om/get-state owner :oc-editing)) ; when the section is new
    (let [title (om/get-state owner :title)
          notes-body (om/get-state owner :notes-body)
          growth-data (om/get-state owner :growth-data)
          growth-metrics (om/get-state owner :growth-metrics)
          section-data {:title title
                        :data growth-data
                        :metrics growth-metrics
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

(defn get-metric-info [metrics focus]
  (first (filter #(= (:slug %) focus) metrics)))

(defn get-metric-data [all-data focus]
  (filter #(= (:slug %) focus) all-data))

(defn get-state [owner data]
  (let [section-data (:section-data data)
        notes-data (:notes section-data)
        metrics (:metrics section-data)
        focus (om/get-state owner :focus)
        current-metric (get-metric-info metrics focus)
        metric-data (get-metric-data (:data section-data) focus)
        interval (:interval current-metric)
        growth-data (growth-utils/map-placeholder-data metric-data focus interval)]
    {:focus (om/get-state owner :focus)
     :title-editing (or (not (not (:oc-editing section-data)))
                        (om/get-state owner :title-editing))
     :notes-editing (or (not (not (:oc-editing section-data)))
                        (om/get-state owner :notes-editing))
     :data-editing (or (not (not (:oc-editing section-data)))
                       (om/get-state owner :data-editing))
     :growth-data growth-data
     :growth-metrics metrics
     :oc-editing (:oc-editing section-data)
     :title (:title section-data)
     :notes-body (:body (:notes section-data))
     :as-of (:updated-at section-data)}))

(defcomponent growth [data owner]

  (init-state [_]
    (let [section-data (:section-data data)
          notes-data (:notes section-data)
          metrics (:metrics section-data)
          focus (:slug (first metrics))
          current-metric (get-metric-info metrics focus)
          metric-data (get-metric-data (:data section-data) focus)
          interval (:interval current-metric)
          growth-data (growth-utils/map-placeholder-data metric-data focus interval)]
      {:focus focus
       :title-editing (not (not (:oc-editing section-data)))
       :notes-editing (not (not (:oc-editing section-data)))
       :data-editing (not (not (:oc-editing section-data)))
       :growth-data growth-data
       :growth-metrics metrics
       :title (:title section-data)
       :notes-body (:body notes-data)}))

  (will-receive-props [_ next-props]
    ; this means the section datas have changed from the API or at a upper lever of this component
    (when-not (= next-props (om/get-props owner))
      (om/set-state! owner (get-state owner next-props))))

  (render [_]
    (let [showing-revision (om/get-state owner :as-of)
          focus (om/get-state owner :focus)
          slug (:slug @router/path)
          section-data (:section-data data)
          section (:section data)
          section-name (utils/camel-case-str (name section))
          growth-metrics (:metrics section-data)
          growth-data (:data section-data)
          notes-data (:notes section-data)
          read-only (:read-only data)
          focus-metric-data (filter #(= (:slug %) focus) growth-data)
          focus-metric-info (first (filter #(= (:slug %) focus) growth-metrics))
          subsection-data {:metric-data focus-metric-data
                           :metric-info focus-metric-info
                           :read-only read-only
                           :start-editing-cb #(start-data-editing-cb owner data)
                           :total-metrics (count growth-metrics)}
          title-editing (om/get-state owner :title-editing)
          notes-editing (om/get-state owner :notes-editing)
          data-editing (om/get-state owner :data-editing)
          cancel-fn #(cancel-cb owner data)
          save-fn #(save-cb owner data)
          notes-body-change-fn (partial change-cb owner :notes-body)
          title-change-fn (partial change-cb owner :title)
          cancel-if-needed-fn #(cancel-if-needed-cb owner data)
          start-title-editing-fn #(start-title-editing-cb owner data)
          start-notes-editing-fn #(start-notes-editing-cb owner data)]
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
          (if data-editing
            (om/build growth-edit {:growth-data (om/get-state owner :growth-data)
                                   :metric-slug focus
                                   :metrics (om/get-state owner :metrics)
                                   :change-growth-cb (partial change-growth-cb owner)})
            (dom/div {}
              (dom/div {:class "link-bar"}
                (when (and focus (> (count growth-metrics) 1))
                  (for [metric growth-metrics]
                    (let [mslug (:slug metric)
                          mname (:name metric)
                          metric-classes (utils/class-set {:composed-section-link true
                                                           mslug true
                                                           :active (= focus mslug)})]
                      (dom/a {:class metric-classes
                              :title mname
                              :data-tab mslug
                              :on-click #(subsection-click % owner)} mname))))
                (om/build add-metric {:click-callback nil :metrics-count (count growth-metrics)}))
              (dom/div {:class (utils/class-set {:composed-section-body true
                                                 :editable (not read-only)})}
                ;; growth metric currently shown
                (when focus
                  (om/build growth-metric subsection-data)))))
            (om/build update-footer {:updated-at (:updated-at section-data)
                                     :author (:author section-data)
                                     :section :growth
                                     :editing (or title-editing notes-editing data-editing)
                                     :notes false})
            (when (or (not (empty? (:body notes-data)))
                      (not read-only))
              (om/build rich-editor {:editing notes-editing
                                     :section :growth
                                     :body-counter (om/get-state owner :body-counter)
                                     :read-only (:read-only data)
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
            (if (or title-editing notes-editing data-editing)
              (om/build section-footer {:edting (or title-editing notes-editing data-editing)
                                        :cancel-cb cancel-fn
                                        :is-new-section (om/get-state owner :oc-editing)
                                        :save-cb save-fn})
              (om/build revisions-navigator data)))))))