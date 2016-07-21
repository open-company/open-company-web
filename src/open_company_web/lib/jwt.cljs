(ns open-company-web.lib.jwt
  (:require [open-company-web.lib.cookies :as cook]
            [goog.date.DateTime :as gdt]
            [goog.date :as gd]))

(defn jwt []
  (cook/get-cookie :jwt))

(defn decode [encoded-jwt]
  (when (exists? js/jwt_decode)
    (js/jwt_decode encoded-jwt)))

(defn get-contents []
  (some-> (jwt) decode js->clj))

(defn get-key [k]
  (-> (get-contents) (get (name k))))

(defn expired? []
  (let [expire (gdt/fromTimestamp (get-key :expire))]
    (= expire (gd/min (js/Date.) expire))))