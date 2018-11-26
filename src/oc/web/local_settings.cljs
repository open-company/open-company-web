(ns oc.web.local-settings)

;; Sentry
(def local-dsn "https://747713ae92c246d1a64bbce9aab3da34@app.getsentry.com/73174") ; insert your Sentry public dsn here
(def local-whitelist-array ["localhost" "127.0.0.1"])

;; Storage location
(def web-server-domain "http://localhost:3559")

;; Storage location
(def storage-server-domain "http://localhost:3001")

;; Auth location
(def auth-server-domain "http://localhost:3003")

;; Pay location
(def pay-server-domain "http://localhost:3004")

;; Interaction location
(def interaction-server-domain "http://localhost:3002")

;; Search location
(def search-server-domain "http://localhost:3007")
(def search-enabled? true)

;; Web location
(def web-server "localhost:3559")

;; JWT
(def jwt-cookie-domain "localhost")
(def jwt-cookie-secure false)

;; Recurly
(def recurly-id "opencompany-staging")
(def recurly-plan "local")

;; Deploy key (cache buster)
(def deploy-key "")

;; Filestack key
(def filestack-key "Aoay0qXUSOyVIcDvls4Egz")

;; Cookie prefix
(def cookie-name-prefix "localhost-")

;; Log level
(def log-level "debug")

;; CDN URL
(def cdn-url "")

;; Attachments bucket
(def attachments-bucket "open-company-attachments-non-prod")

;; Mailchimp api endpoing
(def mailchimp-api-endpoint "https://onhq6jg245.execute-api.us-east-1.amazonaws.com/dev/subscribe")

;; AP seen TTL in days
(def oc-seen-ttl 30)

;; Ziggeo
(def oc-ziggeo-app "c9b611b2b996ee5a1f318d3bacc36b27")
(def oc-ziggeo-profiles [])
(def oc-enable-transcriptions false)

;; WS monitor
(def ws-monitor-interval 30)
