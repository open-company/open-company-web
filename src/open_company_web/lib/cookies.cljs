(ns open-company-web.lib.cookies
  (:require [goog.net.cookies :as cks]))

(def cookies-static-obj (goog.net.Cookies. js/window js/document))

(defn set-cookie!
  ([c-name c-value]
    (set-cookie! c-name c-value -1))
  ([c-name c-value expiracy]
    (println "settings: " c-name c-value)
    (.set cookies-static-obj (name c-name) c-value expiracy)))

(defn get-cookie [c-name]
  (if (keyword? c-name)
    (.get cookies-static-obj (name c-name))
    (.get cookies-static-obj c-name)))