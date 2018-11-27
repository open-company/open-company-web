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
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.user-type-dropdown :refer (user-type-dropdown)]))

(defn user-action [team-id user action method other-link-params remove-cb]
  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide")
  (team-actions/user-action team-id user action method other-link-params nil remove-cb))

(defn real-remove-fn [author user team-id remove-cb]
  (when author
    (team-actions/remove-author author))
  (user-action team-id user "remove" "DELETE"  {:ref "application/vnd.open-company.user.v1+json"} remove-cb))

(defn alert-resend-done []
  (notification-actions/show-notification {:title "Invitation resent"
                                           :primary-bt-title "OK"
                                           :primary-bt-dismiss true
                                           :expire 10
                                           :id :invitation-resent}))

(defn user-match [query user]
  (let [r (js/RegExp (str "^.*(" query ").*$") "i")]
    (or (and (:name user) (.match (:name user) r))
        (and (:first-name user) (.match (:first-name user) r))
        (and (:last-name user) (.match (:last-name user) r))
        (and (:email user) (.match (:email user) r)))))

(rum/defcs org-settings-team-panel
  < rum/reactive
    (drv/drv :invite-data)
    (rum/local false ::resending-invite)
    (rum/local "" ::query)
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
        org-authors (:authors org-data)
        all-users (:users team-data)
        filtered-users (if (seq @(::query s))
                         (filter #(user-match @(::query s) %) all-users)
                         all-users)
        sorted-users (sort-by utils/name-or-email filtered-users)
        team-roster (:team-roster invite-users-data)]
    [:div.org-settings-panel
      ;; Panel rows
      [:div.org-settings-team.org-settings-panel-row
        (when true ;(> (count all-users) 4)
          ;; Search team members
          [:div.org-settings-team-search.org-settings-field
            [:input.org-settings-team-search-field
              {:value @(::query s)
               :placeholder "Find a teammate..."
               :on-change #(reset! (::query s) (.. % -target -value))}]])
        ;; Team table
        [:table.org-settings-table.org-settings-team-table
          {:class utils/hide-class}
          [:thead
            [:tr
              [:th "MEMBER"]
              [:th.role "ROLE"]]]
          [:tbody
            (for [user sorted-users
                  :let [user-type (utils/get-user-type user (dis/org-data))
                        author (some #(when (= (:user-id %) (:user-id user)) %) org-authors)
                        pending? (and (= "pending" (:status user))
                                      (or (contains? user :email)
                                          (contains? user :slack-id)))
                        display-name (utils/name-or-email user)
                        remove-fn (fn []
                                    (let [alert-data {:icon "/img/ML/trash.svg"
                                                      :action
                                                       (if pending?
                                                        "cancel-invitation"
                                                        "remove-user")
                                                      :title (str "Remove " display-name "?")
                                                      :message
                                                       (if pending?
                                                        "Are you sure you want to cancel this invitation?"
                                                        [:span
                                                          "This user will no longer be able to access your team on Carrot."
                                                          [:br][:br]
                                                          "Are you sure you want to remove this user?"])
                                                      :link-button-title (if pending?
                                                                           "No, keep it"
                                                                           "No, keep them")
                                                      :link-button-cb #(alert-modal/hide-alert)
                                                      :solid-button-style :red
                                                      :solid-button-title (if pending?
                                                                            "Yes, cancel"
                                                                            "Yes, remove")
                                                      :solid-button-cb
                                                       #(do
                                                         (real-remove-fn author user (:team-id team-data)
                                                          (fn []
                                                            (notification-actions/show-notification
                                                             {:title (if pending?
                                                                       "Invitation cancelled"
                                                                       "Member removed from team")
                                                              :primary-bt-title "OK"
                                                              :primary-bt-dismiss true
                                                              :expire 10
                                                              :id (if pending?
                                                                   :cancel-invitation
                                                                   :member-removed-from-team)})))
                                                         (alert-modal/hide-alert))}]
                                      (alert-modal/show-alert alert-data)))
                        roster-user (first (filterv #(= (:user-id %) (:user-id user)) (:users team-roster)))
                        resend-fn (fn []
                                    (let [invitation-type (if (contains? roster-user :slack-id) "slack" "email")
                                          inviting-user (if (= invitation-type "email")
                                                         (:email user)
                                                         (select-keys
                                                          roster-user
                                                          [:email :first-name :last-name :slack-id :slack-org-id]))]
                                      (dis/dispatch! [:input [:invite-users]
                                                       [{:user inviting-user
                                                         :type invitation-type
                                                         :role user-type
                                                         :error nil}]])
                                      (reset! (::resending-invite s) true)
                                      (team-actions/invite-users (:invite-users @(drv/get-ref s :invite-data)) "")))
                        slack-display-name (if (or (= (:status user) "uninvited")
                                                   (= (:status user) "pending"))
                                            (:slack-display-name roster-user)
                                            (some #(when (seq (:display-name %)) (:display-name %)) (vals (:slack-users roster-user))))]]
              [:tr
                {:key (str "org-settings-team-" (:user-id user))}
                [:td.user-name
                  {:class (when (some #(= (:status %) "pending") (:users team-data)) "has-pending")}
                  (user-avatar-image user)
                  [:div.user-name-label
                    {:title (str "<span>" (:email user)
                              (when (seq slack-display-name)
                                (str " | <i class=\"mdi mdi-slack\"></i> " slack-display-name))
                              "</span>")
                     :class (when pending? "pending")
                     :data-toggle "tooltip"
                     :data-html "true"
                     :data-placement "top"}
                    display-name]
                  (when pending?
                    [:div.pending-user
                      " (pending: "
                      [:button.mlb-reset.resend-pending-bt
                        {:on-click resend-fn
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :data-container "body"
                         :title (str "Resend invitation via " (if (seq slack-display-name) "slack" "email"))}
                        "resend"]
                      " or "
                      [:button.mlb-reset.remove-pending-bt
                        {:on-click remove-fn
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :data-container "body"
                         :title "Cancel invitation"}
                        "cancel"]
                      ")"])]
                [:td.role
                  (user-type-dropdown {:user-id (:user-id user)
                                       :user-type user-type
                                       :on-change #(team-actions/switch-user-type user user-type % user author)
                                       :hide-admin (not (jwt/is-admin? (:team-id org-data)))
                                       :on-remove (if (and (not= "pending" (:status user))
                                                           (not= (:user-id user) (:user-id cur-user-data)))
                                                    remove-fn
                                                    nil)})]])]]]]))