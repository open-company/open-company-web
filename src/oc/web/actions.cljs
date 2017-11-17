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
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
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

(defmethod dispatcher/action :logout [db _]
  (cook/remove-cookie! :jwt)
  (router/redirect! "/")
  (dissoc db :jwt :latest-entry-point :latest-auth-settings))

(defmethod dispatcher/action :entry-point
  [db [_ {:keys [success collection]}]]
  (let [next-db (assoc db :latest-entry-point (.getTime (js/Date.)))]
    (if success
      (let [orgs (:items collection)]
        (when-let [whats-new-link (utils/link-for (:links collection) "whats-new")]
          (api/get-whats-new whats-new-link))
        ;; Skip all the checks below if looking at the secure page
        (when-not (router/current-secure-activity-id)
          (cond
            (and (:slack-lander-check-team-redirect db)
                 (zero? (count orgs)))
            (router/nav! oc-urls/slack-lander-team)
            (and (:email-lander-check-team-redirect db)
                 (zero? (count orgs)))
            (router/nav! oc-urls/sign-up-team)
            ; If i have an org slug let's load the org data
            (router/current-org-slug)
            (if-let [org-data (first (filter #(= (:slug %) (router/current-org-slug)) orgs))]
              (api/get-org org-data)
              (router/redirect-404!))
            ; In password reset flow, when the token is exchanged and the user is authed
            ; i reload the entry point to get the list of orgs
            ; and redirect the user to its first organization
            ; if he has no orgs to the user profile page
            (and (or (utils/in? (:route @router/path) "password-reset")
                     (utils/in? (:route @router/path) "email-verification"))
                 (:first-org-redirect db))
            (let [to-org (utils/get-default-org orgs)]
              (router/redirect! (if to-org (oc-urls/org (:slug to-org)) oc-urls/user-profile)))
            ; If not redirect the user to the first useful org or to the create org UI
            (and (jwt/jwt)
                 (not (utils/in? (:route @router/path) "create-org"))
                 (not (utils/in? (:route @router/path) "user-profile"))
                 (not (utils/in? (:route @router/path) "email-verification"))
                 (not (utils/in? (:route @router/path) "about"))
                 (not (utils/in? (:route @router/path) "features"))
                 (not (utils/in? (:route @router/path) "email-wall"))
                 (not (utils/in? (:route @router/path) "sign-up"))
                 (not (utils/in? (:route @router/path) "confirm-invitation")))
            (let [login-redirect (cook/get-cookie :login-redirect)]
              (cond
                ; redirect to create-company if the user has no companies
                (zero? (count orgs))
                (let [user-data (if (contains? db :current-user-data)
                                  (:current-user-data db)
                                  (jwt/get-contents))]
                  (if (or (and (empty? (:first-name user-data))
                               (empty? (:last-name user-data)))
                          (empty? (:avatar-url user-data)))
                    (router/nav! oc-urls/sign-up-profile)
                    (router/nav! oc-urls/sign-up-team)))
                ; if there is a login-redirect use it
                (and (jwt/jwt) login-redirect)  (do
                                                  (cook/remove-cookie! :login-redirect)
                                                  (router/redirect! login-redirect))
                ; if the user has only one company, send him to the company dashboard
                (pos? (count orgs))        (router/nav! (oc-urls/org (:slug (utils/get-default-org orgs))))))))
        (-> next-db
            (dissoc :loading)
            (assoc :orgs orgs)
            (assoc-in dispatcher/api-entry-point-key (:links collection))
            (dissoc :slack-lander-check-team-redirect :email-lander-check-team-redirect)))
      (-> next-db
        (assoc :error-banner-message utils/generic-network-error)
        (assoc :error-banner-time 0)))))

(defmethod dispatcher/action :org
  [db [_ org-data saved?]]
  (let [boards (:boards org-data)]

    (cond
      ;; If it's all posts page, loads all posts for the current org
      (and (router/current-board-slug)
           (= (router/current-board-slug) "all-posts"))
      (api/get-all-posts org-data)
      ; If there is a board slug let's load the board data
      (router/current-board-slug)
      (if-let [board-data (first (filter #(= (:slug %) (router/current-board-slug)) boards))]
        ; Load the board data since there is a link to the board in the org data
        (when-let [board-link (utils/link-for (:links board-data) ["item" "self"] "GET")]
          (api/get-board board-link))
        ; The board wasn't found, showing a 404 page
        (if (= (router/current-board-slug) "drafts")
          (utils/after 100 #(dispatcher/dispatch! [:board {:slug "drafts" :name "Drafts" :stories []}]))
          (router/nav! (oc-urls/org (router/current-org-slug)))))
      ;; Board redirect handles
      (and (not (utils/in? (:route @router/path) "create-org"))
           (not (utils/in? (:route @router/path) "org-settings-invite"))
           (not (utils/in? (:route @router/path) "org-settings-team"))
           (not (utils/in? (:route @router/path) "org-settings"))
           (not (utils/in? (:route @router/path) "email-verification"))
           (not (utils/in? (:route @router/path) "sign-up"))
           (not (utils/in? (:route @router/path) "email-wall"))
           (not (utils/in? (:route @router/path) "confirm-invitation")))

      (cond
        ;; Redirect to the first board if only one is present
        (>= (count boards) 1)
        (if (responsive/is-tablet-or-mobile?)
          (utils/after 10 #(router/nav! (oc-urls/boards)))
          (let [board-to (utils/get-default-board org-data)]
            (utils/after 10
              #(router/nav!
                 (if board-to
                   (utils/get-board-url (:slug org-data) (:slug board-to))
                   (oc-urls/all-posts (:slug org-data))))))))))

  ;; Change service connection
  (when (jwt/jwt) ; only for logged in users
    (when-let [ws-link (utils/link-for (:links org-data) "changes")]
      (ws-cc/reconnect ws-link (jwt/get-key :user-id) (:slug org-data) (map :uuid (:boards org-data)))))
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
  (let [is-currently-shown (= (router/current-board-slug) (:slug board-data))
        fixed-board-data (utils/fix-board board-data)
        db-loading (if (and is-currently-shown
                            (router/current-activity-id)
                            (contains? (:fixed-items fixed-board-data) (router/current-activity-id)))
                     (dissoc db :loading)
                     db)]
    (when is-currently-shown

      (when (and (router/current-activity-id)
                 (not (contains? (:fixed-items fixed-board-data) (router/current-activity-id))))
        (router/nav! (utils/get-board-url (router/current-org-slug) (:slug board-data))))

      (when (and (string? (:board-filters db))
                 (not= (:board-filters db) "uncategorized")
                 (zero? (count (filter #(= (:slug %) (:board-filters db)) (:topics board-data)))))
        (router/redirect-404!))

      ;; Follow the interactions link to connect to the Interaction service WebSocket to watch for comment/reaction
      ;; changes, this will also disconnect prior connection if we were watching another board already
      (when (jwt/jwt)
        (when-let [ws-link (utils/link-for (:links board-data) "interactions")]
          (ws-ic/reconnect ws-link (jwt/get-key :user-id))))

      ;; Tell the container service that we are seeing this board,
      ;; and update change-data to reflect that we are seeing this board
      (when-let [board-uuid (:uuid fixed-board-data)]
        (utils/after 10 #(dispatcher/dispatch! [:board-seen {:board-uuid board-uuid}])))

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
          (utils/after 100 #(api/confirm-invitation (:token (:query-params @router/path)))))
        (assoc next-db :auth-settings body))
      ; if the auth-settings call failed retry it in 2 seconds
      (let [auth-settings-retry (or (:auth-settings-retry db) 1000)]
        (utils/after auth-settings-retry #(api/get-auth-settings))
        (assoc next-db :auth-settings-retry (* auth-settings-retry 2))))))

(defmethod dispatcher/action :entry [db [_ {:keys [entry-uuid body]}]]
  (let [is-all-posts (or (:from-all-posts @router/path) (= (router/current-board-slug) "all-posts"))
        board-key (if is-all-posts
                   (dispatcher/all-posts-key (router/current-org-slug))
                   (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug)))
        board-data (get db board-key)
        new-entries (assoc (get board-data :fixed-items)
                     entry-uuid
                     (utils/fix-entry body board-data (:topics board-data)))
        new-board-data (assoc board-data :fixed-items new-entries)]
  (assoc db board-key new-board-data)))

;; This should be turned into a proper form library
;; Lomakeets FormState ideas seem like a good start:
;; https://github.com/metosin/lomakkeet/blob/master/src/cljs/lomakkeet/core.cljs

(defmethod dispatcher/action :input [db [_ path value]]
  (assoc-in db path value))

;; Store JWT in App DB so it can be easily accessed in actions etc.

(defmethod dispatcher/action :jwt
  [db [_ jwt-data]]
  (let [next-db (if (cook/get-cookie :show-login-overlay)
                  (assoc db :show-login-overlay (keyword (cook/get-cookie :show-login-overlay)))
                  db)]
    (when (and (cook/get-cookie :show-login-overlay)
               (not= (cook/get-cookie :show-login-overlay) "collect-name-password")
               (not= (cook/get-cookie :show-login-overlay) "collect-password"))
      (cook/remove-cookie! :show-login-overlay))
    (assoc next-db :jwt (jwt/get-contents))))

;; Stripe Payment related actions

(defmethod dispatcher/action :subscription
  [db [_ {:keys [uuid] :as data}]]
  (if uuid
    (assoc-in db [:subscription uuid] data)
    (assoc db :subscription nil)))

(defmethod dispatcher/action :login-overlay-show
 [db [_ show-login-overlay]]
 (cond
    (= show-login-overlay :login-with-email)
    (-> db
      (assoc :show-login-overlay show-login-overlay)
      (assoc :login-with-email {:email "" :pswd ""})
      (dissoc :login-with-email-error))
    (= show-login-overlay :signup-with-email)
    (-> db
      (assoc :show-login-overlay show-login-overlay)
      (assoc :signup-with-email {:firstname "" :lastname "" :email "" :pswd ""})
      (dissoc :signup-with-email-error))
    :else
    (assoc db :show-login-overlay show-login-overlay)))

(defmethod dispatcher/action :login-with-slack
  [db [_]]
  (let [current (router/get-token)
        auth-url (utils/link-for (:links (:auth-settings db)) "authenticate" "GET" {:auth-source "slack"})
        auth-url-with-redirect (utils/slack-link-with-state
                                (:href auth-url)
                                nil
                                "open-company-auth" oc-urls/slack-lander-check)]
    (when (and (not (.startsWith current oc-urls/login))
               (not (.startsWith current oc-urls/sign-up))
               (not (cook/get-cookie :login-redirect)))
        (cook/set-cookie! :login-redirect current (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
    (router/redirect! auth-url-with-redirect))
  (dissoc db :latest-auth-settings :latest-entry-point))

(defmethod dispatcher/action :bot-auth
  [db [_]]
  (let [current (router/get-token)
        org-data (dispatcher/org-data db)
        team-id (:team-id org-data)
        team-data (dispatcher/team-data team-id)
        auth-link (utils/link-for (:links team-data) "bot")
        user-data (:current-user-data db)
        fixed-auth-url (utils/slack-link-with-state (:href auth-link) (:user-id user-data) team-id (router/get-token))]
    (router/redirect! fixed-auth-url))
  db)

(defmethod dispatcher/action :login-with-email
  [db [_]]
  (api/auth-with-email (:email (:login-with-email db)) (:pswd (:login-with-email db)))
  (dissoc db :login-with-email-error :latest-auth-settings :latest-entry-point))

(defmethod dispatcher/action :login-with-email/failed
  [db [_ error]]
  (assoc db :login-with-email-error error))

(defmethod dispatcher/action :login-with-email/success
  [db [_ jwt]]
  (if (empty? jwt)
    (do
      (utils/after 10 #(router/nav! (str oc-urls/email-wall "?e=" (:email (:signup-with-email db)))))
      db)
    (do
      (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
      (api/get-entry-point)
      (dissoc db :show-login-overlay))))

(defmethod dispatcher/action :auth-with-token
  [db [ _ token-type]]
  (api/auth-with-token (:token (:query-params @router/path)))
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
  (api/get-entry-point)
  (api/get-auth-settings)
  (when (= (:auth-with-token-type db) :password-reset)
    (cook/set-cookie! :show-login-overlay "collect-password"))
  (assoc db :email-verification-success true))

(defmethod dispatcher/action :signup-with-email
  [db [_]]
  (api/signup-with-email
   (or (:firstname (:signup-with-email db)) "")
   (or (:lastname (:signup-with-email db)) "")
   (:email (:signup-with-email db))
   (:pswd (:signup-with-email db)))
  (dissoc db :signup-with-email-error :latest-auth-settings :latest-entry-point))

(defmethod dispatcher/action :signup-with-email/failed
  [db [_ status]]
  (assoc-in db [:signup-with-email :error] status))

(defmethod dispatcher/action :signup-with-email/success
  [db [_ status jwt]]
  (cond
    (= status 204) ;; Email wall since it's a valid signup w/ non verified email address
    (do
      (utils/after 10 #(router/nav! (str oc-urls/email-wall "?e=" (:email (:signup-with-email db)))))
      db)
    (= status 200) ;; Valid login, not signup, redirect to home
    (if (or
          (and (empty? (:first-name jwt)) (empty? (:last-name jwt)))
          (empty? (:avatar-url jwt)))
      (do
        (utils/after 200 #(router/nav! oc-urls/sign-up-profile))
        (api/get-entry-point)
        db)
      (do
        (api/get-entry-point)
        (assoc db :email-lander-check-team-redirect true)))
    :else ;; Valid signup let's collect user data
    (do
      (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
      (utils/after 200 #(router/nav! oc-urls/sign-up-profile))
      (api/get-entry-point)
      (dissoc db :signup-with-email-error))))

(defmethod dispatcher/action :auth-settings-get
  [db [_]]
  (api/get-auth-settings)
  db)

(defmethod dispatcher/action :entry-point-get
  [db [_ flags]]
  (utils/after 100 #(api/get-entry-point))
  (if (map? flags)
    (merge db flags)
    db))

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
    (assoc-in db (dispatcher/team-data-key (:team-id team-data)) team-data)
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

(defmethod dispatcher/action :invitation-confirmed
  [db [_ status]]
  (when (= status 201)
    (api/get-entry-point)
    (api/get-auth-settings))
  (-> db
    (assoc :email-confirmed (= status 201))
    (dissoc :latest-entry-point :latest-auth-settings)))

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
          (router/nav! oc-urls/login))
        (router/nav! oc-urls/confirm-invitation-profile))
      (dissoc db :show-login-overlay))
    (assoc db :collect-password-error status)))

(defmethod dispatcher/action :mobile-menu-toggle
  [db [_]]
  (if (responsive/is-mobile-size?)
    (update-in db [:mobile-menu-open] not)
    db))

(defn sort-reactions [entry]
  (let [reactions (:reactions entry)
        sorted-reactions (vec (sort-by :reaction reactions))]
    (assoc entry :reactions sorted-reactions)))

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

(defmethod dispatcher/action :private-board-add
  [db [_]]
  (let [board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        user-type (:selected-user-type (:private-board-invite db))
        user-id (:selected-user-id (:private-board-invite db))
        board-data (dispatcher/board-data db)
        viewers (:viewers board-data)
        authors (:authors board-data)
        new-viewers (if (= user-type :viewer)
                      (conj viewers {:user-id user-id :loading true})
                      (filterv #(not= (:user-id %) user-id) viewers))
        new-authors (if (= user-type :author)
                      (conj authors {:user-id user-id :loading true})
                      (filterv #(not= (:user-id %) user-id) authors))
        new-board-data (merge board-data {:viewers new-viewers
                                          :authors new-authors})]
    (api/add-private-board board-data user-id user-type)
    (assoc-in db board-key new-board-data)))

(defmethod dispatcher/action :private-board-action
  [db [_ user-id user-type link]]
  (let [board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        board-data (dispatcher/board-data db)
        viewers (:viewers board-data)
        authors (:authors board-data)
        user (if (= user-type :viewer)
                (first (filter #(= (:user-id %) user-id) viewers))
                (first (filter #(= (:user-id %) user-id) authors)))
        new-viewers (if (= user-type :viewer)
                      (conj (filterv #(= (:user-id %) user-id) viewers) (assoc user :loading true))
                      viewers)
        new-authors (if (= user-type :authors)
                      (conj (filterv #(= (:user-id %) user-id) authors) (assoc user :loading true))
                      authors)
        new-board-data (merge board-data {:viewers new-viewers
                                          :authors new-authors})]
    (api/private-board-user-action user link)
    (assoc-in db board-key new-board-data)))

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

(defmethod dispatcher/action :comments-get
  [db [_ activity-data]]
  (api/get-comments activity-data)
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        activity-uuid (or (router/current-activity-id) (router/current-secure-activity-id))
        comments-key (dispatcher/activity-comments-key org-slug board-slug activity-uuid)]
    (assoc-in db (vec (conj (vec (butlast comments-key)) :loading)) true)))

(defmethod dispatcher/action :comments-get/finish
  [db [_ {:keys [success error body activity-uuid]}]]
  (let [fixed-activity-uuid (or (router/current-activity-id) (router/current-secure-activity-id))
        comments-key (dispatcher/activity-comments-key
                      (router/current-org-slug)
                      (router/current-board-slug) fixed-activity-uuid)
        sorted-comments (vec (sort-by :created-at (:items (:collection body))))]
    (-> db
      (assoc-in comments-key sorted-comments)
      (assoc-in (vec (conj (vec (butlast comments-key)) :loading)) false))))

(defmethod dispatcher/action :comment-add
  [db [_ activity-data comment-body]]
  (api/add-comment activity-data comment-body)
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        comments-key (dispatcher/activity-comments-key org-slug board-slug (:uuid activity-data))
        comments-data (get-in db comments-key)
        new-comments-data (conj comments-data {:body comment-body
                                               :created-at (utils/as-of-now)
                                               :author {:name (jwt/get-key :name)
                                                        :avatar-url (jwt/get-key :avatar-url)
                                                        :user-id (jwt/get-key :user-id)}})]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :comment-add/finish
  [db [_ {:keys [activity-uuid]}]]
  (api/get-comments activity-uuid)
  (assoc db :comment-add-finish true))

(defmethod dispatcher/action :reaction-toggle
  [db [_ activity-uuid reaction-data]]
  (let [org-slug (router/current-org-slug)
        is-all-posts (:from-all-posts @router/path)
        ;; if we are coming from all-posts
        board-key (if is-all-posts
                   ;; We need to update the entry in all-posts data, not in the board data
                   (dispatcher/all-posts-key org-slug)
                   (dispatcher/board-data-key org-slug (router/current-board-slug)))
        board-data (get-in db board-key)
        entry-data (get (get board-data :fixed-items) activity-uuid)
        old-reactions-loading (or (:reactions-loading entry-data) [])
        next-reactions-loading (conj old-reactions-loading (:reaction reaction-data))
        updated-entry-data (assoc entry-data :reactions-loading next-reactions-loading)
        entry-key (concat board-key [:fixed-items activity-uuid])]
    (api/toggle-reaction activity-uuid reaction-data)
    (assoc-in db entry-key updated-entry-data)))

(defmethod dispatcher/action :reaction-toggle/finish
  [db [_ activity-uuid reaction reaction-data]]
  (let [is-all-posts (:from-all-posts @router/path)
        org-slug (router/current-org-slug)
        board-key (if is-all-posts
                   (dispatcher/all-posts-key org-slug)
                   (dispatcher/board-data-key org-slug (router/current-board-slug)))
        board-data (get-in db board-key)
        entry-data (get-in board-data [:fixed-items activity-uuid])
        next-reactions-loading (utils/vec-dissoc (:reactions-loading entry-data) reaction)
        entry-key (concat board-key [:fixed-items activity-uuid])]
    (if (nil? reaction-data)
      (let [updated-entry-data (assoc entry-data :reactions-loading next-reactions-loading)]
        (assoc-in db entry-key updated-entry-data))
      (let [reaction (first (keys reaction-data))
            next-reaction-data (assoc (get reaction-data reaction) :reaction (name reaction))
            reactions-data (:reactions entry-data)
            reaction-idx (utils/index-of reactions-data #(= (:reaction %) (name reaction)))
            updated-entry-data (-> entry-data
                                (assoc :reactions-loading next-reactions-loading)
                                (assoc-in [:reactions reaction-idx] next-reaction-data))]
        (assoc-in db entry-key updated-entry-data)))))

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
          (api/get-entry entry-data)
          ; ;; Animate the comments count if we don't have already the same number of comments locally
          ; (when (not= (count all-old-comments-data) (count new-comments-data))
          ;   (utils/pulse-comments-count fixed-activity-uuid))
          ; Update the local state with the new comments list
          (-> db
              (assoc-in comments-key sorted-comments-data)
              (assoc-in (vec (concat board-key [:fixed-items fixed-activity-uuid])) with-authors)))
        ;; the entry is not present, refresh the full topic
        (do
          ;; force refresh of topic
          (api/get-board (utils/link-for (:links (dispatcher/board-data)) ["item" "self"] "GET"))
          db)))))

(defn- update-reaction
  "Need to update the local state with the data we have, if the interaction is from the actual unchecked-short
   we need to refresh the entry since we don't have the links to delete/add the reaction."
  [db interaction-data add-event?]
  (let [; Get the current router data
        org-slug (router/current-org-slug)
        is-all-posts (:from-all-posts @router/path)
        board-slug (router/current-board-slug)
        activity-uuid (:resource-uuid interaction-data)
        ; Board data
        board-key (if is-all-posts (dispatcher/all-posts-key org-slug) (dispatcher/board-data-key org-slug board-slug))
        board-data (get-in db board-key)
        ; Entry data
        fixed-activity-uuid (or (router/current-secure-activity-id) activity-uuid)
        is-secure-activity (router/current-secure-activity-id)
        secure-activity-data (when is-secure-activity
                              (dispatcher/activity-data org-slug board-slug fixed-activity-uuid))
        entry-key (dispatcher/activity-key org-slug board-slug fixed-activity-uuid)
        entry-data (get-in db entry-key)]
    (if (and is-secure-activity
             (not= (:uuid secure-activity-data) activity-uuid))
      db
      (if (and entry-data (seq (:reactions entry-data)))
        ; If the entry is present in the local state and it has reactions
        (let [reaction-data (:interaction interaction-data)
              old-reactions-data (:reactions entry-data)
              reaction-idx (utils/index-of old-reactions-data #(= (:reaction %) (:reaction reaction-data)))
              new-reaction-data {:count (:count interaction-data)}
              is-current-user (= (jwt/get-key :user-id) (:user-id (:author reaction-data)))
              with-reacted (if is-current-user
                              (assoc new-reaction-data :reacted add-event?)
                              new-reaction-data)
              ; Update the reactions data with the new reaction
              new-reactions-data (update-in old-reactions-data [reaction-idx] merge with-reacted)
              ; Update the entry with the new reaction
              updated-entry-data (assoc entry-data :reactions new-reactions-data)]
          ;; Refresh the topic data if the action coming in is from the current user
          ;; to get the new links to interact with
          (when is-current-user
            (api/get-entry entry-data))
          ; ;; Animate the interaction count
          ; (when (not= (:count (get old-reactions-data reaction-idx)) (:count interaction-data))
          ;   (utils/pulse-reaction-count fixed-activity-uuid (:reaction reaction-data)))
          ; Update the entry in the local state with the new reaction
          (assoc-in db entry-key updated-entry-data))
        ;; the entry is not present, refresh the full topic
        (do
          ;; force refresh of topic
          (api/get-board (utils/link-for (:links (dispatcher/board-data)) ["item" "self"] "GET"))
          db)))))

(defmethod dispatcher/action :ws-interaction/reaction-add
  [db [_ interaction-data]]
  (update-reaction db interaction-data true))

(defmethod dispatcher/action :ws-interaction/reaction-delete
  [db [_ interaction-data]]
  (update-reaction db interaction-data false))

(defmethod dispatcher/action :trend-bar-status
  [db [_ status]]
  (assoc db :trend-bar-status status))

(defmethod dispatcher/action :activity-modal-fade-in
  [db [_ board-slug activity-uuid activity-type editing]]
  (utils/after 10
   #(let [from-all-posts (= (router/current-board-slug) "all-posts")
          activity-url (oc-urls/entry board-slug activity-uuid)]
      (router/nav! (str activity-url (when from-all-posts "?ap")))))
  (-> db
    (assoc :activity-modal-fade-in activity-uuid)
    (assoc :modal-editing editing)
    (assoc :dismiss-modal-on-editing-stop editing)
    ;; Make sure the seen-at is not reset when navigating to modal view
    (assoc :no-reset-seen-at true)))

(defmethod dispatcher/action :entry-edit
  [db [_ initial-entry-data]]
  (assoc db :entry-editing initial-entry-data))

(defmethod dispatcher/action :entry-edit/dismiss
  [db [_]]
  ;; If the user was looking at the modal, dismiss it too
  (when (router/current-activity-id)
    (utils/after 1 #(let [board-filters (:board-filters db)
                          from-all-posts (or
                                          (:from-all-posts @router/path)
                                          (= (router/current-board-slug) "all-posts"))
                          board-url (utils/get-board-url (router/current-org-slug) (router/current-board-slug))]
                      (router/nav!
                        (cond
                          ; AA
                          from-all-posts
                          (oc-urls/all-posts (router/current-org-slug))
                          ; Board with topic filter
                          (string? board-filters)
                          (oc-urls/board-filter-by-topic
                           (router/current-org-slug)
                           (router/current-board-slug)
                           board-filters)
                          ;; Board most recent or by topic
                          :else
                          board-url)))))
  ;; Add :entry-edit-dissmissing for 1 second to avoid reopening the activity modal after edit is dismissed.
  (utils/after 1000 #(dispatcher/dispatch! [:input [:entry-edit-dissmissing] false]))
  (-> db
    (dissoc :entry-editing)
    (assoc :entry-edit-dissmissing true)))

(defmethod dispatcher/action :topic-add
  [db [_ topic-map edit-key]]
  (let [board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        board-data (get-in db board-key)
        next-topics (conj (:topics board-data) topic-map)
        next-board-data (assoc board-data :topics next-topics)
        next-db (assoc-in db board-key next-board-data)]
    (if edit-key
      (update-in next-db [edit-key] merge {:topic-slug (:slug topic-map)
                                           :topic-name (:name topic-map)
                                           :has-changes true})
      next-db)))

(defmethod dispatcher/action :entry-save
  [db [_]]
  (let [entry-data (:entry-editing db)]
    (if (:links entry-data)
      (let [redirect-board-slug (if (= (:status entry-data) "published") (router/current-board-slug) "drafts")]
        (api/update-entry entry-data redirect-board-slug :entry-editing))
      (let [org-slug (router/current-org-slug)
            entry-board-key (dispatcher/board-data-key org-slug (:board-slug entry-data))
            entry-board-data (get-in db entry-board-key)
            entry-create-link (utils/link-for (:links entry-board-data) "create")]
        (api/create-entry entry-data entry-create-link)))
    (assoc-in db [:entry-editing :loading] true)))

(defmethod dispatcher/action :entry-save/finish
  [db [_ {:keys [activity-data edit-key]}]]
  (let [board-slug (:board-slug activity-data)
        is-all-posts (or (:from-all-posts @router/path) (= (router/current-board-slug) "all-posts"))]
    ;; FIXME: refresh the last loaded all-posts link
    (when-not is-all-posts
      (api/get-board (utils/link-for (:links (dispatcher/board-data)) ["item" "self"] "GET")))
    (api/get-org (dispatcher/org-data))
    ; Add the new activity into the board
    (let [board-key (dispatcher/board-data-key (router/current-org-slug) board-slug)
          board-data (get-in db board-key)
          fixed-activity-data (utils/fix-entry activity-data board-data (:topics board-data))
          next-fixed-items (assoc (:fixed-items board-data) (:uuid fixed-activity-data) fixed-activity-data)
          next-db (assoc-in db (vec (conj board-key :fixed-items)) next-fixed-items)
          with-edited-key (if edit-key
                            (update-in next-db [edit-key] dissoc :loading)
                            next-db)]
      with-edited-key)))

(defmethod dispatcher/action :entry-save/failed
  [db [_ edit-key]]
  (-> db
    (update-in [edit-key] dissoc :loading)
    (update-in [edit-key] assoc :error true)))

(defmethod dispatcher/action :entry-publish
  [db [_]]
  (let [entry-data (:entry-editing db)
        entry-exists? (seq (:links entry-data))
        board-data-key (dispatcher/board-data-key (router/current-org-slug) (:board-slug entry-data))
        board-data (get-in db board-data-key)
        publish-entry-link (if entry-exists?
                            ;; If the entry already exists use the publish link in it
                            (utils/link-for (:links entry-data) "publish")
                            ;; If the entry is new, use
                            (utils/link-for (:links board-data) "create"))]
    (api/publish-entry entry-data publish-entry-link)
    (assoc-in db [:entry-editing :publishing] true)))

(defmethod dispatcher/action :entry-publish/finish
  [db [_ {:keys [activity-data]}]]
  (let [board-slug (:board-slug activity-data)]
    (api/get-org (dispatcher/org-data))
    ; Add the new activity into the board
    (let [board-key (dispatcher/board-data-key (router/current-org-slug) board-slug)
          board-data (get-in db board-key)
          fixed-activity-data (utils/fix-entry activity-data board-data (:topics board-data))
          next-fixed-items (assoc (:fixed-items board-data) (:uuid fixed-activity-data) fixed-activity-data)]
      (-> db
        (assoc-in (vec (conj board-key :fixed-items)) next-fixed-items)
        (update-in [:entry-editing] dissoc :publishing)))))

(defmethod dispatcher/action :entry-publish/failed
  [db [_]]
  (-> db
    (update-in [:entry-editing] dissoc :publishing)
    (update-in [:entry-editing] assoc :error true)))

(defmethod dispatcher/action :activity-delete
  [db [_ activity-data]]
  (let [is-all-posts (utils/in? (:route @router/path) "all-posts")
        board-key (if is-all-posts
                   (dispatcher/all-posts-key (router/current-org-slug))
                   (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug)))
        board-data (get-in db board-key)
        next-fixed-items (dissoc (:fixed-items board-data) (:uuid activity-data))
        next-board-data (assoc board-data :fixed-items next-fixed-items)]
    (api/delete-activity activity-data)
    (assoc-in db board-key next-board-data)))

(defmethod dispatcher/action :activity-delete/finish
  [db [_]]
  (api/get-board (utils/link-for (:links (dispatcher/board-data)) ["item" "self"] "GET"))
  ;; Reload the org to update the number of drafts in the navigation
  (when (= (router/current-board-slug) "drafts")
    (api/get-org (dispatcher/org-data))
    (let [org-slug (router/current-org-slug)
          org-data (dispatcher/org-data)
          boards-no-draft (sort-by :name (filterv #(not= (:slug %) "drafts") (:boards org-data)))
          board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
          board-data (get-in db board-key)]
      (when (zero? (count (:fixed-items board-data)))
        (utils/after
         100
         #(router/nav!
            (if (pos? (count boards-no-draft))
              ;; If there is at least one board redirect to it
              (utils/get-board-url org-slug (:slug (first boards-no-draft)))
              ;; If not boards are available redirect to the empty org
              (oc-urls/org org-slug)))))))
  db)

(defmethod dispatcher/action :alert-modal-show
  [db [_ modal-data]]
  (assoc db :alert-modal modal-data))

(defmethod dispatcher/action :alert-modal-hide
  [db [_]]
  (assoc-in db [:alert-modal :dismiss] true))

(defmethod dispatcher/action :alert-modal-hide-done
  [db [_]]
  (dissoc db :alert-modal))

(defmethod dispatcher/action :board-edit
  [db [_ initial-board-data]]
  (let [fixed-board-data (or
                           initial-board-data
                           {:name "" :slug "" :access "team"})]
    (assoc db :board-editing fixed-board-data)))

(defmethod dispatcher/action :board-edit-save
  [db [_]]
  (let [board-data (:board-editing db)]
    (if (and (string/blank? (:slug board-data))
             (not (string/blank? (:name board-data))))
      (api/create-board (:name board-data) (:access board-data))
      (api/patch-board board-data)))
  db)

(defmethod dispatcher/action :board-edit-save/finish
  [db [_ board-data]]
  (let [org-slug (router/current-org-slug)
        board-slug (:slug board-data)
        board-key (dispatcher/board-data-key org-slug (:slug board-data))
        fixed-board-data (utils/fix-board board-data)]
    (api/get-org (dispatcher/org-data))
    (if (not= (:slug board-data) (router/current-board-slug))
      ;; If creating a new board, redirect to that board page, and watch the new board
      (do
        (utils/after 100 #(router/nav! (oc-urls/board (router/current-org-slug) (:slug board-data))))
        (ws-cc/container-watch [(:uuid board-data)]))
      ;; If updating an existing board, refresh the org data
      (api/get-org (dispatcher/org-data)))
  (-> db
    (assoc-in board-key fixed-board-data)
    (dissoc :board-editing))))

(defmethod dispatcher/action :board-edit/dismiss
  [db [_]]
  (dissoc db :board-editing))

(defmethod dispatcher/action :all-posts-get
  [db [_]]
  (api/get-all-posts (dispatcher/org-data db))
  db)

(defmethod dispatcher/action :all-posts-calendar
  [db [_ {:keys [link year month]}]]
  (api/get-all-posts (dispatcher/org-data) link year month)
  db)

(defmethod dispatcher/action :all-posts-get/finish
  [db [_ {:keys [org year month body]}]]
  (if body
    (let [all-posts-key (dispatcher/all-posts-key org)
          fixed-all-posts (utils/fix-all-posts (:collection body))
          with-calendar-data (-> fixed-all-posts
                                (assoc :year year)
                                (assoc :month month)
                                ;; Force the component to trigger a did-remount
                                ;; or it won't see the finish of the loading
                                (assoc :rand (rand 1000)))]
      (assoc-in db all-posts-key with-calendar-data))
    db))

(defmethod dispatcher/action :calendar-get
  [db [_]]
  (api/get-calendar (router/current-org-slug))
  db)

(defmethod dispatcher/action :calendar-get/finish
  [db [_ {:keys [org body]}]]
  (let [calendar-key (dispatcher/calendar-key org)]
    (assoc-in db calendar-key body)))

(defmethod dispatcher/action :all-posts-more
  [db [_ more-link direction]]
  (api/load-more-all-posts more-link direction)
  (let [all-posts-key (dispatcher/all-posts-key (router/current-org-slug))
        all-posts-data (get-in db all-posts-key)
        next-all-posts-data (assoc all-posts-data :loading-more true)]
    (assoc-in db all-posts-key next-all-posts-data)))

; (def default-activity-limit 50)

(defmethod dispatcher/action :all-posts-more/finish
  [db [_ {:keys [org direction body]}]]
  (if body
    (let [all-posts-key (dispatcher/all-posts-key org)
          fixed-all-posts (utils/fix-all-posts (:collection body))
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

(defmethod dispatcher/action :activity-move
  [db [_ activity-data board-data]]
  (let [is-all-posts (or (:from-all-posts @router/path) (= (router/current-board-slug) "all-posts"))
        fixed-activity-data (assoc activity-data :board-slug (:slug board-data))]
    (api/update-entry fixed-activity-data (:slug board-data) nil)
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

(defmethod dispatcher/action :initial-loads
  [db [_]]
  (let [force-refresh (or (utils/in? (:route @router/path) "org")
                          (utils/in? (:route @router/path) "login"))
        latest-entry-point (if (or force-refresh (nil? (:latest-entry-point db))) 0 (:latest-entry-point db))
        latest-auth-settings (if (or force-refresh (nil? (:latest-auth-settings db))) 0 (:latest-auth-settings db))
        now (.getTime (js/Date.))
        reload-time (* 60 60 1000)]
    (when (or (> (- now latest-entry-point) reload-time)
              (and (router/current-org-slug)
                   (nil? (dispatcher/org-data db (router/current-org-slug)))))
      (api/get-entry-point))
    (when (> (- now latest-auth-settings) reload-time)
      (api/get-auth-settings)))
  db)

(defmethod dispatcher/action :onboard-overlay-show
  [db [_]]
  (assoc db :show-onboard-overlay true))

(defmethod dispatcher/action :onboard-overlay-hide
  [db [_]]
  (dissoc db :show-onboard-overlay))

(defmethod dispatcher/action :activity-share-show
  [db [_ activity-data]]
  (-> db
    (assoc :activity-share {:share-data activity-data})
    (dissoc :activity-shared-data)))

(defmethod dispatcher/action :activity-share-hide
  [db [_ activity-data]]
  (dissoc db :activity-share))

(defmethod dispatcher/action :activity-share-reset
  [db [_]]
  (dissoc db :activity-shared-data))

(defmethod dispatcher/action :activity-share
  [db [_ share-data]]
  (api/share-activity (:share-data (:activity-share db)) share-data)
  (assoc db :activity-share-data share-data))

(defmethod dispatcher/action :activity-share/finish
  [db [_ shared-data]]
  (assoc db :activity-shared-data (utils/fix-entry shared-data (:board-slug shared-data) nil)))

(defmethod dispatcher/action :made-with-carrot-modal-show
  [db [_]]
  (assoc db :made-with-carrot-modal true))

(defmethod dispatcher/action :made-with-carrot-modal-hide
  [db [_]]
  (dissoc db :made-with-carrot-modal))

(defmethod dispatcher/action :secure-activity-get
  [db [_]]
  (api/get-secure-activity (router/current-org-slug) (router/current-secure-activity-id))
  db)

(defmethod dispatcher/action :activity-get/finish
  [db [_ status activity-data]]
  (let [next-db (if (= status 404)
                  (dissoc db :latest-entry-point)
                  db)]
    (when (= status 404)
      ; (router/redirect-404!)
      (if (router/current-secure-activity-id)
        (router/redirect-404!)
        (router/nav! (utils/get-board-url (router/current-org-slug) (router/current-board-slug)))))
    (when (and (router/current-secure-activity-id)
             (jwt/jwt)
             (jwt/user-is-part-of-the-team (:team-id activity-data)))
      (router/nav! (oc-urls/entry (router/current-org-slug) (:board-slug activity-data) (:uuid activity-data))))
    (let [activity-uuid (:uuid activity-data)
          org-slug (router/current-org-slug)
          board-slug (router/current-board-slug)
          activity-key (if (router/current-secure-activity-id)
                         (dispatcher/secure-activity-key org-slug (router/current-secure-activity-id))
                         (dispatcher/activity-key org-slug board-slug activity-uuid))
          fixed-activity-data (utils/fix-entry
                               activity-data
                               {:slug (or (:board-slug activity-data) board-slug)
                                :name (:board-name activity-data)} nil)]
      (when (jwt/jwt)
        (when-let [ws-link (utils/link-for (:links fixed-activity-data) "interactions")]
          (ws-ic/reconnect ws-link (jwt/get-key :user-id))))
      (-> next-db
        (dissoc :activity-loading)
        (assoc-in activity-key fixed-activity-data)))))

(defmethod dispatcher/action :entry-modal-save
  [db [_ board-slug]]
  (let [entry-data (:modal-editing-data db)]
    (api/update-entry entry-data board-slug :modal-editing-data)
    (assoc-in db [:modal-editing-data :loading] true)))

(defmethod dispatcher/action :activity-modal-edit
  [db [_ activate]]
  (assoc db :modal-editing activate))

(defmethod dispatcher/action :whats-new/finish
  [db [_ whats-new-data]]
  (if whats-new-data
    (let [fixed-whats-new-data (zipmap (map :uuid (:entries whats-new-data)) (:entries whats-new-data))]
      (assoc-in db dispatcher/whats-new-key fixed-whats-new-data))
    db))