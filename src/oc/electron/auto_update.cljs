(ns oc.electron.auto-update
  "The desktop auto-updater's primary API is the `start-update-cycle!` function.
  Calling this function will schedule regular update checks at the provided interval
  in milliseconds (or `default-rate-ms` if 0-arity variant is used), and, if an update
  is found, will perform 3 actions:

  1. Begin downloading that update in the background
  2. Notify the user via a desktop notification that an update is available
  3. Extend the regularly occuring update checks to `extended-rate-ms` as to not annoy
     the user with constant notifications if they choose not to restart the app right away

  Upon restarting the app, the update will be installed.")

;; See https://www.electron.build/auto-update for details on the `electron-updater` API
(def auto-updater (.-autoUpdater (js/require "electron-updater")))
(def auto-updater-js-interval (atom nil))

(def default-rate-ms (* 5 60 1000)) ;; 5 minutes
(def extended-rate-ms (* 24 60 60 1000)) ;; 24 hours

(declare on-update-available)

(defn- updater-running?
  []
  (some? @auto-updater-js-interval))

(defn- check-for-updates
  []
  (js/console.log "Checking for Carrot desktop updates")
  (.checkForUpdatesAndNotify auto-updater))

(defn start-update-cycle!
  ([]
   (start-update-cycle! default-rate-ms))
  ([rate-ms]
   (when-not (updater-running?)
     (let [js-interval (js/setInterval check-for-updates rate-ms)]
       (.addListener auto-updater "update-available" on-update-available)
       (reset! auto-updater-js-interval js-interval)
       (js/console.log "Started Carrot desktop auto-update cycle")))))

(defn stop-update-cycle!
  []
  (when (updater-running?)
    (js/clearInterval @auto-updater-js-interval)
    (.removeListener auto-updater "update-available" on-update-available)
    (reset! auto-updater-js-interval nil)))

(defn- on-update-available
  [info]
  (js/console.log "Carrot desktop update available" info)
  (stop-update-cycle!)
  (start-update-cycle! extended-rate-ms))
