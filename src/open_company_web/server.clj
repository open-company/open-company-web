(ns open-company-web.server
  (:require [ring.util.response :refer [file-response]]
            [compojure.core :refer [defroutes GET PUT POST]]
            [compojure.route :as route]
            [compojure.handler :as handler]))

(defn index []
  (file-response "public/index.html" {:root "resources"}))

(defroutes routes
  (GET "/" [] (index))
  (GET "/edit/:company" [] (index))
  (route/files "/" {:root "resources/public"}))

(defn request-handler [handler]
 (fn [request]
   (handler request)))

(def handler
  (request-handler routes))
