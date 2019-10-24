(ns oc.web.actions.payments
  (:require [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.utils.stripe :as stripe-client]
            [oc.web.components.ui.alert-modal :as alert-modal]))

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
  (let [create-subscription-link (utils/link-for (:links payments-data) "create")
        org-slug (router/current-org-slug)]
    (when create-subscription-link
      (api/update-plan-subscription create-subscription-link plan-id
       (fn [{:keys [status body success] :as resp}]
        (get-payments-cb org-slug resp)
        (callback success))))))

(defn delete-plan-subscription [payments-data plan-id & [callback]]
  (let [delete-subscription-link (utils/link-for (:links payments-data) "delete")
        org-slug (router/current-org-slug)]
    (when delete-subscription-link
      (api/update-plan-subscription delete-subscription-link nil
       (fn [{:keys [status body success] :as resp}]
        (get-payments-cb org-slug resp)
        (callback success))))))

;; Checkout

(defn add-payment-method [payments-data & [change-plan-data]]
  (let [fixed-payments-data (or payments-data (dis/payments-data))
        checkout-link (utils/link-for (:links fixed-payments-data) "checkout")
        base-redirect-url (str ls/web-server-domain (router/get-token) "?org-settings=payments&result=")
        success-redirect-url (str base-redirect-url "true"
                               (when change-plan-data
                                 (str "&update-plan=" (:id change-plan-data))))
        cancel-redirect-url (str base-redirect-url "false")]
    (api/get-checkout-session-id checkout-link success-redirect-url cancel-redirect-url
     (fn [{:keys [success body status]}]
      (when success
       (let [session-data (json->cljs body)]
         (dis/dispatch! [:payments-checkout-session-id session-data])
         (stripe-client/redirect-to-checkout session-data
          (fn [res]
           (when-not res
             (let [alert-data {:icon "/img/ML/trash.svg"
                               :title "Oops"
                               :message "An error occurred, please try again."
                               :solid-button-style :red
                               :solid-button-title "OK, got it"
                               :solid-button-cb alert-modal/hide-alert}]
              (alert-modal/show-alert alert-data)))))))))))

;; Subscriptions data retrieve

(defn get-active-subscription [payments-data]
  (last (:subscriptions payments-data)))

(defn get-current-subscription [payments-data]
  (first (:subscriptions payments-data)))

;; Paywall

(defn show-paywall-alert?
  "Given the loaded payments data return true if the UI needs to show the paywall and prevent any publish.
  Condition to show the paywall, or:
  - status different than trialing/active"
  [payments-data]
  (let [fixed-payments-data (or payments-data
                                (dis/payments-data))
        subscription-data (get-current-subscription fixed-payments-data)
        subscription-status (:status subscription-data)
        is-trial? (= (:status fixed-payments-data) default-trial-status)
        trial-expired? (> (* (:trial-end fixed-payments-data) 1000) (.getDate (js/Date.)))
        period-expired? (> (* (:current-period-end fixed-payments-data) 1000) (.getDate (js/Date.)))]
    (and ;; payments service is enabled
         ls/payments-enabled
         (or ;; if pay data have not been loaded yet or
             fixed-payments-data
             ;; the subscriptions data are not available
             (= fixed-payments-data :404)
             ;; or the org is on a non active plan
             (not (default-positive-statuses subscription-status))))))