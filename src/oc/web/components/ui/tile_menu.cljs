(ns oc.web.components.ui.tile-menu
  (:require [rum.core :as rum]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]))

;; Delete handling

(defn delete-clicked [e activity-data]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-entry"
                    :message (str "Delete this post?")
                    :link-button-title "No"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes"
                    :solid-button-cb #(let [org-slug (router/current-org-slug)
                                            board-slug (router/current-board-slug)
                                            board-url (oc-urls/board org-slug board-slug)]
                                       (router/nav! board-url)
                                       (activity-actions/activity-delete activity-data)
                                       (alert-modal/hide-alert))
                    }]
    (alert-modal/show-alert alert-data)))

(rum/defcs tile-menu < rum/static
  [s activity-data share-container-id tooltip-position]
  (let [delete-link (utils/link-for (:links activity-data) "delete")
        edit-link (utils/link-for (:links activity-data) "partial-update")
        share-link (utils/link-for (:links activity-data) "share")
        fixed-tooltip-position (or tooltip-position "top")
        must-read (:must-read activity-data)
        must-read-title (if must-read
                          "Unmark as must read"
                          "Mark as must read")]
    (when (or edit-link
              delete-link
              share-link)
      [:div.tile-menu
        (when edit-link
          [:button.mlb-reset.tile-menu-bt.tile-menu-must-read-bt
            {:class (utils/class-set {:must-read-on must-read})
             :type "button"
             :ref "tile-menu-must-read-bt"
             :on-click #(activity-actions/toggle-must-read activity-data)
             :data-toggle "tooltip"
             :data-position fixed-tooltip-position
             :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
             :title must-read-title}])
        (when edit-link
          [:button.mlb-reset.tile-menu-bt.tile-menu-edit-bt
            {:type "button"
             :ref "tile-menu-edit-bt"
             :on-click #(activity-actions/activity-edit activity-data)
             :data-toggle "tooltip"
             :data-placement fixed-tooltip-position
             :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
             :title "Edit"}])
        (when delete-link
          [:button.mlb-reset.tile-menu-bt.tile-menu-delete-bt
            {:type "button"
             :ref "tile-menu-delete-bt"
             :on-click #(delete-clicked % activity-data)
             :data-toggle "tooltip"
             :data-placement fixed-tooltip-position
             :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
             :title "Delete"}])
        (when share-link
          [:button.mlb-reset.tile-menu-bt.tile-menu-share-bt
            {:type "button"
             :ref "tile-menu-share-bt"
             :on-click #(activity-actions/activity-share-show activity-data share-container-id)
             :data-toggle "tooltip"
             :data-placement fixed-tooltip-position
             :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
             :title "Share"}])])))