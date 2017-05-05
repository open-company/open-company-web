(ns oc.web.lib.wsclient
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [taoensso.sente :as s]
            [cljs.core.async :as async :refer [<! >! chan]]))

(def ws-port 3000)

(def ws-server "localhost")

(let [{:keys [chsk ch-recv send-fn state]}
      (s/make-channel-socket! "/chsk" {:type :auto :host "localhost:3000"})]
  (def chsk       chsk)
  (def ch-chsk    ch-recv)
  (def chsk-send! send-fn)
  (def chsk-state chsk-state))

(defn handle-event [arg]
  nil)

(declare test-session)

(defn event-loop
  "Handle inbound events."
  []
  (go (loop [[op arg] (:event (<! ch-chsk))]
        #_(js/console.log "-" op)
        (case op
          :chsk/recv (handle-event arg)
          ;; we ignore other Sente events
          (test-session))
        (recur (:event (<! ch-chsk))))))

(defn send-message
  "Ping the server to update the sesssion state."
  [msg]
  (js/console.log "Sending ws message " msg)
  (chsk-send! msg))