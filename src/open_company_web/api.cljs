(ns open-company-web.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async :refer [<!]]
            [cljs-http.client :as http]
            [clojure.string :refer [join]]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-flux.dispatcher :as flux]
            [cognitect.transit :as t]
            [clojure.walk :refer [keywordize-keys stringify-keys]]))

(def endpoint "http://localhost:3000")

(defn- json->cljs [json]
  (let [reader (t/reader :json)]
    (keywordize-keys (t/read reader json))))

(defn- cljs->json [coll]
  (let [stringified-coll (stringify-keys coll)]
    (clj->js stringified-coll)))

(defn- req [method path params headers on-complete]
  (println headers)
  (go
    (let [data {:with-credentials? false}
          data (when params (merge data params))
          data (when headers (assoc data :heders headers))
          response (<! (method (str endpoint path) data))]
      (on-complete response))))

(def apiget (partial req http/get))
(def apipost (partial req http/post))
(def apiput (partial req http/put))

(defn get-companies []
  (apiget "/v1/companies/" nil nil (fn [response]
      (let [body (if (:success response) (json->cljs (:body response)) {})]
        (flux/dispatch dispatcher/companies body)))))

(defn get-company [ticker]
  (when symbol
    (apiget (str "/v1/companies/" ticker) nil nil
      (fn [response]
        (let [body (if (:success response) (json->cljs (:body response)) {})]
          (flux/dispatch dispatcher/company body))))))

(defn real-get-report [ticker year period]
  (apiget (str "/v1/companies/" ticker "/" year "/" period) nil nil
    (fn [response]
      (let [body (if (:success response) (json->cljs (:body response)) {})]
        (flux/dispatch dispatcher/report body)))))

(defn get-report [ticker year period]
  (when (and ticker year period)
    (if (contains? @dispatcher/app-state (keyword ticker))
      ; load the report only
      (real-get-report ticker year period)
      ; load the company data before the report data
      (do
        (flux/register
          dispatcher/company
          (fn [response]
            (real-get-report ticker year period)))
        ; load company data
        (get-company ticker)))))

(defn save-or-create-report [ticker year period data]
  (when (and ticker year period data)
    (when (or (:headcount data) (:finances data) (:compensation data))
      (let [clean-data (stringify-keys (dissoc data :links))
            json-data (cljs->json clean-data)]
        (println clean-data)
        (println "Going to save: " json-data)
        (apiput
          (str "/v1/companies/" ticker "/" year "/" period)
          {:json-params json-data} {
            "Access-Control-Allow-Headers" "Content-Type"
            "Content-Type" "application/vnd.open-company.report+json;version=1"
            "Accept-Charset" "utf-8"
          } (fn [response] (println response)))))))
