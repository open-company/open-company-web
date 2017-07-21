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
            ; Title of the topic group
            [:div.by-topic-header.group
              [:div.by-topic-header-title
                (or topic-name
                    (s/capital topic-slug)
                    "Uncategorized")]
              ; If there are more than 4 add the button to show all of them
              (when (> (count entries-group) 4)
                [:button.view-all-updates.mlb-reset
                  {:on-click #(router/nav! (oc-urls/board-filter-by-topic topic-slug))}
                  "VIEW " (count entries-group) " UPDATES"])]
            ;; First row:
            [:div.entries-cards-container-row.group
              ; Render the first 2 entries
              (for [entry (take 2 entries-group)]
                (rum/with-key (entry-card entry false) (str "entry-by-topic-" topic "-" (:uuid entry))))
              ; If there is only 1 add the empty placeholder
              (when (and (not (empty? topic-slug))
                         (= (count entries-group) 1))
                (entry-card-empty (:read-only board-data)))]
            ; If there are more than 2 entries, render the second row
            (when (> (count entries-group) 2)
              [:div.entries-cards-container-row.group
                ; Render the second 2 entries
                (for [entry (subvec entries-group 2 (min 4 (count entries-group)))]
                  (rum/with-key (entry-card entry false) (str "entry-by-topic-" topic "-" (:uuid entry))))
                ; If the total entries are 3 add a placeholder to avoid taking the full width
                (when (= (count entries-group) 3)
                  [:div.entry-card.entry-card-placeholder])])]))
      ;; by specific topic
      (string? layout-type)
      (let [filtered-entries (if (= layout-type "uncategorized")
                                (vec (filter #(empty? (:topic-slug %)) (:entries board-data)))
                                (vec (filter #(= (:topic-slug %) layout-type) (:entries board-data))))
            sorted-entries (vec (reverse (sort-by :updated-at filtered-entries)))]
        [:div.entry-cards-container.by-specific-topic.group
          ; Calc the number of pairs
          (let [top-index (js/Math.ceil (/ (count sorted-entries) 2))]
            ; For each pari
            (for [idx (range top-index)
                  ; Calc the entries in the row
                  :let [start (* idx 2)
                        end (min (+ start 2) (count sorted-entries))
                        entries (subvec sorted-entries start end)]]
              [:div.entries-cards-container-row.group
                {:key (str "entries-row-" idx)}
                ; Render the entries in the row
                (for [entry sorted-entries]
                  (rum/with-key (entry-card entry false) (str "entry-topic-" (:topic-slug entry) "-" (:uuid entry))))
                ; If there is only one entry add the empty card placeholder
                (if (= (count sorted-entries) 1)
                  (entry-card-empty (:read-only board-data))
                  ; If there is only one entry in this row, but it's not the first add the placheolder
                  (when (= (count entries) 1)
                    [:div.entry-card.entry-card-placeholder]))]))])
      ;; :latest layout
      :else
      (let [sorted-entries (vec (reverse (sort-by :updated-at (:entries board-data))))]
        [:div.entry-cards-container.group
          ; Get the max number of pairs
          (let [top-index (js/Math.ceil (/ (count sorted-entries) 2))]
            ; For each pair
            (for [idx (range top-index)
                  ; calc the entries that needs to render in this row
                  :let [start (* idx 2)
                        end (min (+ start 2) (count sorted-entries))
                        entries (subvec sorted-entries start end)]]
              ; Renteder the entries in thisnrow
              [:div.entries-cards-container-row.group
                {:key (str "entries-row-" idx)}
                (for [entry entries]
                  (rum/with-key (entry-card entry true) (str "entry-latest-" (:uuid entry))))
                ; If the row contains less than 2, add a placeholder div to avoid having the first cover the full width
                (when (= (count entries) 1)
                  [:div.entry-card.entry-card-placeholder])]))]))])