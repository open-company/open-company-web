(ns oc.web.components.ui.carrot-tip
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc carrot-tip < rum/static
  [{:keys [x y title message footer on-next-click]}]
  [:div.carrot-tip-container
    [:div.carrot-tip
      {:style {:left (str x "px") :top (str y "px")}}
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
            "Next"]]]]])