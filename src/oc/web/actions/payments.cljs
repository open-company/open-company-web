(ns oc.web.actions.payments
  (:require [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.json :refer (json->cljs)]))

(def default-trial-status "trialing")
(def default-active-status "active")

;; Payments data handling

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

;; Subscription handling

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

;; Paywall

(defn show-paywall-alert?
  "Given the loaded payments data return true if the UI needs to show the paywall and prevent any publish.
  Condition to show the paywall, or:
  - susbscription has not been created yet
  - status is trialing and trial-end is less than now
  - status is not trialing and current-period-end is less than now"
  [payments-data]
  (let [fixed-payments-data (or payments-data
                                (dis/payments-data))
        subscription-data (:subscription fixed-payments-data)
        is-trial? (= (:status fixed-payments-data) default-trial-status)
        trial-expired? (> (* (:trial-end fixed-payments-data) 1000) (.getDate (js/Date.)))
        period-expired? (> (* (:current-period-end fixed-payments-data) 1000) (.getDate (js/Date.)))]
    (and ls/payments-enabled
         (or ;; No subscription available for current user... TBD
             (= fixed-payments-data :404)
             ;; If org is on trial
             (and is-trial?
                  ;; and trial is expired
                  trial-expired?)
             ;; Org is not on trial anymore
             (and (not is-trial?)
                  ;; the payed period is expired
                  period-expired?)))))