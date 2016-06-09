(ns open-company-web.local-settings)

;; Sentry
(def local-dsn "https://747713ae92c246d1a64bbce9aab3da34@app.getsentry.com/73174") ; insert your Sentry public dsn here

(def local-whitelist-array ["localhost" "127.0.0.1"])

;; API
(def api-server-domain "http://localhost:3000")

;; Auth
(def auth-server-domain "http://localhost:3003")

;; Web
(def web-server "localhost:3559")

;; JWT
(def jwt-cookie-domain "localhost")
(def jwt-cookie-secure false)

;; Deploy key
(def deploy-key "asd")

(def filestack-key "Aoay0qXUSOyVIcDvls4Egz")