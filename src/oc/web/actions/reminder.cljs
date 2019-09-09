(ns oc.web.actions.reminder
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.utils.reminder :as reminder-utils]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.notifications :as notification-actions]))

(defn load-reminders-roster
  "Load the roster of the users that can be assigned to reminders."
  []
  (let [reminders-data (dis/reminders-data)
        roster-link (utils/link-for (:links reminders-data) "roster")]
    (when roster-link
      (api/get-reminders-roster roster-link
       (fn [{:keys [success body status]}]
         (when success
           (dis/dispatch! [:reminders-roster-loaded (router/current-org-slug) (json->cljs body)])))))))

(defn- reminders-loaded
  "Reminders data loaded, parse and dispatch the content to the app-state."
  [{:keys [success body status]}]
  (when success
    (let [parsed-body (json->cljs body)
          reminders-data (:collection parsed-body)
          parsed-reminders (reminder-utils/parse-reminders reminders-data)]
      (dis/dispatch! [:reminders-loaded (router/current-org-slug) parsed-reminders])
      ;; Load the roster if it's not present yet
      (when-not (dis/reminders-roster-data)
        (load-reminders-roster)))))

(defn load-reminders
  "
  Load the reminders list.
  NB: first reminders is loaded in did-mount of dashboard-layout component."
  []
  (when-let* [org-data (dis/org-data)
              reminders-link (utils/link-for (:links org-data) "reminders")]
    (api/get-reminders reminders-link reminders-loaded)))

(defn edit-reminder
  "Move a reminder in the edit location of the app-state and open the edit component."
  [reminder-uuid]
  (dis/dispatch! [:edit-reminder (router/current-org-slug) reminder-uuid])
  ; (dis/dispatch! [:input [:show-reminders] reminder-uuid])
  (nav-actions/edit-reminder reminder-uuid))

(defn new-reminder
  "Move an empty reminder in the edit location of the app-state and open the edit component."
  []
  (dis/dispatch! [:edit-reminder (router/current-org-slug)])
  ; (dis/dispatch! [:input [:show-reminders] :new])
  (nav-actions/show-new-reminder))

(defn update-reminder
  "Update a reminder map."
  [reminder-uuid value-or-fn]
  (dis/dispatch! [:update-reminder (router/current-org-slug) reminder-uuid value-or-fn]))

(defn save-reminder
  "
  Save a reminder moving it into the local list and also by creating or updating it on the server.
  Refresh the list of reminders when finished.
  "
  [reminder-data]
  (dis/dispatch! [:save-reminder (router/current-org-slug) reminder-data])
  (let [reminders-data (dis/reminders-data)
        reminders-link (utils/link-for (:links reminders-data) "self")
        refresh-reminders #(api/get-reminders reminders-link reminders-loaded)]
    (if (:uuid reminder-data)
      (let [update-reminder-link (utils/link-for (:links reminder-data) "partial-update")]
        (api/update-reminder update-reminder-link reminder-data
         (fn [{:keys [status success body]}]
           (if success
             (notification-actions/show-notification {:title "Reminder updated"
                                                      :primary-bt-title "OK"
                                                      :primary-bt-dismiss true
                                                      :expire 3
                                                      :id :reminder-updated})
             (do
              (dis/dispatch! [:save-reminder/error (router/current-org-slug) reminder-data])
              (notification-actions/show-notification {:title "An error occurred"
                                                       :description "Please try again"
                                                       :primary-bt-title "OK"
                                                       :primary-bt-dismiss true
                                                       :expire 3
                                                       :id :reminder-update-failed})
              (nav-actions/edit-reminder (:uuid reminder-data))))
           (refresh-reminders))))
      (let [add-reminder-link (utils/link-for (:links reminders-data) "create")]
        (api/add-reminder add-reminder-link reminder-data
         (fn [{:keys [status success body]}]
           (if success
             (let [self-reminder (= (:user-id (:assignee reminder-data))
                                    (:user-id (dis/current-user-data)))]
               (notification-actions/show-notification {:title (if self-reminder
                                                                "Reminder created"
                                                                "Reminder created and teammate notified")
                                                        :primary-bt-title "OK"
                                                        :primary-bt-dismiss true
                                                        :expire 3
                                                        :id :reminder-create}))
             (do
              (dis/dispatch! [:save-reminder/error (router/current-org-slug) reminder-data])
              (notification-actions/show-notification {:title "An error occurred"
                                                       :description "Please try again"
                                                       :primary-bt-title "OK"
                                                       :primary-bt-dismiss true
                                                       :expire 3
                                                       :id :reminder-update-failed})
              (nav-actions/show-new-reminder)))
           (refresh-reminders)))))))

(defn cancel-edit-reminder
  "Exit edit losing changes."
  []
  (dis/dispatch! [:cancel-edit-reminder (router/current-org-slug)])
  (nav-actions/close-reminders))

(defn delete-reminder
  "Delete a reminder."
  [reminder-uuid]
  (let [reminders-data (dis/reminders-data)
        reminder-data (first (filter #(= (:uuid %) reminder-uuid) (:items reminders-data)))
        delete-reminder-link (utils/link-for (:links reminder-data) "delete")
        reminders-link (utils/link-for (:links reminders-data) "self")]
    (dis/dispatch! [:delete-reminder (router/current-org-slug) reminder-uuid])
    (nav-actions/show-reminders)
    (api/delete-reminder delete-reminder-link
      (fn [{:keys [status success body]}]
        (when success
          (notification-actions/show-notification {:title "Reminder deleted"
                                                   :primary-bt-title "OK"
                                                   :primary-bt-dismiss true
                                                   :expire 3
                                                   :id :reminder-deleted}))
        (api/get-reminders reminders-link reminders-loaded)))))