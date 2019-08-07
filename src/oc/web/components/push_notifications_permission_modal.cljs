(ns oc.web.components.push-notifications-permission-modal
  (:require [rum.core :as rum]
            [oc.web.actions.user :as user-actions]
            [oc.web.expo :as expo]))

(rum/defc push-notifications-permission-modal
  [{:keys [org-data] :as props}]
  [:div.push-notifications-permission-modal
   [:div.modal-header
    [:button.modal-close-bt
     {:on-click #(user-actions/deny-push-notification-permission)}]
    [:div.modal-title "Notifications"]]
   [:div.modal-body
    [:div.carrot-icon
     [:div.notification-bubble "3"]]
    [:p.modal-body-text
     (str "Ensure you never miss out on important communications from "
          (:name org-data)
          ".")]
    [:button.mlb-reset.enable-notifications-bt
     {:on-click #(expo/bridge-request-push-notification-permission!)}
     "Enable notifications"]
    [:button.mlb-reset.no-thanks-btn
     {:on-click #(user-actions/deny-push-notification-permission)}
     "No thanks"]]])
