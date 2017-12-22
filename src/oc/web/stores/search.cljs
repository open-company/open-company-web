(ns oc.web.stores.search
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]))

(defonce search-key :search-results)

(defn search-results []
  (get-in @dispatcher/app-state search-key))

(defmethod dispatcher/action :search-query/finish
  [db [_ {:keys [success error body]}]]
  (timbre/debug body)
  (timbre/debug (:hits body))
  (let [results (vec (sort-by (fn [i] (:created-at i)) (:hits body)))]
    (timbre/debug results)
    (if success
      (assoc db search-key results)
      db)))
