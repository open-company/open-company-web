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

(defmethod dispatcher/action :cmail-toggle-label
  [db [_ toggle-label]]
  (js/console.log "DBG :cmail-toggle-label" toggle-label)
  (update-in db (concat dispatcher/cmail-data-key [:labels])
             (fn [labels]
               (let [cmail-labels-set (set (map :slug labels))]
                 (if (cmail-labels-set (:slug toggle-label))
                   (filterv #(not= (:slug %) (:slug toggle-label)) labels)
                   (vec (concat labels [(select-keys toggle-label [:uuid :name :color :slug])])))))))

(defmethod dispatcher/action :toggle-cmail-labels-view
  [db [_]]
  (update-in db (concat dispatcher/cmail-state-key [:show-labels-view]) not))

(defmethod dispatcher/action :label-saved
  [db [_ org-slug saved-label]]
  (js/console.log "DBG :label-saved" org-slug saved-label)
  (as-> db tdb
    (update-in tdb (dispatcher/org-labels-key org-slug)
                 (fn [labels]
                   (let [found? (atom false)
                         updated-labels (mapv (fn [label]
                                                 (if (= (:uuid label) (:uuid saved-label))
                                                   (do
                                                     (reset! found? true)
                                                     saved-label)
                                                   label))
                                               labels)]
                     (if @found?
                       updated-labels
                       (concat labels [saved-label])))))
    (if (get-in db (conj dispatcher/cmail-state-key :show-labels-view))
      (update-in tdb (conj dispatcher/cmail-data-key :labels)
                 (fn [labels]
                   (let [found? (atom false)
                         new-label (select-keys saved-label [:uuid :slug :color :name])
                         updated-labels (mapv (fn [label]
                                                (if (= (:uuid label) (:uuid saved-label))
                                                  (do (reset! found? true)
                                                      new-label)
                                                  label))
                                              labels)
                         next-labels (if @found?
                                       updated-labels
                                       (vec (conj labels new-label)))]
                     (js/console.log "DBG updating cmail labels" labels)
                     (js/console.log "DBG    updated labels" next-labels)
                     next-labels)))
      tdb)))