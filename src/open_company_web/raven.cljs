(ns open-company-web.raven)

(def dsn "https://eab4fe25a2ab4ea1b1d6e10185d63844@app.getsentry.com/48160")

(def whitelistUrls {:whitelistUrls ["locahost" "127.0.0.1"]})

(defn raven-setup []
  (when js/Raven
    (let [raven (.config js/Raven dsn whitelistUrls)]
      (.install raven))))

(defn test-raven []
  (js/setTimeout #(.captureMessage js/Raven "Message from clojure" 1000))
  (try
    (throw (js/errorThrowingCode.))
    (catch :default e
      (.captureException js/Raven e))))
