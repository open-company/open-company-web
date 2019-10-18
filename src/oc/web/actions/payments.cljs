(ns oc.web.actions.payments
  (:require [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]))

(defn get-payments-cb [org-slug {:keys [body success]}]
  (let [payments-data (when success (json->cljs body) {})]
    (dis/dispatch! [:payments org-slug payments-data])))

(defn get-payments [payments-link]
  (api/get-payments payments-link (partial get-payments-cb (router/current-org-slug))))

(defn maybe-load-payments-data [org-data & [force-refresh?]]
  (let [payments-data (dis/payments-data)]
    (when (and org-data
               (or (not payments-data)
                   force-refresh?))
      (when-let [payments-link (utils/link-for (:links org-data) "payments")]
        (get-payments payments-link)))))