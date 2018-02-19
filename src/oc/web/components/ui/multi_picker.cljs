(ns oc.web.components.ui.multi-picker
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]))

(rum/defcs multi-picker < rum/static
                          (rum/local false ::show-menu)
  [s]
  [:div.multi-picker-container
    {:on-mouse-enter #(reset! (::show-menu s) true)
     :on-mouse-leave #(reset! (::show-menu s) false)}
    [:button.mlb-reset.multi-picker-btn
      {:aria-label "show multi picker"
       :on-click #(swap! (::show-menu s) not)}]
    (when @(::show-menu s)
      [:div.multi-picker
        [:button.mlb-reset.multi-picker-choice.choice-images
          [:div.multi-picker-choice-icon]
          "Images"]
        [:button.mlb-reset.multi-picker-choice.choice-media
          [:div.multi-picker-choice-icon]
          "Media"]
        [:button.mlb-reset.multi-picker-choice.choice-attachment
          [:div.multi-picker-choice-icon]
          "Attachment"]])])