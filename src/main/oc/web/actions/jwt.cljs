(ns oc.web.actions.jwt
  (:require [taoensso.timbre :as timbre]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.lib.cookies :as cook]
            [oc.web.local-settings :as ls]
            [oc.web.lib.fullstory :as fullstory]))

;; Logout

(defn logout
  ([]
   (logout (if ua/pseudo-native?
             oc-urls/native-login
             oc-urls/home)))
  ([location]
   (jwt/remove-jwt!)
   (router/redirect! location)
   (dis/dispatch! [:logout])))

;; ID Token

(defn dispatch-id-token []
  (when-let [id-token-contents (jwt/get-id-token-contents)]
    (utils/after 1 #(dis/dispatch! [:id-token id-token-contents]))
    ;; User identifications for third party services
    (when id-token-contents
      (fullstory/identify))))


(defn update-id-token [token-body]
  (timbre/info "Updating id-token:" token-body)
  (when token-body
    (jwt/set-id-token! token-body)
    (dispatch-id-token)))

;; JWT

(defn dispatch-jwt []
  (when (and (cook/get-cookie :show-login-overlay)
             (not= (cook/get-cookie :show-login-overlay) "collect-name-password")
             (not= (cook/get-cookie :show-login-overlay) "collect-password"))
    (cook/remove-cookie! :show-login-overlay))
  (let [jwt-contents (jwt/get-contents)]
    (utils/after 1
                 #(do
                    (dis/dispatch! [:jwt jwt-contents])
                    (dis/dispatch! [:org-loaded (dis/org-data)])))
    (dis/dispatch! [:input [dis/show-invite-box-key] (seq (cook/get-cookie (router/show-invite-box-cookie (:user-id jwt-contents))))])
    ;; User identifications for third party services
    (when jwt-contents
      (fullstory/identify))))

(defn update-jwt [jbody]
  (timbre/info "Updating jwt:" jbody)
  (when jbody
    (jwt/set-jwt! jbody)
    (dispatch-jwt)))

(defn jwt-refresh
  ([]
    (api/jwt-refresh update-jwt logout))
  ([success-cb]
    (api/jwt-refresh #(do (update-jwt %) (success-cb)) logout)))
