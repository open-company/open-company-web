(ns oc.web.stores.payments
  (:require [oc.web.dispatcher :as dispatcher]))

(defmethod dispatcher/action :payments
  [db [_ {:keys [uuid] :as data}]]
  (if uuid
    (assoc-in db [:payments uuid] data)
    (assoc db :payments nil)))