(ns oc.web.utils.dom
  (:require [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.router :as router]
            [oc.web.lib.responsive :as responsive]))

(defonce _lock-counter (atom 0))

(defn lock-page-scroll
  "Add no-scroll class to the page body tag to lock the scroll"
  []
  (swap! _lock-counter inc)
  (dommy/add-class! (sel1 [:html]) :no-scroll))

(defn unlock-page-scroll
  "Remove no-scroll class from the page body tag to unlock the scroll"
  []
  (when (pos? @_lock-counter)
    (swap! _lock-counter dec)
    (when (zero? @_lock-counter)
      (dommy/remove-class! (sel1 [:html]) :no-scroll))))

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