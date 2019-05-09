(ns oc.web.actions.routing
  (:require [oc.web.urls :as oc-urls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]))

(defn routing [route-path]
  (dis/dispatch! [:routing route-path])
  (dis/dispatch! [:container/status (dis/change-cache-data) true]))

(defn maybe-404 []
  (if (jwt/jwt)
    (router/redirect-404!)
    (dis/dispatch! [:show-login-wall])))

;; Post modal

(defn open-post-modal [activity-data]
  (let [org (router/current-org-slug)
        board (router/current-board-slug)
        activity (:uuid activity-data)
        post-url (oc-urls/entry (:board-slug activity-data) activity)
        query-params (router/query-params)
        route [org board activity "activity"]]
    (router/set-route! route {:org org :board board :activity activity :query-params query-params})
    (.pushState (.-history js/window) #js {} (.-title js/document) post-url)))

(defn dismiss-post-modal []
  (let [org (router/current-org-slug)
        board (router/current-board-slug)
        to-url (oc-urls/board board)
        query-params (router/query-params)
        route [org board "dashboard"]]
    (router/set-route! route {:org org :board board :query-params query-params})
    (.pushState (.-history js/window) #js {} (.-title js/document) to-url)))