(ns oc.web.actions.jwt
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.lib.logrocket :as logrocket]))

;; Logout

(defn logout
  ([]
     (logout oc-urls/home))
  ([location]
     (cook/remove-cookie! :jwt)
     (router/redirect! location)
     (dis/dispatch! [:logout])))

;; JWT

(defn update-jwt-cookie [jwt]
  (cook/set-cookie! :jwt jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure))

(defn dispatch-jwt []
  (when (and (cook/get-cookie :show-login-overlay)
             (not= (cook/get-cookie :show-login-overlay) "collect-name-password")
             (not= (cook/get-cookie :show-login-overlay) "collect-password"))
    (cook/remove-cookie! :show-login-overlay))
  (let [jwt-contents (jwt/get-contents)
        email (:email jwt-contents)]
    (utils/after 1 #(dis/dispatch! [:jwt jwt-contents]))
    (when (and (exists? js/drift)
               email)
      (utils/after 1 #(.identify js/drift (:user-id jwt-contents)
                        (clj->js {:nickname (:name jwt-contents)
                                  :email email}))))
    (when jwt-contents
      (logrocket/identify))))

(defn update-jwt [jbody]
  (timbre/info jbody)
  (when jbody
    (update-jwt-cookie jbody)
    (dispatch-jwt)))

(defn jwt-refresh
  ([]
    (api/jwt-refresh update-jwt logout))
  ([success-cb]
    (api/jwt-refresh #(do (update-jwt %) (success-cb)) logout)))