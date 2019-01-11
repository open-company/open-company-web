(ns oc.web.components.reminders
  (:require [rum.core :as rum]
            [clojure.set :refer (rename-keys)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.reminder :as reminder-utils]
            [oc.web.mixins.ui :refer (no-scroll-mixin)]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.reminder :as reminder-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

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

(defn- update-reminder-value [s k v]
  (let [reminder-data (first (:rum/args s))]
    (reminder-actions/update-reminder (:uuid reminder-data) k v)
    (when (= k :board-data)
      (reminder-actions/update-reminder (:uuid reminder-data) :board-uuid (:uuid v)))
    (reminder-actions/update-reminder (:uuid reminder-data) :has-changes true)))

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
                           (drv/drv :team-data)
                           (rum/local false ::assignee-dropdown)
                           (rum/local false ::board-dropdown)
                           (rum/local false ::frequency-dropdown)
                           (rum/local false ::on-dropdown)
  [s reminder-data]
  (let [org-data (drv/react s :org-data)
        team-data (drv/react s :team-data)
        allowed-users (reminder-utils/users-for-reminders org-data team-data (:board-data reminder-data))
        users-list (vec (map #(-> %
                          (assoc :name (utils/name-or-email %))
                          (select-keys [:name :user-id])
                          (rename-keys {:name :label :user-id :value}))
                    allowed-users))
        all-team-public-boards (filterv #(or (= (:access %) "team") (= (:access %) "public")) (:boards org-data))
        private-boards-with-author-access (filterv #(and (= (:access %) "private") (utils/get-author (jwt/user-id) (:authors %))) (:boards org-data))
        all-allowed-boards (sort-by :name (concat all-team-public-boards private-boards-with-author-access))
        allowed-boards (map #(-> %
                              (select-keys [:name :uuid])
                              (rename-keys {:name :label :uuid :value}))
                        all-allowed-boards)]
    (js/console.log "DBG edit-reminder" reminder-data)
    [:div.reminders-tab.edit-reminder.group
      {:class (when-not (:uuid reminder-data) "new-reminder")}
      [:div.edit-reminder-row
        [:div.edit-reminder-label
          "To update the team about"]
        [:input.edit-reminder-field
          {:value (:title reminder-data)
           :ref :reminder-title
           :type "text"
           :max-length 65
           :placeholder "CEO update, Week in review, etc."
           :on-change #(update-reminder-value s :title (.-value (rum/ref-node s :reminder-title)))}]]
      ; [:div.edit-reminder-row
      ;   [:div.edit-reminder-label
      ;     "Personal note (optional)"]
      ;   [:textarea.edit-reminder-field
      ;     {:value (:description reminder-data)
      ;      :ref :remider-description
      ;      :max-length 256
      ;      :placeholder "Add a personal note to your reminder..."
      ;      :on-change #(update-reminder-value s :description (.-value (rum/ref-node s :remider-description)))}]]
      [:div.edit-reminder-row.group
        [:div.half-row-left
          [:div.edit-reminder-label
            "Reminder for"]
          [:div.edit-reminder-field-container.dropdown-field
            [:div.edit-reminder-field
              {:on-click #(reset! (::assignee-dropdown s) true)}
              (when (:assignee reminder-data)
                (user-avatar-image (:assignee reminder-data)))
              (when (:assignee reminder-data)
                (str (utils/name-or-email (:assignee reminder-data))
                 (when (= (jwt/user-id) (:user-id (:assignee reminder-data)))
                   " (you)")))]
            (when @(::assignee-dropdown s)
              (dropdown-list {:items users-list
                              :value (or (:user-id (:assignee reminder-data)) (:user-id (first users-list)))
                              :on-change (fn [item]
                                           (let [selected-user (first (filter #(= (:user-id %) (:value item)) allowed-users))]
                                             (update-reminder-value s :assignee selected-user))
                                           (reset! (::assignee-dropdown s) false))
                              :on-blur #(reset! (::assignee-dropdown s) false)}))]]
        [:div.half-row-right
          [:div.edit-reminder-label
            "Section"]
          [:div.edit-reminder-field-container.dropdown-field
            [:div.edit-reminder-field
              {:on-click #(reset! (::board-dropdown s) true)}
              (when (:board-data reminder-data)
                (:name (:board-data reminder-data)))]
            (when @(::board-dropdown s)
              (dropdown-list {:items allowed-boards
                              :value (or (:board-uuid reminder-data) (:value (first allowed-boards)))
                              :on-change (fn [item]
                                           (let [board-data (first (filter #(= (:uuid %) (:value item)) (:boards org-data)))]
                                             (update-reminder-value s :board-data board-data))
                                           (reset! (::board-dropdown s) false))
                              :on-blur #(reset! (::board-dropdown s) false)}))]]]
      [:div.edit-reminder-row.group
        [:div.half-row-left
          [:div.edit-reminder-label
            "Frequency"]
          [:div.edit-reminder-field-container.dropdown-field
            [:div.edit-reminder-field
              {:on-click #(reset! (::frequency-dropdown s) true)}
              (when (:frequency reminder-data)
                (:frequency reminder-data))]
            (when @(::frequency-dropdown s)
              (dropdown-list {:items [{:value "Weekly"}
                                      {:value "Every other week"}
                                      {:value "Monthly"}
                                      {:value "Quarterly"}]
                              :value (or (:frequency reminder-data) "Weekly")
                              :on-change (fn [item]
                                           (update-reminder-value s :frequency (:value item))
                                           (reset! (::frequency-dropdown s) false))
                              :on-blur #(reset! (::frequency-dropdown s) false)}))]]
        (let [label (case (:frequency reminder-data)
                      "Weekly" "Every"
                      "Every other week" "On"
                      "On the")
              values (case (:frequency reminder-data)
                      "Weekly"
                      [{:value "Monday"} {:value "Tuesday"} {:value "Wednesday"}
                       {:value "Thursday"} {:value "Friday"} {:value "Saturday"} {:value "Sunday"}]
                      "Every other week"
                      [{:value "Monday"} {:value "Tuesday"} {:value "Wednesday"}
                       {:value "Thursday"} {:value "Friday"} {:value "Saturday"} {:value "Sunday"}]
                      "Monthly"
                      [{:value "First day of the month"} {:value "Last day of the month"}
                       {:value "First Monday of the month"} {:value "Last Friday of the month"}]
                      ;; else "Quarterly"
                      [{:value "First day of the quarter"} {:value "Last day of the quarter"}
                       {:value "First Monday of the quarter"} {:value "Last Friday of the quarter"}])
              current-value (if (utils/in? (map :value values) (:on reminder-data) )
                              (:on reminder-data)
                              (:value (first values)))]
          [:div.half-row-right
            [:div.edit-reminder-label
              label]
            [:div.edit-reminder-field-container.dropdown-field
              [:div.edit-reminder-field
                {:on-click #(reset! (::on-dropdown s) true)}
                current-value]
              (when @(::on-dropdown s)
                (dropdown-list {:items values
                                :value current-value
                                :on-change (fn [item]
                                             (update-reminder-value s :on (:value item))
                                             (reset! (::on-dropdown s) false))
                                :on-blur #(reset! (::on-dropdown s) false)}))]])]
      [:div.edit-reminder-footer
        (when (:uuid reminder-data)
          [:button.mlb-reset.delete-bt
            {:on-click #(delete-reminder-clicked s)}
            "Delete reminder"])
        (let [save-disabled? (or (clojure.string/blank? (:title reminder-data))
                                 (empty? (:assignee reminder-data))
                                 (empty? (:board-data reminder-data))
                                 (empty? (:frequency reminder-data))
                                 (empty? (:on reminder-data)))]
          [:button.mlb-reset.save-bt
            {:on-click #(when-not save-disabled?
                          (reminder-actions/save-reminder)
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
      "Never forget an update again"]
    [:div.empty-reminders-description
      "Reminders come by email or Slack, and you can simply reply to them to update your team."]
    [:button.mlb-reset.add-reminder-bt
      {:on-click #(nav-actions/show-new-reminder)}
      "Create new reminder"]])

(rum/defcs manage-reminders < rum/reactive
                              (drv/drv :show-reminders-tooltip)
  [s reminders-data]
  [:div.reminders-tab.manage-reminders
    (if (empty? reminders-data)
      empty-reminders
      [:div.reminders-list-container
        (when (drv/react s :show-reminders-tooltip)
          [:div.reminder-tooltip
            [:button.mlb-reset.dismiss-reminder-tooltip
              {:on-click #(nux-actions/dismiss-reminders-tooltip)}
              "x"]
            [:div.reminder-tooltip-title
              "Never forget an update again"]
            [:div.reminder-tooltip-description
              "Build trust and transparency by ensuring updates are shared on time."]
            [:button.mlb-reset.got-it-reminder-tooltip
              {:on-click #(nux-actions/dismiss-reminders-tooltip)}
              "Ok, got it"]])
        (for [reminder reminders-data]
          [:div.reminder-row.group
            {:key (str "reminder-" (:uuid reminder))
             :class (when (utils/link-for (:links reminder) "update") "editable-reminder")
             :on-click #(reminder-actions/edit-reminder (:uuid reminder))}
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
              (:title reminder)]
            [:div.reminder-description
              (:parsed-start-date reminder)
              [:span.frequency (str " (" (:frequency reminder) ")")]]])])])

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
                       {:init (fn [s]
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
              [:div.reminders-header-tab
                {:on-click #(reminder-actions/new-reminder)
                 :class (when (= reminder-tab :new) "active")}
                "NEW REMINDER"]])]
        (cond
          (= reminder-tab :new)
          (edit-reminder reminder-edit-data)
          (string? reminder-tab)
          (edit-reminder reminder-edit-data)
          :else
          (manage-reminders reminders-data))]]))