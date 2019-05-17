(ns oc.web.utils.dom
  (:require [dommy.core :as dommy :refer-macros (sel1)]))

(defn lock-page-scroll
  "Add no-scroll class to the page body tag to lock the scroll"
  []
  (dommy/add-class! (sel1 [:body]) :no-scroll))

(defn unlock-page-scroll
  "Remove no-scroll class from the page body tag to unlock the scroll"
  []
  (dommy/remove-class! (sel1 [:body]) :no-scroll))