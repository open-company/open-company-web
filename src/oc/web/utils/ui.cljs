(ns oc.web.utils.ui
  (:require [oc.web.lib.utils :as utils]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.activity :as activity-actions]))

(defn resize-textarea [textarea]
  (when textarea
    (set! (.-height (.-style textarea)) "")
    (set! (.-height (.-style textarea)) (str (.-scrollHeight textarea) "px"))))

(defn ui-compose [& [show-add-post-tooltip]]
  (utils/remove-tooltips)
  (activity-actions/activity-edit)
  (when show-add-post-tooltip
    (utils/after 1000 nux-actions/dismiss-add-post-tooltip)))