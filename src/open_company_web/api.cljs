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
  (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))

(defn- req [endpoint method path params on-complete]
  (let [jwt (j/jwt)]
    (go
      (let [refresh-url (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "refresh-url")]
        (when (and jwt (j/expired?) refresh-url)
          (let [res (<! (refresh-jwt refresh-url))]
            (if (:success res)
              (update-jwt-cookie! (:body res))
              (dispatcher/dispatch! [:logout])))))

      (let [response (<! (method (str endpoint path) (complete-params params)))]
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
      (let [body (if (:success response) (:body response) {})]
        (dispatcher/dispatch! [:auth-settings body])))))

(defn save-or-create-section [section-data]
  (when section-data
    (let [links (:links section-data)
          slug (router/current-company-slug)
          section (keyword (:section section-data))
          section-data (dissoc section-data :section
                                            :revisions
                                            :updated-at
                                            :author
                                            :links
                                            :loading
                                            :as-of
                                            :read-only
                                            :revisions-cache
                                            :title-placeholder
                                            :body-placeholder
                                            :oc-editing)
          json-data (cljs->json section-data)
          section-link (utils/link-for links "update" "PUT")]
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

(defn get-section [slug section-name]
  (when slug
    (api-get (str "/companies/" slug "/" (name section-name))
      nil
      (fn [{:keys [response success status]}]
        (let [section-kw (keyword section-name)
              body (if success (json->cljs (:body response)) {})
              dispatch-body {:slug slug
                             :section section-kw
                             :body body
                             :status status
                             :success success}]
            (dispatcher/dispatch! [:section dispatch-body]))))))

(defn partial-update-section
  "PATCH a section, dispatching the results with a `:section` action, merging the response first with
  the optional preserve map argument."

  ([section partial-section-data] (partial-update-section section partial-section-data {}))
  
  ([section partial-section-data preserve]
  (when (and section partial-section-data)
    (let [slug (keyword (router/current-company-slug))
          section-kw (keyword section)
          company-data (dispatcher/company-data)
          section-data (get company-data section-kw)
          clean-partial-section-data (dissoc partial-section-data :as-of :icon :section)
          json-data (cljs->json clean-partial-section-data)
          partial-update-link (utils/link-for (:links section-data) "partial-update" "PATCH")]
      (api-patch (:href partial-update-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type partial-update-link)
          }}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})
                dispatch-body {:body (merge body preserve)
                               :section section-kw
                               :slug slug}]
            (dispatcher/dispatch! [:section dispatch-body]))))))))

(defn load-revision
  [revision slug section]
    (when revision
      (swap! revisions assoc-in [slug (keyword section) (:updated-at revision)] :loading)
      (api-get (:href revision)
        {:headers {
          ; required by Chrome
          "Access-Control-Allow-Headers" "Content-Type"
          ; custom content type
          "content-type" (:type revision)}}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})
                dispatch-body {:body body
                               :as-of (:updated-at revision)
                               :section (keyword section)
                               :slug (keyword slug)}]
            (dispatcher/dispatch! [:revision dispatch-body]))))))

(defn update-finances-data[finances-data]
  (when finances-data
    (let [links (:links finances-data)
          slug (router/current-company-slug)
          data {:data (map #(dissoc % :burn-rate :runway :value :new :read-only) (:data finances-data))}
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
            (let [fixed-body (if success (json->cljs body) {})]
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
  ([slug update-slug]
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
                            :response fixed-body}]
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
        (if success
          (dispatcher/dispatch! [:enumerate-users/success (:users (:collection body))]))))))

(defn send-invitation [email]
  (when email
    (let [invitation-link (utils/link-for (:links (:auth-settings @dispatcher/app-state)) "invite")]
      (auth-post (:href invitation-link)
        {:json-params {:email email}
         :headers {
          ; required by Chrome
          "Access-Control-Allow-Headers" "Content-Type"
          ; custom content type
          "content-type" (:type invitation-link)
          "accept" (:type invitation-link)}}
        (fn [{:keys [success body status]}]
          (if success
            (dispatcher/dispatch! [:invite-by-email/success (:users (:collection body))])
            (dispatcher/dispatch! [:invite-by-email/failed])))))))

(defn user-invitation-action [user-id action]
  (when (and user-id action)
    (when-let* [user-data (first (filter #(= (:user-id %) user-id) (:enumerate-user @dispatcher/app-state)))
                user-link (utils/link-for (:links user-data) action)]
      (let [auth-req (case (:method user-link)
                        "POST" auth-post
                        "PUT" auth-put
                        "PATCH" auth-patch
                        "DELETE" auth-delete
                        auth-get)]
        (auth-req (:href user-link)
          {:headers {
          ; required by Chrome
          "Access-Control-Allow-Headers" "Content-Type"
          ; custom content type
          "content-type" (:type user-link)
          "accept" (:type user-link)}}
          (fn [{:keys [status success body]}]
            (dispatcher/dispatch! [:user-invitation-action/complete])))))))