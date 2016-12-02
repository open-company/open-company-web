(ns open-company-web.components.topic-view
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.topic :refer (topic)]))

(defcomponent topic-view [{:keys [card-width
                                  columns-num
                                  selected-topic-view
                                  company-data] :as data} owner options]
  (render [_]
    (let [topic-view-width (responsive/topic-view-width card-width columns-num)
          card-width (responsive/calc-update-width columns-num)
          topic-data (->> selected-topic-view keyword (get company-data))]
      (dom/div {:class "topic-view"
                :style {:width (str (+ card-width 40) "px")
                        :margin-right (str (max 0 (- topic-view-width (+ card-width 40) 40)) "px")}}
        (dom/div {:class "topic-view-internal"
                  :style {:width (str card-width "px")}}
          (om/build topic {:section selected-topic-view
                           :section-data topic-data
                           :card-width card-width
                           :foce-data-editing? (:foce-data-editing? data)
                           :read-only-company (:read-only company-data)
                           :currency (:currency company-data)
                           :is-topic-view true}
                           {:opts {:section-name selected-topic-view}}))))))