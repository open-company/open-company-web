(ns oc.web.stores.search
  (:require [taoensso.timbre :as timbre]
            [oc.web.dispatcher :as dispatcher]))

(defonce search-key :search-results)
(defonce search-active? :search-active)

(defn search-results []
  (get-in @dispatcher/app-state search-key))


(defn- cleanup-uuid
  [results]
  (vec (map (fn [result]
              (let [source (:_source result)
                    new-uuid (clojure.string/replace (:uuid source)
                                                     "entry-" "")]
                (assoc result :_source (assoc source :uuid new-uuid))))
            results)))

(defmethod dispatcher/action :search-query/finish
  [db [_ {:keys [success error body]}]]
  (let [total-hits (:total body)
        results (vec (sort-by #(:created-at (:_source %)) (:hits body)))]
    (if success
      (assoc db search-key {:count total-hits :results (cleanup-uuid results)})
      db)))

(defmethod dispatcher/action :search-active
  [db [_]]
  (assoc db search-active? true))

(defmethod dispatcher/action :search-inactive
  [db [_]]
  (assoc db search-active? false))

(defmethod dispatcher/action :search-reset
  [db [_]]
  (assoc db search-key []))