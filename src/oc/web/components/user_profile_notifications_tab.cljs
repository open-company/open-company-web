(ns oc.web.components.user-profile-notifications-tab
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.stores.user :as user-stores]
            [oc.web.actions.user :as user-actions]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.components.org-settings :as org-settings]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-close-bt :refer (carrot-close-bt)]))

(defn change! [s k v]
  (dis/dispatch! [:input [:edit-user-profile k] v])
  (dis/dispatch! [:input [:edit-user-profile :has-changes] true]))

(defn save-clicked [s]
  (reset! (::loading s) true)
  (let [edit-user-profile @(drv/get-ref s :edit-user-profile)
        current-user-data @(drv/get-ref s :current-user-data)
        user-data (:user-data edit-user-profile)]
    (user-actions/user-profile-save current-user-data edit-user-profile)))

(defn add-slack-clicked [current-user-data real-close-cb]
  (let [switch-cb (fn []
                   (real-close-cb current-user-data)
                   (utils/after 150 #(org-settings/show-modal :main)))]
    (if (:has-changes current-user-data)
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "user-profile-unsaved-edits"
                        :message "Leave without saving your changes?"
                        :link-button-title "Stay"
                        :link-button-cb #(alert-modal/hide-alert)
                        :solid-button-style :red
                        :solid-button-title "Lose changes"
                        :solid-button-cb (fn []
                                          (alert-modal/hide-alert)
                                          (switch-cb))}]
        (alert-modal/show-alert alert-data))
      (switch-cb))))

(rum/defcs user-profile-notifications-tab <
                                       rum/reactive
                                       (drv/drv :team-data)
                                       (drv/drv :team-roster)
                                       (drv/drv :edit-user-profile)
                                       (drv/drv :current-user-data)
                                       ;; Locals
                                       (rum/local false ::loading)
                                       (rum/local false ::show-success)

                                       {:after-render (fn [s]
                                        (when-not (utils/is-test-env?)
                                          (doto (js/$ "[data-toggle=\"tooltip\"]")
                                            (.tooltip "fixTitle")
                                            (.tooltip "hide")))
                                        s)
                                       :did-remount (fn [old-state new-state]
                                        (let [user-data (:user-data @(drv/get-ref new-state :edit-user-profile))]
                                          (when (and @(::loading new-state)
                                                     (not (:has-changes user-data)))
                                            (reset! (::show-success new-state) true)
                                            (reset! (::loading new-state) false)
                                            (utils/after 2500 (fn [] (reset! (::show-success new-state) false)))))
                                        new-state)}
  [s org-data real-close-cb]
  (let [user-profile-data (drv/react s :edit-user-profile)
        current-user-data (:user-data user-profile-data)
        error (:error user-profile-data)
        team-data (drv/react s :team-data)
        bots-data (jwt/team-has-bot? (:team-id org-data))
        team-roster (drv/react s :team-roster)
        slack-enabled? (user-actions/user-has-slack-with-bot? org-data team-data current-user-data bots-data team-roster)]
    [:div.user-profile-internal
      [:div.user-profile-content.group
        (when error
          [:div.user-profile-field-box.group
            [:div.user-profile-field-label
              [:span.error
                {:style {:margin-left "0px"}}
                "An error occurred while saving, please try again."]]])
        (when error
          [:div.user-profile-divider-line])
          ;; Digest frequency
        [:div.user-profile-field-box.group
          {:class utils/hide-class}
          [:div.user-profile-field-label
            "Daily newsletter"]
          [:div.user-profile-field.digest-medium
            {:class (when-not slack-enabled? "no-slack")}
            [:div.dropdown.dropdown-button
              [:button.btn-reset.user-type-btn.dropdown-toggle
                {:id "user-digest-medium-dropdown"
                 :data-toggle "dropdown"
                 :aria-haspopup true
                 :aria-expanded false}
                (case (:digest-medium current-user-data)
                  "slack"
                  "Notify me via Slack"
                  "Notify me via Email")]
              [:ul.dropdown-menu.user-type-dropdown-menu
                {:aria-labelledby "user-digest-medium-dropdown"}
                [:li
                  {:on-click #(change! s :digest-medium "email")}
                  "Notify me via Email"]
                ;; Show Slack digest option if
                (when slack-enabled?
                  [:li
                    {:on-click #(change! s :digest-medium "slack")}
                    "Notify me via Slack"])]]
            (when (and (jwt/is-admin? (:team-id org-data))
                       (not slack-enabled?)
                       (or (empty? (:slack-orgs team-data))
                           (not bots-data)))
              [:div.enable-slack
                "Want to get notified via Slack?"
                [:button.mlb-reset.add-slack-bt
                  {:on-click #(add-slack-clicked current-user-data real-close-cb)}
                  (if (empty? (:slack-orgs team-data))
                    "Add a Slack team"
                    "Add Carrot bot for Slack")]])]]
        [:div.user-profile-divider-line]
        [:div.user-profile-field-box.group
          {:class utils/hide-class}
          [:div.user-profile-field-label
            "Comments & mentions"]
          [:div.user-profile-field.notification-medium
            [:div.dropdown.dropdown-button
              [:button.btn-reset.user-type-btn.dropdown-toggle
                {:id "user-notification-medium-dropdown"
                 :data-toggle "dropdown"
                 :aria-haspopup true
                 :aria-expanded false}
                (case (:notification-medium current-user-data)
                  "slack"
                  "Notify me via Slack"
                  "email"
                  "Notify me via Email"
                  "In-app only")]
              [:ul.dropdown-menu.user-type-dropdown-menu
                {:aria-labelledby "user-notification-medium-dropdown"}
                [:li
                  {:on-click #(change! s :notification-medium "email")}
                  "Notify me via Email"]
                ;; Show Slack digest option if
                (when slack-enabled?
                  [:li
                    {:on-click #(change! s :notification-medium "slack")}
                    "Notify me via Slack"])
                [:li
                  {:on-click #(change! s :notification-medium "in-app")}
                  "In-app only"]]]]]
        [:div.user-profile-divider-line]
        [:div.user-profile-field-box.group
          {:class utils/hide-class}
          [:div.user-profile-field-label
            "Reminders"]
          [:div.user-profile-field.reminder-medium
            [:div.dropdown.dropdown-button
              [:button.btn-reset.user-type-btn.dropdown-toggle
                {:id "user-reminder-medium-dropdown"
                 :data-toggle "dropdown"
                 :aria-haspopup true
                 :aria-expanded false}
                (case (:reminder-medium current-user-data)
                  "slack"
                  "Notify me via Slack"
                  "email"
                  "Notify me via Email"
                  "In-app only")]
              [:ul.dropdown-menu.user-type-dropdown-menu
                {:aria-labelledby "user-reminder-medium-dropdown"}
                [:li
                  {:on-click #(change! s :reminder-medium "email")}
                  "Notify me via Email"]
                ;; Show Slack digest option if
                (when slack-enabled?
                  [:li
                    {:on-click #(change! s :reminder-medium "slack")}
                    "Notify me via Slack"])
                [:li
                  {:on-click #(change! s :reminder-medium "in-app")}
                  "In-app only"]]]]]
        [:div.user-profile-divider-line]]
      [:div.user-profile-footer.group
        [:button.mlb-reset.save-bt
          {:on-click #(save-clicked s)
           :class (when @(::show-success s) "no-disable")
           :disabled (not (:has-changes current-user-data))}
           (when (:loading current-user-data)
              (small-loading))
          (if @(::show-success s)
            "Saved!"
            "Save")]
        [:button.mlb-reset.cancel-bt
          {:on-click #(if (:has-changes current-user-data)
                        (user-actions/user-profile-reset)
                        (real-close-cb current-user-data))}
          "Cancel"]]]))