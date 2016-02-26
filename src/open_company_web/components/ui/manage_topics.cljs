(ns open-company-web.components.ui.manage-topics
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defcomponent manage-topics [data owner options]
  (render [_]
    (dom/div {:class "manage-topics group"
              :key "topic-row-manage-topics"}
      (dom/div {:class "manage-topics-internal"
                :on-click (:manage-topics-cb options)}
        (dom/div {:class "manage-topics-cta oc-header"} "Manage topics...")))))