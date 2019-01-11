(ns oc.web.actions.reminder
  (:require [oc.web.api :as api]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.reminder :as reminder-utils]
            [oc.web.actions.nav-sidebar :as nav-actions]))

(defn load-reminders []
  (let [org-data (dis/org-data)]
    (let [reminders-link (utils/link-for (:links org-data) "reminders")]
      (api/get-reminders reminders-link
       #(let [reminders-data (reminder-utils/parse-reminders (reminder-utils/reminders-sample-list))]
          (dis/dispatch! [:reminders-loaded (router/current-org-slug) reminders-data]))))))

(defn edit-reminder [reminder-uuid]
  (dis/dispatch! [:edit-reminder (router/current-org-slug) reminder-uuid])
  (dis/dispatch! [:input [:show-reminders] reminder-uuid]))

(defn new-reminder []
  (dis/dispatch! [:edit-reminder (router/current-org-slug)])
  (dis/dispatch! [:input [:show-reminders] :new]))

(defn update-reminder [reminder-uuid k v]
  (dis/dispatch! [:update-reminder (router/current-org-slug) reminder-uuid k v]))

(defn save-reminder []
  (dis/dispatch! [:save-reminder (router/current-org-slug)]))

(defn cancel-edit-reminder []
  (dis/dispatch! [:cancel-edit-reminder (router/current-org-slug)])
  (nav-actions/show-reminders))

(defn delete-reminder [reminder-uuid]
  (dis/dispatch! [:delete-reminder (router/current-org-slug) reminder-uuid])
  (nav-actions/show-reminders))