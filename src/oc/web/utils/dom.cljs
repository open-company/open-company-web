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
  (swap! _lock-counter dec)
  (when-not (pos? @_lock-counter)
    (reset! _lock-counter 0)
    (dommy/remove-class! (sel1 [:html]) :no-scroll)))

(defn is-element-bottom-in-viewport?
   "Given a DOM element return true if the bottom of it is actually visible in the viewport/"
  [el & [offset]]
  (let [fixed-offset (or offset 0)
        rect (.getBoundingClientRect el)
        zero-pos? #(or (zero? %)
                       (pos? %))
        doc-element (.-documentElement js/document)
        win-height (or (.-clientHeight doc-element)
                       (.-innerHeight js/window))]
           ;; Item bottom is more then the navbar height
      (and (>= (+ (.-top rect) (.-height rect) fixed-offset) responsive/navbar-height)
           ;; and less than the screen height
           (< (+ (.-top rect) (.-height rect)) win-height))))

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
           (< (.-top rect) win-height))))

(defn viewport-width []
  (or (.-clientWidth (.-documentElement js/document))
      (.-innerWidth js/window)))

(defn viewport-height []
  (or (.-clientHeight (.-documentElement js/document))
      (.-innerHeight js/window)))

(defn viewport-size []
  {:width (viewport-width)
   :height (viewport-height)})

(defn viewport-offset [element]
  (when element
    (let [rect (.getBoundingClientRect element)]
      {:x (.-left rect)
       :y (.-top rect)})))

(defn event-inside? [e el]
  (loop [element (.-target e)]
    (if element
      (if (= element el)
        true
        (recur (.-parentElement element)))
      false)))
