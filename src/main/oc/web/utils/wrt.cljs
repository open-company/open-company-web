(ns oc.web.utils.wrt
  (:require [cuerdas.core :as s]
            [oc.web.local-settings :as ls]
            [taoensso.timbre :as timbre]
            [oc.web.lib.utils :as utils]
            [oc.web.urls :as oc-urls]))

(def column-separator ", ")
(def row-separator "\n")
(def empty-value "N/A")

(defn- csv-row [row]
  (let [values (map #(if (keyword? %) (name %) %) row)]
    (s/join column-separator values)))

(defn- csv-rows [rows]
  (let [rows-list (map csv-row rows)]
    (s/join row-separator rows-list)))

(defn- read-date [row]
  (if (:read-at row)
    (.toString (utils/js-date (:read-at row)))
    empty-value))

(defn- clean-value [row k]
  (get row k empty-value))

(defn- name-from-user [{first-name :first-name last-name :last-name}]
  (cond (and (seq first-name) (seq last-name))
        (str first-name " " last-name)
        (seq first-name)
        first-name
        (seq last-name)
        last-name
        :else
        empty-value))

(defn- clean-user [row]
  (vec [(name-from-user row) (clean-value row :email) (read-date row)]))

(defn encoded-csv-string [headers data]
  (let [header (when headers (s/join ", " headers))
        cleaned-data (map clean-user data)
        body (csv-rows cleaned-data)
        csv-content (s/join "\n" [header body])]
    (str "data:text/csv;charset=utf-8," (js/encodeURIComponent csv-content))))