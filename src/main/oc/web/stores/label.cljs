(ns oc.web.stores.label
  (:require [oc.web.dispatcher :as dispatcher]))

(defmethod dispatcher/action :labels-loaded
  [db [_ org-slug labels]]
  (assoc-in db (dispatcher/org-labels-key org-slug) labels))

(defmethod dispatcher/action :editing-label
  [db [_ label-data]]
  (-> db
      (assoc :show-label-editor true)
      (assoc :editing-label label-data)))

(defmethod dispatcher/action :dismiss-editing-label
  [db [_]]
  (-> db
      (assoc :show-label-editor false)
      (dissoc :editing-label)))

(defmethod dispatcher/action :delete-label
  [db [_ org-slug label-data]]
  (-> db
      (update-in (dispatcher/org-labels-key org-slug) (fn [labels] (filterv #(not= (:uuid %) (:uuid label-data)) labels)))
      (assoc :show-label-editor false)))