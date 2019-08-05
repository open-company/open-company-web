(ns oc.web.expo
  "TODO: Write a couple notes here briefly explaining the bridge."
  (:require [oc.web.actions.user :as user-actions]))

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

(defn- bridge-get-push-notification-token!
  "Requests the Expo push notification token from native, possibly displaying a permissions
  dialog on iOS. Will call the `on-push-notification-token` fn of this ns with the fetched
  token, or nil if the user has denied the permission."
  []
  (bridge-call! "get-push-notification-token" nil))

(defn ^:export on-push-notification-token
  "Called by the native side of the bridge with an Expo push notification token, or nil
  if the user has denied the notification permission."
  [json-str]
  (if-let [token (parse-bridge-data json-str)]
    (user-actions/add-expo-push-token token)
    (js/alert "Notification permission denied!")))

;; TODO: Figure out where to actually call this properly (possibly core?)
;; (bridge-get-push-notification-token!)
