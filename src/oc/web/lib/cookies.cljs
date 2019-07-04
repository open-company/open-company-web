(ns oc.web.lib.cookies
  (:require [goog.net.cookies :as cks]
            [oc.web.local-settings :as ls]))

(def default-cookie-expire (* 60 60 24 6))

(def cookies-static-obj (goog.net.Cookies. (.-document js/window)))

(defn- ^:private cookie-name [c-name]
  (str ls/cookie-name-prefix (name c-name)))

(defn set-cookie!
  ([c-name c-value]
    (set-cookie! c-name c-value -1))
  ([c-name c-value expiry]
    (set-cookie! c-name c-value expiry "/"))
  ([c-name c-value expiry c-path]
    (set-cookie! c-name c-value expiry c-path ls/jwt-cookie-domain ls/jwt-cookie-secure))
  ([c-name c-value expiry c-path c-domain c-secure]
    (.set cookies-static-obj (cookie-name c-name) c-value expiry c-path c-domain c-secure)))

(defn get-cookie [c-name]
  (.get cookies-static-obj (cookie-name c-name)))

(defn remove-cookie!
  ([c-name]
    (remove-cookie! (name c-name) "/"))
  ([c-name opt-path]
    (.remove cookies-static-obj (cookie-name c-name) opt-path ls/jwt-cookie-domain)))