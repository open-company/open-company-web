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
                              menu-open
                              show-share-su-button
                              active] :as data} owner options]

  (did-mount [_]
    (scroll-listener nil)
    (om/set-state! owner :scroll-listener
      (events/listen js/window EventType/SCROLL scroll-listener)))

  (will-unmount [_]
    (when-let [scroll-listener (om/get-state owner :scroll-listener)]
      (js/console.log "remove scroll listener" scroll-listener)
      (events/unlistenByKey scroll-listener)))

  (render [_]
    (let [header-width (+ (* card-width columns-num)    ; cards width
                          (* 20 (dec columns-num))      ; cards right margin
                          (when (> columns-num 1) 60))  ; x margins if needed
          fixed-show-share-su-button (if (contains? data :show-share-su-button) show-share-su-button true)]
      (dom/nav {:class "oc-navbar group"}
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
                    (dom/div {:class "group"}
                      (i/icon :menu-34 {}))
                    (if (jwt/jwt)
                      (dom/div {:class "group"}
                        (dom/div {:class "dropdown right"}
                          (user-avatar {:classes "btn-reset dropdown-toggle"})
                          (om/build menu {})))
                      (login-button)))))))
          (dom/div {:class "oc-navbar-separator"}))
        (when-not (responsive/is-mobile-size?)
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
                        :on-click #(do
                                     (utils/event-stop %)
                                     (router/nav! (oc-urls/stakeholder-update-list)))}
                  "Updates")))
            (dom/div {:class "right"}
              (when fixed-show-share-su-button
                (dom/div {:class "sharing-button-container"}
                  (dom/a {:class "btn-reset sharing-button right"
                          :on-click #(router/nav! (oc-urls/stakeholder-update-preview))}
                    (dom/i {:class "fa fa-share"}) " Share an update"))))))))))