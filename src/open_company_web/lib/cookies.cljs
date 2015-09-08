(ns open-company-web.lib.cookies
  (:require [goog.net.cookies :as cks]
            [shodan.console :as console]))

(def cookies-static-obj (goog.net.Cookies. js/document))

(defn set-cookie!
  ([c-name c-value]
    (set-cookie! c-name c-value -1))
  ([c-name c-value expiracy]
    (console/log "settings: " c-name c-value expiracy)
    (.set cookies-static-obj (name c-name) c-value expiracy "/"))
  ([c-name c-value expiracy c-path c-domain c-secure]
    (console/log "settings: " c-name c-value expiracy c-path c-domain c-secure)
    (.set cookies-static-obj (name c-name) c-value expiracy c-path c-domain (or c-secure false))))

(defn get-cookie [c-name]
  (console/log "Getting: " c-name "->" (.get cookies-static-obj (name c-name)))
  (.get cookies-static-obj (name c-name)))