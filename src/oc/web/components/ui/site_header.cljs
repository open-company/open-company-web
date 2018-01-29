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

;; NB: this has a clone in oc.core/nav, every change should be reflected there and vice-versa

(defn nav! [uri e]
  (.preventDefault e)
  (when (responsive/is-mobile-size?)
    (dis/dispatch! [:site-menu-toggle true]))
  (user/show-login nil)
  (router/nav! uri))

(rum/defc site-header < rum/static
  []
  ; <!-- Nav Bar -->
  (let [logged-in (jwt/jwt)
        your-boards (when logged-in (utils/your-boards-url))]
    [:nav.site-navbar
      [:div.site-navbar-container
        [:a.navbar-brand-left
          {:href oc-urls/home
           :on-click (partial nav! oc-urls/home)}]
        [:div.site-navbar-right.big-web-only
          (when-not logged-in
            [:a.login
              {:href (utils/your-boards-url)
               :class (when (jwt/jwt) "your-boards")
               :on-click (fn [e]
                           (.preventDefault e)
                           (if (jwt/jwt)
                             (nav! (utils/your-boards-url) e)
                             )
                           (user/show-login :login-with-slack))}
                "Log in"])
          [:a.start
            {:href (if logged-in your-boards oc-urls/sign-up)
             :class (when logged-in "your-boards")
             :on-click (fn [e]
                         (.preventDefault e)
                         (if logged-in
                          (nav! your-boards e)
                          (nav! oc-urls/sign-up e)))}
            (if logged-in
              [:span
                [:img.user-avatar
                  {:src (jwt/get-key :avatar-url)}]
                [:span "Continue to posts"]]
              "Start")]]
        [:div.site-navbar-right.mobile-only
          (if logged-in
            [:a.mobile-your-boards
              {:href your-boards
               :on-click (partial nav! your-boards)}
              [:img.user-avatar
                {:src (jwt/get-key :avatar-url)}]]
            [:a.start
              {:href oc-urls/sign-up
               :on-click (fn [e]
                           (.preventDefault e)
                           (if (jwt/jwt)
                             (nav! your-boards e)
                             )
                           (user/show-login :login-with-slack))}
                "Start"])]
        [:div.mobile-ham-menu.mobile-only
          {:on-click #(dis/dispatch! [:site-menu-toggle])}]]]))