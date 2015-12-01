(ns open-company-web.components.growth.growth
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.revisions-navigator :refer [revisions-navigator]]
            [open-company-web.api :as api]
            [open-company-web.components.editable-title :refer [editable-title]]
            [open-company-web.components.growth.growth-metric :refer [growth-metric]]
            [open-company-web.components.utility-components :refer [add-metric]]
            [open-company-web.components.section-footer :refer (section-footer)]))

(defn subsection-click [e owner]
  (.preventDefault e)
  (let [tab  (.. e -target -dataset -tab)]
    (om/update-state! owner :focus (fn [] tab))))

(defn start-editing-cb [owner data]
  (om/set-state! owner :editing true))

(defn cancel-cb [owner data]
  (let [section-data (:section-data data)
        notes-data (:notes section-data)]
    (om/set-state! owner :title (:title section-data))
    (om/set-state! owner :growth-data (:data section-data))
    (om/set-state! owner :growth-metrics (:metrics section-data))
    (om/set-state! owner :notes-body (:body notes-data))
    (om/set-state! owner :editing false)))

(defn has-changes [owner data]
  (let [section-data (:section-data data)
        notes-data (:notes-data section-data)]
    (or (not= (:title section-data) (om/get-state owner :title))
        (not= (:data section-data) (om/get-state owner :section-data))
        (not= (:metrics section-data) (om/get-state owner :growth-metrics))
        (not= (:body notes-data) (om/get-state owner :notes-body)))))

(defn cancel-if-needed-cb [owner data]
  (when (not (has-changes owner data))
    (cancel-cb owner data)))

(defn change-cb [owner k v & [c]]
  (when c
    ; the body-counter is needed to avoid that a fast editing
    ; could replace a new updated html in the contenteditable field
    ; it is only an incremental prop that discard old html when it comes in
    (om/set-state! owner :body-counter c))
  (om/set-state! owner k v))

(defn save-cb [owner data]
  (when (or (has-changes owner data) ; when the section already exists
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
    (om/set-state! owner :editing false)
    (om/set-state! owner :oc-editing false)))

(defn get-state [owner data]
  (let [section-data (:section-data data)
        notes-data (:notes section-data)]
    {:focus (or (om/get-state owner :focus) "cash")
     :editing (or (not (not (:oc-editing section-data)))
                  (om/get-state owner :editing))
     :growth-data (:data section-data)
     :growth-metrics (:metrics section-data)
     :title (:title section-data)
     :notes-body (:body (:notes section-data))
     :as-of (:updated-at section-data)}))

(defcomponent growth [data owner]

  (init-state [_]
    (let [section-data (:section-data data)
          growth-metrics (:metrics section-data)]
      {:focus (:slug (first growth-metrics))
       :editing (not (not (:oc-editing section-data)))
       :growth-data (:data section-data)
       :growth-metrics (:metrics section-data)
       :title (:title section-data)
       :notes-body (:body (:notes section-data))}))

  (will-receive-props [_ next-props]
    ; this means the section datas have changed from the API or at a upper lever of this component
    (when-not (= next-props (om/get-props owner))
      (om/set-state! owner (get-state owner next-props))))

  (render [_]
    (let [showing-revision (om/get-state owner :as-of)
          focus (om/get-state owner :focus)
          slug (:slug @router/path)
          section-data (:section-data data)
          growth-metrics (:metrics section-data)
          growth-data (:data section-data)
          notes-data (:notes section-data)
          read-only (:read-only data)
          focus-metric-data (filter #(= (:slug %) focus) growth-data)
          focus-metric-info (first (filter #(= (:slug %) focus) growth-metrics))
          subsection-data {:metric-data focus-metric-data
                           :metric-info focus-metric-info
                           :read-only read-only
                           :total-metrics (count growth-metrics)}
          editing (om/get-state owner :editing)
          cancel-fn #(cancel-cb owner data)
          save-fn #(save-cb owner data)
          notes-body-change-fn (partial change-cb owner :notes-body)
          title-change-fn (partial change-cb owner :title)
          cancel-if-needed-fn #(cancel-if-needed-cb owner data)
          start-editing-fn #(start-editing-cb owner data)]
      (dom/div {:class "section-container" :id "section-growth"}
        (dom/div {:class "composed-section growth"}
          (om/build editable-title {:read-only read-only
                                    :editing editing
                                    :title (om/get-state owner :title)
                                    :section (:section data)
                                    :start-editing-cb start-editing-fn
                                    :change-cb title-change-fn
                                    :cancel-cb cancel-fn
                                    :cancel-if-needed-cb cancel-if-needed-fn
                                    :save-cb save-fn})
          (dom/div {:class "link-bar"}
            (when (> (count growth-metrics) 1)
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
            (om/build growth-metric subsection-data)
            (om/build update-footer {:updated-at (:updated-at section-data)
                                     :author (:author section-data)
                                     :section :growth
                                     :editing editing
                                     :notes false})
            (when (or (not (empty? (:body notes-data)))
                      (not read-only))
              (om/build rich-editor {:editing editing
                                     :section :growth
                                     :body-counter (om/get-state owner :body-counter)
                                     :read-only (:read-only data)
                                     :body (om/get-state owner :notes-body)
                                     :placeholder (str (utils/camel-case-str (name (:section data))) " notes here...")
                                     :start-editing-cb start-editing-fn
                                     :change-cb notes-body-change-fn
                                     :cancel-cb cancel-fn
                                     :cancel-if-needed-cb cancel-if-needed-fn
                                     :save-cb save-fn}))
            (when (not (empty? (:author notes-data)))
              (om/build update-footer {:author (:author notes-data)
                                       :updated-at (:updated-at notes-data)
                                       :section :growth
                                       :editing editing
                                       :notes true}))
            (if editing
              (om/build section-footer {:edting editing
                                        :cancel-cb cancel-fn
                                        :save-cb save-fn})
              (om/build revisions-navigator data))))))))


