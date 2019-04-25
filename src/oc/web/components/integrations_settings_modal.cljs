(ns oc.web.components.integrations-settings-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.actions.notifications :as notification-actions]))

(defn show-modal [& [panel]]
  (dis/dispatch! [:input [:org-settings] (or panel :integrations)]))

(defn dismiss-modal []
  (dis/dispatch! [:input [:org-settings] nil]))

(rum/defcs integrations-settings-modal <
  ;; Mixins
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :team-data)
  (drv/drv :current-user-data)
  ;; Locals
  (rum/local false ::saving)
  ;; Mixins
  mixins/no-scroll-mixin
  [s]
  (let [org-data (drv/react s :org-data)
        team-data (drv/react s :team-data)
        cur-user-data (drv/react s :current-user-data)
        slack-teams (:slack-orgs team-data)
        slack-teams-count (count slack-teams)]
    [:div.integrations-settings-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click dismiss-modal}]
      [:div.integrations-settings
        [:div.integrations-settings-header
          [:div.integrations-settings-header-title
            "Integrations"]
          [:button.mlb-reset.save-bt
            {:on-click dismiss-modal}
            "Save"]
          [:button.mlb-reset.cancel-bt
            {:on-click dismiss-modal}
            "Back"]]
        [:div.integrations-settings-body
          (when (utils/link-for (:links team-data) "authenticate" "GET" {:auth-source "slack"})
            [:button.btn-reset.add-slack-team-bt
              {:on-click #(team-actions/slack-team-add cur-user-data (str (router/get-token) "?org-settings=main"))}
              [:div.slack-icon]
              "Add to Slack"])
          (when-not (zero? slack-teams-count)
            [:div.integrations-settings-list
              [:div.integrations-settings-list-title
                (str "Connected Slack team" (when (> slack-teams-count 1) "s"))]
              (let [slack-bots (get (jwt/get-key :slack-bots) (jwt/slack-bots-team-key (:team-id org-data)))]
                (for [slack-team slack-teams
                      :let [has-logo (seq (:logo-url slack-team))
                            logo-url (if has-logo
                                       (:logo-url slack-team)
                                       (utils/cdn "/img/slack.png"))
                            slack-domain (:slack-domain slack-team)
                            has-bot? (pos? (count (filter #(= (:slack-org-id %) (:slack-org-id slack-team)) slack-bots)))]]
                  [:div.integrations-settings-item
                    {:key (str "slack-org-" (:slack-org-id slack-team))}
                    [:div.integration-item-header
                      [:div.logo-container
                        [:img.slack-logo
                          {:class (when-not has-logo "no-logo")
                           :src logo-url}]]
                      [:div.slack-team-name
                        (:name slack-team)]
                      [:button.mlb-reset.button.remove-slack-team-bt
                        {:on-click (fn []
                                     (alert-modal/show-alert {:icon "/img/ML/trash.svg"
                                                              :action "remove-slack-team"
                                                              :message "Are you sure you want to remove this Slack team?"
                                                              :link-button-title "Keep"
                                                              :link-button-cb #(alert-modal/hide-alert)
                                                              :solid-button-style :red
                                                              :solid-button-title "Yes"
                                                              :solid-button-cb (fn []
                                                                                (team-actions/remove-team (:links slack-team))
                                                                                (alert-modal/hide-alert))}))
                         :title "Remove"
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :data-container "body"}]]
                    (when (seq slack-domain)
                      [:div.linked-to
                        (str slack-domain ".slack.com")])
                    [:div.self-join
                      "Slack members can self-join this as: "
                      [:select.self-join-select
                        [:option
                          "Admin"]
                        [:option
                          "Contributor"]
                        [:option
                          "Viewer"]]]
                    (if has-bot?
                      [:div.bot-line
                        "Carrot bot is currently on."]
                      [:div.bot-line
                        "Carrot bot is currently off. "
                        [:button.mlb-reset.turn-on-bot-bt
                          {:on-click #(org-actions/bot-auth team-data cur-user-data (str (router/get-token) "?org-settings=main"))}
                          "Turn it on?"]])]))])]]]))