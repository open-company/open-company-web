(ns oc.web.components.ui.navbar
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.stores.search :as search]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.search :refer (search-box)]
            [oc.web.components.ui.user-avatar :refer (user-avatar)]
            [oc.web.components.user-notifications :refer (user-notifications)]
            [oc.web.components.ui.login-button :refer (login-button)]
            [oc.web.components.ui.orgs-dropdown :refer (orgs-dropdown)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)])
  (:import [goog.async Throttle]))

(def scroll-top-offset 46)

(defn- check-scroll [s e]
  (when (and (> (.. js/document -scrollingElement -scrollTop) scroll-top-offset)
             (not @(::scrolled s)))
    (reset! (::scrolled s) true))
  (when (and (<= (.. js/document -scrollingElement -scrollTop) scroll-top-offset)
             @(::scrolled s))
    (reset! (::scrolled s) false)))

(rum/defcs navbar < rum/reactive
                    (drv/drv :navbar-data)
                    (drv/drv :cmail-state)
                    (drv/drv :show-add-post-tooltip)
                    (ui-mixins/render-on-resize nil)
                    (rum/local nil ::throttled-scroll-check)
                    (rum/local false ::scrolled)
                    (ui-mixins/on-window-scroll-mixin (fn [s e]
                     (when @(::throttled-scroll-check s)
                       (.fire ^js @(::throttled-scroll-check s)))))
                    (drv/drv search/search-active?)
                    {:will-mount (fn [s]
                      (reset! (::throttled-scroll-check s) (Throttle. (partial check-scroll s) 500))
                     s)
                     :did-mount (fn [s]
                      (.fire ^js @(::throttled-scroll-check s))
                     s)
                     :will-unmount (fn [s]
                      (when @(::throttled-scroll-check s)
                        (.dispose ^js @(::throttled-scroll-check s)))
                     s)}
  [s]
  (let [{:keys [org-data
                board-data
                contributions-user-data
                show-login-overlay
                orgs-dropdown-visible
                search-active
                show-whats-new-green-dot
                panel-stack
                current-org-slug
                current-board-slug
                current-contributions-id
                mobile-user-notifications]} (drv/react s :navbar-data)
        is-mobile? (responsive/is-mobile-size?)
        current-panel (last panel-stack)
        expanded-user-menu (= current-panel :menu)
        org-board-data (dis/org-board-data org-data current-board-slug)
        mobile-title (cond
                       mobile-user-notifications
                       "Alerts"
                       (= (keyword current-board-slug) :replies)
                       "Activity"
                       (= (keyword current-board-slug) :inbox)
                       "Unread"
                       (= (keyword current-board-slug) :all-posts)
                       "All"
                       (= (keyword current-board-slug) :bookmarks)
                       "Bookmarks"
                       (= (keyword current-board-slug) :following)
                       "Home"
                       (= (keyword current-board-slug) :topics)
                       "Explore"
                       (and current-contributions-id (:self? contributions-user-data))
                       "You"
                       (and current-contributions-id (map? contributions-user-data))
                       (:name contributions-user-data)
                       (seq (:name board-data))
                       (:name board-data)
                       :else
                       (:name org-board-data))]
    [:nav.oc-navbar.group
      {:class (utils/class-set {:show-login-overlay show-login-overlay
                                :expanded-user-menu expanded-user-menu
                                :has-prior-updates (and current-org-slug
                                                        (pos?
                                                         (:count
                                                          (utils/link-for (:links org-data) "collection" "GET"))))
                                :showing-orgs-dropdown orgs-dropdown-visible

                                :can-edit-board (and current-org-slug
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
          (if (jwt/jwt)
            [:div.navbar-right.group
              (when-not is-mobile?
                (user-notifications))
              [:div.user-menu
                [:div.user-menu-button
                  {:class (when show-whats-new-green-dot "green-dot")}
                  (user-avatar {:click-cb #(nav-actions/menu-toggle)})]]]
            [:div.navbar-right.anonymous-user
              (login-button)])]]]))
