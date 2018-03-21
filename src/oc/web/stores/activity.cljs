(ns oc.web.stores.activity
  (:require [taoensso.timbre :as timbre]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.utils :as utils]))

(defmethod dispatcher/action :activity-modal-fade-in
  [db [_ activity-data editing]]
  (if (get-in db [:search-active])
    db
    (-> db
      (assoc :activity-modal-fade-in (:uuid activity-data))
      (assoc :dismiss-modal-on-editing-stop editing)
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

(defmethod dispatcher/action :entry [db [_ {:keys [entry-uuid body]}]]
  (let [is-all-posts (or (:from-all-posts @router/path) (= (router/current-board-slug) "all-posts"))
        board-key (if is-all-posts
                   (dispatcher/all-posts-key (router/current-org-slug))
                   (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug)))
        board-data (get db board-key)
        new-entries (assoc (get board-data :fixed-items)
                     entry-uuid
                     (utils/fix-entry body board-data))
        new-board-data (assoc board-data :fixed-items new-entries)]
  (assoc db board-key new-board-data)))

(defmethod dispatcher/action :entry-clear-local-cache
  [db [_ edit-key]]
  (dissoc db :entry-save-on-exit))

(defmethod dispatcher/action :entry-save
  [db [_]]
  (assoc-in db [:entry-editing :loading] true))

(defmethod dispatcher/action :entry-save/finish
  [db [_ activity-data edit-key]]
  (let [board-slug (:board-slug activity-data)
        org-slug (router/current-org-slug)
        board-key (if (= (:status activity-data) "published")
                   (dispatcher/current-board-key)
                   (dispatcher/board-data-key org-slug utils/default-drafts-board-slug))
        board-data (or (get-in db board-key) utils/default-drafts-board)
        activity-board-data (get-in db (dispatcher/board-data-key org-slug board-slug))
        fixed-activity-data (utils/fix-entry activity-data activity-board-data)
        next-fixed-items (assoc (:fixed-items board-data) (:uuid fixed-activity-data) fixed-activity-data)
        next-db (assoc-in db board-key (assoc board-data :fixed-items next-fixed-items))
        with-edited-key (if edit-key
                          (update-in next-db [edit-key] dissoc :loading)
                          next-db)
        without-entry-save-on-exit (dissoc with-edited-key :entry-toggle-save-on-exit)]
    without-entry-save-on-exit))

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
  (let [org-slug (router/current-org-slug)
        board-slug (:slug new-board-data)
        board-key (dispatcher/board-data-key org-slug (:slug new-board-data))
        fixed-board-data (utils/fix-board new-board-data)]
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
        board-key (dispatcher/board-data-key (router/current-org-slug) board-slug)
        board-data (get-in db board-key)
        fixed-activity-data (utils/fix-entry activity-data board-data)
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
  [db [_ activity-data]]
  (let [is-all-posts (or (:from-all-posts @router/path) (= (router/current-board-slug) "all-posts"))
        board-key (if is-all-posts
                   (dispatcher/all-posts-key (router/current-org-slug))
                   (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug)))
        board-data (get-in db board-key)
        next-fixed-items (dissoc (:fixed-items board-data) (:uuid activity-data))
        next-board-data (assoc board-data :fixed-items next-fixed-items)]
    (assoc-in db board-key next-board-data)))

(defmethod dispatcher/action :activity-move
  [db [_ activity-data board-data]]
  (let [is-all-posts (or (:from-all-posts @router/path) (= (router/current-board-slug) "all-posts"))
        fixed-activity-data (assoc activity-data :board-slug (:slug board-data))]
    (if is-all-posts
      (let [next-activity-data-key (dispatcher/activity-key
                                    (router/current-org-slug)
                                    :all-posts
                                    (:uuid activity-data))]
        (assoc-in db next-activity-data-key fixed-activity-data))
      (let [activity-data-key (dispatcher/activity-key
                               (router/current-org-slug)
                               (:board-slug activity-data)
                               (:uuid activity-data))
            next-activity-data-key (dispatcher/activity-key
                                    (router/current-org-slug)
                                    (:slug board-data)
                                    (:uuid activity-data))]
        (-> db
          (update-in (butlast activity-data-key) dissoc (last activity-data-key))
          (assoc-in next-activity-data-key fixed-activity-data))))))

(defmethod dispatcher/action :activity-share-show
  [db [_ activity-data]]
  (-> db
    (assoc :activity-share {:share-data activity-data})
    (dissoc :activity-shared-data)))

(defmethod dispatcher/action :activity-share-hide
  [db [_]]
  (dissoc db :activity-share))

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
      (utils/fix-entry shared-data (:board-slug shared-data))
      {:error true})))

(defmethod dispatcher/action :activity-get/finish
  [db [_ status activity-data]]
  (let [next-db (if (= status 404)
                  (dissoc db :latest-entry-point)
                  db)
        activity-uuid (:uuid activity-data)
        org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        activity-key (if (router/current-secure-activity-id)
                       (dispatcher/secure-activity-key org-slug (router/current-secure-activity-id))
                       (dispatcher/activity-key org-slug board-slug activity-uuid))
        fixed-activity-data (utils/fix-entry
                             activity-data
                             {:slug (or (:board-slug activity-data) board-slug)
                              :name (:board-name activity-data)})]
    (-> next-db
      (dissoc :activity-loading)
      (assoc-in activity-key fixed-activity-data))))