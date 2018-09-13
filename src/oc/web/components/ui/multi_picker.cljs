(ns oc.web.components.ui.multi-picker
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(rum/defcs multi-picker < rum/static
                          (rum/local nil ::window-click-listener)
  [s {:keys [toggle-button-id add-photo-cb add-video-cb add-attachment-cb]}]
  [:div.multi-picker-container
    [:button.mlb-reset.multi-picker-choice.choice-images
      {:on-click #(add-photo-cb %)
       :data-toggle "tooltip"
       :data-placement "top"
       :data-container "body"
       :title "Insert image"}
      [:div.multi-picker-choice-icon]]
    [:button.mlb-reset.multi-picker-choice.choice-attachment
      {:on-click #(add-attachment-cb %)
       :data-toggle "tooltip"
       :data-placement "top"
       :data-container "body"
       :title "Add attachment"}
      [:div.multi-picker-choice-icon]]
    [:button.mlb-reset.multi-picker-choice.choice-media
      {:on-click #(add-video-cb %)
       :data-toggle "tooltip"
       :data-placement "top"
       :data-container "body"
       :title "Embed video"}
      [:div.multi-picker-choice-icon]]])