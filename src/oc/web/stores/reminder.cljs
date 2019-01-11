(ns oc.web.stores.reminder
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.reminder :as reminder-utils]))

(defmethod dispatcher/action :edit-reminder
  [db [_ org-slug reminder-uuid]]
  (let [reminders-data (dispatcher/reminders-data org-slug db)
        reminder-data (if reminder-uuid
                        (first (filter #(= (:uuid %) reminder-uuid) reminders-data))
                        (reminder-utils/new-reminder-data))]
    (js/console.log "DBG :edit-reminder" reminder-data)
    (assoc-in db (dispatcher/reminder-edit-key org-slug)
     (or reminder-data (reminder-utils/new-reminder-data)))))

(defmethod dispatcher/action :reminders-loaded
  [db [_ org-slug reminders-data]]
  (assoc-in db (dispatcher/reminders-data-key org-slug) reminders-data))


(defmethod dispatcher/action :update-reminder
  [db [_ org-slug reminder-uuid k v]]
  (let [reminder-edit-key (dispatcher/reminder-edit-key org-slug)
        reminder-data (get-in db reminder-edit-key)
        updated-reminder-data (assoc reminder-data k v)]
    (assoc-in db reminder-edit-key updated-reminder-data)))

(defmethod dispatcher/action :save-reminder
  [db [_ org-slug]]
  (let [reminder-data (dispatcher/reminder-edit-data org-slug db)
        fixed-reminder-data (if (:uuid reminder-data)
                              reminder-data
                              (assoc reminder-data :uuid (utils/activity-uuid)))
        old-reminders-data (dispatcher/reminders-data org-slug db)
        filtered-reminders (filterv #(not= (:uuid reminder-data) (:uuid %)) old-reminders-data)
        new-reminders-data (conj filtered-reminders fixed-reminder-data)]
    (assoc-in db (dispatcher/reminders-data-key org-slug) new-reminders-data)))

(defmethod dispatcher/action :cancel-edit-reminder
  [db [_ org-slug]]
  (assoc-in db (dispatcher/reminder-edit-key org-slug) nil))

(defmethod dispatcher/action :delete-reminder
  [db [_ org-slug reminder-uuid]]
  (let [reminders-key (dispatcher/reminders-data-key org-slug)
        old-reminders-data (get-in db reminders-key)
        filtered-reminders (filterv #(not= (:uuid %) reminder-uuid) old-reminders-data)]
    (-> db
      (assoc-in reminders-key filtered-reminders)
      (assoc-in (dispatcher/reminder-edit-key org-slug) nil))))