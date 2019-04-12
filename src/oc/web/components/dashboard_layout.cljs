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
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.qsg :as qsg-actions]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.reminder :as reminder-actions]
            [oc.web.components.all-posts :refer (all-posts)]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.empty-org :refer (empty-org)]
            [oc.web.components.ui.empty-board :refer (empty-board)]
            [oc.web.components.section-stream :refer (section-stream)]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.qsg-breadcrumb :refer (qsg-breadcrumb)]
            [oc.web.components.navigation-sidebar :refer (navigation-sidebar)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs dashboard-layout < rum/reactive
                              ;; Derivative
                              (drv/drv :qsg)
                              (drv/drv :route)
                              (drv/drv :org-data)
                              (drv/drv :team-data)
                              (drv/drv :board-data)
                              (drv/drv :ap-initial-at)
                              (drv/drv :filtered-posts)
                              (drv/drv :editable-boards)
                              (drv/drv :show-add-post-tooltip)
                              (drv/drv :mobile-navigation-sidebar)
                              (drv/drv :current-user-data)
                              (drv/drv :hide-left-navbar)
                              ;; Locals
                              ;; Commenting out board sorting for now
                              ; (rum/local :default ::board-sort)
                              ; (rum/local false ::sorting-menu-expanded)
                              ;; Mixins
                              ;; Commenting out board sorting for now
                              ; (on-window-click-mixin (fn [s e]
                              ;  (when (and @(::sorting-menu-expanded s)
                              ;             (not (utils/event-inside? e (rum/ref-node s :board-sort-menu))))
                              ;   (reset! (::sorting-menu-expanded s) false))))
                              {:before-render (fn [s]
                                ;; Check if it needs any NUX stuff
                                (nux-actions/check-nux)
                                s)
                               :did-mount (fn [s]
                                ;; Reopen cmail if it was open
                                (when-let [org-data @(drv/get-ref s :org-data)]
                                  (when (utils/is-admin-or-author? org-data)
                                    (activity-actions/cmail-reopen?)))
                                ;; Preload reminders
                                (reminder-actions/load-reminders)
                                s)}
  [s]
  (let [org-data (drv/react s :org-data)
        board-data (drv/react s :board-data)
        posts-data (drv/react s :filtered-posts)
        route (drv/react s :route)
        team-data (drv/react s :team-data)
        is-all-posts (utils/in? (:route route) "all-posts")
        is-must-see (utils/in? (:route route) "must-see")
        current-activity-id (router/current-activity-id)
        is-tablet-or-mobile? (responsive/is-tablet-or-mobile?)
        is-mobile? (responsive/is-mobile-size?)
        empty-board? (zero? (count posts-data))
        is-drafts-board (= (:slug board-data) utils/default-drafts-board-slug)
        all-boards (drv/react s :editable-boards)
        can-compose (pos? (count all-boards))
        board-view-cookie (router/last-board-view-cookie (router/current-org-slug))
        drafts-board (first (filter #(= (:slug %) utils/default-drafts-board-slug) (:boards org-data)))
        drafts-link (utils/link-for (:links drafts-board) "self")
        ; board-sort (::board-sort s)
        show-drafts (pos? (:count drafts-link))
        mobile-navigation-sidebar (drv/react s :mobile-navigation-sidebar)
        current-user-data (drv/react s :current-user-data)
        is-admin-or-author (utils/is-admin-or-author? org-data)
        should-show-settings-bt (and (router/current-board-slug)
                                     (not is-all-posts)
                                     (not is-must-see)
                                     (not (:read-only board-data)))
        qsg-data (drv/react s :qsg)]
      ;; Entries list
      [:div.dashboard-layout.group
        [:div.dashboard-layout-container.group
          {:class (when (drv/react s :hide-left-navbar) "hide-left-navbar")}
          (when-not is-mobile?
            (navigation-sidebar))
          ;; Show the board always on desktop and
          ;; on mobile only when the navigation menu is not visible
          (when (or (not is-tablet-or-mobile?)
                    (not mobile-navigation-sidebar))
            [:div.board-container.group
              ;; Board name row: board name, settings button and say something button
              [:div.board-name-container.group
                ;; Board name and settings button
                [:div.board-name
                  (when (router/current-board-slug)
                    [:div.board-name-with-icon
                      [:div.board-name-with-icon-internal
                        {:class (utils/class-set {:private (and (= (:access board-data) "private")
                                                                (not is-drafts-board))
                                                  :public (= (:access board-data) "public")})
                         :dangerouslySetInnerHTML (utils/emojify (cond
                                                   is-all-posts
                                                   "All posts"

                                                   is-must-see
                                                   "Must see"

                                                   :default
                                                   (:name board-data)))}]])
                  ; (when (and (= (:access board-data) "private")
                  ;            (not is-drafts-board))
                  ;   [:div.private-board
                  ;     {:data-toggle "tooltip"
                  ;      :data-placement "top"
                  ;      :data-container "body"
                  ;      :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                  ;      :title (if (= (router/current-board-slug) utils/default-drafts-board-slug)
                  ;              "Only visible to you"
                  ;              "Only visible to invited team members")}
                  ;     "Private"])
                  ; (when (= (:access board-data) "public")
                  ;   [:div.public-board
                  ;     {:data-toggle "tooltip"
                  ;      :data-placement "top"
                  ;      :data-container "body"
                  ;      :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                  ;      :title "Visible to the world, including search engines"}
                  ;     "Public"])
                  (when should-show-settings-bt
                    [:div.board-settings-container
                      ;; Settings button
                      [:button.mlb-reset.board-settings-bt
                        {:data-toggle (when-not is-tablet-or-mobile? "tooltip")
                         :data-placement "top"
                         :data-container "body"
                         :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                         :title (str (:name board-data) " settings")
                         :on-click #(do
                                      (when (:show-section-settings-tooltip qsg-data)
                                        (qsg-actions/dismiss-section-settings-tooltip))
                                      (dis/dispatch! [:input [:show-section-editor] true]))}]
                    (when (:show-section-settings-tooltip qsg-data)
                      [:div.section-settings-tooltip-container.group
                        [:div.section-settings-tooltip-top-arrow]
                        [:button.mlb-reset.section-settings-tooltip-dismiss
                          {:on-click #(qsg-actions/dismiss-section-settings-tooltip)}]
                        [:div.section-settings-tooltips
                          [:div.section-settings-tooltip
                            "You can make changes to a section at any time."]
                          [:button.mlb-reset.section-settings-bt
                            {:on-click #(qsg-actions/dismiss-section-settings-tooltip)}
                            "OK, got it"]]])])]
                ; (when-not is-mobile?
                ;   (let [default-sort (= @board-sort :default)]
                ;     [:div.board-sort.group
                ;       {:ref :board-sort-menu}
                ;       [:button.mlb-reset.board-sort-bt
                ;         {:on-click #(swap! (::sorting-menu-expanded s) not)}
                ;         (if default-sort "Recent activity" "Recently posted")]
                ;       [:div.board-sort-menu
                ;         {:class (when @(::sorting-menu-expanded s) "show-menu")}
                ;         [:div.board-sort-menu-item
                ;           {:class (when default-sort "active")
                ;            :on-click #(reset! board-sort :defautl)}
                ;           "Recent activity"]
                ;         [:div.board-sort-menu-item
                ;           {:class (when-not default-sort "active")
                ;            :on-click #(reset! board-sort :own)}
                ;           "Recently posted"]]]))
                ]
              (let [add-post-tooltip (drv/react s :show-add-post-tooltip)
                    non-admin-tooltip (str "Carrot is where you'll find key announcements, updates, and "
                                           "decisions to keep you and your team pulling in the same direction.")
                    is-second-user (= add-post-tooltip :is-second-user)]
                (when (and (not is-drafts-board)
                           add-post-tooltip)
                  [:div.add-post-tooltip-container.group
                    [:button.mlb-reset.add-post-tooltip-dismiss
                      {:on-click #(do
                                    (nux-actions/dismiss-add-post-tooltip)
                                    (qsg-actions/turn-on-show-guide))}]
                    [:div.add-post-tooltips
                      {:class (when is-second-user "second-user")}
                      [:div.add-post-tooltip-box-mobile]
                      [:div.add-post-tooltip-title
                        (str "Welcome to Carrot, " (:first-name current-user-data) "!")]
                        [:div.add-post-tooltip
                          (if is-admin-or-author
                            (if is-second-user
                              non-admin-tooltip
                              "Create a test post now to see how it works. You can delete it anytime.")
                            non-admin-tooltip)
                          (when (and is-admin-or-author
                                     (not is-second-user))
                            [:button.mlb-reset.add-post-bt
                              {:on-click #(when can-compose (ui-compose @(drv/get-ref s :show-add-post-tooltip)))}
                              [:span.add-post-bt-pen]
                              "Create your first post"])]
                      [:div.add-post-tooltip-box
                        {:class (when is-second-user "second-user")}]]]))
              ;; Board content: empty org, all posts, empty board, drafts view, entries view
              (cond
                ;; No boards
                (zero? (count (:boards org-data)))
                (empty-org)
                ;; Empty board
                empty-board?
                (empty-board)
                ;; All Posts
                (and (or is-all-posts
                         is-must-see)
                     ;; Commenting out grid view switcher for now
                     ; (= @board-switch :stream)
                     )
                (rum/with-key (all-posts)
                 (str "all-posts-component-" (if is-all-posts "AP" "MS") "-" (drv/react s :ap-initial-at)))
                ;; Layout boards activities
                :else
                (cond
                  ;; Commenting out grid view switcher for now
                  ;; Entries grid view
                  ; (= @board-switch :grid)
                  ; (entries-layout)
                  ;; Entries stream view
                  :else
                  (section-stream)))])]]))