(ns open-company-web.lib.raven
    (:require [open-company-web.local-settings :as ls]))

(def whitelistUrls {:whitelistUrls ls/local-whitelist-array})

(def dsn "https://dc083098d1bb49068c78381360b36536@app.getsentry.com/49840")

(defn raven-setup []
  (when (and (exists? js/Raven) dsn)
    (js/console.info "Initializing Raven...")
    (.install (.config js/Raven dsn whitelistUrls))))

(defn test-raven []
  (js/setTimeout #(.captureMessage js/Raven "Message from clojure" 1000))
  (try
    (throw (js/errorThrowingCode.))
    (catch :default e
      (.captureException js/Raven e))))