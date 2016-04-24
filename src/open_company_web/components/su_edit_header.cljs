(ns open-company-web.components.su-edit-header
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
            [open-company-web.api :as api]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [cljsjs.medium-editor]
            [cljsjs.react.dom]))

(defcomponent su-edit-header [data owner options]

  (init-state [_]
    {:intro (:intro data)})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (when-let [input (.findDOMNode js/ReactDOM (om/get-ref owner "update-title-input"))]
        (set! (.-value input) (.-value input))
        (.focus input))
      (reset! prevent-route-dispatch true)
      ; save initial innerHTML and setup MediumEditor
      (let [body-el (om/get-ref owner "intro-body")
            med-ed (new js/MediumEditor body-el (clj->js (utils/medium-editor-options "Add an introduction (optional).")))]
        (.subscribe med-ed "editableInput" #((:change-cb options) :intro (.-innerHTML body-el)))
        (om/set-state! owner :medium-editor med-ed))))

  (will-receive-props [_ next-props]
    ; update intro when the content changes from the parent component
    ; or when it force the conetnt refresh
    (let [next-intro (:intro next-props)]
      (when (or (:update-content next-props)
                (not= next-intro (:intro data)))
        (let [intro-body (om/get-ref owner "intro-body")]
          (set! (.-innerHTML intro-body) next-intro))
        (om/set-state! owner :intro next-intro))))

  (render [_]
    (dom/div {:class "update-header"}
      (dom/div {:class "update-header-internal"}
        (dom/div {:class "update-title"}
          (dom/input #js {:className "update-title-input"
                          :value (:title data)
                          :maxLength 100
                          :ref "update-title-input"
                          :onChange #((:change-cb options) :title (.. % -target -value))}))
        (dom/div #js {:className "intro-body"
                      :ref "intro-body"
                      :dangerouslySetInnerHTML (clj->js {"__html" (:intro data)})})))))