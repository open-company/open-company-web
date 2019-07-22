(ns oc.web.actions.routing
  (:require [oc.web.urls :as oc-urls]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.cmail :as cmail-actions]))

(defn routing [route-path]
  (dis/dispatch! [:routing route-path])
  (dis/dispatch! [:container/status (dis/change-data) true]))

(defn maybe-404 []
  (if (jwt/jwt)
    (router/redirect-404!)
    (dis/dispatch! [:show-login-wall])))

;; Post modal

(defn open-post-modal [activity-data scroll-to-top]
  (let [org (router/current-org-slug)
        old-board (router/current-board-slug)
        board (:board-slug activity-data)
        back-to (if (= old-board utils/default-drafts-board-slug)
                  board
                  old-board)
        activity (:uuid activity-data)
        post-url (oc-urls/entry board activity)
        query-params (router/query-params)
        route [org board activity "activity"]
        scroll-y-position (.. js/document -scrollingElement -scrollTop)]
    (router/set-route! route {:org org
                              :board board
                              :sort-type (router/current-sort-type)
                              :activity activity
                              :query-params query-params
                              :back-to back-to
                              :back-y scroll-y-position})
    (cmail-actions/cmail-hide)
    (when-not scroll-to-top
      (utils/scroll-to-y 0 0))
    (.pushState (.-history js/window) #js {} (.-title js/document) post-url)))

(defn dismiss-post-modal []
  (let [org (router/current-org-slug)
        board (or (:back-to @router/path) (router/current-board-slug))
        to-url (oc-urls/board board)
        query-params (router/query-params)
        route [org board "dashboard"]
        back-y (or (:back-y @router/path) 0)]
    (router/set-route! route {:org org
                              :board board
                              :sort-type (router/current-sort-type)
                              :scroll-y back-y
                              :query-params query-params})
    (.pushState (.-history js/window) #js {} (.-title js/document) to-url)))