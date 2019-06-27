(ns oc.web.components.ui.follow-ups-picker
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn- set-users-list
  "Setup the list of assignable users and the already assigned one.
  On the first setup user all the users already in :follow-ups and filter out those not assignable.
  On subsequent setup (remount) use the local active users list and filter out those not assignable."
  [s first-setup]
  (let [activity-data @(drv/get-ref s :follow-ups-activity-data)
        team-roster @(drv/get-ref s :team-roster)
        users-list (activity-actions/follow-up-users-for-activity activity-data team-roster)
        all-user-ids (map :user-id users-list)
        active-base-list (if first-setup
                            (map :user-id (:follow-ups activity-data))
                            @(::active-user-ids s))
        active-users-list (filterv #((set all-user-ids) %)  active-base-list)]
    (reset! (::users-list s) users-list)
    (reset! (::active-user-ids s) active-users-list)))

(rum/defcs follow-ups-picker < rum/reactive
                               (rum/local [] ::users-list)
                               (rum/local [] ::active-user-ids)
                               (drv/drv :follow-ups-activity-data)
                               (drv/drv :team-roster)
                               {:will-mount (fn [s]
                                 (set-users-list s true)
                                 s)
                                :did-remount (fn [_ s]
                                 (set-users-list s false)
                                 s)}
  [s]
  (let [activity-data (drv/react s :follow-ups-activity-data)
        team-roster (drv/react s :team-roster)
        users-list @(::users-list s)
        active-user-ids @(::active-user-ids s)]
    [:div.follow-ups-picker
      [:button.mlb-reset.modal-close-bt
        {:on-click #(nav-actions/close-all-panels)}]
      [:div.follow-ups-picker-container
        [:div.follow-ups-picker-header
          [:div.follow-ups-picker-header-title
            "Create follow ups"]
          [:button.mlb-reset.save-bt
            {:on-click #(nav-actions/close-all-panels)}
            "Save"]]
        [:div.follow-ups-picker-body
          [:div.follow-ups-users-count
            (str (count active-user-ids) " "
            (if (> (count active-user-ids) 1)
              "people"
              "person"))]
          [:div.follow-ups-users-list
            (for [u users-list
                  :let [active? ((set active-user-ids) (:user-id u))]]
              [:div.follow-ups-user-item.group
                {:key (str "follow-ups-user-" (:user-id u))}
                [:div.follow-ups-user-left.group
                  (user-avatar-image u)
                  [:div.user-name
                    (utils/name-or-email u)]]
                [:div.follow-ups-user-right.group
                  (if active?
                    [:button.mlb-reset.remove-bt
                      {:on-click #(let [filtered-users (filterv (fn [id] (not= id (:user-id u))) active-user-ids)]
                                    (reset! (::active-user-ids s) filtered-users))}
                      "Remove"]
                    [:button.mlb-reset.add-bt
                      {:on-click #(reset! (::active-user-ids s) (conj active-user-ids (:user-id u)))}
                      "Add"])]])]]]]))