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
          [:div.invite-picker-body-title
            "Invite new teammates"]
          [:div.invite-picker-body-description
            "Teammates can access posts, files, and comments in any section."]
          [:button.mlb-reset.invite-bt
            {:on-click #(nav-actions/show-org-settings :invite)}
            (if (:can-slack-invite team-data)
              "Invite via Slack or email"
              "Invite via email")]
          [:button.mlb-reset.invite-link-bt
            {:on-click #(nav-actions/show-org-settings :invite-link)}
            (if (:invite-link team-data)
              "Mange your team invite link"
              "Generate an invite link to share")]]]]))