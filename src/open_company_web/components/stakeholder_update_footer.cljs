(ns open-company-web.components.stakeholder-update-footer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
            [open-company-web.api :as api]
            [open-company-web.lib.utils :as utils]
            [cljsjs.medium-editor]))

(defcomponent stakeholder-update-footer [data owner options]

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (reset! open-company-web.core/prevent-route-dispatch true)
      ; save initial innerHTML and setup MediumEditor
      (let [body-el (om/get-ref owner "outro-body")
            med-ed (new js/MediumEditor body-el (clj->js (utils/medium-editor-options "Add a conclusion or wrap-up (optional).")))]
        (.subscribe med-ed "editableInput" #((:change-cb options) :outro (.-innerHTML body-el)))
        (om/set-state! owner :medium-editor med-ed))))

  (render [_]
    (dom/div {:class "update-footer group"}
      (dom/div {:class "update-footer-internal"}
        (dom/div #js {:className "outro-body"
                      :ref "outro-body"
                      :dangerouslySetInnerHTML (clj->js {"__html" (:outro data)})})
        (dom/div {:class "update-footer-close"})))))