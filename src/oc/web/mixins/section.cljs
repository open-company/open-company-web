(ns oc.web.mixins.section
  (:require [oc.web.ws.change-client :as ws-cc]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.router :as router]
            [taoensso.timbre :as timbre]))

(def container-nav-in
  {:did-mount (fn [s]
   (ws-cc/container-watch)
   s)})

(defn focus-reload
  []
  (let [slug (or (router/current-board-slug)
                 (router/current-contributions-id))]
    (timbre/info "Reloading data for:" slug)
   (activity-actions/refresh-board-data slug)))

(def window-focus-auto-loader
  {:did-mount
   (fn [s]
     (.addEventListener js/window "focus" focus-reload)
     s)
   :will-unmount
   (fn [s]
     (.removeEventListener js/window "focus" focus-reload)
     s)})
