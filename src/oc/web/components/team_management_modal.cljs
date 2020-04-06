(ns oc.web.components.team-management-modal
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.user :as user-lib]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.user :as uu]
            [oc.web.stores.user :as user-store]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.user-type-dropdown :refer (user-type-dropdown)]))

(defn user-action [team-id user action method other-link-params remove-cb]
  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide")
  (team-actions/user-action team-id user action method other-link-params nil remove-cb))

(defn real-remove-fn [s author user team-id remove-cb]
  (swap! (::removing s) #(set (conj % (:user-id user))))
  (when author
    (team-actions/remove-author author))
  (user-action team-id user "remove" "DELETE"  {:ref "application/vnd.open-company.user.v1+json"} remove-cb))

(defn alert-resend-done []
  (notification-actions/show-notification {:title "Invitation resent"
                                           :primary-bt-title "OK"
                                           :primary-bt-dismiss true
                                           :expire 3
                                           :id :invitation-resent}))

(defn user-match [query user]
  (let [r (js/RegExp (str "^.*(" query ").*$") "i")]
    (or (and (:name user) (.match (:name user) r))
        (and (:first-name user) (.match (:first-name user) r))
        (and (:last-name user) (.match (:last-name user) r))
        (and (:email user) (.match (:email user) r)))))

(rum/defcs team-management-modal <
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :invite-data)
  ;; Locals
  (rum/local false ::resending-invite)
  (rum/local "" ::query)
  (rum/local #{} ::removing)
  {:will-mount (fn [s]
    (let [org-data @(drv/get-ref s :org-data)]
      (org-actions/get-org org-data true)
      (team-actions/teams-get))
    s)
   :after-render (fn [s]
    (doto (js/$ "[data-toggle=\"tooltip\"]")
     (.tooltip "fixTitle")
     (.tooltip "hide"))
    (when @(::resending-invite s)
      (let [invite-users-data (:invite-users @(drv/get-ref s :invite-data))]
        (when (zero? (count invite-users-data))
          (alert-resend-done)
          (reset! (::resending-invite s) false))))
    s)}
  [s]
  (let [org-data (drv/react s :org-data)
        invite-users-data (drv/react s :invite-data)
        team-data (:team-data invite-users-data)
        team-roster (:team-roster invite-users-data)
        cur-user-data (:current-user-data invite-users-data)
        org-authors (:authors org-data)
        all-users (if team-data
                    (:users team-data)
                    (filter :user-id (:users team-roster)))
        filtered-users (if (seq @(::query s))
                         (filter #(user-match @(::query s) %) all-users)
                         all-users)
        splitted-users (group-by #(= (:user-id %) (:user-id cur-user-data)) filtered-users)
        self-user (-> splitted-users (get true) first)
        other-users (get splitted-users false)
        other-sorted-users (reverse (sort-by user-lib/name-for other-users))
        sorted-users (if self-user
                       (concat [self-user] other-sorted-users)
                       other-sorted-users)
        user-role (user-store/user-role org-data cur-user-data)
        is-admin-or-author? (#{:admin :author} user-role)
        is-admin? (jwt/is-admin? (:team-id org-data))]
    [:div.team-management-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click nav-actions/close-all-panels}]
      [:div.team-management
        [:div.team-management-header
          [:div.team-management-header-title
            (if is-admin?
              "Manage team"
              "View team")]
          (when is-admin-or-author?
            [:button.mlb-reset.save-bt
              {:on-click #(nav-actions/show-org-settings :invite-picker)}
              "Invite"])
          [:button.mlb-reset.cancel-bt
            {:on-click #(nav-actions/show-org-settings nil)}
            "Back"]]
        [:div.team-management-body
          [:div.team-management-body-title
            (str (count all-users) " member" (when (> (count all-users) 1) "s"))]
          [:div.team-management-search-users
            [:input.org-settings-team-search-field.oc-input
              {:value @(::query s)
               :placeholder "Search by name..."
               :on-change #(reset! (::query s) (.. % -target -value))}]]
          [:div.team-management-users-list
            (for [user sorted-users
                  :let [user-type (utils/get-user-type user (dis/org-data))
                        author (some #(when (= (:user-id %) (:user-id user)) %) org-authors)
                        pending? (and (= "pending" (:status user))
                                      (or (contains? user :email)
                                          (contains? user :slack-id)))
                        current-user (= (:user-id user) (:user-id cur-user-data))
                        display-name (user-lib/name-for user)
                        removing? (@(::removing s) (:user-id user))
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
                                                         (real-remove-fn s author user (:team-id team-data)
                                                          (fn [{:keys [success]}]
                                                            (when success
                                                              (notification-actions/show-notification
                                                               {:title (if pending?
                                                                         "Invitation cancelled"
                                                                         "Member removed from team")
                                                                :primary-bt-title "OK"
                                                                :primary-bt-dismiss true
                                                                :expire 3
                                                                :id (if pending?
                                                                     :cancel-invitation
                                                                     :member-removed-from-team)}))))
                                                         (alert-modal/hide-alert))}]
                                      (alert-modal/show-alert alert-data)))
                        roster-user (first (filterv #(= (:user-id %) (:user-id user)) (:users team-roster)))
                        resend-fn (fn []
                                    (let [invitation-type (if (contains? roster-user :slack-id) "slack" "email")
                                          inviting-user (if (= invitation-type "email")
                                                         (:email user)
                                                         (select-keys
                                                          roster-user
                                                          [:email :avatar-url :first-name :last-name :slack-id :slack-org-id]))]
                                      (dis/dispatch! [:input [:invite-users]
                                                       [{:user inviting-user
                                                         :type invitation-type
                                                         :role user-type
                                                         :error nil}]])
                                      (reset! (::resending-invite s) true)
                                      (team-actions/invite-users (:invite-users @(drv/get-ref s :invite-data)) "")))
                        ;; Retrieve the Slack display name for pending and active users
                        slack-display-name (if (or (= (:status user) "uninvited")
                                                   (= (:status user) "pending"))
                                            (:slack-display-name roster-user)
                                            (some #(when (seq (:display-name %)) (:display-name %)) (vals (:slack-users roster-user))))
                        ;; Add @ in front of the slack display name if it's not there already
                        fixed-display-name (if (and (seq slack-display-name)
                                                    (not= slack-display-name "-")
                                                    (not (clojure.string/starts-with? slack-display-name "@")))
                                             (str "@" slack-display-name)
                                             slack-display-name)]]
              [:div.team-management-users-item.group
                {:key (str "org-settings-team-" (:user-id user))
                 :class (when pending? "is-pending-user")}
                (if removing?
                  (small-loading)
                  (user-avatar-image user))
                [:div.user-name
                  [:button.mlb-reset.user-name-label
                    {:title (str "<span>" (:email user)
                              (when (seq fixed-display-name)
                                (str " | <i class=\"mdi mdi-slack\"></i>" (when-not (= fixed-display-name "-") (str " " fixed-display-name))))
                              "</span>")
                     :class (utils/class-set {:pending pending?
                                              :removing removing?})
                     :on-click #(when (uu/active? user)
                                  (utils/event-stop %)
                                  (nav-actions/show-user-info (:user-id user)))
                     :data-toggle "tooltip"
                     :data-html "true"
                     :data-placement "top"}
                    display-name
                    (when current-user
                      [:span.current-user " (you)"])]
                  (when pending?
                    [:div.pending-user
                      " (pending"
                      (when is-admin?
                        ": ")
                      (when is-admin?
                        [:button.mlb-reset.resend-pending-bt
                          {:on-click resend-fn
                           :data-toggle "tooltip"
                           :data-placement "top"
                           :data-container "body"
                           :title (str "Resend invitation via " (if (seq fixed-display-name) "slack" "email"))}
                          "resend"])
                      (when is-admin?
                        [:button.mlb-reset.remove-pending-bt
                          {:on-click remove-fn
                           :data-toggle "tooltip"
                           :data-placement "top"
                           :data-container "body"
                           :title "Cancel invitation"}
                          "cancel"])
                      ")"])]
                [:div.user-role
                  (if (or current-user
                          (not is-admin?))
                    [:span.self-user-type (name user-type)]
                    (user-type-dropdown {:user-id (:user-id user)
                                         :user-type user-type
                                         :disabled? removing?
                                         :on-change #(team-actions/switch-user-type user user-type % user author)
                                         :hide-admin (not is-admin?)
                                         :on-remove (if (and (not= "pending" (:status user))
                                                             (not= (:user-id user) (:user-id cur-user-data)))
                                                      remove-fn
                                                      nil)}))]])]]]]))