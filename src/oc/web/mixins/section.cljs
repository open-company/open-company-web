(ns oc.web.mixins.section
  (:require [taoensso.timbre :as timbre]
            [oc.web.router :as router]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.actions.activity :as activity-actions])
  (:import [goog.async Throttle]))

(def container-nav-in
  {:did-mount (fn [s]
   (ws-cc/container-watch)
   s)})

(defn focus-reload []
  (when-let [slug (or (router/current-board-slug)
                      (router/current-contributions-id))]
    (timbre/info "Reloading data for:" slug)
    (activity-actions/refresh-board-data slug)))

(def window-focus-auto-loader
  (let [throttled-refresh (Throttle. focus-reload 5000)
        fire! #(.fire throttled-refresh)]
    {:did-mount
     (fn [s]
       (.addEventListener js/window "focus" fire!)
       s)
     :will-unmount
     (fn [s]
       (.removeEventListener js/window "focus" fire!)
       (.stop throttled-refresh)
       (.dispose throttled-refresh)
       s)}))