(ns open-company-web.lib.raven
    (:require [open-company-web.local-settings :as ls]))

(def whitelistUrls {:whitelistUrls ls/local-whitelist-array})

(defn raven-setup []
  (when (and (.-Raven js/window) ls/local-dsn)
    (let [raven (.config js/Raven ls/local-dsn whitelistUrls)]
      (.install raven))))

(defn test-raven []
  (js/setTimeout #(.captureMessage js/Raven "Message from clojure" 1000))
  (try
    (throw (js/errorThrowingCode.))
    (catch :default e
      (.captureException js/Raven e))))