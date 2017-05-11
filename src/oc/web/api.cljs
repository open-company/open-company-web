(ns oc.web.api
  (:require-macros [cljs.core.async.macros :refer (go)]
                   [if-let.core :refer (when-let*)])
  (:require [cljs.core.async :as async :refer (<!)]
            [cljs-http.client :as http]
            [taoensso.timbre :as timbre]
            [cognitect.transit :as t]
            [clojure.walk :refer (keywordize-keys stringify-keys)]
            [clojure.string :as s]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as j]
            [oc.web.router :as router]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.raven :as sentry]))

(def ^:private api-endpoint ls/api-server-domain)

(def ^:private auth-endpoint ls/auth-server-domain)

(def ^:private pay-endpoint ls/pay-server-domain)

(defn- content-type [type]
  (str "application/vnd.open-company." type ".v1+json;charset=UTF-8"))

(defn- json->cljs [json]
  (let [reader (t/reader :json)]
    (keywordize-keys (t/read reader json))))

(defn- cljs->json [coll]
  (let [stringified-coll (stringify-keys coll)]
    (clj->js stringified-coll)))

(defn complete-params [params]
  (if-let [jwt (j/jwt)]
    (-> {:with-credentials? false}
        (merge params)
        (update :headers merge {"Authorization" (str "Bearer " jwt)}))
    params))

(defn headers-for-link [link]
 (let [acah-headers (cond
                      ; If there is Access-Control-Allow-Headers and it's nil do not set it
                      (and (contains? link :access-control-allow-headers)
                           (nil? (:access-control-allow-headers link)))
                      {}
                      ; If there is Access-Control-Allow-Headers and it contains a string
                      (and (contains? link :access-control-allow-headers)
                           (:access-control-allow-headers link))
                      {"Access-Control-Allow-Headers" (:access-control-allow-headers link)}
                      ; else set the default
                      :else
                      {"Access-Control-Allow-Headers" "Content-Type, Authorization"})
       with-content-type (if (:content-type link) (assoc acah-headers "content-type" (:content-type link)) acah-headers)
       with-accept (if (:accept link) (assoc with-content-type "accept" (:accept link)) with-content-type)]
  with-accept))

(defn refresh-jwt [refresh-link]
  (let [refresh-url (if (map? refresh-link)
                      (str ls/auth-server-domain (:href refresh-link))
                      refresh-link)
        headers (if (map? refresh-link) {:headers (headers-for-link refresh-link)} {})]
  (http/get refresh-url (complete-params headers))))

(defn update-jwt-cookie! [jwt]
  (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
  (utils/after 1 #(dispatcher/dispatch! [:jwt jwt])))

(defn- method-name [method]
  (cond
    (= method http/delete)
    "DELETE"
    (= method http/get)
    "GET"
    (= method http/head)
    "HEAD"
    (= method http/jsonp)
    "JSONP"
    (= method http/move)
    "MOVE"
    (= method http/options)
    "OPTIONS"
    (= method http/patch)
    "PATCH"
    (= method http/post)
    "POST"
    (= method http/put)
    "PUT"))

(defn- req [endpoint method path params on-complete]
  (timbre/debug "Req:" (method-name method) (str endpoint path))
  (let [jwt (j/jwt)]
    (go
      (when (and jwt (j/expired?) )
        (if-let [refresh-url (j/get-key :refresh-url)]
          (let [res (<! (refresh-jwt refresh-url))]
            (if (:success res)
              (update-jwt-cookie! (:body res))
              (dispatcher/dispatch! [:logout])))
          (dispatcher/dispatch! [:logout])))

      (let [{:keys [status body] :as response} (<! (method (str endpoint path) (complete-params params)))]
        (timbre/debug "Resp:" (method-name method) (str endpoint path) status)
        ; when a request get a 401 logout the user since his using an old token, need to repeat auth process
        ; no token refresh
        (when (and (j/jwt)
                   (= status 401))
          (router/redirect! oc-urls/logout))
        ; report all 5xx to sentry
        (when (or (= status 0)
                  (and (>= status 500) (<= status 599))
                  (= status 400)
                  (= status 422))
          ; If it was a 5xx or a 0 show a banner for network issues
          (when (or (= status 0)
                    (and (>= status 500) (<= status 599)))
            (dispatcher/dispatch! [:error-banner-show utils/generic-network-error 10000]))
          (let [report {:response response
                        :path path
                        :method (method-name method)
                        :jwt (j/jwt)
                        :params params}]
            (timbre/error "xhr response error:" (method-name method) ":" (str endpoint path) " -> " status)
            (sentry/set-user-context! report)
            (sentry/capture-message (str "xhr response error:" status))
            (sentry/set-user-context! nil)))
        (on-complete response)))))

(def ^:private api-get (partial req api-endpoint http/get))
(def ^:private api-post (partial req api-endpoint http/post))
(def ^:private api-put (partial req api-endpoint http/put))
(def ^:private api-patch (partial req api-endpoint http/patch))
(def ^:private api-delete (partial req api-endpoint http/delete))

(def ^:private auth-get (partial req auth-endpoint http/get))
(def ^:private auth-post (partial req auth-endpoint http/post))
(def ^:private auth-put (partial req auth-endpoint http/put))
(def ^:private auth-patch (partial req auth-endpoint http/patch))
(def ^:private auth-delete (partial req auth-endpoint http/delete))

(def ^:private pay-get (partial req pay-endpoint http/get))
(def ^:private pay-post (partial req pay-endpoint http/post))

(defn dispatch-body [action response]
  (let [body (if (:success response) (json->cljs (:body response)) {})]
    (dispatcher/dispatch! [action body])))

(defn api-500-test [with-response]
  (api-get (if with-response "/---error-test---" "/---500-test---")
    {:headers {
      ; required by Chrome
      "Access-Control-Allow-Headers" "Content-Type"
      ; custom content type
      "content-type" "text/plain"}}
    (fn [_])))

(defn auth-500-test [with-response]
  (auth-get (if with-response "/---error-test---" "/---500-test---")
    {:headers (headers-for-link {:content-type "text/plain"})}
    (fn [_])))

(defn get-entry-point []
  (let [entry-point-href (str "/" (when (:org @router/path) (str "?requested=" (:org @router/path))))]
    (api-get entry-point-href
     nil
     (fn [{:keys [success body]}]
       (let [fixed-body (if success (json->cljs body) {})]
         (dispatcher/dispatch! [:entry-point {:success success :collection (:collection fixed-body)}]))))))

(defn get-subscription [company-uuid]
  (pay-get (str "/subscriptions/" company-uuid)
           nil
           (fn [response]
             (let [body (if (:success response) (:body response) {})]
               (dispatcher/dispatch! [:subscription body])))))

(defn get-org [org-data]
  (when-let [org-link (or (utils/link-for (:links org-data) "item" "GET") (utils/link-for (:links org-data) "self" "GET"))]
    (api-get (:href org-link)
      {:headers (headers-for-link org-link)}
      (fn [{:keys [status body success]}]
        (dispatcher/dispatch! [:org (json->cljs body)])))))

(defn get-board [board-data]
  (when-let [board-link (or (utils/link-for (:links board-data) "item" "GET") (utils/link-for (:links board-data) "self" "GET"))]
    (api-get (:href board-link)
      {:headers (headers-for-link board-link)}
      (fn [{:keys [status body success]}]
        (dispatcher/dispatch! [:board (json->cljs body)])))))

(defn patch-board [data]
  (when data
    (let [board-data (dissoc data :links :read-only)
          json-data (cljs->json board-data)
          links (:links (dispatcher/board-data))
          board-patch-link (utils/link-for links "partial-update")]
      (api-patch (:href board-patch-link)
        {:json-params json-data
         :headers (headers-for-link board-patch-link)}
        (fn [{:keys [success body status]}]
          (dispatcher/dispatch! [:board (json->cljs body)])
          ;; Refresh the org data to make sure the list of boards is updated
          (get-org (dispatcher/org-data)))))))

(defn patch-org [data]
  (when data
    (let [org-data (dissoc data :links :read-only)
          json-data (cljs->json org-data)
          links (:links (dispatcher/org-data))
          org-patch-link (utils/link-for links "partial-update")]
      (api-patch (:href org-patch-link)
        {:json-params json-data
         :headers (headers-for-link org-patch-link)}
        (fn [{:keys [success body status]}]
          (dispatcher/dispatch! [:org (json->cljs body)]))))))

(defn get-auth-settings []
  (auth-get "/"
    {:headers (headers-for-link {:access-control-allow-headers nil :content-type "application/json"})}
    (fn [response]
      (let [body (if (:success response) (:body response) false)]
        (dispatcher/dispatch! [:auth-settings body])))))

(def topic-private-keys [:topic
                         :entries
                         :author
                         :links
                         :loading
                         :as-of
                         :read-only
                         :entries-cache
                         :title-placeholder
                         :body-placeholder
                         :oc-editing
                         :entries-data
                         :new
                         :placeholder
                         :was-archived
                         :created-at
                         :updated-at
                         :value
                         :burn-rate
                         :runway])

(defn save-or-create-topic [topic-data]
  (when topic-data
    (let [links (:links topic-data)
          topic (keyword (:topic topic-data))
          cleaned-topic-data (apply dissoc topic-data topic-private-keys)
          json-data (cljs->json cleaned-topic-data)
          topic-link (utils/link-for links "create" "POST")]
      (api-post (:href topic-link)
        { :json-params json-data
          :headers (headers-for-link topic-link)}
        (fn [{:keys [body success headers]}]
          (let [fixed-body (if success (json->cljs body) {})]
            (dispatcher/dispatch! [:topic {:body fixed-body :topic topic}])))))))

(defn load-entries [topic entries-link]
  (when (and topic entries-link)
    (api-get (:href entries-link)
      {:headers (headers-for-link entries-link)}
      (fn [{:keys [status body success]}]
        (dispatcher/dispatch! [:entries-loaded {:topic topic :entries (if success (json->cljs body) {})}])))))

(defn partial-update-topic
  "PATCH a topic, dispatching the results with a `:topic` action."
  ([topic topic-data]
  (when (and topic topic-data)
    (let [partial-update-link (utils/link-for (:links topic-data) "partial-update" "PATCH")
          cleaned-topic-data (apply dissoc topic-data topic-private-keys)
          json-data (cljs->json cleaned-topic-data)]
      (api-patch (:href partial-update-link)
        { :json-params json-data
          :headers (headers-for-link partial-update-link)}
        (fn [{:keys [success body]}]
          (let [fixed-body (if success (json->cljs body) {})]
            (dispatcher/dispatch! [:topic-entry {:body fixed-body :topic topic :created-at (:created-at fixed-body)}]))))))))

(defn patch-topics [topics & [new-topic topic-name]]
  (when topics
    (let [slug (keyword (router/current-board-slug))
          board-data (dispatcher/board-data)
          board-patch-link (utils/link-for (:links board-data) "partial-update" "PATCH")
          payload (if (and new-topic topic-name)
                    {:topics topics
                     (keyword topic-name) new-topic}
                    {:topics topics})
          json-data (cljs->json payload)]
      (api-patch (:href board-patch-link)
        {:json-params json-data
         :headers (headers-for-link board-patch-link)}
        (fn [{:keys [success body status]}]
          (dispatcher/dispatch! [:board (when success (json->cljs body))]))))))

(defn remove-topic [topic-name]
  (when (and topic-name)
    (let [board-data (dispatcher/board-data)
          topics (:topics board-data)
          new-topics (apply merge (map (fn [[k v]]
                                        {k (utils/vec-dissoc v topic-name)})
                                    topics))]
      (patch-topics new-topics))))

(defn get-new-topics []
  "Load new topics, avoid to start multiple request or reload it if it's already loading or loaded.
   It's possible to force the load passing an optional boolean parameter."
  (let [slug (keyword (router/current-board-slug))
        board-data (dispatcher/board-data)
        links (:links board-data)
        add-topic-link (utils/link-for links "new" "GET")]
    (when add-topic-link
      (api-get (:href add-topic-link)
        { :headers (headers-for-link add-topic-link)}
        (fn [{:keys [success body]}]
          (let [fixed-body (if body (json->cljs body) {})]
            (dispatcher/dispatch! [:new-topics-load/finish {:response fixed-body :slug slug}])))))))

(defn create-update [update-data]
  (let [org-data   (dispatcher/org-data)
        share-link (utils/link-for (:links org-data) "create" "POST" {:accept "application/vnd.open-company.update.v1+json"})
        json-data  (merge update-data {:title (:su-title @dispatcher/app-state)
                                       :entries (vec (map #(dissoc % :board-slug) (:dashboard-selected-topics @dispatcher/app-state)))})]
    (api-post (:href share-link)
              {:json-params (cljs->json json-data)
               :headers (headers-for-link share-link)}
      (fn [{:keys [success body]}]
        (if success
          (let [fixed-body (json->cljs body)]
            (dispatcher/dispatch! [:su-edit {:su-slug (:slug fixed-body) :su-date (:created-at fixed-body) :medium (:medium fixed-body)}]))
          (dispatcher/dispatch! [:su-edit nil]))))))

(defn get-updates []
  (let [org-data (dispatcher/org-data)
        links (:links org-data)
        su-link (utils/link-for links "collection" "GET")]
    (when su-link
      (api-get (:href su-link)
        {:headers (headers-for-link su-link)}
        (fn [{:keys [success body]}]
          (let [fixed-body (if success (json->cljs body) {})]
            (dispatcher/dispatch! [:updates-list {:response fixed-body}])))))))

(defn get-update
  ([update-data load-org-data]
    (when update-data
      (let [update-link (utils/link-for (:links update-data) "self")]
        (api-get (:href update-link)
          {:headers (headers-for-link update-link)}
          (fn [{:keys [success body] :as response}]
            (let [fixed-body (if success (json->cljs body) {})
                  response {:org-slug (router/current-org-slug)
                            :update-slug (keyword (:slug update-data))
                            :response fixed-body
                            :load-org-data load-org-data}]
              (dispatcher/dispatch! [:update-loaded response]))))))))

(defn get-update-with-slug
  ([org-slug update-slug load-org-data]
    (when (and org-slug update-slug)
      (let [update-link (str "/orgs/" org-slug "/updates/" update-slug)]
        (api-get update-link
          { :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (content-type "stakeholder-update")}}
          (fn [{:keys [success body]}]
            (let [fixed-body (if success (json->cljs body) {})
                  response {:org-slug (keyword org-slug)
                            :update-slug (keyword update-slug)
                            :response fixed-body
                            :load-org-data load-org-data}]
              (dispatcher/dispatch! [:update-loaded response]))))))))

(defn auth-with-email [email pswd]
  (when (and email pswd)
    (let [email-links (:links (:auth-settings @dispatcher/app-state))
          auth-url (utils/link-for email-links "authenticate" "GET" {:auth-source "email"})]
      (auth-get (:href auth-url)
        {:basic-auth {
          :username email
          :password pswd}
         :headers (headers-for-link auth-url)}
        (fn [{:keys [success body status]}]
         (if success
            (dispatcher/dispatch! [:login-with-email/success body])
            (cond
              (= status 401)
              (dispatcher/dispatch! [:login-with-email/failed 401])
              :else
              (dispatcher/dispatch! [:login-with-email/failed 500]))))))))

(defn auth-with-token [token]
  (when token
    (let [token-links (:links (:auth-settings @dispatcher/app-state))
          auth-url (utils/link-for token-links "authenticate" "GET" {:auth-source "email"})]
      (auth-get (:href auth-url)
        {:headers (merge (headers-for-link auth-url)
                   {; required by Chrome
                    "Access-Control-Allow-Headers" "Content-Type, Authorization"
                    "Authorization" (str "Bearer " token)})}
        (fn [{:keys [success body status]}]
         (if success
            (do
              (update-jwt-cookie! body)
              (dispatcher/dispatch! [:jwt (j/get-contents)])
              (dispatcher/dispatch! [:auth-with-token/success body]))
            (cond
              (= status 401)
              (dispatcher/dispatch! [:auth-with-token/failed 401])
              :else
              (dispatcher/dispatch! [:auth-with-token/failed 500]))))))))

(defn signup-with-email [first-name last-name email pswd]
  (when (and first-name last-name email pswd)
    (let [email-links (:links (:auth-settings @dispatcher/app-state))
          auth-url (utils/link-for email-links "create" "POST" {:auth-source "email"})]
      (auth-post (:href auth-url)
        {:json-params {:first-name first-name
                       :last-name last-name
                       :email email
                       :password pswd}
         :headers (headers-for-link auth-url)}
        (fn [{:keys [success body status]}]
         (if success
            (dispatcher/dispatch! [:signup-with-email/success body])
            (dispatcher/dispatch! [:signup-with-email/failed status])))))))

(defn get-teams []
  (let [enumerate-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "collection" "GET")]
    (auth-get (:href enumerate-link)
      {:headers (headers-for-link enumerate-link)}
      (fn [{:keys [success body status]}]
        (let [fixed-body (if success (json->cljs body) {})]
          (if success
            (dispatcher/dispatch! [:teams-loaded (-> fixed-body :collection :items)])
            ;; Reset the team-data-requested to restart the teams load
            (when (and (>= status 500)
                       (<= status 599))
              (dispatcher/dispatch! [:input [:team-data-requested] false]))))))))

(defn get-team [team-link]
  (when team-link
    (auth-get (:href team-link)
      {:headers (headers-for-link team-link)}
      (fn [{:keys [success body status]}]
        (let [fixed-body (if success (json->cljs body) {})]
          (if success
            (if (= (:rel team-link) "roster")
              (dispatcher/dispatch! [:team-roster-loaded fixed-body])
              (dispatcher/dispatch! [:team-loaded fixed-body]))))))))

(defn enumerate-channels [team-id]
  (when team-id
    (let [team-data (dispatcher/team-data team-id)
          enumerate-link (utils/link-for (:links team-data) "channels" "GET")]
      (when enumerate-link
        (auth-get (:href enumerate-link)
          {:headers (headers-for-link enumerate-link)}
          (fn [{:keys [success body status]}]
            (let [fixed-body (if success (json->cljs body) {})]
              (if success
                (dispatcher/dispatch! [:channels-enumerate/success team-id (-> fixed-body :collection :items)])))))))))

(defn user-action [action-link payload]
  (when action-link
    (let [auth-req (case (:method action-link)
                      "POST" auth-post
                      "PUT" auth-put
                      "PATCH" auth-patch
                      "DELETE" auth-delete
                      auth-get) ; default to GET
          headers {:headers (headers-for-link action-link)}
          with-payload (if payload
                          (assoc headers :json-params payload)
                          headers)]
      (auth-req (:href action-link)
        with-payload
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch! [:user-action/complete]))))))

(defn confirm-invitation [token]
  (let [auth-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "authenticate" "GET" {:auth-source "email"})]
    (when (and token auth-link)
      (auth-get (:href auth-link)
        {:headers (merge (headers-for-link auth-link)
                         {; required by Chrome
                          "Access-Control-Allow-Headers" "Content-Type, Authorization"
                          "Authorization" (str "Bearer " token)})}
        (fn [{:keys [status body success]}]
          (dispatcher/dispatch! [:invitation-confirmed status])
          (utils/after 1000
           #(when success
             (update-jwt-cookie! body))))))))

(defn collect-name-password [firstname lastname pswd]
  (let [update-link (utils/link-for (:links (:current-user-data @dispatcher/app-state)) "partial-update" "PATCH")]
    (when (and (or firstname lastname) pswd update-link)
      (auth-patch (:href update-link)
        {:json-params {
          :first-name firstname
          :last-name lastname
          :password pswd
         }
         :headers (headers-for-link update-link)}
        (fn [{:keys [status body success]}]
          (when success
            (dispatcher/dispatch! [:user-data (json->cljs body)]))
          (utils/after 100 #(dispatcher/dispatch! [:name-pswd-collect/finish status])))))))

(defn collect-password [pswd]
  (let [update-link (utils/link-for (:links (:current-user-data @dispatcher/app-state)) "partial-update" "PATCH")]
    (when (and pswd update-link)
      (auth-patch (:href update-link)
        {:json-params {
          :password pswd}
         :headers (headers-for-link update-link)}
        (fn [{:keys [status body success]}]
          (when success
            (dispatcher/dispatch! [:user-data (json->cljs body)]))
          (utils/after 100 #(dispatcher/dispatch! [:pswd-collect/finish status])))))))

(defn delete-entry [topic entry-data should-redirect-to-board]
  (when (and topic entry-data)
    (let [links (:links entry-data)
          delete-link (utils/link-for links "delete")]
      (when delete-link
        (api-delete (:href delete-link)
          {:headers (headers-for-link delete-link)}
          (fn [_]
            (dispatcher/dispatch! [:entry-delete/success should-redirect-to-board])))))))

(defn get-current-user [auth-links]
  (when-let [user-link (utils/link-for (:links auth-links) "user" "GET")]
    (auth-get (:href user-link)
      {:headers (headers-for-link user-link)}
      (fn [{:keys [status body success]}]
        (dispatcher/dispatch! [:user-data (json->cljs body)])))))

(defn patch-user-profile [old-user-data new-user-data]
  (when (and (:links old-user-data)
             (map? new-user-data))
    (let [user-update-link (utils/link-for (:links old-user-data) "partial-update" "PATCH")]
      (auth-patch (:href user-update-link)
        {:headers (headers-for-link user-update-link)
         :json-params (cljs->json (dissoc new-user-data :links :updated-at :created-at))}
         (fn [{:keys [status body success]}]
           (if (= status 422)
              (dispatcher/dispatch! [:user-profile-update/failed])
             (when success
                (utils/after 1000
                  (fn []
                    (go
                      (when-let [refresh-url (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "refresh")]
                        (let [res (<! (refresh-jwt refresh-url))]
                          (if (:success res)
                            (update-jwt-cookie! (:body res))
                            (dispatcher/dispatch! [:logout])))))))
                (dispatcher/dispatch! [:user-data (json->cljs body)]))))))))

(defn add-email-domain [domain]
  (when domain
    (let [team-data (dispatcher/team-data)
          add-domain-team-link (utils/link-for (:links team-data) "add" "POST" {:content-type "application/vnd.open-company.team.email-domain.v1"})]
      (auth-post (:href add-domain-team-link)
        {:headers (headers-for-link add-domain-team-link)
         :body domain}
        (fn [{:keys [status body success]}]
          (dispatcher/dispatch! [:email-domain-team-add/finish (= status 204)]))))))

(defn refresh-slack-user []
  (let [refresh-url (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "refresh")]
    (auth-get (:href refresh-url)
      {:headers (headers-for-link refresh-url)}
      (fn [{:keys [status body success]}]
        (if success
          (do
            (update-jwt-cookie! body)
            (dispatcher/dispatch! [:jwt body]))
          (router/redirect! oc-urls/logout))))))

(defn patch-team [team-id new-team-data redirect-url]
  (when-let* [team-data (dispatcher/team-data team-id)
              team-patch (utils/link-for (:links team-data) "partial-update")]
    (auth-patch (:href team-patch)
      {:headers (headers-for-link team-patch)
       :json-params (cljs->json new-team-data)}
      (fn [{:keys [success body status]}]
        (when (and success
                   (not (s/blank? redirect-url)))
          (router/redirect! redirect-url))))))

(defn create-org [org-name logo-url]
  (let [create-org-link (utils/link-for (dispatcher/api-entry-point) "create")
        team-id (first (j/get-key :teams))
        org-data {:name org-name :team-id team-id}
        with-logo (if-not (empty? logo-url)
                    (assoc org-data :logo-url logo-url)
                    org-data)]
    (when (and org-name create-org-link)
      (api-post (:href create-org-link)
        {:headers (headers-for-link create-org-link)
         :json-params (cljs->json with-logo)}
        (fn [{:keys [success status body]}]
          (when-let [org-data (if success (json->cljs body) {})]
            (dispatcher/dispatch! [:org org-data])
            (let [team-data (dispatcher/team-data team-id)
                  org-url (oc-urls/org (:slug org-data))]
              (if (and (s/blank? (:name team-data))
                       (utils/link-for (:links team-data) "partial-update"))
                ; if the current team has no name and
                ; the user has write permission on it
                ; use the org name
                ; for it and patch it back
                (patch-team (:team-id org-data) {:name org-name} org-url)
                ; if not refirect the user to the slug
                (router/redirect! org-url)))))))))

(defn create-board [board-name]
  (let [create-link (utils/link-for (:links (dispatcher/org-data)) "create")]
    (when (and board-name create-link)
      (api-post (:href create-link)
        {:headers (headers-for-link create-link)
         :json-params (cljs->json {:name board-name})}
        (fn [{:keys [success status body]}]
          (let [board-data (if success (json->cljs body) {})]
            (dispatcher/dispatch! [:board board-data])
            (router/redirect! (oc-urls/board (router/current-org-slug) (:slug board-data)))))))))

(defn add-author
  "Given a user-id add him as an author to the current org.
  Refresh the user list and the org-data when finished."
  [user-id]
  (when-let [add-author-link (utils/link-for (:links (dispatcher/org-data)) "add")]
    (api-post (:href add-author-link)
      {:headers (headers-for-link add-author-link)
       :body user-id}
      (fn [{:keys [status success body]}]
        (when success
          (get-org (dispatcher/org-data)))))))

(defn remove-author
  "Given a map containing :user-id and :links, remove the user as an author using the `remove` link.
  Refresh the org data when finished."
  [user-author]
  (let [remove-author-link (utils/link-for (:links user-author) "remove")]
    (when remove-author-link
      (api-delete (:href remove-author-link)
        {:headers (headers-for-link remove-author-link)}
        (fn [{:keys [status success body]}]
          (utils/after 1 #(get-org (dispatcher/org-data))))))))

(defn send-invitation
  "Give a user email and type of user send an invitation to the team.
   If the team has only one company, checked via API entry point links, send the company name of that.
   Add the logo of the company if possible"
  [invited-user invite-from user-type first-name last-name]
  (when (and invited-user invite-from user-type)
    (let [org-data (dispatcher/org-data)
          team-data (dispatcher/team-data)
          invitation-link (utils/link-for (:links team-data) "add" "POST" {:content-type "application/vnd.open-company.team.invite.v1"})
          api-entry-point-links (:api-entry-point @dispatcher/app-state)
          companies (count (filter #(= (:rel %) "company") api-entry-point-links))
          json-params {:first-name first-name
                       :last-name last-name
                       :admin (= user-type :admin)}
          with-invited-user (if (= invite-from "slack")
                              (merge json-params {:slack-id (:slack-id invited-user) :slack-org-id (:slack-org-id invited-user)})
                              (assoc json-params :email invited-user))
          with-company-name (merge with-invited-user {:org-name (:name org-data)
                                                      :logo-url (:logo-url org-data)})]
      (auth-post (:href invitation-link)
        {:json-params (cljs->json with-company-name)
         :headers (headers-for-link invitation-link)}
        (fn [{:keys [success body status]}]
          (if success
            ;; On successfull invitation
            ;; if the invited user was an author add it to the org
            (if (or (= user-type :author)
                    (= user-type :admin))
              (let [new-user (json->cljs body)]
                (add-author (:user-id new-user))
                (dispatcher/dispatch! [:invite-user/success]))
              ;; if not reload the users list immediately
              (dispatcher/dispatch! [:invite-user/success]))
            (dispatcher/dispatch! [:invite-user/failed])))))))

(defn switch-user-type
  "Given an existing user switch user type"
  [old-user-type new-user-type user user-author]
  (when (not= old-user-type new-user-type)
    (let [org-data           (dispatcher/org-data)
          add-admin-link     (utils/link-for (:links user) "add")
          remove-admin-link  (utils/link-for (:links user) "remove" "DELETE" {:ref "application/vnd.open-company.team.admin.v1"})
          add-author-link    (utils/link-for (:links org-data) "add")
          remove-author-link (utils/link-for (:links user-author) "remove")
          add-admin?         (= new-user-type :admin)
          remove-admin?      (= old-user-type :admin)
          add-author?        (or (= new-user-type :author)
                                 (= new-user-type :admin))
          remove-author?     (= new-user-type :viewer)]
      ;; Add an admin call
      (when (and add-admin? add-admin-link)
        (auth-put (:href add-admin-link)
          {:headers (headers-for-link add-admin-link)}
          (fn [{:keys [status success body]}]
            (if success
              (dispatcher/dispatch! [:invite-user/success])
              (dispatcher/dispatch! [:invite-user/failed])))))
      ;; Remove admin call
      (when (and remove-admin? remove-admin-link)
        (auth-delete (:href remove-admin-link)
          {:headers (headers-for-link remove-admin-link)}
          (fn [{:keys [status success body]}]
            (if success
              (dispatcher/dispatch! [:invite-user/success])
              (dispatcher/dispatch! [:invite-user/failed])))))
      ;; Add author call
      (when (and add-author? add-author-link)
        (add-author (:user-id user)))
      ;; Remove author call
      (when (and remove-author? remove-author-link)
        (remove-author user-author)))))

(defn archive-topic
  "Give a topic name and it's data, make the request to archive the topic."
  [topic topic-data]
  (when (and topic topic-data)
    (when-let [archive-link (utils/link-for (:links topic-data) "archive")]
      (api-delete (:href archive-link)
        {:headers (headers-for-link archive-link)}
        (fn [{:keys [status success body] :as response}]
          (dispatcher/dispatch! [:topic-archive/success]))))))


(defn add-private-board
  [board-data user-id user-type]
  (when (and board-data user-id user-type)
    (let [content-type {:content-type (if (= user-type :viewer)
                                        "application/vnd.open-company.board.viewer.v1"
                                        "application/vnd.open-company.board.author.v1")}
          add-link (utils/link-for (:links board-data) "add" "POST" content-type)]
      (api-post (:href add-link)
        {:headers (headers-for-link add-link)
         :body user-id}
        (fn [{:keys [status success body]}]
          (get-board (dispatcher/board-data)))))))

(defn private-board-user-action
  [user-data action-link & [params]]
  (when (and user-data action-link)
    (let [api-req (case (:method action-link)
                    "POST" api-post
                    "PUT" api-put
                    "PATCH" api-patch
                    "DELETE" api-delete
                    api-get)
          headers {:headers (headers-for-link action-link)}
          with-params (if params
                        (assoc headers :json-params (cljs->json params))
                        headers)]
      (api-req (:href action-link)
        with-params
        (fn [{:keys [status success body]}]
          (get-board (dispatcher/board-data)))))))

(defn password-reset
  [email]
  (when email
    (when-let [reset-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "reset")]
      (auth-post (:href reset-link)
        {:headers (headers-for-link reset-link)
         :body email}
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch! [:password-reset/finish status]))))))

(defn delete-board [board-slug]
  (when board-slug
    (let [board-data (dispatcher/board-data @dispatcher/app-state (router/current-org-slug) board-slug)
          delete-board-link (utils/link-for (:links board-data) "delete")]
      (when delete-board-link
        (api-delete (:href delete-board-link)
          {:headers (headers-for-link delete-board-link)}
          (fn [{:keys [status success body]}]
            (if success
              (router/nav! (oc-urls/org (router/current-org-slug)))
              (.reload (.-location js/window)))))))))