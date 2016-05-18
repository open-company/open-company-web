(ns open-company-web.components.menu
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.jwt :as jwt]))

(defcomponent menu [data owner options]
  (render [_]
    (dom/ul {:id "menu"}
      (when (jwt/jwt)
        (dom/li {} (dom/a {:title "PROFILE" :href oc-urls/user-profile} "PROFILE")))
      (when (jwt/jwt)
        (dom/li {} (dom/a {:title "SIGN OUT" :href oc-urls/logout} "SIGN OUT")))
      (when-not (jwt/jwt)
        (dom/li {} (dom/a {:title "SIGN IN / SIGN UP" :href oc-urls/login} "SIGN IN / SIGN UP")))
      ; (dom/li {} (dom/a {:title "ABOUT US" :href oc-urls/about} "ABOUT US"))
      (dom/li {} (dom/a {:title "CONTACT" :href oc-urls/contact-mail-to} "CONTACT")))))