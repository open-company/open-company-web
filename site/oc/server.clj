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
            [hickory.core :as h]
            [hickory.select :as s]))

(defn app-shell []
  (res/resource-response "/app-shell.html" {:root "public"}))

; (defn index []
;   (res/resource-response "/index.html" {:root "public"}))

(defn not-found []
  (assoc (res/resource-response "/404.html" {:root "public"}) :status 404))

(defn server-error []
  (assoc (res/resource-response "/500.html" {:root "public"}) :status 500))

(defn proxy-sheets-pass
  "Proxy requests to Google Sheets (needed for CORs). Done by nginx in production."
  [sheet-path params]
  (let [url (str "https://docs.google.com/" sheet-path "?" (ring.util.codec/form-encode params))]
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

(defn fix-script-string [s chart-id chart-width chart-height]
  (let [r0 #"(?i)(\"width\":\d+)" ;(js/RegExp  "gi")
        r01 #"(?i)(\"height\":\d+)" ;(js/RegExp  "gi")
        r1 #"(?i)safeDraw\(document.getElementById\('c'\)\)" ;(js/RegExp  "gi")
        ;; Regexp to match charts exported as HTML page
        r2 #"(?i)activeSheetId = '\d+'; switchToSheet\('\d+'\);" ;(js/RegExp  "gi")
        r3 #"(?i)\"containerId\":\"embed_\d+\"" ;(js/RegExp  "gi")
        r4 #"(?i)posObj\('\d+', 'embed_\d+', 0, 0, 0, 0\);};" ;(js/RegExp  "gi")
        ;; Replace all regexp
        fixed-string (clojure.string/replace s r0 (str "\"width\":" chart-width))
        fixed-string-01 (clojure.string/replace fixed-string r01 (str "\"height\":" chart-height))
        fixed-string-1 (clojure.string/replace fixed-string-01 r1 (str "safeDraw(document.getElementById('" chart-id "'))"))
        fixed-string-2 (clojure.string/replace fixed-string-1 r2 (str "activeSheetId = '" chart-id "'; switchToSheet('" chart-id "');"))
        fixed-string-3 (clojure.string/replace fixed-string-2 r3 (str "\"containerId\":\"" chart-id "\""))
        fixed-string-4 (clojure.string/replace fixed-string-3 r4 (str "posObj('" chart-id "', '" chart-id "', 0, 0, 0, 0);};"))]
    fixed-string-4))

(defn get-script-tag [s chart-id chart-width chart-height]
  (if (empty? (:src (:attrs s)))
    (str "<script type=\"text/javascript\">" (fix-script-string (apply str (:content s)) chart-id chart-width chart-height) "</script>")
    (str "<script type=\"text/javascript\" src=\"/_/sheets-proxy-pass" (:src (:attrs s)) "\"></script>")))

(defn proxy-sheets
  "Proxy requests to Google Sheets (needed for CORs). Done by nginx in production."
  [sheet-path params]
  (let [url (str "https://docs.google.com/" sheet-path "?" (ring.util.codec/form-encode params))]
    (println "Loading chart for:" url)
    (let [{:keys [status body error]} @(http/request {:method :get
                                                      :url url
                                                      :headers {
                                                        "User-Agent" "curl/7.43.0"
                                                        "Accept" "*/*"}})]
      (println "Proxy request status:" status)
      (if error
        (do (println body) {:status status :body body})
        (let [parsed-html (h/as-hickory (h/parse body))
              scripts (s/select (s/tag :script) parsed-html)
              script-strings (apply str (map #(get-script-tag % (get params "chart-id") (get params "chart-width") (get params "chart-height")) scripts))
              output-html (str "<html><head><style type=\"text/css\">html,body{margin:0;padding:0;border:none;overflow:hidden;}</style></head><body>" script-strings "<div id=\"" (get params "chart-id") "\"></div></body></html>")]
          {:status 200 :body output-html})))))

(defroutes resources
  (GET "/404" [] (not-found))
  (GET "/500" [] (server-error))
  (GET "/" [] (app-shell))
  (GET ["/_/sheets-proxy/:path" :path #".*"] [path & params] (proxy-sheets path params))
  (GET ["/_/sheets-proxy-pass/:path" :path #".*"] [path & params] (proxy-sheets-pass path params))
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