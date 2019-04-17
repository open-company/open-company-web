(ns oc.web.components.user-notifications-modal
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as mixins]
            [oc.web.stores.user :as user-stores]
            [oc.web.actions.user :as user-actions]
            [oc.web.utils.user :as user-utils]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.components.org-settings :as org-settings]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn show-modal [& [panel]]
  (dis/dispatch! [:input [:user-settings] (or panel :profile)]))

(defn real-close []
  (dis/dispatch! [:input [:user-settings] nil]))

(defn dismiss-modal [& [s]]
  (if s
    (reset! (::unmounting s) true)
    (real-close)))

(defn change! [s k v]
  (dis/dispatch! [:input [:edit-user-profile k] v])
  (dis/dispatch! [:input [:edit-user-profile :has-changes] true]))

(defn save-clicked [s]
  (when (compare-and-set! (::loading s) false true)
    (let [edit-user-profile @(drv/get-ref s :edit-user-profile)
          current-user-data @(drv/get-ref s :current-user-data)
          user-data (:user-data edit-user-profile)]
      (user-actions/user-profile-save current-user-data edit-user-profile))))

(defn add-slack-clicked [current-user-data]
  (let [switch-cb (fn []
                   (dismiss-modal)
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

(rum/defcs user-notifications-modal <
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :team-data)
  (drv/drv :team-roster)
  (drv/drv :edit-user-profile)
  (drv/drv :current-user-data)
  ;; Locals
  (rum/local false ::unmounting)
  (rum/local false ::unmounted)
  (rum/local false ::loading)
  (rum/local false ::show-success)
  ;; Mixins
  mixins/no-scroll-mixin
  mixins/first-render-mixin
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
  [s]
  (let [appear-class (and @(:first-render-done s)
                          (not @(::unmounting s))
                          (not @(::unmounted s)))
        org-data (drv/react s :org-data)
        user-profile-data (drv/react s :edit-user-profile)
        current-user-data (:user-data user-profile-data)
        error (:error user-profile-data)
        team-data (drv/react s :team-data)
        bots-data (jwt/team-has-bot? (:team-id org-data))
        team-roster (drv/react s :team-roster)
        slack-enabled? (user-utils/user-has-slack-with-bot? current-user-data bots-data team-roster)]
    [:div.user-notifications-modal-container
      {:class (utils/class-set {:appear appear-class})}
      [:button.mlb-reset.modal-close-bt
        {:on-click #(dismiss-modal s)}]
      [:div.user-notifications-modal
        [:div.user-notifications-header
          [:div.user-notifications-header-title
            "Notification settings"]
          [:button.mlb-reset.save-bt
            {:on-click #(if (:has-changes current-user-data)
                          (save-clicked s)
                          (dismiss-modal))
             :class (when @(::show-success s) "no-disable")
             :disabled @(::loading s)}
             (when (:loading current-user-data)
                (small-loading))
            (if @(::show-success s)
              "Saved!"
              "Save")]
          [:button.mlb-reset.cancel-bt
            {:on-click #(if (:has-changes current-user-data)
                          (user-actions/user-profile-reset)
                          (dismiss-modal))}
            "Back"]]
        [:div.user-notifications-body
          [:div.user-profile-modal-fields
            [:div.field-label "Daily digest"]
            [:select.field-value
              {:value (:digest-medium current-user-data)
               :disabled (not slack-enabled?)
               :on-change #(change! s :digest-medium "email")}
              [:option
                {:value "email"}
                "Via email"]
              (when slack-enabled?
                [:option
                  {:value "slack"}
                  "Via slack"])]
            [:div.field-description
              "Carrot will curate all the content you should see and deliver it to you directly each morning."]]
          [:div.user-profile-modal-fields
            [:div.field-label "Comments and mentions"]
            [:select.field-value
              {:value (:notification-medium current-user-data)
               :on-change #(change! s :notification-medium "email")}
              [:option
                {:value "email"}
                "Via email"]
              (when slack-enabled?
                [:option
                  {:value "slack"}
                  "Via Slack"])
              [:option
                {:value "in-app"}
                "In-app"]]]
          [:div.user-profile-modal-fields
            [:div.field-label "Reminders"]
            [:select.field-value
              {:value (:reminder-medium current-user-data)
               :on-change #(change! s :reminder-medium "email")}
              [:option
                {:value "email"}
                "Via email"]
              (when slack-enabled?
                [:option
                  {:value "slack"}
                  "Via Slack"])
              [:option
                {:value "in-app"}
                "In-app"]]]]]]))