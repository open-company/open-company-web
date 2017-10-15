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
            [oc.web.components.ui.try-it-form :refer (get-started-button)]))

(def body-class "mobile-menu-expanded")

(defn toggle-menu
  "Helper function called every time this component is mounted, remounted or the menu button is clicked"
  [force-collapse]
  (let [body (js/$ (.-body js/document))]
    ;; If it's forcing the collapse or the menu is already open
    (if (or force-collapse (.hasClass body body-class))
      (.removeClass body body-class)
      (.addClass body body-class))))

(rum/defcs site-header < {:did-mount (fn [s] (toggle-menu true) s)}
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
          ; [:li
          ;   {:class (if (utils/in? (:route @router/path) "pricing") "active" "")}
          ;   [:a.navbar-item {:href oc-urls/pricing :on-click #(do (.preventDefault %) (router/nav! oc-urls/pricing))} "Pricing"]]
          [:li
            {:class (if (utils/in? (:route @router/path) "features") "active" "")}
            [:a.navbar-item
              {:href oc-urls/features
               :on-click #(do (.preventDefault %) (router/nav! oc-urls/features))}
              "Features"]]
          [:li
            {:class (if (utils/in? (:route @router/path) "about") "active" "")}
            [:a.navbar-item
              {:href oc-urls/about
               :on-click #(do (.preventDefault %) (router/nav! oc-urls/about))}
              "About"]]
          [:li
            [:a.navbar-item {:href oc-urls/blog :target "_blank"} "Blog"]]
          (when-not (jwt/jwt)
            [:li
              [:a.navbar-item {:href oc-urls/login
                               :on-click #(do (utils/event-stop %) (dis/dispatch! [:login-overlay-show :login-with-slack]))} "Login"]])
          [:li.get-started-item
            [:div.get-started-button.navbar-item
              (if (jwt/jwt)
                [:button.mlb-reset.mlb-get-started {:on-click #(router/nav! oc-urls/login)} "Your Boards"]
                [:button.mlb-reset.mlb-get-started {:on-click #(router/nav! oc-urls/sign-up-with-slack)} "Sign Up"])]]]]]])