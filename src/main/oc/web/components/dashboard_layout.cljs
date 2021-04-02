(ns oc.web.components.dashboard-layout
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.user :as user-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.components.cmail :refer (cmail)]
            [oc.web.actions.label :as label-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.user-profile :refer (user-profile)]
            [oc.web.components.explore-view :refer (explore-view)]
            [oc.web.components.user-notifications :as user-notifications]
            [oc.web.components.ui.follow-button :refer (follow-banner)]
            [oc.web.components.paginated-stream :refer (paginated-stream)]
            [oc.web.components.ui.empty-org :refer (empty-org)]
            [oc.web.components.ui.lazy-stream :refer (lazy-stream)]
            [oc.web.components.ui.empty-board :refer (empty-board)]
            [oc.web.components.ui.mobile-tabbar :refer (mobile-tabbar)]
            [oc.web.components.navigation-sidebar :refer (navigation-sidebar)]))

(rum/defcs dashboard-layout < rum/static
                              rum/reactive
                              ;; Derivative
                              (drv/drv :route)
                              (drv/drv :org-data)
                              (drv/drv :team-data)
                              (drv/drv :contributions-user-data)
                              (drv/drv :label-data)
                              (drv/drv :label-entries-data)
                              (drv/drv :container-data)
                              (drv/drv :contributions-data)
                              (drv/drv :org-slug)
                              (drv/drv :board-slug)
                              (drv/drv :label-slug)
                              (drv/drv :contributions-id)
                              (drv/drv :activity-uuid)
                              (drv/drv :filtered-posts)
                              (drv/drv :items-to-render)
                              (drv/drv :show-add-post-tooltip)
                              (drv/drv :current-user-data)
                              (drv/drv :cmail-state)
                              (drv/drv :cmail-data)
                              (drv/drv :activity-data)
                              (drv/drv :foc-layout)
                              (drv/drv :activities-read)
                              (drv/drv :followers-boards-count)
                              (drv/drv :comment-reply-to)
                              (drv/drv :mobile-user-notifications)
                              (drv/drv :user-notifications)
                              (drv/drv :show-invite-box)
                              ;; Mixins
                              ui-mixins/strict-refresh-tooltips-mixin
                              {:did-mount (fn [s]
                                ;; Reopen cmail if it was open
                                (when-let [org-data @(drv/get-ref s :org-data)]
                                  (when (:can-compose? org-data)
                                    (cmail-actions/cmail-reopen?)))
                                (set! (.. js/document -scrollingElement -scrollTop) (utils/page-scroll-top))
                                s)}
  [s]
  (let [org-data (drv/react s :org-data)
        contributions-user-data (drv/react s :contributions-user-data)
        container-data* (drv/react s :container-data)
        contributions-data (drv/react s :contributions-data)
        label-entries-data (drv/react s :label-entries-data)
        label-data (drv/react s :label-data)
        _posts-data (drv/react s :filtered-posts)
        _items-to-render (drv/react s :items-to-render)
        _foc-layout (drv/react s :foc-layout)
        _activities-read (drv/react s :activities-read)
        _comment-reply-to (drv/react s :comment-reply-to)
        current-board-slug (drv/react s :board-slug)
        current-label-slug (drv/react s :label-slug)
        current-contributions-id (drv/react s :contributions-id)
        current-activity-id (drv/react s :activity-uuid)
        current-org-slug (drv/react s :org-slug)
        current-user-data (drv/react s :current-user-data)
        mobile-user-notifications (drv/react s :mobile-user-notifications)
        user-notifications-data (drv/react s :user-notifications)
        add-post-tooltip (drv/react s :show-add-post-tooltip)
        show-invite-box (drv/react s :show-invite-box)
        is-mobile? (responsive/is-mobile-size?)
        is-admin-or-author (#{:admin :author} (:role current-user-data))
        show-mobile-invite-people? (and is-mobile?
                                        current-org-slug
                                        is-admin-or-author
                                        show-invite-box)
        ;; Board data used as fallback until the board is completely loaded
        org-board-data (dis/org-board-data org-data current-board-slug)
        _route (drv/react s :route)
        _team-data (drv/react s :team-data)
        _activity-data (drv/react s :activity-data)
        is-inbox (= current-board-slug "inbox")
        is-all-posts (= current-board-slug "all-posts")
        is-bookmarks (= current-board-slug "bookmarks")
        is-following (= current-board-slug "following")
        is-replies (= current-board-slug "replies")
        is-unfollowing (= current-board-slug "unfollowing")
        is-topics (= current-board-slug "topics")
        is-contributions (seq current-contributions-id)
        is-label (seq current-label-slug)
        is-tablet-or-mobile? (responsive/is-tablet-or-mobile?)
        is-container? (dis/is-container? current-board-slug)
        container-data (if (and (not is-contributions)
                                (not is-label)
                                (not is-container?)
                                (not container-data*))
                         org-board-data
                         container-data*)
        loading-container? (or (not (map? container-data*))
                               (not (contains? container-data* :links)))
        empty-container? (and (not loading-container?)
                              (not (seq (:posts-list container-data*))))
        is-drafts-board (= current-board-slug utils/default-drafts-board-slug)
        should-show-settings-bt (and current-board-slug
                                     (not is-container?)
                                     (not is-topics)
                                     (not is-contributions)
                                     (not (:read-only container-data)))
        should-show-label-edit-bt (and current-label-slug
                                       (:can-edit? label-data))
        cmail-state (drv/react s :cmail-state)
        _cmail-data (drv/react s :cmail-data)
        member? (:member? org-data)
        show-follow-banner? (and (not is-container?)
                                 (not (seq current-contributions-id))
                                 (not (seq current-label-slug))
                                 (not is-drafts-board)
                                 (map? org-board-data)
                                 (false? (:following org-board-data)))
        _followers-boards-count (drv/react s :followers-boards-count)
        can-compose? (:can-compose? org-data)
        show-desktop-cmail? (and (not is-mobile?)
                                 can-compose?
                                 (or (not is-contributions)
                                     (not (:collapsed cmail-state))))
        paginated-stream-key (str "paginated-posts-component-"
                              (cond is-contributions
                                    current-contributions-id
                                    is-label
                                    current-label-slug
                                    current-board-slug
                                    (name current-board-slug))
                              (when (:last-seen-at container-data)
                                (str "-" (:last-seen-at container-data))))
        show-feed? (or (not is-contributions)
                       (not= (:role contributions-user-data) :viewer)
                       (pos? (count (:posts-list contributions-data)))
                       (pos? (count (:posts-list label-entries-data))))
        no-phisical-home-button (^js js/isiPhoneWithoutPhysicalHomeBt)
        can-create-topic? (utils/link-for (:links org-data) "create" "POST")]
      ;; Entries list
      [:div.dashboard-layout.group
        [:div.mobile-more-menu]
        [:div.dashboard-layout-container.group
          {:class (utils/class-set {:has-mobile-invite-box show-mobile-invite-people?
                                    :ios-app-tabbar (and ua/mobile-app?
                                                         no-phisical-home-button)
                                    :ios-web-tabbar (and (not ua/mobile-app?)
                                                         no-phisical-home-button)})}
          (navigation-sidebar)
          (when show-mobile-invite-people?
            [:div.mobile-invite-box
              [:button.mlb-reset.mobile-invite-box-bt
                {:on-click #(nav-actions/show-org-settings :invite-picker)}
                "Explore together - "
                [:span.invite-teammates
                  "Invite your teammates"]
                "!"]
              [:button.mlb-reset.close-mobile-invite-box
                {:on-click #(user-actions/dismiss-invite-box (:user-id current-user-data) true)}]])
          (when (and is-mobile?
                     (or (:collapsed cmail-state)
                         (not cmail-state))
                     member?)
            (mobile-tabbar {:active-tab (cond mobile-user-notifications     :user-notifications
                                              is-following                  :following
                                              is-replies                    :replies
                                              (or is-topics
                                                  (seq current-board-slug)
                                                  (not is-container?))      :topics
                                              :else                         :none)
                            :can-compose? can-compose?
                            :user-notifications-data user-notifications-data
                            :show-add-post-tooltip add-post-tooltip}))
          (when (and is-mobile?
                     mobile-user-notifications)
            (user-notifications/user-notifications))
          ;; Show the board always on desktop except when there is an expanded post and
          ;; on mobile only when the navigation menu is not visible
          [:div.board-container.group
            (when show-desktop-cmail?
              (cmail))
            (when is-contributions
              (user-profile contributions-user-data))
            (when (and show-desktop-cmail?
                       (not (:collapsed cmail-state))
                       (:fullscreen cmail-state))
              [:div.dashboard-layout-cmail-placeholder])
            (when show-follow-banner?
              [:div.dashboard-layout-follow-banner
                (follow-banner container-data)])
            (when show-feed?
              ;; Board name row: board name, settings button and say something button
              [:div.board-name-container.group
                {:class (utils/class-set {:drafts-board is-drafts-board
                                          :topics-view is-topics})}
                ;; Board name and settings button
                [:div.board-name
                  {:class (when is-topics "topics-header")}
                  [:div.board-name-with-icon
                    {:class (when current-contributions-id "contributions")}
                    [:div.board-name-with-icon-internal
                      {:class (utils/class-set {:private (and (= (:access container-data) "private")
                                                              (not is-drafts-board))
                                                :public (= (:access container-data) "public")
                                                :label-icon is-label
                                                :contributions is-contributions
                                                :home-icon is-following
                                                :unfollowing-icon is-unfollowing
                                                :all-icon is-all-posts
                                                :topics-icon is-topics
                                                :saved-icon is-bookmarks
                                                :drafts-icon is-drafts-board
                                                :replies-icon is-replies
                                                :board-icon (and (not is-container?)
                                                                 (not is-contributions)
                                                                 (not is-topics)
                                                                 (not is-drafts-board)
                                                                 (not current-activity-id))})}
                      (cond is-inbox
                            "Unread"

                            is-all-posts
                            "All"

                            is-topics
                            "Explore"

                            is-bookmarks
                            "Bookmarks"

                            is-following
                            "Home"

                            is-unfollowing
                            "Unfollowing"

                            is-replies
                            "Activity"

                            is-label
                            (or (:name label-data) (:slug label-data) current-label-slug)

                            current-contributions-id
                            (str "Latest from " (:short-name contributions-user-data))

                            :else
                            ;; Fallback to the org board data
                            ;; to avoid showing an empty name while loading
                            ;; the board data
                            (:name container-data))]]
                  (when (and (= (:access container-data) "private")
                             (not is-drafts-board))
                    [:div.private-board
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :data-container "body"
                       :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                       :title (if (= current-board-slug utils/default-drafts-board-slug)
                               "Only visible to you"
                               "Only visible to invited team members")}])
                  (when (= (:access container-data) "public")
                    [:div.public-board
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :data-container "body"
                       :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                       :title "Visible to the world, including search engines"}])
                  (when (and is-topics
                             can-create-topic?)
                    [:button.mlb-reset.explore-view-block.create-topic-bt
                     {:on-click #(nav-actions/show-section-add)}
                     [:span.plus]
                     [:span.new-topic "Add new topic"]])
                  (when (and is-topics
                             (:can-create-label? org-data))
                    [:button.mlb-reset.explore-view-block.manage-labels-bt
                     {:on-click #(label-actions/show-labels-manager)}
                     [:span.manage-labels-bt-icon]
                     [:span.manage-labels-bt-text
                       "Manage labels"]])]
                (when-not is-topics
                  [:div.board-name-right
                    (when should-show-settings-bt
                      [:div.board-settings-container
                        ;; Settings button
                        [:button.mlb-reset.board-settings-bt
                          {:data-toggle (when-not is-tablet-or-mobile? "tooltip")
                          :data-placement "top"
                          :data-container "body"
                          :title (str (:name container-data) " settings")
                          :on-click #(nav-actions/show-section-editor (:slug container-data))}]])
                   (when should-show-label-edit-bt
                     [:div.board-settings-container
                        ;; Settings button
                      [:button.mlb-reset.board-settings-bt
                       {:data-toggle (when-not is-tablet-or-mobile? "tooltip")
                        :data-placement "top"
                        :data-container "body"
                        :title (str (:name label-data) " edit")
                        :on-click #(label-actions/edit-label label-data)}]])])])
              (when show-feed?
                ;; Board content: empty org, all posts, empty board, drafts view, entries view
                (cond
                  ;; Explore view
                  is-topics
                  (explore-view)
                  ;; No boards
                  (zero? (count (:boards org-data)))
                  (empty-org)
                  ;; Empty board
                  empty-container?
                  (empty-board)
                  ;; Paginated board/container
                  :else
                  (rum/with-key (lazy-stream paginated-stream) paginated-stream-key)))]]]))
