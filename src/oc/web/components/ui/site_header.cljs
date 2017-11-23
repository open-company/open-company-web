(ns oc.web.components.ui.site-header
  "Component for the site header. This is copied into oc.core/nav
   and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.try-it-form :refer (get-started-button)]))

(defn navigate-to-your-boards []
  (router/redirect! (utils/your-boards-url)))

(defn nav! [uri e]
  (.preventDefault e)
  (when (responsive/is-mobile-size?)
    (dis/dispatch! [:site-menu-toggle true]))
  (dis/dispatch! [:login-overlay-show nil])
  (router/nav! uri))

(rum/defc site-header < rum/static
  []
  ; <!-- Nav Bar -->
  [:nav.site-navbar
    [:div.site-navbar-container
      [:a.navbar-brand-center
        {:href oc-urls/home
         :on-click (partial nav! oc-urls/home)}]
      [:div.site-navbar-left.big-web-only
        [:a
          {:href oc-urls/about
           :on-click (partial nav! oc-urls/about)}
          "About"]
        [:a.big-web-only
          {:href "http://blog.carrot.io"
           :target "_blank"}
          "Blog"]]
      [:div.site-navbar-right.big-web-only
        [:a
          {:href oc-urls/sign-up-with-slack
           :on-click (fn [e]
                       (.preventDefault e)
                       (when (responsive/is-mobile-size?)
                         (dis/dispatch! [:site-menu-toggle true]))
                       (dis/dispatch! [:login-overlay-show :signup-with-slack]))}
          "Get Started"]
        [:a.login
          {:href oc-urls/login
           :on-click (fn [e]
                       (.preventDefault e)
                       (dis/dispatch! [:login-overlay-show :login-with-slack]))}
          "Login"]]
      [:div.mobile-ham-menu.mobile-only
        {:on-click #(dis/dispatch! [:site-menu-toggle])}]]])