(ns open-company-web.components.ui.login-button
  (:require [rum.core :as rum]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.local-settings :as ls]))

(defn login! [auth-url e]
  (.preventDefault e)
  (dis/dispatch! [:login-with-slack auth-url]))

(rum/defc login-button < rum/static
  [auth-settings]
  [:button.btn-reset
   {:on-click #(login! (:extended-scopes-url (:slack auth-settings)) %)}
   [:img {:src "https://api.slack.com/img/sign_in_with_slack.png"}]])
