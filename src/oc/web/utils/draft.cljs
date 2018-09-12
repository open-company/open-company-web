(ns oc.web.utils.draft
  (:require [oc.web.lib.utils :as utils]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(defn delete-draft-clicked [draft e]
  (utils/event-stop e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-entry"
                    :message "Delete this draft?"
                    :link-button-title "No"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-title "Yes"
                    :solid-button-cb #(do
                                       (activity-actions/activity-delete draft)
                                       (nux-actions/maybe-dismiss-draft-post-tooltip draft)
                                       (alert-modal/hide-alert))}]
   (alert-modal/show-alert alert-data)))