(ns oc.web.components.entries-layout
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.router :as router]
            [oc.web.urls :as oc-urls]
            [oc.web.components.entry-card :refer (entry-card entry-card-empty)]))

(rum/defc entries-layout
  [board-data layout-type]
  [:div.entries-layout
    (cond
      ;; :by-topic
      (= layout-type :by-topic)
      (let [grouped-entries (apply merge (map (fn [[k v]] (hash-map k (vec (reverse (sort-by :updated-at v))))) (group-by :topic-slug (:entries board-data))))
            sorted-topics (vec (reverse (sort #(compare (:updated-at (first (get grouped-entries %1))) (:updated-at (first (get grouped-entries %2)))) (keys grouped-entries))))]
        (for [topic sorted-topics
              :let [entries-group (get grouped-entries topic)
                    topic-name (:topic-name (first entries-group))
                    topic-slug (:topic-slug (first entries-group))]]
          [:div.entry-cards-container.by-topic.group
            {:key (str "entries-topic-group-" (or topic "uncategorized"))}
            [:div.by-topic-header.group
              [:div.by-topic-header-title
                (or topic-name
                    (s/capital topic-slug)
                    "Uncategorized")]
              (when (> (count entries-group) 14)
                [:button.view-all-updates.mlb-reset
                  {:on-click #(router/nav! (oc-urls/board-filter-by-topic topic-slug))}
                  "VIEW " (count entries-group) " UPDATES"])]
            (for [entry (take 4 entries-group)]
              (rum/with-key (entry-card entry false) (str "entry-by-topic-" topic "-" (:uuid entry))))
            (when (or (= (count entries-group) 1)
                      (= (count entries-group) 3))
              (entry-card-empty (:read-only board-data)))]))
      ;; by specific topic
      (string? layout-type)
      (let [filtered-entries (vec (filter #(= (:topic-slug %) layout-type) (:entries board-data)))
            sorted-entries (vec (reverse (sort-by :updated-at filtered-entries)))]
        [:div.entry-cards-container.by-specific-topic.group
          (for [entry sorted-entries]
            (rum/with-key (entry-card entry false) (str "entry-topic-" (:topic-slug entry) "-" (:uuid entry))))
          (when (= (count sorted-entries) 1)
              (entry-card-empty (:read-only board-data)))])
      ;; :latest layout
      :else
      (let [sorted-entries (vec (reverse (sort-by :updated-at (:entries board-data))))]
        [:div.entry-cards-container.group
          (for [entry sorted-entries]
            (rum/with-key (entry-card entry true) (str "entry-latest-" (:uuid entry))))]))])