(ns oc.web.components.ui.org-settings-invite-panel
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.user-type-picker :refer (user-type-dropdown)]
            [oc.web.components.ui.slack-users-dropdown :refer (slack-users-dropdown)]))

(def default-user-type "email")
(def default-row-num 1)
(def default-user "")
(def default-slack-user {})
(def default-user-role :author)
(def default-user-row {:type default-user-type :temp-user default-user :user default-user :role default-user-role})

(defn valid-user? [user-map]
  (or (and (= (:type user-map) "email")
           (utils/valid-email? (:user user-map)))
      (and (= (:type user-map) "slack")
           (contains? (:user user-map) :slack-id)
           (contains? (:user user-map) :slack-org-id))))

(defn has-valid-user? [users-list]
  (some valid-user? users-list))

(defn user-type-did-change [s invite-users e]
  (let [value (.. e -target -value)]
    (reset! (::inviting-from s) value)
    (doseq [i (range (count invite-users))
            :let [user (get invite-users i)]]
      (when (and (empty? (:user user))
                 (empty? (:temp-user user)))
        (dis/dispatch! [:input [:invite-users i :type] value])))))

(defn setup-initial-rows [s]
  (let [inviting-users-data @(drv/get-ref s :invite-users)
        invite-users (:invite-users inviting-users-data)]
    (when (zero? (count invite-users))
      (dis/dispatch! [:input [:invite-users] (vec (repeat default-row-num default-user-row))]))))

(defn has-dirty-data? [s]
  (let [invite-users-data @(drv/get-ref s :invite-users)
        invite-users (:invite-users invite-users-data)]
    (some #(not (empty? (:user %))) invite-users)))

(rum/defcs org-settings-invite-panel
  < rum/reactive
    (drv/drv :invite-users)
    (rum/local "email" ::inviting-from)
    (rum/local (int (rand 10000)) ::rand)
    (rum/local "Send" ::send-bt-cta)
    (rum/local 0 ::sending)
    {:will-mount (fn [s]
                   (setup-initial-rows s)
                   s)
     :after-render (fn [s]
                     (doto (js/$ "[data-toggle=\"tooltip\"]")
                        (.tooltip "fixTitle")
                        (.tooltip "hide"))
                     s)
     :will-update (fn [s]
                   (let [sending @(::sending s)]
                     (when (pos? sending)
                       (let [invite-drv @(drv/get-ref s :invite-users)
                             no-error-invites (vec (filter #(not (:error %)) (:invite-users invite-drv)))]
                         (reset! (::sending s) (count no-error-invites))
                         (when (zero? (count no-error-invites))
                           (utils/after 1000
                             (fn []
                               (reset! (::send-bt-cta s) "Sent")
                               (reset! (::sending s) 0)
                               (utils/after 2500 #(reset! (::send-bt-cta s) "Send"))))))))
                   s)
     :did-update (fn [s]
                   (setup-initial-rows s)
                   s)}
  [s org-data dismiss-settings-cb]
  (let [invite-users-data (drv/react s :invite-users)
        team-data (:team-data invite-users-data)
        invite-users (:invite-users invite-users-data)
        cur-user-data (:current-user-data invite-users-data)
        team-roster (:team-roster invite-users-data)
        uninvited-users (vec (filter #(= (:status %) "uninvited") (:users team-roster)))]
    [:div.org-settings-panel.org-settings-invite-panel
      [:div.org-settings-panel-row.invite-from.group
        [:div.invite-from-label "Invite with:"]
        [:div.org-settings-panel-choice
          [:input
            {:type "radio"
             :on-change (partial user-type-did-change s invite-users)
             :value "email"
             :checked (= "email" @(::inviting-from s))
             :id "org-settings-invit-from-medium-email"}]
          [:label
            {:for "org-settings-invit-from-medium-email"}
            "Email"]]
        (let [slack-enabled? (jwt/team-has-bot? (:team-id team-data))]
          [:div.org-settings-panel-choice
            [:input
              {:type "radio"
               :on-change (partial user-type-did-change s invite-users)
               :value "slack"
               :checked (= "slack" @(::inviting-from s))
               :disabled (not slack-enabled?)
               :id "org-settings-invit-from-medium-slack"}]
            [:label
              {:for "org-settings-invit-from-medium-slack"
               :style {:opacity (if slack-enabled? 1 0.4)}}
              "Slack"]])]
      ;; Panel rows
      [:div.org-settings-invite-table.org-settings-panel-row
        ;; Team table
        [:table.org-settings-table
          [:thead
            [:tr
              [:th "Invitee"
                [:span.error
                  (when-let [first-error-user (first (filter #(:error %) invite-users))]
                    (cond
                      (string? (:error first-error-user))
                      (:error first-error-user)
                      :else
                      "Invalid user"))]]
              [:th.role "Role "
                [:i.mdi.mdi-information-outline
                  {:title "Contributors and admins can view and edit. Admins manage the team, invites and billing."
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
                        (slack-users-dropdown {:on-change #(dis/dispatch! [:input [:invite-users i] (merge user-data {:user % :error nil :temp-user nil})])
                                               :on-intermediate-change #(dis/dispatch! [:input [:invite-users] (assoc invite-users i (merge user-data {:user nil :error nil :temp-user %}))])
                                               :initial-value (utils/name-or-email (:user user-data))})
                        (str "slack-users-dropdown-" (count uninvited-users) "-row-" i))]
                    [:input.org-settings-field.email-field
                      {:type "text"
                       :class (when (:error user-data) "error")
                       :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
                       :placeholder "email@example.com"
                       :on-change #(dis/dispatch! [:input [:invite-users] (assoc invite-users i (merge user-data {:error nil :user (.. % -target -value)}))])
                       :value (:user user-data)}])]
                [:td.user-type-field
                  [:div.user-type-dropdown
                    (user-type-dropdown {:user-id (utils/guid)
                                         :user-type (:role user-data)
                                         :hide-admin (not (jwt/is-admin? (:team-id org-data)))
                                         :on-change #(dis/dispatch! [:input [:invite-users] (assoc invite-users i (merge user-data {:role % :error nil}))])})]]
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
                [:button.mlb-reset.mlb-default.add-button
                  {:on-click #(dis/dispatch! [:input [:invite-users] (conj invite-users (assoc default-user-row :type @(::inviting-from s)))])}
                  "+"]]
              [:td]
              [:td]]]]]
      ;; Save and cancel buttons
      [:div.org-settings-footer.group
        [:button.mlb-reset.mlb-default.save-btn
          {:on-click #(do
                        (reset! (::sending s) (count (vec (filter valid-user? invite-users))))
                        (reset! (::send-bt-cta s) "Sending")
                        (dis/dispatch! [:invite-users]))
           :class (when (= "Sent" @(::send-bt-cta s)) "no-disable")
           :disabled (or (not (has-valid-user? invite-users))
                         (pos? @(::sending s)))}
          (let [valid-users-count (count (vec (filter valid-user? invite-users)))
                needs-plural (> valid-users-count 1)
                send-cta @(::send-bt-cta s)]
            (if (zero? valid-users-count)
              send-cta
              (str send-cta " " valid-users-count " Invite" (when needs-plural "s"))))]
        [:button.mlb-reset.mlb-link-black.cancel-btn
          {:on-click #(if (has-dirty-data? s)
                        (do
                          (reset! (::rand s) (int (rand 10000)))
                          (dis/dispatch! [:input [:invite-users] (vec (repeat default-row-num (assoc default-user-row :type @(::inviting-from s))))]))
                        (dismiss-settings-cb))}
          "Cancel"]]]))