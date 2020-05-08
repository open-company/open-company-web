(ns oc.web.components.user-notifications
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.activity :as am]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.stream-item :refer (stream-item)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.post-authorship :refer (post-authorship)]))

(rum/defc user-notification-item < rum/static
  [{entry-uuid         :uuid
    board-slug         :board-slug
    board-name         :board-name
    publisher-board    :publisher-board
    reminder?          :reminder?
    reminder           :reminder
    mention?           :mention?
    mention            :mention
    notification-type  :notification-type
    interaction-id     :interaction-id
    created-at         :created-at
    activity-data      :activity-data
    stream-attribution :stream-attribution
    body               :body
    author             :author
    unread             :unread
    :as n}]
  (let [is-mobile? (responsive/is-mobile-size?)
        authorship-map {:publisher author
                        :board-slug board-slug
                        :board-name board-name}]
    [:div.user-notification.group
      {:class    (utils/class-set {:unread (:unread n)})
       :on-click (fn [e]
                   (this-as user-notification-el
                     (when (and (fn? (:click n))
                                (not (utils/button-clicked? e))
                                (not (utils/input-clicked? e))
                                (not (utils/anchor-clicked? e))
                                (not (utils/event-inside? e (.querySelector user-notification-el "div.add-comment-box-container"))))
                       ((:click n)))))}
      [:div.user-notification-title-container
        (when (:headline activity-data)
          [:div.user-notification-title
            (:headline activity-data)])
        [:span.time-since
          {:data-toggle (when-not is-mobile? "tooltip")
           :data-placement "top"
           :data-container "body"
           :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"}
          [:time
            {:date-time created-at}
            (utils/foc-date-time created-at)]]]
      [:div.user-notification-header
        (post-authorship {:activity-data authorship-map
                          :user-avatar? true
                          :user-hover? true
                          :hide-last-name? true
                          :activity-board? false
                          :current-user-id (jwt/user-id)})
        (when unread
          [:div.separator-dot])
        (when unread
          [:div.new-tag
            "NEW"])]
      [:div.user-notification-body-container
        {:class (utils/class-set {:comment (seq interaction-id)})}
        [:div.user-notification-body.oc-mentions.oc-mentions-hover
          {:dangerouslySetInnerHTML (utils/emojify body)}]]
      (when activity-data
        (rum/with-key (add-comment {:activity-data activity-data
                                    :parent-comment-uuid (when interaction-id interaction-id)
                                    :collapsed? true})
         (str "activity-add-comment-" (:created-at n))))]))

(defn has-new-content? [notifications-data]
  (some :unread notifications-data))

(defn- fix-notification [n]
  (if (and (not (map? (:activity-data n)))
           (not (:reminder? n)))
    (assoc n :activity-data (dis/activity-data (:uuid n)))
    n))

(rum/defcs user-notifications < rum/static
                                rum/reactive
                                (drv/drv :user-notifications)
                                (drv/drv :posts-data)
                                ui-mixins/refresh-tooltips-mixin
                                (am/truncate-element-mixin "div.user-notification-body" (* 18 3))
  [s {:keys [tray-open]}]
  (let [user-notifications-data (drv/react s :user-notifications)
        has-new-content (has-new-content? user-notifications-data)
        is-mobile? (responsive/is-mobile-size?)
        fix-activity? (some #(and (not (:activity-data %)) (not (:reminder? %))) user-notifications-data)
        fixed-user-notifications-data (if fix-activity?
                                        (map fix-notification user-notifications-data)
                                        user-notifications-data)]
    [:div.user-notifications-tray
      {:class (utils/class-set {:hidden-tray (not tray-open)})}
      [:div.user-notifications-tray-header.group
        [:div.title "Activity"]
        (when-not is-mobile?
          [:div.notification-settings-bt-container
            [:button.mlb-reset.notification-settings-bt
              {:on-click #(do
                            (nav-actions/show-user-settings :notifications))
               :data-toggle (when-not is-mobile? "tooltip")
               :data-placement "top"
               :data-container "body"
               :title "Notification settings"}]])
        (when has-new-content
          [:button.mlb-reset.all-read-bt
            {:on-click #(user-actions/read-notifications)
             :data-toggle (when-not is-mobile? "tooltip")
             :data-placement "top"
             :data-container "body"
             :title "Mark all as read"}])]
      [:div.user-notifications-tray-list
        (if (empty? fixed-user-notifications-data)
          [:div.user-notifications-tray-empty
            (all-caught-up)]
          (for [n fixed-user-notifications-data
                :let [entry-uuid (:uuid n)
                      board-slug (:board-slug n)
                      reminder? (:reminder? n)
                      reminder (when reminder?
                                 (:reminder n))
                      notification-type (when reminder?
                                          (:notification-type reminder))
                      ;; Base string for the key of the React child
                      children-key-base (str "user-notification-" (:created-at n) "-")
                      ;; add a unique part to the key to make sure the children are rendered
                      children-key (str children-key-base
                                    (if (seq entry-uuid)
                                      entry-uuid
                                      (if (and reminder?
                                               (seq (:uuid reminder)))
                                        (:uuid reminder)
                                        (rand 1000))))]]
            (rum/with-key (user-notification-item n) (str "user-notification-" (:created-at n)))))]]))

(defn- close-tray [s]
  (reset! (::tray-open s) false)
  (user-actions/read-notifications))

(rum/defcs user-notifications-button < rum/static
                                       rum/reactive
                                       (drv/drv :unread-notifications-count)
                                       (rum/local false ::tray-open)
                                       ; (rum/local (rum/create-ref) ::list-ref)
                                       ui-mixins/refresh-tooltips-mixin
                                       (ui-mixins/on-window-click-mixin (fn [s e]
                                        (when-let [user-notifications-node (rum/ref-node s "user-notifications-list")]
                                          (when (and @(::tray-open s)
                                                     (.-parentElement user-notifications-node)
                                                     (not (utils/event-inside? e user-notifications-node)))

                                            (close-tray s)))))
                                       {:will-mount (fn [s]
                                         (when (responsive/is-mobile-size?)
                                           (dom-utils/lock-page-scroll))
                                        s)
                                        :will-unmount (fn [s]
                                         (when (responsive/is-mobile-size?)
                                           (dom-utils/unlock-page-scroll))
                                        s)}
  [s]
  (let [unread-notifications-count (drv/react s :unread-notifications-count)
        is-mobile? (responsive/is-mobile-size?)]
    [:div.user-notifications
      [:button.mlb-reset.notification-bell-bt
        {:class (utils/class-set {:new (pos? unread-notifications-count)
                                  :active @(::tray-open s)})
         :data-toggle (when-not is-mobile? "tooltip")
         :data-placement "bottom"
         :title "Notifications"
         :on-click #(if @(::tray-open s)
                      (close-tray s)
                      (reset! (::tray-open s) true))}
        [:span.bell-icon]]
      (rum/with-ref
       (user-notifications {:tray-open @(::tray-open s)})
       "user-notifications-list")]))
