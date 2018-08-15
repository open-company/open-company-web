(ns oc.web.utils.user)

(def user-avatar-filestack-config
  {:accept "image/*"
   :fromSources ["local_file_system"]
   :transformations {
     :crop {
       :aspectRatio 1}}})

(defn fix-notification [notification & [unread]]
  (if unread
    (assoc notification :unread true)
    notification))

(defn fix-notifications [notifications]
  (doseq [n notifications]
    (fix-notification n)))