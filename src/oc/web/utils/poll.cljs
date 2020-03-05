(ns oc.web.utils.poll
  (:require [oc.lib.user :as user-lib]
            [oc.web.local-settings :as ls]
            [oc.web.lib.utils :as utils]))

(defonce ^:export poll-selector-prefix "oc-poll-portal-")

(defonce min-poll-replies 2)

(defn created-at []
  (utils/as-of-now))

(defn new-poll-id []
  (utils/activity-uuid))

(defn new-reply-id []
  (utils/activity-uuid))

(defn- author-for-user [user-data]
  {:name (user-lib/name-for user-data)
   :avatar-url (:avatar-url user-data)
   :user-id (:user-id user-data)})

(defn poll-reply [user-data body]
  {:body (or body "")
   :author (author-for-user user-data)
   :votes-count 0
   :reply-id (new-reply-id)
   :votes []})

(defn poll-data [user-data poll-id]
  {:question ""
   :poll-uuid poll-id
   :can-add-reply (boolean ls/poll-can-add-reply)
   :created-at (created-at)
   :updated-at (created-at)
   :author (author-for-user user-data)
   :total-votes-count 0
   :replies (mapv #(poll-reply user-data "") (range min-poll-replies))})

(defn clean-polls
  "Clean not needed keys from poll maps."
  [activity-data]
  (if (contains? activity-data :polls)
    (assoc activity-data :polls
     (mapv (fn [poll]
      (-> poll
       (dissoc :links )
       (update :replies
        (fn [replies]
         (mapv #(dissoc % :links :preview) replies)))))
      (:polls activity-data)))
    activity-data))