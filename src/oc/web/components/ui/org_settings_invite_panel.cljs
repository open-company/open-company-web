(ns oc.web.components.ui.org-settings-invite-panel
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.api :as api]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.user-type-picker :refer (user-type-dropdown)]
            [oc.web.components.ui.slack-users-dropdown :refer (slack-users-dropdown)]))

(rum/defcs org-settings-invite-panel
  < rum/reactive
    (drv/drv :current-user-data)
    (drv/drv :teams-load)
    (drv/drv :team-data)
    (rum/local "slack" ::inviting-from)
    (rum/local nil ::inviting-email)
    (rum/local nil ::inviting-slack-org-id)
    (rum/local nil ::inviting-slack-user-id)
    (rum/local nil ::inviting-user-type)
    {:before-render (fn [s]
                     (let [teams-load-data @(drv/get-ref s :teams-load)]
                       (when (and (:auth-settings teams-load-data)
                                  (not (:teams-data-requested teams-load-data)))
                         (dis/dispatch! [:teams-get])))
                     s)
     :after-render (fn [s]
                     (doto (js/$ "[data-toggle=\"tooltip\"]")
                        (.tooltip "fixTitle")
                        (.tooltip "hide"))
                     s)}
  [s org-data]
  (let [team-data (drv/react s :team-data)
        cur-user-data (drv/react s :current-user-data)]
    [:div.org-settings-panel
      [:div.org-settings-panel-row.invite-from.group
        [:div.invite-from-label "Invite with:"]
        [:div.org-settings-panel-choice
          [:input
            {:type "radio"
             :name "org-settings-invite-from-medium"
             :on-change #(reset! (::inviting-from s) "email")
             :value "email"
             :id "org-settings-invit-from-medium-email"}]
          [:label
            {:for "org-settings-invit-from-medium-email"}
            "Email"]]
        [:div.org-settings-panel-choice
          [:input
            {:type "radio"
             :name "org-settings-invite-from-medium"
             :on-change #(reset! (::inviting-from s) "slack")
             :value "slack"
             :id "org-settings-invit-from-medium-slack"}]
          [:label
            {:for "org-settings-invit-from-medium-slack"}
            "Slack"]]]
      ;; Panel rows
      [:div.org-settings-invite-table.org-settings-panel-row
        ;; Team table
        [:table.org-settings-table
          [:thead
            [:tr
              [:th ""]
              [:th "Email Addresses"]
              [:th "Role "
                [:i.mdi.mdi-information-outline]]
              [:th ""]]]
          [:tbody
            [:tr
              [:td
                [:button.mlb-reset.mlb-default.add-button
                  "+"]]
              [:td
                (if (= "email" @(::inviting-from s))
                  [:input
                    {:type "email"
                     :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
                     :placeholder "email@example.com"
                     :value @(::inviting-email s)}]
                  (slack-users-dropdown {:on-change #(reset! (::inviting-slack-user-id s) %)
                                         :disabled false
                                         :initial-value ""}))]
              [:td
                (user-type-dropdown (utils/guid) @(::inviting-user-type s) #(reset! (::inviting-user-type s) %))]
              [:td]]]]]
      ;; Save and cancel buttons
      [:div.org-settings-footer.group
        [:button.mlb-reset.mlb-default.save-btn
          {:on-click #(dis/dispatch! [:org-edit-save])}
          "Save"]
        [:button.mlb-reset.mlb-link-black.cancel-btn
          {:on-click #()}
          "Cancel"]]]))