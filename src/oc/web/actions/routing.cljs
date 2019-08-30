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

;; Post modal

(defn close-post-modal
  "The state has been popped, reset the routing so the app re-render the stream.
   Reset the scroll to the previous position if one is saved in the current route object."
  []
  (let [org (router/current-org-slug)
        board (or (:back-to @router/path) (router/current-board-slug))
        query-params (router/query-params)
        route [org board "dashboard"]
        back-y (or (:back-y @router/path) (utils/page-scroll-top))]
    (router/set-route! route {:org org
                              :board board
                              :sort-type (router/current-sort-type)
                              :scroll-y back-y
                              :query-params query-params})
    (set! (.-onpopstate js/window) nil)))

(defn open-post-modal
  "Expand a post, reset the route to add the post in it and add the onpopstate listener.
   Also reset the scroll."
  [activity-data dont-scroll]
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
    (when-not dont-scroll
      (if ua/mobile?
        (utils/after 10 #(utils/scroll-to-y 0 0))
        (utils/scroll-to-y 0 0)))
    ;; Set the onpopstate listener to go back to the board not only
    ;; with the internal button but also with the browser back button
    (set! (.-onpopstate js/window) close-post-modal)
    (.pushState (.-history js/window) #js {} (.-title js/document) post-url)))

(defn dismiss-post-modal
  "Function used by the button to close the expanded post modal."
  []
  ;; Simply call history.back() then the onpopstate will be called automatically
  (.back (.-history js/window)))