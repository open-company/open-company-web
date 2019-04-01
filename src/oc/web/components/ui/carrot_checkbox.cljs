(ns oc.web.components.ui.carrot-checkbox
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc carrot-checkbox < rum/static
  [{:keys [selected disabled did-change-cb]}]
  [:div.carrot-checkbox
    {:on-click #(when-not disabled
                  (did-change-cb (not selected)))
     :class (utils/class-set {:selected selected
                              :disabled disabled})}])