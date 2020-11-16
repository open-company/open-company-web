(ns oc.web.components.premium-picker-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.actions.payments :as payments-actions]
            [oc.web.mixins.ui :as ui-mixins]))

(rum/defcs premium-picker <
  rum/reactive
  (rum/local :monthly ::selected-price)
  (drv/drv :payments)
  [s]
  (let [payments-data (drv/react s :payments)]
    [:div.premium-picker
     [:div.premium-picker-plans-container
      [:div.premium-picker-plan
       {:class (when (= @(::selected-price s) :monthly) "selected")
        :on-click #(reset! (::selected-price s) :monthly)}
       [:input.plan-radio-input
        {:checked (= @(::selected-price s) :monthly)
         :value ""
         :ref :monthly-plan-radio}]
       [:div.premium-picker-plan-name
        "Monthly"]]
      [:div.premium-picker-plan
       {:class (when (= @(::selected-price s) :yearly) "selected")
        :on-click #(reset! (::selected-price s) :yearly)}
       [:input.plan-radio-input
        {:checked (= @(::selected-price s) :yearly)
         :value ""
         :ref :yearly-plan-radio}]
       [:div.premium-picker-plan-name
        "Yearly"]]]

     [:div.premium-picker-price
      (if (= @(::selected-price s) :monthly)
        "$5 per user, per month"
        "$50 per user, per year")]

     [:ul.premium-picker-features-list
      [:li "Private/public topics"]
      [:li "View-only users"]
      [:li "Custom digest schedule"]
      [:li "Custom colors and branding"]]
     
     [:button.continue.mlb-reset
      {:on-click #(payments-actions/open-checkout! payments-data nil)}
      "Upgrade to premium"]]))

(rum/defc premium-picker-modal <
  ui-mixins/no-scroll-mixin
  []
  (let []
    [:div.premium-picker-modal

     [:div.premium-picker-modal-inner
      
      [:button.close-modal-bt]

      [:div.premium-picker-modal-content
       [:h3.premium-picker-modal-title
        "Carrot Premium"]

       (premium-picker)]

      [:div.premium-picker-modal-quote
       [:div.quote-copy
        "“We use Carrot to make sure team updates and announcements don't get lost in Slack conversations. It keeps our remote teams in sync.”"]
       [:div.quote-avatar]
       [:div.quote-name
        "Camilo Alvarez, Operations Lead"]
       [:div.quote-link
        "Hopper.com"]]]]))