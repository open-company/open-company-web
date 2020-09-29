(ns oc.web.components.ui.more-menu
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.shared.useragent :as ua]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.ui :as uu]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.activity-move :refer (activity-move)]))

(defn- currently-shown? [s]
  (or @(::move-activity s)
      @(::showing-menu s)
      (-> s :rum/args first :force-show-menu)))

(defn- show-menu
  [s will-open & [force]]
  (when (or force
            (not (currently-shown? s)))
    (utils/remove-tooltips)
    (when (fn? will-open)
      (will-open))
    (reset! (::move-activity s) false)
    (reset! (::showing-menu s) true)))

(defn- hide-menu
  [s will-close & [force]]
  (when (or force
            (currently-shown? s))
    (utils/remove-tooltips)
    (when (fn? will-close)
      (will-close))
    (reset! (::move-activity s) false)
    (reset! (::showing-menu s) false)))

(defn- toggle-menu
  [s will-open will-close]
  (if (currently-shown? s)
    (hide-menu s will-close true)
    (show-menu s will-open true)))

;; Delete handling

(defn delete-clicked [e current-org-slug current-board-slug current-contributions-id activity-data]
  (let [alert-data {:action "delete-entry"
                    :title "Delete this post?"
                    :message "This action cannot be undone."
                    :link-button-style :green
                    :link-button-title "No, keep it"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes, delete it"
                    :solid-button-cb #(let [board-url (cond (= (keyword current-board-slug) :following)
                                                            (oc-urls/following current-org-slug)
                                                            (= (keyword current-board-slug) :replies)
                                                            (oc-urls/replies current-org-slug)
                                                            (= (keyword current-board-slug) :all-posts)
                                                            (oc-urls/all-posts current-org-slug)
                                                            (= (keyword current-board-slug) :inbox)
                                                            (oc-urls/inbox current-org-slug)
                                                            (= (keyword current-board-slug) :unfollowing)
                                                            (oc-urls/unfollowing current-org-slug)
                                                            (seq current-contributions-id)
                                                            (oc-urls/contributions current-org-slug current-contributions-id)
                                                            :else
                                                            (oc-urls/board current-org-slug current-board-slug))]
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
                       (drv/drv :route)
                       (rum/local false ::showing-menu)
                       (rum/local false ::move-activity)
                       (rum/local false ::can-unmount)
                       (rum/local false ::last-force-show-menu)
                       (ui-mixins/on-click-out "more-menu" (fn [s e]
                        (hide-menu s (-> s :rum/args first :will-close))))
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
             can-comment-share? comment-share-cb can-react? react-cb can-reply? react-disabled?
             reply-cb external-bookmark remove-bookmark-title
             show-inbox? force-show-menu capture-clicks external-follow mobile-tray-menu
             mark-unread-cb current-user-data]}]
  (let [{current-org-slug :org
         current-board-slug :board
         current-contributions-id :contributions
         current-activity-id :activity}          (drv/react s :route)
        delete-link (utils/link-for (:links entity-data) "delete")
        edit-link (utils/link-for (:links entity-data) "partial-update")
        share-link (utils/link-for (:links entity-data) "share")
        inbox-unread-link (utils/link-for (:links entity-data) "unread")
        is-mobile? (responsive/is-tablet-or-mobile?)
        add-bookmark-link (utils/link-for (:links entity-data) "bookmark" "POST")
        remove-bookmark-link (when (:bookmarked-at entity-data)
                               (utils/link-for (:links entity-data) "bookmark" "DELETE"))
        should-show-more-bt (or (and show-edit?
                                     edit-link)
                                (and show-delete?
                                     delete-link)
                                can-comment-share?
                                can-react?
                                can-reply?
                                (and (not external-share)
                                     share-link)
                                (and inbox-unread-link
                                     show-unread))
        follow-link (utils/link-for (:links entity-data) "follow")
        unfollow-link (utils/link-for (:links entity-data) "unfollow")
        showing-menu? (currently-shown? s)
        can-move-item? (and show-move?
                            edit-link
                            (> (count editable-boards) 1))]
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
         :class (utils/class-set {:menu-expanded showing-menu?
                                  :has-more-menu-bt should-show-more-bt
                                  :mobile-tray-menu mobile-tray-menu
                                  :android-browser (and ua/android?
                                                        (not ua/mobile-app?))
                                  :ios-browser (and ua/ios?
                                                    (not ua/mobile-app?))})
         :on-click (when mobile-tray-menu
                     #(when (and showing-menu?
                                 @(::can-unmount s))
                        (.stopPropagation %)
                        (hide-menu s will-close)))}
        (when should-show-more-bt
          [:button.mlb-reset.more-menu-bt
            {:type "button"
             :ref "more-menu-bt"
             :on-click #(toggle-menu s will-open will-close)
             :class (when showing-menu? "active")
             :data-toggle (if is-mobile? "" "tooltip")
             :data-placement (or tooltip-position "top")
             :data-container "body"
             :data-delay "{\"show\":\"100\", \"hide\":\"0\"}"
             :title "More"}])
        (cond
          @(::move-activity s)
          (activity-move {:activity-data entity-data
                          :current-user-data current-user-data
                          :dismiss-cb #(reset! (::move-activity s) false)})
          (or @(::showing-menu s) force-show-menu)
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
                              (hide-menu s will-close)
                              (if (fn? edit-cb)
                                (edit-cb entity-data)
                                (do
                                  (when current-activity-id
                                    (nav-actions/dismiss-post-modal %))
                                  (activity-actions/activity-edit entity-data))))}
                "Edit"])
            (when (and delete-link
                       show-delete?)
              [:li.delete.bottom-rounded.bottom-margin
                {:on-click #(do
                              (hide-menu s will-close)
                              (if (fn? delete-cb)
                                (delete-cb entity-data)
                                (delete-clicked % current-org-slug current-board-slug current-contributions-id entity-data)))}
                "Delete"])
            (when can-move-item?
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
                              (hide-menu s will-close)
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
                              (hide-menu s will-close)
                              (activity-actions/mark-unread entity-data)
                              (when (fn? mark-unread-cb)
                                (mark-unread-cb)))}
                "Mark as unread"])
            (when (or is-mobile?
                      (not external-follow))
              (if follow-link
                [:li.follow
                  {:class (when-not (or is-mobile? (not external-bookmark)) "bottom-rounded bottom-margin")
                   :on-click #(do
                                (hide-menu s will-close)
                                (activity-actions/entry-follow (:uuid entity-data)))}
                  "Follow"]
                (when unfollow-link
                  [:li.unfollow
                    {:class (when-not (or is-mobile? (not external-bookmark)) "bottom-rounded bottom-margin")
                     :on-click #(do
                                  (hide-menu s will-close)
                                  (activity-actions/entry-unfollow (:uuid entity-data)))}
                    "Mute"])))
            (when (or is-mobile?
                      (not external-bookmark))
              (if remove-bookmark-link
                [:li.remove-bookmark.bottom-rounded.bottom-margin
                  {:ref "more-menu-remove-bookmark-bt"
                   :on-click #(do
                                (hide-menu s will-close)
                                (activity-actions/remove-bookmark entity-data remove-bookmark-link))}
                  "Remove bookmark"]
                (when add-bookmark-link
                  [:li.add-bookmark.bottom-rounded.bottom-margin
                    {:ref "more-menu-add-bookmark-bt"
                     :data-container "body"
                     :on-click #(do
                                  (hide-menu s will-close)
                                  (activity-actions/add-bookmark entity-data add-bookmark-link))}
                    "Bookmark"])))
            (when can-react?
              [:li.react.top-rounded
                {:on-click #(do
                              (hide-menu s will-close)
                              (when (fn? react-cb)
                                (react-cb)))
                 :disabled react-disabled?}
                "React"])
            (when can-reply?
              [:li.reply
                {:on-click #(do
                              (hide-menu s will-close)
                              (when (fn? reply-cb)
                                (reply-cb)))}
                "Reply"])
            (when can-comment-share?
              [:li.comment-share.bottom-rounded.bottom-margin
                {:on-click #(do
                              (hide-menu s will-close)
                              (when (fn? comment-share-cb)
                                (comment-share-cb)))}
                "Copy link"])
            (when show-inbox?
              [:li.dismiss.top-rounded.bottom-rounded
                {:on-click #(do
                              (hide-menu s will-close)
                              (activity-actions/inbox-dismiss (:uuid entity-data))
                              (when (seq current-activity-id)
                                (nav-actions/dismiss-post-modal %)))}
                "Dismiss"])])
        (when (and external-share
                   share-link)
          [:button.mlb-reset.more-menu-share-bt
            {:type "button"
             :ref "tile-menu-share-bt"
             :class (when (or follow-link
                              unfollow-link
                              (and external-bookmark
                                   (or add-bookmark-link
                                       remove-bookmark-link))) "has-next-bt")
             :on-click #(do
                          (hide-menu s will-close)
                          (activity-actions/activity-share-show entity-data share-container-id))
             :data-toggle (if is-mobile? "" "tooltip")
             :data-container "body"
             :data-placement (or tooltip-position "top")
             :data-delay "{\"show\":\"100\", \"hide\":\"0\"}"
             :title "Share"}])
        (when external-follow
          (if follow-link
            [:button.mlb-reset.more-menu-entry-follow-bt
            {:type "button"
             :ref "more-menu-entry-follow-bt"
             :key "more-menu-entry-follow-bt"
             :class (when (and external-bookmark
                               (or add-bookmark-link
                                   remove-bookmark-link)) "has-next-bt")
             :on-click #(do
                          (hide-menu s will-close)
                          (activity-actions/entry-follow (:uuid entity-data)))
             :data-toggle (if is-mobile? "" "tooltip")
             :data-placement (or tooltip-position "top")
             :data-container "body"
             :title uu/watch-activity-copy
             }]
            (when unfollow-link
              [:button.mlb-reset.more-menu-entry-unfollow-bt
                {:type "button"
                 :ref "more-menu-entry-unfollow-bt"
                 :key "more-menu-entry-unfollow-bt"
                 :class (when (and external-bookmark
                                   (or add-bookmark-link
                                       remove-bookmark-link)) "has-next-bt")
                 :on-click #(do
                              (hide-menu s will-close)
                              (activity-actions/entry-unfollow (:uuid entity-data)))
                 :data-toggle (if is-mobile? "" "tooltip")
                 :data-placement (or tooltip-position "top")
                 :data-container "body"
                 :title uu/unwatch-activity-copy
                 }])))
        (when external-bookmark
          (if remove-bookmark-link
            [:button.mlb-reset.more-menu-remove-bookmark-bt
              {:type "button"
               :ref "more-menu-remove-bookmark-bt"
               :class (when show-inbox? "has-next-bt")
               :on-click #(do
                            (hide-menu s will-close)
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
                                (hide-menu s will-close)
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
                          (hide-menu s will-close)
                          (activity-actions/inbox-dismiss (:uuid entity-data))
                          (when (seq current-activity-id)
                            (nav-actions/dismiss-post-modal %)))
             :data-toggle (if is-mobile? "" "tooltip")
             :data-placement (or tooltip-position "top")
             :data-container "body"
             :title "Dismiss"}])])))
