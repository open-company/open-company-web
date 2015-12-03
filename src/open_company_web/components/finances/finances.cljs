(ns open-company-web.components.finances.finances
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.finances.cash :refer (cash)]
            [open-company-web.components.finances.cash-flow :refer (cash-flow)]
            [open-company-web.components.finances.costs :refer (costs)]
            [open-company-web.components.finances.runway :refer (runway)]
            [open-company-web.components.finances.finances-edit :refer (finances-edit)]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.revisions-navigator :refer (revisions-navigator)]
            [open-company-web.api :as api]
            [open-company-web.components.editable-title :refer (editable-title)]
            [open-company-web.components.utility-components :refer (editable-pen)]
            [open-company-web.components.section-footer :refer (section-footer)]))

(defn map-placeholder-data [data]
  (let [fixed-data (finances-utils/edit-placeholder-data data)]
    (apply merge (map #(hash-map (:period %) %) fixed-data))))

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
    (om/set-state! owner :finances-data (map-placeholder-data (:data section-data)))
    (om/set-state! owner :notes-body (:body notes-data))
    (om/set-state! owner :editing false)))

(defn has-changes [owner data]
  (let [section-data (:section-data data)
        notes-data (:notes-data section-data)]
    (or (not= (:title section-data) (om/get-state owner :title))
        (not= (map-placeholder-data (:data section-data)) (om/get-state owner :finances-data))
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

(defn change-finances-data-cb [owner row]
  (let [period (:period row)
        finances-data (om/get-state owner :finances-data)
        fixed-data (assoc finances-data period row)]
    (om/set-state! owner :finances-data fixed-data)))

(defn clean-data [data]
  (if (and (not (nil? (:cash data)))
           (not (nil? (:costs data)))
           (not (nil? (:revenue data)))
           (not (nil? (:period data))))
    (dissoc data :burn-rate :runway :avg-burn-rate :new :value)
    nil))

(defn clean-finances-data [finances-data]
  (filter #(not (nil? %))
          (vec (map (fn [[k v]] (clean-data v)) finances-data))))

(defn save-cb [owner data]
  (when (or (has-changes owner data) ; when the section already exists
            (om/get-state owner :oc-editing)) ; when the section is new
    (let [title (om/get-state owner :title)
          notes-body (om/get-state owner :notes-body)
          finances-data (om/get-state owner :finances-data)
          fixed-finances-data (clean-finances-data finances-data)
          section-data {:title title
                        :data fixed-finances-data
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
     :finances-data (map-placeholder-data (:data section-data))
     :title (:title section-data)
     :notes-body (:body (:notes section-data))
     :as-of (:updated-at section-data)}))

(defcomponent finances [data owner]

  (init-state [_]
    (let [section-data (:section-data data)
          notes-data (:notes section-data)
          finances-data (map-placeholder-data (:data section-data))]
      {:focus "cash"
       :editing (not (not (:oc-editing section-data)))
       :finances-data finances-data
       :title (:title section-data)
       :notes-body (:body (:notes section-data))
       :as-of (:updated-at section-data)}))

  (will-receive-props [_ next-props]
    ; this means the section datas have changed from the API or at a upper lever of this component
    (when-not (= next-props (om/get-props owner))
      (om/set-state! owner (get-state owner next-props))))

  (render [_]
    (let [showing-revision (om/get-state owner :as-of)
          focus (om/get-state owner :focus)
          classes "composed-section-link"
          section-data (:section-data data)
          notes-data (:notes section-data)
          cash-classes (str classes (when (= focus "cash") " active"))
          cash-flow-classes (str classes (when (= focus "cash-flow") " active"))
          revenue-classes (str classes (when (= focus "revenue") " active"))
          costs-classes (str classes (when (= focus "costs") " active"))
          runway-classes (str classes (when (= focus "runway") " active"))
          read-only (:read-only data)
          cursor @dispatcher/app-state
          finances-row-data (:data section-data)
          sum-revenues (apply + (map #(:revenue %) finances-row-data))
          first-title (if (pos? sum-revenues) "Cash flow" "Burn rate")
          needs-runway (some #(and (contains? % :runway) (neg? (:runway %))) finances-row-data)
          editing (om/get-state owner :editing)
          cancel-fn #(cancel-cb owner data)
          save-fn #(save-cb owner data)
          notes-body-change-fn (partial change-cb owner :notes-body)
          title-change-fn (partial change-cb owner :title)
          cancel-if-needed-fn #(cancel-if-needed-cb owner data)
          start-editing-fn #(start-editing-cb owner data)
          subsection-data {:section-data section-data
                           :read-only read-only
                           :currency (:currency data)}]
      (dom/div {:class "section-container" :id "section-finances"}
        (dom/div {:class "composed-section finances"}
          (om/build editable-title {:read-only read-only
                                    :editing editing
                                    :title (om/get-state owner :title)
                                    :section (:section data)
                                    :start-editing-cb start-editing-fn
                                    :change-cb title-change-fn
                                    :cancel-cb cancel-fn
                                    :cancel-if-needed-cb cancel-if-needed-fn
                                    :save-cb save-fn})
          (when (not editing)
            (dom/div {:class (utils/class-set {:link-bar true
                                               :editable (not read-only)})}
              (dom/a {:href "#"
                      :class cash-classes
                      :title "Cash"
                      :data-tab "cash"
                      :on-click #(subsection-click % owner)} "Cash")
              (dom/a {:href "#"
                      :class cash-flow-classes
                      :title first-title
                      :data-tab "cash-flow"
                      :on-click #(subsection-click % owner)} first-title)
              (when needs-runway
                (dom/a {:href "#"
                        :class runway-classes
                        :title "Runway"
                        :data-tab "runway"
                        :on-click #(subsection-click % owner)} "Runway"))
              (om/build editable-pen {:click-callback start-editing-fn})))
          (dom/div {:class (utils/class-set {:composed-section-body true})}
            (if editing
              (om/build finances-edit {:finances-data (om/get-state owner :finances-data)
                                       :change-finances-cb (partial change-finances-data-cb owner)})
              (case focus

                "cash"
                (om/build cash subsection-data)

                "cash-flow"
                (if (pos? sum-revenues)
                  (om/build cash-flow subsection-data)
                  (om/build costs subsection-data))

                "runway"
                (om/build runway subsection-data)))
            (om/build update-footer {:updated-at (:updated-at section-data)
                                     :author (:author section-data)
                                     :section :finances
                                     :editing editing
                                     :notes false})
            (when (or (not (empty? (:body notes-data)))
                      (not read-only))
              (om/build rich-editor {:editing editing
                                     :section :finances
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
                                       :section :finances
                                       :editing editing
                                       :notes true}))
            (if editing
              (om/build section-footer {:edting editing
                                        :cancel-cb cancel-fn
                                        :save-cb save-fn})
              (om/build revisions-navigator data))))))))