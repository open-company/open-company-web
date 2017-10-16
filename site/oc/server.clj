(ns oc.server
  "Development-time server. This role is played by an nginx proxy in production."
  (:require [ring.util.response :as res]
            [ring.middleware.params :refer (wrap-params)]
            [ring.middleware.resource :refer (wrap-resource)]
            [ring.middleware.file :refer (wrap-file)]
            [ring.middleware.reload :refer (wrap-reload)]
            [cuerdas.core :as s]
            [org.httpkit.client :as http]
            [compojure.core :refer (defroutes GET)]
            [compojure.route :as route]
            [oc.lib.proxy.sheets-chart :as sheets-chart]
            [oc.onboard-tip-svg :refer (get-onboard-image)]))

(defn app-shell []
  (res/resource-response "/app-shell.html" {:root "public"}))

(defn about []
  (res/resource-response "/about.html" {:root "public"}))

(defn features []
  (res/resource-response "/features.html" {:root "public"}))

(defn index []
  (res/resource-response "/index.html" {:root "public"}))

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

(defn- onboard-tip-response [params]
  (let [qparams (:query-params params)
        [px py] (s/split (get qparams "p") ",")
        svg (get-onboard-image (get qparams "w") (get qparams "h") px py)]
    {:status 200 :body svg :headers {"Content-Type" "image/svg+xml"}}))

(defroutes resources
  (GET "/404" [] (not-found))
  (GET "/500" [] (server-error))
  (GET "/about" [] (about))
  (GET "/features" [] (features))
  (GET "/" [] (index))
  (GET ["/_/sheets-proxy/:path" :path #".*"] [path & params] (chart-proxy path params))
  (GET ["/_/sheets-proxy-pass-through/:path" :path #".*"] [path & params] (sheets-proxy path params))
  (GET ["/:path" :path #"[^\.]+"] [path] (app-shell))
  (GET "/img/ML/onboard_tip.svg" {:as params} (onboard-tip-response params)))

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