(ns oc.web.components.qsg
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.qsg :as qsg-actions]))

(rum/defcs qsg < rum/reactive
                 (drv/drv :qsg)
  [s]
  (let [qsg-data (drv/react s :qsg)]
    [:div.qsg-container
      [:div.qsg-header
        [:span.qsg-lifevest]
        [:div.qsg-title "Quickstart guide"]
        [:button.mlb-reset.qsg-top-dismiss
          {:on-click #(qsg-actions/dismiss-qsg-view)}]]
      [:div.qsg-top
        [:div.qsg-top-title
          "A few pointers to help you get started with Carrot."]
        [:div.qsg-progress-bar
          [:div.qsg-progress-bar-inner
            {:style {:width (str (or (:overall-progress qsg-data) 0) "%")}}]]
        [:div.qsg-buttons-list-title
          "Setup"]
        [:div.qsg-buttons-list
          [:div.qsg-list-item.verify-email-item
            {:class (when (:verify-email-done qsg-data)
                      "done")}
            (str "Verify "
             (when (:verify-email-done qsg-data)
               "your ")
             "email"
             (when-not (:verify-email-done qsg-data)
               " ("))
            (when-not (:verify-email-done qsg-data)
              [:button.mlb-reset.resend-email-bt
                {:on-click #()}
                "resend?"])
            (when-not (:verify-email-done qsg-data)
              ")")]
          [:button.mlb-reset.qsg-list-item.add-profile-photo-bt
            {:on-click #(qsg-actions/start-profile-photo-trail)
             :class (when (:profile-photo-done qsg-data)
                      "done")}
            "Add a profile photo"]
          [:button.mlb-reset.qsg-list-item.add-company-logo-bt
            {:on-click #(qsg-actions/start-company-logo-trail)
             :class (when (:company-logo-done qsg-data)
                      "done")}
            "Add a company logo"]
          [:button.mlb-reset.qsg-list-item.qsg-invite-team-bt
            {:on-click #(qsg-actions/start-invite-team-trail)
             :class (when (:invite-team-done qsg-data)
                      "done")}
            "Invite your team"]]
        [:div.qsg-buttons-list-title
          "Product highlight"]
        [:div.qsg-buttons-list
          [:button.mlb-reset.qsg-list-item.qsg-create-post-bt
            {:on-click #(qsg-actions/start-create-post-trail)
             :class (when (:create-post-done qsg-data)
                      "done")}
            "Create a post"]
          [:button.mlb-reset.qsg-list-item.qsg-create-reminder-bt
            {:on-click #(qsg-actions/start-create-reminder-trail)
             :class (when (:create-reminder-done qsg-data)
                      "done")}
            "Create a reminder"]
          [:button.mlb-reset.qsg-list-item.qsg-add-section-bt
            {:on-click #(qsg-actions/start-add-section-trail)
             :class (when (:add-section-done qsg-data)
                      "done")}
            "Add a new section"]
          [:button.mlb-reset.qsg-list-item.qsg-configure-section-bt
            {:on-click #(qsg-actions/start-configure-section-trail)
             :class (when (:configure-section-done qsg-data)
                      "done")}
            "Configure a section"]]]

      [:div.qsg-bottom
        [:div.qsg-using-slack-section
          [:div.qsg-using-slack-title
            "Using Slack?"]
          [:button.mlb-reset.qsg-using-slack-dismiss
            {:on-click #()}]
          [:div.qsg-using-slack-desc
            "View and comment on posts directly from Slack."]
          [:button.mlb-reset.qsg-using-slack-bt
            {:on-click #()}
            [:span.qsg-slack-icon]
            "Connect to Slack"]]
        [:div.qsg-start-fresh-section
          [:div.qsg-start-fresh-title
            "Ready to start fresh?"]
          [:button.mlb-reset.qsg-start-fresh-bt
            {:on-click #()}
            "Delete all sample content"]]
        [:button.mlb-reset.qsg-dismiss
          {:on-click #(qsg-actions/dismiss-qsg-view)}
          "Dismiss quickstart guide"]]]))