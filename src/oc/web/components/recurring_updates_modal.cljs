(ns oc.web.components.recurring-updates-modal
  (:require [rum.core :as rum]
            [clojure.string :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.reminder :as reminder-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn show-modal [& [panel]]
  (dis/dispatch! [:input [:show-reminders] (or panel :reminders)]))

(defn real-close []
  (dis/dispatch! [:input [:show-reminders] nil]))

(defn dismiss-modal [& [s]]
  (if s
    (reset! (::unmounting s) true)
    (real-close)))

(rum/defcs recurring-updates-modal <
  rum/reactive
  (drv/drv :reminders-data)
  ;; Mixins
  mixins/no-scroll-mixin
  mixins/first-render-mixin
  ;; Locals
  (rum/local false ::unmounting)
  (rum/local false ::unmounted)
  {:did-mount (fn [s]
    (reminder-actions/load-reminders-roster)
    (reminder-actions/load-reminders)
    s)}
  [s]
  (let [appear-class (and @(:first-render-done s)
                          (not @(::unmounting s))
                          (not @(::unmounted s)))
        reminders-data (drv/react s :reminders-data)
        reminders-list (:items reminders-data)
        is-tablet-or-mobile? (responsive/is-tablet-or-mobile?)]
    [:div.recurring-updates-modal-container
      {:class (utils/class-set {:appear appear-class})}
      [:button.mlb-reset.modal-close-bt
        {:on-click #(dismiss-modal s)}]
      [:div.recurring-updates-modal
        [:div.recurring-updates-modal-header
          [:div.recurring-updates-modal-header-title
            "Recurring updates"]
          [:button.mlb-reset.new-recurring-update-bt
            {:on-click #(show-modal :new)}
            "New"]]
        [:div.recurring-updates-list
          (for [reminder reminders-list
                :let [patch-link (utils/link-for (:links reminder) "partial-update")
                      now-year (.getFullYear (utils/js-date))
                      next-send-date (utils/js-date (:next-send reminder))
                      show-year (not= now-year (.getFullYear next-send-date))]]
            [:div.recurring-updates-list-item.group
              {:key (str "reminder-" (:uuid reminder))
               :class (when patch-link "editable-reminder")
               :on-click #(when patch-link
                            (reminder-actions/edit-reminder (:uuid reminder)))}
              [:div.reminder-assignee
                {:title (utils/name-or-email (:assignee reminder))
                 :data-toggle (when-not is-tablet-or-mobile? "tooltip")
                 :data-placement "top"
                 :data-container "body"
                 :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}
                (user-avatar-image (:assignee reminder))]
              [:div.reminder-data
                [:div.reminder-title
                  (:headline reminder)]
                [:div.reminder-description
                  (str
                   (utils/name-or-email (:assignee reminder))
                   ", "
                   (utils/get-week-day (.getDay next-send-date) true)
                   " "
                   (utils/date-string next-send-date [:short-month (when show-year :year)])
                   " (" (name (:frequency reminder)) ")")]]])]]]))