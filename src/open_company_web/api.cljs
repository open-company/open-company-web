(ns open-company-web.api
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :as async :refer (<!)]
            [cljs-http.client :as http]
            [clojure.string :refer (join)]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-flux.dispatcher :as flux]
            [cognitect.transit :as t]
            [clojure.walk :refer (keywordize-keys stringify-keys)]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.jwt :as j]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.caches :refer (revisions)]))


(def ^:private api-endpoint ls/api-server-domain)

(def ^:private auth-endpoint ls/auth-server-domain)

(defn- content-type [type]
  (str "application/vnd.open-company." type ".v1+json"))

(defn- json->cljs [json]
  (let [reader (t/reader :json)]
    (keywordize-keys (t/read reader json))))

(defn- cljs->json [coll]
  (let [stringified-coll (stringify-keys coll)]
    (clj->js stringified-coll)))

(defn- req [endpoint method path params on-complete]
  (go
    (let [jwt (j/jwt)
          allow-params (assoc-in params [:headers "Access-Control-Allow-Headers"] "Content-Type, Authorization")
          jwt-params (when jwt (assoc-in allow-params [:headers "Authorization"] (str "Bearer " jwt)))
          initial-data {:with-credentials? false}
          data (when jwt-params (merge initial-data jwt-params))
          response (<! (method (str endpoint path) data))]
      (on-complete response))))

(def ^:private api-get (partial req api-endpoint http/get))
(def ^:private api-post (partial req api-endpoint http/post))
(def ^:private api-put (partial req api-endpoint http/put))
(def ^:private api-patch (partial req api-endpoint http/patch))

(def ^:private auth-get (partial req auth-endpoint http/get))

(defn get-companies []
  (api-get "/companies" nil (fn [response]
      (let [body (if (:success response) (json->cljs (:body response)) {})]
        (flux/dispatch dispatcher/companies body)))))

(defn get-company [slug]
  (when slug
    (api-get (str "/companies/" slug) nil
      (fn [response]
        (let [body (if (:success response) (json->cljs (:body response)) {})]
          (flux/dispatch dispatcher/company body))))))

(defn save-or-create-company[data]
  (when data
    (let [company-data (dissoc data :headcount :finances :compensation :links)
          json-data (cljs->json company-data)
          company-link (utils/link-for (:links data) "update")]
      (api-put (:href company-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type company-link)
          }}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})]
            (flux/dispatch dispatcher/company body)))))))

(defn get-auth-settings []
  (auth-get "/auth-settings" {
        :headers {
          "content-type" "application/json"
        }
      }
  (fn [response]
    (let [body (if (:success response) (:body response) {})]
      (flux/dispatch dispatcher/auth-settings body)))))

(defn save-or-create-section[section-data]
  (when section-data
    (let [links (:links section-data)
          slug (:slug @router/path)
          section (keyword (:section section-data))
          section-data (dissoc section-data :section
                                            :revisions
                                            :updated-at
                                            :author
                                            :links
                                            :loading
                                            :as-of
                                            :read-only
                                            :revisions-cache)
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
            (flux/dispatch dispatcher/section {:body body :section section :slug (keyword slug)})))))))

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
                               :section section
                               :slug (keyword slug)}]
            (flux/dispatch dispatcher/revision dispatch-body))))))

(defn update-finances-data[finances-data]
  (when finances-data
    (let [links (:links finances-data)
          slug (:slug @router/path)
          data {:data (map #(dissoc % :burn-rate :runway) (:data finances-data))}
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
            (flux/dispatch dispatcher/section dispatch-body)))))))

(defn patch-section-notes [notes-data links section]
  (when notes-data
    (let [slug (:slug @router/path)
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
            (flux/dispatch dispatcher/section dispatch-body)))))))

(defn patch-sections [sections]
  (when sections
    (let [slug (keyword (:slug @router/path))
          company-data (slug @dispatcher/app-state)
          company-patch-link (utils/link-for (:links company-data) "partial-update" "PATCH")
          json-data (cljs->json {:sections sections})]
      (api-patch (:href company-patch-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type company-patch-link)}}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})]
            (flux/dispatch dispatcher/company body)))))))

(defn remove-section [section-name]
  (when (and section-name)
    (let [slug (keyword (:slug @router/path))
          company-data (slug @dispatcher/app-state)
          sections (:sections company-data)
          new-sections (apply merge (map (fn [[k v]]
                                           {k (utils/vec-dissoc v section-name)})
                                         sections))]
      (patch-sections new-sections))))

(defn get-new-sections []
  (let [slug (keyword (:slug @router/path))
        company-data (slug @dispatcher/app-state)
        links (:links company-data)
        add-section-link (utils/link-for links "section-list" "GET")]
    (api-get (:href add-section-link)
      { :headers {
          ; required by Chrome
          "Access-Control-Allow-Headers" "Content-Type"
          ; custom content type
          "content-type" (:type add-section-link)}}
      (fn [response]
        (let [body (if (:success response) (json->cljs (:body response)) {})]
          (flux/dispatch dispatcher/new-section {:response body :slug slug}))))))