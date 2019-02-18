(ns oc.web.components.qsg
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.qsg :as qsg-actions]
            [oc.web.actions.user :as user-actions]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]))

(def digest-sample-url "https://invis.io/G2QFA3Y8UZP")

(rum/defcs qsg < rum/reactive
                 (drv/drv :qsg)
                 {:did-mount (fn [s]
                   (.add (.-classList (sel1 [:body])) "showing-qsg")
                   (js/OCYTVideoInit)
                   s)
                  :will-unmount (fn [s]
                   (,remove (.-classList (sel1 [:body])) "showing-qsg")
                   s)}
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
          (if (> (:overall-progress qsg-data) 99)
            "You completed the quickstart - you’re ready to go!"
            "Keep your team informed - let’s get started!")]
        [:div.qsg-progress-bar
          [:div.qsg-progress-bar-inner
            {:style {:width (str (or (:overall-progress qsg-data) 0) "%")}}]]
        [:div.qsg-buttons-list-title
          "Explore"]
        [:div.qsg-buttons-list
          [:button.mlb-reset.qsg-list-item.qsg-create-post-bt
            {:on-click #(qsg-actions/start-create-post-trail)
             :class (when (:add-post? qsg-data)
                      "done")}
            "Create a post"]
          [:button.mlb-reset.qsg-list-item.qsg-add-section-bt
            {:on-click #(qsg-actions/start-add-section-trail)
             :class (when (:add-section? qsg-data)
                      "done")}
            "Add a section"]
          [:button.mlb-reset.qsg-list-item.qsg-digest-sample-bt
            {:on-click #(qsg-actions/see-digest-sample)
             :class (when (:see-digest-sample? qsg-data)
                      "done")}
            "See a sample digest"]
          [:button.mlb-reset.qsg-list-item.qsg-create-reminder-bt
            {:on-click #(qsg-actions/start-create-reminder-trail)
             :class (when (:add-reminder? qsg-data)
                      "done")}
            "Check out reminders"]]
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
             (when (and (not (:verify-email-done qsg-data))
                       (:can-resend-verification? qsg-data))
               " ("))
            (when (and (not (:verify-email-done qsg-data))
                       (:can-resend-verification? qsg-data))
              [:button.mlb-reset.resend-email-bt
                {:on-click #(user-actions/resend-verification-email)}
                "resend?"])
            (when (and (not (:verify-email-done qsg-data))
                       (:can-resend-verification? qsg-data))
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
            "Invite your team"]]]

      [:div.qsg-bottom
        {:class (utils/class-set {:slack-dismissed (:slack-dismissed? qsg-data)
                                  :carrot-video-dismissed (:carrot-video-dismissed? qsg-data)})}
        (when-not (:carrot-video-dismissed? qsg-data)
          [:div.qsg-carrot-video-section
            [:div.qsg-carrot-video-title
              "Carrot in 60 seconds"]
            [:button.mlb-reset.qsg-carrot-video-dismiss
              {:on-click #(qsg-actions/dismiss-carrot-video)}]
            [:button.mlb-reset.qsg-carrot-video-bt
              {:on-click #(qsg-actions/watch-carrot-video)}
              [:span.qsg-youtube-icon]
              "Play video"]])
        (when-not (:slack-dismissed? qsg-data)
          [:div.qsg-using-slack-section
            [:div.qsg-using-slack-title
              (if (:has-slack-user? qsg-data)
                "Receive digests in Slack. Share posts to Slack."
                "Using Slack? Carrot and Slack work together.")]
            [:button.mlb-reset.qsg-using-slack-dismiss
              {:on-click #(qsg-actions/dismiss-slack)}]
            [:button.mlb-reset.qsg-using-slack-bt
              {:on-click #(qsg-actions/slack-click)}
              [:span.qsg-slack-icon]
              (if (:has-slack-user? qsg-data)
                "Enable the Carrot Bot"
                "Add to Slack")]])
        [:button.mlb-reset.qsg-dismiss
          {:on-click #(qsg-actions/dismiss-qsg-view)}
          "Dismiss quickstart guide"]]]))