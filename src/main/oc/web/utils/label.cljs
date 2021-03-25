(ns oc.web.utils.label
  (:require [oc.lib.color :as lib-color]
            [oc.lib.hateoas :as hateoas]
            [oc.web.local-settings :as ls]))

;; Data parse

(defn parse-label [label-map]
  (-> label-map
      (assoc :rgb-color (when (:color label-map)
                          (lib-color/hex->rgb (:color label-map))))
      (assoc :can-edit? (hateoas/link-for (:links label-map) "partial-update"))
      (assoc :can-delete? (hateoas/link-for (:links label-map) "delete"))))

(defn parse-labels [labels-resp]
  (->> labels-resp
       :collection
       :items
       (mapv parse-label)))

(defn parse-entry-label [label-map]
  (assoc label-map
         :rgb-color (when (:color label-map)
                      (lib-color/hex->rgb (:color label-map)))))

(defn parse-entry-labels [entry-labels-data]
  (mapv parse-entry-label entry-labels-data))

;; Clean

(defn clean-entry-label [label-data]
  (select-keys label-data [:color :uuid :slug :name]))

(defn clean-entry-labels [entry-data]
  (update entry-data :labels #(mapv clean-entry-label %)))

(defn clean-label [label-data]
  (select-keys label-data [:color :uuid :slug :name :org-uuid]))

(defn clean-labels [labels-data]
  (mapv clean-label labels-data))

(defn can-add-label? [labels]
  (< (count labels) ls/max-entry-labels))