(ns oc.web.stores.section
  (:require [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dispatcher]
            [oc.lib.time :as oc-time]
            [oc.web.lib.utils :as utils]))

(defmethod dispatcher/action :section
  [db [_ section-data]]
  (let [fixed-section-data (utils/fix-board section-data)
        db-loading (if (:is-loaded section-data)
                     (dissoc db :loading)
                     db)
        old-section-data (get-in db (dispatcher/board-data-key (router/current-org-slug) (keyword (:slug section-data))))
        with-current-edit (if (and (:is-loaded section-data)
                                   (:entry-editing db))
                            old-section-data
                            fixed-section-data)
        next-db (assoc-in db-loading
                  (dispatcher/board-data-key (router/current-org-slug) (keyword (:slug section-data)))
                  with-current-edit)]
    next-db))

(defn- update-change-data [db section-uuid property timestamp]
  (let [change-data-key (dispatcher/change-data-key (router/current-org-slug))
        change-data (get-in db change-data-key)
        change-map (or (get change-data section-uuid) {})
        new-change-map (assoc change-map property timestamp)
        new-change-data (assoc change-data section-uuid new-change-map)]
    (assoc-in db change-data-key new-change-data)))

(defmethod dispatcher/action :section-change
  [db [_ section-uuid change-at]]
  (update-change-data db section-uuid :change-at change-at))

(defmethod dispatcher/action :section-seen
  [db [_ section-uuid]]
  (let [next-db (dissoc db :no-reset-seen-at)]
    (if (:no-reset-seen-at db)
      ;; Do not update the seen-at if coming from the modal view
      next-db
      ;; Update change-data state that we nav'd to the section
      (update-change-data next-db section-uuid :nav-at (oc-time/current-timestamp)))))

(defmethod dispatcher/action :section-nav-away
  [db [_ section-uuid]]
  (timbre/debug "Section nav away:" section-uuid)
  (let [next-db (dissoc db :no-reset-seen-at)]
    (if (:no-reset-seen-at db)
      ;;  Do not update seen-at if navigating to an activity modal of the current section
      next-db
      ;; Update change-data state that we saw the section
      (update-change-data next-db section-uuid :seen-at (oc-time/current-timestamp)))))

(defmethod dispatcher/action :whats-new/finish
  [db [_ whats-new-data]]
  (if whats-new-data
    (let [fixed-whats-new-data (zipmap (map :uuid (:entries whats-new-data)) (:entries whats-new-data))]
      (assoc-in db dispatcher/whats-new-key fixed-whats-new-data))
    db))

(defmethod dispatcher/action :section-edit-save/finish
  [db [_ section-data]]
  (let [org-slug (router/current-org-slug)
        section-slug (:slug section-data)
        board-key (dispatcher/board-data-key org-slug section-slug)
        fixed-section-data (utils/fix-board section-data)]
    (-> db
        (assoc-in board-key fixed-section-data)
        (dissoc :section-editing))))

(defmethod dispatcher/action :section-edit/dismiss
  [db [_]]
  (dissoc db :section-editing))

(defmethod dispatcher/action :private-section-user-add
  [db [_ user user-type]]
  (let [section-data (:section-editing db)
        current-notifications (filterv #(not= (:user-id %) (:user-id user))
                                       (:private-notifications section-data))
        current-authors (filterv #(not= % (:user-id user)) (:authors section-data))
        current-viewers (filterv #(not= % (:user-id user)) (:viewers section-data))
        next-authors (if (= user-type :author)
                       (vec (conj current-authors (:user-id user)))
                       current-authors)
        next-viewers (if (= user-type :viewer)
                       (vec (conj current-viewers (:user-id user)))
                       current-viewers)
        next-notifications (vec (conj current-notifications user))]
    (assoc db :section-editing
           (merge section-data {:authors next-authors
                                :viewers next-viewers
                                :private-notifications next-notifications}))))

(defmethod dispatcher/action :private-section-user-remove
  [db [_ user]]
  (let [section-data (:section-editing db)
        private-notifications (filterv #(not= (:user-id %) (:user-id user))
                                       (:private-notifications section-data))
        next-authors (filterv #(not= % (:user-id user)) (:authors section-data))
        next-viewers (filterv #(not= % (:user-id user)) (:viewers section-data))]
    (assoc db :section-editing
           (merge section-data {:authors next-authors
                                :viewers next-viewers
                                :private-notifications private-notifications}))))

(defmethod dispatcher/action :private-section-kick-out-self/finish
  [db [_ success]]
  (if success
    ;; Force board editing dismiss
    (dissoc db :section-editing)
    ;; An error occurred while kicking the user out, no-op to let the user retry
    db))

(defmethod dispatcher/action :container/section-change
  [db [_ {container-uuid :container-id change-at :change-at user-id :user-id}]]
  (if (not= (jwt/user-id) user-id) ; no need to respond to our own events
    (if (not= container-uuid (:uuid (dispatcher/org-data)))
      (update-change-data db container-uuid :change-at change-at)
      db)
    db))