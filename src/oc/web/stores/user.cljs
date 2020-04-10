(ns oc.web.stores.user
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.notification :as notif-utils]
            [cljsjs.moment-timezone]))

(def default-user-image "/img/ML/happy_face_red.svg")
(def other-user-images
 ["/img/ML/happy_face_green.svg"
  "/img/ML/happy_face_blue.svg"
  "/img/ML/happy_face_purple.svg"
  "/img/ML/happy_face_yellow.svg"])

(defn random-user-image []
  (first (shuffle (vec (conj other-user-images default-user-image)))))

(def default-avatar-url (random-user-image))

(defn user-icon [user-id]
  (if (= user-id (j/get-key :user-id))
    ;; If the user id is the same of the current JWT use the red icon
    default-user-image
    ;; if not get a random icon from the rest of the images vector
    (first other-user-images)))

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

(defn parse-user-data [user-data]
  (-> user-data
    (assoc :avatar-url (fixed-avatar-url (:avatar-url user-data)))
    (assoc :auth-source (or (j/get-key :auth-source) default-invite-type))))

(defn- fix-user-values [user-data]
    (cond-> user-data
     true (assoc :has-changes false)
     (empty? (:first-name user-data)) (merge {:first-name ""})
     (empty? (:last-name user-data)) (merge {:last-name ""})
     (empty? (:current-password user-data)) (merge {:current-password ""})
     (empty? (:password user-data)) (merge {:password ""})
     (empty? (:email user-data)) (merge {:email ""})
     (empty? (:timezone user-data)) (merge {:timezone (or (.. js/moment -tz guess) "")})
     (empty? (:blurb user-data)) (merge {:blurb ""})
     (empty? (:location user-data)) (merge {:location ""})
     (empty? (:title user-data)) (merge {:title ""})
     (empty? (:profiles user-data)) (merge {:profiles {:twitter "" :linked-in "" :instagram "" :facebook ""}})))

(defn update-user-data [db user-data]
  (let [fixed-user-data (parse-user-data user-data)
        org-data (dispatcher/org-data db)
        active-user-key (when org-data
                          (conj (dispatcher/active-users-key (:slug org-data)) (:user-id user-data)))
        next-db (if org-data
                  (update-in db active-user-key merge fixed-user-data)
                  db)]
    (-> next-db
        (assoc :current-user-data fixed-user-data)
        (assoc :edit-user-profile (fix-user-values fixed-user-data))
        (assoc :edit-user-profile-avatar (:avatar-url fixed-user-data))
        (dissoc :edit-user-profile-failed))))

(defmethod dispatcher/action :user-profile-avatar-update/failed
  [db [_]]
  (assoc db :edit-user-profile-avatar (:avatar-url (:current-user-data db))))

(defmethod dispatcher/action :user-data
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
      (assoc :signup-with-email {:firstname "" :lastname "" :email "" :pswd ""})
      (dissoc :signup-with-email-error))
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
  (update-user-data db (:current-user-data db)))

(defmethod dispatcher/action :user-profile-save
  [db [_]]
  (-> db
      ;; Loading user data
      (assoc-in [:edit-user-profile :loading] true)
      ;; Force a refresh of entry-point and auth-settings
      (dissoc :latest-entry-point :latest-auth-settings)
      ;; Remove the new-slack-user flag to avoid redirecting to the profile again
      (dissoc :new-slack-user)))

(defmethod dispatcher/action :user-profile-update/failed
  [db [_]]
  (assoc db :edit-user-profile-failed true))

;; Signup actions

(defmethod dispatcher/action :signup-with-email
  [db [_]]
  (-> db
    (dissoc :signup-with-email-error :latest-auth-settings :latest-entry-point)
    (assoc-in [:signup-with-email :error] nil)))

(defmethod dispatcher/action :signup-with-email/failed
  [db [_ status]]
  (assoc-in db [:signup-with-email :error] status))

(defmethod dispatcher/action :signup-with-email/success
  [db [_]]
  (dissoc db :signup-with-email-error))

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

(defn user-role [org-data user-data]
  (let [is-admin? (j/is-admin? (:team-id org-data))
        is-author? (utils/link-for (:links org-data) "create")]
    (cond
      is-admin? :admin
      is-author? :author
      :else :viewer)))

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
        new-notifications (notif-utils/sorted-notifications (concat [notification] old-notifications))]
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
        read-notifications (map #(if (= (:created-at %) (:created-at notification)) (assoc % :unread false) %) old-notifications)]
    (assoc-in db user-notifications-key read-notifications)))

;; Expo push tokens

(defmethod dispatcher/action :expo-push-token
  [db [_ push-token]]
  (if push-token
    (assoc-in db dispatcher/expo-push-token-key push-token)
    db))

;; Publishers

(defn- publishers-list-with-users [publishers-list active-users-map]
  (->> publishers-list
   (map active-users-map)
   (sort-by :short-name)
   vec))

(defmethod dispatcher/action :publishers/loaded
  [db [_ org-slug {:keys [publisher-uuids user-id] :as resp}]]
  (if (= org-slug (:org-slug resp))
    (let [publishers-list-key (dispatcher/publishers-list-key org-slug)
          active-users (dispatcher/active-users org-slug db)]
      (if active-users
        (assoc-in db publishers-list-key
         (publishers-list-with-users publisher-uuids active-users))
        ;; If users have not been loaded yet save the publishers list for later
        (assoc-in db publishers-list-key publisher-uuids)))
      db))

(defmethod dispatcher/action :publishers/follow
  [db [_ org-slug {:keys [publisher-uuids] :as resp}]]
  (if (= org-slug (:org-slug resp))
    (let [publishers-list-key (dispatcher/publishers-list-key org-slug)
          active-users (dispatcher/active-users org-slug db)]
      (if active-users
        (assoc-in db publishers-list-key
                     (publishers-list-with-users publisher-uuids active-users))
        ;; If users have not been loaded yet save the publishers list for later
        (assoc-in db publishers-list-key publisher-uuids)))
    db))