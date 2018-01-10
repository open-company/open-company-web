(ns oc.web.actions.user
  (:require [taoensso.timbre :as timbre]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.cookies :as cook]
            [oc.web.api :as api]
            [oc.web.dispatcher :as dis]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.local_settings :as ls]))

(defn update-jwt [jwt]
  (timbre/info jwt)
  (when jwt
    (utils/after 1 #(dis/dispatch! [:jwt jwt]))))

(defn logout []
  (dis/dispatch! [:logout]))

(defn logout-click [e]
  (utils/event-stop e)
  (utils/after 100 #(dis/dispatch! [:mobile-menu-toggle]))
  (logout))

(defn login-with-email-finish
  [success body status]
  (if success
    (do
      (if (empty? body)
        (utils/after 10 #(router/nav! (str oc-urls/email-wall "?e=" (:email (:signup-with-email @dis/app-state)))))
        (do
          (cook/set-cookie! :jwt body (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
          (api/get-entry-point)))
      (dis/dispatch! [:login-with-email/success body]))
    (cond
     (= status 401)
     (dis/dispatch! [:login-with-email/failed 401])
     :else
     (dis/dispatch! [:login-with-email/failed 500]))))

(defn login-with-email [email pswd]
  (api/auth-with-email email pswd login-with-email-finish)
  (dis/dispatch! [:login-with-email]))

(defn login-with-slack []
  (let [current (router/get-token)
        auth-url (utils/link-for (:links (:auth-settings @dis/app-state)) "authenticate" "GET" {:auth-source "slack"})
        auth-url-with-redirect (utils/slack-link-with-state
                                 (:href auth-url)
                                 nil
                                 "open-company-auth" oc-urls/slack-lander-check)
        current-route (:route @router/path)]
    (when (and (not (utils/in? current-route "login"))
               (not (utils/in? current-route "sign-up"))
               (not (utils/in? current-route "slack"))
               (not (utils/in? current-route "about"))
               (not (utils/in? current-route "home"))
               (not (cook/get-cookie :login-redirect)))
      (cook/set-cookie! :login-redirect current (* 60 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))
    (router/redirect! auth-url-with-redirect)
    (dis/dispatch! [:login-with-slack])))

(defn show-login [login-type]
  (dis/dispatch! [:login-overlay-show login-type]))