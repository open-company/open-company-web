(ns open-company-web.lib.raven
  (:require [open-company-web.local-settings :as ls]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.jwt :as jwt]
            [cljsjs.raven]))

(defn ravenParameters []
  #js {:whitelistUrls ls/local-whitelist-array
       :tags #js {:isMobile (responsive/is-mobile)
                  :hasJWT (not (not (jwt/jwt)))}
       :release ls/deploy-key})

(defn raven-setup []
  (when (and (exists? js/Raven) ls/local-dsn)
    (.. js/Raven (config ls/local-dsn (ravenParameters)) install)))

(defn test-raven []
  (js/setTimeout #(.captureMessage js/Raven "Message from clojure" 1000))
  (try
    (throw (js/errorThrowingCode.))
    (catch :default e
      (.captureException js/Raven e))))

(defn capture-error [e]
  (.captureException js/Raven e))

(defn capture-message [msg]
  (.captureMessage js/Raven msg))

(defn capture-error-with-message [msg]
  (capture-error (js/Error. msg)))