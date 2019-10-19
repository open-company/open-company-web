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

(defn- plan-summary [s payments-data]
  [:div.plan-summary
    [:div.plan-summary-details
      (when (:payment-method payments-data)
        [:div
          "Payment method:"
          [:br]
          "Visa ending in 8059, exp: 02/2022"
          [:button.mlb-reset.change-pay-method-bt
            "Change"]])]
    [:div.plan-summary-details.bottom-margin
      (when (:subscription payments-data)
        [:div
          "Billing period:"
          [:br]
          (str "Plan billed "
           (when (= (-> payments-data :subscription :interval) "annual")
             "annually"
             "monthly")
          " ($...)")
          [:br]
          "Next payment due on ..."
          [:button.mlb-reset.change-pay-method-bt
            {:on-click #(reset! (::payments-tab s) :change)}
            "Change"]])]
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
          "Chat with us"]])])

(defn- show-error-alert [s]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :title "Oops"
                    :message "An error occurred while saving your change of plan, please try again."
                    :solid-button-style :red
                    :solid-button-title "Yes, change it"
                    :solid-button-cb alert-modal/hide-alert}]
    (alert-modal/show-alert alert-data)))

(def default-minimum-price 6000)

(defn- plan-change [s team-data payments-data]
  (let [initial-plan @(::initial-plan s)
        current-plan (::payments-plan s)
        active-users (filterv #(#{"active" "unverified"} (:status %)) (:users team-data))
        monthly-plan (first (filter #(= (:amount %) "monthly") (:available-plans payments-data)))
        annual-plan (first (filter #(= (:amount %) "annual") (:available-plans payments-data)))
        current-plan-data (first (filter #(= (:interval %) @current-plan) (:available-plans payments-data)))
        total-plan-price* (* (:amount current-plan-data) (count active-users))
        int-plan-price (int (/ total-plan-price* 100))
        decimal-plan-price* (mod total-plan-price* 100)
        decimal-plan-price (if (= (-> decimal-plan-price* str count) 1)
                             (str "0" decimal-plan-price*)
                             (str decimal-plan-price*))
        total-plan-price (str int-plan-price "." decimal-plan-price)
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
                                     (reset! (::show-plans-dropdown s) false)
                                     (reset! current-plan (:value selected-item)))}))
      [:div.plan-change-description
        (if (= @current-plan "free")
          "Free plan details here"
          (str
           "For your team of "
           (count active-users) ;; Number of active/unverified users
           " people, your plan will cost $"
           total-plan-price
           (if (= (:interval current-plan) "month")
            " monthly."
            " annually.")))]
      [:div.plan-change-title
        (str "Due today: $" total-plan-price)]
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
                                                           (payments-actions/create-plan-subscription payments-data (:id current-plan-data))
                                                           (alert-modal/hide-alert)
                                                           (fn [success]
                                                            (if success
                                                              (reset! (::payments-tab s) :summary)
                                                              (show-error-alert s))))}]
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
  {:will-mount (fn [s]
    (let [payments-data @(drv/get-ref s :payments)
          initial-plan (if (:subscription payments-data) (-> payments-data :subscription :interval) "free")]
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