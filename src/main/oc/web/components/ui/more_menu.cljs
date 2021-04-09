(ns oc.web.components.ui.more-menu
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.local-settings :as ls]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.ui :as uu]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.pin :as pin-actions]
            [oc.web.actions.label :as label-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.activity-share-email :refer (activity-share-email)]
            [oc.web.components.ui.foc-labels-picker :refer (foc-labels-picker)]
            [oc.web.components.ui.activity-move :refer (activity-move)]))

(defn- menu-visible? [s]
  (or @(::move-activity s)
      @(::showing-menu s)
      (-> s :rum/args first :foc-labels-picker)
      (-> s :rum/args first :force-show-menu)))

(defn- show-menu
  [s will-open & [force]]
  (when (or force
            (not (menu-visible? s)))
    (label-actions/toggle-foc-labels-picker)
    (utils/remove-tooltips)
    (when (fn? will-open)
      (will-open))
    (reset! (::move-activity s) false)
    (reset! (::showing-menu s) true)))

(defn- hide-menu
  [s will-close & [force]]
  (when (or force
            (menu-visible? s))
    (label-actions/hide-foc-labels-picker)
    (utils/remove-tooltips)
    (when (fn? will-close)
      (will-close))
    (reset! (::move-activity s) false)
    (reset! (::showing-menu s) false)))

(defn- toggle-menu [s will-open will-close]
  (if (menu-visible? s)
    (hide-menu s will-close true)
    (show-menu s will-open true)))

;; Delete handling

(defn delete-clicked [current-org-slug current-board-slug current-contributions-id current-label-slug activity-data]
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
                                                            (seq current-label-slug)
                                                            (oc-urls/label current-org-slug current-label-slug)
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
                       (ui-mixins/on-click-out "more-menu" (fn [s _]
                                                             (hide-menu s (-> s :rum/args first :will-close))))
                       ui-mixins/strict-refresh-tooltips-mixin
                       {:will-update (fn [s]
                                       (let [next-force-show-menu (-> s :rum/args first :force-show-menu)]
                                         (when (not= @(::last-force-show-menu s) next-force-show-menu)
                                           (reset! (::last-force-show-menu s) next-force-show-menu)
                                           (when next-force-show-menu
                                             ;; avoid automatic dismiss of the menu on iOS
                                             (reset! (::can-unmount s) false)
                                             (utils/after 1000 #(reset! (::can-unmount s) true)))))
                                       s)}
  [s {:keys [entity-data current-user-data
             hide-share? external-share showing-share share-prefix
             editable-boards
             will-open will-close tooltip-position force-show-menu mobile-tray-menu custom-class
             show-edit? edit-cb
             show-delete? delete-cb
             show-move?
             can-comment-share? comment-share-cb
             can-react? react-cb react-disabled?
             can-reply? reply-cb
             external-bookmark hide-bookmark?
             external-follow
             show-home-pin show-board-pin
             external-labels hide-labels? show-labels-picker foc-labels-picker-prefix]}]
  (let [is-mobile? (responsive/is-tablet-or-mobile?)
        {current-org-slug :org
         current-board-slug :board
         current-contributions-id :contributions
         current-label-slug :label
         current-activity-id :activity}          (drv/react s :route)
        delete-link (utils/link-for (:links entity-data) "delete")
        edit-link (utils/link-for (:links entity-data) "partial-update")
        toggle-labels-link (or (utils/link-for (:links entity-data) "partial-add-labels")
                               (utils/link-for (:links entity-data) "partial-remove-labels"))
        share-link (when-not hide-share? (utils/link-for (:links entity-data) "share"))
        show-external-share? (and (not is-mobile?)
                                  external-share
                                  share-link)
        add-bookmark-link (when-not hide-bookmark? (utils/link-for (:links entity-data) "bookmark" "POST"))
        remove-bookmark-link (when (and (not hide-bookmark?) (:bookmarked-at entity-data))
                               (utils/link-for (:links entity-data) "bookmark" "DELETE"))
        show-external-bookmarks? (and external-bookmark
                                         (or add-bookmark-link
                                             remove-bookmark-link))
        follow-link (utils/link-for (:links entity-data) "follow")
        unfollow-link (utils/link-for (:links entity-data) "unfollow")
        show-external-follow? (and external-follow
                                   (or follow-link
                                       unfollow-link))
        showing-menu? (menu-visible? s)
        can-move-item? (and show-move?
                            edit-link
                            (> (count editable-boards) 1))
        private-board? (= (:board-access entity-data) "private")
        ;; Pins
        home-pin-link (utils/link-for (:links entity-data) "home-pin")
        can-home-pin? (and home-pin-link
                           (:can-home-pin? entity-data)
                           show-home-pin)
        home-pinned? (get-in entity-data [:pins (keyword ls/seen-home-container-id)])
        board-pin-link (utils/link-for (:links entity-data) "board-pin")
        can-board-pin? (and board-pin-link
                            (:can-board-pin? entity-data)
                            show-board-pin)
        board-pinned? (get-in entity-data [:pins (keyword (:board-uuid entity-data))])
        show-both-pins (and show-home-pin show-board-pin)
        home-pin-title (if home-pinned?
                         (if show-both-pins
                           "Unpin from Home"
                           "Unpin")
                         (if show-both-pins
                           "Pin to Home"
                           "Pin"))
        board-pin-title (if board-pinned?
                          (if show-both-pins
                            (str "Unpin from #" (:board-name entity-data))
                            "Unpin")
                          (str "Pin to #" (:board-name entity-data)))
        pins? (or can-home-pin? can-board-pin?)
        can-edit-labels? (and ls/labels-enabled?
                              (not hide-labels?)
                              toggle-labels-link)
        show-external-labels? (and external-labels
                                   can-edit-labels?)
        show-internal-labels? (and (or (not external-labels)
                                       is-mobile?)
                                   can-edit-labels?)
        should-show-more-bt (or (and show-edit?
                                     edit-link)
                                (and show-delete?
                                     delete-link)
                                can-comment-share?
                                can-react?
                                can-reply?
                                (and (not external-share)
                                     share-link)
                                (and (not external-bookmark)
                                     (or add-bookmark-link
                                         remove-bookmark-link))
                                show-internal-labels?
                                pins?)]
    (when (or edit-link
              share-link
              delete-link
              can-comment-share?
              can-react?
              can-reply?
              add-bookmark-link
              remove-bookmark-link
              follow-link
              unfollow-link
              pins?
              can-edit-labels?
              show-labels-picker)
      [:div.more-menu
        {:class (utils/class-set {:menu-expanded showing-menu?
                                  :has-more-menu-bt should-show-more-bt
                                  :mobile-tray-menu mobile-tray-menu
                                  :android-browser (and ua/android?
                                                        (not ua/mobile-app?))
                                  :ios-browser (and ua/ios?
                                                    (not ua/mobile-app?))
                                  :showing-share showing-share
                                  :move-activity @(::move-activity s)
                                  :foc-labels-picker show-labels-picker
                                  custom-class (seq custom-class)})
         :ref "more-menu"
         :on-click (when mobile-tray-menu
                     #(when (and showing-menu?
                                 @(::can-unmount s)
                                 (not (dom-utils/event-container-matches % "ul.more-menu-list")))
                        (.stopPropagation %)
                        (hide-menu s will-close)))}
        (when show-labels-picker
          [:div.foc-labels-picker-wrapper
           (foc-labels-picker entity-data)])
        (cond
          showing-share
          (activity-share-email {:activity-data entity-data})
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
                                      :has-pins pins?})}
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
                                (delete-clicked current-org-slug current-board-slug current-contributions-id current-label-slug entity-data)))}
                "Delete"])
            (when can-move-item?
              [:li.move.top-rounded
               {:class (when (and (not (and (not external-share)
                                            share-link))
                                  (not (or is-mobile?
                                            (not external-follow)))
                                  (not (or is-mobile?
                                          (not external-bookmark)))
                                  (not (or is-mobile?
                                           (not external-labels)))
                                  (not pins?))
                          "bottom-rounded bottom-margin")
                :on-click #(do
                             (reset! (::showing-menu s) false)
                             (reset! (::move-activity s) true))}
               "Move"])
            (when (and (not external-share)
                       share-link)
              [:li.share
                {:class (when (and (not (or is-mobile?
                                            (not external-follow)))
                                   (not (or is-mobile?
                                            (not external-bookmark)))
                                   (not (or is-mobile?
                                            (not external-labels)))
                                   (not pins?))
                          "bottom-rounded bottom-margin")
                 :on-click #(do
                              (hide-menu s will-close)
                              (activity-actions/activity-share-show entity-data share-prefix))}
                "Share"])
            (when (or is-mobile?
                      (not external-follow))
              (if follow-link
                [:li.follow
                  {:class (when (and (or is-mobile?
                                         (not external-bookmark))
                                     (or is-mobile?
                                         (not external-labels))
                                     pins?)
                            "bottom-rounded bottom-margin")
                   :on-click #(do
                                (hide-menu s will-close)
                                (activity-actions/entry-follow (:uuid entity-data)))}
                  "Follow"]
                (when unfollow-link
                  [:li.unfollow
                    {:class (when (and (or is-mobile?
                                           (not external-bookmark))
                                       (or is-mobile?
                                           (not external-labels))
                                       pins?)
                              "bottom-rounded bottom-margin")
                     :on-click #(do
                                  (hide-menu s will-close)
                                  (activity-actions/entry-unfollow (:uuid entity-data)))}
                    "Mute"])))
            (when (or is-mobile?
                      (not external-bookmark))
              (if remove-bookmark-link
                [:li.remove-bookmark
                  {:class (when (and (or is-mobile?
                                         (not external-labels))
                                     pins?)
                            "bottom-rounded bottom-margin")
                   :on-click #(do
                                (hide-menu s will-close)
                                (activity-actions/remove-bookmark entity-data remove-bookmark-link))}
                  "Remove bookmark"]
                (when add-bookmark-link
                  [:li.add-bookmark
                    {:class (when (and (or is-mobile?
                                           (not external-labels))
                                       pins?)
                              "bottom-rounded bottom-margin")
                     :data-container "body"
                     :on-click #(do
                                  (hide-menu s will-close)
                                  (activity-actions/add-bookmark entity-data add-bookmark-link))}
                    "Bookmark"])))
            (when show-internal-labels?
              [:li.edit-labels.bottom-rounded.bottom-margin.top-rounded.top-margin
               {:on-click #(do
                             (hide-menu s will-close)
                             (label-actions/toggle-foc-labels-picker (str foc-labels-picker-prefix (:uuid entity-data))))}
               "Edit labels"])
            (when can-home-pin?
              [:li.toggle-pin.home-pin
               {:class (utils/class-set {:bottom-rounded (not can-board-pin?)
                                         :bottom-margin (not can-board-pin?)
                                         :pinned home-pinned?
                                         :disabled private-board?})
                :on-click #(do
                             (hide-menu s will-close)
                             (when-not private-board?
                               (pin-actions/toggle-home-pin! entity-data home-pin-link)))
                :title (when private-board?
                         "Private posts can’t be pinned to the Home feed")
                :data-toggle (when-not is-mobile? "tooltip")
                :data-placement "top"
                :data-container "body"}
               home-pin-title])
            (when can-board-pin?
              [:li.toggle-pin.board-pin
               {:class (utils/class-set {:bottom-rounded true
                                         :bottom-margin true
                                         :pinned board-pinned?
                                         :disabled (not can-board-pin?)})
                :on-click #(do
                             (hide-menu s will-close)
                             (when can-board-pin?
                               (pin-actions/toggle-board-pin! entity-data board-pin-link)))}
               board-pin-title])
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
                "Copy link"])])
        (when should-show-more-bt
          [:button.mlb-reset.menu-item-bt.more-menu-bt
           {:type "button"
            :on-click #(toggle-menu s will-open will-close)
            :class (when showing-menu? "active")
            :data-toggle (if is-mobile? "" "tooltip")
            :data-placement (or tooltip-position "top")
            :data-container "body"
            :data-delay "{\"show\":\"100\", \"hide\":\"0\"}"
            :title "More"}
           [:span.menu-item-icon]])
        (when show-external-labels?
          [:button.mlb-reset.menu-item-bt.more-menu-edit-labels-bt
           {:type "button"
            :class (utils/class-set {:has-next-bt (or show-external-share?
                                                      show-external-follow?
                                                      show-external-bookmarks?)
                                     :active show-labels-picker})
            :on-click #(do
                         (hide-menu s will-close)
                         (label-actions/toggle-foc-labels-picker (str foc-labels-picker-prefix (:uuid entity-data))))
            :data-toggle (if is-mobile? "" "tooltip")
            :data-placement (or tooltip-position "top")
            :data-container "body"
            :title "Edit labels"}
           [:span.menu-item-icon]])
        (when show-external-share?
          [:button.mlb-reset.menu-item-bt.more-menu-share-bt
            {:type "button"
             :class (dom-utils/class-set {:has-next-bt (or show-external-follow?
                                                           show-external-bookmarks?)
                                          :active showing-share})
             :on-click #(do
                          (hide-menu s will-close)
                          (activity-actions/activity-share-show entity-data share-prefix))
             :data-toggle (if is-mobile? "" "tooltip")
             :data-container "body"
             :data-placement (or tooltip-position "top")
             :data-delay "{\"show\":\"100\", \"hide\":\"0\"}"
             :title "Share"}
            [:span.menu-item-icon]])
        (when show-external-follow?
          (if follow-link
            [:button.mlb-reset.menu-item-bt.more-menu-entry-follow-bt
             {:type "button"
              :key "more-menu-entry-follow-bt"
              :class (when (and show-external-bookmarks?
                                (or remove-bookmark-link
                                    add-bookmark-link))
                       "has-next-bt")
              :on-click #(do
                           (hide-menu s will-close)
                           (activity-actions/entry-follow (:uuid entity-data)))
              :data-toggle (if is-mobile? "" "tooltip")
              :data-placement (or tooltip-position "top")
              :data-container "body"
              :title uu/watch-activity-copy}
              [:span.menu-item-icon]]
            (when unfollow-link
              [:button.mlb-reset.menu-item-bt.more-menu-entry-unfollow-bt
                {:type "button"
                 :key "more-menu-entry-unfollow-bt"
                 :class (when (and show-external-bookmarks?
                                   (or remove-bookmark-link
                                       add-bookmark-link))
                          "has-next-bt")
                 :on-click #(do
                              (hide-menu s will-close)
                              (activity-actions/entry-unfollow (:uuid entity-data)))
                 :data-toggle (if is-mobile? "" "tooltip")
                 :data-placement (or tooltip-position "top")
                 :data-container "body"
                 :title uu/unwatch-activity-copy}
               [:span.menu-item-icon]])))
        (when show-external-bookmarks?
          (if remove-bookmark-link
            [:button.mlb-reset.menu-item-bt.more-menu-remove-bookmark-bt
              {:type "button"
               :on-click #(do
                            (hide-menu s will-close)
                            (activity-actions/remove-bookmark entity-data remove-bookmark-link))
               :data-toggle (if is-mobile? "" "tooltip")
               :data-placement (or tooltip-position "top")
               :data-container "body"
               :title "Remove bookmark"}
             [:span.menu-item-icon]]
            (when add-bookmark-link
              [:div.more-menu-add-bookmark-bt-container
                [:button.mlb-reset.menu-item-bt.more-menu-add-bookmark-bt
                  {:type "button"
                   :on-click #(do
                                (hide-menu s will-close)
                                (activity-actions/add-bookmark entity-data add-bookmark-link))
                   :data-toggle (if is-mobile? "" "tooltip")
                   :data-placement (or tooltip-position "top")
                   :data-container "body"
                   :title "Bookmark"}
                 [:span.menu-item-icon]]])))])))