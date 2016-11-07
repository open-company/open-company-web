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
            [open-company-web.components.prior-updates :refer (prior-updates)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [om-bootstrap.button :as b]))

(defn logout-click [e]
  (utils/event-stop e)
  (dis/dispatch! [:logout]))

(defn user-profile-click [e]
  (utils/event-stop e)
  (dis/save-last-company-slug)
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! oc-urls/user-profile)))

(defn company-profile-click [e]
  (utils/event-stop e)
  (utils/after (+ utils/oc-animation-duration 100) #(router/nav! (oc-urls/company-settings))))

(defn prior-updates-click [e]
  (utils/event-stop e)
  (utils/after (+ utils/oc-animation-duration 100)
    (if (responsive/is-mobile-size?)
      #(router/nav! (oc-urls/stakeholder-update-list)) ; nav. to prior updates page
      (popover/add-popover-with-rum-component prior-updates {:container-id "prior-updates-dialog"})))) ; popover

(defcomponent menu [data owner options]

  (render [_]
    (dom/ul {:class "dropdown-menu" :aria-labelledby "dropdown-toggle-menu"}
      (when (jwt/jwt)
        (dom/li {:class "oc-menu-item"}
          (dom/a {:title "PRIOR UPDATES" :href (oc-urls/stakeholder-update-list) :on-click prior-updates-click} "PRIOR UPDATES")))
      (when (jwt/jwt)
        (dom/li {:class "oc-menu-item"}
          (dom/a {:title "USER INFO" :href oc-urls/user-profile :on-click user-profile-click} "USER INFO")))
      (when (and (router/current-company-slug)
                 (not (utils/in? (:route @router/path) "profile"))
                 (not (:read-only (dis/company-data)))
                 (not (responsive/is-mobile-size?)))
        (dom/li {:class "oc-menu-item"}
          (dom/a {:title "COMPANY SETTINGS" :href (oc-urls/company-settings) :on-click company-profile-click} "COMPANY SETTINGS")))
      (when (jwt/jwt)
        (dom/li {:class "oc-menu-item"}
          (dom/a {:title "SIGN OUT" :href oc-urls/logout :on-click logout-click} "SIGN OUT")))
      (when-not (jwt/jwt)
        (dom/li {:class "oc-menu-item"}
          (dom/a {:title "SIGN IN / SIGN UP" :href oc-urls/login} "SIGN IN / SIGN UP"))))))