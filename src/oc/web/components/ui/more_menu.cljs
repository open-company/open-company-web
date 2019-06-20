(ns oc.web.components.ui.more-menu
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.activity-move :refer (activity-move)]))

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
  (let [alert-data {:action "delete-entry"
                    :title "Delete this post?"
                    :message "This action cannot be undone."
                    :link-button-style :green
                    :link-button-title "No, keep it"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes, delete it"
                    :solid-button-cb #(let [org-slug (router/current-org-slug)
                                            board-slug (router/current-board-slug)
                                            board-url (oc-urls/board org-slug board-slug)]
                                       (router/nav! board-url)
                                       (activity-actions/activity-delete activity-data)
                                       (alert-modal/hide-alert))
                    :bottom-button-title (when (and (:sample activity-data)
                                                    (activity-actions/has-sample-posts?))
                                           "Delete all sample posts")
                    :bottom-button-style :red
                    :bottom-button-cb #(do
                                         (activity-actions/delete-samples)
                                         (alert-modal/hide-alert))
                    }]
    (alert-modal/show-alert alert-data)))

(rum/defcs more-menu < rum/reactive
                       (rum/local false ::showing-menu)
                       (rum/local false ::move-activity)
                       (on-window-click-mixin (fn [s e]
                        (when-not (utils/event-inside? e (rum/ref-node s "more-menu"))
                          (when-let* [args (vec (:rum/args s))
                                      opts (get args 2)
                                      will-close (:will-close opts)]
                            (when (fn? will-close)
                              (will-close)))
                         (reset! (::showing-menu s) false))))
                       (drv/drv :editable-boards)
                       {:did-mount (fn [s]
                         (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                         s)}
  [s entity-data share-container-id
   {:keys [will-open will-close external-share tooltip-position show-unread
           show-edit? show-delete? edit-cb delete-cb show-move?]}]
  (let [delete-link (utils/link-for (:links entity-data) "delete")
        edit-link (utils/link-for (:links entity-data) "partial-update")
        share-link (utils/link-for (:links entity-data) "share")
        mark-unread-link (utils/link-for (:links entity-data) "mark-unread")
        editable-boards (drv/react s :editable-boards)
        is-mobile? (responsive/is-tablet-or-mobile?)]
    (when (or edit-link
              share-link
              delete-link
              mark-unread-link)
      [:div.more-menu
        {:ref "more-menu"}
        (when (or edit-link
                  delete-link
                  mark-unread-link
                  (and (not external-share)
                       share-link))
          [:button.mlb-reset.more-menu-bt
            {:type "button"
             :ref "more-menu-bt"
             :on-click #(show-hide-menu s will-open will-close)
             :class (when @(::showing-menu s) "active")
             :data-toggle (if is-mobile? "" "tooltip")
             :data-placement (or tooltip-position "top")
             :data-container "body"
             :data-delay "{\"show\":\"100\", \"hide\":\"0\"}"
             :title "More"}])
        (cond
          @(::move-activity s)
          (activity-move {:boards-list (vals editable-boards)
                          :activity-data entity-data
                          :dismiss-cb #(reset! (::move-activity s) false)})
          @(::showing-menu s)
          [:ul.more-menu-list
            {:class (when mark-unread-link "has-read-unread")}
            (when (and edit-link
                       show-edit?)
              [:li.edit
                {:on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (if (fn? edit-cb)
                                (edit-cb entity-data)
                                (activity-actions/activity-edit entity-data)))}
                "Edit"])
            (when (and delete-link
                       show-delete?)
              [:li.delete
                {:on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (if (fn? delete-cb)
                                (delete-cb entity-data)
                                (delete-clicked % entity-data)))}
                "Delete"])
            (when (and edit-link
                       show-move?)
              [:li.move
               {:on-click #(do
                             (reset! (::showing-menu s) false)
                             (reset! (::move-activity s) true))}
               "Move"])
            (when (and (not external-share)
                       share-link)
              [:li.share
                {:on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (activity-actions/activity-share-show entity-data share-container-id))}
                "Share"])
            (when mark-unread-link
              (if show-unread
                [:li.unread
                  {:on-click #(do
                                (reset! (::showing-menu s) false)
                                (when (fn? will-close)
                                  (will-close))
                                (activity-actions/mark-unread entity-data))}
                  "Mark unread"]
                [:li.read
                  {:on-click #(do
                                (reset! (::showing-menu s) false)
                                (when (fn? will-close)
                                  (will-close))
                                (activity-actions/send-item-read (:uuid entity-data) true))}
                  "Mark as read"]))])
        (when (and external-share
                   share-link)
          [:button.mlb-reset.more-menu-share-bt
            {:type "button"
             :ref "tile-menu-share-bt"
             :on-click #(do
                          (reset! (::showing-menu s) false)
                          (when (fn? will-close)
                            (will-close))
                          (activity-actions/activity-share-show entity-data share-container-id))
             :data-toggle "tooltip"
             :data-placement (or tooltip-position "top")
             :data-delay "{\"show\":\"100\", \"hide\":\"0\"}"
             :title "Share"}])])))