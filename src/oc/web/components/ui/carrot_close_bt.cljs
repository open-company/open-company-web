(ns oc.web.components.ui.carrot-close-bt
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(def default-size 32)

(rum/defcs carrot-close-bt < rum/static
                             (rum/local false ::hovering)
  [s {:keys [on-click width height]}]
  (let [fixed-width (if width width default-size)
        fixed-height (if height height default-size)
        image-width (js/Math.round (/ fixed-width 2.7))
        image-height (js/Math.round (/ fixed-height 2.7))]
    [:button.carrot-close-bt.mlb-reset
      {:on-click on-click
       :style {:width (str fixed-width "px")
               :height (str fixed-height "px")
               :padding-bottom (str (/ (- fixed-height image-height) 2) "px")}
       :on-mouse-enter #(reset! (::hovering s) true)
       :on-mouse-leave #(reset! (::hovering s) false)}
      [:img {:src (utils/cdn (str "/img/ML/board_remove_filter" (when @(::hovering s) "_white") ".png"))
             :style {:width (str image-width "px")
                     :height (str image-height "px")}}]]))