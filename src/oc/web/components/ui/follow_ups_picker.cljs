(ns oc.web.components.ui.follow-ups-picker
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn- set-users-list
  "Setup the list of assignable users and the already assigned one.
  On the first setup user all the users already in :follow-ups and filter out those not assignable.
  On subsequent setup (remount) use the local active users list and filter out those not assignable."
  [s first-setup]
  (let [activity-data @(drv/get-ref s :follow-ups-activity-data)
        team-roster @(drv/get-ref s :team-roster)
        users-list (activity-actions/follow-up-users activity-data team-roster)
        all-user-ids (map :user-id users-list)
        filtered-follow-ups (if first-setup
                              (filterv #((set all-user-ids) (-> % :assignee :user-id)) (:follow-ups activity-data))
                              (filterv #((set all-user-ids) (-> % :assignee :user-id)) @(::follow-ups s)))]
    (reset! (::users-list s) users-list)
    (reset! (::follow-ups s) filtered-follow-ups)))

(rum/defcs follow-ups-picker < rum/reactive
                               (rum/local [] ::users-list)
                               (rum/local [] ::follow-ups)
                               (drv/drv :follow-ups-picker-callback)
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
        follow-ups @(::follow-ups s)
        follow-ups-picker-callback (drv/react s :follow-ups-picker-callback)]
    [:div.follow-ups-picker
      [:button.mlb-reset.modal-close-bt
        {:on-click #(nav-actions/close-all-panels)}]
      [:div.follow-ups-picker-container
        [:div.follow-ups-picker-header
          [:div.follow-ups-picker-header-title
            "Create follow ups"]
          [:button.mlb-reset.save-bt
            {:on-click #(do
                         (when (fn? follow-ups-picker-callback)
                           (follow-ups-picker-callback @(::follow-ups s)))
                         (nav-actions/close-all-panels))}
            "Save"]]
        [:div.follow-ups-picker-body
          [:div.follow-ups-picker-body-head.group
            [:div.follow-ups-users-count
              (cond
                (zero? (count follow-ups))
                "No one selected"
                (= (count follow-ups) 1)
                "1 person"
                :else
                (str (count follow-ups) " people"))]
            [:div.follow-ups-users-bt
              (cond
                (< (count follow-ups) (count users-list))
                [:button.mlb-reset.select-all
                  {:on-click (fn [_]
                              (reset! (::follow-ups s) (map (fn [user]
                                                              (let [f (first (filterv #(= (-> % :assignee :user-id) (:user-id user)) follow-ups))]
                                                                (or f
                                                                    (hash-map :assignee (activity-actions/author-for-user user)
                                                                              :completed? false))))
                                                        users-list)))}
                  "Select all"]
                :else
                [:button.mlb-reset.select-all
                  {:on-click (fn [_]
                              (reset! (::follow-ups s)
                               (remove nil?
                                (map (fn [user]
                                      (let [f (first (filterv #(= (-> % :assignee :user-id) (:user-id user)) follow-ups))]
                                        (when (or (:completed? f)
                                                  (and (map? (:author f))
                                                       (not= (-> f :author :user-id) (jwt/user-id)))))))
                                users-list))))}
                  "Deselect all"])]]
          [:div.follow-ups-users-list
            (for [u users-list
                  :let [f (first (filterv #(= (-> % :assignee :user-id) (:user-id u)) follow-ups))
                        disabled? (or (:completed? f)
                                      (and (map? (:author f))
                                           (not= (-> f :author :user-id) (jwt/user-id))))]]
              [:div.follow-ups-user-item.group
                {:key (str "follow-ups-user-" (:user-id u))}
                [:div.follow-ups-user-left.group
                  (user-avatar-image u)
                  [:div.user-name
                    (utils/name-or-email u)]]
                [:div.follow-ups-user-right.group
                  (carrot-checkbox {:selected f
                                    :disabled disabled?
                                    :tooltip (when disabled?
                                              (if (:completed? f)
                                                "Follow-up completed"
                                                "Follow-up created"))
                                    :tooltip-placement "left"
                                    :did-change-cb (fn [v]
                                                     (reset! (::follow-ups s)
                                                      (if v
                                                        (conj follow-ups {:assignee (activity-actions/author-for-user u)
                                                                          :completed? false})
                                                        (filterv (fn [f] (not= (-> f :assignee :user-id) (:user-id u)))
                                                         follow-ups))))})]])]]]]))