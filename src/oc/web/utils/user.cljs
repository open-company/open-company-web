(ns oc.web.utils.user
  (:require [oc.web.utils.activity :as activity-utils]))

(def user-avatar-filestack-config
  {:accept "image/*"
   :fromSources ["local_file_system"]
   :transformations {
     :crop {
       :aspectRatio 1}}})

(defn notification-title [notification]
  (let [author (:author notification)
        first-name (or (:first-name author) (first (clojure.string/split (:name author) #"\s")))]
    (cond
      (and (:mention notification) (:interaction-id notification))
      (str first-name " mentioned you in a comment")
      (:mention notification)
      (str first-name " mentioned you")
      (:interaction-id notification)
      (str first-name " commented on your post")
      :else
      nil)))

(defn fix-notification [notification & [unread]]
  (let [board-data (activity-utils/board-by-uuid (:board-id notification))
        is-interaction (seq (:interaction-id notification))
        created-at (:notify-at notification)
        title (notification-title notification)]
    (when (seq title)
      {:uuid (:entry-id notification)
       :board-slug (:slug board-data)
       :interaction-id (:interaction-id notification)
       :is-interaction is-interaction
       :unread unread
       :mention (:mention notification)
       :created-at (:notify-at notification)
       :body (:content notification)
       :title title
       :author (:author notification)})))

(defn sorted-notifications [notifications]
  (vec (reverse (sort-by :created-at notifications))))

(defn fix-notifications [notifications]
  (sorted-notifications
   (remove nil?
    (map fix-notification
     notifications))))

(def user-name-max-lenth 64)