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

(defmethod dispatcher/action :entry [db [_ {:keys [links]}]]
  (let [create-link    (med/find-first #(= (:rel %) "company-create") links)
        slug           (fn [co] (last (string/split (:href co) #"/")))
        [first second] (filter #(= (:rel %) "company") links)]
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
        (and first second)              (router/nav! oc-urls/companies)))
    (if (utils/in? (:route @router/path) "create-company")
      (dissoc db :loading)
      db)))

(defmethod dispatcher/action :company-submit [db _]
  (api/post-company (:company-editor db))
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
  (utils/after 500 #(api/load-revisions slug section (utils/link-for (:links body) "revisions")))
  (if body
    (let [fixed-section (utils/fix-section body section)]
      (assoc-in db (dispatcher/company-section-key slug section) fixed-section))
    db))

(defmethod dispatcher/action :company [db [_ {:keys [slug success status body]}]]
  (cond
    success
    ;; add section name inside each section
    (let [updated-body (utils/fix-sections body)
          with-company-data (assoc-in db (dispatcher/company-data-key (:slug updated-body)) updated-body)
          with-open-add-topic (if (and (zero? (count (:sections updated-body)))
                                       (zero? (count (:archived updated-body))))
                               (assoc with-company-data :show-add-topic true)
                               with-company-data)]
      ; async preload the SU list
      (utils/after 100 #(api/get-su-list))
      (if (or (:read-only updated-body)
              (pos? (count (:sections updated-body)))
              (:force-remove-loading with-company-data))
          (dissoc with-open-add-topic :loading :force-remove-loading)
          with-open-add-topic))
    (= 403 status)
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

;; Front of Card Edit section
(defmethod dispatcher/action :start-foce [db [_ section-key section-data]]
  (if section-key
    (-> db
        (assoc :foce-key section-key) ; which topic is being FoCE
        (assoc :foce-data section-data) ; map of the in progress edits of the topic data
        (assoc :foce-data-editing? false) ; is the data portion of the topic (e.g. finance, growth) being edited
        (dissoc :show-add-topic)) ; remove the add topic view
    (-> db
        (dissoc :foce-key)
        (dissoc :foce-data)
        (dissoc :foce-data-editing?))))

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
        new-sections (utils/vec-dissoc old-sections (name topic))]
    (api/patch-sections new-sections)
    (-> db
      (dissoc :foce-key)
      (dissoc :foce-data)
      (dissoc :foce-data-editing?)
      (assoc-in (conj (dispatcher/company-data-key slug) :sections) new-sections))))

(defmethod dispatcher/action :foce-save [db [_ & [new-sections topic-data]]]
  (let [slug (keyword (router/current-company-slug))
        topic (:foce-key db)
        topic-data (merge (:foce-data db) (if (map? topic-data) topic-data {}))
        body (:body topic-data)
        with-fixed-headline (assoc topic-data :headline (utils/emoji-images-to-unicode (:headline topic-data)))
        with-fixed-body (assoc with-fixed-headline :body (utils/emoji-images-to-unicode body))
        without-placeholder (dissoc with-fixed-body :placeholder)]
    (if (utils/link-for (:links without-placeholder) "partial-update" "PATCH")
      (api/partial-update-section topic without-placeholder)
      (api/save-or-create-section without-placeholder))
    (assoc-in db (conj (dispatcher/company-data-key slug) (keyword topic)) without-placeholder)))

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
  [db [_ primary-scope?]]
  (let [current (router/get-token)
        slack-ref (if primary-scope? "authenticate" "authenticate-retry")
        auth-url (utils/link-for (:links (:slack (:auth-settings @dispatcher/app-state))) slack-ref)]
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
  (.reload js/location)
  db)

(defmethod dispatcher/action :get-auth-settings
  [db [_]]
  (api/get-auth-settings)
  db)

(defmethod dispatcher/action :enumerate-users
  [db [_]]
  (api/enumerate-users)
  (assoc db :enumerate-users-requested true))

(defmethod dispatcher/action :enumerate-users/success
  [db [_ users]]
  (if users
    (assoc db :enumerate-users users)
    (dissoc db :enumerate-users)))

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
  [db [_ email]]
  (-> db
    (assoc-in [:um-invite :email] email)
    (dissoc :invite-by-email-error)))

(defmethod dispatcher/action :invite-by-email
  [db [_]]
  (let [email (:email (:um-invite db))
        user  (first (filter #(= (:email %) email) (:enumerate-users db)))]
    (if user
      (if (= (:status user) "pending")
        ;resend invitation since user was invited and didn't accept
        (let [company-data (dispatcher/company-data)
              idx (.indexOf (:enumerate-users db) user)]
          (api/user-invitation-action (utils/link-for (:links user) "invite") {:email (:email user)
                                                                               :company-name (:name company-data)
                                                                               :logo (:logo company-data)})
          (assoc-in db [:enumerate-users idx :loading] true))
        ; user is already in, send error message
        (assoc db :invite-by-email-error :user-exists))
      (do ; looks like a new user, sending invitation
        (api/send-invitation (:email (:um-invite db)))
        (dissoc db :invite-by-email-error)))))

(defmethod dispatcher/action :invite-by-email/success
  [db [_ email]]
  ; refresh the users list once the invitation succeded
  (api/enumerate-users)
  (assoc-in db [:um-invite :email] ""))

(defmethod dispatcher/action :invite-by-email/failed
  [db [_ email]]
  ; refresh the users list once the invitation succeded
  (api/enumerate-users)
  (assoc db :invite-by-email-error true))

(defmethod dispatcher/action :user-invitation-action
  [db [_ invitation action payload]]
  (let [idx (.indexOf (:enumerate-users db) invitation)]
    (api/user-invitation-action (utils/link-for (:links invitation) action) payload)
    (assoc-in db [:enumerate-users idx :loading] true)))

(defmethod dispatcher/action :user-invitation-action/complete
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
  (when (= status 200)
    (cook/set-cookie! :show-login-overlay "collect-name-password"))
  (assoc db :email-confirmed (= status 200)))

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
  (assoc-in db (concat (dispatcher/company-data-key slug) [(keyword topic) :revisions-data]) (:revisions (:collection revisions))))

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
  (assoc db :dashboard-selected-topics
    (if (utils/in? (:dashboard-selected-topics db) section-kw)
      (utils/vec-dissoc (:dashboard-selected-topics db) section-kw)
      (conj (:dashboard-selected-topics db) section-kw))))

(defmethod dispatcher/action :dashboard-select-all
  [db [_ section-kw]]
  (assoc db :dashboard-selected-topics (vec (map keyword (:sections (dispatcher/company-data db))))))

(defmethod dispatcher/action :dashboard-share-mode
  [db [_ activate]]
  (-> db
    (assoc :dashboard-sharing activate)
    (assoc :dashboard-selected-topics [])))