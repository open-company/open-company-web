(ns oc.web.actions.payments
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [oc.web.api :as api]
            [cuerdas.core :as string]
            [oc.web.lib.jwt :as jwt]
            [oc.web.utils.activity :as activity-utils]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.lib.cljs.useragent :as ua]
            [oc.lib.time :as lib-time]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.jwt :as jwt-actions]
            [oc.web.lib.json :refer (json->cljs cljs->json)]
            [oc.web.utils.stripe :as stripe-client]
            [oc.web.actions.notifications :as notif-actions]
            [oc.web.lib.cookies :as cook]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(def default-trial-status "trialing")
(def default-active-status "active")
(def default-trial-expired-status "past_due")
(def default-positive-statuses #{default-trial-status default-active-status})

(def upgraded-message "🎉 Your team is now subscribed to Premium!")
(def prompt-upgrade-message "🤓 Upgrade to unlock all premium features")
(def downgrade-message "✄ Your team downgraded to the Free plan.")
(def cancel-message "🙄 Your team's Premium subscription was cancelled, ending on %s.")
(def renew-message "🎉 You’ve re-started your Premium subscription.")
(def plan-change-message "🎉 Your plan was changed.")
;; (def abandon-message "You canceled a change to your subscription plan. You're still subscribed to the %s plan.")
(def generic-error-message "An error occurred during checkout. Please try again")

(defn- error-modal [msg]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :title "Oops"
                    :message msg
                    :solid-button-style :red
                    :solid-button-title "OK, got it"
                    :solid-button-cb alert-modal/hide-alert}]
    (alert-modal/show-alert alert-data)))

;; Banner handlers

(def prompt-after-days 14)

(defn- old-user? [user-created-at]
  (let [created-at (lib-time/millis (lib-time/from-iso user-created-at))
        now-millis (lib-time/now-ts)
        limit-ts (* 1000 60 60 24 prompt-after-days)]
    (>= (- now-millis created-at)
        limit-ts)))

(defn- did-post? []
  (seq (activity-utils/last-used-section)))

(def ^:private dismiss-cookie-value "dismissed")
(def ^:private dismiss-cookie-expire (* 60 60 24 365 10))

(defn- banner-dismissed? [team-id]
  (let [cookie-name (router/payments-prompt-upgrade-cookie team-id)]
    (= (cook/get-cookie cookie-name) dismiss-cookie-value)))

(defn- dismiss-prompt-banner [team-id]
  (cook/set-cookie! (router/payments-prompt-upgrade-cookie team-id)
                    dismiss-cookie-value dismiss-cookie-expire))

(defn should-show-prompt-banner?
  "Decide if the prompt banner needs to be shown.
   Show if:
   - is NOT mobile: not enough space on mobile
   - is an admin: non-admins can't start premium subs
   - is NOT on premium already
   - has NOT been shown and dismissed before
   - user is older than 14 days or user published at least one post"
  [{team-id :team-id premium-team? :premium} {user-created-at :created-at :as _current-user-data}]
  (let [old-enough-user? (old-user? user-created-at)
        user-did-post? (did-post?)]
    (and (not (responsive/is-mobile-size?))
          (jwt/is-admin? team-id)
          (not premium-team?)
          (not (banner-dismissed? team-id))
          (or old-enough-user?
              user-did-post?))))

(defn show-prompt-upgrade-banner []
  (dis/dispatch! [:input [dis/payments-ui-prompt-banner-key] true]))

(defn hide-prompt-upgrade-banner
  ([] (hide-prompt-upgrade-banner false))
  ([skip-persistence?]
   (when-not skip-persistence?
     (dismiss-prompt-banner (:team-id (dis/org-data))))
   (dis/dispatch! [:input [dis/payments-ui-prompt-banner-key] false])))

(defn show-upgraded-banner []
  (dis/dispatch! [:input [dis/payments-ui-upgraded-banner-key] true]))

(defn hide-upgraded-banner []
  (dis/dispatch! [:input [dis/payments-ui-upgraded-banner-key] false]))

(defn maybe-show-prompt-banner []
  (when-let* [org-data (dis/org-data)
              team-data (dis/team-data)
              current-user-data (dis/current-user-data)]
    (if (should-show-prompt-banner? team-data current-user-data)
      (show-prompt-upgrade-banner)
      (hide-prompt-upgrade-banner true))))

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
         (str " (" (price-to-human estimated-amount currency) " for " seat-count " users)"))
       "."))

(defn- parse-price [price seat-count]
  (-> price
      (assoc :description-label (price-description price seat-count))
      (assoc :name-label (price-name (:interval price)))))

(defn parse-payments-data [raw-payments-data]
  (let [manage-subscription-link (utils/link-for (:links raw-payments-data) "manage-subscription" "POST")
        create-subscription-link (utils/link-for (:links raw-payments-data) "create-subscription")
        premium? (premium-customer? raw-payments-data)
        has-payment-method? (not (zero? (count (:payment-methods raw-payments-data))))
        available-prices (if (:available-prices raw-payments-data)
                           (->> (:available-prices raw-payments-data)
                                (map #(parse-price % (:seat-count raw-payments-data)))
                                (sort-by :unit-amount)
                                vec)
                           (:available-prices raw-payments-data))]
    (-> raw-payments-data
        (merge {:available-prices available-prices
                :can-manage-subscription? (map? manage-subscription-link)
                :can-create-subscription? (map? create-subscription-link)
                :manage-subscription-link manage-subscription-link
                :create-subscription-link create-subscription-link
                :premium? premium?
                :paywall? (not premium?)
                :payment-method-on-file? has-payment-method?
                :subscriptions (mapv #(-> %
                                          (assoc :price (parse-price (:price %) (:quantity %)))
                                          (assoc :end-date-label (.toDateString (utils/js-date (* (:current-period-end %) 1000)))))
                                     (:subscriptions raw-payments-data))}))))

(declare check-notify-user)

(defn- get-payments-cb [org-slug {:keys [success status body]}]
  (if-not success
    (dis/dispatch! [:payments org-slug {:error true
                                        :status status}])
    (let [payments-data (parse-payments-data (json->cljs body))]
      (dis/dispatch! [:payments org-slug payments-data])
      (check-notify-user payments-data))))

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

;; Helpers

(defn- format-sub-cookie-data [payments-data]
  (let [sub (get-current-subscription payments-data)
        premium? (jwt/premium? (:team-id payments-data))]
    (->> {:cancel-at-period-end? (boolean (:cancel-at-period-end? sub))
          :premium? (boolean premium?)
          :subs-count (count (:subscriptions payments-data))
          :price-id (-> sub :price :id)}
         cljs->json
         (.stringify js/JSON))))

(defn- parse-sub-cookie-data [val]
  (when (seq val)
    (json->cljs val)))

(defn- parse-boolean-string [v]
  (if (= v "true")
    true
    false))

;; Checkout

(def checkout-session-cookie :checkout-session-open)
(def checkout-team-id-cookie :checkout-team-id)
(def checkout-session-return-param :stripe-checkout)
(def checkout-session-result-param :result)

(defn- notify-user [id msg]
  ;; Hide the banner that prompts the user to upgrade
  ;; and show the upgraded one in case they activate
  ;; or re-activate the premium plan
  (when (#{:sub-upgrade-success :sub-renew-success} id)
    (hide-prompt-upgrade-banner)
    (show-upgraded-banner))
  (notif-actions/show-notification {:title msg
                                    :dismiss true
                                    :expire 3
                                    :id id}))
(defn- check-notify-user* [new-payments-data {team-id :team-id success? :success? old-data :cookie-data}]
  (if success?
    (let [new-premium (jwt/premium? team-id)
          new-sub (get-current-subscription new-payments-data)]
      (when (or new-sub
                (:price-id old-data))
                ;; Notify user of a re-new/re-activation
        (cond (and new-premium
                    (not (:price-id old-data))
                    (not (:premium? old-data))
                    (> (:subscriptions new-payments-data) 1))
              (notify-user :sub-renew-success renew-message)
                ;; Notify user of an upgrade
              (and new-premium
                    (not (:price-id old-data))
                    (not= (:premium? old-data) new-premium))
              (notify-user :sub-upgrade-success upgraded-message)
                ;; Notify user of a downgrade
              (and (:price-id old-data)
                    (not= (:premium? old-data) new-premium)
                    (not new-premium))
              (notify-user :sub-downgrade-success downgrade-message)
                ;; Notify user of a plan change if the price-id of the current sub changed
                ;; or there is a new subscription appeneded to the list (means they scheduled a sub change)
              (or (not= (:price-id old-data) (-> new-sub :price :id))
                  (not= (:subs-count old-data) (count (:subscriptions new-payments-data))))
              (notify-user :price-change-success plan-change-message)
                ;; Notify user of a cancel/renew
              (not= (boolean (:cancel-at-period-end? new-sub)) (:cancel-at-period-end? old-data))
              (if (:cancel-at-period-end? new-sub)
                (notify-user :sub-cancel-success (string/format cancel-message (.toDateString (js/Date. (:current-period-end new-sub)))))
                (notify-user :sub-renew-success renew-message)))))
    (notify-user :sub-change-error generic-error-message)))

(defn- check-notify-user [new-payments-data]
  (when-let [stored-session-data (dis/payments-notify-cache-data)]
    (dis/dispatch! [:notify-cache/reset (dis/current-org-slug)])
    (check-notify-user* new-payments-data stored-session-data)))

(defn- maybe-notify-user
  "Cookie data keep the subscription data before redirecting to Stripe:
   {:cancel-at-period-end? true/false
    :premium? true/false
    :sub-id 'subscription-id'}"
  [team-id success? cookie-data]
  (when cookie-data
    (dis/dispatch! [:notify-cache/store (dis/current-org-slug) {:team-id team-id
                                                                :success? success?
                                                                :cookie-data cookie-data}])))

(defn create-subscription! [payments-data price-id & [cb]]
  (let [fixed-payments-data (or payments-data (dis/payments-data))
        base-domain (if ua/mobile-app?
                      ;; Get the deep link url but strip out the last slash to avoid
                      ;; a double slash
                      (string/join "" (butlast (dis/expo-deep-link-origin)))
                      ls/web-server-domain)
        base-redirect-url (str base-domain (router/get-token) "?" (name checkout-session-return-param) "=" (:team-id fixed-payments-data) "&picked-price=" price-id)
        success-redirect-url (str base-redirect-url "&" (name checkout-session-result-param) "=true")
        cancel-redirect-url (str base-redirect-url "&" (name checkout-session-result-param) "=false")
        org-data (dis/org-data)
        json-data {:org-uuid (:uuid org-data)
                   :org-slug (:slug org-data)
                   :success-url success-redirect-url
                   :cancel-url cancel-redirect-url
                   :price-id price-id}]
    (api/create-subscription (:create-subscription-link fixed-payments-data) json-data
     (fn [{:keys [success body]}]
      (when success
        (let [response-data (parse-payments-data (json->cljs body))
              new-subscription (:new-subscription response-data)
              checkout-session (:checkout-session response-data)]
          (dis/dispatch! [:payments-create-subscription/finished (dis/current-org-slug) response-data new-subscription checkout-session])
          (if new-subscription
           ;; If we have a new subscription it means the user had already a payment method on file
           ;; and we can notify the subscription!
           (let [old-subscription (get-current-subscription fixed-payments-data)]
             (dis/dispatch! [:hide-premium-picker])
             (check-notify-user* response-data {:team-id (:team-id fixed-payments-data)
                                                :success? true
                                                :cookie-data {:cancel-at-period-end? (boolean (:cancel-at-period-end? old-subscription))
                                                              :premium? (boolean (jwt/premium? (:team-id fixed-payments-data)))
                                                              :subs-count (count (:subscriptions fixed-payments-data))
                                                              :price-id (-> old-subscription :price :id)}}))
            (do
              (cook/set-cookie! checkout-session-cookie (format-sub-cookie-data response-data))
              (stripe-client/redirect-to-checkout checkout-session
                                                  (fn [res]
                                                    (when-not res
                                                      (error-modal "An error occurred, please try again!!."))))))))
       (when (fn? cb)
         (cb success))))))

;; Customer portal redirect

(def portal-session-cookie :portal-session-open)
(def portal-team-id-cookie :portal-team-id)
(def portal-session-return-param :customer-portal)

(defn manage-subscription! [payments-data & [cb]]
  (when-let [manage-subscription-link (:manage-subscription-link payments-data)]
        (let [base-domain (if ua/mobile-app?
                            ;; Get the deep link url but strip out the last slash to avoid
                            ;; a double slash
                            (string/join "" (butlast (dis/expo-deep-link-origin)))
                            ls/web-server-domain)
              client-url (str base-domain (router/get-token) "?" (name portal-session-return-param) "=" (:team-id payments-data))]
          (api/manage-subscription manage-subscription-link client-url
                                   (fn [{:keys [success body] :as resp}]
                                     (let [clj-body (when success (json->cljs body))
                                           new-payments-data (when clj-body (parse-payments-data clj-body))
                                           redirect-url (-> clj-body :portal-session :url)]
                                       (if (and success
                                                redirect-url)
                                         (do
                                            ;; Add a session cookie to make sure we show an error message if
                                            ;; user come back here w/o a response from the portal
                                           (cook/set-cookie! portal-session-cookie (format-sub-cookie-data new-payments-data))
                                           (router/redirect! redirect-url))
                                         (do
                                           (when success
                                             (dis/dispatch! [:hide-premium-picker])
                                             (get-payments-cb (dis/current-org-slug) resp))
                                           (error-modal "An error occurred, please try again."))))
                                     (when (fn? cb)
                                       (cb success)))))))

;; Initialization

(defn initial-loading []
  ;; Check come back from checkout session
  (let [checkout-cookie-value (cook/get-cookie checkout-session-cookie)
        checkout-session-value (when checkout-cookie-value (parse-sub-cookie-data checkout-cookie-value))
        checkout-session-result-param (parse-boolean-string (dis/query-param checkout-session-result-param))
        team-id (dis/query-param checkout-session-return-param)]
    (when checkout-session-value
      (cook/remove-cookie! checkout-session-cookie)
      ;; Force refresh the JWToken and check if premium changed for the current team
      (jwt-actions/jwt-refresh #(maybe-notify-user team-id checkout-session-result-param checkout-session-value))))
  ;; Check come back from customer portal
  (let [portal-cookie-value (cook/get-cookie portal-session-cookie)
        portal-session-value (when portal-cookie-value (parse-sub-cookie-data portal-cookie-value))
        team-id (dis/query-param portal-session-return-param)]
    (when portal-session-value
      (cook/remove-cookie! portal-session-cookie)
      ;; Force refresh of the JWToken to make sure the changes are pulled
      (jwt-actions/jwt-refresh #(maybe-notify-user team-id true portal-session-value)))))