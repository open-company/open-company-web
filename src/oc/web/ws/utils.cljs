(ns oc.web.ws.utils
  (:require [taoensso.timbre :as timbre]
            [taoensso.sente :as s]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.raven :as sentry]
            [oc.web.local-settings :as ls]))

;; Connection check

(defn sentry-report [message chsk-send! ch-state & [action-id infos]]
  (let [connection-status (if @ch-state
                            @@ch-state
                            nil)
        ch-send-fn? (fn? @chsk-send!)
        ctx {:action action-id
             :connection-status connection-status
             :send-fn ch-send-fn?
             :infos infos
             :sessionURL (when (exists? js/FS) (.-getCurrentSessionURL js/FS))}]
    (sentry/set-extra-context! ctx)
    (sentry/capture-message message)
    (sentry/clear-extra-context!)
    (timbre/error message ctx)))

;; Real send

(defn- real-send [service-name chsk-send! ch-state args]
 (try
   (apply @chsk-send! args)
   (catch ExceptionInfo e
     (js/console.log "DBG ex-info" e args)
     (sentry-report (str "Error sending event for " service-name)
      chsk-send! ch-state (first (first args))
      {:rest-args (rest (first args))
       :ex-message (.-message e)}))))

;; cmd queue
(defonce cmd-queue (atom {}))

(defn buffer-cmd [service-name args]
  (let [service-key (keyword service-name)
        service-queue (service-key @cmd-queue)
        service-next-queue (conj service-queue args)]
    (swap! cmd-queue assoc service-key service-next-queue)))

(defn reset-queue [service-name]
  (reset! cmd-queue (assoc @cmd-queue (keyword service-name) [])))

(defn send-queue [service-name chsk-send! ch-state]
  (when (and @chsk-send! (:open? @@ch-state))
    (doseq [qargs ((keyword service-name) @cmd-queue)]
      (real-send service-name chsk-send! ch-state qargs))
    (reset-queue service-name)))

(defn send! [service-name chsk-send! ch-state args]
  (if (and @chsk-send! (:open? @@ch-state))
    (do
      ;; empty queue first
      (send-queue service-name chsk-send! ch-state)
      ;; send current command
      (real-send service-name chsk-send! ch-state args))
    ;;disconnected
    (buffer-cmd service-name args)))

(defn check-interval [last-interval service-name chsk-send! ch-state]
  (when @last-interval
    (.clearInterval js/window @last-interval))
  (reset! last-interval
   (.setInterval js/window
    #(when (or (not @ch-state)
               (not @@ch-state)
               (not (:open? @@ch-state)))
       (sentry-report (str "Send over closed " service-name " WS connection") chsk-send! ch-state))
    (* ls/ws-monitor-interval 1000))))

(defn reconnect [last-interval service-name chsk-send! ch-state]
  (send-queue service-name chsk-send! ch-state)
  (check-interval last-interval service-name chsk-send! ch-state))

(defn report-invalid-jwt [service-name ch-state rep]
  (let [connection-status (if @ch-state
                            @@ch-state
                            nil)
        ctx {:jwt (j/jwt)
             :connection-status connection-status
             :timestamp (.getTime (new js/Date))
             :rep rep
             :sessionURL (when (exists? js/FS) (.-getCurrentSessionURL js/FS))}]
    (sentry/set-extra-context! ctx)
    (sentry/capture-message (str service-name " WS: not valid JWT"))
    (sentry/clear-extra-context!)
    (timbre/error (str service-name " WS: not valid JWT") ctx)))

(defn report-connect-timeout [service-name ch-state]
  (let [connection-status (if @ch-state
                            @@ch-state
                            nil)
        ctx {:timestamp (.getTime (new js/Date))
             :connection-status connection-status
             :sessionURL (when (exists? js/FS) (.-getCurrentSessionURL js/FS))}]
    (sentry/set-extra-context! ctx)
    (sentry/capture-message (str service-name " WS: handshake timeout"))
    (sentry/clear-extra-context!)
    (timbre/error (str service-name " WS: handshake timeout") ctx)))

(defn auth-check [service-name ch-state chsk-send! channelsk jwt-refresh-cb reconnect-cb success-cb rep]
  (if (and (s/cb-success? rep)
           (:valid rep))
    (when (fn? success-cb)
      (success-cb rep))
    (do
      (timbre/warn "disconnecting client due to invalid JWT!" rep)
      (s/chsk-disconnect! @channelsk)
      (cond
        (j/expired?)
        (jwt-refresh-cb reconnect-cb)
        (= rep :chsk/timeout)
        (do
          (report-connect-timeout service-name ch-state)
          ;; retry in 10 seconds if sente is not trying reconnecting
          (when (and @ch-state
                     (not (:udt-next-reconnect @@ch-state)))
            (utils/after (* 10 1000)
             (reconnect-cb))))
        (or (= rep :chsk/closed)
            (= rep "closed"))
        (do
          (sentry-report (str "Auth failed for" service-name) chsk-send! ch-state "jwt-validation/auth-check"
           {:chsk/closed (= rep :chsk/closed)
            :closed (= rep "closed")})
          ;; retry in 10 seconds if sente is not trying reconnecting
          (when (and @ch-state
                     (not (:udt-next-reconnect @@ch-state)))
            (utils/after (* 10 1000)
             (reconnect-cb))))
        :else
        (report-invalid-jwt service-name ch-state rep)))))

(defn post-handshake-auth [jwt-refresh-cb auth-cb]
  (timbre/debug "Trying post handshake jwt auth")
  (if (j/expired?)
    (jwt-refresh-cb auth-cb)
    (auth-cb)))