(ns oc.web.lib.ws-interaction-client
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [sablono.core :as html :refer-macros [html]]
            [taoensso.sente :as s]
            [taoensso.timbre :as timbre]
            [cljs.core.async :refer [chan <! >! timeout pub sub unsub unsub-all]]
            [taoensso.encore :as encore :refer-macros (have)]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as j]
            [oc.web.local-settings :as ls]
            [goog.Uri :as guri]))

(def current-board-path (atom nil))

;; Sente WebSocket atoms
(def channelsk (atom nil))
(def ch-chsk (atom nil))
(def ch-state (atom nil))
(def chsk-send! (atom nil))

(def ch-pub (chan))
;; Publication that handlers will subscribe to
(def publication
  (pub ch-pub :topic))

;; Auth
(defn should-disconnect? [rep]
  (when-not (:valid rep)
    (timbre/warn "disconnecting client due to invalid JWT!" rep)
    (s/chsk-disconnect! @channelsk)))

(defn post-handshake-auth []
  (timbre/debug "Trying post handshake jwt auth")
  (@chsk-send! [:auth/jwt {:jwt (j/jwt)}] 1000 should-disconnect?))

;; Actions
(defn board-watch [board-uuid]
  (timbre/debug "Watching board: " board-uuid)
  (@chsk-send! [:watch/board {:board-uuid board-uuid}] 1000))

(defn board-unwatch [callback]
  (timbre/debug "Unwatching all boards.")
  (@chsk-send! [:unwatch/board] 1000 callback))

(defn subscribe
  [topic handler-fn]
  (let [ws-ic-chan (chan)]
    (sub publication topic ws-ic-chan)
    (go-loop []
      (handler-fn (<! ws-ic-chan))
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

(defmethod event-handler :interaction-comment/add
  [_ body]
  (timbre/debug "Comment add event" body)
  (go (>! ch-pub { :topic :interaction-comment/add :data body })))

(defmethod event-handler :interaction-comment/update
  [_ body]
  (timbre/debug "Comment update event" body)
  (go (>! ch-pub { :topic :interaction-comment/update :data body })))

(defmethod event-handler :interaction-comment/delete
  [_ body]
  (timbre/debug "Comment delete event" body)
  (go (>! ch-pub { :topic :interaction-comment/delete :data body })))

(defmethod event-handler :interaction-reaction/add
  [_ body]
  (timbre/debug "Reaction add event" body)
  (go (>! ch-pub { :topic :interaction-reaction/add :data body })))

(defmethod event-handler :interaction-reaction/delete
  [_ body]
  (timbre/debug "Reaction delete event" body)
  (go (>! ch-pub { :topic :interaction-reaction/delete :data body })))


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

;; Session test
(defn test-session
  "Ping the server to update the sesssion state."
  []
  (@chsk-send! [:session/status]))

;;;; Sente event router (our `event-msg-handler` loop)

(defn  stop-router! []
  (when @channelsk
    (s/chsk-disconnect! @channelsk)
    (timbre/info "Connection closed")))

(defn start-router! []
  (s/start-client-chsk-router! @ch-chsk event-msg-handler)
  (timbre/info "Connection estabilished"))

(defn reconnect [ws-link uid]
  (let [ws-uri (guri/parse (:href ws-link))
        ws-domain (str (.getDomain ws-uri) (when (.getPort ws-uri) (str ":" (.getPort ws-uri))))
        ws-board-path (.getPath ws-uri)]
    (when (or (not @ch-state)
              (not (:open? @@ch-state))
              (not= @current-board-path ws-board-path))
      (timbre/debug
       "Reconnect for"
       (:href ws-link)
       "and"
       uid
       "current state:"
       @ch-state
       "current board:"
       @current-board-path)
      ; if the path is different it means
      (when (and @ch-state
                 (:open? @@ch-state))
        (timbre/info "Closing previous connection for" @current-board-path)
        (stop-router!))
      (timbre/info "Attempting interaction service connection to" ws-domain "for board" ws-board-path)
      (let [{:keys [chsk ch-recv send-fn state] :as x} (s/make-channel-socket! ws-board-path
                                                        {:type :auto
                                                         :host ws-domain
                                                         :protocol (if ls/jwt-cookie-secure :https :http)
                                                         :packer :edn
                                                         :uid uid
                                                         :params {:user-id uid}})]
          (reset! current-board-path ws-board-path)
          (reset! channelsk chsk)
          (reset! ch-chsk ch-recv)
          (reset! chsk-send! send-fn)
          (reset! ch-state state)
          (start-router!)))))