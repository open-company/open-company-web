(ns open-company-web.local-settings)

;; Sentry
(def local-dsn "https://747713ae92c246d1a64bbce9aab3da34@app.getsentry.com/73174") ; insert your Sentry public dsn here
(def local-whitelist-array ["localhost" "127.0.0.1"])

;; API location
(def api-server-domain "http://localhost:3000")

;; Auth location
(def auth-server-domain "http://localhost:3003")

;; Pay location
(def pay-server-domain "http://localhost:3004")

;; Web location
(def web-server "localhost:3559")

;; JWT
(def jwt-cookie-domain "localhost")
(def jwt-cookie-secure false)

;; Recurly
(def recurly-id "opencompany-staging")
(def recurly-plan "local")

;; Deploy key (cache buster)
(def deploy-key "asd")

;; Filestack key
(def filestack-key "Aoay0qXUSOyVIcDvls4Egz")