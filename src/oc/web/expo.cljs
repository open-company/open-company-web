(ns oc.web.expo
  "Cljs side of the communication bridge between this web app and the Expo mobile
  app container (a ReactNativeWebView component).
  See https://github.com/open-company/open-company-mobile/blob/master/src/nativeWebBridge.js
  for the native side of the bridge."
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
  (bridge-log! (str "on-push-notification-permission: " json-str))
  (if-let [token (parse-bridge-data json-str)]
    (do
      (bridge-log! "Adding Expo push token")
      (user-actions/add-expo-push-token token))
    (do
      (bridge-log! "Push notification permission denied by user")
      (user-actions/deny-push-notification-permission))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Grabbing the deep link origin for creating mobile URLs

(def ^:private deep-link-origin (atom nil))

(defn get-deep-link-origin
  []
  @deep-link-origin)

(defn bridge-get-deep-link-origin
  ""
  []
  (bridge-call! "get-deep-link-origin" nil))

(defn- ^:export on-deep-link-origin
  ""
  [json-str]
  (when-let [origin (parse-bridge-data json-str)]
    (bridge-log! origin)
    (reset! deep-link-origin origin)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Grabbing the app version from the expo wrapper

(def ^:private app-version (atom nil))

(defn get-app-version
  []
  @app-version)

(defn bridge-get-app-version
  ""
  []
  (bridge-call! "get-app-version" nil))

(defn- ^:export on-app-version
  ""
  [av]
  (when av
    (bridge-log! (str "on-app-version " av))
    (reset! app-version av)))
