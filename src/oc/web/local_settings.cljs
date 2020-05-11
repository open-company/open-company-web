(ns oc.web.local-settings
  (:require [clojure.walk :refer (keywordize-keys)]))

(defn- env [key]
  (some-> js/window
          .-OCEnv
          js->clj
          keywordize-keys
          key))

;; Sentry
(def local-dsn "https://747713ae92c246d1a64bbce9aab3da34@app.getsentry.com/73174") ; insert your Sentry public dsn here
(def local-whitelist-array (remove nil? ["localhost" "127.0.0.1" (env :web-hostname)]))

;; Change this with your machine ip address to test
;; from a device on the same network
(def web-hostname (or (env :web-hostname) "localhost"))
(def web-port (or (env :web-port) "3559"))

;; Storage location
(def web-server-domain (str "http://" web-hostname ":" web-port))

;; Storage location
(def storage-server-domain (str "http://" web-hostname ":3001"))

;; Auth location
(def auth-server-domain (str "http://" web-hostname ":3003"))

;; Pay location
(def pay-server-domain (str "http://" web-hostname ":3004"))

;; Interaction location
(def interaction-server-domain (str "http://" web-hostname ":3002"))

;; Change location
(def change-server-domain (str "http://" web-hostname ":3006"))

;; Search location
(def search-server-domain (str "http://" web-hostname ":3007"))
(def search-enabled? true)

;; Reminder location
(def reminders-enabled? false)
(def reminder-server-domain (str "http://" web-hostname ":3011"))

;; Web location
(def web-server (str web-hostname ":" web-port))

;; JWT
(def jwt-cookie-domain web-hostname)
(def jwt-cookie-secure false)

;; Deploy key (cache buster)
(def deploy-key "")

;; Filestack key
(def filestack-key "Aoay0qXUSOyVIcDvls4Egz")

;; Cookie prefix
(def cookie-name-prefix (str web-hostname "-"))

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

;; Payments enabled
(def payments-enabled true)
(def stripe-api-key "pk_test_srP6wqbAalvBWYxcdAi4NlX0")

;; WS monitor
(def ws-monitor-interval 30)

;; Giphy
(def giphy-api-key "M2FfNXledXWbpa7FZkg2vvUD8kHMTQVF")

;; Image upload limit
(def file-upload-size (* 20 1024 1024))

(def mac-app-url "https://github.com/open-company/open-company-web/releases/download/untagged-a060f76d2ed11d47ff35/Carrot.dmg")
(def win-app-url "https://github.com/open-company/open-company-web/releases/download/untagged-a060f76d2ed11d47ff35/Carrot.exe")
(def ios-app-url "https://apps.apple.com/us/app/carrot-mobile/id1473028573")
(def android-app-url "https://play.google.com/apps/testing/io.carrot.mobile")

;; Polls
(def poll-can-add-reply false)

;; Wut
(def wut? true)

;; Publisher boards feature-flag
(def publisher-board-enabled? false)