(ns oc.web.components.ui.carrot-tip
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.onboard-tip-svg :refer (get-onboard-image)]))

(rum/defc carrot-tip < rum/static
  [{:keys [x y title message footer on-next-click]}]
  [:div.carrot-tip-container
    (let [width (.-innerWidth js/window)
          height (.-innerHeight js/window)
          px (- x 170)
          py (+ y 60)]
      [:div.carrot-tip-background
        {:dangerouslySetInnerHTML #js {"__html" (get-onboard-image width height px py)}}])
    [:div.carrot-tip
      {:style {:left (str (+ x 50) "px") :top (str y "px")}}
      [:div.triangle]
      [:div.carrot-tip-inner
        [:div.carrot-tip-title
          title]
        [:div.carrot-tip-description
          message]
        [:div.carrot-tip-footer.group
          [:div.carrot-tip-num
            footer]
          [:button.mlb-reset.mlb-default.next-button
            {:on-click (fn [e]
                         (utils/event-stop e)
                         (when (fn? on-next-click)
                           (on-next-click)))}
            "Got It!"]]]]])