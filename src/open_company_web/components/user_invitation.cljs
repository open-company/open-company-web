(ns open-company-web.components.user-invitation
  (:require [rum.core :as rum]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.small-loading :refer (small-loading)]))

(defn user-invitation-action [invitation action & [payload]]
  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide")
  (dis/dispatch! [:user-invitation-action invitation action payload]))

(rum/defc invite-row
  [invitation]
  (let [user-links (:links invitation)]
    [:tr
      [:td
        [:div.value
          [:button.btn-reset.user-type-btn
            {:on-click #()}
            (cond
              (:admin invitation)
              [:i.fa.fa-gear]
              (not (:admin invitation))
              [:i.fa.fa-user]
              ;; used for author users but not yet implemented
              :else
              [:i.fa.fa-pencil])]]]
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
                                invitation
                                "invite"
                                {:email (:email invitation)
                                 :company-name (:name company-data)
                                 :logo (or (:logo company-data) "")}))}
                [:i.fa.fa-share]])
            ; if it has a delete link
            (when (utils/link-for (:links invitation) "delete")
              (if (and (:status invitation) (= (clojure.string/lower-case (:status invitation))) "pending")
                ; and it's pending show a cancel invite button
                [:button.btn-reset.invite-row-action
                  {:data-placement "top"
                   :data-toggle "tooltip"
                   :data-container "body"
                   :title "CANCEL INVITE"
                   :on-click #(user-invitation-action invitation "delete")}
                  [:i.fa.fa-times]]
                ; if it's not pending show a remove user button
                [:button.btn-reset.invite-row-action
                  {:data-placement "top"
                   :data-toggle "tooltip"
                   :data-container "body"
                   :title "REMOVE USER"
                   :on-click #(user-invitation-action invitation "delete")}
                  [:i.fa.fa-trash-o]]))])]]))

(rum/defc user-invitation < {:did-mount (fn [s]
                                        (when-not (utils/is-test-env?)
                                          (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                        s)}
  [invitations]
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
          (rum/with-key (invite-row invitation) (str "invitation-tr-" (:href (utils/link-for (:links invitation) "self")))))]]])