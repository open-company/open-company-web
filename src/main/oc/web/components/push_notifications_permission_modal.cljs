(ns oc.web.components.push-notifications-permission-modal
  (:require [rum.core :as rum]
            [oc.web.actions.user :as user-actions]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.expo :as expo]))

(rum/defcs push-notifications-permission-modal < no-scroll-mixin
  [s]
  [:div.push-notifications-permission-modal
   [:div.modal-header
    [:button.modal-close-bt
     {:on-click #(user-actions/deny-push-notification-permission)}]
    [:div.modal-title "Notifications"]]
   [:div.modal-body
    [:div.carrot-icon
     [:div.notification-bubble "3"]]
    [:p.modal-body-text
     "Get notified when your team shares on Wut"]
    [:button.mlb-reset.enable-notifications-bt
     {:on-click #(expo/bridge-request-push-notification-permission!)}
     "Enable notifications"]
    [:button.mlb-reset.no-thanks-btn
     {:on-click #(user-actions/deny-push-notification-permission)}
     "No thanks, skip for now"]]])
