(ns oc.web.actions.reminder
  (:require [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.utils.reminder :as reminder-utils]
            [oc.web.actions.nav-sidebar :as nav-actions]))

(defn- reminders-loaded [{:keys [success body status]}]
  (let [parsed-body (when success (json->cljs body))
        reminders-data (:collection parsed-body)
        parsed-reminders (reminder-utils/parse-reminders reminders-data)]
    (dis/dispatch! [:reminders-loaded (router/current-org-slug) parsed-reminders])))

(defn load-reminders []
  (let [org-data (dis/org-data)
        reminders-link (utils/link-for (:links org-data) "reminders")]
    (api/get-reminders reminders-link reminders-loaded)))

(defn edit-reminder [reminder-uuid]
  (dis/dispatch! [:edit-reminder (router/current-org-slug) reminder-uuid])
  (dis/dispatch! [:input [:show-reminders] reminder-uuid]))

(defn new-reminder []
  (dis/dispatch! [:edit-reminder (router/current-org-slug)])
  (dis/dispatch! [:input [:show-reminders] :new]))

(defn update-reminder [reminder-uuid value-or-fn]
  (dis/dispatch! [:update-reminder (router/current-org-slug) reminder-uuid value-or-fn]))

(defn save-reminder [reminder-data]
  (dis/dispatch! [:save-reminder (router/current-org-slug)])
  (let [reminders-data (dis/reminders-data)
        add-reminder-link (utils/link-for (:links reminders-data) "create")
        reminders-link (utils/link-for (:links reminders-data) "self")]
    (api/add-reminder add-reminder-link reminder-data
     (fn [{:keys [status success body]}]
       (api/get-reminders reminders-link reminders-loaded)))))

(defn cancel-edit-reminder []
  (dis/dispatch! [:cancel-edit-reminder (router/current-org-slug)])
  (nav-actions/show-reminders))

(defn delete-reminder [reminder-uuid]
  (let [reminders-data (dis/reminders-data)
        reminder-data (first (filter #(= (:uuid %) reminder-uuid) (:items reminders-data)))
        delete-reminder-link (utils/link-for (:links reminders-data) "delete")
        reminders-link (utils/link-for (:links reminders-data) "self")]
    (dis/dispatch! [:delete-reminder (router/current-org-slug) reminder-uuid])
    (nav-actions/show-reminders)
    (api/delete-reminder delete-reminder-link
      (fn [{:keys [status success body]}]
        (api/get-reminders reminders-link reminders-loaded)))))