(ns oc.web.actions.subscription
  (:require [oc.web.api :as api]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.json :refer (json->cljs)]))

(defn get-subscription-cb [{:keys [body success]}]
  (let [subscription-data (when success (json->cljs body) {})]
    (dispatcher/dispatch! [:subscription subscription-data])))

(defn get-subscrition [company-uuid]
  (api/get-subscription company-uuid get-subscription-cb))