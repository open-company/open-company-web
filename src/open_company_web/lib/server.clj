(ns open-company-web.lib.server
  "Development-time server. This role is played by an nginx proxy in production."
  (:require [ring.util.response :refer [file-response]]
            [compojure.core :refer [defroutes GET PUT POST]]
            [compojure.route :as route]))

(defn index []
  (file-response "public/index.html" {:root "resources"}))

(defn devcards []
  (file-response "public/devcards.html" {:root "resources"}))

(defroutes resources
  ; serve the react app for all requests
  (GET "/devcards" [] (devcards))
  (GET "*" [] (index))
  ; remove the static paths
  (route/files "/css/*" {:root "resources/public"})
  (route/files "/js/*" {:root "resources/public"})
  (route/files "/lib/*" {:root "resources/public"})
  (route/files "/img/*" {:root "resources/public"}))

(defn request-handler [routes]
 routes)

(def handler
  (request-handler resources))