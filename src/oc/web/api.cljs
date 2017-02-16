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
 (let [acah-headers (if (and (contains? link :access-control-allow-headers)
                             (nil? (:access-control-allow-headers link)))
                      {}
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
          (let [report {:response response
                        :path path
                        :method (method-name method)
                        :jwt (j/jwt)
                        :params params}]
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
  (api-get "/" nil (fn [response]
                     (let [body (if (:success response) (:body response) {})]
                       (dispatcher/dispatch! [:entry-point (json->cljs body)])))))

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

(defn create-company [data]
  (when data
    (let [links (:api-entry-point @dispatcher/app-state)
          create-company-link (utils/link-for links "company-create" "POST")
          data-with-org-id (assoc data :org-id (first (j/get-key :teams)))
          post-data (cljs->json data-with-org-id)]
      (api-post (:href create-company-link)
        {:json-params post-data}
        #(dispatch-body :company-created %)))))

(defn patch-company [slug data]
  (when data
    (let [company-data (dissoc data :links :read-only :entries :su-list :su-list-loaded :entries :topic)
          json-data (cljs->json company-data)
          links (:links (dispatcher/board-data))
          company-link (utils/link-for links "partial-update" "PATCH")]
      (api-patch (:href company-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type company-link)
          }}
        (fn [{:keys [success body status]}]
          (dispatcher/dispatch! [:company {:success success
                                           :status status
                                           :body (when success (json->cljs body))}]))))))

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
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})
                response-content-type (get (:headers response) "content-type")]
            (if (= response-content-type "application/vnd.open-company.entry.v1+json")
              (dispatcher/dispatch! [:topic-entry {:body body :topic topic :created-at (:created-at body)}])
              (dispatcher/dispatch! [:topic {:body body :topic topic}]))))))))

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
    (let [slug (keyword (router/current-board-slug))
          topic-kw (keyword topic)
          partial-update-link (utils/link-for (:links topic-data) "partial-update" "PATCH")
          cleaned-topic-data (apply dissoc topic-data topic-private-keys)
          json-data (cljs->json cleaned-topic-data)]
      (api-patch (:href partial-update-link)
        { :json-params json-data
          :headers (headers-for-link partial-update-link)}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})]
            (load-entries topic (utils/link-for (:links body) "entries")))))))))

(defn update-finances-data[finances-data]
  (when finances-data
    (let [links (:links finances-data)
          slug (router/current-board-slug)
          data {:data (map #(apply dissoc % topic-private-keys) (:data finances-data))}
          json-data (cljs->json data)
          finances-link (utils/link-for links "partial-update" "PATCH")]
      (api-patch (:href finances-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type finances-link)}}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})
                dispatch-body {:body (merge {:topic :finances} body)
                               :topic :finances
                               :slug (keyword slug)}]
            (dispatcher/dispatch! [:topic dispatch-body])))))))

(defn patch-topics [topics & [new-topic topic-name]]
  (when topics
    (let [slug (keyword (router/current-board-slug))
          company-data (dispatcher/board-data)
          company-patch-link (utils/link-for (:links company-data) "partial-update" "PATCH")
          payload (if (and new-topic topic-name)
                    {:topics topics
                     (keyword topic-name) new-topic}
                    {:topics topics})
          json-data (cljs->json payload)]
      (api-patch (:href company-patch-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type company-patch-link)}}
        (fn [{:keys [success body status]}]
          (dispatcher/dispatch! [:company {:success success
                                           :status status
                                           :body (when success (json->cljs body))}]))))))

(defn patch-stakeholder-update [stakeholder-update]
  (when stakeholder-update
    (let [slug (keyword (router/current-board-slug))
          company-data (dispatcher/board-data)
          company-patch-link (utils/link-for (:links company-data) "partial-update" "PATCH")
          json-data (cljs->json {:stakeholder-update stakeholder-update})]
      (api-patch (:href company-patch-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type company-patch-link)}}
        (fn [{:keys [success body status]}]
          (dispatcher/dispatch! [:company {:success success
                                           :status status
                                           :body (when success (json->cljs body))}]))))))

(defn remove-topic [topic-name]
  (when (and topic-name)
    (let [board-data (dispatcher/board-data)
          topics (:topics board-data)
          new-topics (apply merge (map (fn [[k v]]
                                        {k (utils/vec-dissoc v topic-name)})
                                    topics))]
      (patch-topics new-topics))))

(def new-topics-requested (atom false))

(defn get-new-topics [& [force-load]]
  "Load new topics, avoid to start multiple request or reload it if it's already loading or loaded.
   It's possible to force the load passing an optional boolean parameter."
  (when (or force-load (not @new-topics-requested))
    (reset! new-topics-requested true)
    (let [slug (keyword (router/current-board-slug))
          board-data (dispatcher/board-data)
          links (:links board-data)
          add-topic-link (utils/link-for links "new" "GET")]
      (when add-topic-link
        (api-get (:href add-topic-link)
          { :headers (headers-for-link add-topic-link)}
          (fn [{:keys [success body]}]
            (when (not success)
              (reset! new-topics-requested false))
            (let [fixed-body (if body (json->cljs body) {})]
              (dispatcher/dispatch! [:new-topic {:response fixed-body :slug slug}]))))))))

(defn share-stakeholder-update [{:keys [email slack]}]
  (let [slug         (keyword (router/current-board-slug))
        company-data (dispatcher/board-data)
        links        (:links company-data)
        post-data    (cond email (assoc email :email true)
                           slack (assoc slack :slack true))
        share-link   (utils/link-for links "share" "POST")]
    (api-post (:href share-link)
              {:json-params post-data
               :headers {;; required by Chrome
                         "Access-Control-Allow-Headers" "Content-Type"
                         ;; custom content type
                         "content-type" (:type share-link)}}
      (fn [{:keys [success body]}]
        (when success
          (let [fixed-body (json->cljs body)]
            (dispatcher/dispatch! [:su-edit {:slug slug :su-slug (:slug fixed-body) :su-date (:created-at fixed-body)}])))))))

(defn get-su-list []
  (let [slug (keyword (router/current-board-slug))
        company-data (dispatcher/board-data)
        links (:links company-data)
        su-link (utils/link-for links "stakeholder-updates" "GET")]
    (when su-link
      (api-get (:href su-link)
        { :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type su-link)}}
        (fn [{:keys [success body]}]
          (when (not success)
            (reset! new-topics-requested false))
          (let [fixed-body (if success (json->cljs body) {})]
            (dispatcher/dispatch! [:su-list {:response fixed-body :slug slug}])))))))

(defn get-stakeholder-update
  ([slug update-slug load-company-data]
    (when (and slug update-slug)
      (let [update-link (str "/companies/" slug "/updates/" update-slug)]
        (api-get update-link
          { :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (content-type "stakeholder-update")}}
          (fn [{:keys [success body]}]
            (let [fixed-body (if success (json->cljs body) {})
                  response {:slug (keyword slug)
                            :update-slug (keyword update-slug)
                            :response fixed-body
                            :load-company-data load-company-data}]
              (dispatcher/dispatch! [:stakeholder-update response]))))))))

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

(defn get-teams [& [dont-follow-team-link]]
  (let [enumerate-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "collection" "GET")]
    (auth-get (:href enumerate-link)
      {:headers (headers-for-link enumerate-link)}
      (fn [{:keys [success body status]}]
        (let [fixed-body (if success (json->cljs body) {})]
          (if success
            (dispatcher/dispatch! [:teams-loaded (-> fixed-body :collection :items) dont-follow-team-link])))))))

(defn get-team [team-link]
  (when team-link
    (auth-get (:href team-link)
      {:headers (headers-for-link team-link)}
      (fn [{:keys [success body status]}]
        (let [fixed-body (if success (json->cljs body) {})]
          (if success
            (dispatcher/dispatch! [:team-loaded fixed-body])))))))

(defn enumerate-channels []
  (let [enumerate-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "channels" "GET")]
    (auth-get (:href enumerate-link)
      {:headers (headers-for-link enumerate-link)}
      (fn [{:keys [success body status]}]
        (let [fixed-body (if success (json->cljs body) {})]
          (if success
            (dispatcher/dispatch! [:enumerate-channels/success (-> fixed-body :collection :channels)])))))))

(defn send-invitation
  "Give a user email and type of user send an invitation to the team.
   If the team has only one company, checked via API entry point links, send the company name of that.
   Add the logo of the company if possible"
  [email user-type first-name last-name]
  (when (and email user-type)
    (let [team-data (get (:enumerate-users @dispatcher/app-state) (router/current-team-id))
          invitation-link (utils/link-for (:links team-data) "add" "POST" {:content-type "application/vnd.open-company.team.invite.v1"})
          api-entry-point-links (:api-entry-point @dispatcher/app-state)
          companies (count (filter #(= (:rel %) "company") api-entry-point-links))
          json-params {:email email
                       :first-name first-name
                       :last-name last-name
                       :admin (= user-type :admin)}
          with-company-name (if (= companies 1)
                              (let [company-link (utils/link-for api-entry-point-links "company")]
                                (merge json-params {:org-name (:name company-link)
                                                    :logo-url (:logo-url company-link)}))
                              json-params)]
      (auth-post (:href invitation-link)
        {:json-params (cljs->json with-company-name)
         :headers (headers-for-link invitation-link)}
        (fn [{:keys [success body status]}]
          (if success
            (dispatcher/dispatch! [:invite-by-email/success (:users (:collection body))])
            (dispatcher/dispatch! [:invite-by-email/failed])))))))

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
          (when success
            (update-jwt-cookie! body)
            (dispatcher/dispatch! [:jwt (j/get-contents)]))
          (utils/after 100 #(dispatcher/dispatch! [:invitation-confirmed status])))))))

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
          (utils/after 100 #(dispatcher/dispatch! [:collect-name-pswd-finish status])))))))

(defn delete-entry [topic entry-data should-redirect-to-board]
  (when (and topic entry-data)
    (let [links (:links entry-data)
          delete-link (utils/link-for links "delete")]
      (when delete-link
        (api-delete (:href delete-link)
          {:headers (headers-for-link delete-link)}
          (fn [_]
            (when should-redirect-to-board
              (router/nav! (oc-urls/board (router/current-org-slug) (router/current-board-slug))))))))))

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
           (when success
              (utils/after 1000
                (fn []
                  (go
                    (when-let [refresh-url (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "refresh")]
                      (let [res (<! (refresh-jwt refresh-url))]
                        (if (:success res)
                          (update-jwt-cookie! (:body res))
                          (dispatcher/dispatch! [:logout])))))))
              (dispatcher/dispatch! [:user-data (json->cljs body)])))))))

(defn add-email-domain [domain]
  (when domain
    (let [teams-data (:enumerate-users @dispatcher/app-state)
          team-data (get teams-data (router/current-team-id))
          add-domain-team-link (utils/link-for (:links team-data) "add" "POST" {:content-type "application/vnd.open-company.team.email-domain.v1"})]
      (auth-post (:href add-domain-team-link)
        {:headers (headers-for-link add-domain-team-link)
         :body domain}
        (fn [{:keys [status body success]}]
          (dispatcher/dispatch! [:add-email-domain-team/finish (= status 204)]))))))

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

(defn patch-team [new-team-data redirect-url]
  (when-let* [teams-data (dispatcher/teams-data)
              team-data (first (filter #(= (:team-id %) (router/current-team-id)) teams-data))
              team-patch (utils/link-for (:links team-data) "partial-update")]
    (api-patch (:href team-patch)
      {:headers (headers-for-link team-patch)
       :json-params (cljs->json new-team-data)}
      (fn [{:keys [success body status]}]
        (when (and success
                   (not (s/blank? redirect-url)))
          (router/redirect! redirect-url))))))

(defn create-org [org-name]
  (let [create-org-link (utils/link-for (dispatcher/api-entry-point) "create")]
    (when (and org-name create-org-link)
      (api-post (:href create-org-link)
        {:headers (headers-for-link create-org-link)
         :json-params (cljs->json {:name org-name :team-id (router/current-team-id)})}
        (fn [{:keys [success status body]}]
          (when-let [org-data (if success (json->cljs body) {})]
            (dispatcher/dispatch! [:org org-data])
            (let [teams-data (dispatcher/teams-data)
                  team-data (first (filter #(= (:team-id %) (router/current-team-id))))
                  board-url (oc-urls/org (:slug org-data))]
              (if (and (s/blank? (:name team-data)))
                ; if the current team has no name and
                ; the user has write permission on it
                ; use the org name
                ; for it and patch it back
                (patch-team {:name org-name} board-url)
                ; if not refirect the user to the slug
                (router/redirect! board-url)))))))))

(defn create-board [board-name]
  (let [create-link (utils/link-for (:links (dispatcher/org-data)) "create")]
    (when (and board-name create-link)
      (api-post (:href create-link)
        {:headers (headers-for-link create-link)
         :json-params (cljs->json {:name board-name :access "team"})}
        (fn [{:keys [success status body]}]
          (let [board-data (if success (json->cljs body) {})]
            (dispatcher/dispatch! [:board board-data])
            (router/redirect! (oc-urls/board (router/current-org-slug) (:slug board-data)))))))))