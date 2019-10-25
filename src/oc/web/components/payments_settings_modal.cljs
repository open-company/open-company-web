(ns oc.web.components.payments-settings-modal
  (:require [rum.core :as rum]
            [clojure.contrib.humanize :refer (intcomma)]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.payments :as payments-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.small-loading :refer (small-loading)]))

(defn- change-tab [s tab]
  ;; Reset the checkout result key so the user won't see the result message
  ;; another anymore
  (dis/dispatch! [:input [dis/checkout-result-key] nil])
  ;; Remove it also from the local state
  (reset! (::checkout-result s) nil)
  ;; Change tab
  (reset! (::payments-tab s) tab))

(defn- plan-amount-to-human [amount currency]
  (let [int-plan-amount (int (/ amount 100))
        decimal-plan-amount* (mod amount 100)
        decimal-plan-amount (if (= (-> decimal-plan-amount* str count) 1)
                             (str "0" decimal-plan-amount*)
                             (str decimal-plan-amount*))
        currency-symbol (case currency
                         "usd" "$"
                         "eur" "€"
                         (str currency " "))]
    (str currency-symbol int-plan-amount "." decimal-plan-amount)))

(defn- plan-price [plan-data quantity]
  (let [tier (first (filterv #(or (and (:up-to %) (<= quantity (:up-to %)))
                                  (not (:up-to %))) (:tiers plan-data)))
        tier-price (if (:up-to tier)
                     (+ (:flat-amount tier) (* quantity (:unit-amount tier)))
                     (* quantity (:unit-amount tier)))]
    (plan-amount-to-human tier-price (:currency plan-data))))

(defn- plan-minimum-price [plan-data]
  (let [tier (first (:tiers plan-data))]
    (plan-amount-to-human (:flat-amount tier) (:currency plan-data))))

(defn- price-per-user [plan-data]
  (let [tier (second (:tiers plan-data))]
    (plan-amount-to-human (:unit-amount tier) (:currency plan-data))))

(defn- plan-description [plan-nickname]
  (case plan-nickname
   "Monthly" "monthly"
   "annually"))

(defn- plan-label [plan-nickname]
  (case plan-nickname
   "Annual" (str plan-nickname " (save 20%)")
   plan-nickname))

(defn- date-string [linux-epoch]
  (.toDateString (utils/js-date (* linux-epoch 1000))))

(defn- is-trial? [subs-data]
  (= (:status subs-data) payments-actions/default-trial-status))

(defn- trial-remaining-days-string [subscription-data]
  (let [remaining-seconds (- (:trial-end subscription-data) (/ (.getTime (utils/js-date)) 1000))
        days-left (inc (int (/ remaining-seconds (* 60 60 24))))]
    (str days-left " day" (when-not (= days-left 1) "s"))))

(defn- trial-info-string [subscription-data]
  (let [trial-end-date (date-string (:trial-end subscription-data))
        remaining-seconds (- (:trial-end subscription-data) (/ (.getTime (utils/js-date)) 1000))
        trial-remaining-string (if (> remaining-seconds (* 60 60 24))
                                 (str " (" (trial-remaining-days-string subscription-data) " left)")
                                 "(today)")]
    (str "Ends on: " trial-end-date trial-remaining-string)))

(defn- plan-summary [s payments-data]
  (if @(::automatic-update-plan s)
    [:div.plan-summary
      [:div.plan-summary-details
        "Updating your plan..."
        (small-loading)]]
    (let [subscription-data (payments-actions/get-active-subscription payments-data)
          next-payment-due (date-string (-> payments-data :upcoming-invoice :next-payment-attempt))
          current-plan (:plan subscription-data)
          checkout-result @(::checkout-result s)
          quantity (-> subscription-data :upcoming-invoice :line-items first :quantity)] ;; Number of active/unverified users
      [:div.plan-summary
        (when (true? checkout-result)
          [:div.plan-summary-details.success.bottom-margin
            [:div.emoji-icon "👍"]
            (str "Your " (s/lower (:nickname current-plan)) " plan is active.")])
        (when (true? checkout-result)
          [:div.plan-summary-separator])
        (if (seq (:payment-methods payments-data))
          [:div.plan-summary-details
            "Payment methods:"
            [:br]
            (for [c (:payment-methods payments-data)
                  :let [card (:card c)]]
              [:div.plan-summary-details-card-row
                {:key (str "pay-method-" (:id c))
                 :class (when-not (:default? c) "hidden")}
                (case (:brand card)
                 "visa" "Visa"
                 "amex" "American Express"
                 "mastercard" "Mastercard"
                 (s/phrase (:brand card)))
                 (when (seq (:last-4 card))
                   (str " ending in " (:last-4 card)))
                 ", exp: " (utils/add-zero (int (:exp-month card))) "/" (:exp-year card)])
                [:button.mlb-reset.change-pay-method-bt
                  {:on-click #(payments-actions/add-payment-method payments-data)}
                  "Add another payment method"]]
          [:div.plan-summary-details
            [:button.mlb-reset.change-pay-method-bt
              {:on-click #(payments-actions/add-payment-method payments-data)}
              "Add a payment method"]])
        (when (is-trial? subscription-data)
          [:div.plan-summary-details.bottom-margin
            "Trial:"
            [:br]
            (trial-info-string subscription-data)])
        (when subscription-data
          [:div.plan-summary-details.bottom-margin
            "Billing period:"
            [:br]
            "Plan billed "
            (plan-description (:nickname current-plan)) " (" (plan-price current-plan quantity) ")"
            [:br]
            "Next payment due on "
            next-payment-due
            [:button.mlb-reset.change-pay-method-bt
              {:on-click #(change-tab s :change)}
              "Change"]])
        (comment
          [:div.plan-summary-separator]
          [:div.plan-summary-details
            [:button.mlb-reset.history-bt
              "Lookup billing history"]]
          [:div.plan-summary-separator]
          [:div.plan-summary-details
            "Have a team of 250+"
            [:a.chat-with-us
              {:class "intercom-chat-link"
               :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
              "Chat with us"]])])))

(defn- show-error-alert [s]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :title "Oops"
                    :message "An error occurred while saving your change of plan, please try again."
                    :solid-button-style :red
                    :solid-button-title "OK, got it"
                    :solid-button-cb alert-modal/hide-alert}]
    (alert-modal/show-alert alert-data)))

(def default-minimum-price 6000)

(defn- save-plan-change [s payments-data current-plan-data]
  (payments-actions/create-plan-subscription payments-data (:id current-plan-data)
   (fn [success]
     (reset! (::saving-plan s) false)
     (if success
       (do
         (change-tab s :summary)
         (reset! (::payments-plan s) (:nickname current-plan-data)))
       (show-error-alert s)))))

(defn- plan-change [s payments-data]
  (let [initial-plan @(::initial-plan s)
        current-plan (::payments-plan s)
        subscription-data (payments-actions/get-active-subscription payments-data)
        quantity (-> subscription-data :upcoming-invoice :line-items first :quantity) ;; Number of active/unverified users
        monthly-plan (first (filter #(= (:amount %) "monthly") (:available-plans payments-data)))
        annual-plan (first (filter #(= (:amount %) "annual") (:available-plans payments-data)))
        
        current-plan-data (if @(::plan-has-changed s)
                            (first (filter #(= (:nickname %) @current-plan) (:available-plans payments-data)))
                            (:plan subscription-data))
        total-plan-price (plan-price current-plan-data quantity)
        up-to (-> current-plan-data :tiers first :up-to)
        flat-amount (plan-amount-to-human (-> current-plan-data :tiers first :flat-amount) (:currency current-plan-data))
        unit-amount (plan-amount-to-human (-> current-plan-data :tiers second :unit-amount) (:currency current-plan-data))
        available-plans (mapv #(hash-map :value (:nickname %) :label (plan-label (:nickname %))) (:available-plans payments-data))
        has-payment-info? (seq (:payment-methods payments-data))
        is-monthly-plan? (= (:nickname current-plan-data) "Monthly")]
    [:div.plan-change
      (when (and (is-trial? subscription-data)
                 (not has-payment-info?))
        [:div.plan-change-details.expiration-trial.bottom-margin
          [:div.emoji-icon "🗓"]
          (str "Your trial plan is set to expire in " (trial-remaining-days-string subscription-data) ".")])
      (when (and (is-trial? subscription-data)
                 (not has-payment-info?))
        [:div.plan-change-separator.bottom-margin])
      [:button.mlb-reset.plans-dropdown-bt
        {:on-click #(reset! (::show-plans-dropdown s) true)}
        (or (:nickname current-plan-data) "Free")]
      [:div.plan-change-dropdown
        (when @(::show-plans-dropdown s)
          (dropdown-list {:items available-plans
                          :value @current-plan
                          :on-blur #(reset! (::show-plans-dropdown s) false)
                          :on-change (fn [selected-item]
                                       (reset! (::plan-has-changed s) true)
                                       (reset! (::show-plans-dropdown s) false)
                                       (reset! current-plan (:value selected-item)))}))]
      (if (= @current-plan "free")
        [:div.plan-change-description
          "Free plan details here"]
        [:div.plan-change-description
           "For your team of "
           quantity
           (if (not= quantity 1)
            " people"
            " person")
           ", your plan will cost "
           total-plan-price
           (if is-monthly-plan?
            " monthly"
            " annually")
           " (" quantity " user" (when (not= quantity 1) "s") " X " (price-per-user current-plan-data) ")."
          (when (< quantity up-to)
            [:br])
          (when (< quantity up-to)
            (str " Up to " up-to " user" (when (not= up-to 1) "s") " is " flat-amount
            (if is-monthly-plan?
              " per month; "
              " per year; ")
            unit-amount " per user after."))
          (when-not is-monthly-plan?
            [:br])
          (when-not is-monthly-plan?
            "An annual plan saves you 20%.")])
      (when-not (payments-actions/default-positive-statuses (:status subscription-data))
        [:div.plan-change-title
          (str "Due today: " total-plan-price)])
      [:button.mlb-reset.payment-info-bt
        {:disabled (or @(::saving-plan s)
                       (and has-payment-info?
                            (= @current-plan initial-plan)))
         :on-click (fn []
                    (if has-payment-info?
                      (let [alert-data {:title "Are you sure?"
                                        :message (str "Are you sure you want to change your current plan to " (:nickname current-plan-data) "?")
                                        :link-button-style :red
                                        :link-button-title "No, keep it"
                                        :link-button-cb #(alert-modal/hide-alert)
                                        :solid-button-style :green
                                        :solid-button-title "Yes, change it"
                                        :solid-button-cb (fn [_]
                                                             (reset! (::saving-plan s) true)
                                                             (save-plan-change s payments-data current-plan-data)
                                                             (alert-modal/hide-alert))}]
                        (alert-modal/show-alert alert-data))
                      (payments-actions/add-payment-method payments-data
                       ;; In case user changed the plan let's add it to the callback
                       ;; so we save once payment method is added
                       (when (not= initial-plan @current-plan)
                        current-plan-data))))}
        (if has-payment-info?
          "Change plan"
          "Add payment information")]
      (when @(::saving-plan s)
        (small-loading))
     [:div.plan-change-separator]
     [:div.plan-change-details
       "Have a team of 250+"
       [:a.chat-with-us
         {:class "intercom-chat-link"
          :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
         "Chat with us"]]]))

(defn- initial-setup
  "Setup the view data, need to make sure the payments data have been loaded to show it."
  [s]
  (let [payments-data @(drv/get-ref s :payments)]
    (when (and (not @(::initial-setup s))
               payments-data)
      (reset! (::initial-setup s) true)
      (let [subscription-data (payments-actions/get-active-subscription payments-data)
            initial-plan (or (-> subscription-data :plan :nickname) "Monthly")
            checkout-result @(drv/get-ref s dis/checkout-result-key)
            has-payment-info? (seq (:payment-methods payments-data))
            updating-plan (when checkout-result
                            @(drv/get-ref s dis/checkout-update-plan-key))]
        (reset! (::payments-tab s) (if (or (not (payments-actions/get-active-subscription payments-data))
                                           (not has-payment-info?))
                                     :change
                                     :summary))
        (reset! (::payments-plan s) initial-plan)
        (reset! (::initial-plan s) initial-plan)
        (reset! (::checkout-result s) checkout-result)
        (reset! (::automatic-update-plan s) updating-plan)
        ;; When the user come back from adding pay info and has a plan id set as GET parameter
        ;; it means we need to change the plan, but only if the add info went well
        (when (and (true? checkout-result)
                   (seq updating-plan))
          (payments-actions/create-plan-subscription payments-data updating-plan
           (fn [{:keys [success]}]
            (reset! (::automatic-update-plan s) nil)
            (when success
              (dis/dispatch! [:input [dis/checkout-update-plan-key] nil])))))))))

(rum/defcs payments-settings-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :payments)
  (drv/drv dis/checkout-result-key)
  (drv/drv dis/checkout-update-plan-key)
  ui-mixins/refresh-tooltips-mixin
  ;; Locals
  (rum/local false ::initial-setup)
  (rum/local :summary ::payments-tab)
  (rum/local nil ::payments-plan)
  (rum/local false ::show-plans-dropdown)
  (rum/local nil ::initial-plan)
  (rum/local false ::plan-has-changed)
  (rum/local false ::checkout-result)
  (rum/local false ::saving-plan)
  (rum/local nil ::automatic-update-plan)
  {:will-mount (fn [s]
    ;; Force refresh subscription data
    (payments-actions/maybe-load-payments-data @(drv/get-ref s :org-data) true)
    (initial-setup s)
    s)
    :will-update (fn [s]
     (initial-setup s)
     s)
    :will-unmount (fn [s]
     (dis/dispatch! [:input [dis/checkout-result-key] nil])
     s)}
  [s {:keys [org-data]}]
  (let [org-data (drv/react s :org-data)
        payments-tab (::payments-tab s)
        is-change-tab? (= @payments-tab :change)
        payments-data (drv/react s :payments)
        has-payment-info? (seq (:payment-methods payments-data))]
    [:div.payments-settings-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click #(nav-actions/close-all-panels)}]
      [:div.payments-settings-modal-container
        [:div.payments-settings-header.group
          [:div.payments-settings-header-title
            (if (and is-change-tab?
                     payments-data)
             (if has-payment-info?
               "Change plan"
               "Select a plan")
             "Billing")]
          (when (and payments-data
                     (not is-change-tab?)
                     (nil? @(::checkout-result s)))
            [:button.mlb-reset.save-bt
              {:on-click #(change-tab s :change)}
              "Change plan"])
          (when (and payments-data
                     (nil? @(::checkout-result s))
                     has-payment-info?)
            [:button.mlb-reset.cancel-bt
              {:on-click #(if is-change-tab?
                            (change-tab s :summary)
                            (nav-actions/show-org-settings nil))}
              "Back"])]
        [:div.payments-settings-body
          (if-not payments-data
            (small-loading)
            (if is-change-tab?
              (plan-change s payments-data)
              (plan-summary s payments-data)))]]]))