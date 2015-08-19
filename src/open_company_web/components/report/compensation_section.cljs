(ns open-company-web.components.report.compensation-section
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defn count-head [key-name head-data]
  (cond
    (= :employees key-name)
    (+ (:ft-employees head-data) (:pt-employees head-data))

    :else
    (key-name head-data)))

(defcomponent compensation-section [data owner]
  (will-mount [_]
    (let [cursor (:cursor data)
          comp-data (:compensation cursor)
          key-name (:key-name data)
          value (key-name comp-data)]
      (om/set-state! owner :value (utils/thousands-separator value))))
  (render [_]
    (let [cursor (:cursor data)
          key-name (:key-name data)
          description (:description data)
          label (:label data)
          head-data (:headcount cursor)
          show-field (> (count-head key-name head-data) 0)
          comp-data (:compensation cursor)
          value (if show-field (key-name comp-data) 0)
          currency (:currency cursor)
          currency-dict (utils/get-currency currency)
          currency-symbol (utils/get-symbol-for-currency-code currency)]
      (when show-field
        (dom/div {:class "form-group"}
          (dom/label {:class "col-md-4 control-label"} label)
          (dom/div {:class "input-group col-md-3"}
            (dom/div {:class "input-group-addon"} currency-symbol)
            (dom/input {
              :type "text"
              :class "form-control"
              :id (name key-name)
              :value (om/get-state owner :value)
              :on-focus #(om/set-state! owner :value value)
              :on-change #(om/set-state! owner :value (.. % -target -value))
              :on-blur (fn [e]
                          (let [e-value (.. e -target -value)]
                            (utils/handle-change comp-data (utils/String->Number e-value) key-name)
                            (om/set-state! owner :value (utils/thousands-separator e-value))
                            (utils/save-values "save-report")
                            (.stopPropagation e)))
              :placeholder (:text currency-dict)}))
          (dom/p {:class "help-block"} description))))))
