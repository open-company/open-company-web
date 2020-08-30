(ns oc.web.stores.notifications
  "Notifications store is a list of notification having the following form:
  {:title \"A string representing the title to show.\"
   :description \"A string representing the description to show.\"
   :server-error true // Only if representing a server error, mostly it changes color theme.
   :app-update true // Only if representing an app update error, mostly it changes color theme.
   :dismiss true // Show an X to remove the notification
   :opac true // Apply opacity to all the contained elements
   :expire 100 // Number of seconds after which the notification is removed, 0 is infinite.
   :id :unique-id // Used to avoid showing 2 times the same notification.
  }"
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.utils :as utils]))

(defn find-duplicate [n-data notifs]
  (when (:id n-data)
    (first (filter #(= (:id %) (:id n-data)) notifs))))

(defmethod dispatcher/action :notification-add
  [db [_ notification-data]]
  (let [current-notifications (get-in db dispatcher/notifications-key)
        dup (find-duplicate notification-data current-notifications)
        old-notifications (remove #(= (:id %) (:id notification-data)) current-notifications)
        fixed-notification-data (if dup
                                  (if (or (zero? (:expire notification-data)) (zero? (:expire dup)))
                                    (assoc notification-data :expire 0)
                                    (update-in notification-data [:expire] max (:expire dup)))
                                  notification-data)
        next-notifications (conj old-notifications fixed-notification-data)]
    (assoc-in db dispatcher/notifications-key next-notifications)))

(defmethod dispatcher/action :notification-remove
  [db [_ notification-data]]
  (let [current-notifications (get-in db dispatcher/notifications-key)
        next-notifications (filter #(not= (:created-at %) (:created-at notification-data)) current-notifications)]
    (assoc-in db dispatcher/notifications-key next-notifications)))

(defmethod dispatcher/action :notification-remove-by-id
  [db [_ notification-id]]
  (let [current-notifications (get-in db dispatcher/notifications-key)
        next-notifications (filter #(not= (:id %) notification-id) current-notifications)]
    (assoc-in db dispatcher/notifications-key next-notifications)))