(ns oc.web.components.premium-picker-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.actions.payments :as payments-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.mixins.ui :as ui-mixins]))

(def yearly-plan-discount-copy "20% discount for annual plan.")

(def premium-features-list
  [{:title "Team, private and public updates"
    :tooltip "Create updates that are private for select users, or public for people outside the team."}
   {:title "View-only users"
    :tooltip "In the free version, everyone on the team can add updates. With premium, admins can assign editor and view-only roles."}
   {:title "Custom digest schedule"
    :tooltip "In the free version, everyone receives a morning digest at 7AM. With premium, users can add periodic digests at noon and 5PM to stay in sync through the day."}
   {:title "Analytics"
    :tooltip "Know who saw your update, and easily remind those that missed it."}
   {:title "Custom colors and branding"}])

(defn- setup-selected-price [s]
  (when-not @(::selected-price s)
    (let [prices (-> s (drv/get-ref :payments) deref :available-prices)
          monthly-plan* (some #(when (= (:interval %) "month") (:id %)) prices)
          monthly-plan (or monthly-plan* (-> prices first :id))]
      (reset! (::selected-price s) monthly-plan))))

(rum/defcs premium-picker <
  rum/reactive
  (rum/local nil ::selected-price)
  (rum/local false ::working)
  (drv/drv :payments)
  ui-mixins/refresh-tooltips-mixin
  {:will-mount (fn [s]
                 (payments-actions/maybe-load-payments-data true)
                 (setup-selected-price s)
                 s)
   :will-update (fn [s]
                  (setup-selected-price s)
                  s)}
  [s]
  (let [payments-data (drv/react s :payments)
        available-prices (:available-prices payments-data)
        selected-price (::selected-price s)
        current-price (some #(when (= (:id %) @selected-price) %) available-prices)
        current-subscription (payments-actions/get-current-subscription payments-data)
        current-sub-price (:price current-subscription)]
    [:div.premium-picker
     (if current-subscription
       [:div.premium-picker-plans-container.has-active-subscription
        [:span.current-label "Your plan: "]
        [:div.premium-picker-plan
         {:key (or (:id current-sub-price) (rand 1000)) ;; fallback to not leave this empty
          :class (utils/class-set {:selected true
                                   :active true})}
         [:div.premium-picker-plan-name
          (:name-label current-sub-price)]]]
       [:div.premium-picker-plans-container
        (for [price available-prices
              :let [selected? (= @selected-price (:id price))
                    active? (and current-subscription
                                 (= (:id price) (:id (:price current-subscription))))
                    change-cb (when-not current-subscription
                                #(reset! (::selected-price s) (:id price)))]]
          [:div.premium-picker-plan
           {:key (or (:id price) (rand 1000)) ;; fallback to not leave this empty
            :class (utils/class-set {:selected selected?
                                     :active active?})
            :on-click change-cb}
           [:input.plan-radio-input
            {:checked selected?
             :value ""}]
           [:div.premium-picker-plan-name
            (:name-label price)]])])

     [:div.premium-picker-price
      (:description-label current-price)
      (when (= (:interval current-price) "year")
        [:div.premium-picker-price-discount
         yearly-plan-discount-copy])]

     (when current-subscription
       [:div.subscription-end-date
        {:class (when (:cancel-at-period-end? current-subscription) "cancelling")}
        (str
         "Your subscription "
         (if (:cancel-at-period-end? current-subscription)
           "is set to cancel"
           "will renew")
         " on "
         (:end-date-label current-subscription)
         ".")])

     [:ul.premium-picker-features-list
      (for [idx (range (count premium-features-list))
            :let [{:keys [title tooltip]} (get premium-features-list idx)]]
        [:li
         {:key (str "premium-feature-" idx)}
         [:div.premium-picker-feature-item
          {:class (when tooltip "has-tooltip")
           :data-toggle (when tooltip "tooltip")
           :data-placement "top"
           :title tooltip}
          [:span.premium-picker-feature-item
           title]]])]

     (when (seq available-prices)
      (if current-subscription
        [:button.continue.mlb-reset
         {:on-click (fn [_]
                      (reset! (::working s) true)
                      (payments-actions/manage-subscription! payments-data #(reset! (::working s) false)))
          :disabled @(::working s)}
         "Manage subscription"
         (when @(::working s)
           (small-loading))]
        [:button.continue.mlb-reset
         {:on-click (fn [_]
                      (reset! (::working s) true)
                      (payments-actions/create-subscription! payments-data @selected-price #(reset! (::working s) false)))
          :disabled @(::working s)}
         "Upgrade"
         (when @(::working s)
           (small-loading))]))]))

(rum/defc premium-picker-modal <
  ui-mixins/no-scroll-mixin
  rum/static
  []
  [:div.premium-picker-modal

    [:div.premium-picker-modal-inner

    [:button.close-modal-bt
      {:on-click #(nav-actions/toggle-premium-picker!)}]

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
      "Hopper.com"]]]])

(rum/defc prompt-upgrade-banner < rum/static
  []
  [:div.payments-top-banner.prompt-upgrade-banner
   {:on-click #(do
                 (payments-actions/hide-prompt-upgrade-banner)
                 (nav-actions/toggle-premium-picker!))}
   [:span.has-link
    (str payments-actions/prompt-upgrade-message
        " - ")]
   [:span.learn-more
    "Learn more"]
   [:button.mlb-reset.close-bt
    {:on-click #(do
                  (utils/event-stop %)
                  (payments-actions/hide-prompt-upgrade-banner))}]])

(rum/defc upgraded-banner < rum/static
  []
  [:div.payments-top-banner.upgraded-banner
   [:span (str payments-actions/upgraded-message)]
   [:button.mlb-reset.close-bt
    {:on-click #(payments-actions/hide-upgraded-banner)}]])