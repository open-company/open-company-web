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

;; Get the board to show counting the last accessed and the last created

(defn newest-org [orgs]
  (first (sort #(compare (:created-at %1) (:created-at %2)) orgs)))

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
  (first (sort #(compare (:name %1) (:name %2)) boards)))

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
             (not (utils/in? (:route @router/path) "create-board"))
             (not (utils/in? (:route @router/path) "email-verification")))
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
      (and (not (utils/in? (:route @router/path) "create-board"))
           (not (utils/in? (:route @router/path) "create-org"))
           (not (utils/in? (:route @router/path) "org-team-settings"))
           (not (utils/in? (:route @router/path) "org-settings"))
           (not (utils/in? (:route @router/path) "updates-list"))
           (not (utils/in? (:route @router/path) "su-snapshot"))
           (not (utils/in? (:route @router/path) "su-snapshot-preview"))
           (not (utils/in? (:route @router/path) "email-verification")))
      (cond
        ;; Redirect to the first board if only one is presnet
        (>= (count boards) 1)
        (if (responsive/is-tablet-or-mobile?)
          (router/nav! (oc-urls/boards))
          (let [board-to (get-default-board org-data)]
            (router/nav! (oc-urls/board (:slug org-data) (:slug board-to)))))
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
        (assoc :show-add-topic (if (and is-currently-shown                   ;; Is the currently shown board
                                        (not (:foce-key db))                 ;; User is not editing
                                        (not (router/current-topic-slug))    ;; There is not a topic shown
                                        (not (:read-only fixed-board-data))) ;; The user has write permissions on the board
                                 (or (:show-add-topic db) (zero? (count (:topics fixed-board-data))))  ;; Keep add topic if currently shown or show it if the new topics count is 0
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

(defmethod dispatcher/action :entry [db [_ body]]
  (if body
    (let [fixed-topic (utils/fix-topic (:body body) (:topic-slug body) true)
          assoc-in-coll (dispatcher/entry-key (:org-slug body) (:board-slug body) (:topic-slug body) (:as-of body))
          next-db (assoc-in db assoc-in-coll true)]
      next-db)
    db))

(defmethod dispatcher/action :topic [db [_ {:keys [topic body]}]]
  ;; Refresh topic entries
  (api/load-entries topic (utils/link-for (:links body) "up"))
  (if body
    (let [fixed-topic (utils/fix-topic body topic)]
      (assoc-in db (dispatcher/board-topic-key (router/current-org-slug) (router/current-board-slug) topic) fixed-topic))
    db))

(defmethod dispatcher/action :topic-entry [db [_ {:keys [topic body created-at]}]]
  ;; Refresh topic entries
  (api/load-entries topic (utils/link-for (:links body) "up"))
  (if body
    (let [entries-key (dispatcher/topic-entries-key (router/current-org-slug) (router/current-board-slug) topic)
          old-entries (get-in db entries-key)
          fixed-topic (utils/fix-topic body topic)
          entry-index (utils/index-of old-entries #(= (:created-at %) created-at))
          new-entries (if (not (nil? entry-index))
                        (assoc old-entries entry-index fixed-topic)
                        (conj old-entries fixed-topic))
          sorted-entries (vec (sort #(compare (:created-at %2) (:created-at %1)) new-entries))
          board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))
          old-board-data (get-in db board-key)
          new-board-data (assoc old-board-data (keyword topic) (first sorted-entries))]
      (-> db
        (assoc-in entries-key sorted-entries)
        (assoc-in board-key new-board-data)))
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
          topic-entries-data-key (when (router/current-topic-slug)
                                      (dispatcher/topic-entries-key org board (keyword (router/current-topic-slug))))
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
          keeping-entries (if (router/current-topic-slug)
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

(defmethod dispatcher/action :su-edit [db [_ {:keys [su-date su-slug medium] :as update-data}]]
  (let [latest-su-key (dispatcher/latest-update-key (router/current-org-slug))]
    (if (nil? update-data)
      (-> db
        (dissoc :loading)
        (assoc-in latest-su-key ""))
      (let [su-url   (oc-urls/update-link (router/current-org-slug) (utils/su-date-from-created-at su-date) su-slug)
            org-data-links (:links (dispatcher/org-data))
            updates-list-link (utils/link-for org-data-links "collection" "GET" {:accept "application/vnd.collection+vnd.open-company.update+json;version=1"})
            updated-link (assoc updates-list-link :count (inc (:count updates-list-link)))
            new-links (conj (utils/vec-dissoc org-data-links updates-list-link) updated-link)]
        (-> db
          (assoc-in latest-su-key su-url)
          (dissoc :loading)
          (assoc-in (conj (dispatcher/org-data-key (router/current-org-slug)) :links) new-links)
          ; refresh the su list
          (get-updates))))))

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
        board-key (dispatcher/board-data-key (router/current-org-slug) (router/current-board-slug))]
    (api/patch-topics new-topics)
    (-> db
      (dissoc :foce-key)
      (dissoc :foce-data)
      (dissoc :foce-data-editing?)
      (assoc :show-add-topic (zero? (count new-topics)))
      (assoc-in (conj board-key :topics) new-topics)
      (assoc-in (conj board-key :archived) new-archived))))

(defmethod dispatcher/action :archive-topic [db [_ topic]]
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
      (assoc :show-add-topic (zero? (count new-topics)))
      (assoc-in (conj board-key :topics) new-topics)
      (assoc-in (conj board-key :archived) new-archived))))

(defmethod dispatcher/action :archive-topic/success
  [db [_]]
  (router/nav! (oc-urls/board))
  (dissoc db :prevent-topic-not-found-navigation))

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
      (assoc :prevent-topic-not-found-navigation true)
      (assoc-in board-data-key with-fixed-topics)
      (assoc-in (dispatcher/topic-entries-key (router/current-org-slug) (router/current-board-slug) topic) new-entries))))

(defmethod dispatcher/action :delete-entry/success
  [db [_ should-redirect-to-board]]
  (when should-redirect-to-board
    (router/nav! (oc-urls/board)))
  (dissoc db :prevent-topic-not-found-navigation))

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
        org-data (dispatcher/org-data db)
        team-id (:team-id org-data)
        team-data (dispatcher/team-data team-id)
        auth-link (utils/link-for (:links team-data) "bot")
        user-data (:current-user-data db)
        fixed-auth-url (utils/slack-link-with-state (:href auth-link) (:user-id user-data) team-id (oc-urls/org (:slug org-data)))]
    (cook/set-cookie! :login-redirect current (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
    (router/redirect! fixed-auth-url))
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
  (if (empty? jwt)
    (assoc db :signup-with-email-error :verify-email)
    (do
      (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
      (api/get-entry-point)
      (dissoc db :show-login-overlay))))

(defmethod dispatcher/action :get-auth-settings
  [db [_]]
  (api/get-auth-settings)
  db)

(defmethod dispatcher/action :get-teams
  [db [_]]
  (api/get-teams)
  (assoc db :teams-data-requested true))

(defmethod dispatcher/action :teams-loaded
  [db [_ teams]]
  (doseq [team teams
          :let [team-link (utils/link-for (:links team) "item" "GET")
                roster-link (utils/link-for (:links team) "roster" "GET")]]
    (if team-link
      (api/get-team team-link)
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
                             :links (:links (:collection roster-data))
                             :users (:items (:collection roster-data))}]
      (assoc-in db (dispatcher/team-data-key (:team-id roster-data)) fixed-roster-data))
    db))

(defmethod dispatcher/action :enumerate-channels
  [db [_ team-id]]
  (api/enumerate-channels team-id)
  (assoc db :enumerate-channels-requested true))

(defmethod dispatcher/action :enumerate-channels/success
  [db [_ team-id channels]]
  (let [channels-key (dispatcher/team-channels-key team-id)]
    (if channels
      (assoc-in db channels-key channels)
      (-> db
        (update-in (butlast channels-key) dissoc (last channels-key))
        (dissoc :enumerate-channels-requested)))))

(defmethod dispatcher/action :invite-by-email
  [db [_]]
  (let [org-data (dispatcher/org-data)
        email (:email (:um-invite db))
        user-type (:user-type (:um-invite db))
        parsed-email (utils/parse-input-email email)
        email-name (:name parsed-email)
        email-address (:address parsed-email)
        team-data (dispatcher/team-data (:team-id org-data))
        user  (first (filter #(= (:email %) email-address) (:users team-data)))
        old-user-type (when user (utils/get-user-type user org-data))
        new-user-type (:user-type (:um-invite db))]
    ;; Send the invitation only if the user is not part of the team already
    ;; or if it's still pending, ie resend the invitation email
    (when (or (not user)
              (and user
                   (= (string/lower-case (:status user)) "pending")))
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
          (api/switch-user-type old-user-type new-user-type user (utils/get-author (:user-id user) (:authors org-data))))
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

(defmethod dispatcher/action :collect-pswd
  [db [_]]
  (let [form-data (:collect-pswd db)]
    (api/collect-password (:pswd form-data)))
  db)

(defmethod dispatcher/action :collect-pswd-finish
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
      (assoc db :show-add-topic true))
    (assoc db :show-add-topic false)))

(defmethod dispatcher/action :dashboard-select-topic
  [db [_ board-slug topic-slug]]
  (if (pos? (count (filter #(and (= board-slug (:board-slug %)) (= topic-slug (:topic-slug %))) (:dashboard-selected-topics db))))
    (assoc db :dashboard-selected-topics (filter #(or (not= board-slug (:board-slug %)) (not= topic-slug (:topic-slug %))) (:dashboard-selected-topics db)))
    (let [board-data (dispatcher/board-data db (router/current-org-slug) board-slug)
          topic-data (get board-data (keyword topic-slug))
          new-entry {:created-at (:created-at topic-data) :board-slug board-slug :topic-slug topic-slug}
          next-selected-topics (vec (conj (or (:dashboard-selected-topics db) []) new-entry))]
      (assoc db :dashboard-selected-topics next-selected-topics))))

(defmethod dispatcher/action :dashboard-select-all
  [db [_ board-slug]]
  (let [without-board-topics (filter #(not= (:board-slug %) board-slug) (:dashboard-selected-topics db))
        board-data (dispatcher/board-data db (router/current-org-slug) board-slug)
        all-topics (:topics board-data)
        new-entries (vec (map #(hash-map :created-at (:created-at (get board-data (keyword %))) :board-slug board-slug :topic-slug (keyword %)) all-topics))]
    (assoc db :dashboard-selected-topics (vec (concat without-board-topics new-entries)))))

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
    (when (:was-archived topic-data)
      (api/patch-topics updated-topics))
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
  (-> db
    (assoc :edit-user-profile (assoc (:current-user-data db) :password ""))
    (dissoc :edit-user-profile-failed)))

(defmethod dispatcher/action :user-data
  [db [_ user-data]]
  (-> db
      (assoc :current-user-data user-data)
      (assoc :edit-user-profile user-data)
      (dissoc :edit-user-profile-failed)))

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

(defmethod dispatcher/action :delete-board
  [db [_ board-slug]]
  (api/delete-board board-slug)
  db)

(defmethod dispatcher/action :show-error-banner
  [db [_ error-message error-time]]
  (if (empty? error-message)
    (-> db (dissoc :error-banner-message) (dissoc :error-banner-time))
    (if (not (:error-banner db))
      (-> db
       (assoc :error-banner-message error-message)
       (assoc :error-banner-time error-time))
      db)))

(defmethod dispatcher/action :user-profile-update-failed
  [db [_]]
  (assoc db :edit-user-profile-failed true))