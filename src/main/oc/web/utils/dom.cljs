(ns oc.web.utils.dom
  (:require [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.responsive :as responsive]
            [oops.core :refer (oget)]))

(defonce onload-recalc-measure-class "onload-reaclc-measure")

(defonce _lock-counter (atom 0))

(defn lock-page-scroll
  "Add no-scroll class to the page body tag to lock the scroll"
  []
  (swap! _lock-counter inc)
  (dommy/add-class! (sel1 [:html]) :no-scroll))

(defn unlock-page-scroll
  "Remove no-scroll class from the page body tag to unlock the scroll"
  []
  (let [lock-counter (swap! _lock-counter dec)]
    (when-not (pos? lock-counter)
      (reset! _lock-counter 0)
      (dommy/remove-class! (sel1 [:html]) :no-scroll))))

(defn force-unlock-page-scroll []
  (reset! _lock-counter 0)
  (dommy/remove-class! (sel1 [:html]) :no-scroll))

(defn viewport-width []
  (or (oget js/document "documentElement.?clientWidth")
      (oget js/window "innerWidth")))

(defn viewport-height []
  (or (oget js/document "documentElement.?clientHeight")
      (oget js/window "innerHeight")))

(defn viewport-size []
  {:width (viewport-width)
   :height (viewport-height)})

(defn bounding-rect [element]
  (when element
    (let [rect (.getBoundingClientRect element)]
      {:x (oget rect "x")
       :y (oget rect "y")
       :top (oget rect "top")
       :right (oget rect "right")
       :bottom (oget rect "bottom")
       :left (oget rect "left")})))

(defn window-rect []
  (bounding-rect (oget js/document "documentElement")))

(defn window-height []
  (let [wr (window-rect)]
    (- (:bottom wr) (:top wr))))

(defn window-width []
  (let [wr (window-rect)]
    (- (:right wr) (:left wr))))

(defn is-element-bottom-in-viewport?
   "Given a DOM element return true if the bottom of it is actually visible in the viewport/"
  [el & [offset]]
  (let [fixed-offset (or offset 0)
        rect (.getBoundingClientRect el)
        zero-pos? #(or (zero? %)
                       (pos? %))
        doc-element (oget js/document "documentElement")
        win-height (or (oget doc-element "clientHeight")
                       (oget js/window "innerHeight"))]
           ;; Item bottom is more then the navbar height
      (and (>= (+ (oget rect "top") (oget rect "height") fixed-offset) responsive/navbar-height)
           ;; and less than the screen height
           (< (+ (oget rect "top") (oget rect "height")) win-height))))

(defn is-element-top-in-viewport?
   "Given a DOM element return true if it's actually visible in the viewport."
  [el & [offset]]
  (let [fixed-offset (or offset 0)
        rect (.getBoundingClientRect el)
        zero-pos? #(or (zero? %)
                       (pos? %))
        doc-element (oget js/document "documentElement")
        win-height (or (oget doc-element "clientHeight")
                       (oget js/window "innerHeight"))]
           ;; Item top is more then the navbar height
      (and (>= (+ (oget rect "top") fixed-offset) responsive/navbar-height)
           ;; and less than the screen height
           (< (oget rect "top") win-height))))

(defn viewport-offset [element]
  (-> (bounding-rect element)
      (select-keys [:x :y])))

(defn event-inside? [e el]
  (loop [element (oget e "target")]
    (if element
      (if (= element el)
        true
        (recur (oget element "parentElement")))
      false)))

(defn event-cotainer-has-class [e class-name]
  (when e
    (loop [element (oget e "target")]
      (if element
        (if (.contains (oget element "classList") (if (keyword? class-name) (name class-name) class-name))
          true
          (recur (oget element "parentElement")))
        false))))

(defn is-hidden [el]
  (when (and (oget el "?nodeType")
             (= (oget el "?nodeType") (oget js/Node "ELEMENT_NODE")))
    (when-let [style (.getComputedStyle js/window el)]
      (or (= (oget style "?display") "none")
          (= (oget style "?visibility") "hidden")
          (nil? (oget el "?offsetParent"))))))
