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
            [oc.web.lib.responsive :as responsive]))

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

(defmethod dispatcher/action :entry-point [db [_ {:keys [collection]}]]
  (let [orgs (:items collection)]
    (cond
      ; If i have an org slug let's load the org data
      (router/current-org-slug)
      (if-let [org-data (first (filter #(= (:slug %) (router/current-org-slug)) orgs))]
        (api/get-org org-data)
        (router/redirect-404!))
      ; If not redirect the user to the first useful org or to the create org UI
      (and (not (utils/in? (:route @router/path) "create-org"))
           (not (utils/in? (:route @router/path) "create-board")))
      (let [login-redirect (cook/get-cookie :login-redirect)]
        (cond
          ; redirect to create-company if the user has no companies
          (zero? (count orgs))   (router/nav! oc-urls/create-org)
          ; if there is a login-redirect use it
          (and (jwt/jwt) login-redirect)  (do
                                            (cook/remove-cookie! :login-redirect)
                                            (router/redirect! login-redirect))
          ; if the user has only one company, send him to the company dashboard
          (= (count orgs) 1)        (router/nav! (oc-urls/boards (:slug (first orgs))))
          ; if the user has more than one company send him to the companies page
          (> (count orgs) 1)        (router/nav! oc-urls/orgs))))
    (-> db
        (dissoc :loading)
        (assoc :orgs orgs)
        (assoc-in dispatcher/api-entry-point-key (:links collection)))))

(defn newest-board [boards]
  (first (sort #(compare (utils/js-date (:created-at %2)) (utils/js-date (:created-at %1))) boards)))

(defn get-default-board [org-data]
  (if-let [last-board-slug (cook/get-cookie (router/last-board-cookie (:slug org-data)))]
    (let [boards (filter #(= (:slug %) last-board-slug) (:boards org-data))]
      (if (pos? (count boards))
        ; Get the last accessed board from the saved cookie
        (first boards)
        ; Fallback to the newest board if the saved board was not found
        (newest-board (:boards org-data))))
    (newest-board (:boards org-data))))

(defmethod dispatcher/action :org [db [_ org-data]]
  (let [boards (:boards org-data)]
    (cond
      ; If there is a board slug let's load the board data
      (router/current-board-slug)
      (let [board-data (first (filter #(= (:slug %) (router/current-board-slug)) boards))]
        (api/get-board board-data))
      (and (not (utils/in? (:route @router/path) "create-board"))
           (not (utils/in? (:route @router/path) "create-org"))
           (not (utils/in? (:route @router/path) "org-settings"))
           (not (utils/in? (:route @router/path) "updates-list")))
      (cond
        ;; Redirect to the first board if only one is presnet
        (>= (count boards) 1)
        (let [board-to (get-default-board org-data)]
          (router/nav! (oc-urls/board (:slug org-data) (:slug board-to))))
        ;; Redirect to create board if no board are present
        :else
        (router/nav! (oc-urls/create-board (:slug org-data))))))
  (utils/after 100 #(api/get-updates))
  (-> db
    (assoc-in (dispatcher/org-data-key (:slug org-data)) (utils/fix-org org-data))
    (assoc :updates-list-loading (utils/in? (:route @router/path) "updates-list"))))

(defmethod dispatcher/action :load-other-boards [db [_]]
  (doseq [board (:boards (dispatcher/org-data db))
        :when (not= (:slug board) (router/current-board-slug))]
    (api/get-board board))
  db)

(defmethod dispatcher/action :board [db [_ board-data]]
 (let [is-currently-shown (= (:slug board-data) (router/current-board-slug))]
    (when is-currently-shown
      (utils/after 2000 #(dispatcher/dispatch! [:load-other-boards])))
    (let [fixed-board-data (utils/fix-board board-data)
          old-board-data (get-in db (dispatcher/board-data-key (router/current-org-slug) (keyword (:slug board-data))))
          with-current-edit (if (and is-currently-shown
                                     (:foce-key db))
                              old-board-data
                              fixed-board-data)]
      (-> db
        (assoc-in (dispatcher/board-data-key (router/current-org-slug) (keyword (:slug board-data))) with-current-edit)
        ;; show add topic if the board loaded is the one currently shown and it has no topics
        (assoc :show-add-topic (if (and is-currently-shown
                                        (empty? old-board-data)
                                        (not (:selected-topic-view db)))
                                 (zero? (count (:topics fixed-board-data)))
                                 (:show-add-topic db)))))))

(defmethod dispatcher/action :company-submit [db _]
  (api/create-company (:company-editor db))
  db)

(defmethod dispatcher/action :company-created [db [_ body]]
  (if (:links body)
    (let [updated (utils/fix-board body)]
      (router/redirect! (oc-urls/board (:slug updated)))
      (assoc-in db (dispatcher/org-data-key (:slug updated)) updated))
    db))

(defmethod dispatcher/action :new-topic [db [_ body]]
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
        (utils/after 100 #(api/get-teams true))
        ; confirm email invitation
        (and (utils/in? (:route @router/path) "confirm-invitation")
             (contains? (:query-params @router/path) :token))
        (utils/after 100 #(api/confirm-invitation (:token (:query-params @router/path))))))
    ; if the auth-settings call failed retry it in 2 seconds
    (utils/after 2000 #(api/get-auth-settings)))
  (assoc db :auth-settings body))

(defmethod dispatcher/action :entry [db [_ body]]
  (if body
    (let [fixed-topic (utils/fix-topic (:body body) (:topic-slug body) true)
          assoc-in-coll (dispatcher/entry-key (:org-slug body) (:board-slug body) (:topic-slug body) (:as-of body))
          next-db (assoc-in db assoc-in-coll true)]
      next-db)
    db))

(defmethod dispatcher/action :topic [db [_ {:keys [topic body]}]]
  ;; Refresh topic entries
  (api/load-entries topic (utils/link-for (:links body) "collection"))
  (if body
    (let [fixed-topic (utils/fix-topic body topic)]
      (assoc-in db (dispatcher/board-topic-key (router/current-org-slug) (router/current-board-slug) topic) fixed-topic))
    db))

(defmethod dispatcher/action :topic-entry [db [_ {:keys [topic body created-at]}]]
  ;; Refresh topic entries
  ; (let [current-entries ])
  (if body
    (let [fixed-topic (utils/fix-topic body topic)]
      (assoc-in db (dispatcher/board-topic-key (router/current-org-slug) (router/current-board-slug) topic) fixed-topic))
    db))

(defmethod dispatcher/action :company [db [_ {:keys [slug success status body]}]]
  (cond
    success
    ;; add topic name inside each topic
    (let [org (router/current-org-slug)
          board (router/current-board-slug)
          updated-body (utils/fix-board body)
          board-data-key (dispatcher/board-data-key org board)
          board-topics-key (conj board-data-key :topics)
          topic-entries-data-key (when (:selected-topic-view db)
                                      (dispatcher/topic-entries-key org board (keyword (:selected-topic-view db))))
          board-editing-topic-key (when (:foce-key db) (conj board-data-key (keyword (:foce-key db))))
          board-already-loaded-key (conj board-data-key :board-data-loaded)
          already-loaded? (get-in db board-already-loaded-key)
          with-board-data (assoc-in db board-data-key updated-body)
          with-open-add-topic (if (and (not (responsive/is-tablet-or-mobile?))
                                       (zero? (count (:topics updated-body)))
                                       (not already-loaded?))
                                (assoc with-board-data :show-add-topic true)
                                with-board-data)
          with-welcome-screen (if (and (not (responsive/is-tablet-or-mobile?))
                                       (not (:foce-key db))
                                       (zero? (count (:topics updated-body)))
                                       (zero? (count (:archived updated-body)))
                                       (not already-loaded?))
                                (assoc with-open-add-topic :show-welcome-screen true)
                                with-open-add-topic)
          keep-topics-edits (if (not (nil? (:foce-key db)))
                                (assoc-in with-welcome-screen board-topics-key (get-in db board-topics-key))
                                with-welcome-screen)
          keep-editing-topic (if (and (not (nil? (:foce-key db)))
                                        (get-in db board-editing-topic-key))
                                  (assoc-in keep-topics-edits board-editing-topic-key (get-in db board-editing-topic-key))
                                  keep-topics-edits)
          keeping-entries (if (:selected-topic-view db)
                              (assoc-in keep-editing-topic topic-entries-data-key (get-in db topic-entries-data-key))
                              keep-editing-topic)
          with-already-loaded (assoc-in keeping-entries board-already-loaded-key true)]
      ; async preload the SU list
      (utils/after 100 #(api/get-updates))
      (if (or (:read-only updated-body)
              (pos? (count (:topics updated-body)))
              (:force-remove-loading with-board-data))
          (dissoc with-already-loaded :loading :force-remove-loading)
          with-already-loaded))
    (= 401 status)
    (-> db
        (assoc-in (dispatcher/board-access-error-key (router/current-org-slug) (router/current-board-slug)) :forbidden)
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

(defn- get-updates [db]
  (api/get-updates)
  (assoc db :updates-list-loading true))

(defmethod dispatcher/action :get-updates-list [db [_ {:keys [slug response]}]]
  (get-updates db))

(defmethod dispatcher/action :updates-list [db [_ {:keys [response]}]]
  (-> db
    (dissoc :loading)
    (dissoc :updates-list-loading)
    (assoc :updates-list-loaded true)
    (assoc-in (dispatcher/updates-list-key (router/current-org-slug)) (:collection response))))

(defmethod dispatcher/action :su-edit [db [_ {:keys [su-date su-slug]}]]
  (let [su-url   (oc-urls/update-link (router/current-board-slug) (utils/su-date-from-created-at su-date) su-slug)
        latest-su-key (dispatcher/latest-update-key (router/current-board-slug))
        board-data-links (:links (dispatcher/board-data))
        updates-list-link (utils/link-for board-data-links "stakeholder-updates")
        updated-link (assoc updates-list-link :count (inc (:count updates-list-link)))
        new-links (conj (utils/vec-dissoc board-data-links updates-list-link) updated-link)]
    (-> db
      (assoc-in latest-su-key su-url)
      (dissoc :loading)
      (assoc-in (conj (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug)) :links) new-links)
      ; refresh the su list
      (get-updates))))

(defmethod dispatcher/action :update-loaded [db [_ {:keys [org-slug update-slug response load-org-data]}]]
  (let [org-data-keys [:logo-url :logo-width :logo-height :name :slug :currency :public :promoted]
        org-data      (select-keys response org-data-keys)]
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
    (assoc :foce-data-editing? false)   ; is the data portion of the topic (e.g. finance, growth) being edited
    (assoc :show-top-menu nil)          ; dismiss top menu
    (dissoc :show-add-topic)))          ; remove the add topic view)

(defn stop-foce [db]
  (let [board-data (dispatcher/board-data db (router/current-org-slug) (router/current-board-slug))
        show-add-topic (zero? (count (:topics board-data)))]
    (-> db
      (dissoc :foce-key)
      (dissoc :foce-data)
      (assoc :show-add-topic show-add-topic)
      (dissoc :foce-data-editing?))))

;; Front of Card Edit topic
(defmethod dispatcher/action :start-foce [db [_ topic-key topic-data]]
  (if topic-key
    (start-foce db topic-key topic-data)
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

(defmethod dispatcher/action :new-topics [db [_ new-topics]]
  (let [board-data-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))]
    (api/patch-topics new-topics)
    (assoc-in db (conj board-data-key :topics) new-topics)))

(defmethod dispatcher/action :topic-archive [db [_ topic]]
  (let [board-data (dispatcher/board-data)
        old-topics (:topics board-data)
        new-topics (utils/vec-dissoc old-topics (name topic))
        old-archived (:archived board-data)
        new-archived (vec (conj old-archived {:title (:title ((keyword topic) board-data)) :topic (name topic)}))
        company-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))]
    (api/patch-topics new-topics)
    (-> db
      (dissoc :foce-key)
      (dissoc :foce-data)
      (dissoc :foce-data-editing?)
      (assoc :show-add-topic (zero? (count new-topics)))
      (assoc-in (conj company-key :topics) new-topics)
      (assoc-in (conj company-key :archived) new-archived))))

(defmethod dispatcher/action :delete-entry [db [_ topic as-of]]
  (let [board-data (dispatcher/board-data)
        old-topic-data ((keyword topic) board-data)
        entries (dispatcher/topic-entries-data (router/current-org-slug) (router/current-board-slug) topic db)
        entry-data (first (filter #(= (:created-at %) as-of) entries))
        new-entries (vec (filter #(not= (:created-at %) as-of) entries))
        should-remove-topic? (zero? (count new-entries))
        should-update-topic? (= (:created-at old-topic-data) as-of)
        new-topics (if should-remove-topic? (utils/vec-dissoc (:topics board-data) (name topic)) (:topics board-data))
        board-data-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        new-topic-data (if should-update-topic?
                          (merge old-topic-data (first new-entries))
                          old-topic-data)
        with-topics (assoc board-data :topics new-topics)
        with-fixed-topics (if should-remove-topic?
                            (dissoc with-topics (keyword topic))
                            (assoc with-topics (keyword topic) new-topic-data))]
    (api/delete-entry topic entry-data should-remove-topic?)
    (-> db
      (stop-foce)
      (assoc-in board-data-key with-fixed-topics)
      (assoc-in (dispatcher/topic-entries-key (router/current-org-slug) (router/current-board-slug) topic) new-entries))))


(defmethod dispatcher/action :foce-save [db [_ & [new-topics topic-data]]]
  (let [topic (:foce-key db)
        topic-data (merge (:foce-data db) (if (map? topic-data) topic-data {}))
        body (:body topic-data)
        with-fixed-headline (assoc topic-data :headline (utils/emoji-images-to-unicode (:headline topic-data)))
        with-fixed-body (assoc with-fixed-headline :body (utils/emoji-images-to-unicode body))
        with-created-at (if (contains? with-fixed-body :created-at) with-fixed-body (assoc with-fixed-body :created-at (utils/as-of-now)))
        without-placeholder (dissoc with-created-at :placeholder)
        created-at (:created-at without-placeholder)
        topic-entries-key (dispatcher/topic-entries-key (router/current-org-slug) (router/current-board-slug) topic)
        entries-data (or (get-in db topic-entries-key) []) ;(or (:entries-data (get (dispatcher/board-data db) topic)) [])
        without-current-entry (vec (filter #(not= (:created-at %) created-at) entries-data))
        with-new-entry (conj without-current-entry without-placeholder)
        sorted-entries (vec (sort #(compare (:created-at %2) (:created-at %1)) with-new-entry))]
    (if (not (:placeholder topic-data))
      (api/partial-update-topic topic without-placeholder)
      (api/save-or-create-topic without-placeholder))
    (-> db
      (assoc-in (conj (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug)) (keyword topic)) without-placeholder)
      (assoc-in topic-entries-key sorted-entries)
      (stop-foce))))

(defmethod dispatcher/action :force-fullscreen-edit [db [_ topic]]
  (if topic
    (assoc-in db [:force-edit-topic] topic)
    (dissoc db :force-edit-topic)))

(defn- save-topic [db topic topic-data]
  (let [old-topic-data (get (dispatcher/board-data db) (keyword topic))
        new-data (dissoc (merge old-topic-data topic-data) :placeholder)]
    (api/partial-update-topic topic (dissoc topic-data :placeholder))
    (assoc-in db (conj (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug)) (keyword topic)) new-data)))

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
  (api/get-teams)
  (assoc db :enumerate-users-requested true))

(defmethod dispatcher/action :teams-loaded
  [db [_ teams dont-follow-team-link]]
  (when-not dont-follow-team-link
    (doseq [team teams
            :let [team-link (utils/link-for (:links team) "item" "GET")]]
      (api/get-team team-link)))
  (assoc-in db [:enumerate-users :teams] teams))

(defmethod dispatcher/action :team-loaded
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

(defmethod dispatcher/action :invite-by-email
  [db [_]]
  (let [org-data (dispatcher/org-data)
        email (:email (:um-invite db))
        user-type (:user-type (:um-invite db))
        parsed-email (utils/parse-input-email email)
        email-name (:name parsed-email)
        email-address (:address parsed-email)
        user  (first (filter #(= (:email %) email-address) (:users (get (:enumerate-users db) (:team-id org-data)))))
        old-user-type (when user (utils/get-user-type user))
        new-user-type (:user-type (:um-invite db))]
    ;; Send the invitation only if the user is not part of the team already
    ;; or if it's still pending, ie resend the invitation email
    (when (or (not user)
              (and user
                   (= (:status user) "pending")))
      (let [splitted-name (string/split email-name #"\s")
            name-size (count splitted-name)
            splittable-name? (= name-size 2)
            first-name (cond
                        (= name-size 1) email-name
                        splittable-name? (first splitted-name)
                        :else "")
            last-name (cond
                        splittable-name? (second splitted-name)
                        :else "")]
        ;; If the user is already in the list
        ;; but the type changed we need to change the user type too
        (when (and user
                  (not= old-user-type new-user-type))
          (api/switch-user-type old-user-type new-user-type user (utils/get-author (:user-id user))))
        (api/send-invitation email-address (:user-type (:um-invite db)) first-name last-name)
        (update-in db [:um-invite] dissoc :error)))))

(defmethod dispatcher/action :invite-by-email/success
  [db [_]]
  ; refresh the users list once the invitation succeded
  (api/get-teams)
  (assoc db :um-invite {:email ""
                        :user-type nil
                        :error nil}))

(defmethod dispatcher/action :invite-by-email/failed
  [db [_ email]]
  ; refresh the users list once the invitation succeded
  (api/get-teams)
  (assoc-in db [:um-invite :error] true))

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
  (api/get-teams)
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

(defmethod dispatcher/action :reset-updates-list
  [db [_]]
  ; Reset flag to reload su list when needed
  (dissoc db :updates-list-loaded))

(defmethod dispatcher/action :entries-loaded
  [db [_ {:keys [topic entries]}]]
  (let [fixed-entries (map #(utils/fix-topic % topic) (:items (:collection entries)))
        sort-pred (fn [a b] (compare (:created-at b) (:created-at a)))
        sorted-fixed-entries (vec (sort sort-pred fixed-entries))]
    (assoc-in db (dispatcher/topic-entries-key (router/current-org-slug) (router/current-board-slug) topic) sorted-fixed-entries)))

(defmethod dispatcher/action :show-add-topic
  [db [_ active]]
  (if active
    (do
      (utils/after 100 #(router/nav! (oc-urls/board)))
      (-> db
        (assoc :show-add-topic true)
        (dissoc :selected-topic-view)))
    (assoc db :show-add-topic false)))

(defmethod dispatcher/action :dashboard-select-topic
  [db [_ topic-kw]]
  (if (utils/in? (:dashboard-selected-topics db) topic-kw)
    (assoc db :dashboard-selected-topics (utils/vec-dissoc (:dashboard-selected-topics db) topic-kw))
    (let [topics (to-array (:topics (dispatcher/board-data db)))
          all-selected-topics (vec (conj (or (:dashboard-selected-topics db) []) topic-kw))
          next-selected-topics (vec (map keyword (filter #(utils/in? all-selected-topics (keyword %)) topics)))]
      (assoc db :dashboard-selected-topics next-selected-topics))))

(defmethod dispatcher/action :dashboard-select-all
  [db [_ topic-kw]]
  (assoc db :dashboard-selected-topics (vec (map keyword (:topics (dispatcher/board-data db))))))

(defmethod dispatcher/action :dashboard-share-mode
  [db [_ activate]]
  (-> db
    (assoc :dashboard-sharing activate)
    (dissoc :show-add-topic)
    (assoc :dashboard-selected-topics [])))

(defmethod dispatcher/action :add-topic
  [db [_ topic topic-data]]
  (let [board-data (dispatcher/board-data)
        archived-topics (:archived board-data)
        updated-archived (if (:was-archived topic-data)
                            (vec (filter #(not= (:topic %) (name topic)) archived-topics))
                            archived-topics)
        updated-topics (conj (:topics board-data) (name topic))
        updated-board-data (-> board-data
                                (assoc :topics updated-topics)
                                (assoc :archived updated-archived)
                                (assoc (keyword topic) topic-data))
        board-data-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        next-db (assoc-in db board-data-key updated-board-data)]
    (if (:was-archived topic-data)
     next-db
     (start-foce next-db topic topic-data))))

(defmethod dispatcher/action :rollback-add-topic
  [db [_ topic-kw]]
  (let [board-data-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
        board-data (get-in db board-data-key)
        topic-data (get board-data topic-kw)
        archived-topics (:archived board-data)
        updated-archived (if (:was-archived topic-data)
                            (conj archived-topics {:topic (name topic-kw)
                                                   :title (:title topic-data)})
                            archived-topics)
        updated-topics (utils/vec-dissoc (:topics board-data) (name topic-kw))
        updated-board-data (-> board-data
                                (dissoc topic-kw)
                                (assoc :topics updated-topics)
                                (assoc :archived updated-archived))]
    (-> db
      (assoc-in board-data-key updated-board-data)
      (stop-foce))))

(defmethod dispatcher/action :show-top-menu [db [_ topic]]
  (assoc db :show-top-menu topic))

(defmethod dispatcher/action :hide-welcome-screen
  [db [_]]
  (dissoc db :show-welcome-screen))

(defmethod dispatcher/action :reset-user-profile
  [db [_]]
  (assoc db :edit-user-profile (assoc (:current-user-data db) :password "")))

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
    (api/get-teams))
  (-> db
      (assoc-in [:um-domain-invite :domain] (if success "" (:domain (:um-domain-invite db))))
      (assoc :add-email-domain-team-error (if success false true))))

(defmethod dispatcher/action :add-slack-team
  [db [_]]
  (let [teams-data (:enumerate-users db)
        org-data (dispatcher/org-data db)
        team-data (get teams-data (:team-id org-data))
        add-slack-team-link (utils/link-for (:links team-data) "authenticate" "GET" {:auth-source "slack"})]
    (when add-slack-team-link
      (router/redirect! (:href add-slack-team-link))))
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

(defmethod dispatcher/action :create-org
  [db [_]]
  (let [org-name (:create-org db)]
    (when-not (string/blank? org-name)
      (api/create-org org-name)))
  db)

(defmethod dispatcher/action :create-board
  [db [_]]
  (let [board-name (:create-board db)]
    (when-not (string/blank? board-name)
      (api/create-board board-name)))
  db)