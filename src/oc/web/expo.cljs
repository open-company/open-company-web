(ns oc.web.expo
  (:require [dommy.core :as dom]))

(defn- handle-expo-event
  [e]
  (let [detail (js->clj (.-detail e) :keywordize-keys true)]
    (js/alert (pr-str detail))))

(defn ^:export setup-bridge
  []
  ;; Subscribe to future events
  (let [app (js/document.getElementById "app")]
    (.addEventListener app "expoEvent" handle-expo-event)))
