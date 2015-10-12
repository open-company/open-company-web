(ns open-company-web.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async :refer [<!]]
            [cljs-http.client :as http]
            [clojure.string :refer [join]]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-flux.dispatcher :as flux]
            [cognitect.transit :as t]
            [clojure.walk :refer [keywordize-keys stringify-keys]]
            [open-company-web.local-settings :as ls]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))


(def ^:private endpoint ls/api-server-domain)

(defn- content-type [type]
  (str "application/vnd.open-company." type ".v1+json"))

(defn- json->cljs [json]
  (let [reader (t/reader :json)]
    (keywordize-keys (t/read reader json))))

(defn- cljs->json [coll]
  (let [stringified-coll (stringify-keys coll)]
    (clj->js stringified-coll)))

(defn- req [method path params on-complete]
  (go
    (let [data {:with-credentials? false}
          data (when params (merge data params))
          response (<! (method (str endpoint path) data))]
      (on-complete response))))

(def ^:private api-get (partial req http/get))
(def ^:private api-post (partial req http/post))
(def ^:private api-put (partial req http/put))
(def ^:private api-patch (partial req http/patch))

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

(defn save-or-create-section[section-data]
  (when section-data
    (let [links (:links section-data)
          slug (:slug @router/path)
          section (:section section-data)
          section-data (dissoc section-data :section :revisions :updated-at :author :links :sorter)
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
  ([revision slug section]
   (load-revision revision slug section false false))
  ([revision slug section read-only notes?]
    (when revision
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
                               :slug (keyword slug)
                               :read-only read-only}
                dispatch-body (if notes? (merge {:notes true} dispatch-body) dispatch-body)]
            (flux/dispatch dispatcher/revision dispatch-body)))))))

(defn update-finances-data[finances-data]
  (when finances-data
    (let [links (:links finances-data)
          slug (:slug @router/path)
          data {:data (map #(dissoc % :burn-rate :runway) (:data finances-data))}
          json-data (cljs->json data)
          finances-link (utils/link-for links "update" "PATCH")]
      (api-patch (:href finances-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type finances-link)}}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})
                dispatch-body {:body (merge {:section :finances :sorter (:updated-at body)} body)
                               :section :finances
                               :slug (keyword slug)}]
            (flux/dispatch dispatcher/section dispatch-body)))))))


