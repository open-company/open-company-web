(ns oc.web.actions.routing
  (:require [oc.web.urls :as oc-urls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.shared.useragent :as ua]
            [oc.web.actions.cmail :as cmail-actions]))

(defn routing [route-path]
  (dis/dispatch! [:routing route-path])
  (dis/dispatch! [:container/status (dis/change-data) true]))

(defn maybe-404
  ([] (maybe-404 false))
  ([force-404?]
  (if (or (jwt/jwt)
          (jwt/id-token)
          force-404?)
    (router/redirect-404!)
    (dis/dispatch! [:show-login-wall]))))