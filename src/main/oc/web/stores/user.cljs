(ns oc.web.stores.user
  (:require [taoensso.timbre :as timbre]
            [oc.lib.user :as user-lib]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.user :as uu]
            [oc.web.utils.activity :as au]
            [oc.web.utils.notification :as notif-utils]
            ["moment-timezone" :as moment-timezone]))

(defonce default-avatar-url (uu/random-avatar))

(defn user-icon [user-id]
  (if (= user-id (j/get-key :user-id))
    ;; If the user id is the same of the current JWT use the red icon
    uu/default-avatar
    ;; if not get a random icon from the rest of the images vector
    (first uu/other-default-avatars)))

(defonce show-login-overlay? dispatcher/show-login-overlay-key)

;; Signup keys
(defonce signup-with-email :signup-with-email)

(defn get-show-login-overlay []
  (get-in @dispatcher/app-state [dispatcher/show-login-overlay-key]))

;; Auth Settings
(defn auth-settings? []
  (contains? @dispatcher/app-state (first dispatcher/auth-settings-key)))

(defn auth-settings-status? []
  (and (auth-settings?)
       (contains? (dispatcher/auth-settings) :status)))

(defmethod dispatcher/action :auth-settings
  [db [_ body]]
  (let [next-db (assoc db :latest-auth-settings (.getTime (js/Date.)))]
    (assoc-in next-db dispatcher/auth-settings-key body)))

(defn- fixed-avatar-url [avatar-url]
  (if (empty? avatar-url)
    (utils/cdn default-avatar-url true)
    avatar-url))

(def default-invite-type "email")

(defn- pointed-name [{:keys [first-name last-name] :as user}]
  (str first-name " " (first last-name) "."))

(defn parse-users [users-list org-data follow-publishers-list]
  (let [follow-publishers-set (if (every? map? follow-publishers-list)
                                (set (map :user-id follow-publishers-list))
                                (set follow-publishers-list))]
    (map (fn [u] (-> u
                  (update :name #(or % (user-lib/name-for u)))
                  (update :short-name #(or % (user-lib/short-name-for u)))
                  (update :pointed-name #(or % (pointed-name u)))
                  (assoc :follow (follow-publishers-set (:user-id u)))
                  (as-> user
                   (if (map? org-data)
                     (assoc user :role (uu/get-user-type user org-data))
                     user)
                   (if (:role user)
                     (assoc user :role-string (uu/user-role-string (:role user)))
                     user))
                  (assoc :self? (= (:user-id u) (j/user-id)))))
     users-list)))

(defn parse-user-data [user-data org-data active-users]
  (let [active-user-data (get active-users (:user-id user-data))]
    (as-> user-data u
      (merge active-user-data u)
      (assoc u :role (uu/get-user-type u org-data))
      (assoc u :role-string (uu/user-role-string (:role u)))
      (update u :avatar-url fixed-avatar-url)
      (assoc u :auth-source (or (j/get-key :auth-source) default-invite-type))
      (assoc u :name (user-lib/name-for user-data))
      (assoc u :short-name (user-lib/short-name-for user-data))
      (assoc u :digest-delivery (set (map keyword (or (:digest-delivery user-data) [])))))))

(def ^:private empty-user*
 {:first-name ""
  :last-name ""
  :password ""
  :email ""
  :blurb ""
  :location ""
  :title ""
  :profiles {:twitter "" :linked-in "" :instagram "" :facebook ""}})

(defn- empty-user-data
 "This is a function to call the timezone guess when needed and not only one time on page load."
  [edit?]
  (cond-> empty-user*
   true (assoc :timezone (or (.. moment-timezone -tz guess) ""))
   edit? (assoc :has-changes false)))

(defn- editable-user-data
  [edit-user-data new-user-data]
  (let [changed? (:has-changes edit-user-data)
        changed-user-data (select-keys edit-user-data [:first-name :last-name :avatar-url :password :timezone :blurb :location :title :profiles :has-changes])]
    (cond-> (empty-user-data true) ;; Start with the empty user map
     ;; Merge in the new user data
     true (merge new-user-data)
     ;; If user is editing the profile let's merge in all the editable keys
     changed? (merge changed-user-data)
     ;; Merge in the fixed avatar url
     true (assoc :avatar-url (fixed-avatar-url (:avatar-url new-user-data))))))

(defn update-user-data
  ([db user-data] (update-user-data db user-data false))
  ([db user-data force-edit-reset?]
  (let [org-data (dispatcher/org-data db)
        active-users (dispatcher/active-users (:slug org-data) db)
        fixed-user-data (parse-user-data user-data org-data active-users)
        active-user-key (conj (dispatcher/active-users-key (:slug org-data)) (:user-id user-data))
        next-db (if org-data
                  (update-in db active-user-key merge fixed-user-data)
                  db)]
    (-> next-db
     (assoc :current-user-data fixed-user-data)
     (update :edit-user-profile #(editable-user-data (if force-edit-reset? nil %) fixed-user-data))
     (assoc :edit-user-profile-avatar (:avatar-url fixed-user-data))
     (dissoc :edit-user-profile-failed)))))

(defmethod dispatcher/action :user-profile-avatar-update/failed
  [db [_]]
  (assoc db :edit-user-profile-avatar (:avatar-url (:current-user-data db))))

(defmethod dispatcher/action :user-data
  [db [_ user-data]]
  (update-user-data db user-data))

(defmethod dispatcher/action :user-profile-avatar-update/success
  [db [_ user-data]]
  (update-user-data db user-data))

;; Login actions

;; Store in application state whether to display the login overlay
(defmethod dispatcher/action :login-overlay-show
 [db [_ show-login-overlay]]
 (cond
    (= show-login-overlay :login-with-email)
    (-> db
      (assoc dispatcher/show-login-overlay-key show-login-overlay)
      (assoc :login-with-email {:email "" :pswd ""})
      (dissoc :login-with-email-error))
    (= show-login-overlay :signup-with-email)
    (-> db
      (assoc dispatcher/show-login-overlay-key show-login-overlay)
      (assoc :signup-with-email {:firstname "" :lastname "" :email "" :pswd ""}))
    :else
    (assoc db dispatcher/show-login-overlay-key show-login-overlay)))

(defn- dissoc-auth
  [db]
  (dissoc db :latest-auth-settings :latest-entry-point))

(defmethod dispatcher/action :login-with-email
  [db [_]]
  (-> db
      (dissoc :login-with-email-error)
      (dissoc-auth)))

(defmethod dispatcher/action :login-with-slack
  [db [_]]
  (dissoc-auth db))

(defmethod dispatcher/action :login-with-email/failed
  [db [_ error]]
  (assoc db :login-with-email-error error))

(defmethod dispatcher/action :login-with-email/success
  [db [_]]
  (dissoc db dispatcher/show-login-overlay-key))

;; Auth actions

(defmethod dispatcher/action :auth-with-token
  [db [ _ token-type]]
  (-> db
    (assoc :auth-with-token-type token-type)
    (dissoc :latest-auth-settings :latest-entry-point)))

(defmethod dispatcher/action :auth-with-token/failed
  [db [_ error]]
  (if (= (:auth-with-token-type db) :password-reset)
    (assoc db :collect-pswd-error error)
    (assoc db :email-verification-error error)))

(defmethod dispatcher/action :auth-with-token/success
  [db [_ jwt]]
  (assoc db :email-verification-success true))

(defmethod dispatcher/action :pswd-collect
  [db [_ password-reset?]]
  (-> db
    (assoc :is-password-reset password-reset?)
    (dissoc :latest-entry-point :latest-auth-settings)))

(defmethod dispatcher/action :pswd-collect/finish
  [db [_ status]]
  (if (and (>= status 200)
           (<= status 299))
    (dissoc db :show-login-overlay)
    (assoc db :collect-password-error status)))

(defmethod dispatcher/action :password-reset
  [db [_]]
  (dissoc db :latest-entry-point :latest-auth-settings))

(defmethod dispatcher/action :password-reset/finish
  [db [_ status]]
  (assoc-in db [:password-reset :success] (and (>= status 200) (<= status 299))))

(defmethod dispatcher/action :user-profile-reset
  [db [_]]
  (update-user-data db (:current-user-data db) true))

(defmethod dispatcher/action :user-profile-save
  [db [_]]
  (-> db
      ;; Loading user data
      (update :edit-user-profile merge {:loading true :has-changes false})
      ;; Force a refresh of entry-point and auth-settings
      (dissoc :latest-entry-point :latest-auth-settings)
      ;; Remove the new-slack-user flag to avoid redirecting to the profile again
      (dissoc :new-slack-user)))

(defmethod dispatcher/action :user-profile-update/failed
  [db [_]]
  (-> db
   (assoc :edit-user-profile-failed true)
   (update :edit-user-profile merge {:loading false :has-changes true})))

;; Signup actions

(defmethod dispatcher/action :signup-with-email
  [db [_]]
  (update db :signup-with-email merge {:error nil
                                       :loading true}))

(defmethod dispatcher/action :signup-with-email/failed
  [db [_ status]]
  (update db :signup-with-email merge {:error status
                                       :loading false}))

(defmethod dispatcher/action :signup-with-email/success
  [db [_]]
  (update db :signup-with-email merge {:loading false
                                       :error nil}))

;; Logout action

(defmethod dispatcher/action :logout
  [db _]
  (dissoc db :jwt :latest-entry-point :latest-auth-settings))

(defn orgs? []
  (contains? @dispatcher/app-state dispatcher/orgs-key))

;; API entry point
(defmethod dispatcher/action :entry-point
  [db [_ orgs collection]]
  (-> db
      (assoc :latest-entry-point (.getTime (js/Date.)))
      (dissoc :loading)
      (assoc dispatcher/orgs-key orgs)
      (assoc-in dispatcher/api-entry-point-key (:links collection))
      (dissoc :slack-lander-check-team-redirect :email-lander-check-team-redirect)))

;; Invitation
(defmethod dispatcher/action :invitation-confirmed
  [db [_ confirmed]]
  (-> db
      (assoc :email-confirmed confirmed)
      (dissoc :latest-entry-point :latest-auth-settings)))

(defn has-slack-bot? [org-data]
  (j/team-has-bot? (:team-id org-data)))

;; User notifications
(defmethod dispatcher/action :user-notifications
  [db [_ org-slug notifications]]
  (assoc-in db (dispatcher/user-notifications-key org-slug) notifications))

;; User notifications
(defmethod dispatcher/action :user-notification
  [db [_ org-slug notification]]
  (let [user-notifications-key (dispatcher/user-notifications-key org-slug)
        old-notifications (get-in db user-notifications-key)
        new-notifications (cons notification old-notifications)]
    (assoc-in db user-notifications-key new-notifications)))

(defmethod dispatcher/action :user-notifications/read
  [db [_ org-slug]]
  (let [user-notifications-key (dispatcher/user-notifications-key org-slug)
        old-notifications (get-in db user-notifications-key)
        read-notifications (map #(assoc % :unread false) old-notifications)]
    (assoc-in db user-notifications-key read-notifications)))

(defmethod dispatcher/action :user-notification/read
  [db [_ org-slug notification]]
  (let [user-notifications-key (dispatcher/user-notifications-key org-slug)
        old-notifications (get-in db user-notifications-key)
        read-notifications (map #(if (= (:notify-at %) (:notify-at notification)) (assoc % :unread false) %) old-notifications)]
    (assoc-in db user-notifications-key read-notifications)))

(defmethod dispatcher/action :user-notification-remove-by-entry
  [db [_ org-slug board-id entry-id]]
  (let [notifications (get-in db (dispatcher/user-notifications-key org-slug))
        filtered-notifications (filter #(or (not= board-id (:board-id %))
                                            (not= entry-id (:entry-id %)))
                                notifications)]
    (assoc-in db (dispatcher/user-notifications-key org-slug) filtered-notifications)))

;; Expo push tokens

(defmethod dispatcher/action :expo-push-token
  [db [_ push-token]]
  (if push-token
    (assoc-in db dispatcher/expo-push-token-key push-token)
    db))

;; Follow

(defn enrich-publishers-list [publishers-list active-users-map]
  (if (and (seq publishers-list) (seq active-users-map))
    (let [publisher-uuids (remove nil? (if (every? map? publishers-list)
                           (map :user-id publishers-list)
                           publishers-list))]
      (->> publisher-uuids
       (map active-users-map)
       (sort-by :short-name)
       vec))
    publishers-list))

(defn- filter-org-boards [boards-data]
  ;; Filter out drafts board
  (filter #(and (not (:publisher-board %))
                (not= (:uuid %) (:uuid utils/default-drafts-board)))
   boards-data))

(defn enrich-boards-list [unfollow-board-uuids org-boards]
  (when (seq org-boards)
    (let [all-board-uuids (->> org-boards filter-org-boards (map :uuid) set)
          follow-board-uuids (clojure.set/difference all-board-uuids (set unfollow-board-uuids))
          boards-map (zipmap (map :uuid org-boards) org-boards)]
       (->> follow-board-uuids
            (map boards-map)
            (sort-by :name)
            vec))))

(defn- update-contributions-and-boards
  "Given the new list of board and publisher followers, update the following flag in each board and contributions data we have."
  [db org-slug follow-boards-list follow-publishers-list]
  (let [change-data (dispatcher/change-data db)
        org-data (dispatcher/org-data db org-slug)
        follow-publisher-uuids-set (set (map :user-id follow-publishers-list))
        contributions-list-key (dispatcher/contributions-list-key org-slug)
        next-active-users (apply merge
                           (map (fn [[k v]] (hash-map k (assoc v :following (-> v :user-id follow-publisher-uuids-set))))
                            (dispatcher/active-users org-slug db)))
        next-db* (assoc-in db (dispatcher/active-users-key org-slug) next-active-users)
        next-db (reduce (fn [tdb contrib-key]
                         (let [rp-contrib-data-key (dispatcher/contributions-data-key org-slug contrib-key dispatcher/recently-posted-sort)
                               ra-contrib-data-key (dispatcher/contributions-data-key org-slug contrib-key dispatcher/recent-activity-sort)]
                           (-> tdb
                            (update-in rp-contrib-data-key
                             #(dissoc (au/parse-contributions % change-data org-data next-active-users follow-publishers-list dispatcher/recently-posted-sort) :fixed-items))
                            (update-in ra-contrib-data-key
                             #(dissoc (au/parse-contributions % change-data org-data next-active-users follow-publishers-list dispatcher/recent-activity-sort) :fixed-items)))))
                  next-db*
                  (keys (get-in db contributions-list-key)))
        boards-key (dispatcher/boards-key org-slug)]
      (reduce (fn [tdb board-key]
               (let [rp-board-data-key (dispatcher/board-data-key org-slug board-key dispatcher/recently-posted-sort)
                     ra-board-data-key (dispatcher/board-data-key org-slug board-key dispatcher/recent-activity-sort)]
                 (-> tdb
                  (update-in rp-board-data-key
                             #(-> %
                                  (au/parse-board change-data next-active-users follow-boards-list dispatcher/recently-posted-sort)
                                  (dissoc :fixed-items)))
                  (update-in ra-board-data-key
                             #(-> %
                                  (au/parse-board change-data next-active-users follow-boards-list dispatcher/recent-activity-sort)
                                  (dissoc :fixed-items))))))
       next-db
       (keys (get-in db boards-key)))))

(defmethod dispatcher/action :follow/loaded
  [db [_ org-slug {:keys [follow-publisher-uuids unfollow-board-uuids user-id] :as resp}]]
  (if (= org-slug (:org-slug resp))
    (let [org-data-key (dispatcher/org-data-key org-slug)
          org-data (get-in db org-data-key)
          unfollow-boards-uuids-set (set unfollow-board-uuids)
          updated-org-data (update org-data :boards (fn [boards] (map #(assoc % :following (not (unfollow-boards-uuids-set (:uuid %)))) boards)))
          follow-publisher-uuids-set (set follow-publisher-uuids)
          active-users (dispatcher/active-users org-slug db)
          follow-publishers-list-key (dispatcher/follow-publishers-list-key org-slug)
          follow-boards-list-key (dispatcher/follow-boards-list-key org-slug)
          next-follow-boards-data (enrich-boards-list unfollow-board-uuids (:boards org-data))
          next-follow-publishers-data (enrich-publishers-list follow-publisher-uuids active-users)]
      (-> db
       (assoc-in org-data-key updated-org-data)
       (assoc-in follow-publishers-list-key next-follow-publishers-data)
       (assoc-in follow-boards-list-key next-follow-boards-data)
       (assoc-in (dispatcher/unfollow-board-uuids-key org-slug) unfollow-board-uuids)
       (update-contributions-and-boards org-slug next-follow-boards-data next-follow-publishers-data)))
      db))

(defmethod dispatcher/action :followers-count/finish
  [db [_ org-slug data]]
  (let [publisher-uuids (filter au/user? data)
        publishers-map (zipmap (map :resource-uuid publisher-uuids) publisher-uuids)
        unfollow-boards (filter au/board? data)
        unfollow-boards-map (zipmap (map :resource-uuid unfollow-boards) unfollow-boards)
        active-users-count (count (dispatcher/active-users org-slug db))
        org-boards-list (-> db
                            (dispatcher/org-data org-slug)
                            :boards
                            filter-org-boards)
        all-boards-uuid-and-count (map #(hash-map :uuid (:uuid %)
                                                  :active-users-count (if (= (:access %) "private")
                                                                        (+ (count (:viewers %)) (count (:authors %)))
                                                                        active-users-count))
                                       org-boards-list)
        all-boards-count (apply merge
                          (map #(hash-map (:uuid %) {:resource-uuid (:uuid %)
                                                     :resource-type :board
                                                     :count (if-let [unfollow-board (get unfollow-boards-map (:uuid %))]
                                                              (- (:active-users-count %) (:count unfollow-board))
                                                              (:active-users-count %))})
                           all-boards-uuid-and-count))]
    (-> db
     (update-contributions-and-boards org-slug all-boards-count publishers-map)
     (assoc-in (dispatcher/followers-publishers-count-key org-slug) publishers-map)
     (assoc-in (dispatcher/followers-boards-count-key org-slug) all-boards-count))))

(defmethod dispatcher/action :publisher/follow
  [db [_ org-slug {:keys [publisher-uuids follow? publisher-uuid] :as resp}]]
  (if (= org-slug (:org-slug resp))
    (let [follow-publishers-list-key (dispatcher/follow-publishers-list-key org-slug)
          active-users (dispatcher/active-users org-slug db)
          next-follow-publishers-data (enrich-publishers-list publisher-uuids active-users)
          followers-count-key (dispatcher/followers-publishers-count-key org-slug)
          publisher-count-key (conj followers-count-key publisher-uuid)
          fn (cond (true? follow?) inc (false? follow?) dec :else identity)
          follow-boards-data (dispatcher/follow-boards-list org-slug db)]
      (-> db
       (assoc-in follow-publishers-list-key next-follow-publishers-data)
       (update-in publisher-count-key #(if %
                                         (update % :count fn)
                                         {:org-slug org-slug
                                          :resource-uuid publisher-uuid
                                          :resource-type :user
                                          :count (if follow? 1 0)}))
       (update-contributions-and-boards org-slug follow-boards-data next-follow-publishers-data)))
    db))

(defmethod dispatcher/action :board/follow
  [db [_ org-slug {:keys [board-uuids follow? board-uuid] :as resp}]]
  (if (= org-slug (:org-slug resp))
    (let [follow-boards-list-key (dispatcher/follow-boards-list-key org-slug)
          org-data-key (dispatcher/org-data-key org-slug)
          org-data (get-in db org-data-key)
          org-boards (:boards org-data)
          unfollow-board-uuids-key (dispatcher/unfollow-board-uuids-key org-slug)
          all-board-uuids (set (map :uuid org-boards))
          updated-org-data (update org-data :boards (fn [boards] (map #(assoc % :following (if (= (:uuid %) board-uuid) follow? (:following %))) boards)))
          next-unfollow-uuids (clojure.set/difference all-board-uuids (set board-uuids))
          next-follow-boards-data (enrich-boards-list next-unfollow-uuids org-boards)
          followers-count-key (dispatcher/followers-boards-count-key org-slug)
          board-count-key (conj followers-count-key board-uuid)
          fn (cond (true? follow?) inc (false? follow?) dec :else identity)
          follow-publishers-data (dispatcher/follow-publishers-list org-slug db)]
      (-> db
       (assoc-in follow-boards-list-key next-follow-boards-data)
       (assoc-in unfollow-board-uuids-key next-unfollow-uuids)
       (assoc-in org-data-key updated-org-data)
       (update-in board-count-key #(if %
                                     (update % :count fn)
                                     {:org-slug org-slug
                                      :resource-uuid board-uuid
                                      :resource-type :board
                                      :count (if follow? 1 0)}))
       (update-contributions-and-boards org-slug next-follow-boards-data follow-publishers-data)))
    db))

(defmethod dispatcher/action :follow-list-last-added
  [db [_ org-slug {:keys [last-added-uuid resource-type] :as x}]]
  (let [follow-list-last-added-key (conj (dispatcher/follow-list-last-added-key org-slug) resource-type)]
    (assoc-in db follow-list-last-added-key last-added-uuid)))