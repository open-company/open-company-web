(ns oc.web.mixins.section
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.ws.change-client :as ws-cc]
            [oc.web.actions.activity :as activity-actions])
  (:import [goog.async Throttle]))

(def container-nav-in
  {:did-mount (fn [s]
   (ws-cc/container-watch)
   s)})

(defn focus-reload []
  (when-let [slug (or (dis/current-board-slug)
                      (dis/current-contributions-id))]
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

(def ^:private default-get-comments-delay 0)

(defn load-entry-comments
  ([container-data-get] (load-entry-comments container-data-get default-get-comments-delay))
  ([container-data-get delay]
  (let [loaded-uuids (atom #{})
        load-comments (fn [s]
                        (when-let [container-data (container-data-get s)]
                          (doseq [entry (:posts-list container-data)
                                  :when (and (au/entry? entry)
                                             (not (@loaded-uuids (:uuid entry)))
                                             (not (:comments-loaded? entry)))
                                  :let [full-entry (dis/activity-data (:uuid entry))]]
                            (swap! loaded-uuids conj (:uuid entry))
                            (utils/after delay
                              #(au/get-comments-if-needed full-entry)))))]
    {:did-mount (fn [s]
     (load-comments s)
     s)
     :did-remount (fn [_ s]
     (load-comments s)
     s)})))