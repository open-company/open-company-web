(ns open-company-web.actions
  (:require [medley.core :as med]
            [clojure.string :as string]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.caches :as cache]
            [open-company-web.api :as api]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.responsive :as responsive]))

;; ---- Generic Actions Dispatch
;; This is a small generic abstraction to handle "actions".
;; An `action` is a transformation on the app state.
;; The return value of an action will be used as the new app-state.

;; The extended multimethod `action` is defined in the dispatcher
;; namespace to avoid cyclical dependencies between namespaces

(defn- log [& args]
  (js/console.log (apply pr-str args)))

(defmethod dispatcher/action :default [db payload]
  (js/console.warn "No handler defined for" (str (first payload)))
  (js/console.log "Full event: " (pr-str payload))
  db)

(defmethod dispatcher/action :logout [db _]
  (cook/remove-cookie! :jwt)
  (router/redirect! "/")
  (dissoc db :jwt))

(defmethod dispatcher/action :entry [db [_ {:keys [links]} redirect-if-necessary]]
  (let [create-link    (med/find-first #(= (:rel %) "company-create") links)
        slug           (fn [co] (last (string/split (:href co) #"/")))
        [first second] (filter #(= (:rel %) "company") links)]
    (when redirect-if-necessary
      (let [login-redirect (cook/get-cookie :login-redirect)]
        (cond
          ; redirect to create-company if the user has no companies
          (and create-link (not first))   (router/nav! oc-urls/create-company)
          ; if there is a login-redirect use it
          (and (jwt/jwt) login-redirect)  (do
                                            (cook/remove-cookie! :login-redirect)
                                            (router/redirect! login-redirect))
          ; if the user has only one company, send him to the company dashboard
          (and first (not second))        (router/nav! (oc-urls/company (slug first)))
          ; if the user has more than one company send him to the companies page
          (and first second)              (router/nav! oc-urls/companies))))
    (assoc
      (if (utils/in? (:route @router/path) "create-company")
        (dissoc db :loading)
        db)
      :api-entry-point
      links)))

(defmethod dispatcher/action :company-submit [db _]
  (api/create-company (:company-editor db))
  db)

(defmethod dispatcher/action :company-created [db [_ body]]
  (if (:links body)
    (let [updated (utils/fix-sections body)]
      (router/redirect! (oc-urls/company (:slug updated)))
      (assoc-in db (dispatcher/company-data-key (:slug updated)) updated))
    db))

(defmethod dispatcher/action :new-section [db [_ body]]
  (if body
    (let [slug (:slug body)
          response (:response body)]
      (swap! cache/new-sections assoc-in [(keyword slug) :new-sections] (:templates response))
      (swap! cache/new-sections assoc-in [(keyword slug) :new-sections-categories] (:categories response))
      ;; signal to the app-state that the new-sections have been loaded
      (-> db
        (assoc-in [(keyword slug) :new-sections] (:templates response))
        (dissoc :loading)))
    db))

(defmethod dispatcher/action :auth-settings [db [_ body]]
  (if body
    ; auth settings loaded
    (when (and (utils/in? (:route @router/path) "confirm-invitation")
                 (contains? (:query-params @router/path) :token))
      ; call confirm-invitation if needed
      (utils/after 100 #(api/confirm-invitation (:token (:query-params @router/path)))))
    ; if the auth-settings call failed retry it in 2 seconds
    (utils/after 2000 #(api/get-auth-settings)))
  (assoc db :auth-settings body))

(defmethod dispatcher/action :revision [db [_ body]]
  (if body
    (let [fixed-section (utils/fix-section (:body body) (:section body) true)
          assoc-in-coll [(:slug body) (:section body) (:as-of body)]
          assoc-in-coll-2 (dispatcher/revision-key (:slug body) (:section body) (:as-of body))
          next-db (assoc-in db assoc-in-coll-2 true)]
      (swap! cache/revisions assoc-in assoc-in-coll fixed-section)
      next-db)
    db))

(defmethod dispatcher/action :section [db [_ {:keys [slug section body]}]]
  ;; Refresh section revisions
  (api/load-revisions slug section (utils/link-for (:links body) "revisions"))
  (if body
    (let [fixed-section (utils/fix-section body section)]
      (assoc-in db (dispatcher/company-section-key slug section) fixed-section))
    db))

(defmethod dispatcher/action :company [db [_ {:keys [slug success status body]}]]
  (cond
    success
    ;; add section name inside each section
    (let [updated-body (utils/fix-sections body)
          company-data-key (dispatcher/company-data-key (:slug updated-body))
          company-sections-key (conj company-data-key :sections)
          section-revisions-data-key (when (:selected-topic-view db) (concat company-data-key [(keyword (:selected-topic-view db)) :revisions-data]))
          company-editing-section-key (when (:foce-key db) (conj company-data-key (keyword (:foce-key db))))
          company-already-loaded-key (conj company-data-key :company-data-loaded)
          already-loaded? (get-in db company-already-loaded-key)
          with-company-data (assoc-in db company-data-key updated-body)
          with-open-add-topic (if (and (not (responsive/is-tablet-or-mobile?))
                                       (zero? (count (:sections updated-body)))
                                       (not already-loaded?))
                                (assoc with-company-data :show-add-topic true)
                                with-company-data)
          with-welcome-screen (if (and (not (responsive/is-tablet-or-mobile?))
                                       (not (:foce-key db))
                                       (zero? (count (:sections updated-body)))
                                       (zero? (count (:archived updated-body)))
                                       (not already-loaded?))
                                (assoc with-open-add-topic :show-welcome-screen true)
                                with-open-add-topic)
          keep-sections-edits (if (not (nil? (:foce-key db)))
                                (assoc-in with-welcome-screen company-sections-key (get-in db company-sections-key))
                                with-welcome-screen)
          keep-editing-section (if (and (not (nil? (:foce-key db)))
                                        (get-in db company-editing-section-key))
                                  (assoc-in keep-sections-edits company-editing-section-key (get-in db company-editing-section-key))
                                  keep-sections-edits)
          keeping-revisions (if (:selected-topic-view db)
                              (assoc-in keep-editing-section section-revisions-data-key (get-in db section-revisions-data-key))
                              keep-editing-section)
          with-already-loaded (assoc-in keeping-revisions company-already-loaded-key true)]
      ; async preload the SU list
      (utils/after 100 #(api/get-su-list))
      (if (or (:read-only updated-body)
              (pos? (count (:sections updated-body)))
              (:force-remove-loading with-company-data))
          (dissoc with-already-loaded :loading :force-remove-loading)
          with-already-loaded))
    (= 401 status)
    (-> db
        (assoc-in [(keyword slug) :error] :forbidden)
        (dissoc :loading))
    (= 404 status)
    (do
      (router/redirect-404!)
      db)
    (and (>= 500 status)
         (<= 599 status))
    (do
      (router/redirect-500!)
      db)
    ;; probably some default failure handling should be added here
    :else db))

(defmethod dispatcher/action :companies [db [_ body]]
  (if body
    (-> db
     (assoc-in dispatcher/companies-key (:companies (:collection body)))
     (dissoc :loading))
    db))

(defn- get-su-list [db]
  (api/get-su-list)
  (assoc db :su-list-loading true))

(defmethod dispatcher/action :get-su-list [db [_ {:keys [slug response]}]]
  (get-su-list db))

(defmethod dispatcher/action :su-list [db [_ {:keys [slug response]}]]
  (-> db
    (dissoc :loading)
    (dissoc :su-list-loading)
    (assoc :su-list-loaded true)
    (assoc-in (dispatcher/su-list-key slug) response)))

(defmethod dispatcher/action :su-edit [db [_ {:keys [slug su-date su-slug]}]]
  (let [su-url   (oc-urls/stakeholder-update slug (utils/su-date-from-created-at su-date) su-slug)
        latest-su-key (dispatcher/latest-stakeholder-update-key slug)
        company-data-links (:links (dispatcher/company-data))
        updates-list-link (utils/link-for company-data-links "stakeholder-updates")
        updated-link (assoc updates-list-link :count (inc (:count updates-list-link)))
        new-links (conj (utils/vec-dissoc company-data-links updates-list-link) updated-link)]
    (-> db
      (assoc-in latest-su-key su-url)
      (dissoc :loading)
      (assoc-in (conj (dispatcher/company-data-key slug) :links) new-links)
      ; refresh the su list
      (get-su-list))))

(defmethod dispatcher/action :stakeholder-update [db [_ {:keys [slug update-slug response load-company-data]}]]
  (let [company-data-keys [:logo :logo-width :logo-height :name :slug :currency :public :promoted]
        company-data      (select-keys response company-data-keys)]
    ; load-company-data is used to save the subset of company data that is returned with a stakeholder-update data
    (if load-company-data
      ; save the company data returned with the SU data
      (-> db
        (assoc-in (dispatcher/stakeholder-update-key slug update-slug) response)
        (assoc-in (dispatcher/company-data-key slug) (utils/fix-sections company-data))
        (dissoc :loading))
      ; save only the SU data
      (-> db
        (assoc-in (dispatcher/stakeholder-update-key slug update-slug) response)
        (dissoc :loading)))))

(defn start-foce [db section section-data]
  (-> db
    (assoc :foce-key (keyword section)) ; which topic is being FoCE
    (assoc :foce-data section-data)     ; map of the in progress edits of the topic data
    (assoc :foce-data-editing? false)   ; is the data portion of the topic (e.g. finance, growth) being edited
    (assoc :show-top-menu nil)          ; dismiss top menu
    (dissoc :show-add-topic)))          ; remove the add topic view)

(defn stop-foce [db]
  (let [company-data (dispatcher/company-data db)
        show-add-topic (zero? (count (:sections company-data)))]
    (-> db
      (dissoc :foce-key)
      (dissoc :foce-data)
      (assoc :show-add-topic show-add-topic)
      (dissoc :foce-data-editing?))))

;; Front of Card Edit section
(defmethod dispatcher/action :start-foce [db [_ section-key section-data]]
  (if section-key
    (start-foce db section-key section-data)
    (stop-foce db)))

(defmethod dispatcher/action :start-foce-data-editing [db [_ value]]
  (assoc db :foce-data-editing? value))

(defmethod dispatcher/action :foce-input [db [_ topic-data-map]]
  (let [old-data (:foce-data db)]
    (assoc db :foce-data (merge old-data topic-data-map))))

;; This should be turned into a proper form library
;; Lomakeets FormState ideas seem like a good start:
;; https://github.com/metosin/lomakkeet/blob/master/src/cljs/lomakkeet/core.cljs

(defmethod dispatcher/action :input [db [_ path value]]
  (assoc-in db path value))

(defmethod dispatcher/action :new-sections [db [_ new-sections]]
  (let [slug (keyword (router/current-company-slug))]
    (api/patch-sections new-sections)
    (assoc-in db (conj (dispatcher/company-data-key slug) :sections) new-sections)))

(defmethod dispatcher/action :topic-archive [db [_ topic]]
  (let [slug (keyword (router/current-company-slug))
        company-data (dispatcher/company-data)
        old-sections (:sections company-data)
        new-sections (utils/vec-dissoc old-sections (name topic))
        old-archived (:archived company-data)
        new-archived (vec (conj old-archived {:title (:title ((keyword topic) company-data)) :section (name topic)}))
        company-key (dispatcher/company-data-key slug)]
    (api/patch-sections new-sections)
    (-> db
      (dissoc :foce-key)
      (dissoc :foce-data)
      (dissoc :foce-data-editing?)
      (assoc :show-add-topic (zero? (count new-sections)))
      (assoc-in (conj company-key :sections) new-sections)
      (assoc-in (conj company-key :archived) new-archived))))

(defmethod dispatcher/action :delete-revision [db [_ topic as-of]]
  (let [slug (keyword (router/current-company-slug))
        company-data (dispatcher/company-data)
        old-topic-data ((keyword topic) company-data)
        revisions (:revisions-data old-topic-data)
        revision-data (first (filter #(= (:created-at %) as-of) revisions))
        new-revisions (vec (filter #(not= (:created-at %) as-of) revisions))
        should-remove-section? (zero? (count new-revisions))
        should-update-section? (= (:created-at old-topic-data) as-of)
        new-sections (if should-remove-section? (utils/vec-dissoc (:sections company-data) (name topic)) (:sections company-data))
        company-key (dispatcher/company-data-key slug)
        new-topic-data (if should-update-section?
                          (merge (first new-revisions) {:revisions (:revisions old-topic-data)
                                                        :links (:links old-topic-data)
                                                        :revisions-data new-revisions
                                                        :section (:section old-topic-data)})
                          (assoc old-topic-data :revisions-data new-revisions))
        with-sections (assoc company-data :sections new-sections)
        with-fixed-topics (if should-remove-section?
                            (dissoc with-sections (keyword topic))
                            (assoc with-sections (keyword topic) new-topic-data))]
    (api/delete-revision topic revision-data)
    (-> db
      (stop-foce)
      (assoc-in company-key with-fixed-topics))))


(defmethod dispatcher/action :foce-save [db [_ & [new-sections topic-data]]]
  (let [slug (keyword (router/current-company-slug))
        topic (:foce-key db)
        topic-data (merge (:foce-data db) (if (map? topic-data) topic-data {}))
        body (:body topic-data)
        with-fixed-headline (assoc topic-data :headline (utils/emoji-images-to-unicode (:headline topic-data)))
        with-fixed-body (assoc with-fixed-headline :body (utils/emoji-images-to-unicode body))
        with-created-at (if (contains? with-fixed-body :created-at) with-fixed-body (assoc with-fixed-body :created-at (utils/as-of-now)))
        created-at (:created-at with-created-at)
        revisions-data (or (:revisions-data (get (dispatcher/company-data db) topic)) [])
        without-current-revision (vec (filter #(not= (:created-at %) created-at) revisions-data))
        with-new-revision (conj without-current-revision with-created-at)
        sorted-revisions (vec (sort #(compare (:created-at %2) (:created-at %1)) with-new-revision))
        complete-topic-data (merge with-created-at {:revisions-data sorted-revisions})]
    (if (not (:placeholder topic-data))
      (api/partial-update-section topic with-created-at)
      (api/save-or-create-section with-created-at))
    (-> db
      (assoc-in (conj (dispatcher/company-data-key slug) (keyword topic)) complete-topic-data)
      (stop-foce))))

(defmethod dispatcher/action :force-fullscreen-edit [db [_ topic]]
  (if topic
    (assoc-in db [:force-edit-topic] topic)
    (dissoc db :force-edit-topic)))

(defn- save-topic [db topic topic-data]
  (let [slug (keyword (router/current-company-slug))
        old-section-data (get (dispatcher/company-data db slug) (keyword topic))
        new-data (dissoc (merge old-section-data topic-data) :placeholder)]
    (api/partial-update-section topic (dissoc topic-data :placeholder))
    (assoc-in db (conj (dispatcher/company-data-key slug) (keyword topic)) new-data)))

(defmethod dispatcher/action :save-topic [db [_ topic topic-data]]
  (save-topic db topic topic-data))

(defmethod dispatcher/action :save-topic-data [db [_ topic topic-data]]
  ;; save topic data for the company
  (save-topic db topic topic-data)
  ;; update topic data for the still in-progress FoCE
  (assoc db :foce-data (merge (:foce-data db) topic-data)))

(defmethod dispatcher/action :su-share/reset [db _]
  (dissoc db :su-share))

;; Store JWT in App DB so it can be easily accessed in actions etc.

(defmethod dispatcher/action :jwt
  [db [_ jwt-data]]
  (when jwt-data
    (api/get-auth-settings))
  (let [next-db (if (cook/get-cookie :show-login-overlay)
                  (assoc db :show-login-overlay (keyword (cook/get-cookie :show-login-overlay)))
                  db)]
    (when (and (cook/get-cookie :show-login-overlay)
               (not= (cook/get-cookie :show-login-overlay) "collect-name-password"))
      (cook/remove-cookie! :show-login-overlay))
    (assoc next-db :jwt (jwt/get-contents))))

;; Stripe Payment related actions

(defmethod dispatcher/action :subscription
  [db [_ {:keys [uuid] :as data}]]
  (if uuid
    (assoc-in db [:subscription uuid] data)
    (assoc db :subscription nil)))

(defmethod dispatcher/action :show-login-overlay
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
        auth-url (utils/link-for (:links (:auth-settings db)) "authenticate" "GET" {:auth-source "slack"})]
    (when (and (not (.startsWith current oc-urls/login))
               (not (.startsWith current oc-urls/sign-up))
               (not (cook/get-cookie :login-redirect)))
        (cook/set-cookie! :login-redirect current (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
    (router/redirect! (:href auth-url)))
  db)

(defmethod dispatcher/action :auth-bot
  [db [_]]
  (let [current (router/get-token)
        auth-url (utils/link-for (:links (:auth-settings db)) "bot")]
    (when (and (not (.startsWith current oc-urls/login))
               (not (.startsWith current oc-urls/sign-up))
               (not (cook/get-cookie :login-redirect)))
        (cook/set-cookie! :login-redirect current (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
    (router/redirect! (:href auth-url)))
  db)

(defmethod dispatcher/action :login-with-email-change
  [db [_ k v]]
  (assoc-in db [:login-with-email k] v))

(defmethod dispatcher/action :login-with-email
  [db [_]]
  (api/auth-with-email (:email (:login-with-email db)) (:pswd (:login-with-email db)))
  (dissoc db :login-with-email-error))

(defmethod dispatcher/action :login-with-email/failed
  [db [_ error]]
  (assoc db :login-with-email-error error))

(defmethod dispatcher/action :login-with-email/success
  [db [_ jwt]]
  (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
  (.reload js/location)
  db)

(defmethod dispatcher/action :signup-with-email-change
  [db [_ k v]]
  (assoc-in db [:signup-with-email k] v))

(defmethod dispatcher/action :signup-with-email
  [db [_]]
  (api/signup-with-email (:firstname (:signup-with-email db)) (:lastname (:signup-with-email db)) (:email (:signup-with-email db)) (:pswd (:signup-with-email db)))
  (dissoc db :signup-with-email-error))

(defmethod dispatcher/action :signup-with-email/failed
  [db [_ error]]
  (assoc db :signup-with-email-error error))

(defmethod dispatcher/action :signup-with-email/success
  [db [_ jwt]]
  (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
  (router/redirect! oc-urls/home)
  db)

(defmethod dispatcher/action :get-auth-settings
  [db [_]]
  (api/get-auth-settings)
  db)

(defmethod dispatcher/action :enumerate-users
  [db [_]]
  (api/enumerate-users)
  (assoc db :enumerate-users-requested true))

(defmethod dispatcher/action :enumerate-users/teams
  [db [_ teams]]
  (doseq [team teams
          :let [team-link (utils/link-for (:links team) "item" "GET")]]
    (api/enumerate-team-users team-link))
  (assoc-in db [:enumerate-users :teams] teams))

(defmethod dispatcher/action :enumerate-users/success
  [db [_ team-data]]
  (if team-data
    (assoc-in db [:enumerate-users (:team-id team-data)] team-data)
    db))

(defmethod dispatcher/action :enumerate-channels
  [db [_]]
  (api/enumerate-channels)
  (assoc db :enumerate-channels-requested true))

(defmethod dispatcher/action :enumerate-channels/success
  [db [_ channels]]
  (if channels
    (-> db
      (assoc :enumerate-channels channels)
      (dissoc :enumerate-channels-requested))
    (dissoc db :enumerate-channels)))

(defmethod dispatcher/action :invite-by-email-change
  [db [_ k v]]
  (-> db
    (assoc-in [:um-invite k] v)
    (dissoc :invite-by-email-error)))

(defn resend-invite [db user]
  (let [api-entry-point-links (:api-entry-point db)
        companies (count (filter #(= (:rel %) "company") api-entry-point-links))
        team-data (get (:enumerate-users db) (router/current-team-id))
        idx (.indexOf (:users team-data) user)
        json-params {:email (:email user)}
        with-first-name (if (:first-name user) (assoc json-params :first-name (:first-name user)) json-params)
        with-last-name (if (:last-name user) (assoc with-first-name :last-name (:last-name user)) with-first-name)
        with-company-name (if (= companies 1) (assoc with-last-name :org-name (:name (utils/link-for api-entry-point-links "company"))) with-last-name)
        with-admin (assoc with-company-name :admin (or (:admin user) false))]
    (api/user-action (utils/link-for (:links team-data) "add" "POST" {:content-type "application/vnd.open-company.team.invite.v1"}) with-admin)
    (assoc-in db [:enumerate-users (router/current-team-id) :users idx :loading] true)))

(defmethod dispatcher/action :resend-invite
  [db [_ user]]
  (resend-invite db user))

(defmethod dispatcher/action :invite-by-email
  [db [_]]
  (let [email (:email (:um-invite db))
        parsed-email (utils/parse-input-email email)
        email-name (:name parsed-email)
        email-address (:address parsed-email)
        user  (first (filter #(= (:email %) email-address) (:users (get (:enumerate-users db) (router/current-team-id)))))]
    (if user
      (if (= (:status user) "pending")
        ;resend invitation since user was invited and didn't accept
        (resend-invite db user)
        ; user is already in, send error message
        (assoc db :invite-by-email-error :user-exists))
      ; looks like a new user, sending invitation
      (let [splitted-name (string/split email-name #"\s")
            name-size (count splitted-name)
            splittable-name? (= name-size 2)
            first-name (cond
                        (= name-size 1) email-name
                        splittable-name? (first splitted-name)
                        :else "")
            last-name (cond
                        splittable-name? (second splitted-name)
                        :else "")
            first-team (first (:teams (:jwt db)))]
        (api/send-invitation email-address (:user-type (:um-invite db)) first-name last-name)
        (dissoc db :invite-by-email-error)))))

(defmethod dispatcher/action :invite-by-email/success
  [db [_ email]]
  ; refresh the users list once the invitation succeded
  (api/enumerate-users)
  (-> db
      (assoc-in [:um-invite :email] "")
      (assoc-in [:um-invite :user-type] nil)))

(defmethod dispatcher/action :invite-by-email/failed
  [db [_ email]]
  ; refresh the users list once the invitation succeded
  (api/enumerate-users)
  (assoc db :invite-by-email-error true))

(defmethod dispatcher/action :user-action
  [db [_ team-id invitation action method other-link-params payload]]
  (let [teams (:enumerate-users db)
        team-data (get teams team-id)
        idx (.indexOf (:users team-data) invitation)]
    (api/user-action (utils/link-for (:links invitation) action method other-link-params) payload)
    (assoc-in db [:enumerate-users idx :loading] true)))

(defmethod dispatcher/action :user-action/complete
  [db [_]]
  ; refresh the list of users once the invitation action complete
  (api/enumerate-users)
  db)

(defmethod dispatcher/action :confirm-invitation
  [db [_]]
  (api/confirm-invitation (:token (:query-params @router/path)))
  (dissoc db :email-confirmed))

(defmethod dispatcher/action :invitation-confirmed
  [db [_ status]]
  (when (= status 201)
    (cook/set-cookie! :show-login-overlay "collect-name-password"))
  (assoc db :email-confirmed (= status 201)))

(defmethod dispatcher/action :collect-name-pswd
  [db [_]]
  (let [form-data (:collect-name-pswd db)]
    (api/collect-name-password (:firstname form-data) (:lastname form-data) (:pswd form-data)))
  db)

(defmethod dispatcher/action :collect-name-pswd-finish
  [db [_ status]]
  (if (and (>= status 200)
           (<= status 299))
    (do
      (cook/remove-cookie! :show-login-overlay)
      (dissoc db :show-login-overlay))
    (assoc db :collect-name-password-error status)))

(defmethod dispatcher/action :mobile-menu-toggle
  [db [_]]
  (if (responsive/is-mobile-size?)
    (assoc db :mobile-menu-open (not (:mobile-menu-open db)))
    db))

(defmethod dispatcher/action :reset-su-list
  [db [_]]
  ; Reset flag to reload su list when needed
  (dissoc db :su-list-loaded))

(defmethod dispatcher/action :revisions-loaded
  [db [_ {:keys [slug topic revisions]}]]
  (let [sort-pred (fn [a b] (compare (:created-at b) (:created-at a)))]
    (assoc-in db (conj (dispatcher/company-data-key slug) (keyword topic) :revisions-data) (vec (sort sort-pred (:revisions (:collection revisions)))))))

((defmethod dispatcher/action :show-add-topic
  [db [_ active]]
  (if active
    (do
      (utils/after 100 #(router/nav! (oc-urls/company)))
      (-> db
        (assoc :show-add-topic true)
        (dissoc :selected-topic-view)))
    (dissoc db :show-add-topic))))

(defmethod dispatcher/action :dashboard-select-topic
  [db [_ section-kw]]
  (if (utils/in? (:dashboard-selected-topics db) section-kw)
    (assoc db :dashboard-selected-topics (utils/vec-dissoc (:dashboard-selected-topics db) section-kw))
    (let [sections (to-array (:sections (dispatcher/company-data db)))
          all-selected-topics (vec (conj (or (:dashboard-selected-topics db) []) section-kw))
          next-selected-topics (vec (map keyword (filter #(utils/in? all-selected-topics (keyword %)) sections)))]
      (assoc db :dashboard-selected-topics next-selected-topics))))

(defmethod dispatcher/action :dashboard-select-all
  [db [_ section-kw]]
  (assoc db :dashboard-selected-topics (vec (map keyword (:sections (dispatcher/company-data db))))))

(defmethod dispatcher/action :dashboard-share-mode
  [db [_ activate]]
  (-> db
    (assoc :dashboard-sharing activate)
    (dissoc :show-add-topic)
    (assoc :dashboard-selected-topics [])))

(defmethod dispatcher/action :add-topic
  [db [_ topic topic-data]]
  (let [company-data-key (dispatcher/company-data-key (router/current-company-slug))
        company-data (get-in db company-data-key)
        archived-topics (:archived company-data)
        updated-archived (if (:was-archived topic-data)
                            (vec (filter #(not= (:section %) (name topic)) archived-topics))
                            archived-topics)
        updated-sections (conj (:sections company-data) (name topic))
        updated-company-data (-> company-data
                                (assoc :sections updated-sections)
                                (assoc :archived updated-archived)
                                (assoc (keyword topic) topic-data))
        next-db (assoc-in db company-data-key updated-company-data)]
    (if (:was-archived topic-data)
     next-db
     (start-foce next-db topic topic-data))))

(defmethod dispatcher/action :rollback-add-topic
  [db [_ topic-kw]]
  (let [company-data-key (dispatcher/company-data-key (router/current-company-slug))
        company-data (get-in db (dispatcher/company-data-key (router/current-company-slug)))
        topic-data (get company-data topic-kw)
        archived-topics (:archived company-data)
        updated-archived (if (:was-archived topic-data)
                            (conj archived-topics {:section (name topic-kw)
                                                   :title (:title topic-data)})
                            archived-topics)
        updated-sections (utils/vec-dissoc (:sections company-data) (name topic-kw))
        updated-company-data (-> company-data
                                (dissoc topic-kw)
                                (assoc :sections updated-sections)
                                (assoc :archived updated-archived))]
    (-> db
      (assoc-in company-data-key updated-company-data)
      (stop-foce))))

(defmethod dispatcher/action :show-top-menu [db [_ topic]]
  (assoc db :show-top-menu topic))

(defmethod dispatcher/action :hide-welcome-screen
  [db [_]]
  (dissoc db :show-welcome-screen))

(defmethod dispatcher/action :reset-user-profile
  [db [_]]
  (assoc db :edit-user-profile (assoc (:current-user-data db) :password "")))

(defmethod dispatcher/action :get-current-user
  [db [_]]
  (if (:auth-settings db)
    (api/get-current-user (:auth-settings db))
    (utils/after 1000 #(dispatcher/dispatch! [:get-current-user])))
  db)

(defmethod dispatcher/action :user-data
  [db [_ user-data]]
  (-> db
      (assoc :current-user-data user-data)
      (assoc :edit-user-profile user-data)))

(defmethod dispatcher/action :save-user-profile
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
                     (assoc with-pswd :email (:email (:current-user-data db))))]
    (api/patch-user-profile (:current-user-data db) with-email))
  db)

(defmethod dispatcher/action :add-email-domain-team
  [db [_]]
  (let [domain (:domain (:um-domain-invite db))]
    (when (utils/valid-domain? domain))
      (api/add-email-domain (if (.startsWith domain "@") (subs domain 1) domain)))
  (assoc db :add-email-domain-team-error false))

(defmethod dispatcher/action :add-email-domain-team/finish
  [db [_ success]]
  (when success
    (api/enumerate-users))
  (-> db
      (assoc-in [:um-domain-invite :domain] (if success "" (:domain (:um-domain-invite db))))
      (assoc :add-email-domain-team-error (if success false true))))

(defmethod dispatcher/action :add-slack-team
  [db [_]]
  (let [teams-data (:enumerate-users db)
        first-team (first (:teams teams-data))
        team-data (get teams-data (:team-id first-team))
        add-slack-team-link (utils/link-for (:links team-data) "authenticate" "GET" {:auth-source "slack"})]
    (when add-slack-team-link
      (router/redirect! (:href add-slack-team-link))))
  db)