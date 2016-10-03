(ns open-company-web.components.user-management
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.user-invitation :refer (user-invitation)]))

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
    (when (pos? (count (:enumerate-users (rum/react dis/app-state))))
      (user-invitation (:enumerate-users (rum/react dis/app-state))))
    [:div.my3.um-invite.group
      [:div.um-invite-label
        "INVITE USERS BY EMAIL ADDRESS"]
      [:div
        (when (:invite-by-email-error (rum/react dis/app-state))
          [:span.error-message.red "An error occurred, please try again."])
        [:input.left.um-invite-field.email
          {:name "um-invite"
           :type "text"
           :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
           :placeholder "user emails separated by comma"}]
        [:button.right.btn-reset.btn-solid.um-invite-send
          {:on-click #(let [email (.-value (sel1 [:input.um-invite-field.email]))]
                        (if (utils/valid-email? email)
                          (dis/dispatch! [:invite-by-email email])
                          (js/alert "The email address you entered is not valid.")))}
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