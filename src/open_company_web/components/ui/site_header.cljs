(ns open-company-web.components.ui.site-header
  (:require [rum.core :as rum]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.login-button :refer (login-button)]))

(rum/defc site-header []
  ; <!-- Nav Bar -->
  [:nav.navbar.navbar-default.navbar-static-top
    [:div.container-fluid
      [:div.navbar-header
        [:a.navbar-brand {:href oc-urls/home :on-click #(do (utils/event-stop %) (router/nav! oc-urls/home))}
          [:img {:alt "OpenCompany" :src "img/oc-wordmark.svg"}]]
        [:button.navbar-toggle.collapsed {:type "button" :data-toggle "collapse" :data-target "#oc-navbar-collapse"}
            [:span.sr-only "Toggle navigation"]
            [:span.icon-bar]
            [:span.icon-bar]
            [:span.icon-bar]]]
      [:div.collapse.navbar-collapse {:id "oc-navbar-collapse"}
        [:ul.nav.navbar-nav.navbar-right.navbar-top
          (when-not (utils/in? (:route @router/path) "home")
            [:li
                [:a.navbar-item {:href oc-urls/home :on-click #(do (utils/event-stop %) (router/nav! oc-urls/home))} "Home"]])
          [:li
              [:a.navbar-item {:href oc-urls/pricing :on-click #(do (utils/event-stop %) (router/nav! oc-urls/pricing))} "Pricing"]]
          [:li
              [:a.navbar-item {:href oc-urls/about :on-click #(do (utils/event-stop %) (router/nav! oc-urls/about))} "About"]]
          [:li.mobile-only
            [:a.navbar-item.contact {:href (str "mailto:" oc-urls/contact-email)} "Contact"]]
          [:li
            (if (jwt/jwt)
              [:a {:href "" :on-click #(do (utils/event-stop %) (dis/dispatch! [:logout]))} "Log Out"]
              (login-button {:button-classes "navbar-item"}))]]]]])