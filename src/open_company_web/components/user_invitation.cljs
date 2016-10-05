(ns open-company-web.components.user-invitation
  (:require [rum.core :as rum]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]))

(defn user-invitation-action [user-id action & payload]
  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide")
  (dis/dispatch! [:user-invitation-action user-id action payload]))

(rum/defc invite-row
  [invitation]
  [:tr
    [:td [:div.value (:real-name invitation)]]
    [:td [:div.value (:email invitation)]]
    [:td [:div (clojure.string/upper-case (:status invitation))]]
    [:td
      (cond
        (= (clojure.string/lower-case (:status invitation)) "pending")
        [:div
          [:button.btn-reset
            {:data-placement "top"
             :data-toggle "tooltip"
             :data-container "body"
             :title "RESEND INVITE"
             :on-click #(let [company-data (dis/company-data)]
                          (user-invitation-action
                            (:user-id invitation)
                            "invite"
                            {:email (:email invitation)
                             :company-name (:name company-data)
                             :logo (or (:logo company-data) "")}))}
            [:i.fa.fa-share]]
          [:button.btn-reset
            {:data-placement "top"
             :data-toggle "tooltip"
             :data-container "body"
             :title "CANCEL INVITE"
             :on-click #(user-invitation-action (:user-id invitation) "delete")}
            [:i.fa.fa-times]]]
        (= (clojure.string/lower-case (:status invitation)) "active")
        [:div
          [:button.btn-reset
            {:data-placement "top"
             :data-toggle "tooltip"
             :data-container "body"
             :title "REMOVE USER"
             :on-click #(user-invitation-action (:user-id invitation) "delete")}
            [:i.fa.fa-trash-o]]]
        :else
        [:div])]])

(rum/defc user-invitation < {:did-mount (fn [s]
                                        (when-not (utils/is-test-env?)
                                          (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                        s)}
  [invitations]
  [:div.my3.um-invitations-box.container.col-12.group
    [:table.table
      [:thead
        [:tr
          [:th "NAME"]
          [:th "EMAIL"]
          [:th "STATUS"]
          [:th "ACTIONS"]]]
      [:tbody
        (for [invitation invitations]
          (rum/with-key (invite-row invitation) (str "invitation-tr-" (:user-id invitation))))]]])