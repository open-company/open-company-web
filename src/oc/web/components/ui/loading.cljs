(ns oc.web.components.ui.loading
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc loading < rum/static
  [data]
  [:div.oc-loading
    {:class (utils/class-set {:active (:loading data)
                              :setup-screen (:nux data)})}
    ;; Top left corner
    [:div.balloon.big.big-yellow]
    [:div.balloon.small.small-red]
    [:div.balloon.small.face.small-face-purple]
    ;; Top right corner
    [:div.balloon.big.big-red]
    [:div.balloon.small.small-yellow]
    [:div.balloon.small.face.small-face-yellow]
    ;; Bottom left corner
    [:div.balloon.big.big-green]
    [:div.balloon.small.small-purple]
    ;; Bottom right corner
    [:div.balloon.big.big-purple]
    [:div.oc-loading-inner
      [:div.oc-loading-heart]
      [:div.oc-loading-body]]
    [:div.setup-cta
      "Here we go!"]])