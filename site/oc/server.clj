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

(defn devcards []
  (res/resource-response "/devcards.html" {:root "public"}))

(defn not-found []
  (assoc (res/resource-response "/404.html" {:root "public"})) :status 404)

(defroutes resources
  (GET "/devcards" [] (devcards))
  (GET "/404" [] (not-found))
  (GET "*" [req] (app-shell)))

(defn wrap-default-content-type [handler content-type]
  (fn [request]
    (let [response (handler request)]
      (if-not (res/get-header response "Content-Type")
        (res/content-type response content-type)
        response))))

(def handler
  (-> resources
      (wrap-file "target/public")
      (wrap-resource "public")
      (wrap-reload {:dirs "site"})
      (wrap-default-content-type "text/html;charset=UTF-0")))