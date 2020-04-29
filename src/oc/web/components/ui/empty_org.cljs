(ns oc.web.components.ui.empty-org
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.nav-sidebar :as nav-actions]))

(rum/defcs empty-org < rum/reactive
                       (drv/drv :org-data)
  [s]
  (let [org-data (drv/react s :org-data)]
    [:div.empty-org.group
      (if (:read-only org-data)
        [:div.empty-org-headline
          (str "There aren't teams in " (:name org-data) " yet. ")]
        [:div.empty-org-headline
          (str "You don’t have any teams yet. ")
          [:button.mlb-reset
            {:on-click #(nav-actions/show-section-add)}
            "Add one?"]])
      [:div.empty-org-image]]))