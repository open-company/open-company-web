(ns oc.web.lib.cookies
  (:import [goog.net Cookies]
           [goog.string format])
  (:require [taoensso.timbre :as timbre]
            [oc.web.local-settings :as ls]
            ["jwt-decode" :as jwt-decode]
            [oc.web.utils.sentry :as sentry]))

(def default-cookie-expire (* 60 60 24 6))

(def ^{:export true} expGoogCookies Cookies)

(defonce ^{:private true :export true} --cookies (atom nil))

(defn ^:export setup! []
  (timbre/debug "Creating Cookies instance...")
  (js/console.log "DBG Cookies:" Cookies)
  (js/console.log "DBG Cookies.getInstance:" (.-getInstance Cookies))
  (js/console.log "DBG Cookies.getInstance():" (.getInstance Cookies))
  (let [c (.getInstance Cookies)]
    (timbre/debug "Done..." c)
    (js/console.log "DBG c:" c)
    (reset! --cookies c)))

(defn ^:export singleton []
  (if @--cookies
    @--cookies
    (setup!)))

(defn- cookie-name [c-name]
  (str ls/cookie-name-prefix (name c-name)))

(defonce default-expire -1)
(defonce default-path "/")

(defonce max-cookie-length Cookies/MAX_COOKIE_LENGTH)

(defn- calc-length [cval]
  (when (string? cval)
    (count cval)))

(defn- jwt-value [cname cval]
  (try
    (when (= :jwt (keyword cname))
      (-> (jwt-decode cval)
          (js->clj :keywordize-keys true)))
    (catch js/Object _
      nil)))

(defn- check-length [cname cval]
  (let [value-length (calc-length cval)]
    (when (> value-length max-cookie-length)
      (timbre/warnf "Max cookie allowed size (%d) exceeded: %s is %d" max-cookie-length cname value-length)
      (sentry/capture-message-with-extra-context! {:max-cookie-length max-cookie-length
                                                   :current-length value-length
                                                   :user (:user-id (jwt-value cname cval))
                                                   :cookie-name (if (keyword? cname)
                                                                 (name cname)
                                                                 (str cname))}
                                                   "Cookie value exceeds max allowed length"))))

(defn- cookie-options [c-max-age c-path c-domain c-secure]
  (clj->js {:sameSite true
            :secure c-secure
            :domain c-domain
            :path c-path
            :maxAge c-max-age}))

(defn- cookie-expiration-date [c-max-age]
  (js/Date. (+ (.getTime (js/Date.)) (* c-max-age 1000))))

(defn- retry [f]
  (.setTimeout js/window #(f true) 100))

(defn ^:export set-cookie!
  ([c-name c-value]
   (set-cookie! c-name c-value default-expire default-path ls/jwt-cookie-domain ls/jwt-cookie-secure))
  ([c-name c-value c-max-age]
   (set-cookie! c-name c-value c-max-age default-path ls/jwt-cookie-domain ls/jwt-cookie-secure))
  ([c-name c-value c-max-age c-path]
   (set-cookie! c-name c-value c-max-age c-path ls/jwt-cookie-domain ls/jwt-cookie-secure))
  ([c-name c-value c-max-age c-path c-domain c-secure & [retrying?]]
   (timbre/debug (format "Setting cookie \"%s\" with expiration %s (%d seconds from now). Value: %s" c-name (cookie-expiration-date c-max-age) c-max-age c-value))
   (check-length c-name (str c-value))
   (if-let [c (singleton)]
     (.set c (cookie-name c-name) c-value (cookie-options c-max-age c-path c-domain c-secure))
     (when-not retrying?
       (retry (partial set-cookie! c-name c-value c-max-age c-path c-domain c-secure))))))

(defn ^:export get-cookie
  "Get a cookie with the name provided pre-fixed by the environment."
  [c-name]
  (when-let [c (singleton)]
    (.get c (cookie-name c-name))))

(defn ^:export remove-cookie!
  "Remove a cookie with the name provided pre-fixed by the environment."
  ([c-name]
   (remove-cookie! (name c-name) default-path))
  
  ([c-name c-path & [retrying?]]
   (timbre/debugf "Removing cookie %s with path %s" c-name c-path)
   (if-let [c (singleton)]
     (.remove c (cookie-name c-name) c-path ls/jwt-cookie-domain)
     (when-not retrying?
       (retry (partial remove-cookie! c-name c-path))))))