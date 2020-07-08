(ns oc.web.components.ui.refresh-button
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.actions.user :as user-actions]))

(rum/defc refresh-button < rum/static
  [{:keys [click-cb message button-copy visible] :or {message "New updates available"
                                                      button-copy "Refresh"
                                                      visible true}}]
  (let [fixed-click-cb (if (fn? click-cb)
                         click-cb
                         #(user-actions/initial-loading true))]
    [:div.refresh-button-container
      {:class (when visible "visible")}
      [:div.refresh-button-inner
        [:span.comments-number
          ,message]
        [:button.mlb-reset.refresh-button
          {:on-click #(fixed-click-cb %)}
          button-copy]]]))