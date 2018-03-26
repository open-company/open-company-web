(ns oc.web.components.ui.org-settings-team-panel
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.team :as team-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.user-type-dropdown :refer (user-type-dropdown)]))

(defn user-action [team-id user action method other-link-params]
  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide")
  (team-actions/user-action team-id user action method other-link-params nil))

(defn real-remove-fn [author user team-id]
  (when author
    (team-actions/remove-author author))
  (user-action team-id user "remove" "DELETE"  {:ref "application/vnd.open-company.user.v1+json"}))

(defn alert-resend-done []
  (let [alert-data {:icon "/img/ML/invite_resend.png"
                    :action "invite-resend"
                    :message "Invite resent."
                    :link-button-title nil
                    :link-button-cb nil
                    :solid-button-title nil
                    :solid-button-cb nil}]
   (alert-modal/show-alert alert-data)))

(rum/defcs org-settings-team-panel
  < rum/reactive
    (drv/drv :invite-data)
    (rum/local false ::resending-invite)
    {:after-render (fn [s]
                     (doto (js/$ "[data-toggle=\"tooltip\"]")
                        (.tooltip "fixTitle")
                        (.tooltip "hide"))
                     (when @(::resending-invite s)
                      (let [invite-users-data (:invite-users @(drv/get-ref s :invite-data))]
                        (when (zero? (count invite-users-data))
                          (alert-resend-done)
                          (reset! (::resending-invite s) false))))
                     s)}
  [s org-data]
  (let [invite-users-data (drv/react s :invite-data)
        team-data (:team-data invite-users-data)
        cur-user-data (:current-user-data invite-users-data)
        org-authors (:authors org-data)]
    [:div.org-settings-panel
      ;; Panel rows
      [:div.org-settings-team.org-settings-panel-row
        ;; Team table
        [:table.org-settings-table
          [:thead
            [:tr
              [:th "Name"]
              [:th "Status"]
              [:th.role "Role"]]]
          [:tbody
            (for [user (sort-by utils/name-or-email (:users team-data))
                  :let [user-type (utils/get-user-type user (dis/org-data))
                        author (some #(when (= (:user-id %) (:user-id user)) %) org-authors)
                        remove-fn (fn []
                                    (let [alert-data {:icon "/img/ML/trash.svg"
                                                      :action
                                                       (if (= "pending" (:status user))
                                                        "cancel-invitation"
                                                        "remove-user")
                                                      :message
                                                       (if (= "pending" (:status user))
                                                        "Cancel invitation?"
                                                        "Remove user?")
                                                      :link-button-title "No"
                                                      :link-button-cb #(alert-modal/hide-alert)
                                                      :solid-button-title "Yes"
                                                      :solid-button-cb
                                                       #(do
                                                         (real-remove-fn author user (:team-id team-data))
                                                         (alert-modal/hide-alert))}]
                                      (alert-modal/show-alert alert-data)))]]
              [:tr
                {:key (str "org-settings-team-" (:user-id user))}
                [:td.user-name
                  {:class (when (some #(= (:status %) "pending") (:users team-data)) "has-pending")}
                  (user-avatar-image user)
                  (let [display-name (utils/name-or-email user)]
                    [:div.user-name-label
                      {:title (if (not= display-name (:email user)) (:email user) "")
                       :data-toggle "tooltip"
                       :data-placement "top"}
                      display-name])]
                [:td.status-column
                  [:div.status-column-inner.group
                    [:div.status-label (s/capital (:status user))]
                    (when (and (= "pending" (:status user))
                               (or (contains? user :email)
                                   (contains? user :slack-id)))
                      [:button.mlb-reset.mlb-link
                        {:on-click (fn []
                                     (let [invitation-type (if (contains? user :slack-id) "slack" "email")
                                           inviting-user (if (= invitation-type "email")
                                                          (:email user)
                                                          (select-keys
                                                           user
                                                           [:first-name :last-name :slack-id :slack-org-id]))]
                                       (dis/dispatch! [:input [:invite-users]
                                                        [{:user inviting-user
                                                          :type invitation-type
                                                          :role user-type
                                                          :error nil}]])
                                       (reset! (::resending-invite s) true)
                                       (team-actions/invite-users (:invite-users @(drv/get-ref s :invite-data)))))}
                        "Resend"])
                    (when (and (= "pending" (:status user))
                               (utils/link-for (:links user) "remove"))
                      [:button.mlb-reset.mlb-link-red
                        {:on-click remove-fn}
                        "Cancel"])]]
                [:td.role
                  (user-type-dropdown {:user-id (:user-id user)
                                       :user-type user-type
                                       :on-change #(team-actions/switch-user-type user user-type % user author)
                                       :hide-admin (not (jwt/is-admin? (:team-id org-data)))
                                       :on-remove (if (and (not= "pending" (:status user))
                                                           (not= (:user-id user) (:user-id cur-user-data)))
                                                    remove-fn
                                                    nil)})]])]]]]))