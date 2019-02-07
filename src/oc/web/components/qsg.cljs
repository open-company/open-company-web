(ns oc.web.components.qsg
  (:require [rum.core :as rum]))

(rum/defcs qsg
  [s]
  [:div.qsg-container
    [:button.mlb-reset.add-profile-bt
      {:on-click #()}
      "Add a profile photo"]])

