(ns oc.web.lib.cookies
  (:require [taoensso.timbre :as timbre]
            [oc.web.local-settings :as ls]
            ["jwt-decode" :as jwt-decode]
            [oops.core :refer (ocall)]
            [oc.web.utils.sentry :as sentry])
  (:import [goog.net Cookies]))

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

(def default-cookie-expire (* 60 60 24 6))

(def ^:export cookies-static-obj (Cookies. js/document))

(defn- ^:private cookie-name [c-name]
  (str ls/cookie-name-prefix (name c-name)))

(def default-expire -1)
(def default-path "/")

(defn ^:export set-cookie!
  ([c-name c-value]
    (set-cookie! c-name c-value default-expire default-path ls/jwt-cookie-domain ls/jwt-cookie-secure))
  ([c-name c-value expiry]
    (set-cookie! c-name c-value expiry default-path ls/jwt-cookie-domain ls/jwt-cookie-secure))
  ([c-name c-value expiry c-path]
    (set-cookie! c-name c-value expiry c-path ls/jwt-cookie-domain ls/jwt-cookie-secure))
  ([c-name c-value expiry c-path c-domain c-secure]
   (check-length c-name (str c-value))
   (ocall cookies-static-obj "set" (cookie-name c-name) c-value expiry c-path c-domain c-secure)))

(defn ^:export get-cookie
  "Get a cookie with the name provided pre-fixed by the environment."
  [c-name]
  (ocall cookies-static-obj "get" (cookie-name c-name)))

(defn remove-cookie!
  "Remove a cookie with the name provided pre-fixed by the environment."
  ([c-name]
   (remove-cookie! (name c-name) "/"))
  
  ([c-name opt-path]
   (ocall cookies-static-obj "remove" (cookie-name c-name) opt-path ls/jwt-cookie-domain)))