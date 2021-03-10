(ns oc.web.actions.routing
  (:require [oc.web.urls :as oc-urls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.utils.dom :as du]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.theme :as theme-actions]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.ws.interaction-client :as ws-ic]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.ws.notify-client :as ws-nc]))

(defn pre-routing []
  (theme-actions/pre-routing!))

(defn post-routing []
  ;; Re-dispatch the current change data
  (dis/dispatch! [:container/status (dis/change-data) true])
  (nux-actions/check-nux))

(defn maybe-404
  ([] (maybe-404 false))
  ([force-404?]
  (if (or (jwt/jwt)
          (jwt/id-token)
          force-404?)
    (router/redirect-404!)
    (dis/dispatch! [:show-login-wall]))))

(defn switch-org-dashboard [org]
  (du/force-unlock-page-scroll)
  (dis/dispatch! [:org-nav-out (dis/current-org-slug) (:slug org)])
  (ws-ic/reset-connection!)
  (ws-cc/reset-connection!)
  (ws-nc/reset-connection!)
  (router/nav! (oc-urls/default-landing (:slug org))))

(defn routing! [next-route-map]
  (dis/dispatch! [:routing next-route-map])
  (cmail-actions/maybe-reset-cmail))