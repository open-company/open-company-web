(ns oc.web.components.navigation-sidebar
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.components.ui.menu :as menu]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.orgs-dropdown :refer (orgs-dropdown)]))

(defn sort-boards [boards]
  (vec (sort-by :name boards)))

(def sidebar-top-margin 90)

(defn save-content-height [s]
  (when-let [navigation-sidebar-content (rum/ref-node s "left-navigation-sidebar-content")]
    (let [height (+ (.height (js/$ navigation-sidebar-content)) 32)]
      (when (not= height @(::content-height s))
        (reset! (::content-height s) height))))
  (when-let [navigation-sidebar-footer (rum/ref-node s "left-navigation-sidebar-footer")]
    (let [footer-height (+ (.height (js/$ navigation-sidebar-footer)) 8)]
      (when (not= footer-height @(::footer-height s))
        (reset! (::footer-height s) footer-height)))))

(defn filter-board [board-data]
  (let [self-link (utils/link-for (:links board-data) "self")]
    (and (not= (:slug board-data) utils/default-drafts-board-slug)
         (or (not (contains? self-link :count))
             (and (contains? self-link :count)
                  (pos? (:count self-link))))
         (or (not (contains? board-data :draft))
             (not (:draft board-data))))))

(defn filter-boards [all-boards]
  (filterv filter-board all-boards))

(defn save-window-size
  "Save the window height in the local state."
  [s]
  (reset! (::window-height s) (.-innerHeight js/window))
  (reset! (::window-width s) (.-innerWidth js/window)))

(rum/defcs navigation-sidebar < rum/reactive
                                ;; Derivatives
                                (drv/drv :org-data)
                                (drv/drv :board-data)
                                (drv/drv :change-data)
                                (drv/drv :current-user-data)
                                (drv/drv :editable-boards)
                                (drv/drv :show-add-post-tooltip)
                                (drv/drv :hide-left-navbar)
                                (drv/drv :mobile-navigation-sidebar)
                                (drv/drv :drafts-data)
                                (drv/drv :follow-ups-data)
                                ;; Locals
                                (rum/local false ::content-height)
                                (rum/local false ::footer-height)
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
                                  s)}
  [s]
  (let [org-data (drv/react s :org-data)
        board-data (drv/react s :board-data)
        change-data (drv/react s :change-data)
        filtered-change-data (into {} (filter #(-> % first (s/starts-with? "0000-0000-0000-") not) change-data))
        current-user-data (drv/react s :current-user-data)
        left-navigation-sidebar-width (- responsive/left-navigation-sidebar-width 20)
        all-boards (:boards org-data)
        boards (filter-boards all-boards)
        sorted-boards (sort-boards boards)
        is-all-posts (= (router/current-board-slug) "all-posts")
        is-follow-ups (= (router/current-board-slug) "follow-ups")
        is-drafts-board (= (:slug board-data) utils/default-drafts-board-slug)
        create-link (utils/link-for (:links org-data) "create")
        show-boards (or create-link (pos? (count boards)))
        user-is-part-of-the-team? (jwt/user-is-part-of-the-team (:team-id org-data))
        show-all-posts (and user-is-part-of-the-team?
                            (utils/link-for (:links org-data) "activity"))
        show-follow-ups (and user-is-part-of-the-team?
                             (utils/link-for (:links org-data) "follow-ups"))
        drafts-board (first (filter #(= (:slug %) utils/default-drafts-board-slug) all-boards))
        drafts-link (utils/link-for (:links drafts-board) "self")
        org-slug (router/current-org-slug)
        is-mobile? (responsive/is-mobile-size?)
        is-tall-enough? (or (not @(::content-height s))
                            (not @(::footer-height s))
                            (not (neg?
                             (- @(::window-height s) sidebar-top-margin @(::content-height s) @(::footer-height s)))))
        editable-boards (drv/react s :editable-boards)
        can-compose (pos? (count editable-boards))
        follow-ups-data (drv/react s :follow-ups-data)
        drafts-data (drv/react s :drafts-data)]
    [:div.left-navigation-sidebar.group
      {:class (utils/class-set {:hide-left-navbar (drv/react s :hide-left-navbar)
                                :mobile-show-side-panel (drv/react s :mobile-navigation-sidebar)})
       :on-click #(when-not (utils/event-inside? % (rum/ref-node s "left-navigation-sidebar-content"))
                    (dis/dispatch! [:input [:mobile-navigation-sidebar] false]))}
      [:div.left-navigation-sidebar-content
        {:ref "left-navigation-sidebar-content"
         :class (when can-compose "can-compose")}
        (when is-mobile?
          [:div.left-navigation-sidebar-mobile-header
            [:button.mlb-reset.mobile-close-bt
              {:on-click #(dis/dispatch! [:input [:mobile-navigation-sidebar] false])}]
            (orgs-dropdown)])
        ;; All posts
        (when show-all-posts
          [:a.all-posts.hover-item.group
            {:class (utils/class-set {:item-selected is-all-posts})
             :href (oc-urls/all-posts)
             :on-click #(nav-actions/nav-to-url! % "all-posts" (oc-urls/all-posts))}
            [:div.all-posts-icon]
            [:div.all-posts-label
              {:class (utils/class-set {:new (seq (apply concat (map :unread (vals filtered-change-data))))})}
              "All posts"]])
        (when show-follow-ups
          [:a.follow-ups.hover-item.group
            {:class (utils/class-set {:item-selected is-follow-ups})
             :href (oc-urls/follow-ups)
             :on-click #(nav-actions/nav-to-url! % "follow-ups" (oc-urls/follow-ups))}
            [:div.follow-ups-icon]
            [:div.follow-ups-label
              "Follow-ups"]
            (when (pos? (:follow-ups-count org-data))
              [:span.count (:follow-ups-count org-data)])])
        (when drafts-link
          (let [board-url (oc-urls/board (:slug drafts-board))
                draft-count (if drafts-data (count (:posts-list drafts-data)) (:count drafts-link))]
            [:a.drafts.hover-item.group
              {:class (when (and (not is-all-posts)
                                 (= (router/current-board-slug) (:slug drafts-board)))
                        "item-selected")
               :data-board (name (:slug drafts-board))
               :key (str "board-list-" (name (:slug drafts-board)))
               :href board-url
               :on-click #(nav-actions/nav-to-url! % (:slug drafts-board) board-url)}
              [:div.drafts-icon]
              [:div.drafts-label.group
                "Drafts "]
              (when (pos? draft-count)
                [:span.count draft-count])]))
        ;; Boards list
        (when show-boards
          [:div.left-navigation-sidebar-top.group
            ;; Boards header
            [:h3.left-navigation-sidebar-top-title.group
              [:span "Sections"]
              (when create-link
                [:button.left-navigation-sidebar-top-title-button.btn-reset
                  {:on-click #(nav-actions/show-section-add)
                   :title "Create a new section"
                   :data-placement "top"
                   :data-toggle (when-not is-mobile? "tooltip")
                   :data-container "body"}])]])
        (when show-boards
          [:div.left-navigation-sidebar-items.group
            (for [board sorted-boards
                  :let [board-url (oc-urls/board org-slug (:slug board))
                        is-current-board (= (router/current-board-slug) (:slug board))
                        board-change-data (get change-data (:uuid board))]]
              [:a.left-navigation-sidebar-item.hover-item
                {:class (utils/class-set {:item-selected (and (not is-all-posts)
                                                              is-current-board)})
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
                    {:class (utils/class-set {:new (seq (:unread board-change-data))
                                              :has-icon (#{"public" "private"} (:access board))})
                     :key (str "board-list-" (name (:slug board)) "-internal")
                     :dangerouslySetInnerHTML (utils/emojify (or (:name board) (:slug board)))}]]
                (when (= (:access board) "public")
                  [:div.public])
                (when (= (:access board) "private")
                  [:div.private])])])]
      (when can-compose
        [:div.left-navigation-sidebar-footer
          {:ref "left-navigation-sidebar-footer"}
          [:button.mlb-reset.compose-green-bt
            {:on-click #(ui-compose @(drv/get-ref s :show-add-post-tooltip))}
            [:span.compose-green-icon]
            [:span.compose-green-label
              "New post"]]])]))