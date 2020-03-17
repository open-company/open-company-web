(ns oc.web.lib.sentry
  (:require [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.jwt :as jwt]
            [cljsjs.sentry-browser]
            [taoensso.timbre :as timbre]))

(defn init-parameters [dsn]
  {:whitelistUrls ls/local-whitelist-array
   :tags {:isMobile (responsive/is-mobile-size?)
          :hasJWT (not (not (jwt/jwt)))}
   :sourceRoot ls/web-server
   :release ls/deploy-key
   :debug true ;(= ls/log-level "debug")
   :dsn dsn})

(defn sentry-setup []
  (when (and (exists? js/Sentry) ls/local-dsn)
    (timbre/info "Setup Sentry")
    (let [sentry-params (init-parameters ls/local-dsn)]
      (.init js/Sentry (clj->js sentry-params))
      (timbre/debug "Sentry params:" sentry-params)
      (.configureScope js/Sentry (fn [scope]
        (.setTag scope "isMobile" (responsive/is-mobile-size?))
        (.setTag scope "hasJWT" (not (not (jwt/jwt))))
        (when (jwt/jwt)
          (timbre/debug "Set Sentry user:" (jwt/get-key :user-id))
          (.setUser scope (clj->js {:user-id (jwt/get-key :user-id)
                                    :id (jwt/get-key :user-id)
                                    :first-name (jwt/get-key :first-name)
                                    :last-name (jwt/get-key :last-name)}))))))))

(defn- custom-error [error-name error-message]
  (let [err (js/Error. error-message)]
    (set! (.-name err) error-name)
    err))

(defn capture-error!
  ([e]
    (timbre/info "Capture error:" e)
    (.captureException js/Sentry e))
  ([e error-info]
    (timbre/info "Capture error:" e "extra:" error-info)
    (.captureException js/Sentry e #js {:extra error-info})))

(defn capture-message! [msg & [log-level]]
  (timbre/info "Capture message:" msg)
  (.captureMessage js/Sentry msg (or log-level "info")))

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
      (.setExtra scope (str prefix (when (seq prefix) "|") (name k)) (get ctx k)))))

(defn capture-message-with-extra-context! [ctx message]
  (timbre/info "Capture message:" message "with context:" ctx)
  (.withScope js/Sentry (fn [scope]
    (set-extra-context! scope ctx)
    (capture-message! message))))

(defn capture-error-with-extra-context! [ctx error-name & [error-message]]
  (timbre/info "Capture error:" error-name "message:" error-message "with context:" ctx)
  (.withScope js/Sentry (fn [scope]
    (set-extra-context! scope ctx)
    (try
      (throw (custom-error (or error-message error-name) (if error-message error-name "Error")))
      (catch :default e
        (capture-error! e))))))

(defn ^:export capture-error-with-message [error-name & [error-message]]
  (timbre/info "Capture error:" error-name "message:" error-message)
  (js/console.log "DBG capture-error-with-message" error-name error-message)
  (try
    (let [err (custom-error (or error-message error-name) (if error-message error-name "Error"))]
      (js/console.log "DBG    err:" err)
      (throw err))
    (catch :default e
      (js/console.log "DBG   catch:" e)
      (capture-error! e))))