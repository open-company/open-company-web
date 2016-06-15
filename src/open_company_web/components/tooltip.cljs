(ns open-company-web.components.tooltip
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn on-click-out [owner options e]
  ((:dismiss-tooltip options)))

(defcomponent tooltip [data owner options]

  (did-mount [_]
    (om/set-state! owner :shown true)
    (let [click-listener (events/listen (sel1 [:body]) EventType/CLICK (partial on-click-out owner options))]
      (om/set-state! owner :click-listener click-listener)))

  (will-unmount [_]
    (events/unlistenByKey (om/get-state owner :click-listener)))

  (render-state [_ {:keys [shown]}]
    (dom/div {:class (str "tooltip-container " (:class options))}
      (dom/div {:class (str "tooltip-box" (when shown " shown"))}
        (dom/div {:class "triangle"})
        (dom/div {:class "tooltip-cta"} (:cta data))))))