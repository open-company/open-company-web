(ns oc.web.components.reminders
  (:require [rum.core :as rum]
            [clojure.set :refer (rename-keys)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.reminder :as reminder-utils]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.reminder :as reminder-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.mixins.ui :refer (no-scroll-mixin on-window-click-mixin)]))

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
  (let [reminder-data (first (:rum/args s))
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

(rum/defcs edit-reminder < rum/reactive
                           (drv/drv :org-data)
                           (drv/drv :reminders-roster)
                           (rum/local false ::assignee-dropdown)
                           (rum/local false ::frequency-dropdown)
                           (rum/local false ::on-dropdown)
                           (on-window-click-mixin (fn [s e]
                              (when (and (not (utils/event-inside? e (rum/ref-node s :assignee-dd-node)))
                                         (not (utils/event-inside? e (rum/ref-node s :assignee-bt)))
                                         (not (utils/event-inside? e (rum/ref-node s :frequency-dd-node)))
                                         (not (utils/event-inside? e (rum/ref-node s :frequency-bt)))
                                         (not (utils/event-inside? e (rum/ref-node s :on-dd-node)))
                                         (not (utils/event-inside? e (rum/ref-node s :on-bt))))
                                (reset! (::assignee-dropdown s) false)
                                (reset! (::frequency-dropdown s) false)
                                (reset! (::on-dropdown s) false))))
  [s reminder-data]
  (let [org-data (drv/react s :org-data)
        reminders-roster (drv/react s :reminders-roster)
        fixed-roster (map #(assoc % :status (not= (:status %) "active")) (:items reminders-roster))
        users-list (vec (map #(-> fixed-roster
                          (assoc :name (utils/name-or-email %))
                          (select-keys [:name :user-id :status])
                          (rename-keys {:name :label :user-id :value :status :disabled})
                          (assoc :user-map %))
                    fixed-roster))]
    [:div.reminders-tab.edit-reminder.group
      {:class (when-not (:uuid reminder-data) "new-reminder")}
      [:div.edit-reminder-row.group
        [:div.half-row-left
          [:div.edit-reminder-label
            "Reminder for"]
          [:div.edit-reminder-field-container.dropdown-field.users-list
            {:class (when (empty? users-list) "loading-users")}
            [:div.edit-reminder-field
              {:ref :assignee-bt
               :on-click #(when-not (empty? users-list)
                            (swap! (::assignee-dropdown s) not)
                            (reset! (::frequency-dropdown s) false)
                            (reset! (::on-dropdown s) false))}
              (when (:assignee reminder-data)
                (user-avatar-image (:assignee reminder-data)))
              (when (:assignee reminder-data)
                (str (utils/name-or-email (:assignee reminder-data))
                 (when (= (jwt/user-id) (:user-id (:assignee reminder-data)))
                   " (you)")))]
            (when (empty? users-list)
              (small-loading))
            (when @(::assignee-dropdown s)
              [:div
                {:ref :assignee-dd-node}
                (dropdown-list {:items users-list
                                :value (or (:user-id (:assignee reminder-data)) (:user-id (first users-list)))
                                :on-change (fn [item]
                                             (let [selected-user (first (filter #(= (:user-id %) (:value item)) (:items reminders-roster)))]
                                               (update-reminder s {:assignee selected-user}))
                                             (reset! (::assignee-dropdown s) false))})])]]
        [:div.edit-reminder-row
          [:div.half-row-right
            [:div.edit-reminder-label
              "To update the team about"]
            [:input.edit-reminder-field
              {:value (:headline reminder-data)
               :ref :reminder-title
               :type "text"
               :max-length 65
               :placeholder "CEO update, Week in review, etc."
               :on-change #(update-reminder s {:headline (.-value (rum/ref-node s :reminder-title))})}]]]]
      [:div.edit-reminder-row.group
        [:div.half-row-left
          [:div.edit-reminder-label
            "Every"]
          (let [frequency-value (get reminder-utils/frequency-values (:frequency reminder-data))]
            [:div.edit-reminder-field-container.dropdown-field
              [:div.edit-reminder-field
                {:ref :frequency-bt
                 :on-click #(do
                              (reset! (::assignee-dropdown s) false)
                              (swap! (::frequency-dropdown s) not)
                              (reset! (::on-dropdown s) false))}
                frequency-value]
              (when @(::frequency-dropdown s)
                [:div
                  {:ref :frequency-dd-node}
                  (dropdown-list {:items [{:value :weekly :label (:weekly reminder-utils/frequency-values) :occurrence-value :monday}
                                          {:value :biweekly :label (:biweekly reminder-utils/frequency-values) :occurrence-value :monday}
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
                                               (reset! (::frequency-dropdown s) false))})])])]
        (let [label (case (:frequency reminder-data)
                      (:weekly :biweekly) "On"
                      "On the")
              occurrence-field-name (get reminder-utils/occurrence-fields (:frequency reminder-data))
              possible-values (get reminder-utils/occurrence-values (:frequency reminder-data))
              values (map (fn [[k v]] (hash-map :value k :label v)) possible-values)
              occurrence-field (:occurrence-label reminder-data)
              occurrence-field-value (get reminder-data occurrence-field)
              occurrence-label-value (:occurrence-value reminder-data)]
          [:div.half-row-right
            [:div.edit-reminder-label
              label]
            [:div.edit-reminder-field-container.dropdown-field
              [:div.edit-reminder-field
                {:ref :on-bt
                 :on-click #(do
                              (reset! (::assignee-dropdown s) false)
                              (reset! (::frequency-dropdown s) false)
                              (swap! (::on-dropdown s) not))}
                occurrence-label-value]
              (when @(::on-dropdown s)
                [:div
                  {:ref :on-dd-node}
                  (dropdown-list {:items values
                                  :value occurrence-field-value
                                  :on-change (fn [item]
                                               (update-reminder s {occurrence-field-name (:value item)
                                                                   :occurrence-value (get-in reminder-utils/occurrence-values [(:frequency reminder-data) (:value item)])})
                                               (reset! (::on-dropdown s) false))})])]])]
      [:div.edit-reminder-footer
        (when (:uuid reminder-data)
          [:button.mlb-reset.delete-bt
            {:on-click #(delete-reminder-clicked s)}
            "Delete reminder"])
        (let [save-disabled? (or (clojure.string/blank? (:headline reminder-data))
                                 (empty? (:assignee reminder-data))
                                 (not (:frequency reminder-data))
                                 (not (:occurrence-label reminder-data))
                                 (not (get reminder-data (:occurrence-label reminder-data))))]
          [:button.mlb-reset.save-bt
            {:on-click #(when-not save-disabled?
                          (reminder-actions/save-reminder reminder-data)
                          (nav-actions/show-reminders))
             :class (when save-disabled? "disabled")}
            "Save reminder"])
        [:button.mlb-reset.cancel-bt
          {:on-click (fn [_]
                      (cancel-clicked reminder-data #(reminder-actions/cancel-edit-reminder)))}
          "Cancel"]]]))

;; Manage reminders component

(def empty-reminders
  [:div.empty-reminders
    [:div.empty-reminders-logo]
    [:div.empty-reminders-title
      "Update your team on time"]
    [:div.empty-reminders-description
      "Make it easy for team leaders to remember when it's time to update everyone."]
    [:button.mlb-reset.add-reminder-bt
      {:on-click #(reminder-actions/new-reminder)}
      "Create new reminder"]])

(rum/defcs manage-reminders < rum/reactive
                              (drv/drv :show-reminders-tooltip)
                              {:will-unmount (fn [s]
                               ;; Make sure the tooltip is shown only the first time the user access reminders
                               (nux-actions/dismiss-reminders-tooltip)
                               s)}
  [s reminders-data]
  (let [reminders-list (:items reminders-data)]
    [:div.reminders-tab.manage-reminders
      (if (empty? reminders-list)
        empty-reminders
        [:div.reminders-list-container
          (when (drv/react s :show-reminders-tooltip)
            [:div.reminder-tooltip
              [:button.mlb-reset.dismiss-reminder-tooltip
                {:on-click #(nux-actions/dismiss-reminders-tooltip)}]
              [:div.reminder-tooltip-title
                "Keep your team in sync"]
              [:div.reminder-tooltip-description
                "Carrot reminders you when it's time to update your team"]
              [:button.mlb-reset.got-it-reminder-tooltip
                {:on-click #(nux-actions/dismiss-reminders-tooltip)}
                "Ok, got it"]])
          (for [reminder reminders-list
                :let [patch-link (utils/link-for (:links reminder) "partial-update")
                      now-year (.getFullYear (utils/js-date))
                      next-send-date (utils/js-date (:next-send reminder))
                      show-year (not= now-year (.getFullYear next-send-date))]]
            [:div.reminder-row.group
              {:key (str "reminder-" (:uuid reminder))
               :class (when patch-link "editable-reminder")
               :on-click #(when patch-link
                            (reminder-actions/edit-reminder (:uuid reminder)))}
              [:div.reminder-row-inner.group
                [:div.reminder-assignee
                  {:title (utils/name-or-email (:assignee reminder))
                   :data-toggle (when-not (responsive/is-tablet-or-mobile?) "tooltip")
                   :data-placement "top"
                   :data-container "body"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}
                  (user-avatar-image (:assignee reminder))]
                [:div.reminder-title
                  (utils/name-or-email (:assignee reminder))
                  [:span.dot " · "]
                  (:headline reminder)]
                [:div.reminder-description
                  (:parsed-start-date reminder)
                  [:span.frequency
                    (str
                     (utils/get-week-day (.getDay next-send-date) true)
                     ", "
                     (utils/date-string next-send-date [:short-month (when show-year :year)])
                     " (" (name (:frequency reminder)) ")")]]]])])]))

;; Reminders main component

(defn- close-clicked [s]
  (nav-actions/close-reminders))

(rum/defcs reminders < ;; Mixins
                       no-scroll-mixin
                       rum/reactive
                       ;; Derivatives
                       (drv/drv :alert-modal)
                       (drv/drv :show-reminders)
                       (drv/drv :reminders-data)
                       (drv/drv :reminder-edit)
                       {:did-mount (fn [s]
                         (reminder-actions/load-reminders-roster)
                         (reminder-actions/load-reminders)
                         s)}
  [s]
  (let [reminder-tab (drv/react s :show-reminders)
        editing-reminder? (string? reminder-tab)
        alert-modal-data (drv/react s :alert-modal)
        reminders-data (drv/react s :reminders-data)
        reminder-edit-data (drv/react s :reminder-edit)]
    [:div.reminders-container.fullscreen-page
      [:div.reminders-inner
        {:class (utils/class-set {:no-bottom-padding (= reminder-tab :reminders)
                                  :loading (not reminders-data)})}
        (when-not alert-modal-data
          [:button.settings-modal-close.mlb-reset
            {:on-click (fn [_]
                         (cancel-clicked reminder-edit-data #(close-clicked s)))}])
        [:div.reminders-header
          [:div.reminders-header-title
            (if editing-reminder?
              "Edit reminder"
              "Reminders")]
          (when-not editing-reminder?
            [:div.reminders-header-tab-line
              [:div.reminders-header-tab
                {:on-click #(nav-actions/show-reminders)
                 :class (when (= reminder-tab :reminders) "active")}
                "MANAGE"]
              (when reminders-data
                [:div.reminders-header-tab
                  {:on-click #(reminder-actions/new-reminder)
                   :class (when (= reminder-tab :new) "active")}
                  "NEW REMINDER"])])]
        (if reminders-data
          (cond
            (= reminder-tab :new)
            (edit-reminder reminder-edit-data)
            (string? reminder-tab)
            (edit-reminder reminder-edit-data)
            :else
            (manage-reminders reminders-data))
          (small-loading))]]))