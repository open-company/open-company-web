(ns oc.web.stores.activity
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]))

(defn add-remove-item-from-all-posts
  "Given an activity map adds or remove it from the all-posts list of posts depending on the activity
   status"
  [db org-slug activity-data]
  (if (:uuid activity-data)
    (let [;; Add/remove item from AP
          is-published? (= (:status activity-data) "published")
          ap-key (dispatcher/container-key org-slug :all-posts dispatcher/other-sort-type)
          recent-ap-key (dispatcher/container-key org-slug :all-posts dispatcher/default-sort-type)
          old-ap-data (get-in db ap-key)
          old-recent-ap-data (get-in db recent-ap-key)
          old-ap-data-posts (get old-ap-data :posts-list)
          old-recent-ap-data-posts (get old-recent-ap-data :posts-list)
          ap-without-uuid (utils/vec-dissoc old-ap-data-posts (:uuid activity-data))
          recent-ap-without-uuid (utils/vec-dissoc old-recent-ap-data-posts (:uuid activity-data))
          new-ap-data-posts (vec
                             (if is-published?
                               (conj ap-without-uuid (:uuid activity-data))
                               ap-without-uuid))
          new-recent-ap-data-posts (vec
                                    (if is-published?
                                      (conj recent-ap-without-uuid (:uuid activity-data))
                                      recent-ap-without-uuid))
          next-ap-data (assoc old-ap-data :posts-list new-ap-data-posts)
          next-recent-ap-data (assoc old-ap-data :posts-list new-recent-ap-data-posts)]
      (-> db
       (assoc-in ap-key next-ap-data)
       (assoc-in recent-ap-key next-recent-ap-data)))
    db))

(defn add-remove-item-from-follow-ups
  "Given an activity map adds or remove it from the follow-ups list of posts depending on the activity
   status and if the user assigned follow-up is not completed."
  [db org-slug activity-data]
  (if (:uuid activity-data)
    (let [;; Add/remove item from MS
          user-follow-up (first (filterv #(= (-> % :assignee :user-id) (j/user-id)) (:follow-ups activity-data)))
          is-follow-ups? (and (not= (:status activity-data) "draft")
                              (not (:completed? user-follow-up)))
          fu-key (dispatcher/container-key org-slug :follow-ups dispatcher/other-sort-type)
          recent-fu-key (dispatcher/container-key org-slug :follow-ups dispatcher/default-sort-type)
          old-fu-data (get-in db fu-key)
          old-recent-fu-data (get-in db recent-fu-key)
          old-fu-data-posts (get old-fu-data :posts-list)
          old-recent-fu-data-posts (get old-recent-fu-data :posts-list)
          fu-without-uuid (utils/vec-dissoc old-fu-data-posts (:uuid activity-data))
          recent-fu-without-uuid (utils/vec-dissoc old-recent-fu-data-posts (:uuid activity-data))
          new-fu-data-posts (vec
                             (if is-follow-ups?
                               (conj fu-without-uuid (:uuid activity-data))
                               fu-without-uuid))
          new-recent-fu-data-posts (vec
                                    (if is-follow-ups?
                                      (conj recent-fu-without-uuid (:uuid activity-data))
                                      recent-fu-without-uuid))
          next-fu-data (assoc old-fu-data :posts-list new-fu-data-posts)
          next-recent-fu-data (assoc old-recent-fu-data :posts-list new-recent-fu-data-posts)]
      (-> db
        (assoc-in fu-key next-fu-data)
        (assoc-in recent-fu-key next-recent-fu-data)))
    db))

(defmethod dispatcher/action :entry-edit/dismiss
  [db [_]]
  (-> db
    (dissoc :entry-editing)
    (assoc :entry-edit-dissmissing true)))

(defmethod dispatcher/action :capture-video/dismiss
  [db [_]]
  (-> db
    (dissoc :capture-video)
    (assoc :capture-video-dissmissing true)))

(defmethod dispatcher/action :modal-editing-deactivate
  [db [_]]
  (dissoc db :modal-editing))

(defmethod dispatcher/action :modal-editing-activate
  [db [_]]
  (-> db
    (assoc :modal-editing true)
    (assoc :entry-save-on-exit true)))

(defmethod dispatcher/action :entry-toggle-save-on-exit
  [db [_ enabled?]]
  (assoc db :entry-save-on-exit enabled?))

(defmethod dispatcher/action :entry-modal-save
  [db [_]]
  (assoc-in db [:modal-editing-data :loading] true))

(defmethod dispatcher/action :nux-next-step
  [db [_ next-step]]
  (assoc db :nux next-step))

(defmethod dispatcher/action :activity-add-attachment
  [db [_ dispatch-input-key attachment-data]]
  (let [old-attachments (or (-> db dispatch-input-key :attachments) [])
        next-attachments (vec (conj old-attachments attachment-data))]
    (assoc-in db [dispatch-input-key :attachments] next-attachments)))

(defmethod dispatcher/action :activity-remove-attachment
  [db [_ dispatch-input-key attachment-data]]
  (let [old-attachments (or (-> db dispatch-input-key :attachments) [])
        next-attachments (filterv #(not= (:file-url %) (:file-url attachment-data)) old-attachments)]
    (assoc-in db [dispatch-input-key :attachments] next-attachments)))

(defmethod dispatcher/action :entry-clear-local-cache
  [db [_ edit-key]]
  (dissoc db :entry-save-on-exit))

(defmethod dispatcher/action :entry-save
  [db [_ edit-key]]
  (assoc-in db [edit-key :loading] true))

(defmethod dispatcher/action :entry-save/finish
  [db [_ activity-data edit-key]]
  (let [org-slug (utils/post-org-slug activity-data)
        board-slug (:board-slug activity-data)
        activity-key (dispatcher/activity-key org-slug (:uuid activity-data))
        activity-board-data (dispatcher/board-data db org-slug board-slug)
        fixed-activity-data (au/fix-entry activity-data activity-board-data (dispatcher/change-data db))
        next-db (assoc-in db activity-key fixed-activity-data)
        with-edited-key (if edit-key
                          (update-in next-db [edit-key] dissoc :loading)
                          next-db)
        without-entry-save-on-exit (dissoc with-edited-key :entry-toggle-save-on-exit)]
    (dissoc without-entry-save-on-exit :section-editing)))

(defmethod dispatcher/action :entry-save/failed
  [db [_ edit-key]]
  (-> db
    (update-in [edit-key] dissoc :loading)
    (update-in [edit-key] assoc :error true)))

(defmethod dispatcher/action :entry-publish [db [_ edit-key]]
  (assoc-in db [edit-key :publishing] true))

(defmethod dispatcher/action :section-edit/error [db [_ error]]
  (-> db
    (assoc-in [:section-editing :section-name-error] error)
    (update-in [:section-editing] dissoc :loading)))

(defmethod dispatcher/action :entry-publish-with-board/finish
  [db [_ sort-type new-board-data edit-key]]
  (let [org-slug (utils/section-org-slug new-board-data)
        board-slug (:slug new-board-data)
        posts-key (dispatcher/posts-data-key org-slug)
        board-key (dispatcher/board-data-key org-slug board-slug sort-type)
        fixed-board-data (au/fix-board new-board-data (dispatcher/change-data db))
        merged-items (merge (get-in db posts-key)
                            (:fixed-items fixed-board-data))]
    (-> db
      (assoc-in board-key (dissoc fixed-board-data :fixed-items))
      (assoc-in posts-key merged-items)
      (dissoc :section-editing)
      (update-in [edit-key] dissoc :publishing)
      (assoc-in [edit-key :board-slug] (:slug fixed-board-data))
      (assoc-in [edit-key :new-section] true)
      (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :entry-publish/finish
  [db [_ edit-key activity-data]]
  (let [org-slug (utils/post-org-slug activity-data)
        board-data (au/board-by-uuid (:board-uuid activity-data))
        fixed-activity-data (au/fix-entry activity-data board-data (dispatcher/change-data db))]
    (-> db
      (assoc-in (dispatcher/activity-key org-slug (:uuid activity-data)) fixed-activity-data)
      (add-remove-item-from-all-posts org-slug fixed-activity-data)
      (add-remove-item-from-follow-ups org-slug fixed-activity-data)
      (update-in [edit-key] dissoc :publishing)
      (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :entry-publish/failed
  [db [_ edit-key]]
  (-> db
    (update-in [edit-key] dissoc :publishing)
    (update-in [edit-key] assoc :error true)))

(defmethod dispatcher/action :activity-delete
  [db [_ org-slug activity-data]]
  (let [posts-key (dispatcher/posts-data-key org-slug)
        posts-data (dispatcher/posts-data)
        next-posts (dissoc posts-data (:uuid activity-data))
        ;; Remove the post from all the containers posts list
        containers-key (dispatcher/containers-key org-slug)
        with-fixed-containers (reduce
                               (fn [ndb ckey]
                                 (update-in ndb (conj (dispatcher/container-key org-slug ckey) :posts-list)
                                  (fn [posts-list]
                                    (filter #(not= % (:uuid activity-data)) posts-list))))
                               db
                               (keys (get-in db containers-key)))
        ;; Remove the post from all the boards posts list too
        boards-key (dispatcher/boards-key org-slug)
        with-fixed-boards (reduce
                           (fn [ndb ckey]
                             (update-in ndb (conj (dispatcher/board-data-key org-slug ckey) :posts-list)
                              (fn [posts-list]
                                (filter #(not= % (:uuid activity-data)) posts-list))))
                           with-fixed-containers
                           (keys (get-in db boards-key)))]
    ;; Now if the post is the one being edited in cmail let's remove it from there too
    (if (= (:uuid (get-in db [:cmail-data])) (:uuid activity-data))
      (-> with-fixed-boards
          (assoc-in [:cmail-data] {:delete true})
          (assoc-in posts-key next-posts))
      with-fixed-boards)))

(defmethod dispatcher/action :activity-move
  [db [_ activity-data org-slug board-data]]
  (let [change-data (dispatcher/change-data db)
        fixed-activity-data (au/fix-entry activity-data board-data change-data)
        activity-key (dispatcher/activity-key
                      org-slug
                      (:uuid activity-data))]
    (assoc-in db activity-key fixed-activity-data)))

(defmethod dispatcher/action :activity-share-show
  [db [_ activity-data container-element-id share-medium]]
  (-> db
    (assoc :activity-share {:share-data activity-data})
    (assoc :activity-share-container container-element-id)
    (assoc :activity-share-medium share-medium)
    (dissoc :activity-shared-data)))

(defmethod dispatcher/action :activity-share-hide
  [db [_]]
  (-> db
    (dissoc :activity-share)
    (dissoc :activity-share-medium)
    (dissoc :activity-share-container)))

(defmethod dispatcher/action :activity-share-reset
  [db [_]]
  (dissoc db :activity-shared-data))

(defmethod dispatcher/action :activity-share
  [db [_ share-data]]
  (assoc db :activity-share-data share-data))

(defmethod dispatcher/action :activity-share/finish
  [db [_ success shared-data]]
  (assoc db :activity-shared-data
    (if success
      (au/fix-entry shared-data (:board-slug shared-data) (dispatcher/change-data db))
      {:error true})))

(defmethod dispatcher/action :entry-revert [db [_ entry]]
  ;; do nothing for now
  db)

(defmethod dispatcher/action :activity-get/not-found
  [db [_ org-slug activity-uuid secure-uuid]]
  (let [activity-key (if secure-uuid
                       (dispatcher/secure-activity-key org-slug secure-uuid)
                       (dispatcher/activity-key org-slug activity-uuid))]
    (assoc-in db activity-key :404)))

(defmethod dispatcher/action :activity-get/finish
  [db [_ status org-slug activity-data secure-uuid]]
  (let [activity-uuid (:uuid activity-data)
        board-data (au/board-by-uuid (:board-uuid activity-data))
        activity-key (if secure-uuid
                       (dispatcher/secure-activity-key org-slug secure-uuid)
                       (dispatcher/activity-key org-slug activity-uuid))
        fixed-activity-data (au/fix-entry
                             activity-data
                             board-data
                             (dispatcher/change-data db))]
    (assoc-in db activity-key fixed-activity-data)))

(defmethod dispatcher/action :follow-up-toggle
  [db [_ org-slug activity-data follow-ups]]
  (let [should-update-post? (:uuid activity-data)
        activity-key (when should-update-post?
                      (dispatcher/activity-key org-slug (:uuid activity-data)))
        post-activity-data (when should-update-post?
                             (get-in db activity-key))
        next-activity-data (when should-update-post?
                             (if (seq follow-ups)
                               (assoc post-activity-data :follow-ups follow-ups)
                               (dissoc post-activity-data :follow-ups)))
        with-updated-activity-data (if should-update-post?
                                    (assoc-in db activity-key next-activity-data)
                                    db)
        cmail-data (get db :cmail-data)
        next-cmail-data* (assoc cmail-data :has-changes true)
        next-cmail-data (if (seq follow-ups)
                           (assoc next-cmail-data* :follow-ups follow-ups)
                           (dissoc next-cmail-data* :follow-ups))]
      (-> with-updated-activity-data
        (assoc :cmail-data next-cmail-data)
        (add-remove-item-from-follow-ups org-slug next-activity-data))))

(defmethod dispatcher/action :entry-save-with-board/finish
  [db [_ org-slug sort-type fixed-board-data]]
  (let [board-key (dispatcher/board-data-key org-slug (:slug fixed-board-data) sort-type)
        posts-key (dispatcher/posts-data-key org-slug)]
  (-> db
    (assoc-in board-key (dissoc fixed-board-data :fixed-items))
    (assoc-in posts-key (merge (get-in db posts-key) (get fixed-board-data :fixed-items)))
    (dissoc :section-editing)
    (update-in [:modal-editing-data] dissoc :loading)
    (assoc-in [:modal-editing-data :board-slug] (:slug fixed-board-data))
    (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :all-posts-get/finish
  [db [_ org-slug sort-type fixed-posts]]
  (let [posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-posts))
        container-key (dispatcher/container-key org-slug :all-posts sort-type)
        with-posts-list (assoc fixed-posts :posts-list (map :uuid (:items fixed-posts)))]
    (-> db
      (assoc-in container-key (dissoc fixed-posts :fixed-items))
      (assoc-in posts-key merged-items))))

(defmethod dispatcher/action :all-posts-more
  [db [_ org-slug sort-type]]
  (let [container-key (dispatcher/container-key org-slug :all-posts sort-type)
        container-data (get-in db container-key)
        next-posts-data (assoc container-data :loading-more true)]
    (assoc-in db container-key next-posts-data)))

(defmethod dispatcher/action :all-posts-more/finish
  [db [_ org direction sort-type posts-data]]
  (if posts-data
    (let [org-data (dispatcher/org-data db org)
          container-key (dispatcher/container-key org :all-posts sort-type)
          container-data (get-in db container-key)
          posts-data-key (dispatcher/posts-data-key org)
          old-posts (get-in db posts-data-key)
          prepare-posts-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                              :old-links (:links container-data)})
          fixed-posts-data (au/fix-container prepare-posts-data (dispatcher/change-data db) org-data direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more))]
      (-> db
        (assoc-in container-key new-container-data)
        (assoc-in posts-data-key new-items-map)))
    db))

;; Follow-ups

(defmethod dispatcher/action :follow-ups-get/finish
  [db [_ org-slug sort-type fixed-posts]]
  (let [posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-posts))
        container-key (dispatcher/container-key org-slug :follow-ups sort-type)
        with-posts-list (assoc fixed-posts :posts-list (map :uuid (:items fixed-posts)))]
    (-> db
      (assoc-in container-key (dissoc fixed-posts :fixed-items))
      (assoc-in posts-key merged-items))))

(defmethod dispatcher/action :follow-ups-more
  [db [_ org-slug sort-type]]
  (let [container-key (dispatcher/container-key org-slug :follow-ups sort-type)
        container-data (get-in db container-key)
        next-posts-data (assoc container-data :loading-more true)]
    (assoc-in db container-key next-posts-data)))

(defmethod dispatcher/action :follow-ups-more/finish
  [db [_ org direction sort-type posts-data]]
  (if posts-data
    (let [org-data (dispatcher/org-data db org)
          container-key (dispatcher/container-key org :follow-ups sort-type)
          container-data (get-in db container-key)
          posts-data-key (dispatcher/posts-data-key org)
          old-posts (get-in db posts-data-key)
          prepare-posts-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                              :old-links (:links container-data)})
          fixed-posts-data (au/fix-container prepare-posts-data (dispatcher/change-data db) org-data direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more))]
      (-> db
        (assoc-in container-key new-container-data)
        (assoc-in posts-data-key new-items-map)))
    db))

(defmethod dispatcher/action :follow-up-complete
  [db [_ org-slug entry-data]]
  (let [activity-key (dispatcher/activity-key org-slug (:uuid entry-data))
        follow-up-key (dispatcher/container-key org-slug :follow-ups dispatcher/other-sort-type)
        follow-up-data (get-in db follow-up-key)
        recent-follow-up-key (dispatcher/container-key org-slug :follow-ups dispatcher/default-sort-type)
        recent-follow-up-data (get-in db recent-follow-up-key)
        org-key (dispatcher/org-data-key org-slug)]
    (-> db
      (update-in (conj org-key :follow-ups-count) dec)
      (assoc-in activity-key entry-data)
      (add-remove-item-from-follow-ups org-slug entry-data))))

(defmethod dispatcher/action :follow-up-create-self
  [db [_ org-slug activity-data]]
  (let [org-key (dispatcher/org-data-key org-slug)]
    (update-in db (conj org-key :follow-ups-count) inc)))

(defmethod dispatcher/action :activities-count
  [db [_ items-count]]
  (let [old-reads-data (get-in db dispatcher/activities-read-key)
        ks (vec (map :item-id items-count))
        vs (map #(zipmap [:count :reads :item-id :last-read-at]
                         [(:count %)
                          (get-in old-reads-data [(:item-id %) :reads])
                          (:item-id %)
                          (:last-read-at %)]) items-count)
        new-items-count (zipmap ks vs)]
    (update-in db dispatcher/activities-read-key merge new-items-count)))

(defmethod dispatcher/action :activity-reads
  [db [_ org-slug item-id read-data-count read-data team-roster]]
  (let [activity-data   (dispatcher/activity-data org-slug item-id db)
        org-data        (dispatcher/org-data db org-slug)
        board-data      (first (filter #(= (:slug %) (:board-slug activity-data)) (:boards org-data)))
        fixed-read-data (vec (map #(assoc % :seen true) read-data))
        team-users      (filterv #(#{"active" "unverified"} (:status %)) (:users team-roster))
        seen-ids        (set (map :user-id read-data))
        private-access? (= (:access board-data) "private")
        all-private-users (when private-access?
                            (set (concat (:authors board-data) (:viewers board-data))))
        filtered-users  (if private-access?
                          (filterv #(all-private-users (:user-id %)) team-users)
                          team-users)
        all-ids         (set (map :user-id filtered-users))
        unseen-ids      (clojure.set/difference all-ids seen-ids)
        unseen-users    (vec (map (fn [user-id]
                         (first (filter #(= (:user-id %) user-id) team-users))) unseen-ids))
        current-user-id (j/user-id)
        current-user-reads (filterv #(= (:user-id %) current-user-id) read-data)]
    (assoc-in db (conj dispatcher/activities-read-key item-id) {:count read-data-count
                                                                :reads fixed-read-data
                                                                :item-id item-id
                                                                :unreads unseen-users
                                                                :last-read-at (:read-at (last (sort-by :read-at
                                                                               current-user-reads)))
                                                                :private-access? private-access?})))

(defmethod dispatcher/action :must-see-get/finish
  [db [_ org-slug sort-type must-see-posts]]
  (let [posts-data-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-data-key)
        merged-items (merge old-posts (:fixed-items must-see-posts))]
    (-> db
     (assoc-in posts-data-key merged-items)
     (assoc-in (dispatcher/container-key org-slug :must-see sort-type) (dissoc must-see-posts :fixed-items)))))

(defmethod dispatcher/action :must-see-more
  [db [_ org-slug sort-type]]
  (let [container-key (dispatcher/container-key org-slug :must-see sort-type)
        container-data (get-in db container-key)
        next-posts-data (assoc container-data :loading-more true)]
    (assoc-in db container-key next-posts-data)))

(defmethod dispatcher/action :must-see-more/finish
  [db [_ org direction sort-type posts-data]]
  (if posts-data
    (let [org-data (dispatcher/org-data db org)
          container-key (dispatcher/container-key org :must-see sort-type)
          container-data (get-in db container-key)
          posts-data-key (dispatcher/posts-data-key org)
          old-posts (get-in db posts-data-key)
          prepare-posts-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                              :old-links (:links container-data)})

          fixed-posts-data (au/fix-container prepare-posts-data (dispatcher/change-data db) org-data direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more))]
      (-> db
        (assoc-in container-key new-container-data)
        (assoc-in posts-data-key new-items-map)))
    db))

(defmethod dispatcher/action :uploading-video
  [db [_ org-slug video-id]]
  (let [uploading-video-key (dispatcher/uploading-video-key org-slug video-id)]
    (assoc-in db uploading-video-key true)))

(defmethod dispatcher/action :entry-auto-save/finish
  [db [_ activity-data edit-key initial-entry-map]]
  (let [org-slug (utils/post-org-slug activity-data)
        board-slug (:board-slug activity-data)
        activity-key (dispatcher/activity-key org-slug (:uuid activity-data))
        activity-board-data (dispatcher/board-data db org-slug board-slug)
        fixed-activity-data (au/fix-entry activity-data activity-board-data (dispatcher/change-data db))
        ;; these are the data we need to move from the saved post to the editing map
        ;; we don't have to override the keys that the user could have changed during the PATCH/POST request
        keys-for-edit [:uuid :board-uuid :links :revision-id :secure-uuid :status]
        current-edit (get db edit-key)
        ;; if it's still the same board let's get the updated data from the autosave request
        with-board-keys (if (= (:board-name current-edit) (:board-name initial-entry-map))
                          (concat keys-for-edit [:board-slug :board-uuid])
                          keys-for-edit)
        is-same-video?(= (:video-id current-edit) (:video-id initial-entry-map))
        ;; if it's the same video-id let's get video-processed from the server
        with-video-keys (if is-same-video?
                          (concat with-board-keys [:video-processed])
                          with-board-keys)
        ;; and set video-error to true if one of the 2 is tue
        video-error (if is-same-video?
                      (or (:video-error current-edit) (:video-error activity-data))
                      (:video-error current-edit))
        map-for-edit (merge (select-keys activity-data with-video-keys)
                        {:auto-saving false
                         :video-error video-error})]
    (-> db
      (assoc-in activity-key fixed-activity-data)
      (dissoc :entry-toggle-save-on-exit)
      (update-in [edit-key] merge map-for-edit))))

(defmethod dispatcher/action :entry-revert/finish
  [db [_ activity-data]]
  (let [org-slug (utils/post-org-slug activity-data)
        board-slug (:board-slug activity-data)
        activity-key (dispatcher/activity-key org-slug (:uuid activity-data))]
    ;; If board-slug is not present it means the entry was removed
    (if (seq (:board-slug activity-data))
      (let [activity-board-data (dispatcher/board-data db org-slug board-slug)
            fixed-activity-data (au/fix-entry activity-data activity-board-data (dispatcher/change-data db))]
        (assoc-in db activity-key fixed-activity-data))
      (update-in db (butlast activity-key) dissoc (last activity-key)))))

(defmethod dispatcher/action :mark-unread
  [db [_ org-slug activity-data]]
  (let [board-uuid (:board-uuid activity-data)
        activity-uuid (:uuid activity-data)
        section-change-key (vec (concat (dispatcher/change-data-key org-slug) [board-uuid :unread]))
        activity-key (dispatcher/activity-key org-slug activity-uuid)
        next-activity-data (assoc (get-in db activity-key) :unread true)
        temp-val (get-in db section-change-key)
        activity-read-key (conj dispatcher/activities-read-key activity-uuid)]
    (-> db
      (update-in section-change-key #(vec (conj (or % []) activity-uuid)))
      (update-in activity-read-key merge {:last-read-at nil})
      (assoc-in activity-key next-activity-data))))

(defmethod dispatcher/action :mark-read
  [db [_ org-slug activity-data]]
  (let [board-uuid (:board-uuid activity-data)
        activity-uuid (:uuid activity-data)
        section-change-key (vec (concat (dispatcher/change-data-key org-slug) [board-uuid :unread]))
        activity-key (dispatcher/activity-key org-slug activity-uuid)
        next-activity-data (assoc (get-in db activity-key) :unread false)
        temp-val (get-in db section-change-key)
        activity-read-key (conj dispatcher/activities-read-key activity-uuid)]
    (-> db
      (update-in section-change-key (fn [unreads] (filterv #(not= % activity-uuid) (or unreads []))))
      (update-in activity-read-key merge {:last-read-at (utils/as-of-now)})
      (assoc-in activity-key next-activity-data))))