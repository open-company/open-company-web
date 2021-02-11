(ns oc.web.utils.wrt
  (:require [cuerdas.core :as s]
            [oc.lib.time :as lib-time]
            [oc.lib.user :as lib-user]
            [oc.web.urls :as oc-urls]
            [oc.web.local-settings :as ls]))

(def column-separator ", ")
(def row-separator "\n")
(def empty-value "-")

(def premium-download-csv-tooltip "Please upgrade to premium to download your team's Analytics data.")

(defn- csv-row [row]
  (let [values (map #(if (keyword? %) (name %) %) row)]
    (s/join column-separator values)))

(defn- csv-rows [rows]
  (let [rows-list (map csv-row rows)]
    (s/join row-separator rows-list)))

(defn- read-date [row]
  (if (:read-at row)
    (lib-time/csv-date-time (:read-at row))
    empty-value))

(defn- clean-value [row k]
  (get row k empty-value))

(defn- name-from-user [current-user-id user-map]
  (let [nm (lib-user/name-for-csv user-map)]
    (if nm
      (str nm (when (= (:user-id user-map) current-user-id)
                " (you)"))
      empty-value)))

(defn- clean-user [current-user-id row]
  (vec [(name-from-user current-user-id row) (clean-value row :email) (read-date row)]))

(defn- post-href [entry-data]
  (str ls/web-server-domain (oc-urls/entry (:board-slug entry-data) (:uuid entry-data))))

(defn- csv-intro [org-name]
  (str org-name " analytics for post generated on " (lib-time/csv-date) "\n"))

(defn encoded-csv [org-data entry-data headers data current-user-id]
  (let [header (when headers (s/join ", " headers))
        cleaned-data (map (partial clean-user current-user-id) data)
        body (csv-rows cleaned-data)
        title (str "Title: " (:headline entry-data))
        published (str "Published on: " (lib-time/csv-date-time (:published-at entry-data)))
        post-link (str "Link: " (post-href entry-data))
        reads-count (count (filter :read-at data))
        reads-percent (when (pos? reads-count)
                        (str (.toFixed (float (* (/ reads-count (count data)) 100)) 2) "%"))
        stats (str "Reads: " (count (filter :read-at data)) " of " (count data) (when reads-percent (str " (" reads-percent ")")))
        intro (csv-intro (:name org-data))
        csv-content (s/join "\n" [intro title published post-link stats "-" header body])]
    (str "data:text/csv;charset=utf-8," (js/encodeURIComponent csv-content))))

(defn csv-filename [entry-data]
  (str "post-" (:uuid entry-data) "-" (lib-time/to-iso (lib-time/utc-now)) ".csv"))