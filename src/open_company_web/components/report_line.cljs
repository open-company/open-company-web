(ns open-company-web.components.report-line
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [thousands-separator handle-change String->Number]]))


(defcomponent report-editable-line [view-data owner]
  (will-mount [_]
    (om/set-state! owner :editing false)
    (om/set-state! owner :value ((:key view-data) (:cursor view-data))))
  (render [_]
    (let [data (:cursor view-data)
          key (:key view-data)
          prefix (:prefix view-data)
          label (:label view-data)
          on-change (:on-change view-data)
          suffix (if (and (:pluralize view-data) (= (key data) 1)) "" "s")]
      (dom/div
        (if (> (count prefix) 0) (dom/span {:class "label"} (str prefix)))
        (dom/input {
          :class "editable-data"
          :ref (name key)
          :value (if (:editing (om/get-state owner))
                    (:value (om/get-state owner))
                    (thousands-separator (:value (om/get-state owner))))
          :onFocus #(om/set-state! owner :editing true)
          :onBlur #(let [value (String->Number (.. % -target -value))]
                    (om/set-state! owner :editing false)
                    (om/set-state! owner :value value)
                    (handle-change data value key)
                    (if-not (= on-change nil) (on-change value)))
          :onKeyPress #(let [value (.. % -target -value)]
                        (om/set-state! owner :value value)
                        (when (= (.-key %) "Enter")
                          (do
                            (handle-change data (String->Number value) key)
                            (if-not (= on-change nil) (on-change value)))))
          :onChange #(let [value (.. % -target -value)]
                      (om/set-state! owner :value value))
        })
        (dom/span {:class "label"} (str " " label suffix))))))

(defcomponent report-line [data owner]
  (will-mount [_]
    (om/set-state! owner {
      :number (:number data)
      :label (:label data)
      :prefix (or (:prefix data) "")
      :plural-suffix (or (:plural-suffix data) "s")
    }))
  (render [_]
    (let [number (:number data)
          label (:label data)
          prefix (or (:prefix data) "")
          plural-suffix (or (:plural-suffix data) "s")
          suffix (if (> number 0) plural-suffix)]
      (dom/span
        (if-not (= (count prefix) 0)
          (dom/span {:class "label"} (str prefix)))
        (dom/span {:class "num"} (str number))
        (dom/span {:class "label"} (str " " label suffix))))))
