(ns oc.web.events.expand-event
  "Expand event triggered by the stream-item component when a
  body is being expanded or collapsed.
  Loosly inspired by https://stackoverflow.com/a/9089803/561744"
  (:require [goog.events :as events]))

(def EXPAND "stream-item-expand")

(defn ExpandEvent [expanding?]
  (this-as this
    (.call events/Event this EXPAND)
    (set! (.-expanding this) expanding?)))
(goog/inherits ExpandEvent events/Event)

(defn ExpandEventTarget []
  (this-as this
    (.call events/EventTarget this)))
(goog/inherits ExpandEventTarget events/EventTarget)

(def expand-event-target (ExpandEventTarget.))