(ns oc.web.components.ui.site-header
  "Component for the site header. This is copied into oc.core/nav
   and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.user :as user]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.try-it-form :refer (get-started-button)]))

(defn nav! [uri e]
  (.preventDefault e)
  (when (responsive/is-mobile-size?)
    (dis/dispatch! [:site-menu-toggle true]))
  (user/show-login nil)
  (router/nav! uri))

(rum/defc site-header < rum/static
  []
  ; <!-- Nav Bar -->
  [:nav.site-navbar
    [:div.site-navbar-container
      [:a.navbar-brand-left
        {:href oc-urls/home
         :on-click (partial nav! oc-urls/home)}]
      [:div.site-navbar-right.big-web-only
        [:a.login
          {:href (utils/your-boards-url)
           :class (when (jwt/jwt) "your-boards")
           :on-click (fn [e]
                       (.preventDefault e)
                       (if (jwt/jwt)
                         (nav! (utils/your-boards-url) e)
                         (user/show-login :login-with-slack)))}
          (if (jwt/jwt)
            "Your Boards"
            "Log in")]
        (when-not (jwt/jwt)
          [:a.start
            {:href oc-urls/sign-up
             :on-click (fn [e]
                         (.preventDefault e)
                         (when (responsive/is-mobile-size?)
                           (dis/dispatch! [:site-menu-toggle true]))
                         (router/nav! oc-urls/sign-up))}
            "Start"])]
      [:div.mobile-ham-menu.mobile-only
        {:on-click #(dis/dispatch! [:site-menu-toggle])}]]])