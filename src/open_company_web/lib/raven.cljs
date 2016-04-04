(ns open-company-web.lib.raven
  (:require [open-company-web.local-settings :as ls]
            [cljsjs.raven]))

(def whitelistUrls #js {:whitelistUrls ls/local-whitelist-array})

(defn raven-setup []
  (when (and (exists? js/Raven) ls/local-dsn)
    (.. js/Raven (config ls/local-dsn whitelistUrls) install)))

(defn test-raven []
  (js/setTimeout #(.captureMessage js/Raven "Message from clojure" 1000))
  (try
    (throw (js/errorThrowingCode.))
    (catch :default e
      (.captureException js/Raven e))))