(ns oc.web.components.ui.follow-button
  (:require [rum.core :as rum]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]))

(rum/defc follow-button < rum/static
  [{:keys [following tooltip-position resource-type resource-uuid]}]
  [:button.mlb-reset.follow-button
    {:class (when following "unfollow")
     :data-toggle (when-not (responsive/is-mobile-size?) "tooltip")
     :data-placement (or tooltip-position "top")
     :data-container "body"
     :title (when following "Unfollow")
     :on-click #(if (= resource-type :board)
                  (user-actions/toggle-board resource-uuid)
                  (user-actions/toggle-publisher resource-uuid))}
    (if following
      "Following"
      "Follow")])