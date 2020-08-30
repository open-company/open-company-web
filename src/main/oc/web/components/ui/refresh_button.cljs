(ns oc.web.components.ui.refresh-button
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.user :as user-actions]))

(rum/defc refresh-button < rum/static
  [{:keys [click-cb message button-copy visible class-name] :or {message "New updates available"
                                                                 button-copy "Refresh"
                                                                 visible true
                                                                 class-name ""}}]
  (let [fixed-click-cb (if (fn? click-cb)
                         click-cb
                         #(user-actions/initial-loading true))]
    [:div.refresh-button-container
      {:class (utils/class-set {:visible visible
                                class-name true})}
      [:div.refresh-button-inner
        [:span.comments-number
          ,message]
        [:button.mlb-reset.refresh-button
          {:on-click #(fixed-click-cb %)}
          button-copy]]]))