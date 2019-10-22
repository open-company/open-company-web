(ns oc.web.actions.payments
  (:require [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.utils.stripe :as stripe-client]))

(def default-trial-status "trialing")
(def default-active-status "active")
(def default-positive-statuses #{default-trial-status default-active-status})

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

(defn create-plan-subscription [payments-data plan-id & [callback]]
  (let [create-subscription-link (utils/link-for (:links payments-data) "update")]
    (when create-subscription-link
      (api/update-plan-subscription create-subscription-link plan-id
       (fn [{:keys [status body success]}]
        (js/console.log "DBG put response:" (if success (json->cljs body) status))
        (callback success))))))

(defn patch-plan-subscription [payments-data plan-id & [callback]]
  (let [update-subscription-link (utils/link-for (:links payments-data) "partial-update")]
    (when update-subscription-link
      (api/update-plan-subscription update-subscription-link plan-id
       (fn [{:keys [status body success]}]
        (js/console.log "DBG patch response:" (if success (json->cljs body) status))
        (callback success))))))

(defn delete-plan-subscription [payments-data plan-id & [callback]]
  (let [delete-subscription-link (utils/link-for (:links payments-data) "delete")]
    (when delete-subscription-link
      (api/update-plan-subscription delete-subscription-link nil
       (fn [{:keys [status body success]}]
        (js/console.log "DBG patch response:" (if success (json->cljs body) status))
        (callback success))))))

;; Checkout

(defn add-payment-method [payments-data]
  (let [fixed-payments-data (or payments-data (dis/payments-data))
        checkout-link (utils/link-for (:links fixed-payments-data) "checkout")
        base-redirect-url (str ls/web-server-domain (router/get-token) "?org-settings=payments&success=")
        success-redirect-url (str base-redirect-url "true")
        cancel-redirect-url (str base-redirect-url "false")]
    (api/get-checkout-session-id checkout-link success-redirect-url cancel-redirect-url
     (fn [{:keys [success body status]}]
      (when success
       (let [session-data (json->cljs body)]
         (dis/dispatch! [:payments-checkout-session-id session-data])
         (stripe-client/redirect-to-checkout session-data
          (fn [res]
           (js/console.log "DBG result of checkout:" res)))))))))

;; Paywall

(defn show-paywall-alert?
  "Given the loaded payments data return true if the UI needs to show the paywall and prevent any publish.
  Condition to show the paywall, or:
  - status different than trialing/active"
  [payments-data]
  (let [fixed-payments-data (or payments-data
                                (dis/payments-data))
        subscription-data (:subscription fixed-payments-data)
        subscription-status (:status subscription-data)
        is-trial? (= (:status fixed-payments-data) default-trial-status)
        trial-expired? (> (* (:trial-end fixed-payments-data) 1000) (.getDate (js/Date.)))
        period-expired? (> (* (:current-period-end fixed-payments-data) 1000) (.getDate (js/Date.)))]
    (and ls/payments-enabled
         (or ;; No subscription available for current user... TBD
             (= fixed-payments-data :404)
             (not (default-positive-statuses subscription-status))))))