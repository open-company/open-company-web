(ns oc.web.components.payments-settings-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.urls :as oc-urls]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
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

(defn- price-amount-to-human [amount currency]
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

(defn- price-value [price-data quantity]
  (if-not (seq (:tiers price-data))
    (price-amount-to-human (* (:amount price-data) quantity) (:currency price-data))
    (let [tier (first (filterv #(or (and (:up-to %) (<= quantity (:up-to %))) (not (:up-to %)))
                (:tiers price-data)))
          tier-price (if (:up-to tier)
                       (+ (:flat-amount tier) (* quantity (:unit-amount tier)))
                       (* quantity (:unit-amount tier)))]
      (price-amount-to-human tier-price (:currency price-data)))))

(defn- price-per-user [price-data]
  (let [tier (second (:tiers price-data))]
    (price-amount-to-human (:unit-amount tier) (:currency price-data))))

(defn- price-description [price-interval]
  (case price-interval
   "month" "monthly"
   "annual"))

(defn- price-label [price-nickname]
  (case price-nickname
   "Annual" (str price-nickname " (save 20%)")
   price-nickname))

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
                                         (payments-actions/delete-price-subscription payments-data
                                          #(reset! (::canceling-subscription s) false))
                                         (alert-modal/hide-alert))}]
    (alert-modal/show-alert alert-data)))

(defn- price-summary [s payments-data]
  (let [subscription-data (payments-actions/get-active-subscription payments-data)
        next-payment-due (date-string (-> payments-data :upcoming-invoice :next-payment-attempt))
        current-price (:price subscription-data)
        checkout-result @(::checkout-result s)
        quantity (:quantity subscription-data)] ;; Number of active/unverified users
    [:div.price-summary
      (when subscription-data
        (if (:cancel-at-period-end? subscription-data)
          [:div.price-summary-details.success.bottom-margin
            [:div.emoji-icon "🗓"]
            (str "Your " (s/lower (:nickname current-price)) " plan is set to cancel on " (date-string (:current-period-end subscription-data)) ".")]
          [:div.price-summary-details.success.bottom-margin
            [:div.emoji-icon "👍"]
            (str "Your " (s/lower (:nickname current-price)) " plan is active.")]))
      (when subscription-data
        [:div.price-summary-separator.bottom-margin])
      (if (seq (:payment-methods payments-data))
        [:div.price-summary-details
          [:strong "Payment methods"]
          [:br]
          (for [c (:payment-methods payments-data)
                :let [card (:card c)]]
            [:div.price-summary-details-card-row
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
                {:on-click #(payments-actions/open-checkout! payments-data)}
                "Update payment information"]]
        [:div.price-summary-details
          [:button.mlb-reset.change-pay-method-bt
            {:on-click #(payments-actions/open-checkout! payments-data)}
            (str "Subscribe to " ls/product-name)]])
      (when (or (is-trial? subscription-data)
                (is-trial-expired? subscription-data))
        [:div.price-summary-details.bottom-margin
          [:strong "Trial"]
          [:br]
          (trial-remaining-days-string subscription-data)])
      (when subscription-data
        [:div.price-summary-details.bottom-margin
          [:strong "Billing period"]
          [:br]
          "Plan billed "
          (price-description (:interval current-price)) " (" (price-value current-price quantity) ")"
          [:br]
          (if (:cancel-at-period-end? subscription-data)
            (str "Your plan is scheduled to cancel on " (date-string (:current-period-end subscription-data)))
            (str "Next payment due on " next-payment-due))
          [:button.mlb-reset.change-pay-method-bt
            {:on-click #(change-tab s :change)}
            "Change"]])
      (when (and subscription-data
                 (not (:cancel-at-period-end? subscription-data)))
        [:div.price-summary-details
          [:button.mlb-reset.cancel-subscription-bt
            {:on-click #(cancel-subscription s payments-data)}
            "Cancel subscription"]
          (when @(::canceling-subscription s)
            (small-loading))])
      (comment
        [:div.price-summary-separator]
        [:div.price-summary-details
          [:button.mlb-reset.history-bt
            "Lookup billing history"]]
        [:div.price-summary-separator]
        [:div.price-summary-details
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

(defn- save-price-change [s payments-data current-price-data]
  (payments-actions/create-price-subscription payments-data (:id current-price-data)
   (fn [success]
     (reset! (::saving-price s) false)
     (if success
       (do
         (change-tab s :summary)
         (reset! (::initial-price s) (:nickname current-price-data))
         (reset! (::payments-price s) (:nickname current-price-data)))
       (show-error-alert s)))))

(defn- different-prices-price [prices-data quantity]
  (let [annual-price-data (first (filter #(= (:interval %) "year") prices-data))
        monthly-price-data (first (filter #(= (:interval %) "month") prices-data))
        annual-tier (first (filterv #(or (and (:up-to %) (<= quantity (:up-to %)))
                                         (not (:up-to %))) (:tiers annual-price-data)))
        monthly-tier (first (filterv #(or (and (:up-to %) (<= quantity (:up-to %)))
                                          (not (:up-to %))) (:tiers monthly-price-data)))
        annual-price (if-not (nil? (:up-to annual-tier))
                       (+ (:flat-amount annual-tier) (* quantity (:unit-amount annual-tier)))
                       (* (int quantity) (int (:unit-amount annual-tier))))
        monthly-price (if-not (nil? (:up-to monthly-tier))
                       (+ (:flat-amount monthly-tier) (* quantity (:unit-amount monthly-tier)))
                       (* quantity (:unit-amount monthly-tier)))
        diff-price (- (* monthly-price 12) annual-price)]
    (when (pos? diff-price)
      [:span " An annual plan saves you "
        [:strong (price-amount-to-human diff-price (:currency annual-price-data))]
        " per year."])))

(defn- price-change [s payments-data]
  (let [initial-price @(::initial-price s)
        current-price (::payments-price s)
        subscription-data (payments-actions/get-active-subscription payments-data)
        quantity (:quantity subscription-data) ;; Number of active/unverified users
        monthly-price (first (filter #(= (:interval %) "month") (:available-prices payments-data)))
        annual-price (first (filter #(= (:interval %) "year") (:available-prices payments-data)))
        current-price-data (if (and @(::price-has-changed s)
                                   (not= @current-price initial-price))
                            (first (filter #(= (:nickname %) @current-price) (:available-prices payments-data)))
                            (:price subscription-data))
        total-price-value (price-value current-price-data quantity)
        different-prices-price-span (different-prices-price (:available-prices payments-data) quantity)
        up-to (-> current-price-data :tiers first :up-to)
        flat-amount (price-amount-to-human (-> current-price-data :tiers first :flat-amount) (:currency current-price-data))
        unit-amount (if (seq (:tiers current-price-data))
                      (price-amount-to-human (-> current-price-data :tiers second :unit-amount) (:currency current-price-data))
                      (price-amount-to-human (:amount current-price-data) (:currency current-price-data)))
        available-prices* (mapv #(hash-map :value (:nickname %) :label (price-label (:nickname %))) (:available-prices payments-data))
        contains-current-subscription? (some #(= (:value %) (:nickname (:price subscription-data))) available-prices*)
        available-prices (if contains-current-subscription?
                          available-prices*
                          (concat
                           [{:value (:nickname (:price subscription-data)) :label (price-label (:nickname (:price subscription-data)))}]
                           available-prices*))
        has-payment-info? (seq (:payment-methods payments-data))
        is-annual-default-price? (= (:nickname current-price-data) "Annual")
        is-under-up-to? (< quantity up-to)]
    [:div.price-change
      (when (and (or (is-trial? subscription-data)
                     (is-trial-expired? subscription-data))
                 (not has-payment-info?))
        [:div.price-change-details.expiration-trial.bottom-margin
          [:div.emoji-icon "🗓"]
          (trial-remaining-days-string subscription-data)])
      (when (and (is-trial? subscription-data)
                 (not has-payment-info?))
        [:div.price-change-separator.bottom-margin])
      [:button.mlb-reset.prices-dropdown-bt
        {:on-click #(reset! (::show-prices-dropdown s) true)}
        (or (:nickname current-price-data) "Free")]
      [:div.price-change-dropdown
        (when @(::show-prices-dropdown s)
          (dropdown-list {:items available-prices
                          :value @current-price
                          :on-blur #(reset! (::show-prices-dropdown s) false)
                          :on-change (fn [selected-item]
                                       (reset! (::price-has-changed s) true)
                                       (reset! (::show-prices-dropdown s) false)
                                       (reset! current-price (:value selected-item)))}))]
      (if is-under-up-to?
        [:div.price-change-description
          (str
            "The "
            (s/lower (:nickname current-price-data))
            " plan "
            (when is-annual-default-price?
              (str "is 20% lower than monthly. The " (s/lower (:nickname current-price-data)) " plan "))
            "starts at ")
          [:strong flat-amount]
          (str
            ", which includes your first " up-to
            " team members"
            ". Then it's ")
          [:strong unit-amount ]
          " per additional person."
          (when is-annual-default-price?
            different-prices-price-span)]
        [:div.price-change-description
          (str
            "For your team of "
            quantity
            (if (not= quantity 1)
              " people"
              " person")
            ", your plan will cost ")
          [:strong total-price-value]
          (str 
            " per " (:interval current-price-data)
            " (" quantity " user" (when (not= quantity 1) "s") " X " unit-amount ").")
          (when is-annual-default-price?
            different-prices-price-span)])
      (when-not (payments-actions/default-positive-statuses (:status subscription-data))
        [:div.price-change-title
          (str "Due today: " total-price-value)])
      [:button.mlb-reset.payment-info-bt
        {:disabled (or @(::saving-price s)
                       @(::canceling-subscription s)
                       (and has-payment-info?
                            (= @current-price initial-price)
                            (not (:cancel-at-period-end? subscription-data))))
         :on-click (fn []
                    (if has-payment-info?
                      (if (:cancel-at-period-end? subscription-data)
                        ;; If user cancelled we don't ask for confirmation,
                        ;; let's restart the subscription immediately
                        (do
                          (reset! (::saving-price s) true)
                          (save-price-change s payments-data current-price-data))
                        (let [alert-data {:title "Are you sure?"
                                          :message "Are you sure you want to change your current plan?"
                                          :link-button-style :red
                                          :link-button-title "No, keep it"
                                          :link-button-cb #(alert-modal/hide-alert)
                                          :solid-button-style :green
                                          :solid-button-title "Yes, change it"
                                          :solid-button-cb (fn [_]
                                                               (reset! (::saving-price s) true)
                                                               (save-price-change s payments-data current-price-data)
                                                               (alert-modal/hide-alert))}]
                          (alert-modal/show-alert alert-data)))
                      (payments-actions/open-checkout! payments-data
                       ;; In case user changed the price let's add it to the callback
                       ;; so we save once payment method is added
                       (when (not= initial-price @current-price)
                        current-price-data))))}
        (if has-payment-info?
          (if (:cancel-at-period-end? subscription-data)
            (str "Subscribe to " ls/product-name)
            "Change plan")
          (str "Subscribe to " ls/product-name))]
      (when @(::saving-price s)
        (small-loading))
     [:div.price-change-separator]
     [:div.price-change-details
       "Have a question about billing?"
       [:br]
       [:a.chat-with-us
         {:class "intercom-chat-link"
          :href oc-urls/contact-mail-to}
         "Chat with us"]]
     [:div.price-change-details
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
        current-user-data @(drv/get-ref s :current-user-data)]
    ;; If user is not an admin
    (when (and org-data
               current-user-data
               (:role current-user-data)
               (not= (:role current-user-data) :admin))
      ;; Dismiss payments panel
      (nav-actions/close-all-panels))
    (when (and (not @(::initial-setup s))
               payments-data)
      (reset! (::initial-setup s) true)
      (let [subscription-data (payments-actions/get-active-subscription payments-data)
            initial-price-nickname (or (-> subscription-data :price :nickname) "Monthly") ;; Default to the monthly default price
            checkout-result @(drv/get-ref s dis/checkout-result-key)
            has-payment-info? (:payment-method-on-file? payments-data)]
        (reset! (::payments-tab s) (if (or (not (payments-actions/get-active-subscription payments-data))
                                           (not has-payment-info?))
                                     :change
                                     :summary))
        (reset! (::payments-price s) initial-price-nickname)
        (reset! (::initial-price s) initial-price-nickname)
        (reset! (::checkout-result s) checkout-result)))))

(rum/defcs payments-settings-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :payments)
  (drv/drv :current-user-data)
  (drv/drv dis/checkout-result-key)
  (drv/drv dis/checkout-update-price-key)
  ui-mixins/refresh-tooltips-mixin
  ;; Locals
  (rum/local false ::initial-setup)
  (rum/local :summary ::payments-tab)
  (rum/local nil ::payments-price)
  (rum/local false ::show-prices-dropdown)
  (rum/local nil ::initial-price)
  (rum/local false ::price-has-changed)
  (rum/local false ::checkout-result)
  (rum/local false ::saving-price)
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
        {:keys [portal-link can-open-portal?] :as payments-data} (drv/react s :payments)
        has-payment-info? (seq (:payment-methods payments-data))]
    [:div.payments-settings-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click #(nav-actions/close-all-panels)}]
     [:div.payments-settings-modal-container
      [:div.payments-settings-header.group
       [:div.payments-settings-header-title
        "Billing"]
       (when (and payments-data
                  (nil? @(::checkout-result s))
                  has-payment-info?)
         [:button.mlb-reset.cancel-bt
          {:on-click #(nav-actions/show-org-settings nil)}
          "Back"])]
      [:div.payments-settings-body
       (when (:can-open-portal? payments-data)
         [:div.payments-settings-header-title
          "Change your plan:"]
         [:button.mlb-reset.go-premium-button
          {:on-click  #(if has-payment-info?
                         (payments-actions/open-portal! payments-data)
                         (payments-actions/open-checkout! payments-data))}
          "Go Premium!"])]]]))