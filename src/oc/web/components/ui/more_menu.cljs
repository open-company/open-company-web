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
                       (rum/local false ::can-unmount)
                       (rum/local false ::last-force-show-menu)
                       (ui-mixins/on-window-click-mixin (fn [s e]
                        (when-not (utils/event-inside? e (rum/ref-node s "more-menu"))
                          (when-let [will-close (-> s :rum/args first :will-close)]
                            (when (fn? will-close)
                              (will-close)))
                         (reset! (::showing-menu s) false))))
                       {:did-mount (fn [s]
                        (.tooltip (js/$ "[data-toggle=\"tooltip\"]" (rum/dom-node s)))
                       s)
                       :will-update (fn [s]
                        (let [next-force-show-menu (-> s :rum/args first :force-show-menu)]
                          (when (not= @(::last-force-show-menu s) next-force-show-menu)
                            (reset! (::last-force-show-menu s) next-force-show-menu)
                            (when next-force-show-menu
                             ;; avoid automatic dismiss of the menu on iOS
                             (reset! (::can-unmount s) false)
                             (utils/after 1000 #(reset! (::can-unmount s) true)))))
                        s)
                       :did-update (fn [s]
                        (.each (js/$ "[data-toggle=\"tooltip\"]" (rum/dom-node s))
                          #(doto (js/$ %2)
                             (.tooltip "fixTitle")
                             (.tooltip "hide")))
                       s)}
  [s {:keys [entity-data share-container-id editable-boards will-open will-close external-share
             tooltip-position show-edit? show-delete? edit-cb delete-cb show-move? show-unread
             can-comment-share? comment-share-cb can-react? react-cb can-reply?
             reply-cb external-bookmark remove-bookmark-title
             show-inbox? force-show-menu capture-clicks external-follow mobile-tray-menu
             mark-unread-cb]}]
  (let [delete-link (utils/link-for (:links entity-data) "delete")
        edit-link (utils/link-for (:links entity-data) "partial-update")
        share-link (utils/link-for (:links entity-data) "share")
        inbox-unread-link (utils/link-for (:links entity-data) "unread")
        is-mobile? (responsive/is-tablet-or-mobile?)
        add-bookmark-link (utils/link-for (:links entity-data) "bookmark" "POST")
        remove-bookmark-link (when (:bookmarked-at entity-data)
                               (utils/link-for (:links entity-data) "bookmark" "DELETE"))
        should-show-more-bt (or edit-link
                                delete-link
                                can-comment-share?
                                can-react?
                                can-reply?
                                (and (not external-share)
                                     share-link)
                                (and inbox-unread-link
                                     show-unread))
        inbox-follow-link (utils/link-for (:links entity-data) "follow")
        inbox-unfollow-link (utils/link-for (:links entity-data) "unfollow")
        show-menu (or @(::showing-menu s) force-show-menu)]
    (when (or edit-link
              share-link
              inbox-unread-link
              delete-link
              can-comment-share?
              can-react?
              can-reply?
              add-bookmark-link
              remove-bookmark-link
              inbox-unread-link)
      [:div.more-menu
        {:ref "more-menu"
         :class (utils/class-set {:menu-expanded (or @(::move-activity s)
                                                     show-menu)
                                  :has-more-menu-bt should-show-more-bt
                                  :mobile-tray-menu mobile-tray-menu
                                  :android-browser (and ua/android?
                                                        (not ua/mobile-app?))
                                  :ios-browser (and ua/ios?
                                                    (not ua/mobile-app?))})
         :on-click (when mobile-tray-menu
                     #(when (and show-menu
                                 @(::can-unmount s))
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
            {:class (utils/class-set {:has-remove-bookmark (and add-bookmark-link
                                                                (or is-mobile?
                                                                    (not external-bookmark)))
                                      :has-add-bookmark (and remove-bookmark-link
                                                             (or is-mobile?
                                                                 (not external-bookmark)))
                                      :has-mark-unread inbox-unread-link})}
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
               {:class (when (and (not (and (not external-share)
                                            share-link))
                                  (not (or is-mobile?
                                            (not external-follow)))
                                   (not (or is-mobile?
                                            (not external-bookmark))))
                          "bottom-rounded bottom-margin")
                :on-click #(do
                             (reset! (::showing-menu s) false)
                             (reset! (::move-activity s) true))}
               "Move"])
            (when (and (not external-share)
                       share-link)
              [:li.share
                {:class (when (and (not inbox-unread-link)
                                   (not (or is-mobile?
                                            (not external-follow)))
                                   (not (or is-mobile?
                                            (not external-bookmark))))
                          "bottom-rounded bottom-margin")
                 :on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (activity-actions/activity-share-show entity-data share-container-id))}
                "Share"])
            (when (and inbox-unread-link
                       show-unread)
              [:li.unread
                {:class (when (and (not (or is-mobile?
                                            (not external-follow)))
                                   (not (or is-mobile?
                                            (not external-bookmark))))
                          "bottom-rounded bottom-margin")
                 :on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (activity-actions/mark-unread entity-data)
                              (when (fn? mark-unread-cb)
                                (mark-unread-cb)))}
                "Mark as unread"])
            (when (and show-inbox?
                       (or is-mobile?
                           (not external-follow)))
              (if inbox-follow-link
                [:li.follow
                  {:class (when-not (or is-mobile? (not external-bookmark)) "bottom-rounded bottom-margin")
                   :on-click #(do
                                (reset! (::showing-menu s) false)
                                (when (fn? will-close)
                                  (will-close))
                                (activity-actions/inbox-follow (:uuid entity-data)))}
                  "Follow"]
                (when inbox-unfollow-link
                  [:li.unfollow
                    {:class (when-not (or is-mobile? (not external-bookmark)) "bottom-rounded bottom-margin")
                     :on-click #(do
                                  (reset! (::showing-menu s) false)
                                  (when (fn? will-close)
                                    (will-close))
                                  (activity-actions/inbox-unfollow (:uuid entity-data)))}
                    "Mute"])))
            (when (or is-mobile?
                      (not external-bookmark))
              (if remove-bookmark-link
                [:li.remove-bookmark.bottom-rounded.bottom-margin
                  {:ref "more-menu-remove-bookmark-bt"
                   :on-click #(do
                                (reset! (::showing-menu s) false)
                                (when (fn? will-close)
                                  (will-close))
                                (activity-actions/remove-bookmark entity-data remove-bookmark-link))}
                  "Remove bookmark"]
                (when add-bookmark-link
                  [:li.add-bookmark.bottom-rounded.bottom-margin
                    {:ref "more-menu-add-bookmark-bt"
                     :data-container "body"
                     :on-click #(do
                                  (reset! (::showing-menu s) false)
                                  (when (fn? will-close)
                                    (will-close))
                                  (activity-actions/add-bookmark entity-data add-bookmark-link))}
                    "Bookmark"])))
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
                              (and external-bookmark
                                   (or add-bookmark-link
                                       remove-bookmark-link))) "has-next-bt")
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
                              (and external-bookmark
                                   (or add-bookmark-link
                                       remove-bookmark-link))) "has-next-bt")
             :on-click #(do
                          (reset! (::showing-menu s) false)
                          (when (fn? will-close)
                            (will-close))
                          (activity-actions/inbox-follow (:uuid entity-data)))
             :data-toggle (if is-mobile? "" "tooltip")
             :data-placement (or tooltip-position "top")
             :data-container "body"
             :title "Get notified about new post activity"}]
            (when inbox-unfollow-link
              [:button.mlb-reset.more-menu-inbox-unfollow-bt
                {:type "button"
                 :ref "more-menu-inbox-unfollow-bt"
                 :key "more-menu-inbox-unfollow-bt"
                 :class (when (or show-inbox?
                                (and external-bookmark
                                     (or add-bookmark-link
                                         remove-bookmark-link))) "has-next-bt")
                 :on-click #(do
                              (reset! (::showing-menu s) false)
                              (when (fn? will-close)
                                (will-close))
                              (activity-actions/inbox-unfollow (:uuid entity-data)))
                 :data-toggle (if is-mobile? "" "tooltip")
                 :data-placement (or tooltip-position "top")
                 :data-container "body"
                 :title "Ignore future activity unless mentioned"}])))
        (when external-bookmark
          (if remove-bookmark-link
            [:button.mlb-reset.more-menu-remove-bookmark-bt
              {:type "button"
               :ref "more-menu-remove-bookmark-bt"
               :class (when show-inbox? "has-next-bt")
               :on-click #(do
                            (reset! (::showing-menu s) false)
                            (when (fn? will-close)
                              (will-close))
                            (activity-actions/remove-bookmark entity-data remove-bookmark-link))
               :data-toggle (if is-mobile? "" "tooltip")
               :data-placement (or tooltip-position "top")
               :data-container "body"
               :title "Remove bookmark"}
              remove-bookmark-title]
            (when add-bookmark-link
              [:div.more-menu-add-bookmark-bt-container
                [:button.mlb-reset.more-menu-add-bookmark-bt
                  {:type "button"
                   :ref "more-menu-add-bookmark-bt"
                   :class (when show-inbox? "has-next-bt")
                   :on-click #(do
                                (reset! (::showing-menu s) false)
                                (when (fn? will-close)
                                  (will-close))
                                (activity-actions/add-bookmark entity-data add-bookmark-link))
                   :data-toggle (if is-mobile? "" "tooltip")
                   :data-placement (or tooltip-position "top")
                   :data-container "body"
                   :title "Bookmark"}]])))
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
