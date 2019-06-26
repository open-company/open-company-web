(ns oc.web.components.ui.navbar
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.stores.user :as user-store]
            [oc.web.components.ui.menu :as menu]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.actions.user :as user-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.search :as search-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.search :refer (search-box)]
            [oc.web.components.navigation-sidebar :as navigation-sidebar]
            [oc.web.components.ui.login-button :refer (login-button)]
            [oc.web.components.ui.orgs-dropdown :refer (orgs-dropdown)]
            [oc.web.components.user-notifications :refer (user-notifications)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [oc.web.components.ui.user-avatar :refer (user-avatar)]))

(defn- mobile-nav! [e board-slug]
  (router/nav! (oc-urls/board board-slug)))

(rum/defcs navbar < rum/reactive
                    (drv/drv :navbar-data)
                    (drv/drv :show-add-post-tooltip)
                    (ui-mixins/render-on-resize nil)
                    (rum/local false ::show-sections-list)
  [s]
  (let [{:keys [org-data
                board-data
                current-user-data
                show-login-overlay
                orgs-dropdown-visible
                search-active
                mobile-user-notifications
                show-whats-new-green-dot
                panel-stack]
         :as navbar-data} (drv/react s :navbar-data)
         is-mobile? (responsive/is-mobile-size?)
         current-panel (last panel-stack)
         expanded-user-menu (= current-panel :menu)
         org-settings (#{:org :interations :team :invite :billing} current-panel)
         user-settings (#{:profile :notifications} current-panel)
         mobile-ap-active? (and (not expanded-user-menu)
                                (not orgs-dropdown-visible)
                                (not org-settings)
                                (not user-settings)
                                (not search-active)
                                (not mobile-user-notifications))
         user-role (user-store/user-role org-data current-user-data)
         can-compose? (or (= user-role :admin)
                          (= user-role :author))
        section-name (cond
                      (= (router/current-board-slug) "all-posts")
                      "All Posts"
                      (= (router/current-board-slug) "must-see")
                      "Must See"
                      :else
                      (:name board-data))
        create-link (utils/link-for (:links org-data) "create")
        all-boards (:boards org-data)
        boards (navigation-sidebar/filter-boards all-boards)
        show-all-posts (and (jwt/user-is-part-of-the-team (:team-id org-data))
                            (utils/link-for (:links org-data) "activity"))
        drafts-board (first (filter #(= (:slug %) utils/default-drafts-board-slug) all-boards))
        drafts-link (utils/link-for (:links drafts-board) "self")
        show-boards (or create-link (pos? (count boards)))
        sorted-boards (navigation-sidebar/sort-boards boards)]
    [:nav.oc-navbar.group
      {:class (utils/class-set {:show-login-overlay show-login-overlay
                                :expanded-user-menu expanded-user-menu
                                :has-prior-updates (and (router/current-org-slug)
                                                        (pos?
                                                         (:count
                                                          (utils/link-for (:links org-data) "collection" "GET"))))
                                :showing-orgs-dropdown orgs-dropdown-visible
                                :can-edit-board (and (router/current-org-slug)
                                                     (not (:read-only org-data)))})}
      (when-not (utils/is-test-env?)
        (login-overlays-handler))
      [:div.oc-navbar-header.group
        [:div.oc-navbar-header-container.group
          [:div.navbar-left
            (orgs-dropdown)]
          (if is-mobile?
            [:div.navbar-center
              [:button.mlb-reset.mobile-board-button
                {:on-click #(swap! (::show-sections-list s) not)
                 :ref :mobile-board-button}
                section-name]
              (when @(::show-sections-list s)
                [:div.mobile-sections-list
                  {:on-click #(reset! (::show-sections-list s) false)}
                  [:div.mobile-sections-list-inner
                    (when show-all-posts
                      [:button.mlb-reset.mobile-section-item.all-posts
                        {:class (when (= (router/current-board-slug) "all-posts") "active")
                         :on-click #(mobile-nav! % "all-posts")}
                        "All Posts"])
                    (when drafts-link
                      [:button.mlb-reset.mobile-section-item.drafts
                        {:class (when (= (router/current-board-slug) utils/default-drafts-board-slug) "active")
                         :on-click #(mobile-nav! % utils/default-drafts-board-slug)}
                        "Drafts"])
                    (for [board sorted-boards]
                      [:button.mlb-reset.mobile-section-item
                        {:key (str "mobile-section-" (:slug board))
                         :class (when (= (router/current-board-slug) (:slug board)) "active")
                         :on-click #(mobile-nav! % (:slug board))}
                        (:name board)])
                    (when can-compose?
                      [:button.mlb-reset.mobile-section-item-compose
                        {:on-click #(ui-compose @(drv/get-ref s :show-add-post-tooltip))}
                        [:span.compose-green-icon]
                        [:span.compose-green-label "New post"]])]])]
            [:div.navbar-center
              {:class (when search-active "search-active")}
              (search-box)])
          [:div.navbar-right
            (if (jwt/jwt)
              [:div.group
                (user-notifications)
                [:div.user-menu
                  [:div.user-menu-button
                    {:ref "user-menu"
                     :class (when show-whats-new-green-dot "green-dot")}
                    (user-avatar
                     {:click-cb #(nav-actions/menu-toggle)})]]]
              (login-button))]]]]))