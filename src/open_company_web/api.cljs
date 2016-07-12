(ns open-company-web.api
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :as async :refer (<!)]
            [cljs-http.client :as http]
            [clojure.string :refer (join)]
            [open-company-web.dispatcher :as dispatcher]
            [cognitect.transit :as t]
            [clojure.walk :refer (keywordize-keys stringify-keys)]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.jwt :as j]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.caches :refer (revisions)]))

(def ^:private api-endpoint ls/api-server-domain)

(def ^:private auth-endpoint ls/auth-server-domain)

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

(defn refresh-jwt []
  (js/console.info "Refreshing JWToken")
  (http/get (str auth-endpoint "/refresh-token") (complete-params {})))

(defn update-jwt-cookie! [jwt]
  (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))

(defn- req [endpoint method path params on-complete]
  (let [jwt (j/jwt)]
    (go
      (when (and jwt (j/expired?))
        (let [res (<! (refresh-jwt))]
          (if (:success res)
            (update-jwt-cookie! (-> res :body :jwt))
            (dispatcher/dispatch! [:logout]))))

      (let [response (<! (method (str endpoint path) (complete-params params)))]
        (on-complete response)))))

(def ^:private api-get (partial req api-endpoint http/get))
(def ^:private api-post (partial req api-endpoint http/post))
(def ^:private api-put (partial req api-endpoint http/put))
(def ^:private api-patch (partial req api-endpoint http/patch))

(def ^:private auth-get (partial req auth-endpoint http/get))

(defn dispatch-body [action response]
  (let [body (if (:success response) (json->cljs (:body response)) {})]
    (dispatcher/dispatch! [action body])))

(defn get-entry-point []
  (api-get "/" nil (fn [response]
                     (let [body (if (:success response) (:body response) {})]
                       (dispatcher/dispatch! [:entry body])))))

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
    (let [company-data (dissoc data :links :read-only :revisions :su-list :su-list-loaded)
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
  (auth-get "/auth-settings"
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

(defn partial-update-section [section partial-section-data]
  (when (and section partial-section-data)
    (let [slug (keyword (router/current-company-slug))
          section-kw (keyword section)
          company-data (dispatcher/company-data)
          section-data (get company-data section-kw)
          json-data (cljs->json partial-section-data)
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
                dispatch-body {:body body
                               :section section-kw
                               :slug slug}]
            (dispatcher/dispatch! [:section dispatch-body])))))))

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
          data {:data (map #(dissoc % :burn-rate :runway :avg-burn-rate :value :new :read-only) (:data finances-data))}
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

(defn patch-section-notes [notes-data links section]
  (when notes-data
    (let [slug (router/current-company-slug)
          clean-notes-data (dissoc notes-data :author :updated-at)
          json-data (cljs->json {:notes notes-data})
          section-link (utils/link-for links "partial-update" "PATCH")]
      (api-patch (:href section-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type section-link)}}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})
                dispatch-body {:body (merge {:section section} body)
                               :section section
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
            (dispatcher/dispatch! [:su-edit {:slug slug :su-slug (:slug fixed-body)}])))))))

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