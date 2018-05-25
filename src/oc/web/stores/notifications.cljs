(ns oc.web.stores.notifications
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.utils :as utils]))


(defmethod dispatcher/action :notification-add
  [db [_ notification-data]]
  (let [current-notifications (get-in db dispatcher/notifications-key)
        next-notifications (conj current-notifications notification-data)]
    (assoc-in db dispatcher/notifications-key next-notifications)))

(defmethod dispatcher/action :notification-remove
  [db [_ notification-data]]
  (let [current-notifications (get-in db dispatcher/notifications-key)
        next-notifications (filter #(not= (:created-at %) (:created-at notification-data)) current-notifications)]
    (assoc-in db dispatcher/notifications-key next-notifications)))