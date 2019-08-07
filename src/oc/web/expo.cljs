(ns oc.web.expo
  "TODO: Write a couple notes here briefly explaining the bridge."
  (:require [oc.web.actions.user :as user-actions]
            [oc.web.utils.user :as user-utils]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Native/web bridge primitives

(defn- bridge-call!
  "Raises an event on the native side of the bridge with name `op` and accompanying `data`.
  Supported ops are implemented in the open-company-mobile repository."
  [op data]
  (let [event (clj->js {:op op :data data})
        json-event (js/JSON.stringify event)]
    (js/window.ReactNativeWebView.postMessage json-event)))

(defn- parse-bridge-data
  "Exported web functions called by the native side of the bridge accept only
  one arg, and should use this function to deserialize the arg into clj data."
  [json-str]
  (js->clj (js/JSON.parse json-str) :keywordize-keys true))

(defn- bridge-log!
  "Logs the given data to the native console."
  [data]
  (bridge-call! "log" data))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Push notification permissions

(defn bridge-request-push-notification-permission!
  "Displays the native push notification permission dialog on iOS. If the user has already
  granted access, the response simply contains the push notification token. If the user denies
  access, the response will contain nil.
  Note: this method only pertains to iOS devices, as Android permissions are granted at the time
  of installation. On Android, this method will simply return the push notification token."
  []
  (bridge-call! "request-push-notification-permission" nil))

(defn- ^:export on-push-notification-permission
  "Callback for the `bridge-request-push-notification-permission!` bridge method. Response will
  contain the push token if the user granted permission (or had already granted permission). Response
  is `nil` if the user denied the permission (or previously denied the permission)."
  [json-str]
  (if-let [token (parse-bridge-data json-str)]
    (user-actions/add-expo-push-token token)
    (user-actions/deny-push-notification-permission)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Handling of user tapping on push notification

(defn- ^:export on-push-notification-tapped
  "Callback for responding to the user tapping on a native push notification. Response contains
  a push notification payload, which is literally a Carrot notification map."
  [json-str]
  (when-let [notification (parse-bridge-data json-str)]
    (let [fixed-notif (user-utils/fix-notification notification)
          click-handler (:click fixed-notif)]
      (click-handler))))
