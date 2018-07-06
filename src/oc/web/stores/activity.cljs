(ns oc.web.stores.activity
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]))

(defn merge-items
  [old-items items]
  (let [merged-items (merge (:fixed-items old-items)
                            (:fixed-items items))]
    (assoc items :fixed-items merged-items)))

(defn posts-filter-fn
  "Find the filter based on the route"
  [posts-filter]
  (cond
   (= posts-filter "all-posts")
   (fn [p] (not= (:status p) "draft"))

   (= posts-filter "must-see")
   (fn [p] (and (= (:must-see p) true)
                (not= (:status p) "draft")))

   (= posts-filter "drafts")
   (fn [p] (= (:status p) "draft"))

   :default
   (fn [p] (and (= (:board-slug p) posts-filter)
                (not= (:status p) "draft")))))

(defmethod dispatcher/action :posts-filter
  [db [_ posts-filter]]
  (assoc db :posts-filter (posts-filter-fn posts-filter)))

(defmethod dispatcher/action :activity-modal-fade-in
  [db [_ activity-data editing dismiss-on-editing-end]]
  (if (get-in db [:search-active])
    db
    (-> db
      (assoc :activity-modal-fade-in (:uuid activity-data))
      (assoc :dismiss-modal-on-editing-stop (and editing dismiss-on-editing-end))
      ;; Make sure the seen-at is not reset when navigating to modal view
      (assoc :no-reset-seen-at true))))

(defmethod dispatcher/action :activity-modal-fade-out
  [db [_ board-slug]]
  (if (get-in db [:search-active])
    db
    (-> db
      (dissoc :activity-modal-fade-in)
      (dissoc :modal-editing)
      (dissoc :dismiss-modal-on-editing-stop))))

(defmethod dispatcher/action :entry-edit/dismiss
  [db [_]]
  (-> db
    (dissoc :entry-editing)
    (assoc :entry-edit-dissmissing true)))

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

(defmethod dispatcher/action :nux-end
  [db [_]]
  (dissoc db :nux))

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

(defmethod dispatcher/action :entry [db [_ board-key post-uuid body]]
  (let [board-data (get-in db board-key)
        posts-key (dispatcher/activity-key (:org-slug board-data) post-uuid)
        fixed-post (au/fix-entry body board-data (dispatcher/change-data db))]
    (assoc-in db posts-key fixed-post)))

(defmethod dispatcher/action :entry-clear-local-cache
  [db [_ edit-key]]
  (dissoc db :entry-save-on-exit))

(defmethod dispatcher/action :entry-save
  [db [_]]
  (assoc-in db [:entry-editing :loading] true))

(defmethod dispatcher/action :entry-save/finish
  [db [_ activity-data edit-key]]
  (let [org-slug (utils/post-org-slug activity-data)
        board-slug (:board-slug activity-data)
        activity-key (dispatcher/activity-key org-slug (:uuid activity-data))
        activity-board-data (get-in db (dispatcher/board-data-key org-slug board-slug))
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

(defmethod dispatcher/action :entry-publish [db [_]]
  (assoc-in db [:entry-editing :publishing] true))

(defmethod dispatcher/action :section-edit/error [db [_ error]]
  (assoc-in db [:section-editing :section-name-error] error))

(defmethod dispatcher/action :entry-publish-with-board/finish
  [db [_ new-board-data]]
  (let [org-slug (utils/section-org-slug new-board-data)
        board-slug (:slug new-board-data)
        posts-key (dispatcher/posts-data-key org-slug)
        board-key (dispatcher/board-data-key org-slug board-slug)
        fixed-board-data (au/fix-board new-board-data (dispatcher/change-data db))
        merged-items (merge (:fixed-items (get-in db posts-key))
                            (:fixed-items fixed-board-data))]
    (-> db
      (assoc-in board-key (dissoc fixed-board-data :fixed-items))
      (assoc-in posts-key {:fixed-items merged-items})
      (dissoc :section-editing)
      (update-in [:entry-editing] dissoc :publishing)
      (assoc-in [:entry-editing :board-slug] (:slug fixed-board-data))
      (assoc-in [:entry-editing :new-section] true)
      (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :entry-publish/finish
  [db [_ edit-key activity-data]]
  (let [org-slug (utils/post-org-slug activity-data)
        board-slug (:board-slug activity-data)
        board-key (dispatcher/board-data-key org-slug board-slug)
        board-data (get-in db board-key)
        fixed-activity-data (au/fix-entry activity-data board-data (dispatcher/change-data db))]
    (-> db
      (assoc-in (dispatcher/activity-key org-slug (:uuid activity-data)) fixed-activity-data)
      (update-in [edit-key] dissoc :publishing)
      (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :entry-publish/failed
  [db [_]]
  (-> db
    (update-in [:entry-editing] dissoc :publishing)
    (update-in [:entry-editing] assoc :error true)))

(defmethod dispatcher/action :activity-delete
  [db [_ org-slug activity-data]]
  (let [posts-key (dispatcher/posts-data-key org-slug)
        posts-data (dispatcher/posts-data org-slug)
        next-fixed-items (dissoc (:fixed-items posts-data) (:uuid activity-data))
        next-board-data (assoc posts-data :fixed-items next-fixed-items)]
    (assoc-in db posts-key next-board-data)))

(defmethod dispatcher/action :activity-share-show
  [db [_ activity-data container-element-id]]
  (-> db
    (assoc :activity-share {:share-data activity-data})
    (assoc :activity-share-container container-element-id)
    (dissoc :activity-shared-data)))

(defmethod dispatcher/action :activity-share-hide
  [db [_]]
  (-> db
    (dissoc :activity-share)
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

(defmethod dispatcher/action :activity-get/finish
  [db [_ status activity-data secure-uuid]]
  (let [next-db (if (= status 404)
                  (dissoc db :latest-entry-point)
                  db)
        activity-uuid (:uuid activity-data)
        org-slug (:org-slug activity-data)
        board-slug (:board-slug activity-data)
        activity-key (if secure-uuid
                       (dispatcher/secure-activity-key org-slug secure-uuid)
                       (dispatcher/activity-key org-slug activity-uuid))
        fixed-activity-data (au/fix-entry
                             activity-data
                             {:slug (or (:board-slug activity-data) board-slug)
                              :name (:board-name activity-data)}
                             (dispatcher/change-data db))]
    (-> next-db
      (dissoc :activity-loading)
      (assoc-in activity-key fixed-activity-data))))

(defmethod dispatcher/action :entry-save-with-board/finish
  [db [_ org-slug fixed-board-data]]
  (let [board-key (dispatcher/board-data-key org-slug (:slug fixed-board-data))
        posts-key (dispatcher/posts-data-key org-slug)]
  (-> db
    (assoc-in board-key (dissoc fixed-board-data :fixed-items))
    (assoc-in posts-key (merge-items (get-in db posts-key)
                                     fixed-board-data))
    (dissoc :section-editing)
    (update-in [:modal-editing-data] dissoc :loading)
    (assoc-in [:modal-editing-data :board-slug] (:slug fixed-board-data))
    (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :all-posts-get/finish
  [db [_ org-slug fixed-posts]]
  (let [posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        merged-items (merge-items old-posts fixed-posts)]
    (assoc-in db posts-key merged-items)))

(defmethod dispatcher/action :all-posts-more
  [db [_ org-slug]]
  (let [posts-data-key (dispatcher/posts-data-key org-slug)
        posts-data (get-in db posts-data-key)
        next-posts-data (assoc posts-data :loading-more true)]
    (assoc-in db posts-data-key next-posts-data)))

(defmethod dispatcher/action :all-posts-more/finish
  [db [_ org direction posts-data]]
  (if posts-data
    (let [posts-data-key (dispatcher/posts-data-key org)
          fixed-all-posts (au/fix-all-posts (:collection posts-data))
          old-all-posts (get-in db posts-data-key)
          next-links (vec
                      (remove
                       #(if (= direction :up) (= (:rel %) "next") (= (:rel %) "previous"))
                       (:links fixed-all-posts)))
          link-to-move (if (= direction :up)
                          (utils/link-for (:links old-all-posts) "next")
                          (utils/link-for (:links old-all-posts) "previous"))
          fixed-next-links (if link-to-move
                              (vec (conj next-links link-to-move))
                              next-links)
          with-links (assoc fixed-all-posts :links fixed-next-links)
          new-items (merge-items old-all-posts with-links)
          keeping-items (count (:fixed-items old-all-posts))
          new-all-posts (-> new-items
                          (assoc :direction direction)
                          (assoc :saved-items keeping-items))]
      (assoc-in db posts-data-key new-all-posts))
    db))

(defmethod dispatcher/action :must-see-get/finish
  [db [_ org-slug must-see-posts]]
  (let [posts-data-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-data-key)
        merged-items (merge-items old-posts must-see-posts)]
    (assoc-in db posts-data-key merged-items)))