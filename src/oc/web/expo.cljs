(ns oc.web.expo
  (:require [dommy.core :as dom]))

(defn- handle-expo-event
  [e]
  (let [detail (js->clj (.-detail e) :keywordize-keys true)]
    (js/alert (pr-str detail))))

(defn- bridge-call!
  "Raises an event on the native side of the bridge with name `op` with accompanying `data`.
  Supported ops are implemented in the open-company-mobile` repository."
  [op data]
  (let [event (clj->js {:op op :data data})
        json-event (js/JSON.stringify event)]
    (js/window.ReactNativeWebView.postMessage json-event)))

(defn- bridge-log!
  "Logs the given data to the native console."
  [data]
  (bridge-call! "log" data))

(defn- bridge-web-ready!
  "Informs the native side of the bridge that web is ready to receive events."
  []
  (bridge-call! "web-ready" {}))

(defn ^:export setup-bridge
  []
  ;; Subscribe to future events
  (let [app (js/document.getElementById "app")]
    (when app
      (.addEventListener app "expoEvent" handle-expo-event)))
  ;; Let the native side know that we're all set
  (bridge-web-ready!))
