(ns open-company-web.components.users-list
  (:require [rum.core :as rum]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.small-loading :refer (small-loading)]))

(defn user-action [team-id invitation action method other-link-params & [payload]]
  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide")
  (dis/dispatch! [:user-action team-id invitation action method other-link-params payload]))

(rum/defc user-row < rum/static
  [team-id invitation]
  (let [user-links (:links invitation)
        user-dropdown-id (str "user-row-" (:user-id invitation))
        add-link (utils/link-for user-links "add" "PUT")
        is-admin? (:admin invitation)
        admin-type {:ref utils/user-admin-type}
        kick-off-team (utils/link-for user-links "remove" "DELETE")]
    [:tr
      [:td
        [:div.dropdown
          [:button.btn-reset.user-type-btn.dropdown-toggle
            {:on-click #()
             :id user-dropdown-id
             :data-toggle "dropdown"
             :aria-haspopup true
             :aria-expanded false}
            (cond
              is-admin?
              [:i.fa.fa-gear]
              :else
              [:i.fa.fa-user])]
          [:ul.dropdown-menu.user-type-dropdown-menu
            {:aria-labelledby user-dropdown-id}
            [:li
              {:class (when (not is-admin?) "active")
               :on-click #(user-action team-id invitation "remove" "DELETE" admin-type nil)}
              [:i.fa.fa-user] " Viewer"]
            [:li
              {:class (when is-admin? "active")
               :on-click #(user-action team-id invitation "add" "PUT" admin-type nil)}
              [:i.fa.fa-gear] " Admin"]]]]
      [:td [:div.value
             {:title (if (pos? (count (:email invitation))) (:email invitation) "")
              :data-toggle "tooltip"
              :data-placement "top"
              :data-container "body"}
             (or (str (:first-name invitation) " " (:last-name invitation)) (:email invitation))]]
      [:td [:div (when (:status invitation)
                   (let [upper-status (clojure.string/upper-case (:status invitation))]
                     (if (= upper-status "UNVERIFIED")
                       "ACTIVE"
                       upper-status)))]]
      [:td {:style {:text-align "center"}}
        (if (:loading invitation)
          ; if it's loading show the spinner
          [:div (small-loading)]
          [:div
            ; if it has an invite link show a resend invite button
            (when add-link
              [:button.btn-reset.user-row-action
                {:data-placement "top"
                 :data-toggle "tooltip"
                 :data-container "body"
                 :title "RESEND INVITE"
                 :on-click #(dis/dispatch! [:resend-invitation invitation])}
                [:i.fa.fa-share]])
            ; if it has a delete link
            (when kick-off-team
              (let [pending? (not (nil? add-link))]
                [:button.btn-reset.user-row-action
                  {:data-placement "top"
                   :data-toggle "tooltip"
                   :data-container "body"
                   :title (if pending? "CANCEL INVITE" "REMOVE USER")
                   :on-click #(user-action team-id invitation "remove" "DELETE" nil nil)}
                  (if pending?
                    [:i.fa.fa-times]
                    [:i.fa.fa-trash-o])]))])]]))

(rum/defc users-list < rum/static
                            {:did-mount (fn [s]
                                          (when-not (utils/is-test-env?)
                                            (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                        s)}
  [team-id invitations]
  [:div.um-users-box.col-12.group
    [:table.table
      [:thead
        [:tr
          [:th "ACCESS"]
          [:th "NAME"]
          [:th "STATUS"]
          [:th {:style {:text-align "center"}} "ACTIONS"]]]
      [:tbody
        (for [invitation invitations]
          (rum/with-key (user-row team-id invitation) (str "invitation-tr-" team-id "-" (:user-id invitation))))]]])