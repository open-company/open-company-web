(ns oc.web.components.edit-recurring-update-modal
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.user :as user-lib]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.reminder :as reminder-utils]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.reminder :as reminder-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.small-loading :refer (small-loading)]))

;; New/Edit reminder

(defn cancel-clicked [reminder-data dismiss-action]
  (if (:has-changes reminder-data)
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "reminders-unsaved-edits"
                      :message "Leave without saving your changes?"
                      :link-button-title "Stay"
                      :link-button-cb #(alert-modal/hide-alert)
                      :solid-button-style :red
                      :solid-button-title "Lose changes"
                      :solid-button-cb #(do
                                          (alert-modal/hide-alert)
                                          (dismiss-action))}]
      (alert-modal/show-alert alert-data))
    (dismiss-action)))

(defn- update-reminder [s v]
  (let [reminder-data (first (:rum/args s))]
    (reminder-actions/update-reminder (:uuid reminder-data) (merge v {:has-changes true}))))

(defn- delete-reminder-clicked [s]
  (let [reminder-data @(drv/get-ref s :reminder-edit)
        alert-data {:icon "/img/ML/trash.svg"
                      :action "reminder-delete"
                      :message "Delete this reminder?"
                      :link-button-title "No"
                      :link-button-cb #(alert-modal/hide-alert)
                      :solid-button-style :red
                      :solid-button-title "Yes"
                      :solid-button-cb #(do
                                          (alert-modal/hide-alert)
                                          (reminder-actions/delete-reminder (:uuid reminder-data)))}]
    (alert-modal/show-alert alert-data)))

(rum/defcs edit-recurring-update-modal <
  rum/reactive
  (drv/drv :org-data)
  (drv/drv :reminder-edit)
  (drv/drv :reminders-roster)
  ;; Locals
  (rum/local false ::assignee-dropdown)
  (rum/local false ::frequency-dropdown)
  (rum/local false ::on-dropdown)
  ;; Mixins
  (mixins/on-window-click-mixin (fn [s e]
    (when (and (not (utils/event-inside? e (rum/ref-node s :frequency-dd-node)))
               (not (utils/event-inside? e (rum/ref-node s :frequency-bt)))
               (not (utils/event-inside? e (rum/ref-node s :on-dd-node)))
               (not (utils/event-inside? e (rum/ref-node s :on-bt))))
      (reset! (::frequency-dropdown s) false)
      (reset! (::on-dropdown s) false)
      (reset! (::assignee-dropdown s) false))))
  [s]
  (let [reminder-data (drv/react s :reminder-edit)
        reminders-roster (drv/react s :reminders-roster)
        users-list (:users-list reminders-roster)
        ;; on label and value stuff
        on-label (case (:frequency reminder-data)
                  (:weekly :biweekly) "On"
                  "On the")
        occurrence-field-name (get reminder-utils/occurrence-fields (:frequency reminder-data))
        possible-values (get reminder-utils/occurrence-values (:frequency reminder-data))
        values (map (fn [[k v]] (hash-map :value k :label v)) possible-values)
        occurrence-field (:occurrence-label reminder-data)
        occurrence-field-value (get reminder-data occurrence-field)
        occurrence-label-value (:occurrence-value reminder-data)
        self-assignee? (= (jwt/user-id) (:user-id (:assignee reminder-data)))]
    [:div.edit-recurring-update-modal-container
      [:button.mlb-reset.modal-close-bt
        {:on-click (fn [_] (cancel-clicked reminder-data nav-actions/close-all-panels))}]
      [:div.edit-recurring-update-modal
        [:div.edit-recurring-update-modal-header
          [:div.edit-recurring-update-modal-header-title
            "Recurring updates"]
          (let [save-disabled? (or (s/blank? (:headline reminder-data))
                                   (empty? (:assignee reminder-data))
                                   (not (:frequency reminder-data))
                                   (not (:occurrence-label reminder-data))
                                   (not (get reminder-data (:occurrence-label reminder-data))))]
            [:button.mlb-reset.save-bt
              {:on-click #(when-not save-disabled?
                            (reminder-actions/save-reminder reminder-data)
                            (nav-actions/close-reminders))
               :class (when save-disabled? "disabled")}
              "Save"])
          [:button.mlb-reset.cancel-bt
            {:on-click (fn [_]
                        (cancel-clicked reminder-data #(reminder-actions/cancel-edit-reminder)))}
            "Back"]]
        [:div.edit-recurring-update-body
          [:div.field-label "Assign to"]
          (if (empty? users-list)
            [:div.loading-users (small-loading)]
            [:div
              {:class (when (empty? users-list) "loading-users")}
              [:div.field-value.dropdown-field-value.oc-input
                {:ref :assignee-bt
                 :on-click #(when-not (empty? users-list)
                              (swap! (::assignee-dropdown s) not)
                              (reset! (::frequency-dropdown s) false)
                              (reset! (::on-dropdown s) false))
                 :class (utils/class-set {:placeholder (empty? (:assignee reminder-data))
                                          :active @(::assignee-dropdown s)})}
                (if (:assignee reminder-data)
                  (str (user-lib/name-for (:assignee reminder-data)) (when self-assignee? " (you)"))
                  "Pick a user")]
              (when (empty? users-list)
                (small-loading))
              (when @(::assignee-dropdown s)
                [:div.dropdown-container.users-list
                  {:ref :assignee-dd-node}
                  (dropdown-list {:items users-list
                                  :value (or (:user-id (:assignee reminder-data)) (:value (first users-list)))
                                  :on-change (fn [item]
                                               (let [selected-user (first (filter #(= (:user-id %) (:value item)) (:items reminders-roster)))]
                                                 (update-reminder s {:assignee selected-user}))
                                               (reset! (::assignee-dropdown s) false))})])])
          [:div.field-label "To update the team about"]
          [:input.field-value.oc-input
            {:value (or (:headline reminder-data) "")
             :ref :reminder-title
             :type "text"
             :max-length 65
             :placeholder "CEO update, Week in review, etc."
             :on-change #(update-reminder s {:headline (s/trim (.-value (rum/ref-node s :reminder-title)))})}]
          [:div.field-label "Every"]
          (let [frequency-value (get reminder-utils/frequency-values (:frequency reminder-data))]
            [:div
              [:div.field-value.dropdown-field-value.oc-input
                {:ref :frequency-bt
                 :on-click #(do
                              (reset! (::assignee-dropdown s) false)
                              (swap! (::frequency-dropdown s) not)
                              (reset! (::on-dropdown s) false))
                 :class (utils/class-set {:placeholder (empty? frequency-value)
                                          :active @(::frequency-dropdown s)})}
                (or frequency-value "Pick a frequency")]
              (when @(::frequency-dropdown s)
                [:div.dropdown-container
                  {:ref :frequency-dd-node}
                  (dropdown-list {:items [{:value :weekly :label (:weekly reminder-utils/frequency-values) :occurrence-value :friday}
                                          {:value :biweekly :label (:biweekly reminder-utils/frequency-values) :occurrence-value :friday}
                                          {:value :monthly :label (:monthly reminder-utils/frequency-values) :occurrence-value :first}
                                          {:value :quarterly :label (:quarterly reminder-utils/frequency-values) :occurrence-value :first}]
                                  :value (:frequency reminder-data)
                                  :on-change (fn [item]
                                               (let [old-freq (:frequency reminder-data)
                                                     new-freq (:value item)
                                                     with-freq {:frequency new-freq}
                                                     occurrence-field-name (get reminder-utils/occurrence-fields (:value item))
                                                     should-update-occurrence (and (not= new-freq old-freq)
                                                                                   (or (not (#{:weekly :biweekly} new-freq))
                                                                                       (not (#{:weekly :biweekly} old-freq))))
                                                     occurrence-value (when should-update-occurrence
                                                                       (get-in reminder-utils/occurrence-values [(:value item) (:occurrence-value item)]))
                                                     with-occurrence (if should-update-occurrence
                                                                      (merge with-freq {:occurrence-label occurrence-field-name
                                                                                        occurrence-field-name (:occurrence-value item)})
                                                                      with-freq)
                                                     with-occurrence-value (if should-update-occurrence
                                                                            (assoc with-occurrence :occurrence-value occurrence-value)
                                                                            with-occurrence)]
                                                 (update-reminder s with-occurrence-value))
                                               (reset! (::frequency-dropdown s) false))})])])
          [:div.field-label on-label]
          [:div.field-value.dropdown-field-value.oc-input
            {:ref :on-bt
             :on-click #(do
                          (reset! (::assignee-dropdown s) false)
                          (reset! (::frequency-dropdown s) false)
                          (swap! (::on-dropdown s) not))
             :class (when @(::on-dropdown s) "active")}
            occurrence-label-value]
          (when @(::on-dropdown s)
            [:div.dropdown-container
              {:ref :on-dd-node}
              (dropdown-list {:items values
                              :value occurrence-field-value
                              :on-change (fn [item]
                                           (update-reminder s {occurrence-field-name (:value item)
                                                               :occurrence-value (get-in reminder-utils/occurrence-values [(:frequency reminder-data) (:value item)])})
                                           (reset! (::on-dropdown s) false))})])
          (when (utils/link-for (:links reminder-data) "delete")
            [:div.edit-recurring-update-footer.group
              [:button.mlb-reset.delete-bt
                {:on-click #(delete-reminder-clicked s)}
                "Delete recurring update"]])]]]))