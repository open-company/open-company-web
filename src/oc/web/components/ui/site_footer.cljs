(ns oc.web.components.ui.site-footer
  "Component for the site footer. This is copied into oc.core/footer and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.local-settings :as ls]
            [oc.web.lib.utils :as utils]))

(rum/defc site-footer []
  ;; <!-- footer -->
  [:nav.navbar.navbar-default.navbar-bottom
    [:div.container-fluid.group
      [:div.left-column
        [:img.logo
          {:src "/img/ML/carrot_wordmark_white.svg"}]
        [:div.small-links
          [:a "Get started"] "|" [:a "Login"]]
        [:div.small-logos
          [:a.twitter
            {:href oc-urls/oc-twitter}
            [:img {:src "/img/ML/home_page_twitter.svg"}]]
          [:a.medium
            {:href oc-urls/oc-medium}
            [:img {:src "/img/ML/home_page_medium.svg"}]]]
        [:div.copyright "© Copyright 2017. All rights reserved"]]
      [:div.right-column

        [:div.column
          [:div.column-title "SUPPORT"]
          [:div.column-item [:a "Help"]]
          [:div.column-item [:a "Contact"]]]

        [:div.column
          [:div.column-title "INTEGRATIONS"]
          [:div.column-item [:a "Slack"]]
          [:div.column-item [:a "Developers"]]]

        [:div.column
          [:div.column-title "COMPANY"]
          [:div.column-item [:a "About"]]
          [:div.column-item [:a "Blog"]]
          [:div.column-item [:a "Legal"]]]

        [:div.column
          [:div.column-title "TOUR"]
          [:div.column-item [:a "Home"]]
          [:div.column-item [:a "Features"]]
          [:div.column-item [:a "Pricing"]]]]]])