(ns oc.web.components.invite-picker-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.actions.nav-sidebar :as nav-actions]))


(rum/defcs invite-picker-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :team-data) 
  [s]
  (let [team-data (drv/react s :team-data)]
    [:div.invite-picker-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click nav-actions/close-all-panels}]
      [:div.invite-picker
        [:div.invite-picker-header
          [:div.invite-picker-header-title
            "Invite people"]
          [:button.mlb-reset.cancel-bt
            {:on-click #(nav-actions/show-org-settings nil)}
            "Back"]]
        [:div.invite-picker-body
          [:div.invite-picker-body-description
            "Ready for better team discussions? Invite your team!"]
          [:button.mlb-reset.invite-email-bt
            {:on-click #(nav-actions/show-org-settings :invite-email)}
            "Invite via email"]
          (cond
            (or (:can-slack-invite team-data)
                (:can-add-bot team-data))
            [:button.mlb-reset.invite-slack-bt
              {:on-click #(nav-actions/show-org-settings :invite-slack)}
              "Invite via Slack"]
            :else
            [:button.mlb-reset.invite-slack-bt
              {:on-click #(nav-actions/show-org-settings :invite-slack)}
              [:span.disabled "Invite via Slack"]
              [:span.enabled "(add Slack)"]])]]]))