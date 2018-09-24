(ns oc.web.components.ui.carrot-checkbox
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc carrot-checkbox < rum/static
  [{:keys [selected disabled did-change-cb]}]
  [:div.carrot-checkbox
    {:on-click #(did-change-cb (not selected))
     :class (utils/class-set {:selected selected
                              :disabeld disabled})}
    [:span.dot]])