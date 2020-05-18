(ns oc.web.components.payments-settings-modal
  (:require [rum.core :as rum]
            [clojure.contrib.humanize :refer (intcomma)]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.stores.user :as user-store]
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
  (if-not (seq (:tiers plan-data))
    (plan-amount-to-human (* (:amount plan-data) quantity) (:currency plan-data))
    (let [tier (first (filterv #(or (and (:up-to %) (<= quantity (:up-to %))) (not (:up-to %)))
                (:tiers plan-data)))
          tier-price (if (:up-to tier)
                       (+ (:flat-amount tier) (* quantity (:unit-amount tier)))
                       (* quantity (:unit-amount tier)))]
      (plan-amount-to-human tier-price (:currency plan-data)))))

(defn- plan-minimum-price [plan-data]
  (let [tier (first (:tiers plan-data))]
    (plan-amount-to-human (:flat-amount tier) (:currency plan-data))))

(defn- price-per-user [plan-data]
  (let [tier (second (:tiers plan-data))]
    (plan-amount-to-human (:unit-amount tier) (:currency plan-data))))

(defn- plan-description [plan-interval]
  (case plan-interval
   "month" "monthly"
   "annual"))

(defn- plan-label [plan-nickname]
  (case plan-nickname
   "Annual" (str plan-nickname " (save 20%)")
   plan-nickname))

(defn- date-string [linux-epoch]
  (.toDateString (utils/js-date (* linux-epoch 1000))))

(defn- is-trial? [subs-data]
  (= (:status subs-data) payments-actions/default-trial-status))

(defn- is-trial-expired? [subs-data]
  (= (:status subs-data) payments-actions/default-trial-expired-status))

(defn- trial-remaining-days-string [subscription-data]
  (let [remaining-seconds (- (:trial-end subscription-data) (/ (.getTime (utils/js-date)) 1000))
        days-left (inc (int (/ remaining-seconds (* 60 60 24))))]
    (if (neg? remaining-seconds)
      "Your trial has ended. Please select a plan to continue."
      (str "Your trial is set to expire in " days-left " day" (when-not (= days-left 1) "s") ". Please choose a plan."))))

(defn- cancel-subscription [s payments-data]
  (let [alert-data {:title "Are you sure?"
                    :message "Are you sure you want to cancel your current plan?"
                    :link-button-style :red
                    :link-button-title "No, keep it"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :green
                    :solid-button-title "Yes, cancel it"
                    :solid-button-cb (fn [_]
                                         (reset! (::canceling-subscription s) true)
                                         (payments-actions/delete-plan-subscription payments-data
                                          #(reset! (::canceling-subscription s) false))
                                         (alert-modal/hide-alert))}]
    (alert-modal/show-alert alert-data)))

(defn- plan-summary [s payments-data]
  (let [subscription-data (payments-actions/get-active-subscription payments-data)
        next-payment-due (date-string (-> payments-data :upcoming-invoice :next-payment-attempt))
        current-plan (:plan subscription-data)
        checkout-result @(::checkout-result s)
        quantity (:quantity subscription-data)] ;; Number of active/unverified users
    [:div.plan-summary
      (when subscription-data
        (if (:cancel-at-period-end? subscription-data)
          [:div.plan-summary-details.success.bottom-margin
            [:div.emoji-icon "🗓"]
            (str "Your " (s/lower (:nickname current-plan)) " plan is set to cancel on " (date-string (:current-period-end subscription-data)) ".")]
          [:div.plan-summary-details.success.bottom-margin
            [:div.emoji-icon "👍"]
            (str "Your " (s/lower (:nickname current-plan)) " plan is active.")]))
      (when subscription-data
        [:div.plan-summary-separator.bottom-margin])
      (if (seq (:payment-methods payments-data))
        [:div.plan-summary-details
          [:strong "Payment methods"]
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
                "Update payment information"]]
        [:div.plan-summary-details
          [:button.mlb-reset.change-pay-method-bt
            {:on-click #(payments-actions/add-payment-method payments-data)}
            "Subscribe to Wut"]])
      (when (or (is-trial? subscription-data)
                (is-trial-expired? subscription-data))
        [:div.plan-summary-details.bottom-margin
          [:strong "Trial"]
          [:br]
          (trial-remaining-days-string subscription-data)])
      (when subscription-data
        [:div.plan-summary-details.bottom-margin
          [:strong "Billing period"]
          [:br]
          "Plan billed "
          (plan-description (:interval current-plan)) " (" (plan-price current-plan quantity) ")"
          [:br]
          (if (:cancel-at-period-end? subscription-data)
            (str "Your plan is scheduled to cancel on " (date-string (:current-period-end subscription-data)))
            (str "Next payment due on " next-payment-due))
          [:button.mlb-reset.change-pay-method-bt
            {:on-click #(change-tab s :change)}
            "Change"]])
      (when (and subscription-data
                 (not (:cancel-at-period-end? subscription-data)))
        [:div.plan-summary-details
          [:button.mlb-reset.cancel-subscription-bt
            {:on-click #(cancel-subscription s payments-data)}
            "Cancel subscription"]
          (when @(::canceling-subscription s)
            (small-loading))])
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
             :href oc-urls/contact-mail-to}
            "Chat with us"]])]))

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
         (reset! (::initial-plan s) (:nickname current-plan-data))
         (reset! (::payments-plan s) (:nickname current-plan-data)))
       (show-error-alert s)))))

(defn- different-plans-price [plans-data quantity]
  (let [annual-plan-data (first (filter #(= (:interval %) "year") plans-data))
        monthly-plan-data (first (filter #(= (:interval %) "month") plans-data))
        annual-tier (first (filterv #(or (and (:up-to %) (<= quantity (:up-to %)))
                                         (not (:up-to %))) (:tiers annual-plan-data)))
        monthly-tier (first (filterv #(or (and (:up-to %) (<= quantity (:up-to %)))
                                          (not (:up-to %))) (:tiers monthly-plan-data)))
        annual-price (if-not (nil? (:up-to annual-tier))
                       (+ (:flat-amount annual-tier) (* quantity (:unit-amount annual-tier)))
                       (* (int quantity) (int (:unit-amount annual-tier))))
        monthly-price (if-not (nil? (:up-to monthly-tier))
                       (+ (:flat-amount monthly-tier) (* quantity (:unit-amount monthly-tier)))
                       (* quantity (:unit-amount monthly-tier)))
        diff-price (- (* monthly-price 12) annual-price)]
    (when (pos? diff-price)
      [:span " An annual plan saves you "
        [:strong (plan-amount-to-human diff-price (:currency annual-plan-data))]
        " per year."])))

(defn- plan-change [s payments-data]
  (let [initial-plan @(::initial-plan s)
        current-plan (::payments-plan s)
        subscription-data (payments-actions/get-active-subscription payments-data)
        quantity (:quantity subscription-data) ;; Number of active/unverified users
        monthly-plan (first (filter #(= (:interval %) "month") (:available-plans payments-data)))
        annual-plan (first (filter #(= (:interval %) "year") (:available-plans payments-data)))
        current-plan-data (if (and @(::plan-has-changed s)
                                   (not= @current-plan initial-plan))
                            (first (filter #(= (:nickname %) @current-plan) (:available-plans payments-data)))
                            (:plan subscription-data))
        total-plan-price (plan-price current-plan-data quantity)
        different-plans-price-span (different-plans-price (:available-plans payments-data) quantity)
        up-to (-> current-plan-data :tiers first :up-to)
        flat-amount (plan-amount-to-human (-> current-plan-data :tiers first :flat-amount) (:currency current-plan-data))
        unit-amount (if (seq (:tiers current-plan-data))
                      (plan-amount-to-human (-> current-plan-data :tiers second :unit-amount) (:currency current-plan-data))
                      (plan-amount-to-human (:amount current-plan-data) (:currency current-plan-data)))
        available-plans* (mapv #(hash-map :value (:nickname %) :label (plan-label (:nickname %))) (:available-plans payments-data))
        contains-current-subscription? (some #(= (:value %) (:nickname (:plan subscription-data))) available-plans*)
        available-plans (if contains-current-subscription?
                          available-plans*
                          (concat
                           [{:value (:nickname (:plan subscription-data)) :label (plan-label (:nickname (:plan subscription-data)))}]
                           available-plans*))
        has-payment-info? (seq (:payment-methods payments-data))
        is-annual-default-plan? (= (:nickname current-plan-data) "Annual")
        is-under-up-to? (< quantity up-to)]
    [:div.plan-change
      (when (and (or (is-trial? subscription-data)
                     (is-trial-expired? subscription-data))
                 (not has-payment-info?))
        [:div.plan-change-details.expiration-trial.bottom-margin
          [:div.emoji-icon "🗓"]
          (trial-remaining-days-string subscription-data)])
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
      (if is-under-up-to?
        [:div.plan-change-description
          (str
            "The "
            (s/lower (:nickname current-plan-data))
            " plan "
            (when is-annual-default-plan?
              (str "is 20% lower than monthly. The " (s/lower (:nickname current-plan-data)) " plan "))
            "starts at ")
          [:strong flat-amount]
          (str
            ", which includes your first " up-to
            " team members"
            ". Then it's ")
          [:strong unit-amount ]
          " per additional person."
          (when is-annual-default-plan?
            different-plans-price-span)]
        [:div.plan-change-description
          (str
            "For your team of "
            quantity
            (if (not= quantity 1)
              " people"
              " person")
            ", your plan will cost ")
          [:strong total-plan-price]
          (str 
            " per " (:interval current-plan-data)
            " (" quantity " user" (when (not= quantity 1) "s") " X " unit-amount ").")
          (when is-annual-default-plan?
            different-plans-price-span)])
      (when-not (payments-actions/default-positive-statuses (:status subscription-data))
        [:div.plan-change-title
          (str "Due today: " total-plan-price)])
      [:button.mlb-reset.payment-info-bt
        {:disabled (or @(::saving-plan s)
                       @(::canceling-subscription s)
                       (and has-payment-info?
                            (= @current-plan initial-plan)
                            (not (:cancel-at-period-end? subscription-data))))
         :on-click (fn []
                    (if has-payment-info?
                      (if (:cancel-at-period-end? subscription-data)
                        ;; If user cancelled we don't ask for confirmation,
                        ;; let's restart the subscription immediately
                        (do
                          (reset! (::saving-plan s) true)
                          (save-plan-change s payments-data current-plan-data))
                        (let [alert-data {:title "Are you sure?"
                                          :message "Are you sure you want to change your current plan?"
                                          :link-button-style :red
                                          :link-button-title "No, keep it"
                                          :link-button-cb #(alert-modal/hide-alert)
                                          :solid-button-style :green
                                          :solid-button-title "Yes, change it"
                                          :solid-button-cb (fn [_]
                                                               (reset! (::saving-plan s) true)
                                                               (save-plan-change s payments-data current-plan-data)
                                                               (alert-modal/hide-alert))}]
                          (alert-modal/show-alert alert-data)))
                      (payments-actions/add-payment-method payments-data
                       ;; In case user changed the plan let's add it to the callback
                       ;; so we save once payment method is added
                       (when (not= initial-plan @current-plan)
                        current-plan-data))))}
        (if has-payment-info?
          (if (:cancel-at-period-end? subscription-data)
            "Subscribe to Wut"
            "Change plan")
          "Subscribe to Wut")]
      (when @(::saving-plan s)
        (small-loading))
     [:div.plan-change-separator]
     [:div.plan-change-details
       "Have a question about billing?"
       [:br]
       [:a.chat-with-us
         {:class "intercom-chat-link"
          :href oc-urls/contact-mail-to}
         "Chat with us"]]
     [:div.plan-change-details
       "Team of 250+? "
       [:a.chat-with-us
         {:class "intercom-chat-link"
          :href oc-urls/contact-mail-to}
         "Contact us"]
       " about an enterprise plan."]]))

(defn- initial-setup
  "Setup the view data, need to make sure the payments data have been loaded to show it."
  [s]
  (let [payments-data @(drv/get-ref s :payments)
        org-data @(drv/get-ref s :org-data)
        current-user-data @(drv/get-ref s :current-user-data)
        user-role (user-store/user-role org-data current-user-data)]
    ;; If user is not an admin
    (when (and org-data
               current-user-data
               (seq (str user-role))
               (not= user-role :admin))
      ;; Dismiss payments panel
      (nav-actions/close-all-panels))
    (when (and (not @(::initial-setup s))
               payments-data)
      (reset! (::initial-setup s) true)
      (let [subscription-data (payments-actions/get-active-subscription payments-data)
            initial-plan-nickname (or (-> subscription-data :plan :nickname) "Monthly") ;; Default to the monthly default plan
            checkout-result @(drv/get-ref s dis/checkout-result-key)
            has-payment-info? (seq (:payment-methods payments-data))
            updating-plan (when checkout-result
                            @(drv/get-ref s dis/checkout-update-plan-key))]
        (reset! (::payments-tab s) (if (or (not (payments-actions/get-active-subscription payments-data))
                                           (not has-payment-info?))
                                     :change
                                     :summary))
        (reset! (::payments-plan s) initial-plan-nickname)
        (reset! (::initial-plan s) initial-plan-nickname)
        (reset! (::checkout-result s) checkout-result)))))

(rum/defcs payments-settings-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :payments)
  (drv/drv :current-user-data)
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
  (rum/local false ::canceling-subscription)
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
              {:on-click #(change-tab s :change)
               :disabled (or @(::saving-plan s)
                             @(::canceling-subscription s))}
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