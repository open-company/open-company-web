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
  (let [body (sel1 [:body])]
    (when (dommy/has-class? body :no-scroll)
      (dommy/remove-class! (sel1 [:body]) :no-scroll)
      (when (responsive/is-mobile-size?)
        (let [old-scroll-top (or (:back-y @router/path) 0)]
          (swap! router/path dissoc :back-y)
          (set! (.. js/document -scrollingElement -scrollTop) old-scroll-top))))))

(defn is-element-top-in-viewport?
   "Given a DOM element return true if it's actually visible in the viewport."
  [el & [offset]]
  (let [fixed-offset (or offset 0)
        rect (.getBoundingClientRect el)
        zero-pos? #(or (zero? %)
                       (pos? %))
        doc-element (.-documentElement js/document)
        win-height (or (.-clientHeight doc-element)
                       (.-innerHeight js/window))]
           ;; Item top is more then the navbar height
      (and (>= (+ (.-top rect) fixed-offset) responsive/navbar-height)
           ;; and less than the screen height
           (< (- (.-top rect) fixed-offset) win-height))))