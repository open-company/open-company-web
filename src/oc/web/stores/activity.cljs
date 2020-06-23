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

(defn- force-list-update-value
  ([current-value] (force-list-update-value current-value nil))
  ([current-value container-slug]
   (if (or (nil? container-slug)
           (= container-slug (router/current-board-slug)))
     (utils/activity-uuid)
     current-value)))

(defn- item-from-entity [item]
  (select-keys item au/preserved-keys))

(defn- sort-value
  "Calculate the sort value as used on the server while quering the data.

   This is a limited sort-value in respect of what the server is using. Since this is applied
   only to posts the user publishes we can avoid all the unread and cap window related cals."
  [sort-type item]
  (update item :sort-value
   (fn [v]
    (if v
      v
      (let [activity-data (dispatcher/activity-data (:uuid item))
            sort-field (cond
                        (= sort-type dispatcher/recent-activity-sort)
                        (or (:last-activity-at activity-data) (:published-at activity-data) (:created-at activity-data))
                        (= sort-type :bookmarked-at)
                        (or (:bookmarked-at activity-data) (:published-at activity-data) (:created-at activity-data))
                        :else
                        (or (:published-at activity-data) (:created-at activity-data)))]
        (.getTime (utils/js-date sort-field)))))))

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
          ap-rp-without-uuid (filterv #(not= (:uuid %) (:uuid activity-data)) old-ap-rp-data-posts)
          ap-ra-without-uuid (filterv #(not= (:uuid %) (:uuid activity-data)) old-ap-ra-data-posts)
          new-ap-rp-list (vec (if is-published?
                                (conj ap-rp-without-uuid (item-from-entity activity-data))
                                ap-rp-without-uuid))
          new-ap-ra-list (vec (if is-published?
                                (conj ap-ra-without-uuid (item-from-entity activity-data))
                                ap-ra-without-uuid))
          new-ap-rp-with-sort-value (map (partial sort-value dispatcher/recently-posted-sort) new-ap-rp-list)
          new-ap-ra-with-sort-value (map (partial sort-value dispatcher/recent-activity-sort) new-ap-rp-list)
          sorted-new-ap-rp-uuids (reverse (sort-by :sort-value new-ap-rp-with-sort-value))
          sorted-new-ap-ra-uuids (reverse (sort-by :sort-value new-ap-ra-with-sort-value))
          next-ap-rp-data (assoc old-ap-rp-data :posts-list sorted-new-ap-rp-uuids)
          next-ap-ra-data (assoc old-ap-ra-data :posts-list sorted-new-ap-ra-uuids)
          org-data (dispatcher/org-data)
          active-users (dispatcher/active-users)
          parsed-ap-rp-data (au/parse-container next-ap-rp-data {} org-data active-users dispatcher/recently-posted-sort)
          parsed-ap-ra-data (au/parse-container next-ap-ra-data {} org-data active-users dispatcher/recent-activity-sort)]
      (-> db
       (assoc-in ap-rp-key (dissoc parsed-ap-rp-data :fixed-items))
       (assoc-in ap-ra-key (dissoc parsed-ap-ra-data :fixed-items))))
    db))

(defn add-remove-item-from-board
  "Given an activity map adds or remove it from it's board's list of posts depending on the activity status"
  [db org-slug activity-data]
  (if (:uuid activity-data)
    (let [;; Add/remove item from AP
          is-published? (= (:status activity-data) "published")
          rp-board-data-key (dispatcher/board-data-key org-slug (:board-slug activity-data) dispatcher/recently-posted-sort)
          ra-board-data-key (dispatcher/board-data-key org-slug (:board-slug activity-data) dispatcher/recent-activity-sort)
          rp-old-board-data (get-in db rp-board-data-key)
          ra-old-board-data (get-in db ra-board-data-key)
          rp-old-board-data-posts (get rp-old-board-data :posts-list)
          ra-old-board-data-posts (get ra-old-board-data :posts-list)
          rp-board-without-uuid (filterv #(= (:uuid %) (:uuid activity-data)) rp-old-board-data-posts)
          ra-board-without-uuid (filterv #(= (:uuid %) (:uuid activity-data)) ra-old-board-data-posts)
          new-rp-list (vec (if is-published?
                             (conj rp-board-without-uuid (item-from-entity activity-data))
                             rp-board-without-uuid))
          new-ra-list (vec (if is-published?
                             (conj ra-board-without-uuid (item-from-entity activity-data))
                             ra-board-without-uuid))
          new-rp-with-sort-value (map (partial sort-value dispatcher/recently-posted-sort) new-rp-list)
          new-ra-with-sort-value (map (partial sort-value dispatcher/recent-activity-sort) new-rp-list)
          sorted-new-rp-uuids (reverse (sort-by :sort-value new-rp-with-sort-value))
          sorted-new-ra-uuids (reverse (sort-by :sort-value new-ra-with-sort-value))
          parsed-rp-board-data (au/parse-board (assoc rp-old-board-data :posts-list sorted-new-rp-uuids dispatcher/recently-posted-sort))
          parsed-ra-board-data (au/parse-board (assoc ra-old-board-data :posts-list sorted-new-ra-uuids dispatcher/recent-activity-sort))]
      (-> db
       (assoc-in rp-board-data-key (dissoc parsed-rp-board-data :fixed-items))
       (assoc-in ra-board-data-key (dissoc parsed-ra-board-data :fixed-items))))
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
          bm-without-uuid (filterv #(not= (:uuid %) (:uuid activity-data)) old-bm-data-posts)
          new-bm-uuids (vec
                        (if is-bookmark?
                          (conj bm-without-uuid (item-from-entity activity-data))
                          bm-without-uuid))
          new-bm-with-sort-value (map (partial sort-value :bookmarked-at) new-bm-uuids)
          sorted-new-bm-posts (reverse (sort-by :sort-value new-bm-with-sort-value))
          next-bm-data (au/parse-container (assoc old-bm-data :posts-list sorted-new-bm-posts))]
      (assoc-in db bm-key (dissoc next-bm-data :fixed-items)))
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
          fl-rp-without-uuid (filterv #(not= (:uuid %) (:uuid activity-data)) old-fl-rp-data-posts)
          fl-ra-without-uuid (filterv #(not= (:uuid %) (:uuid activity-data)) old-fl-ra-data-posts)
          new-fl-rp-uuids (vec (if (= following-container? include-activity?)
                                 (conj fl-rp-without-uuid (item-from-entity activity-data))
                                 fl-rp-without-uuid))
          new-fl-ra-uuids (vec (if (= following-container? include-activity?)
                                 (conj fl-ra-without-uuid (item-from-entity activity-data))
                                 fl-ra-without-uuid))
          new-fl-rp-with-sort-value (map (partial sort-value dispatcher/recently-posted-sort) new-fl-rp-uuids)
          new-fl-ra-with-sort-value (map (partial sort-value dispatcher/recent-activity-sort) new-fl-ra-uuids)
          sorted-new-fl-rp-uuids (reverse (sort-by :sort-value new-fl-rp-with-sort-value))
          sorted-new-fl-ra-uuids (reverse (sort-by :sort-value new-fl-ra-with-sort-value))
          next-fl-rp-data (assoc old-fl-rp-data :posts-list sorted-new-fl-rp-uuids)
          next-fl-ra-data (assoc old-fl-ra-data :posts-list sorted-new-fl-ra-uuids)
          org-data (dispatcher/org-data)
          active-users (dispatcher/active-users)
          parsed-fl-rp-data (au/parse-container next-fl-rp-data {} org-data active-users dispatcher/recently-posted-sort)
          parsed-fl-ra-data (au/parse-container next-fl-ra-data {} org-data active-users dispatcher/recent-activity-sort)]
      (-> db
       (assoc-in fl-rp-key (dissoc parsed-fl-rp-data :fixed-items))
       (assoc-in fl-ra-key (dissoc parsed-fl-ra-data :fixed-items))))
    db))

(defn add-remove-item-from-contributions
  "Given an activity map adds or remove it from it's contributions' list of posts depending on the activity status"
  [db org-slug activity-data]
  (let [contributions-list-key (dispatcher/contributions-list-key org-slug)
        {{author-uuid :user-id} :publisher} activity-data]
    (if (and (:uuid activity-data)
             (= (:status activity-data) "published")
             (contains? (get db contributions-list-key) author-uuid))
      (let [;; Add/remove item from AP
            rp-contributions-data-key (dispatcher/contributions-data-key org-slug author-uuid dispatcher/recently-posted-sort)
            ra-contributions-data-key (dispatcher/contributions-data-key org-slug author-uuid dispatcher/recent-activity-sort)
            rp-old-data (get-in db rp-contributions-data-key)
            ra-old-data (get-in db ra-contributions-data-key)
            rp-old-data-posts (get rp-old-data :posts-list)
            ra-old-data-posts (get ra-old-data :posts-list)
            rp-without-uuid (filterv #(not= (:uuid %) (:uuid activity-data)) rp-old-data-posts)
            ra-without-uuid (filterv #(not= (:uuid %) (:uuid activity-data)) ra-old-data-posts)
            rp-new-uuids (vec (conj rp-without-uuid (item-from-entity activity-data)))
            ra-new-uuids (vec (conj ra-without-uuid (item-from-entity activity-data)))
            rp-with-sort-value (map (partial sort-value dispatcher/recently-posted-sort) rp-new-uuids)
            ra-with-sort-value (map (partial sort-value dispatcher/recent-activity-sort) ra-new-uuids)
            rp-sorted-new-uuids (reverse (sort-by :sort-value rp-with-sort-value))
            ra-sorted-new-uuids (reverse (sort-by :sort-value ra-with-sort-value))
            rp-new-posts-list (assoc rp-old-data :posts-list rp-sorted-new-uuids)
            ra-new-posts-list (assoc ra-old-data :posts-list ra-sorted-new-uuids)
            change-data (dispatcher/change-data db)
            org-data (dispatcher/org-data db org-slug)
            active-users (dispatcher/active-users org-slug db)
            follow-publishers-list (dispatcher/follow-publishers-list org-slug db)
            parsed-rp-data (au/parse-contributions rp-new-posts-list change-data org-data active-users follow-publishers-list dispatcher/recently-posted-sort)
            parsed-ra-data (au/parse-contributions ra-new-posts-list change-data org-data active-users follow-publishers-list dispatcher/recent-activity-sort)]
        (-> db
         (assoc-in rp-contributions-data-key (dissoc parsed-rp-data :fixed-items))
         (assoc-in ra-contributions-data-key (dissoc parsed-ra-data :fixed-items))))
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
        fixed-activity-data (au/parse-entry activity-data activity-board-data (dispatcher/change-data db))
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
        fixed-board-data (au/parse-board new-board-data (dispatcher/change-data db))
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
      (update-in dispatcher/force-list-update-key force-list-update-value)
      (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :entry-publish/finish
  [db [_ edit-key activity-data]]
  (let [org-slug (utils/post-org-slug activity-data)
        org-data-key (dispatcher/org-data-key org-slug)
        contributions-count-key (vec (conj org-data-key :contributions-count))
        board-data (au/board-by-uuid (:board-uuid activity-data))
        fixed-activity-data (au/parse-entry activity-data board-data (dispatcher/change-data db))
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
      (update-in dispatcher/force-list-update-key force-list-update-value)
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
                                                  (fn [items-list]
                                                    (filterv #(not= (:uuid %) (:uuid activity-data)) items-list)))
                                                 (update-in (conj container-ra-key :posts-list)
                                                  (fn [items-list]
                                                    (filterv #(not= (:uuid %) (:uuid activity-data)) items-list))))
                                       items-to-render-rp-key (conj container-rp-key :items-to-render)
                                       items-list-rp-key (conj container-rp-key :posts-list)
                                       items-to-render-ra-key (conj container-ra-key :items-to-render)
                                       items-list-ra-key (conj container-ra-key :posts-list)
                                       items-data-map (get-in next-ndb (dispatcher/posts-data-key org-slug))]
                                    (-> next-ndb
                                     (assoc-in items-to-render-rp-key
                                      (if (au/show-separators? ckey dispatcher/recently-posted-sort)
                                        (au/grouped-posts (get-in next-ndb items-list-rp-key) items-data-map)
                                        (get-in next-ndb items-list-rp-key)))
                                     (assoc-in items-to-render-rp-key
                                      (if (au/show-separators? ckey dispatcher/recent-activity-sort)
                                        (au/grouped-posts (get-in next-ndb items-list-ra-key) items-data-map)
                                        (get-in next-ndb items-list-ra-key))))))
                               db
                               (keys (get-in db containers-key)))
        ;; Remove the post from contributors lists
        contributions-list-key (dispatcher/contributions-list-key org-slug)
        with-fixed-contribs (reduce
                             (fn [ndb ckey]
                               (let [base-contributions-key (dispatcher/contributions-key org-slug ckey)
                                     posts-list-key (conj base-contributions-key :posts-list)
                                     next-ndb (update-in ndb posts-list-key
                                               (fn [posts-list]
                                                 (filterv #(not= (:uuid %) (:uuid activity-data)) posts-list)))
                                     items-to-render-key (conj base-contributions-key :items-to-render)
                                     posts-data-map (get-in next-ndb (dispatcher/posts-data-key org-slug))]
                                  (assoc-in next-ndb items-to-render-key
                                   (if (au/show-separators? ckey)
                                     (au/grouped-posts (get-in next-ndb posts-list-key) posts-data-map)
                                     (get-in next-ndb posts-list-key)))))
                             db
                             (keys (get-in with-fixed-containers contributions-list-key)))
        ;; Remove the post from all the boards posts list too
        boards-key (dispatcher/boards-key org-slug)
        with-fixed-boards (reduce
                           (fn [ndb ckey]
                             (let [base-board-key (dispatcher/board-data-key org-slug ckey)
                                   posts-list-key (conj base-board-key :posts-list)
                                   next-ndb (update-in ndb posts-list-key
                                             (fn [posts-list]
                                               (filterv #(not= % (:uuid activity-data)) posts-list)))
                                   items-to-render-key (conj base-board-key :items-to-render)
                                   posts-data-map (get-in next-ndb (dispatcher/posts-data-key org-slug))]
                                (assoc-in next-ndb items-to-render-key
                                 (if (au/show-separators? ckey)
                                   (au/grouped-posts (get-in next-ndb posts-list-key) posts-data-map)
                                   (get-in next-ndb posts-list-key)))))
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
        fixed-activity-data (au/parse-entry activity-data board-data change-data)
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
      (au/parse-entry shared-data (:board-slug shared-data) (dispatcher/change-data db))
      {:error true})))

(defmethod dispatcher/action :entry-revert [db [_ entry]]
  ;; do nothing for now
  db)

(defmethod dispatcher/action :activity-get
  [db [_ {:keys [org-slug board-slug board-uuid activity-uuid secure-uuid]}]]
  (let [activity-key (if secure-uuid
                       (dispatcher/secure-activity-key org-slug secure-uuid)
                       (dispatcher/activity-key org-slug activity-uuid))]
    (update-in db activity-key #(let [updated-activity-data {:loading true
                                                             :uuid activity-uuid
                                                             :board-slug board-slug
                                                             :board-uuid board-uuid}]
                                  (if (map? %)
                                    (merge % updated-activity-data)
                                    updated-activity-data)))))

(defmethod dispatcher/action :activity-get/not-found
  [db [_ org-slug activity-uuid secure-uuid]]
  (let [activity-key (if secure-uuid
                       (dispatcher/secure-activity-key org-slug secure-uuid)
                       (dispatcher/activity-key org-slug activity-uuid))]
    (assoc-in db activity-key :404)))

(defmethod dispatcher/action :activity-get/failed
  [db [_ org-slug activity-uuid secure-uuid]]
  (let [activity-key (if secure-uuid
                       (dispatcher/secure-activity-key org-slug secure-uuid)
                       (dispatcher/activity-key org-slug activity-uuid))
        old-activity-data (get-in db activity-key)
        failed-activity-data (if (map? old-activity-data)
                               (dissoc old-activity-data :loading)
                               old-activity-data)]
    (assoc-in db activity-key failed-activity-data)))

(defmethod dispatcher/action :activity-get/finish
  [db [_ status org-slug activity-data secure-uuid]]
  (let [activity-uuid (:uuid activity-data)
        board-data (au/board-by-uuid (:board-uuid activity-data))
        activity-key (if secure-uuid
                       (dispatcher/secure-activity-key org-slug secure-uuid)
                       (dispatcher/activity-key org-slug activity-uuid))
        fixed-activity-data (-> activity-data
                             (au/parse-entry board-data (dispatcher/change-data db))
                             (dissoc :loading))
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
    (update-in posts-key merge (get fixed-board-data :fixed-items))
    (update-in dispatcher/force-list-update-key force-list-update-value)
    (dissoc :section-editing)
    (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :all-posts-get/finish
  [db [_ org-slug sort-type all-posts-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        prepare-posts-data (-> all-posts-data :collection (assoc :container-slug :all-posts))
        fixed-all-posts-data (au/parse-container prepare-posts-data change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-all-posts-data))
        container-key (dispatcher/container-key org-slug :all-posts sort-type)]
    (as-> db ndb
     (assoc-in ndb container-key (dissoc fixed-all-posts-data :fixed-items))
     (assoc-in ndb posts-key merged-items)
     (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :all-posts))
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
                                                              :old-links (:links container-data)
                                                              :container-slug :all-posts})
          fixed-posts-data (au/parse-container prepare-posts-data (dispatcher/change-data db) org-data (dispatcher/active-users) sort-type direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
       (assoc-in ndb container-key new-container-data)
       (assoc-in ndb posts-data-key new-items-map)
       (assoc-in ndb dispatcher/force-list-update-key (utils/activity-uuid))
       (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :all-posts))
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
        prepare-posts-data (-> bookmarks-data :collection (assoc :container-slug :bookmarks))
        fixed-bookmarks-data (au/parse-container prepare-posts-data change-data org-data active-users dispatcher/recently-posted-sort)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-bookmarks-data))
        container-key (dispatcher/container-key org-slug :bookmarks)]
    (as-> db ndb
      (assoc-in ndb container-key (dissoc fixed-bookmarks-data :fixed-items))
      (assoc-in ndb posts-key merged-items)
      (update-in ndb (conj org-data-key :bookmarks-count) #(ou/disappearing-count-value % (:total-count fixed-bookmarks-data)))
      (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :bookmarks))
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
                                                              :old-links (:links container-data)
                                                              :container-slug :bookmarks})
          fixed-posts-data (au/parse-container prepare-posts-data (dispatcher/change-data db) org-data (dispatcher/active-users) dispatcher/recently-posted-sort direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (update-in ndb (conj org-data-key :bookmarks-count) #(ou/disappearing-count-value % (:total-count fixed-posts-data)))
        (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :bookmarks))
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
      (update-in dispatcher/force-list-update-key force-list-update-value)
      (add-remove-item-from-bookmarks org-slug entry-data))))

(defmethod dispatcher/action :add-bookmark
  [db [_ org-slug activity-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)]
    (-> db
     (update-in (conj org-data-key :bookmarks-count) #(ou/disappearing-count-value % (inc %)))
     (update-in dispatcher/force-list-update-key force-list-update-value))))

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
        roster-data     (or team-roster (dispatcher/team-roster (:team-id org-data) db))
        board-data      (first (filter #(= (:slug %) (:board-slug activity-data)) (:boards org-data)))
        fixed-read-data (vec (map #(assoc % :seen true) read-data))
        team-users      (uu/filter-active-users (:users roster-data))
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
        last-read-at     (:read-at (last (sort-by :read-at current-user-reads)))
        current-posts-uuids (map :uuid (dispatcher/filtered-posts-data db))
        ;; In case the reads we loaded are for one of the posts in the current stream
        ;; let's force a refresh of the list key
        should-force-list-update? ((set current-posts-uuids) item-id)]
    (-> db
     (assoc-in (conj dispatcher/activities-read-key item-id) {:count read-data-count
                                                              :reads fixed-read-data
                                                              :item-id item-id
                                                              :unreads unseen-users
                                                              :private-access? private-access?})
     (as-> tdb
      (if should-force-list-update?
        (update-in tdb dispatcher/force-list-update-key force-list-update-value)
        tdb))
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
        fixed-activity-data (au/parse-entry activity-data activity-board-data (dispatcher/change-data db))
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
            fixed-activity-data (au/parse-entry activity-data activity-board-data (dispatcher/change-data db))]
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
      (update-in dispatcher/force-list-update-key force-list-update-value)
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
        ;; Update the activity to read and update the last-activity-at with the max btw the current value
        ;; and the created-at of the last comment.
        next-activity-data (merge old-activity-data {:unread false
                                                     :last-read-at dismiss-at
                                                     :last-activity-at (if (and (seq comments-data)
                                                                                (-> comments-data last :created-at
                                                                                 (compare (:last-activity-at old-activity-data))
                                                                                 pos?))
                                                                         (-> comments-data last :created-at)
                                                                         (:last-activity-at old-activity-data))})
        activity-read-key (conj dispatcher/activities-read-key activity-uuid)]
    (-> db
      (update-in section-change-key (fn [unreads] (filterv #(not= % activity-uuid) (or unreads []))))
      (update-in dispatcher/force-list-update-key force-list-update-value)
      (assoc-in activity-key next-activity-data))))

;; Inbox

(defmethod dispatcher/action :inbox-get/finish
  [db [_ org-slug sort-type inbox-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        prepare-posts-data (-> inbox-data :collection (assoc :container-slug :inbox))
        fixed-inbox-data (au/parse-container prepare-posts-data change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-inbox-data))
        container-key (dispatcher/container-key org-slug :inbox sort-type)]
    (as-> db ndb
      (assoc-in ndb container-key (dissoc fixed-inbox-data :fixed-items))
      (assoc-in ndb posts-key merged-items)
      (assoc-in ndb (conj org-data-key :following-inbox-count) (:total-count fixed-inbox-data))
      (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :inbox))
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
                                                              :old-links (:links container-data)
                                                              :container-slug :inbox})
          fixed-posts-data (au/parse-container prepare-posts-data (dispatcher/change-data db) org-data (dispatcher/active-users) sort-type direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (assoc-in ndb (conj org-data-key :following-inbox-count) (:total-count fixed-posts-data))
        (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :inbox))
        (update-in ndb (dispatcher/user-notifications-key org)
         #(notif-util/fix-notifications ndb %))))
    db))

(defmethod dispatcher/action :inbox/dismiss
  [db [_ org-slug item-id]]
  (if-let [activity-data (dispatcher/activity-data item-id)]
    (let [inbox-key (dispatcher/container-key org-slug "inbox")
          inbox-data (get-in db inbox-key)
          without-item (-> inbox-data
                         (update :posts-list (fn [posts-list] (filterv #(not= (:uuid %) item-id) posts-list)))
                         (update :items-to-render (fn [posts-list] (filterv #(not= % item-id) posts-list))))
          org-data-key (dispatcher/org-data-key org-slug)
          update-count? (not= (-> inbox-data :posts-list count) (-> without-item :posts-list count))]
      (-> db
        (assoc-in inbox-key without-item)
        (assoc-in dispatcher/force-list-update-key (utils/activity-uuid))
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
                     (update-in posts-list-key (fn [posts-list] (->> activity-data item-from-entity (conj (set posts-list)) vec)))
                     (update-in items-to-render-key (fn [posts-list] (->> activity-data item-from-entity (conj (set posts-list)) vec))))
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
       (assoc-in dispatcher/force-list-update-key (utils/activity-uuid))
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
      (assoc-in dispatcher/force-list-update-key (utils/activity-uuid))
      (assoc-in (conj org-data-key :following-inbox-count) 0))))

;; Following

(defmethod dispatcher/action :following-get/finish
  [db [_ org-slug sort-type following-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        prepare-posts-data (-> following-data :collection (assoc :container-slug :following))
        fixed-following-data (au/parse-container prepare-posts-data change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-following-data))
        container-key (dispatcher/container-key org-slug :following sort-type)]
    (as-> db ndb
      (assoc-in ndb container-key (dissoc fixed-following-data :fixed-items))
      (assoc-in ndb posts-key merged-items)
      (assoc-in ndb (conj org-data-key :following-count) (:total-count fixed-following-data))
      (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :following))
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
                                                              :old-links (:links container-data)
                                                              :container-slug :following})
          fixed-posts-data (au/parse-container prepare-posts-data (dispatcher/change-data db) org-data (dispatcher/active-users) sort-type direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (assoc-in ndb (conj org-data-key :following-count) (:total-count fixed-posts-data))
        (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :following))
        (update-in ndb (dispatcher/user-notifications-key org)
         #(notif-util/fix-notifications ndb %))))
    db))

;; Replies

(defmethod dispatcher/action :replies-get/finish
  [db [_ org-slug sort-type replies-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        posts-data-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-data-key)
        prepare-replies-data (-> replies-data :collection (assoc :container-slug :replies))
        fixed-replies-data (au/parse-container prepare-replies-data change-data org-data active-users sort-type)
        merged-items (merge old-posts (:fixed-items fixed-replies-data))
        replies-container-key (dispatcher/container-key org-slug :replies sort-type)
        unread-replies-key (dispatcher/unread-replies-key org-slug)]
    (as-> db ndb
      (assoc-in ndb replies-container-key (dissoc fixed-replies-data :fixed-items))
      (assoc-in ndb unread-replies-key (some #(or (:unread %)
                                                  (pos? (:new-comments-count %)))
                                        (vals (:fixed-items fixed-replies-data))))
      (assoc-in ndb posts-data-key merged-items)
      (assoc-in ndb (conj org-data-key :replies-count) (:total-count fixed-replies-data))
      (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :replies))
      (update-in ndb (dispatcher/user-notifications-key org-slug)
       #(notif-util/fix-notifications ndb %)))))

(defmethod dispatcher/action :replies-more
  [db [_ org-slug sort-type]]
  (let [replies-container-key (dispatcher/container-key org-slug :replies sort-type)
        container-data (get-in db replies-container-key)
        next-replies-data (assoc container-data :loading-more true)]
    (assoc-in db replies-container-key next-replies-data)))

(defmethod dispatcher/action :replies-more/finish
  [db [_ org sort-type direction replies-data]]
  (if replies-data
    (let [org-data-key (dispatcher/org-data-key org)
          org-data (get-in db org-data-key)
          replies-container-key (dispatcher/container-key org :replies sort-type)
          container-data (get-in db replies-container-key)
          posts-data-key (dispatcher/posts-data-key org)
          old-posts (get-in db posts-data-key)
          prepare-replies-data (merge (:collection replies-data) {:posts-list (:posts-list container-data)
                                                                  :old-links (:links container-data)
                                                                  :container-slug :replies})
          fixed-replies-data (au/parse-container prepare-replies-data (dispatcher/change-data db) org-data (dispatcher/active-users) sort-type direction)
          new-posts-map (merge old-posts (:fixed-items fixed-replies-data))
          new-container-data (-> fixed-replies-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))
          unread-replies-key (dispatcher/unread-replies-key org)]
      (as-> db ndb
        (assoc-in ndb replies-container-key new-container-data)
        (assoc-in ndb unread-replies-key (some #(or (:unread %)
                                                    (pos? (:new-comments-count %)))
                                          (vals (:fixed-items fixed-replies-data))))
        (assoc-in ndb posts-data-key new-posts-map)
        (assoc-in ndb (conj org-data-key :replies-count) (:total-count fixed-replies-data))
        (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :replies))
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
        prepare-posts-data (-> unfollowing-data :collection (assoc :container-slug :unfollowing))
        fixed-unfollowing-data (au/parse-container prepare-posts-data change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-unfollowing-data))
        container-key (dispatcher/container-key org-slug :unfollowing sort-type)]
    (as-> db ndb
      (assoc-in ndb container-key (dissoc fixed-unfollowing-data :fixed-items))
      (assoc-in ndb posts-key merged-items)
      (update-in ndb (conj org-data-key :unfollowing-count) #(ou/disappearing-count-value % (:total-count fixed-unfollowing-data)))
      (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :unfollowing))
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
                                                              :old-links (:links container-data)
                                                              :container-slug :unfollowing})
          fixed-posts-data (au/parse-container prepare-posts-data (dispatcher/change-data db) org-data (dispatcher/active-users) sort-type direction)
          new-items-map (merge old-posts (:fixed-items fixed-posts-data))
          new-container-data (-> fixed-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (update-in ndb (conj org-data-key :unfollowing-count) #(ou/disappearing-count-value % (:total-count fixed-posts-data)))
        (update-in ndb dispatcher/force-list-update-key #(force-list-update-value % :unfollowing))
        (update-in ndb (dispatcher/user-notifications-key org)
         #(notif-util/fix-notifications ndb %))))
    db))

(defmethod dispatcher/action :force-list-update
  [db [_]]
  (assoc-in db dispatcher/force-list-update-key (utils/activity-uuid)))