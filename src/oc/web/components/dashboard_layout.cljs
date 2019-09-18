 (ns oc.web.components.dashboard-layout
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.ui :as ui-mixins]
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
            [oc.web.components.paginated-stream :refer (paginated-stream)]
            [oc.web.components.ui.empty-org :refer (empty-org)]
            [oc.web.components.ui.lazy-stream :refer (lazy-stream)]
            [oc.web.components.ui.empty-board :refer (empty-board)]
            [oc.web.components.expanded-post :refer (expanded-post)]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.user-notifications :as user-notifications]
            [oc.web.components.navigation-sidebar :refer (navigation-sidebar)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs dashboard-layout < rum/static
                              rum/reactive
                              ;; Derivative
                              (drv/drv :route)
                              (drv/drv :org-data)
                              (drv/drv :team-data)
                              (drv/drv :board-data)
                              (drv/drv :container-data)
                              (drv/drv :filtered-posts)
                              (drv/drv :editable-boards)
                              (drv/drv :show-add-post-tooltip)
                              (drv/drv :current-user-data)
                              (drv/drv :hide-left-navbar)
                              (drv/drv :sort-type)
                              (drv/drv :cmail-state)
                              (drv/drv :cmail-data)
                              (drv/drv :user-notifications)
                              (drv/drv :mobile-user-notifications)
                              (drv/drv :activity-data)
                              ;; Locals
                              (rum/local false ::sorting-menu-expanded)
                              ;; Mixins
                              ui-mixins/refresh-tooltips-mixin
                              (ui-mixins/on-window-click-mixin (fn [s e]
                               (when (and @(::sorting-menu-expanded s)
                                          (not (utils/event-inside? e (rum/ref-node s :board-sort-menu))))
                                (reset! (::sorting-menu-expanded s) false))))
                              {:before-render (fn [s]
                                ;; Check if it needs any NUX stuff
                                (nux-actions/check-nux)
                                s)
                               :did-mount (fn [s]
                                ;; Reopen cmail if it was open
                                (when-let [org-data @(drv/get-ref s :org-data)]
                                  (when (utils/is-admin-or-author? org-data)
                                    (cmail-actions/cmail-reopen?)))
                                ;; Preload reminders
                                (reminder-actions/load-reminders)
                                (set! (.. js/document -scrollingElement -scrollTop) (utils/page-scroll-top))
                                s)
                               :did-remount (fn [_ s]
                                (doto (.find (js/$ (rum/dom-node s)) "[data-toggle=\"tooltip\"]")
                                  (.tooltip "hide")
                                  (.tooltip "fixTitle"))
                                s)}
  [s]
  (let [org-data (drv/react s :org-data)
        board-data (drv/react s :board-data)
        container-data (drv/react s :container-data)
        posts-data (drv/react s :filtered-posts)
        ;; Board data used as fallback until the board is completely loaded
        org-board-data (first (filter #(= (:slug %) (router/current-board-slug)) (:boards org-data)))
        route (drv/react s :route)
        team-data (drv/react s :team-data)
        activity-data (drv/react s :activity-data)
        is-all-posts (utils/in? (:route route) "all-posts")
        is-follow-ups (utils/in? (:route route) "follow-ups")
        current-activity-id (router/current-activity-id)
        is-tablet-or-mobile? (responsive/is-tablet-or-mobile?)
        is-mobile? (responsive/is-mobile-size?)
        current-board-data (or board-data org-board-data)
        board-container-data (if (or is-all-posts is-follow-ups) container-data board-data)
        empty-board? (and (map? board-container-data)
                          (zero? (count (:posts-list board-container-data))))
        is-drafts-board (= (router/current-board-slug) utils/default-drafts-board-slug)
        all-boards (drv/react s :editable-boards)
        can-compose? (pos? (count all-boards))
        board-view-cookie (router/last-board-view-cookie (router/current-org-slug))
        drafts-board (first (filter #(= (:slug %) utils/default-drafts-board-slug) (:boards org-data)))
        drafts-link (utils/link-for (:links drafts-board) "self")
        board-sort (drv/react s :sort-type)
        show-drafts (pos? (:count drafts-link))
        current-user-data (drv/react s :current-user-data)
        is-admin-or-author (utils/is-admin-or-author? org-data)
        should-show-settings-bt (and (router/current-board-slug)
                                     (not is-all-posts)
                                     (not is-follow-ups)
                                     (not (:read-only board-data)))
        cmail-state (drv/react s :cmail-state)
        _cmail-data (drv/react s :cmail-data)
        user-notifications-data (drv/react s :user-notifications)
        showing-mobile-user-notifications (drv/react s :mobile-user-notifications)
        no-phisical-home-button (js/isiPhoneWithoutPhysicalHomeBt)]
      ;; Entries list
      [:div.dashboard-layout.group
        {:class (when current-activity-id "expanded-post-view")}
        [:div.dashboard-layout-container.group
          {:class (when (drv/react s :hide-left-navbar) "hide-left-navbar")}
          (navigation-sidebar)
          (when (and is-mobile?
                     (not current-activity-id)
                     (or (:collapsed cmail-state)
                         (not cmail-state))
                     (jwt/user-is-part-of-the-team (:team-id org-data)))
            [:div.dashboard-layout-mobile-tabbar
              {:class (utils/class-set {:can-compose can-compose?
                                        :ios-tabbar no-phisical-home-button})}
              [:button.mlb-reset.all-posts-tab
                {:on-click #(do
                              (.stopPropagation %)
                              (nav-actions/nav-to-url! % "all-posts" (oc-urls/all-posts)))
                 :class (when (and (not showing-mobile-user-notifications)
                                   (= (router/current-board-slug) "all-posts"))
                          "active")}]
              [:button.mlb-reset.follow-ups-tab
                {:on-click #(do
                              (.stopPropagation %)
                              (nav-actions/nav-to-url! % "follow-ups" (oc-urls/follow-ups)))
                 :class (when (and (not showing-mobile-user-notifications)
                                   (or (= (router/current-board-slug) "follow-ups")
                                       (= (router/current-board-slug) "must-see")))
                          "active")}]
              [:button.mlb-reset.notifications-tab
                {:on-click #(do
                              (.stopPropagation %)
                              (user-actions/show-mobile-user-notifications))
                 :class (when showing-mobile-user-notifications
                          "active")}]
              (when (user-notifications/has-new-content? user-notifications-data)
                [:span.unread-notifications-dot])
              (when can-compose?
                [:button.mlb-reset.new-post-tab
                  {:on-click #(do
                                (.stopPropagation %)
                                (ui-compose @(drv/get-ref s :show-add-post-tooltip))
                                (user-actions/hide-mobile-user-notifications))}])])
          ;; Mobile notifications
          (when (and is-mobile?
                     showing-mobile-user-notifications)
            (user-notifications/user-notifications))
          ;; Show the board always on desktop except when there is an expanded post and
          ;; on mobile only when the navigation menu is not visible
          [:div.board-container.group
            ; (let [add-post-tooltip (drv/react s :show-add-post-tooltip)
            ;       non-admin-tooltip (str "Carrot is where you'll find key announcements, updates, and "
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
            ;           "Welcome to Carrot!"]
            ;           [:div.add-post-tooltip
            ;             (if is-admin-or-author
            ;               (if is-second-user
            ;                 non-admin-tooltip
            ;                 "Create your first post now to see how Carrot works. Don't worry, you can delete it anytime.")
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
            (when-not current-activity-id
              ;; Board name row: board name, settings button and say something button
              [:div.board-name-container.group
                {:class (when is-drafts-board "drafts-board")}
                ;; Board name and settings button
                [:div.board-name
                  (when (router/current-board-slug)
                    [:div.board-name-with-icon
                      [:div.board-name-with-icon-internal
                        {:class (utils/class-set {:private (and (= (:access current-board-data) "private")
                                                                (not is-drafts-board))
                                                  :public (= (:access current-board-data) "public")})
                         :dangerouslySetInnerHTML (utils/emojify (cond
                                                   is-all-posts
                                                   "All posts"

                                                   is-follow-ups
                                                   "Follow-ups"

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
                       :title (if (= (router/current-board-slug) utils/default-drafts-board-slug)
                               "Only visible to you"
                               "Only visible to invited team members")}])
                  (when (= (:access current-board-data) "public")
                    [:div.public-board
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :data-container "body"
                       :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                       :title "Visible to the world, including search engines"}])
                  (when should-show-settings-bt
                    [:div.board-settings-container
                      ;; Settings button
                      [:button.mlb-reset.board-settings-bt
                        {:data-toggle (when-not is-tablet-or-mobile? "tooltip")
                         :data-placement "top"
                         :data-container "body"
                         :title (str (:name current-board-data) " settings")
                         :on-click #(nav-actions/show-section-editor (:slug current-board-data))}]])]
                (when-not is-drafts-board
                  (let [default-sort (= board-sort dis/default-sort-type)]
                    [:div.board-sort.group
                      {:ref :board-sort-menu}
                      (when is-mobile?
                        [:button.mlb-reset.mobile-search-bt
                          {:on-click (fn [e]
                                       (search-actions/active)
                                       (utils/after 500 #(.focus (js/$ "input.search"))))}])
                      [:button.mlb-reset.board-sort-bt
                        {:on-click #(swap! (::sorting-menu-expanded s) not)}
                        (if default-sort "Recent activity" "Recently posted")]
                      [:div.board-sort-menu
                        {:class (when @(::sorting-menu-expanded s) "show-menu")}
                        [:div.board-sort-menu-item
                          {:class (when default-sort "active")
                           :on-click #(do
                                        (reset! (::sorting-menu-expanded s) false)
                                        (activity-actions/change-sort-type :recent-activity))}
                          "Recent activity"]
                        [:div.board-sort-menu-item
                          {:class (when-not default-sort "active")
                           :on-click #(do
                                        (reset! (::sorting-menu-expanded s) false)
                                        (activity-actions/change-sort-type :recently-posted))}
                          "Recently posted"]]]))])

            ;; Board content: empty org, all posts, empty board, drafts view, entries view
            (cond
              ;; No boards
              (zero? (count (:boards org-data)))
              (empty-org)
              ;; Expanded post
              (and current-activity-id
                   activity-data)
              (expanded-post)
              ;; Empty board
              empty-board?
              (empty-board)
              ;; Paginated board/container
              :else
              (rum/with-key (lazy-stream paginated-stream)
               (str "paginated-posts-component-" (cond is-all-posts "AP" is-follow-ups "FU" :else (:slug current-board-data)) "-" board-sort))
              )]]]))