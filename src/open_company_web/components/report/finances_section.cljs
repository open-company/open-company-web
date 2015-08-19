(ns open-company-web.components.report.finances-section
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.lib.utils :as utils]))


(defcomponent finances-section [data owner]
  (will-mount [_]
    (let [key-name (:key-name data)
          cursor (:cursor data)
          value (key-name cursor)]
      (om/set-state! owner :value (utils/thousands-separator value))))
  (render [_]
    (let [key-name (:key-name data)
          cursor (:cursor data)
          value (key-name cursor)
          label (:label data)
          prefix (:prefix data)
          placeholder (:placeholder data)
          help-block (:help-block data)]
      (dom/div {:class "form-group"}
        (dom/label {:for "cash" :class "col-md-2 control-label"} label)
        (dom/div {:class "input-group col-md-2"}
          (dom/div {:class "input-group-addon"} prefix)
          (dom/input {
            :type "text"
            :class "form-control"
            :value (om/get-state owner :value)
            :id (name key-name)
            :on-focus #(om/set-state! owner :value value)
            :placeholder placeholder
            :on-change #(om/set-state! owner :value (.. % -target -value))
            :on-blur (fn [e]
                        (let [e-value (.. e -target -value)]
                          (utils/handle-change cursor e-value key-name)
                          (om/set-state! owner :cash (utils/thousands-separator e-value))
                          (utils/save-values "save-report")
                          (.stopPropagation e)))
            }))
        (dom/p {:class "help-block"} help-block)))))
