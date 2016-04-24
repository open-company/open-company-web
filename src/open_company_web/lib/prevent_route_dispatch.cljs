(ns open-company-web.lib.prevent-route-dispatch
  (:require [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]))

(defonce prevent-route-dispatch (atom false))