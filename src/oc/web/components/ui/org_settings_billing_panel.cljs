(ns oc.web.components.ui.org-settings-billing-panel
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            
            [goog.object :as gobj]
            [goog.dom :as gdom]))

(rum/defcs org-settings-billing-panel
  < rum/reactive
  [s org-data dismiss-settings-cb]
  (let []
    [:div.org-settings-panel
      ;; Panel rows
      [:div.org-settings-billing]
      ;; Save and cancel buttons
      [:div.org-settings-footer.group]]))