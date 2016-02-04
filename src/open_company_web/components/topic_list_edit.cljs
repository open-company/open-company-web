(ns open-company-web.components.topic-list-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.manage-topic :refer (manage-topic)]))

(defcomponent topic-list-edit [data owner options]

  (init-state [_]
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    {:active-topics (:active-topics data)})

  (render [_]
    (if (empty? @caches/new-sections)
      (dom/h2 {} "Loading sections...")
    (let [active-category (:active-category options)
          all-sections (:new-sections options)
          category-sections (:sections (first (filter #(= (:name %) active-category) (:categories all-sections))))
          company-data (:company-data data)
          active-category (keyword (:active-category data))
          active-sections (get-in company-data [:sections active-category])]
      (dom/div {:class "topic-list-edit group"}
        (for [section category-sections]
          (dom/div {:class "topic-edit group"
                    :key (str "topic-edit-" (:name section))}
            (dom/div {:class "topic-edit-internal"}
              (dom/h3 {} (:title section))))))))))