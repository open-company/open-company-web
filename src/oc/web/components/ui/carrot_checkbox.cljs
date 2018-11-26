(ns oc.web.components.ui.carrot-checkbox
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc carrot-checkbox < rum/static
  [{:keys [selected disabled did-change-cb selected-label unselected-label]}]
  [:div.carrot-checkbox
    {:on-click #(did-change-cb (not selected))
     :class (utils/class-set {:selected selected
                              :disabled disabled})}
    (when (and selected
               selected-label)
      [:span.selected-label
        selected-label])
    [:span.dot]
    (when (and (not selected)
               unselected-label)
      [:span.unselected-label
        unselected-label])])