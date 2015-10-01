(ns open-company-web.router
  (:require [secretary.core :as secretary])
  (:import [goog.history Html5History]))

(enable-console-print!)

(def path (atom {}))

(defn set-route! [route parts]
  (reset! path {})
  (swap! path assoc :route route)
  (doseq [[k v] parts] (swap! path assoc k v)))

(defn get-token []
  (str js/window.location.pathname js/window.location.search))

(defn make-history []
  (doto (Html5History.)
    (.setPathPrefix (str js/window.location.protocol
                         "//"
                         js/window.location.host))
    (.setUseFragment false)))

; FIXME: remove the worning of history not found
(defn nav! [token]
  (swap! path {})
  (.setToken open-company-web.core/history token))
