(ns open-company-web.server
  "Development-time server. This role is played by an nginx proxy in production."
  (:require [ring.util.response :refer [file-response]]
            [compojure.core :refer [defroutes GET PUT POST]]
            [compojure.route :as route]
            [compojure.handler :as handler]))

(defn index []
  (file-response "public/index.html" {:root "resources"}))

(defroutes routes
  ; serve the react app for all requests
  (GET "*" [] (index))
  ; remove the static paths
  (route/files "/css/*" {:root "resources/public"})
  (route/files "/js/*" {:root "resources/public"})
  (route/files "/img/*" {:root "resources/public"}))

(defn request-handler [handler]
 (fn [request]
   (handler request)))

(def handler
  (request-handler routes))
