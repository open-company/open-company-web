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
  (some-> (jwt) decode (js->clj :keywordize-keys true)))

(defn get-key [k]
  (-> (get-contents) (get k)))

(defn expired? []
  (let [expire (gdt/fromTimestamp (get-key :expire))]
    (= expire (gd/min (js/Date.) expire))))

(defn is-slack-org? []
  (= (get-key :auth-source) "slack"))

(defn team-id []
  (first (get-key :teams)))

(defn is-admin? []
  (let [current-team (open-company-web.router/current-team-id)
        admins (get-key :admin)]
    (some #{current-team} admins)))