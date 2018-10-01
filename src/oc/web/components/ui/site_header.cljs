(ns oc.web.components.ui.site-header
  "Component for the site header. This is copied into oc.core/nav
   and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.actions.user :as user]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.components.ui.site-mobile-menu :as site-mobile-menu]
            [oc.web.components.ui.try-it-form :refer (get-started-button)]))

;; NB: this has a clone in oc.core/nav, every change should be reflected there and vice-versa

(defn nav! [uri e]
  (.preventDefault e)
  (when (responsive/is-mobile-size?)
    (site-mobile-menu/site-menu-toggle true))
  (user/show-login nil)
  (router/nav! uri))

(rum/defcs site-header < rum/static
                         (rum/local nil ::scroll-listener)
                         (rum/local false ::sticky-navbar)
                         {:will-mount (fn [s]
                          (reset! (::scroll-listener s)
                           (events/listen js/window EventType/SCROLL
                            #(let [scroll-top (.scrollTop (js/$ js/window))]
                               (reset! (::sticky-navbar s) (pos? scroll-top)))))
                          s)
                          :will-unmount (fn [s]
                           (when @(::scroll-listener s)
                             (events/unlistenByKey @(::scroll-listener s))
                             (reset! (::scroll-listener s) nil))
                           s)}
  [s auth-settings use-slack-signup-button]
  ; <!-- Nav Bar -->
  (let [logged-in (jwt/jwt)
        your-digest (when logged-in (utils/your-digest-url))
        is-slack-lander? (utils/in? (:route @router/path) "slack-lander")
        slack-auth-link (utils/link-for (:links auth-settings) "authenticate" "GET"
                         {:auth-source "slack"})]
    [:nav.site-navbar
      {:class (when @(::sticky-navbar s) "sticky")}
      [:div.site-navbar-container
        [:a.navbar-brand-left
          {:href oc-urls/home-no-redirect
           :class (when is-slack-lander? "is-slack-header")
           :on-click (partial nav! oc-urls/home-no-redirect)}]
        (when-not is-slack-lander?
          [:div.navbar-brand-center
            [:a
              {:href oc-urls/home
               :on-click (partial nav! oc-urls/home)}
              "Home"]
            [:a
              {:href oc-urls/about
               :on-click (partial nav! oc-urls/about)}
              "About"]
            [:a
              {:href oc-urls/pricing
               :on-click (partial nav! oc-urls/pricing)}
              "Pricing"]
            [:a
              {:href oc-urls/blog
               :target "_blank"}
              "Blog"]])
        [:div.site-navbar-right.big-web-only
          (when-not logged-in
            [:a.login
              {:href (utils/your-digest-url)
               :class (when logged-in "your-digest")
               :on-click (fn [e]
                           (.preventDefault e)
                           (if logged-in
                             (nav! (utils/your-digest-url) e)
                             (nav! oc-urls/login e))
                           (user/show-login :login-with-slack))}
                "Login"])
          [:a.start
            {:href (if logged-in
                     your-digest
                     oc-urls/sign-up)
             :class (utils/class-set {:your-digest logged-in
                                      :slack-get-started use-slack-signup-button
                                      :slack is-slack-lander?})
             :on-click (fn [e]
                         (.preventDefault e)
                         (if logged-in
                          (nav! your-digest e)
                          (if use-slack-signup-button
                            (user-actions/login-with-slack slack-auth-link)
                            (nav! oc-urls/sign-up e))))}
            (when (and (not logged-in)
                       use-slack-signup-button
                       is-slack-lander?)
              [:span.slack-orange-icon])
            (if logged-in
              [:span.go-to-digest
                "Launch Carrot"]
              [:span.start-copy
                (if use-slack-signup-button
                  (if is-slack-lander?
                    "Sign in with Slack"
                    "Add to Slack")
                  "Get started")])]]
        [:div.site-navbar-right.mobile-only
          (if use-slack-signup-button
            (if is-slack-lander?
              [:a.start
                {:href "/sign-up"
                 :on-click #(do
                              (.preventDefault %)
                              (if logged-in
                                (nav! your-digest %)
                                (if use-slack-signup-button
                                  (user-actions/login-with-slack slack-auth-link)
                                  (nav! oc-urls/sign-up %))))}
                [:span.slack-orange-icon]
                [:span.slack-copy "SIGN IN"]]
              [:a.start
                {:id "site-header-mobile-signup-item"
                 :href "/sign-up"}
                  [:span.copy "ADD"]])
            [:a.start
              {:id "site-header-mobile-signup-item"
               :href "/sign-up"}
                [:span.copy "START"]])]
        (when-not is-slack-lander?
          [:div.mobile-ham-menu
            {:on-click #(site-mobile-menu/site-menu-toggle)}])]]))