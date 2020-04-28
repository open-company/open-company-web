(ns oc.web.components.invite-email-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.user :as user-lib]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.invite-email :refer (invite-email)]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.user-type-dropdown :refer (user-type-dropdown)]))

(defn close-clicked [s dismiss-action]
  (let [invite-users (filterv #(not (:error %)) (:invite-users @(drv/get-ref s :invite-data)))
        has-unsent-invites (and (pos? (count invite-users))
                                (some #(seq (:user %)) invite-users))]
    (if has-unsent-invites
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "invite-unsaved-edits"
                        :message "Leave without saving your changes?"
                        :link-button-title "Stay"
                        :link-button-cb #(alert-modal/hide-alert)
                        :solid-button-style :red
                        :solid-button-title "Lose changes"
                        :solid-button-cb #(do
                                            (alert-modal/hide-alert)
                                            (dismiss-action))}]
        (alert-modal/show-alert alert-data))
      (dismiss-action))))

(defn- highlight-url
  "Select the whole content of the share link filed."
  [s]
  (when-let [url-field (rum/ref-node s "invite-token-url-field")]
    (.select url-field)))

(rum/defcs invite-email-modal <  ;; Mixins
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :invite-data)
  ;; Locals
  (rum/local false ::creating-invite-link)
  {:will-mount (fn [s]
    (let [org-data @(drv/get-ref s :org-data)]
      (org-actions/get-org org-data true)
      (team-actions/teams-get))
    s)}
  [s]
  (let [org-data (drv/react s :org-data)
        invite-users-data (drv/react s :invite-data)
        team-data (:team-data invite-users-data)
        invite-users (:invite-users invite-users-data)
        cur-user-data (:current-user-data invite-users-data)
        is-admin? (jwt/is-admin? (:team-id org-data))]
    [:div.invite-email-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click #(close-clicked s nav-actions/close-all-panels)}]
      [:div.invite-email-modal-inner
        [:div.invite-email-header
          [:div.invite-email-header-title
            "Invite via email"]
          [:button.mlb-reset.cancel-bt
            {:on-click (fn [_] (close-clicked s #(nav-actions/show-org-settings nil)))}
            "Back"]]
        [:div.invite-email-body
          (when is-admin?
            (if (seq (:invite-token team-data))
              [:div.invite-token-container
                [:div.invite-token-title
                  "Share an invite link"]
                [:div.invite-token-description
                  "Anyone can use this link to join your Carrot team as a "
                  [:strong "contributor"]
                  "."]
                [:div.invite-token-description
                  "Invite link"]
                [:div.invite-token-field
                  [:input.invite-token-field-input
                    {:value (:href (utils/link-for (:links team-data) "invite-token"))
                     :read-only true
                     :ref "invite-token-url-field"
                     :content-editable false
                     :on-click #(highlight-url s)}]
                  [:button.mlb-reset.invite-token-field-bt
                    {:ref "invite-token-copy-btn"
                     :on-click (fn [e]
                                (utils/event-stop e)
                                (let [url-input (rum/ref-node s "invite-token-url-field")]
                                  (highlight-url s)
                                  (let [copied? (utils/copy-to-clipboard url-input)]
                                    (notification-actions/show-notification {:title (if copied? "Invite URL copied to clipboard" "Error copying the URL")
                                                                             :description (when-not copied? "Please try copying the URL manually")
                                                                             :primary-bt-title "OK"
                                                                             :primary-bt-dismiss true
                                                                             :primary-bt-inline copied?
                                                                             :expire 3
                                                                             :id (if copied? :invite-token-url-copied :invite-token-url-copy-error)}))))}
                    "Copy"]]
                [:button.mlb-reset.deactivate-link-bt
                  {:on-click (fn [_]
                               (let [alert-data {:icon "/img/ML/trash.svg"
                                                 :action "deactivate-invite-email-link"
                                                 :title "Are you sure?"
                                                 :message "Anyone that has this link already won't be able to use it to access your team."
                                                 :solid-button-title "OK, got it"
                                                 :solid-button-cb #(do
                                                                    (alert-modal/hide-alert)
                                                                    (reset! (::creating-invite-link s) true)
                                                                    (team-actions/delete-invite-token-link (utils/link-for (:links team-data) "delete-invite-link")
                                                                     (fn [success?]
                                                                      (reset! (::creating-invite-link s) false))))
                                                 :solid-button-style :red
                                                 :link-button-title "No, keep it"
                                                 :link-button-cb #(alert-modal/hide-alert)}]
                                 (alert-modal/show-alert alert-data)))
                   :disabled @(::creating-invite-link s)}
                  "Deactivate invite link"]]
              [:div.invite-token-container
                [:div.invite-token-title
                  "Share an invite link"]
                [:div.invite-token-description
                  "Anyone can use this link to join your Carrot team as a "
                  [:strong "contributor"]
                  "."]
                [:button.mlb-reset.generate-link-bt
                  {:on-click #(do 
                               (reset! (::creating-invite-link s) true)
                               (team-actions/create-invite-token-link
                                (utils/link-for (:links team-data) "create-invite-link")
                                (fn [success?]
                                 (reset! (::creating-invite-link s) false))))
                   :disabled @(::creating-invite-link s)}
                  "Generate invite link"]]))
          [:div.invites-list-container
            {:class (when is-admin? "top-border")}
            [:div.invites-list-title
              "Invite someone with a specific role"]
            [:div.invites-list-description
              "Admin, Contributor, or Viewer"]
            (invite-email)]]]]))