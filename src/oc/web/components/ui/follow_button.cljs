(ns oc.web.components.ui.follow-button
  (:require [rum.core :as rum]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]))

(def ^:private default-button-copy {
  :board {:active-on "Following"
          :active-off "Follow"
          :hover-on "Unfollow"
          :hover-off "Follow"}
  :user  {:active-on "Favorited"
          :active-off "Favorite"
          :hover-on "Unfavorite"
          :hover-off "Favorite"}})

(rum/defc follow-button < rum/static
  [{following :following
    resource-type :resource-type
    resource-uuid :resource-uuid
    button-copy :button-copy}]
  (let [{:keys [active-on active-off hover-on hover-off]}
         (if (map? button-copy) button-copy (get default-button-copy resource-type))]
    [:button.mlb-reset.follow-button
      {:class (if following "on" "off")
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
          hover-off)]]))