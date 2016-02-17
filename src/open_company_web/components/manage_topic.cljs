(ns open-company-web.components.manage-topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defcomponent manage-topic [data owner options]
  (render [_]
    (dom/div {:class "topic-row manage-topic group"
              :key "topic-row-manage-topic"}
      (dom/div {:class "manage-topic-internal"
                :on-click (:manage-topic-cb options)}
        (dom/div {:class "manage-topic-cta oc-header"} "Manage Topics")))))