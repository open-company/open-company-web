(ns oc.web.lib.jwt
  (:require [oc.web.lib.cookies :as cook]
            [taoensso.timbre :as timbre]
            [oc.lib.schema :as lib-schema]
            [oc.lib.time :as lib-time]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [cljs-time.coerce :as tc]
            [cljs-time.core :as t]
            [schema.core :as schema]
            [oc.lib.cljs.interval :as interval]
            [oops.core :refer (oget)]
            ["jwt-decode" :as jwt-decode]))

(defonce ^:private -jwt (atom nil))
(defonce ^:private -jwt-content (atom nil))

(defonce ^:private -id-token (atom nil))
(defonce ^:private -id-token-content (atom nil))

(def ^:private jwt-cookie-name :jwt)
(def ^:private id-token-cookie-name :id-token)

(def ^:private auto-updater (atom nil))

;; jwt_decode

(defn ^:export decode [encoded-jwt]
  (try
    (jwt-decode encoded-jwt)
    (catch js/Object e
      (timbre/warn "Failed attempt to decode JWT:" encoded-jwt)
      nil)))

;; Read ID Token

(defn- read-id-token-cookie []
  (cook/get-cookie id-token-cookie-name))

(defn ^:export id-token []
  (if @-id-token
    @-id-token
    (reset! -id-token (read-id-token-cookie))))

(defn get-id-token-contents []
  (if @-id-token-content
    @-id-token-content
    (reset! -id-token-content
            (some-> (id-token)
                    decode
                    (js->clj :keywordize-keys true)))))

(defn- refresh-id-token []
  (reset! -id-token nil)
  (reset! -id-token-content nil)
  (id-token)
  (get-id-token-contents))

;; Read JWT

(defn- read-jwt-cookie []
  (cook/get-cookie jwt-cookie-name))

(defn  ^:export jwt []
  (if @-jwt
    @-jwt
    (reset! -jwt (read-jwt-cookie))))

(defn ^:export get-contents []
  (if @-jwt-content
    @-jwt-content
    (reset! -jwt-content (some-> (jwt) decode (js->clj :keywordize-keys true)))))

(def token-refreshed-key [:token-refreshed?])

(defn- ^:export refresh-jwt []
  (reset! -jwt nil)
  (reset! -jwt-content nil)
  (jwt)
  (get-contents))

(defn jwt-refreshed []
  (dis/dispatch! [:input token-refreshed-key true]))

;; Validation

(defn ^:export valid?
  ([token-claims]
  (lib-schema/valid? lib-schema/ValidJWTClaims token-claims))
  ([]
   (valid? (get-contents))))

(defn ^:export refresh?
  ([]
   (or (not (valid?))
       (and (dis/query-param :force-refresh-jwt)
            (not (get-in @dis/app-state token-refreshed-key))))))

;; Write/delete ID Token

(defn set-id-token! [id-token-data]
  (cook/set-cookie! id-token-cookie-name id-token-data -1 (oget js/window "location.pathname") ls/jwt-cookie-domain ls/jwt-cookie-secure)
  (id-token)
  (get-id-token-contents))

(defn remove-id-token! []
  (cook/remove-cookie! id-token-cookie-name)
  (reset! -id-token nil)
  (reset! -id-token-content nil))

;; Write/delete JWT

(defn set-jwt! [jwt]
  (interval/stop-interval! auto-updater)
  (cook/set-cookie! jwt-cookie-name jwt (* 60 60 24 60) "/" ls/jwt-cookie-domain ls/jwt-cookie-secure)
  (refresh-jwt)
  (interval/start-interval! auto-updater))

(defn remove-jwt! []
  (timbre/debug "Remove jwt")
  (interval/stop-interval! auto-updater)
  (timbre/debug "- removing cookie")
  (cook/remove-cookie! jwt-cookie-name)
  (timbre/debug "- removing local version")
  (reset! -jwt nil)
  (timbre/debug "- removing local content")
  (reset! -jwt-content nil)
  (timbre/debug "- restart update interval")
  (interval/start-interval! auto-updater))

;; Keys

(defn ^:export get-key [k]
  (let [contents (if (jwt) (get-contents) (get-id-token-contents))]
    (get contents k)))

(defn ^:export expired? []
  (let [epoch (get-key :expire)
        ;; local-expired? (= expire (gd/min (js/Date.) expire))
        ]
    (not (t/before? (t/now) (tc/from-long epoch)))))

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
    (some (fn [[k v]] (when (= (slack-bots-team-key team-id) k) v)) slack-bots)))

(defn init []
  (refresh-jwt)
  (refresh-id-token)
  ;; Refresh the JWT every second
  (interval/start-interval! auto-updater))

(defn ^:export premium? [team-id]
  (when team-id
    (let [team-id-kw (keyword team-id)]
      (some->> (get-key :premium-teams)
               (map keyword)
               set
               team-id-kw
               boolean))))

(some->> ["123" "321"] (map keyword) set :123 boolean)

(defn before? [iso-date]
  (when iso-date
    (let [date (lib-time/from-iso iso-date)
          token-created-at (or (get-key :token-created-at) 0) ;; fallback to 0 = refresh
          jwt-created (lib-time/from-millis token-created-at)]
      (t/before? jwt-created date))))