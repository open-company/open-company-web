(ns open-company-web.components.topic-view
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.topic :refer (topic)]))

(defcomponent topic-view [{:keys [card-width
                                  columns-num
                                  topic-name
                                  company-data] :as data} owner options]
  (render [_]
    (let [topic-view-width (responsive/topic-view-width card-width columns-num)
          topic-data (->> topic-name keyword (get company-data))]
      (js/console.log "topic-view/render" data)
      (js/console.log "   topic-data" topic-data)
      (js/console.log "   topic-view-width" topic-view-width)
      (dom/div {:class "topic-view"
                :style {:width (str topic-view-width "px")}}
        (dom/div {:class "topic-view-internal"
                  :style {:width (str card-width "px")}}
          (om/build topic {:section topic-name
                           :section-data topic-data
                           :card-width (- topic-view-width 200)
                           :foce-data-editing? (:foce-data-editing? data)
                           :read-only-company (:read-only company-data)
                           :currency (:currency company-data)}
                           {:opts {:section-name topic-name}}))))))