(ns oc.web.components.theme-settings-modal
  (:require [rum.core :as rum]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.utils.theme :as theme-utils]
            [oc.web.actions.theme :as theme-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [oc.web.components.ui.carrot-option-button :refer (carrot-option-button)]))

(def automatically-chosen-copy
  [:div.theme-description
    "Chosen automatically by your OS"])

(rum/defcs theme-settings-modal <
  rum/static
  [s theme-data]
  (let [{:keys [setting-value]} theme-data
        computed-value (theme-utils/computed-value theme-data)]
    [:div.theme-settings-modal
      {:on-click #(when-not (dom-utils/event-inside? % (rum/ref-node s :theme-settings))
                    (nav-actions/close-all-panels))}
      [:button.mlb-reset.modal-close-bt
        {:on-click nav-actions/close-all-panels}]
      [:div.theme-settings
        {:ref :theme-settings}
        [:div.theme-settings-header
          [:div.theme-settings-header-title
            "Dark mode"]
          [:button.mlb-reset.cancel-bt
            {:on-click #(nav-actions/hide-theme-settings)}
            "Back"]]
        [:div.theme-settings-body
          (when (or (theme-utils/desktop-theme-supported?)
                    (theme-utils/web-theme-supported?)
                    (theme-utils/mobile-theme-supported?))
            [:div.theme-settings-auto-container
              (carrot-checkbox {:selected (= setting-value :auto)
                                :did-change-cb #(theme-actions/set-theme-setting (if % :auto computed-value))})
              [:span.auto-label
                {:on-click #(theme-actions/set-theme-setting (if (= setting-value :auto) computed-value :auto))}
                "Sync with OS settings"]])
          [:div.theme-settings-rows
            [:button.mlb-reset.theme-settings-row.light-theme
              {:class (when (= computed-value :light) "active")
               :on-click #(theme-actions/set-theme-setting :light)}
              (carrot-option-button {:selected (= setting-value :light)
                                     :disabled false
                                     :did-change-cb #(theme-actions/set-theme-setting :light)})
              [:span.theme-name "Light"]
              [:span.theme-icon]
              (when (and (= computed-value :light)
                         (= setting-value :auto))
                automatically-chosen-copy)]
            [:button.mlb-reset.theme-settings-row.dark-theme
              {:class (when (= computed-value :dark) "active")
               :on-click #(theme-actions/set-theme-setting :dark)}
              (carrot-option-button {:selected (= setting-value :dark)
                                     :disabled false
                                     :did-change-cb #(theme-actions/set-theme-setting :dark)})
              [:span.theme-name "Dark"]
              [:span.theme-icon]
              (when (and (= computed-value :dark)
                         (= setting-value :auto))
                automatically-chosen-copy)]]]]]))