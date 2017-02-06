(ns oc.web.components.ui.team-disclaimer-popover
  (:require [rum.core :as rum]))

(rum/defc team-disclaimer-popover < rum/static
  [{:keys [hide-popover-cb] :as data}]
  [:div.oc-popover-container-internal
    {:on-click #(hide-popover-cb)}
    [:button.close-button]
    [:div.team-disclaimer-popover.oc-popover
      [:div.team-disclaimer-row
        [:span.title [:i.fa.fa-user] " Viewer"] "- read only access to the dashboard"]
      [:div.team-disclaimer-row
        [:span.title [:i.fa.fa-pencil] " Author"] "- edit entries, add new entries, add and archive topics, share company updates"]
      [:div.team-disclaimer-row
        [:span.title [:i.fa.fa-gear] " Admin"] "- company settings, payments, manage team members"]
      [:div.center
        [:button.center.btn-reset.btn-solid.got-it-btn
          {:on-click #(hide-popover-cb)}
          "GOT IT"]]]])