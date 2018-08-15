(ns oc.web.actions.notifications
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dis]))

;; Default time to disappeara notification
(def default-expiration-time 5)

(defn show-notification [notification-data]
  (let [expiration-time (or (:expire notification-data) default-expiration-time)
        fixed-notification-data (-> notification-data
                                 (assoc :created-at (.getTime (js/Date.)))
                                 (assoc :expire expiration-time))]
    (dis/dispatch! [:notification-add fixed-notification-data])))

(defn remove-notification [notification-data]
  (dis/dispatch! [:notification-remove notification-data]))

(defn remove-notification-by-id [notification-id]
  (dis/dispatch! [:notification-remove-by-id notification-id]))