(ns oc.web.components.ui.multi-picker
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs multi-picker < rum/static
                          (rum/local false ::show-menu)
                          (rum/local nil ::window-click-listener)
                          {:will-mount (fn [s]
                            (reset! (::window-click-listener s)
                             (events/listen js/window EventType/CLICK
                              #(when (and @(::show-menu s)
                                          (not (utils/event-inside? % (rum/dom-node s))))
                                 (reset! (::show-menu s) false))))
                            s)
                           :will-unmount (fn [s]
                            (when @(::window-click-listener s)
                              (events/unlistenByKey @(::window-click-listener s))
                              (reset! (::window-click-listener s) nil))
                            s)}
  [s {:keys [toggle-button-id add-photo-cb add-video-cb add-attachment-cb]}]
  [:div.multi-picker-container
    [:button.mlb-reset.multi-picker-btn
      {:aria-label "show multi picker"
       :data-toggle "tooltip"
       :data-placement "top"
       :data-container "body"
       :title "Add media and attachments"
       :id toggle-button-id
       :on-click #(swap! (::show-menu s) not)}]
    (when @(::show-menu s)
      [:div.multi-picker
        [:button.mlb-reset.multi-picker-choice.choice-images
          {:on-click #(add-photo-cb %)}
          [:div.multi-picker-choice-icon]
          "Images"]
        [:button.mlb-reset.multi-picker-choice.choice-media
          {:on-click #(add-video-cb %)}
          [:div.multi-picker-choice-icon]
          "Media"]
        [:button.mlb-reset.multi-picker-choice.choice-attachment
          {:on-click #(add-attachment-cb %)}
          [:div.multi-picker-choice-icon]
          "Attachment"]])])