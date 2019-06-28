(ns oc.web.components.ui.carrot-checkbox
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc carrot-checkbox < rum/static
                            {:did-mount (fn [s]
                              (.tooltip (js/$ "[data-toggle=\"tooltip\"]" (rum/dom-node s)))
                             s)
                             :did-remount (fn [_ s]
                              (.each (js/$ "[data-toggle=\"tooltip\"]" (rum/dom-node s))
                               #(doto (js/$ %2)
                                  (.tooltip "fixTitle")
                                  (.tooltip "hide")))
                              s)}
  [{:keys [selected disabled did-change-cb tooltip tooltip-placement]}]
  [:div.carrot-checkbox
    {:on-click #(when-not disabled
                  (did-change-cb (not selected)))
     :data-toggle (if tooltip "tooltip" "")
     :data-placement (if tooltip (or tooltip-placement "top") "")
     :data-container "body"
     :title tooltip
     :class (utils/class-set {:selected selected
                              :disabled disabled})}])