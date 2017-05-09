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

(defmulti handle-event
  "Handle events based on the event ID."
  (fn [[ev-id ev-arg] app owner] ev-id))

;; Process the server's reply by updating the application state:

(defmethod handle-event :test/reply
  [[_ msg] app owner]
  (js/console.log "handle-event/:test/reply"))

;; Ignore unknown events (we just print to the console):

(defmethod handle-event :default
  [event app owner]
  (js/console.log "handle-event/:default"))

;; Remember the session state in the application component's local state:

(defmethod handle-event :session/state
  [[_ state] app owner]
  (js/console.log "handle-event/:session/state"))

(defn test-session
  "Ping the server to update the sesssion state."
  []
  (chsk-send! [:session/status]))

(defn event-loop
  "Handle inbound events."
  []
  (go (loop [[op arg] (:event (<! ch-chsk))]
        (js/console.log "wsclient receive -" op)
        (case op
          :chsk/recv (handle-event arg)
          ;; we ignore other Sente events
          (test-session))
        (recur (:event (<! ch-chsk))))))