(ns open-company-web.lib.cookies
  (:require [goog.net.cookies :as cks]))

(def cookies-static-obj (goog.net.Cookies. js/document))

(defn set-cookie!
  ([c-name c-value]
    (set-cookie! c-name c-value -1))
  ([c-name c-value expiracy]
    (.set cookies-static-obj (name c-name) c-value expiracy))
  ([c-name c-value expiracy c-path c-domain c-secure]
    (if (= c-domain "localhost")
      (.set cookies-static-obj (name c-name) c-value expiracy c-path)
      (.set cookies-static-obj (name c-name) c-value expiracy c-path c-domain c-secure))))

(defn get-cookie [c-name]
  (.get cookies-static-obj (name c-name)))