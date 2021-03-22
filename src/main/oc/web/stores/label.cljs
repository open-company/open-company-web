(ns oc.web.stores.label
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.utils.activity :as au]))

(defmethod dispatcher/action :labels-loaded
  [db [_ org-slug labels-data]]
  (assoc-in db (dispatcher/labels-key org-slug) labels-data))

(defmethod dispatcher/action :label-editor/start
  [db [_ label-data]]
  (as-> db tdb
    (assoc tdb :show-label-editor true)
    (assoc tdb :show-labels-manager true)
    (assoc tdb :editing-label label-data)
    (if (get db :show-labels-manager)
      tdb
      (assoc-in tdb [:editing-label :dismiss-on-close?] true))))

(defmethod dispatcher/action :label-editor/dismiss
  [db [_]]
  (as-> db tdb
    (if (get-in db [:editing-label :dismiss-on-close?])
      (assoc tdb :show-labels-manager false)
      tdb)
    (assoc tdb :show-label-editor false)
    (dissoc tdb :editing-label)))

(defmethod dispatcher/action :delete-label
  [db [_ org-slug label-data]]
  (-> db
      (update-in (dispatcher/org-labels-key org-slug) (fn [labels] (filterv #(not= (:uuid %) (:uuid label-data)) labels)))
      (assoc :show-label-editor false)))

(defmethod dispatcher/action :label-saved
  [db [_ org-slug saved-label]]
  (as-> db tdb
    (update-in tdb (dispatcher/org-labels-key org-slug)
                 (fn [labels]
                   (let [found? (atom false)
                         updated-labels (mapv (fn [label]
                                                 (if (= (:uuid label) (:uuid saved-label))
                                                   (do
                                                     (reset! found? true)
                                                     saved-label)
                                                   label))
                                               labels)]
                     (if @found?
                       updated-labels
                       (concat labels [saved-label])))))
    (if (or (get-in db (conj dispatcher/cmail-state-key :labels-floating-view))
            (get-in db (conj dispatcher/cmail-state-key :labels-inline-view)))
      (-> tdb
          (update-in (conj dispatcher/cmail-data-key :labels)
                     (fn [labels]
                       (let [found? (atom false)
                             new-label (select-keys saved-label [:uuid :slug :color :name])
                             updated-labels (mapv (fn [label]
                                                    (if (= (:uuid label) (:uuid saved-label))
                                                      (do (reset! found? true)
                                                          new-label)
                                                      label))
                                                  labels)
                             next-labels (if @found?
                                           updated-labels
                                           (vec (conj labels new-label)))]
                         next-labels)))
          (assoc-in (conj dispatcher/cmail-data-key :debounce-autosave) true))
      tdb)))

(defmethod dispatcher/action :label-editor/update
  [db [_ label-data]]
  (update-in db [:editing-label] merge (assoc label-data :has-changes true)))

(defmethod dispatcher/action :label-editor/replace
  [db [_ label-data]]
  (-> db
      (assoc :editing-label label-data)
      (assoc :show-label-editor true)))
  
(defmethod dispatcher/action :labels-manager/show
  [db [_]]
  (assoc db :show-labels-manager true))

(defmethod dispatcher/action :labels-manager/hide
  [db [_]]
  (-> db
      (assoc :show-labels-manager false)
      (dissoc :show-label-editor)))

;; Label entries

(defmethod dispatcher/action :label-entries-get/finish
  [db [_ org-slug label-slug sort-type label-entries-data]]
  (let [org-data (dispatcher/org-data db org-slug)
        prepare-container-data (-> label-entries-data :collection (assoc :container-slug :label))
        fixed-label-entries-data (au/parse-label-entries prepare-container-data (dispatcher/change-data db) org-data (dispatcher/active-users org-slug db) sort-type)
        label-entries-data-key (dispatcher/label-entries-data-key org-slug label-slug sort-type)
        posts-key (dispatcher/posts-data-key org-slug)]
    (-> db
        (update-in posts-key merge (:fixed-items fixed-label-entries-data))
        (assoc-in label-entries-data-key (dissoc fixed-label-entries-data :fixed-items)))))

(defmethod dispatcher/action :label-entries-more
  [db [_ org-slug label-slug sort-type]]
  (let [label-entries-data-key (dispatcher/label-entries-data-key org-slug label-slug sort-type)
        label-entries-data (get-in db label-entries-data-key)
        next-label-entries-data (assoc label-entries-data :loading-more true)]
    (assoc-in db label-entries-data-key next-label-entries-data)))

(defmethod dispatcher/action :label-entries-more/finish
  [db [_ org-slug label-slug sort-type direction next-label-entries-data]]
  (if next-label-entries-data
    (let [label-entries-data-key (dispatcher/label-entries-data-key org-slug label-slug sort-type)
          label-entries-data (get-in db label-entries-data-key)
          posts-data-key (dispatcher/posts-data-key org-slug)
          old-posts (get-in db posts-data-key)
          prepare-label-entries-data (merge next-label-entries-data {:posts-list (:posts-list label-entries-data)
                                                         :old-links (:links label-entries-data)
                                                         :container-slug :label})
          org-data (dispatcher/org-data db org-slug)
          fixed-label-entries-data (au/parse-label-entries prepare-label-entries-data (dispatcher/change-data db) org-data (dispatcher/active-users org-slug db) sort-type direction)
          new-items-map (merge old-posts (:fixed-items fixed-label-entries-data))
          new-label-entries-data (-> fixed-label-entries-data
                               (assoc :direction direction)
                               (dissoc :loading-more))]
      (-> db
          (assoc-in label-entries-data-key new-label-entries-data)
          (assoc-in posts-data-key new-items-map)))
    db))