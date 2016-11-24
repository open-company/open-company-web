(ns open-company-web.components.ui.navbar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
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

(defn scroll-listener [e]
  (let [body-scroll-top (gobj/get (.-body js/document) "scrollTop")]
    (if (>= body-scroll-top 71)
      (dommy/add-class! (sel1 [:body]) "fixed-navbar")
      (dommy/remove-class! (sel1 [:body]) "fixed-navbar"))))

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
                              su-navbar] :as data} owner options]

  (did-mount [_]
    (when-not (and (responsive/is-mobile-size?)
                   (not su-navbar))
      (scroll-listener nil)
      (om/set-state! owner :scroll-listener
        (events/listen js/window EventType/SCROLL scroll-listener))))

  (will-unmount [_]
    (when-let [scroll-listener (om/get-state owner :scroll-listener)]
      (events/unlistenByKey scroll-listener)))

  (render [_]
    (let [header-width (responsive/total-layout-width-int card-width columns-num)
                                                                                    ; show the new update btn if
          fixed-show-share-su-button (and (not (responsive/is-mobile?))              ; it's not mobile
                                          (jwt/jwt)                                  ; the user is logged in
                                          (not (:read-only company-data))            ; it's not a read-only cmp
                                          (if (contains? data :show-share-su-button) ; the including component
                                            show-share-su-button                     ; wants to
                                            true))]
      (dom/nav {:class (utils/class-set {:oc-navbar true
                                         :group true
                                         :su-navbar su-navbar
                                         :mobile-menu-open mobile-menu-open
                                         :no-jwt (not (jwt/jwt))})}
        (when (and (not (jwt/jwt)) (not (utils/is-test-env?)))
          (login-overlays-handler))
        (dom/div {:class "oc-navbar-header group"
                  :style {:width (str header-width "px")}}
          (dom/div {:class "group"}
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
                      (dom/i {:class "fa fa-ellipsis-v"}))
                    (if (jwt/jwt)
                      (dom/div {:class "group"}
                        (dom/div {:class "dropdown right"}
                          (user-avatar {:classes "btn-reset dropdown-toggle"})
                          (om/build menu {})))
                      (login-button)))))))
          (when-not (responsive/is-mobile-size?)
            (dom/div {:class "oc-navbar-separator"})))
        (when-not su-navbar
          (if (responsive/is-mobile-size?)
            ;; Render the menu here only on mobile so it can expand the navbar
            (om/build menu {:mobile-menu-open mobile-menu-open})
            ;; Render the bottom part of the navbar when not on mobile
            (dom/div {:class "oc-navbar-bottom group"
                      :style {:width (str header-width "px")}}
              (dom/div {:class "left"}
                (when (router/current-company-slug)
                  (dom/a {:class (when (= active :dashboard) "active")
                          :href (oc-urls/company)
                          :on-click #(do
                                       (utils/event-stop %)
                                       (router/nav! (oc-urls/company)))}
                    "Dashboard"))
                (when (router/current-company-slug)
                  (dom/a {:class (when (= active :updates) "active")
                          :href (oc-urls/stakeholder-update-list)
                          :on-click (fn [e]
                                      (utils/event-stop e)
                                      (dis/dispatch! [:reset-su-list])
                                      (utils/after 100 #(router/nav! (oc-urls/stakeholder-update-list))))}
                    "Updates")))
              (dom/div {:class "right"}
                (when fixed-show-share-su-button
                  (dom/div {:class "sharing-button-container"}
                    (dom/a {:class "btn-reset sharing-button right"
                            :on-click #(router/nav! (oc-urls/stakeholder-update-preview))}
                      "Share new update")))
                (when create-update-share-button-cb
                  (dom/div {:class "sharing-button-container"}
                    (dom/button {:class "btn-reset btn-solid"
                                 :on-click create-update-share-button-cb
                                 :disabled create-update-share-button-disabled} "SHARE")))))))))))