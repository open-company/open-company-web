(ns oc.web.expo
  (:require [dommy.core :as dom]
            [oc.web.actions.user :as user-actions]))

(defn- bridge-call!
  "Raises an event on the native side of the bridge with name `op` with accompanying `data`.
  Supported ops are implemented in the open-company-mobile` repository."
  [op data]
  (let [event (clj->js {:op op :data data})
        json-event (js/JSON.stringify event)]
    (js/window.ReactNativeWebView.postMessage json-event)))

(defn- parse-bridge-data
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

(bridge-get-push-notification-token!)


