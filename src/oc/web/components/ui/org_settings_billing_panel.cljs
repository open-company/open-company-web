(ns oc.web.components.ui.org-settings-billing-panel
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.lib.chat :as chat]
            [goog.object :as gobj]
            [goog.dom :as gdom]))

(defn plan-summary [s team-data]
  (let [plan-data {:name "Free"
                   :slug "free"
                   :alert [:div.plan-details-label
                            [:strong "12 users"]
                            " are currently on your team."
                            [:br]
                            "Free plans cover up 10 users."]}
        exceeded-users-alert (:exceeded-users team-data)
        upgrade-plan-alert (or true (:upgrade-plan team-data))]
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
            (:alert plan-data)]]]
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
            (:alert plan-data)]]]
      (cond
        exceeded-users-alert
        [:div.plan-summary-alert
          "You've outgrown the Free plan. Please "
          [:button.mlb-reset.upgrade-plan-bt
            {:on-click #(reset! (::billing-tab s) :change)}
            "upgrade"]
          " to continue using Carrot."]
        upgrade-plan-alert
        [:div.plan-summary-alert
          "Your free plan maintains up to six months of history in Carrot."
          [:br]
          [:button.mlb-reset.upgrade-plan-bt
            {:on-click #(reset! (::billing-tab s) :change)}
            "Upgrade"]
          " your plan for unlimited history"])]))

(defn- plan-description [plan current-plan]
  (case plan
    "free"
    (str
     (when (not= current-plan "free")
       "Switch to ")
     "Free plan (free up to 10 users, 6 months history)")
    "team-monthly"
    (str
     (when (not= current-plan "team-monthly")
       "Switch to ")
     "Team plan (billed monthly)")
    "team-annually"
    (str
     (when (not= current-plan "team-annually")
       "Switch to ")
     "Team plan (billed annually, 20% discount)")))

(defn plan-change [s team-data]
  (let [current-plan (::billing-plan s)]
    [:div.plan-change
      [:div.plan-change-title
        "Change your plan"]
      [:button.mlb-reset.plans-dropdown-bt
        {:on-click #(reset! (::show-plans-dropdown s) true)}
        (plan-description @current-plan @current-plan)]
      (when @(::show-plans-dropdown s)
        (dropdown-list {:items [{:value "free"
                                 :label (plan-description "free" @current-plan)
                                 :disabled false ;; Disable in case the team has more then 10 users
                                }
                                {:value "team-monthly"
                                 :label (plan-description "team-monthly" @current-plan)}
                                {:value "team-annually"
                                 :label (plan-description "team-annually" @current-plan)}]
                        :value (:plan team-data)
                        :on-blur #(reset! (::show-plans-dropdown s) false)
                        :on-change (fn [selected-item]
                                     (reset! (::show-plans-dropdown s) false)
                                     (reset! current-plan (:value selected-item)))}))
      [:div.plan-change-description
        [:div.plan-change-description-title
          "Your current plan"]
        [:div.plan-change-description-list
          [:ul
            [:li [:strong "12 users"] " are currently on your team"]
            [:li "Free plans cover up to 10 users"]
            [:li [:strong "Please upgrade to continue using Carrot"]]]]]
      [:div.plan-change-green-alert
        "Looking for the Enterprise plan? "
        [:button.mlb-reset.change-contact-bt
          {:on-click #(chat/chat-click 43235)}
          "Contact us"]
        " to learn more."]]))

(rum/defcs org-settings-billing-panel
  < rum/reactive
    (drv/drv :team-data)
    (rum/local :summary ::billing-tab)
    (rum/local "free" ::billing-plan)
    (rum/local false ::show-plans-dropdown)
  [s org-data dismiss-settings-cb]
  (let [team-data (drv/react s :team-data)]
    [:div.org-settings-panel
      ;; Panel rows
      [:div.org-settings-billing
        (case @(::billing-tab s)
          :summary
          (plan-summary s team-data)
          :change
          (plan-change s team-data)
          [:div.error
            "An error occurred! Please try again"])]
      ;; Save and cancel buttons
      ; [:div.org-settings-footer.group]
      ]))