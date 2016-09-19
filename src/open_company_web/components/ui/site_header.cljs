(ns open-company-web.components.ui.site-header
  (:require [rum.core :as rum]
            [open-company-web.urls :as oc-urls]))

(rum/defc site-header []
  ; <!-- Nav Bar -->
  [:nav.navbar.navbar-default.navbar-static-top
    [:div.container-fluid
      [:div.navbar-header
        [:a.navbar-brand {:href oc-urls/home}
          [:img {:alt "OpenCompany" :src "img/oc-wordmark.svg"}]]
        [:button.navbar-toggle.collapsed {:type "button" :data-toggle "collapse" :data-target "#oc-navbar-collapse"}
            [:span.sr-only "Toggle navigation"]
            [:span.icon-bar]
            [:span.icon-bar]
            [:span.icon-bar]]]
      [:div.collapse.navbar-collapse {:id "oc-navbar-collapse"}
        [:ul.nav.navbar-nav.navbar-right.navbar-top
            [:li
                [:a {:href oc-urls/home} "Home"]]
            [:li
                [:a {:href oc-urls/pricing} "Pricing"]]
            [:li
                [:a {:href oc-urls/about} "About"]]
            [:li.mobile-only
              [:a.contact {:href (str "mailto:" oc-urls/contact-email)} "Contact"]]]]]])