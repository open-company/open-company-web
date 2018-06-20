(ns oc.web.stores.org
  (:require [oc.web.lib.utils :as utils]
            [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]))

(defn read-only-org
  [org-data]
  (let [links (:links org-data)
        read-only (utils/readonly-org? links)]
    (assoc org-data :read-only read-only)))

(defn fix-org
  "Fix org data coming from the API."
  [org-data]
  (read-only-org org-data))

(defmethod dispatcher/action :org-loaded
  [db [_ org-data saved?]]
  ;; We need to remove the boards that are no longer in the org except all-posts and drafts
  (let [boards-key (dispatcher/boards-key (:slug org-data))
        old-boards (get-in db boards-key)
        keep-boards [:all-posts :must-see]
        board-slugs (set (concat (map (comp keyword :slug) (:boards org-data)) keep-boards))
        filter-board (fn [[k v]]
                       (board-slugs k))
        next-boards (into {} (filter filter-board old-boards))]
    (-> db
      (assoc-in (dispatcher/org-data-key (:slug org-data)) (fix-org org-data))
      (assoc :org-editing (-> org-data
                              (assoc :saved saved?)
                              (dissoc :has-changes)))
      (assoc-in boards-key next-boards))))

(defmethod dispatcher/action :org-create
  [db [_]]
  (dissoc db :latest-entry-point :latest-auth-settings))

(defmethod dispatcher/action :org-edit-setup
  [db [_ org-data]]
  (assoc db :org-editing org-data))