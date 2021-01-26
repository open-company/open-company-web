(ns oc.web.utils.wrt
  (:require [cuerdas.core :as s]
            [oc.web.local-settings :as ls]
            [taoensso.timbre :as timbre]
            [oc.web.lib.utils :as utils]
            [oc.web.urls :as oc-urls]))

(def column-separator ", ")
(def row-separator "\n")

(defn- csv-row [row]
  (let [values (map #(if (keyword? %) (name %) %) row)]
    (s/join column-separator values)))

(defn- csv-rows [rows]
  (let [rows-list (map csv-row rows)]
    (s/join row-separator rows-list)))

(defn- read-date [row]
  (if (:read-at row)
    (.toString (utils/js-date (:read-at row)))
    "N/A"))

(defn- field-value [row k]
  (get row k ""))

(defn- clean-users-list [data]
  (map #(vec [(field-value % :name) (field-value % :email) (read-date %)]) data))

(defn encoded-csv-string [headers data]
  (let [header (when headers (s/join ", " headers))
        cleaned-data (clean-users-list data)
        body (csv-rows cleaned-data)
        csv-content (s/join "\n" [header body])]
    (str "data:text/csv;charset=utf-8," (js/encodeURIComponent csv-content))))