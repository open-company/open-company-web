(ns oc.web.actions.payments
  (:require [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]))

(defn get-payments-cb [org-slug {:keys [body success]}]
  (let [payments-data (when success (json->cljs body) {})]
    (dis/dispatch! [:payments org-slug payments-data])))

(defn get-payments [customer-link]
  (api/get-payments customer-link (partial get-payments-cb (router/current-org-slug))))

(defn maybe-load-payments-data [team-data & [force-update?]]
  (let [org-data (dis/org-data)
        payments-data (dis/payments-data)]
    (when (and team-data
               org-data
               (= (:team-id team-data) (:team-id org-data))
               (or (not payments-data)
                  force-update?))
      (when-let [customer-link (utils/link-for (:links team-data) "customer")]
        (get-payments customer-link)))))