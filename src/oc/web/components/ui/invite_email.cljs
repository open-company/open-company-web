(ns oc.web.components.ui.invite-email
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.user-type-dropdown :refer (user-type-dropdown)]))

(def default-row-num 1)
(def default-user "")
(def default-user-role :author)
(def default-user-row
 {:temp-user default-user
  :user default-user
  :role default-user-role})

(defn new-user-row [s]
  (assoc default-user-row :type "email"))

(defn valid-user? [user-map]
  (utils/valid-email? (:user user-map)))

(defn has-valid-user? [users-list]
  (some valid-user? users-list))

(defn setup-initial-rows [s]
  (let [invite-users @(drv/get-ref s :invite-users)]
    ;; Check if there are already invite rows
    (when (zero? (count invite-users))
      ;; if there are no rows setup the default initial rows
      (let [new-row (new-user-row s)]
        (dis/dispatch! [:input [:invite-users] (vec (repeat (or (-> s :rum/args first :rows-num) default-row-num) new-row))])))))

(defn- save-button-cta [s]
  (or (-> s :rum/args first :save-title) "Send email invitations"))

(defn- saving-button-cta [s]
  (or (-> s :rum/args first :saving-title) "Sending email invitations"))

(defn- saved-button-cta [s]
  (or (-> s :rum/args first :saved-title) "Email invitations sent!"))

(rum/defcs invite-email <
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :invite-users)
  ;; Locals
  (rum/local nil ::send-bt-cta)
  (rum/local 0 ::sending)
  (rum/local 0 ::initial-sending)
  {:will-mount (fn [s]
    (setup-initial-rows s)
    (reset! (::send-bt-cta s) (save-button-cta s))
    s)
   :will-update (fn [s]
    (let [sending (::sending s)
          initial-sending (::initial-sending s)]
      (when (pos? @sending)
        (let [invite-users @(drv/get-ref s :invite-users)
              no-error-invites (filter #(not (:error %)) invite-users)
              error-invites (filter :error invite-users)
              hold-initial-sending @initial-sending]
          (reset! sending (count no-error-invites))
          (when (zero? (count no-error-invites))
            (utils/after 1000
              (fn []
                (reset! sending 0)
                (reset! initial-sending 0)
                (if (zero? (count error-invites))
                  (do
                    (reset! (::send-bt-cta s) (saved-button-cta s))
                    (utils/after 2500 #(reset! (::send-bt-cta s) (save-button-cta s)))
                    (notification-actions/show-notification {:title (str "Invite"
                                                                     (when (> hold-initial-sending 1)
                                                                       "s")
                                                                     " sent.")
                                                             :primary-bt-title "OK"
                                                             :primary-bt-dismiss true
                                                             :expire 3
                                                             :primary-bt-inline true
                                                             :id :invites-sent})
                    (setup-initial-rows s))
                  (reset! (::send-bt-cta s) (save-button-cta s)))))))))
    s)
    :will-unmount (fn [s]
     (dis/dispatch! [:input [:invite-users] nil])
     s)}

  [s {:keys [rows-num hide-user-role]}]
  (let [invite-users (drv/react s :invite-users)
        org-data (drv/react s :org-data)
        is-admin? (jwt/is-admin? (:team-id org-data))]
    [:div.invite-email-container
      [:div.invite-email
        {:key "org-settings-invite-table"}
        (for [i (range (count invite-users))
              :let [user-data (get invite-users i)
                    key-string (str "invite-users-tabe-" i)]]
          [:div.invite-email-item-outer
            {:key key-string}
            [:div.invite-email-item.group
              [:input.org-settings-field.email-field.oc-input
                {:type "text"
                 :class (when (:error user-data) "error")
                 :pattern "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
                 :placeholder "name@domain.com"
                 :on-change #(dis/dispatch! [:input [:invite-users]
                               (assoc invite-users i (merge user-data {:error nil :user (.. % -target -value)}))])
                 :value (or (:user user-data) "")}]]
            (when-not hide-user-role
              [:div.user-type-dropdown
                (user-type-dropdown {:user-id (utils/guid)
                                     :user-type (:role user-data)
                                     :hide-admin (not is-admin?)
                                     :on-change
                                      #(dis/dispatch! [:input [:invite-users]
                                         (assoc invite-users i (merge user-data {:role % :error nil}))])})])
            (when-not hide-user-role
              [:button.mlb-reset.remove-user
                {:on-click #(let [before (subvec invite-users 0 i)
                                  after (subvec invite-users (inc i) (count invite-users))
                                  next-invite-users (vec (concat before after))
                                  fixed-next-invite-users (if (zero? (count next-invite-users))
                                                            [(assoc default-user-row :type (:type user-data))]
                                                            next-invite-users)]
                              (dis/dispatch! [:input [:invite-users] fixed-next-invite-users]))}])])]
      [:button.mlb-reset.add-button
        {:on-click #(dis/dispatch! [:input [:invite-users]
                     (conj invite-users (assoc default-user-row :type "email"))])}
        [:div.add-button-plus]
        "Add another"]
      [:button.mlb-reset.save-bt
        {:on-click #(let [valid-count (count (filterv valid-user? invite-users))]
                      (reset! (::sending s) valid-count)
                      (reset! (::initial-sending s) valid-count)
                      (reset! (::send-bt-cta s) (saving-button-cta s))
                      (team-actions/invite-users @(drv/get-ref s :invite-users)))
         :class (when (= (saved-button-cta s) @(::send-bt-cta s)) "no-disable")
         :disabled (or (not (has-valid-user? invite-users))
                       (pos? @(::sending s)))}
        @(::send-bt-cta s)]]))