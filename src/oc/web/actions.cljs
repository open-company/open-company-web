(ns oc.web.actions
  (:require [medley.core :as med]
            [clojure.string :as string]
            [taoensso.timbre :as timbre]
            [oc.lib.time :as oc-time]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.local-settings :as ls]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.actions.user :as ua]
            [oc.web.actions.activity :as aa]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.user-cache :as uc]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.ws-interaction-client :as ws-ic]
            [oc.web.lib.ws-change-client :as ws-cc]
            [oc.web.components.ui.user-avatar :refer (default-avatar-url)]))

;; ---- Generic Actions Dispatch
;; This is a small generic abstraction to handle "actions".
;; An `action` is a transformation on the app state.
;; The return value of an action will be used as the new app-state.

;; The extended multimethod `action` is defined in the dispatcher
;; namespace to avoid cyclical dependencies between namespaces

(defn- log [& args]
  (timbre/info (apply pr-str args)))

(defmethod dispatcher/action :default [db payload]
  (timbre/warn "No handler defined for" (str (first payload)))
  (timbre/info "Full event: " (pr-str payload))
  db)

(defmethod dispatcher/action :org
  [db [_ org-data saved?]]
  ;; Save the last visited org
  (when (and org-data
             (= (router/current-org-slug) (:slug org-data)))
    (cook/set-cookie! (router/last-org-cookie) (:slug org-data) (* 60 60 24 6)))
  (let [boards (:boards org-data)]

    (cond
      ;; If it's all posts page, loads all posts for the current org
      (or (= (router/current-board-slug) "all-posts")
          (:ap-initial-at db))
      (if (utils/link-for (:links org-data) "activity")
        ;; Load all posts only if not coming from a digest url
        ;; in that case do not load since we already have the results we need
        (api/get-all-posts (utils/link-for (:links org-data) "activity") (:ap-initial-at db)
         (partial aa/all-posts-get-finish (:ap-intiial-at db)))
        (router/redirect-404!))
      ; If there is a board slug let's load the board data
      (router/current-board-slug)
      (if-let [board-data (first (filter #(= (:slug %) (router/current-board-slug)) boards))]
        ; Load the board data since there is a link to the board in the org data
        (when-let [board-link (utils/link-for (:links board-data) ["item" "self"] "GET")]
          (api/get-board board-link))
        ; The board wasn't found, showing a 404 page
        (if (= (router/current-board-slug) utils/default-drafts-board-slug)
          (utils/after 100 #(dispatcher/dispatch! [:board utils/default-drafts-board]))
          (router/nav! (oc-urls/org (router/current-org-slug)))))
      ;; Board redirect handles
      (and (not (utils/in? (:route @router/path) "create-org"))
           (not (utils/in? (:route @router/path) "org-settings-invite"))
           (not (utils/in? (:route @router/path) "org-settings-team"))
           (not (utils/in? (:route @router/path) "org-settings"))
           (not (utils/in? (:route @router/path) "email-verification"))
           (not (utils/in? (:route @router/path) "sign-up"))
           (not (utils/in? (:route @router/path) "email-wall"))
           (not (utils/in? (:route @router/path) "confirm-invitation"))
           (not (utils/in? (:route @router/path) "secure-activity")))

      (when (>= (count boards) 1)
        ;; Redirect to the first board if at least one is present
        (let [board-to (utils/get-default-board org-data)]
          (utils/after 10
            #(router/nav!
               (if board-to
                 (oc-urls/board (:slug org-data) (:slug board-to))
                 (oc-urls/all-posts (:slug org-data)))))))))

  ;; Change service connection
  (when (jwt/jwt) ; only for logged in users
    (when-let [ws-link (utils/link-for (:links org-data) "changes")]
      (ws-cc/reconnect ws-link (jwt/get-key :user-id) (:slug org-data) (map :uuid (:boards org-data)))))
  ;; Interaction service connection
  (when (jwt/jwt) ; only for logged in users
    (when-let [ws-link (utils/link-for (:links org-data) "interactions")]
      (ws-ic/reconnect ws-link (jwt/get-key :user-id))))
  (-> db
    (assoc-in (dispatcher/org-data-key (:slug org-data)) (utils/fix-org org-data))
    (assoc :org-editing (-> (:org-editing db)
                            (assoc :saved saved?)
                            (dissoc :has-changes)))))

(defmethod dispatcher/action :boards-load-other
  [db [_ boards]]
  (doseq [board boards
          :when (not= (:slug board) (router/current-board-slug))]
    (api/get-board (utils/link-for (:links board) ["item" "self"] "GET")))
  db)

(defmethod dispatcher/action :board-get
  [db [_ link]]
  (api/get-board link)
  db)

(defn- update-change-data [db board-uuid property timestamp]
  (let [change-data-key (dispatcher/change-data-key (router/current-org-slug))
        change-data (get-in db change-data-key)
        change-map (or (get change-data board-uuid) {})
        new-change-map (assoc change-map property timestamp)
        new-change-data (assoc change-data board-uuid new-change-map)]
    (assoc-in db change-data-key new-change-data)))

(defn- board-change [db board-uuid change-at]
  (timbre/debug "Board change:" board-uuid "at:" change-at)
  (utils/after 1000 (fn []
    (let [current-board-data (dispatcher/board-data)]
      (if (= board-uuid (:uuid current-board-data))
        ;; Reload the current board data
        (dispatcher/dispatch! [:board-get (utils/link-for (:links current-board-data) "self")])
        ;; Reload a secondary board data
        (let [boards (:boards (dispatcher/org-data db))
              filtered-boards (filter #(= (:uuid %) board-uuid) boards)]
          (dispatcher/dispatch! [:boards-load-other filtered-boards]))))))
  ;; Update change-data state that the board has a change
  (update-change-data db board-uuid :change-at change-at))

(defn- org-change [db org-uuid change-at]
  (timbre/debug "Org change:" org-uuid "at:" change-at)
  (utils/after 1000 (fn [] (api/get-org (dispatcher/org-data))))
  db)

(defmethod dispatcher/action :container/change
  [db [_ {container-uuid :container-id change-at :change-at user-id :user-id}]]
  (timbre/debug "Container change:" container-uuid "at:" change-at "by:" user-id)
  (when (not= (jwt/user-id) user-id) ; no need to respond to our own events
    (if (= container-uuid (:uuid (dispatcher/org-data)))
      (org-change db container-uuid change-at)
      (board-change db container-uuid change-at))))

(defmethod dispatcher/action :board-seen
  [db [_ {board-uuid :board-uuid}]]
  (timbre/debug "Board seen:" board-uuid)
  ;; Let the change service know we saw the board
  (ws-cc/container-seen board-uuid)
  (let [next-db (dissoc db :no-reset-seen-at)]
    (if (:no-reset-seen-at db)
      ;; Do not update the seen-at if coming from the modal view
      next-db
      ;; Update change-data state that we nav'd to the board
      (update-change-data next-db board-uuid :nav-at (oc-time/current-timestamp)))))

(defmethod dispatcher/action :board-nav-away
  [db [_ {board-uuid :board-uuid}]]
  (timbre/debug "Board nav away:" board-uuid)
  (let [next-db (dissoc db :no-reset-seen-at)]
    (if (:no-reset-seen-at db)
      ;;  Do not update seen-at if navigating to an activity modal of the current board
      next-db
      ;; Update change-data state that we saw the board
      (update-change-data next-db board-uuid :seen-at (oc-time/current-timestamp)))))

(defmethod dispatcher/action :board
  [db [_ board-data]]
  (let [org (router/current-org-slug)
        is-currently-shown (= (router/current-board-slug) (:slug board-data))
        fixed-board-data (utils/fix-board board-data)
        db-loading (if (and is-currently-shown
                            (router/current-activity-id)
                            (contains? (:fixed-items fixed-board-data) (router/current-activity-id)))
                     (dissoc db :loading)
                     db)]
    (when is-currently-shown

      (when (and (router/current-activity-id)
                 (not (contains? (:fixed-items fixed-board-data) (router/current-activity-id))))
        (router/nav! (oc-urls/board (router/current-org-slug) (:slug board-data))))

      ;; Tell the container service that we are seeing this board,
      ;; and update change-data to reflect that we are seeing this board
      (when-let [board-uuid (:uuid fixed-board-data)]
        (utils/after 10 #(dispatcher/dispatch! [:board-seen {:board-uuid board-uuid}])))
      ;; only watch the currently visible board.
      (when (jwt/jwt) ; only for logged in users
        (ws-ic/board-unwatch (fn [rep]
          (timbre/debug rep "Watching on socket " (:uuid fixed-board-data))
          (ws-ic/board-watch (:uuid fixed-board-data)))))

      ;; Load the other boards
      (utils/after 2000 #(dispatcher/dispatch! [:boards-load-other (:boards (dispatcher/org-data db))])))
    (let [old-board-data (get-in db (dispatcher/board-data-key (router/current-org-slug) (keyword (:slug board-data))))
          with-current-edit (if (and is-currently-shown
                                     (:entry-editing db))
                              old-board-data
                              fixed-board-data)
          next-db (assoc-in db-loading
                   (dispatcher/board-data-key (router/current-org-slug) (keyword (:slug board-data)))
                   with-current-edit)
          without-loading (if is-currently-shown
                            (dissoc next-db :loading)
                            next-db)]
      without-loading)))

(defmethod dispatcher/action :auth-settings
  [db [_ body]]
  (let [next-db (assoc db :latest-auth-settings (.getTime (js/Date.)))]
    (if body
      ; auth settings loaded
      (do
        (api/get-current-user body)
        ;; Start teams retrieve if we have a link
        (when (utils/link-for (:links body) "collection")
          (utils/after 5000 #(dispatcher/dispatch! [:teams-get])))
        (cond
          ; if showing the create organization UI load the list of teams
          ; if a link for it is present
          ; to use the team name as suggestion and to PATCH the name back
          ; if it doesn't has one yet
          (or (utils/in? (:route @router/path) "create-org")
              (utils/in? (:route @router/path) "sign-up"))
          (utils/after 100 #(when (utils/link-for (:links (:auth-settings db)) "collection")
                              (api/get-teams (:auth-settings db))))
          ; confirm email invitation
          (and (utils/in? (:route @router/path) "confirm-invitation")
               (contains? (:query-params @router/path) :token)
               (not (contains? db :email-confirmed)))
          (utils/after 100 #(api/confirm-invitation (:token (:query-params @router/path)) ua/invitation-confirmed)))
        (assoc next-db :auth-settings body))
      ; if the auth-settings call failed retry it in 2 seconds
      (let [auth-settings-retry (or (:auth-settings-retry db) 1000)]
        (utils/after auth-settings-retry #(api/get-auth-settings))
        (assoc next-db :auth-settings-retry (* auth-settings-retry 2))))))

;; This should be turned into a proper form library
;; Lomakeets FormState ideas seem like a good start:
;; https://github.com/metosin/lomakkeet/blob/master/src/cljs/lomakkeet/core.cljs

(defmethod dispatcher/action :input [db [_ path value]]
  (assoc-in db path value))

(defmethod dispatcher/action :update [db [_ path value-fn]]
  (if (fn? value-fn)
    (update-in db path value-fn)
    db))

;; Stripe Payment related actions

(defmethod dispatcher/action :subscription
  [db [_ {:keys [uuid] :as data}]]
  (if uuid
    (assoc-in db [:subscription uuid] data)
    (assoc db :subscription nil)))

;; Auth settings
(defmethod dispatcher/action :auth-settings-get
  [db [_]]
  (api/get-auth-settings)
  db)

(defmethod dispatcher/action :teams-get
  [db [_]]
  (if (utils/link-for (:links (:auth-settings db)) "collection")
    (do
      (api/get-teams (:auth-settings db))
      (assoc db :teams-data-requested true))
    db))

(defmethod dispatcher/action :teams-loaded
  [db [_ teams]]
  (doseq [team teams
          :let [team-link (utils/link-for (:links team) "item")
                roster-link (utils/link-for (:links team) "roster")]]
    ; team link may not be present for non-admins, if so they can still get team users from the roster
    (when team-link
      (api/get-team team-link))
    (when roster-link
      (api/get-team roster-link)))
  (assoc-in db [:teams-data :teams] teams))

(defmethod dispatcher/action :team-loaded
  [db [_ team-data]]
  (if team-data
    (do
      (when (= (:team-id (dispatcher/org-data db)) (:team-id team-data))
        (utils/after 100 #(dispatcher/dispatch! [:channels-enumerate (:team-id team-data)])))
      ;; if team is the current org team, load the slack chennels
      (assoc-in db (dispatcher/team-data-key (:team-id team-data)) team-data))
    db))

(defmethod dispatcher/action :team-roster-loaded
  [db [_ roster-data]]
  (if roster-data
    (let [fixed-roster-data {:team-id (:team-id roster-data)
                             :links (-> roster-data :collection :links)
                             :users (-> roster-data :collection :items)}]
      (assoc-in db (dispatcher/team-roster-key (:team-id roster-data)) fixed-roster-data))
    db))

(defmethod dispatcher/action :channels-enumerate
  [db [_ team-id]]
  (api/enumerate-channels team-id)
  (assoc db :enumerate-channels-requested true))

(defmethod dispatcher/action :channels-enumerate/success
  [db [_ team-id channels]]
  (let [channels-key (dispatcher/team-channels-key team-id)]
    (if channels
      (assoc-in db channels-key channels)
      (-> db
        (update-in (butlast channels-key) dissoc (last channels-key))
        (dissoc :enumerate-channels-requested)))))

(defn invite-user [org-data team-data invite-data]
  (let [invite-from (:type invite-data)
        email (:user invite-data)
        slack-user (:user invite-data)
        user-type (:role invite-data)
        parsed-email (when (= "email" invite-from) (utils/parse-input-email email))
        email-name (:name parsed-email)
        email-address (:address parsed-email)
        ;; check if the user being invited by email is already present in the users list.
        ;; from slack is not possible to select a user already invited since they are filtered by status before
        user  (when (= invite-from "email")
                (first (filter #(= (:email %) email-address) (:users team-data))))
        old-user-type (when user (utils/get-user-type user org-data))]
    ;; Send the invitation only if the user is not part of the team already
    ;; or if it's still pending, ie resend the invitation email
    (if (or (not user)
            (and user
                 (= (string/lower-case (:status user)) "pending")))
      (let [splitted-name (string/split email-name #"\s")
            name-size (count splitted-name)
            splittable-name? (= name-size 2)
            first-name (cond
                        (and (= invite-from "email") (= name-size 1)) email-name
                        (and (= invite-from "email") splittable-name?) (first splitted-name)
                        (and (= invite-from "slack") (seq (:first-name slack-user))) (:first-name slack-user)
                        :else "")
            last-name (cond
                        (and (= invite-from "email") splittable-name?) (second splitted-name)
                        (and (= invite-from "slack") (seq (:last-name slack-user))) (:last-name slack-user)
                        :else "")]
        ;; If the user is already in the list
        ;; but the type changed we need to change the user type too
        (when (and user
                  (not= old-user-type user-type))
          (api/switch-user-type
           invite-data
           old-user-type
           user-type
           user
           (utils/get-author (:user-id user) (:authors org-data))))
        (api/send-invitation
         invite-data
         (if (= invite-from "email") email-address slack-user)
         invite-from user-type first-name last-name)
        {:success true})
      {:error "User already active" :success false})))

(defn valid-inviting-user? [user]
  (or (and (= "email" (:type user))
           (utils/valid-email? (:user user)))
      (and (= "slack" (:type user))
           (map? (:user user))
           (contains? (:user user) :slack-org-id)
           (contains? (:user user) :slack-id))))

(defn duplicated-email-addresses [user users-list]
  (when (= (:type user) "email")
    (> (count (filter #(= (:user %) (:address (utils/parse-input-email (:user user)))) users-list)) 1)))

(defn duplicated-team-user [user users-list]
  (when (= (:type user) "email")
    (let [parsed-email (utils/parse-input-email (:user user))
          dup-user (first (filter #(= (:email %) (:address parsed-email)) users-list))]
      (and dup-user
           (not= (string/lower-case (:status dup-user)) "pending")))))

(defmethod dispatcher/action :invite-users
  [db [_]]
  (let [org-data (dispatcher/org-data)
        team-data (dispatcher/team-data (:team-id org-data))
        invite-users (:invite-users db)
        checked-users (for [user invite-users]
                        (let [valid? (valid-inviting-user? user)
                              intive-duplicated? (duplicated-email-addresses user invite-users)
                              team-duplicated? (duplicated-team-user user (:users team-data))]
                          (cond
                            (not valid?)
                            (merge user {:error true :success false})
                            team-duplicated?
                            (merge user {:error "User already active" :success false})
                            intive-duplicated?
                            (merge user {:error "Duplicated email address" :success false})
                            :else
                            (dissoc user :error))))
        cleaned-invite-users (filterv #(not (:error %)) checked-users)]
    (when (= (count cleaned-invite-users) (count invite-users))
      (doseq [user cleaned-invite-users]
        (invite-user org-data team-data user)))
    (assoc db :invite-users (vec checked-users))))

(defmethod dispatcher/action :invite-user/success
  [db [_ user]]
  ; refresh the users list once the invitation succeded
  (api/get-teams (:auth-settings db))
  (let [inviting-users (:invite-users db)
        next-inviting-users (utils/vec-dissoc inviting-users user)]
    (assoc db :invite-users next-inviting-users)))

(defmethod dispatcher/action :invite-user/failed
  [db [_ user]]
  (let [invite-users (:invite-users db)
        idx (utils/index-of invite-users #(= (:user %) (:user user)))
        next-invite-users (assoc-in invite-users [idx :error] true)]
    (assoc db :invite-users next-invite-users)))

(defmethod dispatcher/action :user-action
  [db [_ team-id invitation action method other-link-params payload]]
  (let [team-data (dispatcher/team-data team-id)
        idx (.indexOf (:users team-data) invitation)]
    (if (> idx -1)
      (do
        (api/user-action (utils/link-for (:links invitation) action method other-link-params) payload)
        (assoc-in db (concat (dispatcher/team-data-key team-id) [:users idx :loading]) true))
      db)))

(defmethod dispatcher/action :user-action/complete
  [db [_]]
  ; refresh the list of users once the invitation action complete
  (api/get-teams (:auth-settings db))
  db)

(defmethod dispatcher/action :name-pswd-collect
  [db [_]]
  (let [form-data (:collect-name-pswd db)]
    (api/collect-name-password (:firstname form-data) (:lastname form-data) (:pswd form-data)))
  (dissoc db :latest-entry-point :latest-auth-settings))

(defn update-user-data [db user-data]
  (let [with-fixed-avatar (if (empty? (:avatar-url user-data))
                            (assoc user-data :avatar-url (utils/cdn default-avatar-url true))
                            user-data)
        with-empty-password (assoc with-fixed-avatar :password "")
        with-has-changes (assoc with-empty-password :has-changes false)]
    (-> db
        (assoc :current-user-data with-fixed-avatar)
        (assoc :edit-user-profile with-has-changes)
        (dissoc :edit-user-profile-failed))))

(defmethod dispatcher/action :name-pswd-collect/finish
  [db [_ status user-data]]
  (if (and status
           (>= status 200)
           (<= status 299))
    (do
      (cook/remove-cookie! :show-login-overlay)
      (dissoc (update-user-data db user-data) :show-login-overlay))
    (assoc db :collect-name-password-error status)))

(defmethod dispatcher/action :pswd-collect
  [db [_ password-reset?]]
  (let [form-data (:collect-pswd db)]
    (api/collect-password (:pswd form-data)))
  (-> db
    (assoc :is-password-reset password-reset?)
    (dissoc :latest-entry-point :latest-auth-settings)))

(defmethod dispatcher/action :pswd-collect/finish
  [db [_ status]]
  (if (and (>= status 200)
           (<= status 299))
    (do
      (if (:is-password-reset db)
        (do
          (cook/remove-cookie! :show-login-overlay)
          (utils/after 200 #(router/nav! oc-urls/login)))
        (do
          (cook/set-cookie!
           (router/show-nux-cookie (jwt/user-id))
           (:new-user router/nux-cookie-values)
           (* 60 60 24 7))
          (router/nav! oc-urls/confirm-invitation-profile)))
      (dissoc db :show-login-overlay))
    (assoc db :collect-password-error status)))

(defmethod dispatcher/action :mobile-menu-toggle
  [db [_]]
  (if (responsive/is-mobile-size?)
    (update-in db [:mobile-menu-open] not)
    db))

(defmethod dispatcher/action :site-menu-toggle
  [db [_ force-close]]
  (let [next-site-menu-open (if (or force-close
                                    (not (responsive/is-mobile-size?)))
                              false
                              (not (:site-menu-open db)))]
    (assoc db :site-menu-open next-site-menu-open)))

(defmethod dispatcher/action :user-profile-reset
  [db [_]]
  (update-user-data db (:current-user-data db)))

(defmethod dispatcher/action :user-data
  [db [_ user-data]]
  (update-user-data db user-data))

(defmethod dispatcher/action :user-profile-save
  [db [_]]
  (let [new-password (:password (:edit-user-profile db))
        password-did-change (pos? (count new-password))
        with-pswd (if (and password-did-change
                           (>= (count new-password) 8))
                    (:edit-user-profile db)
                    (dissoc (:edit-user-profile db) :password))
        new-email (:email (:edit-user-profile db))
        email-did-change (not= new-email (:email (:current-user-data db)))
        with-email (if (and email-did-change
                            (utils/valid-email? new-email))
                     (assoc with-pswd :email new-email)
                     (assoc with-pswd :email (:email (:current-user-data db))))
        without-has-changes (dissoc with-email :has-changes :loading)]
    (api/patch-user-profile (:current-user-data db) without-has-changes))
  (-> db
    (assoc-in [:edit-user-profile :loading] true)
    (dissoc :latest-entry-point :latest-auth-settings)))

(defmethod dispatcher/action :email-domain-team-add
  [db [_]]
  (let [domain (:domain (:um-domain-invite db))]
    (when (utils/valid-domain? domain)
      (api/add-email-domain (if (.startsWith domain "@") (subs domain 1) domain))))
  (assoc db :add-email-domain-team-error false))

(defmethod dispatcher/action :email-domain-team-add/finish
  [db [_ success]]
  (when success
    (api/get-teams (:auth-settings db)))
  (-> db
      (assoc-in [:um-domain-invite :domain] (if success "" (:domain (:um-domain-invite db))))
      (assoc :add-email-domain-team-error (if success false true))))

(defmethod dispatcher/action :slack-team-add
  [db [_]]
  (let [org-data (dispatcher/org-data db)
        team-id (:team-id org-data)
        team-data (dispatcher/team-data team-id)
        user-data (:current-user-data db)
        add-slack-team-link (utils/link-for (:links team-data) "authenticate" "GET" {:auth-source "slack"})
        fixed-add-slack-team-link (utils/slack-link-with-state
                                   (:href add-slack-team-link)
                                   (:user-id user-data)
                                   team-id
                                   (router/get-token))]
    (when fixed-add-slack-team-link
      (router/redirect! fixed-add-slack-team-link)))
  db)

(defmethod dispatcher/action :refresh-slack-user
  [db [_]]
  (api/refresh-slack-user)
  db)

(defmethod dispatcher/action :org-create
  [db [_]]
  (let [org-data (:org-editing db)]
    (when-not (string/blank? (:name org-data))
      (api/create-org (:name org-data) (:logo-url org-data) (:logo-width org-data) (:logo-height org-data))))
  (dissoc db :latest-entry-point :latest-auth-settings))

(defmethod dispatcher/action :password-reset
  [db [_]]
  (api/password-reset (:email (:password-reset db)))
  (dissoc db :latest-entry-point :latest-auth-settings))

(defmethod dispatcher/action :password-reset/finish
  [db [_ status]]
  (assoc-in db [:password-reset :success] (and (>= status 200) (<= status 299))))

(defmethod dispatcher/action :board-delete
  [db [_ board-slug]]
  (api/delete-board board-slug)
  (dissoc db :latest-entry-point))

(defmethod dispatcher/action :error-banner-show
  [db [_ error-message error-time]]
  (if (empty? error-message)
    (-> db (dissoc :error-banner-message) (dissoc :error-banner-time))
    (if-not (:error-banner db)
      (-> db
       (assoc :error-banner-message error-message)
       (assoc :error-banner-time error-time))
      db)))

(defmethod dispatcher/action :user-profile-update/failed
  [db [_]]
  (assoc db :edit-user-profile-failed true))

(defmethod dispatcher/action :ws-interaction/comment-delete
  [db [_ comment-data]]
  (let [; Get the current router data
        org-slug   (router/current-org-slug)
        board-slug (router/current-board-slug)
        item-uuid (:uuid (:interaction comment-data))
        activity-uuid (router/current-activity-id)
        comments-key (dispatcher/activity-comments-key org-slug board-slug activity-uuid)
        comments-data (get-in db comments-key)
        new-comments-data (remove #(= item-uuid (:uuid %)) comments-data)]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :ws-interaction/comment-add
  [db [_ interaction-data]]
  (let [; Get the current router data
        org-slug   (router/current-org-slug)
        board-slug (router/current-board-slug)
        is-all-posts (:from-all-posts @router/path)
        activity-uuid (:resource-uuid interaction-data)
        board-key (if is-all-posts
                    (dispatcher/all-posts-key org-slug)
                    (dispatcher/board-data-key org-slug (router/current-board-slug)))
        board-data (get-in db board-key)
        ; Entry data
        fixed-activity-uuid (or (router/current-secure-activity-id) activity-uuid)
        is-secure-activity (router/current-secure-activity-id)
        secure-activity-data (when is-secure-activity
                              (dispatcher/activity-data org-slug board-slug fixed-activity-uuid))
        entry-key (dispatcher/activity-key org-slug board-slug fixed-activity-uuid)
        entry-data (get-in db entry-key)]
    ;; If looking at a secure activity discard all events for other activities
    (if (and is-secure-activity
             (not= (:uuid secure-activity-data) activity-uuid))
      db
      (if entry-data
        ; If the entry is present in the local state
        (let [; get the comment data from the ws message
              comment-data (:interaction interaction-data)
              created-at (:created-at comment-data)
              all-old-comments-data (dispatcher/activity-comments-data fixed-activity-uuid)
              old-comments-data (filterv :links all-old-comments-data)
              ; Add the new comment to the comments list, make sure it's not present already
              new-comments-data (vec (conj (filter #(not= (:created-at %) created-at) old-comments-data) comment-data))
              sorted-comments-data (vec (sort-by :created-at new-comments-data))
              comments-key (dispatcher/activity-comments-key org-slug board-slug fixed-activity-uuid)
              ; update the comments link of the entry
              comments-link-idx (utils/index-of
                                 (:links entry-data)
                                 #(and (= (:rel %) "comments") (= (:method %) "GET")))
              with-increased-count (update-in entry-data [:links comments-link-idx :count] inc)
              old-authors (or (:authors (get (:links entry-data) comments-link-idx)) [])
              new-author (:author comment-data)
              new-authors (if (and old-authors (first (filter #(= (:user-id %) (:user-id new-author)) old-authors)))
                            old-authors
                            (concat [new-author] old-authors))
              with-authors (assoc-in with-increased-count [:links comments-link-idx :authors] new-authors)]
          ;; Refresh the entry data to get the new links to interact with
          (aa/get-entry entry-data)
          (-> db
              (assoc-in comments-key sorted-comments-data)
              (assoc-in (vec (concat board-key [:fixed-items fixed-activity-uuid])) with-authors)))
        ;; the entry is not present, refresh the full board
        (do
          ;; force refresh of board
          (api/get-board (utils/link-for (:links (dispatcher/board-data)) ["item" "self"] "GET"))
          db)))))

(defmethod dispatcher/action :trend-bar-status
  [db [_ status]]
  (assoc db :trend-bar-status status))

(defmethod dispatcher/action :alert-modal-show
  [db [_ modal-data]]
  (assoc db :alert-modal modal-data))

(defmethod dispatcher/action :alert-modal-hide
  [db [_]]
  (assoc-in db [:alert-modal :dismiss] true))

(defmethod dispatcher/action :alert-modal-hide-done
  [db [_]]
  (dissoc db :alert-modal))

(defmethod dispatcher/action :org-edit
  [db [_ org-data]]
  (assoc db :org-editing org-data))

(defmethod dispatcher/action :org-edit-save
  [db [_]]
  (when (:org-editing db)
    (api/patch-org (:org-editing db)))
  db)

(defmethod dispatcher/action :whats-new-modal-show
  [db [_]]
  (assoc db :whats-new-modal true))

(defmethod dispatcher/action :whats-new-modal-hide
  [db [_]]
  (dissoc db :whats-new-modal))

(defmethod dispatcher/action :org-settings-show
  [db [_ panel]]
  (assoc db :org-settings (or panel :main)))

(defmethod dispatcher/action :org-settings-hide
  [db [_]]
  (dissoc db :org-settings))

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
    (assoc-in db (dispatcher/change-data-key (:slug org-data)) new-status-data)))

(defmethod dispatcher/action :made-with-carrot-modal-show
  [db [_]]
  (assoc db :made-with-carrot-modal true))

(defmethod dispatcher/action :made-with-carrot-modal-hide
  [db [_]]
  (dissoc db :made-with-carrot-modal))

(defmethod dispatcher/action :whats-new/finish
  [db [_ whats-new-data]]
  (if whats-new-data
    (let [fixed-whats-new-data (zipmap (map :uuid (:entries whats-new-data)) (:entries whats-new-data))]
      (assoc-in db dispatcher/whats-new-key fixed-whats-new-data))
    db))

(defmethod dispatcher/action :org-redirect
  [db [_ org-data]]
  ;; Show NUX for first ever user when the dashboard is loaded
  (cook/set-cookie!
   (router/show-nux-cookie (jwt/user-id))
   (:first-ever-user router/nux-cookie-values)
   (* 60 60 24 7))
  (when org-data
    (let [org-slug (:slug org-data)]
      (utils/after 100 #(router/redirect! (oc-urls/all-posts org-slug)))))
  db)

(defmethod dispatcher/action :section-edit-save
  [db [_]]
  (let [section-data (:section-editing db)]
    (if (empty? (:links section-data))
      (api/create-board section-data
       (fn [{:keys [success status body]}]
         (let [section-data (when success (json->cljs body))]
           (if (= status 409)
             ; Board name exists
             (dispatcher/dispatch!
              [:input
               [:section-editing :section-name-error]
               "Section name already exists or isn't allowed"])
             (dispatcher/dispatch! [:section-edit-save/finish section-data])))))
      (api/patch-board section-data)))
  db)

(defmethod dispatcher/action :section-edit-save/finish
  [db [_ section-data]]
  (let [org-slug (router/current-org-slug)
        section-slug (:slug section-data)
        board-key (dispatcher/board-data-key org-slug (:slug section-data))
        fixed-section-data (utils/fix-board section-data)]
    (api/get-org (dispatcher/org-data))
    (if (not= (:slug section-data) (router/current-board-slug))
      ;; If creating a new board, redirect to that board page, and watch the new board
      (do
        (utils/after 100 #(router/nav! (oc-urls/board (router/current-org-slug) (:slug section-data))))
        (ws-cc/container-watch [(:uuid section-data)]))
      ;; If updating an existing board, refresh the org data
      (api/get-org (dispatcher/org-data)))
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

(defmethod dispatcher/action :private-section-kick-out-self
  [db [_ user]]
  ;; If the user is self (same user-id) kick out from the current private section
  (when (= (:user-id user) (jwt/user-id))
    (api/remove-user-from-private-board user))
  db)

(defmethod dispatcher/action :private-section-kick-out-self/finish
  [db [_ success]]
  (if success
    ;; Redirect to the first available board
    (let [org-data (dispatcher/org-data db)
          all-boards (:boards org-data)
          current-board-slug (router/current-board-slug)
          except-this-boards (remove #(#{current-board-slug "drafts"} (:slug %)) all-boards)
          redirect-url (if-let [next-board (first except-this-boards)]
                         (oc-urls/board (:slug next-board))
                         (oc-urls/org (router/current-org-slug)))]
     (api/get-org org-data)
     (utils/after 0 #(router/nav! redirect-url))
     ;; Force board editing dismiss
     (dissoc db :section-editing))
    ;; An error occurred while kicking the user out, no-op to let the user retry
    db))