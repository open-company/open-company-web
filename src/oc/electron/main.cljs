(ns oc.electron.main)

(def main-window (atom nil))

(def path (js/require "path"))
(def electron (js/require "electron"))

(def app (.-app electron))
(def ipc-main (.-ipcMain electron))
(def session (.-session electron))
(def BrowserWindow (.-BrowserWindow electron))

;; Are we in development mode?
;; TODO: Electron has its own concept of "in development", and so it might make more sense to lean on that...
;; https://electronjs.org/docs/api/app#appispackaged
(goog-define dev? false)

(def base-dir (if dev?
                (.join path js/__dirname "../../../")
                js/__dirname))
(def default-page "app-shell.html")

(defn load-page
  [window]
  (println base-dir)
  (if dev?
    (.loadURL window (str "file://" (.join path base-dir "../public" default-page)))
    (.loadURL window (str "file://" (.join path base-dir "public" default-page)))))

(defn mk-window
  [w h frame? show?]
  (BrowserWindow. #js {:width w
                       :height h
                       :frame frame?
                       :show show?
                       ;; Icon of Ubuntu/Linux. Other platforms are configured in package.json
                       :icon (.join path base-dir "carrot.iconset/icon_512x512.png")
                       :webPreferences #js {:nodeIntegration true
                                            :webviewTag true}
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
