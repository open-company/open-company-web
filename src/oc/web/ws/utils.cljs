(ns oc.web.ws.utils
  (:require [taoensso.timbre :as timbre]
            [taoensso.sente :as s]
            [oc.web.lib.jwt :as j]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.raven :as sentry]
            [oc.web.local-settings :as ls]))

;; cmd queue
(defonce cmd-queue (atom {}))

(defn buffer-cmd [service-name args]
  (let [key (keyword service-name)]
    (swap! cmd-queue (assoc @cmd-queue key (conj (key @cmd-queue) args)))))

(defn reset-queue [service-name]
  (reset! cmd-queue (assoc @cmd-queue (keyword service-name) {})))

(defn send-queue [service-name chsk-send! ch-state]
  (when (and chsk-send! (:open? @@ch-state))
    (doseq [qargs ((keyword service-name) @cmd-queue)]
      (apply chsk-send! qargs))
    (reset-queue service-name)))

(declare sentry-report)
(defn not-connected [service-name chsk-send! ch-state & args]
  (let [arg (first (first args))]
    (buffer-cmd service-name arg)
    (sentry-report service-name chsk-send! ch-state arg)))

(defn send! [service-name chsk-send! ch-state & args]
  (if (and @chsk-send! (:open? @@ch-state))
    (do
      ;; empty queue first
      (send-queue service-name @chsk-send! ch-state)
      ;; send current command
      (apply @chsk-send! (first args)))
    ;;disconnected
    (not-connected service-name chsk-send! ch-state args)))

;; Connection check
(defn sentry-report [service-name chsk-send! ch-state & [action-id infos]]
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
    (sentry/capture-message (str "Send over closed " service-name " WS connection"))
    (sentry/clear-extra-context!)
    (timbre/error "Send over closed " service-name " WS connection" ctx)))

(defn check-interval [last-interval service-name chsk-send! ch-state]
  (when @last-interval
    (.clearInterval js/window @last-interval))
  (reset! last-interval
   (.setInterval js/window
    #(when (or (not @ch-state)
               (not @@ch-state)
               (not (:open? @@ch-state)))
       (sentry-report service-name chsk-send! ch-state))
    (* ls/ws-monitor-interval 1000))))

(defn reconnect [last-interval service-name chsk-send! ch-state]
  (send-queue service-name @chsk-send! ch-state)
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
          (sentry-report service-name chsk-send! ch-state "jwt-validation/auth-check"
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