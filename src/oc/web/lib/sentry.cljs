(ns oc.web.lib.sentry
  (:require [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.jwt :as jwt]
            [cljsjs.sentry-browser]
            [taoensso.timbre :as timbre]))

(defonce sentry-hub (atom nil))

(defn init-parameters [dsn]
  #js {:whitelistUrls ls/local-whitelist-array
       :tags #js {:isMobile (responsive/is-mobile-size?)
                  :hasJWT (not (not (jwt/jwt)))}
       :sourceRoot ls/web-server
       :release ls/deploy-key
       :debug (= ls/log-level "debug")
       :dsn dsn})

(defn sentry-setup []
  (when (and (exists? js/Sentry) ls/local-dsn)
    (timbre/info "Setup Sentry")
    (let [sentry-params (init-parameters ls/local-dsn)
          client (js/Sentry.BrowserClient. #js {:dsn ls/local-dsn})
          hub (js/Sentry.Hub. client)]
      (timbre/debug "Sentry params:" (-> sentry-params js->clj (dissoc :dsn) clj->js js/JSON.stringify))
      ; (.init js/Sentry sentry-params)
      (.configureScope js/Sentry (fn [scope]
        (.setTag scope "isMobile" (responsive/is-mobile-size?))
        (.setTag scope "hasJWT" (not (not (jwt/jwt))))
        (when (jwt/jwt)
          (timbre/debug "Set Sentry user:" (jwt/get-key :user-id))
          (.setUser scope (clj->js {:user-id (jwt/get-key :user-id)
                                    :id (jwt/get-key :user-id)
                                    :first-name (jwt/get-key :first-name)
                                    :last-name (jwt/get-key :last-name)})))))
      (reset! sentry-hub hub))))

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

(defn test-sentry []
  (js/setTimeout #(capture-message! "Message from clojure") 1000)
  (try
    (throw (js/errorThrowingCode.))
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
    (let [err (js/Error. (or error-message error-name))]
      (set! (.-name err) (or error-name "Error"))
      (capture-error! err)))))

(defn capture-error-with-message [error-name & [error-message]]
  (timbre/info "Capture error:" error-name "message:" error-message)
  (let [err (js/Error. (or error-message error-name))]
    (set! (.-name err) (or error-name "Error"))
    (capture-error! err)))