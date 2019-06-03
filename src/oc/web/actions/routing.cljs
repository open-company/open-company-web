(ns oc.web.actions.routing
  (:require [oc.web.urls :as oc-urls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(defn routing [route-path]
  (dis/dispatch! [:routing route-path])
  (dis/dispatch! [:container/status (dis/change-data) true]))

(defn maybe-404 []
  (if (jwt/jwt)
    (router/redirect-404!)
    (dis/dispatch! [:show-login-wall])))

;; Post modal

(defn open-post-modal [activity-data scroll-to-bottom]
  (let [org (router/current-org-slug)
        old-board (router/current-board-slug)
        board (:board-slug activity-data)
        activity (:uuid activity-data)
        post-url (oc-urls/entry board activity)
        query-params (router/query-params)
        route [org board activity "activity"]
        scroll-y-position (.. js/document -scrollingElement -scrollTop)]
    (when-not scroll-to-bottom
      (utils/scroll-to-y 0 0))
    (router/set-route! route {:org org
                              :board board
                              :activity activity
                              :query-params query-params
                              :back-to old-board
                              :back-y scroll-y-position})
    (.pushState (.-history js/window) #js {} (.-title js/document) post-url)))

(defn dismiss-post-modal []
  (let [org (router/current-org-slug)
        board (or (:back-to @router/path) (router/current-board-slug))
        to-url (oc-urls/board board)
        query-params (router/query-params)
        route [org board "dashboard"]
        back-y (or (:back-y @router/path) 0)]
    (utils/after 0 #(utils/scroll-to-y back-y 0))
    (router/set-route! route {:org org
                              :board board
                              :query-params query-params})
    (.pushState (.-history js/window) #js {} (.-title js/document) to-url)))