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
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
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

(def default-row-num 1)
(def default-user "")
(def default-user-role :author)
(def default-user-row
 {:temp-user default-user
  :user default-user
  :role default-user-role})

(defn new-user-row [s]
  (assoc default-user-row :type "email"))

(defn valid-user? [user-map]
  (utils/valid-email? (:user user-map)))

(defn has-valid-user? [users-list]
  (some valid-user? users-list))

(defn setup-initial-rows [s]
  (let [inviting-users-data @(drv/get-ref s :invite-data)
        invite-users (:invite-users inviting-users-data)]
    ;; Check if there are already invite rows
    (when (zero? (count invite-users))
      ;; if there are no rows setup the default initial rows
      (let [new-row (new-user-row s)]
        (dis/dispatch! [:input [:invite-users] (vec (repeat default-row-num new-row))])))))

(rum/defcs invite-email-modal <  ;; Mixins
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :invite-data)
  ;; Locals
  (rum/local (int (rand 10000)) ::rand)
  (rum/local "Send email invitations" ::send-bt-cta)
  (rum/local 0 ::sending)
  (rum/local 0 ::initial-sending)
  {:will-mount (fn [s]
                 (setup-initial-rows s)
                 (nux-actions/dismiss-post-added-tooltip)
                 (let [org-data @(drv/get-ref s :org-data)]
                   (org-actions/get-org org-data true)
                   (team-actions/teams-get))
                s)
   :after-render (fn [s]
                   (doto (js/$ "[data-toggle=\"tooltip\"]")
                      (.tooltip "fixTitle")
                      (.tooltip "hide"))
                   s)
   :will-update (fn [s]
                  (let [sending (::sending s)
                        initial-sending (::initial-sending s)]
                    (when (pos? @sending)
                      (let [invite-drv @(drv/get-ref s :invite-data)
                            no-error-invites (filter #(not (:error %)) (:invite-users invite-drv))
                            error-invites (filter :error (:invite-users invite-drv))
                            hold-initial-sending @initial-sending]
                        (reset! sending (count no-error-invites))
                        (when (zero? (count no-error-invites))
                          (utils/after 1000
                            (fn []
                              (reset! sending 0)
                              (reset! initial-sending 0)
                              (if (zero? (count error-invites))
                                (do
                                  (reset! (::send-bt-cta s) "Email invitations sent!")
                                  (utils/after 2500 #(reset! (::send-bt-cta s) "Send email invitations"))
                                  (notification-actions/show-notification {:title (str "Invite"
                                                                                   (when (> hold-initial-sending 1)
                                                                                     "s")
                                                                                   " sent.")
                                                                           :primary-bt-title "OK"
                                                                           :primary-bt-dismiss true
                                                                           :expire 3
                                                                           :primary-bt-inline true
                                                                           :id :invites-sent}))
                                (reset! (::send-bt-cta s) "Send email invitations"))))))))
                  s)}
  [s]
  (let [org-data (drv/react s :org-data)
        invite-users-data (drv/react s :invite-data)
        team-data (:team-data invite-users-data)
        invite-users (:invite-users invite-users-data)
        cur-user-data (:current-user-data invite-users-data)
        team-roster (:team-roster invite-users-data)
        uninvited-users (filterv #(= (:status %) "uninvited") (:users team-roster))]
    [:div.invite-email-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click #(close-clicked s nav-actions/close-all-panels)}]
      [:div.invite-email
        [:div.invite-email-header
          [:div.invite-email-header-title
            "Invite people"]
          [:button.mlb-reset.cancel-bt
            {:on-click (fn [_] (close-clicked s #(nav-actions/show-org-settings nil)))}
            "Back"]]
        [:div.invite-email-body
          [:div.invite-token-container
            [:div.invite-token-title
              "Share an invite link via email"]
            [:div.invite-token-description
              "Anyone can use this link to join your Carrot team as a contributor."]
            [:button.mlb-reset.generate-link-bt
              "Generate invite link"]]
          [:div.invites-list
            {:key "org-settings-invite-table"}
            [:div.invites-list-title
              "Invite someone with a specific permission level"]
            (for [i (range (count invite-users))
                  :let [user-data (get invite-users i)
                        key-string (str "invite-users-tabe-" i)]]
              [:div.invites-list-item
                {:key key-string}
                [:div.invites-list-item.group
                  [:div.org-settings-field-container.group
                    {:class (when (seq (:user user-data)) "has-value")}
                    [:input.org-settings-field.email-field.oc-input
                      {:type "text"
                       :class (when (:error user-data) "error")
                       :pattern "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
                       :placeholder "email@example.com"
                       :on-change #(dis/dispatch! [:input [:invite-users]
                                     (assoc invite-users i (merge user-data {:error nil :user (.. % -target -value)}))])
                       :value (or (:user user-data) "")}]
                    [:button.mlb-reset.clear-user
                      {:on-click #(when (seq (:user user-data))
                                    (dis/dispatch! [:input [:invite-users]
                                     (assoc invite-users i (merge user-data {:error nil :user ""}))]))}]]]
                [:div.user-type-dropdown
                  (user-type-dropdown {:user-id (utils/guid)
                                       :user-type (:role user-data)
                                       :hide-admin (not (jwt/is-admin? (:team-id org-data)))
                                       :on-change
                                        #(dis/dispatch!
                                          [:input
                                           [:invite-users]
                                           (assoc
                                            invite-users
                                            i
                                            (merge user-data {:role % :error nil}))])})]
                [:button.mlb-reset.remove-user
                  {:on-click #(let [before (subvec invite-users 0 i)
                                  after (subvec invite-users (inc i) (count invite-users))
                                  next-invite-users (vec (concat before after))
                                  fixed-next-invite-users (if (zero? (count next-invite-users))
                                                            [(assoc default-user-row :type (:type user-data))]
                                                            next-invite-users)]
                                (dis/dispatch! [:input [:invite-users] fixed-next-invite-users]))}]])]
          [:button.mlb-reset.add-button
            {:on-click
              #(dis/dispatch!
                [:input
                 [:invite-users]
                 (conj
                  invite-users
                  (assoc default-user-row :type "email"))])}
            [:div.add-button-plus]
            "Add another"]
          [:button.mlb-reset.save-bt
            {:on-click #(let [valid-count (count (filterv valid-user? invite-users))]
                          (reset! (::sending s) valid-count)
                          (reset! (::initial-sending s) valid-count)
                          (reset! (::send-bt-cta s) "Sending email invitations")
                          (team-actions/invite-users (:invite-users @(drv/get-ref s :invite-data))))
             :class (when (= "Email invitations sent!" @(::send-bt-cta s)) "no-disable")
             :disabled (or (not (has-valid-user? invite-users))
                           (pos? @(::sending s)))}
            @(::send-bt-cta s)]]]]))