(ns open-company-web.components.growth.growth-sparklines
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.components.growth.growth-metric :refer (growth-metric)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defcomponent growth-sparkline [{:keys [metric-data metric-metadata currency archive-cb edit-cb card-width] :as data} owner]

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=tooltip]"))))

  (render [_]
    (dom/div {:class "growth-sparkline sparkline group"
              :id (str "growth-sparkline-" (:slug metric-metadata))}
      (dom/div {:class "center-box"}
        (let [subsection-data {:metric-data metric-data
                               :metric-info metric-metadata
                               :currency currency
                               :read-only true
                               :circle-radius 2
                               :circle-stroke 3
                               :circle-fill (occ/get-color-by-kw :oc-dark-blue)
                               :circle-selected-stroke 5
                               :line-stroke-width 2}
              fixed-card-width (if (responsive/is-mobile?)
                                 (.-clientWidth (.-body js/document)) ; use all the possible space on mobile
                                 card-width)]
          (om/build growth-metric subsection-data {:opts {:chart-size {:width (- fixed-card-width 50  ;; margin left and right
                                                                                                  170 ;; max left label size of the sparkline
                                                                                                  40  ;; internal padding
                                                                                                  15) ;; internal spacing
                                                                       :height 30}
                                                          :hide-nav true
                                                          :chart-fill-polygons false}})))
      (dom/div {:class "actions group right"}
        (dom/button
          {:class "btn-reset"
           :data-placement "right"
           :data-container "body"
           :data-toggle "tooltip"
           :title "Edit chart"
           :on-click #(edit-cb (:slug metric-metadata))}
          (dom/i {:class "fa fa-pencil"}))
        (dom/button
          {:class "btn-reset"
           :data-placement "right"
           :data-container "body"
           :data-toggle "tooltip"
           :title "Remove this chart"
           :on-click #(archive-cb (:slug metric-metadata))}
          (dom/i {:class "fa fa-times"}))))))

(defcomponent growth-sparklines [{:keys [growth-data growth-metrics growth-metric-slugs currency archive-cb edit-cb] :as data} owner]

  (init-state [_]
    {:card-width (responsive/calc-card-width)})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (events/listen js/window EventType/RESIZE #(om/set-state! owner :card-width (responsive/calc-card-width)))))

  (render-state [_ {:keys [card-width]}]
    (dom/div {:class (str "growth-sparklines sparklines" (when (= (dis/foce-section-key) :growth) " editing"))}
      (for [slug growth-metric-slugs]
        (om/build growth-sparkline {:metric-data (filter #(= (keyword (:slug %)) (keyword slug)) (vals growth-data))
                                    :metric-metadata (get growth-metrics slug)
                                    :currency currency
                                    :card-width card-width
                                    :archive-cb archive-cb
                                    :edit-cb edit-cb})))))