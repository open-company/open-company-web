(ns open-company-web.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async :refer [<!]]
            [cljs-http.client :as http]
            [clojure.string :refer [join]]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-flux.dispatcher :as flux]
            [cognitect.transit :as t]
            [clojure.walk :refer [keywordize-keys stringify-keys]]))

(def api-version "v1")

(def company-endpoint "companies")

(def endpoint (str "http://localhost:3000/" api-version "/" company-endpoint "/"))

(defn- content-type [type]
  (str "application/vnd.open-company." type "+json;version=1"))

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

(def apiget (partial req http/get))
(def apipost (partial req http/post))
(def apiput (partial req http/put))

(defn get-companies []
  (apiget "" nil (fn [response]
      (let [body (if (:success response) (json->cljs (:body response)) {})]
        (flux/dispatch dispatcher/companies body)))))

(defn get-company [symbol]
  (when symbol
    (apiget symbol nil
      (fn [response]
        (let [body (if (:success response) (json->cljs (:body response)) {})]
          (flux/dispatch dispatcher/company body))))))

(defn real-get-report [symbol year period]
  (apiget (str symbol "/" year "/" period) nil
    (fn [response]
      (let [body (if (:success response) (json->cljs (:body response)) {})]
        (flux/dispatch dispatcher/report body)))))

(defn get-report [symbol year period]
  (when (and symbol year period)
    (if (contains? @dispatcher/app-state (keyword symbol))
      ; load the report only
      (real-get-report symbol year period)
      ; load the company data before the report data
      (do
        (flux/register
          dispatcher/company
          (fn [response]
            (real-get-report symbol year period)))
        ; load company data
        (get-company symbol)))))

(defn save-or-create-report [symbol year period data]
  (when (and symbol year period data)
    (when (or (:headcount data) (:finances data) (:compensation data))
      (let [json-data (cljs->json (dissoc data :links))]
        (apiput
          (str symbol "/" year "/" period)
          { :json-params json-data
            :alternative-headers {
              ; required by Chrome
              "Access-Control-Allow-Headers" "Content-Type"
              ; custom content type
              "content-type" (content-type "report")
            }}
          (fn [response]
            (let [body (if (:success response) (json->cljs (:body response)) {})]
              (flux/dispatch dispatcher/report body))))))))

(defn save-or-create-company [symbol data]
  (when (and symbol data)
    (let [data (dissoc data :headcount)
          data (dissoc data :finances)
          data (dissoc data :compensation)
          data (dissoc data :links)
          json-data (cljs->json data)]
      (apiput symbol
        { :json-params json-data
          :alternative-headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (content-type "company")
          }}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})]
            (flux/dispatch dispatcher/company body)))))))
