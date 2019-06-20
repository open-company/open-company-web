(ns oc.electron.main)

(def main-window (atom nil))

(def path (js/require "path"))
(def electron (js/require "electron"))

(def app (.-app electron))
(def ipc-main (.-ipcMain electron))
(def session (.-session electron))
(def BrowserWindow (.-BrowserWindow electron))

(goog-define init-url "http://localhost:3559/login/desktop")

(defn load-page
  [window]
  (println "Loading " init-url)
  (.loadURL window init-url))

(defn mk-window
  [w h frame? show?]
  (BrowserWindow. #js {:width w
                       :height h
                       :frame frame?
                       :show show?
                       ;; Icon of Ubuntu/Linux. Other platforms are configured in package.json
                       :icon (.join path (.getAppPath app) "carrot.iconset/icon_512x512.png")
                       :webPreferences #js {:preload (.join path (.getAppPath app) "electron" "renderer.js")}
                       }))

(defn set-csp
  []
  ;; Define Content Security Policy
  ;; https://electronjs.org/docs/tutorial/security#6-define-a-content-security-policy
  (.. session -defaultSession -webRequest
      (onHeadersReceived
       (fn [details callback]
         (let [details-cljs (js->clj details)
               merged-details (merge details-cljs
                                     {"Content-Security-Policy" ["default-src \"none\""]})]
           (callback (clj->js merged-details)))))))

(defn init-browser
  []
  (set-csp)
  (reset! main-window (mk-window 1280 720 true true))
  (load-page @main-window)
  (when dev? (.openDevTools @main-window))
  (.on @main-window "close" #(reset! main-window nil)))

(defn init
  []
  (.on app "window-all-closed" #(.quit app))
  (.on app "ready" init-browser)
  (.on ipc-main "set-badge-count" (fn [event arg] (.setBadgeCount app arg)))
  (set! *main-cli-fn* (fn [] nil)))
