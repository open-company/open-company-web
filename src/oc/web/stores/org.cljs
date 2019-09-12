(ns oc.web.stores.org
  (:require [oc.web.expo :as expo]
            [oc.shared.useragent :as ua]
            [oc.web.lib.utils :as utils]
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
  [db [_ org-data saved? email-domain]]
  ;; We need to remove the boards that are no longer in the org
  (let [boards-key (dispatcher/boards-key (:slug org-data))
        old-boards (get-in db boards-key)
        ;; No need to add a spacial case for drafts board here since
        ;; we are only excluding keys that already exists in the app-state
        board-slugs (set (mapcat #(vec [(keyword (str (:slug %) "-recent-activity"))
                                        (keyword (str (:slug %) "-recently-posted"))])
                                 (:boards org-data)))
        filter-board (fn [[k v]]
                       (board-slugs k))
        next-boards (into {} (filter filter-board old-boards))
        section-names (:default-board-names org-data)
        selected-sections (map :name (:boards org-data))
        sections (vec (map #(hash-map :name % :selected (utils/in? selected-sections %)) section-names))
        fixed-org-data (fix-org org-data)
        with-saved? (if (nil? saved?)
                      ;; If saved? is nil it means no save happened, so we keep the old saved? value
                      org-data
                      ;; If save actually happened let's update the saved value
                      (assoc org-data :saved saved?))
        next-org-editing (-> with-saved?
                             (assoc :email-domain email-domain)
                             (dissoc :has-changes))
        new-db (-> db
                   (assoc-in (dispatcher/org-data-key (:slug org-data)) fixed-org-data)
                   (assoc :org-editing next-org-editing)
                   (assoc :org-avatar-editing (select-keys fixed-org-data [:logo-url :logo-width :logo-height]))
                   (assoc-in boards-key next-boards))]
    (when ua/mobile-app?
      (expo/org-loaded! fixed-org-data))
    new-db
    ))

(defmethod dispatcher/action :org-avatar-update/failed
  [db [_]]
  (let [org-data (dispatcher/org-data db)]
    (assoc db :org-avatar-editing (select-keys org-data [:logo-url :logo-width :logo-height]))))

(defmethod dispatcher/action :org-create
  [db [_]]
  (dissoc db
   :latest-entry-point
   :latest-auth-settings
   ;; Remove the entry point, orgs and auth settings
   ;; to avoid using the old loaded orgs
   (first dispatcher/api-entry-point-key)
   (first dispatcher/auth-settings-key)
   dispatcher/orgs-key))

(defmethod dispatcher/action :org-edit-setup
  [db [_ org-data]]
  (assoc db :org-editing org-data))
