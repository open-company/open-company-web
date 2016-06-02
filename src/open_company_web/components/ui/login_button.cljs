(ns open-company-web.components.ui.login-button
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]))

(defn login! [auth-url e]
  (let [current (router/get-token)]
    (.preventDefault e)
    (when-not (.startsWith current oc-urls/login)
      (cook/set-cookie! :login-redirect current (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
    (set! (.-location js/window) auth-url)))

(defcomponent login-button [{:keys [auth-settings]} _]
  (render [_]
    (dom/button {:class "login-button" :on-click #(login! (:auth-url auth-settings) %)}
      "Sign in / Sign up")))