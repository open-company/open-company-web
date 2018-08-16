(ns oc.web.utils.user
  (:require [oc.web.utils.activity :as activity-utils]))

(def user-avatar-filestack-config
  {:accept "image/*"
   :fromSources ["local_file_system"]
   :transformations {
     :crop {
       :aspectRatio 1}}})

(defn notification-title [notification]
  (cond
    (and (:mention notification) (:interaction-id notification))
    (str (:name (:author notification)) " mentioned you in a comment")
    (:mention notification)
    (str (:name (:author notification)) " mentioned you")
    (:interaction-id notification)
    (str (:name (:author notification)) " commented on your post")))


(defn fix-notification [notification & [unread]]
  (let [board-data (activity-utils/board-by-uuid (:board-id notification))
        is-interaction (seq (:interaction-id notification))
        created-at (:notify-at notification)]
    {:uuid (:entry-id notification)
     :board-slug (:slug board-data)
     :interaction-id (:interaction-id notification)
     :is-interaction is-interaction
     :unread unread
     :mention (:mention notification)
     :created-at (:notify-at notification)
     :body (:content notification)
     :title (notification-title notification)
     :author (:author notification)}))

(defn fix-notifications [notifications]
  (map fix-notification notifications))