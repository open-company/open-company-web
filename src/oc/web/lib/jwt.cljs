(ns oc.web.lib.jwt
  (:require [oc.web.lib.cookies :as cook]
            [taoensso.timbre :as timbre]
            [goog.date.DateTime :as gdt]
            [goog.date :as gd]
            [cljsjs.jwt-decode]))

;; jwt_decode

(defn decode [encoded-jwt]
  (when (exists? js/jwt_decode)
    (try
      (js/jwt_decode encoded-jwt)
      (catch js/Object e
        (timbre/warn "Failed attempt to decode JWT:" encoded-jwt)
        nil))))

;; ID Token

(defn id-token []
  (cook/get-cookie :id-token))

(defn get-id-token-contents []
  (some-> (id-token) decode (js->clj :keywordize-keys true)))

;; JWT

(defn jwt []
  (cook/get-cookie :jwt))

(defn get-contents []
  (some-> (jwt) decode (js->clj :keywordize-keys true)))

(defn get-key [k]
  (get (get-contents) k))

(defn expired? []
  (let [expire (gdt/fromTimestamp (get-key :expire))]
    (= expire (gd/min (js/Date.) expire))))

(defn is-slack-org? []
  (= (get-key :auth-source) "slack"))

(defn team-id []
  (first (get-key :teams)))

(defn user-id []
  (get-key :user-id))

(defn is-admin? [team-id]
  (let [admins (get-key :admin)]
    (some #{team-id} admins)))

(defn slack-bots-team-key
  "Keys of slack-bots are strings converted to keywords, since JSON adds the double quotes
   when it gets keywordized on this side it becomes :\"team-id\"."
  [team-id]
  (keyword (str "\"" team-id "\"")))

(defn user-is-part-of-the-team [team-id]
  (let [teams (get-key :teams)]
    (some #{team-id} teams)))

(defn team-has-bot? [team-id]
  (let [slack-bots (get-key :slack-bots)]
    ;; Since the team-id is a string on clj side the key in slakc-bots has quotes around that are
    ;; interpreted as part of the key from the JSON parser.
    ;; When decode keywordize the keys it keeps them in the key so we need to add them to check
    ;; for the team value
    (some #(= (slack-bots-team-key team-id) %) (keys slack-bots))))

(set! (.-OCWebPrintJWTContents js/window) #(js/console.log (get-contents)))