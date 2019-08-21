(ns oc.web.lib.sentry
  (:require [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.jwt :as jwt]
            [cljsjs.sentry-browser]))

(defn init-parameters [dsn]
  #js {:whitelistUrls ls/local-whitelist-array
       :tags #js {:isMobile (responsive/is-mobile-size?)
                  :hasJWT (not (not (jwt/jwt)))}
       :sourceRoot ls/web-server
       :release ls/deploy-key
       :debug true
       :dsn dsn})

(defn sentry-setup []
  (when (and (exists? js/Sentry) ls/local-dsn)
    (.init js/Sentry (init-parameters ls/local-dsn))
    (when (jwt/jwt)
      (.setUser js/Sentry (clj->js {:user-id (jwt/get-key :user-id)
                                    :id (jwt/get-key :user-id)
                                    :first-name (jwt/get-key :first-name)
                                    :last-name (jwt/get-key :last-name)})))))

(defn test-sentry []
  (js/setTimeout #(.captureMessage js/Sentry "Message from clojure" "info") 1000)
  (try
    (throw (js/errorThrowingCode.))
    (catch :default e
      (.captureException js/Sentry e))))

(defn capture-error!
  ([e]
    (.captureException js/Sentry e))
  ([e error-info]
    (.captureException js/Sentry e #js {:extra error-info})))

(defn capture-message! [msg]
  (.captureMessage js/Sentry msg "info"))

(defn set-extra-context! [scope ctx & [prefix]]
  (doseq [k (keys ctx)]
    (if (map? (get ctx k))
      (set-extra-context! scope (get ctx k) (str prefix (when prefix ":") (name k)))
      (.setExtra scope (str prefix (name k)) (clj->js (get ctx k))))))

(defn capture-message-with-extra-context! [ctx message]
  (.withScope js/Sentry (fn [scope]
    (set-extra-context! scope ctx)
    (capture-message! message))))

(defn capture-error-with-extra-context! [ctx error-name & [error-message]]
  (.withScope js/Sentry (fn [scope]
    (set-extra-context! scope ctx)
    (let [err (js/Error. (or error-message error-name))]
      (set! (.-name err) (or error-name "Error"))
      (capture-error! err)))))

(defn capture-error-with-message [error-name & [error-message]]
  (let [err (js/Error. (or error-message error-name))]
    (set! (.-name err) (or error-name "Error"))
    (capture-error! err)))

(defn set-user-context! [ctx]
  (.setUser js/Sentry (when ctx (clj->js ctx))))