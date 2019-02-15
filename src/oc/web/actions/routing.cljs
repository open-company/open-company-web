(ns oc.web.actions.routing
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]))

(defn routing [route-path]
  (dis/dispatch! [:routing route-path])
  (dis/dispatch! [:container/status (dis/change-cache-data) true]))

(defn maybe-404 []
  (if (jwt/jwt)
    (router/redirect-404!)
    (dis/dispatch! [:show-login-wall])))