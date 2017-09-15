(ns oc.web.components.users-list
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.api :as api]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-type-picker :refer (user-type-dropdown show-role-explainer-popover)]))

(defn user-action [team-id user action method other-link-params & [payload]]
  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide")
  (dis/dispatch! [:user-action team-id user action method other-link-params payload]))

(rum/defc user-row < rum/static
                     {:after-render (fn [s]
                                      (when-not (utils/is-test-env?)
                                        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                      s)}
  [team-id user author]
  (let [user-links (:links user)
        add-link (utils/link-for user-links "add" "PUT" {:ref "application/vnd.open-company.user.v1+json"})
        user-type (utils/get-user-type user (dis/org-data))
        admin-type {:ref "application/vnd.open-company.team.admin.v1"}
        remove-user (utils/link-for user-links "remove" "DELETE" {:ref "application/vnd.open-company.user.v1+json"})
        pending? (= (:status user) "pending")
        is-self? (= (j/get-key :user-id) (:user-id user))
        visualized-name (utils/name-or-email user)]
    [:tr
      [:td
        (user-type-dropdown {:user-id (:user-id user) :user-type user-type :on-change #(api/switch-user-type user user-type % user author)})]
      [:td [:div.value
             {:title (if (pos? (count (:email user))) (:email user) "")
              :data-toggle "tooltip"
              :data-placement "top"
              :data-container "body"}
             visualized-name]]
      [:td
        [:div
          (when (:status user) (clojure.string/upper-case (:status user)))]]
      [:td {:style {:text-align "center"}}
        (if (:loading user)
          ; if it's loading show the spinner
          [:div (small-loading)]
          [:div
            ; if it has an invite link show a resend invite button
            (when (and pending?
                       (not is-self?))
              [:button.btn-reset.user-row-action
                {:data-placement "top"
                 :data-toggle "tooltip"
                 :data-container "body"
                 :title "RESEND INVITE"
                 :on-click (fn []
                              (dis/dispatch! [:input [:um-invite] {:email (:email user)
                                                                   :invite-from "email"
                                                                   :user-type user-type
                                                                   :error nil}])
                              (utils/after 100 #(dis/dispatch! [:invite-user])))}
                [:i.fa.fa-share]])
            ; if it has a delete link
            (when remove-user
              [:button.btn-reset.user-row-action
                {:data-placement "top"
                 :data-toggle "tooltip"
                 :data-container "body"
                 :title (if (and pending? (not is-self?)) "CANCEL INVITE" "REMOVE USER")
                 :on-click #(do
                              (when author
                                (api/remove-author author))
                             (user-action team-id user "remove" "DELETE"  {:ref "application/vnd.open-company.user.v1+json"}))}
                (if (and pending? (not is-self?))
                  [:i.fa.fa-times]
                  [:i.fa.fa-trash-o])])])]]))

(rum/defc users-list < rum/static
  [team-id users org-authors]
  [:div.um-users-box.col-12.group
    [:table.table
      [:thead
        [:tr
          [:th.pointer
            {:on-click #(show-role-explainer-popover %)}
            "ACCESS "
            [:i.fa.fa-question-circle]]
          [:th "NAME"]
          [:th "STATUS"]
          [:th {:style {:text-align "center"}} "ACTIONS"]]]
      [:tbody
        (for [user (sort-by utils/name-or-email users)
              :let [author (some #(when (= (:user-id %) (:user-id user)) %) org-authors)]]
          (rum/with-key (user-row team-id user author) (str "user-tr-" team-id "-" (:user-id user))))]]])