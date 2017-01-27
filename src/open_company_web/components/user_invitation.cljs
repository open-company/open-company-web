(ns open-company-web.components.user-invitation
  (:require [rum.core :as rum]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.small-loading :refer (small-loading)]))

(defn user-invitation-action [team-id invitation action & [payload]]
  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide")
  (dis/dispatch! [:user-invitation-action team-id invitation action payload]))

(rum/defc invite-row < rum/static
  [team-id invitation]
  (let [user-links (:links invitation)
        user-dropdown-id (str "invite-row-user-" (:user-id invitation))]
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
              (:admin invitation)
              [:i.fa.fa-gear]
              (not (:admin invitation))
              [:i.fa.fa-user]
              ;; used for author users but not yet implemented
              :else
              [:i.fa.fa-pencil])]
          [:ul.dropdown-menu.user-type-dropdown-menu
            {:aria-labelledby user-dropdown-id}
            [:li
              {:class (when (not (:admin invitation)) "active")}
              [:i.fa.fa-user] " Viewer"]
            [:li
              {:class (when (:author invitation) "active")}
              [:i.fa.fa-pencil] " Author"]
            [:li
              {:class (when (:admin invitation) "active")}
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
            (when (utils/link-for (:links invitation) "invite")
              [:button.btn-reset.invite-row-action
                {:data-placement "top"
                 :data-toggle "tooltip"
                 :data-container "body"
                 :title "RESEND INVITE"
                 :on-click #(let [company-data (dis/company-data)]
                              (user-invitation-action
                                team-id
                                invitation
                                "invite"
                                {:email (:email invitation)
                                 :company-name (:name company-data)
                                 :logo (or (:logo company-data) "")}))}
                [:i.fa.fa-share]])
            ; if it has a delete link
            (when (utils/link-for (:links invitation) "remove")
              (let [pending? (and (:status invitation) (= (clojure.string/lower-case (:status invitation)) "pending"))]
                [:button.btn-reset.invite-row-action
                  {:data-placement "top"
                   :data-toggle "tooltip"
                   :data-container "body"
                   :title (if pending? "CANCEL INVITE" "REMOVE USER")
                   :on-click #(user-invitation-action team-id invitation "remove")}
                  (if pending?
                    [:i.fa.fa-times]
                    [:i.fa.fa-trash-o])]))])]]))

(rum/defc user-invitation < rum/static
                            {:did-mount (fn [s]
                                          (when-not (utils/is-test-env?)
                                            (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                        s)}
  [team-id invitations]
  [:div.um-invitations-box.col-12.group
    [:table.table
      [:thead
        [:tr
          [:th "ACCESS"]
          [:th "NAME"]
          [:th "STATUS"]
          [:th {:style {:text-align "center"}} "ACTIONS"]]]
      [:tbody
        (for [invitation invitations]
          (rum/with-key (invite-row team-id invitation) (str "invitation-tr-" (:href (utils/link-for (:links invitation) "self")))))]]])