(ns oc.web.local-settings
  (:require [clojure.walk :refer (keywordize-keys)]))

(defn- env [key]
  (some-> js/window
          .-OCEnv
          js->clj
          keywordize-keys
          key))

;; Sentry
(defonce local-dsn "https://747713ae92c246d1a64bbce9aab3da34@app.getsentry.com/73174") ; insert your Sentry public dsn here
(defonce local-whitelist-array (remove nil? ["localhost" "127.0.0.1" (env :web-hostname)]))

;; Change this with your machine ip address to test
;; from a device on the same network
(defonce web-hostname (or (env :web-hostname) "localhost"))
(defonce web-port (or (env :web-port) "3559"))

;; Storage location
(defonce web-server-domain (str "http://" web-hostname ":" web-port))

;; Storage location
(defonce storage-server-domain (str "http://" web-hostname ":3001"))

;; Auth location
(defonce auth-server-domain (str "http://" web-hostname ":3003"))

;; Pay location
(defonce pay-server-domain (str "http://" web-hostname ":3004"))

;; Interaction location
(defonce interaction-server-domain (str "http://" web-hostname ":3002"))

;; Change location
(defonce change-server-domain (str "http://" web-hostname ":3006"))

;; Search location
(defonce search-server-domain (str "http://" web-hostname ":3007"))
(defonce search-enabled? true)

;; Reminder location
(defonce reminders-enabled? false)
(defonce reminder-server-domain (str "http://" web-hostname ":3011"))

;; Web location
(defonce web-server (str web-hostname ":" web-port))

;; JWT
(defonce jwt-cookie-domain web-hostname)
(defonce jwt-cookie-secure false)

;; Deploy key (cache buster)
(defonce deploy-key "")

;; Filestack key
(defonce filestack-key "Aoay0qXUSOyVIcDvls4Egz")

;; Cookie prefix
(defonce cookie-name-prefix (str web-hostname "-"))

;; Log level
(defonce log-level "debug")

;; CDN URL
(defonce cdn-url "")

;; Attachments bucket
(defonce attachments-bucket "open-company-attachments-non-prod")

;; AP seen TTL in days
(defonce oc-seen-ttl 30)

;; Payments enabled
(defonce payments-enabled true)
(defonce stripe-api-key "pk_test_srP6wqbAalvBWYxcdAi4NlX0")

;; WS monitor
(defonce ws-monitor-interval 30)

;; Giphy
(defonce giphy-api-key "M2FfNXledXWbpa7FZkg2vvUD8kHMTQVF")

;; Image upload limit
(defonce file-upload-size (* 20 1024 1024))

(defonce mac-app-url "https://github.com/open-company/open-company-web/releases/download/untagged-a060f76d2ed11d47ff35/Carrot.dmg")
(defonce win-app-url "https://github.com/open-company/open-company-web/releases/download/untagged-a060f76d2ed11d47ff35/Carrot.exe")
(defonce ios-app-url "https://apps.apple.com/us/app/carrot-mobile/id1473028573")
(defonce android-app-url "https://play.google.com/apps/testing/io.carrot.mobile")

;; Polls
(defonce poll-can-add-reply false)

;; Wut
(defonce wut? true)

;; Publisher boards feature-flag
(defonce publisher-board-enabled? false)

;; Fake container ids for seen table
(defonce seen-home-container-id "1111-1111-1111")
(defonce seen-replies-container-id "2222-2222-2222")

;; Digest times
(defonce digest-times #{:700 :1200 :1700})

;; Primary color
(defonce default-color
  {:primary {:rgb {:r 104 :g 51 :b 241}
             :hex "#6833F1"}
   :secondary {:rgb {:r 244 :g 244 :b 244}
               :hex "#FFFFFF"}})

(defonce default-brand-color
  {:light default-color
   :dark default-color})