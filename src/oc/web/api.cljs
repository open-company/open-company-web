(ns oc.web.api
  (:require-macros [cljs.core.async.macros :refer (go)]
                   [if-let.core :refer (when-let*)])
  (:require [cljs.core.async :as async :refer (<!)]
            [cljs-http.client :as http]
            [defun.core :refer (defun-)]
            [taoensso.timbre :as timbre]
            [clojure.string :as s]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as j]
            [oc.web.router :as router]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.ws-change-client :as ws-cc]
            [oc.web.lib.json :refer (json->cljs cljs->json)]
            [oc.web.lib.raven :as sentry]
            [goog.Uri :as guri]))

(def ^:private web-endpoint ls/web-server-domain)

(def ^:private storage-endpoint ls/storage-server-domain)

(def ^:private auth-endpoint ls/auth-server-domain)

(def ^:private pay-endpoint ls/pay-server-domain)

(def ^:private interaction-endpoint ls/interaction-server-domain)

(def ^:private search-endpoint ls/search-server-domain)

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
    (timbre/debug jwt expired?)
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
        ; when a request get a 401 logout the user since his using an old token, need to repeat auth process
        ; no token refresh
        (when (and (j/jwt)
                   (= status 401))
          (router/redirect! oc-urls/logout))
        ; If it was a 5xx or a 0 show a banner for network issues
        (when (or (zero? status)
                  (and (>= status 500) (<= status 599)))
          (network-error-handler))
        ; report all 5xx to sentry
        (when (or (and (>= status 500) (<= status 599))
                  (= status 400)
                  (= status 422))
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

(def ^:private web-http (partial req web-endpoint))

(def ^:private storage-http (partial req storage-endpoint))

(def ^:private auth-http (partial req auth-endpoint))

(def ^:private pay-http (partial req pay-endpoint))

(def ^:private interaction-http (partial req interaction-endpoint))

(def ^:private search-http (partial req search-endpoint))

;; Report failed api request

(defn- report-missing-link [callee-name link & parameters]
  (timbre/error "report missing link:" callee-name ":" link)
  (sentry/set-user-context! {:callee callee-name
                             :link link
                             :parameters parameters})
  (sentry/capture-message (str "missing link for:" callee-name))
  (sentry/set-user-context! nil))

;; Allowed keys

(def entry-allowed-keys [:headline :body :attachments :video-id :video-transcript :video-error :board-slug :status :must-see])

(def board-allowed-keys [:name :access :slack-mirror :viewers :authors :private-notifications])

(def user-allowed-keys [:first-name :last-name :password :avatar-url :timezone :digest-frequency :digest-medium])

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
  (let [entry-point-href (str "/" (when requested-org (str "?requested=" requested-org)))]
    (storage-http http/get entry-point-href
     nil
     (fn [{:keys [success body]}]
       (let [fixed-body (when success (json->cljs body))]
         (callback success fixed-body))))))

(defn get-auth-settings [callback]
  (auth-http http/get "/"
   {:headers (headers-for-link {:access-control-allow-headers nil :content-type "application/json"})}
   (fn [response]
     (let [body (if (:success response) (:body response) false)]
       (callback body)))))

;; Subscription

(defn get-subscription [company-uuid callback]
  (pay-http http/get (str "/subscriptions/" company-uuid)
   nil
   callback))

;; Org

(defn get-org [org-link callback]
  (when org-link
    (storage-http (method-for-link org-link) (relative-href org-link)
     {:headers (headers-for-link org-link)}
     callback)))

(def org-keys [:name :logo-url :logo-width :logo-height])

(defn patch-org [org-patch-link data callback]
  (when (and org-patch-link data)
    (let [org-data (select-keys data org-keys)
          json-data (cljs->json org-data)]
      (storage-http (method-for-link org-patch-link) (relative-href org-patch-link)
        {:json-params json-data
         :headers (headers-for-link org-patch-link)}
        callback))))

(defn patch-org-sections [org-patch-link data callback]
  (when (and org-patch-link data)
    (let [json-data (cljs->json data)]
      (storage-http (method-for-link org-patch-link) (relative-href org-patch-link)
        {:json-params json-data
         :headers (headers-for-link org-patch-link)}
        callback))))

(defn add-email-domain [add-email-domain-link domain callback team-data]
  (when (and add-email-domain-link domain)
    (auth-http (method-for-link add-email-domain-link) (relative-href add-email-domain-link)
      {:headers (headers-for-link add-email-domain-link)
       :body domain}
      callback)))

(defn create-org [create-org-link org-data callback]
  (when create-org-link
    (let [team-id (first (j/get-key :teams))
          fixed-org-data (assoc (select-keys org-data org-keys) :team-id team-id)]
      (when (and fixed-org-data create-org-link)
        (storage-http (method-for-link create-org-link) (relative-href create-org-link)
          {:headers (headers-for-link create-org-link)
           :json-params (cljs->json fixed-org-data)}
          (fn [response]
            (callback response)))))))

;; Board/section

(defn get-board [board-link callback]
  (when board-link
    (storage-http (method-for-link board-link) (relative-href board-link)
      {:headers (headers-for-link board-link)}
      (fn [{:keys [status body success]}]
        (callback status body success)))))

(defn patch-board [board-patch-link data note callback]
  (when (and board-patch-link data)
    (let [board-data (select-keys data [:name :slug :access :slack-mirror :authors :viewers :private-notifications])
          with-personal-note (assoc board-data :note note)
          json-data (cljs->json with-personal-note)]
      (storage-http (method-for-link board-patch-link) (relative-href board-patch-link)
        {:json-params json-data
         :headers (headers-for-link board-patch-link)}
        (fn [{:keys [success body status]}]
          (callback success body status))))))

(defn create-board [create-board-link board-data note callback]
  (when create-board-link
    (let [fixed-board-data (select-keys board-data board-allowed-keys)
          fixed-entries (map #(select-keys % (conj entry-allowed-keys :uuid :secure-uuid)) (:entries board-data))
          with-entries (if (pos? (count fixed-entries))
                         (assoc fixed-board-data :entries fixed-entries)
                         fixed-board-data)
          with-personal-note (assoc with-entries :note note)]
      (when (and (:name fixed-board-data) create-board-link)
        (storage-http (method-for-link create-board-link) (relative-href create-board-link)
          {:headers (headers-for-link create-board-link)
           :json-params (cljs->json with-personal-note)}
          callback)))))

(defn pre-flight-section-check [pre-flight-link section-slug section-name callback]
  (when (and pre-flight-link
             section-name)
    (storage-http (method-for-link pre-flight-link) (relative-href pre-flight-link)
     {:headers (headers-for-link pre-flight-link)
      :json-params (cljs->json {:name section-name
                                :exclude (vec (remove nil? [section-slug]))
                                :pre-flight true})}
     callback)))

(defn delete-board [delete-board-link board-slug callback]
  (when (and delete-board-link board-slug)
    (storage-http (method-for-link delete-board-link) (relative-href delete-board-link)
      {:headers (headers-for-link delete-board-link)}
      (fn [{:keys [status success body]}]
        (callback status success body)))))

(defn remove-user-from-private-board
  [remove-user-link callback]
  (when remove-user-link
    (storage-http (method-for-link remove-user-link) (relative-href remove-user-link)
     {:headers (headers-for-link remove-user-link)}
     (fn [{:keys [status success body]}]
      (callback status success body)))))

;; All Posts

(defn get-all-posts [activity-link from callback]
  (when activity-link
    (let [href (relative-href activity-link)
          final-href (if from
                       (str href "?start=" from "&direction=around")
                       href)]
      (storage-http (method-for-link activity-link) final-href
        {:headers (headers-for-link activity-link)}
        callback))))

(defn load-more-all-posts [more-link direction callback]
  (when (and more-link direction)
    (storage-http (method-for-link more-link) (relative-href more-link)
      {:headers (headers-for-link more-link)}
      callback)))

;; Auth

(defn auth-with-email [auth-link email pswd callback]
  (when (and auth-link email pswd)
    (auth-http (method-for-link auth-link) (relative-href auth-link)
      {:basic-auth {
        :username email
        :password pswd}
       :headers (headers-for-link auth-link)}
      (fn [{:keys [success body status]}]
        (callback success body status)))))

(defn auth-with-token [auth-link token callback]
  (when (and auth-link token)
    (auth-http (method-for-link auth-link) (relative-href auth-link)
      {:headers (merge (headers-for-link auth-link)
                 {; required by Chrome
                  "Access-Control-Allow-Headers" "Content-Type, Authorization"
                  "Authorization" (str "Bearer " token)})}
      (fn [{:keys [success body status]}]
        (callback success body status)))))

(defn confirm-invitation [auth-link token callback]
  (when (and auth-link token)
    (auth-http (method-for-link auth-link) (relative-href auth-link)
      {:headers (merge (headers-for-link auth-link)
                       {; required by Chrome
                        "Access-Control-Allow-Headers" "Content-Type, Authorization"
                        "Authorization" (str "Bearer " token)})}
      (fn [{:keys [status body success]}]
        (utils/after 100 #(callback status body success))))))

;; Signup

(defn signup-with-email [auth-link first-name last-name email pswd callback]
  (when (and auth-link first-name last-name email pswd)
    (auth-http (method-for-link auth-link) (relative-href auth-link)
      {:json-params {:first-name first-name
                     :last-name last-name
                     :email email
                     :password pswd}
       :headers (headers-for-link auth-link)}
      (fn [{:keys [success body status]}]
        (callback success body status)))))

;; Team(s)

(defn get-teams [enumerate-link callback]
  (when enumerate-link
    (auth-http (method-for-link enumerate-link) (relative-href enumerate-link)
      {:headers (headers-for-link enumerate-link)}
      callback)))

(defn get-team [team-link callback]
  (when team-link
    (auth-http (method-for-link team-link) (relative-href team-link)
      {:headers (headers-for-link team-link)}
      callback)))

(defn enumerate-channels [enumerate-link callback]
  (when enumerate-link
    (auth-http (method-for-link enumerate-link) (relative-href enumerate-link)
               {:headers (headers-for-link enumerate-link)}
               callback)))

(defn patch-team [team-patch-link team-id new-team-data callback]
  (when (and team-patch-link team-id new-team-data)
    (auth-http (method-for-link team-patch-link) (relative-href team-patch-link)
      {:headers (headers-for-link team-patch-link)
       :json-params (cljs->json new-team-data)}
      callback)))

(defn send-invitation
  "Give a user email and type of user send an invitation to the team.
   If the team has only one company, checked via API entry point links, send the company name of that.
   Add the company's logo and its size if possible."
  [invitation-link api-entry-point-links invited-user invite-from user-type first-name last-name note callback]
  (when (and invitation-link api-entry-point-links invited-user invite-from user-type)
    (let [org-data (dispatcher/org-data)
          companies (count (filter #(= (:rel %) "company") api-entry-point-links))
          json-params {:first-name first-name
                       :last-name last-name
                       :note note
                       :admin (= user-type :admin)}
          with-invited-user (if (= invite-from "slack")
                              (merge
                               json-params
                               {:slack-id (:slack-id invited-user)
                                :slack-org-id (:slack-org-id invited-user)})
                              (assoc json-params :email invited-user))
          with-company-name (merge with-invited-user {:org-name (:name org-data)
                                                      :logo-url (:logo-url org-data)
                                                      :logo-width (:logo-width org-data)
                                                      :logo-height (:logo-height org-data)})]
      (auth-http (method-for-link invitation-link) (relative-href invitation-link)
        {:json-params (cljs->json with-company-name)
         :headers (headers-for-link invitation-link)}
        callback))))

(defn add-admin [add-admin-link user callback]
  (when add-admin-link
    (auth-http (method-for-link add-admin-link) (relative-href add-admin-link)
      {:headers (headers-for-link add-admin-link)}
      callback)))

(defn remove-admin [remove-admin-link user callback]
  (when remove-admin-link
    (auth-http (method-for-link remove-admin-link) (relative-href remove-admin-link)
      {:headers (headers-for-link remove-admin-link)}
      callback)))

;; User

(defn user-action [action-link payload callback]
  (when action-link
    (let [headers {:headers (headers-for-link action-link)}
          with-payload (if payload
                          (assoc headers :json-params payload)
                          headers)]
      (auth-http (method-for-link action-link) (relative-href action-link)
        with-payload
        callback))))

(defn collect-password [update-link pswd callback]
  (when (and update-link pswd)
    (auth-http (method-for-link update-link) (relative-href update-link)
      {:json-params {
        :password pswd}
       :headers (headers-for-link update-link)}
      (fn [{:keys [status body success]}]
        (utils/after 100 #(callback status body success))))))

(defn get-current-user [user-link callback]
  (when user-link
    (auth-http (method-for-link user-link) (relative-href user-link)
      {:headers (headers-for-link user-link)}
      (fn [{:keys [status body success]}]
        (callback body)))))

(defn patch-user-profile [user-update-link new-user-data callback]
  (when (and user-update-link
             (map? new-user-data))
    (let [without-email (dissoc new-user-data :email)
          safe-new-user-data (select-keys without-email user-allowed-keys)]
      (auth-http (method-for-link user-update-link) (relative-href user-update-link)
        {:headers (headers-for-link user-update-link)
         :json-params (cljs->json safe-new-user-data)}
         (fn [{:keys [status body success]}]
           (callback status body success))))))

(defn refresh-slack-user [refresh-link callback]
  (when refresh-link
    (auth-http (method-for-link refresh-link) (relative-href refresh-link)
      {:headers (headers-for-link refresh-link)}
      (fn [{:keys [status body success]}]
        (callback status body success)))))

(defn add-author
  "Given a user-id add him as an author to the current org.
  Refresh the user list and the org-data when finished."
  [add-author-link user-id callback]
  (when add-author-link
    (storage-http (method-for-link add-author-link) (relative-href add-author-link)
      {:headers (headers-for-link add-author-link)
       :body user-id}
      callback)))

(defn remove-author
  "Given a map containing :user-id and :links, remove the user as an author using the `remove` link.
  Refresh the org data when finished."
  [remove-author-link user-author callback]
  (when remove-author-link
    (storage-http (method-for-link remove-author-link) (relative-href remove-author-link)
      {:headers (headers-for-link remove-author-link)}
      callback)))

(defn password-reset
  [reset-link email callback]
  (when (and reset-link email)
    (auth-http (method-for-link reset-link) (relative-href reset-link)
      {:headers (headers-for-link reset-link)
       :body email}
      (fn [{:keys [status success body]}]
        (callback status)))))

;; Interactions

(defn get-comments [comments-link callback]
  (when comments-link
    (interaction-http (method-for-link comments-link) (relative-href comments-link)
      {:headers (headers-for-link comments-link)}
      callback)))

(defn add-comment [add-comment-link comment-body callback]
  (when (and add-comment-link comment-body)
    (let [json-data (cljs->json {:body comment-body})]
      (interaction-http (method-for-link add-comment-link) (relative-href add-comment-link)
        {:headers (headers-for-link add-comment-link)
         :json-params json-data}
        callback))))

(defn delete-comment
  [delete-comment-link callback]
  (when delete-comment-link
    (interaction-http (method-for-link delete-comment-link) (relative-href delete-comment-link)
      {:headers (headers-for-link delete-comment-link)}
      callback)))

(defn patch-comment
  [patch-comment-link new-data callback]
  (when (and patch-comment-link new-data)
    (let [json-data (cljs->json {:body new-data})]
      (interaction-http (method-for-link patch-comment-link) (relative-href patch-comment-link)
        {:headers (headers-for-link patch-comment-link)
         :json-params json-data}
        callback))))

(defn toggle-reaction
  [reaction-link callback]
  (when reaction-link
    (interaction-http (method-for-link reaction-link) (relative-href reaction-link)
      {:headers (headers-for-link reaction-link)}
      callback)))

(defn react-from-picker
  "Given the link to react with an arbitrary emoji and the emoji, post it to the interaction service"
  [react-link emoji callback]
  (when react-link
    (interaction-http (method-for-link react-link) (relative-href react-link)
      {:headers (headers-for-link react-link)
       :body emoji}
      callback)))

;; Entry

(defn get-entry
  [entry-link callback]
  (when entry-link
    (storage-http (method-for-link entry-link) (relative-href entry-link)
      {:headers (headers-for-link entry-link)}
      callback)))

(defn create-entry
  [create-entry-link entry-data edit-key callback]
  (when (and create-entry-link entry-data)
    (let [cleaned-entry-data (select-keys entry-data entry-allowed-keys)]
      (storage-http (method-for-link create-entry-link) (relative-href create-entry-link)
        {:headers (headers-for-link create-entry-link)
         :json-params (cljs->json cleaned-entry-data)}
        (partial callback entry-data edit-key)))))

(defn publish-entry
  [publish-entry-link entry-data callback]
  (when (and entry-data
             publish-entry-link)
    (let [cleaned-entry-data (select-keys entry-data entry-allowed-keys)]
      (storage-http (method-for-link publish-entry-link) (relative-href publish-entry-link)
        {:headers (headers-for-link publish-entry-link)
         :json-params (cljs->json cleaned-entry-data)}
        callback))))

(defn patch-entry
  [patch-entry-link entry-data edit-key callback]
  (when patch-entry-link
    (let [cleaned-entry-data (select-keys entry-data entry-allowed-keys)]
      (storage-http (method-for-link patch-entry-link) (relative-href patch-entry-link)
        {:headers (headers-for-link patch-entry-link)
         :json-params (cljs->json cleaned-entry-data)}
        (partial callback entry-data edit-key)))))

(defn delete-entry [delete-entry-link callback]
  (when delete-entry-link
    (storage-http (method-for-link delete-entry-link) (relative-href delete-entry-link)
      {:headers (headers-for-link delete-entry-link)}
      callback)))

(defn revert-entry
  [revert-entry-link entry-data callback]
  (when (and revert-entry-link entry-data)
    (let [cleaned-entry-data (select-keys entry-data [:revision-id])]
      (storage-http (method-for-link revert-entry-link) (relative-href revert-entry-link)
        {:headers (headers-for-link revert-entry-link)
         :json-params (cljs->json cleaned-entry-data)}
        callback))))

(defn share-entry [share-link share-data callback]
  (when (and share-link share-data)
    (storage-http (method-for-link share-link) (relative-href share-link)
      {:headers (headers-for-link share-link)
       :json-params (cljs->json share-data)}
      callback)))

(defn get-secure-entry [org-slug secure-activity-id callback]
  (when secure-activity-id
    (let [activity-link {:href (str "/orgs/" org-slug "/entries/" secure-activity-id)
                         :method "GET"
                         :rel ""
                         :accept "application/vnd.open-company.entry.v1+json"}]
      (storage-http (method-for-link activity-link) (relative-href activity-link)
        {:headers (headers-for-link activity-link)}
        callback))))

;; Search

(defn query
  [org-uuid search-query callback]
  (when search-query
    (let [search-link {:href (str "/search/?q=" search-query "&org=" org-uuid)
                       :content-type (content-type "search")
                       :method "GET" :rel ""}]
      (search-http (method-for-link search-link) (relative-href search-link)
       {:headers (headers-for-link search-link)}
       (fn [{:keys [status success body]}]
         (callback {:query search-query
                    :success success
                    :error (when-not success body)
                    :body (when (seq body) (json->cljs body))}))))))

;; WRT

(defn request-reads-data [item-id]
  (when (seq item-id)
    (ws-cc/who-read item-id)))

(defn request-reads-count [item-ids]
  (when (seq item-ids)
    (ws-cc/who-read-count item-ids)))