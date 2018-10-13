(ns oc.server
  "Development-time server. This role is played by an nginx proxy in production."
  (:require [ring.util.response :as res]
            [ring.middleware.params :refer (wrap-params)]
            [ring.middleware.resource :refer (wrap-resource)]
            [ring.middleware.file :refer (wrap-file)]
            [ring.middleware.reload :refer (wrap-reload)]
            [org.httpkit.client :as http]
            [compojure.core :refer (defroutes GET)]
            [compojure.route :as route]
            [oc.lib.proxy.sheets-chart :as sheets-chart]))

(defn app-shell []
  (res/resource-response "/app-shell.html" {:root "public"}))

(defn about []
  (res/resource-response "/about.html" {:root "public"}))

(defn pricing []
  (res/resource-response "/pricing.html" {:root "public"}))

(defn slack []
  (res/resource-response "/slack.html" {:root "public"}))

(defn slack-lander []
  (res/resource-response "/slack-lander.html" {:root "public"}))

(defn privacy []
  (res/resource-response "/privacy.html" {:root "public"}))

(defn terms []
  (res/resource-response "/terms.html" {:root "public"}))

(defn press-kit []
  (res/resource-response "/press-kit.html" {:root "public"}))

(defn index []
  (res/resource-response "/index.html" {:root "public"}))

(defn version-json []
  (res/resource-response "/version/version.json" {:root "public"}))

; (defn index []
;   (res/resource-response "/index.html" {:root "public"}))

(defn not-found []
  (assoc (res/resource-response "/404.html" {:root "public"}) :status 404))

(defn server-error []
  (assoc (res/resource-response "/500.html" {:root "public"}) :status 500))

(defn- chart-proxy [path params]
  (sheets-chart/proxy-sheets-chart path params))

(defn- sheets-proxy [path params]
  (sheets-chart/proxy-sheets-pass-through path params))

(defroutes resources
  (GET "/404" [] (not-found))
  (GET "/500" [] (server-error))
  (GET "/about" [] (about))
  (GET "/pricing" [] (pricing))
  (GET "/slack" [] (slack))
  (GET "/privacy" [] (privacy))
  (GET "/terms" [] (terms))
  (GET "/version/version.json" [] (version-json))
  (GET "/sign-up/slack" [] (slack-lander))
  (GET "/press-kit" [] (press-kit))
  (GET "/" [] (index))
  (GET ["/_/sheets-proxy/:path" :path #".*"] [path & params] (chart-proxy path params))
  (GET ["/_/sheets-proxy-pass-through/:path" :path #".*"] [path & params] (sheets-proxy path params))
  (GET ["/:path" :path #"[^\.]+"] [path] (app-shell)))

;; Some routes like /, /404 and similar can't have their content-type
;; derived automatically, because of that we set it with the middleware below

(defn html-uri?
  "Return true if `uri` does not end in what looks like a file extension"
  [uri]
  (empty? (re-seq #"\.\w{2,4}$" uri)))

(defn wrap-default-content-type [handler]
  (fn [request]
    (let [response (handler request)]
      (if (and (html-uri? (:uri request))
               (not (res/get-header response "Content-Type")))
        (res/content-type response "text/html;charset=UTF-8")
        response))))

(def handler
  (-> resources
      (wrap-params)
      (wrap-resource "public")
      (wrap-reload {:dirs "site"})
      (wrap-default-content-type)))