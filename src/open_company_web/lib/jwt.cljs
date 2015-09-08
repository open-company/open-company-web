(ns open-company-web.lib.jwt
  (:require [open-company-web.lib.cookies :as cook]))

(defn jwt []
  (cook/get-cookie :jwt))

(defn decode [jwt]
  (.jwt_decode js/window jwt))

(defn get-key [k]
  (let [decoded-jwt (js->clj (decode (jwt)))]
    (decoded-jwt (name k))))