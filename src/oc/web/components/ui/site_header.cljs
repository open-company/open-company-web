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

(defn toggle-menu
  "Helper function called every time this component is mounted, remounted or the menu button is clicked"
  [force-collapse]
  (let [nav (js/$ "nav.navbar-static-top")
        body (js/$ (.-body js/document))]
    ;; If it's forcing the collapse or the menu is already open
    (if (or force-collapse (.hasClass nav "mobile-expanded"))
      (do
        ;; Remove class for expanded menu
        (.removeClass nav "mobile-expanded")
        ;; Remove the overflow hidden for the body
        (.removeClass body "no-scroll")
        ;; Reset the body height to show the full content
        (.css body #js {:height "auto"}))
      (do
        ;; Add the mobile menu expanded class
        (.addClass nav "mobile-expanded")
        ;; Hide the body overflow
        (.addClass body "no-scroll")
        ;; Limit the body height to the screen height
        (.css body #js {:height "100vh"})))))

(rum/defcs site-header < {:did-mount (fn [s] (toggle-menu true) s)
                          :did-remount (fn [_ s] (toggle-menu true) s)}
  [s]
  ; <!-- Nav Bar -->
  [:nav.navbar.navbar-default.navbar-static-top
    [:div.container-fluid
      [:div.navbar-header
        [:a.navbar-brand {:href oc-urls/home :on-click #(do (.preventDefault %) (router/nav! oc-urls/home))}]
        [:button.navbar-toggle.collapsed
          {:type "button"
           :data-toggle "collapse"
           :data-target "#oc-navbar-collapse"
           :on-click #(toggle-menu false)}
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