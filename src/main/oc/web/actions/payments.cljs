(ns oc.web.actions.payments
  (:require [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.local-settings :as ls]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.utils.stripe :as stripe-client]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oops.core :refer (oset!)]
            [goog.dom :as gDom]))

(def default-trial-status "trialing")
(def default-active-status "active")
(def default-trial-expired-status "past_due")
(def default-positive-statuses #{default-trial-status default-active-status})

;; Payments data handling

(defn parse-payments-data [{:keys [status body success]}]
  (let [payments-data (cond
                        success (json->cljs body)
                        (= status 404) :404
                        :else nil)
        portal-link (utils/link-for (:links payments-data) "portal" "POST")]
    (merge payments-data {:can-open-portal? (map? portal-link)
                          :portal-link portal-link})))

(defn get-payments-cb [org-slug resp]
  (let [parsed-payments-data (parse-payments-data resp)]
    (dis/dispatch! [:payments org-slug parsed-payments-data])))

(defn get-payments [payments-link]
  (when ls/payments-enabled
    (api/get-payments payments-link (partial get-payments-cb (dis/current-org-slug)))))

(defn maybe-load-payments-data [org-data & [force-refresh?]]
  (when ls/payments-enabled
    (let [payments-data (dis/payments-data)]
      (when (and org-data
                 (or (not payments-data)
                     force-refresh?))
        (when-let [payments-link (utils/link-for (:links org-data) "payments")]
          (get-payments payments-link))))))

;; Subscription handling

(defn create-price-subscription [payments-data price-id & [callback]]
  (let [create-subscription-link (utils/link-for (:links payments-data) "create")
        org-slug (dis/current-org-slug)]
    (when create-subscription-link
      (api/update-price-subscription create-subscription-link price-id
       (fn [{:keys [status body success] :as resp}]
        (get-payments-cb org-slug resp)
        (callback success))))))

(defn delete-price-subscription [payments-data & [callback]]
  (let [delete-subscription-link (utils/link-for (:links payments-data) "delete")
        org-slug (dis/current-org-slug)]
    (when delete-subscription-link
      (api/update-price-subscription delete-subscription-link nil
       (fn [{:keys [status body success] :as resp}]
        (get-payments-cb org-slug resp)
        (callback success))))))

;; Checkout

(defn add-payment-method [payments-data & [change-price-data]]
  (let [fixed-payments-data (or payments-data (dis/payments-data))
        checkout-link (utils/link-for (:links fixed-payments-data) "checkout")
        base-domain (if ua/mobile-app?
                      ;; Get the deep link url but strip out the last slash to avoid
                      ;; a double slash
                      (clojure.string/join "" (butlast (dis/expo-deep-link-origin)))
                      ls/web-server-domain)
        base-redirect-url (str base-domain (router/get-token) "?org-settings=payments&result=")
        success-redirect-url (str base-redirect-url "true"
                               (when change-price-data
                                 (str "&update-price=" (:id change-price-data))))
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
         ;; Do not show paywall until payments data are loaded
         fixed-payments-data
         (or ;; the payments data are not available yet
             (= fixed-payments-data :404)
             ;; If customer has no subscription yet
             ;; FIXME: added to fix a race condition where users were seeing the
             ;; paywall after signup until refresh (Sean on FF with Slack signup)
             (and (map? subscription-data)
                  ;; or the org is on a non active price
                  (not (default-positive-statuses subscription-status)))))
    false))


;; Customer portal redirect

(defn open-portal! [payments-data]
  (when-let [portal-link (:portal-link payments-data)]
    (let [client-url (str ls/web-server-domain (router/get-token) "?org_settings=payments")
          form (gDom/createElement "form")
          _ (.add (.-classList form) "hidden")
          _ (oset! form "method" "POST")
          _ (oset! form "action" (:href portal-link))
          redirect-input (gDom/createElement "input")
          _ (oset! redirect-input "type" "hidden")
          _ (oset! redirect-input "value" client-url)
          _ (oset! redirect-input "name" "client-url")
          _ (.appendChild form redirect-input)
          authorization-input (gDom/createElement "input")
          _ (oset! authorization-input "type" "hidden")
          _ (oset! authorization-input "value" (str "Bearer " (jwt/jwt)))
          _ (oset! authorization-input "name" "authorization")
          _ (.appendChild form authorization-input)
          _ (.appendChild (.-body js/document) form)]
      (.submit form))))