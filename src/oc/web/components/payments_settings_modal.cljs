(ns oc.web.components.payments-settings-modal
  (:require [rum.core :as rum]
            [clojure.contrib.humanize :refer (intcomma)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.payments :as payments-actions]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.alert-modal :as alert-modal]))

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

(defn- plan-description [plan-nickname]
  (case plan-nickname
   "Monthly" "monthly"
   "annually"))

(defn- date-string [linux-epoch]
  (.toDateString (utils/js-date (* linux-epoch 1000))))

(defn- plan-summary [s payments-data]
  (let [subscription-data (:subscription payments-data)
        is-trial? (= (:status subscription-data) payments-actions/default-trial-status)
        trial-end-date (when is-trial? (date-string (:trial-end subscription-data)))
        remaining-seconds (- (:trial-end subscription-data) (/ (.getTime (utils/js-date)) 1000))
        trial-remaining-string (when is-trial?
                                 (if (> remaining-seconds (* 60 60 24))
                                   (let [days-left (int (/ remaining-seconds (* 60 60 24)))]
                                    (str " (" days-left " day" (when-not (= days-left 1) "s") " left)"))
                                   "(today)"))
        next-payment-due (date-string (:current-period-end subscription-data))
        current-plan (:current-plan subscription-data)]
    [:div.plan-summary
      (when (:payment-method payments-data)
        [:div.plan-summary-details
          "Payment method:"
          [:br]
          "Visa ending in 8059, exp: 02/2022"
          [:button.mlb-reset.change-pay-method-bt
            "Change"]])
      (when is-trial?
        [:div.plan-summary-details.bottom-margin
          "Trial:"
          [:br]
          "Started on: " (date-string (:trial-start subscription-data))
          [:br]
          "Ends on: " trial-end-date trial-remaining-string])
      (when subscription-data
        [:div.plan-summary-details.bottom-margin
          "Billing period:"
          [:br]
          "Plan billed "
          (plan-description (:nickname current-plan)) " (" (plan-amount-to-human (:amount current-plan) (:currency current-plan)) ")"
          [:br]
          "Next payment due on "
          next-payment-due
          [:button.mlb-reset.change-pay-method-bt
            {:on-click #(reset! (::payments-tab s) :change)}
            "Change"]])
      [:div.plan-summary-separator]
      [:div.plan-summary-details
        [:button.mlb-reset.history-bt
          "Lookup billing history"]]
      (comment
        [:div.plan-summary-separator]
        [:div.plan-summary-details
          "Have a team of 250+"
          [:a.chat-with-us
            {:class "intercom-chat-link"
             :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
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

(defn- plan-change [s team-data payments-data]
  (let [initial-plan @(::initial-plan s)
        current-plan (::payments-plan s)
        active-users (filterv #(#{"active" "unverified"} (:status %)) (:users team-data))
        monthly-plan (first (filter #(= (:amount %) "monthly") (:available-plans payments-data)))
        annual-plan (first (filter #(= (:amount %) "annual") (:available-plans payments-data)))
        current-plan-data (if @(::plan-has-changed s)
                            (first (filter #(= (:interval %) @current-plan) (:available-plans payments-data)))
                            (:current-plan (:subscription payments-data)))
        total-plan-price* (* (:amount current-plan-data) (count active-users))
        total-plan-price (plan-amount-to-human total-plan-price* (:currency current-plan-data))
        available-plans (mapv #(hash-map :value (:interval %) :label (:nickname %)) (:available-plans payments-data))]
    [:div.plan-change
      [:button.mlb-reset.plans-dropdown-bt
        {:on-click #(reset! (::show-plans-dropdown s) true)}
        (or (:nickname current-plan-data) "Free")]
      (when @(::show-plans-dropdown s)
        (dropdown-list {:items available-plans
                        :value @current-plan
                        :on-blur #(reset! (::show-plans-dropdown s) false)
                        :on-change (fn [selected-item]
                                     (reset! (::plan-has-changed s) true)
                                     (reset! (::show-plans-dropdown s) false)
                                     (reset! current-plan (:value selected-item)))}))
      [:div.plan-change-description
        (if (= @current-plan "free")
          "Free plan details here"
          (str
           "For your team of "
           (count active-users) ;; Number of active/unverified users
           " people, your plan will cost "
           total-plan-price
           (if (= (:interval current-plan) "month")
            " monthly."
            " annually.")))]
      [:div.plan-change-title
        (str "Due today: " total-plan-price)]
      (when (not= initial-plan @current-plan)
        [:button.mlb-reset.payment-info-bt
          {:on-click (fn []
                      (let [alert-data {:title "Are you sure?"
                                        :message (str "Are you sure you want to change your current plan to " (:nickname current-plan-data) "?")
                                        :link-button-style :red
                                        :link-button-title "No, keep it"
                                        :link-button-cb #(alert-modal/hide-alert)
                                        :solid-button-style :green
                                        :solid-button-title "Yes, change it"
                                        :solid-button-cb #(do
                                                           (payments-actions/patch-plan-subscription payments-data (:id current-plan-data)
                                                            (fn [success]
                                                              (if success
                                                                (reset! (::payments-tab s) :summary)
                                                                (show-error-alert s))))
                                                           (alert-modal/hide-alert))}]
                        (alert-modal/show-alert alert-data)))}
          "Add payment information"])
      ; (comment
       [:div.plan-change-separator]
       [:div.plan-change-details
         "Have a team of 250+"
         [:a.chat-with-us
           {:class "intercom-chat-link"
            :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
           "Chat with us"]];)
  ]))

(rum/defcs payments-settings-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :team-data)
  (drv/drv :payments)
  ui-mixins/refresh-tooltips-mixin
  ;; Locals
  (rum/local :summary ::payments-tab)
  (rum/local nil ::payments-plan)
  (rum/local false ::show-plans-dropdown)
  (rum/local nil ::initial-plan)
  (rum/local false ::plan-has-changed)
  {:will-mount (fn [s]
    (let [payments-data @(drv/get-ref s :payments)
          initial-plan (or (-> payments-data :subscription :current-plan :nickname) "free")]
      (reset! (::payments-tab s) (if-not (:subscription payments-data) :change :summary))
      (reset! (::payments-plan s) initial-plan)
      (reset! (::initial-plan s) initial-plan))
    s)}
  [s {:keys [org-data]}]
  (let [team-data (drv/react s :team-data)
        payments-tab (::payments-tab s)
        is-change-tab? (= @payments-tab :change)
        payments-data (drv/react s :payments)]
    [:div.payments-settings-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click #(nav-actions/close-all-panels)}]
      [:div.payments-settings-modal-container
        [:div.payments-settings-header
          [:div.payments-settings-header-title
            (if is-change-tab?
             "Change plan"
             "Billing")]
          (when-not is-change-tab?
            [:button.mlb-reset.save-bt
              {:on-click #(reset! payments-tab :change)}
              "Change plan"])
          [:button.mlb-reset.cancel-bt
            {:on-click #(if is-change-tab?
                          (reset! payments-tab :summary)
                          (nav-actions/show-org-settings nil))}
            "Back"]]
        [:div.payments-settings-body
          (if is-change-tab?
            (plan-change s team-data payments-data)
            (plan-summary s payments-data))]]]))