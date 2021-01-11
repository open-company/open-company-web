(ns oc.dev
  "Development-time server. This role is played by an nginx proxy in production."
  (:require [ring.util.response :as ring-response]
            [ring.middleware.params :refer (wrap-params)]
            [ring.middleware.resource :refer (wrap-resource)]
            [ring.middleware.file :refer (wrap-file)]
            [ring.middleware.reload :refer (wrap-reload)]
            [org.httpkit.client :as http]
            ;; [compojure.core :refer (defroutes GET)]
            ;; [compojure.route :as route]
            [compojure.core :as compojure :refer (GET)]
            [oc.lib.proxy.sheets-chart :as sheets-chart]))

(defn fresp [html-filename]
  (ring-response/file-response html-filename {:root "public"}))

(defn- chart-proxy [path params]
  (sheets-chart/proxy-sheets-chart path params))

(defn- sheets-proxy [path params]
  (sheets-chart/proxy-sheets-pass-through path params))

(defn routes []
  (compojure/routes
   (GET "/404" [] (fresp "404.html"))
   (GET "/500" [] (fresp "500.html"))
   (GET "/about" [] (fresp "about.html"))
   (GET "/pricing" [] (fresp "pricing.html"))
   (GET "/slack" [] (fresp "slack.html"))
   (GET "/privacy" [] (fresp "privacy.html"))
   (GET "/terms" [] (fresp "terms.html"))
   (GET "/version/version.json" [] ((fresp "version/version.json")))
   (GET "/sign-up/slack" [] (fresp "slack-lander.html"))
   (GET "/press-kit" [] (fresp "press-kit.html"))
   (GET "/" [] (fresp "index.html"))
   (GET ["/_/sheets-proxy/:path" :path #".*"] [path & params] (chart-proxy path params))
   (GET ["/_/sheets-proxy-pass-through/:path" :path #".*"] [path & params] (sheets-proxy path params))
   (GET ["/:path" :path #"[^\.]+"] [path] (fresp "app-shell.html"))))

;; Some routes like /, /404 and similar can't have their content-type
;; derived automatically, because of that we set it with the middleware below

(defn html-uri?
  "Return true if `uri` does not end in what looks like a file extension"
  [uri]
  (empty? (re-seq #"\.\w{2,4}$" uri)))

(defn wrap-default-content-type [handler]
  (fn [request]
    (let [response (handler request)
          content-type? (ring-response/get-header response "Content-Type")]
      (cond
        (and (not content-type?)
             (re-seq #"(?i).svg$" (:uri request)))
        ;; (assoc-in response [:headers "Content-Type"] "image/svg+xml")
        (ring-response/content-type response "image/svg+xml")
        (and (not content-type?)
             (html-uri? (:uri request)))
        (ring-response/content-type response "text/html;charset=UTF-8")
        :else
        response))))

(def handler ;[req]
  (-> (routes)
      (wrap-params)
      (wrap-resource "public")
      (wrap-file "public")
      (wrap-default-content-type)))