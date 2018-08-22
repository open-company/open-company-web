(ns oc.web.components.cmail
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.ziggeo :refer (ziggeo-player ziggeo-recorder)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(rum/defcs cmail < rum/reactive
                   (drv/drv :cmail-fullscreen)
                   (drv/drv :current-user-data)
  [s]
  (let [cmail-fullscreen (drv/react s :cmail-fullscreen)
        current-user-data (drv/react s :current-user-data)]
    [:div.cmail-outer
      {:class (utils/class-set {:fullscreen cmail-fullscreen})}
      [:div.cmail-middle
        [:div.cmail-container
          [:div.cmail-header
            (user-avatar-image current-user-data)
            "Post to general"
            [:button.mlb-reset.fullscreen-bt
              {:on-click #(activity-actions/cmail-toggle-fullscreen)
               :data-toggle "tooltip"
               :data-placement "top"
               :data-container "body"
               :title "Fullscreen"}]]]]]))
