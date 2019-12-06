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
            [oc.web.components.ui.menu :as menu]
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
                    (drv/drv :mobile-user-notifications)
                    (ui-mixins/render-on-resize nil)
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
         showing-mobile-user-notifications (drv/react s :mobile-user-notifications)
         mobile-title (cond
                       showing-mobile-user-notifications
                       "Notifications"
                       (= (router/current-board-slug) "inbox")
                       "What's New"
                       (= (router/current-board-slug) "all-posts")
                       "All Posts"
                       (= (router/current-board-slug) "follow-ups")
                       "Follow-ups"
                       :else
                       (:name board-data))]
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
            (if is-mobile?
              [:button.mlb-reset.mobile-ham-menu
                {:on-click #(dis/dispatch! [:update [:mobile-navigation-sidebar] not])}]
              (orgs-dropdown))]
          (if is-mobile?
            [:div.navbar-center
              [:div.navbar-mobile-title
                mobile-title]]
            [:div.navbar-center
              {:class (when search-active "search-active")}
              (search-box)])
          [:div.navbar-right
            (if (jwt/jwt)
              [:div.group
                (when-not is-mobile?
                  (user-notifications))
                [:div.user-menu
                  [:div.user-menu-button
                    {:ref "user-menu"
                     :class (when show-whats-new-green-dot "green-dot")}
                    (user-avatar
                     {:click-cb #(nav-actions/menu-toggle)})]]]
              (login-button))]]]]))
