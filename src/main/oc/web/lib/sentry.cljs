(ns oc.web.lib.sentry
  (:require [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.jwt :as jwt]
            [oops.core :refer (oget ocall)]
            ["@sentry/browser" :as sentry-browser]
            [taoensso.timbre :as timbre]
            [oc.web.utils.sentry :as sentry-utils]))

(defn init-parameters [dsn]
  {:tags {:isMobile (responsive/is-mobile-size?)
          :hasJWT (not (not (jwt/jwt)))
          :deployKey ls/deploy-key}
   :sourceRoot ls/web-server
   :release ls/sentry-release
   :deploy ls/sentry-release-deploy
   :debug (= ls/log-level "debug")
   :dsn dsn
   :normalizeDepth 6
   :environment ls/sentry-env})

(defn sentry-setup []
  (let [sentry-params (init-parameters ls/local-dsn)]
    (if-not (:dsn sentry-params)
      (timbre/warnf "Empty Sentry DSN: %s" sentry-params)
      (do
        (timbre/debugf "Setup Sentry: %s" sentry-params)
        (ocall sentry-browser "init" (clj->js sentry-params))
        (.configureScope ^js sentry-browser (fn [scope]
                                              (.setTag ^js scope "isMobile" (responsive/is-mobile-size?))
                                              (.setTag ^js scope "hasJWT" (not (not (jwt/jwt))))
                                              (when (jwt/jwt)
                                                (timbre/debugf "Set Sentry user: %s" (jwt/get-key :user-id))
                                                (.setUser ^js scope (clj->js {:user-id (jwt/get-key :user-id)
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
