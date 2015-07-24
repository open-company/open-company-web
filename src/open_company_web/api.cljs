(ns open-company-web.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async :refer [<!]]
            [cljs-http.client :as http]
            [clojure.string :refer [join]]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-flux.dispatcher :as flux]
            [cognitect.transit :as t]
            [clojure.walk :refer [keywordize-keys]]))

(def endpoint "http://localhost:3000")

(defn json->cljs [json]
  (let [reader (t/reader :json)]
    (keywordize-keys (t/read reader json))))

(defn- req [method path params on-complete]
  (go
    (let [response (<! (method (str endpoint path)
                      {:with-credentials? false
                       :form-params params}))]
      (on-complete response))))

(def apiget (partial req http/get))
(def apipost (partial req http/post))

(defn get-companies []
  (apiget "/v1/companies/" {}
    (fn [response]
      (let [body (if (:success response) (json->cljs (:body response)) {})]
        (flux/dispatch dispatcher/companies body)))))

(defn get-company [ticker]
  (when symbol
    (apiget (str "/v1/companies/" ticker) {}
      (fn [response]
        (let [body (if (:success response) (json->cljs (:body response)) {})]
          (flux/dispatch dispatcher/company body))))))

(defn real-get-report [ticker year period]
  (apiget (str "/v1/companies/" ticker "/" year "/" period) {}
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
