(ns oc.web.components.qsg
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.actions.qsg :as qsg-actions]))

(rum/defcs qsg < rum/reactive
                 (drv/drv :qsg)
  [s]
  (let [qsg-data (drv/react s :qsg)]
    [:div.qsg-container
      [:button.mlb-reset.add-profile-bt
        {:on-click #(qsg-actions/start-profile-photo-trail)
         :class (when (:profile-photo-end qsg-data)
                  "done")}
        "Add a profile photo"]]))