(ns oc.web.components.ui.site-header
  "Component for the site header. This is copied into oc.core/nav and every change here should be reflected there and vice versa."
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.login-button :refer (get-started-button)]))

(defn toggle-menu [_ expanding]
  ; $('nav.navbar-static-top').toggleClass('mobile-expanded')
  (if expanding
    (.addClass (js/$ "nav.navbar-static-top") "mobile-expanded")
    (.removeClass (js/$ "nav.navbar-static-top") "mobile-expanded"))
  (.css (js/$ (.-body js/document)) #js {:height (if expanding "100vh" "auto")}))

(defn navbar-menu-toggle-event []
  (doto (js/$ ".navbar-collapse")
    (.on "shown.bs.collapse" toggle-menu true)
    (.on "hidden.bs.collapse" toggle-menu false)))

(rum/defcs site-header < {:did-mount (fn [s] (navbar-menu-toggle-event) s)}
  [s]
  ; <!-- Nav Bar -->
  [:nav.navbar.navbar-default.navbar-static-top
    [:div.container-fluid
      [:div.navbar-header
        [:a.navbar-brand {:href oc-urls/home :on-click #(do (.preventDefault %) (router/nav! oc-urls/home))}]
        [:button.navbar-toggle.collapsed {:type "button" :data-toggle "collapse" :data-target "#oc-navbar-collapse"}
            [:span.sr-only "Toggle navigation"]
            [:span.icon-bar]
            [:span.icon-bar]
            [:span.icon-bar]]]
      [:div.collapse.navbar-collapse {:id "oc-navbar-collapse"}
        [:ul.nav.navbar-nav.navbar-right.navbar-top
          [:li.mobile-only
            {:class (if (utils/in? (:route @router/path) "home") "active" "")}
            [:a.navbar-item {:href oc-urls/home :on-click #(do (.preventDefault %) (router/nav! oc-urls/home))} "Home"]]
          [:li
            {:class (if (utils/in? (:route @router/path) "pricing") "active" "")}
            [:a.navbar-item {:href oc-urls/pricing :on-click #(do (.preventDefault %) (router/nav! oc-urls/pricing))} "Pricing"]]
          [:li
            {:class (if (utils/in? (:route @router/path) "about") "active" "")}
            [:a.navbar-item
              {:href oc-urls/about
               :on-click #(do (.preventDefault %) (router/nav! oc-urls/about))}
              "About"]]
          [:li
            [:a.navbar-item {:href oc-urls/blog} "Blog"]]
          [:li.big-web-only
            {:class (if (utils/in? (:route @router/path) "contact") "active" "")}
            [:a.navbar-item.contact {:href (str "mailto:" oc-urls/contact-email)} "Contact"]]
          [:li.get-started-item
            (if (jwt/jwt)
              [:a {:href "" :on-click #(do (utils/event-stop %) (dis/dispatch! [:logout]))} "Log Out"]
              (get-started-button {:button-classes "navbar-item"}))]]]]])