(ns oc.web.local-settings)

;; Sentry
(def local-dsn "https://747713ae92c246d1a64bbce9aab3da34@app.getsentry.com/73174") ; insert your Sentry public dsn here
(def local-whitelist-array ["localhost" "127.0.0.1"])

;; Change this with your machine ip address to test
;; from a device on the same network
(def local-ip "localhost")

;; Storage location
(def web-server-domain (str "http://" local-ip ":3559"))

;; Storage location
(def storage-server-domain (str "http://" local-ip ":3001"))

;; Auth location
(def auth-server-domain (str "http://" local-ip ":3003"))

;; Pay location
(def pay-server-domain (str "http://" local-ip ":3004"))

;; Interaction location
(def interaction-server-domain (str "http://" local-ip ":3002"))

;; Change location
(def change-server-domain (str "http://" local-ip ":3006"))

;; Search location
(def search-server-domain (str "http://" local-ip ":3007"))
(def search-enabled? true)

;; Reminder location
(def reminder-server-domain (str "http://" local-ip ":3011"))

;; Web location
(def web-server (str local-ip ":3559"))

;; JWT
(def jwt-cookie-domain local-ip)
(def jwt-cookie-secure false)

;; Deploy key (cache buster)
(def deploy-key "")

;; Filestack key
(def filestack-key "Aoay0qXUSOyVIcDvls4Egz")

;; Cookie prefix
(def cookie-name-prefix (str local-ip "-"))

;; Log level
(def log-level "debug")

;; CDN URL
(def cdn-url "")

;; Attachments bucket
(def attachments-bucket "open-company-attachments-non-prod")

;; AP seen TTL in days
(def oc-seen-ttl 30)

;; Ziggeo
(def oc-ziggeo-app "c9b611b2b996ee5a1f318d3bacc36b27")
(def oc-ziggeo-profiles [])
(def oc-enable-transcriptions false)

;; WS monitor
(def ws-monitor-interval 30)

;; Giphy
(def giphy-api-key "M2FfNXledXWbpa7FZkg2vvUD8kHMTQVF")

;; Image upload limit
(def file-upload-size (* 20 1024 1024))

(def mac-app-url "https://github.com/open-company/open-company-web/releases/download/untagged-a060f76d2ed11d47ff35/Carrot.dmg")
(def win-app-url "https://github.com/open-company/open-company-web/releases/download/untagged-a060f76d2ed11d47ff35/Carrot.exe")
(def iphone-app-url "https://")
(def android-app-url "https://play.google.com/apps/testing/io.carrot.mobile")