(ns oc.web.components.ui.carrot-option-button
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc carrot-option-button < rum/static
  [{:keys [selected disabled did-change-cb]}]
  [:div.carrot-option-button
    {:on-click #(when-not disabled
                  (did-change-cb (not selected)))
     :class (utils/class-set {:selected selected
                              :disabled disabled})}])