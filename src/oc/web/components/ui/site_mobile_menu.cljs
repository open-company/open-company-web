(ns oc.web.components.ui.site-mobile-menu
  "Component for the site header. This is copied into oc.core/nav
   and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]))

(def body-class "mobile-menu-expanded")

(defn site-menu-toggle [& [force-close]]
  (let [site-menu-open (:site-menu-open @dis/app-state)
        next-site-menu-open (if (or force-close
                                    (not (responsive/is-mobile-size?)))
                              false
                              (not site-menu-open))]
    (dis/dispatch! [:input [:site-menu-open] next-site-menu-open])))

(rum/defcs site-mobile-menu < rum/reactive
                              (drv/drv :site-menu-open)
                              {:did-mount (fn [s]
                                 (dommy/remove-class! (sel1 :body) body-class)
                                s)
                               :will-update (fn [s]
                                (let [site-menu-open @(drv/get-ref s :site-menu-open)
                                      body (sel1 [:body])]
                                  (when (responsive/is-mobile-size?)
                                    (if site-menu-open
                                      (when-not (dommy/has-class? body body-class)
                                        (dommy/add-class! body body-class))
                                      (when (dommy/has-class? body body-class)
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
             :on-click (fn [e]
                        (utils/event-stop e)
                        (user/show-login nil)
                        (site-menu-toggle)
                        (router/nav! oc-urls/home))}
            "Home"]]
        [:div.site-mobile-menu-item
          [:a
            {:href oc-urls/pricing
             :class (when (utils/in? (:route @router/path) "pricing") "active")
             :on-click (fn [e]
                        (utils/event-stop e)
                        (dis/dispatch! [:login-overlay-show nil])
                        (site-menu-toggle)
                        (router/nav! oc-urls/pricing))}
            "Pricing"]]
        [:div.site-mobile-menu-item
          [:a
            {:href oc-urls/about
             :class (when (utils/in? (:route @router/path) "about") "active")
             :on-click (fn [e]
                        (.preventDefault e)
                        (user/show-login nil)
                        (site-menu-toggle true)
                        (router/nav! oc-urls/about))}
            "About"]]
        [:div.site-mobile-menu-item
          [:a
            {:href oc-urls/blog
             :target "_blank"}
            "Blog"]]]
      [:div.site-mobile-menu-footer
        (when-not (jwt/jwt)
          [:button.mlb-reset.login-btn
            {:on-click (fn [e]
                        (site-menu-toggle)
                        (if (utils/in? (:route @router/path) "login")
                          (user/show-login :login-with-slack)
                          (router/nav! oc-urls/login)))
             :on-touch-start identity}
            "Login"])
        [:button.mlb-reset.get-started-button
          {:class (when (jwt/jwt) "your-digest")
           :on-touch-start identity
           :on-click (fn [e]
                      (site-menu-toggle)
                      (if (jwt/jwt)
                        (router/redirect! (utils/your-digest-url))
                        (if (utils/in? (:route @router/path) "login")
                          (user/show-login :signup-with-slack)
                          (router/nav! oc-urls/sign-up))))}
          (if (jwt/jwt)
            "Your digest"
            "Get started for free")]]]))