(ns oc.electron.auto-update
  "The desktop auto-updater's primary API is the `start-update-cycle!` function.
  Calling this function will schedule regular update checks at the provided interval
  in milliseconds (or `default-rate-ms` if 0-arity variant is used), and, if an update
  is found, will perform 3 actions:

  1. Begin downloading that update in the background
  2. Notify the user via a desktop notification that an update is available
  3. Extend the regularly occuring update checks to `extended-rate-ms` as to not annoy
     the user with constant notifications if they choose not to restart the app right away

  Upon restarting the app, the update will be installed."
  (:require [taoensso.timbre :as timbre]
            [oc.shared.interval :as interval]))

;; See https://www.electron.build/auto-update for details on the `electron-updater` API
(def auto-updater (.-autoUpdater (js/require "electron-updater")))

(def default-rate-ms (* 5 60 1000)) ;; 5 minutes
(def extended-rate-ms (* 24 60 60 1000)) ;; 24 hours

(defn- check-for-updates
  []
  (timbre/info "Checking for desktop updates")
  (.checkForUpdatesAndNotify auto-updater))

(defonce auto-updater-interval (interval/make-interval {:fn check-for-updates
                                                        :ms default-rate-ms}))

(defn- on-update-available
  [data]
  (timbre/info "Wut desktop update available" data)
  (interval/restart-interval! auto-updater-interval extended-rate-ms))

(defn start-update-cycle!
  []
  (timbre/info "Starting desktop auto updater")
  (.addListener auto-updater "update-available" on-update-available)
  (interval/start-interval! auto-updater-interval))

(defn stop-update-cycle!
  []
  (timbre/info "Stopping desktop auto updater")
  (.removeListener auto-updater "update-available" on-update-available)
  (interval/stop-interval! auto-updater-interval))
