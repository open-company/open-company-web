(ns oc.web.actions.payments
  (:require [oc.web.api :as api]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.json :refer (json->cljs)]))

(defn get-payments-cb [{:keys [body success]}]
  (let [payments-data (when success (json->cljs body) {})]
    (dispatcher/dispatch! [:payments payments-data])))

(defn get-payments [customer-link]
  (api/get-payments customer-link get-payments-cb))