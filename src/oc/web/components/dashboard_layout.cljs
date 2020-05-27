(ns oc.web.components.dashboard-layout
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.user :as lib-user]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.shared.useragent :as ua]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.stores.search :as search]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.components.cmail :refer (cmail)]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.actions.search :as search-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.reminder :as reminder-actions]
            [oc.web.components.search :refer (search-box)]
            [oc.web.components.explore-view :refer (explore-view)]
            [oc.web.components.ui.follow-button :refer (follow-button)]
            [oc.web.components.expanded-post :refer (expanded-post)]
            [oc.web.components.paginated-stream :refer (paginated-stream)]
            [oc.web.components.ui.empty-org :refer (empty-org)]
            [oc.web.components.ui.lazy-stream :refer (lazy-stream)]
            [oc.web.components.ui.empty-board :refer (empty-board)]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.user-notifications :as user-notifications]
            [oc.web.components.navigation-sidebar :refer (navigation-sidebar)]
            [oc.web.components.ui.poll :refer (poll-portal)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs dashboard-layout < rum/static
                              rum/reactive
                              ;; Derivative
                              (drv/drv :route)
                              (drv/drv :org-data)
                              (drv/drv :team-data)
                              (drv/drv :board-data)
                              (drv/drv :contributions-data)
                              (drv/drv :contributions-user-data)
                              (drv/drv :container-data)
                              (drv/drv :filtered-posts)
                              (drv/drv :items-to-render)
                              (drv/drv :editable-boards)
                              (drv/drv :show-add-post-tooltip)
                              (drv/drv :current-user-data)
                              (drv/drv :cmail-state)
                              (drv/drv :cmail-data)
                              (drv/drv :sorted-user-notifications)
                              (drv/drv :activity-data)
                              (drv/drv :foc-layout)
                              (drv/drv :activities-read)
                              (drv/drv :followers-boards-count)
                              (drv/drv search/search-active?)
                              ;; Mixins
                              ui-mixins/strict-refresh-tooltips-mixin
                              {:before-render (fn [s]
                                ;; Check if it needs any NUX stuff
                                (nux-actions/check-nux)
                                s)
                               :did-mount (fn [s]
                                ;; Reopen cmail if it was open
                                (when-let [editable-boards @(drv/get-ref s :editable-boards)]
                                  (when (pos? (count editable-boards))
                                    (cmail-actions/cmail-reopen?)))
                                ;; Preload reminders
                                (reminder-actions/load-reminders)
                                (set! (.. js/document -scrollingElement -scrollTop) (utils/page-scroll-top))
                                s)}
  [s]
  (let [org-data (drv/react s :org-data)
        board-data (drv/react s :board-data)
        contributions-data (drv/react s :contributions-data)
        contributions-user-data (drv/react s :contributions-user-data)
        container-data (drv/react s :container-data)
        posts-data (drv/react s :filtered-posts)
        _items-to-render (drv/react s :items-to-render)
        foc-layout (drv/react s :foc-layout)
        _activities-read (drv/react s :activities-read)
        current-board-slug (router/current-board-slug)
        current-contributions-id (router/current-contributions-id)
        ;; Board data used as fallback until the board is completely loaded
        org-board-data (first (filter #(= (:slug %) current-board-slug) (:boards org-data)))
        route (drv/react s :route)
        team-data (drv/react s :team-data)
        activity-data (drv/react s :activity-data)
        is-inbox (= current-board-slug "inbox")
        is-all-posts (= current-board-slug "all-posts")
        is-bookmarks (= current-board-slug "bookmarks")
        is-following (= current-board-slug "following")
        is-threads (= current-board-slug "threads")
        is-unfollowing (= current-board-slug "unfollowing")
        is-explore (= current-board-slug "explore")
        is-contributions (seq current-contributions-id)
        current-activity-id (router/current-activity-id)
        is-tablet-or-mobile? (responsive/is-tablet-or-mobile?)
        is-mobile? (responsive/is-mobile-size?)
        current-board-data (or board-data org-board-data)
        board-container-data (cond (seq current-contributions-id)
                                   contributions-data
                                   (dis/is-container? current-board-slug)
                                   container-data
                                   :else
                                   board-data)
        empty-board? (and (not is-threads)
                          (or (and (not is-contributions)
                                   (map? board-container-data)
                                   (zero? (count (:posts-list board-container-data))))
                              (and is-contributions
                                   (map? contributions-data)
                                   (zero? (count (:posts-list contributions-data))))))
        is-drafts-board (= current-board-slug utils/default-drafts-board-slug)
        all-boards (drv/react s :editable-boards)
        can-compose? (pos? (count all-boards))
        board-view-cookie (router/last-board-view-cookie (router/current-org-slug))
        drafts-board (first (filter #(= (:slug %) utils/default-drafts-board-slug) (:boards org-data)))
        drafts-link (utils/link-for (:links drafts-board) "self")
        show-drafts (pos? (:count drafts-link))
        current-user-data (drv/react s :current-user-data)
        is-admin-or-author (utils/is-admin-or-author? org-data)
        should-show-settings-bt (and current-board-slug
                                     (not (dis/is-container? current-board-slug))
                                     (not (:read-only current-board-data)))
        cmail-state (drv/react s :cmail-state)
        _cmail-data (drv/react s :cmail-data)
        user-notifications-data (drv/react s :sorted-user-notifications)
        show-expanded-post (and current-activity-id
                                activity-data
                                (not= activity-data :404)
                                ;; Do not show the post under the wrong board slug/uuid
                                (or (= (:board-slug activity-data) current-board-slug)
                                    (= (:board-uuid activity-data) current-board-slug)))
        no-phisical-home-button (js/isiPhoneWithoutPhysicalHomeBt)
        dismiss-all-link (when is-inbox
                           (utils/link-for (:links container-data) "dismiss-all"))
        search-active? (drv/react s search/search-active?)
        member? (jwt/user-is-part-of-the-team (:team-id org-data))
        is-own-contributions (= (:user-id contributions-user-data) (:user-id current-user-data))
        show-follow-button? (and (contains? board-container-data :following)
                                 (seq (:user-id current-user-data))
                                 (not is-drafts-board)
                                 (not is-own-contributions))
        followers-boards-count (drv/react s :followers-boards-count)]
      ;; Entries list
      [:div.dashboard-layout.group
        {:class (utils/class-set {:search-active search-active?})}
        [:div.mobile-more-menu]
        [:div.dashboard-layout-container.group
          (navigation-sidebar)
          (when (and is-mobile?
                     (or (:collapsed cmail-state)
                         (not cmail-state))
                     member?)
            [:div.dashboard-layout-mobile-tabbar
              {:class (utils/class-set {:can-compose can-compose?
                                        :ios-app-tabbar (and ua/mobile-app?
                                                             no-phisical-home-button)
                                        :ios-web-tabbar (and (not ua/mobile-app?)
                                                             no-phisical-home-button)})}
              [:button.mlb-reset.tab-button.following-tab
                {:on-click #(do
                              (.stopPropagation %)
                              (nav-actions/nav-to-url! % "following" (oc-urls/following)))
                 :class (when (= current-board-slug "following")
                          "active")}
                [:span.tab-icon]
                [:span.tab-label "Home"]]
              [:button.mlb-reset.tab-button.unfollowing-tab
                {:on-click #(do
                              (.stopPropagation %)
                              (nav-actions/nav-to-url! % "unfollow" (oc-urls/unfollowing)))
                 :class (when (= current-board-slug "unfollow")
                          "active")}
                [:span.tab-icon]
                [:span.tab-label "Explore"]]
              [:button.mlb-reset.tab-button.notifications-tab
                {:on-click #(do
                              (.stopPropagation %)
                              (nav-actions/nav-to-url! % "threads" (oc-urls/threads)))
                 :class (when is-threads
                          "active")}
                [:span.tab-icon
                  (when (user-notifications/has-new-content? user-notifications-data)
                    [:span.unread-dot])]
                [:span.tab-label "Threads"]]
              (when can-compose?
                [:button.mlb-reset.tab-button.new-post-tab
                  {:on-click #(do
                                (.stopPropagation %)
                                (ui-compose @(drv/get-ref s :show-add-post-tooltip)))}
                  [:span.tab-icon]
                  [:span.tab-label "Add"]])])
          ;; Show the board always on desktop except when there is an expanded post and
          ;; on mobile only when the navigation menu is not visible
          [:div.board-container.group
            ; (let [add-post-tooltip (drv/react s :show-add-post-tooltip)
            ;       non-admin-tooltip (str "Wut is where you'll find key announcements, updates, and "
            ;                              "decisions to keep you and your team pulling in the same direction.")
            ;       is-second-user (= add-post-tooltip :is-second-user)]
            ;   (when (and (not is-drafts-board)
            ;              (not current-activity-id)
            ;              add-post-tooltip)
            ;     [:div.add-post-tooltip-container.group
            ;       [:button.mlb-reset.add-post-tooltip-dismiss
            ;         {:on-click #(nux-actions/dismiss-add-post-tooltip)}]
            ;       [:div.add-post-tooltips
            ;         {:class (when is-second-user "second-user")}
            ;         [:div.add-post-tooltip-box-mobile]
            ;         [:div.add-post-tooltip-title
            ;           "Welcome to Wut!"]
            ;           [:div.add-post-tooltip
            ;             (if is-admin-or-author
            ;               (if is-second-user
            ;                 non-admin-tooltip
            ;                 "Create your first post now to see how Wut works. Don't worry, you can delete it anytime.")
            ;               non-admin-tooltip)]
            ;           (when (and is-admin-or-author
            ;                      (not is-second-user))
            ;             [:button.mlb-reset.add-post-bt
            ;               {:on-click #(when can-compose? (ui-compose @(drv/get-ref s :show-add-post-tooltip)))}
            ;               [:span.add-post-bt-pen]
            ;               "New post"])
            ;         [:div.add-post-tooltip-box.big-web-only
            ;           {:class (when is-second-user "second-user")}]]]))
            (when (and (not is-mobile?)
                       can-compose?)
               (cmail))
            (when (not is-explore)
              ;; Board name row: board name, settings button and say something button
              [:div.board-name-container.group
                {:class (when is-drafts-board "drafts-board")}
                ;; Board name and settings button
                [:div.board-name
                  (cond
                    current-contributions-id
                    [:div.board-name-with-icon.contributions
                      [:div.board-name-with-icon-internal
                        (if is-own-contributions
                          "You"
                          (lib-user/name-for contributions-user-data))
                        ; (when (pos? (:total-count contributions-data))
                        ;   [:span.count (:total-count contributions-data)])
                        ]]
                    :else
                    [:div.board-name-with-icon
                      [:div.board-name-with-icon-internal
                        {:class (utils/class-set {:private (and (= (:access current-board-data) "private")
                                                                (not is-drafts-board))
                                                  :public (= (:access current-board-data) "public")
                                                  :home-icon is-following
                                                  :all-icon is-all-posts
                                                  :explore-icon is-unfollowing
                                                  :saved-icon is-bookmarks
                                                  :drafts-icon is-drafts-board
                                                  :threads-icon is-threads})
                         :dangerouslySetInnerHTML (utils/emojify (cond
                                                   is-inbox
                                                   "Unread"

                                                   is-all-posts
                                                   "All"

                                                   is-unfollowing
                                                   "<div class=\"explore-icon-in\"></div> Explore"

                                                   is-bookmarks
                                                   "Bookmarks"

                                                   is-following
                                                   "Home"

                                                   is-threads
                                                   "Threads"

                                                   :default
                                                   ;; Fallback to the org board data
                                                   ;; to avoid showing an empty name while loading
                                                   ;; the board data
                                                   (:name current-board-data)))}]])
                  (when (and (= (:access current-board-data) "private")
                             (not is-drafts-board))
                    [:div.private-board
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :data-container "body"
                       :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                       :title (if (= current-board-slug utils/default-drafts-board-slug)
                               "Only visible to you"
                               "Only visible to invited team members")}])
                  (when (= (:access current-board-data) "public")
                    [:div.public-board
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :data-container "body"
                       :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                       :title "Visible to the world, including search engines"}])]
                [:div.board-name-right
                  (when show-follow-button?
                    (let [resource-type (if is-contributions :user :board)
                          resource-data (if is-contributions
                                          contributions-user-data
                                          board-container-data)]
                      (follow-button {:following (:following resource-data)
                                      :resource-type resource-type
                                      :resource-uuid (if is-contributions
                                                       (:author-uuid board-container-data)
                                                       (:uuid board-container-data))})))
                  (when is-own-contributions
                    [:div.board-settings-container
                      ;; Settings button
                      [:button.mlb-reset.board-settings-bt
                        {:data-toggle (when-not is-tablet-or-mobile? "tooltip")
                         :data-placement "top"
                         :data-container "body"
                         :title "Edit proile"
                         :on-click #(nav-actions/show-user-settings :profile)}]])
                  (when should-show-settings-bt
                    [:div.board-settings-container
                      ;; Settings button
                      [:button.mlb-reset.board-settings-bt
                        {:data-toggle (when-not is-tablet-or-mobile? "tooltip")
                         :data-placement "top"
                         :data-container "body"
                         :title (str (:name current-board-data) " settings")
                         :on-click #(nav-actions/show-section-editor (:slug current-board-data))}]])
                  (when (and show-follow-button?
                             (not is-all-posts)
                             (not is-following)
                             (not is-unfollowing)
                             (not is-bookmarks)
                             (not is-drafts-board)
                             (not is-contributions))
                    ; (let [followers-count (get followers-boards-count (:uuid current-board-data))]
                    ;   [:div.followers-count
                    ;     (if (pos? followers-count)
                    ;       (str followers-count " follower" (when (not= followers-count 1) "s"))
                    ;       "No followers")])
                    )
                  (when (and dismiss-all-link
                             (pos? (count posts-data)))
                    [:button.mlb-reset.complete-all-bt
                      {:on-click #(activity-actions/inbox-dismiss-all)
                       :data-toggle (when-not is-mobile? "tooltip")
                       :data-placement "top"
                       :data-container "body"
                       :title "Dismiss all"}])
                  (when (and is-following
                             member?)
                    [:button.mlb-reset.curate-feed-bt
                      {:on-click #(nav-actions/show-follow-picker)
                       :data-toggle (when-not is-mobile? "tooltip")
                       :data-placement "top"
                       :data-container "body"
                       :title "Curate your Home feed"}])
                  (when (and (not is-drafts-board)
                             is-mobile?)
                    (search-box))]])
            ;; Board content: empty org, all posts, empty board, drafts view, entries view
            (cond
              ;; Explore view
              is-explore
              (explore-view)
              ;; No boards
              (zero? (count (:boards org-data)))
              (empty-org)
              ;; Empty board
              empty-board?
              (empty-board)
              ;; Paginated board/container
              :else
              (rum/with-key (lazy-stream paginated-stream)
               (str "paginated-posts-component-" (
                cond is-inbox       "IN"
                     is-threads     "TH"
                     is-all-posts   "AP"
                     is-bookmarks   "BM"
                     is-following   "FL"
                     is-unfollowing "UF"
                     :else          (:slug current-board-data)))))]]]))