(ns oc.web.components.ui.role-explainer-popover
  "Modal dialog to explain the 2/3 access control roles available per user."
  (:require [rum.core :as rum]))

(rum/defc role-explainer-popover < rum/static
  [{:keys [hide-popover-cb hide-admin] :as data}]
  [:div.oc-popover-container-internal
    {:on-click #(hide-popover-cb)}
    [:button.close-button]
    [:div.role-explainer-popover.oc-popover
      {:style {:width (str (:width data) "px") :height (str (:height data) "px")}}
      [:div.role-explainer-row
        [:span.title [:i.fa.fa-user] " View"] "- read only access"]
      (if hide-admin
         ; board settings
        [:div.role-explainer-row
          [:span.title [:i.fa.fa-pencil] " Edit"] "- add, edit and delete entries, add and archive topics, archive the board"]
         ; not board settings
        [:div.role-explainer-row
          [:span.title [:i.fa.fa-pencil] " Edit"] "- add, edit and delete entries, add and archive boards and topics, share company updates"])
      (when-not (true? hide-admin) ; not board settings
        [:div.role-explainer-row
          [:span.title [:i.fa.fa-gear] " Admin"] "- company settings, payments, manage team members"])
      [:div.center
        [:button.center.btn-reset.btn-solid.got-it-btn
          {:on-click #(hide-popover-cb)}
          "GOT IT"]]]])