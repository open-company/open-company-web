(ns oc.web.components.invite-settings-modal
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
            [oc.web.components.ui.user-type-dropdown :refer (user-type-dropdown)]
            [oc.web.components.ui.slack-users-dropdown :refer (slack-users-dropdown)]))

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

(def default-user-type "email")
(def default-row-num 1)
(def default-user "")
(def default-slack-user {})
(def default-user-role :author)
(def default-user-row
 {:temp-user default-user
  :user default-user
  :role default-user-role})

(defn new-user-row [s]
  (assoc default-user-row :type @(::inviting-from s)))

(defn valid-user? [user-map]
  (or (and (= (:type user-map) "email")
           (utils/valid-email? (:user user-map)))
      (and (= (:type user-map) "slack")
           (contains? (:user user-map) :slack-id)
           (contains? (:user user-map) :slack-org-id))))

(defn has-valid-user? [users-list]
  (some valid-user? users-list))

(defn user-type-did-change [s invite-users value]
  (reset! (::inviting-from s) value)
  (doseq [i (range (count invite-users))
          :let [user (get invite-users i)]]
    (if (= value "slack")
      (when (and (empty? (:user user))
                 (empty? (:temp-user user)))
        (dis/dispatch! [:input [:invite-users i :type] value]))
      (when (empty? (:user user))
        (dis/dispatch! [:update [:invite-users i] #(merge % {:type value
                                                             :user (:temp-user user)
                                                             :temp-user ""})])))))

(defn setup-initial-rows [s]
  (let [inviting-users-data @(drv/get-ref s :invite-data)
        invite-users (:invite-users inviting-users-data)
        cur-user-data (:current-user-data @(drv/get-ref s :invite-data))
        team-data (:team-data inviting-users-data)
        invite-from-default (if (and (= (:auth-source cur-user-data) "slack")
                                     (:can-slack-invite team-data))
                              "slack"
                              "email")
        old-inviting-from @(::inviting-from s)
        invite-from (or old-inviting-from invite-from-default)]
    ;; Setup the invite from if it's not already
    (when (nil? @(::inviting-from s))
      (reset! (::inviting-from s) invite-from))
    ;; Check if there are already invite rows
    (if (zero? (count invite-users))
      ;; if there are no rows setup the default initial rows
      (let [new-row (new-user-row s)]
        (dis/dispatch! [:input [:invite-users] (vec (repeat default-row-num new-row))]))
      ;; if there are change those w/o value to the current invite from
      (when (not= invite-from old-inviting-from)
        (user-type-did-change s invite-users invite-from)))))

(rum/defcs invite-settings-modal <  ;; Mixins
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :invite-data)
  ;; Locals
  (rum/local nil ::inviting-from)
  (rum/local (int (rand 10000)) ::rand)
  (rum/local "Send" ::send-bt-cta)
  (rum/local 0 ::sending)
  (rum/local 0 ::initial-sending)
  (rum/local false ::email-focused)
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
                                  (reset! (::send-bt-cta s) "Sent")
                                  (utils/after 2500 #(reset! (::send-bt-cta s) "Send"))
                                  (notification-actions/show-notification {:title (str "Invite"
                                                                                   (when (> hold-initial-sending 1)
                                                                                     "s")
                                                                                   " sent.")
                                                                           :primary-bt-title "OK"
                                                                           :primary-bt-dismiss true
                                                                           :expire 3
                                                                           :primary-bt-inline true
                                                                           :id :invites-sent}))
                                (reset! (::send-bt-cta s) "Send"))))))))
                  s)}
  [s]
  (let [org-data (drv/react s :org-data)
        invite-users-data (drv/react s :invite-data)
        team-data (:team-data invite-users-data)
        invite-users (:invite-users invite-users-data)
        cur-user-data (:current-user-data invite-users-data)
        team-roster (:team-roster invite-users-data)
        uninvited-users (filterv #(= (:status %) "uninvited") (:users team-roster))]
    [:div.invite-settings-modal
      [:button.mlb-reset.modal-close-bt
        {:on-click #(close-clicked s nav-actions/close-all-panels)}]
      [:div.invite-settings
        [:div.invite-settings-header
          [:div.invite-settings-header-title
            "Invite people"]
          [:button.mlb-reset.save-bt
            {:on-click #(let [valid-count (count (filterv valid-user? invite-users))]
                          (reset! (::sending s) valid-count)
                          (reset! (::initial-sending s) valid-count)
                          (reset! (::send-bt-cta s) "Sending")
                          (team-actions/invite-users (:invite-users @(drv/get-ref s :invite-data))))
             :class (when (= "Sent" @(::send-bt-cta s)) "no-disable")
             :disabled (or (not (has-valid-user? invite-users))
                           (pos? @(::sending s)))}
            (let [valid-users-count (count (filterv valid-user? invite-users))
                  needs-plural (> valid-users-count 1)
                  send-cta @(::send-bt-cta s)]
              (if (zero? valid-users-count)
                send-cta
                (str send-cta " " valid-users-count " Invite" (when needs-plural "s"))))]
          [:button.mlb-reset.cancel-bt
            {:on-click (fn [_] (close-clicked s #(nav-actions/show-org-settings nil)))}
            "Back"]]
        [:div.invite-settings-body
          [:div.invite-via
            [:div.invite-via-label
              "Invite via"]
            [:select.invite-via-select.oc-input
              {:disabled (not (:can-slack-invite team-data))
               :value @(::inviting-from s)
               :on-change #(user-type-did-change s invite-users (.. % -target -value))}
              [:option
                {:value "email"}
                "Email"]
              [:option
                {:value "slack"}
                "Slack"]]]
          [:div.invites-list
            {:key "org-settings-invite-table"}
            [:div.invites-list-title "Invitee"]
            (for [i (range (count invite-users))
                  :let [user-data (get invite-users i)
                        key-string (str "invite-users-tabe-" i)]]
              [:div.invites-list-item
                {:key key-string}
                [:div.invites-list-item.group
                  (if (= "slack" (:type user-data))
                    [:div.user-name-dropdown
                      {:class (when (:error user-data) "error")}
                      (rum/with-key
                       (slack-users-dropdown
                        {:on-change #(dis/dispatch! [:input [:invite-users i]
                                      (merge user-data {:user % :error nil :temp-user nil})])
                         :filter-fn (fn [user]
                                      (let [check-fn #(or
                                                       (not (:user %))
                                                       (not= (:slack-org-id (:user %)) (:slack-org-id user))
                                                       (not= (:slack-id (:user %)) (:slack-id user)))]
                                        (every? check-fn invite-users)))
                         :on-intermediate-change #(dis/dispatch! [:input [:invite-users]
                                                   (assoc invite-users i
                                                    (merge user-data {:user nil :error nil :temp-user %}))])
                          :initial-value (user-lib/name-for (:user user-data))})
                        (str "slack-users-dropdown-" (count uninvited-users) "-row-" i))]
                    [:div.org-settings-field-container.group
                      {:class (utils/class-set {:focus (= @(::email-focused s) key-string)
                                                :has-value (seq (:user user-data))})}
                      [:input.org-settings-field.email-field.oc-input
                        {:type "text"
                         :class (when (:error user-data) "error")
                         :pattern "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
                         :placeholder "email@example.com"
                         :on-focus #(reset! (::email-focused s) key-string)
                         :on-blur #(reset! (::email-focused s) false)
                         :on-change #(dis/dispatch! [:input [:invite-users]
                                       (assoc invite-users i (merge user-data {:error nil :user (.. % -target -value)}))])
                         :value (or (:user user-data) "")}]
                      [:button.mlb-reset.clear-user
                        {:on-click #(dis/dispatch! [:input [:invite-users]
                                     (assoc invite-users i (merge user-data {:error nil :user ""}))])}]])]
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
                  (assoc default-user-row :type @(::inviting-from s)))])}
            [:div.add-button-plus]
            "Add another"]]]]))