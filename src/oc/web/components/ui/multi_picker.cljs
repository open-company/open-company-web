(ns oc.web.components.ui.multi-picker
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]))

(rum/defcs multi-picker < rum/static
                          (rum/local false ::showing-media-video-modal)
                          (on-window-click-mixin (fn [s e]
                            (when-not (utils/event-inside? e (rum/ref-node s :video-button))
                              (reset! (::showing-media-video-modal s) false))))
  [s {:keys [toggle-button-id add-photo-cb add-video-cb add-attachment-cb start-video-recording-cb]}]
  [:div.multi-picker-container
    (when @(::showing-media-video-modal s)
      (media-video-modal {:record-video-cb #(start-video-recording-cb)
                          :dismiss-cb #(reset! (::showing-media-video-modal s) false)}))
    [:button.mlb-reset.multi-picker-choice.choice-images
      {:on-click #(add-photo-cb %)
       :data-toggle "tooltip"
       :data-placement "top"
       :data-container "body"
       :title "Insert image"}
      [:div.multi-picker-choice-icon]]
    [:button.mlb-reset.multi-picker-choice.choice-media
      {:on-click #(reset! (::showing-media-video-modal s) true) ;#(add-video-cb %)
       :ref :video-button
       :data-toggle "tooltip"
       :data-placement "top"
       :data-container "body"
       :title "Embed video"
       :class (when @(::showing-media-video-modal s) "active")}
      [:div.multi-picker-choice-icon]]
    [:button.mlb-reset.multi-picker-choice.choice-attachment
      {:on-click #(add-attachment-cb %)
       :data-toggle "tooltip"
       :data-placement "top"
       :data-container "body"
       :title "Add attachment"}
      [:div.multi-picker-choice-icon]]])