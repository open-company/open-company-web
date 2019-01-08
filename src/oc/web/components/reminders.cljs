(ns oc.web.components.reminders
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn- close-clicked [s]
  (nav-actions/close-reminders))

(def empty-reminders
  [:div.empty-reminders
    [:div.empty-reminders-logo]
    [:div.empty-reminders-title
      "Never forget an update again"]
    [:div.empty-reminders-description
      "Reminders come by email or Slack, and you can simply reply to them to update your team."]
    [:button.mlb-reset.add-reminder-bt
      {:on-click #(nav-actions/show-new-reminder)}
      "Create new reminder"]])

(rum/defcs manage-reminders
  [s reminders-data]
  [:div.reminders-tab.manage-reminders
    (if (empty? reminders-data)
      empty-reminders
      [:div.reminders-list-container
        (for [reminder reminders-data]
          [:div.reminder-row.group
            {:key (str "reminder-" (:uuid reminder))
             :class (when (utils/link-for (:links reminder) "update") "editable-reminder")
             :on-click #(nav-actions/edit-reminder (:uuid reminder))}
            (user-avatar-image (:assignee reminder))
            [:div.reminder-title
              (:title reminder)]
            [:div.reminder-description
              (:description reminder)]])])])

(rum/defcs reminders < ;; Mixins
                       no-scroll-mixin
                       rum/reactive
                       ;; Derivatives
                       (drv/drv :alert-modal)
                       (drv/drv :show-reminders)
                       (drv/drv :reminders-data)
  [s]
  (let [reminder-tab (drv/react s :show-reminders)
        editing-reminder? (string? reminder-tab)
        alert-modal-data (drv/react s :alert-modal)
        reminders-data (drv/react s :reminders-data)]
    [:div.reminders-container.fullscreen-page
      [:div.reminders-inner
        (when-not alert-modal-data
          [:button.settings-modal-close.mlb-reset
            {:on-click #(close-clicked s)}])
        [:div.reminders-header
          [:div.reminders-header-title
            (if editing-reminder?
              "Edit reminder"
              "Reminders")]
          [:div.reminders-header-tab-line
            (when-not editing-reminder?
              [:div.reminders-header-tab
                {:on-click #(nav-actions/show-reminders)
                 :class (when (= reminder-tab :reminders) "active")}
                "MANAGE"])
            (when-not editing-reminder?
              [:div.reminders-header-tab
                {:on-click #(nav-actions/show-new-reminder)
                 :class (when (= reminder-tab :new) "active")}
                "NEW REMINDER"])]]
        (cond
          (= reminder-tab :new)
          [:div]
          (string? reminder-tab)
          [:div]
          :else
          (manage-reminders reminders-data))]]))