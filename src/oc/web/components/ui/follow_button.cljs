(ns oc.web.components.ui.follow-button
  (:require [rum.core :as rum]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]))

(rum/defc follow-button < rum/static
  [{following :following
    resource-type :resource-type
    resource-uuid :resource-uuid
    {:keys [active-on active-off hover-on hover-off]
     :or {active-on "Following"
          active-off "Follow"
          hover-on "Unfollow"
          hover-off "Follow"}} :button-copy}]
  [:button.mlb-reset.follow-button
    {:class (when following "unfollow")
     :on-click #(if (= resource-type :board)
                  (user-actions/toggle-board resource-uuid)
                  (user-actions/toggle-publisher resource-uuid))}
    [:span.main-title
      (if following
        active-on
        active-off)]
    [:span.hover-title
      (if following
        hover-on
        hover-off)]])