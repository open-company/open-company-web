(ns oc.web.components.ui.more-menu
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
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
                       rum/static
                       (rum/local false ::showing-menu)
                       (rum/local false ::move-activity)
                       (ui-mixins/on-window-click-mixin (fn [s e]
                        (when-not (utils/event-inside? e (rum/ref-node s "more-menu"))
                          (when-let [will-close (-> s :rum/args first :will-close)]
                            (when (fn? will-close)
                              (will-close)))
                         (reset! (::showing-menu s) false))))
                       ui-mixins/refresh-tooltips-mixin
  [s {:keys [entity-data share-container-id editable-boards will-open will-close external-share
             tooltip-position show-edit? show-delete? edit-cb delete-cb show-move?
             can-comment-share? comment-share-cb can-react? react-cb can-reply?
             reply-cb assigned-follow-up-data external-follow-up complete-follow-up-title]}]
  (let [delete-link (utils/link-for (:links entity-data) "delete")
        edit-link (utils/link-for (:links entity-data) "partial-update")
        share-link (utils/link-for (:links entity-data) "share")
        is-mobile? (responsive/is-tablet-or-mobile?)
        create-follow-up-link (utils/link-for (:links entity-data) "follow-up" "POST")
        complete-follow-up-link (when (and assigned-follow-up-data
                                           (not (:completed? assigned-follow-up-data)))
                                  (utils/link-for (:links assigned-follow-up-data) "mark-complete" "POST"))]
    (when (or edit-link
              share-link
              delete-link
              can-comment-share?
              can-react?
              can-reply?
              create-follow-up-link
              complete-follow-up-link)
      [:div.more-menu
        {:ref "more-menu"
         :class (when (or @(::move-activity s)
                          @(::showing-menu s))
                  "menu-expanded")}
        (when (or edit-link
                  delete-link
                  can-comment-share?
                  can-react?
                  can-reply?
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
            {:class (utils/class-set {:has-complete-follow-up (and is-mobile?
                                                                   complete-follow-up-link)
                                      :has-create-follow-up (and is-mobile?
                                                                 create-follow-up-link)})}
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
            (when (and is-mobile?
                       (not external-follow-up))
              (if complete-follow-up-link
                [:li.complete-follow-up
                  {:ref "more-menu-complete-follow-up-bt"
                   :on-click #(do
                                (reset! (::showing-menu s) false)
                                (when (fn? will-close)
                                  (will-close))
                                (activity-actions/complete-follow-up entity-data assigned-follow-up-data))}
                  "Complete follow-up"]
                (when create-follow-up-link
                  [:li.create-follow-up
                    {:ref "more-menu-create-follow-up-bt"
                     :data-container "body"
                     :on-click #(do
                                  (reset! (::showing-menu s) false)
                                  (when (fn? will-close)
                                    (will-close))
                                  (activity-actions/create-self-follow-up entity-data create-follow-up-link))}
                    "Follow-up later"])))
            (when can-react?
              [:li.react
                {:on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (when (fn? comment-share-cb)
                                (react-cb)))}
                "React"])
            (when can-reply?
              [:li.reply
                {:on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (when (fn? comment-share-cb)
                                (reply-cb)))}
                "Reply"])
            (when can-comment-share?
              [:li.comment-share
                {:on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (when (fn? comment-share-cb)
                                (comment-share-cb)))}
                "Copy link"])])
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
             :data-toggle (if is-mobile? "" "tooltip")
             :data-container "body"
             :data-placement (or tooltip-position "top")
             :data-delay "{\"show\":\"100\", \"hide\":\"0\"}"
             :title "Share"}])
        (when external-follow-up
          (if complete-follow-up-link
            [:button.mlb-reset.more-menu-complete-follow-up-bt
              {:type "button"
               :ref "more-menu-complete-follow-up-bt"
               :on-click #(do
                            (reset! (::showing-menu s) false)
                            (when (fn? will-close)
                              (will-close))
                            (activity-actions/complete-follow-up entity-data assigned-follow-up-data))
               :data-toggle (if is-mobile? "" "tooltip")
               :data-placement (or tooltip-position "top")
               :data-container "body"
               :title "Complete follow-up"}
              complete-follow-up-title]
            (when create-follow-up-link
              [:div.more-menu-create-follow-up-bt-container
                [:button.mlb-reset.more-menu-create-follow-up-bt
                  {:type "button"
                   :ref "more-menu-create-follow-up-bt"
                   :on-click #(do
                                (reset! (::showing-menu s) false)
                                (when (fn? will-close)
                                  (will-close))
                                (activity-actions/create-self-follow-up entity-data create-follow-up-link))
                   :data-toggle (if is-mobile? "" "tooltip")
                   :data-placement (or tooltip-position "top")
                   :title "Follow up later"}]])))])))
