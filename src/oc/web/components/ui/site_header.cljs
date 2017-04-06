(ns oc.web.components.ui.site-header
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.login-button :refer (login-button)]))

(rum/defc site-header []
  ; <!-- Nav Bar -->
  [:nav.navbar.navbar-default.navbar-static-top
    [:div.container-fluid
      [:div.navbar-header
        [:a.navbar-brand {:href oc-urls/home :on-click #(do (.preventDefault %) (router/nav! oc-urls/home))}
          [:img {:alt "OpenCompany" :src "https://d1wc0stj82keig.cloudfront.net/img/oc-wordmark.svg"}]]
        [:button.navbar-toggle.collapsed {:type "button" :data-toggle "collapse" :data-target "#oc-navbar-collapse"}
            [:span.sr-only "Toggle navigation"]
            [:span.icon-bar]
            [:span.icon-bar]
            [:span.icon-bar]]]
      [:div.collapse.navbar-collapse {:id "oc-navbar-collapse"}
        [:ul.nav.navbar-nav.navbar-right.navbar-top
          (when-not (utils/in? (:route @router/path) "home")
            [:li
                [:a.navbar-item {:href oc-urls/home :on-click #(do (.preventDefault %) (router/nav! oc-urls/home))} "Home"]])
          [:li
              [:a.navbar-item {:href oc-urls/pricing :on-click #(do (.preventDefault %) (router/nav! oc-urls/pricing))} "Pricing"]]
          [:li
              [:a.navbar-item {:href oc-urls/about :on-click #(do (.preventDefault %) (router/nav! oc-urls/about))} "About"]]
          [:li.mobile-only
            [:a.navbar-item.contact {:href (str "mailto:" oc-urls/contact-email)} "Contact"]]
          [:li
            (if (jwt/jwt)
              [:a {:href "" :on-click #(do (utils/event-stop %) (dis/dispatch! [:logout]))} "Log Out"]
              (login-button {:button-classes "navbar-item"}))]]]]])