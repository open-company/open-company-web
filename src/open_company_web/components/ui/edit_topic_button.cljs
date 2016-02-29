(ns open-company-web.components.ui.edit-topic-button
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defcomponent edit-topic-button [data owner {:keys [edit-topic-cb]}]
  (render [_]
    (dom/div {:class "topic-row edit-topic-button"
              :key "topic-row-edit-topic-button"}
      (dom/div {:class "edit-topic-button-internal"
                :on-click (fn [e]
                            (.stopPropagation e)
                            (edit-topic-cb))}
        (dom/div {:class "edit-topic-button-cta oc-header"} "Edit Topic")))))