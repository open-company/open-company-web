(ns oc.web.components.ui.more-menu
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
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

(rum/defcs more-menu < rum/static
                       (rum/local false ::showing-menu)
                       (rum/local false ::move-activity)
                       (rum/local nil ::click-listener)
                       {:will-mount (fn [s]
                         (reset! (::click-listener s)
                           (events/listen js/window EventType/CLICK
                            #(when (not (utils/event-inside? % (rum/ref-node s "more-menu-bt")))
                               (reset! (::showing-menu s) false))))
                         s)
                        :will-unmount (fn [s]
                         (when @(::click-listener s)
                           (events/unlistenByKey @(::click-listener s))
                           (reset! (::click-listener s) nil))
                         s)}
  [s activity-data]
  (let [delete-link (utils/link-for (:links activity-data) "delete")
        edit-link (utils/link-for (:links activity-data) "partial-update")
        share-link (utils/link-for (:links activity-data) "share")]
    (when (or edit-link
              share-link
              delete-link)
      [:div.more-menu
        [:button.mlb-reset.more-menu-bt
          {:type "button"
           :ref "more-menu-bt"
           :on-click (fn [_]
                       (utils/remove-tooltips)
                       (reset! (::showing-menu s) (not @(::showing-menu s))))
           :class (when @(::showing-menu s) "active")
           :data-toggle (if (responsive/is-tablet-or-mobile?) "" "tooltip")
           :data-placement "left"
           :data-container "body"
           :title "More"}]
        (when @(::showing-menu s)
          [:ul.more-menu-list
            (when edit-link
              [:li.edit
                {:on-click #(activity-actions/activity-edit activity-data)}
                "Edit"])
            (when delete-link
              [:li.delete
                {:on-click #(delete-clicked % activity-data)}
                "Delete"])
            (when share-link
             [:li.share
               {:on-click #(activity-actions/activity-share-show activity-data)}
               "Share"])])])))