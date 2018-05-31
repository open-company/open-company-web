(ns oc.web.actions.notifications
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dis]))

(defn show-notification [notification-data]
  (dis/dispatch! [:notification-add notification-data]))

(defn remove-notification [notification-data]
  (dis/dispatch! [:notification-remove notification-data]))