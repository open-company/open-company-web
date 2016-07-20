(ns open-company-web.components.ui.login-button
  (:require [rum.core :as rum]
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

(rum/defc login-button < rum/static
  [auth-settings]
  [:button.btn-reset
   {:on-click #(login! (:extended-scopes-url auth-settings) %)}
   [:img {:src "https://api.slack.com/img/sign_in_with_slack.png"}]])
