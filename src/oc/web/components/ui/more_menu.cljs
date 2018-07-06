(ns oc.web.components.ui.more-menu
  (:require-macros [if-let.core :refer (when-let*)])
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

(defn show-hide-menu
  [s will-open will-close]
  (utils/remove-tooltips)
  (let [next-showing-menu (not @(::showing-menu s))]
    (if next-showing-menu
      (when (fn? will-open)
        (will-open))
      (when (fn? will-close)
        (will-close)))
    (reset! (::showing-menu s) next-showing-menu)))

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
                               (when-let* [delegate-methods (get (:rum/args s) 2)
                                           will-close (:will-close delegate-methods)]
                                 (when (fn? will-close)
                                   (will-close)))
                               (reset! (::showing-menu s) false))))
                         s)
                        :will-unmount (fn [s]
                         (when @(::click-listener s)
                           (events/unlistenByKey @(::click-listener s))
                           (reset! (::click-listener s) nil))
                         s)}
  [s activity-data share-container-id
   {:keys [will-open will-close]}]
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
           :on-click #(show-hide-menu s will-open will-close)
           :class (when @(::showing-menu s) "active")
           :data-toggle (if (responsive/is-tablet-or-mobile?) "" "tooltip")
           :data-placement "top"
           :data-container "body"
           :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
           :title "More"}]
        (when @(::showing-menu s)
          [:ul.more-menu-list
             {:on-mouse-leave #(show-hide-menu s will-open will-close)}
            (when edit-link
              [:li.edit
                {:on-click #(activity-actions/activity-edit activity-data)}
                "Edit"])
            (when delete-link
              [:li.delete
                {:on-click #(delete-clicked % activity-data)}
                "Delete"])
            (when (and share-link (responsive/is-tablet-or-mobile?))
             [:li.share
               {:on-click #(activity-actions/activity-share-show activity-data share-container-id)}
              "Share"])
            (when edit-link
              [:li
               {:class (utils/class-set
                         {:must-see (not (:must-see activity-data))
                          :must-see-on (:must-see activity-data)})
                :on-click #(do
                             (utils/event-stop %)
                             (activity-actions/toggle-must-see activity-data))}
               (if (:must-see activity-data)
                 "Unmark"
                 "Must see")]
              )])
          (when (and share-link (not (responsive/is-tablet-or-mobile?)))
            [:button.mlb-reset.more-menu-share-bt
              {:type "button"
               :ref "tile-menu-share-bt"
               :on-click #(activity-actions/activity-share-show
                           activity-data
                           share-container-id)
               :data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Share"}])])))
