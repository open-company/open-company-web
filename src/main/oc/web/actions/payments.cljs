(ns oc.web.actions.payments
  (:require [oc.web.api :as api]
            [cuerdas.core :as string]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.local-settings :as ls]
            [oc.lib.time :as lib-time]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.utils.stripe :as stripe-client]
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

(defn get-active-subscription [payments-data]
  (last (:subscriptions payments-data)))

(defn get-current-subscription [payments-data]
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

(defn parse-payments-data [{:keys [status body success]}]
  (let [payments-data (cond
                        success (json->cljs body)
                        (= status 404) :404
                        :else nil)
        portal-link (utils/link-for (:links payments-data) "portal" "POST")
        checkout-link (utils/link-for (:links payments-data) "checkout")
        premium? (premium-customer? payments-data)
        has-payments-data? (not (zero? (count (:payment-methods payments-data))))]
    (-> payments-data
        (merge {:can-open-portal? (map? portal-link)
                :can-open-checkout? (map? checkout-link)
                :portal-link portal-link
                :checkout-link checkout-link
                :premium? premium?
                :paywall? (not premium?)
                :payment-method-on-file? has-payments-data?})
        (update :available-prices (fn [prices] (->> prices
                                                    (map #(parse-price % (:seat-count payments-data)))
                                                    (sort-by :unit-amount)
                                                    vec))))))

(defn get-payments-cb [org-slug resp]
  (let [parsed-payments-data (parse-payments-data resp)]
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

(defn open-checkout! [payments-data change-price-id]
  (let [fixed-payments-data (or payments-data (dis/payments-data))
        base-domain (if ua/mobile-app?
                      ;; Get the deep link url but strip out the last slash to avoid
                      ;; a double slash
                      (string/join "" (butlast (dis/expo-deep-link-origin)))
                      ls/web-server-domain)
        base-redirect-url (str base-domain (router/get-token) "?org-settings=payments&picked-price=" change-price-id "&result=")
        success-redirect-url (str base-redirect-url "true")
        cancel-redirect-url (str base-redirect-url "false")]
    (api/get-checkout-session-id (:checkout-link fixed-payments-data) success-redirect-url cancel-redirect-url
     (fn [{:keys [success body]}]
      (when success
       (let [session-data (json->cljs body)]
         (dis/dispatch! [:payments-checkout-session-id session-data])
         (stripe-client/redirect-to-checkout session-data
          (fn [res]
           (when-not res
             (error-modal "An error occurred, please try again."))))))))))

;; Customer portal redirect

(def portal-session-cookie :portal-session-open)
(def portal-session-return-parameter "customer-portal")

(defn open-portal! [payments-data]
  (when-let [portal-link (:portal-link payments-data)]
        (let [base-domain (if ua/mobile-app?
                            ;; Get the deep link url but strip out the last slash to avoid
                            ;; a double slash
                            (string/join "" (butlast (dis/expo-deep-link-origin)))
                            ls/web-server-domain)
              client-url (str base-domain (router/get-token) "?" portal-session-return-parameter "=1")]
          (api/post-customer-portal portal-link client-url
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

(defn initial-loading []
  (let [return-session-cookie (cook/get-cookie portal-session-cookie)
        return-parameter (dis/query-param portal-session-return-parameter)]
    (cook/remove-cookie! portal-session-cookie)
    ;; Show an error message only if the user is returning to carrot from the same session it has been opened
    ;; and the return url is not right
    (when (and (pos? return-session-cookie)
               (not return-parameter))
      (error-modal "An error occurred during the portal session, please try again."))))