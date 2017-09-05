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

(defn add-inviting-user [s]
  (let [type @(::inviting-from s)
        user (if (= "slack" type)
                @(::inviting-slack s)
                @(::inviting-email s))
        role @(::inviting-user-type s)
        invited-users (or (:invite-users @(drv/get-ref s :invite-users)) [])]
    (reset! (::inviting-email s) "")
    (reset! (::inviting-slack s) "")
    (reset! (::inviting-user-type s) :viewer)
    (dis/dispatch! [:input [:invite-users] (conj invited-users {:type type :user user :role (or role :viewer)})])))

(def default-user-type "email")
(def default-row-num 3)
(def default-user "")
(def default-user-role :viewer)
(def default-user-row {:type default-user-type :user default-user :role default-user-role})

(defn valid-user? [user-map]
  (or (and (= (:type user-map) "email")
           (utils/valid-email? (:user user-map)))
      (and (= (:type user-map) "slack")
           (contains? (:user user-map) :slack-id)
           (contains? (:user user-map) :slack-org-id))))

(defn has-valid-user? [users-list]
  (some #(valid-user? %) users-list))

(defn user-type-did-change [s invite-users e]
  (let [value (.. e -target -value)]
    (when @(::last-focused-user s)
      (let [idx (utils/index-of invite-users #(= (:user %) (:user @(::last-focused-user s))))
            updated-user (merge @(::last-focused-user s) {:type value :user (if (= "email" value) "" {})})]
         (dis/dispatch! [:input [:invite-users] (assoc invite-users idx updated-user)])))
    (reset! (::inviting-from s) value)))

(rum/defcs org-settings-invite-panel
  < rum/reactive
    (drv/drv :invite-users)
    (rum/local "email" ::inviting-from)
    (rum/local "" ::inviting-email)
    (rum/local "" ::inviting-slack)
    (rum/local :viewer ::inviting-user-type)
    (rum/local nil ::last-focused-user)
    {:will-mount (fn [s]
                   (let [inviting-users-data @(drv/get-ref s :invite-users)
                        invite-users (:invite-users inviting-users-data)]
                     (when (zero? (count invite-users))
                      (dis/dispatch! [:input [:invite-users] (vec (repeat default-row-num default-user-row))])))
                   s)
     :before-render (fn [s]
                     (let [invite-users-data @(drv/get-ref s :invite-users)]
                       (when (and (:auth-settings invite-users-data)
                                  (not (:teams-data-requested invite-users-data)))
                         (dis/dispatch! [:teams-get])))
                     s)
     :after-render (fn [s]
                     (doto (js/$ "[data-toggle=\"tooltip\"]")
                        (.tooltip "fixTitle")
                        (.tooltip "hide"))
                     s)}
  [s org-data]
  (let [invite-users-data (drv/react s :invite-users)
        team-data (:team-data invite-users-data)
        invite-users (:invite-users invite-users-data)
        cur-user-data (:current-user-data invite-users-data)
        team-roster (:team-roster invite-users-data)
        uninvited-users (filter #(= (:status %) "uninvited") (:users team-roster))]
    [:div.org-settings-panel
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
        [:div.org-settings-panel-choice
          [:input
            {:type "radio"
             :on-change (partial user-type-did-change s invite-users)
             :value "slack"
             :checked (= "slack" @(::inviting-from s))
             :id "org-settings-invit-from-medium-slack"}]
          [:label
            {:for "org-settings-invit-from-medium-slack"}
            "Slack"]]]
      ;; Panel rows
      [:div.org-settings-invite-table.org-settings-panel-row
        ;; Team table
        [:table.org-settings-table
          [:thead
            [:tr
              [:th "Email Addresses"]
              [:th.role "Role "
                [:i.mdi.mdi-information-outline]]
              [:th ""]]]
          [:tbody
            (for [i (range (count invite-users))
                  :let [user-data (get invite-users i)]]
              [:tr
                {:key (str "invite-users-tabe-" i)}
                [:td.user-field
                  (if (= "slack" (:type user-data))
                    [:div
                      {:class (when (:error user-data) "error")}
                      (rum/with-key
                        (slack-users-dropdown {:on-change #(dis/dispatch! [:input [:invite-users] (assoc invite-users i (merge user-data {:user %}))])
                                               :on-intermediate-change #(dis/dispatch! [:input [:invite-users] (assoc invite-users i (merge user-data {:user nil}))])
                                               :initial-value (utils/name-or-email (:user user-data))
                                               :on-focus #(do
                                                           (reset! (::inviting-from s) (:type user-data))
                                                           (reset! (::last-focused-user s) user-data))
                                               :on-blur (fn []
                                                          (utils/after 500 #(reset! (::last-focused-user s) nil)))})
                        (str "slack-users-dropdown-" (count uninvited-users) "-row-" i))]
                    [:input.org-settings-field.email-field
                      {:type "text"
                       :class (when (:error user-data) "error")
                       :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
                       :placeholder "email@example.com"
                       :on-change #(dis/dispatch! [:input [:invite-users] (assoc invite-users i (merge user-data {:user (.. % -target -value)}))])
                       :on-focus #(do
                                    (reset! (::inviting-from s) (:type user-data))
                                    (reset! (::last-focused-user s) user-data))
                       :on-blur (fn []
                                  (utils/after 500 #(reset! (::last-focused-user s) nil)))
                       :value (:user user-data)}])]
                [:td.user-type-field
                  [:div.user-type-dropdown
                    (user-type-dropdown {:user-id (utils/guid)
                                         :user-type (:role user-data)
                                         :hide-admin (not (jwt/is-admin? (:team-id org-data)))
                                         :on-change #(dis/dispatch! [:input [:invite-users] (assoc invite-users i (merge user-data {:role %}))])})]]
                [:td.user-remove
                  [:button.mlb-reset.remove-user
                    {:on-click #(dis/dispatch! [:input [:invite-users] (utils/vec-dissoc invite-users user-data)])}
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
          {:on-click #(dis/dispatch! [:invite-users])
           :disabled (not (has-valid-user? invite-users))}
          (let [valid-users-count (count (filter #(valid-user? %) invite-users))
                needs-plural (> valid-users-count 1)]
            (if (zero? valid-users-count)
              "Send"
              (str "Send " valid-users-count " Invite" (when needs-plural "s"))))]
        [:button.mlb-reset.mlb-link-black.cancel-btn
          {:on-click #(dis/dispatch! [:input [:invite-users] []])}
          "Cancel"]]]))