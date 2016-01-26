(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defcomponent topic [data owner options]
  (render [_]
    (let [company-data (:company-data data)
          section (keyword (:section-name options))
          section-data (company-data section)]
      (dom/div {:class "col-xs-12 topic"}
        (dom/div {:class "topic-title"} (:title section-data))
        (dom/div {:class "topic-headline"} (:headline section-data))
        (dom/div {:class "topic-date"} (:updated-at section))))))