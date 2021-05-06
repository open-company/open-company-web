(ns oc.web.lib.sentry
  (:require [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.jwt :as jwt]
            [oops.core :refer (oget ocall)]
            [cuerdas.core :as s]
            ["@sentry/browser" :as sentry-browser]
            [taoensso.timbre :as timbre]
            [oc.web.utils.sentry :as sentry-utils]))

(def p? (comp not s/empty-or-nil?))

(defn ^:export init-parameters [dsn]
  (cond-> {:tags {:isMobile (responsive/is-mobile-size?)
                  :hasJWT (not (not (jwt/jwt)))}
           :sourceRoot ls/web-server
           :debug (= ls/log-level "debug")
           :dsn dsn
           :normalizeDepth 6}
    (p? ls/sentry-release) (assoc :release ls/sentry-release)
    (and (p? ls/sentry-release)
         (p? ls/deploy-key)) (assoc-in [:tags :deploy-key] ls/deploy-key)
    (and (p? ls/sentry-release)
         (p? ls/deploy-key)) (merge {:deploy ls/deploy-key
                                :dist ls/deploy-key})
    (p? ls/sentry-env) (assoc :environment ls/sentry-env)))

(defn sentry-setup []
  (let [sentry-params (init-parameters ls/local-dsn)]
    (if-not (:dsn sentry-params)
      (timbre/warnf "Empty Sentry DSN: %s" sentry-params)
      (do
        (timbre/debugf "Setup Sentry: %s" sentry-params)
        (ocall sentry-browser "init" (clj->js sentry-params))
        (ocall sentry-browser "configureScope" (fn [scope]
                                                 (ocall scope "setTag" "isMobile" (responsive/is-mobile-size?))
                                                 (ocall scope "setTag" "hasJWT" (not (not (jwt/jwt))))
                                                 (when (jwt/jwt)
                                                   (timbre/debugf "Set Sentry user: %s" (jwt/get-key :user-id))
                                                   (ocall scope "setUser" (clj->js {:user-id (jwt/get-key :user-id)
                                                                                    :id (jwt/get-key :user-id)
                                                                                    :first-name (jwt/get-key :first-name)
                                                                                    :last-name (jwt/get-key :last-name)})))))))))

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
