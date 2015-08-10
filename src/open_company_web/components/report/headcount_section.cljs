(ns open-company-web.components.report.headcount-section
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.lib.utils :as utils]))

(defcomponent headcount-section [data owner]
  (will-mount [_]
    (let [cursor (:cursor data)
          key-name (:key-name data)
          value (if (contains? cursor key-name) (key-name cursor) 0)]
      (om/set-state! owner :value (utils/thousands-separator value))))
  (render [_]
    (let [cursor (:cursor data)
          help-block (:help-block data)
          label (:label data)
          key-name (:key-name data)
          value (key-name cursor)]
      (dom/div {:class "form-group"}
        (dom/label {:for "contractors" :class "col-md-4 control-label"} label)
        (dom/div {:class "input-group col-md-2"}
          (dom/input {
            :type "text"
            :class "form-control"
            :value (om/get-state owner :value)
            :on-change #(om/set-state! owner :value (.. % -target -value))
            :on-focus #(om/set-state! owner :value value)
            :on-blur (fn [e]
                        (let [e-value (.. e -target -value)]
                          (utils/handle-change cursor (utils/String->Number e-value) key-name)
                          (om/set-state! owner :value (utils/thousands-separator e-value))
                          (utils/save-values "save-report")
                          (.stopPropagation e)))}))
        (when help-block
          (dom/p {:class "help-block"} help-block))))))
