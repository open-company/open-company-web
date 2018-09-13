(ns oc.web.lib.raven
  (:require [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.jwt :as jwt]
            [cljsjs.raven]))

(defn ravenParameters []
  #js {:whitelistUrls ls/local-whitelist-array
       :tags #js {:isMobile (responsive/is-mobile-size?)
                  :hasJWT (not (not (jwt/jwt)))}
       :sourceRoot ls/web-server
       :release ls/deploy-key})

(defn raven-setup []
  (when (and (exists? js/Raven) ls/local-dsn)
    (.. js/Raven (config ls/local-dsn (ravenParameters)) install)
    (when (jwt/jwt)
      (.setUserContext js/Raven (clj->js {:user-id (jwt/get-key :user-id)
                                          :id (jwt/get-key :user-id)
                                          :first-name (jwt/get-key :first-name)
                                          :last-name (jwt/get-key :last-name)})))))

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

(defn set-user-context! [ctx]
  (.setUserContext js/Raven (when ctx (clj->js ctx))))