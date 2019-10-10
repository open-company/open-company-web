(ns oc.web.components.billing-settings-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]))

(defn- user-count [team-data]
  (let [user-count (count (:users team-data))]
    (if (> user-count 1)
      [:span [:strong (str user-count " users")] " are"]
      [:span [:strong  "1 user"] " is"])))

(defn plan-summary [s team-data]
  (let [plan-data {:name "Free"
                   :slug "free"
                   :alert [:div.plan-details-label
                            (user-count team-data)
                            " currently on your team."
                            [:br]
                            "Free plans cover up 10 users."]}]
    [:div.plan-summary
      [:div.plan-summary-title
        "Billing summary"]
      [:div.plan-summary-details.group
        [:div.plan-details-left
          "Plan type"]
        [:div.plan-details-right
          [:div.plan-details-plan.group
            [:div.plan-details-label
              "You are currently on the "
              [:strong (str (:name plan-data) " plan")]
              "."]
            [:button.mlb-reset.change-plan-bt
              {:on-click #(reset! (::billing-tab s) :change)}
              "Change plan"]]
          [:div.plan-details-description.group
            (:alert plan-data)]]]]))

(defn- plan-description [plan]
  (case plan
    "team-monthly"
    "Monthly plan"
    "team-annual"
    "Annual plan (save 20%)"
    "Trial"))

(defn plan-change [s team-data]
  (let [current-plan (::billing-plan s)]
    [:div.plan-change
      [:button.mlb-reset.plans-dropdown-bt
        {:on-click #(reset! (::show-plans-dropdown s) true)}
        (plan-description @current-plan)]
      (when @(::show-plans-dropdown s)
        (dropdown-list {:items [{:value "team-monthly"
                                 :label (plan-description "team-monthly")}
                                {:value "team-annual"
                                 :label (plan-description "team-annual")}]
                        :value @current-plan
                        :on-blur #(reset! (::show-plans-dropdown s) false)
                        :on-change (fn [selected-item]
                                     (reset! (::show-plans-dropdown s) false)
                                     (reset! current-plan (:value selected-item)))}))
      [:div.plan-change-description
        (str
         "For your team of 25 people, your plan will cost $1,200 annually "
         "(25 people x $4 x 12 months). An annual plan saves you $300 per year.")]
      ]))

(rum/defcs billing-settings-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :team-data)
  ui-mixins/refresh-tooltips-mixin
  ;; Locals
  (rum/local :summary ::billing-tab)
  (rum/local "free" ::billing-plan)
  (rum/local false ::show-plans-dropdown)
  [s {:keys [org-data]}]
  (let [team-data (drv/react s :team-data)
        billing-tab (::billing-tab s)
        is-change-tab? (= @billing-tab :change)]
    [:div.billing-settings-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click #(nav-actions/close-all-panels)}]
      [:div.billing-settings-modal-container
        [:div.billing-settings-header
          [:div.billing-settings-header-title
            (if is-change-tab?
             "Change plan"
             "Billing")]
          (when-not is-change-tab?
            [:button.mlb-reset.save-bt
              {:on-click #(reset! billing-tab :change)}
              "Change plan"])
          [:button.mlb-reset.cancel-bt
            {:on-click #(if is-change-tab?
                          (reset! billing-tab :summary)
                          (nav-actions/show-org-settings nil))}
            "Back"]]
        [:div.billing-settings-body
          (if is-change-tab?
            (plan-change s team-data)
            (plan-summary s team-data))]]]))