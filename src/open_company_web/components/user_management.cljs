(ns open-company-web.components.user-management
  (:require [rum.core :as rum]))

(rum/defcs user-management < rum/reactive
  [s]
  [:div.user-management.lg-col-5.md-col-7.col-11.mx-auto.mt4.mb4.group
    [:div.um-cta.pb2 "User Management"]
    [:div.um-description
      [:p.um-p
        "USERS CAN ADD AND EDIT TOPICS, AND THEY CAN SHARE UPDATES WITH OTHERS."]
      [:p.um-p
        "ORGANIZATIONS THAT SIGN UP WITH SLACK ENABLE MEMBERS OF THE SLACK ORGANIZATION TO BE A USER. "
        "ORGANIZATIONS THAT SIGN UP WITH EMAIL CAN INVITE USERS TO JOIN  BY EMAIL, "
        "OR THEY CAN MAKE IT AVAILABLE TO ANYONE WITH A COMPANY EMAIL DOMAIN (e.g., @acme.com)."]]
    [:div.my3.um-invite.group
      [:div.um-invite-label
        "INVITE USERS BY EMAIL ADDRESS"]
      [:div
        [:input.left.um-invite-field
          {:value ""
           :name "um-invite"
           :type "text"
           :placeholder "user emails separated by comma"}]
        [:button.right.btn-reset.btn-solid.um-invite-send
          {:on-click #()}
          "SEND INVITE(S)"]]]
    [:div.my2.um-byemail-container.group
      [:div.group
        [:input.left.um-byemail-anyone
          {:type "checkbox"
           :name "um-anyone"
           :checked false}]
        [:span.left.ml1.um-byemail-anyone-span
          "ANYONE WITH THIS EMAIL DOMAIN HAS USER ACCESS"]]
      [:div.mt2.um-byemail.group
        [:span.left.um-byemail-at "@"]
        [:input.left.um-byemail-email
          {:type "text"
           :name "um-byemail-email"
           :placeholder "your email domain without @"}]
        [:button.left.um-byemail-save.btn-reset.btn-outline "SAVE"]]]])