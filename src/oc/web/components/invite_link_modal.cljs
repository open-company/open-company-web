(ns oc.web.components.invite-link-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.actions.nav-sidebar :as nav-actions]))

(rum/defcs invite-link-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :team-data)
  ;; Locals
  (rum/local 30 ::expire)
  (rum/local false ::generating-link)
  (rum/local false ::show-expire-dropdown)
  [s]
  (let [team-data (drv/react s :team-data)]
    [:div.invite-link-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click nav-actions/close-all-panels}]
      [:div.invite-link
        [:div.invite-link-header
          [:div.invite-link-header-title
            "Invite anyone, instantly"]
          [:button.mlb-reset.cancel-bt
            {:on-click #(nav-actions/show-org-settings nil)}
            "Back"]]
        (if (seq (:invite-link team-data))
          [:div.invite-link-body]
          [:div.invite-link-body
            [:div.invite-link-body-description
              "Anyone can use this link to join Carrot on Carrot until the link expires."]
            [:div.invite-link-body-description
              "Set expiary"]
            [:div.expire-container
              [:button.mlb-reset.expire-bt
                {:on-click #(swap! (::show-expire-dropdown s) not)}
                (str @(::expire s) " days")]
              (when @(::show-expire-dropdown s)
                (dropdown-list {:value @(::expire s)
                                :items [{:value 30
                                         :label "30 days"}
                                        {:value (* 30 6)
                                         :label "6 months"}
                                        {:value (* 30 12)
                                         :label "1 year"}]
                                :on-change #(reset! (::expire s) (:value %))}))]
            [:button.mlb-reset.generate-bt
              {:on-click #(reset! (::generating-link s) true)}
              "Generate invite link"]
            (when @(::generating-link s)
              (small-loading))])]]))