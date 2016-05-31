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

(defn profile-click [e]
  (.preventDefault e)
  (put! (utils/get-channel "close-side-menu") {:close true})
  (router/nav! oc-urls/user-profile))

(defcomponent menu [_ owner options]
  (render [_]
    (dom/ul {:id "menu"}
      (when (jwt/jwt)
        (dom/li {} (dom/a {:title "PROFILE" :href oc-urls/user-profile :on-click profile-click} "PROFILE")))
      (when (jwt/jwt)
        (dom/li {} (dom/a {:title "SIGN OUT" :href oc-urls/logout :on-click logout-click} "SIGN OUT")))
      (when-not (jwt/jwt)
        (dom/li {} (dom/a {:title "SIGN IN / SIGN UP" :href oc-urls/login} "SIGN IN / SIGN UP")))
      ; (dom/li {} (dom/a {:title "ABOUT US" :href oc-urls/about} "ABOUT US"))
      (dom/li {} (dom/a {:title "CONTACT" :href oc-urls/contact-mail-to} "CONTACT")))))