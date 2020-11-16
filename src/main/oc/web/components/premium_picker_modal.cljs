(ns oc.web.components.premium-picker-modal
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.actions.payments :as payments-actions]
            [oc.web.mixins.ui :as ui-mixins]))

(rum/defcs premium-picker <
  rum/reactive
  (rum/local :monthly ::selected-price)
  (drv/drv :payments)
  {:will-mount (fn [s]
                 (let [prices (-> s (drv/get-ref :payments) deref :available-prices)
                       monthly-plan (some #(when (= (:interval %) "month") %) prices)]
                   (reset! (::selected-price s) (:id monthly-plan)))
                 s)}
  [s]
  (let [payments-data (drv/react s :payments)
        available-prices (:available-prices payments-data)
        selected-price (::selected-price s)
        current-price (some #(when (= (:id %) @selected-price) %) available-prices)]
    [:div.premium-picker
     [:div.premium-picker-plans-container
      (for [price available-prices
            :let [selected? (= @selected-price (:id price))]]
        [:div.premium-picker-plan
         {:key (:id price)
          :class (when selected? "selected")
          :on-click #(reset! (::selected-price s) (:id price))}
         [:input.plan-radio-input
          {:checked selected?
           :value ""}]
         [:div.premium-picker-plan-name
          (:name-label price)]])]

     [:div.premium-picker-price
      (:description-label current-price)]

     [:ul.premium-picker-features-list
      [:li "Private/public topics"]
      [:li "View-only users"]
      [:li "Custom digest schedule"]
      [:li "Custom colors and branding"]]
     
     [:button.continue.mlb-reset
      {:on-click #(payments-actions/open-checkout! payments-data @selected-price)}
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