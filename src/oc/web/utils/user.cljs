(ns oc.web.utils.user
  (:require [oc.web.utils.activity :as activity-utils]))

(def user-avatar-filestack-config
  {:accept "image/*"
   :fromSources ["local_file_system"]
   :transformations {
     :crop {
       :aspectRatio 1}}})

(defn fix-notification [notification & [unread]]
  (let [board-data (activity-utils/board-by-uuid (:board-id notification))
        is-interaction (seq (:interaction-id notification))
        created-at (:notify-at notification)]
    {:uuid (:entry-id notification)
     :board-slug (:slug board-data)
     :is-interaction is-interaction
     :unread unread
     :created-at (:notify-at notification)
     :body (:content notification)
     :author (:author notification)}))

(defn fix-notifications [notifications]
  (doseq [n notifications]
    (fix-notification n)))