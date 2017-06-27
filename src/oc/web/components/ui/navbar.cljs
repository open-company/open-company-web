(ns oc.web.components.ui.navbar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.api :as api]
            [oc.web.dispatcher :as dis]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as jwt]
            ; [oc.web.lib.tooltip :as t]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.trend-bar :refer (trend-bar)]
            [oc.web.components.ui.icon :as i]
            [oc.web.components.ui.menu :refer (menu)]
            [oc.web.components.ui.user-avatar :refer (user-avatar)]
            [oc.web.components.ui.login-button :refer (login-button)]
            [oc.web.components.ui.small-loading :as loading]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.orgs-dropdown :refer (orgs-dropdown)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]))

(defn- share-new-tooltip [team-id]
  (if (jwt/team-has-bot? team-id)
    "Select topics to share by Slack, email or link."
    "Select topics to share by email or link."))

(defcomponent navbar [{:keys [org-data
                              board-data
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
                              is-dashboard
                              is-topic-view
                              is-update-preview] :as data} owner options]

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (when-not (and (responsive/is-tablet-or-mobile?)
                     (not su-navbar))
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))

  (render [_]
    (let [fixed-show-share-su-button (and (not (responsive/is-mobile?))              ; it's not mobile
                                          (jwt/jwt)                                  ; the user is logged in
                                          (or is-dashboard                           ; is looking at the dashboard
                                              is-topic-view)
                                          (not (:read-only board-data))            ; it's not a read-only cmp
                                          (if (contains? data :show-share-su-button) ; the including component
                                            show-share-su-button                     ; wants to
                                            true))]
      (dom/nav {:class (utils/class-set {:oc-navbar true
                                         :group true
                                         :show-login-overlay (:show-login-overlay data)
                                         :mobile-menu-open mobile-menu-open
                                         :has-prior-updates (and (router/current-org-slug)
                                                                 (pos? (:count (utils/link-for (:links (dis/org-data)) "collection" "GET"))))
                                         :can-edit-board (and (router/current-org-slug)
                                                              (not (:read-only (dis/org-data))))
                                         :jwt (jwt/jwt)})}
        (when (not (utils/is-test-env?))
          (login-overlays-handler))
        (dom/div {:class "oc-navbar-header group"
                  :style {:width (if header-width (str header-width "px") "100%")}}
          (dom/div {:class "oc-navbar-header-container group"}
            (when (and org-data (:su-navbar data))
                ;; If it's showing an update link the org avatar only if there is a link to the company
                (org-avatar org-data (utils/link-for (:links (dis/update-data)) "company" "GET")))
            (dom/div {:class "nav navbar-nav navbar-center"}
              (orgs-dropdown))
            (when-not (:hide-right-menu data)
              (dom/ul {:class "nav navbar-nav navbar-right"}
                (dom/li {}
                  (if (responsive/is-mobile-size?)
                    (dom/button {:class "btn-reset mobile-menu group"
                                 :on-click #(dis/dispatch! [:mobile-menu-toggle])}
                      ; (dom/img {:src (str ls/cdn-url "/img/vert-ellipsis.svg") :width 8})
                      (dom/div {:class "vertical-ellipses"}))
                    (if (jwt/jwt)
                      (dom/div {:class "group"}
                        (dom/div {:class "dropdown right"}
                          (user-avatar {:classes "mlb-reset dropdown-toggle"})
                          (om/build menu {}))
                        (dom/div {:class "right"
                                  :style {:margin-right "0.5rem" :margin-top "5px"}}
                          (dom/img {:width 14 :height 16 :src "/img/ML/alerts_bell.svg"}))
                        (when fixed-show-share-su-button
                          (if dashboard-sharing
                            (dom/div {:class "sharing-button-container"}
                              (dom/button {:class "btn-reset share-selected-topics-button right btn-solid"
                                           :title (share-new-tooltip (:team-id org-data))
                                           :data-toggle "tooltip"
                                           :data-container "body"
                                           :data-placement "left"
                                           :disabled (zero? (count dashboard-selected-topics))
                                           :on-click #(router/nav! (oc-urls/update-preview))}
                                (when (om/get-state owner :su-redirect)
                                  (loading/small-loading))
                                (if (zero? (count dashboard-selected-topics))
                                  "0 Topics selected"
                                  (str "Share " (count dashboard-selected-topics) " topic" (when (not= (count dashboard-selected-topics) 1) "s"))))
                              (dom/button {:class "btn-reset btn-link right sharing-cancel"
                                           :on-click (fn []
                                                       (dis/dispatch! [:dashboard-share-mode false]))}
                                "Cancel"))
                            ; Comment out but not remove until we replace this with something.
                            ; (dom/div {:class "sharing-button-container"}
                            ;   (dom/button {:class "btn-reset sharing-button right"
                            ;                :title (share-new-tooltip (:team-id org-data))
                            ;                :data-toggle "tooltip"
                            ;                :data-container "body"
                            ;                :data-placement "left"
                            ;                :disabled (not (nil? foce-key))
                            ;                :on-click (fn []
                            ;                            (when (nil? foce-key)
                            ;                              (when is-topic-view
                            ;                                 (router/nav! (oc-urls/board)))
                            ;                              (dis/dispatch! [:dashboard-share-mode true])))}
                            ;     (dom/i {:class "fa fa-share"})))
                            )))
                      (login-button)))))))
          (when (and (not (responsive/is-mobile-size?))
                     create-update-share-button-cb)
            (dom/div {:class "oc-navbar-separator"})))
        (trend-bar (:name org-data))
        (when (responsive/is-mobile-size?)
          ;; Render the menu here only on mobile so it can expand the navbar
          (om/build menu {:mobile-menu-open mobile-menu-open}))))))