(ns open-company-web.components.user-management
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.jwt :as jwt]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.components.user-invitation :refer (user-invitation)]
            [open-company-web.components.su-preview-dialog :refer (item-input)]))

(rum/defc email-item [v delete! submitted?]
  [:div.item-input-item
   {:class (when-not submitted? "border b--red")
    :style (when submitted? {:backgroundColor "rgba(78, 90, 107, 0.1)"})}
   [:span.inline-block.p1 v
    [:button.btn-reset.p0.ml1
     {:on-click #(delete!)}
     "x"]]])

(rum/defcs user-management < rum/static
                             rum/reactive
                             (drv/drv :um-invite)
                             {:before-render (fn [s]
                                               (when (and (:auth-settings @dis/app-state)
                                                          (not (:enumerate-users-requested @dis/app-state)))
                                                 (dis/dispatch! [:enumerate-users]))
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
    (when (pos? (count (:enumerate-users (rum/react dis/app-state))))
      (user-invitation (:enumerate-users (rum/react dis/app-state))))
    [:div.my3.um-invite.group
      [:div.um-invite-label
        "INVITE USERS BY EMAIL ADDRESS"
        (when-let [to-field (:email (drv/react s :um-invite))]
          (cond
            (not (every? utils/valid-email? to-field))
            [:span.red.py1 " — Not a valid email address"]))]
      [:div.item-input-container
        (item-input {:item-render email-item
                     :match-ptn #"(\S+)[,|\s]+"
                     :split-ptn #"[,|\s]+"
                     :container-node :div.item-input
                     :input-node :input.border-none.outline-none.mr.mb1
                     :valid-item? utils/valid-email?
                     :on-change (fn [v] (dis/dispatch! [:input [:um-invite :email] v]))})
        [:button.right.btn-reset.btn-solid.um-invite-send
          {:disabled (let [to (:email (drv/react s :um-invite))]
                       (not (and (seq to) (every? utils/valid-email? to))))
           :on-click #(dis/dispatch! [:invite-by-email (:email (drv/react s :um-invite))])}
          "SEND INVITES"]
        (when (:invite-by-email-error (rum/react dis/app-state))
          [:span.small-caps.red.mt1.left "An error occurred, please try again."])]]
    [:div.my2.um-byemail-container.group.hidden
      [:div.group
        [:span.left.ml1.um-byemail-anyone-span
          "ANYONE WITH THIS EMAIL DOMAIN HAS USER ACCESS"]]
      [:div.mt2.um-byemail.group
        [:span.left.um-byemail-at "@"]
        [:input.left.um-byemail-email
          {:type "text"
           :name "um-byemail-email"
           :placeholder "your email domain without @"}]
        [:button.left.um-byemail-save.btn-reset.btn-outline "SAVE"]]]])