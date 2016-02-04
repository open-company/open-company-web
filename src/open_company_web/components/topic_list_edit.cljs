(ns open-company-web.components.topic-list-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.manage-topic :refer (manage-topic)]))

(defcomponent topic-list-edit [data owner options]

  (init-state [_]
    (when (empty? @caches/new-sections)
      (api/get-new-sections))
    {:active-topics (get-in (:company-data data) [:sections (keyword (:active-category data))])})

  (render [_]
    (if (empty? @caches/new-sections)
      (dom/h2 {} "Loading sections...")
      (let [active-category (:active-category options)
            all-sections (:new-sections options)
            category-sections (:sections (first (filter #(= (:name %) active-category) (:categories all-sections))))
            active-sections (om/get-state owner :active-topics)]
        (dom/div {:class "topic-list-edit fix-top-margin-scrolling group"}
          (for [section category-sections]
            (let [active (utils/in? active-sections (:name section))
                  check-src (if active "/img/check_checked.png" "/img/check_empty.png")]
              (dom/div {:class (utils/class-set {:topic-edit true
                                                 :group true
                                                 :active active})
                        :on-click (fn []
                                    (if active
                                      (om/set-state! owner :active-topics (utils/vec-dissoc active-sections (:name section)))
                                      (om/set-state! owner :active-topics (concat active-sections [(:name section)]))))
                        :key (str "topic-edit-" (:name section))}
                (dom/div {:class "topic-edit-internal group"}
                  (dom/div {:class "topic-edit-labels"}
                    (dom/h3 {:class "topic-title oc-header"} (:title section))
                    (dom/label {:class "topic-description"} (:description section)))
                  (dom/img {:class "check" :src check-src}))))))))))