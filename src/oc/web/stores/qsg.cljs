(ns oc.web.stores.qsg
  (:require [oc.web.dispatcher :as dispatcher]))

(defn profile-photo-next-step [cur-step]
  (case cur-step
    nil?
    :profile-photo-1

    :profile-photo-1
    :profile-photo-2

    :profile-photo-2
    :profile-photo-3

    (:profile-photo-3 :reset)
    nil
    ;; default
    cur-step))

(defmethod dispatcher/action :qsg-profile-photo
  [db [_ force-step]]
  (let [cur-step (:step (:qsg db))
        next-step (or force-step
                      (profile-photo-next-step cur-step))
        next-db (if (= force-step :profile-photo-end)
                  (assoc-in db [:qsg :profile-photo-end] true)
                  (update-in db [:qsg] dissoc :profile-photo-end))]
    (assoc-in next-db [:qsg :step] next-step)))