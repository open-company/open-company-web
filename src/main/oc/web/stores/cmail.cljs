(ns oc.web.stores.cmail
  (:require [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.utils :as utils]
            [clojure.set :as clj-set]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.utils.label :as label-utils]))

(defmethod dispatcher/action :cmail-expand
  [db [_]]
  (update-in db dispatcher/cmail-state-key merge {:collapsed false
                                                  :labels-floating-view false
                                                  :labels-inline-view false}))

(defmethod dispatcher/action :cmail-collapse
  [db [_]]
  (update-in db dispatcher/cmail-state-key merge {:collapsed true
                                                  :fullscreen false
                                                  :labels-floating-view false
                                                  :labels-inline-view false}))
  
(defmethod dispatcher/action :cmail-reset
  [db [_]]
  (-> db
      (assoc-in dispatcher/cmail-data-key (cmail-actions/get-board-for-edit))
      (update-in dispatcher/cmail-state-key merge {:collapsed true
                                                   :fullscreen false
                                                   :key (utils/activity-uuid)
                                                   :labels-floating-view false
                                                   :labels-inline-view false})))

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

(defmethod dispatcher/action :cmail-toggle-label
  [db [_ toggle-label]]
  (let [cmail-labels (get-in db (conj dispatcher/cmail-data-key :labels))
        cmail-labels-set (set (map :slug cmail-labels))
        label-is-present? (cmail-labels-set (:slug toggle-label))
        can-change? (or label-is-present?
                        (label-utils/can-add-label? cmail-labels))]
    (if can-change?
      (-> db
          (update-in (conj dispatcher/cmail-data-key :labels)
                    (fn [labels]
                      (let [cmail-labels-set (set (map :slug labels))]
                        (if (cmail-labels-set (:slug toggle-label))
                          (filterv #(not= (:slug %) (:slug toggle-label)) labels)
                          (vec (concat labels [(label-utils/clean-entry-label toggle-label)]))))))
          (assoc-in (conj dispatcher/cmail-data-key :has-changes) true))
      db)))

(defmethod dispatcher/action :cmail-add-label
  [db [_ add-label]]
  (let [cmail-labels (get-in db (conj dispatcher/cmail-data-key :labels))
        label-is-present? (label-utils/compare-labels cmail-labels add-label)
        can-change? (or label-is-present?
                        (label-utils/can-add-label? cmail-labels))]
    (if can-change?
      (-> db
          (update-in (conj dispatcher/cmail-data-key :labels)
                     (fn [labels]
                       (let [add-label-map (label-utils/clean-entry-label add-label)]
                         (if (label-utils/compare-labels labels add-label)
                           (mapv #(if (label-utils/compare-label % add-label)
                                    add-label-map
                                    %)
                                 labels)
                           (vec (conj (or labels []) add-label-map))))))
          (assoc-in (conj dispatcher/cmail-data-key :has-changes) true))
      db)))

(defmethod dispatcher/action :cmail-remove-label
  [db [_ remove-label]]
  (-> db
      (update-in (conj dispatcher/cmail-data-key :labels)
                 (fn [labels]
                   (filterv #(not (label-utils/compare-label % remove-label)) labels)))
      (assoc-in (conj dispatcher/cmail-data-key :has-changes) true)))

(defn- labels-value-update [optional-value current-value]
  (if (boolean? optional-value) optional-value (not current-value)))

(defmethod dispatcher/action :cmail-toggle-floating-labels-view
  [db [_ optional-value]]
  (update-in db (concat dispatcher/cmail-state-key [:labels-floating-view]) (partial labels-value-update optional-value)))

(defmethod dispatcher/action :cmail-toggle-inline-labels-view
  [db [_ optional-value]]
  (update-in db (concat dispatcher/cmail-state-key [:labels-inline-view]) (partial labels-value-update optional-value)))

(defmethod dispatcher/action :cmail-toggle-labels-views
  [db [_ optional-value]]
  (-> db
      (update-in (concat dispatcher/cmail-state-key [:labels-floating-view]) (partial labels-value-update optional-value))
      (update-in (concat dispatcher/cmail-state-key [:labels-inline-view]) (partial labels-value-update optional-value))))
