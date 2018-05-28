(ns oc.web.components.ui.slack-bot-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]))

(defn show-modal [bot-auth-cb]
  (dis/dispatch! [:input [:slack-bot-modal :bot-auth-cb] bot-auth-cb]))

(defn dismiss-modal []
  (dis/dispatch! [:input [:slack-bot-modal] nil]))


(rum/defcs slack-bot-modal < rum/static
                             rum/reactive
                             ;; Derivatives
                             (drv/drv :org-data)
                             (drv/drv :team-data)
                             (drv/drv :current-user-data)
                             (drv/drv :slack-bot-modal)
                             ;; Mixins
                             no-scroll-mixin
  [s]
  (let [slack-bot-modal-data (drv/react s :slack-bot-modal)
        org-data (drv/react s :org-data)
        team-data (drv/react s :team-data)
        current-user-data (drv/react s :current-user-data)]
    [:div.slack-bot-modal.fullscreen-page
      [:div.slack-bot-modal-inner
        [:button.settings-modal-close.mlb-reset
          {:on-click dismiss-modal}]
        [:div.carrot-plus-slack]
        [:h1.slack-bot-modal-title
          "Enable Carrot for Slack"]
        [:div.slack-bot-modal-item.first-item
          "Share posts with your team in Slack"]
        [:div.slack-bot-modal-item.second-item
          "Sync comments in between Carrot and Slack"]
        [:div.slack-bot-modal-item.third-item
          "Invite team members to Carrot with Slack"]
        [:div.slack-bot-modal-buttons.group
          [:button.mlb-reset.slack-bot-modal-dismiss
            {:on-click dismiss-modal}
            "No thanks, maybe later"]
          [:button.mlb-reset.slack-bot-modal-add
            {:on-click #((:bot-auth-cb slack-bot-modal-data) org-data team-data current-user-data)}
            [:span
              [:span.slack-icon]
              "Add to Slack"]]]]]))