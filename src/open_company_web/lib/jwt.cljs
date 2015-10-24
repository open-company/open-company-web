(ns open-company-web.lib.jwt
  (:require [open-company-web.lib.cookies :as cook]))

(defn jwt []
  (cook/get-cookie :jwt))

(defn decode [jwt]
  (when (.-jwt_decode js/window)
    (.jwt_decode js/window jwt)))

(defn get-key [k]
  (if-let [decoded-jwt (js->clj (decode (jwt)))]
    (decoded-jwt (name k))))