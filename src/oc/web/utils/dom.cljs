(ns oc.web.utils.dom
  (:require [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.router :as router]
            [oc.web.lib.responsive :as responsive]))

(defn lock-page-scroll
  "Add no-scroll class to the page body tag to lock the scroll"
  []
  (when (and (responsive/is-mobile-size?)
             (nil? (:back-y @router/path)))
    (swap! router/path assoc :back-y (.. js/document -scrollingElement -scrollTop))
    (set! (.. js/document -scrollingElement -scrollTop) 0))
  (dommy/add-class! (sel1 [:body]) :no-scroll))

(defn unlock-page-scroll
  "Remove no-scroll class from the page body tag to unlock the scroll"
  []
  (dommy/remove-class! (sel1 [:body]) :no-scroll)
  (when (responsive/is-mobile-size?)
    (let [old-scroll-top (or (:back-y @router/path) 0)]
      (swap! router/path dissoc :back-y)
      (set! (.. js/document -scrollingElement -scrollTop) old-scroll-top))))