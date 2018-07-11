(ns oc.web.stores.activity
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]))


(defmethod dispatcher/action :activity-modal-fade-in
  [db [_ activity-data editing dismiss-on-editing-end]]
  (if (get-in db [:search-active])
    db
    (-> db
      (assoc :activity-modal-fade-in (:uuid activity-data))
      (assoc :dismiss-modal-on-editing-stop (and editing dismiss-on-editing-end)))))

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
  [db [_]]
  (assoc-in db [:entry-editing :loading] true))

(defmethod dispatcher/action :entry-save/finish
  [db [_ activity-data edit-key board-key]]
  (let [org-slug (utils/post-org-slug activity-data)
        board-slug (:board-slug activity-data)
        board-data (or (get-in db board-key) utils/default-drafts-board)
        activity-board-data (get-in db (dispatcher/board-data-key org-slug board-slug))
        fixed-activity-data (au/fix-entry activity-data activity-board-data (dispatcher/change-data db))
        next-fixed-items (assoc (:fixed-items board-data) (:uuid fixed-activity-data) fixed-activity-data)
        next-db (assoc-in db board-key (assoc board-data :fixed-items next-fixed-items))
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
        board-key (dispatcher/board-data-key org-slug board-slug)
        fixed-board-data (au/fix-board new-board-data (dispatcher/change-data db))]
    (-> db
      (assoc-in board-key fixed-board-data)
      (dissoc :section-editing)
      (update-in [:entry-editing] dissoc :publishing)
      (assoc-in [:entry-editing :board-slug] (:slug fixed-board-data))
      (assoc-in [:entry-editing :new-section] true)
      (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :entry-publish/finish
  [db [_ edit-key activity-data]]
  (let [board-slug (:board-slug activity-data)
        board-key (dispatcher/board-data-key (utils/post-org-slug activity-data) board-slug)
        board-data (get-in db board-key)
        fixed-activity-data (au/fix-entry activity-data board-data (dispatcher/change-data db))
        next-fixed-items (assoc (:fixed-items board-data) (:uuid fixed-activity-data) fixed-activity-data)]
    (-> db
      (assoc-in (vec (conj board-key :fixed-items)) next-fixed-items)
      (update-in [edit-key] dissoc :publishing)
      (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :entry-publish/failed
  [db [_]]
  (-> db
    (update-in [:entry-editing] dissoc :publishing)
    (update-in [:entry-editing] assoc :error true)))

(defmethod dispatcher/action :activity-delete
  [db [_ board-key activity-data]]
  (let [board-data (get-in db board-key)
        next-fixed-items (dissoc (:fixed-items board-data) (:uuid activity-data))
        next-board-data (assoc board-data :fixed-items next-fixed-items)]
    (assoc-in db board-key next-board-data)))

(defmethod dispatcher/action :activity-move
  [db [_ activity-data org-slug board-data]]
  (let [change-data (dispatcher/change-cache-data db)
        fixed-activity-data (au/fix-entry activity-data board-data change-data)
        all-posts-activity-key (dispatcher/activity-key
                                org-slug
                                :all-posts
                                (:uuid activity-data))
        old-board-activity-key (dispatcher/activity-key
                                org-slug
                                (:board-slug activity-data)
                                (:uuid activity-data))
        new-board-activity-key (dispatcher/activity-key
                                org-slug
                                (:slug board-data)
                                (:uuid activity-data))]
    (-> db
      (assoc-in all-posts-activity-key fixed-activity-data)
      (update-in (butlast old-board-activity-key) dissoc (last old-board-activity-key))
      (assoc-in new-board-activity-key fixed-activity-data))))

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
  [db [_ status org-slug activity-data secure-uuid is-ap?]]
  (let [next-db (if (= status 404)
                  (dissoc db :latest-entry-point)
                  db)
        activity-uuid (:uuid activity-data)
        board-slug (:board-slug activity-data)
        activity-key (if secure-uuid
                       (dispatcher/secure-activity-key org-slug secure-uuid)
                       (dispatcher/activity-key org-slug board-slug activity-uuid))
        fixed-activity-data (au/fix-entry
                             activity-data
                             {:slug (or (:board-slug activity-data) board-slug)
                              :name (:board-name activity-data)}
                             (dispatcher/change-data db))
        ap-activity-key (dispatcher/activity-key org-slug :all-posts activity-uuid)
        with-fixed-ap (if is-ap?
                        (assoc-in next-db ap-activity-key fixed-activity-data)
                        next-db)]
    (assoc-in with-fixed-ap activity-key fixed-activity-data)))

(defmethod dispatcher/action :entry-save-with-board/finish
  [db [_ org-slug fixed-board-data]]
  (let [board-key (dispatcher/board-data-key org-slug (:slug fixed-board-data))]
  (-> db
    (assoc-in board-key fixed-board-data)
    (dissoc :section-editing)
    (update-in [:modal-editing-data] dissoc :loading)
    (assoc-in [:modal-editing-data :board-slug] (:slug fixed-board-data))
    (dissoc :entry-toggle-save-on-exit))))

(defmethod dispatcher/action :all-posts-get/finish
  [db [_ org-slug fixed-all-posts]]
  (let [all-posts-key (dispatcher/all-posts-key org-slug)]
    (assoc-in db all-posts-key fixed-all-posts)))

(defmethod dispatcher/action :all-posts-more
  [db [_ org-slug]]
  (let [all-posts-key (dispatcher/all-posts-key org-slug)
        all-posts-data (get-in db all-posts-key)
        next-all-posts-data (assoc all-posts-data :loading-more true)]
    (assoc-in db all-posts-key next-all-posts-data)))

(defmethod dispatcher/action :all-posts-more/finish
  [db [_ org direction all-posts-data]]
  (if all-posts-data
    (let [all-posts-key (dispatcher/all-posts-key org)
          fixed-all-posts (au/fix-all-posts (:collection all-posts-data) (dispatcher/change-data db))
          old-all-posts (get-in db all-posts-key)
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
          new-items (merge (:fixed-items old-all-posts) (:fixed-items with-links))
          keeping-items (count (:fixed-items old-all-posts))
          new-all-posts (-> with-links
                              (assoc :fixed-items new-items)
                              (assoc :direction direction)
                              (assoc :saved-items keeping-items))]
      (assoc-in db all-posts-key new-all-posts))
    db))

(defmethod dispatcher/action :must-see-get/finish
  [db [_ org-slug must-see-posts]]
  (let [must-see-key (dispatcher/must-see-key org-slug)]
    (assoc-in db must-see-key must-see-posts)))