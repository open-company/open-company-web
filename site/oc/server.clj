(ns oc.server
  "Development-time server. This role is played by an nginx proxy in production."
  (:require [ring.util.response :as res]
            [ring.middleware.params :refer (wrap-params)]
            [ring.middleware.resource :refer (wrap-resource)]
            [ring.middleware.file :refer (wrap-file)]
            [ring.middleware.reload :refer (wrap-reload)]
            [org.httpkit.client :as http]
            [compojure.core :refer (defroutes GET)]
            [compojure.route :as route]))

(defn app-shell []
  (res/resource-response "/app-shell.html" {:root "public"}))

; (defn index []
;   (res/resource-response "/index.html" {:root "public"}))

(defn not-found []
  (assoc (res/resource-response "/404.html" {:root "public"}) :status 404))

(defn server-error []
  (assoc (res/resource-response "/500.html" {:root "public"}) :status 500))

(defn proxy-sheets
  "Proxy requests to Google Sheets (needed for CORs). Done by nginx in production."
  [sheets-id chart-type params]
  (let [url (str "https://docs.google.com/spreadsheets/d/" sheets-id "/" chart-type "?" (ring.util.codec/form-encode params))]
    (println "Proxying request to:" url)
    (let [{:keys [status body error]} @(http/request {:method :get
                                                      :url url
                                                      :headers {
                                                        "User-Agent" "curl/7.43.0"
                                                        "Accept" "*/*"}})]
      (println "Proxy request status:" status)
      (if error
        (do (println body) {:status status :body body})
        {:status 200 :body body :headers {"Content-Type" "text/html"}}))))

(defroutes resources
  (GET "/404" [] (not-found))
  (GET "/500" [] (server-error))
  (GET "/" [] (app-shell))
  (GET "/_/sheets-proxy/:sheets-id/:chart-type" [sheets-id chart-type & params] (proxy-sheets sheets-id chart-type params))
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