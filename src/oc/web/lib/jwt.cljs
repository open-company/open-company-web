(ns oc.web.lib.jwt
  (:require [oc.web.lib.cookies :as cook]
            [taoensso.timbre :as timbre]
            [goog.date.DateTime :as gdt]
            [goog.date :as gd]))

(defn jwt []
  (cook/get-cookie :jwt))

(defn decode [encoded-jwt]
  (when (exists? js/jwt_decode)
    (try
      (js/jwt_decode encoded-jwt)
      (catch js/Object e
        (timbre/warn "Failed attempt to decode JWT:" encoded-jwt)
        nil))))

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

(defn is-admin? [team-id]
  (let [admins (get-key :admin)]
    (some #{team-id} admins)))

(defn team-has-bot? [team-id]
  (let [slack-bots (get-key :slack-bots)]
    ;; Since the team-id is a string on clj side the key in slakc-bots has quotes around that are
    ;; interpreted as part of the key from the JSON parser.
    ;; When decode keywordize the keys it keeps them in the key so we need to add them to check
    ;; for the team value
    (some #(= (keyword (str "\"" team-id "\"")) (first %)) slack-bots)))

(set! (.-OCWebPrintJWTContents js/window) #(js/console.log (get-contents)))