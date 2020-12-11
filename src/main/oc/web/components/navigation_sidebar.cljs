(ns oc.web.components.navigation-sidebar
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.cookies :as cook]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.router :as router]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.actions.search :as search-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.orgs-dropdown :refer (orgs-dropdown)]))

(def drafts-board-prefix (-> utils/default-drafts-board :uuid (str "-")))

(defn sort-boards [boards]
  (vec (sort-by :name boards)))

(def sidebar-top-margin 56)

(defn save-content-height [s]
  (when-let [navigation-sidebar (rum/ref-node s :left-navigation-sidebar)]
    (let [height (.-offsetHeight navigation-sidebar)]
      (when (not= height @(::content-height s))
        (reset! (::content-height s) height)))))

(defn- toggle-collapse-sections [s]
  (let [next-value (not @(::sections-list-collapsed s))]
    (cook/set-cookie! (router/collapse-sections-list-cookie) next-value (* 60 60 24 365))
    (reset! (::sections-list-collapsed s) next-value)
    (reset! (::content-height s) nil)
    (utils/after 100 #(save-content-height s))))

(defn save-window-height
  "Save the window height in the local state."
  [s]
  (reset! (::window-height s) (.-innerHeight js/window)))

(rum/defcs navigation-sidebar < rum/reactive
                                ;; Derivatives
                                (drv/drv :org-data)
                                ; (drv/drv :board-data)
                                (drv/drv :org-slug)
                                (drv/drv :board-slug)
                                (drv/drv :contributions-id)
                                (drv/drv :change-data)
                                (drv/drv :current-user-data)
                                (drv/drv :replies-badge)
                                (drv/drv :following-badge)
                                (drv/drv :mobile-navigation-sidebar)
                                (drv/drv :drafts-data)
                                (drv/drv :cmail-state)
                                ; (drv/drv :show-add-post-tooltip)
                                (drv/drv :show-invite-box)
                                ;; Locals
                                (rum/local nil ::last-mobile-navigation-panel)
                                (rum/local true ::show-invite-people?)
                                (rum/local false ::window-height)
                                (rum/local false ::content-height)
                                (rum/local false ::sections-list-collapsed)
                                ;; Mixins
                                ui-mixins/first-render-mixin
                                (ui-mixins/render-on-resize save-window-height)

                                {:before-render (fn [s]
                                  (nux-actions/check-nux)
                                  s)
                                 :will-mount (fn [s]
                                  (save-window-height s)
                                  (save-content-height s)
                                  (reset! (::sections-list-collapsed s) (= (cook/get-cookie (router/collapse-sections-list-cookie)) "true"))
                                  s)
                                 :will-update (fn [s]
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
                                  s)}
  [s]
  (let [org-data (drv/react s :org-data)
        ; board-data (drv/react s :board-data)
        current-user-data (drv/react s :current-user-data)
        change-data (drv/react s :change-data)
        org-slug (drv/react s :org-slug)
        current-board-slug (drv/react s :board-slug)
        current-contributions-id (drv/react s :contributions-id)
        show-invite-box (drv/react s :show-invite-box)
        ; filtered-change-data (into {} (filter #(when-let [container-uuid (first %)]
        ;                                          (and (not (s/starts-with? container-uuid drafts-board-prefix))
        ;                                               (not (= container-uuid (:uuid org-data)))))
        ;                                       change-data))
        all-boards (:boards org-data)
        sorted-boards (sort-boards (filter #(not= (:slug %) utils/default-drafts-board-slug) all-boards))
        user-is-part-of-the-team? (:member? org-data)
        is-replies (= (keyword current-board-slug) :replies)
        is-following (= (keyword current-board-slug) :following)
        is-drafts-board (= current-board-slug utils/default-drafts-board-slug)
        is-topics (= (keyword current-board-slug) :topics)
        is-bookmarks (= (keyword current-board-slug) :bookmarks)
        is-contributions (seq current-contributions-id)
        is-self-profile? (and is-contributions
                              (= current-contributions-id (:user-id current-user-data)))
        create-link (utils/link-for (:links org-data) "create")
        ; show-boards (or create-link (pos? (count boards)))
        drafts-board (first (filter #(= (:slug %) utils/default-drafts-board-slug) all-boards))
        drafts-link (utils/link-for (:links drafts-board) "self")
        show-following (and user-is-part-of-the-team?
                            (utils/link-for (:links org-data) "following"))
        show-bookmarks (and user-is-part-of-the-team?
                            (utils/link-for (:links org-data) "bookmarks"))
        show-drafts (and user-is-part-of-the-team?
                         drafts-link)
        show-replies (and user-is-part-of-the-team?
                          (utils/link-for (:links org-data) "replies"))
        show-profile (or user-is-part-of-the-team?
                         is-contributions)
        is-mobile? (responsive/is-mobile-size?)
        drafts-data (drv/react s :drafts-data)
        ; all-unread-items (mapcat :unread (vals filtered-change-data))
        following-badge (drv/react s :following-badge)
        replies-badge (drv/react s :replies-badge)
        ; show-you (and user-is-part-of-the-team?
        ;               (pos? (:contributions-count org-data)))
        is-admin-or-author? (#{:admin :author} (:role current-user-data))
        show-invite-people? (and org-slug
                                 is-admin-or-author?
                                 show-invite-box)
        show-topics user-is-part-of-the-team?
        ; show-add-post-tooltip (drv/react s :show-add-post-tooltip)
        cmail-state (drv/react s :cmail-state)
        show-plus-button? (:can-compose? org-data)
        show-boards (and user-is-part-of-the-team?
                         (or create-link
                             (seq all-boards)))
        is-tall-enough? (not (neg? (- @(::window-height s) sidebar-top-margin @(::content-height s))))]
    [:div.left-navigation-sidebar.group
      {:class (utils/class-set {:mobile-show-side-panel (drv/react s :mobile-navigation-sidebar)
                                :absolute-position is-tall-enough?})
       :on-click #(when-not (utils/event-inside? % (rum/ref-node s :left-navigation-sidebar-content))
                    (dis/dispatch! [:input [:mobile-navigation-sidebar] false]))
       :ref :left-navigation-sidebar}
      [:div.left-navigation-sidebar-content
        {:ref :left-navigation-sidebar-content}
        (when is-mobile?
          [:div.left-navigation-sidebar-mobile-header
            [:button.mlb-reset.mobile-close-bt
              {:on-click #(dis/dispatch! [:input [:mobile-navigation-sidebar] false])}]
            [:button.mlb-reset.mobile-search-bt
              {:on-click #(do
                            (dis/dispatch! [:input [:mobile-navigation-sidebar] false])
                            (search-actions/active))}]
            (orgs-dropdown)])
        ;; All posts
        (when show-following
          [:div.left-navigation-sidebar-top
            {:class "home-nav"} ; for intercom targetting
            [:a.nav-link.home.hover-item.group
              {:class (utils/class-set {:item-selected is-following
                                        :new following-badge})
               :href (oc-urls/following)
               :on-click #(nav-actions/nav-to-url! % "following" (oc-urls/following))}
              [:div.nav-link-icon]
              [:div.nav-link-label
                ; {:class (utils/class-set {:new (seq all-unread-items)})}
                "Home"]
              (when following-badge
                ; [:span.count (count all-unread-items)]
                [:span.unread-dot])]])
        (when show-topics
          [:div.left-navigation-sidebar-top
            {:class "explore-nav"} ; for intercom targetting
            [:a.nav-link.topics.hover-item.group
              {:class (utils/class-set {:item-selected is-topics})
               :href (oc-urls/unfollowing)
               :on-click #(nav-actions/nav-to-url! % "topics" (oc-urls/topics))}
              [:div.nav-link-icon]
              [:div.nav-link-label
                ; {:class (utils/class-set {:new (seq all-unread-items)})}
                "Explore"]
              ; (when (pos? (count all-unread-items))
              ;   [:span.count (count all-unread-items)])
              ]])
        (when show-replies
          [:div.left-navigation-sidebar-top
            {:class (when (or show-following show-topics)
                      "top-border")}
            [:a.nav-link.replies-view.hover-item.group
              {:class (utils/class-set {:item-selected is-replies
                                        :new replies-badge})
               :href (oc-urls/replies)
               :on-click (fn [e]
                           (utils/event-stop e)
                           (nav-actions/nav-to-url! e "replies" (oc-urls/replies)))}
              [:div.nav-link-icon]
              [:div.nav-link-label
                ; {:class (utils/class-set {:new (seq all-unread-items)})}
                "Activity"]
                (when replies-badge
                  [:span.unread-dot])]])
        (when show-profile
          (let [contrib-user-id (if is-contributions current-contributions-id (:user-id current-user-data))]
            [:div.left-navigation-sidebar-top
              {:class (when (and (or show-following show-topics)
                                 (not show-replies))
                        "top-border")}
              [:a.nav-link.profile.hover-item.group
                {:class (utils/class-set {:item-selected is-contributions})
                 :href (oc-urls/contributions (:user-id current-user-data))
                 :on-click (fn [e]
                             (utils/event-stop e)
                             (nav-actions/nav-to-author! e (:user-id current-user-data) (oc-urls/contributions (:user-id current-user-data))))}
                [:div.nav-link-icon]
                [:div.nav-link-label
                  ; {:class (utils/class-set {:new (seq all-unread-items)})}
                  "Profile"]]]))
        ;; You
        ; (when show-you
        ;   [:div.left-navigation-sidebar-top.top-border
        ;     [:a.nav-link.my-posts.hover-item.group
        ;       {:class (utils/class-set {:item-selected is-my-posts})
        ;        :href (oc-urls/contributions (:user-id current-user-data))
        ;        :on-click (fn [e]
        ;                    (utils/event-stop e)
        ;                    (nav-actions/nav-to-author! e (:user-id current-user-data) (oc-urls/contributions (:user-id current-user-data))))}
        ;       [:div.nav-link-icon]
        ;       [:div.nav-link-label
        ;         ; {:class (utils/class-set {:new (seq all-unread-items)})}
        ;         "You"]
        ;         ; (when (pos? (:contributions-count org-data))
        ;         ;   [:span.count (:contributions-count org-data)])
        ;         ]])
        ;; Bookmarks
        (when show-bookmarks
          [:div.left-navigation-sidebar-top
            {:class (when (and (or show-following show-topics show-replies)
                               (not show-replies)
                               (not show-profile))
                        "top-border")}
            [:a.nav-link.bookmarks.hover-item.group
              {:class (utils/class-set {:item-selected is-bookmarks})
               :href (oc-urls/bookmarks)
               :on-click #(nav-actions/nav-to-url! % "bookmarks" (oc-urls/bookmarks))}
              [:div.nav-link-icon]
              [:div.nav-link-label
                "Bookmarks"]
              (when (pos? (:bookmarks-count org-data))
                [:span.count (:bookmarks-count org-data)])]])
        ;; Drafts
        (when show-drafts
          (let [board-url (oc-urls/board (:slug drafts-board))
                draft-count (if drafts-data (count (:posts-list drafts-data)) (:count drafts-link))]
            [:div.left-navigation-sidebar-top
              {:class (when (and (or show-following show-topics)
                                 (not show-replies)
                                 (not show-profile)
                                 (not show-bookmarks))
                        "top-border")}
              [:a.nav-link.drafts.hover-item.group
                {:class (utils/class-set {:item-selected (and (not is-following)
                                                              (not is-topics)
                                                              (not is-bookmarks)
                                                              (= current-board-slug (:slug drafts-board)))})
                 :data-board (name (:slug drafts-board))
                 :key (str "board-list-" (name (:slug drafts-board)))
                 :href board-url
                 :on-click #(nav-actions/nav-to-url! % (:slug drafts-board) board-url)}
                [:div.nav-link-icon]
                [:div.nav-link-label.group
                  "Drafts"]
                (when (pos? draft-count)
                  [:span.count draft-count])]]))
        ;; Boards list
        (when show-boards
          [:div.left-navigation-sidebar-top.top-border.group
            ;; Boards header
           [:h3.left-navigation-sidebar-top-title.group
            [:button.mlb-reset.left-navigation-sidebar-sections-arrow
             {:class (when @(::sections-list-collapsed s) "collapsed")
              :on-click #(toggle-collapse-sections s)}
             [:span.sections "Topics"]]
            (when create-link
              [:button.left-navigation-sidebar-top-title-button.btn-reset
               {:on-click #(nav-actions/show-section-add)
                :title "Create a new section"
                :data-placement "top"
                :data-toggle (when-not is-mobile? "tooltip")
                :data-container "body"}])]])
        (when (and show-boards
                   (not @(::sections-list-collapsed s)))
          [:div.left-navigation-sidebar-items.group
            (for [board sorted-boards
                  :let [board-url (oc-urls/board org-slug (:slug board))
                        is-current-board (and (not is-following)
                                              (not is-replies)
                                              (not is-bookmarks)
                                              (not is-drafts-board)
                                              (not is-contributions)
                                              (not is-self-profile?)
                                              (not is-topics)
                                              (= current-board-slug (:slug board)))
                        board-change-data (get change-data (:uuid board))]]
              [:a.left-navigation-sidebar-item.hover-item
                {:class (utils/class-set {:item-selected is-current-board})
                 :data-board (name (:slug board))
                 :key (str "board-list-" (name (:slug board)) "-" (rand 100))
                 :href board-url
                 :on-click #(do
                              (nav-actions/nav-to-url! % (:slug board) board-url))}
                [:div.board-name.group
                  {:class (utils/class-set {:public-board (= (:access board) "public")
                                            :private-board (= (:access board) "private")
                                            :team-board (= (:access board) "team")})}
                  [:div.internal
                    {:class (utils/class-set {:new (seq (:unread board-change-data))
                                              :has-icon (#{"public" "private"} (:access board))})
                     :key (str "board-list-" (name (:slug board)) "-internal")
                     :dangerouslySetInnerHTML (utils/emojify (or (:name board) (:slug board)))}]]
                (when (= (:access board) "public")
                  [:div.public])
                (when (= (:access board) "private")
                  [:div.private])])])
        (when show-plus-button?
          [:button.mlb-reset.create-bt
            {:on-click #(cmail-actions/cmail-fullscreen)
             :disabled (not (:collapsed cmail-state))}
            ; [:span.plus-icon]
            [:span.copy-text
              "New update"]])
        (when show-invite-people?
          [:div.invite-people-box
            [:button.mlb-reset.invite-people-close
              {:on-click #(user-actions/dismiss-invite-box (:user-id current-user-data) true)}]
            [:label.explore-label
              (str "Explore " ls/product-name " together")]
            [:button.mlb-reset.invite-people-bt
              {:on-click #(nav-actions/show-org-settings :invite-picker)}
              "Invite teammates"]])]]))