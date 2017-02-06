(ns oc.web.components.ui.menu
  (:require [cljs.core.async :refer (put!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.dispatcher :as dis]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.popover :as popover]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [om-bootstrap.button :as b]))

(defn prior-updates-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/stakeholder-update-list))))

(defn logout-click [e]
  (utils/event-stop e)
  (utils/after 100 #(dis/dispatch! [:mobile-menu-toggle]))
  (dis/dispatch! [:logout]))

(defn boards-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/boards (router/current-org-slug)))))

(defn user-profile-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! oc-urls/user-profile)))

(defn board-settings-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/board-settings))))

(defn um-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/team-settings-um))))

(defn updates-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (router/nav! (oc-urls/stakeholder-update-list)))

(defn sign-in-sign-up-click [e]
  (dis/dispatch! [:mobile-menu-toggle])
  (.preventDefault e)
  (dis/dispatch! [:show-login-overlay :login-with-slack]))

(defcomponent menu [{:keys [mobile-menu-open]} owner options]

  (render [_]
    (let [menu-classes (str "menu"
                         (if (responsive/is-mobile-size?)
                            (when mobile-menu-open " mobile-menu-open")
                            " dropdown-menu"))]
      (dom/ul {:class menu-classes
               :aria-labelledby "dropdown-toggle-menu"}
        (when-let [su-link (utils/link-for (:links (dis/board-data)) "stakeholder-updates" "GET")]
          (when (and (router/current-org-slug)
                     (router/current-board-slug)
                     (pos? (:count su-link)))
            (dom/li {:class "oc-menu-item menu-separator"}
              (dom/a {:href (oc-urls/stakeholder-update-list) :on-click prior-updates-click} "View Shared Updates"))))
        (when (and (jwt/is-admin?)
                   (not (:read-only (dis/board-data))))
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href (oc-urls/team-settings-um) :on-click um-click} "Manage Team")))
        (when (and (router/current-org-slug)
                   (router/current-board-slug)
                   (not (:read-only (dis/board-data)))
                   (not (responsive/is-mobile-size?)))
          (dom/li {:class "oc-menu-item menu-separator"}
            (dom/a {:href (oc-urls/board-settings) :on-click board-settings-click} "Board Settings")))
        ;; Temp commenting this out since we need API support to know how many companies the user has
        ; (when (jwt/jwt)
        ;   (dom/li {:class "oc-menu-item"}
        ;     (dom/a {:href (oc-urls/boards (router/current-org-slug)) :on-click companies-click} "Companies")))
        (when (jwt/jwt)
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href oc-urls/user-profile :on-click user-profile-click} "User Profile")))
        (if (jwt/jwt)
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href oc-urls/logout :on-click logout-click} "Sign Out"))
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href "" :on-click sign-in-sign-up-click} "Sign In / Sign Up")))))))