(ns oc.web.stores.label
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.utils.activity :as au]
            [oc.web.utils.label :as label-utils]))

(defn- update-label-in-list [label-data labels-list]
  (mapv (fn [label]
          (if (= (:uuid label) (:uuid label-data))
            label-data
            label))
        labels-list))

(defn- upsert-label-in-list [label-data labels-list]
  (if (some #(when (= (:uuid %) (:uuid label-data)) %) labels-list)
    (update-label-in-list label-data labels-list)
    (vec (conj labels-list (label-utils/clean-entry-label label-data)))))

(defn- remove-label-from-list [label labels-list]
  (let [label-uuid (if (map? label) (:uuid label) label)]
    (filterv #(not= (:uuid %) label-uuid) labels-list)))

(defmethod dispatcher/action :labels-loaded
  [db [_ org-slug labels-data]]
  (assoc-in db (dispatcher/labels-key org-slug) labels-data))

(defmethod dispatcher/action :label-editor/start
  [db [_ label-data]]
  (assoc-in db dispatcher/editing-label-key label-data))

(defmethod dispatcher/action :label-editor/dismiss
  [db [_]]
  (assoc-in db dispatcher/editing-label-key nil))

(defmethod dispatcher/action :delete-label
  [db [_ org-slug label-data]]
  (-> db
      (update-in (dispatcher/org-labels-key org-slug) (partial remove-label-from-list label-data))
      (update-in (dispatcher/user-labels-key org-slug) (partial remove-label-from-list label-data))
      (assoc-in dispatcher/editing-label-key nil)))

(defmethod dispatcher/action :label-create
  [db [_ org-slug label-data]]
  (let [foc-labels-picker (get-in db dispatcher/foc-labels-picker-key)
        picker-entry-labels-key (when foc-labels-picker
                                  (dispatcher/entry-labels-key org-slug foc-labels-picker))]
    (if (label-utils/can-add-label? (get-in db picker-entry-labels-key))
      (as-> db tdb
        (assoc-in tdb (conj dispatcher/editing-label-key :saving) true)
        (if picker-entry-labels-key
          (update-in tdb picker-entry-labels-key (partial upsert-label-in-list (label-utils/clean-entry-label label-data)))
          tdb))
      db)))

(defmethod dispatcher/action :label-update
  [db [_ org-slug label-data]]
  (let [foc-labels-picker (get-in db dispatcher/foc-labels-picker-key)
        picker-entry-labels-key (when foc-labels-picker
                                  (dispatcher/entry-labels-key org-slug foc-labels-picker))]
    (if (label-utils/can-add-label? (get-in db picker-entry-labels-key))
      (as-> db tdb
        (assoc-in tdb (conj dispatcher/editing-label-key :saving) true)
        (if picker-entry-labels-key
          (update-in tdb picker-entry-labels-key (partial upsert-label-in-list (label-utils/clean-entry-label label-data)))
          tdb))
      db)))

(defmethod dispatcher/action :label-update/finished
  [db [_ org-slug updated-label]]
  (update-in db (dispatcher/org-labels-key org-slug) (partial update-label-in-list updated-label)))

(defmethod dispatcher/action :label-editor/update
  [db [_ label-data]]
  (update-in db dispatcher/editing-label-key merge (assoc label-data :has-changes true)))

(defmethod dispatcher/action :label-editor/replace
  [db [_ label-data]]
  (assoc-in db dispatcher/editing-label-key label-data))
  
(defmethod dispatcher/action :labels-manager/show
  [db [_]]
  (assoc db :show-labels-manager true))

(defmethod dispatcher/action :labels-manager/hide
  [db [_]]
  (dissoc db :show-labels-manager))

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

(defmethod dispatcher/action :toggle-foc-labels-picker
  [db [_ entry-uuid]]
  (assoc-in db dispatcher/foc-labels-picker-key entry-uuid))

(defmethod dispatcher/action :entry-label/add
  [db [_ org-slug entry-uuid label-data]]
  (if (label-utils/can-add-label? (dispatcher/entry-labels-data entry-uuid))
    (update-in db (dispatcher/entry-labels-key org-slug entry-uuid) (partial upsert-label-in-list (label-utils/clean-entry-label label-data)))
    db))

(defmethod dispatcher/action :entry-label/remove
  [db [_ org-slug entry-uuid {label-uuid :uuid}]]
  (let [entry-labels-key (conj (dispatcher/activity-key org-slug entry-uuid) :labels)]
    (if (get-in db entry-labels-key)
      (update-in db entry-labels-key (partial remove-label-from-list label-uuid))
      db)))