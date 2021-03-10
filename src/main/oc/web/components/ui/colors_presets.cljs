(ns oc.web.components.ui.colors-presets
  (:require [cuerdas.core :as string]
            [rum.core :as rum]))

(rum/defc colors-presets <
  rum/static
  [{:keys [color-list current-selected on-change-cb]}]
  (js/console.log "DBG colors-presets, selected:" current-selected)
  [:div.colors-presets.group
   [:span.colors-presets-label "Presets:"]
   [:div.colors-list.group
    (for [preset color-list
          :let [lower-hex (comp string/lower :hex)
                active? (= (string/lower current-selected) (lower-hex preset))]]
      [:button.mlb-reset.color-preset-bt
        {:key (str "color-preset-" (:hex preset))
        :on-click #(on-change-cb preset)
        :class (when active? "active")}
        [:span.dot
        {:data-color-hex (:hex preset)
          :data-color-rgb (str (-> preset :rgb :r) " " (-> preset :rgb :g) " " (-> preset :rgb :b))
          :style {:background-color (:hex preset)}}]])]])