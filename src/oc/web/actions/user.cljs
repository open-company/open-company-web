(ns oc.web.actions.user
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.local_settings :as ls]))

;; Logout

(defn logout []
  (cook/remove-cookie! :jwt)
  (router/redirect! "/")
  (dis/dispatch! [:logout]))

(defn logout-click [e]
  (utils/event-stop e)
  (utils/after 100 #(dis/dispatch! [:mobile-menu-toggle]))
  (logout))

;; JWT

(defn update-jwt [jwt]
  (timbre/info jwt)
  (when jwt
    (utils/after 1 #(dis/dispatch! [:jwt jwt]))))

(defn jwt-refresh []
  (api/jwt-refresh update-jwt logout))

;; Login

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

;; Auth

(defn bot-auth []
  (dis/dispatch! [:bot-auth]))

(defn auth-with-token-failed [error]
  (dis/dispatch! [:auth-with-token/failed error]))

(defn auth-with-token-success [jwt]
  (api/get-entry-point)
  (api/get-auth-settings)
  (when (= (:auth-with-token-type @dis/app-state) :password-reset)
    (cook/set-cookie! :show-login-overlay "collect-password"))
  (dis/dispatch! [:auth-with-token/success jwt]))

(defn auth-with-token-callback
  [success status body]
  (if success
    (do
      (update-jwt body)
      (auth-with-token-success body))
    (cond
      (= status 401)
      (auth-with-token-failed 401)
      :else
      (auth-with-token-failed 500))))

(defn auth-with-token [token-type]
  (api/auth-with-token (:token (:query-params @router/path)) auth-with-token-callback)
  (dis/dispatch! [:auth-with-token token-type]))

;; Signup

(defn signup-with-email-failed [status]
  (dis/dispatch! [:signup-with-email/failed status]))

(defn signup-with-email-success
  [status jwt]
  (cond
    (= status 204) ;; Email wall since it's a valid signup w/ non verified email address
    (utils/after 10 #(router/nav! (str oc-urls/email-wall "?e=" (:email (:signup-with-email @dis/app-state)))))
    (= status 200) ;; Valid login, not signup, redirect to home
    (if (or
          (and (empty? (:first-name jwt)) (empty? (:last-name jwt)))
          (empty? (:avatar-url jwt)))
      (do
        (utils/after 200 #(router/nav! oc-urls/sign-up-profile))
        (api/get-entry-point))
      (do
        (api/get-entry-point)
        (dis/dispatch! [:input [:email-lander-check-team-redirect] true])))
    :else ;; Valid signup let's collect user data
    (do
      (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
      (cook/set-cookie!
       (router/show-nux-cookie (jwt/user-id))
       (:new-user router/nux-cookie-values)
       (* 60 60 24 7))
      (utils/after 200 #(router/nav! oc-urls/sign-up-profile))
      (api/get-entry-point)
      (dis/dispatch! [:signup-with-email/success]))))

(defn signup-with-email-callback
  [success body status]
  (if success
    (signup-with-email-success status body)
    (signup-with-email-failed status)))

(defn signup-with-email []
  (api/signup-with-email
   (or (:firstname (:signup-with-email @dis/app-state)) "")
   (or (:lastname (:signup-with-email @dis/app-state)) "")
   (:email (:signup-with-email @dis/app-state))
   (:pswd (:signup-with-email @dis/app-state))
   signup-with-email-callback)
  (dis/dispatch! [:signup-with-email]))

(defn signup-with-email-data [signup-with-email-data]
  (dis/dispatch! [:input [:signup-with-email] signup-with-email-data]))

;; Debug

(defn force-jwt-refresh []
  (when (jwt/jwt) (jwt-refresh)))

(set! (.-OCWebForceRefreshToken js/window) force-jwt-refresh)