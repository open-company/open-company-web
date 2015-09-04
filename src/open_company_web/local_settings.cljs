(ns open-company-web.local-settings)

;; Sentry
(def local-dsn nil) ; insert your Sentry public dsn here
(def local-whitelist-array ["locahost" "127.0.0.1"])

;; API
(def api-server-domain "http://localhost:3000")

;; Auth
(def auth-server-domain "http://localhost:3003")