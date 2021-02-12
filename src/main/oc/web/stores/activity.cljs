(ns oc.web.stores.activity
  (:require [cuerdas.core :as string]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.web.local-settings :as ls]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.org :as ou]
            [oc.web.stores.pin :as pins-store]
            [oc.web.utils.user :as uu]
            [oc.web.utils.activity :as au]
            [clojure.set :as set]))

(defn- item-from-entity [entry]
  (select-keys entry au/preserved-keys))

(defn- update-sort-value
  "Calculate the sort value as used on the server while quering the data.

   This is a limited sort-value in respect of what the server is using. Since this is applied
   only to posts the user publishes we can avoid all the unread and cap window related cals."
  ([sort-type item] (update-sort-value sort-type item nil))
  ([sort-type item container-id]
   (assoc item :sort-value (let [sort-key (cond (= sort-type dispatcher/recent-activity-sort)
                                                :last-activity-at
                                                (= sort-type :bookmarked-at)
                                                :bookmarked-at
                                                (and container-id
                                                     (contains? item :pinned-at))
                                                :pinned-at
                                                :else
                                                :published-at)
                                 activity-data (if (contains? item sort-key) item (dispatcher/activity-data (:uuid item)))
                                 sort-val (.getTime (utils/js-date (get activity-data sort-key)))]
                             (if (= sort-key :pinned-at)
                               (+ sort-val pins-store/pins-sort-pivot-ms)
                               sort-val)))))

(defn- add-remove-item-from-all-posts
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
          new-ap-rp-with-sort-value (map (partial update-sort-value dispatcher/recently-posted-sort) new-ap-rp-list)
          new-ap-ra-with-sort-value (map (partial update-sort-value dispatcher/recent-activity-sort) new-ap-ra-list)
          sorted-new-ap-rp-uuids (reverse (sort-by :sort-value new-ap-rp-with-sort-value))
          sorted-new-ap-ra-uuids (reverse (sort-by :sort-value new-ap-ra-with-sort-value))
          next-ap-rp-data (assoc old-ap-rp-data :posts-list sorted-new-ap-rp-uuids)
          next-ap-ra-data (assoc old-ap-ra-data :posts-list sorted-new-ap-ra-uuids)
          org-data (dispatcher/org-data db org-slug)
          active-users (dispatcher/active-users org-slug db)
          parsed-ap-rp-data (when old-ap-rp-data
                              (-> next-ap-rp-data
                                (au/parse-container {} org-data active-users dispatcher/recently-posted-sort)
                                (dissoc :fixed-items)))
          parsed-ap-ra-data (when old-ap-ra-data
                              (-> next-ap-ra-data
                                (au/parse-container {} org-data active-users dispatcher/recent-activity-sort)
                                (dissoc :fixed-items)))]
      (as-> db tdb
       (if old-ap-rp-data
         (assoc-in tdb ap-rp-key parsed-ap-rp-data)
         tdb)
       (if old-ap-ra-data
         (assoc-in tdb ap-ra-key parsed-ap-ra-data)
        tdb)))
    db))

(defn- add-remove-item-from-board
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
          new-rp-with-sort-value (map #(update-sort-value dispatcher/recently-posted-sort % (:board-uuid activity-data)) new-rp-list)
          new-ra-with-sort-value (map #(update-sort-value dispatcher/recent-activity-sort % (:board-uuid activity-data)) new-ra-list)
          sorted-new-rp-uuids (reverse (sort-by :sort-value new-rp-with-sort-value))
          sorted-new-ra-uuids (reverse (sort-by :sort-value new-ra-with-sort-value))
          change-data (dispatcher/change-data db org-slug)
          active-users (dispatcher/active-users org-slug db)
          follow-boards-list (dispatcher/follow-boards-list org-slug db)
          parsed-rp-board-data (when rp-old-board-data
                                 (-> rp-old-board-data
                                  (assoc :posts-list sorted-new-rp-uuids)
                                  (au/parse-board change-data active-users follow-boards-list dispatcher/recently-posted-sort)
                                  (dissoc :fixed-items)))
          parsed-ra-board-data (when ra-old-board-data
                                 (-> ra-old-board-data
                                  (assoc :posts-list sorted-new-ra-uuids)
                                  (au/parse-board change-data active-users follow-boards-list dispatcher/recent-activity-sort)
                                  (dissoc :fixed-items)))]
      (as-> db tdb
       (if rp-old-board-data
         (assoc-in tdb rp-board-data-key parsed-rp-board-data)
         tdb)
       (if ra-old-board-data
         (assoc-in tdb ra-board-data-key parsed-ra-board-data)
         tdb)))
    db))

(defn- add-remove-item-from-bookmarks
  "Given an activity map adds or remove it from the bookmarks list of posts."
  [db org-slug activity-data]
  (if (:uuid activity-data)
    (let [;; Add/remove item from MS
          is-bookmark? (and (not= (:status activity-data) "draft")
                            (:bookmarked-at activity-data))
          bm-key (dispatcher/container-key org-slug :bookmarks)
          old-bm-data (get-in db bm-key)
          old-bm-data-posts (get old-bm-data :posts-list)
          bm-without-item (filter #(not= (:uuid %) (:uuid activity-data)) old-bm-data-posts)
          new-bm-items (vec
                        (if is-bookmark?
                          (conj bm-without-item (item-from-entity activity-data))
                          bm-without-item))
          with-bookmarked-at (map #(as-> % item
                                    (assoc item :bookmarked-at (if (= (:uuid item) (:uuid activity-data))
                                                                 (:bookmarked-at activity-data)
                                                                 (-> item :uuid dispatcher/activity-data :bookmarked-at)))
                                    (update-sort-value :bookmarked-at item)
                                    (dissoc item :bookmarked-at))
                              new-bm-items)
          sorted-new-bm-posts (vec (reverse (sort-by :sort-value with-bookmarked-at)))
          org-data (dispatcher/org-data db org-slug)
          change-data (dispatcher/change-data db org-slug)
          active-users (dispatcher/active-users org-slug db)
          next-bm-data (when old-bm-data
                         (-> old-bm-data
                          (assoc :posts-list sorted-new-bm-posts)
                          (au/parse-container change-data org-data active-users dispatcher/recently-posted-sort)
                          (dissoc :fixed-items)))]
      (assoc-in db bm-key next-bm-data))
    db))

(defn- add-published-post-to-home [db org-slug activity-data]
  (let [fl-rp-key (dispatcher/container-key org-slug :following dispatcher/recently-posted-sort)
        fl-ra-key (dispatcher/container-key org-slug :following dispatcher/recent-activity-sort)
        old-fl-rp-data (get-in db fl-rp-key)
        old-fl-ra-data (get-in db fl-ra-key)
        posts-list-rp-key (conj fl-rp-key :posts-list)
        posts-list-ra-key (conj fl-ra-key :posts-list)
        activity-item (item-from-entity activity-data)
        rp-activity-item (when old-fl-rp-data
                           (as-> activity-item item
                                 (assoc item :container-seen-at (:last-seen-at old-fl-rp-data))
                                 (update-sort-value dispatcher/recently-posted-sort item ls/seen-home-container-id)
                                 (assoc item :unseen false)
                                 (assoc item :unread false)))
        ra-activity-item (when old-fl-ra-data
                           (as-> activity-item item
                                 (assoc item :container-seen-at (:last-seen-at old-fl-ra-data))
                                 (update-sort-value dispatcher/recently-posted-sort item ls/seen-home-container-id)
                                 (assoc item :unseen false)
                                 (assoc item :unread false)))
        sort-posts-list-fn (fn [item posts-list]
                             (->> [item]
                                  (concat posts-list)
                                  (sort-by :sort-value)
                                  (reverse)
                                  (vec)))
        change-data (dispatcher/change-data db org-slug)
        org-data (dispatcher/org-data db org-slug)
        active-users (dispatcher/active-users org-slug db)]
    (as-> db tdb
      (if rp-activity-item
        (update-in tdb posts-list-rp-key (partial sort-posts-list-fn rp-activity-item))
        tdb)
      (if ra-activity-item
        (update-in tdb posts-list-ra-key (partial sort-posts-list-fn ra-activity-item))
        tdb)
      (au/update-container tdb :following org-data change-data active-users))))

(defn- add-remove-item-from-follow
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
          new-fl-rp-with-sort-value (map (partial update-sort-value dispatcher/recently-posted-sort) new-fl-rp-uuids)
          new-fl-ra-with-sort-value (map (partial update-sort-value dispatcher/recent-activity-sort) new-fl-ra-uuids)
          sorted-new-fl-rp-uuids (reverse (sort-by :sort-value new-fl-rp-with-sort-value))
          sorted-new-fl-ra-uuids (reverse (sort-by :sort-value new-fl-ra-with-sort-value))
          next-fl-rp-data (assoc old-fl-rp-data :posts-list sorted-new-fl-rp-uuids)
          next-fl-ra-data (assoc old-fl-ra-data :posts-list sorted-new-fl-ra-uuids)
          org-data (dispatcher/org-data db org-slug)
          active-users (dispatcher/active-users org-slug db)
          parsed-fl-rp-data (when old-fl-rp-data
                              (-> next-fl-rp-data
                               (au/parse-container {} org-data active-users dispatcher/recently-posted-sort)
                               (dissoc :fixed-items)))
          parsed-fl-ra-data (when old-fl-ra-data
                              (-> next-fl-ra-data
                               (au/parse-container {} org-data active-users dispatcher/recent-activity-sort)
                               (dissoc :fixed-items)))]
      (as-> db tdb
       (if old-fl-rp-data
         (assoc-in tdb fl-rp-key parsed-fl-rp-data)
         tdb)
       (if old-fl-ra-data
         (assoc-in tdb fl-ra-key parsed-fl-ra-data)
         tdb)))
    db))

(defn- add-remove-item-from-contributions
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
            rp-with-sort-value (map (partial update-sort-value dispatcher/recently-posted-sort) rp-new-uuids)
            ra-with-sort-value (map (partial update-sort-value dispatcher/recent-activity-sort) ra-new-uuids)
            rp-sorted-new-uuids (reverse (sort-by :sort-value rp-with-sort-value))
            ra-sorted-new-uuids (reverse (sort-by :sort-value ra-with-sort-value))
            rp-new-posts-list (assoc rp-old-data :posts-list rp-sorted-new-uuids)
            ra-new-posts-list (assoc ra-old-data :posts-list ra-sorted-new-uuids)
            change-data (dispatcher/change-data db org-slug)
            org-data (dispatcher/org-data db org-slug)
            active-users (dispatcher/active-users org-slug db)
            follow-publishers-list (dispatcher/follow-publishers-list org-slug db)
            parsed-rp-data (when rp-old-data
                             (-> rp-new-posts-list
                              (au/parse-contributions change-data org-data active-users follow-publishers-list dispatcher/recently-posted-sort)
                              (dissoc :fixed-items)))
            parsed-ra-data (when ra-old-data
                             (-> ra-new-posts-list
                              (au/parse-contributions change-data org-data active-users follow-publishers-list dispatcher/recent-activity-sort)
                              (dissoc :fixed-items)))]
        (as-> db tdb
         (if rp-old-data
           (assoc-in tdb rp-contributions-data-key parsed-rp-data)
           tdb)
         (if ra-old-data
           (assoc-in tdb ra-contributions-data-key parsed-ra-data)
           tdb)))
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
  [db [_ _edit-key]]
  (dissoc db :entry-save-on-exit))

(defmethod dispatcher/action :entry-save
  [db [_ edit-key]]
  (assoc-in db [edit-key :loading] true))

(defmethod dispatcher/action :entry-save/finish
  [db [_ activity-data edit-key]]
  (let [org-slug (utils/post-org-slug activity-data)
        board-slug (:board-slug activity-data)
        change-data (dispatcher/change-data db org-slug)
        activity-key (dispatcher/activity-key org-slug (:uuid activity-data))
        activity-board-data (dispatcher/board-data db org-slug board-slug)
        fixed-activity-data (au/parse-entry activity-data activity-board-data change-data)]
    (as-> db ndb
      (assoc-in ndb activity-key fixed-activity-data)
      (if edit-key
        (update-in ndb [edit-key] dissoc :loading)
        ndb)
      (dissoc ndb :entry-toggle-save-on-exit)
      (dissoc ndb :section-editing))))

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
        fixed-board-data (au/parse-board new-board-data (dispatcher/change-data db org-slug))
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
  [db [_ org-slug edit-key activity-data]]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        contributions-count-key (vec (conj org-data-key :contributions-count))
        board-data (au/board-by-uuid (:board-uuid activity-data))
        fixed-activity-data (-> activity-data
                             (update :published-at #(if (seq %) % (utils/as-of-now)))
                             (au/parse-entry board-data (dispatcher/change-data db org-slug))
                             (assoc :unseen false)
                             (assoc :unread false))]
    (-> db
      (update-in contributions-count-key inc)
      (assoc-in (dispatcher/activity-key org-slug (:uuid activity-data)) fixed-activity-data)
      (add-remove-item-from-all-posts org-slug fixed-activity-data)
      (add-remove-item-from-bookmarks org-slug fixed-activity-data)
      (as-> ndb
       (if (:following board-data)
         (add-published-post-to-home ndb org-slug fixed-activity-data)
         ndb))
      (add-remove-item-from-follow org-slug fixed-activity-data false)
      (add-remove-item-from-board org-slug fixed-activity-data)
      (add-remove-item-from-contributions org-slug fixed-activity-data)
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
        change-data (dispatcher/change-data db org-slug)
        org-data (get-in db org-data-key)
        active-users (dispatcher/active-users org-slug db)
        follow-publishers-list (dispatcher/follow-publishers-list org-slug db)
        follow-boards-list (dispatcher/follow-boards-list org-slug db)
        with-fixed-containers (reduce
                               (fn [ndb ckey]
                                 (let [container-rp-key (dispatcher/container-key org-slug ckey dispatcher/recently-posted-sort)
                                       container-ra-key (dispatcher/container-key org-slug ckey dispatcher/recent-activity-sort)
                                       rp-data (get-in ndb container-rp-key)
                                       ra-data (get-in ndb container-ra-key)
                                       rp? (map? rp-data)
                                       ra? (map? ra-data)
                                       updated-rp-data (when rp?
                                                         (update rp-data :posts-list (fn [items-list]
                                                                                      (filterv #(not= (:uuid %) (:uuid activity-data)) items-list))))
                                       updated-ra-data (when ra?
                                                         (update ra-data :posts-list (fn [items-list]
                                                                                    (filterv #(not= (:uuid %) (:uuid activity-data)) items-list))))
                                       parsed-rp-data (au/parse-container updated-rp-data change-data org-data active-users dispatcher/recently-posted-sort)
                                       parsed-ra-data (au/parse-container updated-ra-data change-data org-data active-users dispatcher/recent-activity-sort)]
                                    (as-> ndb tndb
                                     (if rp?
                                       (assoc-in tndb container-rp-key (dissoc parsed-rp-data :fixed-items))
                                       tndb)
                                     (if ra?
                                       (assoc-in tndb container-ra-key (dissoc parsed-ra-data :fixed-items))
                                       tndb))))
                               db
                               (keys (get-in db containers-key)))
        ;; Remove the post from contributors lists
        contributions-list-key (dispatcher/contributions-list-key org-slug)
        with-fixed-contribs (reduce
                             (fn [ndb ckey]
                               (let [contrib-data-key (dispatcher/contributions-key org-slug ckey)
                                     contrib-data (get-in ndb contrib-data-key)
                                     updated-contrib-data (update contrib-data :posts-list (fn [posts-list]
                                                                                            (filterv #(not= (:uuid %) (:uuid activity-data)) posts-list)))
                                     parsed-contrib-data (au/parse-contributions updated-contrib-data change-data org-data active-users follow-publishers-list dispatcher/recently-posted-sort)]
                                  (assoc-in ndb contrib-data-key
                                   (dissoc parsed-contrib-data :fixed-items))))
                             with-fixed-containers
                             (keys (get-in db contributions-list-key)))
        ;; Remove the post from all the boards posts list too
        boards-key (dispatcher/boards-key org-slug)
        with-fixed-boards (reduce
                           (fn [ndb ckey]
                             (let [board-data-key (dispatcher/board-data-key org-slug ckey)
                                   board-data (get-in ndb board-data-key)
                                   updated-board-data (update board-data :posts-list (fn [posts-list]
                                                                                      (filterv #(not= (:uuid %) (:uuid activity-data)) posts-list)))
                                   parsed-board-data (au/parse-board updated-board-data change-data active-users follow-boards-list dispatcher/recently-posted-sort)]
                                (assoc-in ndb board-data-key (dissoc parsed-board-data :fixed-items))))
                           with-fixed-contribs
                           (keys (get-in db boards-key)))]
    ;; Now if the post is the one being edited in cmail let's remove it from there too
    (if (= (get-in db (conj dispatcher/cmail-data-key :uuid)) (:uuid activity-data))
      (-> with-fixed-boards
          (update-in contributions-count-key dec)
          (assoc-in dispatcher/cmail-data-key {:delete true})
          (assoc-in posts-key next-posts))
      (assoc-in with-fixed-boards posts-key next-posts))))

(defmethod dispatcher/action :activity-move
  [db [_ activity-data _org-slug _board-data]]
  (update db :foc-menu-open #(if (= % (:uuid activity-data)) nil %)))

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

(defmethod dispatcher/action :entry-revert [db [_ _entry]]
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
  [db [_ org-slug activity-data secure-uuid]]
  (let [activity-uuid (:uuid activity-data)
        board-data (au/board-by-uuid (:board-uuid activity-data))
        activity-key (if secure-uuid
                       (dispatcher/secure-activity-key org-slug secure-uuid)
                       (dispatcher/activity-key org-slug activity-uuid))
        fixed-activity-data (-> activity-data
                             (au/parse-entry board-data (dispatcher/change-data db org-slug))
                             (dissoc :loading))
        update-cmail? (and (= (get-in db (conj dispatcher/cmail-data-key :uuid)) activity-uuid)
                           (pos? (compare (:updated-at fixed-activity-data)
                                          (get-in db (conj dispatcher/cmail-data-key :updated-at)))))]
    (cond-> db
     update-cmail? (update-in dispatcher/cmail-data-key #(merge % fixed-activity-data))
     update-cmail? (update-in dispatcher/cmail-state-key assoc :key (utils/activity-uuid))
     true          (assoc-in activity-key fixed-activity-data))))

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
                              (update activity-data :bookmarked-at #(utils/as-of-now))
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
    (dissoc :section-editing)
    (dissoc :entry-toggle-save-on-exit))))

(defn- all-posts-get-finish
  [db org-slug sort-type all-posts-data]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        prepare-all-posts-data (-> all-posts-data :collection (assoc :container-slug :all-posts))
        fixed-all-posts-data (au/parse-container prepare-all-posts-data change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-all-posts-data))
        container-key (dispatcher/container-key org-slug :all-posts sort-type)]
    (as-> db ndb
     (assoc-in ndb container-key (dissoc fixed-all-posts-data :fixed-items))
     (assoc-in ndb posts-key merged-items))))

(defmethod dispatcher/action :all-posts-get/finish
  [db [_ org-slug sort-type all-posts-data]]
  (all-posts-get-finish db org-slug sort-type all-posts-data))

(defmethod dispatcher/action :all-posts-refresh/finish
  [db [_ org-slug sort-type all-posts-data]]
  (all-posts-get-finish db org-slug sort-type all-posts-data))

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
          prepare-all-posts-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                                  :old-links (:links container-data)
                                                                  :container-slug :all-posts})
          active-users (dispatcher/active-users org db)
          fixed-all-posts-data (au/parse-container prepare-all-posts-data (dispatcher/change-data db org) org-data active-users sort-type {:direction direction})
          new-items-map (merge old-posts (:fixed-items fixed-all-posts-data))
          new-container-data (-> fixed-all-posts-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
       (assoc-in ndb container-key new-container-data)
       (assoc-in ndb posts-data-key new-items-map)))
    db))

;; Bookmarks

(defn- bookmarks-get-finish [db org-slug _sort-type bookmarks-data]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        prepare-bookmarks-data (-> bookmarks-data :collection (assoc :container-slug :bookmarks))
        fixed-bookmarks-data (au/parse-container prepare-bookmarks-data change-data org-data active-users dispatcher/recently-posted-sort)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-bookmarks-data))
        container-key (dispatcher/container-key org-slug :bookmarks)]
    (as-> db ndb
      (assoc-in ndb container-key (dissoc fixed-bookmarks-data :fixed-items))
      (assoc-in ndb posts-key merged-items)
      (update-in ndb (conj org-data-key :bookmarks-count) #(ou/disappearing-count-value % (:total-count fixed-bookmarks-data))))))

(defmethod dispatcher/action :bookmarks-get/finish
  [db [_ org-slug sort-type bookmarks-data]]
  (bookmarks-get-finish db org-slug sort-type bookmarks-data))

(defmethod dispatcher/action :bookmarks-refresh/finish
  [db [_ org-slug sort-type bookmarks-data]]
  (bookmarks-get-finish db org-slug sort-type bookmarks-data))

(defmethod dispatcher/action :bookmarks-more
  [db [_ org-slug _sort-type]]
  (let [container-key (dispatcher/container-key org-slug :bookmarks)
        container-data (get-in db container-key)
        next-posts-data (assoc container-data :loading-more true)
        bookmarks-count-key (vec (conj (dispatcher/org-data-key org-slug) :bookmarks-count))]
    (-> db
     (assoc-in container-key next-posts-data)
     (update-in bookmarks-count-key #(ou/disappearing-count-value % (:total-count next-posts-data))))))

(defmethod dispatcher/action :bookmarks-more/finish
  [db [_ org _sort-type direction posts-data]]
  (if posts-data
    (let [org-data-key (dispatcher/org-data-key org)
          org-data (get-in db org-data-key)
          container-key (dispatcher/container-key org :bookmarks)
          container-data (get-in db container-key)
          posts-data-key (dispatcher/posts-data-key org)
          old-posts (get-in db posts-data-key)
          prepare-bookmarks-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                                  :old-links (:links container-data)
                                                                  :container-slug :bookmarks})
          active-users (dispatcher/active-users org db)
          fixed-bookmarks-data (au/parse-container prepare-bookmarks-data (dispatcher/change-data db org) org-data active-users dispatcher/recently-posted-sort {:direction direction})
          new-items-map (merge old-posts (:fixed-items fixed-bookmarks-data))
          new-container-data (-> fixed-bookmarks-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (update-in ndb (conj org-data-key :bookmarks-count) #(ou/disappearing-count-value % (:total-count fixed-bookmarks-data)))))
    db))

(defmethod dispatcher/action :remove-bookmark
  [db [_ org-slug entry-data]]
  (let [activity-key (dispatcher/activity-key org-slug (:uuid entry-data))
        org-data-key (dispatcher/org-data-key org-slug)]
    (-> db
      (update-in (conj org-data-key :bookmarks-count) #(ou/disappearing-count-value % (dec %)))
      (assoc-in activity-key entry-data)
      (add-remove-item-from-bookmarks org-slug entry-data))))

(defmethod dispatcher/action :add-bookmark
  [db [_ org-slug _activity-data]]
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
        last-read-at-map (zipmap ks (map :last-read-at items-count))]
    (as-> db tdb
     (reduce (fn [tdb [activity-uuid activity-last-read-at]]
               (let [entry-data (dispatcher/entry-data org-slug activity-uuid tdb)]
                 ;; Avoid setting the last-read-at if the entry is not loaded yet
                 (if (map? entry-data)
                   (assoc-in tdb (dispatcher/activity-last-read-at-key org-slug activity-uuid) activity-last-read-at)
                   tdb)))
      tdb
      last-read-at-map)
     (update-in tdb dispatcher/activities-read-key merge new-items-count))))

(defmethod dispatcher/action :activity-reads
  [db [_ org-slug item-id read-data-count read-data team-roster]]
  (let [activity-data   (dispatcher/activity-data org-slug item-id db)
        org-data        (dispatcher/org-data db org-slug)
        roster-data     (or team-roster (dispatcher/team-roster (:team-id org-data) db))
        board-data      (first (filter #(= (:slug %) (:board-slug activity-data)) (:boards org-data)))
        team-users      (uu/filter-active-users (:users roster-data))
        get-user-info   (fn [u]
                          (let [user-id (if (string? u)
                                          u
                                          (:user-id u))]
                            (some #(when (= (:user-id %) user-id) %) team-users)))
        fixed-read-data (vec (map #(merge (get-user-info %) % {:seen true}) read-data))
        seen-ids        (set (map :user-id read-data))
        private-access? (= (:access board-data) "private")
        all-private-users (when private-access?
                            (set (concat (:authors board-data) (:viewers board-data))))
        filtered-users  (if private-access?
                          (filterv #(all-private-users (:user-id %)) team-users)
                          team-users)
        all-ids         (set (map :user-id filtered-users))
        unseen-ids      (set/difference all-ids seen-ids)
        unseen-users    (set/difference unseen-ids)
        current-user-id (j/user-id)
        current-user-reads (filterv #(= (:user-id %) current-user-id) read-data)
        last-read-at     (:read-at (last (sort-by :read-at current-user-reads)))]
    (as-> db tdb
     (assoc-in tdb (conj dispatcher/activities-read-key item-id) {:count read-data-count
                                                                  :reads fixed-read-data
                                                                  :item-id item-id
                                                                  :unreads unseen-users
                                                                  :private-access? private-access?})
     (if (map? activity-data)
       (assoc-in tdb (dispatcher/activity-last-read-at-key org-slug item-id) last-read-at)
       tdb))))

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
        fixed-activity-data (au/parse-entry activity-data activity-board-data (dispatcher/change-data db org-slug))
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
            fixed-activity-data (au/parse-entry activity-data activity-board-data (dispatcher/change-data db org-slug))]
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
                            (dissoc :last-read-at))]
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
        ;; Update the activity to read and update the last-activity-at with the max btw the current value
        ;; and the created-at of the last comment.
        next-activity-data (merge old-activity-data {:unread false
                                                     :last-read-at dismiss-at
                                                     :last-activity-at (if (and (seq comments-data)
                                                                                (-> comments-data last :created-at
                                                                                 (compare (:last-activity-at old-activity-data))
                                                                                 pos?))
                                                                         (-> comments-data last :created-at)
                                                                         (:last-activity-at old-activity-data))})]
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
        prepare-inbox-data (-> inbox-data :collection (assoc :container-slug :inbox))
        fixed-inbox-data (au/parse-container prepare-inbox-data change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-inbox-data))
        container-key (dispatcher/container-key org-slug :inbox sort-type)]
    (as-> db ndb
      (assoc-in ndb container-key (dissoc fixed-inbox-data :fixed-items))
      (assoc-in ndb posts-key merged-items)
      (assoc-in ndb (conj org-data-key :following-inbox-count) (:total-count fixed-inbox-data)))))

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
          prepare-inbox-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                              :old-links (:links container-data)
                                                              :container-slug :inbox})
          active-users (dispatcher/active-users org db)
          fixed-inbox-data (au/parse-container prepare-inbox-data (dispatcher/change-data db org) org-data active-users sort-type {:direction direction})
          new-items-map (merge old-posts (:fixed-items fixed-inbox-data))
          new-container-data (-> fixed-inbox-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (assoc-in ndb (conj org-data-key :following-inbox-count) (:total-count fixed-inbox-data))))
    db))

(defmethod dispatcher/action :inbox/dismiss
  [db [_ org-slug item-id]]
  (if (dispatcher/activity-data org-slug item-id db)
    (let [inbox-key (dispatcher/container-key org-slug "inbox")
          inbox-data (get-in db inbox-key)
          without-item (-> inbox-data
                         (update :posts-list (fn [posts-list] (filterv #(not= (:uuid %) item-id) posts-list)))
                         (update :items-to-render (fn [posts-list] (filterv #(not= % item-id) posts-list))))
          org-data-key (dispatcher/org-data-key org-slug)
          update-count? (not= (-> inbox-data :posts-list count) (-> without-item :posts-list count))]
      (-> db
        (assoc-in inbox-key without-item)
        (update-in (conj org-data-key :following-inbox-count) (if update-count? dec identity))))
    db))

(defmethod dispatcher/action :inbox/unread
  [db [_ org-slug _current-board-slug item-id]]
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
                                  (merge link {:href (string/replace (:href link) #"/follow/?$" "/unfollow/")
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

(defn- following-get-finish
  [db org-slug sort-type current-container-slug keep-seen-at? following-data]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        container-key (dispatcher/container-key org-slug :following sort-type)
        old-container-data (get-in db container-key)
        prepare-following-data (-> following-data
                                :collection
                                (assoc :container-slug :following)
                                (update :last-seen-at #(if (and keep-seen-at? (map? old-container-data)) (:last-seen-at old-container-data) %)))
        fixed-following-data (au/parse-container prepare-following-data change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-following-data))
        following-badge-key (dispatcher/following-badge-key org-slug)
        badge-following? (some :unseen (:posts-list fixed-following-data))]
    (as-> db ndb
      (assoc-in ndb container-key (dissoc fixed-following-data :fixed-items))
      (update-in ndb following-badge-key #(if (= (keyword current-container-slug) :following) false (boolean badge-following?)))
      (assoc-in ndb posts-key merged-items)
      (assoc-in ndb (conj org-data-key :following-count) (:total-count fixed-following-data)))))

(defmethod dispatcher/action :following-get/finish
  [db [_ org-slug sort-type current-container-slug keep-seen-at? following-data]]
  (following-get-finish db org-slug sort-type current-container-slug keep-seen-at? following-data))

(defmethod dispatcher/action :following-refresh/finish
  [db [_ org-slug sort-type current-container-slug keep-seen-at? following-data]]
  (following-get-finish db org-slug sort-type current-container-slug keep-seen-at? following-data))

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
          prepare-following-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                                  :old-links (:links container-data)
                                                                  :container-slug :following
                                                                  :last-seen-at (:last-seen-at container-data)
                                                                  :next-seen-at (:next-seen-at container-data)})
          active-users (dispatcher/active-users org db)
          fixed-following-data (au/parse-container prepare-following-data (dispatcher/change-data db org) org-data active-users sort-type {:direction direction})
          new-items-map (merge old-posts (:fixed-items fixed-following-data))
          new-container-data (-> fixed-following-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (assoc-in ndb (conj org-data-key :following-count) (:total-count fixed-following-data))))
    db))

;; Replies

(defn- replies-get-finish
  [db org-slug sort-type current-container-slug keep-seen-at? replies-data]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        posts-data-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-data-key)
        replies-container-key (dispatcher/container-key org-slug :replies sort-type)
        old-container-data (get-in db replies-container-key)
        prepare-replies-data (-> replies-data
                              :collection
                              (assoc :container-slug :replies)
                              (update :last-seen-at #(if (and keep-seen-at? (map? old-container-data)) (:last-seen-at old-container-data) %)))
        fixed-replies-data (au/parse-container prepare-replies-data change-data org-data active-users sort-type)
        merged-items (merge old-posts (:fixed-items fixed-replies-data))
        replies-badge-key (dispatcher/replies-badge-key org-slug)
        badge-replies? (some (comp pos? :unseen-comments) (:posts-list fixed-replies-data))]
    (as-> db ndb
      (assoc-in ndb replies-container-key (dissoc fixed-replies-data :fixed-items))
      (update-in ndb replies-badge-key #(if (= (keyword current-container-slug) :replies) false (boolean badge-replies?)))
      (assoc-in ndb posts-data-key merged-items)
      (assoc-in ndb (conj org-data-key :replies-count) (:total-count fixed-replies-data)))))

(defmethod dispatcher/action :replies-get/finish
  [db [_ org-slug sort-type current-container-slug keep-seen-at? replies-data]]
  (replies-get-finish db org-slug sort-type current-container-slug keep-seen-at? replies-data))

(defmethod dispatcher/action :replies-refresh/finish
  [db [_ org-slug sort-type current-container-slug keep-seen-at? replies-data]]
  (replies-get-finish db org-slug sort-type current-container-slug keep-seen-at? replies-data))

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
                                                                  :container-slug :replies
                                                                  :last-seen-at (:last-seen-at container-data)
                                                                  :next-seen-at (:next-seen-at container-data)})
          active-users (dispatcher/active-users org db)
          fixed-replies-data (au/parse-container prepare-replies-data (dispatcher/change-data db org) org-data active-users sort-type {:direction direction})
          new-posts-map (merge old-posts (:fixed-items fixed-replies-data))
          new-container-data (-> fixed-replies-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
        (assoc-in ndb replies-container-key new-container-data)
        (assoc-in ndb posts-data-key new-posts-map)
        (assoc-in ndb (conj org-data-key :replies-count) (:total-count fixed-replies-data))))
    db))

;; Unfollowing

(defn- unfollowing-get-finish
  [db org-slug sort-type unfollowing-data]
  (let [org-data-key (dispatcher/org-data-key org-slug)
        org-data (get-in db org-data-key)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)
        prepare-unfollowing-data (-> unfollowing-data :collection (assoc :container-slug :unfollowing))
        fixed-unfollowing-data (au/parse-container prepare-unfollowing-data change-data org-data active-users sort-type)
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge old-posts (:fixed-items fixed-unfollowing-data))
        container-key (dispatcher/container-key org-slug :unfollowing sort-type)]
    (as-> db ndb
      (assoc-in ndb container-key (dissoc fixed-unfollowing-data :fixed-items))
      (assoc-in ndb posts-key merged-items)
      (update-in ndb (conj org-data-key :unfollowing-count) #(ou/disappearing-count-value % (:total-count fixed-unfollowing-data))))))

(defmethod dispatcher/action :unfollowing-get/finish
  [db [_ org-slug sort-type unfollowing-data]]
  (unfollowing-get-finish db org-slug sort-type unfollowing-data))

(defmethod dispatcher/action :unfollowing-refresh/finish
  [db [_ org-slug sort-type unfollowing-data]]
  (unfollowing-get-finish db org-slug sort-type unfollowing-data))

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
          prepare-unfollowing-data (merge (:collection posts-data) {:posts-list (:posts-list container-data)
                                                                    :old-links (:links container-data)
                                                                    :container-slug :unfollowing})
          active-users (dispatcher/active-users org db)
          fixed-unfollowing-data (au/parse-container prepare-unfollowing-data (dispatcher/change-data db org) org-data active-users sort-type {:direction direction})
          new-items-map (merge old-posts (:fixed-items fixed-unfollowing-data))
          new-container-data (-> fixed-unfollowing-data
                              (assoc :direction direction)
                              (dissoc :loading-more :fixed-items))]
      (as-> db ndb
        (assoc-in ndb container-key new-container-data)
        (assoc-in ndb posts-data-key new-items-map)
        (update-in ndb (conj org-data-key :unfollowing-count) #(ou/disappearing-count-value % (:total-count fixed-unfollowing-data)))))
    db))

(defmethod dispatcher/action :following-badge/on
  [db [_ org-slug]]
  (assoc-in db (dispatcher/following-badge-key org-slug) true))

(defmethod dispatcher/action :following-badge/off
  [db [_ org-slug]]
  (assoc-in db (dispatcher/following-badge-key org-slug) false))

(defmethod dispatcher/action :replies-badge/on
  [db [_ org-slug]]
  (assoc-in db (dispatcher/replies-badge-key org-slug) true))

(defmethod dispatcher/action :replies-badge/off
  [db [_ org-slug]]
  (assoc-in db (dispatcher/replies-badge-key org-slug) false))

(defmethod dispatcher/action :container-seen
  [db [_ _org-slug _container-id _seen-at]]
  ; (if org-slug
  ;   (assoc-in db (dispatcher/container-seen-key org-slug container-id) seen-at)
  ;   db)
  db)

(defmethod dispatcher/action :container-nav-in
  [db [_ org-slug container-slug _sort-type]]
  (cond
   (= container-slug :replies)
   (assoc-in db (dispatcher/replies-badge-key org-slug) false)
   (= container-slug :following)
   (assoc-in db (dispatcher/following-badge-key org-slug) false)
   :else
   db))

(defmethod dispatcher/action :container-nav-out
  [db [_ org-slug container-slug sort-type]]
  (let [container-key (dispatcher/container-key org-slug container-slug sort-type)
        container-data (get-in db container-key)
        org-data (dispatcher/org-data db org-slug)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)]
    (-> db
     (assoc-in (conj container-key :last-seen-at) (:next-seen-at container-data))
     (au/update-container container-slug org-data change-data active-users))))

(defmethod dispatcher/action :update-replies-comments
  [db [_ org-slug _current-board-slug]]
  (let [org-data (dispatcher/org-data db org-slug)
        change-data (dispatcher/change-data db org-slug)
        active-users (dispatcher/active-users org-slug db)]
    (au/update-replies-comments db org-data change-data active-users)))

(defmethod dispatcher/action :update-container
  [db [_ org-slug current-board-slug container-slug]]
  (let [org-data (dispatcher/org-data db org-slug)
        change-data (dispatcher/change-data db)
        active-users (dispatcher/active-users org-slug db)
        next-db (au/update-container db container-slug org-data change-data active-users)
        badge-following? (when (and (not= current-board-slug :following)
                                    (= container-slug :following))
                           (some :unseen (:posts-list (dispatcher/following-data org-slug next-db))))
        badge-replies? (when (and (not= (keyword current-board-slug) :replies)
                                  (= container-slug :replies))
                         (some :unseen-comments (:posts-list (dispatcher/replies-data org-slug next-db))))]
    (-> next-db
     (update-in (dispatcher/following-badge-key org-slug) #(boolean (or badge-following? %)))
     (update-in (dispatcher/replies-badge-key org-slug) #(boolean (or badge-replies? %))))))

(defmethod dispatcher/action :update-containers
  [db [_ org-slug]]
  (let [org-data (dispatcher/org-data db org-slug)
        change-data (dispatcher/change-data db)
        active-users (dispatcher/active-users org-slug db)]
    (au/update-containers db org-data change-data active-users)))

(defmethod dispatcher/action :foc-menu-open
  [db [_ val]]
  (if (not= (:foc-menu-open db) val)
    (assoc db :foc-menu-open val)
    db))