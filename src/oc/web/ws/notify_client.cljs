(ns oc.web.ws.notify-client
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]
                   [taoensso.encore :refer (have)])
  (:require [cljs.core.async :refer [chan <! >! timeout pub sub unsub unsub-all]]
            [taoensso.sente :as s]
            [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as j]
            [oc.lib.time :as time]
            [oc.web.local-settings :as ls]
            [oc.web.actions.jwt :as ja]
            [oc.web.ws.utils :as ws-utils]
            [goog.Uri :as guri]))

;; Sente WebSocket atoms
(defonce channelsk (atom nil))
(defonce ch-chsk (atom nil))
(defonce ch-state (atom nil))
(defonce chsk-send! (atom nil))
(defonce ch-pub (chan))

(defonce last-ws-link (atom nil))

;; Publication that handlers will subscribe to
(defonce publication
  (pub ch-pub :topic))

;; Send wrapper

(defn- send! [chsk-send! & args]
  (if @chsk-send!
    (apply @chsk-send! args)
    (ws-utils/sentry-report "Notify" ch-state (first (first args)))))

(defn notifications-watch []
  (timbre/debug "Watching notifications.")
  (send! chsk-send! [:watch/notifications {}]))

;; Auth
(declare reconnect)

(defn should-disconnect? [rep]
  (when (:valid rep)
    (notifications-watch))
  (when-not (:valid rep)
    (timbre/warn "disconnecting client due to invalid JWT!" rep)
    (s/chsk-disconnect! @channelsk)
    (if (j/expired?)
      (ja/jwt-refresh
       #(reconnect @last-ws-link (j/user-id)))
      (ws-utils/report-invalid-jwt "Notify" ch-state))))

(defn post-handshake-auth []
  (timbre/debug "Trying post handshake jwt auth")
  (if (j/expired?)
    (ja/jwt-refresh
     #(send! chsk-send! [:auth/jwt {:jwt (j/jwt)}] 1000 should-disconnect?))
    (send! chsk-send! [:auth/jwt {:jwt (j/jwt)}] 1000 should-disconnect?)))

(defn subscribe
  [topic handler-fn]
  (let [ws-nc-chan (chan)]
    (sub publication topic ws-nc-chan)
    (go-loop []
      (handler-fn (<! ws-nc-chan))
      (recur))))

;; Event handler

(defmulti event-handler
  "Multimethod to handle our internal events"
  (fn [event & _]
    (timbre/debug "event-handler" event)
    event))

(defmethod event-handler :default
  [_ & r]
  (timbre/info "No event handler defined for" _))

(defmethod event-handler :chsk/ws-ping
  [_ & r]
  (go (>! ch-pub { :topic :chsk/ws-ping })))

;; ex: {:user-id "e392-488b-9915", :notifications (...)}
(defmethod event-handler :user/notifications
  [_ body]
  (timbre/debug "Notifications list event" body)
  (go (>! ch-pub { :topic :user/notifications :data body })))

(defmethod event-handler :user/notification
  [_ body]
  (timbre/debug "Live notification event" body)
  (go (>! ch-pub { :topic :user/notification :data body })))

;; Sente events handlers

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler :default
  [{:as ev-msg :keys [event]}]
  ; Default/fallback case (no other matching handler)
  (timbre/warn "Unhandled event: " event))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (let [[old-state-map new-state-map] (have vector? ?data)]
    (if (:first-open? new-state-map)
      (timbre/debug "Channel socket successfully established!: " new-state-map)
      (timbre/debug "Channel socket state change: " new-state-map))))

(defmethod -event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (timbre/debug "Push event from server: " ?data)
  (apply event-handler ?data))

(defmethod -event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (post-handshake-auth)
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (timbre/debug "Handshake:" ?uid ?csrf-token ?handshake-data)))

;; ----- Sente event router (our `event-msg-handler` loop) -----

(defn  stop-router! []
  (when @channelsk
    (s/chsk-disconnect! @channelsk)
    (timbre/info "Connection closed")))

(defn start-router! []
  (s/start-client-chsk-router! @ch-chsk event-msg-handler)
  (timbre/info "Connection estabilished"))

(defn reconnect
  "Connect or reconnect the WebSocket connection to the notify service"
  [ws-link uid]
  (let [ws-uri (guri/parse (:href ws-link))
        ws-domain (str (.getDomain ws-uri) (when (.getPort ws-uri) (str ":" (.getPort ws-uri))))
        ws-org-path (.getPath ws-uri)]
    (ws-utils/check-interval "Notify" chsk-send! ch-state)
    (when (or (not @ch-state)
            (not (:open? @@ch-state)))

      ;; Need a connection to notification service
      (do
        (timbre/debug "Reconnect for" (:href ws-link) "and" uid "current state:" @ch-state)
        ; if the path is different it means
        (when (and @ch-state
                   (:open? @@ch-state))
          (timbre/info "Closing previous connection")
          (stop-router!))
        (timbre/info "Attempting notification service connection to:" ws-domain)
        (let [{:keys [chsk ch-recv send-fn state] :as x} (s/make-channel-socket! ws-org-path
                                                          {:type :auto
                                                           :host ws-domain
                                                           :protocol (if ls/jwt-cookie-secure :https :http)
                                                           :packer :edn
                                                           :uid uid
                                                           :params {:user-id uid}})]
            (reset! channelsk chsk)
            (reset! ch-chsk ch-recv)
            (reset! chsk-send! send-fn)
            (reset! ch-state state)
            (start-router!))))))