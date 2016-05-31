(ns open-company-web.components.menu
  (:require [cljs.core.async :refer (put!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.cookies :as cook]))

(defn logout-click [e]
  (.preventDefault e)
  (.stopPropagation e)
  (cook/remove-cookie! :jwt)
  (.reload js/location))

(defn close-menu []
  (put! (utils/get-channel "close-side-menu") {:close true}))

(defn profile-click [e]
  (.preventDefault e)
  (.stopPropagation e)
  (close-menu)
  (router/nav! oc-urls/user-profile))

(defn company-profile-click [e]
  (.preventDefault e)
  (.stopPropagation e)
  (close-menu)
  (router/nav! (oc-urls/company-profile)))

(defcomponent menu [data owner options]
  (render [_]
    (dom/ul {:id "menu"}
      (dom/li {:class "oc-title"} "OpenCompany")
      (when (jwt/jwt)
        (dom/li {:class "menu-link"} (dom/a {:title "PROFILE" :href oc-urls/user-profile :on-click profile-click} "PROFILE")))
      (when (and (router/current-company-slug)
                 (not (utils/in? (:route @router/path) "profile")))
        (dom/li {:class "menu-link"} (dom/a {:title "COMPANY PROFILE" :href (oc-urls/company-profile) :on-click company-profile-click} "COMPANY PROFILE")))
      (when (jwt/jwt)
        (dom/li {:class "menu-link"} (dom/a {:title "SIGN OUT" :href oc-urls/logout :on-click logout-click} "SIGN OUT")))
      (when-not (jwt/jwt)
        (dom/li {:class "menu-link"} (dom/a {:title "SIGN IN / SIGN UP" :href oc-urls/login} "SIGN IN / SIGN UP")))
      ; (dom/li {} (dom/a {:title "ABOUT US" :href oc-urls/about} "ABOUT US"))
      (dom/li {:class "menu-link"} (dom/a {:title "CONTACT" :href oc-urls/contact-mail-to} "CONTACT")))))