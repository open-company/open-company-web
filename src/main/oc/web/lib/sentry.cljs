(ns oc.web.lib.sentry
  (:require [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.jwt :as jwt]
            [oops.core :refer (oget ocall)]
            ["@sentry/browser" :as sentry-browser]
            [taoensso.timbre :as timbre]))

(defn init-parameters [dsn]
  {:whitelistUrls ls/local-whitelist-array
   :tags {:isMobile (responsive/is-mobile-size?)
          :hasJWT (not (not (jwt/jwt)))
          :deployKey ls/deploy-key}
   :sourceRoot ls/web-server
   :release ls/sentry-release
   :deploy ls/sentry-release-deploy
   :debug (= ls/log-level "debug")
   :dsn dsn
   :environment ls/sentry-env})

(defn sentry-setup []
  (when (and (fn? (oget sentry-browser "init")) ls/local-dsn)
    (timbre/info "Setup Sentry")
    (let [sentry-params (init-parameters ls/local-dsn)]
      (ocall sentry-browser "init" (clj->js sentry-params))
      (timbre/debug "Sentry params:" sentry-params)
      (.configureScope ^js sentry-browser (fn [scope]
        (.setTag ^js scope "isMobile" (responsive/is-mobile-size?))
        (.setTag ^js scope "hasJWT" (not (not (jwt/jwt))))
        (when (jwt/jwt)
          (timbre/debug "Set Sentry user:" (jwt/get-key :user-id))
          (.setUser ^js scope (clj->js {:user-id (jwt/get-key :user-id)
                                    :id (jwt/get-key :user-id)
                                    :first-name (jwt/get-key :first-name)
                                    :last-name (jwt/get-key :last-name)}))))))))

(defn- custom-error [error-name error-message]
  (let [err (js/Error. error-message)]
    (set! (.-name ^js err) error-name)
    err))

(defn capture-error!
  ([e]
    (timbre/info "Capture error:" e)
    (.captureException ^js sentry-browser e))
  ([e error-info]
    (timbre/info "Capture error:" e "extra:" error-info)
    (.captureException ^js sentry-browser e #js {:extra error-info})))

(defn capture-message! [msg & [log-level]]
  (timbre/info "Capture message:" msg)
  (.captureMessage ^js sentry-browser msg (or log-level "info")))

(defn ^:export test-sentry []
  (js/setTimeout #(capture-message! "Message from clojure") 1000)
  (try
    (js/errorThrowingCode.)
    (catch :default e
      (capture-error! e))))

(defn set-extra-context! [scope ctx & [prefix]]
  (doseq [k (keys ctx)]
    (if (map? (get ctx k))
      (set-extra-context! scope (get ctx k) (str prefix (when (seq prefix) "|") (name k)))
      (.setExtra ^js scope (str prefix (when (seq prefix) "|") (name k)) (get ctx k)))))

(defn capture-message-with-extra-context! [ctx message]
  (timbre/info "Capture message:" message "with context:" ctx)
  (.withScope ^js sentry-browser (fn [scope]
    (set-extra-context! scope ctx)
    (capture-message! message))))

(defn capture-error-with-extra-context! [ctx error-name & [error-message]]
  (timbre/info "Capture error:" error-name "message:" error-message "with context:" ctx)
  (.withScope ^js sentry-browser (fn [scope]
    (set-extra-context! scope ctx)
    (try
      (throw (custom-error (or error-message error-name) (if error-message error-name "Error")))
      (catch :default e
        (capture-error! e))))))

(defn ^:export capture-error-with-message [error-name & [error-message]]
  (timbre/info "Capture error:" error-name "message:" error-message)
  (try
    (throw (custom-error (or error-message error-name) (if error-message error-name "Error")))
    (catch :default e
      (capture-error! e))))