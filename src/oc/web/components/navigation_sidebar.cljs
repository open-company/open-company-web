(ns oc.web.components.navigation-sidebar
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.lib.user :as lib-user]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.lib.user :as user-lib]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.utils.user :as user-utils]
            [oc.web.stores.user :as user-store]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.components.ui.menu :as menu]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.trial-expired-banner :refer (trial-expired-alert)]
            [oc.web.components.ui.orgs-dropdown :refer (orgs-dropdown)]))

(defn sort-boards [boards]
  (vec (sort-by :name boards)))

(def sidebar-top-margin 56)

(defn save-content-height [s]
  (when-let [navigation-sidebar (rum/ref-node s :left-navigation-sidebar)]
    (let [height (.-offsetHeight navigation-sidebar)]
      (when (not= height @(::content-height s))
        (reset! (::content-height s) height)))))

(defn- toggle-collapse-boards [s & [force-show]]
  (let [next-value (not @(::boards-list-collapsed s))]
    (when (or (not force-show)
              (and force-show
                   (not next-value)))
      (cook/set-cookie! (router/collapse-boards-list-cookie) next-value (* 60 60 24 365))
      (reset! (::boards-list-collapsed s) next-value)
      (reset! (::content-height s) nil)
      (utils/after 100 #(save-content-height s)))))

(defn- toggle-collapse-users [s & [force-show]]
  (let [next-value (not @(::users-list-collapsed s))]
    (when (or (not force-show)
              (and force-show
                   (not next-value)))
      (cook/set-cookie! (router/collapse-users-list-cookie) next-value (* 60 60 24 365))
      (reset! (::users-list-collapsed s) next-value)
      (reset! (::content-height s) nil)
      (utils/after 100 #(save-content-height s)))))

(defn filter-board [board-data]
  (let [self-link (utils/link-for (:links board-data) "self")]
    (and ;; Filter out the draft board
         (not= (:slug board-data) utils/default-drafts-board-slug)
         ;; Filter every publisher board used for `post as myself`
         (not (:publisher-board board-data))
         ;; Filter out boards with a count of posts equal to zero (?)
         (or (not (contains? self-link :count))
             (and (contains? self-link :count)
                  (pos? (:count self-link))))
         ;; Filter out draft boards (marked for delete)
         (or (not (contains? board-data :draft))
             (not (:draft board-data))))))

(defn filter-boards [all-boards]
  (filterv filter-board all-boards))

(defn save-window-size
  "Save the window height in the local state."
  [s]
  (reset! (::window-height s) (.-innerHeight js/window))
  (reset! (::window-width s) (.-innerWidth js/window)))

(def drafts-board-prefix (-> utils/default-drafts-board :uuid (str "-")))

(defn- check-and-reopen-follow-lists [s v]
  (let [local-follow-list-last-added @(::follow-list-last-added s)
        follow-list-last-added @(drv/get-ref s :follow-list-last-added)]
    (when (not= local-follow-list-last-added follow-list-last-added)
      (let [changed (atom #{})]
        (doseq [[k v] follow-list-last-added]
          (when (not= v (get local-follow-list-last-added k))
            (swap! changed conj k)))
        (when (@changed :user)
          (toggle-collapse-users s true))
        (when (@changed :board)
          (toggle-collapse-boards s true))
        (reset! (::follow-list-last-added s) follow-list-last-added)))))

(rum/defcs navigation-sidebar < rum/reactive
                                ;; Derivatives
                                (drv/drv :org-data)
                                (drv/drv :board-data)
                                ; (drv/drv :change-data)
                                (drv/drv :activity-view)
                                (drv/drv :unread-notifications-count)
                                (drv/drv :current-user-data)
                                (drv/drv :mobile-navigation-sidebar)
                                (drv/drv :drafts-data)
                                (drv/drv :follow-publishers-list)
                                (drv/drv :follow-boards-list)
                                (drv/drv :follow-list-last-added)
                                ;; Locals
                                (rum/local false ::content-height)
                                (rum/local nil ::window-height)
                                (rum/local nil ::window-width)
                                (rum/local nil ::last-mobile-navigation-panel)
                                (rum/local nil ::boards-list-collapsed)
                                (rum/local nil ::users-list-collapsed)
                                (rum/local nil ::last-reopen-list)
                                (rum/local nil ::follow-list-last-added)
                                ;; Mixins
                                ui-mixins/first-render-mixin
                                (ui-mixins/render-on-resize save-window-size)

                                {:will-mount (fn [s]
                                  (save-window-size s)
                                  (save-content-height s)
                                  (reset! (::boards-list-collapsed s) (not= (cook/get-cookie (router/collapse-boards-list-cookie)) "false"))
                                  (reset! (::users-list-collapsed s) (= (cook/get-cookie (router/collapse-users-list-cookie)) "true"))
                                  s)
                                 :before-render (fn [s]
                                  (nux-actions/check-nux)
                                  s)
                                 :did-mount (fn [s]
                                  (save-content-height s)
                                  s)
                                 :did-remount (fn [o s]
                                  (check-and-reopen-follow-lists s "did-remount")
                                  s)
                                 :will-update (fn [s]
                                  (save-content-height s)
                                  (when (responsive/is-mobile-size?)
                                    (let [mobile-navigation-panel (boolean @(drv/get-ref s :mobile-navigation-sidebar))
                                          last-mobile-navigation-panel (boolean @(::last-mobile-navigation-panel s))]
                                      (when (not= mobile-navigation-panel last-mobile-navigation-panel)
                                        (if mobile-navigation-panel
                                          ;; Will open panel, let's block page scroll
                                          (do
                                            (dom-utils/lock-page-scroll)
                                            (reset! (::last-mobile-navigation-panel s) true))
                                          ;; Will close panel, let's unblock page scroll
                                          (do
                                            (dom-utils/unlock-page-scroll)
                                            (reset! (::last-mobile-navigation-panel s) false))))))
                                  (check-and-reopen-follow-lists s "will-update")
                                  s)}
  [s]
  (let [org-data (drv/react s :org-data)
        board-data (drv/react s :board-data)
        current-user-data (drv/react s :current-user-data)
        ; change-data (drv/react s :change-data)
        ; filtered-change-data (into {} (filter #(and (-> % first (s/starts-with? drafts-board-prefix) not)
        ;                                             (not= % (:uuid org-data))) change-data))
        left-navigation-sidebar-width (- responsive/left-navigation-sidebar-width 20)
        all-boards (:boards org-data)
        boards (filter-boards all-boards)
        sorted-boards (sort-boards boards)
        selected-slug (or (:board (:back-to @router/path)) (router/current-board-slug))
        activity-view (drv/react s :activity-view)
        is-following (and (not activity-view)
                          (= selected-slug "following"))
        is-unfollowing (and (not activity-view)
                            (= selected-slug "unfollowing"))
        is-home (and (not activity-view)
                     (= selected-slug "all-posts"))
        is-bookmarks (and (not activity-view)
                          (= selected-slug "bookmarks"))
        is-drafts-board (and (not activity-view)
                             (= selected-slug utils/default-drafts-board-slug))
        create-link (utils/link-for (:links org-data) "create")
        show-boards (or create-link (pos? (count boards)))
        user-is-part-of-the-team? (jwt/user-is-part-of-the-team (:team-id org-data))
        drafts-board (first (filter #(= (:slug %) utils/default-drafts-board-slug) all-boards))
        drafts-link (utils/link-for (:links drafts-board) "self")
        show-following (and user-is-part-of-the-team?
                            (utils/link-for (:links org-data) "following"))
        show-unfollowing (and user-is-part-of-the-team?
                              (utils/link-for (:links org-data) "unfollowing"))
        show-all-posts false ;(and user-is-part-of-the-team?
                             ;     (utils/link-for (:links org-data) "entries"))
        show-bookmarks (and user-is-part-of-the-team?
                            (utils/link-for (:links org-data) "bookmarks")
                            (integer? (:bookmarks-count org-data)))
        show-drafts (and user-is-part-of-the-team?
                         drafts-link
                         (integer? (:drafts-count org-data)))
        org-slug (router/current-org-slug)
        is-mobile? (responsive/is-mobile-size?)
        is-tall-enough? (not (neg? (- @(::window-height s) sidebar-top-margin @(::content-height s))))
        drafts-data (drv/react s :drafts-data)
        ; all-unread-items (mapcat :unread (vals filtered-change-data))
        follow-publishers-list (drv/react s :follow-publishers-list)
        show-users-list? (and user-is-part-of-the-team?
                              follow-publishers-list
                              (pos? (count follow-publishers-list)))
        follow-boards-list (drv/react s :follow-boards-list)
        unread-notifications-count (drv/react s :unread-notifications-count)]
    [:div.left-navigation-sidebar.group
      {:class (utils/class-set {:mobile-show-side-panel (drv/react s :mobile-navigation-sidebar)
                                :absolute-position (not is-tall-enough?)
                                :collapsed-boards @(::boards-list-collapsed s)
                                :collapsed-users @(::users-list-collapsed s)})
       :on-click #(when-not (utils/event-inside? % (rum/ref-node s :left-navigation-sidebar-content))
                    (dis/dispatch! [:input [:mobile-navigation-sidebar] false]))
       :ref :left-navigation-sidebar}
      [:div.left-navigation-sidebar-content
        {:ref :left-navigation-sidebar-content}
        (when is-mobile?
          [:div.left-navigation-sidebar-mobile-header
            [:button.mlb-reset.mobile-close-bt
              {:on-click #(dis/dispatch! [:input [:mobile-navigation-sidebar] false])}]
            (orgs-dropdown)])
        ;; All posts
        (when show-following
          [:a.nav-link.home.hover-item.group
            {:class (utils/class-set {:item-selected is-following})
             :href (oc-urls/following)
             :on-click #(nav-actions/nav-to-url! % "following" (oc-urls/following))}
            [:div.nav-link-icon]
            [:div.nav-link-label
              ; {:class (utils/class-set {:new (seq all-unread-items)})}
              "Home"]
            ; (when (pos? (count all-unread-items))
            ;   [:span.count (count all-unread-items)])
            (when user-is-part-of-the-team?
              [:button.mlb-reset.home-ellipsis-bt
                {:title "Curate your Home feed"
                 :data-placement "top"
                 :data-container "body"
                 :data-toggle (when-not is-mobile? "tooltip")
                 :on-click #(nav-actions/show-follow-board-picker)}])])
        (when show-unfollowing
          [:a.nav-link.all-posts.hover-item.group
            {:class (utils/class-set {:item-selected is-unfollowing})
             :href (oc-urls/unfollowing)
             :on-click #(nav-actions/nav-to-url! % "unfollowing" (oc-urls/unfollowing))}
            ; [:div.explore-icon]
            [:div.nav-link-icon]
            [:div.nav-link-label
              ; {:class (utils/class-set {:new (seq all-unread-items)})}
              "Explore"]
            ; (when (pos? (count all-unread-items))
            ;   [:span.count (count all-unread-items)])
            ])
        (when user-is-part-of-the-team?
          [:a.nav-link.activity-view.hover-item.group
            {:class (utils/class-set {:item-selected activity-view
                                      :new (pos? unread-notifications-count)})
             :href "."
             :on-click (fn [e]
                         (utils/event-stop e)
                         (user-actions/show-activity-view))}
            ; [:div.explore-icon]
            [:div.nav-link-icon]
            [:div.nav-link-label
              ; {:class (utils/class-set {:new (seq all-unread-items)})}
              "Activity"]
            (when (pos? unread-notifications-count)
              [:span.count unread-notifications-count])
            ])
        ; ;; All posts
        ; (when show-all-posts
        ;   [:a.nav-link.all-posts.hover-item.group
        ;     {:class (utils/class-set {:item-selected is-home})
        ;      :href (oc-urls/all-posts)
        ;      :on-click #(nav-actions/nav-to-url! % "all-posts" (oc-urls/all-posts))}
        ;     [:div.nav-link-icon]
        ;     [:div.nav-link-label
        ;       ; {:class (utils/class-set {:new (seq all-unread-items)})}
        ;       "All"]
        ;     ; (when (pos? (count all-unread-items))
        ;     ;   [:span.count (count all-unread-items)])
        ;     ])
        ;; Bookmarks
        (when show-bookmarks
          [:a.nav-link.bookmarks.hover-item.group
            {:class (utils/class-set {:item-selected is-bookmarks})
             :href (oc-urls/bookmarks)
             :on-click #(nav-actions/nav-to-url! % "bookmarks" (oc-urls/bookmarks))}
            [:div.nav-link-icon]
            [:div.nav-link-label
              "Bookmarks"]
            (when (pos? (:bookmarks-count org-data))
              [:span.count (:bookmarks-count org-data)])])
        ;; Drafts
        (when show-drafts
          (let [board-url (oc-urls/board (:slug drafts-board))
                draft-count (if drafts-data (count (:posts-list drafts-data)) (:count drafts-link))]
            [:a.nav-link.drafts.hover-item.group
              {:class (when (and (not is-following)
                                 (not is-unfollowing)
                                 (not is-home)
                                 (not is-bookmarks)
                                 (= (router/current-board-slug) (:slug drafts-board)))
                        "item-selected")
               :data-board (name (:slug drafts-board))
               :key (str "board-list-" (name (:slug drafts-board)))
               :href board-url
               :on-click #(nav-actions/nav-to-url! % (:slug drafts-board) board-url)}
              [:div.nav-link-icon]
              [:div.nav-link-label.group
                "Drafts "]
              (when (pos? draft-count)
                [:span.count draft-count])]))
        (when show-users-list?
          [:div.left-navigation-sidebar-top.top-border.group
            ;; Boards header
            [:h3.left-navigation-sidebar-top-title.group
              [:button.mlb-reset.left-navigation-sidebar-title-arrow
                {:class (utils/class-set {:collapsed @(::users-list-collapsed s)})
                 :on-click #(toggle-collapse-users s)}]
              (let [; user-ids (map :user-id follow-publishers-list)
                    ; publisher-boards-change-data (map (partial get change-data) user-ids)
                    ]
                [:button.mlb-reset.left-navigation-sidebar-title
                  {; :class (utils/class-set {:new (and @(::users-list-collapsed s)
                   ;                                    (seq (mapcat :unread publisher-boards-change-data)))})
                   :on-click #(toggle-collapse-users s)
                   :title "People you follow"
                   :data-placement "top"
                   :data-toggle (when-not is-mobile? "tooltip")}
                  [:span.boards "Favorites"]])
              [:button.left-navigation-sidebar-top-ellipsis-bt.btn-reset
                {:on-click #(nav-actions/show-follow-user-picker)
                 :title "People directory"
                 :data-placement "top"
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-delay "{\"show\":\"800\", \"hide\":\"0\"}"
                 :data-container "body"}]]])
        (when show-users-list?
          [:div.left-navigation-sidebar-items.group
            (for [user follow-publishers-list
                  :let [user-url (oc-urls/contributions org-slug (:user-id user))
                        is-current-user (and (router/current-contributions-id)
                                             (= (:user-id user) (router/current-contributions-id)))
                        ; board (some #(when (and (:publisher-board %)
                        ;                         (= (-> % :author :user-id) (:user-id user)))
                        ;                %)
                        ;        all-boards)
                        ; board-change-data (when board (get change-data (:uuid board)))
                        ]
                  :when (or (not @(::users-list-collapsed s))
                            is-current-user)]
              [:a.left-navigation-sidebar-item.hover-item.contributions
                {:class (utils/class-set {:item-selected is-current-user})
                 :data-user-id (:user-id user)
                 :key (str "user-list-" (:user-id user))
                 :href user-url
                 :on-click #(nav-actions/nav-to-author! % (:user-id user) user-url)}
                [:div.board-name.group
                  (user-avatar-image user)
                  [:div.internal
                    ; {:class (when (seq (:unread board-change-data)) "new")}
                    (:short-name user)]]])])
        ;; Boards list
        (when show-boards
          [:div.left-navigation-sidebar-top.top-border.group
            ;; Boards header
            [:h3.left-navigation-sidebar-top-title.group
              [:button.mlb-reset.left-navigation-sidebar-title-arrow
                {:class (utils/class-set {:collapsed @(::boards-list-collapsed s)})
                 :on-click #(toggle-collapse-boards s)}]
              (let [; follow-board-uuids (map :uuid follow-boards-list)
                    ; boards-change-data (map (partial get change-data) follow-board-uuids)
                    ]
                [:button.mlb-reset.left-navigation-sidebar-title
                  {; :class (utils/class-set {:new (and @(::boards-list-collapsed s)
                   ;                                    (seq (mapcat :unread boards-change-data)))})
                   :on-click #(toggle-collapse-boards s)
                   :title "Teams you follow"
                   :data-placement "top"
                   :data-toggle (when-not is-mobile? "tooltip")}
                  [:span.boards "Feeds"]])
              [:button.left-navigation-sidebar-top-title-button.btn-reset
                {:on-click #(nav-actions/show-section-add)
                 :title "New team"
                 :data-placement "top"
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-delay "{\"show\":\"800\", \"hide\":\"0\"}"
                 :data-container "body"}]
              [:button.left-navigation-sidebar-top-ellipsis-bt.people-ellipsis-bt.btn-reset
                {:on-click #(nav-actions/show-follow-board-picker)
                 :title "Team directory"
                 :data-placement "top"
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-delay "{\"show\":\"800\", \"hide\":\"0\"}"
                 :data-container "body"}]]])
        (when (seq follow-boards-list)
          [:div.left-navigation-sidebar-items.group
            (for [board follow-boards-list
                  :let [board-url (oc-urls/board org-slug (:slug board))
                        is-current-board (and (not is-following)
                                              (not is-unfollowing)
                                              (not is-home)
                                              (not is-bookmarks)
                                              (= selected-slug (:slug board)))
                        ; board-change-data (get change-data (:uuid board))
                        ]
                  :when (or (not @(::boards-list-collapsed s))
                            is-current-board)]
              [:a.left-navigation-sidebar-item.hover-item
                {:class (utils/class-set {:item-selected is-current-board})
                 :data-board (name (:slug board))
                 :key (str "board-list-" (name (:slug board)))
                 :href board-url
                 :on-click #(do
                              (nav-actions/nav-to-url! % (:slug board) board-url))}
                [:div.board-name.group
                  {:class (utils/class-set {:public-board (= (:access board) "public")
                                            :private-board (= (:access board) "private")
                                            :team-board (= (:access board) "team")})}
                  [:div.internal
                    {:class (utils/class-set {; :new (seq (:unread board-change-data))
                                              :has-icon (#{"public" "private"} (:access board))})
                     :key (str "board-list-" (name (:slug board)) "-internal")
                     :dangerouslySetInnerHTML (utils/emojify (or (:name board) (:slug board)))}]]
                (when (= (:access board) "team")
                  [:div.team])
                (when (= (:access board) "public")
                  [:div.public])
                (when (= (:access board) "private")
                  [:div.private])])])]]))