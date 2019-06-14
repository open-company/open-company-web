(ns oc.web.stores.reminder
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.reminder :as reminder-utils]))

(defmethod dispatcher/action :edit-reminder
  [db [_ org-slug reminder-uuid]]
  (let [reminders-data (dispatcher/reminders-data org-slug db)
        reminder-edit-key (dispatcher/reminder-edit-key org-slug)
        old-edit-reminder-data (get-in db reminder-edit-key)
        new-reminder-data (reminder-utils/parse-reminder (reminder-utils/new-reminder-data))
        reminder-data (or
                        ;; If the edit reminder is already populated it means a post/patch
                        ;; request failed, this avoid losing the edited data
                        old-edit-reminder-data
                        (if reminder-uuid
                          ;; In case of edit initialize the edit with the reminder data
                          (first (filter #(= (:uuid %) reminder-uuid) (:items reminders-data)))
                          ;; Else use an default reminder data
                          new-reminder-data))]
    (assoc-in db reminder-edit-key reminder-data)))

(defmethod dispatcher/action :reminders-loaded
  [db [_ org-slug reminders-data]]
  (let [sorted-reminders (reminder-utils/sort-reminders (:items reminders-data))]
    (assoc-in db (dispatcher/reminders-data-key org-slug) (assoc reminders-data :items sorted-reminders))))

(defmethod dispatcher/action :reminders-roster-loaded
  [db [_ org-slug roster-data]]
  (let [parsed-roster (reminder-utils/parse-reminders-roster roster-data)]
    (assoc-in db (dispatcher/reminders-roster-key org-slug) parsed-roster)))

(defmethod dispatcher/action :update-reminder
  [db [_ org-slug reminder-uuid value-or-fn]]
  (let [reminder-edit-key (dispatcher/reminder-edit-key org-slug)
        old-reminder-edit-data (get-in db reminder-edit-key)]
    (if (fn? value-or-fn)
      (update-in db reminder-edit-key value-or-fn)
      (assoc-in db reminder-edit-key (merge old-reminder-edit-data value-or-fn)))))

(defmethod dispatcher/action :save-reminder/error
  [db [_ org-slug reminder-data]]
  (let [old-reminders-data (dispatcher/reminders-data org-slug db)
        filtered-reminders (filterv #(nil? (:uuid %)) (:items old-reminders-data))
        new-reminders-data (reminder-utils/sort-reminders filtered-reminders)
        reminders-list-key (conj (dispatcher/reminders-data-key org-slug) :items)]
    (-> db
      (assoc-in reminders-list-key new-reminders-data)
      (assoc-in (dispatcher/reminder-edit-key org-slug) reminder-data))))

(defmethod dispatcher/action :save-reminder
  [db [_ org-slug reminder-data]]
  (let [fixed-reminder-data (if (:uuid reminder-data)
                              reminder-data
                              (assoc reminder-data :uuid (utils/activity-uuid)))
        old-reminders-data (dispatcher/reminders-data org-slug db)
        filtered-reminders (filterv #(not= (:uuid reminder-data) (:uuid %)) (:items old-reminders-data))
        new-reminders-data (reminder-utils/sort-reminders (conj filtered-reminders fixed-reminder-data))
        reminders-list-key (conj (dispatcher/reminders-data-key org-slug) :items)]
    (-> db
      (assoc-in reminders-list-key new-reminders-data)
      (assoc-in (dispatcher/reminder-edit-key org-slug) nil))))

(defmethod dispatcher/action :cancel-edit-reminder
  [db [_ org-slug]]
  (assoc-in db (dispatcher/reminder-edit-key org-slug) nil))

(defmethod dispatcher/action :delete-reminder
  [db [_ org-slug reminder-uuid]]
  (let [reminders-key (dispatcher/reminders-data-key org-slug)
        old-reminders-data (get-in db reminders-key)
        reminders-items-key (conj reminders-key :items)
        filtered-reminders (reminder-utils/sort-reminders (filterv #(not= (:uuid %) reminder-uuid) (:items old-reminders-data)))]
    (-> db
      (assoc-in reminders-items-key filtered-reminders)
      (assoc-in (dispatcher/reminder-edit-key org-slug) nil))))