(ns oc.server
  "Development-time server. This role is played by an nginx proxy in production."
  (:require [ring.util.response :as res]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer (defroutes GET)]
            [compojure.route :as route]))

(defn app-shell []
  (res/resource-response "/app-shell.html" {:root "public"}))

(defn index []
  (res/resource-response "/index.html" {:root "public"}))

(defn not-found []
  (assoc (res/resource-response "/not-found.html" {:root "public"}) :status 404))

(defroutes resources
  (GET "/404" [] (not-found))
  (GET "/" [] (index))
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
      (wrap-resource "public")
      (wrap-reload {:dirs "site"})
      (wrap-default-content-type)))