(ns open-company-web.components.ui.login-button
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]))

(defcomponent login-button [data owner]
  (render [_]
    (let [full-url (:full-url (:auth-settings data))
          current-token (router/get-token)]
      (dom/button {:class "login-button"
                   :on-click (fn [e]
                    (.preventDefault e)
                    (when-not (.startsWith current-token oc-urls/login)
                      (cook/set-cookie! :login-redirect current-token (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
                    (set! (.-location js/window) full-url))}
        "Sign in / Sign up"))))