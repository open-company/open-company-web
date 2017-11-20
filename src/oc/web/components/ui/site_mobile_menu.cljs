(ns oc.web.components.ui.site-mobile-menu
  "Component for the site header. This is copied into oc.core/nav
   and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]))

(def body-class "mobile-menu-expanded")

(rum/defcs site-mobile-menu < rum/reactive
                              (drv/drv :site-menu-open)
                              {:did-mount (fn [s]
                                 (dommy/remove-class! (sel1 :body) body-class)
                                s)
                               :will-update (fn [s]
                                (let [site-menu-open @(drv/get-ref s :site-menu-open)
                                      body (sel1 [:body])]
                                  (js/console.log "site-mobile-menu/will-update site-menu-open" site-menu-open "has-class" (dommy/has-class? body body-class))
                                  (when (responsive/is-mobile-size?)
                                    (if site-menu-open
                                      (when-not (dommy/has-class? body body-class)
                                        (js/console.log "adding class")
                                        (dommy/add-class! body body-class))
                                      (when (dommy/has-class? body body-class)
                                        (js/console.log "removing class")
                                        (dommy/remove-class! body body-class)))))
                                s)}
  [s]
  (when (drv/react s :site-menu-open)
    [:div.site-mobile-menu.mobile-only
      [:div.site-mobile-menu-container
        [:div.site-mobile-menu-item
          [:a
            {:href oc-urls/home
             :class (when (utils/in? (:route @router/path) "home") "active")
             :on-click #(do
                         (utils/event-stop %)
                         (dis/dispatch! [:login-overlay-show nil])
                         (dis/dispatch! [:site-menu-toggle])
                         (router/nav! oc-urls/home))}
            "Home"]]
        ; [:div.site-mobile-menu-item
        ;   [:a
        ;     {:href oc-urls/pricing
        ;      :class (when (utils/in? (:route @router/path) "pricing") "active")
        ;      :on-click #(do
        ;                  (utils/event-stop %)
        ;                  (dis/dispatch! [:login-overlay-show nil])
        ;                  (dis/dispatch! [:site-menu-toggle])
        ;                  (router/nav! oc-urls/pricing))}
        ;     "Pricing"]]
        [:div.site-mobile-menu-item
          [:a
            {:href oc-urls/about
             :class (when (utils/in? (:route @router/path) "about") "active")
             :on-click #(do
                         (utils/event-stop %)
                         (dis/dispatch! [:login-overlay-show nil])
                         (dis/dispatch! [:site-menu-toggle])
                         (router/nav! oc-urls/about))}
            "About"]]
        [:div.site-mobile-menu-item
          [:a
            {:href oc-urls/blog
             :target "_blank"}
            "Blog"]]]
      [:div.site-mobile-menu-footer
        [:button.mlb-reset.mlb-default
          {:on-click #(if (utils/in? (:route @router/path) "login")
                        (dis/dispatch! [:login-overlay-show :login-with-slack])
                        (router/nav! oc-urls/login))}
          "Log In"]
        [:button.mlb-reset.get-started-button
          {:on-click #(if (utils/in? (:route @router/path) "login")
                        (dis/dispatch! [:login-overlay-show :signup-with-slack])
                        (router/nav! oc-urls/sign-up-with-slack))}
          "Get started for free"]]]))