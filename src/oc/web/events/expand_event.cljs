(ns oc.web.events.expand-event
  (:require [goog :as g]
            [goog.events :as events]))

(def EXPAND "stream-item-expand")

(defn ExpandEvent [expanding?]
  (this-as this
    (.call events/Event this EXPAND)
    (set! (.-expanding this) expanding?)))
(g/inherits ExpandEvent events/Event)

(defn ExpandEventTarget []
  (this-as this
    (.call events/EventTarget this)))
(g/inherits ExpandEventTarget events/EventTarget)

(def expand-event-target (ExpandEventTarget.))