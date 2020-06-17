(ns oc.web.components.navigation-sidebar
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.lib.user :as lib-user]
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

(def sidebar-top-margin 56)

(defn save-content-height [s]
  (when-let [navigation-sidebar (rum/ref-node s :left-navigation-sidebar)]
    (let [height (.-offsetHeight navigation-sidebar)]
      (when (not= height @(::content-height s))
        (reset! (::content-height s) height)))))

(defn save-window-size
  "Save the window height in the local state."
  [s]
  (reset! (::window-height s) (.-innerHeight js/window))
  (reset! (::window-width s) (.-innerWidth js/window)))

(def drafts-board-prefix (-> utils/default-drafts-board :uuid (str "-")))

(rum/defcs navigation-sidebar < rum/reactive
                                ;; Derivatives
                                (drv/drv :org-data)
                                (drv/drv :board-data)
                                (drv/drv :change-data)
                                (drv/drv :current-user-data)
                                (drv/drv :unread-replies)
                                (drv/drv :mobile-navigation-sidebar)
                                (drv/drv :drafts-data)
                                ;; Locals
                                (rum/local false ::content-height)
                                (rum/local nil ::window-height)
                                (rum/local nil ::window-width)
                                (rum/local nil ::last-mobile-navigation-panel)
                                ;; Mixins
                                ui-mixins/first-render-mixin
                                (ui-mixins/render-on-resize save-window-size)

                                {:will-mount (fn [s]
                                  (save-window-size s)
                                  (save-content-height s)
                                  s)
                                 :before-render (fn [s]
                                  (nux-actions/check-nux)
                                  s)
                                 :did-mount (fn [s]
                                  (save-content-height s)
                                  s)
                                 :did-remount (fn [o s]
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
                                  s)
                                 :did-update (fn [s]
                                  (save-content-height s)
                                  s)}
  [s]
  (let [org-data (drv/react s :org-data)
        board-data (drv/react s :board-data)
        current-user-data (drv/react s :current-user-data)
        change-data (drv/react s :change-data)
        filtered-change-data (into {} (filter #(and (-> % first (s/starts-with? drafts-board-prefix) not)
                                                    (not= % (:uuid org-data))) change-data))
        left-navigation-sidebar-width (- responsive/left-navigation-sidebar-width 20)
        all-boards (:boards org-data)
        selected-slug (or (:board (:back-to @router/path)) (router/current-board-slug))
        user-is-part-of-the-team? (:member? org-data)
        is-replies (or (:replies (:back-to @router/path))
                        (= selected-slug "replies"))
        is-following (or (:following (:back-to @router/path))
                         (= selected-slug "following"))
        is-drafts-board (= selected-slug utils/default-drafts-board-slug)
        is-topics (or (:topics (:back-to @router/path))
                       (= selected-slug "topics")
                       (and (seq (router/current-board-slug))
                            (not (dis/is-container? (router/current-board-slug)))
                            (not is-drafts-board)))
        ; is-my-posts (and user-is-part-of-the-team?
        ;                  (or (= (:contributions (:back-to @router/path)) (:user-id current-user-data))
        ;                      (= (router/current-contributions-id) (:user-id current-user-data))))
        is-bookmarks (= selected-slug "bookmarks")
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
        org-slug (router/current-org-slug)
        is-mobile? (responsive/is-mobile-size?)
        is-tall-enough? (not (neg? (- @(::window-height s) sidebar-top-margin @(::content-height s))))
        drafts-data (drv/react s :drafts-data)
        all-unread-items (mapcat :unread (vals filtered-change-data))
        unread-replies (drv/react s :unread-replies)
        ; show-you (and user-is-part-of-the-team?
        ;               (pos? (:contributions-count org-data)))
        user-role (user-store/user-role org-data current-user-data)
        is-admin-or-author? (#{:admin :author} user-role)
        show-invite-people? (and org-slug
                                 is-admin-or-author?)
        show-topics user-is-part-of-the-team?]
    [:div.left-navigation-sidebar.group
      {:class (utils/class-set {:mobile-show-side-panel (drv/react s :mobile-navigation-sidebar)
                                :absolute-position (not is-tall-enough?)})
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
            (when (pos? (count all-unread-items))
              ; [:span.count (count all-unread-items)]
              [:span.unread-dot])])
        (when show-topics
          [:div.left-navigation-sidebar-top
            [:a.nav-link.topics.hover-item.group
              {:class (utils/class-set {:item-selected is-topics})
               :href (oc-urls/unfollowing)
               :on-click #(nav-actions/nav-to-url! % "topics" (oc-urls/topics))}
              [:div.nav-link-icon]
              [:div.nav-link-label
                ; {:class (utils/class-set {:new (seq all-unread-items)})}
                "Topics"]
              ; (when (pos? (count all-unread-items))
              ;   [:span.count (count all-unread-items)])
              ]])
        (when show-replies
          [:div.left-navigation-sidebar-top
            {:class (when (or show-following show-topics)
                      "top-border")}
            [:a.nav-link.replies-view.hover-item.group
              {:class (utils/class-set {:item-selected is-replies
                                        :new unread-replies})
               :href (oc-urls/replies)
               :on-click (fn [e]
                           (utils/event-stop e)
                           (nav-actions/nav-to-url! e "replies" (oc-urls/replies)))}
              [:div.nav-link-icon]
              [:div.nav-link-label
                ; {:class (utils/class-set {:new (seq all-unread-items)})}
                "Replies"]
                (when (pos? unread-replies)
                  [:span.unread-dot])]])
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
        ;; Drafts
        (when show-drafts
          (let [board-url (oc-urls/board (:slug drafts-board))
                draft-count (if drafts-data (count (:posts-list drafts-data)) (:count drafts-link))]
            [:div.left-navigation-sidebar-top
              {:class (when (and (or show-following show-topics)
                                 (not show-replies))
                        "top-border")}
              [:a.nav-link.drafts.hover-item.group
                {:class (utils/class-set {:item-selected (and (not is-following)
                                                              (not is-topics)
                                                              (not is-bookmarks)
                                                              (= (router/current-board-slug) (:slug drafts-board)))})
                 :data-board (name (:slug drafts-board))
                 :key (str "board-list-" (name (:slug drafts-board)))
                 :href board-url
                 :on-click #(nav-actions/nav-to-url! % (:slug drafts-board) board-url)}
                [:div.nav-link-icon]
                [:div.nav-link-label.group
                  "Drafts"]
                (when (pos? draft-count)
                  [:span.count draft-count])]]))
        ;; Bookmarks
        (when show-bookmarks
          [:div.left-navigation-sidebar-top
            {:class (when (and (or show-following show-topics)
                               (not show-replies)
                               (not show-drafts))
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
        (when show-invite-people?
          [:div.left-navigation-sidebar-footer.top-border
            [:button.mlb-reset.invite-people-bt
              {:on-click #(nav-actions/show-org-settings :invite-picker)}
              "Invite your team"]])]]))