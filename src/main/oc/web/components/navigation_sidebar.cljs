(ns oc.web.components.navigation-sidebar
  (:require [rum.core :as rum]
            [oops.core :refer (ocall)]
            [org.martinklepsch.derivatives :as drv]
            [defun.core :refer (defun-)]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.cookies :as cook]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.router :as router]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.actions.search :as search-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.orgs-dropdown :refer (orgs-dropdown)])
  (:import [goog.async Throttle]))

(def drafts-board-prefix (-> utils/default-drafts-board :uuid (str "-")))

(defun- filter-sort-boards
  ([_ :guard (comp not seq)]
   [])
  ([boards :guard map?]
   (filter-sort-boards (vals boards)))
  ([boards :guard sequential?]
   (->> boards
        (filter #(and (not (:draft %))
                      (not= (:slug %) utils/default-drafts-board-slug)))
        (sort-by :name)
        vec)))

(def sidebar-top-margin 40)
(def sidebar-bottomm-margin 40)
(def navbar-height 56)

(defn fix-navbar-position [s]
  (when-let [navigation-sidebar (rum/ref-node s :left-navigation-sidebar-content)]
    (let [component-height (.-offsetHeight navigation-sidebar)
          component-total-height (+ navbar-height
                                    sidebar-top-margin
                                    sidebar-bottomm-margin
                                    component-height)
          taller? (> component-total-height
                     (dom-utils/viewport-height))]
      (compare-and-set! (::absolute-position s) (not taller?) taller?))))

(defn- toggle-collapse-sections [s]
  (let [next-value (not @(::sections-list-collapsed s))]
    (cook/set-cookie! (router/collapse-sections-list-cookie) next-value (* 60 60 24 365))
    (reset! (::sections-list-collapsed s) next-value)
    (utils/after 100 #(fix-navbar-position s))))

(defn- get-drafts-board [org-data]
  (some #(when (= (:slug %) utils/default-drafts-board-slug) %) (:boards org-data)))

(defn- drafts-link [org-data]
  (utils/link-for (-> org-data (get-drafts-board) :links) "self"))

(defn- show-drafts? [org-data]
  (and (:member? org-data)
       (drafts-link org-data)))

(defn- show-following? [org-data]
  (and (:member? org-data)
       (utils/link-for (:links org-data) "following")))

(defn- show-topics? [org-data]
  (:member? org-data))

(defn- show-bookmarks? [org-data]
  (and (:member? org-data)
       (utils/link-for (:links org-data) "bookmarks")))

(defn- show-boards? [s org-data]
  (or (utils/link-for (:links org-data) "create")
      (seq @(drv/get-ref s :follow-boards-list))))

(defn- show-replies? [org-data]
  (and (:member? org-data)
       (utils/link-for (:links org-data) "replies")))

(defn- show-profile? [s org-data]
  (or (:member? org-data)
      (seq @(drv/get-ref s :contributions-id))))

(defn- dispose-fn [throttled-fn]
  (when (fn? throttled-fn)
    (ocall throttled-fn "disposeInterval")))

(defn- dispose-fns-map [fns]
  (doseq [[k v] fns]
    (if (map? v)
      (dispose-fns-map v)
      (dispose-fn v))))

(defn- dispose-all-throttled-fns [s]
  (let [throttled-fns @(::throttled-fns s)]
    (dispose-fns-map throttled-fns)
    (reset! (::throttled-fns s) nil)))

(def throttle-ms (* 10 1000))

(defn- setup-throttle-fns [s]
  (dispose-all-throttled-fns s)
  (let [org-data @(drv/get-ref s :org-data)
        follow-boards-list @(drv/get-ref s :follow-boards-list)
        sorted-follow-boards (filter-sort-boards follow-boards-list)
        throttled-fns-map (cond-> {}
                            ;; Home
                            (show-following? org-data)
                            (assoc :home (Throttle. #(nav-actions/nav-to-url! % "following" (oc-urls/following)) throttle-ms))
                            ;; Topics
                            (show-topics? org-data)
                            (assoc :topics (Throttle. #(nav-actions/nav-to-url! % "topics" (oc-urls/topics)) throttle-ms))
                            ;; Replies
                            (show-replies? org-data)
                            (assoc :replies (Throttle. #(nav-actions/nav-to-url! % "replies" (oc-urls/replies)) throttle-ms))
                            ;; Profile
                            (show-profile? s org-data)
                            (assoc :profile (Throttle. #(nav-actions/nav-to-author! %1 %2 (oc-urls/contributions %2)) throttle-ms))
                            ;; Bookmarks
                            (show-bookmarks? org-data)
                            (assoc :bookmarks (Throttle. #(nav-actions/nav-to-url! % "bookmarks" (oc-urls/bookmarks)) throttle-ms))
                            ;; Drafts
                            (show-drafts? org-data)
                            (assoc :drafts (Throttle. #(nav-actions/nav-to-url! %1 %2 (oc-urls/board %2)) throttle-ms))
                            ;; Boards
                            (and (show-boards? s org-data)
                                 (seq sorted-follow-boards))
                            (assoc :boards (into {}
                                                 (map (fn [b]
                                                        (hash-map (:slug b)
                                                                  (Throttle. #(nav-actions/nav-to-url! % (:slug b) (oc-urls/board (:slug b)))
                                                                             throttle-ms)))
                                                      sorted-follow-boards))))]
    (reset! (::throttled-fns s)throttled-fns-map)))

(defn- home-clicked [s e]
  (dom-utils/prevent-default! e)
  (let [f (-> s ::throttled-fns deref :home)]
    (ocall f "fire" e)))

(defn- explore-clicked [s e]
  (dom-utils/prevent-default! e)
  (let [f (-> s ::throttled-fns deref :topics)]
    (ocall f "fire" e)))

(defn- activity-clicked [s e]
  (dom-utils/prevent-default! e)
  (let [f (-> s ::throttled-fns deref :replies)]
    (ocall f "fire" e)))

(defn- profile-clicked [s user-id e]
  (dom-utils/prevent-default! e)
  (let [f (-> s ::throttled-fns deref :profile)]
    (ocall f "fire" e user-id)))

(defn- bookmarks-clicked [s e]
  (dom-utils/prevent-default! e)
  (let [f (-> s ::throttled-fns deref :bookmarks)]
    (ocall f "fire" e)))

(defn- board-clicked [s board-slug e]
  (dom-utils/prevent-default! e)
  (when-let [f (-> s ::throttled-fns deref :board board-slug)]
    (ocall f "fire" e board-slug)))

(rum/defcs navigation-sidebar < rum/reactive
                                ;; Derivatives
                                (drv/drv :org-data)
                                (drv/drv :org-editing)
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
                                (drv/drv :follow-boards-list)
                                ; (drv/drv :show-add-post-tooltip)
                                (drv/drv :show-invite-box)
                                ;; Locals
                                (rum/local nil ::last-mobile-navigation-panel)
                                (rum/local true ::show-invite-people?)
                                (rum/local false ::absolute-position)
                                (rum/local false ::sections-list-collapsed)
                                (rum/local nil ::throttled-fns)
                                ;; Mixins
                                ui-mixins/first-render-mixin
                                (ui-mixins/render-on-resize fix-navbar-position)

                                {:did-mount (fn [s]
                                  (fix-navbar-position s)
                                  (setup-throttle-fns s)
                                  s)
                                 :will-mount (fn [s]
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
        org-editing (drv/react s :org-editing)
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
        follow-boards-list (drv/react s :follow-boards-list)
        sorted-follow-boards (filter-sort-boards follow-boards-list)
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
        show-boards (show-boards? s org-data)
        drafts-board (get-drafts-board org-data)
        drafts-link (utils/link-for (:links drafts-board) "self")
        show-following (show-following? org-data)
        show-bookmarks (show-bookmarks? org-data)
        show-drafts (show-drafts? org-data)
        show-replies (show-replies? org-data)
        show-profile (show-profile? s org-data)
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
        show-plus-button? (:can-compose? org-data)]
    [:div.left-navigation-sidebar.group
      {:class (utils/class-set {:mobile-show-side-panel (drv/react s :mobile-navigation-sidebar)
                                :absolute-position @(::absolute-position s)})
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
               :on-click (partial home-clicked s)}
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
               :on-click (partial explore-clicked s)}
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
               :on-click (partial activity-clicked s)}
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
                 :on-click (partial profile-clicked s (:user-id current-user-data))}
                [:div.nav-link-icon]
                [:div.nav-link-label
                  ; {:class (utils/class-set {:new (seq all-unread-items)})}
                  "Profile"]]]))
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
               :on-click (partial bookmarks-clicked s)}
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
                 :on-click (partial board-clicked s (:slug drafts-board))}
                [:div.nav-link-icon]
                [:div.nav-link-label.group
                  "Drafts"]
                (when (pos? draft-count)
                  [:span.count draft-count])]]))
        (when show-plus-button?
          [:div.left-navigation-sidebar-top.create-bt-container.top-border
            [:button.mlb-reset.create-bt
             {:on-click #(cmail-actions/cmail-fullscreen)
              :disabled (not (:collapsed cmail-state))}
             [:div.copy-text
              (:new-entry-cta org-editing)]]])
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
            (for [board sorted-follow-boards
                  :let [board-url (oc-urls/board org-slug (:slug board))
                        is-current-board (= current-board-slug (:slug board))
                        board-change-data (get change-data (:uuid board))]]
              [:a.left-navigation-sidebar-item.hover-item
                {:class (utils/class-set {:item-selected is-current-board})
                 :data-board (name (:slug board))
                 :key (str "board-list-" (name (:slug board)) "-" (rand 100))
                 :href board-url
                 :on-click (partial board-clicked s (:slug board))}
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
        (when show-invite-people?
          [:div.invite-people-box
            [:button.mlb-reset.invite-people-close
              {:on-click #(user-actions/dismiss-invite-box (:user-id current-user-data) true)}]
            [:label.explore-label
              (str "Explore " ls/product-name " together")]
            [:button.mlb-reset.invite-people-bt
              {:on-click #(nav-actions/show-org-settings :invite-picker)}
              "Invite teammates"]])]]))