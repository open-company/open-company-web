(ns oc.web.stores.org
  (:require [oc.web.lib.utils :as utils]
            [oc.web.dispatcher :as dispatcher]))

(defmethod dispatcher/action :org-loaded
  [db [_ org-data saved?]]  
  (-> db
    (assoc-in (dispatcher/org-data-key (:slug org-data)) (utils/fix-org org-data))
    (assoc :org-editing (-> (:org-editing db)
                            (assoc :saved saved?)
                            (dissoc :has-changes)))))

(defmethod dispatcher/action :org-create
  [db [_]]
  (dissoc db :latest-entry-point :latest-auth-settings))

(defmethod dispatcher/action :org-edit-setup
  [db [_ org-data]]
  (assoc db :org-editing org-data))