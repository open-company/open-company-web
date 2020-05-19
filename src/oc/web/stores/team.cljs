(ns oc.web.stores.team
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.lib.user :as user-lib]
            [oc.web.utils.activity :as au]
            [oc.web.utils.mention :as mu]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.user :as uu]
            [oc.web.stores.user :as user-store]))

(defn- parse-users [users-list follow-publishers-list]
  (let [follow-publishers-set (if (every? map? follow-publishers-list)
                                (set (map :user-id follow-publishers-list))
                                (set follow-publishers-list))]
    (map (fn [u] (-> u
                  (update :name #(or % (user-lib/name-for u)))
                  (update :short-name #(or % (user-lib/short-name-for u)))
                  (update :following (comp follow-publishers-set :user-id))))
     users-list)))

(defn- deep-merge-users [new-users old-users]
  (let [filtered-new-users (filter
                            #(and (seq (:user-id %))
                                  (uu/active? %))
                            (if (map? new-users) (vals new-users) new-users))
        new-users-map (zipmap (map :user-id filtered-new-users) filtered-new-users)]
    (merge-with merge old-users new-users-map)))

(defmethod dispatcher/action :active-users
  [db [_ org-slug active-users-data]]
  (if-let [users (-> active-users-data :collection :items)]
    (let [follow-publishers-list (dispatcher/follow-publishers-list org-slug db)
          fixed-users (parse-users users follow-publishers-list)
          change-data (dispatcher/change-data db)
          org-data (dispatcher/org-data db org-slug)
          contributions-list-key (dispatcher/contributions-list-key org-slug)
          users-map (zipmap (map :user-id users) fixed-users)
          next-db*** (reduce (fn [tdb contrib-key]
                             (let [contrib-data-key (concat contributions-list-key [contrib-key dispatcher/recently-posted-sort])
                                   old-contributions-data (get-in tdb contrib-data-key)]
                               (assoc-in tdb contrib-data-key (au/fix-contributions old-contributions-data change-data org-data users-map follow-publishers-list))))
                      db
                      (keys (get-in db contributions-list-key)))
          boards-key (dispatcher/boards-key org-slug)
          following-boards (dispatcher/follow-boards-list org-slug db)
          next-db** (reduce (fn [tdb board-key]
                             (let [board-data-key (concat boards-key [board-key dispatcher/recently-posted-sort :board-data])
                                   old-board-data (get-in tdb board-data-key)]
                               (assoc-in tdb board-data-key (au/fix-board old-board-data change-data users-map following-boards))))
                     next-db***
                     (keys (get-in db boards-key)))
          containers-key (dispatcher/containers-key org-slug)
          next-db* (reduce (fn [tdb container-key]
                             (let [container-rp-data-key (concat containers-key [container-key dispatcher/recently-posted-sort])
                                   old-container-rp-data (get-in tdb container-rp-data-key)
                                   container-ra-data-key (concat containers-key [container-key dispatcher/recent-activity-sort])
                                   old-container-ra-data (get-in tdb container-ra-data-key)]
                               (-> tdb
                                (assoc-in container-rp-data-key (au/fix-container old-container-rp-data change-data org-data users-map dispatcher/recently-posted-sort))
                                (assoc-in container-ra-data-key (au/fix-container old-container-ra-data change-data org-data users-map dispatcher/recent-activity-sort)))))
                    next-db**
                    (keys (get-in db containers-key)))
          posts-key (dispatcher/posts-data-key org-slug)
          next-db (reduce (fn [tdb post-uuid]
                           (let [post-data-key (concat posts-key [post-uuid])
                                 old-post-data (get-in tdb post-data-key)
                                 board-data (get-in tdb (dispatcher/board-data-key org-slug (:board-slug old-post-data)))]
                            (assoc-in tdb post-data-key (au/fix-entry old-post-data board-data change-data users-map))))
                   next-db*
                   (keys (get-in db posts-key)))
          org-data (get-in next-db (dispatcher/org-data-key org-slug))
          follow-publishers-list-key (dispatcher/follow-publishers-list-key org-slug)
          old-follow-publishers-list (get-in db follow-publishers-list-key)
          next-follow-publishers-list (user-store/enrich-publishers-list old-follow-publishers-list users-map)]
      (-> next-db
       (assoc-in (dispatcher/active-users-key org-slug) users-map)
       (assoc-in (dispatcher/mention-users-key org-slug) (mu/users-for-mentions users-map))
       (assoc-in follow-publishers-list-key next-follow-publishers-list)))
    db))

(defmethod dispatcher/action :teams-get
  [db [_]]
  (assoc db :teams-data-requested true))

(defmethod dispatcher/action :teams-loaded
  [db [_ teams]]
  (assoc-in db [:teams-data :teams] teams))

(defn- users-info-hover-from-roster
  "Given the previous users map and the new users vector coming from team or roster.
   Create a map of the new users with only some arbitrary data and merge them with the old users."
  [old-users-map roster-data]
  (let [filtered-users (uu/filter-active-users (:users roster-data))
        new-users-map (zipmap
                       (map :user-id filtered-users)
                       (map #(select-keys % [:user-id :first-name :last-name :avatar-url :name :short-name :location :timezone :title]) filtered-users))]
    (merge-with merge (or old-users-map {}) new-users-map)))

(defmethod dispatcher/action :team-roster-loaded
  [db [_ org-slug roster-data]]
  (if roster-data
    (let [follow-publishers-list-key (dispatcher/follow-publishers-list-key org-slug)
          old-follow-publishers-list (get-in db follow-publishers-list-key)
          parsed-roster-data (update roster-data :users #(parse-users % old-follow-publishers-list))
          merged-users-data (deep-merge-users (:users parsed-roster-data) (dispatcher/active-users org-slug db))
          next-follow-publishers-list (user-store/enrich-publishers-list old-follow-publishers-list merged-users-data)]
      (-> db
       (assoc-in (dispatcher/team-roster-key (:team-id roster-data)) parsed-roster-data)
       (assoc-in (dispatcher/mention-users-key org-slug) (mu/users-for-mentions merged-users-data))
       (assoc-in (dispatcher/active-users-key org-slug) merged-users-data)
       (update-in (dispatcher/users-info-hover-key org-slug) #(users-info-hover-from-roster % parsed-roster-data))
       (assoc-in follow-publishers-list-key next-follow-publishers-list)))
    db))

(defn parse-team-data [team-data follow-publishers-list]
  (let [team-has-bot? (j/team-has-bot? (:team-id team-data))
        slack-orgs (:slack-orgs team-data)
        slack-users (j/get-key :slack-users)
        can-add-bot? (some #(->> % :slack-org-id keyword (get slack-users)) slack-orgs)]
    (-> team-data
     (assoc :can-slack-invite team-has-bot?)
     (assoc :can-add-bot (and (not team-has-bot?) can-add-bot?))
     (update :users #(parse-users % follow-publishers-list)))))

(defmethod dispatcher/action :team-loaded
  [db [_ org-slug team-data]]
  (if team-data
    ;; if team is the current org team, load the slack chennels
    (let [follow-publishers-list-key (dispatcher/follow-publishers-list-key org-slug)
          old-follow-publishers-list (get-in db follow-publishers-list-key)
          parsed-team-data (parse-team-data team-data old-follow-publishers-list)
          merged-users-data (deep-merge-users (:users parsed-team-data) (dispatcher/active-users org-slug db))
          next-follow-publishers-list (user-store/enrich-publishers-list old-follow-publishers-list merged-users-data)]
      (-> db
       (assoc-in (dispatcher/team-data-key (:team-id team-data)) parsed-team-data)
       (assoc-in (dispatcher/mention-users-key org-slug) (mu/users-for-mentions merged-users-data))
       (assoc-in (dispatcher/active-users-key org-slug) merged-users-data)
       (update-in (dispatcher/users-info-hover-key org-slug) #(users-info-hover-from-roster % parsed-team-data))
       (assoc-in follow-publishers-list-key next-follow-publishers-list)))
    db))

(defmethod dispatcher/action :channels-enumerate
  [db [_ team-id]]
  (assoc db :enumerate-channels-requested true))

(defmethod dispatcher/action :channels-enumerate/success
  [db [_ team-id channels]]
  (let [channels-key (dispatcher/team-channels-key team-id)]
    (if channels
      (assoc-in db channels-key channels)
      (-> db
        (update-in (butlast channels-key) dissoc (last channels-key))
        (dissoc :enumerate-channels-requested)))))

;; Invite users

(defmethod dispatcher/action :invite-users
  [db [_ checked-users]]
  (assoc db :invite-users checked-users))

(defmethod dispatcher/action :invite-user/success
  [db [_ user]]
  (let [inviting-users (:invite-users db)
        next-inviting-users (utils/vec-dissoc inviting-users user)]
    (assoc db :invite-users next-inviting-users)))

(defmethod dispatcher/action :invite-user/failed
  [db [_ user]]
  (let [invite-users (:invite-users db)
        idx (utils/index-of invite-users #(= (:user %) (:user user)))
        next-invite-users (assoc-in invite-users [idx :error] true)]
    (assoc db :invite-users next-invite-users)))

;; User actions

(defmethod dispatcher/action :user-action
  [db [_ team-id idx]]
  (assoc-in db (concat (dispatcher/team-data-key team-id) [:users idx :loading]) true))

(defmethod dispatcher/action :email-domain-team-add
  [db [_]]
  (assoc db :add-email-domain-team-error false))

(defmethod dispatcher/action :email-domain-team-add/finish
  [db [_ success]]
  (-> db
      (assoc-in [:um-domain-invite :domain] (if success "" (:domain (:um-domain-invite db))))
      (assoc :add-email-domain-team-error (if success false true))))