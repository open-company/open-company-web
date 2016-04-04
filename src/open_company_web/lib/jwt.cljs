(ns open-company-web.lib.jwt
  (:require [open-company-web.lib.cookies :as cook]))

(defn jwt []
  (cook/get-cookie :jwt))

(defn decode [encoded-jwt]
  (when (exists? js/jwt_decode)
    (js/jwt_decode encoded-jwt)))

(defn get-key [k]
  (when-let [decoded-jwt (js->clj (decode (jwt)))]
    (decoded-jwt (name k))))