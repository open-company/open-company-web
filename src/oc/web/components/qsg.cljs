(ns oc.web.components.qsg
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.qsg :as qsg-actions]))

(rum/defcs qsg < rum/reactive
                 (drv/drv :qsg)
                 (drv/drv :posts-data)
  [s]
  (let [qsg-data (drv/react s :qsg)
        posts-data (drv/react s :posts-data)
        sample-content-posts (filterv :sample (vals posts-data))]
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
             :class (when (:invited? qsg-data)
                      "done")}
            "Invite your team"]]
        [:div.qsg-buttons-list-title
          "Product highlight"]
        [:div.qsg-buttons-list
          [:button.mlb-reset.qsg-list-item.qsg-create-post-bt
            {:on-click #(qsg-actions/start-create-post-trail)
             :class (when (:add-post? qsg-data)
                      "done")}
            "Create a post"]
          [:button.mlb-reset.qsg-list-item.qsg-create-reminder-bt
            {:on-click #(qsg-actions/start-create-reminder-trail)
             :class (when (:add-reminder? qsg-data)
                      "done")}
            "Create a reminder"]
          [:button.mlb-reset.qsg-list-item.qsg-add-section-bt
            {:on-click #(qsg-actions/start-add-section-trail)
             :class (when (:add-section? qsg-data)
                      "done")}
            "Add a new section"]
          [:button.mlb-reset.qsg-list-item.qsg-configure-section-bt
            {:on-click #(qsg-actions/start-configure-section-trail)
             :class (when (:section-dialog-seen? qsg-data)
                      "done")}
            "Configure a section"]]]

      [:div.qsg-bottom
        {:class (utils/class-set {:slack-dismissed (:slack-dismissed? qsg-data)
                                  :remove-sample-dismissed (zero? (count sample-content-posts))})}
        (when-not (:slack-dismissed? qsg-data)
          [:div.qsg-using-slack-section
            [:div.qsg-using-slack-title
              "Using Slack?"]
            [:button.mlb-reset.qsg-using-slack-dismiss
              {:on-click #(qsg-actions/dismiss-slack)}]
            [:div.qsg-using-slack-desc
              "View and comment on posts directly from Slack."]
            [:button.mlb-reset.qsg-using-slack-bt
              {:on-click #()}
              [:span.qsg-slack-icon]
              "Connect to Slack"]])
        (when-not (zero? (count sample-content-posts))
          [:div.qsg-start-fresh-section
            [:div.qsg-start-fresh-title
              "Ready to start fresh?"]
            [:button.mlb-reset.qsg-start-fresh-bt
              {:on-click #()}
              "Delete all sample content"]])
        [:button.mlb-reset.qsg-dismiss
          {:on-click #(qsg-actions/dismiss-qsg-view)}
          "Dismiss quickstart guide"]]]))