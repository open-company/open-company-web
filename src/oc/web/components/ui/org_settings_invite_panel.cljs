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

(rum/defcs org-settings-invite-panel
  < rum/reactive
    (drv/drv :invite-users)
    (rum/local "email" ::inviting-from)
    (rum/local "" ::inviting-email)
    (rum/local "" ::inviting-slack)
    (rum/local :viewer ::inviting-user-type)
    {:before-render (fn [s]
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
        cur-user-data (:current-user-data invite-users-data)]
    [:div.org-settings-panel
      [:div.org-settings-panel-row.invite-from.group
        [:div.invite-from-label "Invite with:"]
        [:div.org-settings-panel-choice
          [:input
            {:type "radio"
             :on-change #(reset! (::inviting-from s) (.. % -target -value))
             :value "email"
             :checked (= "email" @(::inviting-from s))
             :id "org-settings-invit-from-medium-email"}]
          [:label
            {:for "org-settings-invit-from-medium-email"}
            "Email"]]
        [:div.org-settings-panel-choice
          [:input
            {:type "radio"
             :on-change #(reset! (::inviting-from s) (.. % -target -value))
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
                      (slack-users-dropdown {:on-change #()
                                             :disabled true
                                             :on-intermediate-change #()
                                             :initial-value (utils/name-or-email (:user user-data))})]
                    [:input.org-settings-field.email-field
                      {:type "text"
                       :class (when (:error user-data) "error")
                       :disabled true
                       :value (:user user-data)}])]
                [:td.user-type-field
                  [:div.user-type-dropdown
                    (user-type-dropdown (utils/guid) (:role user-data) #(dis/dispatch! [:input [:invite-users] (assoc invite-users i (merge user-data {:role %}))]))]]
                [:td.user-remove
                  [:button.mlb-reset.remove-user
                    {:on-click #(dis/dispatch! [:input [:invite-users] (utils/vec-dissoc invite-users user-data)])}
                    [:i.mdi.mdi-delete]]]])]
          (let [plus-enabled (or (and (= "email" @(::inviting-from s))
                                      (utils/valid-email? @(::inviting-email s)))
                                 (and (= "slack" @(::inviting-from s))
                                      (not (empty? @(::inviting-slack s)))))]
            [:tbody
              [:tr
                {:key (str "invite-users-table-new")}
                [:td.user-field
                  [:div.user-input
                    [:input.org-settings-field
                      {:type "email"
                       :style {:display (if (= "email" @(::inviting-from s)) "block" "none")}
                       :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
                       :placeholder "email@example.com"
                       :on-change #(reset! (::inviting-email s) (.. % -target -value))
                       :value @(::inviting-email s)}]
                    [:div
                      {:style {:display (if (= "slack" @(::inviting-from s)) "block" "none")}}
                      (rum/with-key
                        (slack-users-dropdown {:on-change #(reset! (::inviting-slack s) %)
                                               :disabled false
                                               :on-intermediate-change #(reset! (::inviting-slack s) nil)
                                               :initial-value ""})
                        (str "slack-users-dropdown-" (count invite-users)))]]]
                [:td.user-type-field
                  [:div.user-type-dropdown
                    (rum/with-key
                      (user-type-dropdown (utils/guid) @(::inviting-user-type s) #(reset! (::inviting-user-type s) %) (not (jwt/is-admin? (:team-id org-data))) nil (not plus-enabled))
                      (str "user-type-dropdown-" (count invite-users)))]]
                [:td.user-remove]]
              [:tr
                [:td
                  [:button.mlb-reset.mlb-default.add-button
                    {:disabled (not plus-enabled)
                     :on-click #(add-inviting-user s)}
                    "+"]]
                [:td]
                [:td]]])]]
      ;; Save and cancel buttons
      [:div.org-settings-footer.group
        [:button.mlb-reset.mlb-default.save-btn
          {:on-click #(dis/dispatch! [:invite-users])
           :disabled (zero? (count invite-users))}
          (if (zero? (count invite-users)) "Send" (str "Send " (count invite-users) " Invites"))]
        [:button.mlb-reset.mlb-link-black.cancel-btn
          {:on-click #(dis/dispatch! [:input [:invite-users] []])}
          "Cancel"]]]))