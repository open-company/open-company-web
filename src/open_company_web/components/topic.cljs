(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defcomponent topic [data owner options]

  (init-state [_]
    {:expanded false})

  (render [_]
    (let [company-data (:company-data data)
          section (keyword (:section-name options))
          section-data (company-data section)
          expanded (om/get-state owner :expanded)]
      (dom/div {:class "col-xs-12 topic"
                :on-click #(om/set-state! owner :expanded (not expanded))}
        (dom/div {:class "topic-title"} (:title section-data))
        (dom/div {:class "topic-headline"} (:headline section-data))
        (dom/div {:class "topic-date"} (utils/time-since (:updated-at section-data)))
        (dom/div #js {:className (utils/class-set {:topic-body true
                                                   :expanded expanded})
                      :dangerouslySetInnerHTML (clj->js {"__html" (:body section-data)})})))))