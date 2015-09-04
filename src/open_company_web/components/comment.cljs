(ns open-company-web.components.comment
    (:require [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.lib.utils :as utils]
              [cljs.core.async :refer [put!]]))

(defcomponent comment-component [data owner]
  (render [_]
    (let [cursor (:cursor data)
          placeholder (:placeholder data)]
      (dom/div {:class "row"}
        (dom/div {:class "col-md-1"})
        (dom/textarea {
          :class "col-md-10"
          :rows "5"
          :value (:comment cursor)
          :on-change #(utils/handle-change cursor (.. % -target -value) :comment)
          :on-blur (fn [e]
                      (utils/save-values "save-report")
                      (.stopPropagation e))
          :placeholder placeholder})
        (dom/div {:class "col-md-1"})))))

(defcomponent comment-readonly-component [data owner]
  (render [_]
    (let [disabled (or (:disabled data) false)
          save-channel (utils/get-channel "save-report")]
      (dom/div
        (dom/label {:class "comments-label"} "Comments:")
        (dom/br)
        (dom/textarea {
          :class "comment"
          :value ((:key data) (:cursor data))
          :onChange #(when-not disabled
                      (utils/handle-change (:cursor data) (.. % -target -value) (:key data)))
          :on-blur (fn [e] (put! save-channel 1) (.stopPropagation e))
          :disabled disabled})))))