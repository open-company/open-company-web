(ns oc.web.components.ui.follow-ups-picker
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.user :as user-lib]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.cmail :as cmail-actions]
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
        users-list (cmail-actions/follow-up-users activity-data team-roster)
        all-user-ids (map :user-id users-list)
        filtered-follow-ups (if first-setup
                              (filterv #((set all-user-ids) (-> % :assignee :user-id)) (:follow-ups activity-data))
                              (filterv #((set all-user-ids) (-> % :assignee :user-id)) @(::follow-ups s)))]
    (reset! (::users-list s) users-list)
    (reset! (::follow-ups s) filtered-follow-ups)))

(defn user-match [query user]
  (let [r (js/RegExp (str "^.*(" query ").*$") "i")]
    (or (and (:name user) (.match (:name user) r))
        (and (:first-name user) (.match (:first-name user) r))
        (and (:last-name user) (.match (:last-name user) r))
        (and (:email user) (.match (:email user) r)))))

(defn- sort-users [users-list]
  (let [current-user-id (jwt/user-id)
        self-user (filterv #(= (:user-id %) current-user-id) users-list)
        other-users (filterv #(not= (:user-id %) current-user-id) users-list)]
    (vec (concat self-user (sort-by user-lib/name-for other-users)))))

(defn- filter-users [s]
  (let [users-list @(::users-list s)
        query @(::query s)
        current-user-id (jwt/user-id)
        users (if (seq query)
                (filterv #(user-match query %) users-list)
                users-list)]
    (sort-users users)))

(rum/defcs follow-ups-picker < rum/reactive
                               (rum/local [] ::users-list)
                               (rum/local [] ::follow-ups)
                               (rum/local "" ::query)
                               (drv/drv :follow-ups-picker-callback)
                               (drv/drv :follow-ups-activity-data)
                               (drv/drv :team-roster)
                               (drv/drv :cmail-data)

                               ui-mixins/refresh-tooltips-mixin

                               {:will-mount (fn [s]
                                 (set-users-list s true)
                                 (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                                 s)
                                :did-remount (fn [_ s]
                                 (set-users-list s false)
                                 s)}
  [s]
  (let [activity-data (drv/react s :follow-ups-activity-data)
        team-roster (drv/react s :team-roster)
        cmail-data (drv/react s :cmail-data)
        users-list @(::users-list s)
        follow-ups @(::follow-ups s)
        follow-ups-picker-callback (drv/react s :follow-ups-picker-callback)
        filtered-users-list (filter-users s)
        current-user-id (jwt/user-id)
        is-mobile? (responsive/is-mobile-size?)
        all-users-set (set (map :user-id users-list))
        current-assignees (set (map (comp :user-id :assignee) follow-ups))
        users-diff (clojure.set/difference all-users-set current-assignees)
        show-select-all? (if (= all-users-set  #{current-user-id})
                             (empty? current-assignees)
                             (and (not-empty users-diff)
                                  (not= users-diff #{current-user-id})))]
    [:div.follow-ups-picker
      [:button.mlb-reset.modal-close-bt
        {:on-click #(nav-actions/close-all-panels)}]
      [:div.follow-ups-picker-container
        [:div.follow-ups-picker-header
          [:div.follow-ups-picker-header-title
            "Request a follow-up"]
          [:button.mlb-reset.save-bt
            {:on-click #(do
                         (when (fn? follow-ups-picker-callback)
                           (follow-ups-picker-callback @(::follow-ups s)))
                         (nav-actions/close-all-panels))}
            "Save"]
          [:button.mlb-reset.cancel-bt
            {:on-click #(nav-actions/close-all-panels)}
            "Cancel"]]
        [:div.follow-ups-picker-body
          [:div.follow-ups-picker-body-head.group
            [:div.follow-ups-users-count
              (str (count follow-ups) " of " (count all-users-set) " ")
              (if (or is-mobile?
                      (not (jwt/is-admin? (:team-id team-roster)))
                      (= (:board-slug cmail-data) utils/default-section-slug))
                "section members"
                [:button.mlb-reset.section-memeber-bt
                  {:on-click (fn [e]
                               (utils/event-stop e)
                               (nav-actions/show-section-editor (:board-slug cmail-data)))
                   :data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title (str (:board-name cmail-data) " settings")}
                  "section members"])]
            [:div.follow-ups-users-bt
              (when-not (seq @(::query s))
                (if show-select-all?
                  [:button.mlb-reset.select-all
                    {:on-click (fn [_]
                                (reset! (::follow-ups s) (map (fn [user]
                                                                (let [f (first (filterv #(= (-> % :assignee :user-id) (:user-id user)) follow-ups))]
                                                                  (or f
                                                                      (hash-map :assignee (cmail-actions/author-for-user user)
                                                                                :completed? false))))
                                                          users-list)))}
                    "Select all"]
                  [:button.mlb-reset.deselect-all
                    {:on-click (fn [_]
                                (reset! (::follow-ups s)
                                 (remove nil?
                                  (map (fn [user]
                                        (let [f (first (filterv #(= (-> % :assignee :user-id) (:user-id user)) follow-ups))]
                                          ;; Can't override follow-ups that are not assigned to self
                                          ;; and that are completed or were created by the user himself
                                          (when (and (not= (-> f :author :user-id) current-user-id)
                                                     (or (:completed? f)
                                                         (= (-> f :author :user-id) (-> f :assignee :user-id))))
                                            f)))
                                  users-list))))}
                    "Deselect all"]))]]
          [:div.follow-ups-users-search
            [:input.follow-ups-query.oc-input
              {:value @(::query s)
               :type "search"
               :on-change #(reset! (::query s) (.. % -target -value))
               :placeholder "Add member by name..."}]]
          [:div.follow-ups-users-list
            (for [u filtered-users-list
                  :let [f (first (filterv #(= (-> % :assignee :user-id) (:user-id u)) follow-ups))
                        disabled? (or (:completed? f)
                                      (and (map? (:author f))
                                           (= (-> f :author :user-id) (-> f :assignee :user-id))))
                        roster-user (first (filterv #(= (:user-id %) (:user-id u)) (:users team-roster)))
                        ;; Retrieve the Slack display name for pending and active users
                        slack-display-name (if (or (= (:status u) "uninvited")
                                                   (= (:status u) "pending"))
                                            (:slack-display-name roster-user)
                                            (some #(when (seq (:display-name %)) (:display-name %)) (vals (:slack-users roster-user))))
                        ;; Add @ in front of the slack display name if it's not there already
                        fixed-display-name (if (and (seq slack-display-name)
                                                    (not= slack-display-name "-")
                                                    (not (clojure.string/starts-with? slack-display-name "@")))
                                             (str "@" slack-display-name)
                                             slack-display-name)]]
              [:div.follow-ups-user-item.group
                {:key (str "follow-ups-user-" (:user-id u))
                 :title (when disabled?
                          (if (:completed? f)
                            "Follow-up completed"
                            "Follow-up created"))
                 :data-toggle (when disabled? "tooltip")
                 :data-placement "top"
                 :data-container "body"
                 :on-click (fn [_]
                             (when-not disabled?
                               (reset! (::follow-ups s)
                                (if f
                                  (filterv (fn [f] (not= (-> f :assignee :user-id) (:user-id u)))
                                   follow-ups)
                                  (conj follow-ups {:assignee (cmail-actions/author-for-user u)
                                                    :completed? false})))))}
                [:div.follow-ups-user-left.group
                  {:class (when disabled? "disabled")}
                  (user-avatar-image u)
                  [:div.user-name
                    {:title (str "<span>" (:email u)
                              (when (seq fixed-display-name)
                                (str " | <i class=\"mdi mdi-slack\"></i>" (when-not (= fixed-display-name "-") (str " " fixed-display-name))))
                              "</span>")
                     :data-toggle "tooltip"
                     :data-html "true"
                     :data-placement "top"}
                    (str
                     (user-lib/name-for u)
                     (when (= (:user-id u) current-user-id)
                       (str " (you)")))]]
                [:div.follow-ups-user-right.group
                  (carrot-checkbox {:selected (or disabled? f)
                                    :disabled disabled?})]])]]]]))