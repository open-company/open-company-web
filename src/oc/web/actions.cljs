(ns oc.web.actions
  (:require [medley.core :as med]
            [clojure.string :as string]
            [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.local-settings :as ls]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.wsclient :as wsc]))

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
  (dissoc db :jwt))

;; Get the board to show counting the last accessed and the last created

(defn newest-org [orgs]
  (first (sort-by :created-at orgs)))

(defn get-default-org [orgs]
  (if-let [last-org-slug (cook/get-cookie (router/last-org-cookie))]
    (let [last-org (first (filter #(= (:slug %) last-org-slug) orgs))]
      (if last-org
        ; Get the last accessed board from the saved cookie
        last-org
        ; Fallback to the newest board if the saved board was not found
        (newest-org orgs)))
    (newest-org orgs)))

;; Get the board to show counting the last accessed and the last created

(defn newest-board [boards]
  (first (sort-by :name boards)))

(defn get-default-board [org-data]
  (if-let [last-board-slug (cook/get-cookie (router/last-board-cookie (:slug org-data)))]
    (let [board (first (filter #(= (:slug %) last-board-slug) (:boards org-data)))]
      (if board
        ; Get the last accessed board from the saved cookie
        board
        ; Fallback to the newest board if the saved board was not found
        (newest-board (:boards org-data))))
    (newest-board (:boards org-data))))

(defmethod dispatcher/action :entry-point [db [_ {:keys [success collection]}]]
  (if success
    (let [orgs (:items collection)]
      (cond
        ; If i have an org slug let's load the org data
        (router/current-org-slug)
        (if-let [org-data (first (filter #(= (:slug %) (router/current-org-slug)) orgs))]
          (api/get-org org-data)
          (when (not (utils/in? (:route @router/path) "su-snapshot"))
            (router/redirect-404!)))
        ; In password reset flow, when the token is exchanged and the user is authed
        ; i reload the entry point to get the list of orgs
        ; and redirect the user to its first organization
        ; if he has no orgs to the user profile page
        (and (or (utils/in? (:route @router/path) "password-reset")
                 (utils/in? (:route @router/path) "email-verification"))
             (:first-org-redirect db))
        (let [to-org (get-default-org orgs)]
          (router/redirect! (if to-org (oc-urls/org (:slug to-org)) oc-urls/user-profile)))
        ; If not redirect the user to the first useful org or to the create org UI
        (and (jwt/jwt)
             (not (utils/in? (:route @router/path) "create-org"))
             (not (utils/in? (:route @router/path) "user-profile"))
             (not (utils/in? (:route @router/path) "email-verification"))
             (not (utils/in? (:route @router/path) "about"))
             (not (utils/in? (:route @router/path) "features")))
        (let [login-redirect (cook/get-cookie :login-redirect)]
          (cond
            ; redirect to create-company if the user has no companies
            (zero? (count orgs))   (router/nav! oc-urls/create-org)
            ; if there is a login-redirect use it
            (and (jwt/jwt) login-redirect)  (do
                                              (cook/remove-cookie! :login-redirect)
                                              (router/redirect! login-redirect))
            ; if the user has only one company, send him to the company dashboard
            (pos? (count orgs))        (router/nav! (oc-urls/org (:slug (get-default-org orgs)))))))
      (-> db
          (dissoc :loading)
          (assoc :orgs orgs)
          (assoc-in dispatcher/api-entry-point-key (:links collection))))
    (-> db
      (assoc :error-banner-message utils/generic-network-error)
      (assoc :error-banner-time 0))))

(defmethod dispatcher/action :org [db [_ org-data]]
  (let [boards (:boards org-data)]
    (cond
      ; If there is a board slug let's load the board data
      (router/current-board-slug)
      (if-let [board-data (first (filter #(= (:slug %) (router/current-board-slug)) boards))]
        ; Load the board data since there is a link to the board in the org data
        (api/get-board board-data)
        ; The board wasn't found, showing a 404 page
        (router/redirect-404!))
      ;; If it's all activity page, loads all activity for the current org
      (utils/in? (:route @router/path) "all-activity")
      (api/get-all-activity org-data)
      ;; Board redirect handles
      (and (not (utils/in? (:route @router/path) "create-org"))
           (not (utils/in? (:route @router/path) "org-team-settings"))
           (not (utils/in? (:route @router/path) "org-settings"))
           (not (utils/in? (:route @router/path) "updates-list"))
           (not (utils/in? (:route @router/path) "su-snapshot"))
           (not (utils/in? (:route @router/path) "su-snapshot-preview"))
           (not (utils/in? (:route @router/path) "email-verification")))
      (cond
        ;; Redirect to the first board if only one is present
        (>= (count boards) 1)
        (if (responsive/is-tablet-or-mobile?)
          (router/nav! (oc-urls/boards))
          (let [board-to (get-default-board org-data)]
            (if (= (keyword (cook/get-cookie (router/last-board-filter-cookie (:slug org-data) (:slug board-to)))) :by-topic)
              (router/redirect! (oc-urls/board-sort-by-topic (:slug org-data) (:slug board-to)))
              (router/nav! (oc-urls/board (:slug org-data) (:slug board-to)))))))))
  ;; FIXME: temporarily remove stories loading
  ; (utils/after 100 #(api/get-updates))
  (-> db
    (assoc-in (dispatcher/org-data-key (:slug org-data)) (utils/fix-org org-data))
    (assoc :updates-list-loading (utils/in? (:route @router/path) "updates-list"))))

(defmethod dispatcher/action :boards-load-other [db [_]]
  (doseq [board (:boards (dispatcher/org-data db))
        :when (not= (:slug board) (router/current-board-slug))]
    (api/get-board board))
  db)

(defmethod dispatcher/action :board [db [_ board-data]]
 (let [is-currently-shown (= (:slug board-data) (router/current-board-slug))]
    (when is-currently-shown
      (when (and (router/current-entry-uuid)
                 (zero? (count (filter #(= (:uuid %) (router/current-entry-uuid)) (:entries board-data)))))
        (router/redirect-404!))
      (when (and (string? (:board-filters db))
                 (not= (:board-filters db) "uncategorized")
                 (zero? (count (filter #(= (:slug %) (:board-filters db)) (:topics board-data)))))
        (router/redirect-404!))
      (when (jwt/jwt)
        (when-let [ws-link (utils/link-for (:links board-data) "interactions")]
          (wsc/reconnect ws-link (jwt/get-key :user-id))))
      (utils/after 2000 #(dispatcher/dispatch! [:boards-load-other])))
    (let [fixed-board-data (utils/fix-board board-data)
          old-board-data (get-in db (dispatcher/board-data-key (router/current-org-slug) (keyword (:slug board-data))))
          with-current-edit (if (and is-currently-shown
                                     (:foce-key db))
                              old-board-data
                              fixed-board-data)]
      (-> db
        (assoc-in (dispatcher/board-data-key (router/current-org-slug) (keyword (:slug board-data))) with-current-edit)))))

(defmethod dispatcher/action :new-topics-load/finish [db [_ body]]
  (if body
    ;; signal to the app-state that the new-topics have been loaded
    (-> db
      (assoc-in (dispatcher/board-new-topics-key (router/current-org-slug) (router/current-board-slug)) (:templates (:response body)))
      (assoc-in (dispatcher/board-new-categories-key (router/current-org-slug) (router/current-board-slug)) (:categories (:response body)))
      (dissoc :loading))
    db))

(defmethod dispatcher/action :auth-settings
  [db [_ body]]
  (if body
    ; auth settings loaded
    (do
      (api/get-current-user body)
      (cond
        ; if showing the create organization UI load the list of teams
        ; to use the team name as suggestion and to PATCH the name back
        ; if it doesn't has one yet
        (utils/in? (:route @router/path) "create-org")
        (utils/after 100 #(api/get-teams))
        ; confirm email invitation
        (and (utils/in? (:route @router/path) "confirm-invitation")
             (contains? (:query-params @router/path) :token)
             (not (contains? db :email-confirmed)))
        (utils/after 100 #(api/confirm-invitation (:token (:query-params @router/path)))))
      (assoc db :auth-settings body))
    ; if the auth-settings call failed retry it in 2 seconds
    (let [auth-settings-retry (or (:auth-settings-retry db) 1000)]
      (utils/after auth-settings-retry #(api/get-auth-settings))
      (assoc db :auth-settings-retry (* auth-settings-retry 2)))))

(defmethod dispatcher/action :entry [db [_ {:keys [entry-uuid body]}]]
  ;; FIXME: Disable this until we have editing back working
  (let [board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        board-data (get db board-key)
        entry-idx (utils/index-of (:entries board-data) #(= (:uuid %) entry-uuid))
        new-entries (assoc (:entries board-data) entry-idx (utils/fix-entry body (router/current-board-slug) (:topics board-data)))
        sorted-entries (vec (sort-by :created-at new-entries))
        new-board-data (assoc board-data :entries sorted-entries)]
  (assoc db board-key new-board-data)))

(defn- get-updates [db]
  (api/get-updates)
  (assoc db :updates-list-loading true))

(defmethod dispatcher/action :udpates-list-get [db [_ {:keys [slug response]}]]
  (get-updates db))

(defmethod dispatcher/action :updates-list [db [_ {:keys [response]}]]
  (-> db
    (dissoc :loading)
    (dissoc :updates-list-loading)
    (assoc :updates-list-loaded true)
    (assoc-in (dispatcher/updates-list-key (router/current-org-slug)) (:collection response))))

(defmethod dispatcher/action :update-loaded [db [_ {:keys [org-slug update-slug response load-org-data]}]]
  (let [org-data-keys [:logo-url :logo-width :logo-height :currency]
        org-data      (-> response
                          (select-keys org-data-keys)
                          (assoc :name (:org-name response)))]
    ; load-org-data is used to save the subset of company data that is returned with a stakeholder-update data
    (if load-org-data
      ; save the company data returned with the SU data
      (-> db
        (assoc-in (dispatcher/update-key org-slug update-slug) response)
        (assoc-in (dispatcher/org-data-key org-slug) (utils/fix-org org-data))
        (dissoc :loading))
      ; save only the SU data
      (-> db
        (assoc-in (dispatcher/update-key org-slug update-slug) response)
        (dissoc :loading)))))

(defn start-foce [db topic topic-data]
  (-> db
    (assoc :foce-key (keyword topic)) ; which topic is being FoCE
    (assoc :foce-data topic-data)     ; map of the in progress edits of the topic data
    (assoc :foce-data-editing? false) ; is the data portion of the topic (e.g. finance, growth) being edited
    (assoc :show-top-menu nil)))      ; dismiss top menu

(defn stop-foce [db]
  (let [board-data (dispatcher/board-data db (router/current-org-slug) (router/current-board-slug))
        show-add-topic (zero? (count (:topics board-data)))]
    (-> db
      (dissoc :foce-key)
      (dissoc :foce-data)
      (dissoc :foce-data-editing?))))

;; Front of Card Edit topic
(defmethod dispatcher/action :foce-start [db [_ topic-key topic-data]]
  (if topic-key
    (start-foce db topic-key topic-data)
    (stop-foce db)))

(defmethod dispatcher/action :foce-data-editing-start [db [_ value]]
  (assoc db :foce-data-editing? value))

(defmethod dispatcher/action :foce-input [db [_ topic-data-map]]
  (let [old-data (:foce-data db)]
    (assoc db :foce-data (merge old-data topic-data-map))))

;; This should be turned into a proper form library
;; Lomakeets FormState ideas seem like a good start:
;; https://github.com/metosin/lomakkeet/blob/master/src/cljs/lomakkeet/core.cljs

(defmethod dispatcher/action :input [db [_ path value]]
  (assoc-in db path value))

(defmethod dispatcher/action :topic-archive [db [_ topic]]
  (let [board-data (dispatcher/board-data)
        topic-data ((keyword topic) board-data)
        old-topics (:topics board-data)
        new-topics (utils/vec-dissoc old-topics (name topic))
        old-archived (:archived board-data)
        new-archived (vec (conj old-archived {:title (:title ((keyword topic) board-data)) :topic (name topic)}))
        board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))]
    (api/archive-topic topic topic-data)
    (-> db
      (stop-foce)
      (assoc :prevent-topic-not-found-navigation true)
      (assoc-in (conj board-key :topics) new-topics)
      (assoc-in (conj board-key :archived) new-archived))))

(defmethod dispatcher/action :topic-archive/success
  [db [_]]
  (router/nav! (oc-urls/board))
  (dissoc db :prevent-topic-not-found-navigation))

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
        auth-url-with-redirect (utils/slack-link-with-state (:href auth-url) nil "open-company-auth" oc-urls/login)]
    (when (and (not (.startsWith current oc-urls/login))
               (not (.startsWith current oc-urls/sign-up))
               (not (cook/get-cookie :login-redirect)))
        (cook/set-cookie! :login-redirect current (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
    (router/redirect! auth-url-with-redirect))
  db)

(defmethod dispatcher/action :bot-auth
  [db [_]]
  (let [current (router/get-token)
        org-data (dispatcher/org-data db)
        team-id (:team-id org-data)
        team-data (dispatcher/team-data team-id)
        auth-link (utils/link-for (:links team-data) "bot")
        user-data (:current-user-data db)
        fixed-auth-url (utils/slack-link-with-state (:href auth-link) (:user-id user-data) team-id (oc-urls/org (:slug org-data)))]
    (cook/set-cookie! :login-redirect current (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
    (router/redirect! fixed-auth-url))
  db)

(defmethod dispatcher/action :login-with-email
  [db [_]]
  (api/auth-with-email (:email (:login-with-email db)) (:pswd (:login-with-email db)))
  (dissoc db :login-with-email-error))

(defmethod dispatcher/action :login-with-email/failed
  [db [_ error]]
  (assoc db :login-with-email-error error))

(defmethod dispatcher/action :login-with-email/success
  [db [_ jwt]]
  (if (empty? jwt)
    (assoc db :login-with-email-error :verify-email)
    (do
      (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
      (api/get-entry-point)
      (dissoc db :show-login-overlay))))

(defmethod dispatcher/action :auth-with-token
  [db [ _ token-type]]
  (api/auth-with-token (:token (:query-params @router/path)))
  (assoc db :auth-with-token-type token-type))

(defmethod dispatcher/action :auth-with-token/failed
  [db [_ error]]
  (if (= (:auth-with-token-type db) :password-reset)
    (assoc db :collect-pswd-error error)
    (assoc db :email-verification-error error)))

(defmethod dispatcher/action :auth-with-token/success
  [db [_ jwt]]
  (api/get-entry-point)
  (when (= (:auth-with-token-type db) :password-reset)
    (cook/set-cookie! :show-login-overlay "collect-password"))
  (assoc db :first-org-redirect true))

(defmethod dispatcher/action :signup-with-email
  [db [_]]
  (api/signup-with-email (:firstname (:signup-with-email db)) (:lastname (:signup-with-email db)) (:email (:signup-with-email db)) (:pswd (:signup-with-email db)))
  (dissoc db :signup-with-email-error))

(defmethod dispatcher/action :signup-with-email/failed
  [db [_ error]]
  (assoc db :signup-with-email-error error))

(defmethod dispatcher/action :signup-with-email/success
  [db [_ jwt]]
  (if (empty? jwt)
    (assoc db :signup-with-email-error :verify-email)
    (do
      (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
      (api/get-entry-point)
      (dissoc db :show-login-overlay))))

(defmethod dispatcher/action :auth-settings-get
  [db [_]]
  (api/get-auth-settings)
  db)

(defmethod dispatcher/action :teams-get
  [db [_]]
  (api/get-teams)
  (assoc db :teams-data-requested true))

(defmethod dispatcher/action :teams-loaded
  [db [_ teams]]
  (doseq [team teams
          :let [team-link (utils/link-for (:links team) "item" "GET")
                roster-link (utils/link-for (:links team) "roster" "GET")]]
    ; team link may not be present for non-admins, if so they can still get team users from the roster
    (when team-link
      (api/get-team team-link))
    ; ;; FIXME: Re-enable roster loading once it's fixed on auth side
    ; (when roster-link
    ;   (api/get-team roster-link))
    )
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

(defmethod dispatcher/action :invite-user
  [db [_]]
  (let [org-data (dispatcher/org-data)
        invite-data (:um-invite db)
        invite-from (:invite-from invite-data)
        email (:email invite-data)
        slack-user (:slack-user invite-data)
        user-type (:user-type invite-data)
        parsed-email (utils/parse-input-email email)
        email-name (:name parsed-email)
        email-address (:address parsed-email)
        team-data (dispatcher/team-data (:team-id org-data))
        ;; check if the user being invited by email is already present in the users list.
        ;; from slack is not possible to select a user already invited since they are filtered by status before
        user  (when (= invite-from "email")
                (first (filter #(= (:email %) email-address) (:users team-data))))
        old-user-type (when user (utils/get-user-type user org-data))
        new-user-type (:user-type invite-data)]
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
                        (and (= invite-from "slack") (not (empty? (:first-name slack-user)))) (:first-name slack-user)
                        :else "")
            last-name (cond
                        (and (= invite-from "email") splittable-name?) (second splitted-name)
                        (and (= invite-from "slack") (not (empty? (:last-name slack-user)))) (:last-name slack-user)
                        :else "")]
        ;; If the user is already in the list
        ;; but the type changed we need to change the user type too
        (when (and user
                  (not= old-user-type new-user-type))
          (api/switch-user-type old-user-type new-user-type user (utils/get-author (:user-id user) (:authors org-data))))
        (api/send-invitation (if (= invite-from "email") email-address slack-user) invite-from user-type first-name last-name)
        (update-in db [:um-invite] dissoc :error))
      (if (and user
               (not= (string/lower-case (:status user)) "pending"))
        (assoc-in db [:um-invite :error] "User already active.")
        db))))

(defmethod dispatcher/action :invite-user-reset
  [db [_]]
  (update-in db [:um-invite] dissoc :last-action-success))

(defmethod dispatcher/action :invite-user/success
  [db [_]]
  ; refresh the users list once the invitation succeded
  (api/get-teams)
  (assoc db :um-invite {:email ""
                        :user-type nil
                        :invite-from "email"
                        :last-action-success true
                        :error nil}))

(defmethod dispatcher/action :invite-user/failed
  [db [_ email]]
  ; refresh the users list once the invitation succeded
  (api/get-teams)
  (-> db
      (assoc-in [:um-invite :error] true)
      (assoc-in [:um-invite :last-action-success] false)))

(defmethod dispatcher/action :user-action
  [db [_ team-id invitation action method other-link-params payload]]
  (let [team-data (dispatcher/team-data team-id)
        idx (.indexOf (:users team-data) invitation)]
    (api/user-action (utils/link-for (:links invitation) action method other-link-params) payload)
    (assoc-in db (concat (dispatcher/team-data-key team-id) [:users idx :loading]) true)))

(defmethod dispatcher/action :user-action/complete
  [db [_]]
  ; refresh the list of users once the invitation action complete
  (api/get-teams)
  db)

(defmethod dispatcher/action :invitation-confirmed
  [db [_ status]]
  (when (= status 201)
    (cook/set-cookie! :show-login-overlay "collect-name-password"))
  (assoc db :email-confirmed (= status 201)))

(defmethod dispatcher/action :name-pswd-collect
  [db [_]]
  (let [form-data (:collect-name-pswd db)]
    (api/collect-name-password (:firstname form-data) (:lastname form-data) (:pswd form-data)))
  db)

(defn update-user-data [db user-data]
  (-> db
      (assoc :current-user-data user-data)
      (assoc :edit-user-profile user-data)
      (dissoc :edit-user-profile-failed)))

(defmethod dispatcher/action :name-pswd-collect/finish
  [db [_ status user-data]]
  (if (and status
           (>= status 200)
           (<= status 299))
    (do
      (cook/remove-cookie! :show-login-overlay)
      (-> (update-user-data db user-data)
          (dissoc :show-login-overlay)))
    (assoc db :collect-name-password-error status)))

(defmethod dispatcher/action :pswd-collect
  [db [_]]
  (let [form-data (:collect-pswd db)]
    (api/collect-password (:pswd form-data)))
  db)

(defmethod dispatcher/action :pswd-collect/finish
  [db [_ status]]
  (if (and (>= status 200)
           (<= status 299))
    (do
      (cook/remove-cookie! :show-login-overlay)
      (dissoc db :show-login-overlay))
    (assoc db :collect-password-error status)))

(defmethod dispatcher/action :mobile-menu-toggle
  [db [_]]
  (if (responsive/is-mobile-size?)
    (assoc db :mobile-menu-open (not (:mobile-menu-open db)))
    db))

(defmethod dispatcher/action :reset-updates-list
  [db [_]]
  ; Reset flag to reload su list when needed
  (dissoc db :updates-list-loaded))

(defn sort-reactions [entry]
  (let [reactions (:reactions entry)
        sorted-reactions (vec (sort-by :reaction reactions))]
    (assoc entry :reactions sorted-reactions)))

(defmethod dispatcher/action :top-menu-show [db [_ topic]]
  (assoc db :show-top-menu topic))

(defmethod dispatcher/action :welcome-screen-hide
  [db [_]]
  (dissoc db :show-welcome-screen))

(defmethod dispatcher/action :user-profile-reset
  [db [_]]
  (-> db
    (assoc :edit-user-profile (assoc (:current-user-data db) :password ""))
    (dissoc :edit-user-profile-failed)))

(defmethod dispatcher/action :user-data
  [db [_ user-data]]
  (update-user-data db user-data))

(defmethod dispatcher/action :user-profile-save
  [db [_]]
  (let [new-password (:password (:edit-user-profile db))
        password-did-change (pos? (count new-password))
        with-pswd (if (and password-did-change
                           (>= (count new-password) 5))
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
  (assoc-in db [:edit-user-profile :loading] true))

(defmethod dispatcher/action :email-domain-team-add
  [db [_]]
  (let [domain (:domain (:um-domain-invite db))]
    (when (utils/valid-domain? domain))
      (api/add-email-domain (if (.startsWith domain "@") (subs domain 1) domain)))
  (assoc db :add-email-domain-team-error false))

(defmethod dispatcher/action :email-domain-team-add/finish
  [db [_ success]]
  (when success
    (api/get-teams))
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
        fixed-add-slack-team-link (utils/slack-link-with-state (:href add-slack-team-link) (:user-id user-data) team-id (oc-urls/org-team-settings (:slug org-data)))]
    (when fixed-add-slack-team-link
      (router/redirect! fixed-add-slack-team-link)))
  db)

(defmethod dispatcher/action :refresh-slack-user
  [db [_]]
  (api/refresh-slack-user)
  db)

(defmethod dispatcher/action :set-board-cache!
  [db [_ k v]]
  (let [cache-key (dispatcher/board-cache-key (router/current-org-slug) (router/current-board-slug))]
    (if (nil? v)
      (update-in db cache-key dissoc k)
      (assoc-in db (conj cache-key k) v))))

(defmethod dispatcher/action :org-create
  [db [_]]
  (let [org-data (:create-org db)]
    (when-not (string/blank? (:name org-data))
      (api/create-org (:name org-data) (:logo-url org-data))))
  db)

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
                      (vec (filter #(not= (:user-id %) user-id) viewers)))
        new-authors (if (= user-type :author)
                      (conj authors {:user-id user-id :loading true})
                      (vec (filter #(not= (:user-id %) user-id) authors)))
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
                      (conj (vec (filter #(= (:user-id %) user-id) viewers)) (assoc user :loading true))
                      viewers)
        new-authors (if (= user-type :authors)
                      (conj (vec (filter #(= (:user-id %) user-id) authors)) (assoc user :loading true))
                      authors)
        new-board-data (merge board-data {:viewers new-viewers
                                          :authors new-authors})]
    (api/private-board-user-action user link)
    (assoc-in db board-key new-board-data)))

(defmethod dispatcher/action :password-reset
  [db [_]]
  (api/password-reset (:email (:password-reset db)))
  db)

(defmethod dispatcher/action :password-reset/finish
  [db [_ status]]
  (assoc-in db [:password-reset :success] (and (>= status 200) (<= status 299))))

(defmethod dispatcher/action :board-delete
  [db [_ board-slug]]
  (api/delete-board board-slug)
  db)

(defmethod dispatcher/action :error-banner-show
  [db [_ error-message error-time]]
  (if (empty? error-message)
    (-> db (dissoc :error-banner-message) (dissoc :error-banner-time))
    (if (not (:error-banner db))
      (-> db
       (assoc :error-banner-message error-message)
       (assoc :error-banner-time error-time))
      db)))

(defmethod dispatcher/action :user-profile-update/failed
  [db [_]]
  (assoc db :edit-user-profile-failed true))

(defmethod dispatcher/action :comments-get
  [db [_ entry-uuid]]
  (api/get-comments entry-uuid)
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        entry-uuid (router/current-entry-uuid)
        comments-key (dispatcher/comments-key org-slug board-slug entry-uuid)]
    (assoc-in db comments-key {:loading true})))

(defmethod dispatcher/action :comments-get/finish
  [db [_ {:keys [success error body]}]]
  (let [comments-key (dispatcher/comments-key (router/current-org-slug) (router/current-board-slug) (router/current-entry-uuid))
        sorted-comments (vec (sort-by :created-at (:items (:collection body))))]
    (assoc-in db comments-key sorted-comments)))

(defmethod dispatcher/action :comment-add
  [db [_ entry-uuid comment-body]]
  (api/add-comment entry-uuid comment-body)
  (let [org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        comments-key (dispatcher/comments-key org-slug board-slug entry-uuid)
        comments-data (get-in db comments-key)
        new-comments-data (conj comments-data {:body comment-body
                                               :created-at (utils/as-of-now)
                                               :author {:name (jwt/get-key :name)
                                                        :avatar-url (jwt/get-key :avatar-url)
                                                        :user-id (jwt/get-key :user-id)}})]
    (assoc-in db comments-key new-comments-data)))

(defmethod dispatcher/action :comment-add/finish
  [db [_ {:keys [entry-uuid]}]]
  (api/get-comments entry-uuid)
  db)

(defmethod dispatcher/action :reaction-toggle
  [db [_ entry-uuid reaction-data]]
  (let [board-key (dispatcher/board-data (router/current-org-slug) (router/current-board-slug))
        board-data (get-in db board-key)
        entry-idx (utils/index-of (:entries board-data) #(= (:uuid %) entry-uuid))
        entry-data (get (:entries board-data) entry-idx)
        old-reactions-loading (or (:reactions-loading entry-data) [])
        next-reactions-loading (conj old-reactions-loading (:reaction reaction-data))
        updated-entry-data (assoc entry-data :reactions-loading next-reactions-loading)
        entry-key (concat board-key [:entries entry-idx])]
    (api/toggle-reaction entry-uuid reaction-data)
    (assoc-in db entry-key updated-entry-data)))

(defmethod dispatcher/action :reaction-toggle/finish
  [db [_ entry-uuid reaction reaction-data]]
  (let [board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        board-data (get-in db board-key)
        entry-idx (utils/index-of (:entries board-data) #(= (:uuid %) entry-uuid))
        entry-data (get-in board-data [:entries entry-idx])
        next-reactions-loading (utils/vec-dissoc (:reactions-loading entry-data) reaction)
        entry-key (concat board-key [:entries entry-idx])]
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
        entry-uuid (:entry-uuid interaction-data)
        board-key (dispatcher/board-data-key org-slug board-slug)
        board-data (get-in db board-key)
        ; Entry data
        entry-idx (utils/index-of (:entries board-data) #(= (:uuid %) entry-uuid))
        entry-data (get (:entries board-data) entry-idx)]
    (if entry-data
      ; If the entry is present in the local state
      (let [; get the comment data from the ws message
            comment-data (:interaction interaction-data)
            created-at (:created-at comment-data)
            all-old-comments-data (dispatcher/comments-data entry-uuid)
            old-comments-data (vec (filter :links all-old-comments-data))
            ; Add the new comment to the comments list, make sure it's not present already
            new-comments-data (vec (conj (filter #(not= (:created-at %) created-at) old-comments-data) comment-data))
            sorted-comments-data (vec (sort-by :created-at new-comments-data))
            comments-key (dispatcher/comments-key org-slug board-slug entry-uuid)
            current-user-id (jwt/get-key :user-id)
            is-current-user (= current-user-id (:user-id (:author comment-data)))
            ; update the comments link of the entry
            comments-link-idx (utils/index-of (:links entry-data) #(and (= (:rel %) "comments") (= (:method %) "GET")))
            with-increased-count (update-in entry-data [:links comments-link-idx :count] inc)
            old-authors (or (:authors (get (:links entry-data) comments-link-idx)) [])
            new-author (assoc (select-keys (:current-user-data db) [:user-id :avatar-url :name]) :created-at (utils/as-of-now))
            new-authors (if (and old-authors (first (filter #(= (:user-id %) current-user-id) old-authors)))
                          old-authors
                          (conj old-authors new-author))
            with-authors (assoc-in with-increased-count [:links comments-link-idx :authors] new-authors)]
        ;; Refresh the topic data if the action coming in is from the current user
        ;; to get the new links to interact with
        (when is-current-user
          (api/get-entry entry-data))
        ;; Animate the comments count if we don't have already the same number of comments locally
        (when (not= (count all-old-comments-data) (count new-comments-data))
          (utils/pulse-comments-count entry-uuid))
        ; Update the local state with the new comments list
        (-> db
            (assoc-in comments-key sorted-comments-data)
            (assoc-in (concat board-key [:entries entry-idx]) with-authors)))
      ;; the entry is not present, refresh the full topic
      (do
        ;; force refresh of topic
        (api/get-board (dispatcher/board-data))
        db))))

(defn- update-reaction
  "Need to update the local state with the data we have, if the interaction is from the actual unchecked-short
   we need to refresh the entry since we don't have the links to delete/add the reaction."
  [db interaction-data add-event?]
  (let [; Get the current router data
        org-slug (router/current-org-slug)
        board-slug (router/current-board-slug)
        entry-uuid (:entry-uuid interaction-data)
        ; Entry data
        entry-data (dispatcher/entry-data entry-uuid)
        ; Board data
        board-key (dispatcher/board-data-key org-slug board-slug)
        board-data (dispatcher/board-data)
        ; Entry idx
        entry-idx (utils/index-of (:entries board-data) #(= (:uuid %) entry-uuid))
        entry-key (concat board-key [:entries entry-idx])]
    (if (and entry-data (not (empty? (:reactions entry-data))))
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
            new-reactions-data (assoc old-reactions-data reaction-idx (merge (get old-reactions-data reaction-idx) with-reacted))
            ; Update the entry with the new reaction
            updated-entry-data (assoc entry-data :reactions new-reactions-data)]
        ;; Refresh the topic data if the action coming in is from the current user
        ;; to get the new links to interact with
        (when is-current-user
          (api/get-entry entry-data))
        (when (not= (:count (get old-reactions-data reaction-idx)) (:count interaction-data))
          (utils/pulse-reaction-count entry-uuid (:reaction reaction-data)))
        ; Update the entry in the local state with the new reaction
        (assoc-in db entry-key updated-entry-data))
      ;; the entry is not present, refresh the full topic
      (do
        ;; force refresh of topic
        (api/get-board (dispatcher/board-data))
        db))))

(defmethod dispatcher/action :ws-interaction/reaction-add
  [db [_ interaction-data]]
  (update-reaction db interaction-data true))

(defmethod dispatcher/action :ws-interaction/reaction-delete
  [db [_ interaction-data]]
  (update-reaction db interaction-data false))

(defmethod dispatcher/action :trend-bar-status
  [db [_ status]]
  (assoc db :trend-bar-status status))

(defmethod dispatcher/action :entry-modal-fade-in
  [db [_ board-slug entry-uuid]]
  (utils/after 10
   #(let [new-route [(router/current-org-slug) "all-activity" board-slug entry-uuid "entry"]
          parts {:org (router/current-org-slug) :board board-slug :entry entry-uuid :query-params (:query-params @router/path) :from-all-activity (not (router/current-board-slug))}]
      (router/set-route! new-route parts)
      (.pushState (.-history js/window) #js {} (.-title js/document) (oc-urls/entry board-slug entry-uuid))
      (reset! dispatcher/app-state (assoc @dispatcher/app-state :entry-pushed entry-uuid))))
  (assoc db :entry-modal-fade-in entry-uuid))

(defmethod dispatcher/action :entry-edit
  [db [_ initial-entry-data]]
  (assoc db :entry-editing initial-entry-data))

(defmethod dispatcher/action :entry-edit/dismiss
  [db [_ show?]]
  (dissoc db :entry-editing))

(defmethod dispatcher/action :topic-add
  [db [_ topic-map use-in-new-entry?]]
  (let [board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        board-data (get-in db board-key)
        next-topics (conj (:topics board-data) topic-map)
        next-board-data (assoc board-data :topics next-topics)
        next-db (assoc-in db board-key next-board-data)]
    (if use-in-new-entry?
      (assoc next-db :entry-editing (merge (:entry-editing next-db) {:topic-slug (:slug topic-map)
                                                                     :topic-name (:name topic-map)
                                                                     :has-changes true}))
      next-db)))

(defn author-data [current-user-data as-of]
  {:avatar-url (:avatar-url current-user-data)
   :name (str (:first-name current-user-data) " " (:last-name current-user-data))
   :user-id (:user-id current-user-data)
   :updated-at as-of})

(defn new-entry-fixed-data [entry-data board-data current-user-data as-of]
  (utils/fix-entry (merge entry-data {:author [(author-data current-user-data as-of)]
                                      :created-at as-of
                                      :updated-at as-of
                                      :reactions []
                                      :uuid (utils/entry-uuid)})
                   (:slug board-data)
                   (:topics board-data)))

(defn entry-fixed-data [entry-data current-user-data as-of]
  (let [new-author (author-data current-user-data as-of)]
    (merge entry-data {:updated-at as-of
                       :author (if (sequential? (:author entry-data))
                                 (conj (:author entry-data) new-author)
                                 [(:author entry-data) new-author])})))

(defmethod dispatcher/action :entry-save
  [db [_]]
  (let [entry-data (:entry-editing db)
        board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        board-data (get-in db board-key)
        as-of (utils/as-of-now)
        new-entry? (empty? (:uuid entry-data))
        current-user-data (:current-user-data db)
        fixed-entry (if new-entry?
                      (new-entry-fixed-data entry-data board-data current-user-data as-of)
                      (entry-fixed-data entry-data current-user-data as-of))
        filtered-entries (filter #(not= (:uuid %) (:uuid fixed-entry)) (:entries board-data))
        new-entries (conj filtered-entries fixed-entry)
        sorted-entries (vec (sort-by :created-at new-entries))
        next-board-data (assoc board-data :entries sorted-entries)
        next-board-filters (if (= (:board-filters db) (:topic-slug entry-data))
                              ; if it's filtering by the same topic of the new entry leave it be
                              (:board-filters db)
                              (if (keyword? (:board-filters db))
                                ; if it's different but it's a keyword it means it's sorting (by latest or topic)
                                (:board-filters db)
                                ; else sort by latest because it's filtering by a different topic
                                :latest))]
    (if new-entry?
      (api/create-entry entry-data)
      (api/update-entry entry-data))
    (-> db
        (assoc-in board-key next-board-data)
        (assoc :board-filters next-board-filters))))

(defmethod dispatcher/action :entry-save/finish
  [db [_]]
  (api/get-board (dispatcher/board-data))
  db)

(defmethod dispatcher/action :board-nav
  [db [_ board-slug board-filters]]
  (let [next-board-filter (if board-filters
                            ; If a board filter is passed use it
                            board-filters
                            (if (:entry-pushed db)
                              ; If the modal was open from the dashboard, restore the previous opened filter
                              (:board-filters db)
                              ; If it was open directly from a link restore the last opened dashboard sort
                              (or (keyword (cook/get-cookie (router/last-board-filter-cookie (router/current-org-slug) board-slug))) :latest)))
        next-board-url (if (string? next-board-filter)
                         (oc-urls/board-filter-by-topic next-board-filter)
                         (if (= next-board-filter :by-topic)
                           (oc-urls/board-sort-by-topic (router/current-org-slug) board-slug)
                           (oc-urls/board (router/current-org-slug) board-slug)))]
    (utils/after 10
      #(if (:entry-pushed db)
         (let [route [(router/current-org-slug) (router/current-board-slug) "dashboard"]
               parts (dissoc @router/path :route :entry)]
            (router/set-route! route parts)
            (.pushState (.-history js/window) #js {} (.-title js/document) next-board-url)
            (reset! dispatcher/app-state (dissoc @dispatcher/app-state :entry-pushed)))
         (router/nav! next-board-url)))
    (assoc db :board-filters next-board-filter)))

(defmethod dispatcher/action :all-activity-nav
  [db [_]]
  (let [all-activity-url (oc-urls/all-activity)]
    (utils/after 10
      #(if (:entry-pushed db)
         (let [route [(router/current-org-slug) "all-activity"]
               parts (dissoc @router/path :route :entry :board)]
            (router/set-route! route parts)
            (.pushState (.-history js/window) #js {} (.-title js/document) all-activity-url)
            (reset! dispatcher/app-state (dissoc @dispatcher/app-state :entry-pushed)))
         (router/nav! all-activity-url)))
    db))

(defmethod dispatcher/action :entry-delete
  [db [_ entry-data]]
  (let [board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        board-data (get-in db board-key)
        filtered-entries (filter #(not= (:uudi %) (:uuid entry-data)) (:entries board-data))
        sorted-entries (vec (sort-by :created-at filtered-entries))
        next-board-data (assoc board-data :entries sorted-entries)]
    (api/delete-entry entry-data)
    (assoc-in db board-key next-board-data)))

(defmethod dispatcher/action :entry-delete/finish
  [db [_]]
  (api/get-board (dispatcher/board-data))
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
  (let [fixed-board-data (if initial-board-data
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

(defmethod dispatcher/action :board-edit/dismiss
  [db [_]]
  (dissoc db :board-editing))

(defmethod dispatcher/action :all-activity-get
  [db [_]]
  (api/get-all-activity (dispatcher/org-data))
  db)

(defmethod dispatcher/action :all-activity-calendar
  [db [_ {:keys [link year month]}]]
  (api/get-all-activity (dispatcher/org-data) link year month)
  db)

(defmethod dispatcher/action :all-activity-get/finish
  [db [_ {:keys [org year month body]}]]
  (if body
    (let [all-activity-key (dispatcher/all-activity-key org)
          fixed-all-activity (utils/fix-all-activity (:collection body))
          sorted-entries (vec (reverse (sort-by :created-at (:entries fixed-all-activity))))
          with-sorted-entries (assoc fixed-all-activity :entries sorted-entries)
          with-calendar-data (-> with-sorted-entries
                                (assoc :year year)
                                (assoc :month month)
                                ;; Force the component to trigger a did-remount
                                ;; or it won't see the finish of the loading
                                (assoc :rand (rand 1000)))]
      (assoc-in db all-activity-key with-calendar-data))
    db))

(defmethod dispatcher/action :calendar-get
  [db [_]]
  (api/get-calendar (router/current-org-slug))
  db)

(defmethod dispatcher/action :calendar-get/finish
  [db [_ {:keys [org body]}]]
  (let [calendar-key (dispatcher/calendar-key org)]
    (assoc-in db calendar-key body)))

(defmethod dispatcher/action :all-activity-more
  [db [_ more-link direction]]
  (api/load-more-all-activity more-link direction)
  (let [all-activity-key (dispatcher/all-activity-key (router/current-org-slug))
        all-activity-data (get-in db all-activity-key)
        next-all-activity-data (assoc all-activity-data :loading-more true)]
    (assoc-in db all-activity-key next-all-activity-data)))

(def default-activity-limit 50)

(defmethod dispatcher/action :all-activity-more/finish
  [db [_ {:keys [org direction body]}]]
  (if body
    (let [all-activity-key (dispatcher/all-activity-key org)
          fixed-all-activity (utils/fix-all-activity (:collection body))
          old-all-activity (get-in db all-activity-key)
          keeping-entries (min default-activity-limit (count (:entries old-all-activity)))
          ;; Keep only x elements before or after the new list
          ; all-activity-entries (if (= direction :up)
          ;                         (concat (:entries fixed-all-activity) (take default-activity-limit (:entries old-all-activity)))
          ;                         (concat (take-last default-activity-limit (:entries old-all-activity)) (:entries fixed-all-activity)))
          ;; Keep all the elements
          all-activity-entries (if (= direction :up)
                                  (concat (:entries fixed-all-activity) (:entries old-all-activity))
                                  (concat (:entries old-all-activity) (:entries fixed-all-activity)))
          new-all-activity (-> fixed-all-activity
                              (assoc :entries (vec (reverse (sort-by :created-at (distinct all-activity-entries)))))
                              (assoc :direction direction)
                              (assoc :saved-entries keeping-entries))]
      (assoc-in db all-activity-key new-all-activity))
    db))