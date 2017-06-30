(ns oc.web.components.entries-layout
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.components.entry-card :refer (entry-card)]))

(rum/defc entries-layout
  [entries layout-type]
  [:div.entries-layout
    (cond
      ;; :by-topic
      (= layout-type :by-topic)
      (let [grouped-entries (group-by :topic-slug entries)]
        (for [[topic entries-group] grouped-entries]
          [:div.entry-cards-container.by-topic.group
            {:key (str "entries-topic-group-" topic)}
            [:div.by-topic-title (s/capital (:topic-slug (first entries-group)))]
            (for [entry entries-group]
              (rum/with-key (entry-card entry) (str "entry-by-topic-" topic "-" (:created-at entry))))]))
      ;; by specific topic
      (string? layout-type)
      (let [filtered-entries (vec (filter #(= (:topic-slug %) layout-type) entries))]
        [:div.entry-cards-container.by-specific-topic.group
          (for [entry filtered-entries]
            (rum/with-key (entry-card entry) (str "entry-topic-" (:topic-slug entry) "-" (:created-at entry))))])
      ;; :latest layout
      :else
      (let [sorted-entries (vec (sort-by :updated-at entries))]
        [:div.entry-cards-container.group
          (for [entry sorted-entries]
            (rum/with-key (entry-card entry) (str "entry-latest-" (:created-at entry))))]))])