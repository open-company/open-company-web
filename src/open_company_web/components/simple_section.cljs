(ns open-company-web.components.simple-section
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.components.editable-title :refer (editable-title)]
            [open-company-web.api :refer (save-or-create-section)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.revisions-navigator :refer (revisions-navigator)]
            [cljs.core.async :refer (chan <!)]
            [open-company-web.api :as api]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.components.section-footer :refer (section-footer)]
            [open-company-web.components.update-footer :refer (update-footer)]))

(defn cancel-cb [owner data]
  (om/set-state! owner :title (:title (:section-data data)))
  (om/set-state! owner :body (:body (:section-data data)))
  (om/set-state! owner :editing false))

(defn has-changes [owner data]
  (or (not= (:title (:section-data data)) (om/get-state owner :title))
      (not= (:body (:section-data data)) (om/get-state owner :body))))

(defn cancel-if-needed-cb [owner data]
  (when (not (has-changes owner data))
    (cancel-cb owner data)))

(defn save-cb [owner data]
  (when (or (has-changes owner data) ; when the section already exists
            (om/get-state owner :oc-editing)) ; when the section is new
    (let [title (om/get-state owner :title)
          body (om/get-state owner :body)
          section-data {:title title
                        :body body}]
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

(defn start-editing-cb [owner data]
  (om/set-state! owner :editing true))

(defn change-cb [owner k v & [c]]
  (when c
    ; the body-counter is needed to avoid that a fast editing
    ; could replace a new updated html in the contenteditable field
    ; it is only an incremental prop that discard old html when it comes in
    (om/set-state! owner :body-counter c))
  (om/set-state! owner k v))

(defcomponent simple-section [data owner]

  (init-state [_]
    (let [new-added-section (:oc-editing (:section-data data))]
      {:editing (not (not new-added-section))
       :oc-editing new-added-section
       :title (:title (:section-data data))
       :body (:body (:section-data data))}))

  (will-receive-props [_ next-props]
    ; this means the title or the body has changed from the API or at a upper lever of this component
    (when-not (= next-props (om/get-props owner))
      (om/set-state! owner :title (:title (:section-data next-props)))
      (om/set-state! owner :body (:body (:section-data next-props)))))

  (render [_]
    (let [section (:section data)
          editing (om/get-state owner :editing)
          start-editing-cb #(start-editing-cb owner data)
          title-change-cb (partial change-cb owner :title)
          body-change-cb (partial change-cb owner :body)
          cancel-cb #(cancel-cb owner data)
          cancel-if-needed-cb #(cancel-if-needed-cb owner data)
          save-cb #(save-cb owner data)]
      (if (:loading data)
        (dom/h4 {} "Loading data...")
        (dom/div {:class "simple-section section-container"
                  :id (str "section-" (name section))}

          (om/build editable-title {:editing editing
                                    :read-only (:read-only data)
                                    :title (om/get-state owner :title)
                                    :start-editing-cb start-editing-cb
                                    :change-cb title-change-cb
                                    :cancel-cb cancel-cb
                                    :cancel-if-needed-cb cancel-if-needed-cb
                                    :save-cb save-cb})

          (dom/div {:class "simple-section-body"}
            (om/build rich-editor {:editing editing
                                   :section section
                                   :body-counter (om/get-state owner :body-counter)
                                   :read-only (:read-only data)
                                   :body (om/get-state owner :body)
                                   :placeholder (str (utils/camel-case-str (name (:section data))) " notes here...")
                                   :start-editing-cb start-editing-cb
                                   :change-cb body-change-cb
                                   :cancel-cb cancel-cb
                                   :cancel-if-needed-cb cancel-if-needed-cb
                                   :save-cb save-cb}))

          (om/build update-footer {:author (:author (:section-data data))
                                   :updated-at (:updated-at (:section-data data))
                                   :section section
                                   :editing editing
                                   :notes false})
          (dom/div {:class "simple-section-footer"}
            (if editing
              (om/build section-footer {:edting editing
                                        :cancel-cb cancel-cb
                                        :save-cb save-cb})
              (om/build revisions-navigator data))))))))