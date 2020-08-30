(ns oc.web.components.ui.follow-button
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]))

(rum/defc follow-button ;< rum/static
  [{following :following
    resource-type :resource-type
    resource-uuid :resource-uuid
    button-copy :button-copy
    disabled :disabled}]
  (let [{:keys [active-on active-off hover-on hover-off]}
         (if (map? button-copy)
           button-copy
           {:active-on "Subscribed"
            :active-off "Subscribe"
            :hover-on "Unsubscribe"
            :hover-off "Subscribe"})]
    [:button.mlb-reset.follow-button
      {:class (utils/class-set {:on following
                                :off (not following)
                                :default-copy (not (map? button-copy))
                                :disabled disabled})
       :on-click #(when-not disabled
                    (if (= resource-type :board)
                      (user-actions/toggle-board resource-uuid)
                      (user-actions/toggle-publisher resource-uuid)))}
      [:span.main-title
        (if following
          active-on
          active-off)]
      [:span.hover-title
        (if following
          hover-on
          hover-off)]]))


(rum/defc follow-banner
  [board-data]
  (let []
    [:div.follow-banner
      [:span.follow-banner-copy (str "Previewing " (:name board-data))]
      (follow-button {:resource-type :board
                      :following (:following board-data)
                      :resource-uuid (:uuid board-data)})]))