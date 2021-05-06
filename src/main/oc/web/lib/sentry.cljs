(ns oc.web.lib.sentry
  (:require [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.jwt :as jwt]
            [cuerdas.core :as s]
            [oops.core :refer (oget ocall)]
            ["@sentry/browser" :as sentry-browser]
            ["@sentry/tracing" :as sentry-tracing]
            [taoensso.timbre :as timbre]
            [oc.web.utils.sentry :as sentry-utils]))

(def p? (comp not s/empty-or-nil?))

(def BrowserTracing (oget sentry-tracing "Integrations.BrowserTracing"))

(defn ^:export init-parameters []
  (cond-> {:tags {:isMobile (responsive/is-mobile-size?)
                  :hasJWT (not (not (jwt/jwt)))}
           :sourceRoot ls/web-server
           :integrations (clj->js [(BrowserTracing.)])
           :debug (= ls/log-level "debug")
           :dsn ls/local-dsn
           :tracesSampleRate 1.0
           :normalizeDepth 6}
    (p? ls/sentry-release) (assoc :release ls/sentry-release)
    (and (p? ls/sentry-release)
         (p? ls/deploy-key)) (assoc-in [:tags :deploy-key] ls/deploy-key)
    (and (p? ls/sentry-release)
         (p? ls/deploy-key)) (merge {:deploy ls/deploy-key
                                     :dist ls/deploy-key
                                     :distribution ls/deploy-key})
    (p? ls/sentry-env) (assoc :environment ls/sentry-env)))

(defn sentry-setup []
  (if (seq ls/local-dsn)
    (timbre/warnf "Empty Sentry DSN: %s" ls/local-dsn)
    (do
      (timbre/debug "Setting up Sentry...")
      (ocall sentry-browser "init" (clj->js (init-parameters)))
      (timbre/debug "Configuring Sentry scope")
      (.configureScope ^js sentry-browser (fn [scope]
                                            (.setTag ^js scope "isMobile" (responsive/is-mobile-size?))
                                            (.setTag ^js scope "hasJWT" (not (not (jwt/jwt))))
                                            (when (jwt/jwt)
                                              (timbre/debugf "Set Sentry user: %s" (jwt/user-id))
                                              (.setUser ^js scope (clj->js {:user-id (jwt/user-id)
                                                                            :id (jwt/user-id)
                                                                            :first-name (jwt/get-key :first-name)
                                                                            :last-name (jwt/get-key :last-name)}))))))))

(def capture-error! sentry-utils/capture-error!)

(def capture-message! sentry-utils/capture-message!)

(defn ^:export test-sentry []
  (js/setTimeout #(sentry-utils/capture-message! "Message from clojure") 1000)
  (try
    (js/errorThrowingCode.)
    (catch :default e
      (sentry-utils/capture-error! e))))

(def set-extra-context! sentry-utils/set-extra-context!)

(def capture-message-with-extra-context! sentry-utils/capture-message-with-extra-context!)

(def capture-error-with-extra-context! sentry-utils/capture-error-with-extra-context!)

(def capture-error-with-message! sentry-utils/capture-error-with-message!)
