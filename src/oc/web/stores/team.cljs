(ns oc.web.stores.team
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.web.utils.activity :as au]
            [oc.web.utils.mention :as mu]
            [oc.web.lib.utils :as utils]))

(defmethod dispatcher/action :teams-get
  [db [_]]
  (assoc db :teams-data-requested true))

(defmethod dispatcher/action :teams-loaded
  [db [_ teams]]
  (assoc-in db [:teams-data :teams] teams))

(defmethod dispatcher/action :active-users
  [db [_ org-slug active-users-data]]
  (if-let [users (-> active-users-data :collection :items)]
    (let [users-map (zipmap (map :user-id users) users)
          org-boards-key (concat (dispatcher/org-data-key org-slug) [:boards])
          next-db* (update-in db org-boards-key #(mapv (fn [board] (au/fix-direct-board board users-map)) %))
          boards-key (dispatcher/boards-key org-slug)
          next-db (reduce (fn [tdb board-key]
                           (let [board-data-key (concat boards-key [board-key :board-data])
                                 old-board-data (get-in tdb board-data-key)]
                             (assoc-in tdb board-data-key (au/fix-direct-board old-board-data users-map))))
                   next-db*
                   (keys (get-in db boards-key)))
          org-data (get-in next-db (dispatcher/org-data-key org-slug))
          cmail-data (get next-db :cmail-data)
          updated-cmail-data (if-let [cmail-board (some #(when (or (= (:board-uuid cmail-data) (:uuid %))
                                                                   (= (:board-slug cmail-data) (:slug %)))
                                                           %)
                                                   (:boards org-data))]
                               (assoc cmail-data :board-name (:name cmail-board))
                               cmail-data)]
      (-> next-db
       (assoc-in (dispatcher/active-users-key org-slug) users-map)
       (assoc-in (dispatcher/mention-users-key org-slug) (mu/users-for-mentions users-map))
       (assoc :cmail-data updated-cmail-data)))
    db))

(defn- deep-merge-users [new-users old-users]
  (let [filtered-new-users (filter
                            #(and (seq (:user-id %))
                                  (#{"active" "unverified"} (:status %)))
                            (if (map? new-users) (vals new-users) new-users))
        new-users-map (zipmap (map :user-id filtered-new-users) filtered-new-users)]
    (merge-with merge old-users new-users-map)))

(defmethod dispatcher/action :team-roster-loaded
  [db [_ org-slug roster-data]]
  (if roster-data
    (let [merged-users-data (deep-merge-users (:users roster-data) (dispatcher/active-users org-slug db))]
      (-> db
       (assoc-in (dispatcher/team-roster-key (:team-id roster-data)) roster-data)
       (assoc-in (dispatcher/mention-users-key org-slug) (mu/users-for-mentions merged-users-data))
       (assoc-in (dispatcher/active-users-key org-slug) merged-users-data)))
    db))

(defn parse-team-data [team-data]
  (let [team-has-bot? (j/team-has-bot? (:team-id team-data))
        slack-orgs (:slack-orgs team-data)
        slack-users (j/get-key :slack-users)
        can-add-bot? (some #(->> % :slack-org-id keyword (get slack-users)) slack-orgs)]
    (-> team-data
     (assoc :can-slack-invite team-has-bot?)
     (assoc :can-add-bot (and (not team-has-bot?) can-add-bot?)))))

(defmethod dispatcher/action :team-loaded
  [db [_ org-slug team-data]]
  (if team-data
    (let [merged-users-data (deep-merge-users (:users team-data) (dispatcher/active-users org-slug db))]
      (-> db
       (assoc-in (dispatcher/team-data-key (:team-id team-data)) (parse-team-data team-data))
       (assoc-in (dispatcher/mention-users-key org-slug) (mu/users-for-mentions merged-users-data))
       (assoc-in (dispatcher/active-users-key org-slug) merged-users-data)))
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