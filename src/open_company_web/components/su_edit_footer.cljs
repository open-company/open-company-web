(ns open-company-web.components.su-edit-footer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
            [open-company-web.api :as api]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [cljsjs.medium-editor]))

(defcomponent su-edit-footer [data owner options]

  (init-state [_]
    {:outro (:outro data)})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (reset! prevent-route-dispatch true)
      ; save initial innerHTML and setup MediumEditor
      (let [body-el (om/get-ref owner "outro-body")
            med-ed (new js/MediumEditor body-el (clj->js (utils/medium-editor-options "Add a conclusion or wrap-up (optional)." false)))]
        (.subscribe med-ed "editableInput" #((:change-cb options) :outro (.-innerHTML body-el)))
        (om/set-state! owner :medium-editor med-ed))))

  (will-receive-props [_ next-props]
    ; update outro when the content changes from the parent component
    ; or when it force the conetnt refresh
    (let [next-outro (:outro next-props)]
      (when (or (:update-content next-props)
                (not= next-outro (:outro data)))
        (let [outro-body (om/get-ref owner "outro-body")]
          (set! (.-innerHTML outro-body) next-outro))
        (om/set-state! owner :outro next-outro))))

  (render-state [_ {:keys [outro]}]
    (dom/div {:class "update-footer group"}
      (dom/div {:class "update-footer-internal group"}
        (dom/div #js {:className "outro-body group"
                      :ref "outro-body"
                      :dangerouslySetInnerHTML (clj->js {"__html" outro})})
        (dom/div {:class "update-footer-close group"})))))