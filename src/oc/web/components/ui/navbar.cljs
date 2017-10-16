(ns oc.web.components.ui.navbar
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.router :as router]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.menu :refer (menu)]
            [oc.web.components.ui.user-avatar :refer (user-avatar)]
            [oc.web.components.ui.login-button :refer (login-button)]
            [oc.web.components.ui.orgs-dropdown :refer (orgs-dropdown)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(defn- share-new-tooltip [team-id]
  (if (jwt/team-has-bot? team-id)
    "Select topics to share by Slack, email or link."
    "Select topics to share by email or link."))

(rum/defcs navbar < rum/reactive
                     (drv/drv :navbar-data)
                     {:did-mount (fn [s]
                                  (when-not (utils/is-test-env?)
                                    (when-not (responsive/is-tablet-or-mobile?)
                                      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))
                                  s)}
  [s disabled-user-menu]
  (let [{:keys [show-login-overlay mobile-menu-open org-data board-data] :as navbar-data} (drv/react s :navbar-data)]
    [:nav.oc-navbar.group
      {:class (utils/class-set {:show-login-overlay show-login-overlay
                                :mobile-menu-open mobile-menu-open
                                :has-prior-updates (and (router/current-org-slug)
                                                        (pos? (:count (utils/link-for (:links org-data) "collection" "GET"))))
                                :can-edit-board (and (router/current-org-slug)
                                                     (not (:read-only org-data)))
                                :jwt (jwt/jwt)})}
      (when (not (utils/is-test-env?))
        (login-overlays-handler))
      [:div.oc-navbar-header.group
        [:div.oc-navbar-header-container.group
          [:div.nav.navbar-nav.navbar-center
            (orgs-dropdown)]
          [:ul.nav.navbar-nav.navbar-right
            [:li
              (if (responsive/is-mobile-size?)
                [:button.btn-reset.mobile-menu.group
                  {:on-click #(dis/dispatch! [:mobile-menu-toggle])}
                  [:div.vertical-ellipses]]
                (if (jwt/jwt)
                  [:div.group
                    [:div.dropdown.right
                      (user-avatar {:classes (str "mlb-reset" (if disabled-user-menu " disabled-user-menu" " dropdown-toggle")) :disable-menu disabled-user-menu})
                      (when-not disabled-user-menu
                        (menu))]
                    (comment ; FIXME: Remove the notification bell until we enable it.
                      [:div.notification-bell.right
                        [:img
                          {:width 14 :height 16 :src (utils/cdn "/img/ML/alerts_bell.svg")}]])]
                  (login-button)))]]]]
      (when (responsive/is-mobile-size?)
        ;; Render the menu here only on mobile so it can expand the navbar
        (menu {:mobile-menu-open mobile-menu-open}))]))