(ns oc.web.stores.subscrition
  (:require [oc.web.dispatcher :as dispatcher]))

(defmethod dispatcher/action :subscription
  [db [_ {:keys [uuid] :as data}]]
  (if uuid
    (assoc-in db [:subscription uuid] data)
    (assoc db :subscription nil)))