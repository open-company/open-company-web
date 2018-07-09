(ns oc.web.stores.routing
  (:require [oc.web.dispatcher :as dispatcher]))

(defmethod dispatcher/action :routing
  [db [_ route]]
  (let [org-slug (:org route)
        change-cache-data (dispatcher/change-cache-data db)]
    (-> db
      (assoc-in (dispatcher/change-data-key org-slug) change-cache-data)
      (assoc :router-path route))))