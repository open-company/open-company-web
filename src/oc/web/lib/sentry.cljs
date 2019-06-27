(ns oc.web.lib.sentry
  (:require [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.jwt :as jwt]
            [cljsjs.sentry-browser]))

(defn initParameters [dsn]
  #js {:whitelistUrls ls/local-whitelist-array
       :tags #js {:isMobile (responsive/is-mobile-size?)
                  :hasJWT (not (not (jwt/jwt)))}
       :sourceRoot ls/web-server
       :release ls/deploy-key
       :dsn dsn})

(defn sentry-setup []
  (when (and (exists? js/Sentry) ls/local-dsn)
    (.init js/Sentry (initParameters ls/local-dsn))
    (when (jwt/jwt)
      (.setUser js/Sentry (clj->js {:user-id (jwt/get-key :user-id)
                                    :id (jwt/get-key :user-id)
                                    :first-name (jwt/get-key :first-name)
                                    :last-name (jwt/get-key :last-name)})))))

(defn test-sentry []
  (js/setTimeout #(.captureMessage js/Sentry "Message from clojure" 1000))
  (try
    (throw (js/errorThrowingCode.))
    (catch :default e
      (.captureException js/Sentry e))))

(defn capture-error [e]
  (.captureException js/Sentry e))

(defn capture-message [msg]
  (.captureMessage js/Sentry msg))

(defn capture-error-with-message [error-name & [error-message]]
  (let [err (js/Error. (or error-message error-name))]
    (set! (.-name err) (or error-name "Error"))
    (capture-error err)))

(defn set-user-context! [ctx]
  (.setUserContext js/Sentry (when ctx (clj->js ctx))))

(defn set-extra-context! [ctx]
  (.setExtraContext js/Sentry (when ctx (clj->js ctx))))

(defn clear-extra-context! []
  (.setExtraContext js/Sentry))