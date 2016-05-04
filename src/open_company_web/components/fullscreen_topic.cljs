(ns open-company-web.components.fullscreen-topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [goog.style :refer (setStyle)]
            [goog.fx.Animation.EventType :as EventType]
            [goog.fx.dom :refer (Fade)]))

(defn show-fullscreen-topic [owner]
  (dommy/add-class! (sel1 [:body]) :no-scroll)
  (.play
    (new Fade (om/get-ref owner "fullscreen-topic") 0 1 utils/oc-animation-duration)))

(defn hide-fullscreen-topic [owner options]
  (dommy/remove-class! (sel1 [:body]) :no-scroll)
  (let [fade-out (new Fade (om/get-ref owner "fullscreen-topic") 1 0 utils/oc-animation-duration)]
    (doto fade-out
      (.listen EventType/FINISH
        #((:close-overlay-cb options)))
      (.play))))

(defcomponent fullscreen-topic [{:keys [section section-data selected-metric currency] :as data} owner options]

  (did-mount [_]
    (show-fullscreen-topic owner))

  (render-state [_ {:keys [] :as state}]
    (dom/div #js {:className "fullscreen-topic"
                  :ref "fullscreen-topic"
                  :onClick #(hide-fullscreen-topic owner options)}
      (dom/div {:class "close"})
      (dom/div {:class "fullscreen-topic-content"}))))