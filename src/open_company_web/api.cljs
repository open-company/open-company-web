(ns open-company-web.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async :refer [<!]]
            [cljs-http.client :as http]
            [clojure.string :refer [join]]
            [open-company-web.dispatcher :as dispatcher]
            [cljs-flux.dispatcher :as flux]
            [cognitect.transit :as t]))

(def endpoint "http://localhost:3000/")

(defn json->cljs [json]
  (let [reader (t/reader :json)]
    (t/read reader json)))

(defn- req [method path params on-complete]
  (go
    (let [response (<! (method (str endpoint path)
                      {:with-credentials? false
                       :form-params params}))]
      (on-complete response))))

(def apiget (partial req http/get))
(def apipost (partial req http/post))

(defn get-companies []
  (apiget "v1/companies/OPEN" {}
    (fn [response]
      (let [body (json->cljs (:body response))]
        (flux/dispatch dispatcher/companies [body])))))

(defn get-company [symbol]
  (when symbol
    (apiget (str "v1/companies/" symbol) {}
      (fn [response]
        (let [body (json->cljs (:body response))]
          (flux/dispatch dispatcher/company body))))))
