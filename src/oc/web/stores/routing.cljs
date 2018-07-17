(ns oc.web.stores.routing
  (:require [oc.web.dispatcher :as dispatcher]))

(defmethod dispatcher/action :routing
  [db [_ route]]
  (assoc db :router-path route))