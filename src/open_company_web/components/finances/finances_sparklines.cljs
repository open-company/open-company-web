(ns open-company-web.components.finances.finances-sparklines
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.finance-utils :as finance-utils]
            [open-company-web.components.finances.finances-metric :refer (finances-metric)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defcomponent finances-sparkline [{:keys [finances-data data-key currency charts-count archive-cb edit-cb card-width] :as data} owner]

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=tooltip]"))))

  (render [_]
    (dom/div {:class "finances-sparkline sparkline group"
              :id (str "finances-sparkline-" (name data-key))
              :key (name data-key)}
      (dom/div {:class "center-box"}
        (let [subsection-data {:finances-data finances-data
                               :data-key data-key
                               :currency currency
                               :read-only true
                               :circle-radius 2
                               :circle-stroke 3
                               :circle-fill (finance-utils/color-for-metric data-key)
                               :circle-selected-stroke 5
                               :line-stroke-width 2}
              fixed-card-width (if (responsive/is-mobile?)
                                 (.-clientWidth (.-body js/document)) ; use all the possible space on mobile
                                 card-width)]
          (om/build finances-metric subsection-data {:opts {:chart-size {:width (- fixed-card-width 50  ;; margin left and right
                                                                                                  170 ;; max left label size of the sparkline
                                                                                                  40  ;; internal padding
                                                                                                  15) ;; internal spacing
                                                                       :height 30}
                                                          :hide-nav true
                                                          :chart-fill-polygons false}}))))))

(defcomponent finances-sparklines [{:keys [finances-data currency archive-cb edit-cb] :as data} owner]

  (init-state [_]
    {:card-width (responsive/calc-card-width)})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (events/listen js/window EventType/RESIZE #(om/set-state! owner :card-width (responsive/calc-card-width)))))

  (render-state [_ {:keys [card-width]}]
    (dom/div {:class (str "finances-sparklines group sparklines" (when (= (dis/foce-section-key) :finances) " editing"))}
      (dom/div {:class "finances-sparklines-inner left group"
                :style {:width "90%"}}
        (let [sum-revenues (apply + (map utils/abs (map :revenue finances-data)))]
          (for [slug (if (pos? sum-revenues) [:revenue :costs :cash] [:costs :cash])]
            (om/build finances-sparkline {:finances-data finances-data
                                          :data-key slug
                                          :currency currency
                                          :card-width card-width}))))
      (dom/div {:class "actions group right"}
        (dom/button
          {:class "btn-reset"
           :data-placement "right"
           :data-container "body"
           :data-toggle "tooltip"
           :title "Edit chart"
           :on-click #(edit-cb)}
          (dom/i {:class "fa fa-pencil"}))
        (dom/button
          {:class "btn-reset"
           :data-placement "right"
           :data-container "body"
           :data-toggle "tooltip"
           :title "Remove this chart"
           :on-click #(archive-cb)}
          (dom/i {:class "fa fa-times"}))))))