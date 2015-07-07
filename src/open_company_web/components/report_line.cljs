(ns open-company-web.components.report-line
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [thousands-separator handle-change String->Number display]]))

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
        (dom/label {
          :class "editable-label"
          :style (display (not (om/get-state owner :editing)))
          :onClick #(om/set-state! owner :editing true)
          } (thousands-separator (key data)))
        (dom/input {
          :style (display (om/get-state owner :editing))
          :class "editable-data"
          :value (key data)
          :onFocus #(om/set-state! owner :editing true)
          :onKeyPress #(let [value (.. % -target -value)]
                        ; need data check!
                        (when (= (.-key %) "Enter")
                          (do
                            (handle-change data (String->Number value) key)
                            (if-not (= on-change nil) (on-change value))
                            (om/set-state! owner :editing false))))
          :onChange #(let [value (.. % -target -value)]
                      (handle-change data value key))
        })
        (dom/span {:class "label"} (str " " label suffix))))))

(defcomponent report-line [data owner]
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
