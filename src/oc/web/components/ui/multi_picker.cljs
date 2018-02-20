(ns oc.web.components.ui.multi-picker
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.components.rich-body-editor :as rich-body-editor]))

(rum/defcs multi-picker < rum/static
                          (rum/local false ::show-menu)
  [s picker-button-id]
  [:div.multi-picker-container
    [:button.mlb-reset.multi-picker-btn
      {:aria-label "show multi picker"
       :id picker-button-id
       :on-click #(swap! (::show-menu s) not)}]
    (when @(::show-menu s)
      [:div.multi-picker
        [:button.mlb-reset.multi-picker-choice.choice-images
          {:on-click #(rich-body-editor/add-photo @rich-body-editor/rich-body-editor-state nil)}
          [:div.multi-picker-choice-icon]
          "Images"]
        [:button.mlb-reset.multi-picker-choice.choice-media
          {:on-click #(rich-body-editor/add-video @rich-body-editor/rich-body-editor-state)}
          [:div.multi-picker-choice-icon]
          "Media"]
        [:button.mlb-reset.multi-picker-choice.choice-attachment
          {:on-click #(rich-body-editor/add-attachment @rich-body-editor/rich-body-editor-state nil)}
          [:div.multi-picker-choice-icon]
          "Attachment"]])])