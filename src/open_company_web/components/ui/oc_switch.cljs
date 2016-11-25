(ns open-company-web.components.ui.oc-switch
  (:require [rum.core :as rum]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]))

(rum/defc oc-switch
  [active]
  [:div.oc-switch.group
    [:div.oc-switch-btn.dashboard
      {:on-click #(router/nav! (oc-urls/company))}
      [:button.btn-reset
        {:class (when (= active :dashboard) "active")}
        "Dashboard"]]
    [:div.oc-switch-btn.updates
      {:on-click #(router/nav! (oc-urls/stakeholder-update-list))}
      [:button.btn-reset
        {:class (when (= active :updates) "active")}
        "Updates"]]])
