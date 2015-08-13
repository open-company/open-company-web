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

(def apiget (partial req http/get))
(def apipost (partial req http/post))
(def apiput (partial req http/put))

(defn- link-for [links rel year period]
  (let [partial-res (filter #(= (:rel %) rel) links)
        partial-res (if year
                      (filter #(= (str (:year %)) year) partial-res)
                      partial-res)
        partial-res (if period
                      (filter #(= (:period %) period) partial-res)
                      partial-res)]
    (when (> (count partial-res) 0)
      (first partial-res))))

(defn get-companies []
  (apiget "" nil (fn [response]
      (let [body (if (:success response) (json->cljs (:body response)) {})]
        (flux/dispatch dispatcher/companies body)))))

(defn get-company [ticker]
  (when ticker
    (apiget (str "/companies/" ticker) nil
      (fn [response]
        (let [body (if (:success response) (json->cljs (:body response)) {})]
          (flux/dispatch dispatcher/company body))))))

(defn save-or-create-company [ticker data]
  (when (and ticker data)
    (let [company-data (dissoc data :headcount :finances :compensation :links)
          json-data (cljs->json company-data)
          report-link (link-for (:links data) "update" nil nil)]
      (apiput (:href report-link)
        { :json-params json-data
          :headers {
            ; required by Chrome
            "Access-Control-Allow-Headers" "Content-Type"
            ; custom content type
            "content-type" (:type report-link)
          }}
        (fn [response]
          (let [body (if (:success response) (json->cljs (:body response)) {})]
            (flux/dispatch dispatcher/company body)))))))

(defn get-report
  ([report-link]
    (apiget (:href report-link) {
        :headers {
          "Access-Control-Allow-Headers" "Content-Type"
          "content-type" (:type report-link)
        }
      }
      (fn [response]
        (let [body (if (:success response) (json->cljs (:body response)) {})]
          (flux/dispatch dispatcher/report body)))))
  ([ticker year period]
    (when (and ticker year period)
      (if (contains? @dispatcher/app-state (keyword ticker))
        ; load the report only
        (let [links (:links ((keyword ticker) @dispatcher/app-state))
              report-link (link-for links "report" year period)]
          (get-report report-link))
        ; load the company data before the report data
        (do
          (flux/register
            dispatcher/company
            (fn [response]
              (let [links (:links response)
                    report-url (link-for links "report" year period)]
                (get-report report-url))))
          ; load company data
          (get-company ticker))))))

(defn save-or-create-report
  ([report-link data]
    (when (and report-link data (or (:headcount data) (:finances data) (:compensation data)))
      (let [json-data (cljs->json (dissoc data :links))]
        (apiput (:href report-link)
          { :json-params json-data
            :headers {
              ; required by Chrome
              "Access-Control-Allow-Headers" "Content-Type"
              ; custom content type
              "content-type" (:type report-link)
            }}
          (fn [response]
            (let [body (if (:success response) (json->cljs (:body response)) {})]
              (flux/dispatch dispatcher/report body)))))))
  ([ticker year period data]
    (let [company-data ((keyword ticker) @dispatcher/app-state)]
      (if-let [report-link (link-for (:links company-data) "report" year period)]
        (save-or-create-report report-link data)))))