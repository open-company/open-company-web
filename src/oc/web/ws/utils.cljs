(ns oc.web.ws.utils
  (:require [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.raven :as sentry]
            [oc.web.local-settings :as ls]))

;; Connection check

(defonce last-interval (atom nil))

(defn- sentry-report [service-name chsk-send! ch-state & [action-id]]
  (let [connection-status (if @ch-state
                            @@ch-state
                            nil)
        ch-send-fn? (fn? @chsk-send!)
        ctx {:action action-id
             :connection-status connection-status
             :send-fn ch-send-fn?
             :sessionURL (when js/LogRocket (.-sessionURL js/LogRocket))}]
    (sentry/set-extra-context! ctx)
    (sentry/capture-message (str "Send over closed " service-name " WS connection"))
    (sentry/clear-extra-context!)
    (timbre/error "Send over closed " service-name " WS connection" ctx)))

(defn check-interval [service-name chsk-send! ch-state]
  (when @last-interval
    (.clearTimeout @last-interval))
  (reset! last-interval
   (.setInterval js/window
    #(when (or (not @ch-state)
               (not @@ch-state)
               (not (:open? @@ch-state)))
       (sentry-report service-name chsk-send! ch-state))
    (* ls/ws-monitor-interval 1000))))

(defn report-invalid-jwt [service-name ch-state]
  (let [connection-status (if @ch-state
                            @@ch-state
                            nil)
        ctx {:jwt (j/jwt)
             :timestamp (.getTime (new js/Date))
             :sessionURL (when js/LogRocket (.-sessionURL js/LogRocket))}]
    (sentry/set-extra-context! ctx)
    (sentry/capture-message (str service-name " WS: not valid JWT"))
    (sentry/clear-extra-context!)
    (timbre/error (str service-name " WS: not valid JWT") ctx)))