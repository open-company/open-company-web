(ns open-company-web.api
  (:require-macros [cljs.core.async.macros :refer (go)]
                   [if-let.core :refer (when-let*)])
  (:require [cljs.core.async :as async :refer (<!)]
            [cljs-http.client :as http]
            [open-company-web.dispatcher :as dispatcher]
            [cognitect.transit :as t]
            [clojure.walk :refer (keywordize-keys stringify-keys)]
            [clojure.string :as s]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.jwt :as j]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.raven :as sentry]
            [open-company-web.caches :refer (revisions)]))

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
    (-> (merge {:with-credentials? false} params)
        (update :headers merge {"Access-Control-Allow-Headers" "Content-Type, Authorization"
                                "Authorization" (str "Bearer " jwt)}))
    params))

(defn refresh-jwt [refresh-url]
  (http/get (str ls/auth-server-domain refresh-url) (complete-params {})))

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
  (let [jwt (j/jwt)]
    (go
      (let [refresh-url (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "refresh")]
        (when (and jwt (j/expired?) refresh-url)
          (let [res (<! (refresh-jwt (:href refresh-url)))]
            (if (:success res)
              (update-jwt-cookie! (:body res))
              (dispatcher/dispatch! [:logout])))))

      (let [{:keys [status body] :as response} (<! (method (str endpoint path) (complete-params params)))]
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
    {:headers {
      ; required by Chrome
      "Access-Control-Allow-Headers" "Content-Type"
      ; custom content type
      "content-type" "text/plain"}}
    (fn [_])))

(defn get-entry-point []
  (api-get "/" nil (fn [response]
                     (let [body (if (:success response) (:body response) {})]
                       (dispatcher/dispatch! [:entry body])))))

(defn get-subscription [company-uuid]
  (pay-get (str "/subscriptions/" company-uuid)
           nil
           (fn [response]
             (let [body (if (:success response) (:body response) {})]
               (dispatcher/dispatch! [:subscription body])))))

(defn get-companies []
  (api-get "/companies" nil #(dispatch-body :companies %)))

(defn get-company [slug]
  (when slug
    (api-get (str "/companies/" slug)
             nil
             (fn [{:keys [success body status]}]
               (dispatcher/dispatch! [:company {:slug slug
                                                :success success
                                                :status status
                                                :body (when success (json->cljs body))}])))))

(defn post-company [data]
  (api-post "/companies" {:json-params data} #(dispatch-body :company-created %)))

(defn patch-company [slug data]
  (when data
    (let [company-data (dissoc data :links :read-only :revisions :su-list :su-list-loaded :revisions :section)
          json-data (cljs->json company-data)
          links (:links (dispatcher/company-data))
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
    {:headers {"content-type" "application/json"}}
    (fn [response]
      (let [body (if (:success response) (:body response) false)]
        (dispatcher/dispatch! [:auth-settings body])))))

(def section-private-keys [:section
                           :revisions
                           :author
                           :links
                           :loading
                           :as-of
                           :read-only
                           :revisions-cache
                           :title-placeholder
                           :body-placeholder
                           :oc-editing
                           :revisions-data
                           :new
                           :placeholder
                           :was-archived])

(defn save-or-create-section [section-data]
  (when section-data
    (let [links (:links section-data)
          slug (router/current-company-slug)
          section (keyword (:section section-data))
          cleaned-section-data (apply dissoc section-data section-private-keys)
          json-data (cljs->json cleaned-section-data)
          section-link (utils/link-for links (if (:new section-data) "create" "update") "PUT")]
      (api-put (:href section-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type section-link)
          }}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})]
            (dispatcher/dispatch! [:section {:body body :section section :slug (keyword slug)}])))))))

(defn load-revisions [slug topic revisions-link]
  (when (and topic revisions-link)
    (api-get (:href revisions-link)
      {:headers {
        ; required by Chrome
          "Access-Control-Allow-Headers" "Content-Type, Authorization"
          ; custom content type
          "content-type" (:type revisions-link)
          "accept" (:type revisions-link)}}
      (fn [{:keys [status body success]}]
        (dispatcher/dispatch! [:revisions-loaded {:slug slug :topic topic :revisions (if success (json->cljs body) {})}])))))

(defn partial-update-section
  "PATCH a section, dispatching the results with a `:section` action."
  ([section section-data]
  (when (and section section-data)
    (let [slug (keyword (router/current-company-slug))
          section-kw (keyword section)
          partial-update-link (utils/link-for (:links section-data) "partial-update" "PATCH")
          cleaned-section-data (apply dissoc section-data section-private-keys)
          json-data (cljs->json cleaned-section-data)]
      (api-patch (:href partial-update-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type partial-update-link)
          }}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})]
            (load-revisions slug section (utils/link-for (:links body) "revisions")))))))))

(defn load-revision
  [revision slug section]
    (when revision
      (swap! revisions assoc-in [slug (keyword section) (:created-at revision)] :loading)
      (api-get (:href revision)
        {:headers {
          ; required by Chrome
          "Access-Control-Allow-Headers" "Content-Type"
          ; custom content type
          "content-type" (:type revision)}}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})
                dispatch-body {:body body
                               :as-of (:created-at revision)
                               :section (keyword section)
                               :slug (keyword slug)}]
            (dispatcher/dispatch! [:revision dispatch-body]))))))

(defn update-finances-data[finances-data]
  (when finances-data
    (let [links (:links finances-data)
          slug (router/current-company-slug)
          data {:data (map #(dissoc % :burn-rate :runway :value :new :read-only :revisions-data) (:data finances-data))}
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
                dispatch-body {:body (merge {:section :finances} body)
                               :section :finances
                               :slug (keyword slug)}]
            (dispatcher/dispatch! [:section dispatch-body])))))))

(defn patch-sections [sections & [new-section section-name]]
  (when sections
    (let [slug (keyword (router/current-company-slug))
          company-data (dispatcher/company-data)
          company-patch-link (utils/link-for (:links company-data) "partial-update" "PATCH")
          payload (if (and new-section section-name)
                    {:sections sections
                     (keyword section-name) new-section}
                    {:sections sections})
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
    (let [slug (keyword (router/current-company-slug))
          company-data (dispatcher/company-data)
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

(defn remove-section [section-name]
  (when (and section-name)
    (let [slug (keyword (router/current-company-slug))
          company-data (dispatcher/company-data)
          sections (:sections company-data)
          new-sections (apply merge (map (fn [[k v]]
                                           {k (utils/vec-dissoc v section-name)})
                                         sections))]
      (patch-sections new-sections))))

(def new-sections-requested (atom false))

(defn get-new-sections [& [force-load]]
  "Load new sections, avoid to start multiple request or reload it if it's already loading or loaded.
   It's possible to force the load passing an optional boolean parameter."
  (when (or force-load (not @new-sections-requested))
    (reset! new-sections-requested true)
    (let [slug (keyword (router/current-company-slug))
          company-data (dispatcher/company-data)
          links (:links company-data)
          add-section-link (utils/link-for links "section-list" "GET")]
      (when add-section-link
        (api-get (:href add-section-link)
          { :headers {
              ; required by Chrome
              "Access-Control-Allow-Headers" "Content-Type"
              ; custom content type
              "content-type" (:type add-section-link)}}
          (fn [{:keys [success body]}]
            (when (not success)
              (reset! new-sections-requested false))
            (let [fixed-body (if body (json->cljs body) {})]
              (dispatcher/dispatch! [:new-section {:response fixed-body :slug slug}]))))))))

(defn share-stakeholder-update [{:keys [email slack]}]
  (let [slug         (keyword (router/current-company-slug))
        company-data (dispatcher/company-data)
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
  (let [slug (keyword (router/current-company-slug))
        company-data (dispatcher/company-data)
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
            (reset! new-sections-requested false))
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
    (let [email-links (:links (:email (:auth-settings @dispatcher/app-state)))
          auth-url (utils/link-for email-links "authenticate" "GET")]
      (auth-get (:href auth-url)
        {:basic-auth {
          :username email
          :password pswd}
         :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type auth-url)}}
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
    (let [email-links (:links (:email (:auth-settings @dispatcher/app-state)))
          auth-url (utils/link-for email-links "create" "POST")]
      (auth-post (:href auth-url)
        {:json-params {:first-name first-name
                       :last-name last-name
                       :email email
                       :password pswd}
         :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type auth-url)}}
        (fn [{:keys [success body status]}]
         (if success
            (dispatcher/dispatch! [:signup-with-email/success body])
            (dispatcher/dispatch! [:signup-with-email/failed status])))))))

(defn enumerate-users []
  (let [enumerate-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "users" "GET")]
    (auth-get (:href enumerate-link)
      {:headers {
        ; required by Chrome
        "Access-Control-Allow-Headers" "Content-Type"
        ; custom content type
        "content-type" (:type enumerate-link)}}
      (fn [{:keys [success body status]}]
        (let [fixed-body (if success (json->cljs body) {})]
          (if success
            (dispatcher/dispatch! [:enumerate-users/success (-> fixed-body :collection :users)])))))))

(defn enumerate-channels []
  (let [enumerate-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "channels" "GET")]
    (auth-get (:href enumerate-link)
      {:headers {
        ; required by Chrome
        "Access-Control-Allow-Headers" "Content-Type"
        ; custom content type
        "content-type" (:type enumerate-link)}}
      (fn [{:keys [success body status]}]
        (let [fixed-body (if success (json->cljs body) {})]
          (if success
            (dispatcher/dispatch! [:enumerate-channels/success (-> fixed-body :collection :channels)])))))))

(defn send-invitation [email]
  (let [company-data (dispatcher/company-data)]
    (when (and email company-data)
      (let [invitation-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "invite")]
        (auth-post (:href invitation-link)
          {:json-params {:email email
                         :company-name (:name company-data)
                         :logo (or (:logo company-data) "")}
           :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type invitation-link)
            "accept" (:type invitation-link)}}
          (fn [{:keys [success body status]}]
            (if success
              (dispatcher/dispatch! [:invite-by-email/success (:users (:collection body))])
              (dispatcher/dispatch! [:invite-by-email/failed]))))))))

(defn user-invitation-action [action-link payload]
  (when (and action-link)
    (let [auth-req (case (:method action-link)
                      "POST" auth-post
                      "PUT" auth-put
                      "PATCH" auth-patch
                      "DELETE" auth-delete
                      auth-get) ; default to GET
          headers {:headers {
                    ; required by Chrome
                    "Access-Control-Allow-Headers" "Content-Type"
                    ; custom content type
                    "content-type" (:type action-link)
                    "accept" (:type action-link)}}
          with-payload (if payload
                          (assoc headers :json-params payload)
                          headers)]
      (auth-req (:href action-link)
        with-payload
        (fn [{:keys [status success body]}]
          (dispatcher/dispatch! [:user-invitation-action/complete]))))))

(defn confirm-invitation [token]
  (let [auth-link (utils/link-for (:links (:email (:auth-settings @dispatcher/app-state))) "authenticate")]
    (when (and token auth-link)
      (auth-get (:href auth-link)
        {:headers {
          ; required by Chrome
          "Access-Control-Allow-Headers" "Content-Type, Authorization"
          ; custom content type
          "content-type" (:type auth-link)
          "accept" (:type auth-link)
          ; pass the token as Authorization
          "Authorization" (str "Bearer " token)}}
        (fn [{:keys [status body success]}]
          (when success
            (update-jwt-cookie! body)
            (dispatcher/dispatch! [:jwt (j/get-contents)]))
          (utils/after 100 #(dispatcher/dispatch! [:invitation-confirmed status])))))))

(defn collect-name-password [firstname lastname pswd]
  (let [update-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "partial-update" "PATCH")]
    (when (and (or firstname lastname) pswd update-link)
      (auth-patch (:href update-link)
        {:json-params {
          :first-name firstname
          :last-name lastname
          :password pswd
         }
         :headers {
          ; required by Chrome
          "Access-Control-Allow-Headers" "Content-Type, Authorization"
          ; custom content type
          "content-type" (:type update-link)
          "accept" (:type update-link)}}
        (fn [{:keys [status body success]}]
          (when success
            (update-jwt-cookie! body)
            (dispatcher/dispatch! [:jwt (j/get-contents)]))
          (utils/after 100 #(dispatcher/dispatch! [:collect-name-pswd-finish status])))))))