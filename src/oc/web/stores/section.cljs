(ns oc.web.stores.section
  (:require [taoensso.timbre :as timbre]
            [cljs-flux.dispatcher :as flux]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dispatcher]
            [oc.lib.time :as oc-time]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]))

;; Reducers used to watch for org/section dispatch data
(defmulti reducer (fn [db [action-type & _]]
                    (when-not (some #{action-type} [:update :input])
                      (timbre/debug "Dispatching section reducer:" action-type))
                    action-type))

(def sections-dispatch
  (flux/register
   dispatcher/actions
   (fn [payload]
     (swap! dispatcher/app-state reducer payload))))

(defmethod dispatcher/action :section
  [db [_ section-data]]
  (let [org-slug (utils/section-org-slug section-data)
        fixed-section-data (au/fix-board section-data (dispatcher/change-data db))
        db-loading (if (:is-loaded section-data)
                     (dissoc db :loading)
                     db)
        old-section-data (get-in db (dispatcher/board-data-key org-slug (keyword (:slug section-data))))
        with-current-edit (if (and (:is-loaded section-data)
                                   (:entry-editing db))
                            old-section-data
                            fixed-section-data)
        posts-key (dispatcher/posts-data-key org-slug)
        merged-items (merge (:fixed-items (get-in db posts-key))
                            (:fixed-items fixed-section-data))]
    (-> db-loading
        (assoc-in
          (dispatcher/board-data-key
             org-slug
             (keyword (:slug section-data)))
          (dissoc with-current-edit :fixed-items))
        (assoc-in posts-key
          {:fixed-items merged-items}))))

(defn new?
  "
  A board is new if:
    user is part of the team (we don't track new for non-team members accessing public boards)
     -and-
    change-at is newer than seen at
      -or-
    we have a change-at and no seen at
  "
  [change-data board]
  (let [changes (get change-data (:uuid board))
        change-at (:change-at changes)
        nav-at (:nav-at changes)
        in-team? (jwt/user-is-part-of-the-team (:team-id (dispatcher/org-data)))
        new? (and in-team?
                  (or (and change-at nav-at (> change-at nav-at))
                      (and change-at (not nav-at))))]
    new?))

(defn add-new-to-sections
  [org-data change-data]
  (let [section-data (:boards org-data)
        new-section-data (for [section section-data]
                           (assoc section :new (new? change-data section)))]
    (assoc org-data :boards new-section-data)))

(defn fix-org-section-data
  [db org-data changes]
  (assoc-in db
            (dispatcher/org-data-key (:slug org-data))
            (add-new-to-sections org-data changes)))

(defn fix-sections
  [db org-data changes]
  (let [sections (:boards org-data)
        org-slug (:slug org-data)]
    (reduce #(if (dispatcher/board-data db org-slug (:slug %2))
               (let [board-key (dispatcher/board-data-key org-slug (:slug %2))
                     board-data (au/fix-board
                                 (dispatcher/board-data db org-slug (:slug %2))
                                 changes)]
                 (assoc-in %1 board-key board-data))
               %1)
            db sections)))

(defn- update-change-data [db section-uuid property timestamp]
  (let [org-data (dispatcher/org-data db)
        change-data-key (dispatcher/change-data-key (:slug org-data))
        change-data (get-in db change-data-key)
        change-map (or (get change-data section-uuid) {})
        new-change-map (assoc change-map property timestamp)
        new-change-data (assoc change-data section-uuid new-change-map)]
    (-> db
      (fix-org-section-data org-data new-change-data)
      (fix-sections org-data new-change-data)
      (assoc-in change-data-key new-change-data))))

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

(defmethod dispatcher/action :section-edit-save/finish
  [db [_ section-data]]
  (let [org-slug (utils/section-org-slug section-data)
        section-slug (:slug section-data)
        board-key (dispatcher/board-data-key org-slug section-slug)
        fixed-section-data (au/fix-board section-data (dispatcher/change-data db))]
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

(defmethod dispatcher/action :section-delete
  [db [_ org-slug section-slug]]
  (let [section-key (dispatcher/board-key org-slug section-slug)
        org-sections-key (vec (conj (dispatcher/org-data-key org-slug) :boards))
        remaining-sections (remove #(= (:slug %) section-slug) (get-in db org-sections-key))
        posts-key (dispatcher/posts-data-key org-slug)
        old-posts (get-in db posts-key)
        removed-posts (filterv (fn [p] (not= (:board-slug p) section-slug))
                               (vals (:fixed-items old-posts)))]
    (-> db
      (update-in (butlast section-key) dissoc (last section-key))
      (assoc posts-key (assoc old-posts
                              (zipmap (map :uuid removed-posts) removed-posts)
                              :fixed-items))
      (assoc org-sections-key remaining-sections)
      (dissoc :section-editing))))

(defmethod dispatcher/action :container/status
  [db [_ status-data]]
  (timbre/debug "Change status received:" status-data)
  (let [org-data (dispatcher/org-data db)
        old-status-data (dispatcher/change-data db)
        status-by-uuid (group-by :container-id status-data)
        clean-status-data (zipmap (keys status-by-uuid) (->> status-by-uuid
                                                          vals
                                                          ; remove the sequence of 1 from group-by
                                                          (map first)
                                                          ; dup seen-at as nav-at
                                                          (map #(assoc % :nav-at (:seen-at %)))))
        new-status-data (merge old-status-data clean-status-data)]
    (timbre/debug "Change status data:" new-status-data)
    (-> db
      (fix-org-section-data org-data new-status-data)
      (fix-sections org-data new-status-data)
      (assoc-in (dispatcher/change-data-key (:slug org-data)) new-status-data))))

;; Section store specific reducers
(defmethod reducer :default [db payload]
  ;; ignore state changes not specific to reactions
  db)

(defmethod reducer :org-loaded
  [db [_ org-data saved?]]
  (fix-org-section-data db org-data (dispatcher/change-data db)))