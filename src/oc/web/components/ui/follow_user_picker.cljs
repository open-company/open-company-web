(ns oc.web.components.ui.follow-user-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :refer (strict-refresh-tooltips-mixin)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn- sort-users [user-id users]
  (let [{:keys [self-user other-users]}
         (group-by #(if (= (:user-id %) user-id) :self-user :other-users) users)
        sorted-other-users (sort-by :short-name other-users)]
    (remove nil? (concat self-user sorted-other-users))))

(defn- search-string [v q]
  (-> v string/lower (string/includes? q)))

(defn- search-user [user q]
  (or (-> user :first-name (search-string q))
      (-> user :last-name (search-string q))
      (-> user :name (search-string q))
      (-> user :email (search-string q))
      (-> user :title (search-string q))))

(defn- filter-user [s user q]
  (or (not (seq q))
      (search-user user q)
      (some (partial search-user user) (string/split q #"\s"))))

(defn- filter-sort-users [s current-user-id users q]
  (sort-users current-user-id (filterv #(filter-user s % (string/lower q)) users)))

(rum/defcs follow-user-picker < rum/reactive

 (drv/drv :org-data)
 (drv/drv :active-users)
 (drv/drv :follow-publishers-list)
 (drv/drv :current-user-data)
 (rum/local "" ::query)
 (rum/local false ::saving)
 strict-refresh-tooltips-mixin
 {:init (fn [s]
   ;; Refresh the following list
   (user-actions/load-follow-list)
   s)
  :will-unmount (fn [s]
   (user-actions/refresh-follow-containers)
   s)}

  [s]
  (let [org-data (drv/react s :org-data)
        follow-publishers-list (map :user-id (drv/react s :follow-publishers-list))
        current-user-data (drv/react s :current-user-data)
        all-active-users (drv/react s :active-users)
        authors-uuids (->> org-data :authors (map :user-id) set)
        all-authors (filter #(and (authors-uuids (:user-id %))
                                  (not= (:user-id current-user-data) (:user-id %)))
                     (vals all-active-users))
        with-follow (map #(assoc % :follow (utils/in? follow-publishers-list (:user-id %))) all-authors)
        sorted-users (filter-sort-users s (:user-id current-user-data) with-follow @(::query s))
        is-mobile? (responsive/is-mobile-size?)
        following-users (filter #(->> % :user-id (utils/in? follow-publishers-list)) sorted-users)
        unfollowing-users (filter #(->> % :user-id (utils/in? follow-publishers-list) not) sorted-users)]
    [:div.follow-user-picker
      [:div.follow-user-picker-modal
        [:button.mlb-reset.modal-close-bt
          {:on-click #(nav-actions/close-all-panels)}]
        [:div.follow-user-picker-header
          [:button.mlb-reset.invite-user-bt
            {:on-click #(nav-actions/show-section-add)}
            "Invite teammates"]
          [:h3.follow-user-picker-title
            "People"]]
        [:div.follow-user-picker-body
          
          [:div.follow-user-picker-subtitle
            "Select someone to follow their posts and comments more easily."]
          (if (zero? (count all-authors))
            [:div.follow-user-picker-empty-users
              [:div.follow-user-picker-empty-icon]
              [:div.follow-user-picker-empty-copy
                "There are no active team members yet. "
                [:button.mlb-reset.follow-user-picker-empty-invite-bt
                  {:on-click #(nav-actions/show-org-settings :invite-picker)}
                  "Invite your team"]
                " to get started."]]
            [:div.follow-user-picker-body-inner.group
              [:input.follow-user-picker-search-field-input.oc-input
                {:value @(::query s)
                 :type "text"
                 :ref :query
                 :placeholder "Find a person"
                 :on-change #(reset! (::query s) (.. % -target -value))}]
              [:div.follow-user-picker-users-list.group
                ;; Following
                (when (seq following-users)
                  [:div.follow-user-picker-row-header
                    (str "Following (" (count following-users) ")")])
                (when (seq following-users)
                  (for [u following-users]
                    [:div.follow-user-picker-user-row.group
                      {:key (str "follow-user-picker-" (:user-id u))
                       :class (when (:follow u) "selected")}
                      (user-avatar-image u)
                      [:div.follow-user-picker-user
                        [:span.user-name
                          (:name u)]
                        (when (seq (:role u))
                          [:span.user-role
                            (:role u)])]
                      [:button.mlb-reset.follow-bt
                        {:on-click #(user-actions/toggle-publisher (:user-id u))
                         :class (when (:follow u) "unfollow")}
                        (if (:follow u)
                          "Unfollow"
                          "Follow")]]))
                ;; Unfollowing
                (when (seq unfollowing-users)
                  [:div.follow-user-picker-row-header
                    (str "Other people (" (count unfollowing-users) ")")])
                (when (seq unfollowing-users)
                  (for [u unfollowing-users]
                    [:div.follow-user-picker-user-row.group
                      {:key (str "unfollow-user-picker-" (:user-id u))
                       :class (when (:follow u) "selected")}
                      (user-avatar-image u)
                      [:div.follow-user-picker-user
                        [:span.user-name
                          (:name u)]
                        (when (seq (:role u))
                          [:span.user-role
                            (:role u)])]
                      [:button.mlb-reset.follow-bt
                        {:on-click #(user-actions/toggle-publisher (:user-id u))
                         :class (when (:follow u) "unfollow")}
                        (if (:follow u)
                          "Unfollow"
                          "Follow")]]))]])]]]))
