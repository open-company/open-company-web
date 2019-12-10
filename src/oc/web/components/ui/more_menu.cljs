(ns oc.web.components.ui.more-menu
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.shared.useragent :as ua]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.activity-move :refer (activity-move)]))

(defn show-hide-menu
  [s will-open will-close]
  (utils/remove-tooltips)
  (let [current-showing-menu (or @(::showing-menu s) (-> s :rum/args first :force-show-menu))
        next-showing-menu (not current-showing-menu)]
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
                       {:did-mount (fn [s]
                        (.tooltip (js/$ "[data-toggle=\"tooltip\"]" (rum/dom-node s)))
                       s)
                       :did-update (fn [s]
                        (.each (js/$ "[data-toggle=\"tooltip\"]" (rum/dom-node s))
                          #(doto (js/$ %2)
                             (.tooltip "fixTitle")
                             (.tooltip "hide")))
                       s)}
  [s {:keys [entity-data share-container-id editable-boards will-open will-close external-share
             tooltip-position show-edit? show-delete? edit-cb delete-cb show-move?
             can-comment-share? comment-share-cb can-react? react-cb can-reply?
             reply-cb assigned-follow-up-data external-follow-up complete-follow-up-title
             show-inbox? force-show-menu capture-clicks external-follow mobile-tray-menu]}]
  (let [delete-link (utils/link-for (:links entity-data) "delete")
        edit-link (utils/link-for (:links entity-data) "partial-update")
        share-link (utils/link-for (:links entity-data) "share")
        is-mobile? (responsive/is-tablet-or-mobile?)
        create-follow-up-link (utils/link-for (:links entity-data) "follow-up" "POST")
        complete-follow-up-link (when (and assigned-follow-up-data
                                           (not (:completed? assigned-follow-up-data)))
                                  (utils/link-for (:links assigned-follow-up-data) "mark-complete" "POST"))
        should-show-more-bt (or edit-link
                                delete-link
                                can-comment-share?
                                can-react?
                                can-reply?
                                (and (not external-share)
                                     share-link))
        inbox-follow-link (utils/link-for (:links entity-data) "follow")
        inbox-unfollow-link (utils/link-for (:links entity-data) "unfollow")
        show-menu (or @(::showing-menu s) force-show-menu)]
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
         :class (utils/class-set {:menu-expanded (or @(::move-activity s)
                                                     show-menu)
                                  :has-more-menu-bt should-show-more-bt
                                  :mobile-tray-menu mobile-tray-menu
                                  :safari-mobile (and ua/ios?
                                                      (not ua/mobile-app?))})
         :on-click (when mobile-tray-menu
                     #(when show-menu
                        (.stopPropagation %)
                        (show-hide-menu s will-open will-close)))}
        (when should-show-more-bt
          [:button.mlb-reset.more-menu-bt
            {:type "button"
             :ref "more-menu-bt"
             :on-click #(show-hide-menu s will-open will-close)
             :class (when show-menu "active")
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
          show-menu
          [:ul.more-menu-list
            {:class (utils/class-set {:has-complete-follow-up (and is-mobile?
                                                                   complete-follow-up-link)
                                      :has-create-follow-up (and is-mobile?
                                                                 create-follow-up-link)})}
            (when (and edit-link
                       show-edit?)
              [:li.edit.top-rounded
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
              [:li.delete.bottom-rounded.bottom-margin
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
              [:li.move.top-rounded
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
            (if inbox-follow-link
              [:li.follow
                {:class (when (or is-mobile? (not external-follow-up)) "bottom-rounded bottom-margin")
                 :on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (activity-actions/inbox-follow (:uuid entity-data)))}
                "Follow"]
              (when inbox-unfollow-link
                [:li.unfollow
                  {:class (when (or is-mobile? (not external-follow-up)) "bottom-rounded bottom-margin")
                   :on-click #(do
                                (reset! (::showing-menu s) false)
                                (when (fn? will-close)
                                  (will-close))
                                (activity-actions/inbox-unfollow (:uuid entity-data)))}
                  "Unfollow"]))
            (when (or is-mobile?
                       (not external-follow-up))
              (if complete-follow-up-link
                [:li.complete-follow-up.bottom-rounded.bottom-margin
                  {:ref "more-menu-complete-follow-up-bt"
                   :on-click #(do
                                (reset! (::showing-menu s) false)
                                (when (fn? will-close)
                                  (will-close))
                                (activity-actions/complete-follow-up entity-data assigned-follow-up-data))}
                  "Complete follow-up"]
                (when create-follow-up-link
                  [:li.create-follow-up.bottom-rounded.bottom-margin
                    {:ref "more-menu-create-follow-up-bt"
                     :data-container "body"
                     :on-click #(do
                                  (reset! (::showing-menu s) false)
                                  (when (fn? will-close)
                                    (will-close))
                                  (activity-actions/create-self-follow-up entity-data create-follow-up-link))}
                    "Follow-up later"])))
            (when can-react?
              [:li.react.top-rounded
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
              [:li.comment-share.bottom-rounded.bottom-margin
                {:on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (when (fn? comment-share-cb)
                                (comment-share-cb)))}
                "Copy link"])
            (when show-inbox?
              [:li.dismiss.top-rounded.bottom-rounded
                {:on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (activity-actions/inbox-dismiss (:uuid entity-data))
                              (when (seq (router/current-activity-id))
                                (nav-actions/dismiss-post-modal %)))}
                "Dismiss"])])
        (when (and external-share
                   share-link)
          [:button.mlb-reset.more-menu-share-bt
            {:type "button"
             :ref "tile-menu-share-bt"
             :class (when (or inbox-follow-link
                              inbox-unfollow-link
                              show-inbox?
                              (and external-follow-up
                                   (or complete-follow-up-link
                                       create-follow-up-link))) "has-next-bt")
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
        (when external-follow
          (if inbox-follow-link
            [:button.mlb-reset.more-menu-inbox-follow-bt
              {:type "button"
               :ref "more-menu-inbox-follow-bt"
               :key "more-menu-inbox-follow-bt"
               :class (when (or show-inbox?
                                (and external-follow-up
                                     (or complete-follow-up-link
                                         create-follow-up-link))) "has-next-bt")
               :on-click #(do
                            (reset! (::showing-menu s) false)
                            (when (fn? will-close)
                              (will-close))
                            (activity-actions/inbox-follow (:uuid entity-data)))
               :data-toggle (if is-mobile? "" "tooltip")
               :data-placement (or tooltip-position "top")
               :data-container "body"
               :title "Follow: Let me know when teammates reply"}]
            (when inbox-unfollow-link
              [:button.mlb-reset.more-menu-inbox-unfollow-bt
                {:type "button"
                 :ref "more-menu-inbox-unfollow-bt"
                 :key "more-menu-inbox-unfollow-bt"
                 :class (when (or show-inbox?
                                (and external-follow-up
                                     (or complete-follow-up-link
                                         create-follow-up-link))) "has-next-bt")
                 :on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (activity-actions/inbox-unfollow (:uuid entity-data)))
                 :data-toggle (if is-mobile? "" "tooltip")
                 :data-placement (or tooltip-position "top")
                 :data-container "body"
                 :title "Mute: Ignore future replies from my team unless I’m mentioned"}])))
        (when external-follow-up
          (if complete-follow-up-link
            [:button.mlb-reset.more-menu-complete-follow-up-bt
              {:type "button"
               :ref "more-menu-complete-follow-up-bt"
               :class (when show-inbox? "has-next-bt")
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
                   :class (when show-inbox? "has-next-bt")
                   :ref "more-menu-create-follow-up-bt"
                   :on-click #(do
                                (reset! (::showing-menu s) false)
                                (when (fn? will-close)
                                  (will-close))
                                (activity-actions/create-self-follow-up entity-data create-follow-up-link))
                   :data-toggle (if is-mobile? "" "tooltip")
                   :data-placement (or tooltip-position "top")
                   :data-container "body"
                   :title "Follow up later"}]])))
        (when show-inbox?
          [:button.mlb-reset.more-menu-inbox-dismiss-bt
            {:type "button"
             :ref "more-menu-inbox-dismiss-bt"
             :on-click #(do
                          (reset! (::showing-menu s) false)
                          (when (fn? will-close)
                            (will-close))
                          (activity-actions/inbox-dismiss (:uuid entity-data))
                          (when (seq (router/current-activity-id))
                            (nav-actions/dismiss-post-modal %)))
             :data-toggle (if is-mobile? "" "tooltip")
             :data-placement (or tooltip-position "top")
             :data-container "body"
             :title "Dismiss"}])])))