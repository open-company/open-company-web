(ns open-company-web.components.report-line
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [thousands-separator handle-change get-event-int-value]]))


(defcomponent report-editable-line [view-data owner]
  (render [_]
    (let [data (:cursor view-data)
          key (:key view-data)
          prefix (:prefix view-data)
          label (:label view-data)
          suffix (if (and (:pluralize view-data) (= (key data) 1)) "" "s")]
      (dom/div
        (if (> (count prefix) 0) (dom/span {:class "label"} (str prefix)))
        (dom/input {
          :class "editable-data"
          :ref (name key)
          :value (thousands-separator (key data))
          :onChange #(handle-change data (get-event-int-value %) key)
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
