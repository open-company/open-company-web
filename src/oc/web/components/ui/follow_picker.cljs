(ns oc.web.components.ui.follow-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.user :as user-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.invite-email :refer (invite-email)]
            [oc.web.components.ui.follow-button :refer (follow-button)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn- is-user? [item]
  (= (:resource-type item) :user))

(defn- is-board? [item]
  (= (:resource-type item) :board))

(defn- sort-items [items]
  (sort-by #(if (is-board? %)
              (:name %)
              (:short-name %))
   items))

(defn- search-string [v q]
  (-> v string/lower (string/includes? q)))

(defn- search-item [item q]
  (or (-> item :name (search-string q))
      (-> item :slug (search-string q))
      (-> item :first-name (search-string q))
      (-> item :last-name (search-string q))
      (-> item :email (search-string q))
      (-> item :title (search-string q))
      (-> item :location (search-string q))))

(defn- filter-item [s current-user-id item q]
  (and (or (and (is-user? item)
                (not= current-user-id (:user-id item)))
           (and (is-board? item)
                (not= (:slug item) utils/default-drafts-board-slug)
                (not (:publisher-board item))))
        (or (not (seq q))
            (search-item item q)
            (some (partial search-item item) (string/split q #"\s"))
            (and (= q "follow")
                 (:follow item))
            (and (= q "unfollow")
                 (not (:follow item))))))

(defn- filter-sort-items [s current-user-id items q]
  (sort-items (filterv #(filter-item s current-user-id % (string/lower q)) items)))

(rum/defc empty-user-component < rum/static
  [{:keys [org-data current-user-data]}]
  [:div.follow-picker-empty-users
    [:div.invite-users-box
      [:div.invite-users-box-inner.group
        [:div.invite-users-title
          "Invite your team to join you!"]
        (invite-email {:rows-num 3
                       :hide-user-role true
                       :save-title "Send invites"
                       :saving-title "Sending invites"
                       :saved-title "Invites sent!"})
        [:div.invite-users-footer
          [:span.invite-user-or
            "Or, "]
          [:button.mlb-reset.invite-link-bt
            {:on-click #(nav-actions/show-org-settings :invite-email)}
            "generate an invite link to share"]]]]
    [:div.follow-picker-empty-header
      "People (1)"]
    [:div.follow-picker-empty-self-user
      (user-avatar-image current-user-data)
      [:span.user-name
        (str (:name current-user-data) " (you)")]
      [:span.user-role
        (:title current-user-data)]
      [:span.followers-count
        ; "No followers"
        ]
      [:button.mlb-reset.edit-profile-bt
        {:on-click #(nav-actions/show-user-settings :profile)}
        "Edit profile"]]])

(defn- follow-picker-items [state items prefix]
  (let [followers-boards-count (drv/react state :followers-boards-count)
        followers-publishers-count (drv/react state :followers-publishers-count)
        is-mobile? (responsive/is-mobile-size?)]
    (for [i items
          :let [board? (is-board? i)]]
      [:div.follow-picker-item-row.group
        {:key (str prefix "-picker-" (if board? (:uuid i) (:user-id i)))
         :class (when (:follow i) "selected")}
        (if board?
          [:div.follow-picker-board-item
            (:name i)]
          [:div.follow-picker-user-item
            (user-avatar-image i)
            [:span.user-name
              (:name i)]
            [:span.user-role
              (:title i)]])
        (when (and board?
                   (not (:read-only i)))
          [:button.mlb-reset.board-settings-bt
            {:data-toggle (when-not is-mobile? "tooltip")
             :data-placement "top"
             :data-container "body"
             :title (str (:name i) " settings")
             :on-click #(nav-actions/show-section-editor (:slug i))}])
        (let [followers (if board?
                          (get followers-boards-count (:uuid i))
                          (get followers-publishers-count (:user-id i)))
              followers-count (:count followers)]
          [:span.followers-count
            (when (pos? followers-count)
              (str followers-count " follower" (when (not= followers-count 1) "s")))])
        (follow-button {:following (:follow i)
                        :resource-type (:resource-type i)
                        :resource-uuid (if board? (:uuid i) (:user-id i))})])))

(rum/defcs follow-picker < rum/reactive

 (drv/drv :org-data)
 (drv/drv :active-users)
 (drv/drv :current-user-data)
 (drv/drv :follow-boards-list)
 (drv/drv :follow-publishers-list)
 (drv/drv :followers-boards-count)
 (drv/drv :followers-publishers-count)
 (rum/local "" ::query)
 (rum/local false ::saving)
 (rum/local :boards ::filter)
 (rum/local false ::filter-open)
 ui-mixins/strict-refresh-tooltips-mixin
 (ui-mixins/on-window-click-mixin (fn [s e]
  (when (and @(::filter-open s)
             (not (utils/event-inside? e (rum/ref-node s :follow-filter-bt))))
    (reset! (::filter-open s) false))))
 {:init (fn [s]
   ;; Refresh the following list
   (user-actions/load-follow-list)
   (user-actions/load-followers-count)
   s)
  :will-unmount (fn [s]
   (user-actions/refresh-follow-containers)
   s)}

  [s]
  (let [org-data (drv/react s :org-data)
        current-user-data (drv/react s :current-user-data)
        all-active-users (drv/react s :active-users)
        follow-boards-list (map :uuid (drv/react s :follow-boards-list))
        follow-publishers-list (map :user-id (drv/react s :follow-publishers-list))
        all-boards (map #(assoc % :resource-type :board) (:boards org-data))
        authors-uuids (->> org-data :authors (map :user-id) set)
        all-authors (->> all-active-users
                     vals
                     (filter #(and (authors-uuids (:user-id %))
                                   (not= (:user-id current-user-data) (:user-id %))))
                     (map #(assoc % :resource-type :user)))
        all-items (case @(::filter s)
                   :users all-authors
                   :boards all-boards
                   (concat all-boards all-authors))
        with-follow (map #(assoc % :follow (or (and (is-user? %)
                                                    (utils/in? follow-publishers-list (:user-id %)))
                                               (and (is-board? %)
                                                    (utils/in? follow-boards-list (:uuid %)))))
                      all-items)
        sorted-items (filter-sort-items s (:user-id current-user-data) with-follow @(::query s))
        following-items (filter :follow sorted-items)
        unfollowing-items (filter (comp not :follow) sorted-items)
        show-following? (or (not= (count all-authors) 1)
                            (not= (count all-boards) 1))]
    [:div.follow-picker
      [:div.follow-picker-modal
        [:button.mlb-reset.modal-close-bt
          {:on-click #(nav-actions/close-all-panels)}]
        [:div.follow-picker-header
          [:button.mlb-reset.create-board-bt
            {:on-click #(nav-actions/show-section-add)}
            "New topic"]
          [:h3.follow-picker-title
            "Personalize your Home feed"]]
        [:div.follow-picker-body
          (if-not show-following?
            [:div.follow-picker-empty-items
              [:div.follow-picker-empty-icon]
              [:div.follow-picker-empty-copy
                "There are no topics to follow yet. "
                (when (utils/link-for (:links org-data) "create")
                  [:button.mlb-reset.follow-picker-empty-invite-bt
                    {:on-click #(nav-actions/show-org-settings :invite-picker)}
                    "Add a topic to get started."])]
              (empty-user-component {:org-data org-data :current-user-data current-user-data})]
            [:div.follow-picker-body-inner.group
              [:input.follow-picker-search-field-input.oc-input
                {:value @(::query s)
                 :type "text"
                 :ref :query
                 :class (name @(::filter s))
                 :placeholder (case @(::filter s)
                               :all    "Find a topic or person"
                               :users  "Find a person"
                               :boards "Find a topic")
                 :on-change #(reset! (::query s) (.. % -target -value))}]
              [:div.follow-picker-items-list.group
                ;; Following
                [:div.follow-picker-row-header.group
                  (when (seq following-items)
                    [:div.follow-picker-row-header-left
                      "Subscriptions"])
                  (comment ;; Hide filter for now
                    [:div.follow-picker-row-header-right
                      [:button.mlb-reset.follow-filter-bt
                        {:ref :follow-filter-bt
                         :on-click #(swap! (::filter-open s) not)}
                        (case @(::filter s)
                         :users "Only people"
                         :boards "Only topics"
                         "All topics & people")]
                      (when @(::filter-open s)
                        (dropdown-list {:items [{:value :all :label "All topics & people"}
                                                {:value :users :label "Only people"}
                                                {:value :boards :label "Only topics"}]
                                        :value @(::filter s)
                                        :on-change #(reset! (::filter s) (:value %))}))])]
                (follow-picker-items s following-items "follow")
                ;; Unfollowing
                (when (seq unfollowing-items)
                  [:div.follow-picker-row-header
                    [:div.follow-picker-row-header-left.unfollow
                      "Suggestions"]])
                (when (seq unfollowing-items)
                  (follow-picker-items s unfollowing-items "unfollow"))]])]]]))
