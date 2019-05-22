(ns oc.web.components.ui.org-settings-invite-panel
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.user-type-dropdown :refer (user-type-dropdown)]
            [oc.web.components.ui.slack-users-dropdown :refer (slack-users-dropdown)]))

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

(rum/defcs org-settings-invite-panel
  < rum/reactive
    (drv/drv :invite-data)
    (rum/local nil ::inviting-from)
    (rum/local (int (rand 10000)) ::rand)
    (rum/local "Send" ::send-bt-cta)
    (rum/local 0 ::sending)
    (rum/local 0 ::initial-sending)
    {:will-mount (fn [s]
                   (setup-initial-rows s)
                   (nux-actions/dismiss-post-added-tooltip)
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
                                                                            :expire 10
                                                                            :primary-bt-inline true
                                                                            :id :invites-sent}))
                                 (reset! (::send-bt-cta s) "Send"))))))))
                   s)
     :did-update (fn [s]
                   (setup-initial-rows s)
                   s)}
  [s org-data dismiss-settings-cb]
  (let [invite-users-data (drv/react s :invite-data)
        team-data (:team-data invite-users-data)
        invite-users (:invite-users invite-users-data)
        cur-user-data (:current-user-data invite-users-data)
        team-roster (:team-roster invite-users-data)
        uninvited-users (filterv #(= (:status %) "uninvited") (:users team-roster))]
    [:div.org-settings-panel.org-settings-invite-panel
      [:div.org-settings-invite-panel-inner
        [:div.org-settings-panel-row.invite-from.group
          [:div.invite-from-label "Invite people via:"]
          [:div.org-settings-panel-choice
            {:on-click #(user-type-did-change s invite-users "email")
             :class (utils/class-set {:active (= "email" @(::inviting-from s))})}
            "Email"]
          (let [has-slack-org? (:has-slack-org team-data)
                slack-enabled? (:can-slack-invite team-data)
                has-slack-user? (pos? (count (:slack-users cur-user-data)))]
            (if slack-enabled?
              [:div.org-settings-panel-choice
                {:on-click #(user-type-did-change s invite-users "slack")
                 :class (utils/class-set {:disabled (not slack-enabled?)
                                          :active (= "slack" @(::inviting-from s))})}
                "Slack"]
              [:div.org-settings-panel-slack-fallback
                [:span.slack-copy
                  "Want to invite your team via Slack? "]
                (let [redirect-to (js/encodeURIComponent
                                   (str (router/get-token) "?org-settings=invite"))]
                  [:button.mlb-reset.add-slack-team
                    {:on-click #(if has-slack-user?
                                  (org-actions/bot-auth team-data cur-user-data redirect-to)
                                  (team-actions/slack-team-add cur-user-data redirect-to))}
                    (if has-slack-user?
                      "Enable Carrot Bot"
                      "Add a Slack team")])]))]
        ;; Panel rows
        [:div.org-settings-invite-table-container.org-settings-panel-row
          ;; Team table
          [:table.org-settings-table.org-settings-invite-table
            {:class utils/hide-class}
            [:thead
              [:tr
                [:th "Invitee"
                  [:span.error
                    (when-let [first-error-user (first (filter :error invite-users))]
                      (cond
                        (string? (:error first-error-user))
                        (:error first-error-user)
                        :else
                        "Invalid user"))]]
                [:th.role "Role "
                  [:i.mdi.mdi-information-outline
                    {:title "Contributors and admins can edit posts and invite users. Admins manage the team, invites and billing."
                     :data-placement "top"
                     :data-toggle "tooltip"}]]
                [:th ""]]]
            [:tbody
              {:key (str "org-settings-invite-table-" @(::rand s))}
              (for [i (range (count invite-users))
                    :let [user-data (get invite-users i)]]
                [:tr
                  {:key (str "invite-users-tabe-" i "-" @(::rand s))}
                  [:td.user-field
                    (if (= "slack" (:type user-data))
                      [:div
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
                            :initial-value (utils/name-or-email (:user user-data))})
                          (str "slack-users-dropdown-" (count uninvited-users) "-row-" i))]
                      [:input.org-settings-field.email-field
                        {:type "text"
                         :class (when (:error user-data) "error")
                         :pattern utils/valid-email-pattern
                         :placeholder "email@example.com"
                         :on-change #(dis/dispatch!
                                      [:input
                                       [:invite-users]
                                       (assoc
                                        invite-users
                                        i
                                        (merge user-data {:error nil :user (.. % -target -value)}))])
                         :value (or (:user user-data) "")}])]
                  [:td.user-type-field
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
                                                (merge user-data {:role % :error nil}))])})]]
                  [:td.user-remove
                    [:button.mlb-reset.remove-user
                      {:on-click #(let [before (subvec invite-users 0 i)
                                        after (subvec invite-users (inc i) (count invite-users))
                                        next-invite-users (vec (concat before after))
                                        fixed-next-invite-users (if (zero? (count next-invite-users))
                                                                  [(assoc default-user-row :type (:type user-data))]
                                                                  next-invite-users)]
                                    (dis/dispatch! [:input [:invite-users] fixed-next-invite-users]))}
                      [:i.mdi.mdi-delete]]]])]
            [:tbody
              [:tr
                [:td
                  [:button.mlb-reset.add-button
                    {:on-click
                      #(dis/dispatch!
                        [:input
                         [:invite-users]
                         (conj
                          invite-users
                          (assoc default-user-row :type @(::inviting-from s)))])}
                    "+ add another"]]
                [:td]
                [:td]]]]]]
      ;; Save and cancel buttons
      [:div.org-settings-footer.group
        [:button.mlb-reset.mlb-default.save-btn
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
        [:button.mlb-reset.mlb-link-black.cancel-btn
          {:on-click #(do
                       (reset! (::rand s) (int (rand 10000)))
                       (dis/dispatch!
                        [:input
                         [:invite-users]
                         (vec
                          (repeat
                           default-row-num
                           (assoc default-user-row :type @(::inviting-from s))))])
                       (dismiss-settings-cb))}
          "Cancel"]]]))