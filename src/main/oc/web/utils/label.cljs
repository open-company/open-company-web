(ns oc.web.utils.label
  (:require [oc.lib.hateoas :as hateoas]
            [oc.web.dispatcher :as dis]
            [clojure.set :as clj-set]
            [oc.web.local-settings :as ls]))

;; Labels comparison

(defn label-compare-set [label deep-check?]
  (set (remove nil?
               [(:slug label)
                (:uuid label)
                (when deep-check?
                  (:name label))])))

(defn compare-label
  ([label-a label-b] (compare-label label-a label-b false))
  ([label-a label-b deep-check?] (seq (clj-set/intersection (label-compare-set label-a deep-check?) (label-compare-set label-b deep-check?)))))

(defn compare-labels
  ([labels label] (compare-labels labels label false))
  ([labels label deep-check?]
   (let [list-values-set (set (mapcat #(label-compare-set % deep-check?) labels))]
     (seq (clj-set/intersection (label-compare-set label deep-check?) list-values-set)))))

;; Data parse

(def default-label-slug "-empty-label-slug")

(defn new-label-data
  ([] (new-label-data "" (:uuid (dis/org-data))))
  ([label-name] (new-label-data label-name (:uuid (dis/org-data))))
  ([label-name org-uuid]
   {:name (or label-name "")
    :slug default-label-slug
    :org-uuid org-uuid
    :uuid ""}))

(defn parse-label [label-map]
  (-> label-map
      (assoc :can-edit? (hateoas/link-for (:links label-map) "partial-update"))
      (assoc :can-delete? (hateoas/link-for (:links label-map) "delete"))))

(defn parse-labels [labels-resp]
  (->> labels-resp
       :collection
       :items
       (mapv parse-label)))

(defn parse-entry-label [label-map]
  (identity label-map))

(defn parse-entry-labels [entry-labels-data]
  (mapv parse-entry-label entry-labels-data))

;; Clean

(defn clean-entry-label [label-data]
  (select-keys label-data [:uuid :slug :name]))

(defn clean-entry-labels [entry-data]
  (update entry-data :labels #(mapv clean-entry-label %)))

(defn clean-label [label-data]
  (select-keys label-data [:uuid :slug :name :org-uuid]))

(defn clean-labels [labels-data]
  (mapv clean-label labels-data))

(defn can-add-label? [labels]
  (< (count labels) ls/max-entry-labels))