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
            [oc.web.lib.jwt :as jwt]
            ; [oc.web.lib.tooltip :as t]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.icon :as i]
            [oc.web.components.ui.menu :refer (menu)]
            [oc.web.components.ui.user-avatar :refer (user-avatar)]
            [oc.web.components.ui.login-button :refer (login-button)]
            [oc.web.components.ui.small-loading :as loading]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [om-bootstrap.random :as r]
            [om-bootstrap.button :as b]))

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
                                         :small-navbar su-navbar
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
            (if (or (utils/in? (:route @router/path) "orgs")
                    (not (router/current-org-slug)))
              (dom/a {:href "https://opencompany.com/" :title "OpenCompany.com"}
                (dom/img {:src "/img/oc-wordmark.svg" :style {:height "25px" :margin-top "12px"}}))
              (om/build org-avatar data))
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
                        (when (and (not dashboard-sharing)
                                   (not is-update-preview)
                                   (router/current-board-slug)
                                   (jwt/is-admin? (:team-id (dis/org-data)))
                                   (pos? (count (:topics (dis/board-data)))))
                          (dom/button {:class "btn-reset invite-others right"
                                       :title "Invite others"
                                       :data-toggle "tooltip"
                                       :data-container "body"
                                       :data-placement "bottom"
                                       :on-click #(let [share-work-tip (str "share-work-" (:slug (dis/org-data)))]
                                                   ; (t/hide share-work-tip)
                                                   (router/nav! (oc-urls/org-team-settings)))}
                            (dom/i {:class "fa fa-user-plus"})))
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
                            (dom/div {:class "sharing-button-container"}
                              (dom/button {:class "btn-reset sharing-button right"
                                           :title (share-new-tooltip (:team-id org-data))
                                           :data-toggle "tooltip"
                                           :data-container "body"
                                           :data-placement "left"
                                           :disabled (not (nil? foce-key))
                                           :on-click (fn []
                                                       (when (nil? foce-key)
                                                         (when is-topic-view
                                                            (router/nav! (oc-urls/board)))
                                                         (dis/dispatch! [:dashboard-share-mode true])))}
                                (dom/i {:class "fa fa-share"}))))))
                      (login-button)))))))
          (when (and (not (responsive/is-mobile-size?))
                     create-update-share-button-cb)
            (dom/div {:class "oc-navbar-separator"})))
        (when (responsive/is-mobile-size?)
          ;; Render the menu here only on mobile so it can expand the navbar
          (om/build menu {:mobile-menu-open mobile-menu-open}))))))