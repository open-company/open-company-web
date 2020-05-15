(ns oc.web.stores.activity
  (:require [cuerdas.core :as str]
            [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.notification :as notif-util]
            [oc.web.utils.org :as ou]
            [oc.web.utils.user :as uu]
            [oc.web.utils.activity :as au]))

(defn add-remove-item-from-all-posts
  "Given an activity map adds or remove it from the all-posts list of posts depending on the activity
   status"
  [db org-slug activity-data]
  (if (:uuid activity-data)
    (let [;; Add/remove item from AP
          is-published? (= (:status activity-data) "published")
          ap-rp-key (dispatcher/container-key org-slug :all-posts dispatcher/recently-posted-sort)
          ap-ra-key (dispatcher/container-key org-slug :all-posts dispatcher/recent-activity-sort)
          old-ap-rp-data (get-in db ap-rp-key)
          old-ap-ra-data (get-in db ap-ra-key)
          old-ap-rp-data-posts (get old-ap-rp-data :posts-list)
          old-ap-ra-data-posts (get old-ap-ra-data :posts-list)
          ap-rp-without-uuid (utils/vec-dissoc old-ap-rp-data-posts (:uuid activity-data))
          ap-ra-without-uuid (utils/vec-dissoc old-ap-ra-data-posts (:uuid activity-data))
          new-ap-rp-uuids (vec (if is-published?
                                 (conj ap-rp-without-uuid (:uuid activity-data))
                                 ap-rp-without-uuid))
          new-ap-ra-uuids (vec (if is-published?
                                 (conj ap-ra-without-uuid (:uuid activity-data))
                                 ap-ra-without-uuid))
          new-ap-rp-data-posts (mapv #(dispatcher/activity-data org-slug % db) new-ap-rp-uuids)
          new-ap-ra-data-posts (mapv #(dispatcher/activity-data org-slug % db) new-ap-ra-uuids)
          sorted-new-ap-rp-posts (reverse (sort-by :published-at new-ap-rp-data-posts))
          sorted-new-ap-ra-posts (reverse (sort-by (juxt :new-at :published-at) new-ap-ra-data-posts))
          sorted-new-ap-rp-uuids (mapv :uuid sorted-new-ap-rp-posts)
          sorted-new-ap-ra-uuids (mapv :uuid sorted-new-ap-ra-posts)
          grouped-ap-rp-uuids (if (au/show-separators? :all-posts dispatcher/recently-posted-sort)
                                (au/grouped-posts (assoc old-ap-rp-data :posts-list sorted-new-ap-rp-uuids))
                                sorted-new-ap-rp-uuids)
          grouped-ap-ra-uuids (if (au/show-separators? :all-posts dispatcher/recent-activity-sort)
                                (au/grouped-posts (assoc old-ap-ra-data :posts-list sorted-new-ap-ra-uuids))
                                sorted-new-ap-ra-uuids)
          next-ap-rp-data (merge old-ap-rp-data {:posts-list sorted-new-ap-rp-uuids
                                                 :items-to-render grouped-ap-rp-uuids})
          next-ap-ra-data (merge old-ap-ra-data {:posts-list sorted-new-ap-ra-uuids
                                                 :items-to-render grouped-ap-ra-uuids})]
      (-> db
       (assoc-in ap-rp-key next-ap-rp-data)
       (assoc-in ap-ra-key next-ap-ra-data)))
    db))

(defn add-remove-item-from-board
  "Given an activity map adds or remove it from it's board's list of posts depending on the activity status"
  [db org-slug activity-data]
  (if (:uuid activity-data)
    (let [;; Add/remove item from AP
          is-published? (= (:status activity-data) "published")
          board-data-key (dispatcher/board-data-key org-slug (:board-slug activity-data))
          old-board-data (get-in db board-data-key)
          old-board-data-posts (get old-board-data :posts-list)
          board-without-uuid (utils/vec-dissoc old-board-data-posts (:uuid activity-data))
          new-board-uuids (vec (if is-published?
                                 (conj board-without-uuid (:uuid activity-data))
                                 board-without-uuid))
          new-board-data-posts (mapv #(dispatcher/activity-data org-slug % db) new-board-uuids)
          sorted-new-board-posts (reverse (sort-by :published-at new-board-data-posts))
          sorted-new-board-uuids (mapv :uuid sorted-new-board-posts)
          grouped-board-uuids (if (au/show-separators? (:board-slug activity-data))
                                (au/grouped-posts (assoc old-board-data :posts-list sorted-new-board-uuids))
                                sorted-new-board-uuids)
          next-board-data (merge old-board-data {:posts-list sorted-new-board-uuids
                                                 :items-to-render grouped-board-uuids})]
      (assoc-in db board-data-key next-board-data))
    db))

(defn add-remove-item-from-bookmarks
  "Given an activity map adds or remove it from the bookmarks list of posts."
  [db org-slug activity-data]
  (if (:uuid activity-data)
    (let [;; Add/remove item from MS
          is-bookmark? (and (not= (:status activity-data) "draft")
                            (:bookmarked-at activity-data))
          bm-key (dispatcher/container-key org-slug :bookmarks)
          old-bm-data (get-in db bm-key)
          old-bm-data-posts (get old-bm-data :posts-list)
          bm-without-uuid (utils/vec-dissoc old-bm-data-posts (:uuid activity-data))
          new-bm-uuids (vec
                        (if is-bookmark?
                          (conj bm-without-uuid (:uuid activity-data))
                          bm-without-uuid))
          new-bm-data-posts (mapv #(dispatcher/activity-data org-slug % db) new-bm-uuids)
          sorted-new-bm-posts (reverse (sort-by :bookmarked-at new-bm-data-posts))
          sorted-new-bm-uuids (mapv :uuid sorted-new-bm-posts)
          grouped-bm-uuids (if (au/show-separators? "bookmarks")
                             (au/grouped-posts (assoc old-bm-data :posts-list sorted-new-bm-uuids))
                             sorted-new-bm-uuids)
          next-bm-data (merge old-bm-data {:posts-list sorted-new-bm-uuids
                                           :items-to-render grouped-bm-uuids})]
      (assoc-in db bm-key next-bm-data))
    db))

(defn add-remove-item-from-follow
  "Given an activity map adds or remove it from the bookmarks list of posts."
  [db org-slug activity-data following-container?]
  (if (:uuid activity-data)
    (let [;; Add/remove item from MS
          follow-list (dispatcher/follow-list org-slug db)
          publisher-id (-> activity-data :publisher :user-id)
          include-activity? (and (not= (:status activity-data) "draft")
                                 (or (and following-container?
                                          (or ((set (:publisher-uuids follow-list)) publisher-id)
                                              ((set (:board-uuids follow-list)) (:board-uuid activity-data))
                                              (= publisher-id (j/user-id))))
                                     (and (not following-container?)
                                          (not ((set (:publisher-uuids follow-list)) publisher-id))
                                          (not ((set (:board-uuids follow-list)) (:board-uuids activity-data)))
                                          (not= publisher-id (j/user-id)))))
          container-slug (if following-container? :following :unfollowing)
          fl-rp-key (dispatcher/container-key org-slug container-slug dispatcher/recently-posted-sort)
          fl-ra-key (dispatcher/container-key org-slug container-slug dispatcher/recent-activity-sort)
          old-fl-rp-data (get-in db fl-rp-key)
          old-fl-ra-data (get-in db fl-ra-key)
          old-fl-rp-data-posts (get old-fl-rp-data :posts-list)
          old-fl-ra-data-posts (get old-fl-ra-data :posts-list)
          fl-rp-without-uuid (utils/vec-dissoc old-fl-rp-data-posts (:uuid activity-data))
          fl-ra-without-uuid (utils/vec-dissoc old-fl-ra-data-posts (:uuid activity-data))
          new-fl-rp-uuids (vec (if (= following-container? include-activity?)
                                 (conj fl-rp-without-uuid (:uuid activity-data))
                                 fl-rp-without-uuid))
          new-fl-ra-uuids (vec (if (= following-container? include-activity?)
                                 (conj fl-ra-without-uuid (:uuid activity-data))
                                 fl-ra-without-uuid))
          new-fl-rp-data-posts (mapv #(dispatcher/activity-data org-slug % db) new-fl-rp-uuids)
          new-fl-ra-data-posts (mapv #(dispatcher/activity-data org-slug % db) new-fl-ra-uuids)
          sorted-new-fl-rp-posts (reverse (sort-by :published-at new-fl-rp-data-posts))
          sorted-new-fl-ra-posts (reverse (sort-by (juxt :new-at :published-at) new-fl-ra-data-posts))
          sorted-new-fl-rp-uuids (mapv :uuid sorted-new-fl-rp-posts)
          sorted-new-fl-ra-uuids (mapv :uuid sorted-new-fl-ra-posts)
          grouped-fl-rp-uuids (if (au/show-separators? container-slug dispatcher/recently-posted-sort)
                                (au/grouped-posts (assoc old-fl-rp-data :posts-list sorted-new-fl-rp-uuids))
                                sorted-new-fl-rp-uuids)
          grouped-fl-ra-uuids (if (au/show-separators? container-slug dispatcher/recent-activity-sort)
                                (au/grouped-posts (assoc old-fl-ra-data :posts-list sorted-new-fl-ra-uuids))
                                sorted-new-fl-ra-uuids)
          next-fl-rp-data (merge old-fl-rp-data {:posts-list sorted-new-fl-rp-uuids
                                                 :items-to-render grouped-fl-rp-uuids})
          next-fl-ra-data (merge old-fl-ra-data {:posts-list sorted-new-fl-ra-uuids
                                                 :items-to-render grouped-fl-ra-uuids})]
      (-> db
       (assoc-in fl-rp-key next-fl-rp-data)
       (assoc-in fl-ra-key next-fl-ra-data)))
    db))

(defn add-remove-item-from-contributions
  "Given an activity map adds or remove it from it's contributions' list of posts depending on the activity status"
  [db org-slug activity-data]
  (let [data-key (dispatcher/contributions-data-key org-slug (-> activity-data :publisher :user-id))]
    (if (and (:uuid activity-data)
             (= (:status activity-data) "published")
             (contains? (get db (butlast data-key)) (last data-key)))
      (let [;; Add/remove item from AP
            publisher (:publisher activity-data)
            data-key (dispatcher/contributions-data-key org-slug (:user-id publisher))
            old-data (get-in db data-key)
            old-data-posts (get old-data :posts-list)
            without-uuid (utils/vec-dissoc old-data-posts (:uuid activity-data))
            new-uuids (vec (conj without-uuid (:uuid activity-data)))
            new-data-posts (map #(dispatcher/activity-data org-slug % db) new-uuids)
            sorted-new-posts (reverse (sort-by :published-at new-data-posts))
            sorted-new-uuids (mapv :uuid sorted-new-posts)
            grouped-uuids (if (au/show-separators? (:board-slug activity-data))
                            (au/grouped-posts (assoc old-data :posts-list sorted-new-uuids))
                            sorted-new-uuids)
            next-data (merge old-data {:posts-list sorted-new-uuids
                                       :items-to-render grouped-uuids})]
        (assoc-in db data-key next-data))
      db)))

(defmethod dispatcher/action :entry-edit/dismiss
  [db [_]]
  (-> db
    (dissoc :entry-editing)
    (assoc :entry-edit-dissmissing true)))

(defmethod dispatcher/action :entry-toggle-save-on-exit
  [db [_ enabled?]]
  (assoc db :entry-save-on-exit enabled?))

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
  [db [_ new-board-data edit-key]]
  (let [org-slug (utils/section-org-slug new-board-data)
        org-data-key (dispatcher/org-data-key org-slug)
        contributions-count-key (vec (conj org-data-key :contributions-count))
        board-slug (:slug new-board-data)
        posts-key (dispatcher/posts-data-key org-slug)
        board-key (dispatcher/board-data-key org-slug board-slug)
        fixed-board-data (au/fix-board new-board-data (dispatcher/change-data db))
        merged-items (merge (get-in db posts-key)
                            (:fixed-items fixed-board-data))]
    (-> db
      (update-in contributions-count-key inc)
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
        org-data-key (dispatcher/org-data-key org-slug)
        contributions-count-key (vec (conj org-data-key :contributions-count))
        board-data (au/board-by-uuid (:board-uuid activity-data))
        fixed-activity-data (au/fix-entry activity-data board-data (dispatcher/change-data db))
        with-published-at (update fixed-activity-data :published-at #(if (seq %) % (utils/as-of-now)))]
    (-> db
      (update-in contributions-count-key inc)
      (assoc-in (dispatcher/activity-key org-slug (:uuid activity-data)) with-published-at)
      (add-remove-item-from-all-posts org-slug with-published-at)
      (add-remove-item-from-bookmarks org-slug with-published-at)
      (add-remove-item-from-follow org-slug with-published-at true)
      (add-remove-item-from-follow org-slug with-published-at false)
      (add-remove-item-from-board org-slug with-published-at)
      (add-remove-item-from-contributions org-slug with-published-at)
      (assoc-in dispatcher/force-list-update-key (utils/activity-uuid))
      (update-in [edit-key] dissoc :publishing)
      (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :entry-publish/failed
  [db [_ edit-key]]
  (-> db
    (update-in [edit-key] dissoc :publishing)
    (update-in [edit-key] assoc :error true)))

(defmethod dispatcher/action :activity-delete
  [db [_ org-slug activity-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        contributions-count-key (vec (conj org-data-key :contributions-count))
        posts-key (dispatcher/posts-data-key org-slug)
        posts-data (dispatcher/posts-data)
        next-posts (dissoc posts-data (:uuid activity-data))
        ;; Remove the post from all the containers posts list
        containers-key (dispatcher/containers-key org-slug)
        with-fixed-containers (reduce
                               (fn [ndb ckey]
                                 (let [container-rp-key (dispatcher/container-key org-slug ckey dispatcher/recently-posted-sort)
                                       container-ra-key (dispatcher/container-key org-slug ckey dispatcher/recent-activity-sort)
                                       next-ndb (-> ndb
                                                 (update-in (conj container-rp-key :posts-list)
                                                  (fn [posts-list]
                                                    (filterv #(not= % (:uuid activity-data)) posts-list)))
                                                 (update-in (conj container-ra-key :posts-list)
                                                  (fn [posts-list]
                                                    (filterv #(not= % (:uuid activity-data)) posts-list))))
                                       items-to-render-rp-key (conj container-rp-key :items-to-render)
                                       items-to-render-ra-key (conj container-ra-key :items-to-render)]
                                    (-> next-ndb
                                     (assoc-in items-to-render-rp-key
                                      (if (au/show-separators? ckey dispatcher/recently-posted-sort)
                                        (au/grouped-posts (get-in next-ndb container-rp-key))
                                        (get-in next-ndb (conj container-rp-key :posts-list))))
                                     (assoc-in items-to-render-rp-key
                                      (if (au/show-separators? ckey dispatcher/recent-activity-sort)
                                        (au/grouped-posts (get-in next-ndb container-ra-key))
                                        (get-in next-ndb (conj container-ra-key :posts-list)))))))
                               db
                               (keys (get-in db containers-key)))
        ;; Remove the post from contributors lists
        contributions-list-key (dispatcher/contributions-list-key org-slug)
        with-fixed-contribs (reduce
                             (fn [ndb ckey]
                               (let [base-contributions-key (dispatcher/contributions-key org-slug ckey)
                                     next-ndb (update-in ndb (conj base-contributions-key :posts-list)
                                               (fn [posts-list]
                                                 (filterv #(not= % (:uuid activity-data)) posts-list)))
                                     items-to-render-key (conj base-contributions-key :items-to-render)]
                                  (assoc-in next-ndb items-to-render-key
                                   (if (au/show-separators? ckey)
                                     (au/grouped-posts (get-in next-ndb base-contributions-key))
                                     (get-in next-ndb (conj base-contributions-key :posts-list))))))
                             db
                             (keys (get-in with-fixed-containers contributions-list-key)))
        ;; Remove the post from all the boards posts list too
        boards-key (dispatcher/boards-key org-slug)
        with-fixed-boards (reduce
                           (fn [ndb ckey]
                             (let [base-board-key (dispatcher/board-data-key org-slug ckey)
                                   next-ndb (update-in ndb (conj base-board-key :posts-list)
                                             (fn [posts-list]
                                               (filterv #(not= % (:uuid activity-data)) posts-list)))
                                   items-to-render-key (conj base-board-key :items-to-render)]
                                (assoc-in next-ndb items-to-render-key
                                 (if (au/show-separators? ckey)
                                   (au/grouped-posts (get-in next-ndb base-board-key))
                                   (get-in next-ndb (conj base-board-key :posts-list))))))
                           with-fixed-contribs
                           (keys (get-in db boards-key)))]
    ;; Now if the post is the one being edited in cmail let's remove it from there too
    (if (= (get-in db [:cmail-data :uuid]) (:uuid activity-data))
      (-> with-fixed-boards
          (update-in contributions-count-key dec)
          (assoc-in [:cmail-data] {:delete true})
          (assoc-in posts-key next-posts))
      (assoc-in with-fixed-boards posts-key next-posts))))

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
                             (dispatcher/change-data db))
        update-cmail? (and (= (get-in db [:cmail-data :uuid]) activity-uuid)
                           (pos? (compare (:updated-at fixed-activity-data)
                                          (get-in db [:cmail-data :updated-at]))))]
    (cond-> db
     update-cmail? (update-in [:cmail-data] #(merge % fixed-activity-data))
     update-cmail? (update :cmail-state assoc :key (utils/activity-uuid))
     true          (assoc-in activity-key fixed-activity-data)
     true          (as-> ndb
                    (update-in ndb (dispatcher/user-notifications-key org-slug)
                     #(notif-util/fix-notifications ndb %))))))

(defmethod dispatcher/action :bookmark-toggle
  [db [_ org-slug activity-uuid bookmark?]]
  (let [bookmarks-count-key (conj (dispatcher/org-data-key org-slug) :bookmarks-count)
        current-bookmarks-count (get-in db bookmarks-count-key)
        activity-key (dispatcher/activity-key org-slug activity-uuid)
        activity-data (get-in db activity-key)
        bookmark-link-index (when activity-data
                              (utils/index-of (:links activity-data) #(= (:rel %) "bookmark")))
        next-activity-data* (when activity-data
                             (if bookmark?
                              (update activity-data :bookmarked-at #(or % (utils/as-of-now)))
                              (dissoc activity-data :bookmarked-at)))
        next-activity-data (when (and activity-data
                                      bookmark-link-index)
                             (assoc-in next-activity-data* [:links bookmark-link-index :method]
                              (if bookmark? "DELETE" "POST")))
        next-db (if activity-data
                  (assoc-in db activity-key next-activity-data)
                  db)
        next-bookmarks-count (cond
                               (and bookmark?
                                    (not (:bookmarked-at activity-data)))
                               (inc current-bookmarks-count)
                               (and (not bookmark?)
                                    (:bookmarked-at activity-data))
                               (dec current-bookmarks-count)
                               :else
                               current-bookmarks-count)]
      (-> next-db
       (add-remove-item-from-bookmarks org-slug next-activity-data)
       (assoc-in bookmarks-count-key next-bookmarks-count))))

(defmethod dispatcher/action :entry-save-with-board/finish
  [db [_ org-slug fixed-board-data]]
  (let [board-key (dispatcher/board-data-key org-slug (:slug fixed-board-data))
        posts-key (dispatcher/posts-data-key org-slug)]
  (-> db
    (assoc-in board-key (dissoc fixed-board-data :fixed-items))
    (assoc-in posts-key (merge (get-in db posts-key) (get fixed-board-data :fixed-items)))
    (dissoc :section-editing)
    (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :all-posts-get/finish
  [db [_ org-slug sort-type all-posts-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        fixed-all-posts-data (au/fix-container (:collection all-posts-data) change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-all-posts-data))
        container-key (dispatcher/container-key org-slug :all-posts sort-type)]
    (as-> db ndb
     (assoc-in ndb container-key fixed-all-posts-data)
     (assoc-in ndb posts-key merged-items)
     (update-in ndb (dispatcher/user-notifications-key org-slug)
      #(notif-util/fix-notifications ndb %)))))

(defmethod dispatcher/action :all-posts-more
  [db [_ org-slug sort-type]]
  (let [container-key (dispatcher/container-key org-slug :all-posts sort-type)
        container-data (get-in db container-key)
        next-posts-data (assoc container-data :loading-more true)]
    (assoc-in db container-key next-posts-data)))

(defmethod dispatcher/action :all-posts-more/finish
  [db [_ org sort-type direction posts-data]]
  (if posts-data
    (let [org-data (dispatcher/org-data db org)
          container-key (dispatcher/container-key org :all-posts sort-type)
          container-data (get-in db container-key)
          posts-data-key (dispatcher/posts-data-key org)
          old-posts (get-in db posts-data-key)
          prepare-posts-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                              :old-links (:links container-data)})
          fixed-posts-data (au/fix-container prepare-posts-data (dispatcher/change-data db) org-data (dispatcher/active-users) sort-type direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more))]
      (as-> db ndb
       (assoc-in ndb container-key new-container-data)
       (assoc-in ndb posts-data-key new-items-map)
       (update-in ndb (dispatcher/user-notifications-key org)
        #(notif-util/fix-notifications ndb %))))
    db))

;; Bookmarks

(defmethod dispatcher/action :bookmarks-get/finish
  [db [_ org-slug sort-type bookmarks-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        fixed-bookmarks-data (au/fix-container (:collection bookmarks-data) change-data org-data active-users dispatcher/recently-posted-sort)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-bookmarks-data))
        container-key (dispatcher/container-key org-slug :bookmarks)]
    (as-> db ndb
      (assoc-in ndb container-key fixed-bookmarks-data)
      (assoc-in ndb posts-key merged-items)
      (update-in ndb (conj org-data-key :bookmarks-count) #(ou/disappearing-count-value % (:total-count fixed-bookmarks-data)))
      (update-in ndb (dispatcher/user-notifications-key org-slug)
       #(notif-util/fix-notifications ndb %)))))

(defmethod dispatcher/action :bookmarks-more
  [db [_ org-slug sort-type]]
  (let [container-key (dispatcher/container-key org-slug :bookmarks)
        container-data (get-in db container-key)
        next-posts-data (assoc container-data :loading-more true)
        bookmarks-count-key (vec (conj (dispatcher/org-data-key org-slug) :bookmarks-count))]
    (-> db
     (assoc-in container-key next-posts-data)
     (update-in bookmarks-count-key #(ou/disappearing-count-value % (:total-count next-posts-data))))))

(defmethod dispatcher/action :bookmarks-more/finish
  [db [_ org sort-type direction posts-data]]
  (if posts-data
    (let [org-data-key (dispatcher/org-data-key org)
          org-data (get-in db org-data-key)
          container-key (dispatcher/container-key org :bookmarks)
          container-data (get-in db container-key)
          posts-data-key (dispatcher/posts-data-key org)
          old-posts (get-in db posts-data-key)
          prepare-posts-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                              :old-links (:links container-data)})
          fixed-posts-data (au/fix-container prepare-posts-data (dispatcher/change-data db) org-data (dispatcher/active-users) dispatcher/recently-posted-sort direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (update-in ndb (conj org-data-key :bookmarks-count) #(ou/disappearing-count-value % (:total-count fixed-posts-data)))
        (update-in ndb (dispatcher/user-notifications-key org)
         #(notif-util/fix-notifications ndb %))))
    db))

(defmethod dispatcher/action :remove-bookmark
  [db [_ org-slug entry-data]]
  (let [activity-key (dispatcher/activity-key org-slug (:uuid entry-data))
        bookmarks-key (dispatcher/container-key org-slug :bookmarks)
        bookmarks-data (get-in db bookmarks-key)
        org-data-key (dispatcher/org-data-key org-slug)]
    (-> db
      (update-in (conj org-data-key :bookmarks-count) #(ou/disappearing-count-value % (dec %)))
      (assoc-in activity-key entry-data)
      (add-remove-item-from-bookmarks org-slug entry-data))))

(defmethod dispatcher/action :add-bookmark
  [db [_ org-slug activity-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)]
    (update-in db (conj org-data-key :bookmarks-count) #(ou/disappearing-count-value % (inc %)))))

(defmethod dispatcher/action :activities-count
  [db [_ org-slug items-count]]
  (let [old-reads-data (get-in db dispatcher/activities-read-key)
        ks (vec (map :item-id items-count))
        vs (map #(zipmap [:count :reads :item-id]
                         [(:count %)
                          (get-in old-reads-data [(:item-id %) :reads])
                          (:item-id %)]) items-count)
        new-items-count (zipmap ks vs)
        last-read-at-map (zipmap ks (map :last-read-at items-count))
        activities-key (dispatcher/posts-data-key org-slug)
        next-db (reduce
                 (fn [tdb [activity-uuid activity-last-read-at]]
                   (assoc-in tdb
                    (dispatcher/activity-last-read-at-key org-slug activity-uuid)
                    activity-last-read-at))
                 db
                 last-read-at-map)]
    (update-in next-db dispatcher/activities-read-key merge new-items-count)))

(defmethod dispatcher/action :activity-reads
  [db [_ org-slug item-id read-data-count read-data team-roster]]
  (let [activity-data   (dispatcher/activity-data org-slug item-id db)
        org-data        (dispatcher/org-data db org-slug)
        board-data      (first (filter #(= (:slug %) (:board-slug activity-data)) (:boards org-data)))
        fixed-read-data (vec (map #(assoc % :seen true) read-data))
        team-users      (uu/filter-active-users (:users team-roster))
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
        current-user-reads (filterv #(= (:user-id %) current-user-id) read-data)
        last-read-at     (:read-at (last (sort-by :read-at current-user-reads)))]
    (-> db
     (assoc-in (conj dispatcher/activities-read-key item-id) {:count read-data-count
                                                                :reads fixed-read-data
                                                                :item-id item-id
                                                                :unreads unseen-users
                                                                :private-access? private-access?})
     (assoc-in (dispatcher/activity-last-read-at-key org-slug item-id) last-read-at))))

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
        next-activity-data (-> db
                            (get-in activity-key)
                            (assoc :unread true)
                            (dissoc :last-read-at))
        activity-read-key (conj dispatcher/activities-read-key activity-uuid)]
    (-> db
      (update-in section-change-key #(vec (conj (or % []) activity-uuid)))
      (assoc-in activity-key next-activity-data))))

(defmethod dispatcher/action :mark-read
  [db [_ org-slug activity-data dismiss-at]]
  (let [board-uuid (:board-uuid activity-data)
        activity-uuid (:uuid activity-data)
        section-change-key (vec (concat (dispatcher/change-data-key org-slug) [board-uuid :unread]))
        all-comments-data (dispatcher/activity-comments-data org-slug activity-uuid db)
        comments-data (filterv #(not= (j/user-id) (-> % :author :user-id)) all-comments-data)
        activity-key (dispatcher/activity-key org-slug activity-uuid)
        old-activity-data (get-in db activity-key)
        ;; Update the activity to read and update the new-at with the max btw the current value
        ;; and the created-at of the last comment.
        next-activity-data (merge old-activity-data {:unread false
                                                     :last-read-at dismiss-at
                                                     :new-at (if (and (seq comments-data)
                                                                                (-> comments-data last :created-at
                                                                                 (compare (:new-at old-activity-data))
                                                                                 pos?))
                                                                         (-> comments-data last :created-at)
                                                                         (:new-at old-activity-data))})
        activity-read-key (conj dispatcher/activities-read-key activity-uuid)]
    (-> db
      (update-in section-change-key (fn [unreads] (filterv #(not= % activity-uuid) (or unreads []))))
      (assoc-in activity-key next-activity-data))))

;; Inbox

(defmethod dispatcher/action :inbox-get/finish
  [db [_ org-slug sort-type inbox-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        fixed-inbox-data (au/fix-container (:collection inbox-data) change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-inbox-data))
        container-key (dispatcher/container-key org-slug :inbox sort-type)]
    (as-> db ndb
      (assoc-in ndb container-key fixed-inbox-data)
      (assoc-in ndb posts-key merged-items)
      (assoc-in ndb (conj org-data-key :following-inbox-count) (:total-count fixed-inbox-data))
      (update-in ndb (dispatcher/user-notifications-key org-slug)
       #(notif-util/fix-notifications ndb %)))))

(defmethod dispatcher/action :inbox-more
  [db [_ org-slug sort-type]]
  (let [container-key (dispatcher/container-key org-slug :inbox sort-type)
        container-data (get-in db container-key)
        next-posts-data (assoc container-data :loading-more true)]
    (assoc-in db container-key next-posts-data)))

(defmethod dispatcher/action :inbox-more/finish
  [db [_ org sort-type direction posts-data]]
  (if posts-data
    (let [org-data-key (dispatcher/org-data-key org)
          org-data (get-in db org-data-key)
          container-key (dispatcher/container-key org :inbox sort-type)
          container-data (get-in db container-key)
          posts-data-key (dispatcher/posts-data-key org)
          old-posts (get-in db posts-data-key)
          prepare-posts-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                              :old-links (:links container-data)})
          fixed-posts-data (au/fix-container prepare-posts-data (dispatcher/change-data db) org-data (dispatcher/active-users) sort-type direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (assoc-in ndb (conj org-data-key :following-inbox-count) (:total-count fixed-posts-data))
        (update-in ndb (dispatcher/user-notifications-key org)
         #(notif-util/fix-notifications ndb %))))
    db))

(defmethod dispatcher/action :inbox/dismiss
  [db [_ org-slug item-id]]
  (if-let [activity-data (dispatcher/activity-data item-id)]
    (let [inbox-key (dispatcher/container-key org-slug "inbox")
          inbox-data (get-in db inbox-key)
          without-item (-> inbox-data
                         (update :posts-list (fn [posts-list] (filterv #(not= % item-id) posts-list)))
                         (update :items-to-render (fn [posts-list] (filterv #(not= % item-id) posts-list))))
          org-data-key (dispatcher/org-data-key org-slug)
          update-count? (not= (-> inbox-data :posts-list count) (-> without-item :posts-list count))]
      (-> db
        (assoc-in inbox-key without-item)
        (update-in (conj org-data-key :following-inbox-count) (if update-count? dec identity))))
    db))

(defmethod dispatcher/action :inbox/unread
  [db [_ org-slug current-board-slug item-id]]
  (if-let [activity-data (dispatcher/activity-data item-id)]
    (let [inbox-key (dispatcher/container-key org-slug "inbox")
          posts-list-key (conj inbox-key :posts-list)
          items-to-render-key (conj inbox-key :items-to-render)
          inbox-data (get-in db inbox-key)
          next-db (if inbox-data
                    (-> db
                     (update-in posts-list-key (fn [posts-list] (->> item-id (conj (set posts-list)) vec)))
                     (update-in items-to-render-key (fn [posts-list] (->> item-id (conj (set posts-list)) vec))))
                    db)
          activity-key (dispatcher/activity-key org-slug item-id)
          activity-data (get-in db activity-key)
          fixed-activity-data (update activity-data :links (fn [links]
                               (mapv (fn [link]
                                (if (= (:rel link) "follow")
                                  (merge link {:href (str/replace (:href link) #"/follow/?$" "/unfollow/")
                                               :rel "unfollow"})
                                  link))
                                 links)))
          org-data-key (dispatcher/org-data-key org-slug)
          update-count? (and inbox-data
                             (not= (count (get-in db posts-list-key)) (count (get-in next-db posts-list-key))))]
      (-> next-db
       (update-in (conj org-data-key :following-inbox-count) (if update-count? inc identity))
       (assoc-in activity-key fixed-activity-data)))
    db))

(defmethod dispatcher/action :inbox/dismiss-all
  [db [_ org-slug]]
  (let [inbox-key (dispatcher/container-key org-slug "inbox")
        inbox-data (get-in db inbox-key)
        without-items (-> inbox-data
                       (assoc-in [:posts-list] [])
                       (assoc-in [:items-to-render] []))
        org-data-key (dispatcher/org-data-key org-slug)]
    (-> db
      (assoc-in inbox-key without-items)
      (assoc-in (conj org-data-key :following-inbox-count) 0))))

;; Following

(defmethod dispatcher/action :following-get/finish
  [db [_ org-slug sort-type following-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        fixed-following-data (au/fix-container (:collection following-data) change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-following-data))
        container-key (dispatcher/container-key org-slug :following sort-type)]
    (as-> db ndb
      (assoc-in ndb container-key fixed-following-data)
      (assoc-in ndb posts-key merged-items)
      (assoc-in ndb (conj org-data-key :following-count) (:total-count fixed-following-data))
      (update-in ndb (dispatcher/user-notifications-key org-slug)
       #(notif-util/fix-notifications ndb %)))))

(defmethod dispatcher/action :following-more
  [db [_ org-slug sort-type]]
  (let [container-key (dispatcher/container-key org-slug :following sort-type)
        container-data (get-in db container-key)
        next-posts-data (assoc container-data :loading-more true)]
    (assoc-in db container-key next-posts-data)))

(defmethod dispatcher/action :following-more/finish
  [db [_ org sort-type direction posts-data]]
  (if posts-data
    (let [org-data-key (dispatcher/org-data-key org)
          org-data (get-in db org-data-key)
          container-key (dispatcher/container-key org :following sort-type)
          container-data (get-in db container-key)
          posts-data-key (dispatcher/posts-data-key org)
          old-posts (get-in db posts-data-key)
          prepare-posts-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                              :old-links (:links container-data)})
          fixed-posts-data (au/fix-container prepare-posts-data (dispatcher/change-data db) org-data (dispatcher/active-users) sort-type direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (assoc-in ndb (conj org-data-key :following-count) (:total-count fixed-posts-data))
        (update-in ndb (dispatcher/user-notifications-key org)
         #(notif-util/fix-notifications ndb %))))
    db))

;; Unfollowing

(defmethod dispatcher/action :unfollowing-get/finish
  [db [_ org-slug sort-type unfollowing-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        fixed-unfollowing-data (au/fix-container (:collection unfollowing-data) change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-unfollowing-data))
        container-key (dispatcher/container-key org-slug :unfollowing sort-type)]
    (as-> db ndb
      (assoc-in ndb container-key fixed-unfollowing-data)
      (assoc-in ndb posts-key merged-items)
      (update-in ndb (conj org-data-key :unfollowing-count) #(ou/disappearing-count-value % (:total-count fixed-unfollowing-data)))
      (update-in ndb (dispatcher/user-notifications-key org-slug)
       #(notif-util/fix-notifications ndb %)))))

(defmethod dispatcher/action :unfollowing-more
  [db [_ org-slug sort-type]]
  (let [container-key (dispatcher/container-key org-slug :unfollowing sort-type)
        container-data (get-in db container-key)
        next-posts-data (assoc container-data :loading-more true)]
    (assoc-in db container-key next-posts-data)))

(defmethod dispatcher/action :unfollowing-more/finish
  [db [_ org sort-type direction posts-data]]
  (if posts-data
    (let [org-data-key (dispatcher/org-data-key org)
          org-data (get-in db org-data-key)
          container-key (dispatcher/container-key org :unfollowing sort-type)
          container-data (get-in db container-key)
          posts-data-key (dispatcher/posts-data-key org)
          old-posts (get-in db posts-data-key)
          prepare-posts-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                              :old-links (:links container-data)})
          fixed-posts-data (au/fix-container prepare-posts-data (dispatcher/change-data db) org-data (dispatcher/active-users) sort-type direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (update-in ndb (conj org-data-key :unfollowing-count) #(ou/disappearing-count-value % (:total-count fixed-posts-data)))
        (update-in ndb (dispatcher/user-notifications-key org)
         #(notif-util/fix-notifications ndb %))))
    db))

(defmethod dispatcher/action :force-list-update
  [db [_]]
  (assoc-in db dispatcher/force-list-update-key (utils/activity-uuid)))