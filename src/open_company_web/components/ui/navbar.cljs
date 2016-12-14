(ns open-company-web.components.ui.navbar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [open-company-web.api :as api]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.menu :refer (menu)]
            [open-company-web.components.ui.user-avatar :refer (user-avatar)]
            [open-company-web.components.ui.login-button :refer (login-button)]
            [open-company-web.components.ui.small-loading :as loading]
            [open-company-web.components.ui.company-avatar :refer (company-avatar)]
            [open-company-web.components.ui.login-overlay :refer (login-overlays-handler)]
            [om-bootstrap.random :as r]
            [om-bootstrap.button :as b]))

(defn- share-new-tooltip []
  (if (utils/slack-share?)
    "Select topics to share by Slack, email or link."
    "Select topics to share by email or link."))

(defcomponent navbar [{:keys [company-data
                              columns-num
                              card-width
                              latest-su
                              link-loading
                              email-loading
                              slack-loading
                              show-share-su-button
                              create-update-share-button-cb
                              create-update-share-button-disabled
                              active
                              foce-key
                              mobile-menu-open
                              header-width
                              su-navbar
                              show-navigation-bar
                              dashboard-selected-topics
                              dashboard-sharing
                              is-dashboard] :as data} owner options]

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (when-not (and (responsive/is-tablet-or-mobile?)
                     (not su-navbar))
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))

  (will-receive-props [_ _]
    (when (om/get-state owner :su-redirect)
      (router/nav! (oc-urls/stakeholder-update-preview))
      (om/set-state! owner :su-redirect nil)))

  (render [_]
    (let [fixed-show-share-su-button (and (not (responsive/is-mobile?))              ; it's not mobile
                                          (jwt/jwt)                                  ; the user is logged in
                                          is-dashboard                               ; is looking at the dashboard
                                          (not (:read-only company-data))            ; it's not a read-only cmp
                                          (if (contains? data :show-share-su-button) ; the including component
                                            show-share-su-button                     ; wants to
                                            true))
          should-show-left-links (and (router/current-company-slug)
                                      show-navigation-bar)]
      (dom/nav {:class (utils/class-set {:oc-navbar true
                                         :group true
                                         :small-navbar (or su-navbar (not show-navigation-bar))
                                         :show-login-overlay (:show-login-overlay data)
                                         :mobile-menu-open mobile-menu-open
                                         :has-prior-updates (and (router/current-company-slug)
                                                                 (pos? (:count (utils/link-for (:links (dis/company-data)) "stakeholder-updates" "GET"))))
                                         :can-edit-company (and (router/current-company-slug)
                                                                (not (:read-only (dis/company-data))))
                                         :jwt (jwt/jwt)})}
        (when (not (utils/is-test-env?))
          (login-overlays-handler))
        (dom/div {:class "oc-navbar-header group"
                  :style {:width (str header-width "px")}}
          (dom/div {:class "oc-navbar-header-container group"}
            (if (utils/in? (:route @router/path) "companies")
              (dom/a {:href "https://opencompany.com/" :title "OpenCompany.com"}
                (dom/img {:src "/img/oc-wordmark.svg" :style {:height "25px" :margin-top "12px"}}))
              (om/build company-avatar data))
            (when-not (:hide-right-menu data)
              (dom/ul {:class "nav navbar-nav navbar-right"}
                (dom/li {}
                  (if (responsive/is-mobile-size?)
                    (dom/button {:class "btn-reset mobile-menu group"
                                 :on-click #(dis/dispatch! [:mobile-menu-toggle])}
                      ; (dom/img {:src "/img/vert-ellipsis.svg" :width 8})
                      (dom/div {:class "vertical-ellipses"}))
                    (if (jwt/jwt)
                      (dom/div {:class "group"}
                        (dom/div {:class "dropdown right"}
                          (user-avatar {:classes "btn-reset dropdown-toggle"})
                          (om/build menu {}))
                        (when fixed-show-share-su-button
                          (if dashboard-sharing
                            (dom/div {:class "sharing-button-container"}
                              (dom/button {:class "btn-reset sharing-button right btn-solid"
                                           :title (share-new-tooltip)
                                           :data-toggle "tooltip"
                                           :data-container "body"
                                           :data-placement "left"
                                           :disabled (zero? (count dashboard-selected-topics))
                                           :on-click (fn []
                                                       (om/set-state! owner :su-redirect true)
                                                       (api/patch-stakeholder-update {:sections dashboard-selected-topics :title (:title (:stakeholder-update company-data))}))}
                                (when (om/get-state owner :su-redirect)
                                  (loading/small-loading))
                                (str "Share " (count dashboard-selected-topics) " topic" (when (not= (count dashboard-selected-topics) 1) "s")))
                              (dom/button {:class "btn-reset btn-link right sharing-cancel"
                                           :on-click (fn []
                                                       (dis/dispatch! [:dashboard-share-mode false]))}
                                "Cancel"))
                            (dom/div {:class "sharing-button-container"}
                              (dom/button {:class "btn-reset sharing-button btn-link right"
                                           :title (share-new-tooltip)
                                           :data-toggle "tooltip"
                                           :data-container "body"
                                           :data-placement "left"
                                           :on-click (fn []
                                                       (dis/dispatch! [:dashboard-share-mode true]))}
                                "Share topics")))))
                      (login-button)))))))
          (when (and (not (responsive/is-mobile-size?))
                     create-update-share-button-cb)
            (dom/div {:class "oc-navbar-separator"})))
        (when (responsive/is-mobile-size?)
          ;; Render the menu here only on mobile so it can expand the navbar
          (om/build menu {:mobile-menu-open mobile-menu-open}))))))