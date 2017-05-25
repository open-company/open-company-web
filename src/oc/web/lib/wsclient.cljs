(ns oc.web.lib.wsclient
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [taoensso.sente :as s]
            [taoensso.timbre :as timbre]
            [cljs.core.async :as async :refer [<! >! chan]]
            [taoensso.encore :as encore :refer-macros (have)]
            [oc.web.lib.jwt :as j]
            [oc.web.local-settings :as ls]
            [goog.Uri :as guri]))

(def ws-port 3002)

(def ws-server "localhost")

(def current-board-path (atom nil))
(def channelsk (atom nil))
(def ch-chsk (atom nil))
(def ch-state (atom nil))
(def chsk-send! (atom nil))

;; Auth

(defn should-disconnect? [rep]
  (when-not (:valid rep)
    (timbre/warn "disconnecting client due to invalid JWT!" rep)
    (s/chsk-disconnect! @channelsk)))

(defn post-handshake-auth []
  (timbre/debug "Trying post handshake jwt auth")
  (@chsk-send! [:auth/jwt {:jwt (j/jwt)}] 1000 should-disconnect?))

;; Event handlers

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
  (timbre/debug "Push event from server: " ?data))

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

(defonce router_ (atom nil))

(defn  stop-router! []
  (when-let [stop-f @router_]
    (stop-f)
    (timbre/info "Connection closed")))

(defn start-router! []
  (stop-router!)
  (reset! router_
    (s/start-client-chsk-router!
      @ch-chsk event-msg-handler))
  (timbre/info "Connection estabilished"))

(defn reconnect [ws-link uid]
  (let [ws-uri (guri/parse (:href ws-link))
        ws-domain (str (.getDomain ws-uri) (when (.getPort ws-uri) (str ":" (.getPort ws-uri))))
        ws-board-path (.getPath ws-uri)]
    (when (or (not @ch-state)
              (not (:open? @@ch-state))
              (not= @current-board-path ws-board-path))
      (timbre/debug "Reconnect for" (:href ws-link) "and" uid "current state:" @ch-state "current board:" @current-board-path)
      ; if the path is different it means
      (when (and @ch-state
                 (:open? @@ch-state))
        (timbre/info "Closing previous connection for" @current-board-path)
        (stop-router!)
        (reset! router_ nil))
      (timbre/info "Attempting connection to" ws-domain "for board" ws-board-path)
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