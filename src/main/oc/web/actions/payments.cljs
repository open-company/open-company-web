(ns oc.web.actions.payments
  (:require [oc.web.api :as api]
            [cuerdas.core :as string]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.local-settings :as ls]
            [oc.lib.time :as lib-time]
            [oc.web.actions.jwt :as jwt-actions]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.utils.stripe :as stripe-client]
            [oc.web.actions.notifications :as notif-actions]
            [oc.web.lib.cookies :as cook]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(def default-trial-status "trialing")
(def default-active-status "active")
(def default-trial-expired-status "past_due")
(def default-positive-statuses #{default-trial-status default-active-status})

(defn- error-modal [msg]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :title "Oops"
                    :message msg
                    :solid-button-style :red
                    :solid-button-title "OK, got it"
                    :solid-button-cb alert-modal/hide-alert}]
    (alert-modal/show-alert alert-data)))

;; Subscriptions data retrieve

(defn get-active-subscription
  "The active subscription is always the last in the subs list.
   If there is only 1 subscription is also the current one, if not it means it's the subs
   the user is going to switch to when the current one finishes."
  [payments-data]
  (last (:subscriptions payments-data)))

(defn get-current-subscription
  "The current subscription is the one the user paid for and is using.
   Its :current-period-start is in the past and :current-period-end is in the future."
  [payments-data]
  (first (:subscriptions payments-data)))

;; Paywall

(defn- premium-customer?
  "Given the loaded payments data return true if the UI needs to show the paywall."
  [payments-data]
  (let [fixed-payments-data (or payments-data
                                (dis/payments-data))
        has-valid-subscription? (some (comp default-positive-statuses :status) (:subscriptions fixed-payments-data))]
    (or ;; in case payments is disabled every user is a premium user
        (not ls/payments-enabled)
        ;; the payments data have been loaded
        (and (map? fixed-payments-data)
             ;; there is at least an active/trialing subscription
             has-valid-subscription?))))

;; Payments data handling

(defn- price-name [price-interval]
  (cond (= price-interval "month")
        "Monthly"
        (= price-interval "year")
        "Yearly"))

(defn- price-to-human [amount currency]
  (let [int-price-amount (int (/ amount 100))
        decimal-price-amount* (mod amount 100)
        decimal-price-amount (if (= (-> decimal-price-amount* str count) 1)
                               (str "0" decimal-price-amount*)
                               (str decimal-price-amount*))
        currency-symbol (case currency
                          "usd" "$"
                          "eur" "€"
                          (str currency " "))]
    (str currency-symbol int-price-amount "." decimal-price-amount)))

(defn- price-description [{:keys [unit-amount currency estimated-amount interval]} seat-count]
  (str (price-to-human unit-amount currency) " per user, per " interval
       (when (not= seat-count 1)
         (str " (" (price-to-human estimated-amount currency) " for " seat-count " users)"))))

(defn- parse-price [price seat-count]
  (-> price
      (assoc :description-label (price-description price seat-count))
      (assoc :name-label (price-name (:interval price)))))

(defn parse-payments-data [{:keys [status body success]} & [old-payments-data]]
  (let [payments-data (cond
                        success (json->cljs body)
                        (= status 404) :404
                        :else nil)
        manage-subscription-link (utils/link-for (:links payments-data) "manage-subscription" "POST")
        create-subscription-link (utils/link-for (:links payments-data) "create-subscription")
        premium? (premium-customer? payments-data)
        has-payments-data? (not (zero? (count (:payment-methods payments-data))))
        available-prices (if (:available-prices payments-data)
                           (->> (:available-prices payments-data)
                                (map #(parse-price % (:seat-count payments-data)))
                                (sort-by :unit-amount)
                                vec)
                           (:available-prices old-payments-data))]
    (-> old-payments-data
        (merge payments-data
               {:can-manage-subscription? (map? manage-subscription-link)
                :can-create-subscription? (map? create-subscription-link)
                :manage-subscription manage-subscription-link
                :create-subscription create-subscription-link
                :premium? premium?
                :paywall? (not premium?)
                :payment-method-on-file? has-payments-data?
                :subscriptions (mapv #(-> %
                                          (assoc :price (parse-price (:price %) (:quantity %)))
                                          (assoc :end-date-label (.toDateString (utils/js-date (* (:current-period-end %) 1000)))))
                                     (:subscriptions payments-data))})
        (assoc :available-prices available-prices))))

(declare checkout-session-return-result)

(defn get-payments-cb [org-slug resp]
  (let [parsed-payments-data (parse-payments-data resp)]
    (checkout-session-return-result parsed-payments-data)
    (dis/dispatch! [:payments org-slug parsed-payments-data])))

(defn get-payments [payments-link]
  (when ls/payments-enabled
    (api/get-payments payments-link (partial get-payments-cb (dis/current-org-slug)))))

(defn maybe-load-payments-data
  ([force-refresh?]
   (let [teams-data (dis/teams-data)
         org-data (dis/org-data)
         payments-link (some #(when (= (:team-id %) (:team-id org-data)) (utils/link-for (:links %) "payments")) teams-data)]
     (maybe-load-payments-data payments-link force-refresh?)))
  ([payments-link force-refresh?]
  (when ls/payments-enabled
    (let [payments-data (dis/payments-data)]
      (when (and payments-link
                 (or (not payments-data)
                     force-refresh?))
        (get-payments payments-link))))))

;; Checkout

(def checkout-session-cookie :checkout-session-open)
(def checkout-session-return-param :stripe-checkout)
(def checkout-session-return-result-param :result)

(defn- notify-subscription-created [success?]
  (notif-actions/show-notification {:title (if success?
                                             "Thank you, your subscription will be active in a few minutes"
                                             "An error occurred during checkout. Please try again")
                                    :dismiss true
                                    :expire 3
                                    :id (if success?
                                          :subscription-created-positive
                                          :subscription-created-negative)}))

(defn create-subscription! [payments-data price-id]
  (let [fixed-payments-data (or payments-data (dis/payments-data))
        base-domain (if ua/mobile-app?
                      ;; Get the deep link url but strip out the last slash to avoid
                      ;; a double slash
                      (string/join "" (butlast (dis/expo-deep-link-origin)))
                      ls/web-server-domain)
        base-redirect-url (str base-domain (router/get-token) "?" checkout-session-return-param "=true&picked-price=" price-id)
        success-redirect-url (str base-redirect-url "&" checkout-session-return-result-param "=true")
        cancel-redirect-url (str base-redirect-url "&" checkout-session-return-result-param "=false")]
    (api/create-subscription (:create-subscription fixed-payments-data) price-id success-redirect-url cancel-redirect-url
     (fn [{:keys [success body]}]
      (when success
       (let [response-data (parse-payments-data (json->cljs body) fixed-payments-data)
             new-subscription (:new-subscription response-data)
             checkout-session (:checkout-session response-data)]
         (dis/dispatch! [:payments-create-subscription/finished (dis/current-org-slug) response-data new-subscription checkout-session])
         (if new-subscription
           ;; If we have a new subscription it means the user had already a payment method on file
           ;; and we can notify the subscription!
           (notify-subscription-created true)
           (stripe-client/redirect-to-checkout checkout-session
                                               (fn [res]
                                                 (when-not res
                                                   (error-modal "An error occurred, please try again.")))))))))))

(defn- checkout-session-return-result []
  (let [show-checkout-session? (contains? @dis/app-state dis/payments-checkout-session-result)
        session-result (get @dis/app-state dis/payments-checkout-session-result)]
    (when show-checkout-session?
      (when show-checkout-session?
        (jwt-actions/jwt-refresh))
      (notify-subscription-created session-result))))

;; Customer portal redirect

(def portal-session-cookie :portal-session-open)
(def portal-session-return-param :customer-portal)

(defn manage-subscription! [payments-data]
  (when-let [manage-subscription-link (:manage-subscription payments-data)]
        (let [base-domain (if ua/mobile-app?
                            ;; Get the deep link url but strip out the last slash to avoid
                            ;; a double slash
                            (string/join "" (butlast (dis/expo-deep-link-origin)))
                            ls/web-server-domain)
              client-url (str base-domain (router/get-token) "?" portal-session-return-param "=1")]
          (api/manage-subscription manage-subscription-link client-url
                                   (fn [{:keys [success body]}]
                                     (let [resp (when success (json->cljs body))
                                           redirect-url (-> resp :portal :url)]
                                       (if (and success
                                                redirect-url)
                                         (do
                                            ;; Add a session cookie to make sure we show an error message if
                                            ;; user come back here w/o a response from the portal
                                           (cook/set-cookie! portal-session-cookie (lib-time/now-ts))
                                           (router/redirect! redirect-url))
                                         (error-modal "An error occurred, please try again."))))))))

;; Initialization

(defn initial-loading []
  ;; Check come back from checkout session
  (let [checkout-session-cookie (cook/get-cookie checkout-session-cookie)
        checkout-return-result-param (js/Boolean. (dis/query-param checkout-session-return-result-param))
        checkout-return-param (.parseInt ^js js/window (dis/query-param checkout-session-return-param) 10)]
    (when checkout-session-cookie
      (cook/remove-cookie! checkout-session-cookie)
      ;; Force refresh of the JWToken to make sure the changes are pulled
      (jwt-actions/jwt-refresh)
      ;; Show an error message only if the user is returning to carrot from a checkout session
      (when checkout-return-param
        (dis/dispatch! [:checkout-session-return checkout-return-result-param]))))
  ;; Check come back from customer portal
  (let [portal-cookie (cook/get-cookie portal-session-cookie)
        portal-param (.parseInt ^js js/window (dis/query-param portal-session-return-param))]
    (when portal-cookie
      (cook/remove-cookie! portal-session-cookie)
      ;; Force refresh of the JWToken to make sure the changes are pulled
      (jwt-actions/jwt-refresh)
      ;; Show an error message only if the user is returning to carrot from a customer portal session
      (when-not portal-param
        (error-modal "An error occurred during the portal session, please try again.")))))