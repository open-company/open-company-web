(ns oc.web.ws.utils
  (:require [taoensso.timbre :as timbre]
            [taoensso.sente :as s]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.sentry :as sentry]
            [oc.web.local-settings :as ls]
            [oc.web.lib.fullstory :as fullstory]))

;; Connection check

(defn sentry-report [message chsk-send! ch-state & [action-id infos]]
  (timbre/debug "Sentry report:" message)
  (let [connection-status (when @ch-state
                            @@ch-state)
        ch-send-fn? (fn? @chsk-send!)
        ctx {:action action-id
             :connection-status connection-status
             :send-fn ch-send-fn?
             :infos infos
             :sessionURL (fullstory/session-url)}]
    (sentry/capture-message-with-extra-context! ctx message)
    (timbre/error message ctx)))

;; Real send

(defn- real-send [service-name chsk-send! ch-state args]
 (try
   (apply @chsk-send! args)
   (catch ExceptionInfo e
     (sentry-report (str "Error sending event for " service-name)
      chsk-send! ch-state (ffirst args)
      {:rest-args (rest (first args))
       :ex-message (.-message e)}))))

;; cmd queue
(defonce cmd-queue (atom {}))

(defn buffer-cmd [service-name args]
  (timbre/debug "Queuing message for" service-name args)
  (let [service-key (keyword service-name)
        service-queue (service-key @cmd-queue)
        service-next-queue (conj service-queue args)]
    (swap! cmd-queue assoc service-key service-next-queue)))

(defn reset-queue [service-name]
  (timbre/debug "Reset queue for" service-name)
  (reset! cmd-queue (assoc @cmd-queue (keyword service-name) [])))

(defn send-queue [service-name chsk-send! ch-state]
  (when (and @chsk-send! (:open? @@ch-state))
    (timbre/debug "Send queue for" service-name (count ((keyword service-name) @cmd-queue)))
    (doseq [qargs ((keyword service-name) @cmd-queue)]
      (real-send service-name chsk-send! ch-state qargs))
    (reset-queue service-name)))

(defn send! [service-name chsk-send! ch-state args]
  (timbre/debug "Send!" service-name args)
  (if (and @chsk-send! (:open? @@ch-state))
    (do
      ;; empty queue first
      (send-queue service-name chsk-send! ch-state)
      ;; send current command
      (real-send service-name chsk-send! ch-state args))
    ;;disconnected
    (buffer-cmd service-name args)))

(defn check-interval [last-interval service-name chsk-send! ch-state reconnect-cb]
  (timbre/debug "Set check-interval for" service-name)
  (when @last-interval
    (.clearInterval js/window @last-interval))
  (reset! last-interval
   (.setInterval js/window
    #(when (or (not @ch-state)
               (not @@ch-state)
               (not (:open? @@ch-state)))
       (timbre/debug "WS check-interval connection down for" service-name)
       ;; WS connection is closed
       (if (:udt-next-reconnect @@ch-state)
         ;; There is an auto reconnect set, let's wait for it
         (timbre/debug "Will reconnect automatically at" (utils/js-date (:udt-next-reconnect @@ch-state)))
         ;; no auto reconnect, let's force a reconnect
         (do
           (timbre/debug "No auto reconnect set, forcing reconnection")
           (sentry-report (str "No auto reconnect set for" service-name ". Forcing reconnect!") chsk-send! ch-state)
           (utils/after 0 reconnect-cb))))
    (* ls/ws-monitor-interval 1000))))

(defn reconnected [last-interval service-name chsk-send! ch-state reconnect-cb]
  (timbre/debug "Reconnect" service-name)
  (send-queue service-name chsk-send! ch-state)
  (check-interval last-interval service-name chsk-send! ch-state reconnect-cb))

(defn report-invalid-jwt [service-name ch-state rep]
  (timbre/debug "Report invalid-jwt" service-name)
  (let [connection-status (when @ch-state
                            @@ch-state)
        ctx {:jwt (j/jwt)
             :connection-status connection-status
             :timestamp (.getTime (new js/Date))
             :rep rep
             :sessionURL (fullstory/session-url)}]
    (sentry/capture-message-with-extra-context! ctx (str service-name " WS: not valid JWT"))
    (timbre/error service-name "WS: not valid JWT" ctx)))

(defn report-connect-timeout [service-name ch-state]
  (timbre/debug "Report connection-timeout" service-name)
  (let [connection-status (when @ch-state
                            @@ch-state)
        ctx {:timestamp (.getTime (new js/Date))
             :connection-status connection-status
             :sessionURL (fullstory/session-url)}]
    (sentry/capture-message-with-extra-context! ctx (str service-name " WS: handshake timeout"))
    (timbre/error service-name "WS: handshake timeout" ctx)))

(defn auth-check [service-name ch-state chsk-send! channelsk jwt-refresh-cb reconnect-cb success-cb rep]
  (timbre/debug "Auth-check" service-name)
  (if (and (s/cb-success? rep)
           (:valid rep))
    (when (fn? success-cb)
      (success-cb rep))
    (do
      (timbre/warn "Disconnecting client due to invalid JWT!" rep)
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

(def after utils/after)