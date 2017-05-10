(ns oc.web.lib.wsclient
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [taoensso.sente :as s]
            [cljs.core.async :as async :refer [<! >! chan]]
            [taoensso.encore :as encore :refer-macros (have)]
            [oc.web.lib.jwt :as j]
            [oc.web.local-settings :as ls]))

(def ws-port 3000)

(def ws-server "localhost")

(def ch-chsk (atom nil))
(def chsk-send! (atom nil))

;; Auth

(defn post-handshake-auth []
  (js/console.log "wsc/post-handshake-auth")
  (@chsk-send! [:auth/jwt {:jwt (j/jwt)}] 1000 #(js/console.log "reply" %)))

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
  (js/console.log (encore/format "Unhandled event: %s" event)))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (let [[old-state-map new-state-map] (have vector? ?data)]
    (if (:first-open? new-state-map)
      (js/console.log (encore/format "Channel socket successfully established!: %s" new-state-map))
      (js/console.log (encore/format "Channel socket state change: %s" new-state-map)))))

(defmethod -event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (js/console.log (encore/format "Push event from server: %s" ?data)))

(defmethod -event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (post-handshake-auth)
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (js/console.log "Handshake:" ?uid ?csrf-token ?handshake-data)))

;; Session test

(defn test-session
  "Ping the server to update the sesssion state."
  []
  (@chsk-send! [:session/status]))

;;;; Sente event router (our `event-msg-handler` loop)

(defonce router_ (atom nil))
(defn  stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
    (s/start-client-chsk-router!
      @ch-chsk event-msg-handler)))

(defn reconnect [uid]
  (js/console.log "wsclient/reconnect" uid)
  (let [{:keys [chsk ch-recv send-fn state] :as x}
          (s/make-channel-socket! "/chsk" {:type :auto
                                           :host (encore/format "%s:%d" ws-server ws-port)
                                           :protocol (if ls/jwt-cookie-secure :https :http)
                                           :packer :edn
                                           :uid uid
                                           :params {:user-id uid}})]
    (reset! ch-chsk ch-recv)
    (reset! chsk-send! send-fn)
    (start-router!)))