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
            [open-company-web.data.finances :as finances-data]))


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

(defn- link-for
  ([links rel] (some #(if (= (:rel %) rel) %) links))
  ([links rel year period]
    (let [pred #(if (and
                      (= (:rel %) rel)
                      (= (str (:year %)) year)
                      (= (:period %) period)) %)]
      (some pred links))))

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

(defn save-or-create-company [ticker data]
  (when (and ticker data)
    (let [company-data (dissoc data :headcount :finances :compensation :links)
          json-data (cljs->json company-data)
          company-link (link-for (:links data) "update" nil nil)]
      (api-put (:href report-link)
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