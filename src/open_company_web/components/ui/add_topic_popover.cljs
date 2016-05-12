(ns open-company-web.components.ui.add-topic-popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn is-child-of-popover [el]
  (loop [cur-el el]
    (if (= (.-id cur-el) "add-topic-popover")
      true
      (if (.-parentNode cur-el)
        (recur (.-parentNode cur-el))
        false))))

(defn on-click-out [owner options e]
  (when (not (is-child-of-popover (.-target e)))
    ((:dismiss-popover options) nil)))

(defn on-click-in [owner options e]
  (.stopPropagation e))

(defcomponent add-topic-popover [data owner options]

  (did-mount [_]
    (let [listener (events/listen (sel1 [:body]) EventType/CLICK (partial on-click-out owner options))]
      (om/set-state! owner :click-out-listener listener)))

  (will-unmount [_]
    (events/unlistenByKey (om/get-state owner :click-out-listener)))

  (render [_]
    (dom/div {:class "add-topic-popover"
              :id "add-topic-popover"
              :on-click (partial on-click-in owner options)}
      (dom/div {:class "triangle"})
      (dom/div {:class "add-topic-popover-header"} "CHOOSE A TOPIC")
      (dom/div {:class "add-topic-popover-subheader"} "SUGGESTED TOPICS")
      (dom/div {:class "topics-to-add"}
        (dom/div {:class "potential-topic"} "Finances")
        (dom/div {:class "potential-topic"} "Key Metrics")
        (dom/div {:class "potential-topic"} "Key Metrics")
        (dom/div {:class "potential-topic"} "Key Metrics")
        (dom/div {:class "potential-topic"} "Key Metrics")
        (dom/div {:class "potential-topic"} "Key Metrics")
        (dom/div {:class "potential-topic"} "Key Metrics")
        (dom/div {:class "potential-topic"} "Key Metrics")
        (dom/div {:class "potential-topic"} "Key Metrics")
        (dom/div {:class "potential-topic"} "Key Metrics")
        (dom/div {:class "potential-topic"} "Key Metrics")))))