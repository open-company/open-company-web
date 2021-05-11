(ns oc.web.lib.cookies
  (:require [taoensso.timbre :as timbre]
            [oc.web.local-settings :as ls]
            ["jwt-decode" :as jwt-decode]
            [oops.core :refer (ocall)]
            [oc.web.utils.sentry :as sentry])
  (:import [goog.net Cookies]
           [goog.string format]))

(def default-cookie-expire (* 60 60 24 6))

(def ^{:private true :export true} --cookies (atom nil))

(defn ^:export cookies []
  (when-not @--cookies
    (timbre/debug "Creating Cookies instance...")
    (let [c (ocall Cookies "getInstance")]
      (timbre/debug "Done..." c)
      (reset! --cookies c)))
  (timbre/debug "Return cookies instance:" @--cookies)
  @--cookies)

(defn- cookie-name [c-name]
  (str ls/cookie-name-prefix (name c-name)))

(def default-expire -1)
(def default-path "/")

(def max-cookie-length Cookies/MAX_COOKIE_LENGTH)

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

(defn ^:export set-cookie!
  ([c-name c-value]
   (set-cookie! c-name c-value default-expire default-path ls/jwt-cookie-domain ls/jwt-cookie-secure))
  ([c-name c-value c-max-age]
   (set-cookie! c-name c-value c-max-age default-path ls/jwt-cookie-domain ls/jwt-cookie-secure))
  ([c-name c-value c-max-age c-path]
   (set-cookie! c-name c-value c-max-age c-path ls/jwt-cookie-domain ls/jwt-cookie-secure))
  ([c-name c-value c-max-age c-path c-domain c-secure]
   (timbre/debug (format "Setting cookie \"%s\" with expiration %s (%d seconds from now). Value: %s" c-name (cookie-expiration-date c-max-age) c-max-age c-value))
   (check-length c-name (str c-value))
   (when-let [c (cookies)]
     (ocall c "set" (cookie-name c-name) c-value (cookie-options c-max-age c-path c-domain c-secure)))))

(defn ^:export get-cookie
  "Get a cookie with the name provided pre-fixed by the environment."
  [c-name]
  (when-let [c (cookies)]
    (ocall c "get" (cookie-name c-name))))

(defn ^:export remove-cookie!
  "Remove a cookie with the name provided pre-fixed by the environment."
  ([c-name]
   (remove-cookie! (name c-name) "/"))
  
  ([c-name opt-path]
   (timbre/debug "Removing cookie" c-name)
   (when-let [c (cookies)]
     (ocall c "remove" (cookie-name c-name) opt-path ls/jwt-cookie-domain))))