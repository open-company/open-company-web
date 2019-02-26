(ns oc.web.components.ui.navbar
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
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
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.login-button :refer (login-button)]
            [oc.web.components.ui.orgs-dropdown :refer (orgs-dropdown)]
            [oc.web.components.user-notifications :refer (user-notifications)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [oc.web.components.ui.user-avatar :refer (user-avatar)]))

(rum/defcs navbar < rum/reactive
                    (drv/drv :navbar-data)
                    (ui-mixins/render-on-resize nil)
                    (rum/local false ::expanded-user-menu)
                    (on-window-click-mixin (fn [s e]
                     (when-not (utils/event-inside? e (rum/ref-node s "user-menu"))
                       (reset! (::expanded-user-menu s) false))))
                    {:did-mount (fn [s]
                     (when-not (utils/is-test-env?)
                       (when-not (responsive/is-tablet-or-mobile?)
                         (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))
                     s)}
  [s]
  (let [{:keys [org-data
                board-data
                show-login-overlay
                mobile-navigation-sidebar
                mobile-menu-open
                orgs-dropdown-visible
                user-settings
                org-settings
                search-active
                mobile-user-notifications]
         :as navbar-data} (drv/react s :navbar-data)
         is-mobile? (responsive/is-mobile-size?)
         mobile-ap-active? (and (not mobile-menu-open)
                                (not orgs-dropdown-visible)
                                (not org-settings)
                                (not user-settings)
                                (not search-active)
                                (not mobile-user-notifications))]
    [:nav.oc-navbar.group
      {:class (utils/class-set {:show-login-overlay show-login-overlay
                                :mobile-menu-open mobile-menu-open
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
            [:button.mlb-reset.mobile-navigation-sidebar-ham-bt
              {:class (utils/class-set {:active mobile-ap-active?})
               :on-click nav-actions/mobile-nav-sidebar}
              (cond
                (= (:access board-data) "private")
                [:span.private-icon]
                (= (:access board-data) "public")
                [:span.public-icon]
                (= (router/current-board-slug) "must-see")
                [:span.must-see-icon])
              [:span.board-name
                (cond
                  (= (router/current-board-slug) "all-posts")
                  "All Posts"
                  (= (router/current-board-slug) "must-see")
                  "Must See"
                  :else
                  (:name board-data))]]
           (when-not is-mobile?
             (orgs-dropdown))]
          (when-not is-mobile?
            [:div.navbar-center
              {:class (when search-active "search-active")}
              (search-box)])
          [:div.navbar-right
            (if is-mobile?
              [:div.mobile-right-nav
                [:button.mlb-reset.search-bt
                  {:on-click #(do
                                (menu/mobile-menu-close)
                                (search-actions/active)
                                (user-actions/hide-mobile-user-notifications)
                                (utils/after 500 search-actions/focus))
                   :class (when search-active "active")}]
                (when (jwt/user-is-part-of-the-team (:team-id org-data))
                  [:button.mlb-reset.mobile-notification-bell
                    {:class (utils/class-set {:active mobile-user-notifications})
                     :on-click #(do
                                  (search-actions/inactive)
                                  (menu/mobile-menu-close)
                                  (when (or org-settings
                                          user-settings)
                                    (dis/dispatch! [:input [:user-settings] nil])
                                    (dis/dispatch! [:input [:org-settings] nil]))
                                  (user-actions/show-mobile-user-notifications))}])]
              (if (jwt/jwt)
                [:div.group
                  (user-notifications)
                  [:div.user-menu
                    {:ref "user-menu"}
                    (user-avatar
                     {:click-cb #(swap! (::expanded-user-menu s) not)})
                    (when @(::expanded-user-menu s)
                      (menu/menu))]]
                (login-button)))]]]
      (when is-mobile?
        ;; Render the menu here only on mobile so it can expand the navbar
        (menu/menu))]))