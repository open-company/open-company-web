(ns oc.web.utils.ui
  (:require [oc.web.lib.utils :as utils]
            [oc.web.actions.activity :as activity-actions]))

(defn resize-textarea [textarea]
  (when textarea
    (set! (.-height (.-style textarea)) "")
    (set! (.-height (.-style textarea)) (str (.-scrollHeight textarea) "px"))))

(defn ui-compose []
  (utils/remove-tooltips)
  (activity-actions/activity-edit))

(def watch-activity-copy "Watch this update for future activity") ; "Get notified about new post activity"
(def unwatch-activity-copy "Ignore future activity for this update") ; "Don't show replies to this update" ; "Ignore future activity unless mentioned"