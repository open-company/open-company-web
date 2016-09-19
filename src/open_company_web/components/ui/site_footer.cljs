(ns open-company-web.components.ui.site-footer
  (:require [rum.core :as rum]
            [open-company-web.urls :as oc-urls]))

(rum/defc site-footer []
  ;; <!-- footer -->
  [:nav.navbar.navbar-default.navbar-bottom

      [:ul.nav.navbar-nav.navbar-left.navbar-bottom-left
        [:li [:a.navbar-logo {:href oc-urls/home}
          [:img {:alt "OpenCompany" :src "img/oc-logo-grey.svg"}]]]
        [:li.web-only
            [:a {:href oc-urls/pricing} "Pricing"]]
        [:li.web-only
            [:a {:href oc-urls/about} "About"]]
        [:li
          [:a.contact {:href (str "mailto:" oc-urls/contact-email)} "Contact"]]
        [:li.mobile-only {style {:float "right" :margin-right "15px"}}
          [:a {:href "https://twitter.com/opencompanyhq"}
            [:i.fa.fa-2x.fa-twitter {:aria-hidden "true"}]]]]
      [:ul.nav.navbar-nav.navbar-right
        [:li
          [:a {:href oc-urls/oc-twitter}>
            [:i.fa.fa-2x.fa-twitter {:aria-hidden "true"}]]]
        [:li
          [:a {:href "https://github.com/open-company"}
            [:i.fa.fa-2x.fa-github {:aria-hidden "true"}]]]]])