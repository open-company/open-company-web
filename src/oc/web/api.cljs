(ns oc.web.api
  (:require-macros [cljs.core.async.macros :refer (go)]
                   [if-let.core :refer (when-let*)])
  (:require [goog.Uri :as guri]
            [clojure.string :as s]
            [cljs-http.client :as http]
            [defun.core :refer (defun-)]
            [taoensso.timbre :as timbre]
            [cljs.core.async :as async :refer (<!)]
            [oc.web.lib.jwt :as j]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.poll :as poll-utils]
            [oc.web.lib.sentry :as sentry]
            [oc.web.local-settings :as ls]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.lib.fullstory :as fullstory]
            [oc.web.utils.ws-client-ids :as ws-client-ids]
            [oc.web.lib.json :refer (json->cljs cljs->json)]
            [oc.web.actions.notifications :as notification-actions]))

(def ^:private web-endpoint ls/web-server-domain)

(def ^:private storage-endpoint ls/storage-server-domain)

(def ^:private auth-endpoint ls/auth-server-domain)

(def ^:private pay-endpoint ls/pay-server-domain)

(def ^:private interaction-endpoint ls/interaction-server-domain)

(def ^:private change-endpoint ls/change-server-domain)

(def ^:private search-endpoint ls/search-server-domain)

(def ^:private reminders-endpoint ls/reminder-server-domain)

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

(declare jwt-refresh-handler)
(declare jwt-refresh-error-hn)
(declare network-error-handler)

(defn config-request
  [jwt-refresh-hn jwt-error-handler network-error-hn]
  (def jwt-refresh-handler jwt-refresh-hn)
  (def jwt-refresh-error-hn jwt-error-handler)
  (def network-error-handler network-error-hn))

(defn complete-params [params]
  (let [change-client-id @ws-client-ids/change-client-id
        interaction-client-id @ws-client-ids/interaction-client-id
        notify-client-id @ws-client-ids/notify-client-id
        with-client-ids (cond-> params
                               change-client-id (assoc-in [:headers "OC-Change-Client-ID"] change-client-id)
                               interaction-client-id (assoc-in [:headers "OC-Interaction-Client-ID"] interaction-client-id)
                               notify-client-id (assoc-in [:headers "OC-Notify-Client-ID"] notify-client-id))
        jwt-or-id-token (or (j/jwt)
                            (j/id-token))
        ; invite-token (-> @router/path :query-params :invite-token)
        ]
    (cond
      jwt-or-id-token
      (-> {:with-credentials? false}
          (merge with-client-ids)
          (update :headers merge {"Authorization" (str "Bearer " jwt-or-id-token)}))
      ; invite-token
      ; (-> {:with-credentials? false}
      ;     (merge with-client-ids)
      ;     (update :headers merge {"Authorization" (str "Bearer " invite-token)}))
      :else
      with-client-ids)))

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

(defn- refresh-jwt [refresh-link]
  (let [refresh-url (if (map? refresh-link)
                      (str ls/auth-server-domain (relative-href refresh-link))
                      refresh-link)
        headers (if (map? refresh-link) {:headers (headers-for-link refresh-link)} {})
        method (if (map? refresh-link)
                  (method-for-link refresh-link)
                  http/get)]
  (method refresh-url (complete-params headers))))

;; Async version of jwt-refresh
(defn- jwt-refresh [success-cb error-cb]
  (go
   (if-let [refresh-url (j/get-key :refresh-url)]
     (let [res (<! (refresh-jwt refresh-url))]
       (timbre/debug "jwt-refresh" res)
       (if (:success res)
         (success-cb (:body res))
         (error-cb)))
     (error-cb))))

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
  (let [jwt (j/jwt)
        expired? (j/expired?)]
    (go
     ;; sync refresh
     (when (and jwt expired?)
       (if-let [refresh-url (j/get-key :refresh-url)]
         (let [res (<! (refresh-jwt refresh-url))]
            (timbre/debug "jwt-refresh" res)
            (if (:success res)
             (jwt-refresh-handler (:body res))
              (jwt-refresh-error-hn)))
          (jwt-refresh-error-hn)))


      (let [{:keys [status body] :as response} (<! (method (str endpoint path) (complete-params params)))]
        (timbre/debug "Resp:" (method-name method) (str endpoint path) status response)
        ; when a request gets a 401, redirect the user to logout
        ; (presumably they are using an old token, or attempting anonymous access),
        ; but only if they are already logged in
        (when (and jwt
                   (= status 401))
          (router/redirect! oc-urls/logout))
        ; If it was a 5xx or a 0 show a banner for network issues
        (when (or (zero? status)
                  (<= 500 status 599))
          (network-error-handler))
        ; report all 5xx to sentry
        (when (or (<= 500 status 599)
                  (= status 400)
                  (= status 422))
          (let [report {:response response
                        :path path
                        :method (method-name method)
                        :jwt (j/jwt)
                        :params params
                        :sessionURL (fullstory/session-url)}]
            (timbre/error "xhr response error:" (method-name method) ":" (str endpoint path) " -> " status)
            (sentry/capture-error-with-extra-context! report (str "xhr response error:" status))))
        (on-complete response)))))

(def ^:private web-http (partial req web-endpoint))

(def ^:private storage-http (partial req storage-endpoint))

(def ^:private auth-http (partial req auth-endpoint))

(def ^:private pay-http (partial req pay-endpoint))

(def ^:private interaction-http (partial req interaction-endpoint))

(def ^:private change-http (partial req change-endpoint))

(def ^:private search-http (partial req search-endpoint))

(def ^:private reminders-http (partial req reminders-endpoint))

;; Report failed api request

(defn- handle-missing-link [callee-name link callback & [parameters]]
  (timbre/error "Handling missing link:" callee-name ":" link)
  (sentry/capture-message-with-extra-context!
    (merge {:callee callee-name
            :link link
            :sessionURL (fullstory/session-url)}
     parameters)
    (str "Client API error on: " callee-name))
  (notification-actions/show-notification (assoc utils/internal-error :expire 5))
  (when (fn? callback)
    (callback {:success false :status 0})))

;; Allowed keys

(def org-allowed-keys [:name :logo-url :logo-width :logo-height :content-visibility :why-carrot])

(def entry-allowed-keys [:headline :body :abstract :attachments :video-id :video-error :board-slug :status :must-see :polls :publisher-board])

(def board-allowed-keys [:name :access :slack-mirror :viewers :authors :private-notifications :publisher-board])

(def user-allowed-keys [:first-name :last-name :password :avatar-url :timezone :digest-medium :notification-medium :reminder-medium :qsg-checklist :title :location :blurb :profiles])

(def reminder-allowed-keys [:org-uuid :headline :assignee :frequency :period-occurrence :week-occurrence])

(defn web-app-version-check [callback]
  (web-http http/get (str "/version/version" ls/deploy-key ".json")
    {:heades {
      ; required by Chrome
      "Access-Control-Allow-Headers" "Content-Type"
      ; custom content type
      "content-type" "application/json"}}
    #(when (fn? callback)
      (callback %))))

(defn store-500-test [with-response]
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

;; Entry point and Auth settings

(defn get-entry-point [requested-org callback]
  (storage-http http/get "/"
    {:query-params {:requested requested-org}}
    (fn [{:keys [success body]}]
      (let [fixed-body (when success (json->cljs body))]
        (callback success fixed-body)))))

(defn get-auth-settings [callback]
  (let [invite-token (-> @router/path :query-params :invite-token)
        default-headers (headers-for-link {:access-control-allow-headers nil
                                           :content-type "application/json"})
        with-auth-token (if invite-token
                          (merge default-headers {"Authorization" (str "Bearer " invite-token)})
                          default-headers)
        header-options {:headers with-auth-token}]
    (auth-http http/get "/" header-options
     (fn [{:keys [status success body] :as response}]
       (let [body (when success body)]
         (callback body status))))))

;; Payments

(defn get-payments [payments-link callback]
  (if payments-link
    (auth-http (method-for-link payments-link) (relative-href payments-link)
     {:headers (headers-for-link payments-link)}
     callback)
    (handle-missing-link "get-payments" payments-link callback)))

(defn update-plan-subscription
  "Used for PUT, PATCH and DELETE of subscriptions. Adds json-params with {:plan-id plan-id}
   only if a plan-id is passed."
  [update-link plan-id callback]
  (if update-link
    (let [update-subscription-body (cljs->json {:plan-id plan-id})
          options* {:headers (headers-for-link update-link)}
          options (if plan-id (assoc options* :json-params update-subscription-body) options*)]
      (auth-http (method-for-link update-link) (relative-href update-link)
       options callback))
    (handle-missing-link "update-plan-subscription" update-link callback)))

(defn get-checkout-session-id [checkout-link success-url cancel-url callback]
  (if checkout-link
    (auth-http (method-for-link checkout-link) (relative-href checkout-link)
     {:headers (headers-for-link checkout-link)
      :json-params (cljs->json {:success-url success-url
                                :cancel-url cancel-url})}
     callback)
    (handle-missing-link "get-checkout-session-id" checkout-link callback)))

;; Org

(defn get-org [org-link callback]
  (if org-link
    (let [params {:headers (headers-for-link org-link)}]
      (storage-http (method-for-link org-link)
                    (relative-href org-link)
                    params
                    callback))
    (handle-missing-link "get-org" org-link callback)))

(defn patch-org [org-patch-link data callback]
  (if (and org-patch-link data)
    (let [org-data (select-keys data org-allowed-keys)
          json-data (cljs->json org-data)]
      (storage-http (method-for-link org-patch-link) (relative-href org-patch-link)
        {:json-params json-data
         :headers (headers-for-link org-patch-link)}
        callback))
    (handle-missing-link "patch-org" org-patch-link callback {:data data})))

(defn add-email-domain [add-email-domain-link domain callback team-data & [pre-flight]]
  (if (and add-email-domain-link domain)
    (let [email-domain-payload {:email-domain domain}
          with-preflight (if pre-flight
                           (assoc email-domain-payload :pre-flight true)
                           email-domain-payload)
          json-data (cljs->json with-preflight)]
      (auth-http (method-for-link add-email-domain-link) (relative-href add-email-domain-link)
        {:headers (headers-for-link add-email-domain-link)
         :json-params json-data}
        callback))
    (handle-missing-link "add-email-domain" add-email-domain-link callback
     {:domain domain :team-data team-data :pre-flight pre-flight})))

(defn create-org [create-org-link org-data callback]
  (if create-org-link
    (let [team-id (first (j/get-key :teams))
          fixed-org-data (assoc (select-keys org-data org-allowed-keys) :team-id team-id)]
      (when (and fixed-org-data create-org-link)
        (storage-http (method-for-link create-org-link) (relative-href create-org-link)
          {:headers (headers-for-link create-org-link)
           :json-params (cljs->json fixed-org-data)}
          callback)))
    (handle-missing-link "create-org" create-org-link callback {:org-data org-data})))

(defn delete-samples [delete-samples-link callback]
  (if delete-samples-link
    (storage-http (method-for-link delete-samples-link) (relative-href delete-samples-link)
      {:headers (headers-for-link delete-samples-link)}
      callback)
    (handle-missing-link "delete-samples" delete-samples-link callback)))

;; Board/section

(defn get-board [board-link callback]
  (if board-link
    (storage-http (method-for-link board-link) (relative-href board-link)
      {:headers (headers-for-link board-link)}
      callback)
    (handle-missing-link "get-board" board-link callback)))

(defn patch-board [board-patch-link data note callback]
  (if (and board-patch-link data)
    (let [board-data (select-keys data [:name :slug :access :slack-mirror :authors :viewers :private-notifications])
          with-personal-note (assoc board-data :note note)
          json-data (cljs->json with-personal-note)]
      (storage-http (method-for-link board-patch-link) (relative-href board-patch-link)
        {:json-params json-data
         :headers (headers-for-link board-patch-link)}
        (fn [{:keys [success body status]}]
          (callback success body status))))
    (handle-missing-link "patch-board" board-patch-link callback {:note note :data data})))

(defn create-board [create-board-link board-data note callback]
  (if (and create-board-link board-data)
    (let [fixed-board-data (select-keys board-data board-allowed-keys)
          fixed-entries (mapv #(-> %
                                (select-keys (conj entry-allowed-keys :uuid :secure-uuid))
                                (poll-utils/clean-polls))
                         (:entries board-data))
          with-entries (if (pos? (count fixed-entries))
                         (assoc fixed-board-data :entries fixed-entries)
                         fixed-board-data)
          with-personal-note (assoc with-entries :note note)]
      (when (and (:name fixed-board-data) create-board-link)
        (storage-http (method-for-link create-board-link) (relative-href create-board-link)
          {:headers (headers-for-link create-board-link)
           :json-params (cljs->json with-personal-note)}
          callback)))
    (handle-missing-link "create-board" create-board-link callback
     {:board-data board-data :note note})))

(defn pre-flight-section-check [pre-flight-link section-slug section-name callback]
  (if (and pre-flight-link
             section-name)
    (storage-http (method-for-link pre-flight-link) (relative-href pre-flight-link)
     {:headers (headers-for-link pre-flight-link)
      :json-params (cljs->json {:name section-name
                                :exclude (vec (remove nil? [section-slug]))
                                :pre-flight true})}
     callback)
    (handle-missing-link "pre-flight-section-check" pre-flight-link callback
     {:section-slug section-slug :section-name section-name})))

(defn delete-board [delete-board-link board-slug callback]
  (if (and delete-board-link board-slug)
    (storage-http (method-for-link delete-board-link) (relative-href delete-board-link)
      {:headers (headers-for-link delete-board-link)}
      (fn [{:keys [status success body]}]
        (callback status success body)))
    (handle-missing-link "delete-board" delete-board-link callback {:board-slug board-slug})))

(defn remove-user-from-private-board [remove-user-link callback]
  (if remove-user-link
    (storage-http (method-for-link remove-user-link) (relative-href remove-user-link)
     {:headers (headers-for-link remove-user-link)}
     (fn [{:keys [status success body]}]
      (callback status success body)))
    (handle-missing-link "remove-user-from-private-board" remove-user-link callback)))

;; All Posts

(defn get-all-posts [activity-link callback]
  (if activity-link
    (let [href (relative-href activity-link)]
      (storage-http (method-for-link activity-link) href
        {:headers (headers-for-link activity-link)}
        callback))
    (handle-missing-link "get-all-posts" activity-link callback)))

(defn load-more-items [more-link direction callback]
  (if (and more-link direction)
    (storage-http (method-for-link more-link) (relative-href more-link)
      {:headers (headers-for-link more-link)}
      callback)
    (handle-missing-link "load-more-items" more-link callback {:direction direction})))

;; Auth

(defn auth-with-email [auth-link email pswd callback]
  (if (and auth-link email pswd)
    (auth-http (method-for-link auth-link) (relative-href auth-link)
      {:basic-auth {
        :username email
        :password pswd}
       :headers (headers-for-link auth-link)}
      (fn [{:keys [success body status]}]
        (callback success body status)))
    (handle-missing-link "auth-with-email" auth-link callback
     {:email email :pswd (str (repeat (count pswd) "*"))})))

(defn auth-with-token [auth-link token callback]
  (if (and auth-link token)
    (auth-http (method-for-link auth-link) (relative-href auth-link)
      {:headers (merge (headers-for-link auth-link)
                 {; required by Chrome
                  "Access-Control-Allow-Headers" "Content-Type, Authorization"
                  "Authorization" (str "Bearer " token)})}
      (fn [{:keys [success body status]}]
        (callback success body status)))
    (handle-missing-link "auth-with-token" auth-link callback {:token token})))

(defn confirm-invitation [auth-link token callback]
  (if (and auth-link token)
    (auth-http (method-for-link auth-link) (relative-href auth-link)
      {:headers (merge (headers-for-link auth-link)
                       {; required by Chrome
                        "Access-Control-Allow-Headers" "Content-Type, Authorization"
                        "Authorization" (str "Bearer " token)})}
      (fn [{:keys [status body success]}]
        (utils/after 100 #(callback status body success))))
    (handle-missing-link "confirm-invitation" auth-link callback {:token token})))

;; Signup

(defn signup-with-email [auth-link first-name last-name email pswd timezone callback]
  (let [invite-token (-> @router/path :query-params :invite-token)
        default-headers (headers-for-link auth-link)
        with-auth-token (if invite-token
                          (merge default-headers {"Authorization" (str "Bearer " invite-token)})
                          default-headers)
        user-map {:first-name first-name
                  :last-name last-name
                  :email email
                  :password pswd
                  :timezone timezone}]
    (if (and auth-link first-name last-name email pswd)
      (auth-http (method-for-link auth-link) (relative-href auth-link)
        {:json-params (cljs->json user-map)
         :headers with-auth-token}
        (fn [{:keys [success body status]}]
          (callback success body status)))
      (handle-missing-link "signup-with-email" auth-link callback
       (assoc user-map :password (str (repeat (count pswd) "*")))))))

;; Team(s)

(defn get-teams [enumerate-link callback]
  (if enumerate-link
    (auth-http (method-for-link enumerate-link) (relative-href enumerate-link)
      {:headers (headers-for-link enumerate-link)}
      callback)
    (handle-missing-link "get-teams" enumerate-link callback)))

(defn get-team [team-link callback]
  (if team-link
    (auth-http (method-for-link team-link) (relative-href team-link)
      {:headers (headers-for-link team-link)}
      callback)
    (handle-missing-link "get-team" team-link callback)))

(defn enumerate-channels [enumerate-link callback]
  (if enumerate-link
    (auth-http (method-for-link enumerate-link) (relative-href enumerate-link)
               {:headers (headers-for-link enumerate-link)}
               callback)
    (handle-missing-link "enumerate-channels" enumerate-link callback)))

(defn patch-team [team-patch-link team-id new-team-data callback]
  (if (and team-patch-link team-id new-team-data)
    (auth-http (method-for-link team-patch-link) (relative-href team-patch-link)
      {:headers (headers-for-link team-patch-link)
       :json-params (cljs->json new-team-data)}
      callback)
    (handle-missing-link "patch-team" team-patch-link callback
     {:team-id team-id :new-team-data new-team-data})))

(defn send-invitation
  "Give a user email and type of user send an invitation to the team.
   If the team has only one company, checked via API entry point links, send the company name of that.
   Add the company's logo and its size if possible."
  [invitation-link invited-user invite-from user-type first-name last-name note callback]
  (if (and invitation-link invited-user invite-from user-type)
    (let [org-data (dispatcher/org-data)
          json-params {:first-name first-name
                       :last-name last-name
                       :note note
                       :admin (= user-type :admin)}
          with-invited-user (if (= invite-from "slack")
                              (merge
                               json-params
                               {:slack-id (:slack-id invited-user)
                                :slack-org-id (:slack-org-id invited-user)
                                :avatar-url (:avatar-url invited-user)
                                :email (:email invited-user)})
                              (assoc json-params :email invited-user))
          with-company-name (merge with-invited-user {:org-name (:name org-data)
                                                      :logo-url (:logo-url org-data)
                                                      :logo-width (:logo-width org-data)
                                                      :logo-height (:logo-height org-data)})]
      (auth-http (method-for-link invitation-link) (relative-href invitation-link)
        {:json-params (cljs->json with-company-name)
         :headers (headers-for-link invitation-link)}
        callback))
    (handle-missing-link "send-invitation" invitation-link callback
     {:invited-user invited-user :invite-from invite-from :user-type user-type
      :first-name first-name :last-name last-name :note note})))

(defn add-admin [add-admin-link user callback]
  (if add-admin-link
    (auth-http (method-for-link add-admin-link) (relative-href add-admin-link)
      {:headers (headers-for-link add-admin-link)}
      callback)
    (handle-missing-link "add-admin" add-admin-link callback {:user user})))

(defn remove-admin [remove-admin-link user callback]
  (if remove-admin-link
    (auth-http (method-for-link remove-admin-link) (relative-href remove-admin-link)
      {:headers (headers-for-link remove-admin-link)}
      callback)
    (handle-missing-link "remove-admin" remove-admin-link callback {:user user})))

(defn handle-invite-link [invite-token-link callback]
  (if invite-token-link
    (auth-http (method-for-link invite-token-link) (relative-href invite-token-link)
      {:headers (headers-for-link invite-token-link)}
      callback)
    (handle-missing-link "handle-invite-link" invite-token-link callback)))

(defn get-active-users [active-users-link callback]
  (if active-users-link
    (auth-http (method-for-link active-users-link) (relative-href active-users-link)
     {:headers (headers-for-link active-users-link)}
     callback)
    (handle-missing-link "get-active-users" active-users-link callback)))

;; User

(defn user-action [action-link payload callback]
  (if action-link
    (let [headers {:headers (headers-for-link action-link)}
          with-payload (if payload
                          (assoc headers :json-params payload)
                          headers)]
      (auth-http (method-for-link action-link) (relative-href action-link)
        with-payload
        callback))
    (handle-missing-link "user-action" action-link callback {:payload payload})))

(defn collect-password [update-link pswd callback]
  (if (and update-link pswd)
    (auth-http (method-for-link update-link) (relative-href update-link)
      {:json-params {
        :password pswd}
       :headers (headers-for-link update-link)}
      (fn [{:keys [status body success]}]
        (utils/after 100 #(callback status body success))))
    (handle-missing-link "collect-password" update-link callback {:pswd (str (repeat (count pswd) "*"))})))

(defn get-user [user-link callback]
  (if user-link
    (auth-http (method-for-link user-link) (relative-href user-link)
      {:headers (headers-for-link user-link)}
      (fn [{:keys [status body success]}]
        (callback success body)))
    (handle-missing-link "get-user" user-link callback)))

(defn patch-user [patch-user-link new-user-data callback]
  (if (and patch-user-link
           (map? new-user-data))
    (let [without-email (dissoc new-user-data :email)
          safe-new-user-data (select-keys without-email user-allowed-keys)]
      (auth-http (method-for-link patch-user-link) (relative-href patch-user-link)
        {:headers (headers-for-link patch-user-link)
         :json-params (cljs->json safe-new-user-data)}
         (fn [{:keys [status body success]}]
           (callback status body success))))
    (handle-missing-link "patch-user" patch-user-link callback
     {:new-user-data new-user-data})))

(defn refresh-slack-user [refresh-link callback]
  (if refresh-link
    (auth-http (method-for-link refresh-link) (relative-href refresh-link)
     {:headers (headers-for-link refresh-link)}
     (fn [{:keys [status body success]}]
       (callback status body success)))
    (handle-missing-link "refresh-slack-user" refresh-link callback)))

(defn add-author
  "Given a user-id add him as an author to the current org.
  Refresh the user list and the org-data when finished."
  [add-author-link user-id callback]
  (if add-author-link
    (storage-http (method-for-link add-author-link) (relative-href add-author-link)
     {:headers (headers-for-link add-author-link)
      :body user-id}
     callback)
    (handle-missing-link "add-author" add-author-link callback)))

(defn remove-author
  "Given a map containing :user-id and :links, remove the user as an author using the `remove` link.
  Refresh the org data when finished."
  [remove-author-link user-author callback]
  (if remove-author-link
    (storage-http (method-for-link remove-author-link) (relative-href remove-author-link)
     {:headers (headers-for-link remove-author-link)}
     callback)
    (handle-missing-link "remove-author" remove-author-link callback
     {:user-author user-author})))

(defn password-reset
  [reset-link email callback]
  (if (and reset-link email)
    (auth-http (method-for-link reset-link) (relative-href reset-link)
      {:headers (headers-for-link reset-link)
       :body email}
      (fn [{:keys [status success body]}]
        (callback status)))
    (handle-missing-link "password-reset" reset-link callback
     {:email email})))

(defn resend-verification-email [resend-link callback]
  (if resend-link
    (auth-http (method-for-link resend-link) (relative-href resend-link)
     {:headers (headers-for-link resend-link)}
     (fn [{:keys [status success body]}]
      (callback success)))))

(defn add-expo-push-token [add-token-link push-token callback]
  (if (and add-token-link push-token)
    (auth-http (method-for-link add-token-link) (relative-href add-token-link)
               {:headers (headers-for-link add-token-link)
                :body push-token}
               (fn [{:keys [status success body]}]
                 (callback success)))
    (handle-missing-link "add-expo-push-token" add-token-link callback
                         {:push-token push-token})))

;; Interactions

(defn get-comments [comments-link callback]
  (if comments-link
    (interaction-http (method-for-link comments-link) (relative-href comments-link)
      {:headers (headers-for-link comments-link)}
      callback)
    (handle-missing-link "get-comments" comments-link callback)))

(defn add-comment [add-comment-link comment-body comment-uuid parent-comment-uuid callback]
  (if (and add-comment-link comment-body)
    (let [json-data (cljs->json {:body comment-body
                                 :uuid comment-uuid
                                 :parent-uuid parent-comment-uuid})]
      (interaction-http (method-for-link add-comment-link) (relative-href add-comment-link)
        {:headers (headers-for-link add-comment-link)
         :json-params json-data}
        callback))
    (handle-missing-link "add-comment" add-comment-link callback
     {:comment-body comment-body})))

(defn delete-comment
  [delete-comment-link callback]
  (if delete-comment-link
    (interaction-http (method-for-link delete-comment-link) (relative-href delete-comment-link)
     {:headers (headers-for-link delete-comment-link)}
     callback)
    (handle-missing-link "delete-comment" delete-comment-link callback)))

(defn patch-comment
  [patch-comment-link new-comment-body callback]
  (if (and patch-comment-link new-comment-body)
    (let [json-data (cljs->json {:body new-comment-body})]
      (interaction-http (method-for-link patch-comment-link) (relative-href patch-comment-link)
        {:headers (headers-for-link patch-comment-link)
         :json-params json-data}
        callback))
    (handle-missing-link "patch-comment" patch-comment-link callback
     {:new-comment-body new-comment-body})))

(defn toggle-reaction
  [reaction-link callback]
  (if reaction-link
    (interaction-http (method-for-link reaction-link) (relative-href reaction-link)
     {:headers (headers-for-link reaction-link)}
     callback)
    (handle-missing-link "toggle-reaction" reaction-link callback)))

(defn react-from-picker
  "Given the link to react with an arbitrary emoji and the emoji, post it to the interaction service"
  [react-link emoji callback]
  (if react-link
    (interaction-http (method-for-link react-link) (relative-href react-link)
     {:headers (headers-for-link react-link)
      :body emoji}
     callback)
    (handle-missing-link "react-from-picker" react-link callback
     {:emoji emoji})))

;; Entry

(defn get-entry
  [entry-link callback]
  (if entry-link
    (storage-http (method-for-link entry-link) (relative-href entry-link)
     {:headers (headers-for-link entry-link)}
     callback)
    (handle-missing-link "get-entry" entry-link callback)))

(defn create-entry
  [create-entry-link entry-data edit-key callback]
  (if (and create-entry-link entry-data)
    (let [cleaned-entry-data (-> entry-data
                              (select-keys entry-allowed-keys)
                              (poll-utils/clean-polls))]
      (storage-http (method-for-link create-entry-link) (relative-href create-entry-link)
       {:headers (headers-for-link create-entry-link)
        :json-params (cljs->json cleaned-entry-data)}
       (partial callback entry-data edit-key)))
    (handle-missing-link "create-entry" create-entry-link callback
     {:entry-data entry-data
      :edit-key edit-key})))

(defn publish-entry
  [publish-entry-link entry-data callback]
  (if (and entry-data publish-entry-link)
    (let [cleaned-entry-data (-> entry-data
                              (select-keys entry-allowed-keys)
                              (poll-utils/clean-polls))]
      (storage-http (method-for-link publish-entry-link) (relative-href publish-entry-link)
        {:headers (headers-for-link publish-entry-link)
         :json-params (cljs->json cleaned-entry-data)}
        callback))
    (handle-missing-link "publish-entry" publish-entry-link callback
     {:entry-data entry-data})))

(defn patch-entry
  [patch-entry-link entry-data edit-key callback]
  (if patch-entry-link
    (let [cleaned-entry-data (-> entry-data
                              (select-keys entry-allowed-keys)
                              (poll-utils/clean-polls))]
      (storage-http (method-for-link patch-entry-link) (relative-href patch-entry-link)
       {:headers (headers-for-link patch-entry-link)
        :json-params (cljs->json cleaned-entry-data)}
       (partial callback entry-data edit-key)))
    (handle-missing-link "patch-entry" patch-entry-link callback
     {:entry-data entry-data
      :edit-key edit-key})))

(defn delete-entry [delete-entry-link callback]
  (if delete-entry-link
    (storage-http (method-for-link delete-entry-link) (relative-href delete-entry-link)
     {:headers (headers-for-link delete-entry-link)}
     callback)
    (handle-missing-link "delete-entry" delete-entry-link callback)))

(defn revert-entry [revert-entry-link entry-data callback]
  (if (and revert-entry-link entry-data)
    (let [cleaned-entry-data (select-keys entry-data [:revision-id])]
      (storage-http (method-for-link revert-entry-link) (relative-href revert-entry-link)
       {:headers (headers-for-link revert-entry-link)
        :json-params (cljs->json cleaned-entry-data)}
       callback))
    (handle-missing-link "revert-entry" revert-entry-link callback
     {:entry-data entry-data})))

(defn share-entry [share-link share-data callback]
  (if (and share-link share-data)
    (storage-http (method-for-link share-link) (relative-href share-link)
     {:headers (headers-for-link share-link)
      :json-params (cljs->json share-data)}
     callback)
    (handle-missing-link "share-entry" share-link callback
     {:share-data share-data})))

(defn get-secure-entry [secure-link callback]
  (if secure-link
    (storage-http (method-for-link secure-link) (relative-href secure-link)
     {:headers (headers-for-link secure-link)}
     callback)
    (handle-missing-link "get-secure-entry" secure-link callback)))

(defn get-current-entry [org-slug board-slug activity-uuid callback]
  (let [activity-link {:href (str "/orgs/" org-slug "/boards/" board-slug "/entries/" activity-uuid)
                         :method "GET"
                         :rel ""
                         :accept "application/vnd.open-company.entry.v1+json"}]
    (if (and org-slug board-slug activity-uuid)
      (storage-http (method-for-link activity-link) (relative-href activity-link)
       {:headers (headers-for-link activity-link)}
       callback)
      (handle-missing-link "get-current-entry" activity-link callback
       {:org-slug org-slug :board-slug board-slug :activity-uuid activity-uuid}))))

;; Search

(defn query
  [org-uuid search-query callback]
  (let [search-link {:href (str "/search/?q=" search-query "&org=" org-uuid)
                     :content-type (content-type "search")
                     :method "GET" :rel ""}]
    (if search-query
      (search-http (method-for-link search-link) (relative-href search-link)
       {:headers (headers-for-link search-link)}
       (fn [{:keys [status success body]}]
         (callback {:query search-query
                    :success success
                    :error (when-not success body)
                    :body (when (and success (seq body)) (json->cljs body))})))
      (handle-missing-link "query" search-link callback
       {:org-uuid org-uuid
        :search-query search-query}))))

;; Reminders

(defn get-reminders
  [reminders-link callback]
  (if reminders-link
    (reminders-http (method-for-link reminders-link) (relative-href reminders-link)
     {:headers (headers-for-link reminders-link)}
     callback)
    (handle-missing-link "get-reminders" reminders-link callback)))

(defn add-reminder [add-reminder-link reminder-data callback]
  (if (and add-reminder-link reminder-data)
    (let [fixed-reminder-data (select-keys reminder-data reminder-allowed-keys)
          json-data (cljs->json fixed-reminder-data)]
      (reminders-http (method-for-link add-reminder-link) (relative-href add-reminder-link)
       {:headers (headers-for-link add-reminder-link)
        :json-params json-data}
       callback))
    (handle-missing-link "add-reminder" add-reminder-link callback)))

(defn update-reminder [update-reminder-link reminder-data callback]
  (if (and update-reminder-link reminder-data)
    (let [fixed-reminder-data (select-keys reminder-data reminder-allowed-keys)
          json-data (cljs->json fixed-reminder-data)]
      (reminders-http (method-for-link update-reminder-link) (relative-href update-reminder-link)
       {:headers (headers-for-link update-reminder-link)
        :json-params json-data}
       callback))
    (handle-missing-link "update-reminder" update-reminder-link callback)))

(defn delete-reminder [delete-reminder-link callback]
  (if delete-reminder-link
    (reminders-http (method-for-link delete-reminder-link) (relative-href delete-reminder-link)
     {:headers (headers-for-link delete-reminder-link)}
     callback)
    (handle-missing-link "delete-reminder" delete-reminder-link callback)))

(defn get-reminders-roster [roster-link callback]
  (if roster-link
    (reminders-http (method-for-link roster-link) (relative-href roster-link)
     {:headers (headers-for-link roster-link)}
     callback)
    (handle-missing-link "get-reminders-roster" roster-link callback)))

;; Bookmarks

(defn toggle-bookmark [bookmark-link callback]
  (if bookmark-link
    (storage-http (method-for-link bookmark-link) (relative-href bookmark-link)
     {:headers (headers-for-link bookmark-link)}
     callback)
    (handle-missing-link "toggle-bookmark" bookmark-link callback)))

;; Inbox

(defn inbox-dismiss [dismiss-link dismiss-at callback]
  (if dismiss-link
    (storage-http (method-for-link dismiss-link) (relative-href dismiss-link)
     {:headers (headers-for-link dismiss-link)
      :body dismiss-at}
     callback)
    (handle-missing-link "inbox-dismiss" dismiss-link callback {:dismiss-at dismiss-at})))

(defn inbox-unread [unread-link callback]
  (if unread-link
    (storage-http (method-for-link unread-link) (relative-href unread-link)
     {:headers (headers-for-link unread-link)}
     callback)
    (handle-missing-link "inbox-unread" unread-link callback)))

(defn inbox-follow [follow-link callback]
  (if follow-link
    (storage-http (method-for-link follow-link) (relative-href follow-link)
     {:headers (headers-for-link follow-link)}
     callback)
    (handle-missing-link "inbox-follow" follow-link callback)))

(defn inbox-unfollow [unfollow-link callback]
  (if unfollow-link
    (storage-http (method-for-link unfollow-link) (relative-href unfollow-link)
     {:headers (headers-for-link unfollow-link)}
     callback)
    (handle-missing-link "inbox-unfollow" unfollow-link callback)))

(defn inbox-dismiss-all [dismiss-all-link dismiss-at callback]
  (if dismiss-all-link
    (storage-http (method-for-link dismiss-all-link) (relative-href dismiss-all-link)
     {:headers (headers-for-link dismiss-all-link)
      :body dismiss-at}
     callback)
    (handle-missing-link "inbox-dismiss-all" dismiss-all-link callback {:dismiss-at dismiss-at})))

;; Contributors

(defn get-contributor [contrib-link callback]
  (if contrib-link
    (storage-http (method-for-link contrib-link) (relative-href contrib-link)
     {:headers (headers-for-link contrib-link)}
     callback)
    (handle-missing-link "get-contributor" contrib-link callback)))

;; Polls

(defn poll-vote [vote-link callback]
  (if vote-link
    (storage-http (method-for-link vote-link) (relative-href vote-link)
     {:headers (headers-for-link vote-link)}
     callback)
    (handle-missing-link "vote-link" vote-link callback)))

(defn poll-add-reply [add-reply-link reply-body callback]
  (if add-reply-link
    (storage-http (method-for-link add-reply-link) (relative-href add-reply-link)
     {:headers (headers-for-link add-reply-link)
      :body reply-body}
     callback)
    (handle-missing-link "poll-add-reply" add-reply-link callback {:reply-body reply-body})))

(defn poll-delete-reply [delete-reply-link callback]
  (if delete-reply-link
    (storage-http (method-for-link delete-reply-link) (relative-href delete-reply-link)
     {:headers (headers-for-link delete-reply-link)}
     callback)
    (handle-missing-link "poll-delete-reply" delete-reply-link callback)))

;; WRT

(defn request-reads-data [item-id]
  (when (seq item-id)
    (ws-cc/who-read item-id)))

(defn request-reads-count [item-ids]
  (when (seq item-ids)
    (ws-cc/who-read-count item-ids)))

;; Change service http

(defn mark-unread [mark-unread-link container-id callback]
  (if mark-unread-link
    (change-http (method-for-link mark-unread-link) (relative-href mark-unread-link)
     {:headers (headers-for-link mark-unread-link)
      :body container-id}
     callback)
    (handle-missing-link "mark-unread" mark-unread-link callback)))