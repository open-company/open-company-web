(ns oc.web.components.ui.menu
  (:require [cljs.core.async :refer (put!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel sel1)]
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
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/updates-list))))

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

(defn org-settings-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/org-settings))))

(defn um-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/org-team-settings))))

(defn updates-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:mobile-menu-toggle])
  (router/nav! (oc-urls/updates-list)))

(defn sign-in-sign-up-click [e]
  (dis/dispatch! [:mobile-menu-toggle])
  (.preventDefault e)
  (dis/dispatch! [:show-login-overlay :login-with-slack]))

(defn list-boards-click [e]
  (dis/dispatch! [:mobile-menu-toggle])
  (.preventDefault e)
  (router/nav! (oc-urls/boards)))

(def nav-height 69)
(def menu-item-height 38)

(defn setup-mobile-nav-height [owner]
  (when (responsive/is-mobile-size?)
    (let [mobile-menu-open (om/get-props owner :mobile-menu-open)
          nav-item (sel1 [:nav])
          menu-items-count (count (sel [:ul.menu :li.oc-menu-item]))
          final-nav-height (str (+ nav-height
                                   (when mobile-menu-open (+ (* menu-item-height menu-items-count) 14)))
                                "px")]
      (dommy/set-style! nav-item :height final-nav-height))))

(defcomponent menu [{:keys [mobile-menu-open]} owner options]

  (did-mount [_]
    (setup-mobile-nav-height owner))

  (did-update [_ _ _]
    (setup-mobile-nav-height owner))

  (render [_]
    (let [menu-classes (str "menu"
                         (if (responsive/is-mobile-size?)
                            (when mobile-menu-open " mobile-menu-open")
                            " dropdown-menu"))
          org-data (dis/org-data)]
      (dom/ul {:class menu-classes
               :aria-labelledby "dropdown-toggle-menu"}
        (when-let [su-link (utils/link-for (:links org-data) "collection" "GET")]
          (when (router/current-org-slug)
            (dom/li {:class "oc-menu-item menu-separator"}
              (dom/a {:href (oc-urls/updates-list) :on-click prior-updates-click} "View Shared Updates"))))
        (when (and (router/current-org-slug)
                   (not (utils/in? (:route @router/path) "boards-list"))
                   (responsive/is-tablet-or-mobile?))
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href (oc-urls/org) :on-click list-boards-click} "Boards List")))
        (when (and (router/current-org-slug)
                   (jwt/is-admin? (:team-id org-data)))
          (dom/li {:class "oc-menu-item"}
            (dom/a {:href (oc-urls/org-team-settings) :on-click um-click} "Manage Team")))
        (when (and (router/current-org-slug)
                   (not (:read-only org-data))
                   (not (responsive/is-mobile-size?)))
          (dom/li {:class "oc-menu-item menu-separator"}
            (dom/a {:href (oc-urls/org-settings) :on-click org-settings-click} "Company Settings")))
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