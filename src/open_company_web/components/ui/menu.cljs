(ns open-company-web.components.ui.menu
  (:require [cljs.core.async :refer (put!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.popover :as popover]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [om-bootstrap.button :as b]))

(defn logout-click [e]
  (utils/event-stop e)
  (utils/after 100 #(dis/dispatch! [:mobile-menu-toggle]))
  (dis/dispatch! [:logout]))

(defn user-profile-click [e]
  (utils/event-stop e)
  (dis/save-last-company-slug)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! oc-urls/user-profile)))

(defn company-profile-click [e]
  (utils/event-stop e)
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/company-settings))))

(defn um-click [e]
  (utils/event-stop e)
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/company-settings-um))))

(defn updates-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (router/nav! (oc-urls/stakeholder-update-list)))

(defcomponent menu [{:keys [mobile-menu-open]} owner options]

  (render [_]
    (let [menu-classes (str "menu"
                         (if (responsive/is-mobile-size?)
                            (when mobile-menu-open " mobile-menu-open")
                            " dropdown-menu"))]
      (dom/ul {:class menu-classes
               :aria-labelledby "dropdown-toggle-menu"}
        (when (jwt/jwt)
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href oc-urls/user-profile :on-click user-profile-click} "User Profile")))
        (when (and (router/current-company-slug)
                   (not (utils/in? (:route @router/path) "user-management"))
                   (not (:read-only (dis/company-data)))
                   (not (responsive/is-mobile-size?)))
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href (oc-urls/company-settings-um) :on-click um-click} "Invite and Manage Team")))
        (when (and (router/current-company-slug)
                   (not (utils/in? (:route @router/path) "profile"))
                   (not (:read-only (dis/company-data)))
                   (not (responsive/is-mobile-size?)))
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href (oc-urls/company-settings) :on-click company-profile-click} "Company Settings")))
        (when (jwt/jwt)
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href oc-urls/logout :on-click logout-click} "Sign Out")))
        (when-not (jwt/jwt)
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href oc-urls/login} "Sign In / Sign Up")))))))