(ns oc.web.components.growth.growth-sparklines
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.oc-colors :as occ]
            [oc.web.components.growth.growth-metric :refer (growth-metric)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(def editing-actions-width 15)

(defcomponent growth-sparkline [{:keys [metric-data
                                        metric-metadata
                                        currency
                                        archive-cb
                                        card-width
                                        editing?] :as data} owner]

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=tooltip]"))))

  (render [_]
    (dom/div {:class "growth-sparkline sparkline group"
              :id (str "growth-sparkline-" (:slug metric-metadata))}
      (let [ww (responsive/ww)
            total-card-width (if (>= ww responsive/c1-min-win-width) card-width ww)
            center-box-width (if (responsive/is-mobile-size?)
                               (- total-card-width 20 80)
                               (- card-width 80 (if editing? editing-actions-width 0)))]
        (dom/div {:class "center-box"
                  :style {:width (str center-box-width "px")}}
          (let [fixed-card-width (if (responsive/is-mobile-size?)
                                   total-card-width ; use all the possible space on mobile
                                   card-width)
                chart-width (- fixed-card-width
                               50                                     ;; margin left and right
                               190                                    ;; max left label size of the sparkline
                               40                                     ;; internal padding
                               5                                      ;; internal spacing
                               (if editing? editing-actions-width 0)) ;; remove 15 more pixel only in editing mode
                subtopic-data {:metric-data metric-data
                                 :metric-info metric-metadata
                                 :currency currency
                                 :card-width card-width
                                 :read-only true
                                 :circle-radius 2
                                 :circle-stroke 3
                                 :circle-fill (occ/get-color-by-kw :oc-dark-blue)
                                 :circle-selected-stroke 5
                                 :line-stroke-width 2
                                 :chart-size {:height 40
                                              :width (max 30 (min 100 chart-width))}}]
            (om/build growth-metric subtopic-data {:opts {:hide-nav true
                                                            :chart-fill-polygons false}}))))
      (dom/div {:class "actions group right"}
        (dom/button
          {:class "btn-reset"
           :data-placement "right"
           :data-container "body"
           :data-toggle "tooltip"
           :title "Edit chart"
           :on-click #(dis/dispatch! [:foce-data-editing-start (:slug metric-metadata)])}
          (dom/i {:class "fa fa-pencil"}))
        (dom/button
          {:class "btn-reset"
           :data-placement "right"
           :data-container "body"
           :data-toggle "tooltip"
           :title "Remove this chart"
           :on-click #(archive-cb (:slug metric-metadata))}
          (dom/i {:class "fa fa-times"}))))))

(defcomponent growth-sparklines [{:keys [growth-data growth-metrics growth-metric-slugs currency archive-cb editing? card-width columns-num] :as data} owner]

  (render [_]
    (dom/div {:class (str "growth-sparklines sparklines" (when (= (dis/foce-topic-key) :growth) " editing"))}
      (for [slug growth-metric-slugs]
        (om/build growth-sparkline {:metric-data (filter #(= (keyword (:slug %)) (keyword slug)) (vals growth-data))
                                    :metric-metadata (get growth-metrics slug)
                                    :currency currency
                                    :card-width card-width
                                    :editing? editing?
                                    :archive-cb archive-cb})))))