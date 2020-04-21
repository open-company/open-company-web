(ns oc.web.ws.change-client
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :refer [chan <! >! timeout pub sub unsub unsub-all]]
            [defun.core :refer (defun)]
            [taoensso.sente :as s]
            [taoensso.timbre :as timbre]
            [taoensso.encore :as encore :refer-macros (have)]
            [oc.lib.time :as time]
            [oc.web.local-settings :as ls]
            [oc.web.ws.utils :as ws-utils]
            [oc.web.utils.ws-client-ids :as ws-client-ids]
            [goog.Uri :as guri]))

(defonce last-ws-link (atom nil))
(defonce current-org (atom nil))
(defonce current-uid (atom nil))
(defonce container-ids (atom []))

;; Sente WebSocket atoms
(defonce channelsk (atom nil))
(defonce ch-chsk (atom nil))
(defonce ch-state (atom nil))
(defonce chsk-send! (atom nil))
(defonce ch-pub (chan))

(defonce last-interval (atom nil))

;; Publication that handlers will subscribe to
(defonce publication
  (pub ch-pub :topic))

;; Send wrapper

(defn- send! [chsk-send! & args]
  (ws-utils/send! "Change" chsk-send! ch-state args))

;; ----- Actions -----

(defun container-watch
  ([watch-id :guard string?]
    (timbre/debug "Adding container/watch for:" watch-id)
    (container-watch [watch-id]))

  ([watch-ids :guard sequential?]
    (timbre/debug "Adding container/watch for:" watch-ids)
    ;; Remove duplicated sections by moving old and new ids into sets
    ;; and replacing with the union of the 2.
    (let [current-set (set @container-ids)
          new-set (set watch-ids)
          union-set (clojure.set/union current-set new-set)]
      (reset! container-ids (vec union-set)))
    (container-watch))

  ([]
    (timbre/debug "Sending container/watch for:" @container-ids)
    (send! chsk-send! [:container/watch @container-ids])))

(defn container-seen [container-id]
  (timbre/debug "Sending container/seen for:" container-id)
  (send! chsk-send! [:container/seen {:container-id container-id :seen-at (time/current-timestamp)}]))

(defn item-seen [publisher-id container-id item-id]
  (timbre/debug "Sending item/seen for container:" container-id "item:" item-id)
  (send! chsk-send! [:item/seen {:publisher-id publisher-id :container-id container-id :item-id item-id :seen-at (time/current-timestamp)}]))

(defn item-read [org-id container-id item-id user-name avatar-url]
  (timbre/debug "Sending item/read for container:" container-id "item:" item-id)
  (send! chsk-send! [:item/read {:org-id org-id
                                 :container-id container-id
                                 :item-id item-id
                                 :name user-name
                                 :avatar-url avatar-url
                                 :read-at (time/current-timestamp)}]))

(defn who-read-count [item-ids]
  (timbre/debug "Sending item/who-read-count for item-ids:" item-ids)
  (send! chsk-send! [:item/who-read-count item-ids]))

(defn who-read [item-ids]
  (timbre/debug "Sending item/who-read for item-ids:" item-ids)
  (send! chsk-send! [:item/who-read item-ids]))

;; Follow

(defn follow-list []
  (timbre/debug "Sending follow-list for user-id:" @current-uid "org-slug:" @current-org)
  (send! chsk-send! [:follow/list {:user-id @current-uid
                                   :org-slug @current-org}]))

;; Publishers follow

(defn publisher-watch []
  (send! chsk-send! [:user/watch {:org-slug @current-org :user-ids [@current-uid]}]))

(defn publishers-follow [publisher-uuids]
  (timbre/debug "Sending publishers/follow for user-id:" @current-uid
   "org-slug:" @current-org "with uuids:" publisher-uuids)
  (send! chsk-send! [:publishers/follow {:user-id @current-uid
                                         :org-slug @current-org
                                         :publisher-uuids (vec publisher-uuids)}]))

(defn publisher-follow [publisher-uuid]
  (timbre/debug "Sending publisher/follow for user-id:" @current-uid
   "org-slug:" @current-org "with uuid:" publisher-uuid)
  (send! chsk-send! [:publisher/follow {:user-id @current-uid
                                        :org-slug @current-org
                                        :publisher-uuid publisher-uuid}]))

(defn publisher-unfollow [publisher-uuid]
  (timbre/debug "Sending publisher/unfollow for user-id:" @current-uid
   "org-slug:" @current-org "with uuid:" publisher-uuid)
  (send! chsk-send! [:publisher/unfollow {:user-id @current-uid
                                          :org-slug @current-org
                                          :publisher-uuid publisher-uuid}]))

;; Boards follow

(defn boards-follow [board-uuids]
  (timbre/debug "Sending boards/follow for user-id:" @current-uid
   "org-slug:" @current-org "with uuids:" board-uuids)
  (send! chsk-send! [:boards/follow {:user-id @current-uid
                                     :org-slug @current-org
                                     :board-uuids (vec board-uuids)}]))

(defn board-follow [board-uuid]
  (timbre/debug "Sending board/follow for user-id:" @current-uid
   "org-slug:" @current-org "with uuid:" board-uuid)
  (send! chsk-send! [:board/follow {:user-id @current-uid
                                    :org-slug @current-org
                                    :board-uuid board-uuid}]))

(defn board-unfollow [board-uuid]
  (timbre/debug "Sending board/unfollow for user-id:" @current-uid
   "org-slug:" @current-org "with uuid:" board-uuid)
  (send! chsk-send! [:board/unfollow {:user-id @current-uid
                                      :org-slug @current-org
                                      :board-uuid board-uuid}]))

(defn subscribe
  [topic handler-fn]
  (let [ws-cc-chan (chan)]
    (sub publication topic ws-cc-chan)
    (go-loop []
      (handler-fn (<! ws-cc-chan))
      (recur))))

;; ----- Event handlers -----

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

(defmethod event-handler :container/status
  [_ body]
  (timbre/debug "Status event :container/status" body)
  (go (>! ch-pub { :topic :container/status :data body })))

(defmethod event-handler :org/change
  [_ body]
  (timbre/debug "Change event :org/change" body)
  (go (>! ch-pub { :topic :org/change :data body })))

(defmethod event-handler :container/change
  [_ body]
  (timbre/debug "Change event :container/change" body)
  (go (>! ch-pub { :topic :container/change :data body })))

(defmethod event-handler :item/change
  [_ body]
  (timbre/debug "Change event :item/change" body)
  (go (>! ch-pub { :topic :item/change :data body })))

(defmethod event-handler :item/counts
  [_ body]
  (timbre/debug "Change event :item/counts" body)
  (go (>! ch-pub { :topic :item/counts :data body })))

(defmethod event-handler :item/status
  [_ body]
  (timbre/debug "Change event :item/status" body)
  (go (>! ch-pub { :topic :item/status :data body })))

(defmethod event-handler :entry/inbox-action
  [_ body]
  (timbre/debug "Change event :entry/inbox-action" body)
  (go (>! ch-pub { :topic :entry/inbox-action :data body })))

;; Follow event handler

(defmethod event-handler :follow/list
  [_ body]
  (timbre/debug "Change event :follow/list" body)
  (go (>! ch-pub { :topic :follow/list :data body })))

;; ----- Sente event handlers -----

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
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (timbre/debug "Handshake:" ?uid ?csrf-token ?handshake-data)
    (follow-list)
    (container-watch)
    (publisher-watch)))

;; ----- Sente event router (our `event-msg-handler` loop) -----

(declare reconnect)

(defn  stop-router! []
  (when @channelsk
    (s/chsk-disconnect! @channelsk)
    (timbre/info "Connection closed")))

(defn start-router! []
  (s/start-client-chsk-router! @ch-chsk event-msg-handler)
  (timbre/info "Connection estabilished")
  (ws-utils/reconnected last-interval "Change" chsk-send! ch-state
   #(reconnect @last-ws-link @current-uid @current-org @container-ids)))

(defn reconnect
  "Connect or reconnect the WebSocket connection to the change service"
  [ws-link uid org-slug containers]
  (timbre/debug "Change service reconnect" (:href ws-link) uid org-slug containers)
  (let [ws-uri (guri/parse (:href ws-link))
        ws-domain (str (.getDomain ws-uri) (when (.getPort ws-uri) (str ":" (.getPort ws-uri))))
        ws-org-path (.getPath ws-uri)]
    ;; Save passed parameters
    (reset! last-ws-link ws-link)
    (reset! container-ids containers)
    (reset! current-org org-slug)
    (reset! current-uid uid)
    (if (or (not @ch-state)
            (not (:open? @@ch-state))
            (not= @current-org org-slug))
      ;; Need a connection to change service
      (do
        (timbre/debug "Connection is down, reconnecting. Current state:" @ch-state)
        ; if the path is different it means
        (when (and @ch-state
                   (:open? @@ch-state))
          (timbre/info "Closing previous connection for:" @current-org)
          (stop-router!))
        (timbre/info "Attempting change service connection to:" ws-domain "for org:" org-slug)
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
            (when @ch-state
              (remove-watch @ch-state :change-client-state-watcher))
            (reset! ch-state state)
            (add-watch @ch-state :change-client-state-watcher
             (fn [key a old-state new-state]
               (reset! ws-client-ids/change-client-id (:uid new-state))))
            (start-router!)))

      ;; already connected, make sure we're watching all the current containers
      (do
        (timbre/debug "Connection already up, watch containers")
        (container-watch)))))