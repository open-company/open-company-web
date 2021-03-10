(ns oc.web.stores.cmail
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.cmail :as cmail-actions]))

(defmethod dispatcher/action :cmail-expand
  [db [_]]
  (update-in db dispatcher/cmail-state-key merge {:collapsed false
                                                  :show-labels-view false}))

(defmethod dispatcher/action :cmail-collapse
  [db [_]]
  (update-in db dispatcher/cmail-state-key merge {:collapsed true
                                                  :fullscreen false
                                                  :show-labels-view false}))
  
(defmethod dispatcher/action :cmail-reset
  [db [_]]
  (-> db
      (assoc-in dispatcher/cmail-data-key (cmail-actions/get-board-for-edit))
      (update-in dispatcher/cmail-state-key merge {:collapsed true
                                                   :key (utils/activity-uuid)
                                                   :show-labels-view false})))

(defmethod dispatcher/action :cmail-state/update
  [db [_ cmail-state]]
  (assoc-in db dispatcher/cmail-state-key cmail-state))

(defmethod dispatcher/action :cmail-data/update
  [db [_ cmail-data]]
  (update-in db dispatcher/cmail-data-key merge cmail-data))

(defmethod dispatcher/action :cmail-data/replace
  [db [_ cmail-data]]
  (assoc-in db dispatcher/cmail-data-key cmail-data))

(defmethod dispatcher/action :cmail-data/remove-has-changes
  [db [_]]
  (update-in db dispatcher/cmail-data-key dissoc :has-changes))