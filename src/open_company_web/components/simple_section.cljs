(ns open-company-web.components.simple-section
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.components.editable-title :refer (editable-title)]
            [open-company-web.api :refer (save-or-create-section)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.revisions-navigator :refer (revisions-navigator)]
            [open-company-web.api :as api]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.components.section-footer :refer (section-footer)]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.lib.section-utils :as section-utils]))

(defn cancel-cb [owner data]
  (if (om/get-state owner :oc-editing)
    ; remove an unsaved section
    (section-utils/remove-section (:section data))
    ; cancel editing
    (do
      (om/set-state! owner :title (:title (:section-data data)))
      (om/set-state! owner :body (:body (:section-data data)))
      (om/set-state! owner :title-editing false)
      (om/set-state! owner :body-editing false))))

(defn has-changes [owner data]
  (or (not= (:title (:section-data data)) (om/get-state owner :title))
      (not= (:body (:section-data data)) (om/get-state owner :body))))

(defn cancel-if-needed-cb [owner data]
  (when (and (not (has-changes owner data))
             (not (om/get-state owner :oc-editing)))
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
    (om/set-state! owner :title-editing false)
    (om/set-state! owner :body-editing false)
    (om/set-state! owner :oc-editing false)))

(defn start-title-editing-cb [owner data]
  (om/set-state! owner :title-editing true))

(defn start-body-editing-cb [owner data]
  (om/set-state! owner :body-editing true))

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
      {:title-editing new-added-section
       :body-editing new-added-section
       :oc-editing new-added-section
       :title (:title (:section-data data))
       :body (:body (:section-data data))}))

  (did-mount [this]
    (when (:oc-editing (:section-data data))
      (let [selector (str "div#section-" (name (:section data)) " div.rich-editor")
            rich-editor (.querySelector js/document selector)]
        (.setTimeout js/window #(.focus rich-editor) 200))))

  (will-receive-props [_ next-props]
    ; this means the title or the body has changed from the API or at a upper lever of this component
    (when-not (= next-props (om/get-props owner))
      (om/set-state! owner :title (:title (:section-data next-props)))
      (om/set-state! owner :body (:body (:section-data next-props)))))

  (render [_]
    (let [section (:section data)
          section-name (utils/camel-case-str (name section))
          section-data (:section-data data)
          title-editing (om/get-state owner :title-editing)
          body-editing (om/get-state owner :body-editing)
          start-title-editing-fn #(start-title-editing-cb owner data)
          start-body-editing-fn #(start-body-editing-cb owner data)
          title-change-fn (partial change-cb owner :title)
          body-change-fn (partial change-cb owner :body)
          cancel-fn #(cancel-cb owner data)
          cancel-if-needed-fn #(cancel-if-needed-cb owner data)
          save-fn #(save-cb owner data)
          read-only-section (:read-only section-data)
          read-only (or (:read-only data) read-only-section)]
      (dom/div {:class "simple-section section-container"
                :id (str "section-" (name section))}

        (om/build editable-title {:editing title-editing
                                  :read-only read-only
                                  :title (om/get-state owner :title)
                                  :placeholder (or (:title-placeholder section-data) section-name)
                                  :section (:section data)
                                  :start-editing-cb start-title-editing-fn
                                  :change-cb title-change-fn
                                  :cancel-cb cancel-fn
                                  :cancel-if-needed-cb cancel-if-needed-fn
                                  :save-cb save-fn})

        (dom/div {:class "simple-section-body"}
          (om/build rich-editor {:editing body-editing
                                 :section section
                                 :body-counter (om/get-state owner :body-counter)
                                 :read-only read-only
                                 :body (om/get-state owner :body)
                                 :placeholder (or (:body-placeholder section-data)
                                                  (str section-name " notes here..."))
                                 :start-editing-cb start-body-editing-fn
                                 :change-cb body-change-fn
                                 :cancel-cb cancel-fn
                                 :cancel-if-needed-cb cancel-if-needed-fn
                                 :save-cb save-fn}))

        (om/build update-footer {:author (:author section-data)
                                 :updated-at (:updated-at section-data)
                                 :section section
                                 :editing (or title-editing body-editing)
                                 :notes false})
        (dom/div {:class "simple-section-footer"}
          (if (or title-editing body-editing)
            (om/build section-footer {:edting (or title-editing body-editing)
                                      :cancel-cb cancel-fn
                                      :save-cb save-fn
                                      :is-new-section (om/get-state owner :oc-editing)
                                      :save-disabled (not (has-changes owner data))})
            (om/build revisions-navigator data)))))))