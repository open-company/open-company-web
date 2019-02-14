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
          "Keep your team informed - let’s get started"]
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
          [:a.qsg-list-item.qsg-digest-sample-bt
            {:href digest-sample-url
             :target "_bank"}
            "See a sample digest"]]
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
            "Invite your team"]
          [:button.mlb-reset.qsg-list-item.qsg-create-reminder-bt
            {:on-click #(qsg-actions/start-create-reminder-trail)
             :class (when (:add-reminder? qsg-data)
                      "done")}
            "Create a reminder"]]]

      [:div.qsg-bottom
        {:class (utils/class-set {:slack-dismissed (:slack-dismissed? qsg-data)
                                  :digest-sample-dismissed (:digest-sample-dismissed? qsg-data)})}
        (when-not (:digest-sample-dismissed? qsg-data)
          [:div.qsg-digest-sample-section
            [:div.qsg-digest-sample-title
              "Your morning digest keeps everyone aligned"]
            [:button.mlb-reset.qsg-digest-sample-dismiss
              {:on-click #(qsg-actions/dismiss-digest-sample)}]
            [:a.qsg-digest-sample-bt
              {:target "_blank"
               :href digest-sample-url}
              [:span.qsg-digest-sample-icon]
              "Send sample digest"]])
        (when-not (:slack-dismissed? qsg-data)
          [:div.qsg-using-slack-section
            [:div.qsg-using-slack-title
              "Using Slack? Share posts with your team"]
            [:button.mlb-reset.qsg-using-slack-dismiss
              {:on-click #(qsg-actions/dismiss-slack)}]
            [:button.mlb-reset.qsg-using-slack-bt
              {:on-click #(qsg-actions/slack-click)}
              [:span.qsg-slack-icon]
              "Add to Slack"]])
        [:button.mlb-reset.qsg-dismiss
          {:on-click #(qsg-actions/dismiss-qsg-view)}
          "Dismiss quickstart guide"]]]))