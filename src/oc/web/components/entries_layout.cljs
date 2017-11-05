(ns oc.web.components.entries-layout
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [cljs-time.core :as time]
            [cljs-time.format :as f]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.urls :as oc-urls]
            [taoensso.timbre :as timbre]
            [oc.web.components.activity-card :refer (activity-card activity-card-empty)]))

(defn new?
  "
  An entry is new if:
  
    user is part of the team (we don't track new for non-team members accessing public boards)
      -and-
    user is not the post's author
      -and-
    created-at is < 30 days
      -and-
    
    created-at of the entry is newer than seen at
      -or-
    no seen at
  "
  [entry changes]
  (let [created-at (:created-at entry)
        too-old (f/unparse (f/formatters :date-time) (-> 30 time/days time/ago))
        seen-at (:seen-at changes)
        user-id (jwt/get-key :user-id)
        author-id (-> entry :author first :user-id)
        in-team? (jwt/user-is-part-of-the-team (:team-id (dis/org-data)))
        new? (and in-team?
                  (not= author-id user-id)
                  (> created-at too-old)
                  (or (> created-at seen-at)
                      (nil? seen-at)))]
    (timbre/debug "New'ness in board test for:" (:uuid entry)
                  "in-team?:" in-team?
                  "user-id / author-id:" user-id author-id
                  "created:" created-at
                  "seen:" seen-at
                  "new?:" new?)
    new?))

(rum/defcs entries-layout < rum/reactive
                          (drv/drv :change-data)
                          {:will-unmount (fn [s]
                            (dis/dispatch! [:board-nav-away {:board-uuid (:uuid (first (:rum/args s)))}])
                            s)}

  [s board-data layout-type]
  [:div.entries-layout
    (let [change-data (drv/react s :change-data)
          board-uuid (:uuid board-data)
          changes (get change-data board-uuid)]
      ;; Determine what sort of layout this is
      (cond
        ;; :by-topic
        (= layout-type :by-topic)
        (let [entries (vals (:fixed-items board-data))
              grouped-entries (apply merge (map (fn [[k v]] (hash-map k (vec (reverse (sort-by :created-at v))))) (group-by :topic-slug entries)))
              sorted-topics (vec (reverse (sort #(compare (:created-at (first (get grouped-entries %1))) (:created-at (first (get grouped-entries %2)))) (keys grouped-entries))))]
          (for [topic sorted-topics
                :let [entries-group (get grouped-entries topic)
                      topic-name (:topic-name (first entries-group))
                      topic-slug (:topic-slug (first entries-group))
                      first-line-entries (take 2 entries-group)
                      first-has-headline (some #(seq (:headline %)) first-line-entries)
                      first-has-body (some #(seq (:body %)) first-line-entries)
                      second-line-entries (if (> (count entries-group) 2) (subvec entries-group 2 (min 4 (count entries-group))) [])
                      second-has-headline (some #(seq (:headline %)) second-line-entries)
                      second-has-body (some #(seq (:body %)) second-line-entries)]]
            [:div.entry-cards-container.by-topic.group
              {:key (str "entries-topic-group-" (or topic "uncategorized"))}
              ; Title of the topic group
              [:div.by-topic-header.group
                [:div.by-topic-header-title
                  (or topic-name
                      (s/capital topic-slug)
                      [:span.oblique "No topic"])]
                ; If there are more than 4 add the button to show all of them
                (when (> (count entries-group) 4)
                  [:button.view-all-updates.mlb-reset
                    {:on-click #(router/nav! (oc-urls/board-filter-by-topic (or topic-slug "uncategorized")))}
                    "View " (count entries-group) " updates"])]
              ;; First row:
              [:div.entries-cards-container-row.group
                ; Render the first 2 entries
                (for [entry first-line-entries]
                  (rum/with-key (activity-card entry first-has-headline first-has-body (new? entry changes))
                    (str "entry-by-topic-" topic "-" (:uuid entry))))
                ; If there is only 1 add the empty placeholder
                (when (= (count entries-group) 1)
                  [:div.entry-card.entry-card-placeholder])]
              ; If there are more than 2 entries, render the second row
              (when (> (count entries-group) 2)
                [:div.entries-cards-container-row.group
                  ; Render the second 2 entries
                  (when (> (count entries-group) 2)
                    (for [entry (subvec entries-group 2 (min 4 (count entries-group)))]
                      (rum/with-key (activity-card entry second-has-headline second-has-body (new? entry changes))
                        (str "entry-by-topic-" topic "-" (:uuid entry)))))
                  ; If the total entries are 3 add a placeholder to avoid taking the full width
                  (when (= (count entries-group) 3)
                    [:div.entry-card.entry-card-placeholder])])]))
        ;; by specific topic
        (string? layout-type)
        (let [entries (vals (:fixed-items board-data))
              filtered-entries (if (= layout-type "uncategorized")
                                  (filterv #(empty? (:topic-slug %)) entries)
                                  (filterv #(= (:topic-slug %) layout-type) entries))
              sorted-entries (vec (reverse (sort-by :created-at filtered-entries)))]
          [:div.entry-cards-container.by-specific-topic.group
            ; Calc the number of pairs
            (let [top-index (js/Math.ceil (/ (count sorted-entries) 2))]
              ; For each pari
              (for [idx (range top-index)
                    ; Calc the entries in the row
                    :let [start (* idx 2)
                          end (min (+ start 2) (count sorted-entries))
                          entries (subvec sorted-entries start end)
                          has-headline (some #(seq (:headline %)) entries)
                          has-body (some #(seq (:body %)) entries)]]
                [:div.entries-cards-container-row.group
                  {:key (str "entries-row-" idx)}
                  ; Render the entries in the row
                  (for [entry entries]
                    (rum/with-key (activity-card entry has-headline has-body (new? entry changes))
                      (str "entry-topic-" (:topic-slug entry) "-" (:uuid entry))))
                  ; If there is only one entry add the empty card placeholder
                  (if (= (count sorted-entries) 1)
                    (let [entry-data (select-keys (first entries) [:board-name :topic-slug :topic-name])
                          with-board (merge entry-data {:board-slug (:slug board-data) :board-name (:name board-data)})]
                      (activity-card-empty with-board (:read-only board-data)))
                    ; If there is only one entry in this row, but it's not the first add the placheolder
                    (when (= (count entries) 1)
                      [:div.entry-card.entry-card-placeholder]))]))])
        ;; :latest layout
        :else
        (let [entries (vals (:fixed-items board-data))
              sorted-entries (vec (reverse (sort-by :created-at entries)))]
          [:div.entry-cards-container.group
            ; Get the max number of pairs
            (let [top-index (js/Math.ceil (/ (count sorted-entries) 2))]
              ; For each pair
              (for [idx (range top-index)
                    ; calc the entries that needs to render in this row
                    :let [start (* idx 2)
                          end (min (+ start 2) (count sorted-entries))
                          entries (subvec sorted-entries start end)
                          has-headline (some #(seq (:headline %)) entries)
                          has-body (some #(seq (:body %)) entries)]]
                ; Renteder the entries in thisnrow
                [:div.entries-cards-container-row.group
                  {:key (str "entries-row-" idx)}
                  (for [entry entries]
                    (rum/with-key (activity-card entry has-headline has-body (new? entry changes))
                      (str "entry-latest-" (:uuid entry))))
                  ; If the row contains less than 2, add a placeholder div to avoid having the first cover the full width
                  (when (= (count entries) 1)
                    [:div.entry-card.entry-card-placeholder])]))])))])