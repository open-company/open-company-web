(ns open-company-web.components.comment
    (:require [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.utils :refer [abs thousands-separator handle-change]]))


(defcomponent comment-component [data owner]
  (render [_]
    (let [disabled (or (:disabled data) false)]
      (dom/div
        (dom/label {:class "comments-label"} "Comments:")
        (dom/br)
        (dom/textarea {
          :class "comment"
          :value ((:key data) (:cursor data))
          :onChange #(when (not disabled)
                      (handle-change (:cursor data) (.. % -target -value) (:key data)))
          :disabled disabled
          })))))
