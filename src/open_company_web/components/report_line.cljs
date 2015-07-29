(ns open-company-web.components.report-line
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.lib.utils :refer [thousands-separator handle-change String->Number display get-channel]]
              [cljs.core.async :refer [put!]]))

(defn focus-and-move-cursor [owner]
  (let [ref (om/get-ref owner "field")]
    ; avoid eventual errors
    (when ref
      (let [input (.getDOMNode ref)
            value (.-value input)]
        (.focus input)
        (aset input "value" "")
        (aset input "value" value)))))

(defn set-editing-state! [owner state]
  ; if we are enabling the editing state focus on the field after a timeout
  (when state
    (do
      (.setTimeout js/window #(focus-and-move-cursor owner) 100)))
  (om/set-state! owner :editing state))

(defn save-field [e owner data key on-change]
  ; need data check!
  (let [value (String->Number (.. e -target -value))
        save-channel (get-channel "save-report")]
    (handle-change data value key)
    (if-not (= on-change nil) (on-change value))
    ; send signal to save report
    (put! save-channel 1)))

(defcomponent report-editable-line [view-data owner]
  (will-mount [_]
    (set-editing-state! owner false)
    (om/set-state! owner :value (get (:cursor view-data) (:key view-data))))
  (render [_]
    (let [data (:cursor view-data)
          key (:key view-data)
          prefix (:prefix view-data)
          label (:label view-data)
          on-change (:on-change view-data)
          pluralize (if (contains? view-data :pluralize) (:pluralize view-data) true)
          suffix (if (and pluralize (not (= (get data key) 1))) "s" "")]
      (dom/div {:class "report-editable-line"}
        (if (> (count prefix) 0) (dom/span {:class "label"} (str prefix)))
        (dom/label {
          :class "editable-label"
          :style (display (not (om/get-state owner :editing)))
          :onClick #(set-editing-state! owner true)
          } (thousands-separator (get data key)))
        (dom/input #js {
          :style (display (om/get-state owner :editing))
          :className "editable-data"
          :value (get data key)
          :onFocus #(set-editing-state! owner true)
          :onKeyDown #(when (= (.-key %) "Enter")
                        (set-editing-state! owner false))
          :onBlur #(save-field % owner data key on-change)
          :onChange #(let [value (String->Number (.. % -target -value))]
                      (handle-change data value key))
          :ref "field"
        })
        (dom/span {:class "label"} (str " " label suffix))))))

(defcomponent report-line [data owner]
  (render [_]
    (let [number (:number data)
          label (:label data)
          prefix (or (:prefix data) "")
          pluralize (if (contains? data :pluralize) (:pluralize data) true)
          plural-suffix (or (:plural-suffix data) "s")
          suffix (if (and pluralize (not (= number 1))) plural-suffix "")]
      (dom/span {:class "report-line"}
        (if-not (= (count prefix) 0)
          (dom/span {:class "label"} (str prefix)))
        (dom/span {:class "num"} (thousands-separator number))
        (dom/span {:class "label"} (str " " label suffix))))))