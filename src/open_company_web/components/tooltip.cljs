(ns open-company-web.components.tooltip
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn on-click-in [owner options e]
  (.stopPropagation e))

(defn on-click-out [owner options e]
  ((:dismiss-tooltip options)))

(defcomponent tooltip [data owner options]

  (did-mount [_]
    (let [click-listener (events/listen (sel1 [:body]) EventType/CLICK (partial on-click-out owner options))]
      (om/set-state! owner :click-listener click-listener)))

  (will-unmount [_]
    (events/unlistenByKey (om/get-state owner :click-listener)))

  (render [_]
    (dom/div {:class "tooltip-container"}
      (dom/div {:class "tooltip-box"
                :on-click (partial on-click-in owner options)}
        (dom/div {:class "triangle"})
        (dom/div {:class "tooltip-cta"} (:cta data))))))