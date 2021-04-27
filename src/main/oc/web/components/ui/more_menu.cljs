(ns oc.web.components.ui.more-menu
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.local-settings :as ls]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.ui :as uu]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.pin :as pin-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.foc-menu :as foc-menu-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.activity-share-email :refer (activity-share-email)]
            [oc.web.components.ui.foc-labels-picker :refer (foc-labels-picker) :rename {foc-labels-picker labels-picker}]
            [oc.web.components.ui.activity-move :refer (activity-move)]))

(defonce click-out-listener (atom nil))
(defonce esc-press-listener (atom nil))

(def menu-keys [:foc-show-menu :foc-menu-open :foc-activity-move :foc-labels-picker :foc-share-entry])

(defn- menu-visible? [s]
  (some second (-> s :rum/args first (select-keys menu-keys))))

(defn- show-menu
  [s]
  (utils/remove-tooltips)
  (foc-menu-actions/toggle-foc-menu-open (-> s :rum/args first :entity-data :uuid)))

(defn- hide-menu
  [_s]
  (utils/remove-tooltips)
  (foc-menu-actions/toggle-foc-menu-open))

(defn- toggle-menu [s]
  (if (-> s :rum/args first :foc-menu-open)
    (hide-menu s)
    (show-menu s)))

;; Delete handling

(defn delete-clicked [current-activity-id activity-data]
  (let [alert-data {:action "delete-entry"
                    :title "Delete this post?"
                    :message "This action cannot be undone."
                    :link-button-style :green
                    :link-button-title "No, keep it"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes, delete it"
                    :solid-button-cb (fn [e]
                                       (when (= current-activity-id (:uuid activity-data))
                                         (nav-actions/dismiss-post-modal e))
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

(defn- maybe-add-listeners [s]
  (when-not @click-out-listener
    (reset! click-out-listener (events/listen js/window EventType/CLICK
                                              (fn [e]
                                                (when-not (dom-utils/event-container-matches e ".foc-menu-event-stop")
                                                  (foc-menu-actions/hide-foc-menu-if-needed))))))
  (when-not @esc-press-listener
    (reset! esc-press-listener (events/listen js/window EventType/KEYUP
                                              (fn [e]
                                                (when (= (.-key e) "Escape")
                                                  (foc-menu-actions/hide-foc-menu-if-needed)))))))

(defn- pp [s]
  (-> s :rum/args first (select-keys menu-keys)))

(rum/defcs more-menu < rum/reactive
                       rum/static
                       (drv/drv :ui-tooltip)
                       (drv/drv :activity-uuid)
                       (rum/local nil ::click-out-listener)
                       ui-mixins/strict-refresh-tooltips-mixin
                       {:will-mount (fn [s]
                                     (maybe-add-listeners s)
                                     s)}
  [s {:keys [entity-data current-user-data entity-type
             hide-share? external-share
             editable-boards
             tooltip-position mobile-tray-menu custom-classes
             show-edit? edit-cb
             show-delete? delete-cb
             show-move?
             can-comment-share? comment-share-cb
             can-react? react-cb react-disabled?
             can-reply? reply-cb
             external-bookmark hide-bookmark?
             external-follow
             show-home-pin show-board-pin
             external-labels hide-labels? 
             foc-show-menu  foc-menu-open foc-labels-picker foc-activity-move foc-share-entry] :as props}]
  (let [is-mobile? (responsive/is-tablet-or-mobile?)
        _ (drv/react s :ui-tooltip)
        current-activity-id (drv/react s :activity-uuid)
        delete-link (utils/link-for (:links entity-data) "delete")
        edit-link (utils/link-for (:links entity-data) "partial-update")
        toggle-labels-link (or (utils/link-for (:links entity-data) "partial-add-label")
                               (utils/link-for (:links entity-data) "partial-remove-label"))
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
                                pins?)
        main-classes* (utils/class-set {:menu-expanded showing-menu?
                                        :has-more-menu-bt should-show-more-bt
                                        :mobile-tray-menu mobile-tray-menu
                                        :android-browser (and ua/android?
                                                              (not ua/mobile-app?))
                                        :ios-browser (and ua/ios?
                                                          (not ua/mobile-app?))
                                        :showing-share foc-share-entry
                                        :move-activity foc-activity-move
                                        :foc-labels-picker foc-labels-picker})
        main-classes (str main-classes* " " custom-classes)]
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
              foc-labels-picker)
      [:div.more-menu.foc-menu-event-stop
        {:ref "more-menu"
         :class main-classes
         :on-click (when mobile-tray-menu
                     #(when (and foc-menu-open
                                 (not (dom-utils/event-container-matches % "ul.more-menu-list li")))
                        (dom-utils/stop-propagation! %)
                        (hide-menu s)))}
        (when foc-labels-picker
          [:div.foc-labels-picker-wrapper
           (labels-picker entity-data)])
        (cond
          foc-share-entry
          (activity-share-email {:activity-data entity-data})
          foc-activity-move
          (activity-move {:activity-data entity-data
                          :current-user-data current-user-data
                          :dismiss-cb #(reset! (::move-activity s) false)})
          foc-menu-open
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
                              (foc-menu-actions/toggle-foc-menu-open)
                              (if (fn? delete-cb)
                                (delete-cb entity-data)
                                (delete-clicked current-activity-id entity-data)))}
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
                :on-click #(foc-menu-actions/toggle-foc-activity-move (:uuid entity-data))}
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
                 :on-click #(foc-menu-actions/toggle-foc-share-entry (:uuid entity-data))}
                "Share"])
            (when (and (or follow-link
                           unfollow-link)
                       (or is-mobile?
                           (not external-follow)))
              (if follow-link
                [:li.follow
                  {:class (when (and (or is-mobile?
                                         (not external-bookmark))
                                     (or is-mobile?
                                         (not external-labels))
                                     pins?)
                            "bottom-rounded bottom-margin")
                   :on-click #(do
                                (hide-menu s)
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
                                  (hide-menu s)
                                  (activity-actions/entry-unfollow (:uuid entity-data)))}
                    "Mute"])))
            (when (and (or add-bookmark-link
                           remove-bookmark-link)
                       (or is-mobile?
                           (not external-bookmark)))
              (if remove-bookmark-link
                [:li.remove-bookmark
                  {:class (when (and (or is-mobile?
                                         (not external-labels))
                                     pins?)
                            "bottom-rounded bottom-margin")
                   :on-click #(do
                                (hide-menu s)
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
                                  (hide-menu s)
                                  (activity-actions/add-bookmark entity-data add-bookmark-link))}
                    "Bookmark"])))
            (when (and show-internal-labels?
                       toggle-labels-link)
              [:li.edit-labels.bottom-rounded.bottom-margin.top-rounded.top-margin
               {:on-click #(foc-menu-actions/toggle-foc-labels-picker (:uuid entity-data))}
               "Edit labels"])
            (when can-home-pin?
              [:li.toggle-pin.home-pin
               {:class (utils/class-set {:bottom-rounded (not can-board-pin?)
                                         :bottom-margin (not can-board-pin?)
                                         :pinned home-pinned?
                                         :disabled private-board?})
                :on-click #(do
                             (hide-menu s)
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
                             (hide-menu s)
                             (when can-board-pin?
                               (pin-actions/toggle-board-pin! entity-data board-pin-link)))}
               board-pin-title])
            (when can-react?
              [:li.react.top-rounded
                {:on-click #(do
                              (foc-menu-actions/toggle-foc-show-menu (:uuid entity-data))
                              (when (fn? react-cb)
                                (react-cb)))
                 :disabled react-disabled?}
                "React"])
            (when can-reply?
              [:li.reply
                {:on-click #(do
                              (hide-menu s)
                              (when (fn? reply-cb)
                                (reply-cb)))}
                "Reply"])
            (when can-comment-share?
              [:li.comment-share.bottom-rounded.bottom-margin
                {:on-click #(do
                              (hide-menu s)
                              (when (fn? comment-share-cb)
                                (comment-share-cb)))}
                "Copy link"])])
        (when should-show-more-bt
          [:button.mlb-reset.menu-item-bt.more-menu-bt
           {:type "button"
            :on-click #(toggle-menu s)
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
                                     :active foc-labels-picker})
            :on-click #(foc-menu-actions/toggle-foc-labels-picker (:uuid entity-data))
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
                                          :active foc-share-entry})
             :on-click #(foc-menu-actions/toggle-foc-share-entry (:uuid entity-data))
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
                           (hide-menu s)
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
                              (hide-menu s)
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
                            (hide-menu s)
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
                                (hide-menu s)
                                (activity-actions/add-bookmark entity-data add-bookmark-link))
                   :data-toggle (if is-mobile? "" "tooltip")
                   :data-placement (or tooltip-position "top")
                   :data-container "body"
                   :title "Bookmark"}
                 [:span.menu-item-icon]]])))])))