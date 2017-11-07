(ns oc.web.api
  (:require-macros [cljs.core.async.macros :refer (go)]
                   [if-let.core :refer (when-let*)])
  (:require [cljs.core.async :as async :refer (<!)]
            [cljs-http.client :as http]
            [defun.core :refer (defun-)]
            [taoensso.timbre :as timbre]
            [clojure.string :as s]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as j]
            [oc.web.router :as router]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs cljs->json)]
            [oc.web.lib.raven :as sentry]
            [goog.Uri :as guri]))

(def ^:private storage-endpoint ls/storage-server-domain)

(def ^:private auth-endpoint ls/auth-server-domain)

(def ^:private pay-endpoint ls/pay-server-domain)

(def ^:private interaction-endpoint ls/interaction-server-domain)

(defun- relative-href
  "Given a link map or a link string return the relative href."

  ([link-map :guard map?]
    (relative-href (:href link-map)))

  ([href :guard string?]
    (let [parsed-uri (guri/parse href)]
      (str (.getPath parsed-uri)
           (when (.hasQuery parsed-uri)
             (str "?" (.getEncodedQuery parsed-uri)))
           (when (.hasFragment parsed-uri)
             (str "#" (.getFragment parsed-uri)))))))

(defn- content-type [type]
  (str "application/vnd.open-company." type ".v1+json;charset=UTF-8"))

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
       with-content-type (if (:content-type link)
                          (assoc acah-headers "content-type" (:content-type link))
                          acah-headers)
       with-accept (if (:accept link) (assoc with-content-type "accept" (:accept link)) with-content-type)]
  with-accept))

(defn method-for-link [link]
  (case (:method link)
    "POST" http/post
    "PUT" http/put
    "PATCH" http/patch
    "DELETE" http/delete
    http/get))

(defn refresh-jwt [refresh-link]
  (let [refresh-url (if (map? refresh-link)
                      (str ls/auth-server-domain (relative-href refresh-link))
                      refresh-link)
        headers (if (map? refresh-link) {:headers (headers-for-link refresh-link)} {})
        method (if (map? refresh-link)
                  (method-for-link refresh-link)
                  http/get)]
  (method refresh-url (complete-params headers))))

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
        (when (or (zero? status)
                  (and (>= status 500) (<= status 599))
                  (= status 400)
                  (= status 422))
          ; If it was a 5xx or a 0 show a banner for network issues
          (when (or (zero? status)
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

(def ^:private storage-http (partial req storage-endpoint))

(def ^:private auth-http (partial req auth-endpoint))

(def ^:private pay-http (partial req pay-endpoint))

(def ^:private interaction-http (partial req interaction-endpoint))

(defn dispatch-body [action response]
  (let [body (if (:success response) (json->cljs (:body response)) {})]
    (dispatcher/dispatch! [action body])))

(defn api-500-test [with-response]
  (storage-http http/get (if with-response "/---error-test---" "/---500-test---")
    {:headers {
      ; required by Chrome
      "Access-Control-Allow-Headers" "Content-Type"
      ; custom content type
      "content-type" "text/plain"}}
    (fn [_])))

(defn auth-500-test [with-response]
  (auth-http http/get (if with-response "/---error-test---" "/---500-test---")
    {:headers (headers-for-link {:content-type "text/plain"})}
    (fn [_])))

(defn get-entry-point []
  (let [entry-point-href (str "/" (when (:org @router/path) (str "?requested=" (:org @router/path))))]
    (storage-http http/get entry-point-href
     nil
     (fn [{:keys [success body]}]
       (let [fixed-body (when success (json->cljs body))]
         (dispatcher/dispatch! [:entry-point {:success success :collection (:collection fixed-body)}]))))))

(defn get-subscription [company-uuid]
  (pay-http http/get (str "/subscriptions/" company-uuid)
           nil
           (fn [response]
             (let [body (if (:success response) (:body response) {})]
               (dispatcher/dispatch! [:subscription body])))))

(defn get-org [org-data]
  (when-let [org-link (utils/link-for (:links org-data) ["item" "self"] "GET")]
    (storage-http (method-for-link org-link) (relative-href org-link)
      {:headers (headers-for-link org-link)}
      (fn [{:keys [status body success]}]
        (dispatcher/dispatch! [:org (json->cljs body)])))))

(defn get-board [board-link]
  (when board-link
    (storage-http (method-for-link board-link) (relative-href board-link)
      {:headers (headers-for-link board-link)}
      (fn [{:keys [status body success]}]
        (dispatcher/dispatch! [:board (json->cljs body)])))))

(defn get-whats-new [whats-new-link]
  (when whats-new-link
    (storage-http (method-for-link whats-new-link) (relative-href whats-new-link)
      {:headers (headers-for-link whats-new-link)}
      (fn [{:keys [status body success]}]
        (dispatcher/dispatch! [:whats-new/finish (if success (json->cljs body) {:entries []})])))))

(defn patch-board [data]
  (when data
    (let [board-data (select-keys data [:name :slug :access :slack-mirror])
          json-data (cljs->json board-data)
          board-patch-link (utils/link-for (:links data) "partial-update")]
      (storage-http (method-for-link board-patch-link) (relative-href board-patch-link)
        {:json-params json-data
         :headers (headers-for-link board-patch-link)}
        (fn [{:keys [success body status]}]
          (if (= status 409)
            ; Board name exists
            (dispatcher/dispatch!
             [:input
              [:board-editing :board-name-error]
              "Board name already exists or isn't allowed"])
            (dispatcher/dispatch! [:board-edit-save/finish (json->cljs body)])))))))

(def org-keys [:name :logo-url :logo-width :logo-height])

(defn patch-org [data]
  (when data
    (let [org-data (select-keys data org-keys)
          json-data (cljs->json org-data)
          links (:links (dispatcher/org-data))
          org-patch-link (utils/link-for links "partial-update")]
      (storage-http (method-for-link org-patch-link) (relative-href org-patch-link)
        {:json-params json-data
         :headers (headers-for-link org-patch-link)}
        (fn [{:keys [success body status]}]
          (dispatcher/dispatch! [:org (json->cljs body) true]))))))

(defn get-auth-settings []
  (auth-http http/get "/"
    {:headers (headers-for-link {:access-control-allow-headers nil :content-type "application/json"})}
    (fn [response]
      (let [body (if (:success response) (:body response) false)]
        (dispatcher/dispatch! [:auth-settings body])))))

(defn auth-with-email [email pswd]
  (when (and email pswd)
    (let [email-links (:links (:auth-settings @dispatcher/app-state))
          auth-url (utils/link-for email-links "authenticate" "GET" {:auth-source "email"})]
      (auth-http (method-for-link auth-url) (relative-href auth-url)
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
      (auth-http (method-for-link auth-url) (relative-href auth-url)
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
      (auth-http (method-for-link auth-url) (relative-href auth-url)
        {:json-params {:first-name first-name
                       :last-name last-name
                       :email email
                       :password pswd}
         :headers (headers-for-link auth-url)}
        (fn [{:keys [success body status]}]
         (if success
            (dispatcher/dispatch! [:signup-with-email/success status body])
            (dispatcher/dispatch! [:signup-with-email/failed status])))))))

(defn get-teams [auth-settings]
  (let [enumerate-link (utils/link-for (:links auth-settings) "collection" "GET")]
    (auth-http (method-for-link enumerate-link) (relative-href enumerate-link)
      {:headers (headers-for-link enumerate-link)}
      (fn [{:keys [success body status]}]
        (let [fixed-body (when success (json->cljs body))]
          (if success
            (dispatcher/dispatch! [:teams-loaded (-> fixed-body :collection :items)])
            ;; Reset the team-data-requested to restart the teams load
            (when (and (>= status 500)
                       (<= status 599))
              (dispatcher/dispatch! [:input [:team-data-requested] false]))))))))

(defn get-team [team-link]
  (when team-link
    (auth-http (method-for-link team-link) (relative-href team-link)
      {:headers (headers-for-link team-link)}
      (fn [{:keys [success body status]}]
        (let [fixed-body (when success (json->cljs body))]
          (if success
            (if (= (:rel team-link) "roster")
              (dispatcher/dispatch! [:team-roster-loaded fixed-body])
              (dispatcher/dispatch! [:team-loaded fixed-body]))))))))

(defn enumerate-channels [team-id]
  (when team-id
    (let [team-data (dispatcher/team-data team-id)
          enumerate-link (utils/link-for (:links team-data) "channels" "GET")]
      (when enumerate-link
        (auth-http (method-for-link enumerate-link) (relative-href enumerate-link)
          {:headers (headers-for-link enumerate-link)}
          (fn [{:keys [success body status]}]
            (let [fixed-body (when success (json->cljs body))]
              (if success
                (dispatcher/dispatch! [:channels-enumerate/success team-id (-> fixed-body :collection :items)])))))))))

(defn user-action [action-link payload]
  (when action-link
    (let [headers {:headers (headers-for-link action-link)}
          with-payload (if payload
                          (assoc headers :json-params payload)
                          headers)]
      (auth-http (method-for-link action-link) (relative-href action-link)
        with-payload
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch! [:user-action/complete]))))))

(defn confirm-invitation [token]
  (let [auth-link (utils/link-for
                   (:links (:auth-settings @dispatcher/app-state))
                   "authenticate"
                   "GET"
                   {:auth-source "email"})]
    (when (and token auth-link)
      (auth-http (method-for-link auth-link) (relative-href auth-link)
        {:headers (merge (headers-for-link auth-link)
                         {; required by Chrome
                          "Access-Control-Allow-Headers" "Content-Type, Authorization"
                          "Authorization" (str "Bearer " token)})}
        (fn [{:keys [status body success]}]
          (utils/after 100 #(dispatcher/dispatch! [:invitation-confirmed status]))
          (when success
            (update-jwt-cookie! body)))))))

(defn collect-password [pswd]
  (let [update-link (utils/link-for (:links (:current-user-data @dispatcher/app-state)) "partial-update" "PATCH")]
    (when (and pswd update-link)
      (auth-http (method-for-link update-link) (relative-href update-link)
        {:json-params {
          :password pswd}
         :headers (headers-for-link update-link)}
        (fn [{:keys [status body success]}]
          (when success
            (dispatcher/dispatch! [:user-data (json->cljs body)]))
          (utils/after 100 #(dispatcher/dispatch! [:pswd-collect/finish status])))))))

(defn get-current-user [auth-links]
  (when-let [user-link (utils/link-for (:links auth-links) "user" "GET")]
    (auth-http (method-for-link user-link) (relative-href user-link)
      {:headers (headers-for-link user-link)}
      (fn [{:keys [status body success]}]
        (dispatcher/dispatch! [:user-data (json->cljs body)])))))

(def user-profile-keys [:first-name :last-name :email :password :avatar-url])

(defn patch-user-profile [old-user-data new-user-data]
  (when (and (:links old-user-data)
             (map? new-user-data))
    (let [user-update-link (utils/link-for (:links old-user-data) "partial-update" "PATCH")]
      (auth-http (method-for-link user-update-link) (relative-href user-update-link)
        {:headers (headers-for-link user-update-link)
         :json-params (cljs->json (select-keys new-user-data user-profile-keys))}
         (fn [{:keys [status body success]}]
           (if (= status 422)
              (dispatcher/dispatch! [:user-profile-update/failed])
             (when success
                (utils/after 1000
                  (fn []
                    (go
                      (when-let [refresh-url (utils/link-for
                                              (:links (:auth-settings @dispatcher/app-state))
                                              "refresh")]
                        (let [res (<! (refresh-jwt refresh-url))]
                          (if (:success res)
                            (update-jwt-cookie! (:body res))
                            (dispatcher/dispatch! [:logout])))))))
                (dispatcher/dispatch! [:user-data (json->cljs body)]))))))))

(defn collect-name-password [firstname lastname pswd]
  (let [update-link (utils/link-for (:links (:current-user-data @dispatcher/app-state)) "partial-update" "PATCH")]
    (when (and (or firstname lastname) pswd update-link)
      (auth-http (method-for-link update-link) (relative-href update-link)
        {:json-params {
          :first-name firstname
          :last-name lastname
          :password pswd
         }
         :headers (headers-for-link update-link)}
        (fn [{:keys [status body success]}]
          (if-not success
            (dispatcher/dispatch! [:name-pswd-collect/finish status nil])
            (when success
              (dispatcher/dispatch! [:name-pswd-collect/finish status (json->cljs body)])
              (utils/after 1000
                (fn []
                  (go
                    (when-let [refresh-url (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "refresh")]
                      (let [res (<! (refresh-jwt refresh-url))]
                        (if (:success res)
                          (update-jwt-cookie! (:body res))
                          (dispatcher/dispatch! [:logout]))))))))))))))

(defn add-email-domain [domain]
  (when domain
    (let [team-data (dispatcher/team-data)
          add-domain-team-link (utils/link-for
                                (:links team-data)
                                "add"
                                "POST"
                                {:content-type "application/vnd.open-company.team.email-domain.v1"})]
      (auth-http (method-for-link add-domain-team-link) (relative-href add-domain-team-link)
        {:headers (headers-for-link add-domain-team-link)
         :body domain}
        (fn [{:keys [status body success]}]
          (dispatcher/dispatch! [:email-domain-team-add/finish (= status 204)]))))))

(defn refresh-slack-user []
  (let [refresh-url (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "refresh")]
    (auth-http (method-for-link refresh-url) (relative-href refresh-url)
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
    (auth-http (method-for-link team-patch) (relative-href team-patch)
      {:headers (headers-for-link team-patch)
       :json-params (cljs->json new-team-data)}
      (fn [{:keys [success body status]}]
        (when (and success
                   (not (s/blank? redirect-url)))
          (router/redirect! redirect-url))))))

(defn create-org [org-name logo-url logo-width logo-height]
  (let [create-org-link (utils/link-for (dispatcher/api-entry-point) "create")
        team-id (first (j/get-key :teams))
        org-data {:name org-name :team-id team-id}
        with-logo (if-not (empty? logo-url)
                    (merge org-data {:logo-url logo-url
                                     :logo-width logo-width
                                     :logo-height logo-height})
                    org-data)]
    (when (and org-name create-org-link)
      (storage-http (method-for-link create-org-link) (relative-href create-org-link)
        {:headers (headers-for-link create-org-link)
         :json-params (cljs->json with-logo)}
        (fn [{:keys [success status body]}]
          (when-let [org-data (when success (json->cljs body))]
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

(defn create-board [board-name board-access]
  (let [create-link (utils/link-for (:links (dispatcher/org-data)) "create")]
    (when (and board-name create-link)
      (storage-http (method-for-link create-link) (relative-href create-link)
        {:headers (headers-for-link create-link)
         :json-params (cljs->json {:name board-name :access board-access})}
        (fn [{:keys [success status body]}]
          (let [board-data (when success (json->cljs body))]
            (if (= status 409)
              ; Board name exists
              (dispatcher/dispatch!
               [:input
                [:board-editing :board-name-error]
                "Board name already exists or isn't allowed"])
              (dispatcher/dispatch! [:board-edit-save/finish board-data]))))))))

(defn add-author
  "Given a user-id add him as an author to the current org.
  Refresh the user list and the org-data when finished."
  [user-id]
  (when-let [add-author-link (utils/link-for (:links (dispatcher/org-data)) "add")]
    (storage-http (method-for-link add-author-link) (relative-href add-author-link)
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
      (storage-http (method-for-link remove-author-link) (relative-href remove-author-link)
        {:headers (headers-for-link remove-author-link)}
        (fn [{:keys [status success body]}]
          (utils/after 1 #(get-org (dispatcher/org-data))))))))

(defn send-invitation
  "Give a user email and type of user send an invitation to the team.
   If the team has only one company, checked via API entry point links, send the company name of that.
   Add the logo of the company if possible"
  [complete-user-data invited-user invite-from user-type first-name last-name]
  (when (and invited-user invite-from user-type)
    (let [org-data (dispatcher/org-data)
          team-data (dispatcher/team-data)
          invitation-link (utils/link-for
                           (:links team-data)
                           "add"
                           "POST"
                           {:content-type "application/vnd.open-company.team.invite.v1"})
          api-entry-point-links (:api-entry-point @dispatcher/app-state)
          companies (count (filter #(= (:rel %) "company") api-entry-point-links))
          json-params {:first-name first-name
                       :last-name last-name
                       :admin (= user-type :admin)}
          with-invited-user (if (= invite-from "slack")
                              (merge
                               json-params
                               {:slack-id (:slack-id invited-user)
                                :slack-org-id (:slack-org-id invited-user)})
                              (assoc json-params :email invited-user))
          with-company-name (merge with-invited-user {:org-name (:name org-data)
                                                      :logo-url (:logo-url org-data)})]
      (auth-http (method-for-link invitation-link) (relative-href invitation-link)
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
                (dispatcher/dispatch! [:invite-user/success complete-user-data]))
              ;; if not reload the users list immediately
              (dispatcher/dispatch! [:invite-user/success complete-user-data]))
            (dispatcher/dispatch! [:invite-user/failed complete-user-data])))))))

(defn switch-user-type
  "Given an existing user switch user type"
  [complete-user-data old-user-type new-user-type user user-author]
  (when (not= old-user-type new-user-type)
    (let [org-data           (dispatcher/org-data)
          add-admin-link     (utils/link-for (:links user) "add")
          remove-admin-link  (utils/link-for
                              (:links user)
                              "remove"
                              "DELETE"
                              {:ref "application/vnd.open-company.team.admin.v1"})
          add-author-link    (utils/link-for (:links org-data) "add")
          remove-author-link (utils/link-for (:links user-author) "remove")
          add-admin?         (= new-user-type :admin)
          remove-admin?      (= old-user-type :admin)
          add-author?        (or (= new-user-type :author)
                                 (= new-user-type :admin))
          remove-author?     (= new-user-type :viewer)]
      ;; Add an admin call
      (when (and add-admin? add-admin-link)
        (auth-http (method-for-link add-admin-link) (relative-href add-admin-link)
          {:headers (headers-for-link add-admin-link)}
          (fn [{:keys [status success body]}]
            (if success
              (dispatcher/dispatch! [:invite-user/success complete-user-data])
              (dispatcher/dispatch! [:invite-user/failed complete-user-data])))))
      ;; Remove admin call
      (when (and remove-admin? remove-admin-link)
        (auth-http (method-for-link remove-admin-link) (relative-href remove-admin-link)
          {:headers (headers-for-link remove-admin-link)}
          (fn [{:keys [status success body]}]
            (if success
              (dispatcher/dispatch! [:invite-user/success complete-user-data])
              (dispatcher/dispatch! [:invite-user/failed complete-user-data])))))
      ;; Add author call
      (when (and add-author? add-author-link)
        (add-author (:user-id user)))
      ;; Remove author call
      (when (and remove-author? remove-author-link)
        (remove-author user-author)))))

(defn add-private-board
  [board-data user-id user-type]
  (when (and board-data user-id user-type)
    (let [content-type {:content-type (if (= user-type :viewer)
                                        "application/vnd.open-company.board.viewer.v1"
                                        "application/vnd.open-company.board.author.v1")}
          add-link (utils/link-for (:links board-data) "add" "POST" content-type)]
      (storage-http (method-for-link add-link) (relative-href add-link)
        {:headers (headers-for-link add-link)
         :body user-id}
        (fn [{:keys [status success body]}]
          (get-board (dispatcher/board-data)))))))

(defn private-board-user-action
  [user-data action-link & [params]]
  (when (and user-data action-link)
    (let [headers {:headers (headers-for-link action-link)}
          with-params (if params
                        (assoc headers :json-params (cljs->json params))
                        headers)]
      (storage-http (method-for-link action-link) (relative-href action-link)
        with-params
        (fn [{:keys [status success body]}]
          (get-board (dispatcher/board-data)))))))

(defn password-reset
  [email]
  (when email
    (when-let [reset-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "reset")]
      (auth-http (method-for-link reset-link) (relative-href reset-link)
        {:headers (headers-for-link reset-link)
         :body email}
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch! [:password-reset/finish status]))))))

(defn delete-board [board-slug]
  (when board-slug
    (let [board-data (dispatcher/board-data @dispatcher/app-state (router/current-org-slug) board-slug)
          delete-board-link (utils/link-for (:links board-data) "delete")]
      (when delete-board-link
        (storage-http (method-for-link delete-board-link) (relative-href delete-board-link)
          {:headers (headers-for-link delete-board-link)}
          (fn [{:keys [status success body]}]
            (if success
              (router/nav! (oc-urls/org (router/current-org-slug)))
              (.reload (.-location js/window)))))))))

(defn get-comments [activity-data]
  (when activity-data
    (let [comments-link (utils/link-for (:links activity-data) "comments")]
      (when comments-link
        (interaction-http (method-for-link comments-link) (relative-href comments-link)
          {:headers (headers-for-link comments-link)}
          (fn [{:keys [status success body]}]
            (dispatcher/dispatch! [:comments-get/finish {:success success
                                                         :error (when-not success body)
                                                         :body (when (seq body) (json->cljs body))
                                                         :activity-uuid (:uuid activity-data)}])))))))

(defn add-comment [activity-data comment-body]
  (when (and activity-data comment-body)
    (let [add-comment-link (utils/link-for (:links activity-data) "create" "POST")
          json-data (cljs->json {:body comment-body})]
      (interaction-http (method-for-link add-comment-link) (relative-href add-comment-link)
        {:headers (headers-for-link add-comment-link)
         :json-params json-data}
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch! [:comment-add/finish {:success success
                                                      :error (when-not success body)
                                                      :body (when (seq body) (json->cljs body))
                                                      :activity-uuid (:uuid activity-data)}]))))))

(defn toggle-reaction
  [activity-uuid reaction-data]
  (when (and activity-uuid reaction-data)
    (let [reaction-link (utils/link-for (:links reaction-data) "react" ["PUT" "DELETE"])]
      (interaction-http (method-for-link reaction-link) (relative-href reaction-link)
        {:headers (headers-for-link reaction-link)}
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch!
           [:reaction-toggle/finish
            activity-uuid
            (:reaction reaction-data)
            (when success (json->cljs body))]))))))

(defn get-entry
  [entry-data]
  (when entry-data
    (let [entry-self-link (utils/link-for (:links entry-data) "self")]
      (storage-http (method-for-link entry-self-link) (relative-href entry-self-link)
        {:headers (headers-for-link entry-self-link)}
        (fn [{:keys [status success body]}]
          (if success
            (dispatcher/dispatch! [:entry (:uuid entry-data) (clj->js body)])))))))

(def entry-keys [:headline :body :topic-name :attachments :title :board-slug])

(defn create-entry
  [entry-data]
  (when entry-data
    (let [board-data (dispatcher/board-data)
          create-entry-link (utils/link-for (:links board-data) "create")
          cleaned-entry-data (select-keys entry-data entry-keys)]
      (storage-http (method-for-link create-entry-link) (relative-href create-entry-link)
        {:headers (headers-for-link create-entry-link)
         :json-params (cljs->json cleaned-entry-data)}
        (fn [{:keys [status success body headers] :as resp}]
          (if success
            (dispatcher/dispatch!
             [:entry-save/finish
              {:activity-data (if success (json->cljs body) {})
               :board-slug (:slug board-data)
               :edit-key :entry-editing}])
            (dispatcher/dispatch! [:entry-save/failed  :entry-editing])))))))

(defn update-entry
  [entry-data board-slug]
  (when entry-data
    (let [update-entry-link (utils/link-for (:links entry-data) "partial-update")
          cleaned-entry-data (select-keys entry-data entry-keys)]
      (storage-http (method-for-link update-entry-link) (relative-href update-entry-link)
        {:headers (headers-for-link update-entry-link)
         :json-params (cljs->json cleaned-entry-data)}
        (fn [{:keys [status success body]}]
          (if success
            (dispatcher/dispatch!
             [:entry-save/finish
              {:activity-data (if success (json->cljs body) {})
               :board-slug board-slug
                :edit-key :modal-editing-data}])
            (dispatcher/dispatch! [:entry-save/failed  :modal-editing-data])))))))

(defn delete-activity [activity-data]
  (when activity-data
    (when-let [activity-delete-link (utils/link-for (:links activity-data) "delete")]
      (storage-http (method-for-link activity-delete-link) (relative-href activity-delete-link)
        {:headers (headers-for-link activity-delete-link)}
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch! [:activity-delete/finish]))))))

(defn get-all-posts [org-data & [activity-link year month]]
  (when org-data
    (let [all-posts-link (or activity-link (utils/link-for (:links org-data) "activity"))]
      (storage-http (method-for-link all-posts-link) (relative-href all-posts-link)
        {:headers (headers-for-link all-posts-link)}
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch!
           [:all-posts-get/finish
            {:org (:slug org-data)
             :year year
             :month month
             :body (when success (json->cljs body))}]))))))

(defn load-more-all-posts [more-link direction]
  (when (and more-link direction)
    (storage-http (method-for-link more-link) (relative-href more-link)
      {:headers (headers-for-link more-link)}
      (fn [{:keys [status success body]}]
        (dispatcher/dispatch!
         [:all-posts-more/finish
          {:org (router/current-org-slug)
           :direction direction
           :body (when success (json->cljs body))}])))))

(defn get-calendar [org-slug]
  (when org-slug
    (let [org-data (dispatcher/org-data)
          calendar-link (utils/link-for (:links org-data) "calendar")]
      (storage-http (method-for-link calendar-link) (relative-href calendar-link)
        {:headers (headers-for-link calendar-link)}
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch!
           [:calendar-get/finish
            {:org (router/current-org-slug)
             :body (when success (json->cljs body))}]))))))

(defn autosave-draft [story-data share-data]
  (when story-data
    (let [autosave-link (utils/link-for (:links story-data) "partial-update")
          fixed-story-data (select-keys
                            story-data
                            [:title :body :board-slug :banner-url :banner-width :banner-height])]
      (storage-http (method-for-link autosave-link) (relative-href autosave-link)
        {:headers (headers-for-link autosave-link)
         :json-params (cljs->json fixed-story-data)}
        (fn [{:keys [success status body]}]
          (dispatcher/dispatch! [:draft-autosave/finish share-data]))))))

(defn get-activity [activity-uuid activity-link]
  (when activity-link
    (storage-http (method-for-link activity-link) (relative-href activity-link)
      {:headers (headers-for-link activity-link)}
      (fn [{:keys [status success body]}]
        (dispatcher/dispatch! [:activity-get/finish status (when success (json->cljs body))])))))

(defn share-activity [post-data share-data]
  (when post-data
    (let [share-link (utils/link-for (:links post-data) "share")
          headers {:headers (headers-for-link share-link)}
          with-json-params (assoc headers :json-params (cljs->json share-data))]
      (storage-http (method-for-link share-link) (relative-href share-link)
        with-json-params
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch! [:activity-share/finish (when success (json->cljs body))]))))))

(defn get-secure-activity [org-slug secure-activity-id]
 (when secure-activity-id
    (let [activity-link {:href (str "/orgs/" org-slug "/entries/" secure-activity-id)
                         :method "GET"
                         :rel ""
                         :accept "application/vnd.open-company.entry.v1+json"}]
      (storage-http (method-for-link activity-link) (relative-href activity-link)
        {:headers (headers-for-link activity-link)}
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch! [:activity-get/finish status (if success (json->cljs body) {})]))))))

(defn force-jwt-refresh []
  (when (j/jwt)
    (go
      (if-let [refresh-url (j/get-key :refresh-url)]
        (let [res (<! (refresh-jwt refresh-url))]
          (if (:success res)
            (update-jwt-cookie! (:body res))
            (dispatcher/dispatch! [:logout])))
        (dispatcher/dispatch! [:logout])))))

(set! (.-OCWebForceRefreshToken js/window) force-jwt-refresh)