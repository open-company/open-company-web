(ns open-company-web.components.user-management
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.jwt :as jwt]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.components.user-invitation :refer (user-invitation)]))

(rum/defcs user-management < rum/static
                             rum/reactive
                             {:before-render (fn [s]
                                               (when (and (:auth-settings @dis/app-state)
                                                          (not (:enumerate-users-requested @dis/app-state)))
                                                 (dis/dispatch! [:enumerate-users]))
                                               s)
                             :did-mount (fn [s]
                                          (when-not (utils/is-test-env?)
                                            (dis/dispatch! [:input [:um-invite :email] ""]))
                                          s)}
  [s]
  [:div.user-management.lg-col-5.md-col-7.col-11.mx-auto.mt4.mb4.group
    [:div.um-cta.pb2 "User Management"]
    [:div.um-description
      [:p.um-p
        "Users can add and edit topics, and they can share updates with others."]
      (when (= (jwt/get-key :auth-source) "slack")
        [:p.um-p
          "All members of your Slack organization (not guests) can authenticate as users. You can also invite users to join by email."])]
    [:div.my4.um-invite.group
      [:div.um-invite-label
        "INVITE USER BY EMAIL ADDRESS"]
        [:div
          [:div.group
            [:input.left.um-invite-field.email
              {:name "um-invite"
               :type "email"
               :autoCapitalize "none"
               :value (:email (:um-invite (rum/react dis/app-state)))
               :pattern "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
               :on-change #(dis/dispatch! [:invite-by-email-change (.. % -target -value)])
               :placeholder "Email address"}]
            [:button.right.btn-reset.btn-solid.um-invite-send
              {:disabled (not (utils/valid-email? (:email (:um-invite (rum/react dis/app-state)))))
               :on-click #(let [email (:email (:um-invite @dis/app-state))]
                            (if (utils/valid-email? email)
                              (dis/dispatch! [:invite-by-email email])
                              (dis/dispatch! [:input [:invite-by-email-error] true])))}
             "SEND INVITE"]]
        (when (:invite-by-email-error (rum/react dis/app-state))
          [:div
            (cond
              (and (= (:invite-by-email-error (rum/react dis/app-state)) :user-exists)
                   (:email (:um-invite @dis/app-state)))
              [:span.small-caps.red.mt1.left (str (:email (:um-invite @dis/app-state)) " is already a user.")]
              :else
              [:span.small-caps.red.mt1.left "An error occurred, please try again."])])]]
    (when (pos? (count (:enumerate-users (rum/react dis/app-state))))
      (user-invitation (:enumerate-users (rum/react dis/app-state))))
    (comment
      [:div.my2.um-byemail-container.group
        [:div.group
          [:span.left.ml1.um-byemail-anyone-span
            "ANYONE WITH THIS EMAIL DOMAIN HAS USER ACCESS"]]
        [:div.mt2.um-byemail.group
          [:span.left.um-byemail-at "@"]
          [:input.left.um-byemail-email
            {:type "text"
             :name "um-byemail-email"
             :placeholder "your email domain without @"}]
          [:button.left.um-byemail-save.btn-reset.btn-outline "SAVE"]]])])