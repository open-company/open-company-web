(ns oc.web.actions.payments
  (:require [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.json :refer (json->cljs)]))

(defn get-payments-cb [org-slug {:keys [body success status]}]
  (let [payments-data (cond
                        success (json->cljs body)
                        (= status 404) :404
                        :else nil)]
    (dis/dispatch! [:payments org-slug payments-data])))

(defn get-payments [payments-link]
  (when ls/payments-enabled
    (api/get-payments payments-link (partial get-payments-cb (router/current-org-slug)))))

(defn maybe-load-payments-data [org-data & [force-refresh?]]
  (when ls/payments-enabled
    (let [payments-data (dis/payments-data)]
      (when (and org-data
                 (or (not payments-data)
                     force-refresh?))
        (when-let [payments-link (utils/link-for (:links org-data) "payments")]
          (get-payments payments-link))))))

(defn show-paywall-alert? [payments-data]
  (let [fixed-payments-data (or payments-data
                                (dis/payments-data))]
    (and ls/payments-enabled
         (= fixed-payments-data :404))))

(defn create-plan-subscription [payments-data plan-id]
  (let [create-subscription-link (utils/link-for (:links payments-data) "update")]
    (when create-subscription-link
      (api/update-plan-subscription create-subscription-link plan-id
       (fn [{:keys [status body success]}]
        (js/console.log "DBG put response:" (if success (json->cljs body) status)))))))

(defn patch-plan-subscription [payments-data plan-id]
  (let [update-subscription-link (utils/link-for (:links payments-data) "partial-update")]
    (when update-subscription-link
      (api/update-plan-subscription update-subscription-link plan-id
       (fn [{:keys [status body success]}]
        (js/console.log "DBG patch response:" (if success (json->cljs body) status)))))))

(defn delete-plan-subscription [payments-data plan-id]
  (let [delete-subscription-link (utils/link-for (:links payments-data) "delete")]
    (when delete-subscription-link
      (api/update-plan-subscription delete-subscription-link nil
       (fn [{:keys [status body success]}]
        (js/console.log "DBG patch response:" (if success (json->cljs body) status)))))))